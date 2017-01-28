package cs455.overlay.util;

import java.math.BigInteger;

public class NodeInformation {

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
	public NodeInformation() {
		this.sent = BigInteger.valueOf(0);
		this.received = BigInteger.valueOf(0);
		this.sumSent = BigInteger.valueOf(0);
		this.sumReceived = BigInteger.valueOf(0);
	}

	public BigInteger getSent() {
		return sent;
	}

	public synchronized void addSent() {
		this.sent.add(BigInteger.valueOf(1));
	}

	public BigInteger getReceived() {
		return received;
	}

	public synchronized void addReceived() {
		this.received.add(BigInteger.valueOf(1));
	}

	public BigInteger getSumSent() {
		return sumSent;
	}

	public synchronized void setSumSent() {
		this.sumSent.add(BigInteger.valueOf(1));
	}

	public BigInteger getSumReceived() {
		return sumReceived;
	}

	public synchronized void setSumReceived() {
		this.sumReceived.add(BigInteger.valueOf(1));
	}

	@Override
	public String toString() {
		return "NodeInformation [sent=" + sent + ", received=" + received + 
				", sumSent=" + sumSent + ", sumReceived=" + sumReceived + "]";
	}
	
}
