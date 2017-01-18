package cs455.overlay.node;

/**
 * 
 * @author G van Andel
 *
 */

import java.io.IOException;

import cs455.overlay.node.registry.*;

public class Registry {

	public static void main(String [] args) {
		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			port = 5555;
		}
		try {
			Thread s = new RegistryServer(port);
			s.start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
