/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary.file;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rita.wordnet.jwnl.wndata.POS;

/**
 * Simple container for <code>DictionaryCatalog</code>s that allows
 * a <code>DictionaryFile</code> to be retrieved by its <code>POS</code>
 * and <code>DictionaryFileType</code>.
 */
public class DictionaryCatalogSet {
	public Map _catalogs = new HashMap();

  public DictionaryCatalogSet() {
    // TODO Auto-generated constructor stub
  }

	/** Creates a catalog set of the specified type of file using files in the specified dictionary directory. */
	public DictionaryCatalogSet(String path, Class dictionaryFileType) {
		path = path.trim();
		for (Iterator itr = DictionaryFileType.getAllDictionaryFileTypes().iterator(); itr.hasNext();) {
      DictionaryFileType dft = (DictionaryFileType)itr.next();
      // could grap zip file here and insert each entry into catalog map???
			DictionaryCatalog cat = new DictionaryCatalog(path, dft, dictionaryFileType);
			_catalogs.put(cat.getKey(), cat);
      //System.err.println("DictionaryCatalogSet.put("+cat.getKey()+","+cat._files+")");
		}
	}

  public void open() throws IOException {
		if (!isOpen()) {
			for (Iterator itr = getCatalogIterator(); itr.hasNext();) {
			  DictionaryCatalog dc = (DictionaryCatalog)itr.next();	
				dc.open();
			}
		}
	}

	public boolean isOpen() {
		for (Iterator itr = getCatalogIterator(); itr.hasNext();)
			if (!((DictionaryCatalog)itr.next()).isOpen())
				return false;
		return true;
	}

	public void close() {
		for (Iterator itr = getCatalogIterator(); itr.hasNext();)
			((DictionaryCatalog)itr.next()).close();
	}

	public DictionaryCatalog get(DictionaryFileType fileType) {
		return (DictionaryCatalog)_catalogs.get(fileType);
	}

	public int size() {
		return _catalogs.size();
	}
  
  public void put(DictionaryCatalog dc) {  // added -dch
    _catalogs.put(dc.getKey(), dc);
  }

	public Iterator getCatalogIterator() {
		return _catalogs.values().iterator();
	}

	public DictionaryFile getDictionaryFile(POS pos, DictionaryFileType fileType) {
		return get(fileType).get(pos);
	}
}