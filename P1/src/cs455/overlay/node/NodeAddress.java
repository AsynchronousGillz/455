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
	 * Make the object with a socket, hostName, ipAddress, and port.
	 * 
	 * @param socket Assign the hashCode of the socket to nodeHash 
	 * @param hostName Assign the host name to hostName
	 * @param ipAddress Assign the ip address to ipAddress
	 * @param port Assign the port number to port
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
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + nodeHash;
		result = prime * result + port;
		return result;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof NodeAddress == false)
			return false;
		
		NodeAddress other = (NodeAddress) obj;
		if (this.hostName == null && other.hostName != null) {
				return false;
		} else if (hostName.equals(other.hostName) == false) {
			return false;
		}

		if (this.ipAddress == null && other.ipAddress != null) {
				return false;
		} else if (ipAddress.equals(other.ipAddress) == false) {
			return false;
		}

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
	
	/**
	 * Get the IP Address, and port in String format
	 * @return the "{ip}:{port}" in String format
	 */
	public String getConnection() {
		return this.ipAddress+":"+this.port;
	}
	


	public void clone(NodeAddress node) {
		this.nodeHash = node.getNodeHash();
		this.hostName = node.getHost();
		this.ipAddress = node.getAddress();
		this.port = node.getPort();
	}

}