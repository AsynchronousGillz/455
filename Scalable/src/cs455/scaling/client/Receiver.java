package cs455.scaling.client;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Iterator;

import cs455.scaling.msg.Hash;

/**
 * 
 * @author G van Andel
 *
 * @see Client.NodeMain
 */

public class Receiver extends Thread {

	// INSTANCE VARIABLES ***********************************************

	/**
	 * 
	 */
	final private Selector selector;

	/**
	 * 
	 */
	private SelectionKey key;

	/**
	 * 
	 */
	private Integer receivedCount;

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
	public Receiver(SelectionKey key) throws IOException {
		this.selector = key.selector();
		this.key = key;
		this.receivedCount = 0;
		super.setName("Receiver-"+super.getId());
	}

	// METHODS -------------------------------------------------------

	/**
	 * Read bytes from the server. This is the only way that methods should
	 * communicate with the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when sending
	 */
	final private void readFromServer(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		int read = 0;
		ByteBuffer bytes = ByteBuffer.allocate(Hash.size);
		try {
			while (bytes.hasRemaining() && read != -1 && channel != null) {
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
		this.incrementReceived();
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * @return true if the node is connnected.
	 */
	final public boolean isConnected() {
		return this.isAlive() && this.running;
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
		String ret = ", Total Received Count: ";
		synchronized (this.receivedCount) {
			ret += this.receivedCount;
			this.receivedCount = 0;
		}
		// Total Sent Count: x, Total Received Count: y
		return ret;
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
				    this.key = keyIterator.next();
					if (this.key.isValid() == false) {
						continue;
					} else if (this.key.isReadable()) {
						this.readFromServer(this.key);
					}
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

		System.exit(mode);
	}

	// ----------------------------------------------------------

}
