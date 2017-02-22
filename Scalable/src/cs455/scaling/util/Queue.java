package cs455.scaling.util;

import java.util.LinkedList;

/**
 * A thread safe message queue
 * 
 * @author G van Andel
 *
 */

public final class Queue<T> {

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
	
	synchronized public int getCount() {
		return queue.size();
	}

	synchronized public void enqueue(T item) throws InterruptedException {
		while (this.queue.size() == this.size) {
			this.wait();
		}
		if (this.queue.size() == 0) {
			this.notifyAll();
		}
		this.queue.add(item);
	}

	synchronized public T dequeue() throws InterruptedException {
		while (this.queue.size() == 0) {
			this.wait();
		}
		if (this.queue.size() == this.size) {
			this.notifyAll();
		}
		return this.queue.remove(0);
	}
	
}
