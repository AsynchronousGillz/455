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
	private String nodeName;
	
	/**
	 * This class represents an Internet Protocol (IP) address.
	 * 
	 * @see java.net.InetAddress
	 */
	private InetAddress inetAddress;
	
	/**
	 * This class represents an Internet Protocol (IP) address.
	 * 
	 * @see java.net.InetAddress
	 */
	private String nodeAddress;

	/**
	 * This is to make nodes port information accessible.
	 */
	private int port;

	public NodeAddress(InetAddress inetAddress, int port) {
		this.inetAddress = inetAddress;
		this.port = port;
		setNodeAddress();
		setNodeName();
	}
	
	public void setNodeName() {
		String[] oc = this.nodeAddress.split("\\.");
		this.nodeName = oc[2]+"."+oc[3]+":"+this.port;
	}

	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	public InetAddress getInetAddress() {
		return inetAddress;
	}

	public int getPort() {
		return port;
	}

	public String getNodeName() {
		return nodeName;
	}
	
	public String getNodeAddress() {
		return nodeAddress;
	}

	public void setNodeAddress() {
		try {
			this.nodeAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		
	}


	public boolean equals(Object o) {
		if (o instanceof NodeAddress == false) {
			return false;
		}
		
		NodeAddress nodeAddress = (NodeAddress) o;
		int hostName = inetAddress.getHostName().compareTo(nodeAddress.getInetAddress().getHostName());
		int hostAddress = inetAddress.getHostAddress().compareTo(nodeAddress.getInetAddress().getHostAddress());

		int hostCheck = (hostName == 0 || hostAddress == 0)?0:1;
		
		if (port != nodeAddress.getPort() || hostCheck != 0) {
			return false;
		}
		
		return true;
	}

	public String toString() {
		return this.nodeAddress+" "+this.port;
	}

	public void clone(NodeAddress node) {
		this.nodeName = node.getNodeName();
		this.inetAddress = node.getInetAddress();
		this.port = node.getPort();
	}
}