package rita;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import rita.support.Constants;
import rita.support.JSONLexicon;
import rita.support.MinEditDist;
import rita.support.Phoneme;
import rita.support.PosTagger;
import rita.support.SetOp;

/**
 * RiLexicon represents the core 'dictionary' (or lexicon) for the RiTa tools.
 * It contains ~35,000 words augmented with phonemic and syllabic data, as well
 * as a list of valid parts-of-speech for each. The lexicon can be extended or
 * customized for additional words, usages, or pronunciations.
 * <P>
 * 
 * Additionally the lexicon is equipped with implementations of a variety of
 * matching algorithms (min-edit-distance, soundex, anagrams, alliteration,
 * rhymes, looks-like, etc.) based on combinations of letters, syllables, and
 * phonemes. An example use:
 * 
 * <pre>
 * RiLexicon lex = new RiLexicon(this);
 * String[] sims = lex.similarBySound(&quot;cat&quot;);
 * String[] rhymes = lex.rhymes(&quot;cat&quot;);
 * // etc.
 * </pre>
 * 
 * Note: For performance, the data for all RiLexicon instances is shared (there
 * is only 1 copy)
 */
public class RiLexicon implements Constants {
  static {
    RiTa.init();
  }

  private static final String DOT_STAR = ".*";

  static final int MATCH_MIN_LENGTH = 4;

  public static boolean SILENCE_LTS = false;

  static MinEditDist minEditDist;

  public static boolean enabled = true;

  public JSONLexicon lexImpl;

  /**
   * Constructs a (singleton) instance of the RiLexicon class.
   * <p>
   * Note: For performance, the data for all RiLexicon instances is shared
   * (there is only 1 copy)
   */
  public RiLexicon(Object parent) // ignore parent
  {
    this(null);
  }

  /**
   * Constructs a (singleton) instance of the RiLexicon class.
   * <p>
   * Note: For performance, the data for all RiLexicon instances is shared
   * (there is only 1 copy)
   */
  public RiLexicon() {
    this(null);
  }

  public RiLexicon(Map data) {
    this.lexImpl = JSONLexicon.getInstance();
    if (data != null)
      this.lexImpl.setLexicalData(data);
  }

  public RiLexicon removeWord(String s) {
    lexicalData().remove(s.toLowerCase());
    return this;
  }

  public RiLexicon addWord(String word, String pronunciation,
      String partsOfSpeech) {
    lexImpl.addWord(word.toLowerCase(), pronunciation.toLowerCase(),
	partsOfSpeech.toLowerCase());
    return this;
  }

  public RiLexicon reload() {
    lexImpl = JSONLexicon.reload();
    return this;
  }

  public RiLexicon clear() {
    lexicalData().clear();
    return this;
  }

  /**
   * Returns true if the word exists in the lexicon (case-insensitive)
   */
  public boolean containsWord(String word) {
    return lexicalData().get(word.toLowerCase()) != null;
  }

  /**
   * Returns true if the first stressed consonant of the two words match, else
   * false.
   * <P>
   * Note: returns true if wordA.equals(wordB) and false if either (or both) are
   * null;
   */
  public boolean isAlliteration(String wordA, String wordB) {

    if (wordA != null && wordB != null) {

      if (wordB.equals(wordA))
	return true;

      String fcA = firstConsonant(firstStressedSyllable(wordA, true));
      String fcB = firstConsonant(firstStressedSyllable(wordB, true));

      // System.out.println(fcA+" ?= "+fcB);
      if (fcA != null && fcB != null && fcA.equals(fcB))
	return true;
    }
    return false;
  }

  // NOTE: this is just an optimization of the above
  boolean _isAlliteration(String firstConsOfFirstStressed, String wordB,
      boolean useLTS) {

    if (wordB != null && firstConsOfFirstStressed != null) {

      String fcB = firstConsonant(firstStressedSyllable(wordB, useLTS));

      // System.out.println(fcA+" ?= "+fcB);
      if (fcB != null && firstConsOfFirstStressed.equals(fcB))
	return true;
    }
    return false;
  }

  public Map<String, String> lexicalData() {
    return lexImpl.getLexicalData();
  }

  public RiLexicon lexicalData(Map m) {
    lexImpl.setLexicalData(m);
    return this;
  }

  /** Returns a random word from the lexicon */
  public String randomWord() {
    return randomWord(-1);
  }

  /**
   * Returns a random word from the lexicon with the specified part-of-speech
   * 
   * @see PosTagger
   */
  public String randomWord(String pos) {
    return randomWordByLength(pos, -1);
  }

  /**
   * Returns a random word from the lexicon with the specified syllable-count or
   * null if no such word exists.
   */
  public String randomWord(int syllableCount) {
    return randomWord(null, syllableCount);
  }

  /**
   * Returns a random word from the lexicon with the specified part-of-speech
   * and syllable-count, or null if no such word exists.
   * 
   * @see PosTagger
   */
  public String randomWord(String pos, int syllableCount) {
    Iterator<String> it = getIterator(pos);

    if (it != null) {

      if (syllableCount < 0)
	return it.next(); // ignore syllable-count

      Map<String, String> lookup = lexImpl.getLexicalData();

      while (it.hasNext()) {

	String s = it.next(), data = lookup.get(s);
	if (data == null) {
	  System.err.println("[WARN] null data after lookup: " + s);
	  return E;
	}

	String sylStr = data.split("\\|")[0].trim();
	if (sylStr.split(SP).length == syllableCount)
	  return s;
      }
    }

    return E;
  }

  private Iterator<String> getIterator(String pos) {
    Iterator<String> it = (pos == null) ? lexImpl.randomIterator() : lexImpl
	.randomPosIterator(pos);
    return it;
  }

  /**
   * Returns a random word from the lexicon with the specified part-of-speech
   * and target-length, or null if no such word exists.
   * 
   * @see PosTagger
   */
  String randomWordByLength(String pos, int targetLength) { // NIAPI

    Iterator<String> it = getIterator(pos);

    if (targetLength < 0)
      return it.next();

    while (it.hasNext()) {
      String s = it.next();
      if (s.length() == targetLength)
	return s;
    }

    return E;
  }

  /**
   * Returns a random word from the lexicon with the specified target-length
   * (where length>0), or null if no such word exists.
   */
  String randomWordByLength(int targetLength) { // NIAPI

    return randomWordByLength(null, targetLength);
  }

  /**
   * Returns the rhymes for a given word or null if none found
   * <p>
   * Two words rhyme if their final stressed vowel and all following phonemes
   * are identical.
   */
  public String[] rhymes(String input) {
    Set result = new HashSet();
    rhymes(input, result);
    return SetOp.toStringArray(result);
  }

  /*
   * In the specific sense, two words rhyme if their final stressed vowel and
   * all following sounds are identical;
   * 
   * masculine: a rhyme in which the stress is on the final syllable of the
   * words. (rhyme, sublime, crime) feminine: a rhyme in which the stress is on
   * the penultimate (second from last) syllable of the words. (picky, tricky,
   * sticky, icky) dactylic: a rhyme in which the stress is on the
   * antepenultimate (third from last) syllable ('cacophonies", "Aristophanes")
   * 
   * In the general sense, "rhyme" can refer to various kinds of phonetic
   * similarity between words, and to the use of such similar-sounding words in
   * organizing verse. Rhymes in this general sense are classified according to
   * the degree and manner of the phonetic similarity:
   * 
   * syllabic: a rhyme in which the last syllable of each word sounds the same
   * but does not necessarily contain vowels. (cleaver, silver, or pitter,
   * patter) imperfect: a rhyme between a stressed and an unstressed syllable.
   * (wing, caring) semirhyme: a rhyme with an extra syllable on one word.
   * (bend, ending) oblique (or slant): a rhyme with an imperfect match in
   * sound. (green, fiend; one, thumb) assonance: matching vowels. (shake, hate)
   * Assonance is sometimes used to refer to slant rhymes. consonance: matching
   * consonants. (rabies, robbers) half rhyme (or sprung rhyme): matching final
   * consonants. (bent, ant)
   */

  /**
   * Returns the rhymes for a given word or null if none found.
   * <P>
   * Two words rhyme if their final stressed vowel and all following phonemes
   * are identical.
   */
  private void rhymes(String input, Set result) {
    String lss = lastStressedPhoneToEnd(input, false); // TODO: change to true !
    if (lss == null)
      return; // no result

    for (Iterator it = lexicalData().keySet().iterator(); it.hasNext();) {
      String cand = (String) it.next();

      if (cand.equals(input))
	continue;

      String chck = lexImpl.getRawPhones(cand);
      if (chck != null && chck.endsWith(lss))
	result.add(cand);
    }
  }

  public String[] words() {
    return this.words(false);
  }

  public String[] words(String regex) {
    return words(regex, false);
  }

  public String[] words(boolean shuffled) {
    return SetOp.toStringArray(lexicalData().keySet(), shuffled);
  }

  /**
   * Returns the set of words in the lexicon that match the supplied regular
   * expression. For example, lex.words("ee"); returns 661 words with 2 or more
   * consecutive e's, while lex.words("ee.*ee"); returns exactly 2:
   * 'freewheeling' and 'squeegee'.
   * 
   * @param sorted
   *          Sorted alphabetically when true
   */
  public String[] words(String regex, boolean sorted) {
    if (!regex.startsWith(DOT_STAR))
      regex = DOT_STAR + regex;
    if (!regex.endsWith(DOT_STAR))
      regex = regex + DOT_STAR;
    return this.words(Pattern.compile(regex), sorted);
  }

  public String[] words(Pattern regex, boolean sorted) {
    Set words = lexImpl.getWords(regex);
    String[] s = (String[]) words.toArray(EMPTY);
    if (!sorted)
      RiTa.shuffle(s);
    return s;
  }

  public boolean isAdverb(String s) {
    String[] posTags = lexImpl.getPosArr(s);
    for (int i = 0; posTags != null && i < posTags.length; i++) {
      if (PosTagger.isAdverb(posTags[i]))
	return true;
    }
    return false;
  }

  public boolean isNoun(String s) {
    String[] posTags = lexImpl.getPosArr(s);

    for (int i = 0; posTags != null && i < posTags.length; i++) {
      if (PosTagger.isNoun(posTags[i]))
	return true;
    }
    return false;
  }

  public boolean isVerb(String s) {
    String[] posTags = lexImpl.getPosArr(s);

    for (int i = 0; posTags != null && i < posTags.length; i++) {
      if (PosTagger.isVerb(posTags[i]))
	return true;
    }
    return false;
  }

  public boolean isAdjective(String s) {
    String[] posTags = lexImpl.getPosArr(s);

    for (int i = 0; posTags != null && i < posTags.length; i++) {
      if (PosTagger.isAdjective(posTags[i]))
	return true;
    }
    return false;
  }

  /**
   * Returns true if the two words rhyme (that is, if their final stressed
   * phoneme and all following phonemes are identical). Note: returns false if
   * wordA.equals(wordB) or if either are null.
   */
  public boolean isRhyme(String wordA, String wordB) {
    return isRhyme(wordA, wordB, false); // TODO: change to true (with others)
  }

  /**
   * Returns true if the two words rhyme (that is, if their final stressed
   * phoneme and all following phonemes are identical). Note: returns false if
   * wordA.equals(wordB) or if either are null;
   * <p>
   * Note: will only use letter-to-sound engine for words not found in lexicon
   * if useLTS=true
   */
  public boolean isRhyme(String wordA, String wordB, boolean useLTS) {
    boolean dbug = false;

    if (dbug)
      System.out.println("RiLexicon.isRhyme('" + wordA + "' ?= '" + wordB
	  + "') ->");
    boolean result = false;

    if (wordA != null && wordB != null && !wordB.equalsIgnoreCase(wordA)) {

      String lspA = lastStressedPhoneToEnd(wordA, useLTS);
      String lspB = lastStressedPhoneToEnd(wordB, useLTS);

      if (dbug)
	System.out.println("RiLexicon.isRhyme('" + lspA + "' ?= '" + lspB
	    + "') ->");
      if (lspA != null && lspB != null && lspA.equals(lspB))
	result = true;
    }

    if (dbug)
      System.out.println("  " + result);
    return result;
  }

  /**
   * Compares the characters of the input string (using a version of the
   * min-edit distance algorithm) to each word in the lexicon, returning the set
   * of closest matches.
   */
  public String[] similarByLetter(String input) {
    Set result = new HashSet();
    similarByLetter(lexImpl.getWords(), input, result, 1, false);
    return SetOp.toStringArray(result);
  }

  /*
   * Compares the characters of the input string (using a version of the
   * min-edit distance algorithm) to each word in the lexicon, adding the set of
   * closest matches to <code>result</code>. If 'preserveLength' is true, the
   * method will favor words of the same length as the input.
   */
  int similarByLetter(Collection candidates, String input, Collection result,
      int minMed, boolean preserveLength) {
    if (result == null)
      throw new IllegalArgumentException("Null Arg: result[Collection](3)");

    if (input == null || input.length() < 1)
      return -1;

    int minVal = Integer.MAX_VALUE;

    if (minEditDist == null)
      minEditDist = new MinEditDist();

    for (Iterator i = candidates.iterator(); i.hasNext();) {
      String candidate = (String) i.next();

      if (preserveLength && candidate.length() != input.length())
	continue;

      if (candidate.equalsIgnoreCase(input))
	continue;

      // System.out.println("testing: "+candidate);

      int med = minEditDist.computeRaw(candidate, input);

      if (med == 0)
	continue; // same word

      // we found something even closer
      if (med >= minMed && med < minVal) {
	if (checkResult(input, result, candidate, 2)) {
	  // System.out.println("Found "+candidate+", med="+med+" -> "+result);
	  minVal = med;
	  result.clear();
	  result.add(candidate);
	}
      }
      // we have another best to add
      else if (med == minVal) {
	addResult(input, result, candidate, 2);
	// System.out.println("  Adding "+candidate+", med="+med);
      }
    }

    // System.out.println("Min: "+minVal+" -> "+result);
    return minVal;
  }

  int similarBySound(String input, Set<String> result, int minDist) {
    if (result == null)
      throw new IllegalArgumentException("Null Arg: result[Collection](3)");

    if (input == null || input.length() < 1)
      return -1;

    int minVal = Integer.MAX_VALUE;
    String[] targetPhones = lexImpl.getPhonemeArr(input, true);

    if (targetPhones == null)
      return -1;

    if (minEditDist == null)
      minEditDist = new MinEditDist();

    // System.out.println("TARGET: "+RiTa.asList(targetPhones));

    for (Iterator<String> i = lexImpl.iterator(); i.hasNext();) {
      String candidate = (String) i.next();
      String[] phones = lexImpl.getPhonemeArr(candidate, false);

      int med = minEditDist.computeRaw(phones, targetPhones);

      if (med == 0)
	continue; // same phones

      // found something even closer
      if (med >= minDist && med < minVal) {

	if (checkResult(input, result, candidate, 3)) {

	  // System.out.println("BEST: "+candidate + " "+med +
	  // " "+Arrays.asList(phones));

	  minVal = med;
	  result.clear();
	  result.add(candidate);
	}
      }
      // we have another best to add
      else if (med == minVal) {

	// System.out.println("TIED: "+candidate + " "+med +
	// " "+Arrays.asList(phones));

	addResult(input, result, candidate, 3);
      }
    }

    return minVal;
  }

  /**
   * Compares the phonemes of the input String to those of each word in the
   * lexicon, returning the set of closest matches as a String[].
   */
  public String[] similarBySound(String input) {
    Set<String> result = new HashSet<String>();
    similarBySound(input, result, 1);
    return SetOp.toStringArray(result);
  }

  /**
   * First calls similarBySound(), then filters the result set by the algorithm
   * used in similarByLetter(); (useful when similarBySound() returns too large
   * a result set)
   * 
   * @see #similarByLetter(String)
   * @see #similarBySound(String)
   */
  public String[] similarBySoundAndLetter(String input) {
    Set result = new HashSet();
    similarBySoundAndLetter(input, result);
    if (result.size() == 0)
      return EMPTY;
    return SetOp.toStringArray(result);
  }

  private int similarBySoundAndLetter(String input, Set result) {
    Set tmp = new TreeSet();
    int min = this.similarBySound(input, tmp, 1);
    this.similarByLetter(tmp, input, result, 1, false);
    result.remove(input); // don't accept the same word
    return min;
  }

  public int size() {
    return lexicalData().size();
  }

  /**
   * Returns all valid substrings of the input word in the lexicon
   */
  public String[] substrings(String input) {
    return this.substrings(input, MATCH_MIN_LENGTH);
  }

  /**
   * Returns all valid substrings of the input word in the lexicon of length at
   * least <code>minLength</code>
   */
  public String[] substrings(String input, int minLength) {
    Set result = new HashSet();
    this.substrings(input, result, minLength);
    if (result.size() == 0)
      return EMPTY;
    return SetOp.toStringArray(result);
  }

  public void substrings(String input, Set result) {
    this.substrings(input, result, MATCH_MIN_LENGTH);
  }

  public void substrings(String input, Set result, int minLength) {
    if (minLength < 0)
      minLength = MATCH_MIN_LENGTH;

    // List partials = getPartialCandidates(input, minLength);
    for (Iterator j = lexImpl.iterator(); j.hasNext();) {
      String candidate = (String) j.next();

      if (candidate.length() < minLength)
	continue;

      if (arrayContains(input, candidate))
	addResult(input, result, candidate, 0);
    }
  }

  /**
   * Returns all valid superstrings of the input word in the lexicon
   */
  public String[] superstrings(String input) {
    Set result = new HashSet();
    this.superstrings(input, result);
    ;
    return SetOp.toStringArray(result);
  }

  public void superstrings(String input, Set result) {
    this.superstrings(input, result, MATCH_MIN_LENGTH);
  }

  public void superstrings(String input, Set result, int minLength) {
    if (minLength < 0)
      minLength = MATCH_MIN_LENGTH;

    for (Iterator j = lexImpl.iterator(); j.hasNext();) {
      String candidate = (String) j.next();
      if (candidate.length() < minLength)
	continue;
      if (arrayContains(candidate, input))
	addResult(input, result, candidate, 0);
    }
  }

  public String[] similarByLetter(String s, int minEditDistance) {
    return similarByLetter(s, minEditDistance, false);
  }

  public String[] similarByLetter(String input, int minEditDistance,
      boolean preserveLength) {
    Set result = new HashSet();
    this.similarByLetter(lexImpl.getWords(), input, result, minEditDistance,
	preserveLength);
    return SetOp.toStringArray(result);
  }

  public String[] similarBySound(String input, int minDist) {
    Set result = new HashSet();
    similarBySound(input, result, minDist);
    return SetOp.toStringArray(result);
  }

  // PRIVATES

  private String firstStressedSyllable(String word) {
    return firstStressedSyllable(word, false);
  }

  private String firstStressedSyllable(String word, boolean useLTS) {
    if (word.indexOf(' ') > -1)
      return null;

    String raw = lexImpl.getRawPhones(word, useLTS);
    // System.out.println(word + "-> raw='"+raw+"'");
    int idx = -1;

    if (raw != null)
      idx = raw.indexOf(STRESSED);
    else
      System.out.println("[WARN] No stress data for '" + word + "'");

    // System.out.print("idx="+idx);

    if (idx < 0)
      return null; // no stresses

    char c = raw.charAt(--idx);
    while (c != ' ') {
      if (--idx < 0) {
	idx = 0; // single-stressed syllable
	break;
      }
      c = raw.charAt(idx);
    }
    String firstToEnd = idx == 0 ? raw : raw.substring(idx).trim();
    idx = firstToEnd.indexOf(' ');

    return (idx < 0 ? firstToEnd : firstToEnd.substring(0, idx));
  }

  private boolean addResult(String input, Collection result, String candidate,
      int minLength) {
    if (checkResult(input, result, candidate, minLength)) {
      result.add(candidate);
      return true;
    }
    return false;
  }

  private boolean checkResult(String input, Collection result,
      String candidate, int minLength) {
    if (candidate.length() < minLength) {
      // System.out.println("RiLexicon.addResult(False::BAD_LENGTH) -> "+candidate);
      return false;
    }
    if (candidate.equals(input) || candidate.equals(input + "s")
	|| candidate.equals(input + "es")) {
      // System.out.println("RiLexicon.addResult(False::INPUT) -> "+candidate);
      return false;
    }
    return true;
  }

  private static String firstConsonant(String rawPhones) {
    if (rawPhones != null) {
      String[] phones = rawPhones.split(PHONEME_BOUNDARY);
      if (phones != null) {
	for (int j = 0; j < phones.length; j++) {
	  if (Phoneme.isConsonant(phones[j]))
	    return phones[j];
	}
      }
    }
    return null;
  }

  /*
   * Includes the last stressed vowel and all subsequent phonemes
   * If none is found, return null(?) 
   * 
   * TODO: Cyrus
   */
  public String lastStressedVowelPhonemeToEnd(String word, boolean useLTS) {
    return "";
  }

  /*
   * Includes the last stressed syllable and all subsequent phonemes in the form
   */
  public String lastStressedPhoneToEnd(String word, boolean useLTS) {
    boolean dbug = false;

    String raw = lexImpl.getRawPhones(word, useLTS);
    if (raw == null)
      return null;

    if (dbug)
      System.out.print("'" + raw + "' -> ");

    int idx = raw.lastIndexOf(STRESSED);

    if (dbug)
      System.out.print("idx=" + idx);

    if (idx < 0)
      return null;

    /*
     * if (idx == raw.length()-1) { // edge case
     * 
     * String[] syls = raw.split(SP); return syls[syls.length-1]; }
     */

    char c = raw.charAt(--idx);

    if (dbug)
      System.out.print("\n  chk:" + idx + "=" + c);

    while (c != '-' && c != ' ') {

      if (--idx < 0) {

	if (dbug)
	  System.out.println("\nres=" + raw);

	return raw; // single-stressed syllable
      }

      if (dbug)
	System.out.print("  chk:" + idx + "=" + raw.charAt(idx));

      c = raw.charAt(idx);
    }

    String res = raw.substring(idx + 1);

    if (dbug)
      System.out.println("\nres=" + res);

    return res;
  }

  public Iterator iterator() // NIAPI
  {
    return lexicalData().keySet().iterator();
  }

  private static boolean arrayContains(String full, String search) {
    if (full == null)
      return false;
    return (full.indexOf(search) > -1);
  }

  public String[] alliterations(String input) {
    return alliterations(input, MATCH_MIN_LENGTH);
  }

  // TODO: make sure this method version is in RiTaJS
  public String[] alliterations(String input, int minLength) {

    Set s = new HashSet();
    alliterations(input, s, minLength);
    return SetOp.toStringArray(s);
  }

  private void alliterations(String input, Set result, int minLength) {

    alliterations(input, result, minLength, true); // Will use LTS for now
  }

  private void alliterations(String input, Set result, int minLength,
      boolean useLTS) {

    String fC = firstConsonant(firstStressedSyllable(input, useLTS));

    if (input != null && (input.indexOf(' ') < 0) && fC != null) {

      Map<String, String> m = lexicalData();

      for (Iterator<String> it = m.keySet().iterator(); it.hasNext();) {

	String cand = it.next();

	if (_isAlliteration(fC, cand, useLTS))
	  addResult(input, result, cand, minLength);
      }
    }
  }

  public static void main(String[] args) {
    RiLexicon rl = new RiLexicon();
    // System.out.println(rl.lastStressedPhoneToEnd("mellow",true));
    System.out.println(rl.lastStressedPhoneToEnd("toy", true));
    System.out.println(rl.lastStressedPhoneToEnd("boy", true));
    System.out.println(rl.lastStressedPhoneToEnd("wellow", true));
    // System.out.println(rl.lexicalData().get("dry"));
  }

}
