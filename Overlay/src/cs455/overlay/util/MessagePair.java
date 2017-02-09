package cs455.overlay.util;

import cs455.overlay.msg.ProtocolMessage;
import cs455.overlay.node.MessagingConnection;

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
	final private MessagingConnection node;

	/**
	 * 
	 */
	public MessagePair(ProtocolMessage msg, MessagingConnection node) {
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
	public MessagingConnection getNode() {
		return node;
	}
	
}
