package cs455.scaling.server;

import cs455.scaling.task.*;
import cs455.scaling.util.*;

public final class TaskManager extends Thread {

	/**
	 * The connection master statistics holder. Each message will be placed in
	 * the queue until the a {@link Processor} can process the message.
	 * The default size is 10000.
	 */
	private Queue<Task> queue;

	/**
	 * The connection master statistics holder. All Processor treads will be placed
	 * in this pool to be called when a message is received.
	 */
	private ThreadPool threadpool;
	
	/**
	 * 
	 */
	private boolean debug = true;
	
	/**
	 * 
	 * @param poolSize
	 * @param queueSize
	 */
	public TaskManager(NioServer server, int poolSize, int queueSize) {
		this.queue = new Queue<Task>(queueSize);
		this.threadpool = new ThreadPool(this, server, poolSize);
	}
	
	/**
	 * TODO write comment
	 * @param hashTask
	 */
	public void taskComplete(Task t) {
		if (debug)
			System.out.println("receiving: " + t);
		try {
			queue.enqueue(t);
		} catch (InterruptedException e) {
			System.err.println("TaskManager:: taskComplete() interrupted.");
		}
	}
	
	/**
	 * 
	 */
	public void run() {
		try {
			while(Boolean.toString(true).equals("true")) {
				Task task = queue.dequeue();
				Processor processor = threadpool.dequeue();
				processor.addTask(task);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.close();
		}
	}
	
	/**
	 * 
	 */
	public void close() {
		// TODO :: Add close to TaskManager
	}
	
}
