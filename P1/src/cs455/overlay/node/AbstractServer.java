package cs455.overlay.node;

import java.net.*;
import java.text.*;
import java.util.*;

import cs455.overlay.msg.*;

import java.io.*;

public abstract class AbstractServer extends Thread {
	// INSTANCE VARIABLES *********************************************

	/**
	 * The server socket: listens for clients who want to connect.
	 */
	private ServerSocket serverSocket = null;

	/**
	 * The connection listener thread.
	 */
	private Thread connectionListener;

	/**
	 * The port number
	 */
	private int port;

	/**
	 * The server timeout while for accepting connections. After timing out, the
	 * server will check to see if a command to stop the server has been issued;
	 * it not it will resume accepting connections. Set to half a second by
	 * default.
	 */
	private int timeout = 500;

	/**
	 * The maximum queue length; i.e. the maximum number of clients that can be
	 * waiting to connect. Set to 10 by default.
	 */
	private int backlog = 10;

	/**
	 * The thread group associated with client threads. Each member of the
	 * thread group is a <code> ConnectionToClient </code>.
	 */
	protected ThreadGroup nodeThreadGroup;

	/**
	 * Indicates if the listening thread is ready to stop. Set to false by
	 * default.
	 */
	private boolean stop = true;
	
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
		this.nodeThreadGroup = new ThreadGroup("NodeConnection threads") {
			// All uncaught exceptions in connection threads will
			// be sent to the clientException callback method.
			public void uncaughtException(Thread thread, Throwable exception) {
				nodeException((NodeConnection) thread, exception);
			}
		};
		this.port = port;
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
		if (isListening() == false) {
			if (serverSocket == null) {
				serverSocket = new ServerSocket(getPort(), backlog);
			}
			if (getPort() == 0)
				this.setPort(this.serverSocket.getLocalPort());
			setName();

			serverSocket.setSoTimeout(timeout);
			stop = false;
			connectionListener = new Thread(this);
			connectionListener.start();
		}
	}
	
	/**
	 * Sets the server name.
	 */
	final public void setName() throws IOException {
		String ipAddress = InetAddress.getLocalHost().getHostAddress();
		this.setName(ipAddress+":"+this.port);
	}
	
	/**
	 * Causes the server to stop accepting new connections.
	 */
	final public void stopListening() {
		stop = true;
	}

	/**
	 * Closes the server socket and the connections with all clients. Any
	 * exception thrown while closing a client is ignored. If the server 
	 * is already closed, this call has no effect.
	 *
	 * @exception IOException
	 *                if an I/O error occurs while closing the server socket.
	 */
	final synchronized public void close() {
		if (serverSocket == null)
			return;
		stopListening();
		try {
			serverSocket.close();
		} catch (IOException ex) {
			System.err.println(ex.toString());
		} finally {
			// Close the client sockets of the already connected clients
			Thread[] clientThreadList = getNodeConnections();
			for (int i = 0; i < clientThreadList.length; i++) {
				try {
					((NodeConnection) clientThreadList[i]).close();
				} catch (Exception ex) {}
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
	public void sendToAllNodes(Protocol msg) {
		Thread[] clientThreadList = getNodeConnections();

		for (int i = 0; i < clientThreadList.length; i++) {
			try {
				((NodeConnection) clientThreadList[i]).sendToNode(msg);
			} catch (Exception ex) {
			}
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * Returns true if the server is ready to accept new clients.
	 *
	 * @return true if the server is listening.
	 */
	final public boolean isListening() {
		return (connectionListener != null);
	}
	
	/**
	 * Returns true if the server has stopped.
	 *
	 * @return true if the server has stopped.
	 */
	final public boolean getStatus() {
		return stop;
	}

	
	/**
	 * Returns a String array containing the names of the existing node 
	 * connections.
	 *
	 * @return an array of Strings containing NodeConnection Names.
	 */
	synchronized final public String[] getConnectionNames() {
		int size = nodeThreadGroup.activeCount();
		NodeConnection[] nodeThreadList = new NodeConnection[size];
		nodeThreadGroup.enumerate(nodeThreadList);
		String[] ret = new String[size];
		for (int i = 0; i < size; i ++) {
			ret[i] = nodeThreadList[i].getName();
		}
		return ret;
	}
	
	/**
	 * Returns an array containing the existing node connections. New
	 * node can also connect. This is only for that time.
	 *
	 * @return an array of NodeConnection containing NodeConnection instances.
	 */
	synchronized final public NodeConnection[] getNodeConnections() {
		NodeConnection[] nodeThreadList = new NodeConnection[nodeThreadGroup.activeCount()];
		nodeThreadGroup.enumerate(nodeThreadList);
		return nodeThreadList;
	}

	/**
	 * Counts the number of clients currently connected.
	 *
	 * @return the number of clients currently connected.
	 */
	final public int getNumberOfClients() {
		return nodeThreadGroup.activeCount();
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
		} catch (UnknownHostException e) {}
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
		} catch (UnknownHostException e) {}
		return ret;
	}
	
	/**
	 * Returns a string host name.
	 * @param ip of target machine.
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
	 * Validate input is a valid number.
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

	// RUN METHOD -------------------------------------------------------

	/**
	 * Runs the listening thread that allows clients to connect. Not to be
	 * called.
	 */
	final public void run() {
		// call the hook method to notify that the server is starting
		serverStarted();

		try {
			// Repeatedly waits for a new client connection, accepts it, and
			// starts a new thread to handle data exchange.
			while (stop == false) {
				try {
					// Wait here for new connection attempts, or a timeout
					Socket clientSocket = serverSocket.accept();

					// When a client is accepted, create a thread to handle
					// the data exchange, then add it to thread group
					synchronized (this) {
						new NodeConnection(this.nodeThreadGroup, clientSocket, this);
					}

				} catch (InterruptedIOException exception) {
					// This will be thrown when a timeout occurs.
					// The server will continue to listen if not ready to stop.
				}
			}

			// call the hook method to notify that the server has stopped
			serverClosed();
		} catch (IOException exception) {
			if (stop == false) {
				// Closing the socket must have thrown a SocketException
				listeningException(exception);
			} else {
				serverClosed();
			}
		} finally {
			stop = true;
			connectionListener = null;
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
	protected abstract void nodeConnected(NodeConnection client);

	/**
	 * Hook method called each time a client disconnects. The default
	 * implementation does nothing. The method may be overridden by subclasses
	 * but should remains synchronized.
	 *
	 * @param client
	 *            the connection with the client.
	 */
	synchronized protected void nodeDisconnected(NodeConnection node) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(node.getAddress()+" disconnected at "+dateFormat.format(date));
	}

	/**
	 * Hook method called each time an exception is thrown in a
	 * ConnectionToClient thread. The method may be overridden by subclasses but
	 * should remains synchronized.
	 *
	 * @param client
	 *            the client that raised the exception.
	 * @param Throwable
	 *            the exception thrown.
	 */
	synchronized protected void nodeException(NodeConnection client, Throwable exception) {
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
	 * Hook method called when the server is closed. When the server
	 * is closed while still listening, serverStopped() will also be 
	 * called.
	 */
	protected abstract void serverClosed();

	/**
	 * Handles a command sent from one client to the server. This MUST be
	 * implemented by subclasses, who should respond to messages. This method is
	 * called by a synchronized method so it is also implicitly synchronized.
	 *
	 * @param msg
	 *            the message sent.
	 * @param client
	 *            the connection connected to the client that sent the message.
	 */
	protected abstract void MessageFromNode(Object msg, NodeConnection client);

	// ------------------------------------------------------------------------

	/**
	 * Receives a command sent from the client to the server. Called by the run
	 * method of NodeConnection instances that are watching for
	 * messages coming from the server. This method is synchronized to ensure
	 * that whatever effects it has do not conflict with work being done by
	 * other threads. The method simply calls the
	 * <code>MessageFromNode</code> slot method.
	 *
	 * @param msg
	 *            the message sent.
	 * @param client
	 *            the connection connected to the client that sent the message.
	 */
	final synchronized void receiveMessageFromNode(Object msg, NodeConnection client) {
		this.MessageFromNode(msg, client);
	}

}
// End of AbstractServer Class
