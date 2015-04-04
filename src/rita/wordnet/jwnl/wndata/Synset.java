/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

import rita.wordnet.jwnl.JWNL;

/**
 * A <code>Synset</code>, or <b>syn</b>onym <b>set</b>, represents a
 * line of a WordNet <var>pos</var><code>.data</code> file. A <code>Synset</code>
 * represents a concept, and contains a set of <code>Word</code>s, each of
 * which has a sense that names that concept (and each of which is therefore
 * synonymous with the other words in the <code>Synset</code>).
 * <p>
 * <code>Synset</code>'s are linked by {@link Pointer}s into a network of related
 * concepts; this is the <it>Net</it> in WordNet. {@link #getTargets getTargets}
 * retrieves the targets of these links, and {@link #getPointers getPointers}
 * retrieves the pointers themselves.
 */
public class Synset extends PointerTarget implements DictionaryElement {
	static final long serialVersionUID = 4038955719653496529L;

	private POS _pos;
	private Pointer[] _pointers;
	/** The offset of this synset in the data file. */
	private long _offset;
	/** The words in this synset. */
	private Word[] _words;
	/** The text (definition, usage examples) associated with the synset. */
	private String _gloss;
	private BitSet _verbFrameFlags;
	/** for use only with WordNet 1.6 and earlier */
	private boolean _isAdjectiveCluster;

	public Synset(POS pos, long offset, Word[] words, Pointer[] pointers, String gloss, BitSet verbFrames) {
		this(pos, offset, words, pointers, gloss, verbFrames, false);
	}

	public Synset(POS pos, long offset, Word[] words, Pointer[] pointers, String gloss,
	              BitSet verbFrames, boolean isAdjectiveCluster) {
		_pos = pos;
		_pointers = pointers;		
		_offset = offset;
		_words = words;
		_gloss = gloss;
		_verbFrameFlags = verbFrames;
		_isAdjectiveCluster = isAdjectiveCluster;
		//if (_pointers == null) throw new RuntimeException("null pointers! "+toString());
	}

	public DictionaryElementType getType() {
		return DictionaryElementType.SYNSET;
	}

	// Object methods

	/** Two Synsets are equal if their POS's and offsets are equal */
	public boolean equals(Object object) {
		return (object instanceof Synset) && ((Synset) object).getPOS().equals(getPOS()) && ((Synset) object).getOffset() == getOffset();
	}

	public int hashCode() {
		return getPOS().hashCode() ^ (int) getOffset();
	}

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null) {
			StringBuffer words = new StringBuffer();
			for (int i = 0; i < getWordsSize(); ++i) {
				if (i > 0) words.append(", ");
				words.append(getWord(i).getLemma());
			}

			if (getGloss() != null)
				words.append(" -- (" + getGloss() + ")");

			Object[] o = new Object[]{new Long(getOffset()), getPOS(), words.toString()};
			_cachedToString = Arrays.asList(o).toString();
			    JWNL.resolveMessage("DATA_TOSTRING_009", o);
		}
		return _cachedToString;
	}

	// Accessors

	public POS getPOS() {
		return _pos;
	}

	public Pointer[] getPointers() {
		return _pointers;
	}

	public String getGloss() {
		return _gloss;
	}

	public Word[] getWords() {
		return _words;
	}

	public int getWordsSize() {
		return getWords().length;
	}

	public Word getWord(int index) {
		return _words[index];
	}

	public long getOffset() {
		return _offset;
	}

	public Object getKey() {
		return new Long(getOffset());
	}

	public boolean isAdjectiveCluster() {
		return _isAdjectiveCluster;
	}

	/** Returns all Verb Frames that are valid for all the words in this synset */
	public String[] getVerbFrames() {
		return VerbFrame.getFrames(_verbFrameFlags);
	}

	public BitSet getVerbFrameFlags() {
		return _verbFrameFlags;
	}

	public int[] getVerbFrameIndicies() {
		return VerbFrame.getVerbFrameIndicies(_verbFrameFlags);
	}

	/** Returns true if <code>lemma</code> is one of the words contained in this synset.*/
	public boolean containsWord(String lemma) {
		for (int i = 0; i < getWordsSize(); i++) {
			if (getWord(i).getLemma().equals(lemma))
				return true;
		}
		return false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// set POS to reference the static instance defined in the current runtime environment
		_pos = POS.getPOSForKey(_pos.getKey());
	}
}