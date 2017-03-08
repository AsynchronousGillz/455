package cs455.fileSystem.util;

import java.util.*;

import cs455.fileSystem.chunk.*;

/**
 * 
 * @author G van Andel
 *
 */

public class RegistryInfo {

	/**
	 * The list of node connected to the register.
	 */
	ArrayList<Connection> data;

	/**
	 * The number of connections for each node.
	 */
	private int numberOfConnections;

	/**
	 * Indication if the overlay has been built and if it is valid.
	 */
	private boolean validOverlay;

	/**
	 * Indication if the overlay has been built and if it is valid.
	 */
	private boolean overlaySent;

	/**
	 * The list of connections to other nodes.
	 */
	private int[][] overlay;

	/**
	 * When the server is given the command to set up the overlay.
	 * 
	 * @param numberOfConnections
	 */
	public RegistryInfo(int numberOfConnections) {
		this.numberOfConnections = numberOfConnections;
		data = new ArrayList<>();
	}

	/**
	 * Returns if the overlay has been built yet.
	 * 
	 * @return true if the overlay has been built.
	 */
	final public boolean getValidOverlay() {
		return validOverlay;
	}

	/**
	 * Return the status of the current overlay.
	 * 
	 * @return true if sent.
	 */
	final public boolean getOverlaySent() {
		return overlaySent;
	}

	/**
	 * Sets the current overlay status to sent.
	 * 
	 * @return true if sent.
	 */
	final public void setOverlaySent() {
		this.overlaySent = true;
	}
	
	/**
	 * Resets the current overlay status to un-sent.
	 */
	final public void resetOverlaySent() {
		this.overlaySent = false;		
	}

	/**
	 * Return the number of connections for each node.
	 * 
	 * @return the number of connections in int format
	 */
	public int getNumberOfConnections() {
		return numberOfConnections;
	}

	/**
	 * Get the {@link ArrayList} of {@link NodeAddress} to build the
	 * {@link StatisticsCollector}.
	 * 
	 * @return
	 */
	public ArrayList<Connection> getData() {
		return data;
	}

	/**
	 * Set the number of connections for each node.
	 * 
	 * @param numberOfConnections
	 */
	public void setNumberOfConnections(int numberOfConnections) throws Exception {
		if (numberOfConnections > data.size())
			throw new Exception("Invalid selction for connection number.");
		this.numberOfConnections = numberOfConnections;
		this.validOverlay = false;
		this.overlaySent = false;
	}

	/**
	 * Check that the number of nodes connected and entered into the registry is
	 * more then the number of connections.
	 * 
	 * @return (numberOfConnections < data.size())
	 */
	final public boolean checkOverlay() {
		return (numberOfConnections < data.size()) && this.validOverlay == false;
	}

	/**
	 * Returns all of the NodeAddress in String[] format toString() format in
	 * String format.
	 *
	 * @return list of all the nodes
	 */
	public String[] getRegistration(Connection node) {
		int length = data.size();
		int index = data.indexOf(node);
		int connection = 0;

		for (int i = index; i < length; i++)
			if (overlay[index][i] != 0)
				connection++;

		String[] ret = new String[connection];
		for (int i = index, r = 0; i < length; i++) {
			if (overlay[index][i] != 0)
				ret[r++] = data.get(i).toString();
		}
		return ret;
	}

	/**
	 * Returns all of the NodeConnections in getInfo() format
	 *
	 * @return list of all the nodes [ ip:port ]
	 */
	public String[] getList() throws Exception {
		if (data.size() == 0)
			new Exception("Node list is currently empty.");
		String[] ret = new String[data.size()];
		int index = 0;
		for (Connection node : data) {
			ret[index++] = node.getConnection();
		}
		return ret;
	}

	/**
	 * Returns all NodeConnections getInfo() format.
	 *
	 * @return list
	 */
	public String getNodeInfo(String search) {
		if (data.size() == 0)
			return "Node list is currently empty.";
		String ret = "";
		int port = 0;
		try {
			port = Integer.parseInt(search);
		} catch (NumberFormatException e) {
		}
		for (Connection node : data) {
			if (node.getAddress().contains(search))
				ret += node.getInfo() + "\n";
			else if (node.getHost().contains(search))
				ret += node.getInfo() + "\n";
			else if (node.getPort() == port)
				ret += node.getInfo() + "\n";
		}
		if (ret.equals("") == true)
			return "No results found. Try again.";
		return ret;
	}

	/**
	 * Returns the Nodes connections
	 * 
	 * @return
	 */
	public String displayOverlay() {
		if (data.size() == 0)
			return "Node list is currently empty.";
		if (validOverlay == false)
			return "Overlay has not been constructed.";
		StringBuilder ret = new StringBuilder();
		int index = 0;
		for (int[] bytes : overlay) {
			ret.append(String.format("%02d -> ", index++));
			for (int b : bytes) {
				ret.append(b + " ");
			}
			ret.append("\n");
		}
		return ret.toString();
	}

	/**
	 * Returns the Nodes connections from the overlay.
	 * 
	 * @return String array format host:port host:port weight
	 */
	public String[] getConnections() throws Exception {
		if (data.size() == 0)
			throw new Exception("Node list is currently empty.");
		if (validOverlay == false)
			throw new Exception("Overlay has not been constructed.");
		int length = data.size();
		int size = (length * numberOfConnections) / 2;
		String[] ret = new String[size];
		for (int index = 0, rdex = 0; index < length; index++) {
			String established = data.get(index).getConnection() + " ";
			for (int outdex = index; outdex < length; outdex++) {
				if (overlay[index][outdex] != 0)
					ret[rdex++] = established + data.get(outdex).getConnection() + " " + overlay[index][outdex];
			}
		}
		return ret;
	}

	/**
	 * When a node connects to the Server and sends the registry request and is
	 * added to the list.
	 */
	public void addToList(Connection node) {
		synchronized (data) {
			data.add(node);
		}
	}

	/**
	 * If node disconnects between 'setup-overlay' and
	 * 'send-overlay-link-weights' then it will notify the server about the
	 * change, and make the overlay void.
	 */
	public void removeFromList(Connection node) {
		if (validOverlay == true) {
			validOverlay = false;
			System.out.println("The overlay is no longer correct. Please run 'setup-overlay' again.");
		}
		synchronized (data) {
			data.remove(node);
		}
	}

	/**
	 * The overlay is a byte[][] that when byte[x][y] != 0 lists the weight of
	 * the connection. To ensure that every node can connect to every other node
	 * first loop around the array and connect every node to two of its N
	 * closest nodes.
	 */
	public void buildOverlay() {
		Random rand = new Random();
		while (validOverlay == false) {

			overlay = new int[data.size()][data.size()];
			int size = overlay.length;
			int sum = 0;

			int weight = 0;
			for (int i = 0; i < size; i++) {
				int aI = ((i - 1) % size + size) % size;
				int bI = (i % size + size) % size;
				weight = (rand.nextInt(9) + 1);
				overlay[aI][bI] = overlay[bI][aI] = weight;
				sum += 2;
			}

			for (int row = 0; row < size; row++) {
				for (int column = 0; column < size; column++) {
					if (checkConnection(size, row, column) == true) {
						weight = rand.nextInt(9) + 1;
						overlay[row][column] = overlay[column][row] = weight;
						sum += 2;
					}
				}
			}
			if (sum == (size * numberOfConnections))
				validOverlay = true;
		}
	}

	/**
	 * To ensure that a node is not already connected as well as that node nor
	 * its partner will be connected to more then the numberOfConnections
	 * 
	 * @param size
	 *            The size of the array.
	 * @return sum The sum of the number of connections made.
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

}
