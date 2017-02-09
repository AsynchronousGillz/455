package cs455.overlay.node;

import java.net.*;
import java.text.*;
import java.util.*;

import cs455.overlay.msg.*;
import cs455.overlay.util.*;

import java.io.*;

public abstract class AbstractServer extends Thread {
	// INSTANCE VARIABLES *********************************************

	/**
	 * The server socket: listens for clients who want to connect.
	 */
	private ServerSocket serverSocket = null;

	/**
	 * The port number
	 */
	private int port;

	/**
	 * The server timeout while for accepting connections.
	 */
	private int timeout = 5000;

	/**
	 * The maximum queue length; i.e. the maximum number of clients that can be
	 * waiting to connect. Set to 15 by default.
	 */
	private int backlog = 15;

	/**
	 * The thread group associated with client threads. Each member of the
	 * thread group is a <code> MessagingConnection </code>.
	 */
	protected ThreadGroup connectionGroup;
	
	/**
	 * The thread group associated with client threads. Each member of the
	 * thread group is a <code> MessagingProcessor </code>.
	 */
	protected ThreadGroup processorGroup;
	
	/**
	 * The connection master statistics holder. Each message will be placed in 
	 * the queue until the a {@link MessagingProcessor} can process the message.
	 * The default size is 10000. 
	 */
	protected MessageQueue inbox;
	
	/**
	 * The connection master statistics holder. Each message will be placed in 
	 * the queue until the a {@link MessagingProcessor} can process the message.
	 * The default size is 10000. 
	 */
	protected MessageQueue outbox;

	/**
	 * The connection master statistics holder.
	 */
	protected StatisticsCollector stats;

	/**
	 * For debug purposes
	 */
	protected final boolean debug = false;

	// CONSTRUCTOR ******************************************************

	/**
	 * Constructs a new server.
	 *
	 * @param port
	 *            the port number on which to listen.
	 */
	public AbstractServer(int port) {
		this.connectionGroup = new ThreadGroup("MessagingConnection threads") {
			public void uncaughtException(Thread thread, Throwable exception) {
				connectionException((MessagingConnection) thread, exception);
			}
		};
		this.processorGroup = new ThreadGroup("MessagingProcessor threads") {
			public void uncaughtException(Thread thread, Throwable exception) {
				processorException((MessagingProcessor) thread, exception);
			}
		};
		this.port = port;
		inbox = new MessageQueue(10000);
		outbox = new MessageQueue(10000);
	}

	// INSTANCE METHODS *************************************************

	/**
	 * Begins the thread that waits for new clients. If the server is already in
	 * listening mode, this call has no effect.
	 *
	 * @exception IOException
	 *                if an I/O error occurs when creating the server socket.
	 */
	final public void listen() throws IOException {
		if (serverSocket == null) {
			serverSocket = new ServerSocket(getPort(), backlog);
		}
		if (getPort() == 0)
			this.setPort(this.serverSocket.getLocalPort());
		setName();
		serverSocket.setSoTimeout(timeout);
	}

	/**
	 * Sets the server name.
	 */
	final public void setName() throws IOException {
		String ipAddress = InetAddress.getLocalHost().getHostAddress();
		super.setName(ipAddress + ":" + this.port);
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
	
	/**
	 * Add a connection to the server to another server.
	 * @param host
	 * @param sPort
	 */
	final public void addConnection(String host, String sPort) {
		int port = validateInput(sPort);
		if (port == 0)
			return;
		// Wait here for new connection attempts, or a timeout
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(host, port);
		} catch (IOException e) {
			System.err.println("Could not connect to: "+host+":"+port);
			return;
		}
		MessagingConnection node = createConnection(clientSocket);
		String hostName = this.getTargetHostName(host);
		node.setClientInfo(hostName, host, port);
		node.start();
	}
	
	/**
	 * Create a new MessagingNode connection. This will also add MessagingProcessors
	 * 
	 */
	synchronized final private MessagingConnection createConnection(Socket clientSocket) {
		try {
			new MessagingProcessor(this.processorGroup, inbox, this).start();
			new MessagingProcessor(this.processorGroup, outbox, this).start();
			return new MessagingConnection(this.connectionGroup, clientSocket, this);
		} catch (IOException e) {
			System.err.println("Error: could not create MessagingConnection.");
			return null;
		}
	}

	/**
	 * Closes the server socket and the connections with all clients. Any
	 * exception thrown while closing a client is ignored. If the server is
	 * already closed, this call has no effect.
	 *
	 * @exception IOException
	 *                if an I/O error occurs while closing the server socket.
	 */
	final synchronized public void close() {
		if (serverSocket == null)
			return;
		try {
			serverSocket.close();
		} catch (IOException ex) {
			System.err.println(ex.toString());
		} finally {
			// Close the client sockets of the already connected clients
			for (MessagingConnection node : getNodeConnections()) {
				try {
					node.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			serverSocket = null;
			serverClosed();
		}
	}

	/**
	 * Sends a message to every client connected to the server.
	 *
	 * @param msg
	 *            Object The message to be sent
	 */
	public void sendToAllNodes(ProtocolMessage msg) {
		for (MessagingConnection node : getNodeConnections()) {
			try {
				node.sendToNode(msg);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * Returns a String array containing the names of the existing node
	 * connections.
	 *
	 * @return an array of Strings containing NodeConnection Names.
	 */
	synchronized final public String[] getConnectionNames() {
		int size = connectionGroup.activeCount();
		MessagingConnection[] nodeThreadList = new MessagingConnection[size];
		connectionGroup.enumerate(nodeThreadList);
		String[] ret = new String[size];
		for (int i = 0; i < size; i++) {
			ret[i] = nodeThreadList[i].toString();
		}
		return ret;
	}

	/**
	 * Returns the NodeConnection Object that has both port and ip matching.
	 * 
	 * @param address
	 *            "ip:port"
	 * @return an array of Strings containing NodeConnection Names.
	 */
	final public MessagingConnection getConnection(String address) {
		int size = connectionGroup.activeCount();
		MessagingConnection[] nodeThreadList = new MessagingConnection[size];
		connectionGroup.enumerate(nodeThreadList);
		for (MessagingConnection node : nodeThreadList) {
			if (address.equals(node.getConnection()) == true) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Returns an array containing the existing node connections. New node can
	 * also connect. This is only for that time.
	 *
	 * @return an array of NodeConnection containing NodeConnection instances.
	 */
	final public MessagingConnection[] getNodeConnections() {
		MessagingConnection[] nodeThreadList = new MessagingConnection[connectionGroup.activeCount()];
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
	 * Sets the port number for the next connection. The server must be closed
	 * and restarted for the port change to be in effect.
	 *
	 * @param port
	 *            the port number.
	 */
	final public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Each Server will have a {@link StatisticsCollector} even the registry to
	 * add up the total of the Statistics.
	 * 
	 * @return
	 */
	final public StatisticsCollector getStats() {
		return stats;
	}

	/**
	 * Each Server will have a {@link StatisticsCollector} even the registry
	 * this will create the StatisticsCollector for the server.
	 * 
	 * @param list
	 */
	final public void setStats() {
		this.stats = new StatisticsCollector();
	}

	/**
	 * Sets the timeout time when accepting connections. The default is half a
	 * second. This means that stopping the server may take up to timeout
	 * duration to actually stop. The server must be stopped and restarted for
	 * the timeout change to be effective.
	 *
	 * @param timeout
	 *            the timeout time in ms.
	 */
	final public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Sets the maximum number of waiting connections accepted by the operating
	 * system. The default is 20. The server must be closed and restarted for
	 * the backlog change to be in effect.
	 *
	 * @param backlog
	 *            the maximum number of connections.
	 */
	final public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	/**
	 * Validate input is a valid number. If number is zero then parse int failed
	 * and an error will be printed and return zero.
	 * 
	 * @param input
	 *            The string to be validated.
	 */
	final public int validateInput(String input) {
		try {
			int num = Integer.parseInt(input);
			if (num < 0)
				throw new Exception();
			return num;
		} catch (NumberFormatException e) {
			System.err.println("Error: Invalid input must input a number.");
		} catch (Exception e) {
			System.err.println("Error: number must be greater then zero.");
		}
		return 0;
	}
	
	/**
	 * Add the {@link MessagePair} to the queue.
	 * @param pair
	 */
	public void addPair(MessagePair pair) {
		inbox.put(pair);
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
			while (super.isInterrupted() == false) {
				try {
					createConnection(serverSocket.accept()).start();
				} catch (InterruptedIOException exception) {}
			}
			serverClosed();
		} catch (IOException exception) {
			if (super.isInterrupted() == true) {
				serverClosed();
			}
		}
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	/**
	 * Hook method called each time a new client connection is accepted. The
	 * default implementation does nothing.
	 * 
	 * @param client
	 *            the connection connected to the client.
	 */
	protected abstract void nodeConnected(MessagingConnection client);

	/**
	 * Hook method called each time a client disconnects. The default
	 * implementation does nothing. The method may be overridden by subclasses
	 * but should remains synchronized.
	 *
	 * @param client
	 *            the connection with the client.
	 */
	synchronized protected void nodeDisconnected(MessagingConnection node) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(node.getAddress() + " disconnected at " + dateFormat.format(date));
	}

	/**
	 * Called each time an exception is thrown in a MessagingConnection thread.
	 *
	 * @param client
	 *            the client that raised the exception.
	 * @param Throwable
	 *            the exception thrown.
	 */
	final synchronized protected void connectionException(MessagingConnection client, Throwable exception) {
		System.err.println(client + " has thrown Exception: " + exception.toString());
		exception.printStackTrace();
	}
	
	/**
	 * Called each time an exception is thrown in a  thread.
	 *
	 * @param client
	 *            the client that raised the exception.
	 * @param Throwable
	 *            the exception thrown.
	 */
	final synchronized protected void processorException(MessagingProcessor client, Throwable exception) {
		System.err.println(client + " has thrown Exception: " + exception.toString());
		exception.printStackTrace();
	}

	/**
	 * Hook method called when the server stops accepting connections because an
	 * exception has been raised. The default implementation does nothing. This
	 * method may be overriden by subclasses.
	 *
	 * @param exception
	 *            the exception raised.
	 */
	protected abstract void listeningException(Throwable exception);

	/**
	 * Hook method called when the server starts listening for connections. The
	 * default implementation does nothing. The method may be overridden by
	 * subclasses.
	 */
	protected abstract void serverStarted();

	/**
	 * Hook method called when the server is closed. When the server is closed
	 * while still listening, serverStopped() will also be called.
	 */
	protected abstract void serverClosed();

	/**
	 * Handles a command sent from one client to the server. This MUST be
	 * implemented by subclasses, who should respond to messages.
	 *
	 * @param msg
	 *            the message sent.
	 * @param client
	 *            the connection connected to the client that sent the message.
	 */

	protected abstract void MessageFromNode(ProtocolMessage msg, MessagingConnection client);
	
}
// End of AbstractServer Class
