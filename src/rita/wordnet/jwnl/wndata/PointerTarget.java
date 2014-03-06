/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rita.wordnet.jwnl.JWNLException;
/**
 * A <code>PointerTarget</code> is the source or target of a <code>Pointer</code>.
 * The target of a semantic <code>PointerTarget</code> is a <code>Synset</code>;
 * the target of a lexical <code>PointerTarget</code> is a <code>Word</code>.
 */
public abstract class PointerTarget implements Serializable {
	static final long serialVersionUID = 3230195199146939027L;
	private transient PointerTarget[] _pointerTargets;

	protected PointerTarget() {
	}

	/** Return this target's POS */
	public abstract POS getPOS();

	/** Return a list of Target's pointers */
	public abstract Pointer[] getPointers();

	public abstract String toString();

	public boolean equals(Object obj) {
		return (obj instanceof PointerTarget) && ((PointerTarget)obj).getPOS().equals(getPOS());
	}

	/** Get all pointers of type <code>type</code>.*/
	public Pointer[] getPointers(PointerType type) {
		List list = new ArrayList();
		Pointer[] pointers = getPointers();
		for (int i = 0; i < pointers.length; ++i) {
		  PointerType pt = pointers[i].getType();
			if (pt!=null && pt.equals(type)) {
                list.add(pointers[i]);
            }
        }
		return (Pointer[])list.toArray(new Pointer[list.size()]);
	}

	/** Get all the pointer targets of this synset */
	public PointerTarget[] getTargets() throws JWNLException {
		if (_pointerTargets == null)
			_pointerTargets = collectTargets(getPointers());
		return _pointerTargets;
	}

	/** Get all the targets of the pointers of type <code>type</code>.*/
	public PointerTarget[] getTargets(PointerType type) throws JWNLException {
		return collectTargets(getPointers(type));
	}

	/** Get an array of all the targets of <code>pointers</code>.*/
	private PointerTarget[] collectTargets(Pointer[] pointers) throws JWNLException {
		PointerTarget[] targets = new PointerTarget[pointers.length];
		for (int i = 0; i < pointers.length; ++i)
			targets[i] = pointers[i].getTarget();
		return targets;
	}
}