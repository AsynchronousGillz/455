package cs455.overlay.node;

import java.net.*;
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
	
	public synchronized void removeFromList(NodeAddress node) throws Exception {
		if(data.contains(node) == false)
			throw new Exception("Node not found in registry.");
		data.remove(node);
	}

	public synchronized NodeAddress getNode(InetAddress inetAddress, int port) {
		for (NodeAddress node: data) {
			if (node.getInetAddress().equals(inetAddress) && node.getPort() == port)
				return node;
		}
		return null;
	}

}

