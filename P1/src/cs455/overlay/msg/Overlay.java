package cs455.overlay.msg;

import java.io.*;

public class Overlay extends Protocol {
	
	
	//******************************************************************************
	
	/**
	 * Used when creating the message from a Protocol.
	 * @param message
	 * 			connection information in byte form.
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
	 * Used when creating a message from the Overlay.
	 * @param nodes
	 * 			connection information.
	 * @param number
	 * 			number node in array
	 * @param type
	 * 			0 for MESSAGING_NODES_LIST
	 * 			1 for LINK_WEIGHTS
	 */
	public Overlay(byte[][] nodes, int number, int type) {
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
	 * Used when creating a message from the Overlay.
	 * @param nodes
	 * 			connection information.
	 * @param number
	 * 			number node in array
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
			out.writeInt(number);
			out.writeInt(nodes.length);
			for (String node : nodes) {
				out.writeInt(node.length());
				out.write(node.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		message = output.toByteArray();
	}
	
	/**
	 * 
	 * @param nodes
	 * @param number
	 */
	public void convertArray(byte[][] nodes, int number) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(output);
		try {
			out.writeInt(number);
			out.writeInt(nodes.length);
			for (byte[] node : nodes) {
				out.write(node);
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
	public byte[][] convertToArray() {
		byte[][] ret = null;
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		try {
			this.num = in.readInt();
			int length = in.readInt();
			ret = new byte[length][length];
			for (int i = 0; i < length; i++) {
				in.read(ret[i], 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public void displayOverlay() {
		final byte[][] info = convertToArray();
		int length = info.length;
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				ret.append(info[i][j]+" ");
			}
			ret.append("\n");
		}
		System.out.println(ret.toString());
	}
}
