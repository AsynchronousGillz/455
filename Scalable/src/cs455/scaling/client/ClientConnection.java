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
	 * To stop the run loop.
	 */
	private boolean running;

	/**
	 * For debug purposes
	 */
	final private boolean debug = true;

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
			Addr = new InetSocketAddress(serverAddress, serverPort);
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
		if (debug)
			System.out.println("sending: " + msg);
		synchronized (channel) {
			channel.write(msg.makeBytes());
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
	
	/**
	 * Causes the server to stop accepting new connections.
	 */
	final public void sleep(int time) {
		int step = 1000;
		for (int i = 1; i < time; i += step) {
			double percent = (i * 1.0 / time * 1.0) * 100;
			printProgBar((int) percent);
			try {
				Thread.sleep(step);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}

	final public void printProgBar(int percent) {
		StringBuilder bar = new StringBuilder("[");
		for (int i = 0; i < 50; i++) {
			if (i < (percent / 2)) {
				bar.append("=");
			} else if (i == (percent / 2)) {
				bar.append(">");
			} else {
				bar.append(" ");
			}
		}
		bar.append("]   " + percent + "%     ");
		System.out.print("\r" + bar.toString());
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
			while (this.running == true) {
				this.sendToServer(new Message());
				this.sleep(4000);
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
		this.running = true;
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
