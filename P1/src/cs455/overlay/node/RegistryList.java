package cs455.overlay.node;

import java.util.*;

/**
 * 
 * @author G van Andel
 *
 */

public class RegistryList {
	
	ArrayList<NodeAddress> data;
	
	public RegistryList() {
		data = new ArrayList<>();
	}

	public String getList() {
		if (data.size() == 0) 
			return "Node list is currently empty.";
		String ret = "";
		for (NodeAddress node: data) {
			ret += node.toString() + "\n";
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

}

