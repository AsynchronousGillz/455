package cs455.overlay.util;

import cs455.overlay.msg.*;
import cs455.overlay.node.*;

public class MessageQueue {
	
	/**
	 * 
	 */
	final private int size;
	
	/**
	 * 
	 */
	private int used;
	
	/**
	 * 
	 */
	private int front;
	
	/**
	 * 
	 */
	private int back;
	
	/**
	 * 
	 */
	private MessagePair contents[];

	/**
	 * MessageQueue Constructor, store messages to be processed 
	 * by the {@link MessagingProcessor}.
	 * @param size
	 * 			size of the queue.
	 */
	public MessageQueue(int size) {
		this.size = size;
		this.used = this.front = this.back = 0;
		this.contents = new MessagePair[size];
	}
	
	/**
	 * Gets the size of the queue.
	 * @return size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gets the size of the queue.
	 * @return size
	 */
	public int getDifference() {
		return this.front - this.back;
	}

	/**
	 * Get a message from the queue.
	 * @return a {@link ProtocolMessage}
	 */
	public synchronized MessagePair get() {
		while (used == 0) {
			try {
				this.wait();
//				System.out.println("Queue is empty");
			} catch (InterruptedException e) {
			}
		}
		this.used--;
		notify();
		return (contents[this.front++ % this.size]);
	}

	/**
	 * Place a {@link MessagePair} into the queue to be 
	 * processed by a {@link MessagingProcessor}.
	 * @param m
	 */
	public synchronized void put(MessagePair m) {
		while (used == size) {
			try {
				this.wait();
//				System.out.println("Queue is full");
			} catch (InterruptedException e) {
			}
		}
		this.used++;
		contents[this.back++ % this.size] = m;
		notifyAll();
	}
}
