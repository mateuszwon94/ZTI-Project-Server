package zti.server.data;

public class Node implements Comparable<Node> {
	public Node(Stop stop) {
		this(stop, null);
	}
	
	public Node(Stop stop, Node parent) {
		this.stop = stop;
		this.parent = parent;
	}
	
	public float getAllCost() {
		return costFromStart + costToEnd;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Node))return false;
	    Node otherNode = (Node)other;
	    return stop.getId().equals(otherNode.stop.getId());
	}
	
	@Override
	public int compareTo(Node o) {
		if ( getAllCost() < o.getAllCost() ) return -1;
		if ( getAllCost() == o.getAllCost() ) return 0;
		return 1;
	}
	
	public float costFromStart = 0;
	public float costToEnd = Float.MIN_VALUE;
	
	public Stop stop;
	public Node parent;
}