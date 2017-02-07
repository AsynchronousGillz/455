package cs455.overlay.util;

import cs455.overlay.msg.StatisticsMessage;

public class StatisticsCollector {

	/**
	 * 
	 */
	private long sent;

	/**
	 * 
	 */
	private long received;

	/**
	 * 
	 */
	private long sumSent;

	/**
	 * 
	 */
	private long sumReceived;
	
	/**
	 * 
	 */
	private long relayed;

	/**
	 * 
	 */
	public StatisticsCollector() {
		this.sent = 0;
		this.received = 0;
		this.sumSent = 0;
		this.sumReceived = 0;
		this.relayed = 0;
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
	}

	public long getSent() {
		return sent;
	}

	public synchronized void addSent(int x) {
		this.sent += 1;
		this.sumSent += x;
	}

	public long getReceived() {
		return received;
	}

	public synchronized void addReceived(int x) {
		this.received += 1;
		this.sumReceived += x;
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
	
	public synchronized void addRelayed() {
		this.relayed += 1;
	}

	@Override
	public String toString() {
		return "\t" + sent + "\t" + received + "\t" + sumSent + "\t" + sumReceived +"\t" + relayed;
	}

}
