package cs455.overlay.node;

/**
 * 
 * @author G van Andel
 *
 */

import java.io.IOException;

public class Registry {

	public static void main(String [] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			port = 60100;
		}
		
		RegistryServer server = null;
		
		try {
			server = new RegistryServer(port);
			server.listen();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		
		try {
			Thread s = new RegistryInterface(server);
			s.start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
