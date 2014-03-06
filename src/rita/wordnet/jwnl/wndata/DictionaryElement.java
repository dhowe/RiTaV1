/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;

import java.io.Serializable;

/**
 * Any class that represents an element contained in the dictionary (<code>IndexWord</code>s,
 * <code>Synset</code>s, and <code>Exc</code>eptions) must implement this interface.
 */
public interface DictionaryElement extends Serializable {
	/** Get a key that can be used to index this element. */
	public Object getKey();
	/** Get the element's type. */
	public DictionaryElementType getType();
}