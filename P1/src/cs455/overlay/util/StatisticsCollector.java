package cs455.overlay.util;

import java.net.*;
import java.util.ArrayList;

import cs455.overlay.node.NodeAddress;

public class StatisticsCollector {
	/**
	 * The list of node connected to the register.
	 */
	ArrayList<NodeAddress> data;
	
	/**
	 * The number of connections for each node.
	 */
	private int numberOfNodes;

	/**
	 * Indication if the overlay has been built and if it is valid.
	 */
	
	public StatisticsCollector(RegistryList r) {
		this.numberOfNodes = r.getNumberOfConnections();
		this.data = r.getData();
	}
	
	public StatisticsCollector(String[] nodes, int serverPort) {
		this.numberOfNodes = nodes.length;
		String ipAddress = null;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		data = new ArrayList<>();
		for (int i = 1; i < this.numberOfNodes; i++) {
			String[] temp = nodes[i].split(":");
			int port = Integer.parseInt(temp[2]);
			if (temp[1].equals(ipAddress) == false && port != serverPort)
				data.add(new NodeAddress(temp[0], temp[1], port));
		}
	}
	
	/**
	 * Return the number of connections for each node.
	 * @return the number of connections in int format
	 */
	public int getNumberOfConnections() {
		return numberOfNodes;
	}

}
