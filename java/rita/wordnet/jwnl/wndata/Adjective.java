/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.util.Resolvable;

/**
 * An <code>Adjective</code> is a <code>Word</code> that can have an adjective position.
 * <p>
 * Note: Adjective positions are only supported through WordNet v1.5.
 */
public class Adjective extends Word {
	static final long serialVersionUID = 937870634100403173L;

	public static final AdjectivePosition NONE = new AdjectivePosition("NONE", "NONE_KEY");
	public static final AdjectivePosition PREDICATIVE = new AdjectivePosition("AP_PREDICATIVE", "AP_PREDICATIVE_KEY");
	public static final AdjectivePosition ATTRIBUTIVE = new AdjectivePosition("AP_ATTRIBUTIVE", "AP_ATTRIBUTIVE_KEY");
	public static final AdjectivePosition IMMEDIATE_POSTNOMINAL =
		new AdjectivePosition("AP_IMMEDIATE_POSTNOMIAL", "AP_IMMEDIATE_POSTNOMIAL_KEY");

	public static final AdjectivePosition[] ADJECTIVE_POSITIONS = { NONE, PREDICATIVE, ATTRIBUTIVE, IMMEDIATE_POSTNOMINAL };

	private static final Map KEY_TO_OBJECT_MAP = new HashMap();

	private static boolean _initialized = false;

	public static void initialize() {
		if (!_initialized) {
			for (int i = 0; i < ADJECTIVE_POSITIONS.length; i++)
				KEY_TO_OBJECT_MAP.put(ADJECTIVE_POSITIONS[i].getKey(), ADJECTIVE_POSITIONS[i]);
			_initialized = true;
		}
	}

	public static AdjectivePosition getAdjectivePositionForKey(String key) {
		return (AdjectivePosition) KEY_TO_OBJECT_MAP.get(key);
	}

	private AdjectivePosition _adjectivePosition;

	public Adjective(Synset synset, int index, String lemma, AdjectivePosition adjectivePosition) {
		super(synset, index, lemma);
		_adjectivePosition = adjectivePosition;
	}

	public AdjectivePosition getAdjectivePosition() {
		return _adjectivePosition;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// set adjective position to reference the static instance defined in the current runtime environment
		_adjectivePosition = getAdjectivePositionForKey(_adjectivePosition.getKey());
	}

	/**
	 * Adjective positions denote a restriction on the on the syntactic position the
	 * adjective may have in relation to noun that it modifies. Adjective positions are
	 * only used through WordNet version 1.6.
	 */
	public static final class AdjectivePosition implements Serializable {
		private Resolvable _key;
		private Resolvable _label;

		private AdjectivePosition(String label, String key) {
			_label = new Resolvable(label);
			_key = new Resolvable(key);
		}

		public String getKey() {
			return _key.toString();
		}

		public String getLabel() {
			return _label.toString();
		}

		private transient String _cachedToString = null;

		public String toString() {
			if (_cachedToString == null) {
				_cachedToString = JWNL.resolveMessage("DATA_TOSTRING_006", _label);
			}
			return _cachedToString;
		}
	}
}