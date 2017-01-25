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
import cs455.overlay.util.RegistryList;

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
	 * Search the serverList for the Node with a host name, ip address
	 * or port similar to the String parameter.
	 * @param string
	 * @return NodeAddress.toString()
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
		if (serverList.getValidOverlay() == false)
			return "Overlay has not been constructed.";
		return serverList.getConnections(index);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getOverlay() {
		if (serverList.getValidOverlay() == false)
			return "Overlay has not been constructed.";
		
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < serverList.getNumberOfConnections(); i++) {
			ret.append(serverList.getConnections(i));
		}
		return ret.toString();
	}
	
	/**
	 * From the interface given the command "setup-overlay"
	 * first check the number of node is more then the number
	 * of connections and if so print an error else have the
	 * RegistryList build the overlay and print success.
	 */
	public void makeOverlay() {
		if (serverList.checkOverlay() == false) {
			System.err.println("Invalid connections to node ratio.");
			return;
		}
		serverList.buildOverlay();
		System.out.println("The overlay has been succesfully setup.");
	}
	
	/**
	 * From the interface given the command "setup-overlay"
	 * first check the number of node is more then the number
	 * of connections and if so print an error else have the
	 * RegistryList build the overlay and print success.
	 * 
	 * @param number of connections.
	 */
	public void makeOverlay(int numberConnections) {
		try {
			serverList.setNumberOfConnections(numberConnections);
		} catch (Exception e) {
			System.err.println(e.toString());
			return;
		}
		if (serverList.checkOverlay() == false) {
			System.err.println("Invalid connections to node ratio.");
			return;
		}
		serverList.buildOverlay();
		System.out.println("The overlay has been succesfully setup.");
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
		if (clientAddress.equals(tokens[0]) == false)
			sendRegistrationResponse(false, client);
		int clientPort = Integer.parseInt(tokens[1]);
		String clientName = getTargetHostName(tokens[0]);
		serverList.addToList(new NodeAddress(client.getNodeSocket(), clientName, tokens[0], clientPort));
		sendRegistrationResponse(true, client);
	}
	
	public void unregisterNode(NodeMessage m, NodeConnection client) {
		String[] tokens = m.getMessage().split(" ");
		String clientAddress = client.getInetAddress().getHostAddress();
		if (tokens[0].equals(clientAddress) == false)
			sendRegistrationResponse(false, client);
		int clientPort = Integer.parseInt(tokens[1]);
		String clientName = getTargetHostName(tokens[0]);
		try {
			serverList.removeFromList(new NodeAddress(client.getNodeSocket(), clientName, tokens[0], clientPort));
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

