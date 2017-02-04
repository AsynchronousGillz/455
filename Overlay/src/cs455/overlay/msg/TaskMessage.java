package cs455.overlay.msg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TaskMessage extends Protocol {
	
	/**
	 * Convert from a {@link Protocol} to a {@link TaskMessage}
	 * @param message
	 */
	public TaskMessage(byte[] message) {
		super();
		switch(super.getStringType()) {
			case "TASK_INITIATE":
			case "TASK_COMPLETE":
				this.setMessage(message);
				break;
			case "TASK_MESSAGE":
				this.setMessage(message);
				break;
		}
	}
	
	/**
	 * Make the TaskMessage either a TASK_INITIATE or TASK_COMPLETE
	 * @param
	 * 			The number of rounds.
	 * @param
	 * 			0 for TASK_INITIATE
	 * 			1 for TASK_COMPLETE
	 */
	public TaskMessage(int number, int type) {
		super();
		switch(type) {
			case 0:
				this.setType("TASK_INITIATE");
				try {
					this.convertNumber(number);
				} catch (IOException e) {
					System.err.println(e.toString());
				}
				break;
			case 1:
				this.setType("TASK_COMPLETE");
				break;
		}
	}
	
	/**
	 * Make the TaskMessage a TASK_MESSAGE
	 * @param number
	 * 			random number for message
	 */
	public TaskMessage(String dest, int number) {
		super();
		this.setType("TASK_MESSAGE");
		try {
			this.makeMessage(dest, number);
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	/**
	 * Convert the String and int into the message byte array.
	 */
	public void makeMessage(String dest, int i) throws IOException {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(byteOutputStream));
		output.writeInt(i);
        byte[] identifierBytes = dest.getBytes();
        int elementLength = identifierBytes.length;
        output.writeInt(elementLength);
        output.write(identifierBytes);
		this.message = byteOutputStream.toByteArray();
		byteOutputStream.close();
		output.close();
	}
	
	/**
	 * Convert the int into the message byte array.
	 */
	public void convertNumber(int i) throws IOException {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(byteOutputStream));
		output.writeInt(i);
		this.message = byteOutputStream.toByteArray();
		byteOutputStream.close();
		output.close();
	}
	
	/**
	 * Gets the String from the message byte array.
	 * @return String of message destination.
	 */
	public String getDest(){
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(message);
		DataInputStream input = new DataInputStream(new BufferedInputStream(byteInputStream));
		byte[] bytes = null;
		try {
			input.readInt();
	        int identifierLength = input.readInt();
	        bytes = new byte[identifierLength];
	        input.readFully(bytes);
			byteInputStream.close();
			input.close();
		} catch (IOException e) {
			System.out.println();
		}
		return new String(bytes);
	}
	
	/**
	 * Gets the long from the message byte array.
	 * @return long of message
	 */
	public int getNumber() {
		int ret = 0;
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(message);
		DataInputStream input = new DataInputStream(new BufferedInputStream(byteInputStream));
		try {
			ret = input.readInt();
			byteInputStream.close();
			input.close();
		} catch (IOException e) {
			System.out.println();
		}
		return ret;
	}
	
}
