/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata.list;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.wndata.PointerTarget;
import rita.wordnet.jwnl.wndata.PointerType;
import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.Word;

/** A node in a <code>PointerTargetNodeList</code>. */
public class PointerTargetNode implements Node {
	/** The PointerTarget */
	private PointerTarget _target;
	/**
	 * The relation type that produced this node. e.g. if you make a call to
	 * getDirectHypernyms(), each node in the resultant list will have a
	 * type of PointerType.HYPERNYM.
	 */
	private PointerType _type;

	public PointerTargetNode(PointerTarget target) {
		this(target, null);
	}

	public PointerTargetNode(PointerTarget target, PointerType type) {
		this._target = target;
		this._type = type;
	}

	public void setType(PointerType type) {
		this._type = type;
	}

	public PointerType getType() {
		return _type;
	}

	public PointerTarget getPointerTarget() {
		return _target;
	}

	/** Returns true if the target is a Word, else false. */
	public boolean isLexical() {
		return _target instanceof Word;
	}

	/**
	 * If the target is a synset, return it, otherwise it's a word
	 * so return the word's parent synset.
	 */
	public Synset getSynset() {
		if (isLexical()) {
			return ((Word)_target).getSynset();
		} else {
			return (Synset)_target;
		}
	}

	/** If the target is a word, return it, otherwise return null. */
	public Word getWord() {
		if (isLexical()) {
			return (Word)_target;
		} else {
			return null;
		}
	}

	/** Two PointerTargetNodes are equal if they have the same type and PointerTarget */
	public boolean equals(Object object) {
		if (object instanceof PointerTargetNode) {
			PointerTargetNode node = (PointerTargetNode)object;
			return getPointerTarget().equals(node.getPointerTarget()) && getType() == node.getType();
		}
		return false;
	}

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null) {
			_cachedToString =
				JWNL.resolveMessage("DATA_TOSTRING_014", new Object[] { getPointerTarget(), getType() });
		}
		return _cachedToString;
	}

	public int hashCode() {
		return getPointerTarget().hashCode() ^ getType().hashCode();
	}

	public Object clone() {
		return new PointerTargetNode(getPointerTarget(), getType());
	}

	public Object deepClone() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}