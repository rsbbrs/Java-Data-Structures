package myCollections;
import java.util.Iterator;
//import java.util.Comparator;

/**
 * Generic class that implements a dynamic array data structure.
 * @author Renato Scudere.
 * @param <T> The generic parameter used throughout the class.
 */
public class DynamicArray<T> implements Iterable<T> {
	
	/**
	 * Default capacity for the storage array.
	 */
	private static final int INITCAP = 2; //default initial capacity
	
	/**
	 * Underlying array used as the dynamic array list.
	 */
	private T[] storage; //underlying array, you MUST use this for credit (do not change the name or type)
	
	/**
	 * Array used during expansion and reduction of the dynamic array.
	 */
	private T[] oldArr;
	
	/**
	 * Holds the size of the list in the array.
	 */
	private int size = 0;
	
	/**
	 * Holds the value that was removed from the list through the set and remove methods.
	 */
	private T oldVal;
	
	/**
	 * Default constructor sets the size of the new storage array to INITCAP (2).
	 */
	@SuppressWarnings("unchecked")
	public DynamicArray()
	{
		storage = (T[]) new Object[INITCAP];
	}

	/**
	 * Constructor sets the size of the new storage array to the size specified by the parameter.
	 * @param initCapacity	Receives a value to be used as the capacity of the storage array.
	 * @throws IllegalArgumentException if the capacity is less than 1.
	 */
	@SuppressWarnings("unchecked")
	public DynamicArray(int initCapacity) 
	{

		if(initCapacity < 1)
		{
			throw new IllegalArgumentException("Capacity cannot be zero or negative");
		}
		storage = (T[]) new Object[initCapacity];

	}

	/**
	 * Method that returns the number of elements in the storage array to the caller.
	 * @return	The number of elements in the storage array.
	 */
	public int size() 
	{	
		return size;
	}  
	
	/**
	 * Method that returns the maximum number of elements in the storage array to the caller before expansion.
	 * @return	The length of the array.
	 */
	public int capacity() 
	{
		return storage.length;
	}
	
	/**
	 * Changes the item at the given index to the new value.
	 * @param index	The index to be retrieved.
	 * @param value The value to be inserted at the given index.
	 * @return The old value that was changed.
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the list size.
	 */
	public T set(int index, T value) 
	{
		if(index >= size() || index < 0)
		{
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!");
		}
		
		oldVal = get(index);
		storage[index] = value;
		return oldVal;
	}

	/**
	 * Returns the value at the specified index.
	 * @param index Used to retrieve the value from the storage array.
	 * @return The value at the specified index.
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the list size.
	 */
	public T get(int index) 
	{
		if(index >= size() || index < 0)
		{
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!");
		}
		
		return storage[index];
	}
	
	/**
	 * Checks to see if an item is in the array.
	 * @param item The item being looked for.
	 * @return True if the item is found, false otherwise.
	 */
	public boolean contains(T item)
	{
		for(int i = 0; i < size(); i++)
		{
			if(storage[i].equals(item))
				return true;
		}
		
		return false;
	}

	/**
	 * Appends an element to the end of the list and returns true.
	 * Doubles the size of the array if capacity is reached.
	 * @param value The value to be appended at the end of the list.
	 * @return True once the item has been added to the end of the list.
	 */
	public boolean add(T value) 
	{
		if(capacity() == size())
		{
			expandArr();
		}
		
		storage[size++] = value;

		return true;
	}
	
	/**
	 * Method that adds a value at a given index. Can insert at the beginning, middle or end of the list.
	 * Doubles the size of the array if the capacity is reached.
	 * @param index	The index where the value is to be inserted.
	 * @param value	The value to be inserted.
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the list size.
	 */
	public void add(int index, T value) 
	{
		if(capacity() == size())
		{
			expandArr();
		}
		
		if(index > size() || index < 0)
		{
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!");
		}
		
		//Starting at the end of the list and going up until the index, shift all elements one over in the array.
		for(int i = size() - 1; i >= index; i--)
		{
			storage[i + 1] = storage[i];
		}
		storage[index] = value;
		size++;
	}
	
	/**
	 * Removes the element specified by the index.
	 * Halves the array when the capacity is 1/3 of the original.
	 * @param index	The index where the value is to be removed.
	 * @return	The removed value.
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the list size.
	 */
	public T remove(int index) 
	{
		if(index >= size() || index < 0)
		{
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!");
		}
		
		oldVal = storage[index];
		
		//Shifts the items to the left starting at the index being removed to the end of the list.
		//Loop is completely skipped if the last item is being removed.
		//Doing so leaves the value in the array, but size-- will allow for insertion at that index later on.
		for(int i = index; i < size() - 1; i++)
		{
			storage[i] = storage[i + 1];
		}
	
		size--;
		
		if (size == capacity()/3)
		{
			reduceArr();
		}
		
		return oldVal;
	}
	
	/**
	 * Helper method that will expand the array when needed.
	 */
	@SuppressWarnings("unchecked")
	private void expandArr()
	{
		oldArr = storage;
		storage = (T[]) new Object[capacity()*2];
		
		for(int i = 0; i < size(); i++)
		{
			storage[i] = oldArr[i];
		}
	}
	
	/**
	 * Helper method that will shrink the array when needed.
	 */
	@SuppressWarnings("unchecked")
	private void reduceArr()
	{
		oldArr = storage;
		storage = (T[]) new Object[capacity()/2];
		
		for(int i = 0; i < size(); i++)
		{
			storage[i] = oldArr[i];
		}
	}
	
	/**
	 * Iterator method that is used to iterate through the dynamic array list.
	 * Implements the iterator class definition as an abstract class.
	 * @return The Iterator class itself.
	 */
	public Iterator<T> iterator() 
	{
		
		return new Iterator<>() 
		{
			private int currentVal = 0;
			
			public T next() 
			{
				return storage[currentVal++];
			}
			
			public boolean hasNext() 
			{
				return currentVal < size();
			}
		};
	}
	
	/**
	 * Class used for testing purposes.
	 * Converts the dynamic array structure into a string that can be output.
	 * @return A StringBuilder object used to read the structure's parameters.
	 */
	public String toString() {
		//This method is provided for debugging purposes
		//(use/modify as much as you'd like), it just prints
		//out the list for easy viewing.
		StringBuilder s = new StringBuilder("Dynamic array with " + size()
			+ " items and a capacity of " + capacity() + ":");
		for (int i = 0; i < size(); i++) {
			s.append("\n  ["+i+"]: " + get(i));
		}
		return s.toString();		
	}
}