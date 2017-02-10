package cs455.overlay.node;

import java.io.IOException;

import cs455.overlay.util.MessagePair;
import cs455.overlay.util.MessageQueue;

/**
 * 
 * @author G van Andel
 *
 */

public class MessagingSender extends Thread {
	// INSTANCE VARIABLES ***********************************************

	/**
	 * The server message queue.
	 */
	final private MessageQueue queue;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs a new connection to a node.
	 * 
	 * @param group
	 *            the thread groupSystem.out.println("node at "+ node +
	 *            "connected"); that contains the connections.
	 * @param queue
	 *            a reference to the queue that the {@link MessagingProcessor}
	 *            will read messages from.
	 * @param server
	 *            a reference to the server that created this instance
	 */
	public MessagingSender(ThreadGroup group, MessageQueue queue) {
		super(group, (Runnable) null);
		setName("MS-" + getId());
		this.queue = queue;
	}

	// INSTANCE METHODS *************************************************

	public void run() {
		while (isInterrupted() == false) {
			MessagePair msg = queue.get();
			try {
				msg.getNode().sendToNode(msg.getMsg());
			} catch (IOException e) {}
		}
	}

}
