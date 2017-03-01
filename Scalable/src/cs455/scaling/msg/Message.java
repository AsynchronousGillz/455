package cs455.scaling.msg;

import java.nio.*;
import java.util.*;

public final class Message {

	final static public int size = 8192;

	public static ByteBuffer makeMessage() {
		Random rand = new Random();
		byte[] bytes = new byte[size];
		rand.nextBytes(bytes);
		return ByteBuffer.wrap(bytes);
	}

}