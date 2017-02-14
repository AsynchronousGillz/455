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

	private RegistryServer server;

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
			case "list-messaging": case "list":
				if (tokens.length == 1) {
					System.out.print(server.getList());
				} else if (tokens.length == 2) {
					System.out.print(server.getNode(tokens[1]));
				} else {
					this.invalid(message);
				}
				break;
			case "setup-overlay": case "setup":
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
					System.out.println(server.displayOverlay());
				} else {
					this.invalid(message);
				}
				break;
			case "send-overlay-link-weights": case "send":
				if (tokens.length == 1) {
					server.sendOverlay();
				} else {
					this.invalid(message);
				}
				break;
			case "start":
				if (tokens.length == 2) {
					server.sendStart(server.validateInput(tokens[1]));
				} else {
					this.invalid(message);
				}
				break;
			case "print-stats": case "print":
				if (tokens.length == 1) {
					System.out.println(server.getStats());
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
		String info = "invalid command \"" + message + "\" try:\n";
		info += "\t[ setup-overlay | send-overlay-link-weights | start # ]\n";
		info += "\t[ list-messaging | list-weights | print-stats | get-port | get-host ]";
		System.err.println(info);
	}

	
	/**
	 * It closes the server.
	 */
	public void shutdown() throws IOException {
		server.interrupt();
	}

	public static void main(String [] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			System.err.println("Port argument must be a number.");
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException ex) {
			port = 60100;
		}
		
		RegistryServer server = null;
		
		try {
			server = new RegistryServer(port);
			server.listen();
			server.start();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		
		Registry r = new Registry(server);
		r.runConsole();
	}
	
}
