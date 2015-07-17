package rita.support;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rita.RiTa;
import rita.RiTaException;

// NEXT: TF-IDF, Sentence Map example, options/boolean-flags

/**
 * Maintains a simple word frequency table for a set of input data
 * 
 * <pre>
 * RiConcorder ric = new RiConcorder(this);
 * ric.setIgnoreCase(false);
 * ric.setIgnoreStopWords(false);
 * ric.setIgnorePunctuation(false);
 * ric.loadFile(&quot;myTestFile.txt&quot;);
 * ric.dump();
 * String[] mostCommon = ric.getMostCommonTokens(5);
 * print(mostCommon);
 * </pre>
 */
@SuppressWarnings("boxing")
public class RiConcorder {

  protected Map<String, Lookup> model;
  //protected Map<String, Integer> output;
  public String[] wordsToIgnore = {};
  
  protected boolean ignoreCase=false, ignoreStopWords=true, ignorePunctuation;
  protected int wordCount;
  protected boolean sorted;
  
  class Lookup implements Comparable<Lookup> {
    int count;
    String word;
    public Lookup(String s) {
      word = s;
      count = 1;
    }
    public int compareTo(Lookup o) {
      return Integer.compare(this.count,o.count);
    }
  }

  public RiConcorder(String text) {
    this(text, null);
  }

  public RiConcorder(String text, Map options) {

    // handle options here
    
    if (ignoreStopWords) {
	wordsToIgnore = RiTa.STOP_WORDS;
    }
    
    this.build(text);
    
  }
  
  // ========================= METHODS =============================

  protected void build(String text) {

    String[] sentences = RiTa.splitSentences(text);
    model = new HashMap<String, Lookup>();
    for (int i = 0; i < sentences.length; i++) {
      String[] words = RiTa.tokenize(sentences[i]);
      for (int j = 0; j < words.length; j++) {
	if (ignorable(words[j]))
	  continue;
	String key = compareKey(words[j]);

	wordCount++;
	Lookup lookup = model.get(key);
	if (lookup != null) {
	  ++lookup.count;
	} else {
	  model.put(key, new Lookup(words[j]));
	}

	// if (word.length() > 3) {
	// String sentenceIdxs = concordance.get(word);
	// if (sentenceIdxs == null)
	// sentenceIdxs = "";
	// sentenceIdxs += i+",";
	// concordance.set(word, sentenceIdxs);
	// }
	
//	wordCount++;
//	Integer val = map.get(word);
//	if (val != null) {
//	  map.put(word, ++val);
//	} else {
//	  map.put(word, 1);
//	}
      }
    }
    //return sortData(model);
    //return output;
  }

  private boolean ignorable(String key) {
    
    for (int i = 0; i < wordsToIgnore.length; i++) {
      
      if (ignoreCase) {
	
	if (key.equalsIgnoreCase(wordsToIgnore[i]))
	  return true;
      }
      else { // check case
	
	if (key.equals(wordsToIgnore[i]))
	  return true;
      }
    }
    return false;
  }

  private String compareKey(String word) {
    
    return ignoreCase ? word.toUpperCase() : word;
  }

  /**
   * Returns the # of occurences of <code>word</code> or 0 if the word does not
   * exist in the table.
   */
  public int count(String word) {
    String key = compareKey(word);
    //System.out.println("key: "+word);
    if (model == null)
      throw new RiTaException("Concordance not initialized");
    Lookup value = model.get(key);
    return value == null ? 0 : value.count;
  }
  
  public int wordCount() {
    return wordCount;
  }
  
  public int uniqueCount() {
    if (model == null)
      throw new RiTaException("Concordance not initialized");
    return model.keySet().size();
  }

  static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) 
  {
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
  
  static <K, V extends Comparable<? super V>> Map<String, Integer> sortData(Map<K, V> map) 
  {
    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
    
    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    Map<String, Integer> result = new LinkedHashMap<String, Integer>();
    for (Map.Entry<K, V> entry : list) {
      Lookup lookup = (Lookup)entry.getValue();
      result.put(lookup.word, lookup.count);
    }
    
    return result;
  }

  public Map<String, Integer> concordance() {    
    return sortData(model);
  }
  
  public static void main(String[] args) {
    RiConcorder ric = new RiConcorder("The dog ate the cat");
    System.out.println(ric.concordance());
    System.out.println(ric.count("the"));
    System.out.println(ric.count("The"));
    //System.out.println(ric.build(RiTa.loadString("kafka.txt")));
  }

}//
