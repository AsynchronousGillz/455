package cs455.scaling.client;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import cs455.scaling.msg.Message;

/**
 * 
 * @author G van Andel
 *
 * @see Client.NodeMain
 */

public class Receiver extends Thread {

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
	 * 
	 */
	private Selector selector;
	
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
	public Receiver(String serverAddress, int serverPort) throws IOException {
		
		// Selector: multiplexor of SelectableChannel objects
		this.selector = SelectorProvider.provider().openSelector();
		
		// Create a new non-blocking socket channel
		this.Addr = new InetSocketAddress(serverAddress, serverPort);
		this.channel = SocketChannel.open(Addr);
		this.channel.configureBlocking(false);
		 
		// Adjusts this channel's ops
		channel.register(this.selector, channel.validOps());
	}
	
	/**
	 * Read bytes from the server. This is the only way that methods should
	 * communicate with the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when sending
	 */
	final private Message readFromServer() throws IOException {
		if (channel == null || Addr == null)
			throw new SocketException("Connection error when sending.");
		int read = 0;
		Message msg = new Message();
		ByteBuffer bytes = msg.getMessage();
		bytes.clear();
		try {
			while (bytes.hasRemaining() && read != -1){
				read = channel.read(bytes);
			}
			if (read == -1)
				throw new IOException();
		} catch (IOException e) {
			System.err.println("An error occured when reading.");
		} finally {
			bytes.clear();
		}
		return msg;
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * @return true if the node is connnected.
	 */
	final public boolean isConnected() {
		return this.isAlive() && this.running;
	}
	
	/**
	 * 
	 * @return
	 */
	final public Sender makeSender() {
		return new Sender(this.selector, this.channel);
	}

	
	// RUN METHOD -------------------------------------------------------

	/**
	 * Waits for messages from the server. When each arrives, a call is made to
	 * <code>messageFromServer()</code>. Not to be explicitly called.
	 */
	final public void run() {
		connectionEstablished();
		try {
			while (this.running == true) {
				this.selector.select();
				SelectionKey key = selector.selectedKeys().iterator().next();
			    if (key.isValid() == false) {
			    	continue;
			    } else if (key.isConnectable()) {
			    	this.channel.finishConnect();
			    	this.channel.register(this.selector, (SelectionKey.OP_READ | SelectionKey.OP_WRITE));
			    } else if (key.isReadable()) {
			    	System.out.println("Received message: "+this.readFromServer().toHash());
			    }
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
		exception.printStackTrace();
	}

	/**
	 * Hook method called after a connection has been established.
	 */
	private void connectionEstablished() {
		this.running = true;
	}
	
	/**
	 * Hook method to stop the client.
	 */
	public void closeConnection() {
		this.running = false;
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
