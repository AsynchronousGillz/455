package cs455.overlay.msg;

import java.io.*;

public class TaskMessage extends Protocol {
	
	/**
	 * Convert from a {@link Protocol} to a {@link TaskMessage}
	 * @param message
	 * 			message in bye form
	 */
	public TaskMessage(byte[] message) {
		super();
		this.setType("TASK_MESSAGE");
		super.setMessage(message);
	}

	/**
	 * Make the TaskMessage a TASK_MESSAGE
	 * @param dest
	 * 			string "ip:port"
	 * @param number
	 * 			random number for message
	 */
	public TaskMessage(String dest, int number) {
		super();
		this.setType("TASK_MESSAGE");
		this.makeMessage(dest, number);
	}
	
	/**
	 * Convert the String and int into the message byte array.
	 */
	public void makeMessage(String dest, int i) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(output);
		byte[] bytes = dest.getBytes();
		int len = bytes.length;
		try {
			out.writeInt(i);
			out.writeInt(len);
			out.write(bytes);
		} catch (IOException e){
			System.err.println(e.toString());
		}
		super.setMessage(output.toByteArray());
	}
	
	/**
	 * Gets the String from the message byte array.
	 * @return String of message destination.
	 */
	public String getDest(){
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		byte[] bytes = null;
		try {
			in.readInt();
			int identifierLength = in.readInt();
			bytes = new byte[identifierLength];
			in.readFully(bytes);
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return new String(bytes);
	}

	/**
	 * Gets the int from the message byte array.
	 * @return int of msg payload.
	 */
	public int getNumber(){
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		int ret = 0;
		try {
			ret = in.readInt();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return ret;
	}
	
}
