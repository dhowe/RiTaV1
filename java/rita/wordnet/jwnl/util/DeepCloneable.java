/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.util;

/**
 * A <code>DeepCloneable</code> is a cloneable object that can be cloned shallowly (by
 * creating a copy of the object that contains references to the same
 * members as the original) or deeply (by creating a copy of the object
 * and of all it's member objects).
 */
public interface DeepCloneable extends Cloneable {
	/** Create a shallow clone of the object */
	public Object clone() throws CloneNotSupportedException;
	/** Create a deep clone of the object */
	public Object deepClone() throws UnsupportedOperationException;
}
