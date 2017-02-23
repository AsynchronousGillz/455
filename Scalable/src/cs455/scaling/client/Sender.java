package cs455.scaling.client;

import java.io.*;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

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
	final private SocketChannel channel;
	
	/**
	 * 
	 */
	final private Selector selector;

	/**
	 * To stop the run loop.
	 */
	private boolean running;
	
	/**
	 * 
	 */
	private int count;

	/**
	 * For debug purposes
	 */
	final private boolean debug = true;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the node.
	 * 
	 * @param serverAddress
	 *            the server's host name.
	 * @param serverPort
	 *            the port number.
	 */
	public Sender(Selector selector, SocketChannel channel) {
		this.selector = selector;
		this.channel = channel;
		this.count = 0;
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
	final private void sendToServer(Message msg) throws IOException {
		if (channel == null)
			throw new SocketException("Connection error when sending.");
		if (debug)
			System.out.println("sending: " + msg);
		synchronized (channel) {
			channel.write(msg.getMessage());
		}
	}

	// ACCESSING METHODS ------------------------------------------------

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
	final synchronized public void incrementCount() {
		this.count++;
	}
	
	/**
	 * 
	 * @return
	 */
	final synchronized public String getCount() {
		return "Number of messages sent: "+this.count;
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
				this.selector.select();
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while(keyIterator.hasNext()) {
				    SelectionKey key = keyIterator.next();
				    if (key.isValid() == false) {
				    	continue;
				    } else if (key.isWritable()) {
			    		this.sendToServer(new Message());
				    }
				    keyIterator.remove();
				}
				this.sleep(4000);
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
