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
	 * 			2 for MESSAGING_NODES
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
			case 2:
				this.setType("MESSAGING_NODES");
				break;
		}
		this.setMessage(message);
	}
	
	/**
	 * Used when creating a message from the Overlay.
	 * @param nodes
	 * 			connection information.
	 * @param type
	 * 			0 for MESSAGING_NODES_LIST
	 * 			1 for LINK_WEIGHTS
	 * 			2 for MESSAGING_NODES
	 */
	public Overlay(String[] nodes, int type) {
		super();
		switch(type) {
			case 0: 
				this.setType("MESSAGING_NODES_LIST");
				break;
			case 1:
				this.setType("LINK_WEIGHTS");
				break;
			case 2:
				this.setType("MESSAGING_NODES");
				break;
		}
		convertArray(nodes);
	}
	
	/**
	 * TODO
	 * @param nodes
	 */
	public void convertArray(String[] nodes) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(output);
		try {
			out.writeInt(nodes.length);
			for (String n: nodes) {
				out.writeInt(n.length());
				out.write(n.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		message = output.toByteArray();
	}
	
	/**
	 * TODO
	 */
	public String[] getString() {
		String[] ret = null;
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		try {
			byte[] bytes = null;
			int size = in.readInt();
			ret = new String[size];
			for (int i = 0; i < size; i ++) {
				int length = in.readInt();
				bytes = new byte[length];
				in.read(bytes, 0, length);
				ret[i] = new String(bytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * TODO
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		try {
			byte[] bytes = null;
			int size = in.readInt();
			for (int i = 0; i < size; i ++) {
				int length = in.readInt();
				bytes = new byte[length];
				in.read(bytes, 0, length);
				sb.append(new String(bytes)+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
}
