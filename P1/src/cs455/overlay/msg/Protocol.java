package cs455.overlay.msg;

import java.io.*;
import java.text.*;
import java.util.*;

public class Protocol {
	
	
	protected int type;
	protected long time;
	protected byte indicator;
	protected byte[] message;

	protected final String[] Types = { 
            "REGISTER_REQUEST",
            "REGISTER_RESPONSE",
            "DEREGISTER_REQUEST",
            "MESSAGING_NODES_LIST",
            "LINK_WEIGHTS",
            "SINGLE_WEIGHT",
            "TASK_INITIATE",
            "TASK_MESSAGE",
            "TASK_COMPLETE",
            "PULL_TRAFFIC_SUMMARY",
            "TRAFFIC_SUMMARY"
	};
	
	public Protocol() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		dateFormat.format(date);// 2016/11/16 12:08:43
		time = date.getTime();
	}
	
	public Protocol(byte[] bytes) {
		try {
			makeObject(bytes);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	//******************************************************************************
	
	public int getType() {
		return type;
	}
	
	public void setIndicator(byte i) {
		this.indicator = i;
	}
	
	public byte getIndicator() {
		return indicator;
	}
	
	public String getStringType() {
		return Types[type];
	}

	public void setType(String type) throws IllegalArgumentException {
		int index = -1;
		for (int i = 0; i < Types.length; i++) {
			if (Types[i].equals(type)) {
				index = i;
				break;
			}
		}
		if (index == -1)
			throw new IllegalArgumentException("Invalid type of Message.");
		this.type = index;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}
	
	public long getTime() {
		return time;
	}

	public String toString() {
		return "id: "+type+", type: " + Types[type];
	}

	//******************************************************************************
	
	public Registation convertToRegistation() {
		String msg = new String(message);
		Registation ret = new Registation(msg, type);
		return ret;
	}
	
	public Overlay convertToOverlay() {
		Overlay ret = new Overlay(message, type);
		return ret;
	}
	
	//******************************************************************************

	/**
	 * Converts Protocol to byte array to be sent across the socket.
	 * @return byte array.
	 * @throws IOException
	 */
	public final byte[] makeBytes() throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(byteOutputStream));
		output.writeInt(type);
		output.writeLong(time);
		byte[] messageBytes = message;
		int elementLength = messageBytes.length;
		output.writeInt(elementLength);
		output.write(messageBytes);
		output.flush();
		bytes = byteOutputStream.toByteArray();
		byteOutputStream.close();
		output.close();
		return bytes;
	}

	/**
	 * Takes the bytes sent on the socket and assigns values.
	 * @param bytes byte array from socket.
	 * @throws IOException
	 */
	public final void makeObject(byte[] bytes) throws IOException {
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
		DataInputStream input = new DataInputStream(new BufferedInputStream(byteInputStream));
		this.type = input.readInt();
		this.time = input.readLong();
		int identifierLength = input.readInt();
		byte[] messageBytes = new byte[identifierLength];
		input.readFully(messageBytes);
		this.message = messageBytes;
		byteInputStream.close();
		input.close();
	}

}
