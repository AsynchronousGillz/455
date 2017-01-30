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
	 * This String represents an Internet Protocol (IP) address.
	 * 
	 * @see java.net.InetAddress
	 */
	private String ipAddress;
	
	/**
	 * This String represents the host name.
	 * 
	 * @see java.net.InetAddress
	 */
	private String hostName;
	
	/**
	 * This is to make nodes port information accessible.
	 */
	private int port;
	
	/**
	 * This is to make nodes port information accessible.
	 */
	private int wieght;
	
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
	public NodeConnection(ThreadGroup group, Socket nodeSocket, AbstractServer server) throws IOException {
		super(group, (Runnable) null);
		// Initialize variables
		this.nodeSocket = nodeSocket;
		this.server = server;

		nodeSocket.setSoTimeout(0); // make sure timeout is infinite

		// Initialize the objects streams
		try {
			this.input = new DataInputStream(nodeSocket.getInputStream());
			this.output = new DataOutputStream(nodeSocket.getOutputStream());
		} catch (IOException ex) {
			close();
			throw ex; // Rethrow the exception.
		}
		setName(nodeSocket.getInetAddress().getHostAddress());
		this.stopping = false;
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
	final public void sendToNode(Protocol msg) throws IOException {
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
	 * Returns a string host name.
	 * @param ip of target machine.
	 * @return machine host name.
	 */
	public String getTargetHostName(String targetIP) {
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
	 * Returns a string representation of the node.
	 * 
	 * @return the node's description.
	 */
	public String toString() {
		return getName();
	}
	
	/**
	 * Add info with a hostName, ipAddress, and port.
	 * 
	 * @param hostName Assign the host name to hostName
	 * @param ipAddress Assign the ip address to ipAddress
	 * @param port Assign the port number to port
	 */

	public void setClientInfo(String hostName, String ipAddress, int port) {
		this.hostName = hostName;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	/**
	 * Set the weight of the NodeAddress contained within the NodeConnection
	 */
	public void setWeight(int wieght) {
		this.wieght = wieght;
	}
	
	/**
	 * Returns the NodeAddress contained within the NodeConnection
	 * 
	 * @return the weight of the connection.
	 */
	public int getWeight() {
		return this.wieght;
	}
	
	/**
	 * Returns the IP Address of the node in String format
	 * @return "{ip}" in string format
	 */
	public String getAddress() {
		return ipAddress;
	}

	/**
	 * Returns the host name of the node in String format
	 * @return "{host name}" in string format
	 */
	public String getHost() {
		return hostName;
	}
	
	/**
	 * Returns the port number of the node in int format
	 * @return the port number in int format
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Get the IP Address, and port in String format
	 * @return the "{ip} {port}" in String format
	 */
	public String getRegistryInfo() {
		return this.ipAddress+" "+this.port;
	}
	
	/**
	 * Get the Host name, IP Address, and port in String format
	 * @return the "{host name} {ip} {port}" in String format
	 */
	public String getInfo() {
		return this.hostName+" "+this.ipAddress+" "+this.port;
	}
	
	/**
	 * Get the IP Address, and port in String format
	 * @return the "{ip}:{port}" in String format
	 */
	public String getConnection() {
		return this.ipAddress+":"+this.port;
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
				server.receiveMessageFromNode(new Protocol(bytes), this);
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

}
// EOF
