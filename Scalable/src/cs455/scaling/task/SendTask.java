package cs455.scaling.task;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Hash;
import cs455.scaling.server.TaskManager;

public class SendTask extends Task {

	public SendTask(SelectionKey key, ByteBuffer msg) {
		super(TaskType.WRITE, key);
		super.msg = msg;
		super.hash = Hash.toHash(msg.array());
		System.out.println("Message hash: "+super.hash);
	}

	public void exec(TaskManager manager) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer bytes = ByteBuffer.wrap(super.hash.getBytes());
		try {
			socketChannel.write(bytes);
			if (bytes.remaining() > 0)
				throw new Exception();
		} catch (IOException e) {
			System.err.println("An error occured while sending.");
			super.closeKey();
			return;
		} catch (Exception e) {
			System.err.println("Client buffer is full could not send.");
			return;
		}
		manager.incrementSent();
	}

}
