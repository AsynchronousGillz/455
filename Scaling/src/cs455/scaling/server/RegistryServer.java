package cs455.scaling.server;

/**
 * 
 * @author G van Andel
 *
 * @see AbstractServer
 * 
 */

import java.text.*;
import java.util.*;

import cs455.scaling.msg.ProtocolMessage;

import java.io.*;

public class RegistryServer extends AbstractServer {

	// CONSTRUCTOR ******************************************************

	public RegistryServer(int port) throws IOException {
		super(port);
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
	
	@Override
	protected void MessageFromNode(Object o, NodeConnection client) {
		if (o instanceof ProtocolMessage == false)
			return;
		ProtocolMessage msg = (ProtocolMessage) o;
		switch(msg.getStringType()) {
		}
	}
	
}

