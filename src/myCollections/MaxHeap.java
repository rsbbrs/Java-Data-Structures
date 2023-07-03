package myCollections;

import java.util.Iterator;

/**
 * A generic binary heap class.
 * The tree is implemented as a dynamic array.
 * Children located at 2i+1 and 2i+2
 * @author Renato Scudere
 * @param <T extends Comparable<? super T> The generic parameter used throughout that must be comparable in order to properly add into the heap.
 */
public class MaxHeap<T extends Comparable<? super T>> {
	
	/**
	 * Stores the array-based representation of the tree.
	 */
	private DynamicArray<T> heap;
	
	/**
	 * Constructor initializes the heap with the default value.
	 */
	public MaxHeap()
	{
		this(7);
	}
	
	/**
	 * Overloaded constructor that creates a heap with the specified size.
	 * @param size 
	 */
	public MaxHeap(int size)
	{
		heap = new DynamicArray<>(size);
	}
	
	/**
	 * Checks to see if the heap is empty.
	 * @return True if the heap is empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		return heap.size() == 0;
	}
	
	/**
	 * Creates a new heap with the standard size.
	 */
	public void makeEmpty()
	{
		heap = new DynamicArray<>(7);
	}
	
	/**
	 * Adds an item at the next available location in the array.
	 * Percolates up if needed.
	 * @param x The item to be added.
	 */
	public void add(T x)
	{
		//Makes sure no duplicate items are added to the heap.
		if(heap.contains(x))
		{
			System.out.println("No duplicate elements allowed.");
			return;
		}
		
		//The hole is the location where the element is added, which is at the back of the array.
		int hole = heap.size();
		
		//Appends the element to the back of the array.
		heap.add(x);
		
		//Used to move the element up the tree if its parent is smaller than it.
		percolateUp(hole);
	}
	
	public T removeMax()
	{
		//Gets the item at the root of the tree, which is at array index 0.
		T min = heap.get(0);
		
		//Gets the last item in the array and puts it at the root of the tree (array index 0).
		heap.set(0, heap.get(heap.size() - 1));
		
		//Used to move the item down the tree if its parent is smaller than it.
		percolateDown(0);
		
		//Removes the last element in the array, which was just moved to the top.
		/* This is done afterward so that the array can preserve the size of the tree before the element was removed
		   and thus allows percolate down to go all the way to the edge of the array without losing a spot.
		 */
		heap.remove(heap.size() - 1);
		
		return min;
	}

	/**
	 * Checks the child and parent nodes in the array.
	 * If the child is larger than the parent, they are swapped in the array.
	 * @param hole The index of the child that was inserted.
	 */
	private void percolateUp(int hole) 
	{
		/* Iterates while the hole is greater than 0, meaning it reached the root
		   and while the parent is smaller than the child.
		   This ensures the order of the heap.
		 */
		//This can be changed to use a comparator instead if necessary.
		//I'm not sure how to implement Comparators for dynamic arrays, so I leave it to you to figure that out.
		while(hole > 0 && heap.get(hole).compareTo(heap.get((hole - 1) / 2)) > 0)
		{
			//Stores the parent for later use.
			T parent = heap.get((hole - 1) / 2);
			
			//Places the child at the parent spot.
			heap.set((hole - 1) / 2, heap.get(hole));
			
			//Places the parent in the child's spot.
			heap.set(hole, parent);
			
			//Moves to the parent location where the child is now located.
			hole = (hole - 1) / 2;
		}
	}
	
	/**
	 * Checks the parent and child nodes in the array.
	 * It chooses the largest of its children and if the parent is smaller than the child, swaps them.
	 * @param index The index where the parent is located.
	 */
	private void percolateDown(int index)
	{
		//Iterates while it has children.
		//If the child calculation is larger than the size of the tree, then the node has no children.
		//I still am a little iffy on whether this test will always maintain the method in the bounds of the array.
		while((2 * index) + 2 < heap.size())
		{
			//Gets the left and right children of the parent at index.
			T left = heap.get((2 * index) + 1);
			T right = heap.get((2 * index) + 2);
			
			//If the left child is larger than both the parent and the right child, then swap it with the current parent.
			//These can be changed to use a comparator instead if necessary.
			//I'm not sure how to implement Comparators for dynamic arrays, so I leave it to you to figure that out.
			if(left != null && left.compareTo(right) > 0 && heap.get(index).compareTo(left) < 0)
			{
				//Places the parent at the left child's location.
				heap.set((2 * index) + 1, heap.get(index));
				
				//Places the child where the parent used to be.
				heap.set(index, left);
				
				//Moves the index to the location where the parent now is.
				index = (2 * index) + 1;
			}
			//If the right child is larger than both the parent and the let child, then swap it with the current parent.
			else if(left != null && right.compareTo(left) > 0 && heap.get(index).compareTo(right) < 0)
			{
				//Places the parent at the right child's location.
				heap.set((2 * index) + 2, heap.get(index));
				
				//Places the child where the parent used to be.
				heap.set(index, right);
				
				//Moves the index to the location where the parent is now.
				index = (2 * index) + 2;
			}
			//If both children were smaller, then the parent is in the right spot, so return.
			else
				return;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		StringBuilder string = new StringBuilder("Level Order Min. Heap:\n");
		
		//Adds all the elements in the heap to a string depicting a level order traversal.
		string.append("[ ");
		for(T element: heap)
		{
			string.append(element + " ");
		}
		string.append("]");
		
		return string.toString();
	}
	
	/**
	 * Returns the iterator from the DynamicArray class.
	 * @return The iterator from the dynamic array.
	 */
	public Iterator<T> iterator()
	{
		return heap.iterator();
	}
}
