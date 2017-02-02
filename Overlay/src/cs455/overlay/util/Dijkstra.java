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
	 * 
	 */
	private PriorityQueue<Node> queue;
	
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
		for (String i : info) {
			String[] connections = i.split(" ");
			int row = getIndex(connections[0]);
			int col = getIndex(connections[1]);
			int tem = Integer.parseInt(connections[2]);
			graph.addEdge(row, col, tem);
			graph.addEdge(col, row, tem);
		}
		this.calculate();
	}

	/**
	 * Get the index of the connection
	 * 
	 * @param addres
	 *            [address:port]
	 */
	private int getIndex(String address) {
		int index = 0;
		for (String node : connections) {
			if (node.equals(address) == true)
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
	public void calculate() {
		Node source = graph.getNode(connections[sourceIndex]);
		source.minDistance = 0;
		queue = new PriorityQueue<Node>();
		queue.add(source);
		
		while (queue.isEmpty() == false) {
			
			Node u = queue.poll();
		
			for (Edge neighbors : u.neighbors) {
				int newDist = u.minDistance + neighbors.cost;
				
				if (neighbors.target.minDistance > newDist) {
					// Remove the node from the queue to update the distance value.
					queue.remove(neighbors.target);
					neighbors.target.minDistance = newDist;
					
					// Take the path visited till now and add the new node.s
					neighbors.target.path = new LinkedList<Node>(u.path);
					neighbors.target.path.add(u);
					
					//Reenter the node with new distance.
					queue.add(neighbors.target);
				}
			}
		}
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String[] getPaths() {
		int len = connections.length;
		String[] ret = new String[len];
		Node source = graph.getNode(connections[sourceIndex]);
		int index = 0;
		for (Edge neighbors : source.neighbors) {
			ret[index++] = neighbors.target + " " + neighbors.cost;
		}
		return ret;
	}

	public class Graph {

		private ArrayList<Node> nodes;

		public Graph(String[] connections){
			int len = connections.length;
			nodes = new ArrayList<Node>(len);
			for(String i : connections){
				nodes.add(new Node(i));
			}
		}
		
		public void addEdge(int src, int dest, int cost){
			nodes.get(src).neighbors.add(new Edge(nodes.get(dest), cost));
		}
		
		public ArrayList<Node> getNodes() {
			return nodes;
		}
		
		public Node getNode(String address){
			return nodes.get(nodes.indexOf(new Node(address)));
		}
		
		public String toString() {
			return nodes.toString();
		}
	}

	class Edge{
		public final Node target;
		public final int cost;

		public Edge(Node target, int cost){
			this.target = target;
			this.cost = cost;
		}
		
		public String toString() {
			return target + " " + cost;
		}
	}

	class Node implements Comparable<Node> {

		public final String name;
		public ArrayList<Edge> neighbors;
		public LinkedList<Node> path;
		public int minDistance = Integer.MAX_VALUE;
		public Node previous;

		public Node(String name){
			this.name = name;
			neighbors = new ArrayList<Edge>();
			path = new LinkedList<Node>();
		}

		public int compareTo(Node other){
			return Integer.compare(minDistance, other.minDistance);
		}
		
		public boolean equals(Object obj) {
			if (obj instanceof Node == false)
				return false;
			Node v = (Node) obj;
			if (name == null && v.name != null)
				return false;
			else if (name.equals(v.name) == false)
				return false;
			return true;
		}

		public String toString(){
			return name + " " + minDistance;
		}
	}

}
