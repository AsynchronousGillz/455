package cs455.scaling.util;

import java.util.LinkedList;

/**
 * A thread safe message queue
 * 
 * @author G van Andel
 *
 */

public class Queue<T> {

	/**
	 * 
	 */
	private LinkedList<T> queue;
	
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
		queue = new LinkedList<T>();
	}

	synchronized public void enqueue(T item) throws InterruptedException {
		while (this.queue.size() == this.size) {
			wait();
		}
		if (this.queue.size() == 0) {
			notifyAll();
		}
		this.queue.add(item);
	}

	synchronized public T dequeue() throws InterruptedException {
		while (this.queue.size() == 0) {
			wait();
		}
		if (this.queue.size() == this.size) {
			notifyAll();
		}

		return this.queue.remove(0);
	}
	
}
