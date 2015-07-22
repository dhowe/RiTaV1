package rita.wordnet.jwnl.wndata;

import rita.wordnet.jwnl.JWNLException;

/**
 * Proxy for a <code>Synset</code>. This class wraps a <code>Synset</code> that may be null at
 * the time of creation. When this <code>Synset</code> is initialized (set), all
 * method calls are forwarded to it.
 */
public class SynsetProxy extends Synset {
	static final long serialVersionUID = -9020360688433081684L;

	private Synset _source;

	/**
	 * The only value that matters is <var>pos</var>, since the
	 * call to getPOS is not forwarded to <var>_source</var>
	 */
	public SynsetProxy(POS pos) {
		super(pos, 0, null, null, null, null);
	}

	public Pointer[] getPointers(PointerType type) {
		return getSource().getPointers(type);
	}

	public boolean equals(Object object) {
		if (object instanceof SynsetProxy) {
			return getSource().equals(((SynsetProxy)object).getSource());
		} else if (object instanceof Synset) {
			return getSource().equals(object);
		} else {
			return false;
		}
	}

	public PointerTarget[] getTargets() throws JWNLException {
		return getSource().getTargets();
	}

	public int hashCode() {
		return getSource().hashCode();
	}

	public PointerTarget[] getTargets(PointerType type) throws JWNLException {
		return getSource().getTargets(type);
	}

	public String toString() {
		return getSource().toString();
	}

	public Pointer[] getPointers() {
		return getSource().getPointers();
	}

	public String getGloss() {
		return getSource().getGloss();
	}

	public Word[] getWords() {
		return getSource().getWords();
	}

	public Word getWord(int index) {
		return getSource().getWord(index);
	}

	public long getOffset() {
		return getSource().getOffset();
	}

	public Object getKey() {
		return getSource().getKey();
	}

	public String[] getVerbFrames() {
		return getSource().getVerbFrames();
	}

	public boolean containsWord(String lemma) {
		return getSource().containsWord(lemma);
	}

	protected Synset getSource() {
		return _source;
	}

	public void setSource(Synset source) {
		_source = source;
	}
}