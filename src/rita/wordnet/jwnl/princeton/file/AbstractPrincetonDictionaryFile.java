/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.princeton.file;


import java.util.HashMap;
import java.util.Map;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.dictionary.file.AbstractDictionaryFile;
import rita.wordnet.jwnl.dictionary.file.DictionaryFileType;
import rita.wordnet.jwnl.wndata.POS;

/**
 * <code>AbstractDictionaryFile</code> that uses file names compatible with Princeton's distribution of WordNet.
 * The filenames associated are:
 * WINDOWS: <noun, verb, adj, adv>.<idx, dat, exc>
 * MAC, UNIX: <index, data>.<noun, verb, adj, adv>, <noun, verb, adj, adv>.exc
 */
public abstract class AbstractPrincetonDictionaryFile extends AbstractDictionaryFile {
	private static final String NOUN_EXT = "noun";
	private static final String VERB_EXT = "verb";
	private static final String ADJECTIVE_EXT = "adj";
	private static final String ADVERB_EXT = "adv";

	private static final Map _posToExtMap;
    private static final Map _fileTypeToFileNameMap;

	static {
		_posToExtMap = new HashMap(4, 1);
		_posToExtMap.put(POS.NOUN, NOUN_EXT);
		_posToExtMap.put(POS.VERB,  VERB_EXT);
		_posToExtMap.put(POS.ADJECTIVE, ADJECTIVE_EXT);
		_posToExtMap.put(POS.ADVERB, ADVERB_EXT);

		_fileTypeToFileNameMap = new HashMap(3, 1);
		_fileTypeToFileNameMap.put(DictionaryFileType.INDEX, new FileNames("idx", "index"));
		_fileTypeToFileNameMap.put(DictionaryFileType.DATA, new FileNames("dat", "data"));
		_fileTypeToFileNameMap.put(DictionaryFileType.EXCEPTION, new FileNames("exc", "exc"));
	}

	public AbstractPrincetonDictionaryFile() {}

	public AbstractPrincetonDictionaryFile(String path, POS pos, DictionaryFileType fileType) {
		super(path, pos, fileType);
	}

	protected String makeFilename() {
		String posString = getExtension(getPOS());
		if (getFileType() == DictionaryFileType.EXCEPTION || JWNL.getOS() == JWNL.WINDOWS)
			return makeWindowsFilename(posString, getFileNames(getFileType())._windowsFileTypeName);
		else
			return makeNonWindowsFilename(posString, getFileNames(getFileType())._nonWindowsFileTypeName);
	}

	private String makeWindowsFilename(String posStr, String fileTypeStr) {
		return posStr + "." + fileTypeStr;
	}

	private String makeNonWindowsFilename(String posStr, String fileTypeStr) {
		return fileTypeStr + "." + posStr;
	}

	private String getExtension(POS pos) {
		return (String)_posToExtMap.get(pos);
	}

	private FileNames getFileNames(DictionaryFileType type) {
		return (FileNames)_fileTypeToFileNameMap.get(type);
	}

	private static final class FileNames {
		String _windowsFileTypeName;
		String _nonWindowsFileTypeName;

		public FileNames(String windowsFileTypeName, String nonWindowsFileTypeName) {
			_windowsFileTypeName = windowsFileTypeName;
			_nonWindowsFileTypeName = nonWindowsFileTypeName;
		}
	}
}