package searchTrees;

/**
 * Ordered map that uses the AVL tree as its supporting data structure.
 * @author Renato Scudere.
 * @param <K extends Comparable<? super K>, V> The generic parameters used throughout. K must be comparable or provide a Comparator, or else this class doesn't work.
 *
 */
public class TreeMap<K extends Comparable<? super K>, V> {
	
	/**
	 * Pair class used to store the key/value pairs.
	 * @author Renato Scudere.
	 *
	 */
	private class Pair implements Comparable<Pair>
	{
		/**
		 * Stores the key.
		 */
		K key;
		
		/**
		 * Stores the value.
		 */
		V value;
		
		/**
		 * Constructor simply calls the overloaded constructor.
		 * @param key The key being used.
		 */
		public Pair(K key)
		{
			this(key, null);
		}
		
		/**
		 * Overloaded constructor that accepts a key and value.
		 * @param key The key being used.
		 * @param value The value being stored alongside the key.
		 * @throws IllegalArgumentException if the key is null (key cannot be null).
		 */
		public Pair(K key, V value)
		{
			if(key == null)
				throw new IllegalArgumentException("The key cannot be null.");
			
			this.key = key;
			this.value = value;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(Pair o) {
			return this.key.compareTo(o.key);
		}
		
		/**
		 * {@inheritDoc}
		 */
		public String toString()
		{
			return "<" + key + ", " + value + ">";
		}
	}
	
	/**
	 * The tree that supports the map. Holds pairs as its nodes.
	 */
	private AVLTree<Pair> treeMap;
	
	/**
	 * Constructor creates an empty map.
	 */
	public TreeMap()
	{
		treeMap = new AVLTree<>();
	}
	
	/**
	 * Overloaded constructor creates a new map with a key and null value.
	 * @param key The key being added to the map.
	 */
	public TreeMap(K key)
	{
		treeMap = new AVLTree<>(new Pair(key));
	}
	
	/**
	 * Overloaded constructor creates a new map with a key and value.
	 * @param key The key being added.
	 * @param value The map being added.
	 */
	public TreeMap(K key, V value)
	{
		treeMap = new AVLTree<>(new Pair(key, value));
	}
	
	/**
	 * Checks to see if the map is empty by calling AVLTree's isEmpty method.
	 * @return True if the map is empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		return treeMap.isEmpty();
	}
	
	/**
	 * Clears the map by calling AVLTree's makeEmpty method.
	 */
	public void makeEmpty()
	{
		treeMap.makeEmpty();
	}
	
	/**
	 * Add method that calls the overloaded add with a key and null value.
	 * @param key The key being added.
	 * @return True if the pair was added, false otherwise.
	 */
	public boolean add(K key)
	{
		return add(key, null) ? true : false;
	}
	
	/**
	 * Overloaded add method that adds the key/value pair to the map.
	 * @param key The key being added.
	 * @param value The value being added with the corresponding key.
	 * @return True if the pair was added, false otherwise.
	 */
	public boolean add(K key, V value)
	{
		try
		{
			treeMap.insert(new Pair(key, value));
		}
		catch(IllegalArgumentException e)
		{
			/* Since the insert method in AVLTree can throw 2 different IllegalArgumentExceptions, 
			   if it is the one where the keys are the same, then we want to update the map instead.
			*/
			if(e.getMessage().equals("The data cannot be null."))
				return false;
			
			//Updates the value at the key if the key is already in the tree.
			update(key, value);
		}
		
		return true;
	}
	
	/**
	 * Helper method that updates the value of a Pair in the map.
	 * @param key The key used to search.
	 * @param value The value being used to update the Pair's existing value.
	 */
	public void update(K key, V value)
	{
		Pair change = treeMap.find(new Pair(key));
		change.value = value;
	}
	
	/**
	 * Removes the pair from the map. Fails if the pair is not in the map.
	 * @param key The key being used for removal.
	 * @return True if the pair was removed, false otherwise.
	 */
	public boolean remove(K key)
	{
		try
		{
			treeMap.remove(new Pair(key));
		}
		catch(IllegalArgumentException e)
		{
			System.out.println(e.toString());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Finds the given item in the map.
	 * @param key The key being used to search.
	 * @return The value at the given key, or null if it's not in the map.
	 */
	public V find(K key)
	{
		try
		{
			return valueAt(treeMap.find(new Pair(key)));
		}
		catch(IllegalArgumentException e)
		{
			System.out.println(e.toString());
		}
		
		return null;
		
	}
	
	/**
	 * Finds the smallest value in the map.
	 * @return The smallest value.
	 */
	public V findMin()
	{
		return valueAt(treeMap.findMin());
	}
	
	/**
	 * Finds the largest value in the map.
	 * @return The largest value.
	 */
	public V findMax()
	{
		return valueAt(treeMap.findMax());
	}
	
	/**
	 * Method that returns a TreeSet of only the keys in the map.
	 * @return A TreeSet containing only the keys.
	 */
	public TreeSet<K> keySet()
	{
		//The new tree set.
		TreeSet<K> keySet = new TreeSet<>();
		
		//Used to hold the key value from the retrieved pair.
		K key = null;
		
		//Performs a level-order traversal over all the nodes in the treeMap tree.
		for(AVLTree.AVLNode<Pair> itr : treeMap)
		{
			//Gets the key from the itr pair and checks if it's null.
			key = keyAt(itr);
			
			//Adds the key to the set if the key is not null.
			if(key != null)
				keySet.add(key);
		}
		
		//Returns the set.
		return keySet;
	}
	
	/**
	 * Helper method that returns the value of a Pair object.
	 * @param newPair The pair being tested.
	 * @return The value if the pair is not null, null otherwise.
	 */
	public V valueAt(Pair newPair)
	{
		return newPair != null ? newPair.value : null;
	}
	
	/**
	 * Helper method that returns the key of a Pair object.
	 * @param newPair The pair being tested.
	 * @return The key if the pair is not null, null otherwise.
	 */
	public K keyAt(AVLTree.AVLNode<Pair> newPair)
	{
		return newPair != null ? newPair.data.key : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return toString(false);
	}
	
	/**
	 * Overloaded toString method that prints either a breadth-first or in-order traversal of the tree.
	 * @param val Boolean value used to choose between the two traversals.
	 * @return The string version of the map.
	 */
	public String toString(boolean val)
	{
		StringBuilder string = new StringBuilder();
		
		//Makes the decision on whether to use breadth-first or in-order traversals.
		//True for breadth-first, false for in-order.
		if(val == true)
			return treeMap.toString();
		else
		{
			if(!isEmpty())
			{
				//Gets the in-order representation of the tree map.
				string = inOrder(treeMap.getRoot(), string);
				
				//Adds the opening bracket to the string.
				string.insert(0, "[ ");
				
				//Deletes the last 2 characters of the string, which are a comma and space(", ") and replaces it with the ending bracket.
				string.replace(string.length() - 2, string.length() - 1, " ]");
			}
			else
				string.append("[ ]");
			
		}
			
		return string.toString();
	}
	
	/**
	 * Helper method that produces the in-order traversal of the tree.
	 * @param node The node currently being looked at.
	 * @param string The string to which data will be appended to.
	 * @return The newly created string.
	 */
	private StringBuilder inOrder(AVLTree.AVLNode<Pair> node, StringBuilder string)
	{
		//Base case.
		if(node == null)
			return null;
		
		//Appends the left subtree to the string first.
		//Appends the current node to the string second.
		//Appends the right subtree to the string third.
		inOrder(node.left, string);
		string.append(node.data + ", ");
		inOrder(node.right, string);
		
		return string;
	}
}
