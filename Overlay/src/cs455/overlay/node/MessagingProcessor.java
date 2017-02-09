package cs455.overlay.node;

import cs455.overlay.util.MessagePair;
import cs455.overlay.util.MessageQueue;

/**
 * 
 * @author G van Andel
 *
 */

public class MessagingProcessor extends Thread {
	// INSTANCE VARIABLES ***********************************************

	/**
	 * The server message queue.
	 */
	final private MessageQueue queue;
	
	/**
	 * A reference to the Server that created this instance.
	 */
	final private AbstractServer server;

	
	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs a new connection to a node.
	 * 
	 * @param group
	 *            the thread groupSystem.out.println("node at "+ node +
	 *            "connected"); that contains the connections.
	 * @param queue
	 *            a reference to the queue that the {@link MessagingProcessor} will 
	 *            read messages from.
	 * @param server
	 *            a reference to the server that created this instance
	 */
	public MessagingProcessor(ThreadGroup group, MessageQueue queue, AbstractServer server) {
		super(group, (Runnable) null);
		setName("MP-"+getId());
		this.queue = queue;
		this.server = server;
	}

	// INSTANCE METHODS *************************************************
	
	public void run() {
		while (isInterrupted() == false) {
			MessagePair msg = queue.get();
			server.MessageFromNode(msg.getMsg(), msg.getNode());
		}
	}
	
}