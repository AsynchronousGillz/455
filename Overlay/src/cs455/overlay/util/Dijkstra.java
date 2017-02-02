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
	private final int sourceIndex;

	/**
	 * The node information.
	 */
	private final String[] connections;
	
	/**
	 * Graph for Dijkstra
	 */
	private Graph graph;

	/**
	 * Lets get the shortest path.
	 * 
	 * @param connections
	 *            [129.82.44.133:46283]
	 * @param address
	 * @param port
	 */
	public Dijkstra(String[] connections, String address, int port) {
		int index = 0;
		for (String node : connections) {
			String[] info = node.split(":");
			int tPort = Integer.parseInt(info[1]);
			if (info[0].equals(address) == true && port == tPort)
				break;
			index++;
		}
		this.sourceIndex = index;
		this.graph = new Graph(connections);
		this.connections = connections;
	}

	/**
	 * Sets the weights of the nodes in the
	 * 
	 * @param info
	 *            [ip:port ip:port cost]
	 */
	public void addOverlay(String[] info) {
		int length = info.length;
		for (int i = 0; i < length; i++) {
			String[] connections = info[i].split(" ");
			int row = getIndex(connections[0]);
			int col = getIndex(connections[1]);
			int tem = Integer.parseInt(connections[2]);
			graph.addEdge(row, col, tem);
		}
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
		for (String node : connections) {
			String[] info = node.split(" ");
			int tPort = Integer.parseInt(info[2]);
			if (info[1].equals(host[0]) == true && sPort == tPort)
				return index;
			index++;
		}
		return -1;
	}


	/** 
	* Take the unvisited node with minimum weight, then Visit all its neighbors
	* update the distances for all the neighbors (In the Priority Queue), you
	* repeat the process till all the connected nodes are visited.
	*/
	public void calculate(){
		Vertex source = graph.getVertex(connections[sourceIndex]);
		source.minDistance = 0;
		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
		queue.add(source);
		
		while(queue.isEmpty() == false){
			
			Vertex u = queue.poll();
		
			for(Edge neighbour : u.neighbours){
				int newDist = u.minDistance + neighbour.cost;
				
				if(neighbour.target.minDistance>newDist){
					// Remove the node from the queue to update the distance value.
					queue.remove(neighbour.target);
					neighbour.target.minDistance = newDist;
					
					// Take the path visited till now and add the new node.s
					neighbour.target.path = new LinkedList<Vertex>(u.path);
					neighbour.target.path.add(u);
					
					//Reenter the node with new distance.
					queue.add(neighbour.target);
				}
			}
		}
	}

	public class Graph {

		private ArrayList<Vertex> vertices;

		public Graph(String[] connections){
			int len = connections.length;
			vertices = new ArrayList<Vertex>(len);
			for(String i : connections){
				vertices.add(new Vertex(i));
			}
		}
		
		public void addEdge(int src, int dest, int cost){
			vertices.get(src).neighbours.add(new Edge(vertices.get(dest), cost));
		}
		
		public ArrayList<Vertex> getVertices() {
			return vertices;
		}
		
		public Vertex getVertex(String address){
			return vertices.get(vertices.indexOf(address));
		}
	}

	class Edge{
		public final Vertex target;
		public final int cost;

		public Edge(Vertex target, int cost){
			this.target = target;
			this.cost = cost;
		}
	}

	class Vertex implements Comparable<Vertex> {

		public final String name;
		public ArrayList<Edge> neighbours;
		public LinkedList<Vertex> path;
		public int minDistance = Integer.MAX_VALUE;
		public Vertex previous;

		public Vertex(String name){
			this.name = name;
			neighbours = new ArrayList<Edge>();
			path = new LinkedList<Vertex>();
		}

		public int compareTo(Vertex other){
			return Integer.compare(minDistance, other.minDistance);
		}
		
		public boolean equals(Object obj) {
			if (obj instanceof Vertex == false)
				return false;
			Vertex v = (Vertex) obj;
			if (name == null && v.name != null)
				return false;
			else if (name.equals(v.name) == false)
				return false;
			return true;
		}

		public String toString(){
			return name;
		}
	}

}
