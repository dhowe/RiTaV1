package rita;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.support.*;

public class RiString implements FeaturedIF, Constants, Comparable<RiString> {

  static boolean DBUG_CACHED_FEATURES = false;
  
  static List<String> trackedFeatures = Arrays.asList(new String[] { 
      TOKENS, STRESSES, PHONEMES, SYLLABLES, POS,  TEXT
  });

  static {
    RiTa.init();
  }

  protected String delegate;
  protected HashMap<String, String> features;
  
  // convenience fields, in case we use this object for rendering
  public int x,y,z;

  public RiString(String string) {
    this.delegate = string;
  }

  // =======================================================

  @Override
  public String toString() {
    return "[" + delegate + "]";
  }

  public static RiString[] fromStrings(String[] s) {
    RiString[] result = new RiString[s.length];
    for (int i = 0; i < s.length; i++)
      result[i] = new RiString(s[i]);
    return result;
  }

  public String text() {
    return this.delegate.toString();
  }

  public String subSequence(int start, int end) {
    
    start = Math.min(start < 0 ? delegate.length() + start : start, length() - 1);
    end = Math.min(end < 0 ? delegate.length() + end : end, length() - 1);

    if (end < start) {
      int k = start;
      start = end; // swap
      end = k;
    }

    return (String) delegate.subSequence(start, end);
  }

  public RiString analyze() {

    LetterToSound lts = null;
    JSONLexicon lex = JSONLexicon.getInstance();

    String[] words = RiTa.tokenize(delegate.toLowerCase());

    if (features == null || features.size() < 1)
      initFeatureMap();

    String phonemes = E, syllables = E, stresses = E, ipaPhones = null;

    for (int i = 0; i < words.length; i++) {

      boolean useRaw = false;

      String phones = null;

      if (lex != null)
	phones = RiLexicon.getRawPhones(lex, words[i]); // fetch phones from lex

      if (phones == null || phones.length() < 1) {

	if (lts == null) lts = LetterToSound.getInstance();

	phones = lts.getPhones(words[i]); // next try LTS

	if (phones == null || phones.length() < 1) {

	  phones = words[i]; // still nothing, use the raw chars (should be punct)
	  useRaw = true;
	} 
	else if (!RiTa.SILENT && !RiTa.SILENT_LTS && RiLexicon.enabled
	    && words[i].matches("[a-zA-Z]+")) 
	{
	  System.out.println("[RiTa] Used LTS-rules for '" + words[i] + "'");
	}
      }

      //System.out.println(i+") '"+ phones+"'");

      phonemes += ((RiTa.PHONEME_TYPE == IPA) ? Phoneme.arpaToIPA(phones): 
	phones.replaceAll("[0-2]", E).replace(SP, DASH)) + SP;
      
      syllables += phones.replaceAll("[0-2]", E).replace(SP, FS) + SP;

      if (!useRaw) {

	// we have phones, so parse out the stresses

	String[] stressyls = phones.split(SP);
	for (int j = 0; j < stressyls.length; j++) {

	  if (stressyls[j].length() < 1)
	    continue;

	  stresses += (stressyls[j].indexOf(RiTa.STRESSED) > -1) ? 
	      RiTa.STRESSED : RiTa.UNSTRESSED;

	  if (j < stressyls.length - 1)
	    stresses += FS;
	}
      } else {

	// no phones, just use raw word (punct)
	stresses += words[i];
      }

      if (!stresses.endsWith(SP)) stresses += SP;
    }
      

    this.features.put(TOKENS, RiTa.join(words));
    this.features.put(STRESSES, stresses.trim());
    this.features.put(PHONEMES, phonemes.trim().replaceAll("\\s+", SP));
    this.features.put(SYLLABLES, syllables.trim().replaceAll("\\s+", SP));
    this.features.put(POS, RiTa.join(RiTa.getPosTags(this.delegate)));

    return this;
  }
  
  void initFeatureMap() {
    if (this.features == null)
      this.features = new HashMap<String, String>();
    else {
      clearFeatures();
    }

    this.features.put(TEXT, delegate);
  }

  private void clearFeatures() {
    this.features.remove(TOKENS);
    this.features.remove(STRESSES);
    this.features.remove(PHONEMES);
    this.features.remove(SYLLABLES);
    this.features.remove(POS);
  }

  public String charAt(int charIdx) {
    charIdx = Math.min(charIdx < 0 ? delegate.length() + charIdx : charIdx,
	length() - 1);
    return Character.toString(this.delegate.charAt(charIdx));
  }

  public RiString concat(String cs) {
    text(delegate.concat(cs));
    return this;
  }

  public boolean endsWith(String suffix) {
    return delegate.endsWith(suffix);
  }

  public boolean equalsIgnoreCase(String cs) {
    return cs != null && delegate.toLowerCase().equals(cs.toLowerCase());
  }

  public String get(String featureName) {
    
    if (features == null)
      this.initFeatureMap();
    
    String s = features.get(featureName);
    if (s == null && (trackedFeatures.indexOf(featureName) > -1))
    {
      this.analyze();
      s = features.get(featureName);
    }
    
    return s;
  }

  public HashMap<String, String> features() {

    if (features == null)
      this.analyze();

    return features;
  }

  public int indexOf(String s) {
    return delegate.indexOf(s);
  }

  public int indexOf(String s, int startIdx) {
    return delegate.indexOf(s, startIdx);
  }

  public int lastIndexOf(String s, int startIdx) {
    return delegate.indexOf(s, startIdx);
  }

  public int lastIndexOf(String s) {
    return delegate.lastIndexOf(s);
  }

  public int length() {
    return delegate.length();
  }

  /**
   * Inserts <code>newWord</code> at <code>wordIdx</code> and shifts each
   * subsequent word accordingly. Returns true if the replace was successful, or
   * false if the index does not exist.
   */
  public RiString insertWord(int wordIdx, String newWord) {
    if (newWord == null || newWord.length() < 1 || newWord.matches(WS))
      return this;

    String[] words = words();

    wordIdx = Math.min(wordIdx < 0 ? (words.length + wordIdx) % words.length
	: wordIdx, words.length);

    String[] newArr = new String[words.length + 1];
    System.arraycopy(words, 0, newArr, 0, words.length);

    newArr[wordIdx] = newWord;
    for (int i = wordIdx; i < words.length; i++)
      newArr[i + 1] = words[i];

    return text(RiTa.untokenize(newArr));
  }

  public String[] pos() {

    return this.pos(false);
  }

  /**
   * Returns the part-of-speech at <code>wordIdx</code> using the default
   * WordTokenizer & PosParser...
   */
  public String getPosAt(int wordIdx, boolean useWordNetTags) {
    String[] pos = pos(useWordNetTags);
    wordIdx = Math.min(wordIdx < 0 ? (pos.length + wordIdx) % pos.length
	: wordIdx, pos.length); // -index?
    return pos == null ? E : pos[wordIdx];
  }

  /**
   * Returns an array of part-of-speech tags, one per word, using the default
   * WordTokenizer & PosParser...
   */
  public String[] pos(boolean useWordNetTags) {

    if (hasFeature(POS)) {

      if (DBUG_CACHED_FEATURES)
	System.out.println("Using cached feature: " + getFeature(POS) + " for "
	    + text());

      return getFeature(POS).split(WORD_BOUNDARY);
    }

    String[] words = RiTa.tokenize(delegate.toString());
    String[] tag = RiTa.getPosTags(words);

    for (int i = 0; i < tag.length; i++) {

      if (tag[i] == null)
	throw new RiTaException("Unable to parse pos for word: " + words[i]);

      if (useWordNetTags)
	tag[i] = PosTagger.toWordNet(tag[i]);
    }
    return tag;
  }

  /**
   * Returns the part-of-speech at <code>wordIdx</code> using the default
   * WordTokenizer & PosParser...
   */
  public String posAt(int wordIdx) {
    return posAt(wordIdx, false);
  }

  public String posAt(int wordIdx, boolean useWordNetTags) { // NAPI

    String[] words = words();
    wordIdx = Math.min(wordIdx < 0 ? words.length + wordIdx : wordIdx,
	words.length - 1); // -index?
    // System.out.println(wordIdx+"/"+words.length);
    String[] pos = pos(useWordNetTags);

    // TODO: a bug - length of words() is diff (longer, if punctuation) than
    // length of pos()
    wordIdx = Math.min(pos.length - 1, wordIdx); // tmp hack-fix

    // System.out.println(wordIdx+"|"+words.length + " = "+words[wordIdx]);

    return (pos == null) ? E : pos[wordIdx];
  }

  public RiString replaceChar(int idx, char replaceWith) {
    return this.replaceChar(idx, Character.toString(replaceWith));
  }

  /**
   * Replaces the character at 'idx' with 'replaceWith'. If the specified 'idx'
   * is less than xero, or beyond the length of the current text, there will be
   * no effect. Returns true if the replacement was made
   */
  public RiString replaceChar(int idx, String replaceWith) {
    idx = Math.min(idx < 0 ? delegate.length() + idx : idx, length() - 1);

    String s = text();
    String front = s.substring(0, idx);
    String back = s.substring(idx + 1);

    if (replaceWith != null)
      front += replaceWith;

    front += back;

    return text(front);
  }

  /**
   * Replaces the word at <code>wordIdx</code> with <code>newWord</code>.
   * Returns true if the replace was successful, or false if the index does not
   * exist.
   */
  public RiString replaceWord(int wordIdx, String newWord) {

    String[] words = words();
    wordIdx = Math.min(wordIdx < 0 ? words.length + wordIdx : wordIdx,
	words.length - 1);
    words[wordIdx] = newWord;
    return text(RiTa.untokenize(words));
  }

  public RiString removeChar(int idx) {
    return replaceChar(idx, E);
  }

  public RiString replaceFirst(String regex, String replacement) {
    return text(this.delegate.replaceFirst(regex, replacement));
  }

  public RiString replaceAll(String regex, String replacement) {
    return text(this.delegate.replaceAll(regex, replacement));
  }

  public String slice(int start, int end) {
    start = Math.min(start < 0 ? delegate.length() + start : start,
	length() - 1);
    end = Math.min(end < 0 ? delegate.length() + end : end, length());
    if (end < start) {
      int tmp = start;
      start = end; // swap
      end = tmp;
    }
    return delegate.substring(start, end);
  }

  /**
   * Tokenizes the RiString into words, then checks for features with the same
   * number of elements as the resulting array and adds the appropriate feature
   * to each newly created RiString<br>
   * Example: <br>
   * 'only a handful of responses' / {chunk=noun-phrase}, {pos=rb dt nn in nns}
   * ->
   * <ul>
   * <li>'only' / {pos=rb}
   * <li>'a' / {pos=dt}
   * <li>'handful' / {pos=nn}
   * <li>'of' / {pos=in}
   * <li>'responses' / {pos=nns}
   * </ul>
   * 
   * @see Constants#WORD_BOUNDARY
   */
  public RiString[] split() {
    return _doSplit(RiTa.tokenize(delegate));
  }

  /**
   * Splits the RiString as per <code>String.split(regex)</code>, then checks
   * for any features with the same number of elements as the resulting String[]
   * and adds the appropriate feature to each individual RiString.<br>
   * Example: <br>
   * 'only a handful of responses' / {chunk=noun-phrase}, {pos=rb dt nn in nns}
   * ->
   * <ul>
   * <li>'only' / {pos=rb}
   * <li>'a' / {pos=dt}
   * <li>'handful' / {pos=nn}
   * <li>'of' / {pos=in}
   * <li>'responses' / {pos=nns}
   * </ul>
   * 
   * @see Constants#WORD_BOUNDARY
   */
  public RiString[] split(String regex) {
    return _doSplit(delegate.split(regex));
  }

  private RiString[] _doSplit(String[] s) {

    List<String> l = new ArrayList<String>();
    for (int i = 0; i < s.length; i++) {
      if (s[i] != null && s[i].length() > 0)
	l.add(s[i]);
    }
    s = RiTa.strArr(l);

    RiString[] fs = new RiString[s.length];
    for (int i = 0; i < fs.length; i++)
      fs[i] = new RiString(s[i]);

    for (int i = 0; i < fs.length; i++) {
      for (Iterator<String> iter = getAvailableFeatures().iterator(); iter
	  .hasNext();) {

	String fkey = iter.next();
	String feature = getFeature(fkey);

	// only add per-word features if they match in #
	if (feature.indexOf(WORD_BOUNDARY) > -1) {

	  String[] wordFeatures = feature.split(WORD_BOUNDARY);
	  if (wordFeatures.length == fs.length)
	    fs[i].set(fkey, wordFeatures[i]);
	}
      }
    }

    return fs;
  }

  public boolean startsWith(String cs) {

    return delegate.startsWith(cs);
  }

  public String substring(int start, int end) {
    start = Math.min(start < 0 ? delegate.length() + start : start,
	length() - 1);
    end = Math.min(end < 0 ? delegate.length() + end : end, length());
    if (end < start) {
      int k = start;
      start = end; // swap
      end = k;
    }
    return delegate.substring(start, end);
  }

  public String substr(int start, int end) {
    start = Math.min(start < 0 ? delegate.length() + start : start,
	length() - 1);
    end = Math.min(end, length() - start);
    // end = Math.min(end < 0 ? delegate.length() + end : end, length());
    /*
     * if (end < start){ int k = start; start = end; // swap end = k; }
     */
    // System.out.println("RiString.substr("+start+","+(start+end)+")");
    return delegate.substring(start, start + end);
  }

  public char[] toCharArray() {
    return delegate.toCharArray();
  }

  public RiString toLowerCase() {
    return text(delegate.toLowerCase());
  }

  public RiString toUpperCase() {
    return text(delegate.toUpperCase());
  }

  public boolean equals(String obj) {
    return obj != null && obj.equals(text());
  }

  public RiString trim() {
    return text(this.delegate.toString().trim());
  }

  /**
   * Returns the word at <code>wordIdx</code> using the default WordTokenizer.
   */
  public String wordAt(int wordIdx) {

    String[] words = RiTa.tokenize(delegate.toString());
    if (words == null || words.length < 1)
      return E;
    wordIdx = wordIdx < 0 ? (words.length + wordIdx) : wordIdx;
    wordIdx = Math.min(wordIdx, words.length - 1);
    return words[wordIdx];
  }

  public int wordCount() {
    return words().length;
  }

  public String[] words() {
    return RiTa.tokenize(delegate);
  }

  public RiString text(String s) {
    this.delegate = s;
    initFeatureMap();
    return this;
  }

  public String[] match(String s) {
    return match(s, 0);
  }

  public String[] match(String s, int flags) {
    Matcher m = Pattern.compile(s, flags).matcher(this.delegate);
    List<String> matches = new ArrayList<String>();
    while (m.find()) {
      matches.add(m.group(0));
    }
    return RiTa.strArr(matches);
  }

  public RiString concat(RiString cs) {
    return text(delegate + cs.text());
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public boolean equals(Object o) {
    if (!(o instanceof RiString))
      return false;
    RiString rs = (RiString) o;
    return (delegate == null && rs.delegate == null)
	|| (delegate != null && delegate.equals(rs.delegate));
  }

  public RiString copy() {
    RiString rs = new RiString(delegate);

    if (features != null) {

      Map<String, String> feats = features;
      rs.features = new HashMap<String, String>();
      for (Iterator<String> it = feats.keySet().iterator(); it.hasNext();) {
	String key = (String) it.next();
	rs.features.put(key, feats.get(key));
      }
    }
    return rs;
  }

  // For FeaturedIF

  public String getFeature(String name) {
    return (String) features().get(name);
  }

  public RiString set(String name, String value) {
    if (features == null)
      initFeatureMap();

    features.put(name, value); // clobber?

    return this;
  }

  public void setFeature(String name, String value) {

    this.set(name, value);
  }

  public RiString insertChar(int idx, char c) {
    return insertAt(idx, Character.toString(c));
  }

  public RiString insertAt(int charIdx, String s) {
    charIdx = Math.min(charIdx < 0 ? delegate.length() + charIdx : charIdx,
	length() - 1);
    return text(delegate.substring(0, charIdx) + s
	+ delegate.substring(charIdx + 1));
  }

  public RiString removeWord(int idx) {
    return replaceWord(idx, E);
  }

  public Set<String> getAvailableFeatures() {
    return features().keySet();
  }

  public boolean hasFeature(String name) {

    return features != null && features.containsKey(name);
  }

  public String substring(int start) {
    int newstart = Math.min(start < 0 ? delegate.length() + start : start, length() - 1);
    return this.delegate.substring(newstart);
  }

  static final Map<String, String[]> Phones = new HashMap<String, String[]>();

  static {
    Phones.put("consonants", new String[] { "b", "ch", "d", "dh", "f", "g",
	"hh", "jh", "k", "l", "m", "n", "ng", "p", "r", "s", "sh", "t", "th",
	"v", "w", "y", "z", "zh" });

    Phones.put("vowels", new String[] { "aa", "ae", "ah", "ao", "aw", "ax",
	"ay", "eh", "er", "ey", "ih", "iy", "ow", "oy", "uh", "uw" });

    Phones.put("onsets", new String[] { "p", "t", "k", "b", "d", "g", "f", "v",
	"th", "dh", "s", "z", "sh", "ch", "jh", "m", "n", "r", "l", "hh", "w",
	"y", "p r", "t r", "k r", "b r", "d r", "g r", "f r", "th r", "sh r",
	"p l", "k l", "b l", "g l", "f l", "s l", "t w", "k w", "d w", "s w",
	"s p", "s t", "s k", "s f", "s m", "s n", "g w", "sh w", "s p r",
	"s p l", "s t r", "s k r", "s k w", "s k l", "th w", "zh", "p y",
	"k y", "b y", "f y", "hh y", "v y", "th y", "m y", "s p y", "s k y",
	"g y", "hh w", "" });

    Phones.put("digits", new String[] { "z-ih-r-ow", "w-ah-n", "t-uw",
	"th-r-iy", "f-ao-r", "f-ay-v", "s-ih-k-s", "s-eh1-v-ax-n", "ey-t",
	"n-ih-n" });
  }

  /*
   * Takes a syllabification and turns it into a string of phonemes, delimited
   * with dashes, and spaces between syllables
   * 
   * @private
   */
  public static String stringify(String[][][] syllables) {

    List<String> ret = new ArrayList<String>();

    for (int i = 0; i < syllables.length; i++) {

      String[][] syl = syllables[i];
      String stress = syl[0][0];
      String[] onset = syl[1];
      String[] nucleus = syl[2];
      String[] coda = syl[3];

      if (stress != null && nucleus.length > 0) // dch
	nucleus[0] += (E + stress);

      List<String> data = new ArrayList<String>();
      for (int j = 0; j < onset.length; j++)
	data.add(onset[j]);

      for (int j = 0; j < nucleus.length; j++)
	data.add(nucleus[j]);

      for (int j = 0; j < coda.length; j++)
	data.add(coda[j]);

      ret.add(RiTa.join(data, "-"));
    }

    return RiTa.join(ret, SP);
  }

  static boolean inArray(String[] array, String val) {
    boolean found = false;
    if (array != null) {
      for (int i = 0; i < array.length; i++) {
	if (array[i] != null && array[i].equals(val)) {
	  found = true;
	  break;
	}
      }
    }
    return found;
  }

  static String last(String word) { // last char of string

    if (word == null || word.length() < 1)
      return E;
    return E + word.charAt(word.length() - 1);
  }

  static char lastChar(String word) { // last char of string

    if (word == null || word.length() < 1)
      return Character.UNASSIGNED;
    return word.charAt(word.length() - 1);
  }

  public static void dispose(RiString rs) {
    if (rs != null && rs.features != null)
      rs.features.clear();
  }

  public int compareTo(RiString o) {

    return delegate.compareTo(o.delegate);
  }

  public static void main(String[] args) {
   // RiLexicon.enabled = false;
    //RiTa.PHONEME_TYPE = RiTa.IPA;
    RiString ri = new RiString("The laggin dragon");
    ri.analyze();
    System.out.println(ri.features());
  }

}// end
