package cs455.overlay.node;

import java.io.IOException;

/**
 * 
 * @author G van Andel
 *
 * @see cs455.overlay.node
 * 
 */

public class MessagingNode {

	public boolean debug = false;
	
	public static void main(String args[]) {
		
		int registryPort = 0;
		String registryIP = null;
		
		try {
			registryIP = args[0];
		} catch (IndexOutOfBoundsException ex) {
			registryIP = "venus";
		}
		
		try {
			registryPort = Integer.parseInt(args[1]);
		} catch (IndexOutOfBoundsException ex) {
			registryPort = 60100;
		}
		
		NodeServer nodeServer = null;

		try {
			nodeServer = new NodeServer();
			nodeServer.listen();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		
		NodeClient nodeClient = new NodeClient(nodeServer, registryIP, registryPort);
		
		NodeInterface ni = new NodeInterface(nodeClient, nodeServer);
		ni.start();
		
	}
	
}
