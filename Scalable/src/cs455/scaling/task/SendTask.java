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
//		System.out.println("[ HASH ] "+super.hash); // PRINT HASH
	}

	public void exec(TaskManager manager) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer bytes = ByteBuffer.wrap(super.hash.getBytes());
		try {
			socketChannel.write(bytes);
			if (bytes.remaining() > 0)
				throw new Exception();
		} catch (IOException e) {
			System.err.println("[ ERROR ] Fail to send.");
			super.closeKey();
			return;
		} catch (Exception e) {
			System.err.println("[ ERROR ] Client buffer is full.");
			return;
		}
		manager.incrementSent();
	}

}
