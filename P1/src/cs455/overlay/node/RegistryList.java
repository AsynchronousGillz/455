package cs455.overlay.node;

import java.net.Socket;
import java.util.*;

/**
 * 
 * @author G van Andel
 *
 */

public class RegistryList {
	
	/**
	 * The list of node connected to the register.
	 */
	ArrayList<NodeAddress> data;
	
	/**
	 * The number of connections for each node.
	 */
	private int numberOfConnections;

	/**
	 * Indication if the overlay has been built and if it is valid.
	 */
	private boolean validOverlay;
	
	/**
	 * The list of connections to other nodes
	 */
	private byte[][] overlay;
	
	public RegistryList(int numberOfConnections) {
		this.numberOfConnections = numberOfConnections;
		data = new ArrayList<>();
	}
	
	/**
	 * Return the number of connections for each node.
	 * @return the number of connections in int format
	 */
	public boolean getValidOverlay() {
		return validOverlay;
	}
	
	/**
	 * Return the number of connections for each node.
	 * @return the number of connections in int format
	 */
	public int getNumberOfConnections() {
		return numberOfConnections;
	}

	/**
	 * Set the number of connections for each node.
	 * @param numberOfConnections
	 */
	public void setNumberOfConnections(int numberOfConnections) {
		this.numberOfConnections = numberOfConnections;
	}

	/**
	 * Returns all of the NodeAddress in table getInfo() format
	 * in String format. 
	 * @return list of all the nodes
	 */
	public String getList() {
		if (data.size() == 0) 
			return "Node list is currently empty.";
		String ret = "";
		for (NodeAddress node: data) {
			ret += node.getInfo() + "\n";
		}
		return ret;
	}
	
	/**
	 * Returns the Nodes connections
	 * @return
	 */
	public String getOverlay() {
		if (data.size() == 0) 
			return "Node list is currently empty.";
		StringBuilder ret = new StringBuilder();
		int index = 0;
		for (byte[] bytes: overlay) {
			ret.append(String.format("%02d -> | ", index++));
			for (byte b: bytes) {
				ret.append(b + " | ");
			}
			ret.append("\n");
		}
		return ret.toString();
	}
	
	/**
	 * Returns the Nodes connections
	 * @return
	 */
	public String getConnections(int index) {
		if (data.size() == 0) 
			return "Node list is currently empty.";
		if (overlay == null)
			return "Overlay has not been constructed.";
		StringBuilder ret = new StringBuilder();
		ret.append(String.format("%s -> | ", data.get(index)));
		int column = 0;
		for (byte b: overlay[index]) {
			if (b == 1)
				ret.append(data.get(column) + " | ");
			column++;
		}
		ret.append("\n");
		return ret.toString();
	}
	
	public synchronized void addToList(NodeAddress node) {
		data.add(node);
	}
	
	public synchronized void removeFromList(NodeAddress node) {
		data.remove(node);
	}

	public synchronized NodeAddress getNode(String ipAddress, int port) {
		for (NodeAddress node: data) {
			if (node.getAddress().equals(ipAddress) && node.getPort() == port)
				return node;
		}
		return null;
	}
	
	public synchronized void buildOverlay() {
		overlay = new byte[data.size()][data.size()];
		int size = overlay.length;
		setOverlayStart(size);
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				if (checkConnection(size, row, column) == true) {
					overlay[row][column] = 1;
					overlay[column][row] = 1;
				}
			}
		}
		int sum = 0;
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				sum += overlay[row][column];
			}
		}
		if (sum == (size * numberOfConnections))
			System.out.println("VALID");
	}
	
	private void setOverlayStart(int size) {
		for (int i = 1; i < size; i++) {
			overlay[i-1][i] = 1;
			overlay[i][i-1] = 1;
		}
		overlay[0][size-1] = 1;
		overlay[size-1][0] = 1;
	}
	
	private boolean checkConnection(int size, int row, int column) {
		if (row == column)
			return false;
		if (overlay[row][column] == 1)
			return false;
		int row_sum = 0, col_sum = 0;
		for (int i = 0; i < size; i++) {
			row_sum += overlay[row][i];
			col_sum += overlay[column][i];
		}
		if (row_sum >= numberOfConnections)
			return false;
		if (col_sum >= numberOfConnections)
			return false;
		Random rand = new Random();
		if (rand.nextInt(size) > row)
			return false;
		return true;
	}
	
	public static void main(String args[]) {
		RegistryList registerList = new RegistryList(4);
		for(int i = 0; i < 10; i++) {
			registerList.addToList(new NodeAddress(new Socket(), "127.0.0."+i, 40000+i));
		}
		System.out.println(registerList.getList());
		registerList.buildOverlay();
		System.out.println(registerList.getOverlay());
		System.out.println(registerList.getConnections(0));
	}

}

