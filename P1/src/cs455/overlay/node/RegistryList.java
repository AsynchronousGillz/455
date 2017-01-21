package cs455.overlay.node;

import java.net.InetAddress;
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
			ret += node.toString();
		}
		return ret;
	}
	
	public void addToList(NodeAddress node) {
		data.add(node);
	}
	
	public void removeFromList(NodeAddress node) throws Exception {
		int index = data.indexOf(node);
		if (index == -1)
			throw new Exception("Node not found in registry.");
		data.remove(node.toString());
	}

	public NodeAddress getNode(InetAddress inetAddress, int port) {
		for (NodeAddress node: data) {
			if (node.getInetAddress().equals(inetAddress) && node.getPort() == port)
				return node;
		}
		return null;
	}

}

