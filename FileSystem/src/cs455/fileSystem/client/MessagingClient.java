package cs455.fileSystem.client;

import java.io.*;
import java.net.*;

import cs455.fileSystem.chunk.ChunkNode;
import cs455.fileSystem.msg.*;

/**
 * 
 * @author G van Andel
 *
 * @see ChunkNode.NodeMain
 */

public class MessagingClient extends Thread {

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
	 * The server's host name.
	 */
	private String registryIP;

	/**
	 * The port number.
	 */
	private int registryPort;

	/**
	 * For debug purposes
	 */
	final private boolean debug = false;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the node.
	 * 
	 * @param registryIP
	 *            the server's host name.
	 * @param registryPort
	 *            the port number.
	 */
	public MessagingClient(String registryIP, int registryPort) {
		// Initialize variables
		this.registryIP = registryIP;
		this.registryPort = registryPort;
		try {
			nodeSocket = new Socket(registryIP, registryPort);
			output = new DataOutputStream(nodeSocket.getOutputStream());
			input = new DataInputStream(nodeSocket.getInputStream());
		} catch (IOException ex) {
			closeConnection();
		}
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
	final public void sendToServer(ProtocolMessage msg) throws IOException {
		if (nodeSocket == null || output == null)
			throw new SocketException("socket does not exist");
		byte[] bytes = msg.makeBytes();
		synchronized (output) {
			output.writeInt(bytes.length);
			output.write(bytes, 0, bytes.length);
			output.flush();
		}
	}

	/**
	 * Closes the connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when closing.
	 */
	final public void closeConnection() {
		try {
			closeAll();
		} catch (Exception exc) {
			if (debug)
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
		return this.isAlive();
	}

	/**
	 * @return the port number.
	 */
	final public int getRegistryPort() {
		return registryPort;
	}

	/**
	 * Sets the server port number for the next connection. The change in port
	 * only takes effect at the time of the next call to openConnection().
	 * 
	 * @param port
	 *            the port number.
	 */
	final public void setRegistryPort(int port) throws IllegalArgumentException {
		if ((port < 0) || (port > 65535))
			throw new IllegalArgumentException("Invalid port. Must be with in 0 to 65535.");
		this.registryPort = port;
	}

	/**
	 * @return the host name.
	 */
	final public String getRegistryHost() {
		return registryIP;
	}

	/**
	 * Sets the server host for the next connection. The change in host only
	 * takes effect at the time of the next call to openConnection().
	 * 
	 * @param host
	 *            the host name.
	 */
	final public void setRegistryHost(String registryIP) {
		this.registryIP = registryIP;
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
		try {
			while (super.isInterrupted() == false) {
				int byteSize = input.readInt();
				byte[] bytes = new byte[byteSize];
				input.readFully(bytes, 0, byteSize);
				messageFromServer(new ProtocolMessage(bytes));
			}
		} catch (Exception exception) {
			connectionException(exception);
			close(1);
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
	public void connectionException(Exception exception) {
		if (exception instanceof EOFException == false)
			System.out.println("connectionException :: " + exception.toString());
	}

	/**
	 * Hook method called after a connection has been established.
	 */
	public void connectionEstablished() {
		if (nodeSocket == null)
			return;
		
	}

	/**
	 * Hook method called after the connection has been closed.
	 */
	public void connectionClosed() {
		if (nodeSocket == null)
			return;
		
	}

	// METHODS TO BE USED FROM WITHIN THE FRAMEWORK ONLY ----------------

	/**
	 * Closes the node. If the connection is already closed, this call has no
	 * effect.
	 * 
	 * @param node
	 *            0 for exit 1 for error
	 * @exception IOException
	 *                if an error occurs when closing the socket.
	 */
	final public void close(int mode) {
		if (mode == 0)
			System.out.println("Disconnecting from the server. Exitting.");
		else
			System.err.println("An error occured connecting to the server. Exitting.");
		
		try {
			closeAll();
		} catch (IOException ex) {
			if (debug)
				System.err.println(ex.toString());
		} finally {
			System.exit(mode);
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
			output = null;
			input = null;
			nodeSocket = null;
		}
	}

	// ----------------------------------------------------------

	/**
	 * Handles a message sent from the server to this node.
	 * 
	 * Then checks if the message is a protocol then if so converts it to a
	 * Protocol. Then switch based on the message type.
	 * 
	 * @param o
	 *            the incoming message.
	 */
	protected void messageFromServer(Object o) {
		if (o instanceof ProtocolMessage == false)
			return;
		ProtocolMessage m = (ProtocolMessage) o;
		switch (m.getStringType()) {
			case "REGISTER_RESPONSE":
				break;
		}
	}

}
