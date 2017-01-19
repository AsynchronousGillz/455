// File Name RegistryServer.java
package cs455.overlay.node;

/**
 * 
 * @author G van Andel
 *
 * @see AbstractServer
 * 
 */

import java.net.*;
import java.text.*;
import java.util.*;
import java.io.*;

public class RegistryServer extends AbstractServer {
	
	/**
	 * The connection listener thread.
	 */
	private RegistryList serverList;

	// CONSTRUCTOR ******************************************************

	public RegistryServer(int port) throws IOException {
		super(port);
	}
	
	// INSTANCE METHODS *************************************************


	public void nodeConnected(NodeConnection nodeConnection) {
		NodeAddress node = nodeConnection.getAddress();
		System.out.println("Node connected from: "+node);
	}

	synchronized public void nodeDisconnected(Socket nodeSocket) {
		NodeAddress node = serverList.getNode(nodeSocket.getInetAddress(), nodeSocket.getPort());
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(node+" disconnected at "+dateFormat.format(date));
		try {
			serverList.removeFromList(node);
		} catch (Exception e) {}
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
	
	public ArrayList<String> getList() {
		return serverList.getList();
	}

	@Override
	protected void MessageFromNode(Object msg, NodeConnection client) {
		if (msg instanceof Message == false)
			return;
		Message m = (Message) msg;
		System.out.println(m);	
		
	}

}

