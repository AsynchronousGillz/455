package cs455.scaling.task;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;
import cs455.scaling.server.TaskManager;
import cs455.scaling.util.Pair;
import cs455.scaling.util.Processor;

public class WriteTask extends Task {

	public WriteTask(SelectionKey key, Message msg) {
		super(TaskType.WRITE, key, msg);
		// TODO Auto-generated constructor stub
	}

	public void exec(TaskManager manager) {
/*		Pair p = thread.getMessage();
		try {
			SocketChannel chan = p.getChannel();
			synchronized (chan) {
				chan.write(p.getMsg().getMessage());	
			}
		} catch (IOException e) {
			System.err.println("An error occured while sending.");
			e.printStackTrace();
		}*/
		
	}

}
