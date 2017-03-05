package cs455.scaling.server;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.text.*;
import java.util.*;

import cs455.scaling.task.*;
import cs455.scaling.util.*;

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
	 * The manager contains both the {@link Queue} and the 
	 * {@link ThreadPool} this way it will pair the task to a thread. 
	 */
	private TaskManager manager;
	
	/**
	 * TODO
	 */
	private Selector selector;
	
	/**
	 * 
	 */
	private Integer clientCount;
	
	/**
	 * To stop the run loop.
	 */
	private boolean running;

	// CONSTRUCTOR ******************************************************

	/**
	 * Constructs a new server.
	 *
	 * @param port
	 *            the port number on which to listen.
	 * @throws IOException 
	 */
	public NioServer(int port, int poolSize) throws IOException {
		this.port = port;
		
		// Selector: multiplexor of SelectableChannel objects
		this.selector = SelectorProvider.provider().openSelector();
		 
		// Create a new non-blocking server socket channel
		this.serverChannel = ServerSocketChannel.open();
		this.serverChannel.configureBlocking(false);
		 
		// Binds the channel's socket to a local address and configures the socket to listen for connections
		String ipAddress = InetAddress.getLocalHost().getHostAddress();
		this.serverChannel.bind(new InetSocketAddress(ipAddress, port));
		 
		// Adjusts this channel's blocking mode.
		this.serverChannel.configureBlocking(false);
		serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		
		this.manager = new TaskManager(poolSize, 10000);
		this.manager.start();
		
		this.running = false;
		super.setName(ipAddress + ":" + this.port);
		this.clientCount = 0;
	}
	
	/**
	 * Closes the server socket and the connections with all clients. Any
	 * exception thrown while closing a client is ignored. If the server is
	 * already closed, this call has no effect.
	 *
	 * @exception IOException
	 *                if an I/O error occurs while closing the server socket.
	 */
	final public void close() {
		if (this.serverChannel == null)
			return;
		try {
			this.serverChannel.close();
			this.selector.close();
		} catch (IOException ex) {
			System.err.println(ex.toString());
		} finally {
			this.manager.close();
			this.serverClosed();
		}
		System.exit(0);
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * Returns the port number.
	 *
	 * @return the port number.
	 */
	final public int getPort() {
		return port;
	}


	final public String getQueueStatus() {
		return manager.getInfo();
	}

	// RUN METHOD -------------------------------------------------------

	/**
	 * Runs the listening thread that allows clients to connect. Not to be
	 * called.
	 */
	public void run() {
		// call the hook method to notify that the server is starting
		serverStarted();

		try {
			while (running == true) { 
				this.selector.select();
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while(keyIterator.hasNext()) {
				    SelectionKey key = keyIterator.next();
				    keyIterator.remove();
				    if (key.isValid() == false) {
				    	continue;
				    } else if(key.isAcceptable()) {
				        this.accept(key);
				    } else if (key.isReadable() && key.attachment() == null) {
				    	key.attach(new Object());
				    	this.manager.enqueueTask(new ReadTask(key, this));
				    }
				}
			}
		} catch (Exception exception) {
//			if (running == true)
//				exception.printStackTrace();
		} finally {
			this.close();
		} 
		
	}
	
	// PRIVATE METHODS -----------------------------------
	
	private void accept(SelectionKey key) {
		this.serverChannel = (ServerSocketChannel) key.channel();
		// Accept the connection and make it non-blocking
		SocketChannel socketChannel = null;
		try {
			socketChannel = serverChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.clientConnected();
	}

	// METHODS --------------------------------------------

	public void serverStarted() {
		System.out.println("Registry server started "+getName());
		this.running = true;
	}

	public void serverClosed() {
		this.running = false;
		System.out.println("Exitting.");
	}
	
	public String getInfo(Date time) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String info = "Current Server Throughput: "+this.manager.getSent()+" messages/s";
		synchronized (this.clientCount) {
			info +=  ", Active Client Connections: "+this.clientCount;
		}
		return "[ "+dateFormat.format(time)+" ] "+info;	
	}
	
	public void clientDisconnected() {
		synchronized (this.clientCount) {
			this.clientCount--;	
		}
		System.err.println("A client has disconnected.");
	}
	
	public void clientConnected() {
		synchronized (this.clientCount) {
			this.clientCount++;	
		}
		System.out.println("A new client has connected.");
	}
	
}
// EOF
