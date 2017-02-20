package cs455.scaling.util;

import java.io.IOException;
import java.nio.ByteBuffer;
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
	private Pair msg;
	
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
		this.msg = null;
	}

	// INSTANCE METHODS *************************************************
	
	public void processMessage(Pair msg) {
		System.out.println(msg);
		ByteBuffer buf = msg.getMsg().getMessage();
		try {
			SocketChannel chan = msg.getChannel();
			synchronized (chan) {
				chan.write(buf);	
			}
		} catch (IOException e) {
			System.err.println("An error occured while sending.");
			e.printStackTrace();
		}
		this.msg = null;
	}
	
	public void addMessage(Pair msg) throws Exception {
		if (this.msg != null)
			throw new Exception("Error: Tried to over write Message.");
		this.msg = msg;
		notifyAll();
	}
	
	public boolean checkMessage() {
		if (this.msg != null)
			return true;
		else
			return false;
	}
	
	// RUN METHODS ******************************************************

	public void run() {
		while (isInterrupted() == false) {
			try {
				while (this.msg == null) {
					wait();
				}
				this.processMessage(this.msg);
			} catch(InterruptedException e) {
				// Stopped by wait()
			}
		}
	}
	
}