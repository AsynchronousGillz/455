package cs455.scaling.client;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

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
	 * 
	 */
	private Selector selector;
	
	/**
	 * 
	 */
	private ByteBuffer bytes = ByteBuffer.allocate(8192);

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
	public ClientConnection(String serverAddress, int serverPort) throws IOException {
		// Initialize variables
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		
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
	 * Sends an object to the server. This is the only way that methods should
	 * communicate with the server.
	 * 
	 * @param msg
	 *            The message to be sent.
	 * @exception IOException
	 *                if an I/O error occurs when sending
	 */
	final private void sendToServer(Message msg) throws IOException {
		if (channel == null || Addr == null)
			throw new SocketException("Connection error when sending.");
		if (debug)
			System.out.println("sending: " + msg);
		synchronized (channel) {
			channel.write(msg.getMessage());
		}
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
		try {
			while (bytes.hasRemaining() && read != -1){
				read = channel.read(bytes);
			}
			if (read == -1)
				throw new IOException();
			return new Message(bytes);
		} catch (IOException e) {
			System.err.println("An error occured when reading.");
		} finally {
			bytes.clear();
		}
		return null;
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * @return true if the node is connnected.
	 */
	final public boolean isConnected() {
		return this.isAlive() && this.running;
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
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// RUN METHOD -------------------------------------------------------

	/**
	 * Waits for messages from the server. When each arrives, a call is made to
	 * <code>messageFromServer()</code>. Not to be explicitly called.
	 */
	final public void run() {
		// Additional setting up
		connectionEstablished();
		boolean info = true;
		try {
			while (this.running == true) {
				this.selector.select();
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while(keyIterator.hasNext()) {
				    SelectionKey key = keyIterator.next();
				    if (key.isValid() == false) {
				    	continue;
				    } else if (key.isConnectable()) {
				    	// a channel is ready to connect
				    	this.channel.finishConnect();
				    	this.channel.register(this.selector, (SelectionKey.OP_READ | SelectionKey.OP_WRITE));
				    } else if (key.isReadable()) {
				    	Message msg = this.readFromServer();
				    	System.out.println("Received message: "+msg);
				    } else if (key.isWritable()) {
				        // a channel is ready for writing
				    	if (info)
				    		this.sendToServer(new Message());
				    	info = false;
//						this.sleep(4000);
				    }
				    keyIterator.remove();
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
	}

	/**
	 * Hook method called after a connection has been established.
	 */
	public void connectionEstablished() {
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
