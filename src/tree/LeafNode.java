package tree;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author saurabhagarwal
 * Doc Source : http://www.mec.ac.in/resources/notes/notes/ds/bplus.htm
 * 	¥ A leaf node in a B + -Tree consists of a set of key values and data pointers.
	Each key value has one data pointer. The key values and data pointers are
	ordered by the key values.
	¥ The data pointer points to a record or block in the database that contains
	the record identified by the key value. For instance, in the example,
	above, the pointer attached to key value 7 points to the record identified by the value 7.
	¥ Searching a leaf node for a key value begins at the leftmost value and
	moves rightwards until a matching key is found.
	¥ The leaf node also has a pointer to its immediate sibling node in the tree.
	The sibling node is the node immediately to the right of the current node.
	Because of the order of keys in the B + -Tree the sibling pointer always
	points to a node that has key values that are greater than the key values in
	the current node.
 */
public class LeafNode<V> extends Node<V>{
	protected LeafNode<V> sibling;
	private V[] values;
	
	public LeafNode(int maxKeys) {
		super(maxKeys);
		
		values = (V[]) new Object[maxKeys];
	}

	@Override
	protected InternalNode<V> insert(int key, V value) {
		if(nKeys == maxKeys){ // The Node is full. Need to split
			int mid = (nKeys + 1) / 2;
			int nSibling = nKeys - mid;
			this.nKeys = mid;
			
			sibling = new LeafNode<V>(maxKeys);
			sibling.nKeys = nSibling;
			System.arraycopy(this.keys, mid, sibling.keys, 0, nSibling); //Copying the keys
			System.arraycopy(this.values, mid, sibling.values, 0, nSibling); //Copying the values
			
			// We need a new InternalNode as a parent to this and sibling.
			InternalNode<V> parent = new InternalNode<V>(maxKeys);
			parent.nKeys = 1;
			parent.keys[0] = this.keys[mid - 1];
			parent.pointers[0] = this;
			parent.pointers[1] = sibling;
			
			if(key <= parent.keys[0]){
				this.insert(key, value);
			}else{
				sibling.insert(key, value);
			}
			
			return parent;
		}else{
			int pos = findInsertPos(key);
			if(pos != nKeys){
				// Shift the array from index pos by one place
				System.arraycopy(keys, pos, keys, pos + 1, nKeys - pos);
				System.arraycopy(values, pos, values, pos + 1, nKeys - pos);
			}
			
			//Set the pointers right
			this.keys[pos] = key;
			this.values[pos] = (value);
			
			// Increment the number of keys
			nKeys++;
		}
		
		return null;
	}

	@Override
	public void print() {
		System.out.println("Printing leaf node : ");
		for(int i = 0; i < nKeys; i++){
			System.out.print(keys[i] + " --> " + values[i] + ", ");
		}
		System.out.println("");
	}

	@Override
	protected List<V> search(int key) {
		List<V> vals = new ArrayList<V>();
		int i = 0;
		for(; i < nKeys; i++){
			if(key == keys[i]){
				vals.add(values[i]);
			}else if(key < keys[i]) break;
		}
		if(i == nKeys && sibling != null){
			vals.addAll(sibling.search(key));
		}
		return vals;
	}
}
