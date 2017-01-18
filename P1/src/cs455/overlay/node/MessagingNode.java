package cs455.overlay.node;

import java.io.*;

public class MessagingNode {

	public boolean debug = false;
	
	public static void main(String args[]) {
		
		int port = 0;
		
		try {
			port = Integer.parseInt(args[1]);
		} catch (IndexOutOfBoundsException ex) {
			port = 5555;
		}
		
		
		NodeServer ns = new NodeServer(5555);
		try {
			ns.listen();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		NodeClient nc = new NodeClient("127.0.0.1", port);
		nc.run();
		
	}
	
}
