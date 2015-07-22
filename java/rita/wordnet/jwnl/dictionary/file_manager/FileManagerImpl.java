/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary.file_manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rita.RiTa;
import rita.RiWordNet;
import rita.wordnet.RandomAccessByteArray;
import rita.wordnet.RandomAccessIF;
import rita.wordnet.RiZipReader;
import rita.wordnet.WordnetDictionaryFile;
import rita.wordnet.WordnetUtil;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.JWNLRuntimeException;
import rita.wordnet.jwnl.dictionary.file.DictionaryCatalog;
import rita.wordnet.jwnl.dictionary.file.DictionaryCatalogSet;
import rita.wordnet.jwnl.dictionary.file.DictionaryFile;
import rita.wordnet.jwnl.dictionary.file.DictionaryFileType;
import rita.wordnet.jwnl.dictionary.file.RandomAccessDictionaryFile;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.wndata.POS;

/**
 * An implementation of <code>FileManager</code> that reads files from the local file system.
 * <code>FileManagerImpl</code> caches the file position before and after <code>readLineAt</code>
 * in order to eliminate the redundant IO activity that a naive implementation of these methods
 * would necessitate.
 */
public class FileManagerImpl implements FileManager {
    /**
     * File type install parameter. The value should be the
     * name of the appropriate subclass of DictionaryFileType.
     */
	public static final String FILE_TYPE = "file_type";
	/**
	 * Dictionary path install parameter. The value should be the absolute path
	 * of the directory containing the dictionary files.
	 */
	public static final String PATH = "dictionary_path";
    /**
     * Random number generator used by getRandomLineOffset().
     */
    private static final Random _rand = new Random(new Date().getTime());

	private DictionaryCatalogSet _files;

	public FileManagerImpl() {}

	/**
	 * Construct a file manager backed by a set of files contained
	 * in the default WN search directory.
	 */
	public FileManagerImpl(String customWordnetPath, Class dictionaryFileType) throws IOException {
	  
//System.err.println("FileManagerImpl.FileManagerImpl("+customWordnetPath+","+dictionaryFileType+")");
		checkFileType(dictionaryFileType);
    
    // use the default constructor here instead, then populate the set 
    // manually by reading through the zip file...
    if (customWordnetPath == null) {            
      
      RandomAccessIF rab = null;   
      List posList = POS.getAllPOS();
      String archive = RiWordNet.WORDNET_ARCHIVE;
      
      InputStream is = WordnetUtil.getResourceStream(RiWordNet.class, archive); 

//System.err.println("FileManagerImpl.FileManagerImpl("+is+")");
      
      RiZipReader zr = new RiZipReader(archive, is);
      
      this._files = new DictionaryCatalogSet();
      DictionaryFile[] dfiles = new DictionaryFile[posList.size()];
      for (Iterator i = DictionaryFileType.getAllDictionaryFileTypes().iterator(); i.hasNext();)
      {
        int idx=0;
        DictionaryFileType fileType = (DictionaryFileType)i.next();
        try {
          for (Iterator itr = posList.iterator(); itr.hasNext();) {
            POS pos = (POS)itr.next();
            String fname = WordnetDictionaryFile.buildFilename(pos, fileType);

            byte[] buf = zr.getResource(fname);
            //System.err.println("FileManagerImpl.fetching: "+fname+" buf="+buf.length);
            rab = new RandomAccessByteArray(fname, buf);            
            dfiles[idx++] = new WordnetDictionaryFile(rab, pos, fileType);
          } 
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        _files.put(new DictionaryCatalog(fileType, dfiles));
        dfiles = new DictionaryFile[posList.size()];
      }     
      _files.open();
    }
    else {
		  _files = new DictionaryCatalogSet(customWordnetPath, dictionaryFileType);
      _files.open();
    }
	}

	public Object create(Map params) throws JWNLException {
	  
		Class fileClass = null;
		try {
			fileClass = Class.forName(((Param)params.get(FILE_TYPE)).getValue());
		} catch (ClassNotFoundException ex) {
			throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_002", ex);
		}
		checkFileType(fileClass);

		//String path = ((Param)params.get(PATH)).getValue();    

    // add dict path =====================================
    String path = RiWordNet.wordNetHome;    
    if (path != null) {
      if (path.indexOf("dict")<0) {
        path += "dict"+RiTa.SLASH;
      }
      else if (path.endsWith("dict")) { 
        path += RiTa.SLASH;
      }
    }
    
		try 
    {
			return new FileManagerImpl(path, fileClass);
		}
    catch (IOException ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_016", fileClass, ex);
		}
	}

	private void checkFileType(Class c) {
		if (!DictionaryFile.class.isAssignableFrom(c)) {
            throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_003", c);
        }
	}

	public void close() {
		_files.close();
	}

	public DictionaryFile getFile(POS pos, DictionaryFileType fileType) {
		return _files.getDictionaryFile(pos, fileType);
	}

	//
	// IO primitives
	//

	private void skipLine(RandomAccessDictionaryFile file) throws IOException {
		int c;
		while (((c = file.read()) != -1) && c != '\n' && c != '\r');
		c = file.read();
		if (c != '\n' && c != '\r') {
			file.seek(file.getFilePointer()-1);
		}
	}

	//
	// Line-based interface methods
	//
	public String readLineAt(POS pos, DictionaryFileType fileType, long offset) throws IOException {
		RandomAccessDictionaryFile file = (RandomAccessDictionaryFile)getFile(pos, fileType);
		synchronized (file) {
			file.seek(offset);
			String line = file.readLine();
			long nextOffset = file.getFilePointer();
			if (line == null) {
				nextOffset = -1;
			}
			file.setNextLineOffset(offset, nextOffset);
			return line;
		}
	}

	private String readLineWord(RandomAccessDictionaryFile file) throws IOException {
		StringBuffer input = new StringBuffer();
		int c;
		while (((c = file.read()) != -1) && c != '\n' && c != '\r' && c != ' ') {
			input.append((char) c);
		}
		return input.toString();
	}

	public long getNextLinePointer(POS pos, DictionaryFileType fileType, long offset) throws IOException {
		RandomAccessDictionaryFile file = (RandomAccessDictionaryFile)getFile(pos, fileType);
		synchronized (file) {
			if (file.isPreviousLineOffset(offset) && offset != file.getNextLineOffset()) {
                return file.getNextLineOffset();
			}
			file.seek(offset);
			skipLine(file);
			return file.getFilePointer();
		}
	}

	//
	// Searching
	//
	public long getMatchingLinePointer(POS pos, DictionaryFileType fileType, long offset, String substring)
	    throws IOException {

		RandomAccessDictionaryFile file = (RandomAccessDictionaryFile)getFile(pos, fileType);
		if (file == null || file.length() == 0) return -1;

		synchronized (file) {
			file.seek(offset);
			do {
				String line = readLineWord(file);
				long nextOffset = file.getFilePointer();
				if (line == null) return -1;
				file.setNextLineOffset(offset, nextOffset);
				if (line.indexOf(substring) >= 0) return offset;
				offset = nextOffset;
			} while (true);
		}
	}

	public long getIndexedLinePointer(POS pos, DictionaryFileType fileType, String target) throws IOException 
  {
    //System.err.println("FileManagerImpl.getIndexedLinePointer("+pos+","+fileType+",'"+target+"')");
    
		RandomAccessDictionaryFile file = (RandomAccessDictionaryFile)getFile(pos, fileType);;
		if (file == null || file.length() == 0) 
			return -1;

		synchronized (file) {
			long start = 0;
			long stop = file.length();
			long offset = start, midpoint;
			int compare;
			String word;
			while (true) {
				midpoint = (start + stop) / 2;
				file.seek(midpoint);
				file.readLine();
				offset = file.getFilePointer();
        //System.err.println("off="+offset);
				if (stop == offset) {
					file.seek(start);
					offset = file.getFilePointer();
					while (offset != stop) {
						word = readLineWord(file);
						if (word.equals(target)) {
							return offset;
						} else {
							file.readLine();
							offset = file.getFilePointer();
						}
					}
					return -1;
				}        
				word = readLineWord(file);
				compare = word.compareTo(target);
				if (compare == 0) {
					return offset;
				} else if (compare > 0) {
					stop = offset;
				} else {
					start = offset;
				}
			}
		}
	}

  public long getRandomLinePointer(POS pos, DictionaryFileType fileType) throws IOException {
      long fileLength = ((RandomAccessDictionaryFile) getFile(pos, fileType)).length();
      long start = getFirstLinePointer(pos, fileType);
      long offset = start + (long) _rand.nextInt(((int) fileLength) - (int) start);
      return getNextLinePointer(pos, fileType, offset);
  }

  public long getFirstLinePointer(POS pos, DictionaryFileType fileType) throws IOException {
      long offset = 0;
      RandomAccessDictionaryFile file = (RandomAccessDictionaryFile) getFile(pos, fileType);
      String line = null;
      for (line = null; line == null || line.trim().length() == 0; line = readLineWord(file)) {
          offset = getNextLinePointer(pos, fileType, offset);
      }
      return offset;
  }
}