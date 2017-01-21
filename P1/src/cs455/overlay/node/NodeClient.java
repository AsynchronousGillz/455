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

public class NodeClient implements Runnable {

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
	private DataOutputStream output;

	/**
	 * The stream to handle data from the server.
	 */
	private DataInputStream input;

	/**
	 * The thread created to read data from the server.
	 */
	private Thread nodeReader;

	/**
	 * Indicates if the thread is ready to stop. Needed so that the loop in the
	 * run method knows when to stop waiting for incoming messages.
	 */
	private boolean stopping = false;

	/**
	 * The server's host name.
	 */
	private String registryIP;

	/**
	 * The port number.
	 */
	private int registryPort;

	/**
	 * The node server port number.
	 */
	private int nodePort;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the node.
	 * 
	 * @param registryIP
	 *            the server's host name.
	 * @param registryPort
	 *            the port number.
	 */
	public NodeClient(String registryIP, int registryPort, int nodePort) {
		// Initialize variables
		this.registryIP = registryIP;
		this.registryPort = registryPort;
		this.nodePort = nodePort;
		openConnection();
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
			nodeSocket = new Socket(registryIP, registryPort);
			output = new DataOutputStream(nodeSocket.getOutputStream());
			input = new DataInputStream(nodeSocket.getInputStream());
		} catch (IOException ex) {
			closeConnection();
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
	final public void sendToServer(Message msg) throws IOException {
		if (nodeSocket == null || output == null)
			throw new SocketException("socket does not exist");
		byte[] bytes = msg.makeBytes();
		output.writeInt(bytes.length);
		output.write(bytes, 0, bytes.length);
	}

	/**
	 * Closes the connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when closing.
	 */
	final public void closeConnection() {
		// Prevent the thread from looping any more
		stopping = true;

		try {
			closeAll();
		} catch (Exception exc) {
			System.err.println(exc.toString());
		} finally {
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
	final public int getNodePort() {
		return nodePort;
	}

	/**
	 * Sets the server port number for the next connection. The change in port
	 * only takes effect at the time of the next call to openConnection().
	 * 
	 * @param port
	 *            the port number of the node server port.
	 */
	final public void setNodePort(int port) throws IllegalArgumentException {
		if ((port < 0) || (port > 65535))
			throw new IllegalArgumentException("Invalid port. Must be with in 0 to 65535.");
		this.nodePort = port;
	}

	/**
	 * @return the port number.
	 */
	final public int getPort() {
		return registryPort;
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
		this.registryPort = port;
	}

	/**
	 * @return the host name.
	 */
	final public String getHost() {
		return registryIP;
	}

	/**
	 * Sets the server host for the next connection. The change in host only
	 * takes effect at the time of the next call to openConnection().
	 * 
	 * @param host
	 *            the host name.
	 */
	final public void setHost(String host) {
		this.registryIP = host;
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
		// Additional setting up
		connectionEstablished();

		// The message from the server
		int byteSize;

		// Loop waiting for data
		try {
			while (stopping == false) {
				byteSize = input.readInt();
				byte[] bytes = new byte[byteSize];
				input.readFully(bytes, 0, byteSize);
				messageFromServer(new Message(bytes));
			}
		} catch (Exception exception) {
			if (stopping == false) {
				close();
				connectionException(exception);
			}
		} finally {
			nodeReader = null;
		}
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	/**
	 * Hook method called each time an exception is thrown by the node's thread
	 * that is waiting for messages from the server.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		System.out.println("connectionException :: " + exception.toString());
	}

	/**
	 * Hook method called after a connection has been established.
	 */
	protected void connectionEstablished() {
		if (nodeSocket == null)
			return;
		NodeAddress node = new NodeAddress(getInetAddress(), getNodePort());
		Message m = new Message(node.toString());
		m.setType("REGISTER_REQUEST");
		try {
			sendToServer(m);
		} catch (IOException e) {
			System.err.println("Could not send Registration.");
		}
	}

	/**
	 * Hook method called after the connection has been closed.
	 */
	protected void connectionClosed() {
		if (nodeSocket == null)
			return;
		NodeAddress node = new NodeAddress(getInetAddress(), getNodePort());
		Message m = new Message(node.toString());
		m.setType("DEREGISTER_REQUEST");
		try {
			sendToServer(m);
		} catch (IOException e) {
			System.err.println("Could not send Deregistration.");
		}
	}

	/**
	 * Handles a message sent from the server to this node.
	 * 
	 * @param msg
	 *            the message sent.
	 */
	protected void messageFromServer(Object o) {
		if (o instanceof Message == false)
			return;
		Message m = (Message) o;
		System.out.println(m);
	}

	// METHODS TO BE USED FROM WITHIN THE FRAMEWORK ONLY ----------------

	/**
	 * Closes the node. If the connection is already closed, this call has no
	 * effect.
	 * 
	 * @exception IOException
	 *                if an error occurs when closing the socket.
	 */
	final public void close() {
		stopping = true; // Set the flag that tells the thread to stop

		try {
			closeAll();
		} catch (IOException ex) {
			System.err.println(ex.toString());
		}
	}

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