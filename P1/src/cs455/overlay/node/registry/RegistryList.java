package cs455.overlay.node.registry;

import java.util.*;
import cs455.overlay.node.*;

/**
 * 
 * @author G van Andel
 *
 */

public class RegistryList {
	
	HashMap<String, NodeAddress> data;
	
	public RegistryList() {
		data = new HashMap<>();
	}

	public ArrayList<String> getList() {
		ArrayList<String> ret = new ArrayList<>(data.size());
		for (NodeAddress node: data.values()) {
			ret.add(node.toString());
		}
		return ret;
	}
	
	public void addToList(NodeAddress node) {
		String key = node.toString();
		data.put(key, node);
	}
	
	public void removeFromList(NodeAddress node) throws Exception {
		NodeAddress temp = data.get(node.toString());
		if (temp == null)
			throw new Exception("Node not found in registry.");
		data.remove(node.toString());
	}

}

