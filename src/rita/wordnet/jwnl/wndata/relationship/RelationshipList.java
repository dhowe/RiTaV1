/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata.relationship;


import java.util.ArrayList;

import rita.wordnet.jwnl.util.TypeCheckingList;

/** A list of <code>Relationship</code>s. */
public class RelationshipList extends TypeCheckingList {
	/** The index of the shallowest relationship. */
	private int _shallowestIndex = Integer.MAX_VALUE;
	/** The index of the deepest relationship. */
	private int _deepestIndex = -1;

	public RelationshipList() {
		super(new ArrayList(), Relationship.class);
	}

	public synchronized boolean add(Object o) {
		int curSize = size();
		boolean success = super.add(o);
		if (success) {
			Relationship r = (Relationship)o;
			if (r.getDepth() < _shallowestIndex) {
				_shallowestIndex = curSize;
			}
			if (r.getDepth() > _deepestIndex) {
				_deepestIndex = curSize;
			}
		}
		return success;
	}

	/** Return the shallowest Relationship in the list. */
	public synchronized Relationship getShallowest() {
		if (_shallowestIndex >= 0) {
			return (Relationship)get(_shallowestIndex);
		}
		return null;
	}

	/** Return the deepest Relationship in the list. */
	public synchronized Relationship getDeepest() {
		if (_deepestIndex >= 0) {
			return (Relationship)get(_deepestIndex);
		}
		return null;
	}
}