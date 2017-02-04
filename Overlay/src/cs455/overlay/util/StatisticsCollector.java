package cs455.overlay.util;

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
	public StatisticsCollector() {
		this.sent = 0;
		this.received = 0;
		this.sumSent = 0;
		this.sumReceived = 0;
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

	@Override
	public String toString() {
		return "NodeInformation [sent=" + sent + ", received=" + received +
				", sumSent=" + sumSent + ", sumReceived=" + sumReceived + "]";
	}

}
