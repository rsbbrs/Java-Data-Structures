package myCollections;
import java.util.Map;
import java.util.Set;

import java.util.Collection; //for returning in the values() function only

/**
 * Map class that will be used in the graph.
 * @author Renato Scudere
 *
 * @param <K> The generic type for the key.
 * @param <V> The generic type for the value.
 */
public class HashMap<K,V> implements Map<K,V> {
	
	/**
	 * An array of key/value pairs used in the map.
	 */
	private Pair<K,V>[] storage;
	
	/**
	 * The number of elements in the map.
	 */
	private int numElements = 0;
	
	/**
	 * Class that stores one key/value pairs that will be stored in the map.
	 * @author Renato Scudere.
	 * @param <K> The generic type for the key.
	 * @param <V> The generic type for the value.
	 */
	protected static class Pair<K,V> {
		
		/**
		 * Stores the key in the pair.
		 */
		K key;
		
		/**
		 * Stores the value in the pair.
		 */
		V value;
		
		/**
		 * Constructor that initializes the key/value pair object.
		 * @param key The key to be stored in the pair object.
		 * @param value The value to be stored in the pair object.
		 */
		Pair(K key, V value) { this.key = key; this.value = value; }
		
		/**
		 * {@inheritDoc}
		 */
		public String toString() { return "<" + key + "," + value + ">"; }
	}
	
	/**
	 * Stores the maximum load the table can have before rehashing.
	 */
	private double maxLoad;
	
	/**
	 * Stores the original size of the table.
	 */
	private int tableSize;
	
	/**
	 * Constructor creates a hash table with the smallest power of two larger than the requested size as its initial size.
	 * It uses the default max load value of 0.5.
	 * @param size The requested size for the hash table.
	 */
	@SuppressWarnings("unchecked")
	public HashMap(int size) {
		
		for(int i = 0; tableSize < size; i++)
		{
			tableSize = (int) Math.pow(2, i);
		}
		
		storage = (Pair<K, V>[]) new Pair[tableSize];
		
		//This will use the default maxLoad of 0.5.
		maxLoad = 0.5;
	}
	
	/**
	 * Constructor creates a hash table with the smallest power of two larger than the requested size as its initial size.
	 * It uses the max load value passed to the constructor.
	 * @param size The requested size of the hash table.
	 * @param maxLoad The max load value that the table must inhibit.
	 */
	@SuppressWarnings("unchecked")
	public HashMap(int size, double maxLoad) {
		
		for(int i = 0; tableSize < size; i++)
		{
			tableSize = (int) Math.pow(2, i);
		}
		
		storage = (Pair<K, V>[]) new Pair[tableSize];

		this.maxLoad = maxLoad;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		
		storage = (Pair<K, V>[]) new Pair[tableSize];
		numElements = 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		
		return numElements == 0;
	}
	
	/**
	 * Returns the number of slots in the table.
	 * @return The number of slots in the table.
	 */
	public int capacity() {
		
		return storage.length;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int size() {
		//return the number of elements in the table
		//O(1)
		return numElements;
	}
	
	/**
	 * Method that determines if a value for an element is at the given index.
	 * @param index The index being tested for legitimacy.
	 * @return True if the index contains a value, else returns false.
	 */
	private boolean slotContainsValue(int index) {
		//Private helper method for toString()
		//O(1)
		
		//Returns true if a "real" value is at the index given
		//Tombstones (and bunnies) are not "real" values in the map.
		if(storage[index] != null && storage[index].key != null)
			return true;
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public V get(Object key) {
		//Makes sure the key cannot be null.
		if(key == null)
			return null;
		
		int index = key.hashCode() % storage.length;
		Pair<K, V> pair;
		
		//Converts any negative hash codes to positive.
		if(index < 0)
			index *= -1;
		
		//Checks to see if the item is directly at the index computed by the hash code.
		//If not, it iterates through the Hopgood-Davenport probing system.
		pair = getItem(index, key, storage);
		
		//If the value returned by getNext is null, then the item is not on the table.
		if(pair != null)
			return pair.value;
		else
			return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Set<K> keySet() {
		//a ThreeTenSet is a Set, so return one of those
		//max of O(m) where m = number of slots in the table
		HashSet<K> set = new HashSet<>();

		for(int i = 0; i < storage.length; i++)
		{
			if(storage[i] != null && storage[i].key != null)
			{
				set.add(storage[i].key);
			}
				
		}
		
		if(set.size() == 0)
			return null;
		else
			return set;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public V remove(Object key) {
		//Makes sure the key cannot be null.
		if(key == null)
			return null;
		
		int index = key.hashCode() % storage.length;
		Pair<K, V> pair;
		
		//Converts any negative hash codes to positive.
		if(index < 0)
			index *= -1;
		
		//Checks to see if the item is directly at the index computed by the hash code.
		//If not, it iterates through the Hopgood-Davenport probing system.
		pair = getItem(index, key, storage);
		
		//If the value is found, it is converted to a bunny/tombstone by making the key null.
		if(pair != null)
		{
			pair.key = null;
			numElements--;
			return pair.value;
		}
		else
			return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public V put(K key, V value) {
		if(key == null)
			throw new NullPointerException("The key cannot be null.");
		
		//This is the new pair to be added into the table.
		Pair<K, V> newPair = new Pair<K, V>(key, value);
		
		//Stores the old value in the slot being inserted to.
		V oldVal;
		
		//Holds the hash code that will be used as the index.
		int index = key.hashCode() % storage.length;
		
		//The pair used to hold a pair whose key matches the key passed.
		Pair<K, V> pair;
		
		//Converts any negative hash codes to positive.
		if(index < 0)
			index *= -1;
		
		//Rehashes if the table is full. If not successful, then it returns null and does not add any elements into the table.
		if(numElements == storage.length)
		{
			
			if(!rehash(storage.length * 2))
				return null;
		}
		
		//This call to getItem and subsequent if statement will verify that no two items are inserted twice into the table.
		//getItem will either find the same key or return null. If it returns the same key, the value is updated.
		//If it returns null, then that means that the key does not exist in the table and we can proceed adding without worry of duplicates.
		pair = getItem(index, key, storage);
		if(pair != null)
		{
			oldVal = pair.value;
			pair.value = value;
			return oldVal;
		}
			
		//findNext called to find the next open slot in the table.
		index = findNext(index, storage);
				
		//The new pair is placed at the open slot.
		storage[index] = newPair;
		oldVal = null;
		numElements++;
		
		//Rehashes if after adding, the table's load is greater than max load.
		if((numElements / (float)(storage.length)) > maxLoad)
		{
			if(!rehash(storage.length * 2))
				return null;
		}
		
		return oldVal;
	}
	
	/**
	 * Increases or decreases the size of the map to the smallest power of two larger than the requested size, and moves all elements to their new locations relative to the new size.
	 * @param size The requested size for the hash table.
	 * @return True if rehashing was successful, false otherwise.
	 */
	@SuppressWarnings("unchecked")
	public boolean rehash(int size) {
		
		int newIndex;
		int newSize = 0;
		
		//Calculates the new size of the table as a power of 2.
		for(int i = 0; newSize < size; i++)
		{
			newSize = (int) Math.pow(2, i);
		}
		
		//Returns false if the new size would increase the load.
		if((numElements / (float)(newSize)) > maxLoad)
			return false;
		
		//New array that will be getting the items in old storage.
		Pair<K, V> [] newStorage = (Pair<K, V>[]) new Pair[newSize];
		
		//Iterates from the start of storage to length - 1.
		for(int i = 0; i < storage.length; i++)
		{
			//Checks to see if the item at the index i is either null or a bunny/tombstone.
			//If it is, they are skipped
			if(storage[i] == null || storage[i].key == null)
				continue;
			else
			{
				//Computes a new hash with the new table size.
				newIndex = storage[i].key.hashCode() % newStorage.length;
				
				//Converts any negative hash codes to positive.
				if(newIndex < 0)
					newIndex *= -1;
				
				//Finds the next open slot in the new array.
				newIndex = findNext(newIndex, newStorage);
				newStorage[newIndex] = storage[i];
			}
		}
		
		//Sets storage to the new array so that it can now reference the new table.
		storage = newStorage;
		
		return true;
	}
	
	/**
	 * Helper method used by put and rehash to find the next open slot in the table.
	 * @param index The original index produced by the hash function.
	 * @param arr The array being searched.
	 * @return The index of the next open slot based on the Hopgood-Davenport probing system.
	 */
	private int findNext(int index, Pair<K, V> [] arr)
	{
		int check = index;

		for(int i = 1; arr[check] != null; i++)
		{	
			
			//Checks if the key at a particular index is a bunny/tombstone.
			//If it is, then we break and return this index because bunnies/tombstones can be overwritten.
			if(arr[check].key == null)
				break;
			
			check = (int) (index + (0.5 * i) + (0.5 * Math.pow(i, 2)));
			
			//Wrap-around is implemented by adding the step size to the original index and getting the remainder with the array size.
			if(check == arr.length)
				check = 0;
			else if(check >= arr.length)
				check = check % arr.length;
		}
		
		return check;
	}
	
	/**
	 * Helper method for get, remove and put. It finds the value at the key or returns null if not found.
	 * @param index The starting index to search.
	 * @param key The key being compared.
	 * @param arr The array being iterated through.
	 * @return The pair at the location found based on the Hopgood-Davenport probing system.
	 */
	private Pair<K, V> getItem(int index, Object key, Pair<K, V> [] arr)
	{
		int check = index;
		int prevCheck;
		
		//Iterates through the probing sequence until either the element is found or a null appears.
		//If a null appears after a probing step, then the item is not on the list.
		for(int i = 1; arr[check] != null; i++)
		{
			//Checks if the key matches an item in the list.
			if(key.equals(arr[check].key))
				return arr[check];
			else
			{
				//Stores the previous checked index before computing the next index. Used in the infinite loop test later.
				prevCheck = check;
				check = (int) (index + (0.5 * i) + (0.5 * Math.pow(i, 2)));
			}
				
			
			//Wrap-around is implemented by adding the step size to the original index and getting the remainder with the array size.
			if(check == arr.length)
				check = 0;
			else if(check > arr.length)
				check = check % arr.length;
			
			//Makes sure that the loop will eventually stop once it returns back to the original index or if it has gone to the same index twice in a row.
			//I bet there are better ways to make this stop, and I'm not even sure if this will stop all cases.
			//This will definitely need to be studied further in the future.
			if(check == index || check == prevCheck)
				break;
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		
		return toString(false);
	}
	
	/**
	 * Overloaded toString method that creates the string of the map based on the locations of the items in the map.
	 * @param showEmpty Boolean value used to compare if an item is in the map or not.
	 * @return The newly constructed string.
	 */
	public String toString(boolean showEmpty) {
		
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < storage.length; i++) {
			if(showEmpty || slotContainsValue(i))  {
				s.append("[");
				s.append(i);
				s.append("]: ");
				s.append(storage[i]);
				s.append("\n");
			}
		}
		s.deleteCharAt(s.length()-1);
		return s.toString();
	}

	/**
	 * Converts the map into an array.
	 * @return The array version of the map.
	 */
	@SuppressWarnings("unchecked")
	public Object[] toArray() {
		
		Pair<K,V>[] ret = (Pair<K,V>[]) new Pair[numElements];
		for(int i = 0, j = 0; i < storage.length; i++) {
			if(slotContainsValue(i)) {
				ret[j++] = new Pair<>(storage[i].key, storage[i].value);
			}
		}
		return (Object[]) ret;
	}
	
	//********************************************************************************
	//   YOU MAY, BUT DON'T NEED TO EDIT THINGS IN THIS SECTION
	// These are some methods we didn't write for you, but you could write,
	// if you need/want them for building your graph. We will not test
	// (or grade) these methods.
	//********************************************************************************
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void	putAll(Map<? extends K,? extends V> m) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Set<Map.Entry<K,V>> entrySet() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}
}