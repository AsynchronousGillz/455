// File name NodeClient.java

package cs455.overlay.node;

import java.io.*;
import java.net.*;

/**
 * 
 * @author G van Andel
 *
 * @see MessagingNode.NodeMain
 */

public  class NodeClient implements Runnable {

	// INSTANCE VARIABLES ***********************************************

	/**
	 * Sockets are used in the operating system as channels of communication
	 * between two processes.
	 * 
	 * @see java.net.Socket
	 */
	private Socket nodeSocket;

	/**
	 * The stream to handle data going to the server.
	 */
	private ObjectOutputStream output;

	/**
	 * The stream to handle data from the server.
	 */
	private ObjectInputStream input;

	/**
	 * The thread created to read data from the server.
	 */
	private Thread nodeReader;

	/**
	 * Indicates if the thread is ready to stop. Needed so that the loop in the
	 * run method knows when to stop waiting for incoming messages.
	 */
	private boolean stopping	= false;

	/**
	 * The server's host name.
	 */
	private String host;

	/**
	 * The port number.
	 */
	private int	port;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the node.
	 * 
	 * @param host
	 *            the server's host name.
	 * @param port
	 *            the port number.
	 */
	public NodeClient(String host, int port) {
		// Initialize variables
		this.host = host;
		this.port = port;
	}
     
	// INSTANCE METHODS *************************************************

	/**
	 * Opens the connection with the server. If the connection is already
	 * opened, this call has no effect.
	 */
	final public void openConnection() {
		// Do not do anything if the connection is already open
		if (isConnected() == true)
			return;

		// Create the sockets and the data streams
		try {
			nodeSocket = new Socket(host, port);
			output = new ObjectOutputStream(nodeSocket.getOutputStream());
			input = new ObjectInputStream(nodeSocket.getInputStream());
		} catch (IOException ex) {
			try {
				closeAll();
			} catch (Exception exc) {
				System.err.println(exc.toString());
			}
		}

		nodeReader = new Thread(this); // Create the data reader thread
		stopping = false;
		nodeReader.start(); // Start the thread
	}

	/**
	 * Sends an object to the server. This is the only way that methods should
	 * communicate with the server.
	 * 
	 * @param msg
	 *            The message to be sent.
	 * @exception IOException
	 *                if an I/O error occurs when sending
	 */
	final public void sendToServer(Object msg) throws IOException {
		if (nodeSocket == null || output == null)
			throw new SocketException("socket does not exist");

		output.writeObject(msg);
	}

	/**
	 * Reset the object output stream so we can use the same
	 * buffer repeatedly. This would not normally be used, but is necessary
    * in some circumstances when Java refuses to send data that it thinks has been sent.
	 */
	final public void forceResetAfterSend() throws IOException {
      output.reset();
	}

	/**
	 * Closes the connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when closing.
	 */
	final public void closeConnection() throws IOException {
		// Prevent the thread from looping any more
		stopping = true;

		try {
			closeAll();
		} finally {
			// Call the hook method
			connectionClosed();
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * @return true if the node is connnected.
	 */
	final public boolean isConnected() {
		return nodeReader != null && nodeReader.isAlive();
	}

	/**
	 * @return the port number.
	 */
	final public int getPort() {
		return port;
	}

	/**
	 * Sets the server port number for the next connection. The change in port
	 * only takes effect at the time of the next call to openConnection().
	 * 
	 * @param port
	 *            the port number.
	 */
	final public void setPort(int port) throws IllegalArgumentException {
		if ((port < 0) || (port > 65535))
			throw new IllegalArgumentException("Invalid port. Must be with in 0 to 65535.");
		this.port = port;
	}

	/**
	 * @return the host name.
	 */
	final public String getHost() {
		return host;
	}

	/**
	 * Sets the server host for the next connection. The change in host only
	 * takes effect at the time of the next call to openConnection().
	 * 
	 * @param host
	 *            the host name.
	 */
	final public void setHost(String host) {
		this.host = host;
	}

	/**
	 * returns the node's description.
	 * 
	 * @return the node's Inet address.
	 */
	final public InetAddress getInetAddress() {
		return nodeSocket.getInetAddress();
	}

	// RUN METHOD -------------------------------------------------------

	/**
	 * Waits for messages from the server. When each arrives, a call is made to
	 * <code>messageFromServer()</code>. Not to be explicitly called.
	 */
	final public void run() {
		// Open the connection
		openConnection();
		
		// Additional setting up
		connectionEstablished();

		// The message from the server
		Object msg;

		// Loop waiting for data

		try {
			while (stopping == false) {
				msg = input.readObject();
				messageFromServer(msg);
			}
		} catch (Exception exception) {
			if (stopping == false) {
				try {
					closeAll();
				} catch (Exception ex) {}

				connectionException(exception);
			}
		} finally {
			nodeReader = null;
		}
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	/**
	 * Hook method called after the connection has been closed.
	 */
	protected void connectionClosed() {
		System.out.println("connectionClosed :: Not implemented.");
	}

	/**
	 * Hook method called each time an exception is thrown by the node's
	 * thread that is waiting for messages from the server.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		System.out.println("connectionException :: " + exception.getLocalizedMessage());
	}

	/**
	 * Hook method called after a connection has been established.
	 */
	protected void connectionEstablished() {
		System.out.println("connectionEstablished :: Not implemented.");
	}

	/**
	 * Handles a message sent from the server to this node.
	 * 
	 * @param msg
	 *            the message sent.
	 */
	protected void messageFromServer(Object msg){
		System.out.println("messageFromServer :: Not implemented.");
	}

	// METHODS TO BE USED FROM WITHIN THE FRAMEWORK ONLY ----------------

	/**
	 * Closes all aspects of the connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when closing.
	 */
	private void closeAll() throws IOException {
		try {
			// Close the socket
			if (nodeSocket != null)
				nodeSocket.close();

			// Close the output stream
			if (output != null)
				output.close();

			// Close the input stream
			if (input != null)
				input.close();
		} finally {
			// Set the streams and the sockets to NULL no matter what
			// Doing so allows, but does not require, any finalizers
			// of these objects to reclaim system resources if and
			// when they are garbage collected.
			output = null;
			input = null;
			nodeSocket = null;
		}
	}
}
// end of Client class