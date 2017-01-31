// File name NodeClient.java

package cs455.overlay.node;

import java.io.*;
import java.net.*;

import cs455.overlay.msg.*;

/**
 * 
 * @author G van Andel
 *
 * @see MessagingNode.NodeMain
 */

public class NodeClient implements Runnable {

	// INSTANCE VARIABLES ***********************************************

	/**
	 * Sockets are used in the operating system as channels of communication
	 * between two processes.
	 * 
	 * @see java.net.Socket
	 */
	private Socket nodeSocket;

	/**
	 * The stream to handle data going to the server.
	 */
	private DataOutputStream output;

	/**
	 * The stream to handle data from the server.
	 */
	private DataInputStream input;

	/**
	 * The thread created to read data from the server.
	 */
	private Thread nodeReader;

	/**
	 * Indicates if the thread is ready to stop. Needed so that the loop in the
	 * run method knows when to stop waiting for incoming messages.
	 */
	private boolean stop = false;

	/**
	 * The server's host name.
	 */
	private String registryIP;

	/**
	 * The port number.
	 */
	private int registryPort;
	
	/**
	 * The node server
	 */
	private NodeServer nodeServer;

	/**
	 * For debug purposes
	 */
	final private boolean debug = false;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the node.
	 * 
	 * @param registryIP
	 *            the server's host name.
	 * @param registryPort
	 *            the port number.
	 */
	public NodeClient(NodeServer nodeServer, String registryIP, int registryPort) {
		// Initialize variables
		this.nodeServer = nodeServer;
		this.registryIP = registryIP;
		this.registryPort = registryPort;
		openConnection();
	}

	// INSTANCE METHODS *************************************************

	/**
	 * Opens the connection with the server. If the connection is already
	 * opened, this call has no effect.
	 */
	final public void openConnection() {
		// Do not do anything if the connection is already open
		if (isConnected() == true)
			return;

		// Create the sockets and the data streams
		try {
			nodeSocket = new Socket(registryIP, registryPort);
			output = new DataOutputStream(nodeSocket.getOutputStream());
			input = new DataInputStream(nodeSocket.getInputStream());
		} catch (IOException ex) {
			closeConnection();
		}

		nodeReader = new Thread(this); // Create the data reader thread
		stop = false;
		nodeReader.start(); // Start the thread
	}

	/**
	 * Sends an object to the server. This is the only way that methods should
	 * communicate with the server.
	 * 
	 * @param msg
	 *            The message to be sent.
	 * @exception IOException
	 *                if an I/O error occurs when sending
	 */
	final public void sendToServer(Protocol msg) throws IOException {
		if (nodeSocket == null || output == null)
			throw new SocketException("socket does not exist");
		byte[] bytes = msg.makeBytes();
		output.writeInt(bytes.length);
		output.write(bytes, 0, bytes.length);
	}

	/**
	 * Closes the connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when closing.
	 */
	final public void closeConnection() {
		// Prevent the thread from looping any more
		stop = true;

		try {
			closeAll();
		} catch (Exception exc) {
			if (debug)
				System.err.println(exc.toString());
		} finally {
			connectionClosed();
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * @return true if the node is connnected.
	 */
	final public boolean isConnected() {
		return nodeReader != null && nodeReader.isAlive();
	}

	/**
	 * @return the port number.
	 */
	final public int getRegistryPort() {
		return registryPort;
	}

	/**
	 * Sets the server port number for the next connection. The change in port
	 * only takes effect at the time of the next call to openConnection().
	 * 
	 * @param port
	 *            the port number.
	 */
	final public void setRegistryPort(int port) throws IllegalArgumentException {
		if ((port < 0) || (port > 65535))
			throw new IllegalArgumentException("Invalid port. Must be with in 0 to 65535.");
		this.registryPort = port;
	}

	/**
	 * @return the host name.
	 */
	final public String getRegistryHost() {
		return registryIP;
	}

	/**
	 * Sets the server host for the next connection. The change in host only
	 * takes effect at the time of the next call to openConnection().
	 * 
	 * @param host
	 *            the host name.
	 */
	final public void setRegistryHost(String host) {
		this.registryIP = host;
	}

	/**
	 * returns the node's description.
	 * 
	 * @return the node's Inet address.
	 */
	final public InetAddress getInetAddress() {
		return nodeSocket.getInetAddress();
	}

	// RUN METHOD -------------------------------------------------------

	/**
	 * Waits for messages from the server. When each arrives, a call is made to
	 * <code>messageFromServer()</code>. Not to be explicitly called.
	 */
	final public void run() {
		// Additional setting up
		connectionEstablished();

		// The message from the server
		int byteSize;

		// Loop waiting for data
		try {
			while (stop == false) {
				byteSize = input.readInt();
				byte[] bytes = new byte[byteSize];
				input.readFully(bytes, 0, byteSize);
				messageFromServer(new Protocol(bytes));
			}
		} catch (Exception exception) {
			if (stop == false) {
				close(1);
				connectionException(exception);
			}
		} finally {
			nodeReader = null;
		}
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	/**
	 * Hook method called each time an exception is thrown by the node's thread
	 * that is waiting for messages from the server.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		if (exception instanceof EOFException)
			close(1);
		else
			System.out.println("connectionException :: " + exception.toString());
	}

	/**
	 * Hook method called after a connection has been established.
	 */
	protected void connectionEstablished() {
		if (nodeSocket == null)
			return;
		String info = nodeServer.getHost()+" "+nodeServer.getPort();
		Registation m = new Registation(info, 0);
		try {
			sendToServer(m);
		} catch (IOException e) {
			System.err.println("Could not send Registration.");
		}
	}

	/**
	 * Hook method called after the connection has been closed.
	 */
	protected void connectionClosed() {
		if (nodeSocket == null)
			return;
		String info = nodeServer.getHost()+" "+nodeServer.getPort();
		Registation m = new Registation(info, 1);
		try {
			sendToServer(m);
		} catch (IOException e) {
			System.err.println("Could not send Deregistration.");
		}
	}

	// METHODS TO BE USED FROM WITHIN THE FRAMEWORK ONLY ----------------

	/**
	 * Closes the node. If the connection is already closed, this call has no
	 * effect.
	 * @param node
	 * 			0 for exit
	 * 			1 for error 
	 * @exception IOException
	 * 			if an error occurs when closing the socket.
	 */
	final public void close(int mode) {
		stop = true; // Set the flag that tells the thread to stop

		try {
			closeAll();
		} catch (IOException ex) {
			if (debug)
				System.err.println(ex.toString());
		} finally {
			if (mode == 0) {
				System.out.println("Disconnecting from the server. Exitting.");
			} else {
				System.err.println("An error occured connecting to the server. Exitting.");
				System.exit(1);
			}
		}
	}

	/**
	 * Closes all aspects of the connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when closing.
	 */
	private void closeAll() throws IOException {
		try {
			// Close the socket
			if (nodeSocket != null)
				nodeSocket.close();

			// Close the output stream
			if (output != null)
				output.close();

			// Close the input stream
			if (input != null)
				input.close();
		} finally {
			// Set the streams and the sockets to NULL no matter what
			// Doing so allows, but does not require, any finalizers
			// of these objects to reclaim system resources if and
			// when they are garbage collected.
			output = null;
			input = null;
			nodeSocket = null;
		}
	}

	// ----------------------------------------------------------


	public void unregister() {
		if (nodeSocket == null)
			return;
		String info = nodeServer.getHost()+" "+nodeServer.getPort();
		Registation m = new Registation(info, 1);
		try {
			sendToServer(m);
		} catch (IOException e) {
			System.err.println("Could not send Registration.");
		}
		close(0);
	}
	
	/**
	 * Handles a the response message sent from the server to this node.
	 * 
	 * @param m
	 *            the message sent.
	 */
	public void registerResponse(Registation m) {
		if (debug)
			System.out.println(m.getMessageString());;
		if (m.getMessage().equals("False")) {
			close(0);
		}
	}
	
	/**
	 * Handles a the overlay message sent from the server.
	 * This Message contains all of the nodes in the Overlay.
	 * 
	 * @param o
	 * 			the overlay message sent.
	 */
	private void registerNodes(Overlay o) {
		if (debug)
			System.out.print(o);
		String[] nodes = o.getString();
		nodeServer.makeDijkstra(nodes);
	}
	
	/**
	 * Handles a the overlay message sent from the server. With 
	 * information about the connections within the overlay.
	 * @param o
	 *            the overlay message sent.
	 */
	public void registerConnections(Overlay o) {
		if (debug)
			System.out.print(o);
		String[] nodes = o.getString();
		nodeServer.setInfo(nodes);
		nodeServer.setStats();
	}
	
	/**
	 * Handles a the Weight message sent from the server. With 
	 * information about the weights of the connections within 
	 * the overlay.
	 * 
	 * @param e
	 *            the EdgeInformation message sent.
	 */
	public void registerWeights(EdgeInformation e) {
//		if (debug) // DEBUG
			System.out.print(e);
		String address = e.getAddress();
		int weight = e.getWeight();
		nodeServer.setWeight(address, weight);
	}

	/**
	 * Handles a message sent from the server to this node.
	 * 
	 * Then checks if the message is a protocol then if so
	 * converts it to a Protocol. Then switch based on the 
	 * message type. 
	 * 
	 * @param o
	 *            the incoming message.
	 */
	protected void messageFromServer(Object o) {
		if (o instanceof Protocol == false)
			return;
		Protocol m = (Protocol) o;
		if (debug)
			System.out.println(m);
		switch (m.getStringType()) {
			case "REGISTER_RESPONSE":
				registerResponse(m.convertToRegistation());
				break;
			case "MESSAGING_NODES":
				registerNodes(m.convertToOverlay());
				break;
			case "MESSAGING_NODES_LIST":
				registerConnections(m.convertToOverlay());
				break;
			case "LINK_WEIGHTS":
				registerWeights(m.convertToEdgeInformation());
				break;
			default:
		}
	}

}
// end of Client class
