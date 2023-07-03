package searchTrees;

import java.util.Iterator;
import myCollections.Queue;

/**
 * BST class based on an AVL tree structure.
 * Since AVLs are not used professionally, this will be more of a test to see if I can make a BST.
 * @author Renato Scudere.
 * @param <T extends Comparable<? super T>> Generic type used throughout. It must be either comparable or provide a comparator, or else the tree won't work.
 */
public class AVLTree <T extends Comparable<? super T>> implements Iterable<AVLTree.AVLNode<T>>{
	
	protected static class AVLNode<T> extends BinaryTreeNode<T, AVLNode<T>> {
		/**
		 * Stores the height of the node.
		 */
		public int height;
		
		/**
		 * Constructor calls the BinaryTreeNodeConstructor to set the data and links.
		 * Sets the height of the node to -1.
		 * @param data The data stored in the node.
		 */
		public AVLNode(T data)
		{
			super(data);
			this.height = -1;
		}
	}
	
	/**
	 * The root of the tree.
	 */
	private AVLNode<T> root;
	
	/**
	 * Used to indicate that the program is doing a double rotation.
	 * Prevents the program from doing lg(n) rotations as it recursively traverses to a leaf node.
	 */
	private boolean doubleRot = false;
	
	/**
	 * Constructor that creates an empty tree.
	 */
	public AVLTree()
	{
		root = null;
	}
	
	/**
	 * Constructor that initializes the root of the tree.
	 * @param data The data to be stored in the node.
	 */
	public AVLTree(T data)
	{
		root = new AVLNode<>(data);
	}
	
	/**
	 * Method that checks to see if the tree is empty.
	 * @return True if the tree is empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		return root == null;
	}
	
	/**
	 * Sets the root to null.
	 */
	public void makeEmpty()
	{
		root = null;
	}
	
	/**
	 * Method that passes the root of the tree to the caller.
	 * Used in case other classes want to do their own traversals.
	 * @return The root of the tree.
	 */
	public AVLNode<T> getRoot()
	{
		return root;
	}
	
	/**
	 * Public find method that calls the private method.
	 * @param data The data being looked for.
	 * @return The data if found, else null.
	 * @throws IllegalArgumentException when the data passed is null.
	 */
	public T find(T data)
	{
		if(data == null)
			throw new IllegalArgumentException("The data cannot be null.");
		
		return elementAt(find(data, root));
	}
	
	/**
	 * Public findMin method that calls the private findMin method.
	 * @return The minimum element if found, else null.
	 */
	public T findMin()
	{
		return elementAt(findMin(root));
	}
	
	/**
	 * Public findMax method that calls the private findMax method.
	 * @return The maximum element if found, else null.
	 */
	public T findMax()
	{
		return elementAt(findMax(root));
	}
	
	/**
	 * Public insert method that calls the private insert method.
	 * @param data The data to be inserted into the tree.
	 * @throws IllegalArgumentException if the data passed is null.
	 */
	public void insert(T data)
	{
		if(data == null)
			throw new IllegalArgumentException("The data cannot be null.");
		
		root = insert(data, root);
	}

	/**
	 * Public remove method that calls the private remove method.
	 * @param data The data to be removed from the tree.
	 * @throws IllegalArgumentException if the data passed is null.
	 */
	public void remove(T data)
	{
		if(data == null)
			throw new IllegalArgumentException("The data cannot be null.");
		
		root = remove(data, root);
	}

	/**
	 * Overloaded find method that non-recursively finds a node in the tree.
	 * @param data The data being searched.
	 * @param node The root of the tree.
	 * @return The node if found, else null.
	 */
	private AVLNode<T> find(T data, AVLNode<T> node) 
	{
		//Non-recursively scan for the node in the tree.
		while(node != null)
		{
			//If the data is larger than the data in the node, go to the right subtree.
			if(data.compareTo(node.data) > 0)
				node = node.right;
			//If the data is smaller than the data in the node, go to the left subtree.
			else if(data.compareTo(node.data) < 0)
				node = node.left;
			else
				return node;		//Found
		}
			return null;			//Not found.
	}
	
	/**
	 * Method that finds the minimum element in the tree.
	 * @param node The root of the tree.
	 * @return The node containing the minimum element.
	 */
	private AVLNode<T> findMin(AVLNode<T> node)
	{
		if(node != null)
		{
			//In BSTs, the smallest node in the tree is contained in the leftmost node.
			//To find it, simply follow the left links starting from the root.
			while(node.left != null)
				node = node.left;
		}
		
		return node;
	}
	
	/**
	 * Method that finds the maximum element in the tree.
	 * @param node The root of the tree.
	 * @return The node containing the maximum element.
	 */
	private AVLNode<T> findMax(AVLNode<T> node)
	{
		if(node != null)
		{
			//In BSTs, the largest node in the tree is contained in the rightmost node.
			//To find it, simply follow the right links starting from the root.
			while(node.right != null)
				node = node.right;
		}
		
		return node;
	}
	
	/**
	 * Private insert method that is called by the public insert.
	 * It finds a location for the new node and performs any rotations as we work our way back up the tree.
	 * @param value The value to be added to the tree.
	 * @param node The root of the tree being looked at.
	 * @return The node being inserted, or the new root of the subtree after rotations.
	 * @throws IllegalArgumentException if the value passed is already in the tree.
	 */
	private AVLNode<T> insert(T value, AVLNode<T> node) 
	{
		//Base case.
		if(node == null)
			return new AVLNode<>(value);
		
		//Follows the correct links in order to insert the node correctly.
		//Throws an exception if the value is already in the tree.
		if(value.compareTo(node.data) > 0)
			node.right = insert(value, node.right);
		else if(value.compareTo(node.data) < 0)
			node.left = insert(value, node.left);
		else
			throw new IllegalArgumentException("The value " + value + " is already in the tree.");
		
		//Gets the heights of the left and right subtrees.
		int leftHeight = node.left == null ? -1 : node.left.height;
		int rightHeight = node.right == null ? -1 : node.right.height;
		
		//If the left and right heights differ by more than 1, then the tree is unbalanced and needs to be rotated.
		if(Math.abs(leftHeight - rightHeight) > 1)
		{
			//Handles the case where the imbalance was due to an insertion in the right subtree.
			if(leftHeight < rightHeight)
				node = rotateLeft(node);
			//Handles the case where the imbalance was due to an insertion in the left subtree.
			else if(leftHeight > rightHeight)
				node = rotateRight(node);
		}
		
		//Height update done after rotations so that the node, which was previously a level below, can get the correct height.
		//If no rotations were performed, this executes as normal.
		node.height = updateHeight(node);
		
		return node;
	}
	
	/**
	 * Private remove method called by the public remove.
	 * Finds the node to be removed in the tree, removes it and performs any necessary rotations.
	 * @param data The value to be deleted.
	 * @param node The root of the current subtree.
	 * @return Null if the node deleted was a root, or else the new root of the subtree after deletion and rotations.
	 * @throws IllegalArgumentException if the data is not in the tree.
	 */
	private AVLNode<T> remove(T data, AVLNode<T> node)
	{
		//If the node is null, it means we reached a null link in a leaf. Thus, no node was found.
		if(node == null)
			throw new IllegalArgumentException("The value " + data + " is not in the tree");
		
		//Remove is performed in 2 parts.
		//PART 1: Find the node and delete it.
		//Finds the node in the tree down the left or right subtrees, depending on the value of the data.
		if(data.compareTo(node.data) > 0)
			node.right = remove(data, node.right);
		else if(data.compareTo(node.data) < 0)
			node.left = remove(data, node.left);
		else
		{
			//If the node is found, there are 4 cases to check.
			//Case 1: If the node has no left or right children (a leaf), then return null.
			//Case 2: If the node has no left child, bypass it by returning the right child.
			//Case 3: If the node has no right child, bypass it by returning the left child.
			//Case 4: If the node has two children, find the successor (the leftmost node in the right subtree), copy its data to the deleted node and delete the successor.
			if(node.left == null && node.right == null)
				return null;
			else if(node.left == null)
				return node.right;
			else if(node.right == null)
				return node.left;
			else
			{
				//Gets the data at the successor and sets the node to that data.
				T successor = elementAt(findMin(node.right));
				node.data = successor;
				
				//Removes the successor.
				node.right = remove(successor, node.right);
			}
		}
		
		//PART 2: Perform any necessary rotations at the node. This is identical to the rotations in the insert method.
		//Gets the heights of the left and right subtrees.
		int leftHeight = node.left == null ? -1 : node.left.height;
		int rightHeight = node.right == null ? -1 : node.right.height;
				
		//If the left and right heights differ by more than 1, then the tree is unbalanced and needs to be rotated.
		if(Math.abs(leftHeight - rightHeight) > 1)
		{
			//Handles the case where the imbalance was due to an insertion in the right subtree.
			if(leftHeight < rightHeight)
				node = rotateLeft(node);
			//Handles the case where the imbalance was due to an insertion in the left subtree.
			else if(leftHeight > rightHeight)
				node = rotateRight(node);
		}
				
		//Height update done after rotations so that the node, which was previously a level below, can get the correct height.
		//If no rotations were performed, this executes as normal.
		node.height = updateHeight(node);
		
		return node;
	}
	
	/**
	 * Method that handles left outer rotations in the tree.
	 * @param parent The node being rotated.
	 * @return The new subtree root.
	 */
	private AVLNode<T> rotateLeft(AVLNode<T> parent)
	{
		AVLNode<T> child = parent.right;
		
		//Gets the heights of the left and right subtrees of the child.
		int leftHeight = child.left == null ? -1 : child.left.height;
		int rightHeight = child.right == null ? -1 : child.right.height;
		
		//Handles the case where a double rotation is needed.
		//DoubleRot ensures that if a double rotation is needed, only two rotations are performed.
		//If it wasn't used, the program could enter a recursive loop until it finds a leaf and then proceeds to rotate all those nodes instead of the necessary ones.
		if((leftHeight > rightHeight) && doubleRot == false)
		{
			doubleRot = true;				//Sets doubleRot to true as it goes into the method.
			child = rotateRight(child);
			doubleRot = false;				//Sets doubleRot to false once the double rotation has been performed.
		}
		
		parent.right = child.left;
		child.left = parent;
		
		//Updates the height of the previous parent.
		parent.height = updateHeight(parent);
		
		return child;
	}
	
	/**
	 * Method that handles right outer rotations in the tree.
	 * @param parent The node being rotated.
	 * @return The new subtree root.
	 */
	private AVLNode<T> rotateRight(AVLNode<T> parent)
	{
		AVLNode<T> child = parent.left;
		
		//Gets the heights of the left and right subtrees of the child.
		int leftHeight = child.left == null ? -1 : child.left.height;
		int rightHeight = child.right == null ? -1 : child.right.height;
		
		//Handles the case where a left-right double rotation is needed.
		//DoubleRot ensures that if a double rotation is needed, only two rotations are performed.
		//If it wasn't used, the program could enter a recursive loop until it finds a leaf and then proceeds to rotate all those nodes instead of the necessary ones.
		if((leftHeight < rightHeight) && doubleRot == false)
		{
			doubleRot = true;				//Sets doubleRot to true as it goes into the method.
			child = rotateLeft(child);
			doubleRot = false;				//Sets doubleRot to false once the double rotation has been performed.
		}
		
		parent.left = child.right;
		child.right = parent;
		
		//Updates the height of the previous parent.
		parent.height = updateHeight(parent);
		
		return child;
	}
	
	/**
	 * Private helper method used to get the data from a node.
	 * @param node The node whose data is being looked at.
	 * @return The data if the node is not null, else null.
	 */
	private T elementAt(AVLNode<T> node) 
	{
		return node != null ? node.data: null;
	}
	
	/**
	 * Helper method updates the height at a node.
	 * @param The node whose height is being updated.
	 * @return The new height of the node.
	 */
	private int updateHeight(AVLNode<T> node)
	{
		int height = -1;
		
		//Handles the case where a node becomes a leaf after deletion.
		if(node.left == null && node.right == null)
			return height;
		//Handles the case where the node has no left child.
		else if(node.left == null)
			height = node.right.height + 1;
		//Handles the case where the node has no right child.
		else if(node.right == null)
			height = node.left.height + 1;
		//Handles the case where the node has both children.
		else
		{
			//If the left subtree is larger, then the height at the node is the left subtree height + 1.
			if(node.left.height > node.right.height)
				height = node.left.height + 1;
			//If the right subtree is larger, then the height at the node is the right subtree height + 1.
			else if(node.left.height < node.right.height)
				height = node.right.height + 1;
			//If both are equal, choose either (I chose the left).
			else
			{
				height = node.left.height + 1;
			}
				
		}

		return height;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		StringBuilder string = new StringBuilder("Depth-First Tree Traversal\n");
		
		//Iterates over the tree to convert the values to strings.
		for(AVLNode<T> itr: this)
		{
			//This checks for null links, which simply adds null to the string.
			if(itr == null)
				string.append("null, ");
			//If itr is not a null link, add the data to the string.
			else
				string.append(itr.data + ">>height: " + itr.height + ", ");
		}
		
		//Deletes the final , and space at the end of the string.
		string.delete(string.length() - 2, string.length() - 1);
		
		return string.toString();
	}
	
	/**
	 * Iterator that performs a breadth-first traversal of the tree.
	 * @return The actual iterator as an abstract class.
	 */
	@Override
	public Iterator<AVLNode<T>> iterator() 
	{
		return new Iterator<>() 
		{
			/**
			 * Creates a queue and adds the root to it.
			 */
			Queue<AVLNode<T>> queue = new Queue<>(root);
			
			/**
			 * Reference to the current tree node being looked at.
			 */
			AVLNode<T> current;
			
			/**
			 * Checks to see if the queue is empty.
			 * @returns True if the queue is not empty, false otherwise.
			 */
			@Override
			public boolean hasNext() 
			{
				return !queue.isEmpty();
			}

			/**
			 * Dequeues the node at the head of the queue and enqueues its children.
			 * @return The item previously at the top of the queue.
			 */
			@Override
			public AVLNode<T> next() 
			{
				current = queue.dequeue();
				
				//Enqueues both children only when the current element is not null.
				//Since a child may be null, it will help represent the null links in the traversal.
				if(current != null)
				{
					queue.enqueue(current.left);
					queue.enqueue(current.right);
				}
				else
					return null;
				
				return current;
			}
			
		};
	}
}
