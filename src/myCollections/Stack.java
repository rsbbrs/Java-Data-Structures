package myCollections;
import java.util.Iterator;

/**
 * Stack class that works as a LIFO structure.
 * This means that the last item added is the first to be removed.
 * The underlying data structure used is a linked list.
 * The stack pushes and pops items at the head of the linked list.
 * By using a linked list and pushing/popping from the head, all operation save the iterator and toString are O(1).
 * Because this stack uses the linked list. All methods are essentially one liners.
 * @author Renato Scudere.
 * @param <T> The generic parameter used throughout.
 */
public class Stack<T> implements Iterable<T> {
	
	/**
	 * The linked list object used to maintain the stack
	 */
	LinkedList<T> stack;
	
	/**
	 * Constructor that initializes the stack with the first item.
	 * @param data The data stored at the head.
	 */
	public Stack(T data)
	{
		stack = new LinkedList<>(data);
	}
	
	/**
	 * Overloaded constructor with no parameters.
	 */
	public Stack()
	{
		stack = new LinkedList<>();
	}
	
	/**
	 * Checks if the list is empty.
	 * @return True if the list is empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		return stack.isEmpty();
	}
	
	/**
	 * Makes the list empty by calling the linked list's makeEmpty method.
	 */
	public void makeEmpty()
	{
		stack.makeEmpty();
	}
	
	/**
	 * Gets the size of the stack.
	 * @return The size of the stack.
	 */
	public int size()
	{
		return stack.size();
	}
	
	/**
	 * Inserts the item at the head of the list.
	 * @param item The item being added.
	 */
	public void push(T item)
	{
		stack.addHead(item);
	}
	
	/**
	 * Removes the item at the top of the stack.
	 * @return The item removed from the top of the stack.
	 */
	public T pop()
	{
		return stack.removeHead();
	}
	
	/**
	 * Looks at the top of the stack without removing it.
	 * @return The item at the top of the stack.
	 */
	public T peek()
	{
		return stack.getHead();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		
		return stack.iterator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return stack.toString();
	}
}
