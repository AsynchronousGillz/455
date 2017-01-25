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
	 * 
	 * @param socket
	 * @param inetAddress
	 * @param port
	 */

	public NodeAddress(Socket socket, String hostName, String ipAddress, int port) {
		this.nodeHash = socket.hashCode();
		this.hostName = hostName;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	/**
	 * Returns the hashCode of the socket.
	 * @return the socket hashCode in int format
	 */
	public int getNodeHash() {
		return nodeHash;
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
		System.out.println(this+" - "+other); //DEBUG
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
	
	/**
	 * Get just IP Address
	 * @return the "{ip}" in String format
	 */
	public String toString() {
		return this.ipAddress;
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


	public void clone(NodeAddress node) {
		this.nodeHash = node.getNodeHash();
		this.hostName = node.getHost();
		this.ipAddress = node.getAddress();
		this.port = node.getPort();
	}
}