package cs455.overlay.node;

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

public class MessagingNode {

	// Instance variables **********************************************

	private final MessagingClient client;
	private final MessagingServer server;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param client
	 * 		The client that connects to the registry.
	 * @param server
	 * 		The server for the messaging nodes to connect to.
	 */

	public MessagingNode(MessagingClient client, MessagingServer server) {
		this.client = client;
		this.server = server;
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
			case "exit-overlay": case "exit":
				if (tokens.length == 1) {
					client.unregister();
					exit();
				} else {
					this.invalid(message);
				}
				break;
			case "get-stats": case "stats":
				if (tokens.length == 1) {
					System.out.println(server.getStats());
				} else {
					this.invalid(message);
				}
				break;
			case "get-paths":
				if (tokens.length == 1) {
					String[] info = server.getConnectionNames();
					for (String i : info)
						System.out.println(i);
				} else {
					this.invalid(message);
				}
				break;
			case "print-shortest-path": case "print":
				if (tokens.length == 1) {
					System.out.print(server.getShortestPath());
				} else {
					this.invalid(message);
				}
				break;
			case "print-cost": case "cost":
				if (tokens.length == 1) {
					System.out.print(server.getNodeCost());
				} else {
					this.invalid(message);
				}
				break;
			case "get-port":
				if (tokens.length == 1) {
					System.out.println(server.getPort());
				} else {
					this.invalid(message);
				}
				break;
			case "get-host":
				if (tokens.length == 1) {
					System.out.println(server.getHost());
				} else {
					this.invalid(message);
				}
				break;
			default:
				this.invalid(message);
		}
	}
	
	/**
	 * Build the shortest path string.
	 */
	public String getShortestPath() {
		return server.getShortestPath();
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
		
		MessagingServer nodeServer = null;

		try {
			nodeServer = new MessagingServer();
			nodeServer.listen();
			nodeServer.start();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		
		MessagingClient nodeClient = new MessagingClient(nodeServer, registryIP, registryPort);
		nodeClient.start();
		
		MessagingNode node = new MessagingNode(nodeClient, nodeServer);
		node.runConsole();
		
	}
	
}
