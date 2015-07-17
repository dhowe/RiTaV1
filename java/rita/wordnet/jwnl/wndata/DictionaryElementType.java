package rita.wordnet.jwnl.wndata;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rita.wordnet.jwnl.JWNL;

public class DictionaryElementType {
	public static final DictionaryElementType INDEX_WORD = new DictionaryElementType("INDEX_WORD");
	public static final DictionaryElementType SYNSET = new DictionaryElementType("SYNSET");
	public static final DictionaryElementType EXCEPTION = new DictionaryElementType("EXCEPTION");

	private static final List ALL_TYPES = Collections.unmodifiableList(
            Arrays.asList(new  DictionaryElementType[] { INDEX_WORD, SYNSET, EXCEPTION }));

	public static List getAllDictionaryElementTypes() {
		return ALL_TYPES;
	}

	private final String _name;

	private DictionaryElementType(String name) {
		_name = name;
	}

	public String toString() {
		return JWNL.resolveMessage("DATA_TOSTRING_016", getName());
	}

	public String getName() {
		return _name;
	}

	public int hashCode() {
		return _name.hashCode();
	}
}