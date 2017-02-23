package cs455.scaling.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	 * The server's host name.
	 */
	final private String serverAddress;

	/**
	 * The port number.
	 */
	final private int serverPort;
	
	/**
	 * 
	 */
	private final Receiver receiver;
	
	/**
	 * 
	 */
	private final Sender sender;


	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param client
	 * 		The client that connects to the registry.
	 * @param server
	 * 		The server for the messaging nodes to connect to.
	 */

	public Client(Receiver receiver, String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.receiver = receiver;
		this.sender = receiver.makeSender();
		this.sender.start();
	}

	// Instance methods ************************************************
	
	public void runConsole() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message = null;
			while (true) {
				message = fromConsole.readLine();
				if (message.trim().equals(""))
					continue;
				this.getAction(message);
			}
		} catch(Exception e) {
			System.out.println("Unexpected error while reading from console!");
		}
	}
	
	/**
	 *            
	 * This method implements the commands for the server. Called when a command 
	 * is entered into the server while listening for connections.
	 *
	 * @param message
	 *            The message received from the client with a # at index 0.
	 */

	private void getAction(String message){
		String[] tokens = message.split(" ");
		switch(tokens[0]){
			case "get-count":
				if (tokens.length == 1) {
					System.out.println(sender.getCount());
				} else {
					this.invalid(message);
				}
				break;
			case "get-server":
				if (tokens.length == 1) {
					System.out.println(serverAddress+":"+serverPort);
				} else {
					this.invalid(message);
				}
				break;
			case "get-name":
				if (tokens.length == 1) {
					System.out.println(receiver.getName());
				} else {
					this.invalid(message);
				}
				break;
			case "exit":
				if (tokens.length == 1) {
					this.exit();
				} else {
					this.invalid(message);
				}
				break;
			default:
				this.invalid(message);
		}
	}
	
	/**
	 * It closes the program.
	 */
	public void exit() {
		this.receiver.closeConnection();
		System.exit(0);
	}
	
	/**
	 * It displays small help onto the screen.
	 *
	 * @param message
	 *            The string to be displayed with error format.
	 */
	public void invalid(String message) {
		String info = "invalid command \"" + message + "\" try:\n";
		info += "\t[ get-name ]\n";
		info += "\t[ exit ]";
		System.err.println(info);
	}
	
	public static void main(String args[]) {
		
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
		
		Receiver nodeClient = null;
		try {
			nodeClient = new Receiver(registryIP, registryPort);
		} catch (IOException e) {
			System.err.println("An error occured while connecting to server.");
			System.exit(1);
		}
		System.out.println("Client has succsesfully connected to server and will now send messages.");
		nodeClient.start();
		
		Client node = new Client(nodeClient, registryIP, registryPort);
		node.runConsole();
		
	}
	
}
