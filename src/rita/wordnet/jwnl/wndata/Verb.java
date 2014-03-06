/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.util.BitSet;

import rita.wordnet.jwnl.JWNL;

/**
 * A <code>Verb</code> is a subclass of <code>Word</code> that can have 1 or more
 * <code>VerbFrame</code>s (use cases of the verb).
 */
public class Verb extends Word {
	static final long serialVersionUID = 1639186403690898842L;
	/**
	 * A bit array of all the verb frames that are valid for this word.
	 *  see {@link VerbFrame} for more explanation.
	 */
	private BitSet _verbFrameFlags;

	public Verb(Synset synset, int index, String lemma, BitSet verbFrameFlags) {
		super(synset, index, lemma);
		_verbFrameFlags = verbFrameFlags;
	}

	public BitSet getVerbFrameFlags() {
		return _verbFrameFlags;
	}

	public int[] getVerbFrameIndicies() {
		return VerbFrame.getVerbFrameIndicies(_verbFrameFlags);
	}

	public String[] getVerbFrames() {
		return VerbFrame.getFrames(getVerbFrameFlags());
	}

	private String getVerbFramesAsString() {
		String[] frames = getVerbFrames();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < frames.length; i++) {
			buf.append(frames[i]);
			if (i != frames.length - 1) {
				buf.append(", ");
			}
		}
		return buf.toString();
	}

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null) {
			_cachedToString = JWNL.resolveMessage("DATA_TOSTRING_008", new Object[] { getPOS(), getLemma(), getSynset(),
																					  new Integer(getIndex()),
																					  getVerbFramesAsString() });
		}
		return _cachedToString;
	}
}