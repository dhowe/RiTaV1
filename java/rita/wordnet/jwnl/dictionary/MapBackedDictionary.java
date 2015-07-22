/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.JWNLRuntimeException;
import rita.wordnet.jwnl.dictionary.file.DictionaryCatalog;
import rita.wordnet.jwnl.dictionary.file.DictionaryCatalogSet;
import rita.wordnet.jwnl.dictionary.file.DictionaryFile;
import rita.wordnet.jwnl.dictionary.file.DictionaryFileType;
import rita.wordnet.jwnl.dictionary.file.ObjectDictionaryFile;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.wndata.Exc;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;


/**
 * A <code>Dictionary</code> backed by <code>Map</code>s. Warning: this has huge memory requirements.
 * Make sure to start the interpreter with a large enough free memory pool to accomodate this.
 */
public class MapBackedDictionary extends Dictionary {
	/**
	 * <code>MorphologicalProcessor</code> class install parameter. The value should be the
	 * class of <code>MorphologicalProcessor</code> to use.
	 */
	public static final String MORPH = "morphological_processor";
	/**
	 * File type install parameter. The value should be * the name of the appropriate subclass
	 * of <code>DictionaryFileType</code>.
	 */
	public static final String FILE_TYPE = "file_type";
	/** The path of the dictionary files */
	public static final String PATH = "dictionary_path";
    /** Random number generator used by getRandomIndexWord() */
    private static final Random _rand = new Random(new Date().getTime());

	private Map _tableMap = new HashMap();

	public MapBackedDictionary() {
	}

	public static void install(String searchDir, Class dictionaryFileType) throws JWNLException {
		install(searchDir, dictionaryFileType, null);
	}

	public static void install(String searchDir, Class dictionaryFileType, MorphologicalProcessor morph) throws JWNLException {
		checkFileType(dictionaryFileType);
		DictionaryCatalogSet files = new DictionaryCatalogSet(searchDir, dictionaryFileType);
		setDictionary(new MapBackedDictionary(files, morph));
		files.close();
	}

	/**
	 * Install a <code>MapBackedDictionary</code> from a map of parameters. The parameters are chosen from the static
	 * variables above.
	 */
	public void install(Map params) throws JWNLException {
		Param param = (Param) params.get(MORPH);
		MorphologicalProcessor morph = (param == null) ? null : (MorphologicalProcessor) param.create();

		param = (Param) params.get(FILE_TYPE);
		Class dictionaryFileType = null;
		try {
			dictionaryFileType = Class.forName(param.getValue());
		} catch (Exception ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_003", param.getValue(), ex);
		}
		checkFileType(dictionaryFileType);

		param = (Param) params.get(PATH);
		String path = param.getValue();

		install(path, dictionaryFileType, morph);
	}

	private static void checkFileType(Class c) {
		if (!ObjectDictionaryFile.class.isAssignableFrom(c)) {
            throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_010", c);
        }
	}

	/** Create a <code>MapBackedDictionary</code> with the specified set of files.*/
	private MapBackedDictionary(DictionaryCatalogSet files, MorphologicalProcessor morph) throws JWNLException {
		super(morph);
		if (!files.isOpen()) {
			try {
				files.open();
			} catch (Exception ex) {
				throw new JWNLException("DICTIONARY_EXCEPTION_019", ex);
			}
		}

		for (Iterator typeItr = DictionaryFileType.getAllDictionaryFileTypes().iterator(); typeItr.hasNext();) {
			DictionaryFileType fileType = (DictionaryFileType)typeItr.next();
			DictionaryCatalog catalog = files.get(fileType);
			for (Iterator posItr = POS.getAllPOS().iterator(); posItr.hasNext();) {
				POS pos = (POS)posItr.next();
				putTable(pos, fileType, loadDictFile(catalog.get(pos)));
			}
		}
	}

	public IndexWord getIndexWord(POS pos, String lemma) {
		return (IndexWord) getTable(pos, DictionaryFileType.INDEX).get(prepareQueryString(lemma));
	}

/*	public Iterator getIndexWordIterator(POS pos, String substring) {
		substring = prepareQueryString(substring);

		final Iterator itr = getIndexWordIterator(pos);
		String temp = null;
		while (itr.hasNext()) {
			IndexWord word = (IndexWord) itr.next();
			String w = word.getLemma();
			if (w.indexOf(substring) != -1) {
				temp = w;
				break;
			}
		}
        return new IndexWordIterator(itr, substring, temp);
	}
*/
	public Iterator getIndexWordIterator(POS pos) {
		return getIterator(getTable(pos, DictionaryFileType.INDEX));
	}

    // this is a very inefficient implementation, but a better
    // one would require a custom Map implementation that allowed
    // access to the underlying Entry array.
    public IndexWord getRandomIndexWord(POS pos) throws JWNLException {
        int index = _rand.nextInt(getTable(pos, DictionaryFileType.INDEX).size());
        Iterator itr = getIndexWordIterator(pos);
        for (int i = 0; i < index && itr.hasNext(); i++) {
            itr.next();
        }
        return (itr.hasNext()) ? (IndexWord) itr.next() : null;
    }

	public Iterator getSynsetIterator(POS pos) {
		return getIterator(getTable(pos, DictionaryFileType.DATA));
	}

	public Iterator getExceptionIterator(POS pos) {
		return getIterator(getTable(pos, DictionaryFileType.EXCEPTION));
	}

	private Iterator getIterator(Map map) {
		return map.values().iterator();
	}

	public Synset getSynsetAt(POS pos, long offset) {
		return (Synset) getTable(pos, DictionaryFileType.DATA).get(new Long(offset));
	}

	public Exc getException(POS pos, String derivation) {
		return (Exc) getTable(pos, DictionaryFileType.EXCEPTION).get(prepareQueryString(derivation));
	}

	public void close() {
		_tableMap = null;
	}

	private Map loadDictFile(DictionaryFile file) throws JWNLException {
		try {
			return (Map) ((ObjectDictionaryFile) file).readObject();
		} catch (Exception ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_020", file.getFile(), ex);
		}
	}

	/**
	 * Use <var>table</var> for lookups to the file represented by <var>pos</var> and
	 * <var>fileType</var>.
	 */
	private void putTable(POS pos, DictionaryFileType fileType, Map table) {
		_tableMap.put(new MapTableKey(pos, fileType), table);
	}

	private Map getTable(POS pos, DictionaryFileType fileType) {
		return (Map) _tableMap.get(new MapTableKey(pos, fileType));
	}

	private static final class MapTableKey {
		private POS _pos;
		private DictionaryFileType _fileType;

		private MapTableKey(POS pos, DictionaryFileType fileType) {
			_pos = pos;
			_fileType = fileType;
		}

		public int hashCode() {
			return _pos.hashCode() ^ _fileType.hashCode();
		}

		public boolean equals(Object obj) {
			if (obj instanceof MapTableKey) {
				MapTableKey k = (MapTableKey) obj;
				return _pos.equals(k._pos) && _fileType.equals(k._fileType);
			}
			return false;
		}
	}

	private static final class IndexWordIterator implements Iterator {
		private Iterator _itr;
		private String _searchString;
		private String _startWord;

		public IndexWordIterator(Iterator itr, String searchString, String startWord) {
			_itr = itr;
			_searchString = searchString;
			_startWord = startWord;
		}

		public boolean hasNext() {
			return (_startWord != null);
		}

		public Object next() {
			if (hasNext()) {
				String thisWord = _startWord;
				_startWord = null;
				while (_itr.hasNext()) {
					IndexWord word = (IndexWord) _itr.next();
					String w = word.getLemma();
					if (w.indexOf(_searchString) != -1) {
						_startWord = w;
						break;
					}
				}
				return thisWord;
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}