/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.util.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A fixed-capacity <code>Cache</code> that stores the most recently used elements. Once the cache reaches
 * capacity, the least recently used elements will be removed.<p>
 * 
 * DCH: several changes here...
 */
public class LRUCache extends LinkedHashMap implements Cache {
	private int _capacity;

	/**
	 * @param capacity the maximum number of elements that can be contained in the cache.
	 */
	public LRUCache(int capacity) {
    this(capacity, 0.75f, true); // added -dch (to use access rather than insertion order)
	}
  
  // added -dch
  public LRUCache(int capacity, float loadFactor, boolean ordering) {
    super(capacity, loadFactor, ordering);
    setCapacity(capacity);
  }

	public boolean isFull() {
		return size() >= getCapacity();
	}

	protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > getCapacity();
	}

 
  public int setCapacity(int capacity) {
    trimToCapacity(capacity); // added -dch      
    _capacity = capacity;
    return _capacity;
  }
  
  /*
   * removes extra items according to LRU policy
   * when capacity is set less than size()
   */
  private void trimToCapacity(int capacity)
  {
    Iterator it = null;
    while (size()>capacity) {
      if (it == null) it = keySet().iterator();
      if (it.hasNext())  {
        it.next();
        it.remove();
      }
    }
  }

	public int getCapacity() {
		return _capacity;
	}

	public int getSize() {
		return size();
	}
}