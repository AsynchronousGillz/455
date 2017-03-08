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
	 */
	final private Object lock;

	/**
	 * 
	 * @param size
	 */
	public Queue(int size) {
		this.size = size;
		this.queue = new LinkedList<T>();
		this.lock = new Object();
	}
	
	public int getCount() {
		synchronized (this.lock) {
			return queue.size();
		}
	}

	public void enqueue(T item) throws InterruptedException {
		synchronized (this.lock) {
			while (this.queue.size() == this.size) {
				this.lock.wait();
			}
			if (this.queue.size() == 0) {
				this.lock.notify();
			}
			this.queue.add(item);	
		}
	}

	public T dequeue() throws InterruptedException {
		synchronized (this.lock) {
			while (this.queue.size() == 0) {
				this.lock.wait();
			}
			if (this.queue.size() == this.size) {
				this.lock.notify();
			}
			return this.queue.remove(0);
		}
	}
	
}
