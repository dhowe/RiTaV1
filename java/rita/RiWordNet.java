package rita;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import rita.support.RiPos;
import rita.wordnet.RiWordNetError;
import rita.wordnet.RiZipReader;
import rita.wordnet.WordnetFilters;
import rita.wordnet.WordnetUtil;
import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.JWNLRuntimeException;
import rita.wordnet.jwnl.dictionary.Dictionary;
import rita.wordnet.jwnl.dictionary.JWNLPosException;
import rita.wordnet.jwnl.dictionary.MorphologicalProcessor;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.IndexWordSet;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.PointerTarget;
import rita.wordnet.jwnl.wndata.PointerType;
import rita.wordnet.jwnl.wndata.PointerUtils;
import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.Word;
import rita.wordnet.jwnl.wndata.list.PointerTargetNode;
import rita.wordnet.jwnl.wndata.list.PointerTargetNodeList;
import rita.wordnet.jwnl.wndata.list.PointerTargetTree;
import rita.wordnet.jwnl.wndata.relationship.AsymmetricRelationship;
import rita.wordnet.jwnl.wndata.relationship.RelationshipFinder;
import rita.wordnet.jwnl.wndata.relationship.RelationshipList;

public class RiWordNet
{
  /** String constant for Noun part-of-speech */
  public final static String NOUN = "n";

  /** String constant for Verb part-of-speech */
  public final static String VERB = "v";

  /** String constant for Adjective part-of-speech */
  public final static String ADJ = "a";

  /** String constant for Adverb part-of-speech */
  public final static String ADV = "r";

  // Filter flags ----------------------------------
  public static final int ANAGRAMS = 1;
  public static final int CONTAINS = 8;
  public static final int ENDS_WITH = 16;
  public static final int REGEX_MATCH = 64;
  public static final int STARTS_WITH = 128;
  public static final int SIMILAR_TO = 256;
  public static final int SOUNDS_LIKE = 512;
  public static final int WILDCARD_MATCH = 1024;
  public static final int HAS_EXAMPLE = 2048;
  
  public static final String WORDNET_ARCHIVE = "wdict.dat";
  public static final String QQ = "";
  public static final String SYNSET_DELIM = ":";
  public static final String DEFAULT_CONF = "file_properties.xml";
  
  public static final int[] ALL_FILTERS = { ENDS_WITH, STARTS_WITH, ANAGRAMS, CONTAINS,
      SIMILAR_TO, SOUNDS_LIKE, WILDCARD_MATCH, REGEX_MATCH // HAS_EXAMPLE,CONTAINS_ALL,CONTAINS_SOME
  };
  
  private static final String ROOT = "entity";

  private static final String[] EA = {};
  // TODO: test that this can't be changed after being returned and then maul
  // future returns

  /** @invisible */
  public static String wordNetHome;
  public static boolean useMorphologicalProcessor;

  /** @invisible */
  public Dictionary jwnlDict;

  public static RiZipReader zipReader;
  protected WordnetFilters filters;
  protected int maxCharsPerWord = 10;

  protected boolean randomizeResults = false;
  protected boolean ignoreCompoundWords = false;
  protected boolean ignoreUpperCaseWords = false;

  // -------------------- CONSTRUCTORS ----------------------------

  /**
   * Constructs an instance of <code>RiWordNet</code> using the WordNet
   * installation whose location is specified at <code>wordnetInstallDir</code>.
   * 
   * @param wordnetInstallDir
   *          home directory for a pre-installed WordNet installation.
   */
  public RiWordNet(String wordnetInstallDir)
  {
    this(wordnetInstallDir, null, false, false);
  }
  
   public RiWordNet(String wordnetInstallDir, boolean ignoreCompoundWords)
  {
    this(wordnetInstallDir, null, ignoreCompoundWords, false);
  }
  
  public RiWordNet(String wordnetInstallDir, boolean ignoreCompoundWords, boolean ignoreUpperCaseWords)
  {
    this(wordnetInstallDir, null, ignoreCompoundWords, ignoreUpperCaseWords);
  }

  protected RiWordNet(String wordnetInstallDir, Object parent, boolean ignoreCompoundWords, boolean ignoreUpperCaseWords)
  {
    this.ignoreCompoundWords = ignoreCompoundWords;
    this.ignoreUpperCaseWords = ignoreUpperCaseWords;
    
    String confFile = getDefaultConfFile();

    this.setWordNetHome(wordnetInstallDir);

    if (false && !RiTa.SILENT)
      System.err.println("RiWordNet.RiWordNet(" + wordnetInstallDir + "," + confFile + ")");

    if (!JWNL.isInitialized())
    {
      try
      {
        initWordNet(confFile);
      }
      catch (Exception e)
      {
        throw new RiWordNetError("Unable to find WordNet at '" + 
            wordnetInstallDir+"' -- have you downloaded & installed it?\n", e);
      }
    }

    if (this.jwnlDict == null)
      this.jwnlDict = Dictionary.getInstance();
  }

  private static String getDefaultConfFile()
  {
    // set the locale since the default conf is only English

    Locale.setDefault(Locale.ENGLISH);

    return DEFAULT_CONF;
  }

  /**
   * for remote creation only
   * 
   * @invisible
   * 
   *            public static RiWordNet createRemote(Map params) { return new
   *            RiWordNet(); }
   */

  // METHODS =====================================================

  /**
   * Returns an iterator over all words of the specified 'pos'
   */
  public Iterator iterator(String pos)
  {
    return getFilters().lemmaIterator(jwnlDict, convertPos(pos));
  }
  
  /**
   * Returns up to <code>maxResults</code> full anagram matches for the
   * specified <code>word</code> and <code>pos</code>
   * <p>
   * Example: 'table' returns 'bleat' (but not 'tale').
   * 
   * @param word
   * @param posStr
   * @param maxResults
   */
  public String[] getAnagrams(String word, String posStr, int maxResults)
  {
    return filter(ANAGRAMS, word, (posStr), maxResults);
  }

  /**
   * Returns all full anagram matches for the specified <code>word</code> and
   * <code>pos</code>
   * <p>
   * Example: 'table' returns 'bleat' (but not 'tale').
   * 
   * @param word
   * @param posStr
   */
  public String[] getAnagrams(String word, String posStr)
  {
    return getAnagrams(word, posStr, Integer.MAX_VALUE);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * where each contains the given <code>word</code>
   * <p>
   * Example:
   * 
   * @param word
   * @param posStr
   * @param maxResults
   */
  public String[] getContains(String word, String posStr, int maxResults)
  {
    return filter(CONTAINS, word, (posStr), maxResults);
  }

  /**
   * Returns all 'contains' matches for the specified <code>word</code> and
   * <code>pos</code>
   * <p>
   * Example:
   * 
   * @param word
   * @param posStr
   */
  public String[] getContains(String word, String posStr)
  {
    return getContains(word, posStr, Integer.MAX_VALUE);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * ending with the given <code>word</code>.
   * <p>
   * Example: 'table' returns 'turntable' & 'uncomfortable'
   * 
   * @param word
   * @param posStr
   * @param maxResults
   */
  public String[] getEndsWith(String word, String posStr, int maxResults)
  {
    return filter(ENDS_WITH, word, (posStr), maxResults);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * ending with the given <code>word</code>.
   * <p>
   * Example: 'table' returns 'turntable' & 'uncomfortable'
   * 
   * @param word
   * @param posStr
   */
  public String[] getEndsWith(String word, String posStr)
  {
    return getEndsWith(word, posStr, Integer.MAX_VALUE);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * starting with the given <code>word</code>.
   * <p>
   * Example: 'turn' returns 'turntable'
   * 
   * @param word
   * @param posStr
   * @param maxResults
   */
  public String[] getStartsWith(String word, String posStr, int maxResults)
  {
    return filter(STARTS_WITH, word, posStr, maxResults);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * starting with the given <code>word</code>.
   * <p>
   * Example: 'turn' returns 'turntable'
   * 
   * @param word
   * @param posStr
   */
  public String[] getStartsWith(String word, String posStr)
  {
    return getStartsWith(word, posStr, Integer.MAX_VALUE);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * matching the the given regular expression <code>pattern</code>.
   * <p>
   * 
   * @param pattern
   * @param posStr
   * @param maxResults
   * @see java.util.regex.Pattern
   */
  public String[] getRegexMatch(String pattern, String posStr, int maxResults)
  {
    return filter(REGEX_MATCH, pattern, posStr, maxResults);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * Example: '.*table' returns 'turntable' & 'uncomfortable'
   * 
   * @param pattern
   * @param posStr
   * @see java.util.regex.Pattern
   */
  public String[] getRegexMatch(String pattern, String posStr)
  {
    return getRegexMatch(pattern, posStr, Integer.MAX_VALUE);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * that match the soundex code of the given <code>word</code>.
   * <p>
   * 
   * @param pattern
   * @param posStr
   * @param maxResults
   */
  public String[] getSoundsLike(String pattern, String posStr, int maxResults)
  {
    return filter(SOUNDS_LIKE, pattern, posStr, maxResults);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * that match the soundex code of the given <code>word</code>.
   * 
   * @param pattern
   * @param posStr
   */
  public String[] getSoundsLike(String pattern, String posStr)
  {
    return getSoundsLike(pattern, posStr, Integer.MAX_VALUE);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * matching a wildcard <code>pattern</code>,<br>
   * with * '*' equals any number of characters, <br>
   * and '?' equals any single character.
   * <p>
   * Example: 't?le' returns (tale,tile,tole)<br>
   * Example: 't*le' returns (tatumble, turtle, tussle, etc.)<br>
   * Example: 't?le*' returns (telex, tile,tilefish,tile,talent, tiles, etc.)
   * <br>
   * 
   * @param pattern
   * @param posStr
   * @param maxResults
   */
  public String[] getWildcardMatch(String pattern, String posStr, int maxResults)
  {
    return filter(WILDCARD_MATCH, pattern, posStr, maxResults);
  }

  /**
   * Returns up to <code>maxResults</code> of the specified <code>pos</code>
   * matching a wildcard <code>pattern</code>,<br>
   * with '*' representing any number of characters, <br>
   * and '?' equals any single character..
   * <p>
   * Example: 't?le' returns (tale,tile,tole)<br>
   * Example: 't*le' returns (tatumble, turtle, tussle, etc.)<br>
   * Example: 't?le*' returns (telex, tile,tilefish,tile,talent, tiles, etc.)
   * <br>
   * 
   * @param pattern
   * @param posStr
   */
  public String[] getWildcardMatch(String pattern, String posStr)
  {
    return getWildcardMatch(pattern, posStr, Integer.MAX_VALUE);
  }

  /**
   * Return up to <code>maxResults</code> instances of specified
   * <code>posStr</code> matching the filter specified with
   * <code>filterFlag</code>
   * <p>
   * Filter types include:
   * 
   * <pre>
   * RiWordNet.EXACT_MATCH
   *         RiWordNet.ENDS_WITH
   *         RiWordNet.STARTS_WITH
   *         RiWordNet.ANAGRAMS 
   *         RiWordNet.CONTAINS_ALL
   *         RiWordNet.CONTAINS_SOME  
   *         RiWordNet.CONTAINS
   *         RiWordNet.SIMILAR_TO
   *         RiWordNet.SOUNDS_LIKE
   *         RiWordNet.WILDCARD_MATCH
   *         RiWordNet.REGEX_MATCH
   * </pre>
   * 
   * @param filterFlag
   * @param word
   * @param pos
   * @param maxResults
   * @invisible
   */
  public String[] filter(int filterFlag, String word, String pos, int maxResults)
  {
    try
    {
      return toStrArr(getFilters().filter(filterFlag, word, convertPos(pos), maxResults));
    }
    catch (Exception e)
    {
      return EA;
    }
  }

  /**
   * @invisible Return all instances of specified <code>posStr</code> matching
   *            the filter specified with <code>filterFlag</code>.
   *            <p>
   *            Filter types include:
   * 
   *            <pre>
   * RiWordNet.EXACT_MATCH
   *         RiWordNet.ENDS_WITH
   *         RiWordNet.STARTS_WITH
   *         RiWordNet.ANAGRAMS 
   *         RiWordNet.CONTAINS_ALL
   *         RiWordNet.CONTAINS_SOME  
   *         RiWordNet.CONTAINS
   *         RiWordNet.SIMILAR_TO
   *         RiWordNet.SOUNDS_LIKE
   *         RiWordNet.WILDCARD_MATCH
   *         RiWordNet.REGEX_MATCH
   * </pre>
   * @param word
   * @param pos
   * @param filterFlag
   */
  public String[] filter(int filterFlag, String word, String pos)
  {
    return filter(filterFlag, word, pos, Integer.MAX_VALUE);
  }

  /**
   * Return up to <code>maxResults</code> instances of specified matching ANY of
   * the filters specified with <code>filterFlags</code>.
   * <p>
   * Filter types include:
   * 
   * <pre>
   * RiWordNet.EXACT_MATCH
   *         RiWordNet.ENDS_WITH
   *         RiWordNet.STARTS_WITH
   *         RiWordNet.ANAGRAMS 
   *         RiWordNet.CONTAINS_ALL
   *         RiWordNet.CONTAINS_SOME  
   *         RiWordNet.CONTAINS
   *         RiWordNet.SIMILAR_TO
   *         RiWordNet.SOUNDS_LIKE
   *         RiWordNet.WILDCARD_MATCH
   *         RiWordNet.REGEX_MATCH
   * </pre>
   * 
   * @param filterFlags
   * @param words
   * @param pos
   * @param maxResults
   * @invisible
   */
  public String[] orFilter(int[] filterFlags, String[] words, String pos, int maxResults)
  {
    return toStrArr(getFilters().orFilter(filterFlags, words, convertPos(pos), maxResults));
  }

  private WordnetFilters getFilters()
  {
    if (filters == null)
      filters = new WordnetFilters(this);
    return filters;
  }

  /**
   * @invisible Return all instances of specified <code>posStr</code> matching
   *            ANY of the filters specified with <code>filterFlags</code>.
   *            <p>
   *            Filter types include:
   * 
   *            <pre>
   *         RiWordNet.EXACT_MATCH
   *         RiWordNet.ENDS_WITH
   *         RiWordNet.STARTS_WITH
   *         RiWordNet.ANAGRAMS 
   *         RiWordNet.CONTAINS_ALL
   *         RiWordNet.CONTAINS_SOME  
   *         RiWordNet.CONTAINS
   *         RiWordNet.SIMILAR_TO
   *         RiWordNet.SOUNDS_LIKE
   *         RiWordNet.WILDCARD_MATCH
   *         RiWordNet.REGEX_MATCH
   * </pre>
   * @param words
   * @param pos
   * @param filterFlag
   */
  public String[] orFilter(int[] filterFlag, String[] words, String pos)
  {
    return orFilter(filterFlag, words, pos, Integer.MAX_VALUE);
  }

  /**
   * Return up to <code>maxResults</code> instances of specified matching ALL of
   * the filters specified with <code>filterFlags</code>.
   * <p>
   * Filter types include:
   * 
   * <pre>
   * RiWordNet.EXACT_MATCH
   *         RiWordNet.ENDS_WITH
   *         RiWordNet.STARTS_WITH
   *         RiWordNet.ANAGRAMS 
   *         RiWordNet.CONTAINS_ALL
   *         RiWordNet.CONTAINS_SOME  
   *         RiWordNet.CONTAINS
   *         RiWordNet.SIMILAR_TO
   *         RiWordNet.SOUNDS_LIKE
   *         RiWordNet.WILDCARD_MATCH
   *         RiWordNet.REGEX_MATCH
   * </pre>
   * 
   * @param filterFlags
   * @param words
   * @param pos
   * @param maxResults
   */
  public String[] andFilter(int[] filterFlags, String[] words, String pos, int maxResults)
  {
    return toStrArr(getFilters().andFilter(filterFlags, words, convertPos(pos), maxResults));
  }

  /**
   * Return all instances of specified <code>posStr</code> matching ALL of the
   * filters specified with <code>filterFlags</code>.
   * <p>
   * Filter types include:
   * 
   * <pre>
   *         RiWordNet.EXACT_MATCH
   *         RiWordNet.ENDS_WITH
   *         RiWordNet.STARTS_WITH
   *         RiWordNet.ANAGRAMS 
   *         RiWordNet.CONTAINS_ALL
   *         RiWordNet.CONTAINS_SOME  
   *         RiWordNet.CONTAINS
   *         RiWordNet.SIMILAR_TO
   *         RiWordNet.SOUNDS_LIKE
   *         RiWordNet.WILDCARD_MATCH
   *         RiWordNet.REGEX_MATCH
   * </pre>
   * 
   * @param word
   * @param pos
   * @param filterFlag
   */
  public String[] andFilter(int[] filterFlag, String[] word, String pos)
  {
    return andFilter(filterFlag, word, pos, Integer.MAX_VALUE);
  }

  // ---------------- end filter methods -------------------

  protected void setWordNetHome(String path)
  {
    if (path != null)
    {
      if (!(path.endsWith("/") || path.endsWith("\\")))
        path += RiTa.SLASH;
    }

    wordNetHome = path;
    
    // String home = path != null ? path : "jar:/rita/wordnet/WordNet3.1";
    //if (!RiTa.SILENT) System.out.println("[INFO] RiTa.WordNet.HOME=" + wordNetHome);
  }

  // -------------------------- MAIN METHODS ----------------------------

  private List getSynsetList(int id)
  {
    Synset syns = getSynsetAtId(id);

    // System.out.println("getSynsetList("id+") -> "+syns);

    List l = new LinkedList();
    if (syns == null || syns.getWordsSize() < 1)
      return l;

    addLemmas(syns.getWords(), l);

    return l;
  }

  private Synset getSynsetAtId(int id)
  {
    String idStr = Integer.toString(id);
    int posDigit = Integer.parseInt(idStr.substring(0, 1));
    long offset = Long.parseLong(idStr.substring(1));

    POS pos = null;
    switch (posDigit)
    {
      case 9:
        pos = POS.NOUN;
        break;
      case 8:
        pos = POS.VERB;
        break;
      case 7:
        pos = POS.ADJECTIVE;
        break;
      case 6:
        pos = POS.ADVERB;
        break;
    }

    try
    {
      return jwnlDict.getSynsetAt(pos, offset);
    }
    catch (Throwable e)
    {
      //System.err.println(e.getMessage());
      return null;
    }
  }

  /**
   * Returns an array of unique ids, one for each 'sense' of <code>word</code>
   * with <code>pos</code>, or an empty int[] array if none are found.
   * <p>
   * A WordNet 'sense' refers to a specific WordNet meaning and maps 1-1 to the
   * concept of synsets. Each 'sense' of a word exists in a different synset.
   * <p>
   * For more info, see: {@link http://wordnet.princeton.edu/gloss}
   */
  public int[] getSenseIds(String word, String posStr)
  {
    POS pos = convertPos(posStr);
    // System.out.println("getSenseIds()="+posStr+" -> "+pos);
    IndexWord idw = lookupIndexWord(pos, word);
    return getSenseIds(idw);
  }

  /**
   * Returns an array of unique ids, one for each sense of <code>word</code>
   * with <code>pos</code>.
   */
  public int[] getSenseIds(IndexWord idw)
  {
    int[] result = null;
    try
    {
      if (idw == null) return new int[0];
      
      int numSenses = idw.getSenseCount();
      if (numSenses == 0) return new int[0];
      
      long[] offsets = idw.getSynsetOffsets();
      result = new int[offsets.length];
      
      for (int i = 0; i < result.length; i++)
        result[i] = toId(idw.getPOS(), offsets[i]);
    }
    catch (Exception e)
    {
      throw new RiWordNetError(e);
    }
    
    // System.err.println("ids: "+Util.asList(result));
    
    return result;
  }
  
  protected String getBaseForm(String word, String posStr) {
    
    POS pos = convertPos(posStr);
    
    if (pos == null) return null;
    
    MorphologicalProcessor mp = jwnlDict.getMorphologicalProcessor();
    
    if (mp == null)
      throw new RiTaException("No MorphologicalProcessor!");
    
    try
    {
      IndexWord baseForm = mp.lookupBaseForm(pos, word);
      return baseForm == null ? null : baseForm.getLemma();
    }
    catch (JWNLException e)
    {
      throw new RiTaException(e);
    }
  }

  private int toId(POS wnpos, long offset)
  {
    int posDigit = -1;
    if (wnpos == POS.NOUN)
      posDigit = 9;
    else if (wnpos == POS.VERB)
      posDigit = 8;
    else if (wnpos == POS.ADJECTIVE)
      posDigit = 7;
    else if (wnpos == POS.ADVERB)
      posDigit = 6;
    else
      throw new RiWordNetError("Invalid POS type: " + wnpos);
    return Integer.parseInt((Integer.toString(posDigit) + offset));
  }

  /**
   * Returns glosses for all senses of 'word' with 'pos', 
   */
  public String[] getAllGlosses(String word, String pos)
  {
    List glosses = new LinkedList();

    Synset[] synsets = allSynsets(word, pos);
    if (synsets == null || synsets.length<0)
      return EA;
    
    for (int i = 0; i < synsets.length; i++)
    {
      String gloss = WordnetUtil.parseGloss(getGlossFromSynset(synsets[i]));
      if (gloss != null)
        glosses.add(gloss);
    }
    
    return toStrArr(glosses);
  }

  /**
   * Returns gloss for word with unique <code>senseId</code>, or null if
   * not found
   */
  public String getGloss(int senseId)
  {
    String gloss = getGlossFromSynset(getSynsetAtId(senseId));
    return WordnetUtil.parseGloss(gloss);
  }

  private String getGlossFromSynset(Synset synset) // returns null
  {
    if (synset == null)
      return null;
    return synset.getGloss();
  }

  /**
   * Returns gloss for <code>word</code> with <code>pos</code> or null if
   * not found
   */
  public String getGloss(String word, String pos)
  {
    Synset synset = getSynsetAtIndex(word, pos, 1);
    String gloss = getGlossFromSynset(synset);
    return WordnetUtil.parseGloss(gloss);
  }

  /**
   * Returns all examples for 1st sense of <code>word</code> with
   * <code>pos</code>, 
   */
  public String[] getExamples(String word, String pos)
  {
    Synset synset = getSynsetAtIndex(word, pos, 1);
    List l = getExamples(synset);
    return toStrArr(l);
  }

  /**
   * Return a random example from the set of examples from all senses of
   * <code>word</code> with <code>pos</code>, assuming they contain
   * <code>word</code>, or else null if not found
   */
  public String getRandomExample(String word, String pos) // returns null;
  {
    String[] all = getAllExamples(word, pos);
    if (all == null || all.length <1) return null;
    int rand = (int) (Math.random() * all.length);
    return all[rand];
  }

  /**
   * Returns examples for word with unique <code>senseId</code>
   */
  public String[] getExamples(int senseId)
  {
    Synset synset = getSynsetAtId(senseId);
    if (synset == null)
      return EA;
    return toStrArr(getExamples(synset));
  }

  /**
   * Returns examples for all senses of <code>word</code> with <code>pos</code>
   * if they contain the <code>word</code>.
   */
  public String[] getAllExamples(String word, String pos)
  {
    Synset[] syns = allSynsets(word, pos);
    if (syns == null || syns.length < 1)
      return EA;
    List l = new LinkedList();
    for (int i = 0; i < syns.length; i++)
    {
      if (syns[i] != null)
      {
          List examples = getExamples(syns[i]);
          if (examples == null)
            continue;
          for (Iterator k = examples.iterator(); k.hasNext();)
          {
            String example = (String) k.next();
            // does it contain the word
            if (example.indexOf(word) < 0)
              continue;
            if (!l.contains(example))
              l.add(example);
          }
      }
    }
    l.remove(word);
    return toStrArr(l);
  }

  private List getExamples(Synset synset)
  {
    String gloss = getGlossFromSynset(synset);
    return WordnetUtil.parseExamples(gloss);
  }

  /**
   * Returns an unordered String[] containing the synset, hyponyms, similars,
   * alsoSees, and coordinate terms (checking each in order)
   */
  public String[] getSynonyms(int senseId, int maxResults)
  {
    String[] result = null;
    Set set = new TreeSet();

    result = getSynset(senseId);
    this.addSynsetsToSet(result, set);
    // System.err.println("Synsets: "+RiTa.asList(result));

    result = getHyponymTree(senseId);
    this.addSynsetsToSet(result, set);
    // System.err.println("Hypornyms: "+RiTa.asList(result));

    /*
     * result = getHypernyms(senseId); this.addSynsetsToSet(result, set);
     */
    // System.err.println("Hypernyms: "+RiTa.asList(result));

    result = getSimilar(senseId);
    this.addSynsetsToSet(result, set);
    // System.err.println("Similar: "+RiTa.asList(result));

    result = getCoordinates(senseId);
    this.addSynsetsToSet(result, set);
    // System.err.println("Coordinates: "+RiTa.asList(result));

    result = getAlsoSees(senseId);
    this.addSynsetsToSet(result, set);
    // System.err.println("AlsoSees: "+RiTa.asList(result));

    // System.err.println("=======================================");

    return setToStrings(set, maxResults);
  }

  public String[] getSynonyms(int id)
  {
    return getSynonyms(id, Integer.MAX_VALUE);
  }

  /**
   * Returns an unordered String[] containing the synset, hyponyms, similars,
   * alsoSees, and coordinate terms (checking each in order) for the first sense
   * of <code>word</code> with <code>pos</code>, 
   */
  public String[] getSynonyms(String word, String posStr, int maxResults)
  {
    if (maxResults < 0) maxResults = Integer.MAX_VALUE;
    
    boolean dbug = false;

    String[] result = null;
    Set set = new TreeSet();

    result = getSynset(word, posStr, false);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("Synsets: " + RiTa.asList(result));

    result = getHyponyms(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("Hyponyms: " + RiTa.asList(result));

    result = getSimilar(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("Similar: " + RiTa.asList(result));

    result = getAlsoSees(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("AlsoSees: " + RiTa.asList(result));

    result = getCoordinates(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("Coordinates: " + RiTa.asList(result));

    if (dbug)
      System.err.println("=======================================");
    
    set.remove(word);

    return setToStrings(set, maxResults);
  }

  /**
   * Returns an unordered String[] containing the synset, hyponyms, similars,
   * alsoSees, and coordinate terms (checking each in order) for the first sense
   * of <code>word</code> with <code>pos</code>
   */
  public String[] getSynonyms(String word, String posStr)
  {
    return getSynonyms(word, posStr, Integer.MAX_VALUE);
  }

  /**
   * Returns an unordered String[] containing the synset, hyponyms, similars,
   * alsoSees, and coordinate terms (checking each in order) for all senses of
   * <code>word</code> with <code>pos</code>
   */
  public String[] getAllSynonyms(String word, String posStr, int maxResults)
  {
    final boolean dbug = false;

    String[] result = null;
    Set set = new TreeSet();

    result = getAllSynsets(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("Synsets: " + RiTa.asList(result));

    result = getAllHyponyms(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("Hyponyms: " + RiTa.asList(result) + " Set: "
          + RiTa.asList(set));

    /*
     * result = getAllHypernyms(word, posStr); this.addSynsetsToSet(result,
     * set); if
     * (dbug)System.err.println("Hypernyms: "+RiTa.asList(result));
     */

    result = getAllSimilar(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("Similar: " + RiTa.asList(result));

    result = getAllAlsoSees(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("AlsoSees: " + RiTa.asList(result));

    result = getAllCoordinates(word, posStr);
    this.addSynsetsToSet(result, set);
    if (dbug)
      System.err.println("Coordinates: " + RiTa.asList(result));

    // System.err.println("=======================================");
    return setToStrings(set, maxResults);
  }

  public String[] getAllSynonyms(String word, String posStr)
  {
    return getAllSynonyms(word, posStr, Integer.MAX_VALUE);
  }

  private void addSynsetsToSet(String[] s, Set set)
  {
    addSynsetsToSet(s, set, Integer.MAX_VALUE);
  }

  private void addSynsetsToSet(String[] s, Set set, int maxResults)
  {
    if (s == null || s.length < 0)
      return;
    for (int i = 0; i < s.length; i++)
    {
      if (s[i].indexOf(SYNSET_DELIM) > 0)
      {
        String[] t = s[i].split(SYNSET_DELIM);
        for (int u = 0; u < t.length; u++)
        {
          set.add(t[u]);
          if (set.size() >= maxResults)
            return;
        }
      }
      else
      {
        set.add(s[i]);
        if (set.size() >= maxResults)
          return;
      }
    }
  }

  private String[] setToStrings(Set set, int maxSize)
  {
    if (set == null || set.size() == 0)
      return EA;

    List result = new ArrayList(set.size());
    result.addAll(set);

    if (randomizeResults)
      Collections.shuffle(result);

    int size = Math.min(maxSize, set.size());

    int idx = 0;
    String[] ret = new String[size];
    for (Iterator i = result.iterator(); i.hasNext();)
    {
      ret[idx++] = (String) i.next();
      if (idx == size)
        break;
    }
    return ret;
  }

  /**
   * Returns String[] of Common Parents for 1st senses of words with specified
   * pos' 
   */
  public String[] getCommonParents(String word1, String word2, String pos)
  {
    List result = getCommonParentList(word1, word2, pos);
    return toStrArr(result);
  }

  /**
   * Returns the id for the common parent of 2 words with unique ids <code>id1</code>,
   * <code>id2</code>, or -1 if no common parent is found
   */
  public int getCommonParent(int id1, int id2)
  {
    int notFound = -1;
    
    Synset syn1 = getSynsetAtId(id1);
    if (syn1 == null)
      return notFound;
    Synset syn2 = getSynsetAtId(id2);
    if (syn2 == null)
      return notFound;
    
    RelationshipList list;
    try
    {
      list = RelationshipFinder.getInstance().findRelationships(syn1, syn2, PointerType.HYPERNYM);
    }
    catch (JWNLException e)
    {
      // no relationship found
      return notFound;
    }

    AsymmetricRelationship ar = (AsymmetricRelationship) list.get(0); 
    PointerTargetNodeList nl = ar.getNodeList();
    PointerTargetNode ptn = (PointerTargetNode) nl.get(ar.getCommonParentIndex());
 
    return toId(ptn.getSynset());
  }

  private int toId(Synset synset)
  {
    return synset != null ? toId(synset.getPOS(), synset.getOffset()) : 0;
  }

  private List getCommonParentList(String word1, String word2, String pos) // returns null if not found
  {
    Synset syn = null;
    try
    {
      POS wnpos = convertPos(pos);
      
      IndexWord iw1 = lookupIndexWord(wnpos, word1);
      if (iw1 == null)
        return null;
      
      IndexWord iw2 = lookupIndexWord(wnpos, word2);
      if (iw2 == null)
        return null;
      
      syn = getCommonParent(iw1, iw2);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
    
    List l = new ArrayList();
    
    if (syn != null)
      addLemmas(syn.getWords(), l);
    
    return l.size() < 1 ? null : l;
  }

  private Synset getCommonParent(IndexWord start, IndexWord end) throws JWNLException  // returns null if not found
  {
    if (start == null || end == null)
      return null;

    RelationshipList list = null;
    try
    {
      list = RelationshipFinder.getInstance().findRelationships(start.getSense(1), end.getSense(1), PointerType.HYPERNYM);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug
    }
    
    if (list == null || list.size()<1)
      return null;
    
    // System.out.println("Hypernym relationship between \"" + start.getLemma()
    // + "\" and \"" + end.getLemma() + "\":");
    AsymmetricRelationship ar = (AsymmetricRelationship) list.get(0);
    PointerTargetNodeList nl = ar.getNodeList();
    
    // System.out.println("Common Parent Index: "+ar.getCommonParentIndex());
    PointerTargetNode ptn = (PointerTargetNode) nl.get(ar.getCommonParentIndex());
    
    return ptn == null ? null : ptn.getSynset();
  }

  // SYNSETS

  /**
   * Returns String[] of words in synset for first sense of <code>word</code>
   * with <code>pos</code>.
   * <P>
   * Note: original word is excluded by default.
   * 
   * @see #getSynset(String, String, boolean)
   */
  public String[] getSynset(String word, String pos)
  {
    return getSynset(word, pos, false);
  }
  
  public String[] getSynset(String word, String pos, boolean includeOriginal) {
    return this.getSynset(word, pos, 1, includeOriginal);
  }
  
  public String[] getSynset(String word, String pos, int senseNum) {
    return this.getSynset(word, pos, senseNum, false);
  }

  /**
   * Returns String[] of words in synset for the specified sense # of <code>word</code>
   * with <code>pos</code>.
   */
  public String[] getSynset(String word, String pos, int senseNum, boolean includeOriginal)
  {
    Synset syns = getSynsetAtIndex(word, pos, senseNum);
    
    if (syns == null || syns.getWordsSize() < 1)
      return EA;
    
    List l = new LinkedList();

    Word[] words = syns.getWords();
    addLemmas(words, l);
    
    // System.out.println("RiWordNet.getSynset("+word+","+pos+") -> "+l);

    if (includeOriginal) 
      l.add(word);
    else
      l.remove(word);
    
    return toStrArr(l);
  }

  /**
   * Returns String[] of Synsets for unique id <code>id</code> 
   */
  public String[] getSynset(int id)
  {
    List sl;
    try
    {
      sl = getSynsetList(id);
    }
    catch (Exception e)
    {
      return EA;
    }
    return toStrArr(sl);
  }

  /**
   * Returns String[] of words in each synset for all senses of
   * <code>word</code> with <code>pos</code>, 
   */
  public String[] getAllSynsets(String word, String posStr)
  {
    POS pos = convertPos(posStr);
    IndexWord idw = null;
    List result = null;
    
    try
    {
      idw = lookupIndexWord(pos, word);

      //System.out.println("sense-count="+idw.getSenseCount());

      if (idw == null || idw.getSenseCount() < 1)
        return EA;
      
      result = new LinkedList();
      for (int i = 1; i <= idw.getSenseCount(); i++)
      {
        List syns = this.getSynsetAtIndex(idw, i);
        if (syns == null || syns.size() < 1)
          continue;
        
        for (Iterator j = syns.iterator(); j.hasNext();)
        {
          String lemma = (String) j.next();
          addLemma(lemma, result);
        }
      }
      
      result.remove(word); // don't include original
      
      return toStrArr(result);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
  }

  private List getSynsetAtIndex(IndexWord word, int index) throws JWNLException
  {
    List l = new ArrayList();
    
    if (index < 1)
      throw new IllegalArgumentException("Invalid index: " + index);

    if (word != null)// && word.getSenseCount() < 1)
      addLemmas(word.getSense(index).getWords(), l);
    
    return l;
  }
  
  private Synset[] allSynsets(String word, String posStr) // returns null
  {
    POS pos = convertPos(posStr);
    IndexWord idw = lookupIndexWord(pos, word);
    
    if (idw == null) return null;
    
    int senseCount = idw.getSenseCount();
    
    if (senseCount < 1) return null;
    
    Synset[] syns = new Synset[senseCount];
    for (int i = 0; i < syns.length; i++)
    {
      try
      {
        syns[i] = idw.getSense(i + 1);
        if (syns[i] == null)
          System.err.println("[WARN] Null Synset for: "+word+"/"+ pos);
      }
      catch (JWNLException e)
      {
        throw new RiWordNetError(e);
      }
    }
    
    return syns;
  }

  private Synset getSynsetAtIndex(String word, String posStr, int idx) // returns null
  {
    IndexWord idw = lookupIndexWord(convertPos(posStr), word);
    
    if (idw == null) return null;

    if (idx < 1 || idx > idw.getSenseCount())
      throw new RiWordNetError("Bad sense #: "+idx+", expected # from 1-"+
	  this.getSenseCount(word, posStr));

    try
    {
      return idw.getSense(idx);
    }
    catch (JWNLException e)
    {
      return null;
      //throw new RiWordNetError(e);
    }
  }

  /**
   * Return the # of senses (polysemy) for a given word/pos. A 'sense' refers to
   * a specific WordNet meaning and maps 1-1 to the concept of synsets. Each
   * 'sense' of a word exists in a different synset.
   * <p>
   * For more info, see: {@link http://wordnet.princeton.edu/gloss}
   * 
   * @return # of senses or 0 if not found
   */
  public int getSenseCount(String word, String pos)
  {
    try
    {
      IndexWord iw = lookupIndexWord(pos, word);
      return (iw != null) ? iw.getSenseCount() : 0;
    }
    catch (RiWordNetError e)
    {
      System.err.println("[WARN] " + e.getMessage());
      return 0;
    }
  }

  /*
   * private List getAllSynsets(IndexWord word) throws JWNLException { List l =
   * new ArrayList(); Synset[] syns = word.getSenses(); if (syns == null ||
   * syns.length <= 0) return l; for (int k = 0; k < syns.length; k++)
   * addLemmas(syns[k].getWords(), l); return l; }
   */

  // ANTONYMS ------------
  /**
   * Returns String[] of Antonyms for the 1st sense containing valid Antonyms of <code>word</code>
   * with <code>pos</code> <br>
   * 
   * Example: 'night' -> 'day', "full", -> "empty"
   */
  public String[] getAntonyms(String word, String pos)
  {
    String[] result = EA;
    int index = getSenseCount(word, pos), i = 1;

    while (i <= index) {
      result = getPointerTargetsAtIndex(word, pos, PointerType.ANTONYM, i);
  
      if (result == EA)
        i++;
      else 
        break;
    }

    return result;
  }

  /**
   * Returns String[] of Antonyms for the specified id, <br>
   */
  //Holds for adjectives only (?)
  public String[] getAntonyms(int id)
  {
    return getPointerTargetsAtId(id, PointerType.ANTONYM);
  }

  /**
   * Returns String[] of Antonyms for the 1st sense of <code>word</code> with
   * <code>pos</code>.
   */
  //Holds for adjectives only (?)
  public String[] getAllAntonyms(String word, String pos)
  {
    return getAllPointerTargets(word, pos, PointerType.ANTONYM);
  }

  // HYPERNYMS -- direct
  
  /**
   * Returns Hypernym String[] for all senses of <code>word</code> with
   * <code>pos</code> 
   * <p>
   * X is a hyponym of Y if there exists an is-a relationship between X and Y.<br>
   * That is, if X is a subtype of Y. <br>
   * Or, for xample, if X is a species of the genus Y. <br>
   * X is a hypernym of Y is Y is a hyponym of X. <br>
   * Holds between: nouns and nouns & verbs and verbs<br>
   * Examples:
   * <ul>
   * <li>artifact is a hyponym of object
   * <li>object is a hypernym of artifact
   * <li>carrot is a hyponym of herb
   * <li>herb is a hypernym of carrot
   * </ul>
   */
  public String[] getHypernyms(String word, String posStr)
  {
    Synset synset = getSynsetAtIndex(word, posStr, 1);
    PointerTargetNodeList ptnl = null;
    try
    {
      ptnl = PointerUtils.getInstance().getDirectHypernyms(synset);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug
      //System.err.println("[WARN] JWNL Error: " + word + "/" + posStr);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
    return ptnlToStrings(word, ptnl);
  }

  /**
   * Returns Hypernym String[] for id, 
   * <p>
   * X is a hyponym of Y if there exists an is-a relationship between X and Y.<br>
   * That is, if X is a subtype of Y. <br>
   * Or, for example, if X is a species of the genus Y. <br>
   * X is a hypernym of Y is Y is a hyponym of X. <br>
   * Holds between: nouns and nouns & verbs and verbs<br>
   * Examples:
   * <ul>
   * <li>artifact is a hyponym of object
   * <li>object is a hypernym of artifact
   * <li>carrot is a hyponym of herb
   * <li>herb is a hypernym of carrot
   * </ul>
   */
  public String[] getHypernyms(int id)
  {
    Synset synset = getSynsetAtId(id);
    PointerTargetNodeList ptnl = null;
    try
    {
      ptnl = PointerUtils.getInstance().getDirectHypernyms(synset);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
    return ptnlToStrings(null, ptnl);
  }

  /*
   * Adds the hypernyms for this 'synset' to List TODO: redo with a List,
   * checking for dups
   */
  private void getHypernyms(Synset syn, Collection l) throws JWNLException
  {

    PointerTargetNodeList ptnl = null;
    try
    {
      ptnl = PointerUtils.getInstance().getDirectHypernyms(syn);
    }
    catch (NullPointerException e)
    {
      // bug from jwnl, ignore
    }
    getLemmaSet(ptnl, l);
  }

  // HYPERNYMS -- tree

  /**
   * Returns an ordered String[] of hypernym-synsets (each a semi-colon
   * delimited String) up to the root of WordNet for the 1st sense of the word,
   * 
   * 
   * @example VariousHypernyms
   */
  public String[] getAllHypernyms(String word, String posStr)
  {
    try
    {
      IndexWord idw = lookupIndexWord(convertPos(posStr), word);
      return toStrArr(this.getAllHypernyms(idw));
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /**
   * Returns an ordered String[] of hypernym-synsets (each a semi-colon
   * delimited String) up to the root of WordNet for the <code>id</code>.
   */
  public String[] getHypernymTree(int id)
  {
    Synset synset = getSynsetAtId(id);
    if (synset == null)
      return new String[] { ROOT };
    
    try
    {
      return toStrArr(getHypernymTree(synset));
    }
    catch (JWNLException e)
    {
      return EA; // not found
    }
  }

  private List getAllHypernyms(IndexWord idw) throws JWNLException // returns null
  {
    if (idw == null)
      return null;
    Synset[] synsets = idw.getSenses();
    if (synsets == null || synsets.length <= 0)
      return null;

    List result = new LinkedList();
    for (int i = 0; i < synsets.length; i++)
      getHypernyms(synsets[i], result);

    return result.size() < 1 ? null : result;
  }

  private List getHypernymTree(Synset synset) throws JWNLException // returns null
  {
    // System.err.println("RiWordNet.getHypernymTree("+word+","+synset+")");
    if (synset == null) return null;

    PointerTargetTree ptt = null;
    try
    {
      ptt = PointerUtils.getInstance().getHypernymTree(synset);

    }
    catch (NullPointerException e)
    {
      // ignore bad jwnl bug here
    }

    if (ptt == null) return null;

    List pointerTargetNodeLists = ptt.toList();

    // System.err.println("#PTNLS -> "+pointerTargetNodeLists.size());
    
    List l = new ArrayList();
    for (Iterator i = pointerTargetNodeLists.iterator(); i.hasNext();)
    {
      PointerTargetNodeList ptnl = (PointerTargetNodeList) i.next();
      List strs = this.getLemmaStrings(ptnl, SYNSET_DELIM, false);
      // System.err.println("  STRS -> "+strs);
      for (Iterator it = strs.iterator(); it.hasNext();)
      {
        String lemma = (String) it.next();
        if (lemma.length() > 0 && !l.contains(lemma))// &&
                                                     // !lemma.equalsIgnoreCase(word))
          l.add(lemma);
      }
    }
    
    l.remove(synset); // ignore the current synset (is this ok??)

    return l.size() < 1 ? null : l;
  }

  // HYPONYMS (direct)

  /**
   * Returns Hyponym String[] for 1st sense of <code>word</code> with
   * <code>pos</code> 
   * <p>
   * X is a hyponym of Y if there exists an is-a relationship between X and Y.<br>
   * That is, if X is a subtype of Y. <br>
   * Or, for xample, if X is a species of the genus Y. <br>
   * X is a hypernym of Y is Y is a hyponym of X. <br>
   * Holds between: nouns and nouns & verbs and verbs<br>
   * Examples:
   * <ul>
   * <li>artifact is a hyponym of object
   * <li>object is a hypernym of artifact
   * <li>carrot is a hyponym of herb
   * <li>herb is a hypernym of carrot
   * </ul>
   */
  public String[] getHyponyms(String word, String posStr)
  {
    Synset synset = getSynsetAtIndex(word, posStr, 1);
    // System.out.println("syn="+(synset.toString()));
    PointerTargetNodeList ptnl = null;
    try
    {
      PointerUtils pu = PointerUtils.getInstance();
      ptnl = pu.getDirectHyponyms(synset);

      if (ptnl == null) {
        //throw new RuntimeException("JWNL ERR: " + word + "/" + posStr);
        System.err.println("JWNL ERR1: " + word+" / "+posStr);
        return EA;
      }
    }
    catch (NullPointerException e)
    {
      //System.err.println("JWNL ERR2: " + word + " / " + posStr);
      return EA;
    }
    catch (JWNLException e)
    {
      System.err.println("JWNL ERR3: " + word + " / " + posStr);
      return EA;
    }
    
    return ptnlToStrings(word, ptnl);
  }

  /**
   * Returns Hyponym String[] for id, 
   * <p>
   * X is a hyponym of Y if there exists an is-a relationship between X and Y.<br>
   * That is, if X is a subtype of Y. <br>
   * Or, for xample, if X is a species of the genus Y. <br>
   * X is a hypernym of Y is Y is a hyponym of X. <br>
   * Holds between: nouns and nouns & verbs and verbs<br>
   * Examples:
   * <ul>
   * <li>artifact is a hyponym of object
   * <li>object is a hypernym of artifact
   * <li>carrot is a hyponym of herb
   * <li>herb is a hypernym of carrot
   * </ul>
   */
  public String[] getHyponyms(int id)
  {
    Synset synset = getSynsetAtId(id);
    PointerTargetNodeList ptnl = null;
    try
    {
      ptnl = PointerUtils.getInstance().getDirectHyponyms(synset);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
    return ptnlToStrings(null, ptnl);
  }

  /* Adds the hyponyms for this 'synset' to List */
  private void getHyponyms(Synset syn, Collection l) throws JWNLException
  {
    PointerTargetNodeList ptnl = null;
    try
    {
      PointerUtils pu = PointerUtils.getInstance();
      ptnl = pu.getDirectHyponyms(syn);
    }
    catch (NullPointerException e)
    {
      // bug in jwnl, throws null-pointer instead of returning null or 0-size list
      //System.out.println("JWNL BUG: " + e);
      return;
    }
    getLemmaSet(ptnl, l);
  }

  // HYPONYMS (tree)

  /**
   * Returns an unordered String[] of hyponym-synsets (each a colon-delimited
   * String)
   * 
   * @example VariousHyponyms
   */
  public String[] getAllHyponyms(String word, String posStr)
  {
    IndexWord idw = lookupIndexWord(convertPos(posStr), word);
    List l = this.getAllHyponyms(idw);
    if (l == null) return EA;
    l.remove(word);
    return toStrArr(l);
  }

  private List getAllHyponyms(IndexWord idw) // returns null
  {
    if (idw == null)
      return null;

    // String lemma = idw.getLemma();
    Synset[] synsets = null;
    try
    {
      synsets = idw.getSenses();
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
    
    if (synsets == null || synsets.length <= 0)
      return null;

    List l = new LinkedList();
    for (int i = 0; i < synsets.length; i++)
    {
      try
      {
        // System.err.println(i+") "+synsets[i].getGloss());
        getHyponyms(synsets[i], l);
      }
      catch (JWNLException e)
      {
        throw new RiWordNetError(e);
      }
    }
    // for (Iterator i = l.iterator(); i.hasNext();)
    // System.err.println(i.next());

    return l.size() < 1 ? null : l;
  }

  /**
   * Returns an unordered String[] of hyponym-synsets (each a colon-delimited
   * String) representing all paths to leaves in the ontology (the full hyponym
   * tree)
   * <p>
   */
  public String[] getHyponymTree(int id)
  {
    Synset synset = getSynsetAtId(id);

    if (synset == null)
      return EA;

    try
    {
      return toStrArr(getHyponymTree(synset));
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
  }

  private List getHyponymTree(Synset synset) throws JWNLException // returns null
  {
    if (synset == null)
      return null;

    PointerTargetTree ptt = null;
    try
    {
      ptt = PointerUtils.getInstance().getHyponymTree(synset);
    }
    catch (NullPointerException e)
    {
      // ignore bad jwnl bug here
    }

    if (ptt == null)
      return null;

    List pointerTargetNodeLists = ptt.toList();

    // 1 element per unique path to a leaf
    List l = new ArrayList();
    for (Iterator i = pointerTargetNodeLists.iterator(); i.hasNext();)
    {
      PointerTargetNodeList ptnl = (PointerTargetNodeList) i.next();
      List tmp = this.getLemmaStrings(ptnl, SYNSET_DELIM, true);

      // 1 element per synset (comma-delimited)
      for (Iterator it = tmp.iterator(); it.hasNext();)
      {
        String syn = (String) it.next();
        syn = trimFirstandLastChars(syn);
        if (syn.length() < 2)// || syn.equalsIgnoreCase(word)) // not the
                             // original
          continue;
        if (!l.contains(syn)) // no-dups
          l.add(syn);
      }
    }

    // remove all the entries from the current synset (rethink?)
    Set syns = new TreeSet();
    addLemmas(synset.getWords(), syns);

    OUTER: for (Iterator iter = l.iterator(); iter.hasNext();)
    {
      String syn = (SYNSET_DELIM + (String) iter.next() + SYNSET_DELIM); // yuck
      for (Iterator j = syns.iterator(); j.hasNext();)
      {
        String lemma = (SYNSET_DELIM + j.next() + SYNSET_DELIM);
        if (syn.indexOf(lemma) > -1)
        {
          // System.err.println("removing: "+syn);
          iter.remove();
          continue OUTER;
        }
      }
    }

    return l;
  }

  // -------------------------- AUX METHODS ----------------------------

  public boolean isAdjective(String word)
  {
    if (word == null || word.length()<1) 
      return false;
    return (getPosStr(word).indexOf(Character.toString('a')) > -1);
  }

  public boolean isAdverb(String word)
  {
    if (word == null || word.length()<1) 
      return false;
    return (getPosStr(word).indexOf(Character.toString('r')) > -1);
  }

  public boolean isVerb(String word)
  {
    if (word == null || word.length()<1) 
      return false;
    return (getPosStr(word).indexOf(Character.toString('v')) > -1);
  }

  public boolean isNoun(String word)
  {
    if (word == null || word.length()<1) 
      return false;
    return (getPosStr(word).indexOf(Character.toString('n')) > -1);
  }

  /**
   * Returns an array of all stems, 
   * 
   * @param query
   * @param pos
   */
  public String[] getStems(String query, String pos)
  {
    List tmp = getStemList(query, pos);
    return toStrArr(tmp);
  }

  /**
   * Returns stem for <code>pos</code> with <code>pos</code>
   * 
   * public String getStem(String word, String pos) { IndexWord iw = null; try {
   * iw = dictionary.getMorphologicalProcessor()
   * .lookupBaseForm(convertPos(pos), word); } catch (JWNLException e) { throw
   * new CTextError(this, e); } return (iw != null) ? iw.getLemma() : null; }
   */

  /**
   * Returns true if 'word' exists with 'pos' and is equal (via String.equals())
   * to any of its stem forms, else false;
   * 
   * @param query
   * @param pos
   */
  public boolean isStem(String word, String pos)
  {
    if (word == null || word.length()<1) 
      return false;
    String[] stems = getStems(word, pos);
    if (stems == null)
      return false;
    for (int i = 0; i < stems.length; i++)
      if (word.equals(stems[i]))
        return true;
    return false;
  }

  private List getStemList(String query, String pos)
  {
    try
    {
      POS wnpos = convertPos(pos);
      return jwnlDict.getMorphologicalProcessor().lookupAllBaseForms(wnpos, query);
    }
    catch (JWNLPosException e)
    {
      //System.err.println("ERROR: "+e.getMessage());
      //System.out.println(RiTa.stackToString(e));
      return new ArrayList();
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /**
   * Checks the existence of a 'word' in the ontology (is case-sensitive)
   * 
   * @param word
   */
  public boolean exists(String word)
  {
    IndexWord[] iw = null;
    try
    {
      if (jwnlDict == null)
        throw new RiWordNetError("Dictionary is null!");
      
      IndexWordSet iws = jwnlDict.lookupAllIndexWords(word);

      if (iws == null || iws.size() < 1)
        return false;

      iw = iws.getIndexWordArray();
//for (int i = 0; i < iw.length; i++)
  //System.out.println(i+")"+iw[i].getLemma());

    }
    catch (JWNLException e)
    {
      System.err.println("[WARN] " + e.getMessage()); // throw?
    }
    
    return (iw != null && iw.length > 0);
  }

  /**
   * Check each word in 'words' and removes those that don't exist in the
   * ontology.
   * <p>
   * Note: destructive operation
   * 
   * @invisible
   * 
   * @param words
   */
  public RiWordNet removeNonExistent(Collection words)
  {
    for (Iterator i = words.iterator(); i.hasNext();)
    {
      String word = (String) i.next();
      if (!exists(word))
        i.remove();
    }
    return this;
  }

  // -------------------------- PRIVATE METHODS ------------------------------

  private IndexWord lookupIndexWord(String pos, String word)
  {
    return this.lookupIndexWord(convertPos(pos), word);
  }

  private POS convertPos(String pos)
  {
    POS wnPos = getPOS(pos);
    if (wnPos == null && !RiTa.SILENT)
      System.err.println("[WARN] RiWordNet: Invalid POS: '" + pos + "'");
    return wnPos;
  }
  
  public static POS getPOS(String pos)  // returns null
  {
    if (pos.equalsIgnoreCase("N")) return rita.wordnet.jwnl.wndata.POS.NOUN;
    if (pos.equalsIgnoreCase("V")) return rita.wordnet.jwnl.wndata.POS.VERB;
    if (pos.equalsIgnoreCase("R")) return rita.wordnet.jwnl.wndata.POS.ADVERB;
    if (pos.equalsIgnoreCase("A")) return rita.wordnet.jwnl.wndata.POS.ADJECTIVE;
    return null;
  }

  protected IndexWord lookupIndexWord(POS pos, String cs) // returns null
  {
    // System.err.println("RiWordNet.lookupIndexWord("+cs+")");
    try
    {
      return (cs != null) ? jwnlDict.lookupIndexWord(pos, cs) : null;
    }
    catch (JWNLException e)
    {
      // JWNL bug returns null here, ignore...
      return null;
    }
  }

  private String toLemmaString(Word[] words, String delim, boolean addStartAndEndDelims) // returns null
  {
    if (words == null || words.length == 0)
      return null;

    List dest = new ArrayList();
    addLemmas(words, dest);

    String result = RiTa.join(dest, delim);

    if (addStartAndEndDelims)
      result = delim + result + delim;

    return result;
  }

  private void addLemmas(Word[] words, Collection dest)
  {
    if (words == null) return;

    for (int k = 0; k < words.length; k++)
      addLemma(words[k], dest);
  }

  private void addLemma(Word word, Collection dest)
  {
    this.addLemma(word.getLemma(), dest);
  }

  private void addLemma(String lemma, Collection dest)
  {
    if (_ignorable(lemma)) return;

    if (lemma.endsWith(")")) 
      lemma = lemma.substring(0, lemma.length() - 3);
    
    lemma = lemma.replaceAll(RiTa.USC, RiTa.SP);
    
    if (!dest.contains(lemma)) // no dups
      dest.add(lemma);
  }

  public boolean _ignorable(String lemma)
  {
    if (ignoreCompoundWords && isCompound(lemma)) 
      return true;

    if (ignoreUpperCaseWords && WordnetUtil.startsWithUppercase(lemma)) 
      return true;

    return false;
  }

  private void getLemmaSet(PointerTargetNodeList source, Collection dest)
  {
    if (source == null)
      return;

    for (Iterator i = source.iterator(); i.hasNext();)
    {
      PointerTargetNode targetNode = (PointerTargetNode) i.next();
      if (!targetNode.isLexical())
      {
        Synset syn = targetNode.getSynset();
        if (syn != null)
          addLemmas(syn.getWords(), dest);
      }
      else
      {
        addLemma(targetNode.getWord(), dest);
      }
    }
  }

  private List getLemmaStrings(PointerTargetNodeList source, String delim, boolean addStartAndEndDelims)
  {
    List l = new ArrayList();
    for (Iterator i = source.iterator(); i.hasNext();)
    {
      PointerTargetNode targetNode = (PointerTargetNode) i.next();
      if (!targetNode.isLexical())
      {
        Synset syn = targetNode.getSynset();
        if (syn != null)
        {
          String s = toLemmaString(syn.getWords(), delim, addStartAndEndDelims);
          l.add(s);
        }
      }
      else
      { // Never called???
        List dest = new ArrayList();
        addLemma(targetNode.getWord(), dest);
        System.err.println("ILLEGAL CALL TO TARGET: " + targetNode.getWord());
      }
    }
    return l == null || l.size() < 1 ? null : l;
  }

  private static String trimFirstandLastChars(String s)
  {
    if (s.length() < 2)
      throw new IllegalArgumentException("Invalid length String: '" + s + "'");
    return s.substring(1, s.length() - 1);
  }

/*  private String cleanLemma(String lemma)
  {
    if (lemma.endsWith(")")) 
      lemma = lemma.substring(0, lemma.length() - 3);
    
    lemma = lemma.replaceAll("_", RiTa.SP);
    return lemma;
  }*/

  /**
   * Returns an array of all parts-of-speech ordered according to their polysemy
   * count, returning the pos with the most different senses in the first
   * position, etc.
   * 
   * @return String[], one element for each part of speech ("a" = adjective, "n"
   *         = noun, "r" = adverb, "v" = verb).
   */
  public String[] getPos(String word)
  {
    IndexWord[] all = getIndexWords(word);
    
    if (all == null || all.length < 1)
      return EA;
    
    String[] pos = new String[all.length];
    
    for (int i = 0; i < all.length; i++)
      pos[i] = all[i].getPOS().getKey();
    
    return pos;
  }

  /**
   * @return String from ("a" = adjective, "n" = noun, "r" = adverb, "v" =
   *         verb), .
   */
  public String getPos(int id)
  {
    Synset synsets = getSynsetAtId(id);
    if (synsets == null)
      return null;
    return synsets.getPOS().getKey();
  }

  /**
   * @invisible Returns a String of characters, 1 for each part of speech: ("a"
   *            = adjective, "n" = noun, "r" = adverb, "v" = verb) or an empty
   *            String if not found.
   *            <p>
   */
  public String getPosStr(String word)
  {
    String pos = QQ;
    IndexWord[] all = getIndexWords(word);
    if (all == null)
      return pos;
    for (int i = 0; i < all.length; i++)
      pos += all[i].getPOS().getKey();
    return pos;
  }

  /**
   * Finds the most common part-of-speech for a word based on its
   * polysemy count, returning the pos for the version of the word 
   * with the most different senses.
   * 
   * @return single character String for the most common part-of-speech ("a" =
   *         adjective, "n" = noun, "r" = adverb, "v" = verb), or null if the 
   *         word is not found.
   */
  public String getBestPos(String word) // returns null
  {
    IndexWord[] all = getIndexWords(word);
    
    if (all == null || all.length < 1)
      return null;
    
    POS p = all[0].getPOS();
    
    if (p == POS.NOUN)
      return NOUN;
    if (p == POS.VERB)
      return VERB;
    if (p == POS.ADVERB)
      return ADV;
    if (p == POS.ADJECTIVE)
      return ADJ;
    
    throw new RiWordNetError("no pos for word: " + word);
  }

  public String posToWordNet(String pos)
  { 
    return RiPos.posToWordNet(pos);
  }
  
  private IndexWord[] getIndexWords(String word)
  {
    // IndexWord[] all = null;
    List list = new ArrayList();
    for (Iterator itr = POS.getAllPOS().iterator(); itr.hasNext();)
    {
      IndexWord current = lookupIndexWord((POS) itr.next(), word.toString());
      if (current != null)
      {
        int polysemy = current.getSenseCount();
        list.add(new ComparableIndexWord(current, polysemy));
      }
    }
    int idx = 0;
    Collections.sort(list);
    IndexWord[] iws = new IndexWord[list.size()];
    for (Iterator i = list.iterator(); i.hasNext();)
    {
      ComparableIndexWord ciw = (ComparableIndexWord) i.next();
      iws[idx++] = ciw.iw;
    }
    return iws;
  }

  class ComparableIndexWord implements Comparable
  {
    IndexWord iw;
    int polysemy = -1;

    public ComparableIndexWord(IndexWord current, int polysemy)
    {
      this.iw = current;
      this.polysemy = polysemy;
    }

    public String toString()
    {
      return iw.toString() + "polysemy=" + polysemy;
    }

    public int compareTo(Object arg0)
    {
      ComparableIndexWord ciw = (ComparableIndexWord) arg0;
      if (ciw.polysemy == polysemy)
        return 0;
      return (ciw.polysemy > polysemy) ? 1 : -1;
    }
  }

  /**
   * @param confFile
   *          wordnet xml-based configuration file full path.
   * @throws FileNotFoundException
   */
  private void initWordNet(String confFile) throws JWNLException
  {
    //System.err.println("[INFO] Initializing WordNet: conf='" + confFile + "'");

    InputStream is = WordnetUtil.getResourceStream(WordnetUtil.class, confFile);

    //System.err.println("[INFO] Initializing WordNet: stream='" + is + "'");

    JWNL.initialize(is);
    
    //System.err.println("[INFO] Initialized *** ");
  }

  String[] toStrArr(List l)
  {
    if (l == null || l.size() == 0)
      return EA;
    
    if (randomizeResults)
      Collections.shuffle(l);
    
    return (String[]) l.toArray(new String[l.size()]);
  }

  String[] ptnlToStrings(String query, PointerTargetNodeList ptnl)
  {
    if (ptnl == null || ptnl.size() == 0)
      return EA;
    
    List l = new LinkedList();
    getLemmaSet(ptnl, l);
    
    // ??? (remove this? what if we dont know the original?)
    if (query != null)
      l.remove(query); // remove original
    
    if (randomizeResults)
      Collections.shuffle(l);
    
    return toStrArr(l);
  }

  // RANDOM METHODS ==============================================

  /**
   * Returns a random example from a random word w' <code>pos</code> or null if the pos is not valid
   */
  public String getRandomExample(String pos)
  {
    String[] exs = getRandomExamples(pos, 1);
    return (exs == null || exs.length<1) ? null : exs[0];
  }

  /**o
   * Returns <code>numExamples</code> random examples from random words w'
   * <code>pos</code>
   * 
   * @return random examples
   */
  public String[] getRandomExamples(String pos, int numExamples)
  {
    POS wnpos = convertPos(pos);
    if (wnpos == null) return EA;
    
    int idx = 0;
    String[] result = new String[numExamples];
    WHILE: while (true)
    {
      try
      {
        IndexWord iw = null;
        while (iw == null || (!ignoreCompoundWords && iw.getLemma().indexOf(' ') > -1)) {
          iw = jwnlDict.getRandomIndexWord(wnpos);
        }

        Synset syn = iw.getSenses()[0];
        List l = getExamples(syn);
        if (l == null || l.size() < 1)
          continue;
        for (Iterator i = l.iterator(); i.hasNext();)
        {
          String example = (String) i.next();
          if (example != null)
          {
            result[idx++] = example;
            break;
          }
        }
        if (idx == result.length)
          break WHILE;
      }
      catch (JWNLException e)
      {
        System.err.println("WARN] Unexpected Exception: " + e.getMessage());
      }
    }
    return result;
  }

  /**
   * Returns <code>count</code> random words w' <code>pos</code>
   * 
   * @return String[] of random words
   */
  public String[] getRandomWords(String pos, int count)
  {
    POS wnpos = convertPos(pos);
    if (wnpos == null) return EA;
    String[] result = new String[count];
    for (int i = 0; i < result.length; i++)
      result[i] = getRandomWord(pos, true, maxCharsPerWord);
    return result;
  }

  /**
   * Returns a random stem with <code>pos</code> and a max length of
   * <code>this.maxCharsPerWord</code>.
   * 
   * @return random word
   */
  public String getRandomWord(String pos)
  {
    return this.getRandomWord(pos, true, maxCharsPerWord);
  }

  /**
   * Returns a random word with <code>pos</code> and a maximum of
   * <code>maxChars</code>.
   * 
   * @return a random word or null if none are found
   */
  public String getRandomWord(String pos, boolean stemsOnly, int maxChars)
  {
    IndexWord iw = null;
    POS wnPos = convertPos(pos);
    if (wnPos == null) return null;
    while (true)
    {
      try
      {
        iw = jwnlDict.getRandomIndexWord(wnPos);
        if (iw == null) continue;
      }
      catch (JWNLPosException e)
      {
        // System.err.println("[WARN] "+e.getMessage());
        // if (e != null &&
        // e.getMessage().trim().startsWith("Illegal tokenizer state"))
        // System.out.println("\n[WARN] "+e.getMessage());
        continue;
      }
      catch (JWNLRuntimeException e)
      {
        throw new RiWordNetError("UNEXPECTED!",e);
      }
      catch (JWNLException e)
      {
        throw new RiWordNetError(e);
      }
      
      String word = iw.getLemma();
      
      if (_ignorable(word) || word.length() > maxChars)
        continue;
      
      if (!stemsOnly || isStem(word, pos))
        return word;
    }
  }

  /**
   * Returns true if the word is considered compound (contains either a space,
   * dash,or underscore), else false
   */
  public static boolean isCompound(String word)
  {
    return word.indexOf(RiTa.USC) > 0 || word.indexOf(' ') > 0;
  }

  /** @invisible */
  public Dictionary getDictionary()
  {
    return jwnlDict;
  }

  /**
   * Prints the full hyponym tree to System.out (primarily for debugging).
   * 
   * @param senseId
   * @invisible
   */
  public RiWordNet printHyponymTree(int senseId)
  {
    return dumpHyponymTree(System.out, getSynsetAtId(senseId));
  }

  void dumpHyponymTree(String word, String pos) {
  
    dumpHyponymTree(System.err, word, pos);
  }

  public RiWordNet dumpHyponymTree(PrintStream ps, String word, String pos) 
  {
    IndexWord iw = lookupIndexWord(pos, word);
    Synset syn = null;
    try
    {
      syn = iw.getSense(1);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
    return this.dumpHyponymTree(ps, syn);
  }

  RiWordNet dumpHyponymTree(PrintStream ps, Synset syn) 
  {
    PointerTargetTree hyponyms = null;
    try
    {
      hyponyms = PointerUtils.getInstance().getHyponymTree(syn);
    }
    catch (JWNLException e) {
      
      throw new RiWordNetError(e);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug
    }
    if (hyponyms != null) {
      Set syns = new TreeSet();
      addLemmas(syn.getWords(), syns);
      ps.println("\nHyponyms of synset" + syns
          + ":\n-------------------------------------------");
  
      hyponyms.print(ps);
      ps.println();
    }
    return this;
  }

  /**
   * Prints the full hypernym tree to System.out (primarily for debugging).
   * 
   * @param senseId
   */
  public RiWordNet printHypernymTree(int senseId)
  {
    Synset s = getSynsetAtId(senseId);
    // System.out.println("Syn: "+s);
    return dumpHypernymTree(System.out, s);
  }

  RiWordNet printHypernymTree(String word, String pos)
  {
    return dumpHypernymTree(System.err, word, pos);
  }

  /** @invisible */
  public RiWordNet dumpHypernymTree(PrintStream ps, String word, String pos) 
  {
    // Get all the hyponyms (children) of the first sense of <var>word</var>
    IndexWord iw = lookupIndexWord(pos, word);
    Synset syn = null;
    try
    {
      syn = iw.getSense(1);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
    return dumpHypernymTree(ps, syn);
  }

  RiWordNet dumpHypernymTree(PrintStream ps, Synset syn) 
  {
    PointerTargetTree hypernyms = null;
    try
    {
      hypernyms = PointerUtils.getInstance().getHypernymTree(syn);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
    catch (StackOverflowError e)
    {
      PointerUtils.getInstance().setOverflowError(true);
      try
      {
        hypernyms = PointerUtils.getInstance().getHypernymTree(syn);
      }
      catch (JWNLException f)
      {
        throw new RiWordNetError(f);
      }
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug
    }

    if (hypernyms == null)
      return this;

    Set syns = new TreeSet();
    addLemmas(syn.getWords(), syns);
    ps.println("\nHypernyms of synset" + syns
        + ":\n-------------------------------------------");
    hypernyms.print(ps);
    ps.println();
    return this;
  }

  /**
   * Returns the min distance between any two senses for the 2 words (nouns or verbs only) in the
   * wordnet tree (result normalized to 0-1) with specified pos, or 1.0 if
   * either is not found.
   * <P>
   * The algorithm procedes as follows:
   * <ol>
   * <li>locate node <code>cp</code>, the common parent of the two lemmas, if
   * one exists, by checking each sense of each lemma; if one is not found,
   * return 1.0
   * <li>calculate <code>minDistToCommonParent</code>, the shortest path from
   * either lemma to cp
   * <li>calculate <code>distFromCommonParentToRoot</code>, the length of the
   * path from cp to the root of ontology
   * <li>calculate and return the <code>normalizedDistToCommonParent</code> as:
   * <br>
   * <code>(minDistToCommonParent / (distFromCommonParentToRoot + minDistToCommonParent))</code>
   * <ol>
   */
  public float getDistance(String lemma1, String lemma2, String pos)
  {
    IndexWordSet WORDSET1, WORDSET2;
    IndexWord WORD1, WORD2;

    float d = 1.0f;
    float smallestD = 1.0f;
    POS p = convertPos(pos);

    if (lemma1.equals(lemma2))
    {
      smallestD = 0.0f;
    }
    else
    {
      try
      {
        // get complete definition for each word (all POS, all senses)
        WORDSET1 = this.jwnlDict.lookupAllIndexWords(lemma1);
        WORDSET2 = this.jwnlDict.lookupAllIndexWords(lemma2);

        // for each POS listed in wordTypes...
        // for (int i = 0; i < wordTypes.length; i++)
        // p = wordTypes[i];

        if (WORDSET1.isValidPOS(p) && WORDSET2.isValidPOS(p))
        {
          WORD1 = WORDSET1.getIndexWord(p);
          WORD2 = WORDSET2.getIndexWord(p);

          // get distance between words based on this POS
          try
          {
            d = getWordDistance(WORD1, WORD2);
          }
          catch (NullPointerException e)
          {
            // ignore jwnl bug
          }
          if (d < smallestD)
          {
            smallestD = d;
          }
        }

      }
      catch (JWNLException e)
      {
        System.err.println("[WARN] Error obtaining distance: " + e);
        return 1.0f;
      }
    }

    return smallestD;
  }

  // get distance between words that are the same POS
  private float getWordDistance(IndexWord start, IndexWord end) throws JWNLException, NullPointerException // on
                                                                                                           // //
                                                                                                           // bug
  {
    RelationshipList relList;
    AsymmetricRelationship rel;
    int cpIndex, relLength, depth, depthRootCp, depthCpLeaf;
    float distance, newDistance;
    PointerTargetNode cpNode;
    Synset cpSynset;
    List cpHypListList;
    distance = 1.0f;

    int senseCount1 = start.getSenseCount();
    int senseCount2 = end.getSenseCount();

    // for each pairing of word senses...
    for (int i = 1; i <= senseCount1; i++)
    {
      for (int j = 1; j <= senseCount2; j++)
      {
        // get list of relationships between words (usually only one)
        try
        {
          // System.out.println(i+","+j+": "+start.getSense(i)+","+end.getSense(j));
          relList = RelationshipFinder.getInstance().findRelationships(start.getSense(i), end.getSense(j), PointerType.HYPERNYM);
        }
        catch (Exception e)
        {
          // System.out.println("RiWordNet.getWordDistance().exception="+e.getMessage());
          continue;
        }

        // calculate distance for each one
        for (Iterator relListItr = relList.iterator(); relListItr.hasNext();)
        {
          rel = (AsymmetricRelationship) relListItr.next();
          cpIndex = rel.getCommonParentIndex();
          relLength = rel.getDepth();

          // distance between items going through the CP
          // (depth of furthest word from CP)
          depthCpLeaf = Math.max(relLength - cpIndex, cpIndex);

          // get the CPI node
          cpNode = (PointerTargetNode) rel.getNodeList().get(cpIndex);
          // get the synset of the CPI node
          cpSynset = cpNode.getSynset();
          // get all the hypernyms of the CPI synset
          // returns a list of hypernym chains.
          // probably always one chain, but better to be safe...
          cpHypListList = (PointerUtils.getInstance().getHypernymTree(cpSynset)).toList();

          // System.out.println("PARENT: "+cpSynset);

          // get shortest depth from root to CP
          depthRootCp = -1;
          for (Iterator cpHypListListItr = cpHypListList.iterator(); cpHypListListItr.hasNext();)
          {
            depth = ((List) cpHypListListItr.next()).size();
            if (depthRootCp == -1)
            {
              depthRootCp = depth;
            }
            else
            {
              if (depth < depthRootCp)
              {

                depthRootCp = depth;
              }
            }
          }

          // normalize the distance
          newDistance = (float) depthCpLeaf / (depthRootCp + depthCpLeaf);
          if (newDistance < distance)
          {
            distance = newDistance;
          }
        }
      }
    }
    return distance;
  }

  /**
   * Returns array of whole-to-part relationships for 1st sense of word/pos, or
   * null if not found<br>
   * X is a meronym of Y if Y has X as a part.<br>
   * X is a holonym of Y if X has Y as a part. That is, if Y is a meronym of X. <br>
   * Holds between: Nouns and nouns<br>
   * Returns part,member, and substance meronyms<br>
   * Example: arm -> [wrist, carpus, wrist-joint, radiocarpal-joint...]
   * 
   * @param query
   * @param pos
   */
  public String[] getMeronyms(String query, String pos)
  {
    try
    {
      Synset synset = getSynsetAtIndex(query, pos, 1);
      if (synset == null)
        return EA;
      
      PointerTargetNodeList ptnl = null;
      try
      {
        ptnl = PointerUtils.getInstance().getMeronyms(synset);
      }
      catch (NullPointerException e)
      {
        // ignore jwnl bug
        // throw new WordnetError(e);
      }
      return ptnlToStrings(query, ptnl);
    }

    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /**
   * Returns array of whole-to-part relationships for id, <br>
   * X is a meronym of Y if Y has X as a part.<br>
   * X is a holonym of Y if X has Y as a part. That is, if Y is a meronym of X. <br>
   * Holds between: Nouns and nouns<br>
   * Returns part,member, and substance meronyms<br>
   * Example: arm -> [wrist, carpus, wrist-joint, radiocarpal-joint...]
   */
  public String[] getMeronyms(int id)
  {
    try
    {
      Synset synset = getSynsetAtId(id);
      if (synset == null)
        return EA;
      PointerTargetNodeList ptnl = null;
      try
      {
        ptnl = PointerUtils.getInstance().getMeronyms(synset);
      }
      catch (NullPointerException e)
      {
        // ignore jwnl bug
        // throw new WordnetError(e);
      }
      return ptnlToStrings(null, ptnl);
    }

    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /**
   * Returns array of whole-to-part relationships for all senses of word/pos, or
   * null if not found<br>
   * X is a meronym of Y if Y has X as a part.<br>
   * X is a holonym of Y if X has Y as a part. That is, if Y is a meronym of X. <br>
   * Holds between: Nouns and nouns<br>
   * Returns part,member, and substance meronyms<br>
   * Example: arm -> [wrist, carpus, wrist-joint, radiocarpal-joint...]
   * 
   * @param query
   * @param pos
   */
  public String[] getAllMeronyms(String query, String pos)
  {
    try
    {
      Synset[] synsets = allSynsets(query, pos);
      if (synsets == null)
        return EA;
      List l = new LinkedList();
      for (int i = 0; i < synsets.length; i++)
      {
        if (synsets[i] == null)
          continue;
        PointerTargetNodeList ptnl = null;
        try
        {
          ptnl = PointerUtils.getInstance().getMeronyms(synsets[i]);
        }
        catch (NullPointerException e)
        {
          // ignore jwnl bug
          // throw new WordnetError(e);
        }
        getLemmaSet(ptnl, l);
      }
      l.remove(query); // skip original
      return toStrArr(l);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /**
   * Returns part-to-whole relationships for 1st sense of word/pos, or none if
   * not found<br>
   * X is a meronym of Y if Y has X as a part.<br>
   * X is a holonym of Y if X has Y as a part. That is, if Y is a meronym of X. <br>
   * Holds between: nouns and nouns<br>
   * Returns part, member, and substance holonyms<br>
   * Example: arm -> [body, physical-structure, man, human...]
   * 
   * @param query
   * @param pos
   */
  public String[] getHolonyms(String query, String pos)
  {
    PointerTargetNodeList ptnl = null;
    try
    {
      Synset synset = getSynsetAtIndex(query, pos, 1);
      if (synset == null)
        return EA;
      ptnl = PointerUtils.getInstance().getHolonyms(synset);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
    // returns part,member, and substance holonyms
    return ptnlToStrings(query, ptnl);
  }

  /**
   * Returns part-to-whole relationships for 1st sense of word/pos, or none if
   * not found<br>
   * X is a meronym of Y if Y has X as a part.<br>
   * X is a holonym of Y if X has Y as a part. That is, if Y is a meronym of X. <br>
   * Holds between: nouns and nouns<br>
   * Returns part, member, and substance holonyms<br>
   * Example: arm -> [body, physical-structure, man, human...]
   * 
   * @param query
   * @param pos
   */
  public String[] getHolonyms(int id)
  {
    PointerTargetNodeList ptnl = null;
    try
    {
      Synset synset = getSynsetAtId(id);
      if (synset == null)
        return EA;
      ptnl = PointerUtils.getInstance().getHolonyms(synset);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
    return ptnlToStrings(null, ptnl);
  }

  /**
   * Returns part-to-whole relationships for all sense of word/pos, or none if
   * not found<br>
   * X is a meronym of Y if Y has X as a part.<br>
   * X is a holonym of Y if X has Y as a part. That is, if Y is a meronym of X. <br>
   * Holds between: nouns and nouns<br>
   * Returns part, member, and substance holonyms<br>
   * Example: arm -> [body, physical-structure, man, human...]
   * 
   * @param query
   * @param pos
   */
  public String[] getAllHolonyms(String query, String pos)
  {
    try
    {
      Synset[] synsets = allSynsets(query, pos);
      if (synsets == null)
        return EA;
      List l = new LinkedList();
      for (int i = 0; i < synsets.length; i++)
      {
        if (synsets[i] == null)
          continue;
        PointerTargetNodeList ptnl = null;
        try
        {
          ptnl = PointerUtils.getInstance().getHolonyms(synsets[i]);
        }
        catch (NullPointerException e)
        {
          // jwnl bug
        }
        getLemmaSet(ptnl, l);
      }
      l.remove(query); // skip original
      return toStrArr(l);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /**
   * Returns coordinate terms for 1st sense of word/pos, <br>
   * X is a coordinate term of Y if there exists a term Z which is the hypernym
   * of both X and Y.<br>
   * Examples:
   * <ul>
   * <li>blackbird and robin are coordinate terms (since they are both a kind of
   * thrush)
   * <li>gun and bow are coordinate terms (since they are both weapons)
   * <li>fork and spoon are coordinate terms (since they are both cutlery, or
   * eating utensils)
   * <li>hat and helmet are coordinate terms (since they are both a kind of
   * headgear or headdress)
   * </ul>
   * Example: arm -> [hind-limb, forelimb, flipper, leg, crus, thigh, arm...]<br>
   * Holds btwn nouns/nouns and verbs/verbs
   * 
   * @param query
   * @param pos
   */
  public String[] getCoordinates(String query, String pos)
  {
    String[] result = null;
    try
    {
      Synset synset = getSynsetAtIndex(query, pos, 1);
      if (synset == null)
        return EA;
      PointerTargetNodeList ptnl = PointerUtils.getInstance().getCoordinateTerms(synset);
      if (ptnl != null)
        result = ptnlToStrings(query, ptnl);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug here
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
    return result;
  }

  /**
   * Returns String[] of Coordinates for the specified id, <br>
   */
  public String[] getCoordinates(int id)
  {
    String[] result = null;
    try
    {
      Synset synset = getSynsetAtId(id);
      if (synset == null)
        return EA;
      PointerTargetNodeList ptnl = PointerUtils.getInstance().getCoordinateTerms(synset);
      if (ptnl != null)
        result = ptnlToStrings(null, ptnl);
    }
    catch (NullPointerException e)
    {
      // ignore jwnl bug here
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
    return result;
  }

  /**
   * Returns coordinate terms for all sense of word/pos, <br>
   * X is a coordinate term of Y if there exists a term Z which is the hypernym
   * of both X and Y.<br>
   * Examples:
   * <ul>
   * <li>blackbird and robin are coordinate terms (since they are both a kind of
   * thrush)
   * <li>gun and bow are coordinate terms (since they are both weapons)
   * <li>fork and spoon are coordinate terms (since they are both cutlery, or
   * eating utensils)
   * <li>hat and helmet are coordinate terms (since they are both a kind of
   * headgear or headdress)
   * </ul>
   * Example: arm -> [hind-limb, forelimb, flipper, leg, crus, thigh, arm...]<br>
   * Holds btwn nouns/nouns and verbs/verbs
   * 
   * @param query
   * @param pos
   */
  public String[] getAllCoordinates(String query, String pos)
  {
    try
    {
      Synset[] synsets = allSynsets(query, pos);
      if (synsets == null)
        return EA;
      List l = new LinkedList();
      for (int i = 0; i < synsets.length; i++)
      {
        if (synsets[i] == null)
          continue;
        PointerTargetNodeList ptnl = null;
        try
        {
          ptnl = PointerUtils.getInstance().getCoordinateTerms(synsets[i]);
        }
        catch (NullPointerException e)
        {
          // ignore jwnl bug
        }
        getLemmaSet(ptnl, l);
      }
      l.remove(query); // skip original
      return toStrArr(l);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /**
   * Returns verb group for 1st sense of verb <br>
   * Example: live -> [dwell, inhabit]<br>
   * Holds for verbs
   * 
   * @param query
   * @param pos
   */
  public String[] getVerbGroup(String query, String pos)
  {
    PointerTargetNodeList ptnl = null;
    try
    {
      Synset synset = /*is(VERB, pos).*/getSynsetAtIndex(query, pos, 1);
      if (synset == null)
        return EA;
      try
      {
        ptnl = PointerUtils.getInstance().getVerbGroup(synset);
      }
      catch (NullPointerException e)
      {
        // ignore jwnl bug
      }
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
    
    return ptnlToStrings(query, ptnl);
  }

  /**
   * Returns verb group for id, <br>
   * Example: live -> [dwell, inhabit]<br>
   * Holds for verbs
   */
  public String[] getVerbGroup(int id)
  {
    PointerTargetNodeList ptnl = null;
    try
    {
      Synset synset = getSynsetAtId(id);
      if (synset == null)
        return EA;
      try
      {
        ptnl = PointerUtils.getInstance().getVerbGroup(synset);
      }
      catch (NullPointerException e)
      {
        // ignore jwnl bug
      }
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
    return ptnlToStrings(null, ptnl);
  }

  /**
   * Returns verb group for all senses of verb <br>
   * Example: live -> [dwell, inhabit]<br>
   * Holds for verbs
   * 
   * @param query
   * @param pos
   */
  public String[] getAllVerbGroups(String query, String pos)
  {
    try
    {
      Synset[] synsets = /*is(VERB, pos).*/allSynsets(query, pos);
      if (synsets == null)
        return EA;
      
      List l = new LinkedList();
      for (int i = 0; i < synsets.length; i++)
      {
        if (synsets[i] == null)
          continue;
        PointerTargetNodeList ptnl = null;
        try
        {
          ptnl = PointerUtils.getInstance().getVerbGroup(synsets[i]);
        }
        catch (NullPointerException e)
        {
          // ignore jwnl bug
        }
        getLemmaSet(ptnl, l);
      }
      l.remove(query); // skip original
      return toStrArr(l);
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /**
   * Returns derived terms for 1st sense of word/pos <br>
   * Holds for adverbs <br>
   * Example: happily -> [jubilant, blithe, gay, mirthful, merry, happy]
   * 
   * @param query
   * @param pos
   */
  public String[] getDerivedTerms(String query, String pos)
  {
    return /*is(ADV, pos).*/getPointerTargetsAtIndex(query, pos, PointerType.DERIVED, 1);
  }

  /**
   * Returns derived terms for the id, <br>
   * Holds for adverbs <br>
   * Example: happily -> [jubilant, blithe, gay, mirthful, merry, happy]
   */
  public String[] getDerivedTerms(int id)
  {
    return getPointerTargetsAtId(id, PointerType.DERIVED);
  }

  /**
   * Returns derived terms forall senses of word/pos <br>
   * Holds for adverbs <br>
   * Example: happily -> [jubilant, blithe, gay, mirthful, merry, happy]
   * 
   * @param query
   * @param pos
   */
  public String[] getAllDerivedTerms(String query, String pos)
  {
    return /*is(ADV, pos).*/getAllPointerTargets(query, pos, PointerType.DERIVED);
  }

  // Verifies pos type or throws exception
  private RiWordNet is(String expected, String pos)
  {
    if (!pos.equals(expected)) 
      throw new RiTaException("Expecting pos of '"+expected+"' but found: '"+pos+"'");
    return this;
  }

  /**
   * Returns also-see terms for 1st sense of word/pos <br>
   * Holds for nouns (?) & adjectives<br>
   * Example: happy -> [cheerful, elated, euphoric, felicitous, joyful,
   * joyous...]
   * 
   * @param query
   * @param pos
   */
  public String[] getAlsoSees(String query, String pos)
  {
    return getPointerTargetsAtIndex(query, pos, PointerType.SEE_ALSO, 1);
  }

  /**
   * Returns also-see terms for senseId <br>
   * Holds for nouns (?) & adjectives<br>
   * Example: happy -> [cheerful, elated, euphoric, felicitous, joyful,
   * joyous...]
   * 
   * @param senseId
   */
  public String[] getAlsoSees(int senseId)
  {
    return getPointerTargetsAtId(senseId, PointerType.SEE_ALSO);
  }

  /**
   * Returns also-see terms for all senses of word/pos <br>
   * Holds for nouns (?) & adjectives<br>
   * Example: happy -> [cheerful, elated, euphoric, felicitous, joyful,
   * joyous...]
   * 
   * @param query
   * @param pos
   */
  public String[] getAllAlsoSees(String query, String pos)
  {
    return getAllPointerTargets(query, pos, PointerType.SEE_ALSO);
  }

  /**
   * Returns nominalized terms for 1st sense of word/pos <br>
   * Refers to the use of a verb or an adjective as a noun. Holds for nouns,
   * verbs & adjecstives(?)<br>
   * Example: happiness(n) -> [happy, unhappy]<br>
   * happy(a) -> [happiness, felicity]<br>
   * 
   * @param query
   * @param pos
   */
  public String[] getNominalizations(String query, String pos)
  {
    return getPointerTargetsAtIndex(query, pos, PointerType.NOMINALIZATION, 1);
  }

  /**
   * Returns nominalized terms for id, <br>
   * Refers to the use of a verb or an adjective as a noun. Holds for nouns,
   * verbs & adjecstives(?)<br>
   * Example: happiness(n) -> [happy, unhappy]<br>
   * happy(a) -> [happiness, felicity]<br>
   */
  public String[] getNominalizations(int id)
  {
    return getPointerTargetsAtId(id, PointerType.NOMINALIZATION);
  }

  /**
   * Returns nominalized terms for all sense of word/pos <br>
   * Refers to the use of a verb or an adjective as a noun. Holds for nouns,
   * verbs & adjecstives(?)<br>
   * Example: happiness(n) -> [happy, unhappy]<br>
   * happy(a) -> [happiness, felicity]<br>
   * 
   * @param query
   * @param pos
   */
  public String[] getAllNominalizations(String query, String pos)
  {
    return getAllPointerTargets(query, pos, PointerType.NOMINALIZATION);
  }

  /**
   * Returns similar-to list for first sense of word/pos <br>
   * Holds for adjectives<br>
   * Example:<br>
   * happy(a) -> [blessed, blissful, bright, golden, halcyon, prosperous...]<br>
   * 
   * @param query
   * @param pos
   */
  public String[] getSimilar(String query, String pos)
  {
    return getPointerTargetsAtIndex(query, pos, PointerType.SIMILAR_TO, 1);
  }

  /**
   * Returns similar-to list for id, <br>
   * Holds for adjectives<br>
   * Example:<br>
   * happy(a) -> [blessed, blissful, bright, golden, halcyon, prosperous...]<br>
   */
  public String[] getSimilar(int id)
  {
    return getPointerTargetsAtId(id, PointerType.SIMILAR_TO);
  }

  /**
   * Returns similar-to list for all sense of word/pos <br>
   * Holds for adjectives<br>
   * Example:<br>
   * happy(a) -> [blessed, blissful, bright, golden, halcyon, prosperous...]<br>
   * 
   * @param query
   * @param pos
   */
  public String[] getAllSimilar(String query, String pos)
  {
    return getAllPointerTargets(query, pos, PointerType.SIMILAR_TO);
  }

  // PRIVATES --------------------------------------------------------

  /* Get all the pointer targets of <var>synset</var> of type <var>type</var>, or null if not found*/
  private PointerTargetNodeList getPointerTargets(Synset synset, PointerType type)
      throws JWNLException // returns null
  {
    if (synset == null)
      return null;

    // System.err.println("RiWordNet.getPointerTargets("+synset+", "+type+")");

    try
    {
      PointerTarget[] pta = synset.getTargets(type);
      return (pta == null || pta.length == 0) ? null: new PointerTargetNodeList(pta);
    }
    catch (NullPointerException e) // JWNL bug
    {
      return null;
    }
  }

  public boolean randomizeResults()
  {
    return this.randomizeResults;
  }

  public RiWordNet randomizeResults(boolean random)
  {
    this.randomizeResults = random;
    return this;
  }

  public boolean ignoreCompoundWords()
  {
    return this.ignoreCompoundWords;
  }

  public RiWordNet ignoreCompoundWords(boolean val)
  {
    this.ignoreCompoundWords = val;
    return this;
  }

  public boolean ignoreUpperCaseWords()
  {
    return this.ignoreUpperCaseWords;
  }

  public RiWordNet ignoreUpperCaseWords(boolean val)
  {
    this.ignoreUpperCaseWords = val;
    return this;
  }

  private String[] getAllPointerTargets(String word, String pos, PointerType type) 
  {
    Synset[] syns = allSynsets(word, pos);
    if (syns == null || syns.length < 1)
      return EA;
    
    List result = new LinkedList();
    for (int i = 0; i < syns.length; i++)
    {
      try
      {
        PointerTargetNodeList ptnl = getPointerTargets(syns[i], type);
        String[] targets = ptnlToStrings(word, ptnl);
        if (targets == null)
          continue;
        for (int j = 0; j < targets.length; j++)
        {
          if (targets[j] != null)
            addLemma(targets[j], result);
        }
      }
      catch (JWNLException e)
      {
        throw new RiWordNetError(e);
      }
    }

    result.remove(word); // skip the original
    
    return toStrArr(result);
  }

  /*
   * Get a String[] from the pointer targets of <var>synset</var> of type
   * <var>type</var>,
   */
  private String[] getPointerTargetsAtIndex(String word, String pos, PointerType type, int index) 
  {
    try
    {
      Synset synset = getSynsetAtIndex(word, pos, index);

      return ptnlToStrings(word, getPointerTargets(synset, type));
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(this, e);
    }
  }

  /*
   * Get a String[] from the pointer <var>id</var> of type <var>type</var>.
   */
  private String[] getPointerTargetsAtId(int id, PointerType type) 
  {
    Synset synset = getSynsetAtId(id);

    try
    {
      return ptnlToStrings(null, getPointerTargets(synset, type));
    }
    catch (JWNLException e)
    {
      throw new RiWordNetError(e);
    }
  }

  public static void main(String[] args)
  {
    String result = "No WordNet in JS";
    if (RiTa.env() == RiTa.JAVA)
    {
      RiWordNet w = new RiWordNet("/WordNetx-3.1");
      String[] s = w.getAllSynsets("dog", "n");
      System.out.println(Arrays.asList(s));
//      int[] is = w.getSenseIds("dog", "n");
//      for (int i = 0; i < is.length; i++) {
//	System.out.println(i+"] "+is[i]);
//      }
      //System.out.println(Arrays.asList(w.getSynset("dog", "n", 1)));
      //System.out.println(Arrays.asList(w.getAllGlosses("dog", "n")));
      System.out.println(Arrays.asList(w.getGloss("dog", "n")));
      //System.out.println(Arrays.asList(w.getDescription("dog", "n")));
      System.out.println(Arrays.asList(w.getExamples("dog", "n")));

    }
    else
      System.out.println(result);
  }

}