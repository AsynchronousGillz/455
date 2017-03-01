package cs455.scaling.task;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.server.NioServer;
import cs455.scaling.server.TaskManager;

public class ReadTask extends Task {
	
	final private NioServer server;
	
	public ReadTask(SelectionKey key, NioServer server) {
		super(TaskType.READ, key);
		this.server = server;
	}

	public void exec(TaskManager manager) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer bytes = ByteBuffer.allocate(8192);
		int read = 0;
		try {
			while (bytes.hasRemaining() && read != -1){
				read = socketChannel.read(bytes);
			}
			if (read == -1)
				throw new IOException();
		} catch (IOException e) {
			this.server.clientDisconnected();
			super.closeKey();
			return;
		}
		key.attach(null);
		manager.enqueueTask(new SendTask(key, bytes));
	}

}
