package cs455.overlay.node;

public class NodeSignal {

	/**
	 * 
	 */
	private NodeServer ns;
	
	/**
	 * 
	 */
	private NodeClient nc;
	
	/**
	 * 
	 */
	private NodeInterface ni;
	
	/**
	 * 
	 */
	private boolean stop = false;
	
	/**
	 * 
	 * @param ns
	 * @param nc
	 * @param ni
	 */
	public NodeSignal(NodeServer ns, NodeClient nc, NodeInterface ni) {
		this.ns = ns;
		this.nc = nc;
		this.ni = ni;
		stop = false;
	}

	public void watch() {
		while(stop == false) {
			if (ns.getStatus())
				stop();
			// TODO nc, ni
		}
	}
	
	public void stop() {
		ns.close();
		nc.close();
		ni.close();
	}

}
