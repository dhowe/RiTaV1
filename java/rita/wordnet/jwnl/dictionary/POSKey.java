/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.wndata.POS;

/** A cache key consists of a <code>POS</code> and an object */
public class POSKey {
	private POS _pos;
	private Object _key;

	private POSKey(POS pos, Object key) {
		if (pos == null || key == null) 
		  throw new JWNLPosException("DICTIONARY_EXCEPTION_001");
		_pos = pos;
		_key = key;
	}

	public POSKey(POS pos, String lemma) {
		this(pos, (Object)lemma);
	}

	public POSKey(POS pos, long offset) {
		this(pos, new Long(offset));
	}

	public boolean equals(Object object) {
		return object instanceof POSKey
				&& ((POSKey)object)._pos.equals(_pos)
				&& ((POSKey)object)._key.equals(_key);
	}

	public POS getPOS() {
		return _pos;
	}

	public Object getKey() {
		return _key;
	}

	public boolean isLemmaKey() {
		return _key instanceof String;
	}

	public boolean isOffsetKey() {
		return _key instanceof Long;
	}

	public int hashCode() {
		return _pos.hashCode() ^ _key.hashCode();
	}

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null)
			_cachedToString = JWNL.resolveMessage("DICTIONARY_TOSTRING_001", new Object[] { _pos, _key });
		return _cachedToString;
	}
}