package cs455.scaling.task;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;

public class HashTask extends Task {

	public HashTask(SelectionKey key, Message msg) {
		super(TaskType.HASH, key);
		super.setMessage(msg);
	}

	public void exec(TaskManager manager) {
		String hash = msg.toString();
		System.out.println("Message hash: "+hash);
		ByteBuffer bytes = ByteBuffer.wrap(hash.getBytes());
		// Hand the data off to our worker thread
		manager.enqueueTask(new SendTask(key, new Message(bytes)));
	}

}
