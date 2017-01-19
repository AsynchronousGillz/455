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
			registryIP = "127.0.0.1";
		}
		
		try {
			registryPort = Integer.parseInt(args[1]);
		} catch (IndexOutOfBoundsException ex) {
			registryPort = 60100;
		}
		
		NodeServer ns = null;

		try {
			ns = new NodeServer();
			ns.listen();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		
		NodeClient nc = new NodeClient(registryIP, registryPort, ns.getPort());
		nc.run();
		
		NodeInterface ni = new NodeInterface(nc, ns);
		ni.run();
		
	}
	
}
