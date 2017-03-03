package cs455.scaling.client;

import java.io.*;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.msg.Message;

/**
 * 
 * @author G van Andel
 *
 * @see Client.NodeMain
 */

public class Sender extends Thread {

	// INSTANCE VARIABLES ***********************************************

	/**
	 * 
	 */
	private SelectionKey key;
	
	/**
	 * To stop the run loop.
	 */
	private boolean running;
	
	/**
	 * 
	 */
	final private int messageRate;
	
	/**
	 * 
	 */
	private Integer sentCount;
	
	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the node.
	 * 
	 * @param serverAddress
	 *            the server's host name.
	 * @param serverPort
	 *            the port number.
	 */
	public Sender(SelectionKey key, int messageRate) {
		this.key = key;
		this.messageRate = messageRate;
		this.sentCount = 0;
		super.setName("Sender-"+super.getId());
	}

	/**
	 * Sends an object to the server. This is the only way that methods should
	 * communicate with the server.
	 * 
	 * @param msg
	 *            The message to be sent.
	 * @exception IOException
	 *                if an I/O error occurs when sending
	 */
	final private void sendToServer() throws IOException {
		SocketChannel channel = (SocketChannel) this.key.channel();
		if (channel == null)
			throw new SocketException("Connection error when sending.");
		synchronized (channel) {
			channel.write(Message.makeMessage());
		}
		this.incrementSent();
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * TODO Comment incrementSent
	 */
	final public void incrementSent() {
		synchronized (sentCount) {
			this.sentCount++;	
		}
	}
	
	/**
	 * @return true if the node is connnected.
	 */
	final public boolean isConnected() {
		return this.isAlive() && this.running;
	}
	
	/**
	 * 
	 * @return
	 */
	final public String getInfo() {
		String ret = "Total Sent Count: ";
		synchronized (this.sentCount) {
			ret += this.sentCount;
			this.sentCount = 0;
		}
		return ret;
	}

	/**
	 * Causes the server to stop accepting new connections.
	 */
	final public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// RUN METHOD -------------------------------------------------------

	/**
	 * Waits for messages from the server. When each arrives, a call is made to
	 * <code>messageFromServer()</code>. Not to be explicitly called.
	 */
	final public void run() {
		connectionEstablished();
		try {
			while (this.running == true) {
			    this.sendToServer();
			    this.sleep(1000 / messageRate);
			    this.key.selector().wakeup();
				// generate packets is messageRate per-second;
			}
		} catch (Exception exception) {
			connectionException(exception);
		}
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	/**
	 * Hook method called each time an exception is thrown by the node's thread
	 * that is waiting for messages from the server.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	public void connectionException(Exception exception) {
		if (exception instanceof EOFException == false)
			System.out.println("connectionException :: " + exception.toString());
	}

	/**
	 * Hook method called after a connection has been established.
	 */
	public void connectionEstablished() {
		this.running = true;
	}
	
	/**
	 * Hook method to stop the client.
	 */
	public void closeConnection() {
		this.running = false;
	}

}
