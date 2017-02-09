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

	// CONSTRUCTOR ******************************************************

	public RegistryServer(int port) throws IOException {
		super(port);
		connectionInfo = new RegistryInfo(4);
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
		connectionInfo.removeFromList(nodeConnection);
	}

	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.out.println("listeningException :: "+exception.getStackTrace());
		System.exit(1);
	}

	public void serverStarted() {
		System.out.println("Registry server started "+getName());
	}

	protected void serverClosed() {
		System.out.println("serverStopped :: Exitting.");
	}
	
	public boolean getRegistration() {
		return this.registationCheck;
	}
	
	/**
	 * Set the flag registationCheck to true, then uses the serverlist to 
	 * build the overlay. Then a list of connections is sent to each node
	 * connected in the overlay. Then print an acknowledgment.
	 */
	private void setRegistration() {
		this.registationCheck = true;
		connectionInfo.buildOverlay();
		for (NodeConnection node : connectionInfo.getData()) {
			String[] info = connectionInfo.getRegistration(node);
			try {
				node.sendToNode(new OverlayMessage(info, 0));
			} catch (IOException e) {
				System.out.println("Error: Could not send overlay to node: "+node);
			}
		}
		String[] info = null;
		try {
			info = connectionInfo.getList();
		} catch (Exception e) {}
		super.sendToAllNodes(new OverlayMessage(info, 2));
		System.out.println("The overlay has been succesfully setup.");
	}

	/**
	 * Returns a list of all the nodes that are connected to the register.
	 * 
	 * @return
	 */
	public String getList() {
		String[] info = null;
		try {
			info = connectionInfo.getList();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		String ret = "";
		for (String t : info) {
			ret += t + "\n";
		}
		return ret;
	}
	
	/**
	 * Used for the command 'list-messaging node' where node can be a
	 * port, ip, or host name.
	 * 
	 * @param info
	 * @return
	 */
	public String getNode(String info) {
		return connectionInfo.getNodeInfo(info);
	}

	
	/**
	 * From the interface given the command "setup-overlay"
	 * first check the number of node is more then the number
	 * of connections and if so print an error else have the
	 * RegistryList build the overlay and print success.
	 */
	public void makeOverlay() {
		if (connectionInfo.checkOverlay() == false) {
			System.err.println("Invalid connections to node ratio.");
			return;
		}
		this.setRegistration();
	}
	
	/**
	 * From the interface given the command "setup-overlay" first 
	 * check the number of node is more then the number of 
	 * connections and if so print an error else have the
	 * RegistryList build the overlay and print success.
	 * 
	 * @param number of connections.
	 */
	public void makeOverlay(int numberConnections) {
		try {
			connectionInfo.setNumberOfConnections(numberConnections);
		} catch (Exception e) {
			System.err.println(e.toString());
			return;
		}
		if (connectionInfo.checkOverlay() == false) {
			System.err.println("Invalid connections to node ratio.");
			return;
		}
		this.setRegistration();
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String testDisplayOverlay() {
		return connectionInfo.displayOverlay();
	}

	/**
	 * When the command 'list-weights' is entered it will call the
	 * displayOverlay method. This will return a String of the all the
	 * connections and links.
	 * @return
	 */
	public String displayOverlay() {
		String info[] = null;
		try {
			info = connectionInfo.getConnections();
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
	 * The overlay have been constructed and the overlay has yet to 
	 * be sent to the nodes. Each node will be sent all the connections
	 * to the registration.
	 */
	public void sendOverlay() {
		if (connectionInfo.getValidOverlay() == false) {
			System.err.println("Overlay has not been constructed.");
			return;
		}
		if (connectionInfo.getOverlaySent() == true) {
			System.err.println("Overlay has already been sent.");
			return;
		}
		String info[] = null;
		try {
			info = connectionInfo.getConnections();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
		this.sendToAllNodes(new OverlayMessage(info, 1));
		connectionInfo.setOverlaySent();
		System.out.println("The overlay has been succesfully sent to all nodes.");
	}
	
	/**
	 * The the command is given to start sending with a number of 
	 * rounds to send 5 messages. These five messages are sent from 
	 * each node to a random node in the connections. 
	 * @param numberOfRounds 
	 */
	public void sendStart(int numberOfRounds) {
		if (numberOfRounds == 0)
			return;
		if (connectionInfo.getValidOverlay() == false) {
			System.err.println("Overlay has not been constructed.");
			return;
		}
		if (connectionInfo.getOverlaySent() == false) {
			System.err.println("Overlay has not yet been sent.");
			return;
		}
		boolean complete = false;
		for (NodeConnection node : super.getNodeConnections()) {
			complete = (node.getComplete() || complete);
		}
		if (complete == true) {
			System.err.println("Messaging in progress.");
			return;
		}
		super.sendToAllNodes(new InitiateMessage(numberOfRounds));
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
		try {
			client.sendToNode(new RegistationMessage(message, 2));
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
	public void registerNode(RegistationMessage m, NodeConnection client) {
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
		String hostName = getTargetHostName(tokens[0]);
		int clientPort = Integer.parseInt(tokens[1]);
		client.setClientInfo(hostName, tokens[0], clientPort);
		connectionInfo.addToList(client);
		sendRegistrationResponse(true, client);
	}
	
	/**
	 * The node has sent a de-registration request to the registry 
	 * server now we need to remove the node.
	 * @param m
	 * @param client
	 */
	public void unregisterNode(RegistationMessage m, NodeConnection client) {
		if (debug)
			System.out.println(m.getMessageString());
		String[] tokens = m.getMessageString().split(" ");
		String clientAddress = client.getAddress();
		if (tokens[0].equals(clientAddress) == false) {
			sendRegistrationResponse(false, client);
			return;
		}
		if (validateInput(tokens[1]) == client.getPort()) {
			sendRegistrationResponse(false, client);
			return;
		}
		connectionInfo.removeFromList(client);
		sendRegistrationResponse(true, client);
	}
	
	/**
	 * The node has sent a task-complete request to the registry.
	 * @param m
	 * @param client
	 */
	public synchronized void taskComplete(RegistationMessage m, NodeConnection client) {
		if (debug)
			System.out.println(m);
		client.setComplete();
		System.out.println("Client: "+client+" has finshed sending.");
		boolean complete = true;
		for (NodeConnection node : super.getNodeConnections()) {
			complete = (node.getComplete() && complete);
		}
		if (complete == false)
			return;
		super.sleep(15000);
		this.sendToAllNodes(new RegistationMessage("PULL_TRAFFIC_SUMMARY.", 4));
	}
	
	/**
	 * The node has sent a task-complete request to the registry.
	 * @param m
	 * @param client
	 */
	public synchronized void statisticsCompiler(StatisticsMessage m, NodeConnection client) {
		if (debug)
			System.out.println(m);
		connectionInfo.addStats(client, m.makeCollector());
		if (connectionInfo.getInfoStatus() == false)
			return;
		String[] names = null;
		try {
			 names = connectionInfo.getList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int index = 0;
		long[] total = new long[5];
		System.out.printf("%-18s %10s %15s %10s %15s %10s\n", "Name", "Sent", "Sum Sent", "Received", "Sum Received", "Relayed");
		for (StatisticsCollector s : connectionInfo.getStats()) {
			System.out.println(names[index++] + s); 
			total[0] += s.getSent();
			total[1] += s.getSumSent();
			total[2] += s.getReceived();
			total[3] += s.getSumReceived();
			total[4] += s.getRelayed();
		}
		System.out.printf("%-18s %10d %15d %10d %15d %10d\n", "Total", total[0], total[1], total[2], total[3], total[4]);
		for (NodeConnection node : super.getNodeConnections()) {
			node.resetComplete();
		}
		connectionInfo.resetCollector();
	}
	
	/**
	 * 
	 */
	public void sendStop() {
		
	}

	@Override
	protected void MessageFromNode(Object o, NodeConnection client) {
		if (o instanceof ProtocolMessage == false)
			return;
		ProtocolMessage msg = (ProtocolMessage) o;
		switch(msg.getStringType()) {
			case "REGISTER_REQUEST":
				registerNode(msg.convertToRegistation(), client);
				break;
			case "DEREGISTER_REQUEST":
				unregisterNode(msg.convertToRegistation(), client);
				break;
			case "TASK_COMPLETE":
				taskComplete(msg.convertToRegistation(), client);
				break;
			case "TRAFFIC_SUMMARY":
				statisticsCompiler(msg.convertToStats(), client);
				break;
		}
	}
	
}

