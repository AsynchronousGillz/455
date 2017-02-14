package cs455.overlay.util;

import java.util.concurrent.LinkedBlockingQueue;

import cs455.overlay.msg.*;
import cs455.overlay.node.*;

public class MessageQueue {

	/**
	 * 
	 */
	private LinkedBlockingQueue<MessagePair> queue;

	/**
	 * MessageQueue Constructor, store messages to be processed 
	 * by the {@link MessagingProcessor}.
	 * @param size
	 * 			size of the queue.
	 */
	public MessageQueue() {
		queue = new LinkedBlockingQueue<MessagePair>();
	}
	
	/**
	 * Gets the size of the queue.
	 * @return size
	 */
	public int getSize() {
		return queue.size();
	}
	
	/**
	 * Get a message from the queue.
	 * @return a {@link ProtocolMessage}
	 */
	public MessagePair get() throws InterruptedException {
		return queue.take();
	}

	/**
	 * Place a {@link MessagePair} into the queue to be 
	 * processed by a {@link MessagingProcessor}.
	 * @param m
	 */
	public void put(MessagePair m) throws InterruptedException {
		queue.put(m);
	}
}
