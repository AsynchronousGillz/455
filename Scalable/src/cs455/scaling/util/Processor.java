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
	 * @param manager
	 *            a reference to the {@link TaskManager}
	 */
	public Processor(TaskManager manager) {
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
		while (this.currentTask != null)
			this.lock.wait();
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
						this.lock.wait();
					this.currentTask.exec(this.manager);
					this.currentTask = null;
					this.lock.notify();
					this.manager.taskComplete(this);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}