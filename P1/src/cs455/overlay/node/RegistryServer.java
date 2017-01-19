// File Name RegistryServer.java
package cs455.overlay.node;

/**
 * 
 * @author G van Andel
 *
 * @see cs455.overlay.registry.RegistryInterface
 * 
 */

import java.net.*;
import java.util.*;
import java.io.*;

public class RegistryServer extends Thread {

	/**
	 * The server socket: listens for nodes who want to connect.
	 */
	private ServerSocket serverSocket = null;

	/**
	 * The connection listener thread.
	 */
	private Thread connectionListener;
	
	/**
	 * The connection listener thread.
	 */
	private RegistryList serverList;

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
	 * The maximum queue length; i.e. the maximum number of nodes that can be
	 * waiting to connect. Set to 10 by default.
	 */
	private int backlog = 10;

	/**
	 * Indicates if the listening thread is ready to stop. Set to false by
	 * default.
	 */
	private boolean stopping = false;


	public RegistryServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(timeout);
		serverList = new RegistryList();
		this.port = port;
	}
	
	// INSTANCE METHODS *************************************************

	/**
	 * Begins the thread that waits for new nodes. If the server is already in
	 * listening mode, this call has no effect.
	 *
	 * @exception IOException
	 *                if an I/O error occurs when creating the server socket.
	 */
	final public void listen() throws IOException {
		if (isListening() == false) {
			if (serverSocket == null) {
				serverSocket = new ServerSocket(port, backlog);
			}

			serverSocket.setSoTimeout(timeout);
			stopping = false;
			connectionListener = new Thread(this);
			connectionListener.start();
		}
	}

	/**
	 * Causes the server to stop accepting new connections.
	 */
	final public void stopListening() {
		stopping = true;
	}

	/**
	 * Closes the server socket and the connections with all nodes. Any
	 * exception thrown while closing a node is ignored.
	 *
	 * @exception IOException
	 *                if an I/O error occurs while closing the server socket.
	 */
	final synchronized public void close() throws IOException {
		if (serverSocket == null)
			return;
		stopListening();
		try {
			serverSocket.close();
		} finally {
			serverSocket = null;
			serverStopped();
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * Returns true if the server is ready to accept new nodes.
	 *
	 * @return true if the server is listening.
	 */
	final public boolean isListening() {
		return (connectionListener != null);
	}
	
	/**
	 * Returns the port number.
	 *
	 * @return the port number.
	 */
	final public String getPort() {
		return Integer.toString(port);
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

	// RUN METHOD -------------------------------------------------------

	/**
	 * Runs the listening thread that allows nodes to connect. Not to be
	 * called.
	 */
	final public void run() {
		serverStarted();

		try {
			while (stopping == false) {
				try {
					
					Socket nodeSocket = serverSocket.accept();

					synchronized (this) {
						this.nodeConnected(nodeSocket);
						// Add connection to
					}
				} catch (InterruptedIOException exception) {
					// This will be thrown when a timeout occurs.
					
				}
			}
			serverStopped();
		} catch (IOException exception) {
			if (stopping == false) {
				// Closing the socket must have thrown a SocketException
				listeningException(exception);
			} else {
				serverStopped();
			}
		} finally {
			stopping = true;
			connectionListener = null;
		}
	}
	
	/**
	 * Hook method called each time a new node connection is accepted. The
	 * default implementation does nothing.
	 * 
	 * @param node
	 *            the connection connected to the node.
	 */
	public void nodeConnected(Socket nodeSocket) {
		NodeAddress node = new NodeAddress(nodeSocket.getInetAddress(), nodeSocket.getPort());
		System.out.println("Node connected from: "+node);
		serverList.addToList(node);
	}

	/**
	 * Hook method called each time a node disconnects.
	 *
	 * @param node
	 *            the connection with the node.
	 */
	synchronized public void nodeDisconnected(Socket nodeSocket) {
		NodeAddress node = serverList.getNode(nodeSocket.getInetAddress(), nodeSocket.getPort());
		System.out.println("Node connected from: "+node);
		try {
			serverList.removeFromList(node);
		} catch (Exception e) {}
	}

	/**
	 * Hook method called when the server stops accepting connections because an
	 * exception has been raised. 
	 *
	 * @param exception
	 *            the exception raised.
	 */
	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.exit(1);
	}

	/**
	 * Hook method called when the server starts listening for connections.
	 */
	public void serverStarted() {
		System.out.println("serverStarted :: "+this.getPort());
	}

	/**
	 * Hook method called when the server stops accepting connections. 
	 */
	public void serverStopped() {
		System.out.println("serverStopped :: Exitting.");
	}
	
	public ArrayList<String> getList() {
		return serverList.getList();
	}

	public String getHost() {
		String serverAddress = null;
		try {
			InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		return serverAddress;
	}

}

