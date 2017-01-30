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
	ArrayList<NodeConnection> connections;
	
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
	public void setWeights(String[] nodes) {
		connections = new ArrayList<>();
//		for (String info : nodes) {
//			String[] node = info.split(":");
//			addConnection(node[0], node[1], node[2]);
//			int weight = validateInput(sWeight);
// get connection
//			try {
//				newConnection.sendToNode(new EdgeInformation(weight));
//			} catch (IOException e) {
//				System.err.println("Error when sending wieght information.");
//			}
//		}
	}
	// A = [ 127.0.0.0:40000, 127.0.0.1:40001:4, 127.0.0.2:40002:4, 127.0.0.3:40003:9, 127.0.0.9:40009:4 ]
	
	/**
	 * TODO
	 * @param nodes
	 */
	public void setInfo(String[] nodes) {
		connections = new ArrayList<>();
		for (String node : nodes) {
			String[] host = node.split(":");
			addConnection(host[0], host[1]);
		}
	} 
	// 129.82.44.175:38271
	
	/**
	 * Add a connection to the server to another server.
	 * @param host
	 * @param sPort
	 */
	public void addConnection(String host, String sPort) {
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
		synchronized (this) {
			try {
				new NodeConnection(this.nodeThreadGroup, clientSocket, this);
			} catch (IOException e) {}
		}
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("Established connection to "+host+" at "+dateFormat.format(date));
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
	 *  TODO
	 */
	public void updateConnectionWeight(EdgeInformation m, NodeConnection client) {
		if (debug)
			System.out.println(m);
		client.setWeight(m.getWeight());
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
			case "SINGLE_WEIGHT":
				updateConnectionWeight(m.convertToEdgeInformation(), client);
				break;
			case "TASK_MESSAGE":
				break;
			default:
		}
	}
	
}
// End of AbstractServer Class
