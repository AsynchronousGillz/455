package cs455.overlay.util;

import java.util.*;

import cs455.overlay.node.NodeAddress;

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
	 * The list of connections to other nodes.
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
	public void setNumberOfConnections(int numberOfConnections) throws Exception {
		if (numberOfConnections > data.size())
			throw new Exception("Invalid selction for connection number.");
		this.numberOfConnections = numberOfConnections;
	}
	
	/**
	 * Check that the number of nodes connected and entered into
	 * the registry is more then the number of connections.
	 * @return (numberOfConnections < data.size())
	 */
	public boolean checkOverlay() {
		return (numberOfConnections < data.size());
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
	 * Returns the overlay in the two dimensional byte array. 
	 * @return byte[][] overlay
	 */
	public byte[][] getOverlay() throws Exception {
		if (data.size() == 0) 
			throw new Exception("Node list is currently empty.");
		if (validOverlay == false)
			throw new Exception("Overlay has not been constructed.");
		return overlay;
	}
	
	/**
	 * Returns the Nodes connections
	 * @return
	 */
	public String displayOverlay() {
		if (data.size() == 0) 
			return "Node list is currently empty.";
		if (validOverlay == false)
			return "Overlay has not been constructed.";
		StringBuilder ret = new StringBuilder();
		int index = 0;
		for (byte[] bytes: overlay) {
			ret.append(String.format("%02d -> ", index++));
			for (byte b: bytes) {
				ret.append(b + " ");
			}
			ret.append("\n");
		}
		return ret.toString();
	}
	
	/**
	 * Returns the Nodes connections from the overlay.
	 * @return 
	 */
	public String[] getConnections() throws Exception {
		if (data.size() == 0) 
			throw new Exception("Node list is currently empty.");
		if (validOverlay == false)
			throw new Exception("Overlay has not been constructed.");
		int length = data.size();
		String[] ret = new String[length];
		for (int index = 0; index < length; index++) {
			ret[index] = data.get(index).getConnection()+" ";
			int column = 0;
			for (byte b: overlay[index]) {
				if (b != 0)
					ret[index] += data.get(column).getConnection()+" ";
				column++;
			}
		}
		return ret;
	}
	
	/**
	 * TODO
	 */
	public synchronized void addToList(NodeAddress node) {
		data.add(node);
	}
	
	/**
	 * TODO
	 */
	public synchronized void removeFromList(NodeAddress node) {
		data.remove(node);
	}
	
	/**
	 * TODO
	 */
	public synchronized NodeAddress findNode(String info) {
		NodeAddress ret = null;
		int port = 0;
		try {
			port = Integer.parseInt(info);
		} catch (NumberFormatException e){}
		
		for (NodeAddress node: data) {
			if (node.getAddress().contains(info))
				return node;
			if (port != 0 && node.getPort() == port)
				return node;
		}
		return ret;
	}

	public synchronized NodeAddress getNode(String ipAddress, int port) {
		for (NodeAddress node: data) {
			if (node.getAddress().equals(ipAddress) && node.getPort() == port)
				return node;
		}
		return null;
	}
	/**
	 * The overlay is a byte[][] that when byte[x][y] != 0
	 * lists the weight of the connection.
	 */
	public synchronized void buildOverlay() {
		Random rand = new Random();
		while (validOverlay == false) {
			
			overlay = new byte[data.size()][data.size()];
			int size = overlay.length;
			int sum = setOverlayStart(size);

			for (int row = 0; row < size; row++) {
				for (int column = 0; column < size; column++) {
					if (checkConnection(size, row, column) == true) {
						int weight = rand.nextInt(9) + 1;
						overlay[row][column] = weight;
						overlay[column][row] = weight;
						sum += 2;
					}
				}
			}
			if (sum == (size * numberOfConnections))
				validOverlay = true;
		}
	}
	/**
	 * To ensure that every node can connect to every other node
	 * first loop around the array and connect every node to two
	 * of its N closest nodes.
	 * @param size
	 * 			The size of the array.
	 * @return sum
	 * 			The sum of the number of connections made.
	 */
	private int setOverlayStart(Random rand, int size) {
		int ret = 2;
		int weight = rand.nextInt(9) + 1;
		overlay[0][size-1] = weight;
		overlay[size-1][0] = weight;
		for (int i = 1; i < size; i++) {
			weight = rand.nextInt(9) + 1;
			overlay[i-1][i] = weight;
			overlay[i][i-1] = weight;
			ret += 2;
		}
		return ret;
	}

	/**
	 * To ensure that a node is not already connected as well as
	 * that node nor its partner will be connected to more then
	 * the numberOfConnections 
	 * @param size
	 * 			The size of the array.
	 * @return sum
	 * 			The sum of the number of connections made.
	 */
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
		System.out.println(registerList.displayOverlay());
	}

}

