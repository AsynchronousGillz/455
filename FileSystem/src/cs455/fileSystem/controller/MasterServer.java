package cs455.fileSystem.controller;

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

import cs455.fileSystem.chunk.*;
import cs455.fileSystem.msg.*;
import cs455.fileSystem.util.*;

public class MasterServer extends AbstractServer {
	
	/**
	 * The connection registration list.
	 */
	protected RegistryInfo connectionInfo;
	
	// CONSTRUCTOR ******************************************************
	
	/**
	 * 
	 * @param port
	 * @throws IOException
	 */
	public MasterServer(int port) throws IOException {
		super(port);
		connectionInfo = new RegistryInfo(4);
	}
	
	// INSTANCE METHODS *************************************************


	public void nodeConnected(Connection nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" connected at "+dateFormat.format(date));
	}

	synchronized public void nodeDisconnected(Connection nodeConnection) {
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

	@Override
	protected void MessageFromNode(ProtocolMessage msg, Connection client) {
		switch(msg.getStringType()) {
			case "REGISTER_REQUEST":
				break;
			case "DEREGISTER_REQUEST":
				break;
			case "SEND_CHUNK":
				break;
			case "TRAFFIC_SUMMARY":
				break;
		}
	}
	
}

