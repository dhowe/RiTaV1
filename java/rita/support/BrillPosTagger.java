package rita.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import rita.RiTa;
import rita.RiTaException;

/*
 * TODO: 
 *   BAD TAG EXAMPLES:
 *      'The quick brown fox jumped over the frightened(vbn) dog.'
 */

/**
 * Simple transformation-based pos-tagger for the RiTa libary using the Penn
 * tagset
 * <p>
 *
 * Uses the Brill data set with a minimal subset of the original
 * context-sensitive transformations (plus some custom additions.)
 * <p>
 * 
 * For more info see: Brill (1995) 'Unsupervised Learning of Disambiguation
 * Rules for Part of Speech Tagging'
 * <p>
 * The full Penn tag set follows:
 * <ol>
 * <li><b><code>cc</code> </b> Coordinating conjunction
 * <li><b><code>cd</code> </b> Cardinal number
 * <li><b><code>dt</code> </b> Determiner
 * <li><b><code>ex</code> </b> Existential there
 * <li><b><code>fw</code> </b> Foreign word
 * <li><b><code>in</code> </b> Preposition/subord. conjunction
 * <li><b><code>jj</code> </b> Adjective
 * <li><b><code>jjr</code> </b> Adjective, comparative
 * <li><b><code>jjs</code> </b> Adjective, superlative
 * <li><b><code>ls</code> </b> List item marker
 * <li><b><code>md</code> </b> Modal
 * <li><b><code>nn</code> </b> Noun, singular or mass
 * <li><b><code>nns</code> </b> Noun, plural
 * <li><b><code>nnp</code> </b> Proper noun, singular
 * <li><b><code>nnps</code> </b> Proper noun, plural
 * <li><b><code>pdt</code> </b> Predeterminer
 * <li><b><code>pos</code> </b> Possessive ending
 * <li><b><code>prp</code> </b> Personal pronoun
 * <li><b><code>prp$</code> </b> Possessive pronoun
 * <li><b><code>rb</code> </b> Adverb
 * <li><b><code>rbr</code> </b> Adverb, comparative
 * <li><b><code>rbs</code> </b> Adverb, superlative
 * <li><b><code>rp</code> </b> Particle
 * <li><b><code>sym</code> </b> Symbol (mathematical or scientific)
 * <li><b><code>to</code> </b> to
 * <li><b><code>uh</code> </b> Interjection
 * <li><b><code>vb</code> </b> Verb, base form
 * <li><b><code>vbd</code> </b> Verb, past tense
 * <li><b><code>vbg</code> </b> Verb, gerund/present participle
 * <li><b><code>vbn</code> </b> Verb, past participle
 * <li><b><code>vbp</code> </b> Verb, non-3rd ps. sing. present
 * <li><b><code>vbz</code> </b> Verb, 3rd ps. sing. present
 * <li><b><code>wdt</code> </b> wh-determiner
 * <li><b><code>wp</code> </b> wh-pronoun
 * <li><b><code>wp$</code> </b> Possessive wh-pronoun
 * <li><b><code>wrb</code> </b> wh-adverb
 * <li><b><code>#</code> </b> Pound sign
 * <li><b><code>$</code> </b> Dollar sign
 * <li><b><code>.</code> </b> Sentence-final punctuation
 * <li><b><code>,</code> </b> Comma
 * <li><b><code>:</code> </b> Colon, semi-colon
 * <li><b><code>(</code> </b> Left bracket character
 * <li><b><code>)</code> </b> Right bracket character
 * <li><b><code>"</code> </b> Straight double quote
 * <li><b><code>`</code> </b> Left open single quote
 * <li><b><code>"</code> </b> Left open double quote
 * <li><b><code>'</code> </b> Right close single quote
 * <li><b><code>"</code> </b> Right close double quote
 * <li><b><code>-</code> </b> Right close double quote
 * </ol>
 */
public class BrillPosTagger implements Constants {

  static final boolean DBUG = false;

  static final Pattern number = Pattern.compile("[0-9\\.][0-9\\.]*");

  private static JSONLexicon lexicon;
  private static BrillPosTagger instance;

  public static BrillPosTagger getInstance() {
    if (instance == null)
      instance = new BrillPosTagger();
    return instance;
  }

  private BrillPosTagger() {

    lexicon = JSONLexicon.getInstance();
    if (lexicon == null && !RiTa.SILENT)
      System.err.println("RiTa lexicon appears to be missing! "
	  + "Part-of-speech tagging (at least) will be inaccurate");
  }

  public String[] tagFile(String fileName) {

    return this.tagFile(fileName, null);
  }

  /**
   * Loads a file, splits the input into sentences and returns a String[] of the
   * most probably tags.
   */
  public String[] tagFile(String fileName, Object pApplet) {

    List<String> result = new ArrayList<String>();
    String text = RiTa.loadString(fileName, pApplet);

    String[] sents = RiTa.splitSentences(text);
    for (int i = 0; i < sents.length; i++) {
      // System.out.println("BrillPosTagger: "+sen);
      String[] words = RiTa.tokenize(sents[i]);
      tag(words, result);
    }

    String[] tmp = new String[result.size()];
    return result.toArray(tmp);
  }

  /**
   * Returns the part(s)-of-speech from the Penn tagset for a single word
   * 
   * @param word
   *          String
   * @see #tag(String[])
   * @return String (or String[])
   */
  public String tag(String word) {
    word = word.trim(); // ???
    if (word.indexOf(" ") > -1) // check for/strip punct too?
      throw new RiTaException("Method accepts only single words!");
    String[] result = tag(new String[] { word });
    if (result == null || result.length < 1)
      return null;
    return result[0];
  }

  /**
   * Tags the word (as usual) with a part-of-speech from the Penn tagset, then
   * returns the corresponding part-of-speech for WordNet from the set { 'n',
   * 'v', 'a', 'r', '-'} as a String.
   * 
   * @param word
   * @see #tag
   */
  public String tagForWordNet(String word) {
    return RiPos.posToWordNet(tag(word));
  }

  /**
   * Adds the parts-of-speech from the Penn tagset to the result List.
   * 
   * @param words
   *          String[]
   */
  protected void tag(String[] words, List<String> result) {
    // System.out.println("BrillPosTagger.tag("+Util.asList(words)+")");
    String[] tmp = tag(words);
    for (int j = 0; j < tmp.length; j++)
      result.add(tmp[j]);
  }

  /**
   * Returns an array of parts-of-speech from the Penn tagset each corresponding
   * to one word of input.
   * 
   * @param words
   *          String[]
   * @return String[]
   */
  public String[] tag(String[] words) {
    
    if (DBUG) System.out.println("Tagging: " + RiTa.asList(words));

    String[] result = new String[words.length];
    String[][] choices = new String[words.length][];
    for (int i = 0, size = words.length; i < size; i++) {
      String word = words[i];

      if (word.length() < 1) {
	result[i] = E;
	continue;
      }

      if (word.length() == 1) {

	result[i] = handleSingleLetter(word);
	continue;
      }

      String[] data = lookup(word);

      if (DBUG) System.out.println("  "+words[i] + " -> " + RiTa.asList(data) + " "
	    + data.length);
      
   // use stemmer categories if no lexicon
      if (data == null || data.length == 0) {
        
	// choices[i] = word.endsWith("s") ? NOUNP : NOUN;
	result[i] = word.endsWith("s") ? "nns" : "nn";

	if (word.endsWith("s")) {
	  String sub = word.substring(0, words[i].length() - 1);
	  String sub2 = word.endsWith("es")
	      ? words[i].substring(0, words[i].length() - 2) : null;
	  if (lexContains(RiPos.N, sub) || lexContains(RiPos.N, sub2)) {
	    choices[i] = new String[] { "nns" };
	  } else {
	    String sing = RiTa.singularize(word);
	    if (lexContains(RiPos.N, sing))
	      choices[i] = new String[] { "nns" };
	  }

	} else {
	  String sing = RiTa.singularize(word);
	  if (lexContains(RiPos.N, sing)) {
	    choices[i] = new String[] { "nns" };
	    result[i] = "nns";
	  }
	}

      } else {

	result[i] = data[0];
	choices[i] = data;

      }
    }
  
    // adjust pos according to transformation rules
    this.applyContext(words, result, choices);

    return result;
  }

  protected String handleSingleLetter(String word) {

    String result = word;

    char c = word.charAt(0);
    if (c == 'a' || c == 'A')
      result = "dt";
    else if (c == 'I')
      result = "prp";
    else if (Character.isDigit(c))
      result = "cd";

    // System.out.println("handleSingleLetter("+word+") :: "+result);

    return result;
  }

  public String[] lookup(String word) {
    if (lexicon == null)
      return null;

    String[] posArr = lexicon.getPosArr(word);

    if (DBUG) System.out.println("  lookup(" + word + ") -> "
	  + RiTa.asList(posArr));

    return posArr;
  }

  /**
   * Applies a customized subset of the Brill transformations
   */
  protected void applyContext(String[] words, String[] result, String[][] choices) {
    
    if (DBUG) System.out.println("  applyContext(" + Arrays.asList(words)
	  + "," + Arrays.asList(result) + ")");
   
    // Apply transformations
    for (int i = 0; i < words.length; i++) {

      String word = words[i], tag = result[i];

      if (DBUG)
	System.out.println("  " + i + ") pre: " + word + " -> " + tag);

      // transform 1a: DT, {VBD | VBP | VB} --> DT, NN
      if (i > 0 && result[i - 1].equals("dt")) {
	if (tag.startsWith("vb")) {

	  tag = "nn";

	  // transform 7: if a word has been categorized as a common noun and it
	  // ends with "s", then set its type to plural common noun (NNS)
	  if (word.matches(".*[^s]s$")) { // added dch

	    if (!NULL_PLURALS.applies(word)) // added dch
	      tag = "nns";
	  }

	  customTagged("1a", word, tag);
	}

	// transform 1b: DT, {RB | RBR | RBS} --> DT, {JJ | JJR | JJS}
	else if (tag.startsWith("rb")) {// added -dch

	  tag = "jj";

	  if (tag.length() > 2)
	    tag += tag.charAt(2);

	  customTagged("1b", word, tag);
	}
      }
      // transform 2: convert a noun to a number (cd)
      // if it is all digits and/or a decimal "."
      if (tag.startsWith("n") && choices[i] == null) {
	if (isNum(word))
	  tag = "cd"; // mods: dch (add choice check above)
      }

      // transform 3: convert a noun to a past participle if word ends with "ed"
      if (i > 0 && tag.startsWith("n") && word.endsWith("ed") && !word.endsWith("eed")
	  && in(result[i - 1], "nn", "nns", "nnp", "nnps", "prp")) {
	tag = "vbn";
      }

      // transform 4: convert any type to adverb if it ends in "ly";
      if (word.endsWith("ly")) {
	tag = "rb";
      }

      // transform 5: convert a common noun (NN or NNS) to a adjective if it
      // ends with "al"
      if (tag.startsWith("nn") && word.endsWith("al") && !word.equals("mammal"))
	tag = "jj"; // special-case for mammal

      // transform 6: convert a noun to a verb if the preceding word is "would"
      if (i > 0 && tag.startsWith("nn") && result[i - 1].startsWith("md"))
	tag = "vb";

      // transform 8: convert a common noun to a present participle verb (i.e.,
      // a gerund)
      if (tag.startsWith("nn") && word.endsWith("ing")) {

	// DH: fix here -- added check on choices for any verb: eg 'morning'
	if (hasTag(choices[i], "vb")) {
	  tag = "vbg";
	  customTagged(8, word, tag);
	}
      }

      // transform 9(dch): convert plural nouns (which are also 3sg-verbs) to
      // 3sg-verbs when following a singular noun (the boy jumps, the dog
      // dances)
      if (i > 0 && tag.equals("nns") && hasTag(choices[i], "vbz")
	  && in(result[i - 1], "nn", "prp", "cc", "nnp")) {
	tag = "vbz";
	customTagged(9, word, tag);
      }

      // transform 10 (dch): convert common nouns to proper nouns when they
      // start w' a capital
      if (tag.startsWith("nn") && Character.isUpperCase(word.charAt(0))) {
	// if it is not at the start of a sentence or it is the only word
	// or when it is at the start of a sentence but can't be found in the
	// dictionary
	if (i != 0 || words.length == 1 || (i == 0
	    && !lexContains(RiPos.N, RiTa.singularize(word).toLowerCase()))) {
	  tag = tag.endsWith("s") ? "nnps" : "nnp";
	  customTagged(10, word, tag);
	}
      }

      // transform 11(dch): convert plural nouns (which are also
      // 3sg-verbs) to 3sg-verbs when followed by an adverb (jumps, dances)
      if (i < result.length - 1 && tag.equals("nns")
	  && result[i + 1].startsWith("rb") && hasTag(choices[i], "vbz")) {

	tag = "vbz";
	customTagged(11, word, tag);
      }

      // transform 12(dch): convert plural nouns which have an entry for their
      // base form to vbz
      if (tag.equals("nns")) {

	// if only word and not in lexicon OR word is preceded by ["nn", "prp",
	// "cc", "nnp"]

	if ((words.length == 1 && choices[i] == null)
	    || (i > 0 && in(result[i - 1], "nn", "prp", "cc", "nnp"))) {
	  // if word is ends with es or s and is 'nns' and has a vb

	  if (word.endsWith("es")
	      && lexContains(RiPos.VB, word.substring(0, word.length() - 2))
	      || word.endsWith("s") && lexContains(RiPos.VB,
		  word.substring(0, word.length() - 1))) {
	    tag = "vbz";
	    customTagged(12, word, tag);
	  }
	}
      }

      // transform 13(cqx): convert a vb/ potential vb to vbp when following nns
      // (Elephants dance, they dance)
      if (tag.equals("vb") || (tag.equals("nn") && hasTag(choices[i], "vb"))) {
	if (i > 0 && result[i - 1].matches("^(nns|nnps|prp)$")) {
	  tag = "vbp";
	  customTagged(13, word, tag);
	}
      }

      result[i] = tag;
    }
  }

  boolean lexContains(String ... words) {
   return lexContains(null, words);
  }

  boolean lexContains(RiPos pos, String ... words) {
    
    for (int i = 0; i < words.length; i++) {
      
      if (lexicon.contains(words[i])) {
	
	if (pos == null) return true;
	
	String[] tags = lexicon.getPosArr(words[i]);
	for (int j = 0; j < tags.length; j++) {
	  
	  if (pos == RiPos.N && RiPos.isNoun(tags[j])   ||
	      pos == RiPos.V && RiPos.isVerb(tags[j])   ||
	      pos == RiPos.R && RiPos.isAdverb(tags[j]) ||
	      pos == RiPos.A && RiPos.isAdj(tags[j])    ||
	      pos.getTag().equals(tags[j])) 
	  {
	    return true;
	  }
	}
      }
    }
    return false;
  }

  private void customTagged(int i, String from, String to) {
     customTagged(i+"", from, to);
  }
  
  private void customTagged(String i, String from, String to) {
    if (DBUG) System.out.print("\n  Custom(" + i + ") tagged '" + 
	from + "' -> '" + to + "'\n\n");
  }

  // Is this tag in the array of tags ?
  public static boolean in(String tag, String... tags) {
    return Arrays.asList(tags).contains(tag);
  }

  private static boolean isNum(String word) {
    for (int j = 0; j < word.length(); j++) {
      char c = word.charAt(j);
      if (!(Character.isDigit(c) || c == '.' || c == '-')) {
	return false;
      }
    }
    return true;
  }

  private boolean hasTag(/* String word, */String[] choices, String tag) {
    String choiceStr = RiTa.join(choices);
    // System.err.println("RiPosTagger.canBeVerb("+word+","+choiceStr+")");
    boolean hasTag = choiceStr.indexOf(tag) > -1;
    // System.err.println("VERB! "+verb);
    return hasTag;
  }

  /*
   * public static void mainXX(String[] args) { RiLexicon lex = new RiLexicon();
   * BrillPosTagger ft = new BrillPosTagger(); RiWordnet rw = new RiWordnet();
   * int count = 0; for (Iterator i = lex.iterator(); i.hasNext();) { String
   * word = (String) i.next(); if (rw.exists(word)) if (++count % 1000==0)
   * System.out.println(count); } System.out.println("MATCHES: "+count); }
   */

  /** Returns a String with pos-tags notated inline */
  public String tagInline(String[] tokens) {
    String[] tags = tag(tokens);
    return PosTagger.inlineTags(tokens, tags);
  }

  // not used for now
  private String tagInline(String sentence) {

    if (sentence == null || sentence.length() < 1)
      return E;

    int count = 0;
    int cursor = sentence.length() - 1;
    char last = sentence.charAt(cursor);
    while (RiTa.PUNCT_CHARS.indexOf(last) > -1) {
      last = sentence.charAt(--cursor);
      count++;
    }
    String end = " ";
    int lastPunct = sentence.length() - count;
    end += sentence.substring(lastPunct);
    sentence = sentence.substring(0, lastPunct);

    return (tagInline(RiTa.tokenize(sentence)) + end).trim();
  }

  /** @exclude */
  public static void tests() {
    boolean failed = false;
    BrillPosTagger ft = new BrillPosTagger();
    for (int i = 0; i < tests.length; i += 2) {
      System.out.print(i / 2 + ": ");
      String[] words = RiTa.tokenize(tests[i]);
      String tags = RiTa.join(ft.tag(words));
      String expected = tests[i + 1];

      if (!tags.equals(expected)) {
	System.out.println(RiTa.asList(words) + "\nFAILED: expected '"
	    + expected + "'\n        but got: '" + tags + "'");
	failed = true;
	// break;
      } else
	System.out.println("ok");
    }
    if (!failed)
      System.out.println("All Tests OK!\n");
  }

  static String[] tests = {

      "I run to school.",
      "prp vb to nn .",
      "I went for a run.",
      "prp vbd in dt nn .",
      // "Red is a beautiful color", "nn vbz dt jj nn", // fails!
      "The little boy jumps quickly over the great fence and dances happily.",
      "dt jj nn vbz rb in dt jj nn cc vbz rb .",
      "The little boy jumps quickly", "dt jj nn vbz rb",
      "The little boy dances happily", "dt jj nn vbz rb",
      "The little boy jumped 3 times", "dt jj nn vbd cd nns",
      "The little boy jumps", "dt jj nn vbz", "The little boy jumps wildly",
      "dt jj nn vbz rb", "The little boy jumps rope", "dt jj nn vbz nn",
      "I woke up early that morning", "prp vbd in rb in nn", };

  public static void main(String[] args) {
    BrillPosTagger ft = new BrillPosTagger();
    RiTa.out(ft.tag("flunks".split(" ")));
    //RiTa.out(ft.tag("He flunks the test".split(" ")));
//    System.out.println(ft.lexContains("flunked","flunking"));
//    System.out.println(ft.lexContains(RiPos.V, "flunked","flunking"));
//    System.out.println(ft.lexContains(RiPos.V, "flunkedx","flunkingx"));
//    System.out.println(ft.lexContains(RiPos.VBD, "flunked","flunking"));
//    // System.out.println(ft.tag("I"));
    //tests();

    if (1 == 1) return;
    System.out.println(ft.tag("bronchitis"));
    System.out.println(ft.tag("cleanliness"));
    System.out.println(ft.tag("orderliness"));

    System.out.println(RiTa.asList(ft.tag("is it nourishing".split(" "))));
    System.out.println(ft.tag("gets"));
    if (1 == 1)
      return;
    // String[] test = {"small", "modest", "young"};
    String[] test = { "gets", "nourishing", "young" };
    for (int i = 0; i < test.length; i++) {
      System.out.println(i + ") " + ft.tag(test[i]));
    }
    tests();
    // RiLexicon rl = new RiLexicon();

    /*
     * System.out.println(rl.getFeatures("jumps")); ///RiTaLexicon rl =
     * RiTaLexicon.getInstance(); //System.out.println(rl.getFeatures("jumps"));
     * 
     * // System.out.println("dog="+ft.tag("dog")); String[] s = RiTa.tokenize(
     * "One morning, the man, stricken with fear, cried sadly like 2 little babies"
     * ); // System.out.println(RiTa.asList(s)); String[] tags = ft.tag(s);
     * System.out.println(RiTa.asList(tags));
     */

  }
}// end

