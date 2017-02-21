package cs455.scaling.task;

import java.nio.channels.SelectionKey;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;

public class HashTask extends Task {

	public HashTask(SelectionKey key, Message msg) {
		super(TaskType.HASH, key, msg);
	}

	public void exec(TaskManager manager) {
		// TODO Auto-generated method stub
		
	}

}
