package cs455.overlay.msg;

import java.util.ArrayList;

import cs455.overlay.node.NodeAddress;

public class Overlay extends Protocol {
	
	/**
	 * 
	 * @param message
	 * 			connection information in byte form.
	 * @param number
	 * 			number of links or number of peers.
	 * @param type
	 * 			0 for MESSAGING_NODES_LIST
	 * 			1 for LINK_WEIGHTS
	 */
	public Overlay(byte[] message, int type) {
		super();
		switch(type) {
			case 0: 
				this.setType("MESSAGING_NODES_LIST");
				break;
			case 1:
				this.setType("LINK_WEIGHTS");
				break;
		}
		this.setMessage(message);
	}
	
	/**
	 * 
	 * @param nodes
	 * 			connection information.
	 * @param number
	 * 			number of links or number of peers.
	 * @param type
	 * 			0 for MESSAGING_NODES_LIST
	 * 			1 for LINK_WEIGHTS
	 */
	public Overlay(ArrayList<NodeAddress> nodes, int number, int type) {
		super();
		switch(type) {
			case 0: 
				this.setType("MESSAGING_NODES_LIST");
				break;
			case 1:
				this.setType("LINK_WEIGHTS");
				break;
		}
		this.setMessage(convertArrayList(nodes, number));
	}
	
	public byte[] convertArrayList(ArrayList<NodeAddress> nodes, int number) {
		return null;
	}
	
	public ArrayList<NodeAddress> convertArrayList(byte[] nodes) {
		return null;
	}
}
