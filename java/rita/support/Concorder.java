package rita.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rita.RiTa;

/**
 * Maintains a simple word frequency table for a set of input data
 */
@SuppressWarnings("boxing")
public class Concorder {
  protected static KwicCache kwicCache;

  protected Map<String, Lookup> model;
  protected String[] wordsToIgnore = {};
  protected List<String> words = new ArrayList<String>();
  protected boolean ignoreCase, ignoreStopWords, ignorePunctuation;
  protected int wordCount;

  public Concorder(String text) {
    this(text, null);
  }

  public Concorder(String[] text) {
    this(text, null);
  }

  /**
   * @description Create a new RiConcorder object with text and an optional
   *              'options' object.
   * @param {String} text The words from which to create the model
   * @param {Object} options
   * @param {int} options.wordCount # of words of context on either side of
   *        input word
   * @param {boolean} options.ignoreCase Ignore upper/lower case in the model
   * @param {boolean} options.ignoreStopWords Ignore words like "the", "and",
   *        "a", "of", etc, as specified in RiTa.STOP_WORDS
   * @param {boolean} options.ignorePunctuation Ignore punctuation tokens in the
   *        model
   * @param {String[]} options.wordsToIgnore A set of words (alternative
   *        stop-words, for example) to ignore.
   */
  public Concorder(String text, Map<String, Object> options) {

    this(tokenize(text), options);
  }

  public Concorder(String text[], Map<String, Object> options) {

    this(Arrays.asList(text), options);
  }

  protected Concorder(List<String> text, Map<String, Object> options) {

    if (options != null)
      handleOptions(options);

    if (this.ignoreStopWords)
      this.wordsToIgnore = concat(this.wordsToIgnore, RiTa.STOP_WORDS);

    this.words = text;
  }

  // ///////////////////////////////Start-API//////////////////////////////////////
  /**
   * Returns the # of occurences of <code>word</code> or 0 if the word does not
   * exist in the table.
   */
  public int count(String word) {

    Lookup value = lookup(word);
    return value == null ? 0 : value.count();
  }

  public Map<String, Integer> concordance() {
    if (model == null)
      this.build();
    return sortData(model);
  }

  // A memoizing version of kwic(), rebuilds only when options or text changes
  public static String[] cachedKwic(String text, String word, Map options) {

    if (kwicCache == null || !kwicCache.matches(text, options)) {
      kwicCache = new KwicCache(text, options);
    }
    return kwicCache.concorder.kwic(word, extractWordCount(options));
  }

  public String[] kwic(String word, int numWords) {

    Lookup value = lookup(word);
    
    if (value == null) return new String[0];
    
    Integer[] idxs = value.indexes.toArray(new Integer[0]);
    List<String> result = new ArrayList<String>();

    // Now create a phrase for each index, by appending numWords to either side of the target word
    for (int i = 0; i < idxs.length; i++) {
      
      List<String> sub = words.subList(Math.max(0, idxs[i] - numWords),
	  Math.min(words.size(), idxs[i] + numWords + 1));
      
      // What to do if the word appears more than once in our sub-list? (issue #169)
      // that is, if the next idx is less than numWords ahead...
      if (i < 1 || (idxs[i] - idxs[i - 1]) > numWords)
	result.add(RiTa.untokenize(sub.toArray(new String[0]))); 
    }
    
    return result.toArray(new String[result.size()]);
  }

  // /////////////////////////////End-API//////////////////////////////////////

  protected static List<String> tokenize(String text) {
    List<String> l = new ArrayList<String>();
    RiTokenizer.getInstance(RiTokenizer.PENN_WORD_TOKENIZER).tokenize(text, l);
    return l;
  }

  protected void build() {

    //long ts = System.currentTimeMillis();
    model = new HashMap<String, Lookup>();

    for (int j = 0; j < words.size(); j++) {

      String word = words.get(j);

      if (ignorable(word))
	continue;

      Lookup lookup = lookup(word);
      
      // first time for this word, add it
      if (lookup == null) {
	lookup = new Lookup(word);
	model.put(lookup.key, lookup);
      }
      
      // otherwise, just add the the index
      lookup.indexes.add(j);
    }

    //System.out.println("KWIC: " + (System.currentTimeMillis() - ts) + "ms");
  }

  protected void handleOptions(Map<String, Object> args) {

    Object ignorePunctuationOpt = args.get("ignorePunctuation");
    this.ignorePunctuation = (ignorePunctuationOpt != null) ? (Boolean) (ignorePunctuationOpt)
	: false;

    Object ignoreStopWordsOpt = args.get("ignoreStopWords");
    this.ignoreStopWords = (ignoreStopWordsOpt != null) ? (Boolean) (ignoreStopWordsOpt)
	: false;

    Object ignoreCaseOpt = args.get("ignoreCase");
    this.ignoreCase = (ignoreCaseOpt != null) ? (Boolean) (ignoreCaseOpt)
	: false;

    Object wordCountOpt = args.get("wordCount");
    this.wordCount = (wordCountOpt != null) ? (Integer) (wordCountOpt) : 4;

    Object wordsToIgnoreOpt = args.get("wordsToIgnore");
    this.wordsToIgnore = (wordsToIgnoreOpt != null) ? (String[]) (wordsToIgnoreOpt)
	: new String[0];
  }

  protected boolean ignorable(String key) {

    if (ignorePunctuation && RiTa.isPunctuation(key))
      return true;

    for (int i = 0; i < wordsToIgnore.length; i++) {

      String word = wordsToIgnore[i];

      if (ignoreCase) {

	if (key.equalsIgnoreCase(word))
	  return true;
      } else { // check case

	if (key.equals(word))
	  return true;
      }
    }
    return false;
  }

  protected String compareKey(String word) {

    return ignoreCase ? word.toLowerCase() : word;
  }

  private Lookup lookup(String word) {

    String key = compareKey(word);
    if (model == null)
      this.build();
    return model.get(key);
  }

  static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
      Map<K, V> map) {
    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());

    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list) {
      result.put(entry.getKey(), entry.getValue());
    }

    return result;
  }

  static <K, V extends Comparable<? super V>> Map<String, Integer> sortData(
      Map<K, V> map) {
    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());

    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    Map<String, Integer> result = new LinkedHashMap<String, Integer>();
    for (Map.Entry<K, V> entry : list) {
      Lookup lookup = (Lookup) entry.getValue();
      result.put(lookup.key, lookup.count());
    }

    return result;
  }

  class Lookup implements Comparable<Lookup> {
    String word, key;
    ArrayList<Integer> indexes = new ArrayList<Integer>();

    public Lookup(String s) {
      word = s;
      key = compareKey(s);
    }

    public int count() {
      return indexes.size();
    }

    public int compareTo(Lookup o) {
      return Integer.compare(this.count(), o.count());
    }
  }

  static String[] concat(String[] a, String[] b) {
    if (a == null)
      a = new String[0];
    if (b == null)
      b = new String[0];
    int aLen = a.length, bLen = b.length;
    String[] c = new String[aLen + bLen];
    System.arraycopy(a, 0, c, 0, aLen);
    System.arraycopy(b, 0, c, aLen, bLen);
    return c;
  }

  private static int extractWordCount(Map options) {
    int wordCount = 4;
    if (options != null) {
      Object wc = options.get("wordCount");
      if (wc != null)
	wordCount = ((Integer) wc).intValue();
    }
    return wordCount;
  }

  static class KwicCache {
    Concorder concorder;
    String text, options = "";

    public KwicCache(String t, Map o) {
      this.text = t;
      if (o != null)
	this.options = o.toString();
      this.concorder = new Concorder(t, o);
    }

    public boolean matches(String t, Map o) {
      String mopts = o != null ? o.toString() : "";
      return this.text.equals(t) && this.options.equals(mopts);
    }
  }

  public static void main(String[] args) {
    Map o = new HashMap();
    o.put("ignoreCase", true);
    Concorder ric = new Concorder("The dog ate the cat.", o);
    System.out.println(ric.concordance());
    System.out.println(ric.count("the"));
    System.out.println(ric.count("The"));
    RiTa.out(ric.kwic("atex", 3));
    // System.out.println(ric.build(RiTa.loadString("kafka.txt")));
  }

}//
