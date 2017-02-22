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
	 * @param poolSize
	 * @param queueSize
	 */
	public TaskManager(int poolSize, int queueSize) {
		this.queue = new Queue<Task>(queueSize);
		this.threadpool = new ThreadPool(this, poolSize);
	}
	
	public String getInfo() {
		return "[ queue count: "+this.queue.getCount()+" threadpool count: "+this.threadpool.getCount()+" ]";
	}
	
	/**
	 * TODO write comment
	 * @param t
	 */
	public void enqueueTask(Task t) {
		System.out.println("task: "+t); // DEBUG
		try {
			queue.enqueue(t);
		} catch (InterruptedException e) {
			System.err.println("TaskManager:: enqueueTask() interrupted.");
		}
	}
	
	/**
	 * TODO write comment
	 * @param p
	 */
	public void taskComplete(Processor p) {
		try {
			threadpool.enqueue(p);
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
