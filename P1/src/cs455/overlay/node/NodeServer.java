// File name Server.java
package cs455.overlay.node;

import java.net.*;
import java.io.*;

/**
 * 
 * @author G van Andel
 *
 * @see MessagingNode.NodeMain
 */

public class NodeServer implements Runnable {
	// INSTANCE VARIABLES *********************************************

	/**
	 * The server socket: listens for nodes who want to connect.
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
	 * The maximum queue length; i.e. the maximum number of nodes that can be
	 * waiting to connect. Set to 10 by default.
	 */
	private int backlog = 10;

	/**
	 * java.lang.ThreadGroup
	 * The thread group associated with node threads. Each member of the
	 * thread group is a <code> ConnectionTonode </code>.
	 */
	private ThreadGroup nodeThreadGroup;

	/**
	 * Indicates if the listening thread is ready to stop. Set to false by
	 * default.
	 */
	private boolean stopping = false;

	// CONSTRUCTOR ******************************************************

	/**
	 * Constructs a new server.
	 *
	 * @param port
	 *            the port number on which to listen.
	 */
	public NodeServer(int port) {
		this.port = port;

		this.nodeThreadGroup = new ThreadGroup("ConnectionTonode threads") {
			// All uncaught exceptions in connection threads will
			// be sent to the nodeException callback method.
			public void uncaughtException(Thread thread, Throwable exception) {
				nodeException((NodeConnection) thread, exception);
			}
		};
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
				serverSocket = new ServerSocket(getPort(), backlog);
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
			// Close the node sockets of the already connected nodes
			Thread[] nodeThreadList = getNodeConnections();
			for (int i = 0; i < nodeThreadList.length; i++) {
				try {
					((NodeConnection) nodeThreadList[i]).close();
				}
				// Ignore all exceptions when closing nodes.
				catch (Exception ex) {
				}
			}
			serverSocket = null;
			serverClosed();
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
	 * Returns an array containing the existing node connections. This can be
	 * used by concrete subclasses to implement messages that do something with
	 * each connection (e.g. kill it, send a message to it etc.). Remember that
	 * after this array is obtained, some nodes in this migth disconnect. New
	 * nodes can also connect, these later will not appear in the array.
	 *
	 * @return an array of <code>Thread</code> containing
	 *         <code>ConnectionTonode</code> instances.
	 */
	synchronized final public Thread[] getNodeConnections() {
		Thread[] nodeThreadList = new Thread[nodeThreadGroup.activeCount()];

		nodeThreadGroup.enumerate(nodeThreadList);

		return nodeThreadList;
	}

	/**
	 * Counts the number of nodes currently connected.
	 *
	 * @return the number of nodes currently connected.
	 */
	final public int getNumberOfnodes() {
		return nodeThreadGroup.activeCount();
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
						new NodeConnection(this.nodeThreadGroup, nodeSocket, this);
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
	public void nodeConnected(NodeConnection node) {
		System.out.println("nodeConnected :: Not implemented.");
	}

	/**
	 * Hook method called each time a node disconnects. The default
	 * implementation does nothing. The method may be overridden by subclasses
	 * but should remains synchronized.
	 *
	 * @param node
	 *            the connection with the node.
	 */
	synchronized public void nodeDisconnected(NodeConnection node) {
		System.out.println("nodeDisconnected :: Not implemented.");
	}

	/**
	 * Hook method called each time an exception is thrown in a ConnectionTonode thread.
	 *
	 * @param node
	 *            the node that raised the exception.
	 * @param Throwable
	 *            the exception thrown.
	 */
	synchronized public void nodeException(NodeConnection node, Throwable exception) {
		System.out.println("nodeException :: Not implemented.");
	}

	/**
	 * Hook method called when the server stops accepting connections because an
	 * exception has been raised. 
	 *
	 * @param exception
	 *            the exception raised.
	 */
	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: Not implemented.");
	}

	/**
	 * Hook method called when the server starts listening for connections.
	 */
	public void serverStarted() {
		System.out.println("serverStarted :: Not implemented.");
	}

	/**
	 * Hook method called when the server stops accepting connections. 
	 */
	public void serverStopped() {
		System.out.println("serverStopped :: Not implemented.");
	}

	/**
	 * When the server is closed while still listening, serverStopped() 
	 * will also be called.
	 */
	public void serverClosed() {
		System.out.println("serverClosed :: Not implemented.");
	}

	/**
	 * Handles a command sent from one node to the server. This MUST be
	 * implemented by subclasses, who should respond to messages. This method is
	 * called by a synchronized method so it is also implicitly synchronized.
	 *
	 * @param msg
	 *            the message sent.
	 * @param node
	 *            the connection connected to the node that sent the message.
	 */
	public synchronized void receiveMessageFromNode(Object msg, NodeConnection node) {

	}

}
// End of AbstractServer Class
