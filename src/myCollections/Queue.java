package myCollections;
import java.util.Iterator;
/**
 * Class that implements a circular queue.
 * @author Renato Scudere.
 * 
 * @param <T> The generic parameter for the class.
 */

//IMPORTANT: I followed the method implementations found in chapter 16, pgs 633-635 of the textbook.
public class Queue<T> implements Iterable<T>{
	
	/**
	 * The array representation for the circular queue.
	 */
	private T[] queue;
	
	/**
	 * The size of the queue.
	 */
	private int queueSize;
	
	/**
	 * Index representing the front of the queue.
	 */
	private int front;
	
	/**
	 * Index representing the back of the queue.
	 */
	private int back;
	
	/**
	 * Default constructor that creates the queue with the default size.
	 */
	@SuppressWarnings("unchecked")
	public Queue()
	{
		front = 0;
		back = front - 1;
		
		queue = (T[]) new Object[20];
	}
	
	/**
	 * Overloaded constructor used to create the array and indices for the queue using a passed size.
	 */
	@SuppressWarnings("unchecked")
	public Queue(int size)
	{
		front = 0;
		back = front - 1;
		
		queue = (T[]) new Object[size];
	}
	
	/**
	 * Overloaded constructor that adds an item at initialization.
	 * @param item The first item being added to the queue. 
	 */
	@SuppressWarnings("unchecked")
	public Queue(T item)
	{
		front = 0;
		back = front - 1;
		
		queue = (T[]) new Object[20];
		enqueue(item);
	}
	
	/**
	 * Determines if the queue is empty.
	 * @return True if the queue is empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		return queueSize == 0;
	}
	
	/**
	 * Gets the total capacity of the queue.
	 * @return The total capacity of the queue.
	 */
	public int capacity()
	{
		return queue.length;
	}
	
	/**
	 * Gets the size of the queue.
	 * @return The number of elements in the queue.
	 */
	public int size()
	{
		return queueSize;
	}
	
	/**
	 * Adds an item to the back of the queue.
	 * @param item The item to be added to the queue.
	 */
	public void enqueue(T item)
	{
		if(queueSize == queue.length)
			doubleQueue();

		back = increment(back);
		queue[back] = item;
		
		queueSize++;
	}
	
	/**
	 * Removes the front item of the queue.
	 * @return The dequeued item.
	 */
	public T dequeue()
	{
		if(isEmpty())
			throw new RuntimeException();
		
		queueSize--;
		T item = queue[front];
		front = increment(front);
		
		/*
		if(queueSize <= queue.length/2)
			halfQueue();
		*/
		
		return item;
	}
	
	/**
	 * Inspects the item at the front of the queue without removing it.
	 * @return The item that was inspected.
	 */
	public T peek()
	{
		if(isEmpty())
			throw new RuntimeException();
		
		return queue[front];
	}
	
	/**
	 * Increments the index passed as an argument with wrap-around if necessary.
	 * @param index The index, front or back, that is to be incremented.
	 * @return The new value of the index.
	 */
	private int increment(int index)
	{
		if(++index == queue.length)
			index = 0;
		
		return index;
	}
	
	/**
	 * Method used to double the size of the queue when necessary.
	 */
	@SuppressWarnings("unchecked")
	private void doubleQueue()
	{
		T[] newQueue = (T[]) new Object[queue.length * 2];
		
		for(int i = 0; i < queueSize; i++)
		{
			newQueue[i] = queue[front];
			front = increment(front);
		}
		
		queue = newQueue;
		front = 0;
		back = queueSize - 1;
	}
	
	/**
	 * Method used to shrink the queue size.
	private void halfQueue()
	{
		T[] newQueue = (T[]) new Object[queue.length / 2];
		
		for(int i = 0; i < queueSize; i++)
		{
			newQueue[i] = queue[front];
			front = increment(front);
		}
		
		queue = newQueue;
		front = 0;
		back = queueSize - 1;
	}
	*/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		
		return new Iterator<>() 
		{
			private int currentVal = front;
			private int count = 0;
			
			public T next() 
			{
				T next = queue[currentVal];
				count++;
				currentVal = increment(currentVal);
				return next;
			}
			
			public boolean hasNext() 
			{
				return count < queueSize;
			}
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		int index = front;
		StringBuilder s = new StringBuilder();
		for(T itr: this)
		{
			s.append("[" + index + "]: " +  itr + "\n");
			index = increment(index);
		}
		
		return s.toString();
	}
}
