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
		for (int i = 1; i < this.numberOfNodes; i++) {
			String[] temp = nodes[i].split(":");
			int port = Integer.parseInt(temp[2]);
			if (temp[1].equals(ipAddress) == false && port != serverPort) {
				String[] connections = nodes[i].split(" ");
				for (String node: connections) {
					temp = node.split(":");
					data.put(new NodeAddress(temp[0], temp[1], port), new NodeInformation());
				}
				break;
			}
		}
	}
	// Host:ServerPort ip:port:wieght 
	// 127.0.0.0:40000 127.0.0.1:40001:4  127.0.0.2:40002:4  127.0.0.3:40003:9  127.0.0.9:40009:4
	
	/**
	 * Return the number of connections for each node.
	 * @return the number of connections in int format
	 */
	public int getNumberOfConnections() {
		return numberOfNodes;
	}

}
