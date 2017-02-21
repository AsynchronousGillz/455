package cs455.scaling.task;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;

public class WriteTask extends Task {

	public WriteTask(SelectionKey key, Message msg) {
		super(TaskType.WRITE, key, msg);
		// TODO Auto-generated constructor stub
	}

	public void exec(TaskManager manager) {
		if (debug)
			System.out.println("Message to be sent back.");
		
		SocketChannel socketChannel = (SocketChannel) key.channel();
		try {
			synchronized (socketChannel) {
				socketChannel.write(msg.getMessage());	
			}
		} catch (IOException e) {
			System.err.println("An error occured while sending.");
			e.printStackTrace();
		}
		
	}

}
