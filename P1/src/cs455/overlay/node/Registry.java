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
		int port = 0;
		try {
			port = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			port = 60100;
		}
		try {
			Thread s = new RegistryInterface(port);
			s.start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
