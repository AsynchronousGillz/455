package cs455.scaling.util;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * 
 * @author G van Andel
 *
 */

public final class Processor extends Thread {
	// INSTANCE VARIABLES ***********************************************

	/**
	 * TODO
	 */
	private Queue queue;
	
	/**
	 * 
	 */
	private boolean debug = false;
	
	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs a new connection to a node.
	 * 
	 * @param group
	 *            the thread groupSystem.out.println("node at "+ node +
	 *            "connected"); that contains the connections.
	 * @param queue
	 *            a reference to the queue that the {@link Processor} will 
	 *            read messages from.
	 * @param server
	 *            a reference to the server that created this instance
	 */
	public Processor(ThreadGroup group) {
		super(group, (Runnable) null);
		setName("Processor-"+getId());
		this.queue = new Queue(10);
	}

	// INSTANCE METHODS *************************************************
	
	public void processMessage(Pair msg) {
		if (debug)
			System.out.println(msg);
		try {
			SocketChannel chan = msg.getChannel();
			synchronized (chan) {
				chan.write(msg.getMsg().getMessage());	
			}
		} catch (IOException e) {
			System.err.println("An error occured while sending.");
			e.printStackTrace();
		}
	}
	
	public void addMessage(Pair msg) throws InterruptedException {
		this.queue.enqueue(msg);
	}
	
	// RUN METHODS ******************************************************

	public void run() {
		while (isInterrupted() == false) {
			try {
				this.processMessage(this.queue.dequeue());
			} catch(InterruptedException e) {
				// Stopped by wait()
			}
		}
	}
	
}