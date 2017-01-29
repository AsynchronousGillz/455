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
	 * Get the {@link ArrayList} of {@link NodeAddress}
	 * to build the {@link StatisticsCollector}.
	 * @return
	 */
	public ArrayList<NodeAddress> getData() {
		return data;
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
	 * Returns all of the NodeAddress in String[] format
	 * getInfo() format in String format. 
	 * @return list of all the nodes
	 */
	public String[] getRegistration(int index) {
		int length = numberOfConnections;
		String[] ret = new String[length];
		ret[0] = data.get(index).getConnection() + " ";
		for (int i = 0; i < length; i++) {
			if (overlay[index][i] != 0)
				ret[i+1] = data.get(i).getConnection() + " ";
		}
		return ret;
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
			for (int outdex = index; outdex < length; outdex++) {
				if (overlay[index][outdex] != 0)
					ret[index] += data.get(outdex).getConnection()+":"+overlay[index][outdex]+" ";
			}
		}
		return ret;
	}
	
	/**
	 * When a node connects to the Server and sends 
	 * the registry request and is added to the list. 
	 */
	public synchronized void addToList(NodeAddress node) {
		data.add(node);
	}
	
	/**
	 * If  node disconnects between 'setup-overlay' and 
	 * 'send-overlay-link-weights' then it will notify the
	 * server about the change, and make the overlay void.
	 */
	public synchronized void removeFromList(NodeAddress node) {
		if (validOverlay == true) {
			validOverlay = false;
			System.out.println("The overlay is no longer correct. Please run 'setup-overlay' again.");
		}
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

	/**
	 * 
	 * @param ipAddress
	 * @param port
	 * @return
	 */
	public synchronized NodeAddress getNode(String ipAddress, int port) {
		for (NodeAddress node: data) {
			if (node.getAddress().equals(ipAddress) && node.getPort() == port)
				return node;
		}
		return null;
	}
	
	/**
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	public synchronized int getNodeIndex(String address, int port) {
		int lenght = data.size();
		for (int i = 0; i < lenght; i++) {
			if (data.get(i).getAddress().equals(address) && data.get(i).getPort() == port)
				return i;
		}
		return -1;
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
			int sum = setOverlayStart(rand, size);

			for (int row = 0; row < size; row++) {
				for (int column = 0; column < size; column++) {
					if (checkConnection(size, row, column) == true) {
						byte weight = (byte)((byte)rand.nextInt(9) + 1);
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
		byte weight = (byte)((byte)rand.nextInt(9) + 1);
		overlay[0][size-1] = weight;
		overlay[size-1][0] = weight;
		for (int i = 1; i < size; i++) {
			weight = (byte)((byte)rand.nextInt(9) + 1);
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
		if (overlay[row][column] != 0)
			return false;
		int row_sum = 0, col_sum = 0;
		for (int i = 0; i < size; i++) {
			row_sum += (overlay[row][i] != 0) ? 1 : 0;
			col_sum += (overlay[column][i] != 0) ? 1 : 0;
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

	/*
	public static void main(String args[]) {
		RegistryList registerList = new RegistryList(4);
		for(int i = 0; i < 10; i++) {
			registerList.addToList(new NodeAddress(new Socket(), "127.0.0."+i, "127.0.0."+i, 40000+i));
		}
		System.out.println(registerList.getList());
		registerList.buildOverlay();
		String[] info = null;
		try {
			info = registerList.getConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (String i: info) {
			System.out.println(i);
		}
	}
	*/
	// Host:ServerPort ip:port:wieght 
	// 127.0.0.0:40000 127.0.0.1:40001:4  127.0.0.2:40002:4  127.0.0.3:40003:9  127.0.0.9:40009:4

}

