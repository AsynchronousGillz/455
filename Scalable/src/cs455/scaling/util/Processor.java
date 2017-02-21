package cs455.scaling.util;

import cs455.scaling.server.TaskManager;
import cs455.scaling.task.Task;

/**
 * 
 * @author G van Andel
 *
 */

public final class Processor extends Thread {
	// INSTANCE VARIABLES ***********************************************

	/**
	 * TODO write comment
	 */
	private Task currentTask;
	
	/**
	 * TODO comment
	 */
	private Object lock;
	
	/**
	 * TODO write more comments
	 */
	final private TaskManager manager;
	
	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs a new connection to a node.
	 * 
	 * @param group
	 *            the thread groupSystem.out.println("node at "+ node +
	 *            "connected"); that contains the connections.
	 * @param queue
	 *            a reference to the queue that the {@link Processor} will 
	 *            read messages from.
	 * @param server
	 *            a reference to the server that created this instance
	 */
	public Processor(ThreadGroup group, TaskManager manager) {
		super(group, (Runnable) null);
		setName("Processor-"+getId());
		this.manager = manager;
		this.lock = new Object();
		this.currentTask = null;
	}

	// INSTANCE METHODS *************************************************
	
	public Task getMessage() {
		return null;
	}
	
	public void addTask(Task task) throws InterruptedException {
		synchronized (this.lock) {
			this.currentTask = task;
			this.lock.notify();
		}
	}
	
	// RUN METHODS ******************************************************

	public void run() {
		try {
			while(Boolean.toString(true).equals("true")) {
				synchronized (this.lock) {
					while (this.currentTask == null)
						lock.wait();
					this.currentTask.exec(manager);
					this.currentTask = null;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}