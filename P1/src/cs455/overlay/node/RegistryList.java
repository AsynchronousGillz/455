package cs455.overlay.node;

import java.util.*;

/**
 * 
 * @author G van Andel
 *
 */

public class RegistryList {
	
	/**
	 * The list of node connected to the register.
	 */
	ArrayList<NodeAddress> data;
	
	/**
	 * The number of connections for each node.
	 */
	private int numberOfConnections;

	/**
	 * The list of connections to other nodes
	 */
	ArrayList<NodeAddress[]> overlay;
	
	public RegistryList(int numberOfConnections) {
		this.numberOfConnections = numberOfConnections;
		data = new ArrayList<>();
	}
	
	/**
	 * Return the number of connections for each node.
	 * @return the number of connections in int format
	 */
	public int getNumberOfConnections() {
		return numberOfConnections;
	}

	/**
	 * Set the number of connections for each node.
	 * @param numberOfConnections
	 */
	public void setNumberOfConnections(int numberOfConnections) {
		this.numberOfConnections = numberOfConnections;
	}

	public String getList() {
		if (data.size() == 0) 
			return "Node list is currently empty.";
		String ret = "";
		for (NodeAddress node: data) {
			ret += node.getInfo() + "\n";
		}
		return ret;
	}
	
	public synchronized void addToList(NodeAddress node) {
		data.add(node);
	}
	
	public synchronized void removeFromList(NodeAddress node) {
		data.remove(node);
	}

	public synchronized NodeAddress getNode(String ipAddress, int port) {
		for (NodeAddress node: data) {
			if (node.getAddress().equals(ipAddress) && node.getPort() == port)
				return node;
		}
		return null;
	}
	
	public synchronized void buildOverlay() {
		overlay = new ArrayList<>();
		for(int nodeNumber = 0; nodeNumber < data.size(); nodeNumber++) {
			int[] randomList = new int[numberOfConnections];
			for (int i = 0; i < numberOfConnections; i++) {
				// get random numbers
			}
		}
	}

}

