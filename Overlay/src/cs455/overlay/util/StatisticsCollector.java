package cs455.overlay.util;

import java.util.concurrent.locks.ReentrantLock;

import cs455.overlay.msg.StatisticsMessage;

public class StatisticsCollector {

	/**
	 * 
	 */
	private long sent;

	/**
	 * 
	 */
	private long sumSent;
	
	/**
	 * 
	 */
	private ReentrantLock sentLock;
	
	/**
	 * 
	 */
	private long received;

	/**
	 * 
	 */
	private long sumReceived;
	
	/**
	 * 
	 */
	private ReentrantLock receivedLock;
	
	/**
	 * 
	 */
	private long relayed;
	
	/**
	 * 
	 */
	private ReentrantLock relayedLock;

	/**
	 * 
	 */
	public StatisticsCollector() {
		this.sent = 0;
		this.received = 0;
		this.sumSent = 0;
		this.sumReceived = 0;
		this.relayed = 0;
		sentLock = new ReentrantLock();
		receivedLock = new ReentrantLock();
		relayedLock = new ReentrantLock();
	}
	
	/**
	 * Makes a {@link StatisticsCollector} from a {@link StatisticsMessage}
	 * @param sent sent
	 * @param received received
	 * @param sumSent sumSent
	 * @param sumReceived sumReceived
	 * @param relayed relayed
	 */
	public StatisticsCollector(long sent, long received, long sumSent, long sumReceived, long relayed) {
		this.sent = sent;
		this.received = received;
		this.sumSent = sumSent;
		this.sumReceived = sumReceived;
		this.relayed = relayed;
		sentLock = new ReentrantLock();
		receivedLock = new ReentrantLock();
		relayedLock = new ReentrantLock();
	}
	
	/**
	 * Reset back to zero.
	 */
	public void reset() {
		this.sent = 0;
		this.received = 0;
		this.sumSent = 0;
		this.sumReceived = 0;
		this.relayed = 0;
	}


	public long getSent() {
		return sent;
	}

	public void addSent(int x) {
		sentLock.lock();
		this.sent += 1;
		this.sumSent += x;
		sentLock.unlock();
	}

	public long getReceived() {
		return received;
	}

	public void addReceived(int x) {
		receivedLock.lock();
		this.received += 1;
		this.sumReceived += x;
		receivedLock.unlock();
	}

	public long getSumSent() {
		return sumSent;
	}

	public long getSumReceived() {
		return sumReceived;
	}
	
	public long getRelayed() {
		return relayed;
	}
	
	public void addRelayed() {
		relayedLock.lock();
		this.relayed += 1;
		relayedLock.unlock();
	}

	@Override
	public String toString() {
		try {
			sentLock.lock();
			receivedLock.lock();
			relayedLock.lock();
			return new String(String.format("%1$10d %2$15d %3$10d %4$15d %5$10d", sent, sumSent, received, sumReceived, relayed));
		} finally {
			sentLock.unlock();
			receivedLock.unlock();
			relayedLock.unlock();
		}
	}

}
