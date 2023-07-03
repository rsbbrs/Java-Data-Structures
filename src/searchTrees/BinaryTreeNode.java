package searchTrees;

/**
 * An abstract binary tree class that simply has the most basic binary tree node information.
 * Anything else is added in the class that will be using the nodes.
 * @author Renato Scudere
 * @param <T, N> The generic types used throughout. N is used so that the links can match the data type of the subclass and avoid type conversion issues.
 */
abstract class BinaryTreeNode<T, N>{
	
	/**
	 * Holds the data of the node.
	 */
	T data;
	
	/**
	 * Pointer to the left child.
	 */
	N left;
	
	/**
	 * Pointer to the right child.
	 */
	N right;
	
	/**
	 * Standard constructor that sets no data to the node.
	 */
	public BinaryTreeNode()
	{
		this(null);
	}
	
	/**
	 * Overloaded constructor that sets the data of the node.
	 * @param data
	 */
	public BinaryTreeNode(T data)
	{
		this.data = data;
		left = null;
		right = null;
	}
}