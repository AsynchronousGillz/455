package cs455.scaling.util;

import cs455.scaling.server.NioServer;

public class ThreadPool {

	private Processor[] pool;
	
	final private int size;

	public ThreadPool(NioServer server, int size) {
		this.pool = new Processor[size];
		this.size = size;
		for (int i = 0; i < size; i++) {
			this.pool[i] = new Processor(server.getThreadGroup());
			this.pool[i].start();
		}
	}

	public int getSize() {
		return size;
	}
	
	public Processor getProcessor() {
		for (Processor p : pool)
			if (p.checkMessage() == false)
				return p;
		return null;
	}
	
}
