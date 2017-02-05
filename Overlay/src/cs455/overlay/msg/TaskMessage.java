package cs455.overlay.msg;

import java.io.*;

public class TaskMessage extends Protocol {
	
	/**
	 * Convert from a {@link Protocol} to a {@link TaskMessage}
	 * @param message
	 */
	public TaskMessage(byte[] message) {
		super();
		this.setMessage(message);
	}
	
	/**
	 * Make the TaskMessage either a TASK_INITIATE or TASK_COMPLETE
	 * @param number
	 * 			The number of rounds.
	 * @param type
	 * 			0 for TASK_INITIATE
	 * 			1 for TASK_COMPLETE
	 */
	public TaskMessage(int number, int type) {
		super();
		switch(type) {
			case 0:
				this.setType("TASK_INITIATE");
				break;
			case 1:
				this.setType("TASK_COMPLETE");
				break;
		}
		this.setNumber(number);
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
		message = output.toByteArray();
	}
	
	/**
	 * Convert the int into the message byte array.
	 */
	public void setNumber(int i) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(output);
		try {
			out.writeInt(i);
			out.writeInt(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		message = output.toByteArray();
	}
	
	/**
	 * Gets the long from the message byte array.
	 * @return long of message
	 */
	public int getNumber() {
		int ret = 0;
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		try {
			ret = in.readInt();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return ret;
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
	
}
