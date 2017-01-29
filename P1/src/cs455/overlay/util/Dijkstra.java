package cs455.overlay.util;

import java.util.ArrayList;

/**
 * Dijkstra's algorithm to find shortest path from s to all other nodes
 * 
 * @author G van Andel
 */
public class Dijkstra {

	public int [] dijkstra (String[] connections, int s) {
		final int [] dist = new int [connections.length];  // shortest known distance from "s"
		final int [] pred = new int [connections.length];  // preceeding node in path
		final boolean [] visited = new boolean [connections.length]; // all false initially

		for (int i=0; i<dist.length; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		dist[s] = 0;

		for (int i = 0; i < dist.length; i++) {
			final int next = minVertex (dist, visited);
			visited[next] = true;

			// The shortest path to next is dist[next] and via pred[next].

			final int [] n = G.neighbors (next);
			for (int j = 0; j < n.length; j++) {
				final int v = n[j];
				final int d = dist[next] + G.getWeight(next,v);
				if (dist[v] > d) {
					dist[v] = d;
					pred[v] = next;
				}
			}
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
		int x = e;
		while (x != s) {
			path.add (0, G.getLabel(x));
			x = pred[x];
		}
		path.add (0, G.getLabel(s));
		System.out.println (path);
	}

}

