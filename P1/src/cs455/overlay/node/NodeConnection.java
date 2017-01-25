// File name Node.java
package cs455.overlay.node;

import java.io.*;
import java.net.*;

import cs455.overlay.msg.*;

/**
 * 
 * @author G van Andel
 *
 * @see node.NodeServer
 */

public class NodeConnection extends Thread {
	// INSTANCE VARIABLES ***********************************************

	/**
	 * A reference to the Server that created this instance.
	 */
	private AbstractServer server;

	/**
	 * Sockets are used in the operating system as channels of communication
	 * between two processes.
	 * 
	 * @see java.net.Socket
	 */
	private Socket nodeSocket;

	/**
	 * Stream used to read from the node.
	 * 
	 * @see java.io.DataOutputStream
	 */
	private DataInputStream input;

	/**
	 * Stream used to write to the node.
	 * 
	 * @see java.io.DataOutputStream
	 */
	private DataOutputStream output;

	/**
	 * Indicates if the thread is ready to stop. Set to true when closing of the
	 * connection is initiated.
	 */
	private boolean stopping;

	/**
	 * Set in constructor. Used when sending messages.
	 * 
	 * @see node.NodeAddress
	 */
	private NodeAddress nodeAddress;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs a new connection to a node.
	 * 
	 * @param group
	 *            the thread groupSystem.out.println("node at "+ node +
	 *            "connected"); that contains the connections.
	 * @param nodeSocket
	 *            contains the node's socket.
	 * @param server
	 *            a reference to the server that created this instance
	 * @exception IOException
	 *                if an I/O error oclcur when creating the connection.
	 */
	NodeConnection(ThreadGroup group, Socket nodeSocket, AbstractServer server) throws IOException {
		super(group, (Runnable) null);
		// Initialize variables
		this.nodeSocket = nodeSocket;
		this.server = server;

		nodeSocket.setSoTimeout(0); // make sure timeout is infinite

		// Initialize the objects streams
		try {
			input = new DataInputStream(nodeSocket.getInputStream());
			output = new DataOutputStream(nodeSocket.getOutputStream());
		} catch (IOException ex) {
			close();
			throw ex; // Rethrow the exception.
		}
		String clientIP = nodeSocket.getInetAddress().getHostAddress();
		this.nodeAddress = new NodeAddress(nodeSocket, clientIP, nodeSocket.getPort());
		this.setName(nodeAddress.toString());
		stopping = false;
		start(); // Start the thread waits for data from the socket
	}

	// INSTANCE METHODS *************************************************

	/**
	 * Sends an object to the node.
	 * 
	 * @param msg
	 *            the message to be sent.
	 * @exception IOException
	 *                if an I/O error occur when sending the message.
	 */
	final public void sendToNode(NodeMessage msg) throws IOException {
		if (nodeSocket == null || output == null)
			throw new SocketException("socket does not exist");
		byte[] bytes = msg.makeBytes();
		output.writeInt(bytes.length);
		output.write(bytes, 0, bytes.length);
	}

	/**
	 * Closes the node. If the connection is already closed, this call has no
	 * effect.
	 * 
	 * @exception IOException
	 *                if an error occurs when closing the socket.
	 */
	final public void close() {
		stopping = true; // Set the flag that tells the thread to stop

		try {
			closeAll();
		} catch (IOException ex) {
			System.err.println(ex.toString());
		} finally {
			server.nodeDisconnected(this);
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * Returns the address of the node.
	 * 
	 * @return the node's Internet address.
	 */
	final public InetAddress getInetAddress() {
		return nodeSocket == null ? null : nodeSocket.getInetAddress();
	}

	/**
	 * Returns the socket of the node.
	 * 
	 * @return the node's socket.
	 */
	public Socket getNodeSocket() {
		return nodeSocket;
	}
	/**
	 * Returns a string representation of the node.
	 * 
	 * @return the node's description.
	 */
	public String toString() {
		return nodeAddress.toString();
	}

	// RUN METHOD -------------------------------------------------------

	/**
	 * Constantly reads the node's input stream. Sends all objects that are
	 * read to the server. Not to be called.
	 */
	final public void run() {
		server.nodeConnected(this);

		try {
			int byteSize;

			while (stopping == false) {
				byteSize = input.readInt();
				
				byte[] bytes = new byte[byteSize];
				input.readFully(bytes, 0, byteSize);
				server.receiveMessageFromNode(new NodeMessage(bytes), this);
			}
		} catch (EOFException ex) {
			close();
		} catch (Exception ex) {
			if (stopping == false) {
				close();
				server.nodeException(this, ex);
			}
		}
	}

	/**
	 * Closes all connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occur when closing the connection.
	 */
	private void closeAll() throws IOException {
		try {
			// Close the socket
			if (nodeSocket != null)
				nodeSocket.close();

			// Close the output stream
			if (output != null)
				output.close();

			// Close the input stream
			if (input != null)
				input.close();
		} finally {
			// to reclaim system resources
			output = null;
			input = null;
			nodeSocket = null;
		}
	}

	/**
	 * This method is called by garbage collection.
	 */
	protected void finalize() {
		try {
			closeAll();
		} catch (IOException e) {
		}
	}

	public NodeAddress getAddress() {
		return nodeAddress;
	}

}
// EOF
