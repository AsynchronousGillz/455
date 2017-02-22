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
	 * @param size
	 */
	public ThreadPool(TaskManager manager, int size) {
		this.size = size;
		queue = new LinkedList<Processor>();
		for (int i = 0; i < size; i++) {
			Processor p = new Processor(manager);
			queue.add(p);
			p.start();
		}
	}
	
	synchronized public int getCount() {
		return queue.size();
	}

	synchronized public void enqueue(Processor item) throws InterruptedException {
		while (this.queue.size() == this.size) {
			this.wait();
		}
		if (this.queue.size() == 0) {
			this.notifyAll();
		}
		this.queue.add(item);
	}

	synchronized public Processor dequeue() throws InterruptedException {
		while (this.queue.size() == 0) {
			this.wait();
		}
		if (this.queue.size() == this.size) {
			this.notifyAll();
		}
		return this.queue.remove(0);
	}
	
}
