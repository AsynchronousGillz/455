package cs455.overlay.node;

/**
 * 
 * @author G van Andel
 *
 * @see cs455.overlay.registry.RegistryInterface
 * 
 */

import java.io.*;

public class MessagingNode {

	public boolean debug = false;
	
	public static void main(String args[]) {
		
		int registryPort = 0;
		String registryIP = null;
		
		try {
			registryIP = args[1];
		} catch (IndexOutOfBoundsException ex) {
			registryIP = "127.0.0.1";
		}
		
		try {
			registryPort = Integer.parseInt(args[2]);
		} catch (IndexOutOfBoundsException ex) {
			registryPort = 60100;
		}
		
		NodeClient nc = new NodeClient(registryIP, registryPort);
		nc.run();
		
		
		int serverPort = 60101;	
		NodeServer ns = new NodeServer(serverPort);
		try {
			ns.listen();
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}
	
}
