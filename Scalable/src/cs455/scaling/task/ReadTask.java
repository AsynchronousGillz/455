package cs455.scaling.task;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;

public class ReadTask extends Task {

	public ReadTask(SelectionKey key) {
		super(TaskType.READ, key);
	}

	public void exec(TaskManager manager) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		// Clear out our read buffer so it's ready for new data
		ByteBuffer bytes = ByteBuffer.allocate(8192);
		// Attempt to read off the channel
		int read = 0; 
		try {
			while (bytes.hasRemaining() && read != -1){
				read = socketChannel.read(bytes);
			}
			if (read == -1)
				throw new IOException();
		} catch (IOException e) {
			System.err.println("An error occured when reading.");
			super.closeKey(key);
			return;
		} finally {
			bytes.clear();
		}

		// Hand the data off to our worker thread
		manager.enqueueTask(new HashTask(key, new Message(bytes)));
	}

}
