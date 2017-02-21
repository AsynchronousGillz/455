package cs455.scaling.server;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
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
	 * To stop the run loop.
	 */
	private boolean running;
	
	/**
	 * For debug purposes
	 */
	public final boolean debug = true;

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
		
		// SOmething here
		serverChannel.register(this.selector, serverChannel.validOps());
		
		this.manager = new TaskManager(this, poolSize, 20000);
		this.running = false;
		
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
	final public void close() {
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
	 * Returns the port number.
	 *
	 * @return the port number.
	 */
	final public int getPort() {
		return port;
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
			while (running == true) { 
				selector.select();
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while(keyIterator.hasNext()) {
				    SelectionKey key = keyIterator.next();
				    if (key.isValid() == false) {
				    	continue;
				    } else if(key.isAcceptable()) {
				        manager.acceptTask(new AcceptTask(key));
				    } else if (key.isConnectable()) {
				    	// a channel is ready for Something
				    } else if (key.isReadable()) {
				    	manager.readTask(new ReadTask(key));
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


}
// EOF
