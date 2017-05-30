package rita;

import java.util.*;
import java.util.regex.Pattern;

import rita.support.*;

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
 * String[] rhymes = lex.rhymes(&quot;cat&quot;);
 * // etc.
 * </pre>
 * 
 * Note: For performance, the data for all RiLexicon instances is shared.
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
    this((HashMap) null);
  }

  /**
   * Constructs a (singleton) instance of the RiLexicon class.
   * <p>
   * Note: For performance, the data for all RiLexicon instances is shared
   * (there is only 1 copy)
   */
  public RiLexicon() {
    this((HashMap) null);
  }

  public RiLexicon(HashMap data) {
    this.lexImpl = JSONLexicon.getInstance();
    if (data != null)
      this.lexImpl.setLexicalData(data);
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

  private boolean isPlural(String word) {

    String stem = RiTa.stem(word, RiTa.PLING);
    if (stem.equals(word)) return false;
    
    String lookup = lexicalData().get(RiTa.singularize(word));
    if (lookup == null) return false;
    
    String[] data = lookup.split("\\|");
    //RiTa.out(data);
    if (data != null && data.length == 2) {
      String[] pos = data[1].split(SP);
      for (int i = 0; i < pos.length; i++) {
        if (pos[i].equals("nn"))
          return true;
      }
    } else if (word.endsWith("ses") || word.endsWith("zes")) {

      String sing = word.substring(0, word.length() - 1);
      lookup = lexicalData().get(RiTa.singularize(sing));
      data = lookup.split("\\|");
      if (data != null && data.length == 2) {
          String[] pos = data[1].split(SP);
          for (int i = 0; i < pos.length; i++) {
              if (pos[i].equals("nn"))
                  return true;
          }
      }
    }
    return false;
  }

  /**
   * Returns true if the word exists in the lexicon (case-insensitive)
   */
  public boolean containsWord(String word) {
    if (word == null || word.length() < 1) 
      	return false;
    word = word.toLowerCase();
    String data = lexicalData().get(word);
    return (data != null) ? true : isPlural(word); 
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

      // space
      if (wordA.indexOf(" ") > -1 || wordB.indexOf(" ") > -1)
	return false;

      if (wordB.equals(wordA))
	return true;

      String fcA = firstPhoneme(firstStressedSyllable(wordA, true));
      String fcB = firstPhoneme(firstStressedSyllable(wordB, true));

      if (Phoneme.isVowel("" + fcA.charAt(0))
	  || Phoneme.isVowel("" + fcB.charAt(0)))
	return false;

      if (fcA != null && fcB != null && fcA.equals(fcB))
	return true;
    }
    return false;
  }

  // NOTE: this is just an optimization of the above
  boolean _isAlliteration(String firstConsOfFirstStressed, String wordB,
      boolean useLTS) {

    if (wordB != null && firstConsOfFirstStressed != null) {
      // Alliteration of vowels
      char c = firstConsOfFirstStressed.charAt(0);
      if (VOWELS.indexOf(c) != -1) {
	char cB = wordB.charAt(0);
	if (c == cB)
	  return true;
      }
      String fcB = firstConsonant(firstStressedSyllable(wordB, useLTS));

      // System.out.println(fcA+" ?= "+fcB);
      if (fcB != null && firstConsOfFirstStressed.equals(fcB))
	return true;
    }
    return false;
  }

  public HashMap<String, String> lexicalData() {

    return lexImpl != null ? lexImpl.getLexicalData() : new HashMap();
  }

  public RiLexicon lexicalData(HashMap m) {
    if (lexImpl == null)
      throw new RuntimeException("Null lexicon");
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
    String word = randomWordByLength(pos, -1);
    if (word == null)
      throw new RiTaException("No words with pos=" + pos + " found");
    return word;
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

    boolean pluralize = false;

    if (pos != null) {
      pluralize = pos.equals("nns");
      if (pos.equals("v"))
	pos = "vb";
      if (pos.equals("r"))
	pos = "rb";
      if (pos.equals("a"))
	pos = "jj";
      if (pos.equals("n") || pos.equals("nns"))
	pos = "nn";
    }

    Iterator<String> it = getIterator(pos);

    if (it != null) {

      if (syllableCount < 0) { // ignore syllable-count
	return pluralize ? RiTa.pluralize(it.next()) : it.next();
      }

      Map<String, String> lookup = lexImpl.getLexicalData();
      while (it.hasNext()) {

	String word = it.next();
	String data = lookup.get(word);
	if (data == null) {
	  System.err.println("[WARN] null data after lookup: " + word);
	  return E;
	}

	String sylStr = data.split("\\|")[0].trim();

	if (sylStr.split(SP).length == syllableCount) {
	  return pluralize ? RiTa.pluralize(word) : word;
	}
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
  public String randomWordByLength(String pos, int targetLength) { // NIAPI

    Iterator<String> it = getIterator(pos);

    try {

      if (targetLength < 0) {
	return it.next();
      }

      while (it.hasNext()) {
	String s = it.next();
	if (s.length() == targetLength)
	  return s;
      }

    } catch (Exception e) {
      return null;
    }

    return E;
  }

  /**
   * Returns a random word from the lexicon with the specified target-length
   * (where length>0), or null if no such word exists.
   */
  public String randomWordByLength(int targetLength) { // NIAPI

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
  private void rhymes(String word, Set result) {

    String input = RiTa.trimPunctuation(word);
    String lss = lastStressedPhoneToEnd(input, true);
    if (lss == null)
      return; // no result

    for (Iterator it = lexicalData().keySet().iterator(); it.hasNext();) {
      String cand = (String) it.next();

      if (cand.equals(input))
	continue;

      String chck = getRawPhones(cand);

      if (chck != null && chck.endsWith(lss))
	result.add(cand);
    }
  }

  // NOTE: Default is NOT to use LTS
  public String getRawPhones(String word) {

    return getRawPhones(word, false); // Default is NOT to use LTS
  }

  public String getRawPhones(String word, boolean useLTS) {

    return getRawPhones(this.lexImpl, word, useLTS);
  }

  public static String getRawPhones(JSONLexicon lex, String word) {

    return getRawPhones(lex, word, false);
  }

  // Only uses LTS if useLTS is true AND the word is NOT found in the dictionary
  public static String getRawPhones(JSONLexicon lex, String word, boolean useLTS) {

    if (word == null || word.length() < 1)
      return E;

    String data = null;
    if (lex != null)
      data = lex.lookupRaw(word);

    if (data == null && useLTS) { // try LTS rules

      String phones = LetterToSound.getInstance().getPhones(word);
      if (phones != null && phones.length() > 0) {

	if (!RiTa.SILENT && !RiLexicon.SILENCE_LTS && RiLexicon.enabled)
	  System.out.println("[RiTa] Using letter-to-sound rules for: " + word);

	return phones;
      }
    }

    return data == null ? E : data.split(DATA_DELIM)[0].trim();
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

    boolean result = false;

    for (int i = 0; posTags != null && i < posTags.length; i++) {
      if (PosTagger.isNoun(posTags[i]))
	result = true;
    }

    // check whether it is plural
    if (!result) {
      String singular = RiTa.singularize(s);
      if (singular != s) {
	posTags = lexImpl.getPosArr(singular);
	for (int i = 0; posTags != null && i < posTags.length; i++) {
	  if (PosTagger.isNoun(posTags[i]))
	    result = true;
	}
	//System.out.println("found plural noun: " + s + " (" + singular + ")");
      }
    }

    return result;
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
   * Returns true if the two words rhyme (that is, if their final stressed vowel
   * phoneme and all following phonemes are identical). Note: returns false if
   * wordA.equals(wordB) or if either are null.
   */
  public boolean isRhyme(String wordA, String wordB) {
    return isRhyme(wordA, wordB, true);
  }

  /**
   * Returns true if the two words rhyme (that is, if their final stressed vowel
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
    String phonesA = getRawPhones(wordA, useLTS);
    String phonesB = getRawPhones(wordB, useLTS);

    if (wordA != null && wordB != null && !wordB.equalsIgnoreCase(wordA)
	&& !phonesB.equals(phonesA)) {

      String lspA = lastStressedVowelPhonemeToEnd(wordA, useLTS);
      String lspB = lastStressedVowelPhonemeToEnd(wordB, useLTS);

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
      String candidate = i.next();
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
  private String firstSyllable(String word, boolean useLTS) {
    if (word.indexOf(' ') > -1)
      return null;

    String raw = getRawPhones(word, useLTS);
    String[] syllables = raw.split(" ");

    return syllables[0];
  }

  private String firstStressedSyllable(String word, boolean useLTS) {

    if (word.indexOf(' ') > -1)
      return null;

    String raw = getRawPhones(word, useLTS);
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

  private static String firstPhoneme(String rawPhones) {
    if (rawPhones != null) {
      String[] phones = rawPhones.split(PHONEME_BOUNDARY);
      if (phones != null) {
	return phones[0];
      }
    }
    return null;
  }

  /*
   * Includes the last stressed vowel and all subsequent phonemes If none is
   * found, return null
   */
  public String lastStressedVowelPhonemeToEnd(String word, boolean useLTS) {

    String raw = lastStressedPhoneToEnd(word, useLTS);
    if (raw == null)
      return null;

    String[] syllables = raw.split(" ");
    String lastSyllable = syllables[syllables.length - 1];
    lastSyllable = lastSyllable.replaceAll("[^a-z-1 ]", "");

    int idx = -1;

    for (int i = 0; i < lastSyllable.length(); i++) {
      char c = lastSyllable.charAt(i);
      if (VOWELS.indexOf(c) != -1) {
	idx = i;
	break;
      }
    }

    // System.out.println(word + " " + raw + " last:" + lastSyllable + " idx=" +
    // idx + " result:" + lastSyllable.substring(idx));

    return lastSyllable.substring(idx);
  }

  /*
   * Includes the last stressed syllable and all subsequent phonemes in the form
   */
  public String lastStressedPhoneToEnd(String word, boolean useLTS) {
    boolean dbug = false;

    String raw = getRawPhones(word, useLTS);
    if (raw == null || raw == "")
      return null;

    if (dbug)
      System.out.print("'" + raw + "' -> ");

    int idx = raw.lastIndexOf(STRESSED);

    if (dbug)
      System.out.print("idx=" + idx);

    // if only one syllable, return all
    String[] syllables = raw.split(" ");
    if (idx < 0 && syllables.length == 1)
      return raw;

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
    if (input.length() == 0)
      return;
    // System.out.println(input.matches("[(^a-zA-Z)*]"));
    if (input.matches("[^a-zA-Z]"))
      return;

    input = input.toLowerCase();
    if (Phoneme.isVowel("" + input.charAt(0)))
      return;

    String firstStressedSyllable = firstStressedSyllable(input, useLTS);
    String fC = firstPhoneme(firstStressedSyllable);

    if (fC == null && firstStressedSyllable != null)
      fC = firstStressedSyllable.substring(0, 1);

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
    System.out.println(rl.randomWord("nn"));
    System.out.println(rl.isAlliteration("withdraw", "wind"));
    if (1 == 1)
      return;
    // System.out.println(rl.lastStressedPhoneToEnd("mellow",true));
    System.out.println(rl.lastStressedPhoneToEnd("toy", true));
    System.out.println(rl.lastStressedPhoneToEnd("boy", true));
    System.out.println(rl.lastStressedPhoneToEnd("wellow", true));
    // System.out.println(rl.lexicalData().get("dry"));
  }
}
