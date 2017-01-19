// File name Server.java
package cs455.overlay.node;

import java.net.*;
import java.text.*;
import java.util.*;
import java.io.*;


/**
 * 
 * @author G van Andel
 *
 * @see MessagingNode.NodeMain
 * @see AbstractServer
 */

public class NodeServer extends AbstractServer {


	// CONSTRUCTOR ******************************************************
	
	/**
	 * Constructs a new server.
	 *
	 */
	public NodeServer() throws IOException {
		super(0);
	}

	public void nodeConnected(Socket nodeSocket) {
		NodeAddress node = new NodeAddress(nodeSocket.getInetAddress(), nodeSocket.getPort());
		System.out.println("Node connected from: "+node);
	}

	synchronized public void nodeDisconnected(Socket nodeSocket) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("Node disconnected at "+dateFormat.format(date));
	}

	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.exit(1);
	}

	public void serverStarted() {
		System.out.println("serverStarted :: "+this.getPort());
	}

	protected void serverClosed() {
		System.out.println("serverStopped :: Exitting.");
	}

	@Override
	protected void MessageFromNode(Object msg, NodeConnection client) {
		// TODO Auto-generated method stub
		
	}

}
// End of AbstractServer Class
