package cs455.scaling.task;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;

public class SendTask extends Task {

	public SendTask(SelectionKey key, Message msg) {
		super(TaskType.WRITE, key);
		super.setMessage(msg);
	}

	public void exec(TaskManager manager) {
		System.out.println("sending msg: "+msg);
		SocketChannel socketChannel = (SocketChannel) key.channel();
		try {
			synchronized (socketChannel) {
				socketChannel.write(msg.getMessage());	
			}
		} catch (IOException e) {
			System.err.println("An error occured while sending.");
			e.printStackTrace();
		}
		key.interestOps(SelectionKey.OP_READ);
	}

}
