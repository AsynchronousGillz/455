package cs455.scaling.task;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import cs455.scaling.server.TaskManager;

public abstract class Task {

	protected enum TaskType {
		ACCEPT, HASH, READ,	WRITE
	}
	
	protected final TaskType type;
	
	protected final SelectionKey key;
	
	protected ByteBuffer msg;
	
	public Task(TaskType type, SelectionKey key) {
		this.type = type;
		this.key = key;
		this.msg = null;
	}
	
	final public void setMessage(ByteBuffer msg) { 
		this.msg = msg;
	}
	
	final public String toString() {
		return "Task [ "+"type: "+type+"]";
	}
	
	final public void closeKey() {
		this.key.cancel();
	}
	
	abstract public void exec(TaskManager manager);
	
}
