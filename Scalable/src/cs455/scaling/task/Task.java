package cs455.scaling.task;

import java.nio.channels.SelectionKey;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;

public abstract class Task {

	protected enum TaskType {
		ACCEPT, HASH, READ,	WRITE
	}
	
	protected final TaskType type;
	
	protected final SelectionKey key;
	
	protected Message msg;
	
	protected volatile boolean done;

	public Task(TaskType type, SelectionKey key) {
		this.type = type;
		this.key = key;
		this.msg = null;
		this.done = false;
	}
	
	final public void setMessage(Message msg) { 
		this.msg = msg;
	}
	
	final public void setDone() { 
		this.done = false;
	}
	
	final public String toString() {
		return "Task [ "+"type: "+type+"]";
	}
	
	final public void closeKey(SelectionKey key) {
		key.cancel();
	}
	
	abstract public void exec(TaskManager manager);
	
}
