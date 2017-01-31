package cs455.overlay.util;

import java.util.*;

/**
 * Dijkstra's algorithm to find shortest path from s to all other nodes
 * 
 * @author G van Andel
 */
public class Dijkstra {
	
	/**
	 * 
	 */
	private int sourceIndex;
	
	/**
	 * the overlay as constructed on the server.
	 */
	private byte[][] overlay;
	
	/**
	 * shortest known distance from "sourceIndex"
	 */
	private int[] dist;
	
	/**
	 * Preceeding node in path
	 */
	private int[] pred;
	
	/**
	 * The node information.
	 */
	private String[] hostInformation;

	/**
	 * Lets get the shortest path.
	 * 
	 * @param connections
	 * @param address
	 * @param port
	 */
	public Dijkstra (String[] connections, String address, int port) {
		int length = connections.length;
		dist = new int [length];  
		pred = new int [length];
		hostInformation = connections;
		this.getSource(address, port);
		for (int i = 0; i < length; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		dist[sourceIndex] = 0;
	}
	// [boise.cs.colostate.edu 129.82.44.133 46283, dover.cs.colostate.edu 129.82.44.143 44085]
	
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
	 * @param bytes
	 */
	public void addOverlay(String[] info) {
		int length = info.length;
		byte[][] o = new byte[length][length];
		for (int i = 0; i < length; i++) {
			System.out.print(info[i]+" ");
			String[] connections = info[i].split(" ");
			for (int j = 0; j < connections.length; j++) {
				System.out.print(connections[j]+" ");
			}
		}
		this.overlay = o;
	}
	
	/**
	 * Returns the Nodes connections
	 * @return
	 */
	public String displayOverlay() {
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
	
	public int[] getPathInformation(String hostName) {
		
		boolean [] visited = new boolean [pred.length]; // all false initially
		dist[sourceIndex] = 0;

		for (int i = 0; i < dist.length; i++) {
			final int next = minVertex (dist, visited);
			visited[next] = true;

			// The shortest path to next is dist[next] and via pred[next].

//			final int [] n = G.neighbors (next);
//			for (int j = 0; j < n.length; j++) {
//				final int v = n[j];
//				final int d = dist[next] + G.getWeight(next,v);
//				if (dist[v] > d) {
//					dist[v] = d;
//					pred[v] = next;
//				}
//			}
		}
		return pred;  // (ignore pred[s]==0!)
	}

	private int minVertex (int [] dist, boolean [] v) {
		int x = Integer.MAX_VALUE;
		int y = -1;   // graph not connected, or no unvisited vertices
		for (int i = 0; i < dist.length; i++) {
			if (v[i] == false && dist[i] < x) {
				y = i;
				x = dist[i];
			}
		}
		return y;
	}

	public void printPath (String[] connections, int [] pred, int s, int e) {
		final ArrayList<String> path = new ArrayList<String>();
//		int x = e;
//		while (x != s) {
//			path.add (0, G.getLabel(x));
//			x = pred[x];
//		}
//		path.add (0, G.getLabel(s));
		System.out.println(path);
	}

}

