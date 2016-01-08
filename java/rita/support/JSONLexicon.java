package rita.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import rita.RiLexicon;
import rita.RiTa;
import rita.RiTaException;

/**
 * Provides an implementation of a user-customizable Lexicon using CMU-style
 * pronunciation tags and Penn-style part-of-speech tags.
 * <p>
 * 
 * Note: this is a support class, public access is provided through
 * rita.RiLexicon.
 * <p>
 * The implementation also allows users to define their own addenda to be added
 * to the system addenda, overriding any existing elements in the system
 * addenda.
 */
public class JSONLexicon implements Constants {
  public static boolean USE_NIO = false;
  static boolean DBUG_CACHE = false;
  static int MAP_SIZE = 40000;

  // statics ====================================

  protected static JSONLexicon instance;
  public static boolean cacheEnabled = false;
  protected static Map featureCache;

  // members ====================================

  protected String dictionaryFile;
  protected HashMap<String, String> lexicalData;
  protected boolean loaded, lazyLoadLTS;
  protected LetterToSound letterToSound;

  public static JSONLexicon reload() {
    instance = null;
    return getInstance();
  }

  /**
   * Creates, loads and returns the singleton lexicon instance.
   */
  public static JSONLexicon getInstance() {
    if (!RiLexicon.enabled)
      return null;
    return getInstance(DEFAULT_LEXICON);
  }

  /**
   * Creates, loads & returns the singleton lexicon instance for a specific path.
   */
  protected static JSONLexicon getInstance(String pathToLexicon) {
    if (instance == null) {
      try {
	long start = System.currentTimeMillis();
	instance = new JSONLexicon(pathToLexicon);
	instance.load();

	int addenda = instance.getAddendaCount();

	if (!RiTa.SILENT)
	  System.out.println("[INFO] Loaded " + instance.size() + "(" + addenda
	      + ") lexicon in " + (System.currentTimeMillis() - start) + " ms");
		
      } catch (Throwable e) {
	
	  throw new RiTaException(e.getMessage());
      }
    }
    return instance;
  }

  // constructors ====================================
  /**
   * Constructs an unloaded instance of the lexicon.
   */
  protected JSONLexicon(String basename) {
    this.dictionaryFile = basename;
  }

  // methods ====================================

  /**
   * Returns the raw data (as a Map) used in the lexicon. Modifications to this
   * Map will be immediately reflected in the lexicon.
   */
  public HashMap<String, String> getLexicalData() {
    return lexicalData;
  }

  /**
   * Sets the raw data (a Map) used in the lexicon, replacing all default words
   * and features.
   */
  public void setLexicalData(HashMap<String, String> lexicalData) {
    this.lexicalData = lexicalData;
  }

  /**
   * Returns the number of user addenda items added to the lexicon
   */
  public int getAddendaCount() {
    return addendaCount;
  }

  protected int addendaCount = 0;

  /**
   * Determines if this lexicon is loaded.
   * 
   * @return <code>true</code> if the lexicon is loaded
   */
  public boolean isLoaded() {
    return loaded;
  }

  public void load() {
    
    String[] lines = loadJSON(this.dictionaryFile);

    if (lines == null || lines.length < 2)
      throw new RiTaException("Problem parsing RiLexicon data files");

    lexicalData = new LinkedHashMap<String, String>(MAP_SIZE);

    for (int i = 1; i < lines.length - 1; i++) // ignore JS prefix/suffix
    {
      String[] parts = lines[i].split(LEXICON_DELIM);
      if (parts == null || parts.length != 2)
	throw new RiTaException("Illegal entry: " + lines[i]);
      lexicalData.put(parts[0], parts[1].trim());
    }

    if (LOAD_USER_ADDENDA) // deprecate
      addAddendaEntries(DEFAULT_USER_ADDENDA_FILE, lexicalData);

    loaded = true;

    if (!lazyLoadLTS)
      getLTSEngine();
  }

  public static String[] loadJSON(String file) {
    
    if (file == null)
      throw new RiTaException("No dictionary path specified!");

    String data = readFile(file);

    if (data == null)
      throw new RiTaException("Unable to load lexicon from: " + file);

    // clean out the JSON formatting (TODO: optimize)
    String clean = data.replaceAll("['\\[\\]]", E).replaceAll(",", "|");

    // check the newline char used in the string (for #227)
    String newLine = clean.contains(BRN) ? BRN : BN;

    return clean.split("\\|?" + newLine);
  }

  public static String readFile(String filename) // load file into single string
  {
    try {
      
      StringBuilder builder = new StringBuilder();
      InputStream is = RiLexicon.class.getResourceAsStream(filename);
      if (is == null)
	throw new RiTaException("Unable to load lexicon: rita/java/"+filename);
      InputStreamReader isr = new InputStreamReader(is);
      Reader reader = new BufferedReader(isr);
      char[] buffer = new char[8192];
      int read;
      while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
	builder.append(buffer, 0, read);
      }
      isr.close();
      is.close();
      
      return builder.toString();
            
    } catch (Exception e) {
      
      throw new RiTaException(e);
    }
  }

  protected LetterToSound getLTSEngine() {
    if (letterToSound == null)
      letterToSound = LetterToSound.getInstance();
    return letterToSound;
  }

  protected int addToMap(InputStream is, Map lexicon) throws IOException {
    int num = 0;
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = reader.readLine();
    while (line != null) {
      if (!line.startsWith(CMUDICT_COMMENT)) {
	line = line.trim();
	if (line.length() > 0) {
	  parseAndAdd(lexicon, line);
	  num++;
	}
      }
      line = reader.readLine();
    }
    reader.close();
    reader = null;
    return num;
  }

  protected void addAddendaEntries(String fileName, Map compiledMap) {
    InputStream is = null;
    try {
      is = RiTa._openStream(null, fileName);
      if (is == null)
	throw new RiTaException("Null input stream for addenda file: "
	    + fileName);
    } catch (Throwable e) {
      // this is the default (when the user hasn't
      // provided an addenda file), just return...
      return;
    }

    try {
      this.addendaCount = addToMap(is, compiledMap);
      if (addendaCount > 0)
	if (!RiTa.SILENT)
	  System.out.println("[INFO] Loaded " + addendaCount
	      + " entries from user addenda file");
    } catch (Throwable e) {
      // System.out.println("[WARN] User-addenda file not"
      // + " in expected location: data/" + fileName);
      throw new RiTaException(e);
    }
  }

  /**
   * Creates a word from the given input line and add it to the lexicon. Returns
   * true if the word was not a duplicate, else false
   * 
   * @param lexicon
   *          the lexicon
   * @param line
   *          the input text
   */
  protected void parseAndAdd(Map lexicon, String line) {
    String[] parts = line.split(LEXICON_DELIM);
    if (parts == null || parts.length != 2)
      throw new RiTaException("Illegal entry: " + line);
    lexicon.put(parts[0], parts[1].trim());
  }

  /**
   * Gets the phoneme list for a given word, either via lookup or, if not found,
   * (and <code>useLTS</code> is true), generated via the letter-to-sound
   * engine, else null.
   */
  public String getPhonemes(String word, boolean useLTS) {
    String[] arr = getPhonemeArr(word, useLTS);
    return arr != null ? RiTa.join(arr, PHONEME_BOUNDARY) : E;
  }

  /**
   * Gets the phoneme list for a given word, either via lookup or, if not found,
   * (and <code>useLTS</code> is true), generated via the letter-to-sound
   * engine, else null.
   */
  public String[] getPhonemeArr(String word, boolean useLTS) {
    String s = null;
    Map<String, String> m = getFeatures(word);

    if (m != null) // check the lexicon first
      s = m.get(PHONEMES);

    if (s == null && useLTS)
      s = stripStressesAndSyllables(getLTSEngine().getPhones(word));

    // s = s.replaceAll(" ", PHONEME_BOUNDARY);

    // System.out.println(s);

    return (s != null) ? s.split(PHONEME_BOUNDARY) : null;
  }

  String[] stripStresses(String[] phonesAndStresses) // not used
  {
    // System.out.println("RiTaLexicon.stripStresses("+RiTa.asList(phonesAndStresses)+")");
    for (int i = 0; i < phonesAndStresses.length; i++) {
      String syl = phonesAndStresses[i];
      char c = syl.charAt(syl.length() - 1);
      if (c == STRESSED) {
	syl = syl.substring(0, syl.length() - 1);
	phonesAndStresses[i] = syl;
      }
    }
    return phonesAndStresses;
  }

  String stripStressesAndSyllables(String phonesAndStresses) {
    char pb = PHONEME_BOUNDARY.charAt(0);
    StringBuilder phonesOnly = new StringBuilder();

    for (int i = 0; i < phonesAndStresses.length(); i++) {
      char c = phonesAndStresses.charAt(i);
      if (c == ' ')
	phonesOnly.append(pb);
      else if (c != STRESSED)
	phonesOnly.append(c);
    }
    return phonesOnly.toString();
  }

  /**
   * Removes a word from the lexicon.
   * 
   * @param word
   *          the word to remove
   * @param partOfSpeech
   *          the part of speech
   */
  public void removeAddendum(String word, String partOfSpeech) {
    lexicalData.remove(word);// + fixPartOfSpeech(partOfSpeech));
  }

  public int size() {
    if (lexicalData == null) {
      System.err.println("NULL compiled Map!");
      return -1;
    }
    return this.lexicalData.size();
  }

  public Set getWords() {
    return lexicalData.keySet();
  }

  /**
   * Returns the raw lexicon entry or null if not found
   */
  public String lookupRaw(String word) {
    return lexicalData.get(word.toLowerCase());
  }

  public boolean contains(String word) {
    return lexicalData.get(word) != null;
  }

  public Iterator<String> iterator() {
    return lexicalData.keySet().iterator();
  }

  RandomIterator randomIterator = null;

  public Iterator<String> randomIterator() {
    if (randomIterator == null)
      randomIterator = new RandomIterator(lexicalData.keySet());
    else
      randomIterator.reset();
    return randomIterator;
  }

  public Iterator<String> randomPosIterator(String pos) {
    return new RandomIterator(getWordsWithPos(pos));
  }

  public Iterator<String> posIterator(String pos) {
    return getWordsWithPos(pos).iterator();
  }

  public Set<String> keySet() {
    return lexicalData.keySet();
  }

  //
  // public Set<String> getWords(String regex)
  // {
  // return getWords(Pattern.compile(regex));
  // }

  public Set<String> getWords(Pattern regex) {
    Set<String> s = new TreeSet<String>();
    for (Iterator<String> iter = iterator(); iter.hasNext();) {
      String str = iter.next();
      if (regex.matcher(str).matches())
	s.add(str);
    }
    return s;
  }

  /** Returns all words where 'pos' is the first (or only) tag listed */
  public Set<String> getWordsWithPos(String pos) {
    
    // System.out.println("JSONLexicon.getWordsWithPos("+pos+")");

    if (!RiPos.isPennTag(pos)) {
      throw new RiTaException("Pos '"+ pos + "' is not a known part-of-speech tag." + 
	  " Check the list at http://rednoise.org/rita/reference/PennTags.html");
    }

    Set<String> s = new TreeSet<String>();
    String posSpc = pos + " ";
    for (Iterator<String> iter = iterator(); iter.hasNext();) {
      String word = iter.next();
      String poslist = getPosStr(word);
      if (poslist.startsWith(posSpc) || poslist.equals(pos))
	s.add(word);
    }
    return s;
  }

  protected void addToFeatureCache(String word, Map m) {
    if (featureCache == null)
      featureCache = new HashMap<String, String>();

    if (DBUG_CACHE)
      System.out.println("Caching " + word + ": " + m);

    featureCache.put(word, m);
  }

  protected Map checkFeatureCache(String word) {
    if (featureCache == null)
      return null;

    Map<String, String> m = (Map<String, String>) featureCache.get(word);

    if (DBUG_CACHE && m != null)
      System.out.println("Using cache for: " + word);

    return m;
  }

  public Map<String, String> getFeatures(String word) {
    Map<String, String> m = null;

    if (cacheEnabled) {
      m = checkFeatureCache(word);
      if (m != null)
	return m; // return cache hit
    }

    String dataStr = lookupRaw(word);
    if (dataStr == null)
      return new HashMap<String, String>();

    String[] data = dataStr.split(DATA_DELIM);
    if (data == null || data.length != 2)
      throw new RiTaException("Invalid lexicon entry: " + word + " -> '"
	  + dataStr + "'");

    StringBuilder phones = new StringBuilder();
    StringBuilder stresses = new StringBuilder();
    StringBuilder syllables = new StringBuilder();
    String[] phonesAndStresses = data[0].split(SP);
    for (int i = 0; i < phonesAndStresses.length; i++) {
      String syl = phonesAndStresses[i];
      boolean stressed = false;
      for (int j = 0; j < syl.length(); j++) {
	char c = syl.charAt(j);
	if (c == '1') {
	  stressed = true;
	} else {
	  // add phones and syls
	  phones.append(c);
	  syllables.append(c);
	}
      }

      // add the stress for each syllable
      stresses.append(stressed ? STRESSED : UNSTRESSED);

      if (i < phonesAndStresses.length - 1) {
	phones.append(PHONEME_BOUNDARY);
	syllables.append(SYLLABLE_BOUNDARY);
	stresses.append(SYLLABLE_BOUNDARY);
      }
    }

    m = new HashMap<String, String>(8); // create feature-map
    m.put(SYLLABLES, syllables.toString());
    m.put(POSLIST, data[1].trim());
    m.put(PHONEMES, phones.toString());
    m.put(STRESSES, stresses.toString());

    if (cacheEnabled) // add to cache
      addToFeatureCache(word, m);

    return m;
  }

  public void addAddendum(String word, String pos, String[] phones) {
    // lexMap.put(word, phones.joi)
    throw new RiTaException("addAddendum not implemented...");
  }

  /**
   * Determines if the currentPhone represents a new syllable boundary.
   * 
   * @param syllablePhones
   *          the phones in the current syllable so far
   * @param wordPhones
   *          the phones for the whole word
   * @param currentWordPhone
   *          the word phone in question
   * 
   * @return <code>true</code> if the word phone in question is on a syllable
   *         boundary; otherwise <code>false</code>.
   */
  public boolean isSyllableBoundary(List syllablePhones, String[] wordPhones,
      int currentWordPhone) {
    boolean ib = false;
    if (currentWordPhone >= wordPhones.length) {
      ib = true;
//    } else if (Phoneme.isSilence(wordPhones[currentWordPhone])) {
//      ib = true;
    } else if (!Phoneme.hasVowel(wordPhones, currentWordPhone)) { // rest ofword
      ib = false;
    } else if (!Phoneme.hasVowel(syllablePhones)) { // current syllable
      ib = false;
    } else if (Phoneme.isVowel(wordPhones[currentWordPhone])) {
      ib = true;
    } else if (currentWordPhone == (wordPhones.length - 1)) {
      ib = false;
    } else {
      int p, n, nn;
      p = Phoneme.getSonority((String) syllablePhones.get(syllablePhones.size() - 1));
      n = Phoneme.getSonority(wordPhones[currentWordPhone]);
      nn = Phoneme.getSonority(wordPhones[currentWordPhone + 1]);
      if ((p <= n) && (n <= nn)) {
	ib = true;
      } else {
	ib = false;
      }
    }
    // System.out.println("RiTaLexicon.isSyllableBoundary("+
    // syllablePhones+", "+RiTa.asList(wordPhones)+", "+currentWordPhone+") -> "+ib);
    return ib;
  }

  public static boolean isCaching() {
    return cacheEnabled;
  }

  public void preloadFeatures() {
    cacheEnabled = true;
    long start = System.currentTimeMillis();
    for (Iterator<String> it = iterator(); it.hasNext();)
      getFeatures(it.next());
    if (!RiTa.SILENT)
      System.out.println("[INFO] Created and cached features... in "
	  + (System.currentTimeMillis() - start) + "ms");
  }

  public String getPosStr(String word) {
    String data = lookupRaw(word);
    if (data == null)
      return E;
    return data.split(DATA_DELIM)[1];
  }

  public String getBestPos(String word) {
    String[] posArr = getPosArr(word);
    return posArr.length > 0 ? posArr[0] : E;
  }

  public String[] getPosArr(String word) {
    if (word.contains(SP))
      throw new RiTaException("Only single words allowed");
    String pl = getPosStr(word);
    if (pl == null)
      throw new RuntimeException("null pl");
    return (pl == null || pl.length() < 1) ? EMPTY : pl.split(SP);
  }

  public int addWord(String s, String t, String u) {
    lexicalData.put(s, t + "|" + u);
    return lexicalData.size();
  }

  public static void testTiming(int numTests) {
    RiTa.SILENT = true;
    long ts = System.currentTimeMillis();
    USE_NIO = false;
    for (int i = 0; i < numTests; i++) {
      System.out.print(".");
      new JSONLexicon(DEFAULT_LEXICON).load();
    }
    System.out.println("\nAVG TIME(NIO=" + USE_NIO + ")="
	+ (System.currentTimeMillis() - ts) / (float) numTests);
    ts = System.currentTimeMillis();
    USE_NIO = true;
    for (int i = 0; i < numTests; i++) {
      System.out.print(".");
      new JSONLexicon(DEFAULT_LEXICON).load();
    }
    System.out.println("\nAVG TIME(NIO=" + USE_NIO + ")="
	+ (System.currentTimeMillis() - ts) / (float) numTests);
  }

  public static void main(String[] args) {
    // testTiming(50); if (1==1) return;
    JSONLexicon lex = JSONLexicon.getInstance();
    System.out.println(lex.getWordsWithPos("vbg"));
    if (1 == 1)
      return;
    String test = "swimming";
    System.out.println(lex.lookupRaw(test));
    System.out.println(lex.getPosStr(test));
    System.out.println(lex.getPhonemes(test, true));
    System.out.println(RiTa.asList(lex.getPhonemeArr(test, true)) + "\n");
    test = "laggin"; // WITH LTS
    System.out.println(lex.lookupRaw(test));
    System.out.println(lex.getPhonemes(test, true));
    System.out.println(RiTa.asList(lex.getPhonemeArr(test, true)));
  }

}// end
