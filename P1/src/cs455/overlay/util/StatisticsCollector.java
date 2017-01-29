package cs455.overlay.util;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import cs455.overlay.node.NodeAddress;

public class StatisticsCollector {
	/**
	 * The list of node connected to the register.
	 */
	HashMap<NodeAddress, NodeInformation> data;
	
	/**
	 * The number of connections for each node.
	 */
	private int numberOfNodes;
	
	public StatisticsCollector(RegistryList r) {
		this.numberOfNodes = r.getNumberOfConnections();
		this.data = new HashMap<>();
		ArrayList<NodeAddress> info = r.getData();
		for (NodeAddress node: info) {
			this.data.put(node, new NodeInformation());
		}
	}
	
	/**
	 * TODO
	 * @param nodes
	 * @param serverPort
	 */
	public StatisticsCollector(String[] nodes, int serverPort) {
		this.numberOfNodes = nodes.length;
		String ipAddress = null;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		this.data = new HashMap<>();
		for (String node: nodes) {
			String[] temp = node.split(" ");
			int port = Integer.parseInt(temp[2]);
			if (temp[1].equals(ipAddress) == false && port != serverPort) {
				data.put(new NodeAddress(temp[0], temp[1], port), new NodeInformation());
				break;
			}
		}
	}
	// topeka.cs.colostate.edu 129.82.44.174 34257 
	
	/**
	 * Return the number of connections for each node.
	 * @return the number of connections in int format
	 */
	public int getNumberOfConnections() {
		return numberOfNodes;
	}

}
