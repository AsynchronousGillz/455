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

import cs455.overlay.msg.*;

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
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection.getAddress()+" connected at "+dateFormat.format(date));
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
		System.out.println("listeningException :: "+exception.getStackTrace());
		System.exit(1);
	}

	/**
	 * 
	 */
	public void serverStarted() {
		System.out.println("Registry server started "+getName());
	}

	/**
	 * 
	 */
	protected void serverClosed() {
		System.out.println("serverStopped :: Exitting.");
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public String getNode(String info) {
		return serverList.findNode(info).toString();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getList() {
		return serverList.getList();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getOverlay(int index) {
		return serverList.getConnections(index);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getOverlay() {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < serverList.getNumberOfConnections(); i++) {
			ret.append(serverList.getConnections(i));
		}
		return ret.toString();
	}
	
	/**
	 * 
	 */
	public void sendOverlay() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @param numberOfRounds 
	 */
	public void sendStart(int numberOfRounds) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @param status
	 * @param client
	 */
	public void sendRegistrationResponse(boolean status, NodeConnection client) {
		String message = (status)?"True":"False";
		NodeMessage m = new NodeMessage(message);
		m.setType("REGISTER_RESPONSE");
		try {
			client.sendToNode(m);
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		if (status == false)
			client.close();
	}
	
	public void registerNode(NodeMessage m, NodeConnection client) {
		String[] tokens = m.getMessage().split(" ");
		String clientAddress = client.getInetAddress().getHostAddress();
		if (clientAddress.equals(tokens[0]) ^ getHost().equals(tokens[0]) == false)
			sendRegistrationResponse(false, client);
		int clientPort = Integer.parseInt(tokens[1]);
		serverList.addToList(new NodeAddress(client.getNodeSocket(), tokens[0], clientPort));
		sendRegistrationResponse(true, client);
	}
	
	public void unregisterNode(NodeMessage m, NodeConnection client) {
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
	protected void MessageFromNode(Object o, NodeConnection client) {
		if (o instanceof NodeMessage == false)
			return;
		NodeMessage msg = (NodeMessage) o;
		if (debug)
			System.out.println(msg);
		switch(msg.getStringType()) {
			case "REGISTER_REQUEST":
				registerNode(msg, client);
				break;
			case "DEREGISTER_REQUEST":
				unregisterNode(msg, client);
				break;
			default:
		}
	}


}

