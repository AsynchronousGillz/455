package cs455.overlay.node;

import java.net.*;
import java.util.ArrayList;

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
	
	/**
	 * This is the list of associated nodes that are connected.
	 */
	private ArrayList<NodeAddress> connections;
	/**
	 * 
	 * @param socket
	 * @param inetAddress
	 * @param port
	 */

	public NodeAddress(Socket socket, InetAddress inetAddress, int port) {
		this.nodeHash = socket.hashCode();
		this.inetAddress = inetAddress;
		this.port = port;
		this.connections = new ArrayList<>();
		setNodeAddress();
		setNodeName();
	}
	
	public int getNodeHash() {
		return nodeHash;
	}

	public void setNodeHash(Socket socket) {
		this.nodeHash = socket.hashCode();
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
	
	public void addNodeAddress(NodeAddress node) {
		connections.add(node);
	}

	public void setNodeAddress() {
		try {
			this.nodeAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		
	}

	public ArrayList<NodeAddress> getConnections() {
		return connections;
	}

	public void setConnections(ArrayList<NodeAddress> connections) {
		this.connections = connections;
	}

	public boolean equals(Object o) {
		if (o instanceof NodeAddress == false) {
			return false;
		}
		
		NodeAddress node = (NodeAddress) o;
		
		if (this.getNodeHash() != node.getNodeHash()) {
			return false;
		}
		
		return true;
	}
	
	public int hashCode() {
		return nodeHash + nodeAddress.hashCode() + nodeName.hashCode();
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