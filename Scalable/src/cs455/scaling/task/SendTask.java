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
		System.out.println("Message hash: "+Hash.toHash(msg.array()));
	}

	public void exec(TaskManager manager) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		super.msg.rewind();
		try {
			socketChannel.write(super.msg);
			if (this.msg.remaining() > 0)
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
