package cs455.scaling.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author G van Andel
 *
 * @see cs455.overlay.node
 * 
 */

public class Client {

	// Instance variables **********************************************
	
	/**
	 * 
	 */
	private Selector selector;
	
	/**
	 * 
	 */
	private SelectionKey key;
	
	/**
	 * 
	 */
	private SocketChannel channel;
	
	/**
	 * 
	 */
	private final Sender sender;
	
	/**
	 * 
	 */
	private final Receiver receiver;
	
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param client
	 * 		The client that connects to the registry.
	 * @param server
	 * 		The server for the messaging nodes to connect to.
	 */

	public Client(String serverAddress, int serverPort, int messageRate) throws IOException {
		// Selector: multiplexor of SelectableChannel objects
		this.selector = SelectorProvider.provider().openSelector();
		
		// Create a new non-blocking socket channel
		this.channel = SocketChannel.open();
		this.channel.configureBlocking(false);
		this.channel.connect(new InetSocketAddress(serverAddress, serverPort));
		 
		// Adjusts this channel's ops
		this.channel.register(this.selector, SelectionKey.OP_CONNECT);
		
		//
		this.selector.select();
		this.key = selector.selectedKeys().iterator().next();
		// Finish the connection. If the connection operation failed
		// this will raise an IOException.
		if (this.key.isConnectable() == false || this.channel.finishConnect() == false)
			throw new IOException("Failed to connect to server.");

		// Register an interest in read on this channel
		this.key.interestOps(SelectionKey.OP_READ);
		
		// key is readable
		this.receiver = new Receiver(this.key);
		this.receiver.start();
		this.sender = new Sender(this.key, messageRate);
		this.sender.start();
	}
	
	// Instance methods ************************************************
	
	public void exec() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date current = new Date();
		while (true) {
			Date time = new Date();
			if ((time.getTime() - current.getTime()) > 5000) {
				System.out.println("[ "+dateFormat.format(time)+" ] "+sender.getInfo()+receiver.getInfo());
				// [timestamp] Total Sent Count: x, Total Received Count: y 
				current = time;
			}
		}
	}
	
	public static void main(String args[]) {
		
		int messageRate = 0;
		int registryPort = 0;
		String registryIP = null;
		
		try {
			registryIP = args[0];
		} catch (IndexOutOfBoundsException ex) {
			registryIP = "saturn";
		}
		
		try {
			registryPort = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			System.err.println("Error: registry port must be a number.");
			System.exit(1);
		} catch (IndexOutOfBoundsException ex) {
			registryPort = 60100;
		}
		
		try {
			messageRate = Integer.parseInt(args[2]);
		} catch (NumberFormatException ex) {
			System.err.println("Error: massage rate must be a number. Ex 3.");
			System.exit(1);
		} catch (IndexOutOfBoundsException ex) {
			messageRate = 1;
		}

		Client node = null;
		try {
			node = new Client(registryIP, registryPort, messageRate);
		} catch (IOException e) {
			System.err.println("An error occured while connecting to server.");
			System.exit(1);
		}
		System.out.println("Client has succsesfully connected to server and will now send messages.");
		node.exec();
		
	}
	
}
