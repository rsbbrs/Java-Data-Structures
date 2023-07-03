package myCollections;
import java.util.Iterator;

/**
 * Generic class that is used to implement the linked list.
 * @author Renato Scudere.
 *
 * @param <AnyType> The generic data type to be used.
 */
public class LinkedList<AnyType> implements Iterable<AnyType> {
	
	/**
	 * Class used to create list nodes.
	 * @author Renato Scudere.
	 *
	 * @param <AnyType> Used as the generic type.
	 */
	private static class ListNode<AnyType> {
		
		/**
		 * Holds the data for each node.
		 */
		public AnyType data;
		
		/**
		 * The pointer to the next node.
		 */
		public ListNode<AnyType> next;
		
		/**
		 * Constructor that sets the first element of the list.
		 * @param element The data to be stored in the node.
		 */
		public ListNode(AnyType element)
		{
			this(element, null);
		}
		
		/**
		 * Constructor that creates the next element in the list.
		 * @param element The data to be stored in the node.
		 * @param next The address of the next node in the list.
		 */
		public ListNode(AnyType element, ListNode<AnyType> next)
		{
			data = element;
			this.next = next;
		}
	}
	
	/**
	 * Pointers to the head and tail of the list.
	 */
	private ListNode<AnyType> head, tail;
	
	/**
	 * The size of the list.
	 */
	private int listSize;
	
	/**
	 * Constructor initializes the list.
	 */
	public LinkedList(AnyType data)
	{
		head = tail = new ListNode<>(data, null);
	}
	
	/**
	 * Overloaded constructor used when no arguments are passed.
	 */
	public LinkedList()
	{
		head = tail = null;
	}
	
	/**
	 * Test if the list is empty.
	 * @return True if the list is empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		return head == null;
	}
	
	/**
	 * Makes the list empty.
	 */
	public void makeEmpty()
	{
		head = tail = null;
		listSize = 0;
	}
	
	/**
	 * Method that returns the size of the list.
	 * @return The size of the list.
	 */
	public int size()
	{
		return listSize;
	}
	
	/**
	 * Simply returns the head of the list.
	 * @return The head of the list.
	 */
	public AnyType getHead()
	{
		if(!isEmpty())
			return head.data;
			
		return null;
	}
	
	/**
	 * Method that searches the list for a node based on index.
	 * @param index The index to search for.
	 * @return The value at the index if it has been found, else null.
	 */
	public AnyType get(int index)
	{
		ListNode<AnyType> temp = head;
		
		//Iterates over the entire list until the correct index is found.
		for(int i = 0; i < size(); i++)
		{
			if(i == index)
				return temp.data;
			
			temp = temp.next;
		}
		
		return null;
	}
	
	/**
	 * Inserts a new node at the head of the list. Since the head pointer is kept, the add is O(1).
	 * @param value The value for the new node.
	 */
	public void addHead(AnyType value)
	{
		ListNode<AnyType> newNode = new ListNode<>(value);
		
		if(isEmpty())
			head = tail = newNode;
		else
		{
			newNode.next = head;
			head = newNode;
		}
		
		listSize++;
	}
	
	/**
	 * Appends the node at the tail of the linked list. By keeping the tail, the add is O(1).
	 * @param value The value to be added.
	 */
	public void add(AnyType value)
	{
		if(isEmpty())
			addHead(value);
		else
		{
			ListNode<AnyType> newNode = new ListNode<>(value);
			tail.next = newNode;
			tail = newNode;
			listSize++;
		}			
	}
	
	/**
	 * Overloaded add method that adds a node based on an index in the list.
	 * @param index The index to be inserted at.
	 * @param value The value of the new node.
	 */
	public void add(int index, AnyType value)
	{
		//Makes sure the index is within bounds.
		if(index < 0 || index > size())
			throw new IndexOutOfBoundsException("The index " + index + " is out of bounds.");
		
		//Calls addHead if the index is 0.
		if(index == 0)
			addHead(value);
		else
		{
			//Finds the previous node and inserts the newNode in front of it.
			ListNode<AnyType> newNode = new ListNode<>(value);
			ListNode<AnyType> prev = findPreviousIndex(index);
			
			if(prev != null)
			{
				newNode.next = prev.next;
				prev.next = newNode;
				listSize++;
			}
		}
	}
	
	/**
	 * Removes the first occurrence of the value specified.
	 * @param val The value to be removed.
	 */
	public void remove(AnyType val)
	{
		//Calls removeHead if the item to be removed is the head.
		if(val.equals(head.data))
		{
			removeHead();
		}
		else
		{
			//Finds the previous item in the list and bypasses the current node.
			ListNode<AnyType> prev = findPreviousNode(val);
			if(prev != null)
			{
				prev.next = prev.next.next;
				listSize--;
			}
				
		}
			
	}
	
	/**
	 * Method for removing an item from the list based on its index positioning.
	 * @param index The index used to find the item to be removed.
	 */
	public void removeIndex(int index)
	{
		//Makes sure the index is within bounds.
		if(index < 0 || index > size() - 1)
			throw new IndexOutOfBoundsException("The index " + index + " is out of bounds.");
		
		if(index == 0)
			removeHead();
		else
		{
			//If the previous node is found, then use it to bypass the node being removed.
			ListNode<AnyType> prev = findPreviousIndex(index);
			if(prev != null)
			{
				prev.next = prev.next.next;
				listSize--;
			}
		}
	}
	
	/**
	 * Removes the head of the list.
	 * @return The head object being removed.
	 */
	public AnyType removeHead()
	{
		ListNode<AnyType> temp = head;
		
		if(!isEmpty())
		{
			head = head.next;
			listSize--;
			return temp.data;
		}
		
		return null;
	}
	
	/**
	 * Finds the previous node in the list based on value.
	 * @param val The value of the node whose previous node should be found.
	 * @return The node if found, else null.
	 */
	private ListNode<AnyType> findPreviousNode(AnyType val)
	{
		ListNode<AnyType> temp = head;
		
		while(temp.next != null)
		{
			if(temp.next.data.equals(val))
			{
				return temp;
			}
			
			temp = temp.next;
		}
		
		return null;
	}
	
	/**
	 * Method ethod that finds the previous node based on index.
	 * @param index The index being searched.
	 * @return The node at the index.
	 */
	private ListNode<AnyType> findPreviousIndex(int index)
	{
		ListNode<AnyType> temp = head;
		
		for(int i = 0; i < size(); i++)
		{
			if(i + 1 == index)
				return temp;
			
			temp = temp.next;
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		StringBuilder listString = new StringBuilder();
		int i = 0;
		
		for(AnyType node: this)
		{
			listString.append("[" + i + "]: " + node + "\n");
			i++;
		}
		
		return listString.toString();
	}
	
	/**
	 * Iterator that returns the nodes in the list.
	 * @return The actual iterator as an abstract class.
	 */
	public Iterator<AnyType> iterator()
	{
		return new Iterator<>()
		{
			/**
			 * Initializes current to the head of the list.
			 */
			private ListNode<AnyType> current = head;

			/**
			 * Checks if the list has a next node.
			 */
			@Override
			public boolean hasNext() 
			{
				return current != null;
			}

			/**
			 * Returns the next node in the list.
			 */
			@Override
			public AnyType next()
			{
				ListNode<AnyType> temp = current;
				current = current.next;
				return temp.data;
			}
			
		};
	}
}
