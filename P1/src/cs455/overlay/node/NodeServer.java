// File name Server.java
package cs455.overlay.node;

import java.text.*;
import java.util.*;
import java.io.*;
import java.net.Socket;

import cs455.overlay.msg.*;
import cs455.overlay.util.StatisticsCollector;

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
	private StatisticsCollector stats;
	
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
	 * Get the {@link StatisticsCollector} to send the results
	 * to the registry.
	 * @return this.stats
	 */
	public StatisticsCollector getStats() {
		return this.stats;
	}
	
	/**
	 * 
	 * @param nodes
	 */
	public void setStats(String[] nodes) {
		this.stats = new StatisticsCollector(nodes, this.getPort());
	}
	
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

		// When a client is accepted, create a thread to handle
		// the data exchange, then add it to thread group
		synchronized (this) {
			try {
				new NodeConnection(this.nodeThreadGroup, clientSocket, this);
			} catch (IOException e) {}
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
