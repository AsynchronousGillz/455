// File name Message.java
package cs455.overlay.msg;

import java.io.*;
import java.text.*;
import java.util.*;

public class NodeMessage {

	private int type;
	private long time;
	private String message;

	
	private final String[] Types = { 
            "REGISTER_REQUEST",
            "REGISTER_RESPONSE",
            "DEREGISTER_REQUEST",
            "MESSAGING_NODES_LIST",
            "LINK_WEIGHTS",
            "TASK_INITIATE",
            "TASK_COMPLETE",
            "PULL_TRAFFIC_SUMMARY",
            "TRAFFIC_SUMMARY"
		};
	
	public NodeMessage(byte[] bytes) {
		try {
			makeObject(bytes);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	public NodeMessage(String message) {
		this.message = message;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		dateFormat.format(date);// 2016/11/16 12:08:43
		time = date.getTime();
	}
	
	public int getType() {
		return type;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String toString() {
		return "{ \"id\" : \""+type+"\", \"type\" : \"" + Types[type] + "\", \"message\" : \"" + message + "\" }";
	}
	
	public final byte[] makeBytes() throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(byteOutputStream));
		output.writeInt(type);
		output.writeLong(time);
		byte[] identifierBytes = message.getBytes();
		int elementLength = identifierBytes.length;
		output.writeInt(elementLength);
		output.write(identifierBytes);
		output.flush();
		bytes = byteOutputStream.toByteArray();
		byteOutputStream.close();
		output.close();
		return bytes;
	}

	public final void makeObject(byte[] bytes) throws IOException {
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
		DataInputStream input = new DataInputStream(new BufferedInputStream(byteInputStream));
		type = input.readInt();
		time = input.readLong();
		int identifierLength = input.readInt();
		byte[] identifierBytes = new byte[identifierLength];
		input.readFully(identifierBytes);
		message = new String(identifierBytes);
		byteInputStream.close();
		input.close();
	}

}
