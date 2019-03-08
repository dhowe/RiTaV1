package rita;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.support.*;

/**
 * A set of static properties and utility functions for the package
 */
public class RiTa implements Constants {

  public final static String VERSION = "##version##";

  /** For tokenization, Can't -> Can not, etc. */
  public static boolean SPLIT_CONTRACTIONS = false;

  /** For Phonemization: ARPA or IPA */
  public static int PHONEME_TYPE = ARPA;

  /** Stops all RiTa output to the console */
  public static boolean SILENT = false;

  /** Stops all output from the LTS-engine to the console */
  public static boolean SILENT_LTS = false;

  public static final int JS = 1, NODE = 2, JAVA = 4, ANDROID = 8;

  public static boolean callbacksDisabled = false;

  public static Object context = null;

  private static boolean INITD = false;

  private static Pattern CHOMP, PUNC, PUNCT, QUOTES, SQUOTES, APOS; // USE
  // Constants

  static {
    if (!INITD)
      RiTa.init();
  }

  // METHODS ///////////////////////////////////////////////////////////

  public static void start(Object theContext) {
    RiTa.context = theContext;
  }

  public static String stem(String s) {
    return Stemmer.getInstance().stem(s);
  }

  public static String stem(String s, String stemmerType) {
    return Stemmer.getInstance(stemmerType).stem(s);
  }

  public static String conjugate(String s, Map args) {
    return getConjugator().handleArgs(args).conjugate(s);
  }

  public static String getPastParticiple(String s) {
    return getConjugator().getPastParticiple(s);
  }

  public static String getPresentParticiple(String s) {
    return getConjugator().getPresentParticiple(s);
  }

  public static String getPhonemes(String s) {
    return getFeature(s, PHONEMES);
  }

  public static String getPhonemes(String[] s) {
    return getFeature(s, PHONEMES);
  }

  public static String getStresses(String s) {
    return getFeature(s, STRESSES);
  }

  public static String getStresses(String[] s) {
    return getFeature(s, STRESSES);
  }

  public static String getSyllables(String s) {
    return getFeature(s, SYLLABLES);
  }

  public static String getSyllables(String[] s) {
    return getFeature(s, SYLLABLES);
  }

  public static String[] getPosTags(String[] words) {
    return PosTagger.getInstance().tag(words);
  }

  public static String[] getPosTags(String s) {
    return PosTagger.getInstance().tag(tokenize(s));
  }

  public static String getPosTagsInline(String[] words) {
    return PosTagger.getInstance().tagInline(words);
  }

  public static String getPosTagsInline(String s) {
    return PosTagger.getInstance().tagInline(s);
  }

  protected static long millisOffset = System.currentTimeMillis();

  protected static Conjugator conjugator;

  protected static Conjugator getConjugator() {
    if (conjugator == null)
      conjugator = new Conjugator();
    return conjugator;
  }

  public static String[] getPosTags(String[] words, boolean useWordnetTags) {

    return useWordnetTags ? PosTagger.getInstance().tagForWordNet(words)
	: PosTagger.getInstance().tag(words);
  }

  public static String[] getPosTags(String s, boolean useWordnetTags) {

    return getPosTags(tokenize(s), useWordnetTags);
  }

  public HashMap features(String text) {
    return new RiString(text).features();
  }

  public static int env() {

    return System.getProperty("java.vm.name").equals("Dalvik") ? ANDROID : JAVA;
  }

  public static String trim(String s) {

    return s.trim();
  }

  // RiLexicon delegates ====================================================

  public static void addWord(String word, String pronunciation,
      String partsOfSpeech) {
    getLexicon().addWord(word, pronunciation, partsOfSpeech);
  }

  public static String getBestPos(String word) { // niapi, for tests
    return getLexicon().lexImpl.getBestPos(word);
  }

  public static void reload() {
    getLexicon().reload();
  }

  public static void clear() {
    getLexicon().clear();
  }

  public static boolean containsWord(String word) {
    return getLexicon().containsWord(word);
  }

  public static HashMap<String, String> lexicalData() {
    return getLexicon().lexicalData();
  }

  public static void lexicalData(HashMap m) {
    getLexicon().lexicalData(m);
  }

  public static String randomWordByLength(String pos, int targetLength) {
    return getLexicon().randomWordByLength(pos, targetLength);
  }

  public static String randomWordByLength(int targetLength) {
    return getLexicon().randomWordByLength(targetLength);
  }

  public static String getRawPhones(String word) {
    return getLexicon().getRawPhones(word);
  }

  public static String getRawPhones(String word, boolean useLTS) {
    return getLexicon().getRawPhones(word, useLTS);
  }

  public static String[] words() {
    return getLexicon().words();
  }

  public static String[] words(String regex) {
    return getLexicon().words(regex);
  }

  public static String[] words(boolean shuffled) {
    return getLexicon().words(shuffled);
  }

  public static String[] words(String regex, boolean sorted) {
    return getLexicon().words(regex, sorted);
  }

  public static String[] words(Pattern regex, boolean sorted) {
    return getLexicon().words(regex, sorted);
  }

  public static int size() { // niapi
    return getLexicon().size();
  }

  public static String[] rhymes(String input) {
    return getLexicon().rhymes(input);
  }

  public static String[] alliterations(String input) {
    return getLexicon().alliterations(input);
  }

  public static String[] alliterations(String input, int minLength) {
    return getLexicon().alliterations(input, minLength);
  }

  public static String randomWord() {
    return getLexicon().randomWord();
  }

  public static String randomWord(String pos) {
    return getLexicon().randomWord(pos);
  }

  public static String randomWord(int syllableCount) {
    return getLexicon().randomWord(syllableCount);
  }

  public static String randomWord(String pos, int syllableCount) {
    return getLexicon().randomWord(pos, syllableCount);
  }

  public static boolean isAlliteration(String wordA, String wordB) {
    return getLexicon().isAlliteration(wordA, wordB);
  }

  public static boolean isAdverb(String s) {
    return getLexicon().isAdverb(s);
  }

  public static boolean isNoun(String s) {
    return getLexicon().isNoun(s);
  }

  public static boolean isVerb(String s) {
    return getLexicon().isVerb(s);
  }

  public static boolean isAdjective(String s) {
    return getLexicon().isAdjective(s);
  }

  public static boolean isRhyme(String wordA, String wordB) {
    return getLexicon().isRhyme(wordA, wordB);
  }

  public static boolean isRhyme(String wordA, String wordB, boolean useLTS) {
    return getLexicon().isRhyme(wordA, wordB, useLTS);
  }

  public static String[] similarByLetter(String input) {
    return getLexicon().similarByLetter(input);
  }

  public static String[] similarBySound(String input) {
    return getLexicon().similarBySound(input);
  }

  public static String[] similarBySoundAndLetter(String input) {
    return getLexicon().similarBySoundAndLetter(input);
  }

  public static String[] substrings(String input) {
    return getLexicon().substrings(input);
  }

  public static String[] substrings(String input, int minLength) {
    return getLexicon().substrings(input, minLength);
  }

  public static void substrings(String input, Set result) {
    getLexicon().substrings(input, result);
  }

  public static void substrings(String input, Set result, int minLength) {
    getLexicon().substrings(input, result, minLength);
  }

  public static String[] superstrings(String input) {
    return getLexicon().superstrings(input);
  }

  public static void superstrings(String input, Set result, int minLength) {
    getLexicon().superstrings(input, result, minLength);
  }

  public static String[] similarByLetter(String s, int minEditDistance) {
    return getLexicon().similarByLetter(s, minEditDistance);
  }

  public static String[] similarByLetter(String input, int med,
      boolean preserveLength) {
    return getLexicon().similarByLetter(input, med, preserveLength);
  }

  public static String[] similarBySound(String input, int minDist) {
    return getLexicon().similarBySound(input, minDist);
  }

  // =============================================================================

  static RiLexicon getLexicon() {
    if (lexicon == null)
      lexicon = new RiLexicon();
    return lexicon;
  }

  static RiLexicon lexicon;

  public static int getWordCount(String s) {
    return RiTa.tokenize(s).length;
  }

  public static void shuffle(Object[] items) {
    List tmp = new LinkedList();
    for (int i = 0; i < items.length; i++)
      tmp.add(items[i]);
    Collections.shuffle(tmp, randomSource());
    int idx = 0;
    for (Iterator i = tmp.iterator(); i.hasNext(); idx++)
      items[idx] = i.next();
  }

  /**
   * Packs an array of floats (size 4) representing (a,getLexicon(),g,b) color
   * values into a single integer
   */
  public static int pack(int a, int r, int g, int b) {
    if (a > 255)
      a = 255;
    else if (a < 0)
      a = 0;
    if (r > 255)
      r = 255;
    else if (r < 0)
      r = 0;
    if (g > 255)
      g = 255;
    else if (g < 0)
      g = 0;
    if (b > 255)
      b = 255;
    else if (b < 0)
      b = 0;
    return (a << 24) | (r << 16) | (g << 8) | b;
  }

  /**
   * Unpacks a integer into an array of floats (size 4) representing
   * (a,getLexicon(),g,b) color values
   */
  public static int[] unpack(int pix) {
    int a = (pix >> 24) & 0xff;
    int r = (pix >> 16) & 0xff;
    int g = (pix >> 8) & 0xff;
    int b = (pix) & 0xff;
    return new int[] { a, r, g, b };
  }

  public static String untokenize(String[] arr) {
    return untokenize(arr, ' ', true);
  }

  public static String untokenize(String[] arr, char delim) {
    return untokenize(arr, delim, true);
  }

  public static String untokenize(String[] arr, boolean adjustPunctuationSpacing) {
    return untokenize(arr, ' ', adjustPunctuationSpacing);
  }

  public static Method _findCallback(Object parent, String callbackName) {
    try {
      return (callbackName == null) ? _findMethod(parent, DEFAULT_CALLBACK,
	  new Class[] { RiTaEvent.class }, false) : _findMethod(parent,
	      callbackName, new Class[] {}, false);
    } catch (RiTaException e) {
      String msg = (callbackName == null) ? DEFAULT_CALLBACK
	  + "(RiTaEvent re);" : callbackName + "();";
      System.err.println("[WARN] Expected callback not found: "
	  + shortName(parent) + "." + msg);
      return null;
    }
  }

  /**
   * Joins array of word, similar to words.join(delim), but attempts to preserve
   * punctuation position unless the 'adjustPunctuationSpacing' flag is set to
   * false
   * 
   * @param arr
   *          the array to join
   * @return the joined array as a String
   */
  public static String untokenize(String[] arr, char delim, boolean adjustPunctuationSpacing) {

    // System.out.println("RiTa.untokenize("+RiTa.asList(arr)+",'"+delim+"',"+adjustPunctuationSpacing+")");

    if (arr == null || arr.length < 1) return E;

    String result = arr[0];

    if (adjustPunctuationSpacing) {

      if (PUNC == null) {
	PUNC = Pattern.compile("^[,.;:?!)\"“”’‘`']+$");
	QUOTES = Pattern.compile("^[(\"“”’‘`']+$");
	SQUOTES = Pattern.compile("^[’‘`']+$");
	APOS = Pattern.compile("^[’']+$");
      }

      boolean thisPunct, thisQuote, thisComma, isLast, lastQuote, lastComma, lastPunct, lastEndWithS;
      boolean midSentence = false, afterQuote = false, dbug = false;
      boolean withinQuote = arr.length > 0 && QUOTES.matcher(arr[0]).matches();

      for (int i = 1; i < arr.length; i++) {

	if (arr[i].equals(null))
	  continue;

	thisComma = arr[i] == ",";
	thisPunct = PUNC.matcher(arr[i]).matches();
	thisQuote = QUOTES.matcher(arr[i]).matches();
	lastComma = arr[i - 1] == ",";
	lastPunct = PUNC.matcher(arr[i - 1]).matches();
	lastQuote = QUOTES.matcher(arr[i - 1]).matches();
	isLast = (i == arr.length - 1);
	lastEndWithS = arr[i - 1].length() > 0 ?
	    arr[i - 1].charAt(arr[i - 1].length() - 1) == 's' 
	    : false;

	if (dbug)
	  System.out.println("before'" + arr[i] + "' " + i + " inquote?"
	      + withinQuote + " " + "thisPunct?" + thisPunct + " "
	      + "thisQuote?" + thisQuote);

	if (thisQuote) {

	  if (withinQuote) {
	    // no-delim, mark quotation done
	    afterQuote = true;
	    withinQuote = false;
	  } else if (!(APOS.matcher(arr[i]).matches() && lastEndWithS)) {
	    if (dbug)
	      System.out.println("set withinQuote=1");
	    withinQuote = true;
	    afterQuote = false;
	    result += delim;
	  }

	} else if (afterQuote && !thisPunct) {
	  result += delim;
	  if (dbug)
	    System.out.println("hit1 " + arr[i]);
	  afterQuote = false;

	} else if (lastQuote && thisComma) {
	  midSentence = true;

	} else if (midSentence && lastComma) {

	  result += delim;
	  if (dbug)
	    System.out.println("hit2 " + arr[i]);
	  midSentence = false;

	} else if ((!thisPunct && !lastQuote)
	    || (!isLast && thisPunct && lastPunct)) {
	  result += delim;
	}

	result += arr[i]; // add to result

	if (thisPunct && !lastPunct && !withinQuote
	    && SQUOTES.matcher(arr[i]).matches()) {
	  if (dbug)
	    System.out.println("hitnew " + arr[i]);
	  result += delim; // fix to #477
	}
      }

    }

    return result.trim();
  }

  /**
   * Joins Array of String into space-delimited String.
   * 
   * @param full
   *          - Array of Strings to be joined
   * @return String containing elements of String[] or ""
   */
  public static String join(Object[] full) {
    return join(full, SP);
  }

  public static String join(List input) {
    return join(input, SP);
  }

  public static float elapsed(long start) {
    return ((System.currentTimeMillis() - start) / 1000f);
  }

  /**
   * Joins Array of Objects into delimited String.
   * 
   * @param full
   *          - Array of Strings to be joined
   * @param delim
   *          - Delimiter to parse elements in resulting String
   * @return String containing elements of String[] or "" if null
   */
  public static String join(Object[] full, String delim) {
    StringBuilder result = new StringBuilder();
    if (full != null) {
      for (int index = 0; index < full.length; index++) {
	if (index == full.length - 1)
	  result.append(full[index]);
	else
	  result.append(full[index] + delim);
      }
    }
    return result.toString();
  }

  /**
   * Concatenates the array 'input' into a single String, separated by 'delim'
   */
  public static String join(String[] input, char delim) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < input.length; i++) {
      sb.append(input[i]);
      if (i < input.length - 1)
	sb.append(delim);
    }
    return sb.toString();
  }

  /**
   * Uses the default WordTokenizer to split the line into words
   * 
   * @see RiTokenizer
   */
  public static String[] tokenize(String line) {
    return RiTokenizer.getInstance().tokenize(line);
  }

  /**
   * Uses a RegexTokenizer to split the line into words
   * 
   * @see RiTokenizer
   */
  public static String[] tokenize(String line, String regex) {
    return (regex == null) ? tokenize(line) : RiTokenizer.getRegexInstance(
	regex).tokenize(line);
  }

  /**
   * Trims punctuation from each side of the <code>token</code> (does not trim
   * whitespace or internal punctuation).
   */
  public static String trimPunctuation(String token) {
    if (token == null || token.length() < 1)
      return token;

    // Note: needs to handle byte-order marks...
    if (punctPattern == null)
      punctPattern = Pattern.compile(PUNCT_PATT, Pattern.CASE_INSENSITIVE);

    Matcher m = punctPattern.matcher(token);
    boolean match = m.find();
    if (!match || m.groupCount() < 1) {
      System.err
      .println("[WARN] RiTa.trimPunctuation(): invalid regex state for String "
	  + "\n       '"
	  + token
	  + "', perhaps an unexpected byte-order mark?");
      return token;
    }

    return m.group(1);
  }

  static Pattern punctPattern = null;

  /**
   * An alternative to {@link String#split(String)} that optionally returns the
   * delimiters.
   */
  public static String[] split(String toSplit, Pattern regexPattern,
      boolean returnDelims) // NIAPI
  {
    if (!returnDelims)
      return regexPattern.split(toSplit);

    int index = 0;
    List matchList = new ArrayList();
    Matcher m = regexPattern.matcher(toSplit);
    while (m.find()) {
      String match = toSplit.subSequence(index, m.start()).toString();
      matchList.add(match);
      matchList.add(toSplit.subSequence(m.start(), m.end()).toString());
      index = m.end();
    }

    if (index == 0)
      return new String[] { toSplit };

    matchList.add(toSplit.subSequence(index, toSplit.length()).toString());

    int resultSize = matchList.size();
    while (resultSize > 0 && matchList.get(resultSize - 1).equals(""))
      resultSize--;

    return (String[]) matchList.subList(0, resultSize).toArray(
	new String[resultSize]);
  }

  public static String pluralize(String noun) {

    return Pluralizer.pluralize(noun);
  }

  /**
   * Returns true iff ALL characters in the string are punctuation
   * 
   * @param s
   *          String to check
   * @return boolean
   */
  public static boolean isPunctuation(String s) {
    if (PUNCT == null) {
      PUNCT = Pattern.compile(ALL_PUNCT);
    }
    return PUNCT.matcher(s).matches();
  }

  /**
   * Returns true iff the character is punctuation
   * 
   * @return boolean
   */
  public static boolean isPunctuation(char s) {
    if (PUNCT == null)
      PUNCT = Pattern.compile(ALL_PUNCT);
    return PUNCT.matcher(Character.toString(s)).matches();
  }

  public static float distance(float x1, float y1, float x2, float y2) {
    float dx = x1 - x2;
    float dy = y1 - y2;
    return (float) Math.sqrt(dx * dx + dy * dy);

  }

  public static String singularize(String noun) {
    return Singularizer.singularize(noun);
  }

  public static String stripPunctuation(String phrase) {
    return stripPunctuation(phrase, null);
  }

  /**
   * Strips any punctuation characters from the String
   */
  public static String stripPunctuation(String phrase, char[] charsToIgnore) {
    if (phrase == null || phrase.length() < 1)
      return "";

    StringBuilder sb = new StringBuilder();
    OUTER: for (int i = 0; i < phrase.length(); i++) {
      char c = phrase.charAt(i);
      // System.out.println("char: "+c+" "+Character.valueOf(c));
      if (charsToIgnore != null) {
	for (int j = 0; j < charsToIgnore.length; j++) {
	  if (c == charsToIgnore[j]) {
	    sb.append(c);
	    continue OUTER;
	  }
	}
      }
      if (PUNCT_CHARS.indexOf(c) < 0)
	sb.append(c);
    }
    return sb.toString();
  }

  /**
   * Delegates to the default sentence-parser to split <code>text</code> into
   * sentences
   */
  public static String[] splitSentences(String text) {
    return Splitter.getInstance().splitSentences(text);
  }

  /**
   * Returns a random element from a Collection (NAPI)
   * 
   * @return Object a random item
   */
  public static Object randomItem(Collection c) {

    if (c == null || c.isEmpty())
      throw new RiTaException("Null passed to randomItem()");

    int rand = (int) (random() * c.size());
    Object result = null;
    Iterator it = c.iterator();
    for (int i = 0; i <= rand; i++)
      result = it.next();
    return result;
  }

  /**
   * Returns a random element from a List (NAPI)
   * 
   * @return Object a random item
   */
  public static Object randomItem(List list) {
    if (list == null || list.size() == 0)
      throw new RiTaException("Null passed to randomItem()");
    int rand = (int) (random() * list.size());
    return list.get(rand);
  }

  /**
   * Returns a random element from an array (NAPI)
   * 
   * @return Object the random item
   */
  public static Object randomItem(Object[] list) {
    if (list == null || list.length == 0)
      throw new RiTaException("Null passed to randomItem()");
    int rand = (int) (random() * list.length);
    return list[rand];
  }

  /** NIAPI */
  public static final String upperCaseFirst(String value) {
    return Character.toString(value.charAt(0)).toUpperCase()
	+ value.substring(1);
  }

  /**
   * Removes white-space and line breaks from start and end of String
   * 
   * @param s
   *          String to be chomped
   * @return string without starting or ending white-space or line-breaks
   */
  public static String chomp(String s) {
    if (CHOMP == null) {
      CHOMP = Pattern.compile("\\s+$|^\\s+");
    }
    Matcher m = CHOMP.matcher(s);
    return m.replaceAll(Constants.E);
  }

  /** Returns true if 'input' is an abbreviation */
  public static boolean isAbbreviation(String input) {
    return abbreviations.contains(input); // case??
  }

  /**
   * Returns true if <code>sentence</code> starts with a question word. * e.g.,
   * (is,are,does,who,what,why,where,when,etc.)
   */
  public static boolean isQuestion(String sentence) {
    for (int i = 0; i < QUESTION_STARTS.length; i++)
      if ((sentence.trim().toUpperCase()).startsWith(QUESTION_STARTS[i]
	  .toUpperCase()))
	return true;
    return false;
  }

  /**
   * Returns true if <code>sentence</code> starts with a w-question word, e.g.,
   * (who,what,why,where,when,etc.)
   */
  public static boolean isW_Question(String sentence) {
    for (int i = 0; i < W_QUESTION_STARTS.length; i++)
      if ((sentence.trim().toUpperCase()).startsWith(W_QUESTION_STARTS[i]
	  .toUpperCase()))
	return true;
    return false;
  }

  /**
   * Returns true if 'currentWord' is the final word of a sentence.
   * <p>
   * This is a simplified version of the OAK/JET sentence splitter method.
   */
  public static boolean isSentenceEnd(String currentWord, String nextWord) {
    // System.out.println("RiTa.isSentenceEnd("+currentWord+", "+nextWord+")");

    if (currentWord == null)
      return false;

    int cWL = currentWord.length();

    // token is a mid-sentence abbreviation (mainly, titles) --> middle of sent
    if (RiTa.isAbbreviation(currentWord))
      return false;

    if (cWL > 1 && isIn(currentWord.charAt(0), "`'\"([{<")
	&& RiTa.isAbbreviation(currentWord.substring(1)))
      return false;

    if (cWL > 2
	&& ((currentWord.charAt(0) == '\'' && currentWord.charAt(1) == '\'') || (currentWord
	    .charAt(0) == '`' && currentWord.charAt(1) == '`'))
	    && RiTa.isAbbreviation(currentWord.substring(2))) {
      return false;
    }

    char currentToken0 = currentWord.charAt(cWL - 1);
    char currentToken1 = (cWL > 1) ? currentWord.charAt(cWL - 2) : ' ';
    char currentToken2 = (cWL > 2) ? currentWord.charAt(cWL - 3) : ' ';

    int nTL = nextWord.length();
    char nextToken0 = nextWord.charAt(0);
    char nextToken1 = (nTL > 1) ? nextWord.charAt(1) : ' ';
    char nextToken2 = (nTL > 2) ? nextWord.charAt(2) : ' ';

    // nextToken does not begin with an upper case,
    // [`'"([{<] + upper case, `` + upper case, or < -> middle of sent.
    if (!(Character.isUpperCase(nextToken0)
	|| (Character.isUpperCase(nextToken1) && isIn(nextToken0, "`'\"([{<"))
	|| (Character.isUpperCase(nextToken2) && ((nextToken0 == '`' && nextToken1 == '`') || (nextToken0 == '\'' && nextToken1 == '\'')))
	|| nextWord.equals("_") || nextToken0 == '<'))
      return false;

    // ends with ?, !, [!?.]["'}>)], or [?!.]'' -> end of sentence
    if (currentToken0 == '?'
	|| currentToken0 == '!'
	|| (isIn(currentToken1, "?!.") && isIn(currentToken0, "\"'}>)"))
	|| (isIn(currentToken2, "?!.") && currentToken1 == '\'' && currentToken0 == '\''))
      return true;

    // last char not "." -> middle of sentence
    if (currentToken0 != '.')
      return false;

    // Note: wont handle Q. / A. at start of sentence, as in a news wire
    // if (startOfSentence && (currentWord.equalsIgnoreCase("Q.")
    // || currentWord.equalsIgnoreCase("A.")))return true;

    // single upper-case alpha + "." -> middle of sentence
    if (cWL == 2 && Character.isUpperCase(currentToken1))
      return false;

    // double initial (X.Y.) -> middle of sentence << added for ACE
    if (cWL == 4
	&& currentToken2 == '.'
	&& (Character.isUpperCase(currentToken1) && Character
	    .isUpperCase(currentWord.charAt(0))))
      return false;

    // U.S. or U.N. -> middle of sentence
    // if (currentToken.equals("U.S.") || currentToken.equals("U.N."))
    // return false; // dch

    // f (Util.isAbbreviation(currentToken)) return false;

    // (for XML-marked text) next char is < -> end of sentence
    if (nextToken0 == '<')
      return true;

    return true;
  }

  /**
   * Returns a randomly ordered array of unique integers from 0 to
   * <code>numElements</code> -1. The size of the array will be
   * <code>numElements</code>.
   */
  public static int[] randomOrdering(int numElements) {
    int[] result = new int[numElements];
    List tmp = new LinkedList();
    for (int i = 0; i < result.length; i++)
      tmp.add(new Integer(i));
    Collections.shuffle(tmp, randomSource());
    int idx = 0;
    for (Iterator iter = tmp.iterator(); iter.hasNext(); idx++)
      result[idx] = ((Integer) iter.next()).intValue();
    return result;
  }

  private static Random randomSource() {
    if (randSource == null)
      randSource = new Random();
    return randSource;
  }

  public static int timer(float period) {
    throw new RiTaException("Missing parent object, did you mean: RiTa.timer(this, " + period + ");");
  }

  public static int timer(Object parent, float period) {
    return new RiTimer(parent, period).id();
  }

  public static int timer(Object parent, float period,
      String callbackFunctionName) {
    return new RiTimer(parent, period, callbackFunctionName).id();
  }

  public static void stopTimer(int id) {
    RiTimer rt = RiTimer.findById(id);
    if (rt != null)
      rt.stop();
  }

  public static boolean isStopWord(String s) { // ADD to API??
    if (stopWords == null) {
      stopWords = new HashSet();
      stopWords.addAll(Arrays.asList(RiTa.STOP_WORDS));
    }
    return (stopWords.contains(s.toUpperCase()));
  }

  static Set stopWords;

  public static void pauseTimer(int id, boolean b) {
    RiTimer rt = RiTimer.findById(id);
    if (rt != null)
      rt.pause(b);
  }

  public static void pauseTimer(int id, float pauseFor) {
    RiTimer rt = RiTimer.findById(id);
    if (rt != null)
      rt.pauseFor(pauseFor);
  }

  /**
   * Generates random numbers. Each time the <b>random()</b> function is called,
   * it returns an unexpected value within the specified range. If one parameter
   * is passed to the function it will return a <b>float</b> between zero and
   * the value of the <b>high</b> parameter.
   */
  public static int random(int max) {
    return random(0, max);
  }

  /**
   * Generates random numbers. Each time the <b>random()</b> function is called,
   * it returns an unexpected value within the specified range. If one parameter
   * is passed to the function it will return a <b>float</b> between zero and
   * the value of the <b>high</b> parameter.
   */
  public static int random(int low, int high) {

    return (int) (low + (random() * (high - low)));
  }

  /**
   * Generates random numbers. Each time the <b>random()</b> function is called,
   * it returns an unexpected value within the specified range. If one parameter
   * is passed to the function it will return a <b>float</b> between zero and
   * the value of the <b>high</b> parameter.
   */
  public static float random() {
    return randomSource().nextFloat();
  }

  public static Random randSource;

  public static float random(float high) {
    // avoid an infinite loop
    if (high == 0)
      return 0;

    float value = 0;
    do {
      value = random() * high;
    } while (value == high);
    return value;
  }

  /**
   * Generates random numbers. Each time the <b>random()</b> function is called,
   * it returns an unexpected value within the specified range. If one parameter
   * is passed to the function it will return a <b>float</b> between zero and
   * the value of the <b>high</b> parameter. The function call <b>random(5)</b>
   * returns values between 0 and 5 (starting at zero, up to but not including
   * 5). If two parameters are passed, it will return a <b>float</b> with a
   * value between the the parameters. The function call <b>random(-5, 10.2)</b>
   * returns values starting at -5 up to (but not including) 10.2.
   */
  public static float random(float low, float high) {
    if (low >= high)
      return low;
    float diff = high - low;
    return random(diff) + low;
  }

  /**
   * Sets the seed value for <b>random()</b>. By default, <b>random()</b>
   * produces different results each time the program is run. Set the
   * <b>value</b> parameter to a constant to return the same pseudo-random
   * numbers each time the software is run.
   */
  public static void randomSeed(long seed) {
    randomSource().setSeed(seed);
  }

  @SuppressWarnings("unused")
  private static String escapeHTML(String s) { // problem?

    return EntityLookup.getInstance().escape(s);
  }

  public static String unescapeHTML(String s) {

    return EntityLookup.getInstance().unescape(s);
  }

  // ////// HELPERS //////////////////////////////////////////////////////////

  /** Converts collection to String array */
  public static String[] strArr(Collection l) {
    if (l == null || l.size() == 0)
      return RiTa.EMPTY;
    return (String[]) l.toArray(new String[0]);
  }

  public static Object invoke(Object callee, String methodName,
      Class[] argTypes, Object[] args) {
    // System.out.println("INVOKE: "+callee.getClass()+"."+methodName+"(types="+asList(argTypes)+", vals="+asList(args)+")");
    return _invoke(callee, _findMethod(callee, methodName, argTypes, true),
	args);
  }

  public static Object invoke(Object callee, String methodName) {
    return invoke(callee, methodName, null, null);
  }

  public static Object invoke(Object callee, String methodName, Object[] args) {
    if (args == null)
      return invoke(callee, methodName);

    Class[] argTypes = new Class[args.length];

    for (int i = 0; i < args.length; i++) {
      argTypes[i] = args[i].getClass();
      if (argTypes[i] == Integer.class)
	argTypes[i] = Integer.TYPE;
      else if (argTypes[i] == Boolean.class)
	argTypes[i] = Boolean.TYPE;
      else if (argTypes[i] == Float.class)
	argTypes[i] = Float.TYPE;
      else if (argTypes[i] == Double.class)
	argTypes[i] = Double.TYPE;
      else if (argTypes[i] == Character.class)
	argTypes[i] = Character.TYPE;
    }

    return invoke(callee, methodName, argTypes, args);
  }

  public static Object _invoke(Object callee, Method m, Object[] args) {
    try {
      // System.out.println("INVOKE: "+callee+"."+m.getName()+"("+asList(args)+")");
      return m.invoke(callee, args);
    } catch (Throwable e) {
      Throwable cause = e.getCause();
      while (cause != null) {
	e = cause;
	cause = e.getCause();
      }
      System.err.println("[WARN] Invoke error on " + RiTa.shortName(callee)
	  + "." + m.getName() + "(" + asList(args) + ")\n  "
	  + _exceptionToString(e));

      throw new RiTaException(e);
    }
  }

  public static Method _findMethod(Object callee, String methodName,
      Class[] argTypes) {
    return _findMethod(callee, methodName, argTypes, false);
  }

  /** @exclude */
  public static Method _findMethod(Object callee, String methodName,
      Class[] argTypes, boolean isPublic) {
    // System.err.println("RiTa.findMethod("+callee+"."+methodName+"(), "+isPublic+")");

    if (callee == null)
      throw new RiTaException("Method not found: null." + methodName + "()");

    Method m = null;

    try {
      if (callee instanceof Class) { // static method

	if (isPublic) {

	  try {
	    m = ((Class) callee).getMethod(methodName, argTypes);
	  } catch (Exception e) {
	  }

	}
	if (m == null) {
	  m = ((Class) callee).getDeclaredMethod(methodName, argTypes);
	  m.setAccessible(true);
	}
      } else // non-static method
      {
	if (isPublic) {
	  try {
	    m = callee.getClass().getMethod(methodName, argTypes);
	  } catch (Exception e) {
	  }

	}
	if (m == null) {
	  m = callee.getClass().getDeclaredMethod(methodName, argTypes);
	  m.setAccessible(true);
	}
      }
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RiTaException("Method not found: "
	  + callee.getClass().getName() + "." + methodName + "()", e);
    }

    return m;
  }

  /**
   * Returns a String representation of Exception and stacktrace (only elements
   * with line numbers)
   */
  public static String _exceptionToString(Throwable e) {

    if (e == null)
      return "null";

    StringBuilder s = new StringBuilder(e + "\n");
    StackTraceElement[] stes = e.getStackTrace();
    for (int i = 0; i < stes.length; i++) {
      String ste = stes[i].toString();
      if (ste.matches(".*[0-9]+\\)"))
	s.append("    " + ste + '\n');
    }
    return s.toString();
  }

  /** @exclude */
  protected static boolean isIn(char c, String s) {

    return s.indexOf(c) >= 0;
  }

  /**
   * Concatenates the list 'input' into a single String, separated by 'delim'
   */
  public static String join(List input, String delim) {
    StringBuilder sb = new StringBuilder();
    if (input != null) {
      for (Iterator i = input.iterator(); i.hasNext();) {
	sb.append(i.next());
	if (i.hasNext())
	  sb.append(delim);
      }
    }
    return sb.toString();
  }

  /**
   * Opens an InputStream to the specified filename
   * 
   * @exclude
   */
  public static InputStream _openStream(Class resourceDir, String fileName) {
    return resourceDir.getResourceAsStream(fileName);
  }

  /*
   * public static InputStream _openStreamP5(PApplet p, String fileName) {
   * return openStreamLocal(fileName);
   * //System.err.println("openStream("+p+", "+fileName+")"); InputStream is =
   * null; try { if (p != null) { is = p.createInput(fileName); } else is =
   * openStreamLocal(fileName);
   * 
   * if (is == null) throw new RiTaException("null IS"); } catch (RiTaException
   * e) { throw new
   * RiTaException("Unable to open stream: "+fileName+" with pApplet="+p); }
   * return is;//new UnicodeInputStream(is); }
   */

  protected static String[] includedFiles = new String[] { "addenda.txt",
  "bin.gz" };

  private static Class<?> pAppletClass;

  private static boolean pAppletAttempted;

  protected static boolean isIncluded(String fname) {
    for (int i = 0; i < includedFiles.length; i++) {
      if (fname.endsWith(includedFiles[i]))
	return true;
    }
    return false;
  }

  @SuppressWarnings("resource")
  public static InputStream openStream(String streamName) // from Processing
  {
    // System.out.println("RiTa.openStreamLocal("+streamName+")");
    boolean dbug = false;

    try // check for url first (from PApplet)
    {
      URL url = new URL(streamName);
      return url.openStream();
    } catch (MalformedURLException mfue) {
      // not a url, that's fine
    } catch (FileNotFoundException fnfe) {
      // Java 1.5 likes to throw this when URL not available.
      // http://dev.processing.org/bugs/show_bug.cgi?id=403
    } catch (Throwable e) {
      throw new RiTaException("Throwable in openStreamLocal()", e);
    }

    InputStream is = null;
    String[] guesses = { "src/data", "data", "" };
    for (int i = 0; i < guesses.length; i++) {
      String guess = streamName;
      if (guesses[i].length() > 0) {
	if (_isAbsolutePath(guess))
	  continue;
	guess = guesses[i] + SLASH + guess;
      }

      // boolean isDefaultFile = isIncluded(guess);
      if (dbug && !RiTa.SILENT)
	System.out.print("[INFO] Trying " + guess);

      try {
	is = new FileInputStream(guess);
	if (dbug && !RiTa.SILENT)
	  System.out.println("... OK");
      } catch (FileNotFoundException e) {
	if (dbug && !RiTa.SILENT)
	  System.out.println("... failed");
	if (is != null)
	  try {
	    is.close();
	  } catch (IOException e1) {
	  }
      }
      if (is != null)
	break;
    }

    if (is == null) // last try with classloader...
    {
      // Using getClassLoader() prevents java from converting dots
      // to slashes or requiring a slash at the beginning.
      // (a slash as a prefix means that it'll load from the root of
      // the jar, rather than trying to dig into the package location)
      ClassLoader cl = RiTa.class.getClassLoader();

      // by default, data files are exported to the root path of the jar.
      // (not the data folder) so check there first.
      if (dbug && !RiTa.SILENT)
	System.out.print("[INFO] Trying data/" + streamName + " as resource");

      is = cl.getResourceAsStream("data/" + streamName);
      if (is != null) {

	String cn = is.getClass().getName();

	// this is an irritation of sun's java plug-in, which will return
	// a non-null stream for an object that doesn't exist. like all good
	// things, this is probably introduced in java 1.5. awesome!
	// http://dev.processing.org/bugs/show_bug.cgi?id=359
	if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {

	  if (dbug && !RiTa.SILENT)
	    System.out.println("... OK");

	  return is;
	}
      }
      if (dbug && !RiTa.SILENT)
	System.out.println("... failed");
    }

    if (is == null)
      throw new RiTaException("Unable to create stream for: " + streamName);

    return is;
  }

  /**
   * Returns true if Processing classes are available, else false
   */
  public static boolean hasProcessing() {
    return getProcessingClass() != null;
  }

  static Class getProcessingClass() {
    if (pAppletClass == null && !pAppletAttempted) {

      pAppletAttempted = true;

      try {
	pAppletClass = Class.forName("processing.core.PApplet");
      } catch (Exception e) {
      }
    }
    return pAppletClass;
  }

  public static String[] loadStrings(String fileName) {
    String[] str = loadStrings(fileName, null);
    if (str != null)
      return str;
    throw new RiTaException("Unable to load: " + fileName);
  }

  static String[] loadStrings(String fileName, Object parent) {

    if (parent != null) {

      Class pclass = getProcessingClass();

      if (pclass != null) {

	if (pclass.isInstance(parent)) {

	  PAppletIF pApplet = (PAppletIF) RiDynamic.cast(parent,
	      PAppletIF.class);
	  return pApplet.loadStrings(fileName);
	}
	System.err.println("[WARN] RiTa.loadString(s): Expecting a PApplet"
	    + " as 2nd argument, but found: " + parent.getClass());
      }
    }

    return loadStrings(openStream(fileName), 100);
  }

  static String[] loadStrings(URL url) {
    try {
      return loadStrings(url.openStream(), 100);
    } catch (IOException e) {
      throw new RiTaException("Unable to load: " + url, e);
    }
  }

  static String[] loadStrings(InputStream input) {
    return loadStrings(input, 100);
  }

  static String[] loadStrings(InputStream input, int numLines) {

    if (input == null)
      throw new RiTaException("Null input stream!");

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(input,
	  "UTF-8"));

      String lines[] = new String[numLines];
      int lineCount = 0;
      String line = null;
      while ((line = reader.readLine()) != null) {
	if (lineCount == lines.length) {
	  String temp[] = new String[lineCount << 1];
	  System.arraycopy(lines, 0, temp, 0, lineCount);
	  lines = temp;
	}
	lines[lineCount++] = line;
      }
      reader.close();

      if (lineCount == lines.length) {
	return lines;
      }

      // resize array to appropriate amount for these lines
      String output[] = new String[lineCount];
      System.arraycopy(lines, 0, output, 0, lineCount);
      return output;

    } catch (IOException e) {
      e.printStackTrace();
    }

    return EMPTY;
  }

  /** @exclude */
  public static boolean _isAbsolutePath(String fileName) {
    return (fileName.startsWith(SLASH) || fileName.matches("^[A-Za-z]:")); // hmmmmm...
    // 'driveA:\\'?
  }

  private static void throwPAppletMessage(String methodName, String file) {
    if (methodName != null) { // else do nothing

      System.err
      .println("[WARN] Unable to load: '"
	  + file
	  + "', please double-check "
	  + "the location.\n\nIf you are using the Processing IDE, try passing 'this' as the 2nd "
	  + "argument to " + methodName + ":\n\n    " + methodName + "("
	  + file + ", this);\n");

      throw new RiTaException();
    }
  }

  private static void throwPAppletMessage(String methodName, String[] files) {
    if (methodName != null) { // else do nothing

      String fs = E;
      for (int i = 0; i < files.length; i++) {
	fs += DQ + files[i] + DQ;
	if (i < files.length - 1)
	  fs += ", ";
      }

      System.err
      .println("[WARN] Unable to load files "
	  + RiTa.asList(files)
	  + ", please double-check "
	  + "the locations.\n\nIf you are using the Processing IDE, try passing 'this' as the 2nd "
	  + "argument to " + methodName + ":\n\n    " + methodName
	  + "(new String[]{" + fs + "}, this);\n");

      throw new RiTaException();
    }
  }

  public static String loadString(URL url) {
    return loadString(url, BN);
  }

  public static String loadString(URL url, String lbc) {
    return join(loadStrings(url), lbc);
  }

  public static String loadString(String fileName) {
    return loadString(fileName, BN);
  }

  public static String loadString(String fileName, String lbc) {
    return loadString(fileName, lbc, "RiTa.loadString");
  }

  static String loadString(String[] files, String lbc) {
    return loadString(files, lbc, "RiTa.loadString");
  }

  static String loadString(String url, String lbc, String methodNameForConsole) {
    String[] str = null;
    try {
      str = loadStrings(openStream(url));
    } catch (RiTaException e) {
      if (!url.startsWith("http"))
	throwPAppletMessage(methodNameForConsole, "\"" + url + "\"");
      else
	throw new RiTaException("Unable to load URL: " + url);

      return null;
    }

    return join(str, lbc);
  }

  static String loadString(String[] files, String lbc,
      String methodNameForConsole) {
    // System.out.println("RiTa.loadString("+files.length+", "+methodNameForConsole+")");
    String s = E;
    try {
      for (int i = 0; i < files.length; i++) {
	String content = loadString(files[i], null);
	// System.out.println(i+") "+content);
	if (content == null)
	  throw new RiTaException("Unable to load: " + files[i]);
	s += content;
      }
    } catch (Throwable e) {
      throwPAppletMessage(methodNameForConsole, files);
    }

    return s;
  }

  public static String loadString(String[] files, Object parent) {
    return loadString(files, parent, BN);
  }

  public static String loadString(String[] files, Object parent, String lbc) {
    if (parent == null)
      return loadString(files, lbc);

    String s = E;
    for (int i = 0; i < files.length; i++)
      s += loadString(files[i], parent, false, lbc);

    if (s.length() > 0)
      fireDataLoaded(files, parent, s);

    return s;
  }

  public static String loadString(URL[] urls) {
    return loadString(urls, BN);
  }

  public static String loadString(URL[] urls, String lbc) {
    String s = E;
    for (int i = 0; i < urls.length; i++)
      s += loadString(urls[i]);
    return s;
  }

  public static String loadString(String url, Object parent) {
    return loadString(url, parent, true, BN);
  }

  public static String loadString(String url, Object parent, String lbc) {
    return loadString(url, parent, true, lbc);
  }

  static String loadString(String url, Object parent, boolean fireEvent,
      String lbc) {
    String result = join(loadStrings(url, parent), lbc);

    if (fireEvent && parent != null)
      fireDataLoaded(new String[] { url }, parent, result);

    return result;
  }

  static class RiTaLoaderSource { // stub to match JS object

    public String[] urls;
    public String name = "RiTaLoader";

    public RiTaLoaderSource(String[] u) {
      this.urls = u;
    }

    public RiTaLoaderSource(String url) {
      this.urls = new String[] { url };
    }
  }

  private static boolean fireDataLoaded(String[] urls, Object parent,
      String result) {
    return new RiTaEvent(new RiTaLoaderSource(urls), EventType.DataLoaded,
	result).fire(parent);
  }

  /** Returns a String holding the current working directory */
  public static String cwd() {

    String cwd = "unknown";
    try {
      cwd = System.getProperty("user.dir");
    } catch (Exception e) {
      System.out.println("[WARN] Unable to determine current directory!");
    }
    return cwd;
  }

  /** @exclude */
  public static boolean _lastCharMatches(String string, char[] chars) {
    char c = string.charAt(string.length() - 1);
    for (int i = 0; i < chars.length; i++)
      if (c == chars[i])
	return true;
    return false;
  }

  public static int millis() {
    return (int) (System.currentTimeMillis() - millisOffset);
  }

  public static int millis(long startTime) {
    return (int) (System.currentTimeMillis() - startTime);
  }

  public static List asList(Set s) {
    List l = new ArrayList();
    if (s != null)
      l.addAll(s);
    return Arrays.asList(l);
  }

  public static List asList(Object[] o) {
    return (o == null) ? new ArrayList() : Arrays.asList(o);
  }

  public static List asList(float[] o) {
    return (o == null) ? new ArrayList() : Arrays.asList(o);
  }

  public static List asList(int[] o) {
    return (o == null) ? new ArrayList() : Arrays.asList(o);
  }

  public static String shortName(Class c) {
    String name = c.getName();
    int idx = name.lastIndexOf(".");
    return name.substring(idx + 1);
  }

  public static String shortName(Object c) {
    return shortName(c.getClass());
  }

  protected static String getFeature(String[] str, String featureName) {

    return getFeature(RiTa.join(str), featureName);
  }

  protected static String getFeature(String str, String featureName) {

    RiString riString = new RiString(str);
    String feature = riString.get(featureName);
    return feature == null ? E : feature;
  }

  public static Set abbreviations = new HashSet(64);

  static {
    abbreviations.add("Adm.");
    abbreviations.add("Capt.");
    abbreviations.add("Cmdr.");
    abbreviations.add("Col.");
    abbreviations.add("Dr.");
    abbreviations.add("Gen.");
    abbreviations.add("Gov.");
    abbreviations.add("Lt.");
    abbreviations.add("Maj.");
    abbreviations.add("Messrs.");
    abbreviations.add("Mr.");
    abbreviations.add("Mrs.");
    abbreviations.add("Ms.");
    abbreviations.add("Prof.");
    abbreviations.add("Rep.");
    abbreviations.add("Reps.");
    abbreviations.add("Rev.");
    abbreviations.add("Sen.");
    abbreviations.add("Sens.");
    abbreviations.add("Sgt.");
    abbreviations.add("Sr.");
    abbreviations.add("St.");

    // abbreviated first names
    abbreviations.add("Benj.");
    abbreviations.add("Chas.");
    // abbreviations.add("Alex."); // dch

    // abbreviated months
    abbreviations.add("Jan.");
    abbreviations.add("Feb.");
    abbreviations.add("Mar.");
    abbreviations.add("Apr.");
    abbreviations.add("Mar.");
    abbreviations.add("Jun.");
    abbreviations.add("Jul.");
    abbreviations.add("Aug.");
    abbreviations.add("Sept.");
    abbreviations.add("Oct.");
    abbreviations.add("Nov.");
    abbreviations.add("Dec.");

    // other abbreviations
    abbreviations.add("a.k.a.");
    abbreviations.add("c.f.");
    abbreviations.add("i.e.");
    abbreviations.add("e.g.");
    abbreviations.add("vs.");
    abbreviations.add("v.");

    Set tmp = new HashSet(64);
    Iterator it = abbreviations.iterator();
    while (it.hasNext())
      tmp.add(((String) it.next()).toLowerCase());
    abbreviations.addAll(tmp);
  }

  public static String stackToString(Throwable aThrowable) {
    final Writer result = new StringWriter();
    final PrintWriter printWriter = new PrintWriter(result);
    aThrowable.printStackTrace(printWriter);
    return result.toString();
  }

  /**
   * Whether LTS notifications are output to the console
   */
  public static boolean PRINT_LTS_INFO = false;

  public static int LEFT = 37, UP = 38, RIGHT = 39, DOWN = 40, CENTER = 3;

  public static void out(Collection l) {
    int i = 0;
    if (l == null || l.size() < 1) {
      System.out.println("[]");
      return;
    }
    for (Iterator it = l.iterator(); it.hasNext(); i++)
      System.out.println(i + ") '" + it.next() + "'");
  }

  public static void out(int[] l) {
    if (l == null || l.length < 1) {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j] + "'");
  }

  public static void out(float[] l) {
    if (l == null || l.length < 1) {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j] + "'");
  }

  public static void out(Object[] l) {
    if (l == null) {
      System.out.println("null");
      return;
    }
    if (l.length < 1) {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j] + "'");
  }

  public static void out(Map l) {
    if (l == null || l.size() < 1) {
      System.out.println("[]");
      return;
    }
    for (Iterator it = l.keySet().iterator(); it.hasNext();) {
      Object key = it.next();
      Object val = l.get(key);
      System.out.println(key + "='" + val + "'");
    }
  }

  public static void out(Object l) {
    if (l instanceof Object[]) {
      out((Object[]) l);
      return;
    }

    System.out.println(l);
  }

  static void init() {
    new Thread() { // hack for silencing
      public void run() {
	if (!INITD) {
	  INITD = true;
	  if (!SILENT)
	    System.out.println("[INFO] RiTa.version [" + VERSION + "]");
	  // isAndroid? "+(RiTa.env() == RiTa.ANDROID));
	}
      }
    }.start();
  }

  public static String[] kwic(String text, String word, Map options) {
    return Concorder.cachedKwic(text, word, options);
  }

  public static String[] kwic(String text, String word) {
    return kwic(text, word, null);
  }

  public static Map<String, Integer> concordance(String text, Map options) {
    return new Concorder(text, options).concordance();
  }

  public static Map<String, Integer> concordance(String text) {
    return concordance(text, null);
  }

  public static float minEditDistance(String s1, String s2) {
    return new MinEditDist().computeRaw(s1, s2);
  }

  public static float minEditDistance(String[] s1, String[] s2) {
    return new MinEditDist().computeRaw(s1, s2);
  }

  public static float minEditDistance(String s1, String s2, boolean normalized) {
    return (!normalized) ? new MinEditDist().computeRaw(s1, s2)
	: new MinEditDist().computeAdjusted(s1, s2);
  }

  public static float minEditDistance(String[] s1, String[] s2,
      boolean normalized) {
    return (!normalized) ? new MinEditDist().computeRaw(s1, s2)
	: new MinEditDist().computeAdjusted(s1, s2);
  }

  public static void main(String[] args) {
    RiTa.PHONEME_TYPE = RiTa.IPA;
    System.out.println(RiTa.getPhonemes("become"));
    System.out.println(RiTa.INTERNAL);
    RiTa.out(RiTa.join(RiTa.tokenize("The cat ate the stinky cheese."), ","));

  }
}
