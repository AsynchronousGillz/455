package cs455.overlay.util;

import cs455.overlay.msg.*;

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
	private ProtocolMessage contents[];

	public MessageQueue(int size) {
		this.size = size;
		this.used = this.front = this.back = 0;
		this.contents = new ProtocolMessage[size];
	}

	public int get_size() {
		return (size);
	}

	public synchronized ProtocolMessage get() {
		while (used == 0) {
			try {
				this.wait();
				// System.out.printf("Queue is empty. %d = this.size\n");
			} catch (InterruptedException e) {
			}
		}
		notify();
		this.used--;
		return (contents[this.front++ % this.size]);
	}

	public synchronized void put(ProtocolMessage m) {
		while (used == size) {
			try {
				this.wait();
				// System.out.printf("Queue is full. %d = this.size\n");
			} catch (InterruptedException e) {
			}
		}
		this.used++;
		contents[this.back++ % this.size] = m;
		notify();
	}
}
