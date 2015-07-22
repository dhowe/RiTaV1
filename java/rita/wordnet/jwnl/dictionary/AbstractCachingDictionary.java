/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import rita.wordnet.jwnl.JWNLRuntimeException;
import rita.wordnet.jwnl.util.cache.Cache;
import rita.wordnet.jwnl.util.cache.CacheSet;
import rita.wordnet.jwnl.util.cache.LRUCache;
import rita.wordnet.jwnl.wndata.DictionaryElementType;
import rita.wordnet.jwnl.wndata.Exc;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;

/** Extends <code>Dictionary</code> to provide caching of elements. */
public abstract class AbstractCachingDictionary extends Dictionary {
  
  private static boolean defaultCachingEnabled = true;
  static int count=0;
  
	private DictionaryCacheSet _caches;
	private boolean _isCachingEnabled;

	protected AbstractCachingDictionary() {
		this(defaultCachingEnabled);
	}

	protected AbstractCachingDictionary(boolean enableCaching) {
		setCachingEnabled(enableCaching);
	}

	protected AbstractCachingDictionary(MorphologicalProcessor morph) {
		this(morph, defaultCachingEnabled);
	}

	protected AbstractCachingDictionary(MorphologicalProcessor morph, boolean enableCaching) { // impl
		super(morph);
		setCachingEnabled(enableCaching);
	}
	
	// ============================================================================

	public boolean isCachingEnabled() {
		return _isCachingEnabled;
	}

	public void setCachingEnabled(boolean cachingEnabled) {
	  //System.out.println("AbstractCachingDictionary.setCachingEnabled("+cachingEnabled+")");
		_isCachingEnabled = cachingEnabled;
	}

	public int getCacheSizes(DictionaryElementType type) {
		return getCaches().getCacheSize(type);
	}

	public int getCacheCapacity(DictionaryElementType type) {
		return getCaches().getCacheCapacity(type);
	}

	public void setCacheCapacity(int size) {
		for (Iterator itr = DictionaryElementType.getAllDictionaryElementTypes().iterator(); itr.hasNext();) {
			setCacheCapacity((DictionaryElementType)itr.next(), size);
		}
	}

	public void setCacheCapacity(DictionaryElementType type, int size) {
		getCaches().setCacheCapacity(type, size);
	}

	public void clearCache() {
		for (Iterator itr = DictionaryElementType.getAllDictionaryElementTypes().iterator(); itr.hasNext();) {
			clearCache((DictionaryElementType)itr.next());
		}
	}

	public void clearCache(DictionaryElementType elementType) {
		if (isCachingEnabled()) {
			getCaches().clearCache(elementType);
		}
	}

	protected void cacheIndexWord(POSKey key, IndexWord word) {
		cache(DictionaryElementType.INDEX_WORD, key, word);
	}

	protected IndexWord getCachedIndexWord(POSKey key) {
		return (IndexWord) getCached(DictionaryElementType.INDEX_WORD, key);
	}

	protected void cacheSynset(POSKey key, Synset synset) {
		cache(DictionaryElementType.SYNSET, key, synset);
	}

	protected Synset getCachedSynset(POSKey key) {
		return (Synset) getCached(DictionaryElementType.SYNSET, key);
	}

	protected void cacheException(POSKey key, Exc exception) {
		cache(DictionaryElementType.EXCEPTION, key, exception);
	}

	protected Exc getCachedException(POSKey key) {
		return (Exc) getCached(DictionaryElementType.EXCEPTION, key);
	}

	private DictionaryCacheSet getCaches() {
		if (!isCachingEnabled()) {
			throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_022");
		}
		if (_caches == null) {
			_caches = new DictionaryCacheSet();
		}
		return _caches;
	}

	private void cache(DictionaryElementType fileType, POSKey key, Object obj) {
		if (isCachingEnabled()) {
			getCaches().cacheObject(fileType, key, obj);
		}
	}

	private Object getCached(DictionaryElementType fileType, POSKey key) {
		if (isCachingEnabled()) {
			return getCaches().getCachedObject(fileType, key);
		}
		return null;
	}

	private static final class DictionaryCacheSet extends CacheSet implements Observer {
		private Map _lemmaToOffsetMaps;

		public DictionaryCacheSet() {
			super(DictionaryElementType.getAllDictionaryElementTypes().toArray());
			initLemmaToOffsetMaps();
		}

		public DictionaryCacheSet(int[] sizes) {
			super(DictionaryElementType.getAllDictionaryElementTypes().toArray(), sizes);
			initLemmaToOffsetMaps();
		}

		public Object getCachedObject(DictionaryElementType fileType, Object key) {
			if (((POSKey) key).isLemmaKey())
				key = getMap(fileType).get(key);
			return key == null ? null : super.getCachedObject(fileType, key);
		}

		public void cacheObject(DictionaryElementType fileType, Object key, Object value) {
			if (value instanceof IndexWord) {
				IndexWord word = (IndexWord) value;
				getMap(DictionaryElementType.INDEX_WORD).put(new POSKey(word.getPOS(), word.getLemma()), key);
			} else if (value instanceof Exc) {
				Exc exc = (Exc) value;
				getMap(DictionaryElementType.EXCEPTION).put(new POSKey(exc.getPOS(), exc.getLemma()), key);
			}
			super.cacheObject(fileType, key, value);
		}

		public void clearCache(DictionaryElementType fileType) {
			Map m = getMap(fileType);
			if (m != null) m.clear();
			super.clearCache(fileType);
		}

		public void update(Observable obs, Object obj) {
			if (obj instanceof IndexWord) {
				IndexWord word = (IndexWord) obj;
				removeLemma(DictionaryElementType.INDEX_WORD, word.getLemma(), word.getPOS());
			} else if (obj instanceof Exc) {
				Exc exc = (Exc) obj;
				removeLemma(DictionaryElementType.EXCEPTION, exc.getLemma(), exc.getPOS());
			}
		}

		private void initLemmaToOffsetMaps() {
			_lemmaToOffsetMaps = new HashMap(2);
			_lemmaToOffsetMaps.put(DictionaryElementType.INDEX_WORD,
			                       new HashMap(getCache(DictionaryElementType.INDEX_WORD).getCapacity()));
			_lemmaToOffsetMaps.put(DictionaryElementType.EXCEPTION,
			                       new HashMap(getCache(DictionaryElementType.EXCEPTION).getCapacity()));
		}

		private void removeLemma(DictionaryElementType fileType, String lemma, POS pos) {
			((Map) _lemmaToOffsetMaps.get(fileType)).remove(new POSKey(pos, lemma));
		}

		private Map getMap(DictionaryElementType fileType) {
			return (Map) _lemmaToOffsetMaps.get(fileType);
		}

		protected Cache createCache(int size) {
			ObservableCache cache = new ObservableCache(size);
			cache.addObserver(this);
			return cache;
		}
	}

	private static final class ObservableCache extends LRUCache {
		private Observable _observable = new Observable();

		public ObservableCache(int capacity) {
			super(capacity);
		}

		public void addObserver(Observer obs) {
			_observable.addObserver(obs);
		}

		public Object remove(Object key) {
			Object obj = super.remove(key);
			_observable.notifyObservers(obj);
			return obj;
		}
	}
}