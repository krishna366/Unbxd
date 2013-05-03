package tree;

import java.util.List;


public abstract class Node<V> {
	protected final int maxKeys;
	protected int nKeys;
	protected int[] keys;

	public Node(int maxKeys) {
		super();
		
		this.maxKeys = maxKeys;
		keys = new int[maxKeys];
		nKeys = 0;
	}
	
	protected abstract InternalNode<V> insert(int key, V value);
	
	protected abstract List<V> search(int key);
	
	public abstract void print();
	
	/**
	 * Finds the index at which key should be inserted
	 * @param key
	 * @return Index at which the key should be inserted
	 */
	protected int findInsertPos(int key){
		if(nKeys == 0 || key < this.keys[0]) return 0;
		if(key > this.keys[nKeys - 1]) return nKeys;
		
		int lb = 0;
		int ub = nKeys - 1;
		return binSearch(key, lb, ub);
	}
	
	protected int binSearch(int key, int lb, int ub){
		if(keys[lb] >= key) return lb;
		
		int mid = (lb + ub) / 2;
		
		if(keys[mid] < key){
			lb = mid + 1;
		}else{
			ub = mid;
		}
		
		return binSearch(key, lb, ub);
	}
}
