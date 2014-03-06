/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.util.Resolvable;

/**
 * Instances of this class enumerate the possible major syntactic categories, or
 * <b>P</b>art's <b>O</b>f <b>S</b>peech. Each <code>POS</code> has a human-readable
 * label that can be used to print it, and a key by which it can be looked up.
 */
public final class POS implements Serializable {
	static final long serialVersionUID = 4311120391558046419L;

	public static final POS NOUN = new POS("NOUN", "NOUN_KEY");
	public static final POS VERB = new POS("VERB", "VERB_KEY");
	public static final POS ADJECTIVE = new POS("ADJECTIVE", "ADJECTIVE_KEY");
	public static final POS ADVERB = new POS("ADVERB", "ADVERB_KEY");

	private static final List ALL_POS =
	    Collections.unmodifiableList(Arrays.asList(new POS[] {NOUN, VERB, ADJECTIVE, ADVERB}));

	public static List getAllPOS() {
		return ALL_POS;
	}

	/**
	 * Return the <code>POS</code> whose key matches <var>label</var>,
	 * or null if the label does not match any POS.
	 */
	public static POS getPOSForLabel(String label) {
		for (Iterator itr = ALL_POS.iterator(); itr.hasNext();) {
			POS pos = (POS)itr.next();
			if (pos.getLabel().equals(label)) {
				return pos;
			}
		}
		return null;
	}

	/**
	 * Return the <code>POS</code> whose key matches <var>key</var>,
	 * or null if the key does not match any POS.
	 */
	public static POS getPOSForKey(String key) {
		for (Iterator itr = ALL_POS.iterator(); itr.hasNext();) {
			POS pos = (POS)itr.next();
			if (pos.getKey().equals(key)) {
				return pos;
			}
		}
		return null;
	}

	private Resolvable _label;
	private Resolvable _key;

	private POS(String label, String key) {
		_label = new Resolvable(label);
		_key = new Resolvable(key);
	}

	// Object methods

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null) {
			_cachedToString = JWNL.resolveMessage("DATA_TOSTRING_010", getLabel());
		}
		return _cachedToString;
	}

	public int hashCode() {
		return _key.toString().hashCode();
	}

	// Accessors

	/** Return a label intended for textual presentation. */
	public String getLabel() {
		return _label.toString();
	}

	public String getKey() {
		return _key.toString();
	}
}