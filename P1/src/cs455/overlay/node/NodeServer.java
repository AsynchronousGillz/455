// File name Server.java
package cs455.overlay.node;

import java.text.*;
import java.util.*;
import java.io.*;

import cs455.overlay.msg.*;

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
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" connected at "+dateFormat.format(date));
	}

	synchronized public void nodeDisconnected(NodeConnection nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" disconnected at "+dateFormat.format(date));
	}

	/**
	 * 
	 */
	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.exit(1);
	}

	/**
	 * 
	 */
	public void serverStarted() {
		System.out.println("Node server started "+this.getName());
	}
	
	/**
	 * 
	 */
	protected void serverClosed() {
		System.out.println("serverStopped :: Exitting.");
	}
	
	/**
	 * 
	 * @return
	 */
	public String getShortestPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void MessageFromNode(Object msg, NodeConnection client) {
		if (msg instanceof NodeMessage == false)
			return;
		NodeMessage m = (NodeMessage) msg;
		if (debug)
			System.out.println(m);
		switch(m.getStringType()) {
			case "DEBUG":
				break;
			default:
		}
	}
	
}
// End of AbstractServer Class
