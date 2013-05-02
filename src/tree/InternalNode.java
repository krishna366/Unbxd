package tree;

import java.util.List;



/**
 * 
 * @author saurabhagarwal
 * Doc Source : http://www.mec.ac.in/resources/notes/notes/ds/bplus.htm
 * 	¥ An internal node in a B + -Tree consists of a set of key values and pointers.
	The set of keys and values are ordered so that a pointer is followed by a
	key value. The last key value is followed by one pointer.
	¥ Each pointer points to nodes containing values that are less than or equal
	to the value of the key immediately to its right
	¥ The last pointer in an internal node is called the infinity pointer. The
	infinity pointer points to a node containing key values that are greater than
	the last key value in the node.
	¥ When an internal node is searched for a key value, the search begins at the
	leftmost key value and moves rightwards along the keys.
	¥ If the key value is less than the sought key then the pointer to the
	left of the key is known to point to a node containing keys less than
	the sought key.
	¥ If the key value is greater than or equal to the sought key then the
	pointer to the left of the key is known to point to a node containing
	keys between the previous key value and the current key value.
 */
public class InternalNode<V> extends Node<V>{
	
	protected Node<V>[] pointers;
	
	@SuppressWarnings("unchecked")
	public InternalNode(int maxKeys) {
		super(maxKeys);
		
		pointers = new Node[maxKeys + 1]; // pointers[nKeys] is the infinity pointer. 
	}
	
	/**
	 * This method is called only in scenarios where the node is not full
	 * @param node
	 */
	private void insert(InternalNode<V> node){
		int pos = findInsertPos(node.keys[0]);
		if(pos != nKeys){
			// Shift the array from index pos by one place
			System.arraycopy(keys, pos, keys, pos + 1, nKeys - pos);
			System.arraycopy(pointers, pos, pointers, pos + 1, nKeys - pos + 1);
		}
		
		//Set the pointers right
		this.keys[pos] = node.keys[0];
		this.pointers[pos] = node.pointers[0];
		this.pointers[pos + 1] = node.pointers[1];
		
		// Increment the number of keys
		nKeys++;
		
		//Siblings belonging to adjacent subtrees have to be connected in case the pointers are leafnodes
		if(this.pointers.length > pos + 2 && this.pointers[pos + 1] instanceof LeafNode && this.pointers[pos + 2] instanceof LeafNode){
			((LeafNode)this.pointers[pos + 1]).sibling = (LeafNode)this.pointers[pos + 2];
		}
	}

	@Override
	protected InternalNode<V> insert(int key, V value) {
		int pos = findInsertPos(key);
		InternalNode<V> node = pointers[pos].insert(key, value);
		if(node != null){ // There was a split and a new node was created. It has to be accomodated.
			if(nKeys == maxKeys){ // The Node is full. Need to split
				int mid = (nKeys + 1) / 2;
				int nSibling = nKeys - mid;
				this.nKeys = mid - 1;
				
				InternalNode<V> sibling = new InternalNode<V>(maxKeys);
				sibling.nKeys = nSibling;
				System.arraycopy(this.keys, mid, sibling.keys, 0, nSibling); //Copying the keys
				System.arraycopy(this.pointers, mid, sibling.pointers, 0, nSibling + 1); //Copying the pointers including infinity pointer
				
				// We need a new InternalNode as a parent to this and sibling.
				InternalNode<V> parent = new InternalNode<V>(maxKeys);
				parent.nKeys = 1;
				parent.keys[0] = this.keys[mid - 1];
				parent.pointers[0] = this;
				parent.pointers[1] = sibling;
				
				if(node.keys[0] <= parent.keys[0]){
					this.insert(node);
				}else{
					sibling.insert(node);
				}
				
				return parent;
			}else{
				this.insert(node);
			}
		}
		
		return null;
	}
	
	public void print(){
		System.out.println("Printing internal node : ");
		for(int i = 0; i <= nKeys; i++){
			if(i < nKeys)
				System.out.print(keys[i] + " --> ");
			else
				System.out.print("Inf --> ");
			
			Node<V> node = pointers[i];
			for(int j = 0; j < node.nKeys; j++){
				System.out.print(node.keys[j] + " ");
			}
			System.out.println("");
		}
		
		for(int i = 0; i <= nKeys; i++){
			Node<V> node = pointers[i];
			node.print();
		}
	}

	@Override
	protected List<V> search(int key) {
		for(int i = 0; i < nKeys; i++){
			if(key <= keys[i])
				return pointers[i].search(key);
		}
		return pointers[nKeys].search(key);
	}

}
