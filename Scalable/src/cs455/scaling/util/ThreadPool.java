package cs455.scaling.util;

import java.util.LinkedList;

import cs455.scaling.server.TaskManager;

/**
 * A thread safe thread pool
 * 
 * @author G van Andel
 *
 */

public final class ThreadPool {

	/**
	 * 
	 */
	final private LinkedList<Processor> queue;
	
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
	public ThreadPool(TaskManager manager, int size) {
		this.size = size;
		this.lock = new Object();
		this.queue = new LinkedList<Processor>();
		for (int i = 0; i < size; i++) {
			Processor p = new Processor(manager);
			queue.add(p);
			p.start();
		}
	}
	
	public int getCount() {
		synchronized (lock) {
			return queue.size();
		}
	}

	public void enqueue(Processor item) throws InterruptedException {
		synchronized (lock) {
			while (this.queue.size() == this.size) {
				this.lock.wait();
			}
			if (this.queue.size() == 0) {
				this.lock.notifyAll();
			}
			this.queue.add(item);
		}
	}

	public Processor dequeue() throws InterruptedException {
		synchronized (lock) {
			while (this.queue.size() == 0) {
				this.lock.wait();
			}
			if (this.queue.size() == this.size) {
				this.lock.notifyAll();
			}
			return this.queue.remove(0);
		}
	}
	
}
