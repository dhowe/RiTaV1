package rita.wordnet.jwnl.util.cache;

import java.util.HashMap;
import java.util.Map;

/** A set of </code>Caches</code>, indexed by <code>CacheKey</code>. */
public abstract class CacheSet {
	public static final int DEFAULT_CACHE_CAPACITY = 1000;

	private Map _caches = new HashMap();

	public CacheSet(Object[] keys) {
		this(keys, DEFAULT_CACHE_CAPACITY);
	}

	public CacheSet(Object[] keys, int size) {
		for (int i = 0; i < keys.length; i++)
			addCache(keys[i], size);
	}

	public CacheSet(Object[] keys, int[] sizes) {
		for (int i = 0; i < keys.length; i++)
			addCache(keys[i], sizes[i]);
	}

	protected abstract Cache createCache(int size);

	public void addCache(Object key) {
		addCache(key, DEFAULT_CACHE_CAPACITY);
	}

	public void addCache(Object key, int size) {
		_caches.put(key, createCache(size));
	}

	public void cacheObject(Object cacheKey, Object key, Object value) {
		getCache(cacheKey).put(key, value);
	}

	public Object getCachedObject(Object cacheKey, Object key) {
		return getCache(cacheKey).get(key);
	}

	public void clearCache(Object key) {
		getCache(key).clear();
	}

	public int getCacheSize(Object cacheKey) {
		return getCache(cacheKey).getSize();
	}

	public int getCacheCapacity(Object cacheKey) {
		return getCache(cacheKey).getCapacity();
	}

	public int setCacheCapacity(Object cacheKey, int capacity) {
		return getCache(cacheKey).setCapacity(capacity);
	}

	public int getSize() {
		return _caches.size();
	}

	protected Cache getCache(Object cacheKey) {
		return (Cache) _caches.get(cacheKey);
	}
}