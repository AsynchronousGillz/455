package cs455.overlay.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cs455.overlay.util.StatisticsCollector;

public class StatisticsMessage extends ProtocolMessage {
	
	/**
	 * Convert from a {@link ProtocolMessage} to a {@link StatisticsMessage}
	 * @param message
	 * 			message in bye form
	 */
	public StatisticsMessage(byte[] message) {
		super();
		this.setType("TRAFFIC_SUMMARY");
		super.setMessage(message);
	}

	/**
	 * Make the TaskMessage a TRAFFIC_SUMMARY
	 * @param info
	 * 			string "ip:port"
	 */
	public StatisticsMessage(StatisticsCollector info) {
		super();
		this.setType("TRAFFIC_SUMMARY");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(output);
		try {
			out.writeLong(info.getSent());
			out.writeLong(info.getSumSent());
			out.writeLong(info.getReceived());
			out.writeLong(info.getSumReceived());
			out.writeLong(info.getRelayed());
		} catch (IOException e){
			System.err.println(e.toString());
		}
		super.setMessage(output.toByteArray());
	}
	
	/**
	 * Makes a {@link StatisticsCollector} out of the message.
	 * @return
	 */
	public StatisticsCollector makeCollector() {
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		long sent = 0;
		long received = 0;
		long sentSum = 0;
		long receivedSum = 0;
		long relayed = 0;
		try {
			sent = in.readLong();
			sentSum = in.readLong();
			received = in.readLong();
			receivedSum = in.readLong();
			relayed = in.readLong();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return new StatisticsCollector(sent, received, sentSum, receivedSum, relayed);
	}
	
	/**
	 * Gets the sent.
	 * @return long of sent.
	 */
	public long getSent(){
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		long ret = 0;
		try {
			ret = in.readLong();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Gets the sum of send messages.
	 * @return long
	 */
	public long getSentSum(){
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		long ret = 0;
		try {
			in.readLong();
			ret = in.readLong();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Gets the received.
	 * @return long
	 */
	public long getReceived(){
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		long ret = 0;
		try {
			in.readLong();
			in.readLong();
			ret = in.readLong();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Gets the sum of received messages.
	 * @return long
	 */
	public long getSumReceived(){
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		long ret = 0;
		try {
			in.readLong();
			in.readLong();
			in.readLong();
			ret = in.readLong();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Gets the relayed.
	 * @return long
	 */
	public long getRelayed(){
		ByteArrayInputStream input = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(input);
		long ret = 0;
		try {
			in.readLong();
			in.readLong();
			in.readLong();
			in.readLong();
			ret = in.readLong();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return ret;
	}

	
}