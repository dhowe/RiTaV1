package rita.wordnet.jwnl.util.cache;

public class LRUCacheSet extends CacheSet {
	public LRUCacheSet(Object[] keys) {
		super(keys);
	}

	public LRUCacheSet(Object[] keys, int size) {
		super(keys, size);
	}

	public LRUCacheSet(Object[] keys, int[] sizes) {
		super(keys, sizes);
	}

	protected Cache createCache(int size) {
		return new LRUCache(size);
	}
}