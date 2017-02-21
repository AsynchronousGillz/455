package cs455.scaling.util;

import cs455.scaling.server.NioServer;
import cs455.scaling.server.TaskManager;

public final class ThreadPool {

	private Processor[] pool;
	
	private int size;
	
	private int used;
	
	private int front;
	
	private int back;

	public ThreadPool(TaskManager manager, NioServer server, int size) {
		this.pool = new Processor[size];
		this.size = size;
		this.used = this.front = this.back = 0;
		for (int i = 0; i < size; i++) {
			this.pool[i] = new Processor(server.getThreadGroup(), manager);
			this.pool[i].start();
		}
	}

	public int getSize() {
		return size;
	}

	public synchronized void enqueue(Processor value) throws InterruptedException {
		while (used == size)
			this.wait();
		this.used++;
		pool[this.back++ % this.size] = value;
		notifyAll();
	}
	
	public synchronized Processor dequeue() throws InterruptedException {
		while (used == 0)
			this.wait();
		notifyAll();
		this.used--;
		return pool[this.front++ % this.size];
	}
	
}
