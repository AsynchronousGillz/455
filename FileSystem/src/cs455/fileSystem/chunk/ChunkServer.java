package cs455.fileSystem.chunk;

import java.text.*;
import java.util.*;
import java.io.*;

import cs455.fileSystem.msg.*;
import cs455.fileSystem.util.*;

/**
 * 
 * @author G van Andel
 *
 * @see ChunkNode.NodeMain
 * @see AbstractServer
 */

public class ChunkServer extends AbstractServer {
	
	/**
	 * @see Dijkstra
	 */
	Dijkstra dijkstra;
	
	// CONSTRUCTOR -----------------------------------------------------
	
	/**
	 * Constructs a new server.
	 *
	 */
	public ChunkServer() throws IOException {
		super(0);
	}
	// ACCESSING METHODS ------------------------------------------------

	/**
	 * TODO::Write getInfo [TIME] chunks
	 * @return
	 */
	public String getInfo() {
		return null;
		
	}
	
	// HOOK METHODS -----------------------------------------------------
	
	public void nodeConnected(Connection nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" connected at "+dateFormat.format(date));
	}


	public void nodeDisconnected(Connection nodeConnection) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(nodeConnection+" disconnected at "+dateFormat.format(date));
	}

	public void listeningException(Throwable exception) {
		System.out.println("listeningException :: "+exception.toString());
		System.exit(1);
	}

	public void serverStarted() {
		System.out.println("Node server started "+this.getName());
	}
	
	protected void serverClosed() {
		if (debug)
			System.out.println("serverClosed :: Exitting.");
	}
	
	
	@Override
	protected void MessageFromNode(ProtocolMessage msg, Connection client) {
		switch(msg.getStringType()) {
			case "SINGLE_WEIGHT":
				break;
			case "TASK_MESSAGE":
				break;
		}
	}
	
}
