package cs455.scaling.client;

import java.io.IOException;
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
	private final Connection receiver;
	
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param client
	 * 		The client that connects to the registry.
	 * @param server
	 * 		The server for the messaging nodes to connect to.
	 */

	public Client(Connection receiver) {
		this.receiver = receiver;
	}

	// Instance methods ************************************************
	
	public void exec() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date current = new Date();
		while (true) {
			Date time = new Date();
			if ((time.getTime() - current.getTime()) > 5000) {
				System.out.println("[ "+dateFormat.format(time)+" ] "+receiver.getInfo());
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
		} catch (IndexOutOfBoundsException ex) {
			registryPort = 60100;
		}
		
		try {
			messageRate = Integer.parseInt(args[2]);
		} catch (IndexOutOfBoundsException ex) {
			messageRate = 1;
		}
		
		Connection nodeClient = null;
		try {
			nodeClient = new Connection(registryIP, registryPort, messageRate);
		} catch (IOException e) {
			System.err.println("An error occured while connecting to server.");
			System.exit(1);
		}
		System.out.println("Client has succsesfully connected to server and will now send messages.");
		nodeClient.start();
		
		Client node = new Client(nodeClient);
		node.exec();
		
	}
	
}
