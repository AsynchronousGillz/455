package cs455.overlay.node.registry;

import java.io.*;

/**
 * 
 * @author G van Andel
 *
 * @see cs455.overlay.node.Registry;
 */

public class RegistryInterface extends Thread {
	// Class variables *************************************************

	final public static boolean debug = false;

	// Instance variables **********************************************

	RegistryServer server;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host
	 * 		The server to connect to.
	 * @param portSimple Chat/OCSF Phase: 2
	 * 		The port number to connect on.
	 * @param serverUI
	 * 		The interface type variable.
	 */

	public RegistryInterface(int port) throws IOException {
		server = new RegistryServer(port);
	}

	// Instance methods ************************************************
	
	public void run() {
		this.display("Registry Server Console");
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message;
			while (true) {
				message = fromConsole.readLine();
				if (message.equals(""))
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
			case "getPort":
				if (tokens.length == 1) {
					display(server.getPort());
				} else {
					this.invalid(message);
				}
				break;
			case "getHost":
				if (tokens.length == 1) {
					display(server.getHost());
				} else {
					this.invalid(message);
				}
				break;
			default:
				this.invalid(message);
		}
	}
	
	/**
	 * It closes the server.
	 */
	public void shutdown() throws IOException {
		server.stopListening();
	}
	
	/**
	 * It displays a message onto the screen.
	 *
	 * @param message
	 *            The string to be displayed.
	 */
	public void display(String message) {
		System.out.println(message);
	}
	
	/**
	 * It displays an error onto the screen.
	 *
	 * @param message
	 *            The string to be displayed with error format.
	 */
	public void error(String message) {
		System.err.println(message);
	}
	
	/**
	 * It displays small help onto the screen.
	 *
	 * @param message
	 *            The string to be displayed with error format.
	 */
	public void invalid(String message) {
		String info = "invalid command \"" + message + "\" try: [ #quit | #stop | #close | #help ]";
		System.err.println(info);
	}
	
	/**
	 * It displays an error onto the screen.
	 *
	 */
	public void help() {
		// TODO Read usage file
		System.err.println("");
	}

}
