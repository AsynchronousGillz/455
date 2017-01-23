package cs455.overlay.node;

import java.net.*;

/**
 * 
 * @author G van Andel
 * 
 * @see node.NodeConnection
 *
 */


public class NodeAddress {

	/**
	 * This is to make nodes easily identifiable. 
	 * This is set to null when first created.
	 */
	private int nodeHash;
	
	/**
	 * This class represents an Internet Protocol (IP) address.
	 * 
	 * @see java.net.InetAddress
	 */
	private String ipAddress;
	
	/**
	 * This is to make nodes port information accessible.
	 */
	private int port;
	
	/**
	 * 
	 * @param socket
	 * @param inetAddress
	 * @param port
	 */

	public NodeAddress(Socket socket, String ipAddress, int port) {
		this.nodeHash = socket.hashCode();
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public int getNodeHash() {
		return nodeHash;
	}

	public String getAddress() {
		return ipAddress;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + nodeHash;
		result = prime * result + port;
		return result;
	}

	public boolean equals(NodeAddress other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (nodeHash != other.nodeHash)
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	public String toString() {
		return this.ipAddress+" "+this.port;
	}

	public void clone(NodeAddress node) {
		this.nodeHash = node.getNodeHash();
		this.ipAddress = node.getAddress();
		this.port = node.getPort();
	}
}