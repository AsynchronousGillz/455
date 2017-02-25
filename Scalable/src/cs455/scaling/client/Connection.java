package cs455.scaling.client;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

import cs455.scaling.msg.Message;

/**
 * 
 * @author G van Andel
 *
 * @see Client.NodeMain
 */

public class Connection extends Thread {

	// INSTANCE VARIABLES ***********************************************

	/**
	 * 
	 */
	private SocketChannel channel;
	
	/**
	 * 
	 */
	private Selector selector;
	
	/**
	 * 
	 */
	private Integer sentCount;
	
	/**
	 * 
	 */
	private Integer receivedCount;
	
	/**
	 * 
	 */
	private List<String> hashList;
	
	/**
	 * 
	 */
	final private int messageRate;
	
	/**
	 * To stop the run loop.
	 */
	private boolean running;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the node.
	 * 
	 * @param serverAddress
	 *            the server's host name.
	 * @param serverPort
	 *            the port number.
	 */
	public Connection(String serverAddress, int serverPort, int messageRate) throws IOException {
		this.messageRate = messageRate;
		// Selector: multiplexor of SelectableChannel objects
		this.selector = SelectorProvider.provider().openSelector();
		
		// Create a new non-blocking socket channel
		this.channel = SocketChannel.open();
		this.channel.configureBlocking(false);
		this.channel.connect(new InetSocketAddress(serverAddress, serverPort));
		 
		// Adjusts this channel's ops
//		channel.register(this.selector, channel.validOps());
		channel.register(this.selector, SelectionKey.OP_CONNECT);
		this.sentCount = this.receivedCount = 0;
		this.hashList = new ArrayList<String>();
	}
	
	/**finishConnect
	 * Read bytes from the server. This is the only way that methods should
	 * communicate with the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when sending
	 */
	final private void readFromServer(SelectionKey key) throws IOException {
		if (channel == null)
			throw new SocketException("Connection error when sending.");
		int read = 0;
		ByteBuffer bytes = ByteBuffer.allocate(8192);
		try {
			while (bytes.hasRemaining() && read != -1 && channel != null){
				read = channel.read(bytes);
			}
			if (read == -1)
				throw new IOException();
			if (channel == null)
				throw new Exception();
		} catch (IOException e) {
			this.close(0);
		} catch (Exception e) {
			this.close(1);
		}
		System.out.println("HOLY SHIT THIS WILL NEVER PRINT");
		hashList.remove(new Message(bytes).toHash());
		this.incrementReceived();
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
	final private void sendToServer(SelectionKey key, Message msg) throws IOException {
		if (channel == null)
			throw new SocketException("Connection error when sending.");
		this.hashList.add(msg.toHash());
		synchronized (channel) {
			channel.write(msg.getMessage());
		}
		this.incrementSent();
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * @return true if the node is connnected.
	 */
	final public boolean isConnected() {
		return this.isAlive() && this.running;
	}
	
	/**
	 * TODO Comment incrementSent
	 */
	final public void incrementSent() {
		synchronized (sentCount) {
			this.sentCount++;	
		}
	}
	
	/**
	 * TODO Comment incrementReceived
	 */
	final public void incrementReceived() {
		synchronized (receivedCount) {
			this.receivedCount++;	
		}
	}
	
	/**
	 * TODO Comment getInfo
	 */
	final public String getInfo() {
		String ret = "Total Sent Count: ";
		synchronized (this.sentCount) {
			ret += this.sentCount + ", Total Received Count: ";
			this.sentCount = 0;
		}
		synchronized (this.receivedCount) {
			ret += this.receivedCount;
			this.receivedCount = 0;
		}
		// Total Sent Count: x, Total Received Count: y 
		return ret;
	}
	
	/**
	 * Causes the server to stop accepting new connections.
	 */
	final private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	// RUN METHOD -------------------------------------------------------

	/**
	 * Waits for messages from the server. When each arrives!
	 */
	public void run() {
		connectionEstablished();
		try {
			while (this.running == true) {
				this.selector.select();				
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while(keyIterator.hasNext()) {
				    SelectionKey key = keyIterator.next();
				    if (key.isValid() == false) {
				    	continue;
				    } else if (key.isConnectable()) {
						this.channel.finishConnect();
						key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				    } else if (key.isReadable()) {
				    	this.readFromServer(key);
				    } else if (key.isWritable()) {
			    		this.sendToServer(key, new Message());
				    }
					this.sleep(1000 / messageRate);
					// generate packets is messageRate per-second;
					keyIterator.remove();
				}
			}
		} catch (Exception exception) {
			connectionException(exception);
			close(1);
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
		exception.printStackTrace();
	}

	/**
	 * Hook method called after a connection has been established.
	 */
	private void connectionEstablished() {
		this.running = true;
	}
	
	/**
	 * Hook method to stop the client.
	 */
	public void closeConnection() {
		this.running = false;
	}

	// METHODS TO BE USED FROM WITHIN THE FRAMEWORK ONLY ----------------

	/**
	 * Closes the node. If the connection is already closed, this call has no
	 * effect.
	 * 
	 * @param node
	 *            0 for exit 1 for error
	 * @exception IOException
	 *                if an error occurs when closing the socket.
	 */
	final private void close(int mode) {
		if (mode == 0)
			System.out.println("Disconnecting from the server. Exitting.");
		else if (mode == 1)
			System.out.println("An error occured. Exitting.");
		else
			System.err.println("An error occured connecting to the server. Exitting.");

		try {
			channel.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			System.exit(mode);
		}
	}

	// ----------------------------------------------------------


}
