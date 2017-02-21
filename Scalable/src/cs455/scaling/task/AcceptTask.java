package cs455.scaling.task;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import cs455.scaling.server.TaskManager;

public class AcceptTask extends Task {

	public AcceptTask(SelectionKey key) {
		super(TaskType.ACCEPT, key, null);
	}
	
	public void exec(TaskManager manager) {
		if (debug)
			System.out.println("New node registered.");
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		// Accept the connection and make it non-blocking
		SocketChannel socketChannel;
		try {
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
