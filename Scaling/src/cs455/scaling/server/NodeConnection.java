// File name Node.java
package cs455.scaling.server;

import java.io.*;
import java.net.*;

import cs455.scaling.msg.ProtocolMessage;

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
	private int cost;
	
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
	private boolean complete;
	
	/**
	 * Debug this.
	 */
	private final boolean debug = false;

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
		this.ipAddress = getName();
		this.hostName = null;
		this.cost = this.port = 0;
		this.complete = false;
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
	final public void sendToNode(ProtocolMessage msg) throws IOException {
		if (nodeSocket == null || output == null)
			throw new SocketException("socket does not exist");
		if (debug)
			System.out.println(msg.toString());
		byte[] bytes = msg.makeBytes();
		synchronized (output) {
			output.writeInt(bytes.length);
			output.write(bytes, 0, bytes.length);
		}
	}

	/**
	 * Closes the node. If the connection is already closed, this call has no
	 * effect.
	 */
	final public void close() {
		complete = true; // Set the flag that tells the thread to stop
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
	 * Returns a string representation of the node. If serverPort 
	 * has been sent it will be "ip:port" else it will just be the "ip"
	 * 
	 * @return the node's description.
	 */
	public String toString() {
		if (this.ipAddress != null && port != 0)
			return this.ipAddress+":"+this.port;
		else
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
	 * Set the weight of the connection.
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * Sets the job to state to true;
	 */
	public synchronized void resetComplete() {
		this.complete = false;
	}
	
	/**
	 * Sets the job to state to true;
	 */
	public synchronized void setComplete() {
		this.complete = true;
	}
	
	/**
	 * Get the state of the job.
	 */
	public boolean getComplete() {
		return this.complete;
	}
	
	/**
	 * Returns the NodeAddress contained within the NodeConnection
	 * 
	 * @return the weight of the connection.
	 */
	public int getCost() {
		return this.cost;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeConnection other = (NodeConnection) obj;
		if (hostName == null && other.hostName != null)
			return false;
		else if (hostName.equals(other.hostName) == false)
			return false;
		if (ipAddress == null && other.ipAddress != null)
			return false;
		else if (ipAddress.equals(other.ipAddress) == false)
			return false;
		if (port != other.port)
			return false;
		return true;
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
			while (super.isInterrupted() == false) {
				byteSize = input.readInt();
				byte[] bytes = new byte[byteSize];
				input.readFully(bytes, 0, byteSize);
				server.MessageFromNode(new ProtocolMessage(bytes), this);
			}
		} catch (EOFException ex) {
			close();
		} catch (Exception ex) {
			if (complete == false) {
				close();
				server.nodeException(this, ex);
			}
		}
	}
	
	// CLOSE METHOD -------------------------------------------------------


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
