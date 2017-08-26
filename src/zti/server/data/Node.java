package zti.server.data;

/**
 * @author Mateusz Winiarski
 * Klasa bedaca reprezentacja wezla wykorzystywanego w algorytmie A*
 */
public class Node implements Comparable<Node> {
	/**
	 * Konstruktor dla wezela bez rodzica
	 * @param stop przystanek jaki reprezentuje wezel
	 */
	public Node(Stop stop) {
		this(stop, null);
	}
	
	/**
	 * Konstruktor dla wezla z rodzicem
	 * @param stop przystanek jaki reprezentuje wezel
	 * @param parent wezel bedacy rodzicem tworzonego
	 */
	public Node(Stop stop, Node parent) {
		this.stop = stop;
		this.parent = parent;
	}
	
	/**
	 * @return calkowity koszt dotarcia do wezla koncowego z przejsciem przez ten wezel
	 */
	public float getAllCost() {
		return costFromStart + costToEnd;
	}
	
	/**
	 * Sprawdza czy dwa wezly roprezentuja ten sam przystanek
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Node)) return false;
	    Node otherNode = (Node)other;
	    return stop.getId().equals(otherNode.stop.getId());
	}
	
	/**
	 * Porownuje dwa wezly na podstawie calkowitego kosztu
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Node o) {
		if ( getAllCost() < o.getAllCost() ) return -1;
		if ( getAllCost() == o.getAllCost() ) return 0;
		return 1;
	}
	
	/**
	 * Koszt dotarcia do wezla
	 */
	public float costFromStart = 0;
	
	/**
	 * Przewidiwany koszt dotarcia do wezle koncowego
	 */
	public float costToEnd = Float.MIN_VALUE;
	
	/**
	 * Przystanek jaki reprezentuje wezel
	 */
	public Stop stop;
	
	/**
	 * Wezel bedacy rodzicem obecnego
	 */
	public Node parent;
}