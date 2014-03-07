/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.io.IOException;
import java.io.Serializable;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.dictionary.Dictionary;

/**
 * A <code>Pointer</code> encodes a lexical or semantic relationship between WordNet entities.  A lexical
 * relationship holds between Words; a semantic relationship holds between Synsets.  Relationships
 * are <it>directional</it>:  the two roles of a relationship are the <it>source</it> and <it>target</it>.
 * Relationships are <it>typed</it>: the type of a relationship is a {@link PointerType}, and can
 * be retrieved via {@link Pointer#getType getType}. */
public class Pointer implements Serializable {
	static final long serialVersionUID = -1275571290466732179L;

	/**
	 * The index of this Pointer within the array of Pointer's in the source Synset.
	 * Used by <code>equal</code>.
	 */
	private int _index;
	private PointerType _pointerType;
	/**
	 * The source of this poiner. If the pointer applies to all words in the
	 * parent synset, then <code>source</code> and <code>synset</code> are the same,
	 * otherwise <code>source</code> is the specific <code>Word</code> object that
	 * this pointer applies to.
	 */
	private PointerTarget _source = null;
	/** An index that can be used to retrieve the target. */
	private TargetIndex _targetIndex;

	/** Cache for the target after it has been resolved. */
	private transient PointerTarget _target = null;

	public Pointer(PointerTarget source, int index, PointerType pointerType,
	               POS targetPOS, long targetOffset, int targetIndex) {
		_source = source;
		_index = index;
		_pointerType = pointerType;
		_targetIndex = new TargetIndex(targetPOS, targetOffset, targetIndex);
	}

	// Object methods //

	public boolean equals(Object object) {
		return (object instanceof Pointer)
		    && ((Pointer) object).getSource().equals(getSource())
		    && ((Pointer) object).getSourceIndex() == getSourceIndex();
	}

	public int hashCode() {
		return getSource().hashCode() ^ getSourceIndex();
	}

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null) {
			String targetMsg = (_target == null) ? _targetIndex.toString() : _target.toString();
			_cachedToString = JWNL.resolveMessage("DATA_TOSTRING_012",
			                                      new Object[]{new Integer(getSourceIndex()), getSource(), targetMsg});
		}
		return _cachedToString;
	}

	// Accessors

	public int getSourceIndex() {
		return _index;
	}

	public PointerType getType() {
		return _pointerType;
	}

	/** True if this pointer's source is a Word */
	public boolean isLexical() {
		return getSource() instanceof Word;
	}

	/** Get the source of this pointer. */
	public PointerTarget getSource() {
		return _source;
	}

	/** Get the actual target of this pointer. */
	public PointerTarget getTarget() throws JWNLException {
		if (_target == null) {
			Dictionary dic = Dictionary.getInstance();
			Synset syn = dic.getSynsetAt(_targetIndex._pos, _targetIndex._offset);
			_target = (_targetIndex._index == 0) ?
			    (PointerTarget) syn : (PointerTarget) syn.getWord(_targetIndex._index - 1);
		}
		return _target;
	}

	/**
	 * Get the synset that is a) the target of this pointer, or b) the	 * synset that contains the target of this pointer.
	 */
	public Synset getTargetSynset() throws JWNLException {
		PointerTarget target = getTarget();
		if (target instanceof Word) {
			return ((Word) target).getSynset();
		} else {
			return (Synset) target;
		}
	}

	/**
	 * Get the offset of the target within the target synset. If the offset is	 * 0, then this pointer applies to all words in the target.
	 */
	public long getTargetOffset() {
		return _targetIndex._offset;
	}

	public int getTargetIndex() {
		return _targetIndex._index;
	}

	public POS getTargetPOS() {
		return _targetIndex._pos;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// set pointer type to reference the static instance defined in the current runtime environment
		_pointerType = PointerType.getPointerTypeForKey(_pointerType.getKey());
	}

	/**
	 * This class is used to avoid paging in the target before it is required, and to prevent
	 * keeping a large portion of the database resident once the target has been queried.
	 */
	private static class TargetIndex implements Serializable {
		POS _pos;
		long _offset;
		int _index;

		TargetIndex(POS pos, long offset, int index) {
			_pos = pos;
			_offset = offset;
			_index = index;
		}

		private transient String _cachedToString = null;

		public String toString() {
			if (_cachedToString == null) {
				_cachedToString =
				    JWNL.resolveMessage("DATA_TOSTRING_013", new Object[]{_pos, new Long(_offset), new Integer(_index)});
			}
			return _cachedToString;
		}

		private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
			in.defaultReadObject();
			// set POS to reference the static instance defined in the current runtime environment
			_pos = POS.getPOSForKey(_pos.getKey());
		}
	}
}