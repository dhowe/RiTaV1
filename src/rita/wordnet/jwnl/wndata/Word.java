/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.util.ArrayList;
import java.util.List;

import rita.wordnet.jwnl.JWNL;

/**
 * A <code>Word</code> represents the lexical information related to a specific sense of an <code>IndexWord</code>.
 * <code>Word</code>'s are linked by {@link Pointer}s into a network of lexically related words.
 * {@link #getTargets getTargets} retrieves the targets of these links, and
 * {@link Word#getPointers getPointers} retrieves the pointers themselves.
 */
public class Word extends PointerTarget {
	static final long serialVersionUID = 8591237840924027785L;
	/** The Synset to which this word belongs. */
	private Synset _synset;
	/** This word's index within the synset. */
	private int _index;
	/** The string representation of the word. */
	private String _lemma;

	public Word(Synset synset, int index, String lemma) {
		_synset = synset;
		_index = index;
		_lemma = lemma;
	}

	// Object methods

	/** Two words are equal if their parent Synsets are equal and they have the same index */
	public boolean equals(Object object) {
		return (object instanceof Word)
				&& ((Word) object).getSynset().equals(getSynset())
				&& ((Word) object).getIndex() == getIndex();
	}

	public int hashCode() {
		return getSynset().hashCode() ^ getIndex();
	}

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null) {
			Object[] params = new Object[]{getPOS(), getLemma(), getSynset(), new Integer(getIndex())};
			_cachedToString = JWNL.resolveMessage("DATA_TOSTRING_005", params);
		}
		return _cachedToString;
	}

	// Accessors

	public Synset getSynset() {
		return _synset;
	}

	public POS getPOS() {
		return _synset.getPOS();
	}

	public int getIndex() {
		return _index;
	}

	public String getLemma() {
		return _lemma;
	}

	/** returns all the pointers of the synset that contains this word whose source is this word */
	public Pointer[] getPointers() {
		Pointer[] source = getSynset().getPointers();
		List list = new ArrayList(source.length);
		for (int i = 0; i < source.length; ++i) {
			Pointer pointer = source[i];
			if (this.equals(pointer.getSource()))
				list.add(pointer);
		}
		return (Pointer[]) list.toArray(new Pointer[list.size()]);
	}
}