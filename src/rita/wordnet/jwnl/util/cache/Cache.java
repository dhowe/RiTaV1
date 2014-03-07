/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.util.cache;

/**
 * A <code>Cache</code> is a collection of values that are indexed by keys and that are stored for an
 * unspecified amount of time (which the implementor of <code>Cache</code> may further specify).
 */
public interface Cache {
	/**
	 * Store <var>value</var> in the cache, indexed by <var>key</var>.  This operation makes
	 * it likely, although not certain, that a subsquent call to <code>get</code> with the
	 * same (<code>equal</code>) key will retrieve the same (<code>==</code>) value.
	 *
	 * <P>Multiple calls to <code>put</code> with the same <var>key</var> and <var>value</var>
	 * are idempotent.  A set of calls to <code>put</code> with the same <var>key</var> but
	 * different <var>value</var>s has only the affect of the last call (assuming there were
	 * no intervening calls to <code>get</code>).
	 */
	Object put(Object key, Object value);
	
	/**
	 * If <var>key</var> was used in a previous call to <code>put</code>, this call may
	 * return the <var>value</var> of that call.  Otherwise it returns <code>null</code>.
	 */
	Object get(Object key);

	/**
	 * Remotes the object associated with <var>key</var> and returns that
	 * object.
	 */
	Object remove(Object key);

	/**
	 * Returns the maximum number of elements the cache can hold.
	 */
	int getCapacity();

	/**
	 * Set the maximum number of elements the cache can hold.
	 */
	int setCapacity(int capacity);

	/**
	 * Returns the current size of the cache.
	 */
	public int getSize();

	/**
	 * Remove all values stored in this cache.  Subsequent calls to <code>get</code>
	 * will return <code>null</code>.
	 */
	void clear();
}