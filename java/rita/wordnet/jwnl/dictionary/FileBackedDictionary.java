/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import rita.RiTa;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.dictionary.file.DictionaryFileType;
import rita.wordnet.jwnl.dictionary.file_manager.FileManager;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.wndata.DictionaryElementType;
import rita.wordnet.jwnl.wndata.Exc;
import rita.wordnet.jwnl.wndata.FileDictionaryElementFactory;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;


/**
 * A <code>Dictionary</code> that retrieves objects from the text files in the
 * WordNet distribution directory.
 */
public class FileBackedDictionary extends AbstractCachingDictionary
{
  /**
   * Morphological processor class install parameter. The value should be the
   * class of MorphologicalProcessor to use.
   */
  public static final String MORPH = "morphological_processor";

  /**
   * File manager install parameter. The value should be the class of
   * FileManager to use.
   */
  public static final String FILE_MANAGER = "file_manager";

  /** The class of FileDictionaryElementFactory to use. */
  public static final String DICTIONARY_ELEMENT_FACTORY = "dictionary_element_factory";

  /** The value should be "true" or "false". The default is "true". */
  public static final String ENABLE_CACHING = "enable_caching";

  /** The default cache size. */
  public static final String CACHE_SIZE = "cache_size";

  /** Size of the index word cache. Overrides the default cache size */
  public static final String INDEX_WORD_CACHE_SIZE = "index_word_cache_size";

  /** Size of the synset cache. Overrides the default cache size */
  public static final String SYNSET_WORD_CACHE_SIZE = "synset_word_cache_size";

  /** Size of the exception cache. Overrides the default cache size */
  public static final String EXCEPTION_WORD_CACHE_SIZE = "exception_word_cache_size";

  /**
   * Construct a Dictionary that retrieves file data from
   * <code>fileManager</code>. A client can use this to create a Dictionary
   * backed by a RemoteFileManager.
   * 
   * @see rita.wordnet.jwnl.dictionary.file_manager.RemoteFileManager

  public static void install(FileManager fileManager,
      FileDictionaryElementFactory factory)
  {
    install(fileManager, null, factory);
  } 

  /**
   * Construct a Dictionary that retrieves file data from
   * <code>fileManager</code>. If enableCaching is true, lookup operations
   * will check the relavant cache before doing a lookup and will cache their
   * results after doing a lookup.

  public static void install(FileManager fileManager,
      MorphologicalProcessor morph, FileDictionaryElementFactory factory)
  {
    install(fileManager, morph, factory, true);
  } 

  public static void install(FileManager fileManager,
      FileDictionaryElementFactory factory, boolean enableCaching)
  {
    install(fileManager, null, factory, enableCaching);
  }  */

  private static void install(FileManager fileManager,
      MorphologicalProcessor morph, FileDictionaryElementFactory factory,
      boolean enableCaching)
  {
    setDictionary(new FileBackedDictionary(fileManager, morph, factory, enableCaching));
  }

  private FileManager _db = null;

  private FileDictionaryElementFactory _factory = null;

  public FileBackedDictionary() {
  }

  private FileBackedDictionary(FileManager manager,
      MorphologicalProcessor morph, FileDictionaryElementFactory factory,
      boolean enableCaching) 
  {
    super(morph, enableCaching);
    _db = manager;
    _factory = factory;
  }

  public static void installStatic(Map params) throws JWNLException
  {
    Param param = (Param) params.get(MORPH);
    MorphologicalProcessor morph = (param == null) ? null
        : (MorphologicalProcessor) param.create();
    
    Param p = (Param) params.get(FILE_MANAGER);
    FileManager manager = (FileManager) p.create();
    
    // caching is enabled by default
    FileDictionaryElementFactory factory = (FileDictionaryElementFactory) ((Param) params
        .get(DICTIONARY_ELEMENT_FACTORY)).create();
    
    boolean enableCaching = !params.containsKey(ENABLE_CACHING)
        || !((Param) params.get(ENABLE_CACHING)).getValue().equalsIgnoreCase("false");
    
    install(manager, morph, factory, enableCaching);

    FileBackedDictionary dictionary = (FileBackedDictionary) getInstance();
    if (params.containsKey(CACHE_SIZE))
    {
      dictionary.setCacheCapacity(Integer.parseInt(((Param) params
          .get(CACHE_SIZE)).getValue()));
    } else
    {
      if (params.containsKey(INDEX_WORD_CACHE_SIZE))
      {
        dictionary.setCacheCapacity(DictionaryElementType.INDEX_WORD, Integer
            .parseInt(((Param) params.get(INDEX_WORD_CACHE_SIZE)).getValue()));
      }
      if (params.containsKey(SYNSET_WORD_CACHE_SIZE))
      {
        dictionary.setCacheCapacity(DictionaryElementType.SYNSET, Integer
            .parseInt(((Param) params.get(SYNSET_WORD_CACHE_SIZE)).getValue()));
      }
      if (params.containsKey(EXCEPTION_WORD_CACHE_SIZE))
      {
        dictionary.setCacheCapacity(DictionaryElementType.EXCEPTION, Integer
            .parseInt(((Param) params.get(EXCEPTION_WORD_CACHE_SIZE))
                .getValue()));
      }
    }
  }
    
  /**
   * Install a FileBackedDictionary from a map of parameters. The keys are chose
   * from the static variables above.
   * 
   * DCH: Changed -- now calls installStatic directly to avoid double-construction
   */
  public void install(Map params) throws JWNLException
  {
    installStatic(params);
  }

  public void close()
  {
    _db.close();
  }

  /** Get the file manager that backs this database. */
  protected FileManager getFileManager()
  {
    return _db;
  }

  public FileDictionaryElementFactory getDictionaryElementFactory()
  {
    return _factory;
  }

  //
  // IndexWord methods
  //

  public Iterator getIndexWordIterator(final POS pos) throws JWNLException
  {

    return new IndexFileLookaheadIterator(pos);
  }

/*  public Iterator getIndexWordIterator(final POS pos, final String substring)
      throws JWNLException
  {
    return new SubstringIndexFileLookaheadIterator(pos,
        prepareQueryString(substring));
  }*/

  public IndexWord getIndexWord(POS pos, String lemma) throws JWNLException
  {
    //lemma = prepareQueryString(lemma);
    
    // System.err.println("FileBackedDictionary.getIndexWord('"+lemma+"');");

    IndexWord word = null;
    if (lemma.length() > 0)
    {
      if (isCachingEnabled())
      {
        // System.err.println("cached.getIndexWord('"+lemma+"');");
        word = getCachedIndexWord(new POSKey(pos, lemma));
      }
      
      if (word == null)
      {
        try
        {

          FileManager fm = getFileManager();
          
          lemma = lemma.replace(RiTa.SP, RiTa.USC);
          
          long offset = fm.getIndexedLinePointer(pos, DictionaryFileType.INDEX, lemma);
          // System.err.println("offset = "+offset);
          if (offset >= 0)
          {
            word = parseAndCacheIndexWordLine(pos, offset, getFileManager()
                .readLineAt(pos, DictionaryFileType.INDEX, offset));
          }
        } catch (IOException e)
        {
          throw new JWNLException("DICTIONARY_EXCEPTION_004", lemma, e);
        }
      }
    }
    return word;
  }

  public IndexWord getRandomIndexWord(POS pos) throws JWNLException
  {
    if (pos == null) return null;
    try
    {
      long offset = getFileManager().getRandomLinePointer(pos, DictionaryFileType.INDEX);
      
      return parseAndCacheIndexWordLine(pos, offset, getFileManager()
          .readLineAt(pos, DictionaryFileType.INDEX, offset));
      
    } catch (IOException ex)
    {
      throw new JWNLException("DICTIONARY_EXCEPTION_004", ex);
    }
  }

  private IndexWord parseAndCacheIndexWordLine(POS pos, long offset, String line)
  {
    IndexWord word = _factory.createIndexWord(pos, line);
    if (isCachingEnabled() && word != null)
      cacheIndexWord(new POSKey(pos, offset), word);
    return word;
  }

  //
  // Synset methods
  //

  public Iterator getSynsetIterator(POS pos)
  {
    return new FileLookaheadIterator(pos, DictionaryFileType.DATA) {
      protected Object parseLine(POS pos, long offset, String line)
      {
        try
        {
          return getSynset(pos, offset, line);
        } catch (JWNLException ex)
        {
          throw new RuntimeException(ex);
        }
      }
    };
  }

  public Synset getSynsetAt(POS pos, long offset) throws JWNLException
  {
    return getSynset(pos, offset, null);
  }

  private Synset getSynset(POS pos, long offset, String line)
      throws JWNLException
  {
    POSKey key = new POSKey(pos, offset);
    Synset synset = getCachedSynset(key);
    if (synset == null)
    {
      try
      {
        if (line == null)
        {
          line = getFileManager().readLineAt(pos, DictionaryFileType.DATA,
              offset);
        }
        synset = _factory.createSynset(pos, line);
        if (synset != null)
        {
          cacheSynset(key, synset);
        }
      } catch (IOException e)
      {
        throw new JWNLException("DICTIONARY_EXCEPTION_005", new Long(offset), e);
      }
    }
    return synset;
  }

  //
  // Exception methods
  //

  public Iterator getExceptionIterator(POS pos)
  {
    return new FileLookaheadIterator(pos, DictionaryFileType.EXCEPTION) {
      protected Object parseLine(POS pos, long offset, String line)
      {
        Exc exc = null;
        if (isCachingEnabled())
        {
          exc = getCachedException(new POSKey(pos, offset));
        }
        if (exc == null)
        {
          exc = parseAndCacheExceptionLine(pos, offset, line);
        }
        return exc;
      }
    };
  }

  public Exc getException(POS pos, String derivation) throws JWNLException
  {
    derivation = prepareQueryString(derivation);

    Exc exc = null;
    POSKey key = null;
    if (derivation != null)
    {
      if (isCachingEnabled())
      {
        key = new POSKey(pos, derivation);
        exc = getCachedException(key);
      }
      if (exc == null)
      {
        long offset = -1;
        try
        {
          offset = getFileManager().getIndexedLinePointer(pos,
              DictionaryFileType.EXCEPTION, derivation);//.replace(' ', '_'));
          if (offset >= 0)
          {
            exc = parseAndCacheExceptionLine(pos, offset, getFileManager()
                .readLineAt(pos, DictionaryFileType.EXCEPTION, offset));
          }
        } catch (IOException ex)
        {
          throw new JWNLException("DICTIONARY_EXCEPTION_006", ex);
        }
      }
    }
    return exc;
  }

  private Exc parseAndCacheExceptionLine(POS pos, long offset, String line)
  {
    Exc exc = _factory.createExc(pos, line);
    if (isCachingEnabled() && exc != null)
      cacheException(new POSKey(pos, offset), exc);
    return exc;
  }

  /**
   * A lookahead iterator over a dictionary file. Each element in the
   * enumeration is a line in the enumerated file.
   */
  private abstract class FileLookaheadIterator implements Iterator
  {
    protected POS _pos;
    protected DictionaryFileType _fileType;
    private String _currentLine = null;
    private long _currentOffset = -1;
    private long _nextOffset = 0;
    private boolean _more = true;

    public FileLookaheadIterator(POS pos, DictionaryFileType fileType) {
      _pos = pos;
      _fileType = fileType;
      try
      {
        _nextOffset = _db.getFirstLinePointer(pos, fileType);
        nextLine();
      } 
      catch (IOException ex)
      {
        System.err.println(ex.getMessage());
      }
    }

    protected abstract Object parseLine(POS pos, long offset, String line);

    public final Object next()
    {
      if (hasNext())
      {
        Object returnVal = parseLine(_pos, _currentOffset, _currentLine);
        nextLine();
        return returnVal;
      } else
        throw new NoSuchElementException();
    }

    public final boolean hasNext()
    {
      return _more;
    }

    /**
     * This method can be over-ridden to remove the currently pointed-at object
     * from the data source backing the iterator.
     */
    public void remove()
    {
    }

    /** Read the next line in the iterated file. */
    protected final void nextLine()
    {
      try
      {
        _currentLine = _db.readLineAt(_pos, _fileType, _nextOffset);
        if (_currentLine != null)
        {
          nextOffset();
          return;
        }
      } catch (Exception ex)
      {
      }
      _more = false;
    }

    protected final void nextOffset() throws JWNLException
    {
      _currentOffset = _nextOffset;
      _nextOffset = getNextOffset(_currentOffset);
    }

    protected long getNextOffset(long currentOffset) throws JWNLException
    {
      try
      {
        return _db.getNextLinePointer(_pos, _fileType, currentOffset);
      } catch (IOException ex)
      {
        throw new JWNLException("DICTIONARY_EXCEPTION_008", new Object[] {
            _pos, _fileType }, ex);
      }
    }
  }

  private class IndexFileLookaheadIterator extends FileLookaheadIterator
  {
    public IndexFileLookaheadIterator(POS pos) {
      super(pos, DictionaryFileType.INDEX);
    }

    protected Object parseLine(POS pos, long offset, String line)
    {
      IndexWord word = null;
      if (isCachingEnabled())
        word = getCachedIndexWord(new POSKey(_pos, offset));
      if (word == null)
        word = parseAndCacheIndexWordLine(_pos, offset, line);
      return word;
    }
  }

  private class SubstringIndexFileLookaheadIterator extends
      IndexFileLookaheadIterator
  {
    private String _substring = null;

    public SubstringIndexFileLookaheadIterator(POS pos, String substring)
        throws JWNLException {
      super(pos);
      _substring = substring;
      nextOffset();
    }

    protected long getNextOffset(long currentOffset) throws JWNLException
    {
      try
      {
        return getFileManager().getMatchingLinePointer(_pos,
            DictionaryFileType.INDEX, currentOffset, _substring);
      } catch (IOException ex)
      {
        throw new JWNLException("DICTIONARY_EXCEPTION_008", new Object[] {
            _pos, _fileType }, ex);
      }
    }
  }
}