// File name Node.java
package cs455.overlay.node;

import java.io.*;
import java.net.*;

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
			try {
				closeAll();
			} catch (Exception exc) {}

			throw ex; // Rethrow the exception.
		}
		
		this.nodeAddress = new NodeAddress(this.getInetAddress(), nodeSocket.getPort());
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
	final public void sendToNode(Object msg) throws IOException {
		if (nodeSocket == null || output == null)
			throw new SocketException("socket does not exist");
		byte[] bytes = convertToBytes(msg);
		output.write(bytes);
	}

	/**
	 * Closes the node. If the connection is already closed, this call has no
	 * effect.
	 * 
	 * @exception IOException
	 *                if an error occurs when closing the socket.
	 */
	final public void close() throws IOException {
		stopping = true; // Set the flag that tells the thread to stop

		try {
			closeAll();
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
	 * Returns a string representation of the node.
	 * 
	 * @return the node's description.
	 */
	public String toString() {
		if (nodeAddress.getNodeName() != null)
			return nodeAddress.getNodeName();
		if (nodeSocket != null)
			return nodeSocket.getInetAddress().getHostName();
		return null;
	}

	// RUN METHOD -------------------------------------------------------

	/**
	 * Constantly reads the node's input stream. Sends all objects that are
	 * read to the server. Not to be called.
	 */
	final public void run() {
		server.nodeConnected(this);

		try {
			byte[] bytes = null;
			Object msg = null;

			while (stopping == false) {
				input.read(bytes); /// DEBUG error
				msg = convertBytes(bytes);
				server.receiveMessageFromNode(msg, this);
			}
		} catch (Exception exception) {
			if (stopping == false) {
				try {
					closeAll();
				} catch (Exception ex) {
				}

				server.nodeException(this, exception);
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
	
	public byte[] convertToBytes(Object o) throws IOException {
		byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
	}
	
	public Object convertBytes(byte[] bytes) throws IOException {
		Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (ClassNotFoundException e) {
        	System.err.println("Error: Class not found");
        }finally {
            if (bais != null) {
                bais.close();
            }
            if (ois != null) {
                ois.close();
            }
        } 
        return obj;
	}
}
// EOF