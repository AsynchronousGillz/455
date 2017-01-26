package cs455.overlay.node;

import java.io.BufferedReader;

/**
 * 
 * @author G van Andel
 *
 */

import java.io.IOException;
import java.io.InputStreamReader;

public class Registry {
	
	// Class variables *************************************************

	final public static boolean debug = false;

	// Instance variables **********************************************

	RegistryServer server;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the registry server.
	 *
	 * @param server
	 * 		The server for the nodes to connect to.
	 */

	public Registry(RegistryServer server) {
		this.server = server;
	}

	// Instance methods ************************************************
	
	public void runConsole() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message;
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
			case "list-messaging":
				if (tokens.length == 1) {
					System.out.println(server.getList());
				} else if (tokens.length == 2) {
					System.out.println(server.getNode(tokens[1]));
				} else {
					this.invalid(message);
				}
				break;
			case "setup-overlay":
				if (tokens.length == 1) {
					server.makeOverlay();
				} else if (tokens.length == 2) {
					int numberConnections = server.validateInput(tokens[1]);
					server.makeOverlay(numberConnections);
				} else {
					this.invalid(message);
				}
				break;
			case "list-weights":
				if (tokens.length == 1) {
					System.out.println(server.getOverlay());
				} else {
					this.invalid(message);
				}
				break;
			case "send-overlay-link-weights":
				if (tokens.length == 1) {
					server.sendOverlay();
				} else {
					this.invalid(message);
				}
				break;
			case "start":
				if (tokens.length == 2) {
					int numberOfRounds = server.validateInput(tokens[1]);
					server.sendStart(numberOfRounds);
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
	 * It displays small help onto the screen.
	 *
	 * @param message
	 *            The string to be displayed with error format.
	 */
	public void invalid(String message) {
		String info = "invalid command \"" + message + "\" try: ";
		info += "\t[ send-overlay-link-weights | list-messaging | setup-overlay ]\n";
		info += "\t[ list-messaging | list-weights | get-port | get-host ]\n";
		System.err.println(info);
	}

	
	/**
	 * It closes the server.
	 */
	public void shutdown() throws IOException {
		server.stopListening();
	}

	public static void main(String [] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			port = 60100;
		}
		
		RegistryServer server = null;
		
		try {
			server = new RegistryServer(port);
			server.listen();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		
		Registry r = new Registry(server);
		r.runConsole();
	}
	
}
