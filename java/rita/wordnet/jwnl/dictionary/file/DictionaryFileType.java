/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary.file;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.util.Resolvable;
import rita.wordnet.jwnl.wndata.DictionaryElementType;

/** Instances of this class specify the different types of dictionary files (the different classes of dictionary files. */
public class DictionaryFileType {
    // File type constants
	public static final DictionaryFileType INDEX = new DictionaryFileType("INDEX_KEY", DictionaryElementType.INDEX_WORD);
	public static final DictionaryFileType DATA = new DictionaryFileType("DATA_KEY", DictionaryElementType.SYNSET);
	public static final DictionaryFileType EXCEPTION = new DictionaryFileType("EXCEPTION_KEY", DictionaryElementType.EXCEPTION);

	private static final List ALL_TYPES = Collections.unmodifiableList(Arrays.asList(
            new DictionaryFileType[] { INDEX, DATA, EXCEPTION }));

	public static List getAllDictionaryFileTypes() {
		return ALL_TYPES;
	}

	private Resolvable _name;
	private DictionaryElementType _elementType;

	private DictionaryFileType(String type, DictionaryElementType elementType) {
		_name = new Resolvable(type);
		_elementType = elementType;
	}

	public String getName() {
		return _name.toString();
	}

	public DictionaryElementType getElementType() {
		return _elementType;
	}

	public String toString() {
		return JWNL.resolveMessage("DICTIONARY_TOSTRING_002", getName());
	}

	public int hashCode() {
		return getName().hashCode();
	}
}