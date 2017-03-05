package cs455.scaling.server;

/**
 * 
 * @author G van Andel
 *
 */

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
	
	// Class variables *************************************************

	final public boolean debug = false;

	// Instance variables **********************************************

	private NioServer server;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the registry server.
	 *
	 * @param server
	 * 		The server for the nodes to connect to.
	 */

	public Server(NioServer server) {
		this.server = server;
	}

	// Instance methods ************************************************
	
	public void exec() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		while (true) {
			Date current = new Date();
			System.out.println("[ "+dateFormat.format(current)+" ] "+this.server.getInfo(current));
			// [timestamp] Total Sent Count: x, Total Received Count: y 
			if (debug)
				System.out.println(this.server.getQueueStatus());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * It closes the server.
	 */
	public void shutdown() throws IOException {
		server.interrupt();
	}

	public static void main(String [] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			System.err.println("Error: Port argument must be a number.");
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException ex) {
			port = 60100;
		}
		
		int poolSize = 0;
		try {
			poolSize = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			System.err.println("Error: Thread pool size argument must be a number.");
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException ex) {
			poolSize = 100;
		}
		
		NioServer server = null;
		
		try {
			server = new NioServer(port, poolSize);
			server.start();
		} catch (IOException ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
		}
		
		Server r = new Server(server);
		r.exec();
	}
	
}
