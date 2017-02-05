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
	 *            [129.82.44.133]
	 * @param port
	 *            [46283]
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
		}
		this.calculatePaths();
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
	* Take the unvisited node with minimum weight, then Visit all its link
	* update the distances for all the link (In the Priority Queue), you
	* repeat the process till all the connected nodes are visited.
	*/
	public void calculatePaths() {
		Node source = graph.getNode(connections[sourceIndex]);
		source.minDistance = 0;
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(source);
		
		while (queue.isEmpty() == false) {
			Node u = queue.poll();
			for (Edge link : u.links) {
				int newDist = u.minDistance + link.cost;
				if (link.target.minDistance > newDist) {
					queue.remove(link.target);
					link.target.minDistance = newDist;
					link.target.path = new LinkedList<Node>(u.path);
					link.target.path.add(u);
					queue.add(link.target);
				}
			}
		}
	}
	
	/**
	 * Get a random node in the overlay.
	 * @param index
	 * 		random index of connection
	 * @return "ip:port"
	 */
	public String getRandomNode() {
		Random rand = new Random();
		int temp = sourceIndex;
		int index = sourceIndex;
		while(temp == (index = rand.nextInt(connections.length)));
		return connections[index];
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String[] getDist() {
		String[] ret = new String[connections.length];
		int index = 0;
		for (Node node : graph.getNodes()) 
			ret[index++] = node + "~"+ node.minDistance;
		return ret;
	}

	/**
	 * TODO
	 * @return
	 */
	public String[] getPaths() {
		String[] ret = new String[connections.length];
		int index = 0;
		for (Node node : graph.getNodes()) {
			ret[index] = "";
			for (Node path : node.path) {
				ret[index] += path + " ";
			}
			ret[index++] += node + " " + node.minDistance;
		}
		return ret;
	}
	
	/**
	 * TODO
	 * @param dest
	 * @return
	 */
	public String getNextHop(String dest) {
		Node node = graph.getNode(dest);
		if (dest.equals(node.toString()))
			return node.toString();
		return node.path.get(1).toString();
	}

	public class Graph {

		private ArrayList<Node> nodes;

		public Graph(String[] connections){
			int len = connections.length;
			nodes = new ArrayList<Node>(len);
			for (String i : connections)
				nodes.add(new Node(i));
		}
		
		public void addEdge(int src, int dest, int cost){
			nodes.get(src).links.add(new Edge(nodes.get(dest), cost));
			nodes.get(dest).links.add(new Edge(nodes.get(src), cost));
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
		public ArrayList<Edge> links;
		public LinkedList<Node> path;
		public int minDistance;
		public Node previous;

		public Node(String name){
			this.name = name;
			this.minDistance = Integer.MAX_VALUE;
			links = new ArrayList<Edge>();
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
			return name;
		}
	}

}
