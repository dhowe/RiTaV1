/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.io.IOException;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.dictionary.Dictionary;

/**
 * An <code>IndexWord</code> represents a line of the <var>pos</var><code>.index</code> file.
 * An <code>IndexWord</code> is created or retrieved via {@link Dictionary#lookupIndexWord lookupIndexWord}.
 */
public class IndexWord implements DictionaryElement {
	static final long serialVersionUID = -2136983562978852712L;
	/** This word's part-of-speech */
	private POS _pos;
	/** The string representation of this IndexWord */
	private String _lemma;
	/** senses are initially stored as offsets, and paged in on demand.*/
	private long[] _synsetOffsets;
	/** This is null until getSenses has been called. */
	private transient Synset[] _synsets;
	/** True when all synsets have been loaded */
	private transient boolean _synsetsLoaded = false;

	public IndexWord(String lemma, POS pos, long[] synsetOffsets) {
		_lemma = lemma;
		_pos = pos;
		_synsetOffsets = synsetOffsets;
		_synsets = new Synset[synsetOffsets.length];
	}

	public DictionaryElementType getType() {
		return DictionaryElementType.INDEX_WORD;
	}

	// Object methods	//

	public boolean equals(Object object) {
		return (object instanceof IndexWord)
		    && ((IndexWord) object).getLemma().equals(getLemma()) && ((IndexWord) object).getPOS().equals(getPOS());
	}

	public int hashCode() {
		return getLemma().hashCode() ^ getPOS().hashCode();
	}

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null) {
			_cachedToString = JWNL.resolveMessage("DATA_TOSTRING_002", new Object[]{getLemma(), getPOS()});
		}
		return _cachedToString;
	}

	// Accessors	//

	/** Get the word's part-of-speech. */
	public POS getPOS() {
		return _pos;
	}

	/**
	 * Return the word's <it>lemma</it>.  Its lemma is its orthographic representation, for
	 * example <code>"dog"</code> or <code>"get up"</code>.
	 */
	public String getLemma() {
		return _lemma;
	}

	public long[] getSynsetOffsets() {
		return _synsetOffsets;
	}

	public Object getKey() {
		return getLemma();
	}

	/** Get the word's sense count. */
	public int getSenseCount() {
		return _synsetOffsets.length;
	}

	/** Get an array of all the senses of this word.*/
	public Synset[] getSenses() throws JWNLException {
		if (!_synsetsLoaded) {
			for (int i = 0; i < getSynsetOffsets().length; ++i)
				loadSynset(i);
			_synsetsLoaded = true;
		}
		return _synsets;
	}

	/** Get a particular sense of this word. Sense indices start at 1. */
	public Synset getSense(int index) throws JWNLException {
		loadSynset(index - 1);
		return _synsets[index - 1];
	}

	private void loadSynset(int i) throws JWNLException {
		if (_synsets[i] == null) {
            _synsets[i] = Dictionary.getInstance().getSynsetAt(_pos, _synsetOffsets[i]);
        }
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// set POS to reference the static instance defined in the current runtime environment
		_pos = POS.getPOSForKey(_pos.getKey());
		_synsets = new Synset[_synsetOffsets.length];
	}
}