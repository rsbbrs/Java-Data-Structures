package searchTrees;

/**
 * Ordered set data structure that uses the AVLTree to support it.
 * @author Renato Scudere.
 * @param <T extends Comparable<? super T>> The generic parameter used throughout. Must be comparable or have a Comparator, or else the set won't work.
 *
 */
public class TreeSet<T extends Comparable<? super T>>{
	
	/**
	 * The tree that will hold the set.
	 */
	private AVLTree<T> treeSet;
	
	/**
	 * Constructor that creates an empty set.
	 */
	public TreeSet()
	{
		treeSet = new AVLTree<>();
	}
	
	/**
	 * Overloaded constructor that creates a set with one element.
	 * @param value The value of the element to be added.
	 */
	public TreeSet(T value)
	{
		treeSet = new AVLTree<>(value);
	}
	
	/**
	 * Checks to see if the set is empty by calling AVLTree's isEmpty method.
	 * @return True if the set is empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		return treeSet.isEmpty();
	}
	
	/**
	 * Clears the set by calling AVLTree's makeEmpty method.
	 */
	public void makeEmpty()
	{
		treeSet.makeEmpty();
	}
	
	/**
	 * Adds an item to the set. Fails if the item is already in the set.
	 * @param item The item to be added into the set.
	 * @return True if the item was added, false otherwise.
	 */
	public boolean add(T item)
	{
		try
		{
			treeSet.insert(item);
		}
		catch(IllegalArgumentException e)
		{
			System.out.println(e.toString());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Removes the item from the set. Fails if the item is not in the set.
	 * @param item The item to be added into the set.
	 * @return True if the item was removed, false otherwise.
	 */
	public boolean remove(T item)
	{
		try
		{
			treeSet.remove(item);
		}
		catch(IllegalArgumentException e)
		{
			System.out.println(e.toString());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Finds the given item in the tree.
	 * @param item
	 * @return
	 */
	public T find(T item)
	{
		try
		{
			return treeSet.find(item);
		}
		catch(IllegalArgumentException e)
		{
			System.out.println(e.toString());
		}
		
		return null;
		
	}
	
	/**
	 * Finds the smallest item in the set.
	 * @return The smallest item.
	 */
	public T findMin()
	{
		return treeSet.findMin();
	}
	
	/**
	 * Finds the largest item in the set.
	 * @return The largest item.
	 */
	public T findMax()
	{
		return treeSet.findMax();
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
	 * @return The string version of the set.
	 */
	public String toString(boolean val)
	{
		StringBuilder string = new StringBuilder();
		
		//Makes the decision on whether to use breadth-first or in-order traversals.
		//True for breadth-first, false for in-order.
		if(val == true)
			return treeSet.toString();
		else
		{
			if(!isEmpty())
			{
				//Gets the in-order representation of the tree set.
				string = inOrder(treeSet.getRoot(), string);
				
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
	private StringBuilder inOrder(AVLTree.AVLNode<T> node, StringBuilder string)
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
