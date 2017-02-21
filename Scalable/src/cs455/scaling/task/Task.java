package cs455.scaling.task;

import java.nio.channels.SelectionKey;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;

public abstract class Task {

	protected final boolean debug = true;
	
	protected enum TaskType {
		ACCEPT, HASH, READ,	WRITE
	}
	
	protected final TaskType type;
	
	protected final SelectionKey key;
	
	protected final Message msg;
	
	protected volatile boolean done;

	public Task(TaskType type, SelectionKey key, Message msg) {
		this.type = type;
		this.key = key;
		this.msg = msg;
		this.done = false;
	}
	
	final public void setDone() { 
		this.done = false;
	}
	
	final public String toString() {
		return "Task [ "+"type: "+type+"]";
	}
	
	abstract public void exec(TaskManager manager);
	
}
