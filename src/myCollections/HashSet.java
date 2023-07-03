package myCollections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Set class that uses an underlying hash map to create the set.
 * @author Renato Scudere.
 *
 * @param <T> The generic parameter used throughout.
 */
public class HashSet<T> implements Set<T>{
	
	/**
	 * The underlying set is constructed from a HashMap.
	 */
	private HashMap<T, T> set;
	
	/**
	 * Constructor that calls the single parameter constructor to set the size to 7.
	 */
	public HashSet()
	{
		this(7);
	}
	
	/**
	 * Overloaded constructor that passes a size as an argument.
	 * @param size The starting size of the set.
	 */
	public HashSet(int size)
	{
		set = new HashMap<>(size);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int size()
	{
		return set.size();
	}

	/**
	 * {@inheritDocs}
	 */
	public boolean isEmpty() 
	{
		return size() == 0;
	}
	
	/**
	 * Adds an item to the set by calling the put method in the hash map.
	 * @param item The item to be added.
	 * @return True if the item was added, false otherwise.
	 */
	public boolean add(T item)
	{
		//Makes sure the item was not null.
		if(item == null)
			return false;
		
		//Adds the item to the set by calling the method from the map.
		set.put(item, item);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		
		//Simply removes the item in the map.
		return set.remove(o) != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		
		//Simply gets the item from the map.
		return set.get(o) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		
		//Creates a new map with a size of 7.
		set = new HashMap<>(7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		
		/**
		 * Returns the abstract iterator class.
		 */
		return new Iterator<>()
		{
			//Holds the array representation of the set.
			private Object [] val = toArray();
			
			//The current index being looked at.
			private int index = 0;
			
			/**
			 * Checks to see if the array has a next item.
			 * @return True if it index is not at the last element, false otherwise.
			 */
			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return index != val.length;
			}

			/**
			 * Gets the next item in the array.
			 * @return The element at the index.
			 */
			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				// TODO Auto-generated method stub
				return (T) val[index++];
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object[] toArray() {
		
		//Creates an array of only keys.
		HashMap.Pair<T, T> [] arr = (HashMap.Pair<T, T> []) set.toArray();
		
		//The array that will be returned.
		Object [] ret = new Object[arr.length];
		
		//Gets all the keys from the map toArray and puts them in an array.
		for(int i = 0; i < arr.length; i++)
		{
			ret[i] = arr[i].key;
		}
		
		return ret;
	}
	
	/* The following methods are unsupported.
	 * May change if needed.
	 */

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

}
