package cs455.overlay.util;

import java.util.*;

/**
 * Dijkstra's algorithm to find shortest path from s to all other nodes
 * 
 * @author G van Andel
 */
public class Dijkstra {

	/**
	 * The index of the current Node in the overlay, dist, pred, hostInfomation
	 */
	private int sourceIndex;

	/**
	 * the overlay as constructed on the server.
	 */
	private int[][] overlay;

	/**
	 * The node information.
	 */
	private String[] hostInformation;

	/**
	 * Lets get the shortest path.
	 * 
	 * @param connections
	 *            [boise.cs.colostate.edu 129.82.44.133 46283]
	 * @param address
	 * @param port
	 */
	public Dijkstra(String[] connections, String address, int port) {
		hostInformation = connections;
		this.getSource(address, port);
	}

	/**
	 * Set the sourceIndex of the hostNode
	 * 
	 * @param address
	 * @param port
	 */
	private void getSource(String address, int port) {
		int index = 0;
		for (String node : hostInformation) {
			String[] info = node.split(" ");
			int tPort = Integer.parseInt(info[2]);
			if (info[1].equals(address) == true && port == tPort)
				this.sourceIndex = index;
			index++;
		}
	}

	/**
	 * Sets the weights of the nodes in the
	 * 
	 * @param info
	 *            [ip:port ip:port cost]
	 */
	public void addOverlay(String[] info) {
		int length = info.length;
		int len = hostInformation.length;
		int[][] o = new int[len][len];
		for (int i = 0; i < length; i++) {
			String[] connections = info[i].split(" ");
			int row = getIndex(connections[0]);
			int col = getIndex(connections[1]);
			byte temp = Byte.parseByte(connections[2]);
			o[row][col] = o[col][row] = temp;
		}
		this.overlay = o;
	}

	/**
	 * Get the index of the connection
	 * 
	 * @param addres
	 *            [address:port]
	 */
	private int getIndex(String address) {
		int index = 0;
		String[] host = address.split(":");
		int sPort = Integer.parseInt(host[1]);
		for (String node : hostInformation) {
			String[] info = node.split(" ");
			int tPort = Integer.parseInt(info[2]);
			if (info[1].equals(host[0]) == true && sPort == tPort)
				return index;
			index++;
		}
		return -1;
	}

	/**
	 * Returns the Nodes connections
	 * 
	 * @return
	 */
	public String displayOverlay() {
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

	public int[] getPathInformation() {
		int len = overlay.length;
		int[] dist = new int[len];
		int[] prev = new int[len];
		boolean[] Q = new boolean[len];
		for (int i = 0; i < len; i++) {
			if (overlay[sourceIndex][i] == 0 && i != sourceIndex)
				dist[i] = Integer.MAX_VALUE;
			else
				dist[i] = overlay[sourceIndex][i];
			prev[i] = -1;
		}
		boolean flag = true;
		while (flag == true) {
		}
		System.out.println(Arrays.toString(dist)); // DEBUG
		
		return dist;
	}

	private int minVertex(int dist) {
		int x = Integer.MAX_VALUE;
		int y = -1; // graph not connected, or no unvisited vertices
		for (int i = 0; i < overlay.length; i++) {
			if (v[i] == false && overlay[index][i] < x) {
				y = i;
//				x = dist[i];
			}
		}
		return y;
	}
	
	public static void main(String args[]) {
		String[] connections =  {
				"a 1 1", "b 1 2",
				"c 1 3", "d 1 4"
				};
		String one = "1"; int port = 1;
		Dijkstra d = new Dijkstra(connections, one, port);
		String[] info =  {
				"1:1 1:2 1", "1:1 1:3 3",
				"1:3 1:4 1", "1:1 1:4 1"
				};
		d.addOverlay(info);
		
		System.out.println(d.displayOverlay());
		
		d.getPathInformation();
	}

}
