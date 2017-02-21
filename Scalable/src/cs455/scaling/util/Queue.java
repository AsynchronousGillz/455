package cs455.scaling.util;

import java.util.LinkedList;

/**
 * A thread safe message queue
 * 
 * @author G van Andel
 *
 */

public class Queue {

	/**
	 * 
	 */
	private LinkedList<Pair> queue;
	
	/**
	 * 
	 */
	final private int size;

	/**
	 * 
	 * @param size
	 */
	public Queue(int size) {
		this.size = size;
		queue = new LinkedList<>();
	}

	synchronized public void enqueue(Pair item) throws InterruptedException {
		while (this.queue.size() == this.size) {
			wait();
		}
		if (this.queue.size() == 0) {
			notifyAll();
		}
		this.queue.add(item);
	}

	synchronized public Pair dequeue() throws InterruptedException {
		while (this.queue.size() == 0) {
			wait();
		}
		if (this.queue.size() == this.size) {
			notifyAll();
		}

		return this.queue.remove(0);
	}
	
}
