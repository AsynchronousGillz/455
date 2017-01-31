package cs455.overlay.util;

import java.math.BigInteger;

public class StatisticsCollector {

	/**
	 * 
	 */
	private BigInteger sent;

	/**
	 * 
	 */
	private BigInteger received;

	/**
	 * 
	 */
	private BigInteger sumSent;

	/**
	 * 
	 */
	private BigInteger sumReceived;

	/**
	 * 
	 */
	public StatisticsCollector() {
		this.sent = BigInteger.valueOf(0);
		this.received = BigInteger.valueOf(0);
		this.sumSent = BigInteger.valueOf(0);
		this.sumReceived = BigInteger.valueOf(0);
	}

	public BigInteger getSent() {
		return sent;
	}

	public synchronized void addSent(BigInteger x) {
		this.sent.add(BigInteger.valueOf(1));
		this.sumSent.add(x);
	}

	public BigInteger getReceived() {
		return received;
	}

	public synchronized void addReceived(BigInteger x) {
		this.received.add(BigInteger.valueOf(1));
		this.sumReceived.add(x);
	}

	public BigInteger getSumSent() {
		return sumSent;
	}

	public BigInteger getSumReceived() {
		return sumReceived;
	}

	@Override
	public String toString() {
		return "NodeInformation [sent=" + sent + ", received=" + received + ", sumSent=" + sumSent + ", sumReceived="
				+ sumReceived + "]";
	}

}
