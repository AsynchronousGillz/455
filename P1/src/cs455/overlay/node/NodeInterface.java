package cs455.overlay.node;


import java.io.*;

/**
 * 
 * @author G van Andel
 *
 * @see cs455.overlay.node.Registry;
 */

public class NodeInterface extends Thread {
	// Class variables *************************************************

	/**
	 * 
	 */
	private boolean stop = false;

	/**
	 * 
	 */
	final public static boolean debug = false;

	// Instance variables **********************************************

	NodeClient client;
	NodeServer server;

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

	public NodeInterface(NodeClient client, NodeServer server) {
		this.client = client;
		this.server = server;
	}

	// Instance methods ************************************************
	
	public void run() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message;
			while (stop == false) {
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
			case "de-register":
				if (tokens.length == 1) {
					client.unregister();
					exit();
				} else {
					this.invalid(message);
				}
				break;
			case "print-shortest-path":
				if (tokens.length == 1) {
					server.getShortestPath();
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
		String ret = "";
		ret += server.getShortestPath();
		return ret;
	}
	
	/**
	 * It closes the program.
	 */
	public void exit() {
		client.close();
		server.serverClosed();
		System.exit(0);
	}
	
	/**
	 * It displays small help onto the screen.
	 *
	 * @param message
	 *            The string to be displayed with error format.
	 */
	public void invalid(String message) {
		String info = "invalid command \"" + message + "\" try: [ getPort | getHost ]";
		System.err.println(info);
	}

	public void close() {
		// TODO
	}

}
