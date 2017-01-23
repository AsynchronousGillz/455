// File Name RegistryServer.java
package cs455.overlay.node;

/**
 * 
 * @author G van Andel
 *
 * @see AbstractServer
 * 
 */

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
		serverList = new RegistryList(4);
	}
	
	// INSTANCE METHODS *************************************************


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
		try {
			serverList.removeFromList(nodeConnection.getAddress());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.exit(1);
	}

	public void serverStarted() {
		System.out.println("Registry server started "+getName());
	}

	protected void serverClosed() {
		System.out.println("serverStopped :: Exitting.");
	}
	
	public String getList() {
		return serverList.getList();
	}
	
	public void sendRegistrationResponse(boolean status, NodeConnection client) {
		String message = (status)?"True":"False";
		Message m = new Message(message);
		m.setType("REGISTER_RESPONSE");
		try {
			client.sendToNode(m);
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		if (status == false)
			client.close();
	}
	
	public void registerNode(Message m, NodeConnection client) {
		String[] tokens = m.getMessage().split(" ");
		String clientAddress = client.getInetAddress().getHostAddress();
		if (clientAddress.equals(tokens[0]) ^ getHost().equals(tokens[0]) == false)
			sendRegistrationResponse(false, client);
		int clientPort = Integer.parseInt(tokens[1]);
		serverList.addToList(new NodeAddress(client.getNodeSocket(), tokens[0], clientPort));
		sendRegistrationResponse(true, client);
	}
	
	public void unregisterNode(Message m, NodeConnection client) {
		String[] tokens = m.getMessage().split(" ");
		String clientAddress = client.getInetAddress().getHostAddress();
		if (tokens[0].equals(clientAddress) == false)
			sendRegistrationResponse(false, client);
		int clientPort = Integer.parseInt(tokens[1]);
		try {
			serverList.removeFromList(new NodeAddress(client.getNodeSocket(), tokens[0], clientPort));
		} catch (Exception e) {
			System.err.println(e.toString());
		}
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
				registerNode(m, client);
				break;
			case "DEREGISTER_REQUEST":
				unregisterNode(m, client);
				break;
			default:
		}
	}

}

