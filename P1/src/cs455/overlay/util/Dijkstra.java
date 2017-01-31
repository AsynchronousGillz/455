package cs455.overlay.util;

import java.util.ArrayList;

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
	 * @param hostName
	 * @param port
	 */
	public Dijkstra (String[] connections, String address, int port) {
		dist = new int [connections.length];  
		pred = new int [connections.length];
		hostInformation = connections;
		this.getSource(address, port);

		for (int i=0; i<dist.length; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
	}
	
	/**
	 * Set the sourceIndex of the hostNode
	 * @param hostName
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
	
	public void addConnections(String host) {
		
	}
	
	public void addWeights() {
		
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

