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
	 * The setup-overlay has begun.
	 */
	private boolean registationCheck;
	
	/**
	 * The connection listener thread.
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
	 * TODO
	 */
	public void setRegistration() {
		this.registationCheck = true;
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
		this.setRegistration();
		serverList.buildOverlay();
		System.out.println("The overlay has been succesfully setup.");
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
			System.err.println("Overlay has not been setup yet.");
			return;
		}
		NodeConnection[] con = getClientConnections();
		String info[] = null;
		try {
			info = serverList.getConnections();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		int length = con.length;
		try {
			for (int index = 0; index < length; index++) {
				con[index].sendToNode(new Overlay(info, 0));
			}
		} catch (IOException e) {
			System.err.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println("The overlay has been succesfully sent to all nodes.");
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
		Registation m = new Registation(message, 2);
		try {
			client.sendToNode(m);
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		if (status == false)
			client.close();
	}
	
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

