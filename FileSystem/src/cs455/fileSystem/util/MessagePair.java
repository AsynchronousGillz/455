package cs455.fileSystem.util;

import cs455.fileSystem.msg.ProtocolMessage;
import cs455.fileSystem.chunk.Connection;

/**
 *
 * @author G van Andel
 *
 */

public class MessagePair {

	/**
	 * 
	 */
	final private ProtocolMessage msg;
	
	/**
	 * 
	 */
	final private Connection node;

	/**
	 * 
	 */
	public MessagePair(ProtocolMessage msg, Connection node) {
		this.msg = msg;
		this.node = node;
	}

	/**
	 * 
	 */
	public ProtocolMessage getMsg() {
		return msg;
	}

	/**
	 * 
	 */
	public Connection getNode() {
		return node;
	}

	@Override
	public String toString() {
		return "MessagePair [msg=" + msg + ", node=" + node + "]";
	}
	
	
}
