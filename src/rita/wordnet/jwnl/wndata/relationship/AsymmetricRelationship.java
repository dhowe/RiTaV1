/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata.relationship;

import rita.wordnet.jwnl.wndata.PointerType;
import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.list.PointerTargetNode;
import rita.wordnet.jwnl.wndata.list.PointerTargetNodeList;

/**
 * An asymmetric relationship is one whose source and target synsets have lineages with a definite divergence point.
 * The commonParentIndex is the index of the node in the relationship that represents this divergence point.
 * <p>
 * For example, in finding a hypernym  relationship between dog and cat, the relationship is dog -> canine ->
 * carnivore -> feline -> cat. The ancestry of "dog" and the ancestry of "cat" diverge at "carnivore," so
 * the common parent index is thus 2.
 */
public class AsymmetricRelationship extends Relationship {
	/**
	 * The index of the node in the relationship that represents the point
	 * at which the source and target nodes' ancestries diverge.
	 */
	private int _commonParentIndex;
	private transient int _cachedRelativeTargetDepth = -1;

	public AsymmetricRelationship(
	    PointerType type, PointerTargetNodeList nodes, int commonParentIndex, Synset sourceSynset, Synset targetSynset) {

		super(type, nodes, sourceSynset, targetSynset);
		_commonParentIndex = commonParentIndex;
	}

	public int getCommonParentIndex() {
		return _commonParentIndex;
	}

	/**
	 * Get the depth of the target, from the commonParentIndex, relative to the depth of the source.
	 * If both target and source are eqidistant from the commonParentIndex, this method returns 0;
	 */
	public int getRelativeTargetDepth() {
		if (_cachedRelativeTargetDepth == -1) {
			int distSourceToParent = _commonParentIndex;
			int distParentToTarget = (getNodeList().size() - 1) - _commonParentIndex;
			_cachedRelativeTargetDepth = distParentToTarget - distSourceToParent;
		}
		return _cachedRelativeTargetDepth;
	}

	public Relationship reverse() {
		PointerTargetNodeList list = ((PointerTargetNodeList) getNodeList().deepClone()).reverse();
		int commonParentIndex = (list.size() - 1) - getCommonParentIndex();
		for (int i = 0; i < list.size(); i++) {
			if (i != commonParentIndex) {
				((PointerTargetNode) list.get(i)).setType(getType().getSymmetricType());
			}
		}
		return new AsymmetricRelationship(getType(), list, commonParentIndex, getSourceSynset(), getTargetSynset());
	}
}