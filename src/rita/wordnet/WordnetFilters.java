package rita.wordnet;

import java.util.*;

import rita.RiWordnet;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.data.IndexWord;
import rita.wordnet.jwnl.data.POS;
import rita.wordnet.jwnl.dictionary.Dictionary;


/**
 * Add to RiFilter as static methods?
 * 
 * @invisible
 * @author dhowe
 */
public class WordnetFilters implements Wordnet
{  
  protected Dictionary dictionary;
  protected boolean ignoreCompoundWords;
  protected boolean ignoreUpperCaseWords;
  protected Map filterCache;

  public WordnetFilters(RiWordnet wl) {
    this.dictionary = wl.getDictionary();
    this.filterCache = new HashMap();
    this.ignoreCompoundWords = wl.isIgnoringCompoundWords();
    this.ignoreUpperCaseWords = wl.isIgnoringUpperCaseWords();
  }
  
  protected List filter(RiFilter filter, POS pos, int maxResults)
  {
    //System.out.println("WordnetFilters.filter("+pos+","+filter+")");
    if (pos == null) return null;
    List result = new LinkedList();
    Iterator it = iterator(pos);
    WHILE: while (it.hasNext())
    {
      String lemma = nextWord( it);
      if (lemma == null) continue WHILE;
      if (filter.accept(lemma))
        result.add(lemma);
    }
    return result;
  }
  
  /**
   * Runs the specified <code>filter</code> over the <code>pos</code>
   * returning at most <code>maxResults</code> instances 
   */
  public List filter(int filterFlag, String word, POS pos, int maxResults) {
    RiFilter filter = RiFilter.create(filterFlag, word);      
    return filter(filter, pos, maxResults);
  }
  
  /**
   * Runs the specified <code>filter</code> over the <code>pos</code> 
   */
  public List filter(int filterFlag, String word, POS pos) {
    return this.filter(filterFlag, word, pos, Integer.MAX_VALUE);
  }
  
  /**
   * Runs the specified <code>filters</code> over the 
   * <code>pos</code> accepting if ANY of the filters accept,
   * returning at most <code>maxResults</code> instances 
   * @see RiFilter#accept(String)
   */
  public List orFilter(int[] filterFlags, String[] filterPatterns, POS pos, int maxResults)
  {
    if (filterFlags.length != filterPatterns.length)
      throw new IllegalArgumentException("[ERROR] must pass equal # of flags and patterns!");
    
    RiFilter[] filters = new RiFilter[filterFlags.length];
    for (int i = 0; i < filterPatterns.length; i++)
      filters[i] = RiFilter.create(filterFlags[i], filterPatterns[i]);
    return orFilter(filters, pos, maxResults);
  }
  
  /**
   * Runs the specified <code>filters</code> over the 
   * <code>pos</code> accepting if ANY of the filters accept,
   * returning at most <code>maxResults</code> instances 
   * @see RiFilter#accept(String)
   */
  public List orFilter(RiFilter[] filters, POS pos, int MaxResults)
  {
    if (pos == null) return null;
    List result = new LinkedList();
    Iterator it = iterator(pos);
    
    WHILE: while (it.hasNext())
    {
      String lemma = nextWord(it);
      if (lemma == null)  continue WHILE;
      // check that we accept on every filter
      for (int i = 0; i < filters.length; i++) {
        if (filters[i].accept(lemma)) {
          result.add(lemma);
          continue WHILE;
        }
      }      
    }
    return result;
  }
  
  /**
   * Runs the specified <code>filters</code> over the 
   * <code>pos</code> accepting if ANY of the filters accept. 
   * @see RiFilter#accept(String)
   */
  public List orFilter(RiFilter[] filters, POS pos)
  {
    return this.orFilter(filters, pos, Integer.MAX_VALUE);
  }
  
  /**
   * Runs the specified <code>filters</code> over the 
   * <code>pos</code> accepting only when ALL filters accept,
   * returning at most <code>maxResults</code> instances 
   * @see RiFilter#accept(String) 
   */
  public List andFilter(int[] filterFlags, String[] filterPatterns, POS pos, int maxResults)
  {
    if (filterFlags.length != filterPatterns.length)
      throw new IllegalArgumentException("[ERROR] must pass equal # of flags and patterns!");
    
    RiFilter[] filters = new RiFilter[filterFlags.length];
    for (int i = 0; i < filterPatterns.length; i++)
      filters[i] = RiFilter.create(filterFlags[i], filterPatterns[i]);
    return andFilter(filters, pos, maxResults);
  }
  
  /**
   * Runs the specified <code>filters</code> over the 
   * <code>pos</code> accepting only when ALL filters accept,
   * returning at most <code>maxResults</code> instances 
   * @see RiFilter#accept(String) 
   */
  public List andFilter(RiFilter[] filters, POS pos, int maxResults)
  {
    if (pos == null) return null;
    List result = new LinkedList();
    Iterator it = iterator(pos);
    WHILE: while (it.hasNext())
    {
      String lemma = nextWord(it);
      if (lemma == null) continue WHILE;
      
      // check that we accept on every filter
      for (int i = 0; i < filters.length; i++) {
        if (!filters[i].accept(lemma))
          continue WHILE;
        result.add(lemma);
      }      
    }
    return result;
  }
  /**
   * Runs the specified <code>filters</code> over the 
   * <code>pos</code> accepting only when ALL filters accept
   * @see RiFilter#accept(String)
   */
  public List andFilter(RiFilter[] filters, POS pos)
  {
    return this.andFilter(filters, pos, Integer.MAX_VALUE);
  }  

  // returns an iterator over all IndexWords for 'pos'
  protected Iterator iterator(POS pos)
  {
    return iterator(dictionary, pos);
  }
  
  // returns an iterator over all Lemmas for for 'pos'
  public Iterator lemmaIterator(Dictionary d, POS pos)
  {
    String tag = pos.getKey()+"Set";
    // check the Set cache 
    Set result = (Set)filterCache.get(tag);
    if (result == null) { 
      result = new HashSet();
      Iterator it = iterator(d, pos);
      while (it.hasNext()) {
       String lemma = nextWord(it);
        if (lemma != null) 
          result.add(lemma);
      }
      // cache the Set
      filterCache.put(tag, result);
    }
    return result.iterator();
  }
  
  static Iterator iterator(Dictionary d, POS pos)
  {
    Iterator it = null;
    try {
      it = d.getIndexWordIterator(pos);
    } 
    catch (JWNLException e)
    {
      throw new WordnetError();
    }
    return it;
  }

  // privates ==============================================
  
  private String nextWord(Iterator it)
  {
    IndexWord iw = (IndexWord) it.next();   
    String lemma = iw.getLemma();
    if (lemma == null) return null;
    if (ignoreCompoundWords && RiWordnet.isCompound(lemma))
      return null;
    if (ignoreUpperCaseWords && WordnetUtil.startsWithUppercase(lemma))
      return null;
    return lemma;
  } 
  
}