package cs455.scaling.client;

import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;

/**
 * 
 * @author G van Andel
 *
 * @see Client.NodeMain
 */

public class ClientConnection extends Thread {

	// INSTANCE VARIABLES ***********************************************

	/**
	 * 
	 */
	private InetSocketAddress Addr;

	/**
	 * 
	 */
	private SocketChannel channel;

	/**
	 * The server's host name.
	 */
	private String serverAddress;

	/**
	 * The port number.
	 */
	private int serverPort;

	/**
	 * For debug purposes
	 */
	final private boolean debug = false;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the node.
	 * 
	 * @param serverAddress
	 *            the server's host name.
	 * @param serverPort
	 *            the port number.
	 */
	public ClientConnection(String serverAddress, int serverPort) {
		// Initialize variables
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		try {
			Addr = new InetSocketAddress("localhost", 1111);
			channel = SocketChannel.open(Addr);
		} catch (IOException ex) {
			close(1);
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
	final public void sendToServer(Message msg) throws IOException {
		if (channel == null || Addr == null)
			throw new SocketException("socket does not exist");
		synchronized (channel) {
			channel.write(msg.getMessage());
			if (debug)
				System.out.println("sending: " + msg);
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
		return serverPort;
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
		this.serverPort = port;
	}

	/**
	 * @return the host name.
	 */
	final public String getRegistryHost() {
		return serverAddress;
	}

	/**
	 * Sets the server host for the next connection. The change in host only
	 * takes effect at the time of the next call to openConnection().
	 * 
	 * @param host
	 *            the host name.
	 */
	final public void setRegistryHost(String serverAddress) {
		this.serverAddress = serverAddress;
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
				// TODO: Read from the channel
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
		if (channel == null)
			return;
		try {
			sendToServer(new Message());
		} catch (IOException e) {
			System.err.println("Could not send Registration.");
		}
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
	final private void close(int mode) {
		if (mode == 0)
			System.out.println("Disconnecting from the server. Exitting.");
		else
			System.err.println("An error occured connecting to the server. Exitting.");

		try {
			channel.close();
		} catch (IOException ex) {
			if (debug)
				System.err.println(ex.toString());
		} finally {
			System.exit(mode);
		}
	}

	// ----------------------------------------------------------


}
