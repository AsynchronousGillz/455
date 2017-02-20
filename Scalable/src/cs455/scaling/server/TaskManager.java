package cs455.scaling.server;

import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;
import cs455.scaling.util.Pair;
import cs455.scaling.util.Processor;
import cs455.scaling.util.Queue;
import cs455.scaling.util.ThreadPool;

public class TaskManager {

	/**
	 * The connection master statistics holder. Each message will be placed in
	 * the queue until the a {@link Processor} can process the message.
	 * The default size is 10000.
	 */
	public Queue queue;

	/**
	 * The connection master statistics holder. All Processor treads will be placed
	 * in this pool to be called when a message is received.
	 */
	public ThreadPool threadpool;

	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	public void addMessage(SocketChannel socketChannel, Message message) {
		Processor p = threadpool.getProcessor();
		try {
			if (p != null)
				p.addMessage(new Pair(message, socketChannel));
		} catch (Exception e) {
			System.err.println("TaskManager:: addMessage() could not add message to Processor.");
		}
		try {
			if (p == null)
				queue.enqueue(new Pair(message, socketChannel));
		} catch (InterruptedException e) {
			System.err.println("TaskManager:: addMessage() interrupted.");
		}
	}
	
}
