// File name Server.java
package cs455.overlay.node;

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

	public void nodeConnected(NodeConnection nodeConnection) {
		NodeAddress node = nodeConnection.getAddress();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(node+" connected at "+dateFormat.format(date));
	}

	synchronized public void nodeDisconnected(NodeConnection nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" disconnected at "+dateFormat.format(date));
	}

	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.exit(1);
	}

	public void serverStarted() {
		System.out.println("Node server started "+this.getName());
	}

	protected void serverClosed() {
		System.out.println("serverStopped :: Exitting.");
	}
	
	@Override
	protected void MessageFromNode(Object msg, NodeConnection client) {
		if (msg instanceof Message == false)
			return;
		Message m = (Message) msg;
		if (debug)
			System.out.println(m);
		switch(m.getStringType()) {
			case "REGISTER_REQUEST":
				break;
			default:
		}
	}
	
}
// End of AbstractServer Class
