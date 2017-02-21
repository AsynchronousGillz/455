package cs455.scaling.server;

import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;
import cs455.scaling.util.Pair;
import cs455.scaling.util.Processor;
import cs455.scaling.util.Queue;
import cs455.scaling.util.ThreadPool;

public final class TaskManager extends Thread {

	/**
	 * The connection master statistics holder. Each message will be placed in
	 * the queue until the a {@link Processor} can process the message.
	 * The default size is 10000.
	 */
	private Queue queue;

	/**
	 * The connection master statistics holder. All Processor treads will be placed
	 * in this pool to be called when a message is received.
	 */
	private ThreadPool threadpool;
	
	/**
	 * 
	 */
	private boolean running;
	
	/**
	 * 
	 */
	private boolean debug = false;
	
	/**
	 * 
	 * @param poolSize
	 * @param queueSize
	 */
	public TaskManager(NioServer server, int poolSize, int queueSize) {
		this.queue = new Queue(queueSize);
		this.threadpool = new ThreadPool(server, poolSize);
		this.running = false;
	}

	/**
	 * 
	 * @param socketChannel
	 * @param message
	 */
	public void addMessage(SocketChannel socketChannel, Message msg) {
		if (debug)
			System.out.println("sending: " + msg);
		try {
			queue.enqueue(new Pair(msg, socketChannel));
		} catch (InterruptedException e) {
			System.err.println("TaskManager:: addMessage() interrupted.");
		}
	}
	
	/**
	 * 
	 */
	public void close() {
		// TODO :: Add close to TaskManager
	}
	
	public void run() {
		this.running = true;
		while (running == true) {
			this.threadpool.getSize(); // TODO pair message with processor
		}
	}
	
}
