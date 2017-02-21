package cs455.scaling.msg;

import java.math.*;
import java.nio.*;
import java.security.*;
import java.util.*;

public final class Message {

	private int size = 8192;
	private ByteBuffer message;

	public Message() {
		Random rand = new Random();
		byte[] bytes = new byte[size];
		rand.nextBytes(bytes);
		this.message = ByteBuffer.wrap(bytes);
	}

	public Message(ByteBuffer bytes) {
		this.message = bytes;
	}

	public ByteBuffer getMessage() {
		return message;
	}

	public byte[] getBytes() {
		return message.array();
	}

	public String toString() {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {}
		byte[] hash = digest.digest(message.array());
		BigInteger hashInt = new BigInteger(1, hash);
		return hashInt.toString(16);
	}

}
