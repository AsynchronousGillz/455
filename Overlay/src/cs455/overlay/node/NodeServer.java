// File name NodeServer.java
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
	 * @see Dijkstra
	 */
	Dijkstra dijkstra;
	
	// CONSTRUCTOR -----------------------------------------------------
	
	/**
	 * Constructs a new server.
	 *
	 */
	public NodeServer() throws IOException {
		super(0);
	}
	// ACCESSING METHODS ------------------------------------------------
	
	/**
	 * TODO
	 * @param nodes
	 */
	public void setWeights(String[] nodes) {
		for (String info : nodes) {
			String[] host = info.split(" ");
			if (host[0].equals(this.getName()) == false)
				continue;
			String[] node = host[1].split(":");
			int port = super.validateInput(node[1]);
			int weight = super.validateInput(node[2]);
			NodeConnection nodeConnection = super.getConnection(node[0], port);
			try {
				nodeConnection.sendToNode(new EdgeInformation(host[0], weight));
			} catch (IOException e) {
				System.err.println("Error when sending wieght information.");
			}
		}
	}
	// A = [ 127.0.0.0:40000 , 127.0.0.1:40001:4, 127.0.0.2:40002:4, 127.0.0.3:40003:9, 127.0.0.9:40009:4 ]
	
	/**
	 * TODO
	 * @param address
	 * @param weight
	 */
	public void setWeight(String address, int weight) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Additional information containing all the nodes and the
	 * This is an array that should be formated as "host:port"
	 *
	 * @param nodes
	 */
	public void setInfo(String[] nodes) {
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
		if (debug) {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			System.out.println("Established connection to "+host+" at "+dateFormat.format(date));
		}
	}
	
	/**
	 *  TODO
	 */
	public void makeDijkstra(String[] info) {
		String address = super.getHost();
		int port = super.getPort();
		dijkstra = new Dijkstra(info, address, port);
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
