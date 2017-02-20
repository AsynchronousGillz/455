package cs455.scaling.server;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.text.*;
import java.util.*;

import cs455.scaling.util.*;
import cs455.scaling.msg.*;

public class NioServer extends Thread {
	// INSTANCE VARIABLES *********************************************

	/**
	 * The server socket: listens for clients who want to connect.
	 */
	private ServerSocketChannel serverChannel = null;

	/**
	 * The port number
	 */
	private int port;

	/**
	 * The thread group associated with client threads. Each member of the
	 * thread group is a <code> Connection </code>.
	 */
	private ThreadGroup connectionGroup;
	
	/**
	 * The manager contains both the {@link Queue} and the 
	 * {@link ThreadPool} this way it will pair the task to a thread. 
	 */
	private TaskManager manager;
	
	/**
	 * TODO
	 */
	private Selector selector;
	
	/**
	 * TODO
	 */
	private SelectionKey selectionKey;

	/**
	 * The buffer to read data when it's available. Approximately 8K 
	 */
	private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

	/**
	 * To stop the run loop.
	 */
	private boolean running;
	
	/**
	 * For debug purposes
	 */
	public final boolean debug = false;

	// CONSTRUCTOR ******************************************************

	/**
	 * Constructs a new server.
	 *
	 * @param port
	 *            the port number on which to listen.
	 * @throws IOException 
	 */
	public NioServer(int port) throws IOException {
		this.port = port;
		this.initSelector();
		this.running = false;
		serverChannel = ServerSocketChannel.open();
		serverChannel.socket().bind(new InetSocketAddress(this.getHost(), port));
		serverChannel.configureBlocking(false);
		selectionKey = serverChannel.register(selector, SelectionKey.OP_READ);
		setName();
	}
	
	/**
	 * Sets up the {@link Selector}.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs when creating the server socket.
	 */
	private void initSelector() throws IOException {
		// Create a new selector
		Selector socketSelector = SelectorProvider.provider().openSelector();

		// Create a new non-blocking server socket channel
		this.serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(this.getHost(), this.port);
		serverChannel.socket().bind(isa);

		// Register the server socket channel, indicating an interest in
		// accepting new connections
		serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

		this.selector = socketSelector;
	}

	/**
	 * Sets the server name.
	 */
	final public void setName() throws IOException {
		String ipAddress = InetAddress.getLocalHost().getHostAddress();
		super.setName(ipAddress + ":" + this.port);
	}
	
	/**
	 * Closes the server socket and the connections with all clients. Any
	 * exception thrown while closing a client is ignored. If the server is
	 * already closed, this call has no effect.
	 *
	 * @exception IOException
	 *                if an I/O error occurs while closing the server socket.
	 */
	synchronized final public void close() {
		if (serverChannel == null)
			return;
		try {
			serverChannel.close();
			selector.close();
			selectionKey.cancel();
			selectionKey = null;
		} catch (IOException ex) {
			System.err.println(ex.toString());
		} finally {
			manager.close();
			serverClosed();
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * Returns an array containing the existing node connections. New node can
	 * also connect. This is only for that time.
	 *
	 * @return an array of NodeConnection containing NodeConnection instances.
	 */
	final public Processor[] getNodeConnections() {
		Processor[] nodeThreadList = new Processor[connectionGroup.activeCount()];
		connectionGroup.enumerate(nodeThreadList);
		return nodeThreadList;
	}

	/**
	 * Counts the number of clients currently connected.
	 *
	 * @return the number of clients currently connected.
	 */
	final public int getNumberOfClients() {
		return connectionGroup.activeCount();
	}

	/**
	 * Returns the raw IP address.
	 *
	 * @return the ip address in a String format.
	 */
	final public String getHost() {
		String ret = null;
		try {
			ret = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		}
		return ret;
	}

	/**
	 * Returns the host name for this IP address.
	 *
	 * @return the host name in String format.
	 */
	final public String getHostName() {
		String ret = null;
		try {
			ret = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return ret;
	}

	/**
	 * Returns a string host name.
	 *
	 * @param ip
	 *            of target machine.
	 * @return machine host name.
	 */
	final public String getTargetHostName(String targetIP) {
		String ret = null;
		try {
			InetAddress host = InetAddress.getByName(targetIP);
			ret = host.getHostName();
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	/**
	 * Returns the port number.
	 *
	 * @return the port number.
	 */
	final public int getPort() {
		return port;
	}

	/**
	 * Validate input is a valid number. If number is zero then parse int failed
	 * and an error will be printed and return zero.
	 * 
	 * @param input
	 *            The string to be validated.
	 */
	final public int validateInput(String input) throws Exception {
		int num = 0;
		try {
			num = Integer.parseInt(input);
			if (num < 0)
				throw new Exception();
		} catch (NumberFormatException e) {
			throw new Exception("Error: Invalid input must input a number.");
		} catch (Exception e) {
			throw new Exception("Error: number must be greater then zero.");
		}
		return num;
	}

	// RUN METHOD -------------------------------------------------------

	/**
	 * Runs the listening thread that allows clients to connect. Not to be
	 * called.
	 */
	final public void run() {
		// call the hook method to notify that the server is starting
		serverStarted();

		try {
			while (running) {   
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while(keyIterator.hasNext()) {
				    SelectionKey key = keyIterator.next();
				    if (key.isValid() == false) {
				    	continue;
				    } else if(key.isAcceptable()) {
				        this.accept(key);
				    } else if (key.isConnectable()) {
				        this.read(key);
				    } else if (key.isReadable()) {
				        this.read(key);
				    } else if (key.isWritable()) {
				        // a channel is ready for writing
				    }
				    keyIterator.remove();
				}
			}
		} catch (Exception exception) {
			if (running == true) {
				exception.printStackTrace();
			}
		} finally {
			serverClosed();
		} 
		
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	/**
	 * Called each time an exception is thrown in a Connection thread.
	 *
	 * @param client
	 *            the client that raised the exception.
	 * @param Throwable
	 *            the exception thrown.
	 */
	final synchronized public void connectionException(Processor client, Throwable exception) {
		System.err.println(client + " has thrown Exception: " + exception.toString());
		exception.printStackTrace();
	}

	public void nodeConnected(Processor nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" connected at "+dateFormat.format(date));
	}

	synchronized public void nodeDisconnected(Processor nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" disconnected at "+dateFormat.format(date));
	}

	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.out.println("listeningException :: "+exception.getStackTrace());
		System.exit(1);
	}

	public void serverStarted() {
		System.out.println("Registry server started "+getName());
		this.running = true;
	}

	public void serverClosed() {
		System.out.println("serverStopped :: Exitting.");
	}
	
	/**
	 * For an accept to be pending the channel must be a server socket channel.
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void accept(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		// Accept the connection and make it non-blocking
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		// Register the new SocketChannel with our Selector, indicating
		// we'd like to be notified when there's data waiting to be read
		socketChannel.register(this.selector, SelectionKey.OP_READ);
		if (debug)
			System.out.println("New node registered.");
	}

	/**
	 * For a read to be
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		this.readBuffer.clear();

		// Attempt to read off the channel

		int read = 0;   
		try {
			while (this.readBuffer.hasRemaining() && read != -1){
				read = socketChannel.read(readBuffer);
			}
		} catch (IOException e) {
			key.cancel();
			socketChannel.close();
			return;
		}

		// Hand the data off to our worker thread
		this.manager.addMessage(socketChannel, new Message(readBuffer.array()));
	}


}
// EOF
