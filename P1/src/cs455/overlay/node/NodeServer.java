// File name Server.java
package cs455.overlay.node;

import java.text.*;
import java.util.*;
import java.io.*;
import java.net.Socket;

import cs455.overlay.msg.*;
import cs455.overlay.util.*;

/**
 * 
 * @author G van Andel
 *
 * @see MessagingNode.NodeMain
 * @see AbstractServer
 */

public class NodeServer extends AbstractServer {
	
	/**
	 * 
	 */
	ArrayList<NodeAddress> connections;
	
	// CONSTRUCTOR -----------------------------------------------------
	
	/**
	 * Constructs a new server.
	 *
	 */
	public NodeServer() throws IOException {
		super(0);
		connections = null;
	}
	// ACCESSING METHODS ------------------------------------------------
	
	/**
	 * Get the {@link StatisticsCollector} to send the results
	 * to the registry.
	 * @return this.stats
	 */
	public StatisticsCollector getStats() {
		return this.stats;
	}
	
	/**
	 * TODO
	 * @param nodes
	 */
	public void setStats(String[] nodes) {
		this.stats = new StatisticsCollector(nodes, this.getPort());
	}
	
	/**
	 * TODO
	 * @param nodes
	 */
	public void setInfo(String[] nodes) {
		connections = new ArrayList<>();
		int length = nodes.length;
		String[] info = null;
		for (int i = 0; i < length; i++) {
			String[] total = nodes[i].split(" ");
			String[] host = total[0].split(":");
			if (host[0].equals(this.getHost()) && validateInput(host[1]) == getPort()) {
				info = total;
				break;
			}
		}
		int numberOfConnections = info.length;
		
		for (int i = 1; i < numberOfConnections; i++) {
			String[] node = info[i].split(":");
			addConnection(node[0], node[1], node[2]);
		}
		// info = A
	}
	// A = [ 127.0.0.0:40000, 127.0.0.1:40001:4, 127.0.0.2:40002:4, 127.0.0.3:40003:9, 127.0.0.9:40009:4 ]
	
	/**
	 * Add a connection to the server to another server.
	 * @param host
	 * @param sPort
	 */
	public void addConnection(String host, String sPort, String sWeight) {
		int port = validateInput(sPort);
		if (port == 0)
			return;
		// Wait here for new connection attempts, or a timeout
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(host, port);
		} catch (IOException e) {
			System.err.println("Could not connect to: "+host+":"+port);
		}
		NodeConnection newConnection = null;
		synchronized (this) {
			try {
				newConnection = new NodeConnection(this.nodeThreadGroup, clientSocket, this);
			} catch (IOException e) {}
		}
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("Established connection to "+host+" at "+dateFormat.format(date));
		int weight = validateInput(sWeight);
		try {
			newConnection.sendToNode(new EdgeInformation(weight));
		} catch (IOException e) {
			System.err.println("Error when sending wieght information.");
		}
	}
	
	// HOOK METHODS -----------------------------------------------------
	
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
		if (msg instanceof Protocol == false)
			return;
		Protocol m = (Protocol) msg;
		if (debug)
			System.out.println(m);
		switch(m.getStringType()) {
			case "TASK_MESSAGE":
				break;
			default:
		}
	}
	
}
// End of AbstractServer Class
