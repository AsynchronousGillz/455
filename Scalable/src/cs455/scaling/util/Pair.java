package cs455.scaling.util;

import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;

/**
 *
 * @author G van Andel
 *
 */

public class Pair {

	/**
	 * 
	 */
	final private Message msg;
	
	/**
	 * 
	 */
	final private SocketChannel channel;

	/**
	 * 
	 */
	public Pair(Message msg, SocketChannel channel) {
		this.msg = msg;
		this.channel = channel;
	}

	/**
	 * 
	 */
	public Message getMsg() {
		return msg;
	}

	/**
	 * 
	 */
	public SocketChannel getChannel() {
		return channel;
	}

	@Override
	public String toString() {
		return "MessagePair [msg=" + msg + ", channel=" + channel + "]";
	}
	
	
}
