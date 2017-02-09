package cs455.overlay.util;

import cs455.overlay.node.AbstractServer;
import cs455.overlay.util.MessageQueue;

/**
 * 
 * @author G van Andel
 *
 */

public class MessageProcessor extends Thread {
	// INSTANCE VARIABLES ***********************************************

	/**
	 * The server message queue.
	 */
	final private MessageQueue queue;
	
	/**
	 * A reference to the Server that created this instance.
	 */
	private AbstractServer server;

	
	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs a new connection to a node.
	 * 
	 * @param group
	 *            the thread groupSystem.out.println("node at "+ node +
	 *            "connected"); that contains the connections.
	 * @param queue
	 *            a reference to the queue that the {@link MessageProcessor} will 
	 *            read messages from.
	 * @param server
	 *            a reference to the server that created this instance
	 */
	public MessageProcessor(ThreadGroup group, MessageQueue queue, AbstractServer server) {
		super(group, (Runnable) null);
		this.queue = queue;
	}

	// INSTANCE METHODS *************************************************
	
	public void run() {
		while (isInterrupted() == false) {
//			server.processMessage(queue.get());
		}
	}
	
}