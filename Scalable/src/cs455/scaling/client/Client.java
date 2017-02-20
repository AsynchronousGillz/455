package cs455.scaling.client;

import java.io.BufferedReader;
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

	private final ClientConnection client;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param client
	 * 		The client that connects to the registry.
	 * @param server
	 * 		The server for the messaging nodes to connect to.
	 */

	public Client(ClientConnection client) {
		this.client = client;
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
				getAction(message);
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
			case "get-name":
				if (tokens.length == 1) {
					System.out.println(client.getName());
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
		info += "\t[ print-shortest-path | get-port | get-host ]\n";
		info += "\t[ print-cost | get-paths | exit-overlay ]";
		System.err.println(info);
	}
	
	public static void main(String args[]) {
		
		int registryPort = 0;
		String registryIP = null;
		
		try {
			registryIP = args[0];
		} catch (IndexOutOfBoundsException ex) {
			registryIP = "mars";
		}
		
		try {
			registryPort = Integer.parseInt(args[1]);
		} catch (IndexOutOfBoundsException ex) {
			registryPort = 60100;
		}
		
		ClientConnection nodeClient = new ClientConnection(registryIP, registryPort);
		nodeClient.start();
		
		Client node = new Client(nodeClient);
		node.runConsole();
		
	}
	
}
