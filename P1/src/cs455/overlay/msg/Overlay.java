package cs455.overlay.msg;

import java.io.*;

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
	public Overlay(String[] nodes, int number, int type) {
		super();
		switch(type) {
			case 0: 
				this.setType("MESSAGING_NODES_LIST");
				break;
			case 1:
				this.setType("LINK_WEIGHTS");
				break;
		}
		convertArray(nodes, number);
	}
	
	/**
	 * 
	 * @param nodes
	 * @param number
	 */
	public void convertArray(String[] nodes, int number) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(output);
		try {
			out.writeInt(nodes.length);
			for (String node : nodes) {
				out.writeUTF(node);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		message = output.toByteArray();
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] convertToArray() {
		String[] ret = null;
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		try {
			int length = in.readInt();
			ret = new String[length];
			for (int i = 0; i < length; i++) {
				ret[i] = in.readUTF();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
