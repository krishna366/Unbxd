package tree;

import java.util.List;


/**
 * 
 * @author saurabhagarwal
 *
 * @param <V> The type of values stored in the tree
 * 
 * Doc Source : http://www.mec.ac.in/resources/notes/notes/ds/bplus.htm
 *  ¥ A   B + -Tree consists of one or more blocks of data, called nodes, linked 
	together by pointers. The B + -Tree is a tree structure. The tree has a single
	node at the top, called the root node. The root node points to two or more
	blocks , called child nodes. Each child nodes points to further child nodes
	and so on.
	¥ The B + -Tree consists of two types of (1) internal nodes and (2) leaf nodes:
	¥ Internal nodes point to other nodes in the tree.
	¥ Leaf nodes point to data in the database using data pointers. Leaf
	nodes  also contain an additional pointer, called the sibling pointer,
	which is used to improve the efficiency of certain types of search.
	¥ All the nodes in a B + -Tree must be at least half full except the root node
	which may contain a minimum of two entries. The algorithms that allow
	data to be inserted into and deleted from a B + -Tree guarantee that each
	node in the tree will be at least half full.
	¥ Searching for a value in the B + -Tree always starts at the root node and
	moves downwards until it reaches a leaf node.
	¥ Both internal and leaf nodes contain key values that are used to guide the
	search for entries in the index.
	¥ The B + -Tree is called a balanced tree because every path from the root
	node to a leaf node is the same length. A balanced tree means that all
	searches for individual values require the same number of nodes to be
	read from the disc.
	
	Notes :
	1. Delete not implemented 
	2. Range queries might not work correctly. Skipped as it was not a use case in the problem statement
 */
public class BPlusTree<V> {
	// Root node of the tree.
	private Node<V> root;
	
	// Max no. of keys in internal nodes.
	private final int iMax;
	
	//Max no. of keys in leaf nodes
	private final int lMax;
	
	public BPlusTree(int iMax, int lMax) {
		super();
		this.iMax = iMax;
		this.lMax = lMax;
		
		this.root = new LeafNode<V>(lMax);
	}
	
	public Node<V> getRoot() {
		return root;
	}
	
	public void setRoot(Node<V> root) {
		this.root = root;
	}
	
	public int getiMax() {
		return iMax;
	}
	
	public int getlMax() {
		return lMax;
	}
	
	public void add(Object key, V value){
		InternalNode<V> node = this.root.insert(key.hashCode(), value);
		if(node != null){ //There was a split
			this.root = node;
		}
	}
	
	public void print(){
		root.print();
	}
	
	public List<V> search(Object key){
		return root.search(key.hashCode());
	}
}
