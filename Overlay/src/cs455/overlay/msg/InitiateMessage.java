package cs455.overlay.msg;

import java.io.*;

public class InitiateMessage extends ProtocolMessage {
	
	/**
	 * Convert from a {@link ProtocolMessage} to a {@link InitiateMessage}
	 * @param message
	 * 			message in bye form
	 */
	public InitiateMessage(byte[] message) {
		super();
		this.setType("TASK_INITIATE");
		this.setMessage(message);
	}
	
	/**
	 * Make the TaskInitiate [ TASK_INITIATE ]
	 * @param number
	 * 			The number of rounds.
	 */
	public InitiateMessage(int number) {
		super();
		this.setType("TASK_INITIATE");
		this.setNumber(number);
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
			output.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		message = output.toByteArray();
	}
	
	/**
	 * Gets the int from the message byte array.
	 * @return int of message
	 */
	public int getNumber() {
		int ret = 0;
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		try {
			ret = in.readInt();
			input.close();
			in.close();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return ret;
	}
	

}
