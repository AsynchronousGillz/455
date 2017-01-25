package cs455.overlay.node;


import java.io.*;

/**
 * 
 * @author G van Andel
 *
 * @see cs455.overlay.node.Registry;
 */

public class RegistryInterface {
	// Class variables *************************************************

	final public static boolean debug = false;

	// Instance variables **********************************************

	RegistryServer server;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the registry server.
	 *
	 * @param server
	 * 		The server to connect to.
	 */

	public RegistryInterface(RegistryServer server) {
		this.server = server;
	}

	// Instance methods ************************************************
	
	public void runConsole() {
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
			case "listNodes":
				if (tokens.length == 1) {
					System.out.println(server.getList());
				} else {
					this.invalid(message);
				}
				break;
			case "showOverlay":
				if (tokens.length == 1) {
					System.out.println(server.getOverlay());
				} else {
					this.invalid(message);
				}
				break;
			case "getPort":
				if (tokens.length == 1) {
					System.out.println(server.getPort());
				} else {
					this.invalid(message);
				}
				break;
			case "getHost":
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
	 * It closes the server.
	 */
	public void shutdown() throws IOException {
		server.stopListening();
	}
	
	/**
	 * It displays small help onto the screen.
	 *
	 * @param message
	 *            The string to be displayed with error format.
	 */
	public void invalid(String message) {
		String info = "invalid command \"" + message + "\" try: [ showOverlay | listNodes | getPort | getHost ]";
		System.err.println(info);
	}

}
