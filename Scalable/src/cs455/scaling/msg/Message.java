package cs455.scaling.msg;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.*;
import java.util.*;

public class Message {

	private int id;
	private int size = 8192;
	private long time;
	private byte[] message;

	public Message() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		dateFormat.format(date);// 2016/11/16 12:08:43
		time = date.getTime();
		Random rand = new Random();
		message = new byte[size];
		rand.nextBytes(message);
	}
	
	/**
	 * Takes the bytes sent on the socket and assigns values.
	 * 
	 * @param bytes byte array from socket.
	 */
	public Message(byte[] bytes) {
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
		DataInputStream input = new DataInputStream(new BufferedInputStream(byteInputStream));
		try {
			this.id = input.readInt();
			this.time = input.readLong();
			int identifierLength = input.readInt();
			byte[] messageBytes = new byte[identifierLength];
			input.readFully(messageBytes);
			this.message = messageBytes;
			byteInputStream.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//******************************************************************************

	public int getID() {
		return this.id;
	}

	public ByteBuffer getMessage() {
		return ByteBuffer.wrap(message);
	}

	public long getTime() {
		return time;
	}

	public String toString() {
		return "id: "+id;
	}

	//******************************************************************************

	/**
	 * Converts Protocol to byte array to be sent across the socket.
	 * 
	 * @return byte array.
	 * @throws IOException
	 */
	public final byte[] makeBytes() throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(byteOutputStream));
		output.writeInt(id);
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
	
}
