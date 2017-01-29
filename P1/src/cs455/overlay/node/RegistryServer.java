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
import cs455.overlay.util.*;

public class RegistryServer extends AbstractServer {
	
	/**
	 * The setup-overlay has begun.
	 */
	private boolean registationCheck;
	
	/**
	 * The connection registration list.
	 */
	private RegistryList serverList;

	// CONSTRUCTOR ******************************************************

	public RegistryServer(int port) throws IOException {
		super(port);
		serverList = new RegistryList(4);
		registationCheck = false;
	}
	
	// INSTANCE METHODS *************************************************


	public void nodeConnected(NodeConnection nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" connected at "+dateFormat.format(date));
	}

	synchronized public void nodeDisconnected(NodeConnection nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" disconnected at "+dateFormat.format(date));
		serverList.removeFromList(nodeConnection.getAddress());
	}

	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.out.println("listeningException :: "+exception.getStackTrace());
		System.exit(1);
	}

	/**
	 * TODO
	 */
	public void serverStarted() {
		System.out.println("Registry server started "+getName());
	}

	/**
	 * TODO
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
	 * TODO
	 */
	public boolean getRegistration() {
		return this.registationCheck;
	}
	
	/**
	 * Set the flag registationCheck to true, then uses the serverlist to 
	 * build the overlay. Then a list of all the nodes is sent to each node
	 * connected in the overlay. Then print an acknowledgment.
	 */
	private void setRegistration() {
		this.registationCheck = true;
		serverList.buildOverlay();
		String[] info = serverList.getRegistration();
		this.sendToAllNodes(new Overlay(info, 0));
		System.out.println("The overlay has been succesfully setup.");
	}

	/**
	 * TODO
	 * @return
	 */
	public String getList() {
		return serverList.getList();
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
		this.setRegistration();
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
		this.setRegistration();
	}

	/**
	 * TODO
	 * @return
	 */
	public String displayOverlay() {
		String info[] = null;
		try {
			info = serverList.getConnections();
		} catch (Exception e) {
			return e.getMessage();
		}
		StringBuilder sb = new StringBuilder();
		for (String s: info) {
			sb.append(s+"\n");
		}
		return sb.toString();
	}
	
	/**
	 * TODO
	 */
	public void sendOverlay() {
		if (serverList.getValidOverlay() == false) {
			System.err.println("Overlay has not been constructed.");
			return;
		}
		String info[] = null;
		try {
			info = serverList.getConnections();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
		this.sendToAllNodes(new Overlay(info, 1));
		System.out.println("The overlay has been succesfully sent to all nodes.");
	}
	
	/**
	 * TODO
	 * @param numberOfRounds 
	 */
	public void sendStart(int numberOfRounds) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * When the client send a registration request the server will
	 * check that the request is from the connected socket. IE. that 
	 * the ip address of the socket matches the registration request
	 * ip and that the overlay has not yet been constructed.
	 * @param status
	 * @param client
	 */
	public void sendRegistrationResponse(boolean status, NodeConnection client) {
		String message = (status)?"True":"False";
		Registation m = new Registation(message, 2);
		try {
			client.sendToNode(m);
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		if (status == false)
			client.close();
	}
	
	/**
	 * The node has sent a registration request to the registry server
	 * now we need to check the request and send a response.
	 * @param m
	 * @param client
	 */
	public void registerNode(Registation m, NodeConnection client) {
		if (debug)
			System.out.println(m.getMessageString());
		if (registationCheck == true) {
			sendRegistrationResponse(false, client);
			return;
		}
		String[] tokens = m.getMessageString().split(" ");
		String clientAddress = client.getInetAddress().getHostAddress();
		if (tokens[0].equals(clientAddress) == false) {
			sendRegistrationResponse(false, client);
			return;
		}
		String clientName = getTargetHostName(tokens[0]);
		int clientPort = Integer.parseInt(tokens[1]);
		client.makeNodeAddress(new NodeAddress(client.getNodeSocket(), clientName, tokens[0], clientPort));
		serverList.addToList(client.getAddress());
		sendRegistrationResponse(true, client);
	}
	
	/**
	 * The node has sent a de-registration request to the registry 
	 * server now we need to remove the node.
	 * @param m
	 * @param client
	 */
	public void unregisterNode(Registation m, NodeConnection client) {
		if (debug)
			System.out.println(m.getMessageString());
		String[] tokens = m.getMessageString().split(" ");
		String clientAddress = client.getInetAddress().getHostAddress();
		if (tokens[0].equals(clientAddress) == false) {
			sendRegistrationResponse(false, client);
			return;
		}
		if (validateInput(tokens[1]) == client.getAddress().getPort()) {
			sendRegistrationResponse(false, client);
			return;
		}
		serverList.removeFromList(client.getAddress());
		sendRegistrationResponse(true, client);
	}

	@Override
	protected void MessageFromNode(Object o, NodeConnection client) {
		if (o instanceof Protocol == false)
			return;
		Protocol msg = (Protocol) o;
		if (debug)
			System.out.println(msg);
		switch(msg.getStringType()) {
			case "REGISTER_REQUEST": {
				registerNode(msg.convertToRegistation(), client);
				break;
			}
			case "DEREGISTER_REQUEST": {
				unregisterNode(msg.convertToRegistation(), client);
				break;
			}
			default:
		}
	}

}

