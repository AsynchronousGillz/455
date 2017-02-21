package cs455.scaling.task;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;

public class HashTask extends Task {

	public HashTask(SelectionKey key, Message msg) {
		super(TaskType.HASH, key, msg);
	}

	public void exec(TaskManager manager) {
		if (debug)
			System.out.println("Message to be hashed.");
		
		ByteBuffer bytes = ByteBuffer.wrap(msg.toString().getBytes());
		// Hand the data off to our worker thread
		manager.taskComplete(new WriteTask(key, new Message(bytes)));
		
	}

}
