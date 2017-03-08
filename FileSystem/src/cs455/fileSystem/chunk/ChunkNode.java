package cs455.fileSystem.chunk;

import java.io.IOException;


/**
 * 
 * @author G van Andel
 *
 * @see cs455.overlay.node
 * 
 */

public class ChunkNode {

	// Instance variables **********************************************

	private final ChunkServer server;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param client
	 * 		The client that connects to the registry.
	 * @param server
	 * 		The server for the messaging nodes to connect to.
	 */

	public ChunkNode(ChunkServer server, String host, int port) {
		this.server = server;
		this.server.addConnection(host, port);
	}

	// Instance methods ************************************************
	
	public void runConsole() {
		try {
			while (true) {
				System.out.println(server.getInfo());
				Thread.sleep(10000);
			}
		} catch(Exception e) {
			System.out.println("Unexpected error while reading from console!");
		}
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
		} catch (NumberFormatException ex) {
			System.err.println("Error: port must be a number.");
		}
		
		ChunkServer nodeServer = null;

		try {
			nodeServer = new ChunkServer();
			nodeServer.listen();
			nodeServer.start();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		
		ChunkNode node = new ChunkNode(nodeServer, registryIP, registryPort);
		node.runConsole();
		
	}
	
}
