package rita.wordnet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rita.RiTa;
import rita.RiWordNet;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.dictionary.Dictionary;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;


/**
 * Add to RiFilter as static methods?
 * 
 * @invisible
 * @author dhowe
 */
public class WordnetFilters
{  
  private static final String ERROR_FLAGS_AND_PATTERNS = "[ERROR] must pass equal # of filter flags and patterns";
  
  protected Dictionary dictionary;
  protected Map filterCache;
  protected RiWordNet wordnet;

  public WordnetFilters(RiWordNet wl) {
    
    this.wordnet = wl;
    this.dictionary = wl.getDictionary();
    this.filterCache = new HashMap();
  }
  
  protected List filter(RiFilter filter, POS pos, int maxResults) // impl
  {
    //System.out.println("WordnetFilters.filter("+pos+","+filter+")");
    if (pos == null) return null;
    
    List result = new LinkedList();
    Iterator it = iterator(pos);
    
    while (it.hasNext() && result.size() < maxResults)
    {
      String lemma = nextWord(it);
      if (lemma == null || wordnet._ignorable(lemma)) 
        continue;
      if (filter.accept(lemma)) {
        
        result.add(lemma.replaceAll(RiTa.USC, RiTa.SP));
      }
    }
    return result;
  }
  
  /**
   * Runs the specified <code>filter</code> over the <code>pos</code>
   * returning at most <code>maxResults</code> instances 
   */
  public List filter(int filterFlag, String word, POS pos, int maxResults) { // impl
    
    //System.out.println("WordnetFilters.filter("+filterFlag+","+word+","+pos+","+maxResults+")");
    
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
      throw new RiWordNetError(ERROR_FLAGS_AND_PATTERNS);
    
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
  public List orFilter(RiFilter[] filters, POS pos, int maxResults)
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
          if (result.size()>=maxResults)
            break WHILE;
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
      throw new RiWordNetError(ERROR_FLAGS_AND_PATTERNS);
    
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
  public Iterator lemmaIterator(Dictionary d, POS pos) // TODO: this may be very slow!
  {
    String tag = pos.getKey()+"Set";
    
    // check the Set cache 
    Set result = (Set)filterCache.get(tag);
    
    if (result == null) { 
      
      result = new HashSet();
      Iterator it = iterator(d, pos);
      while (it.hasNext()) {
        IndexWord iw = (IndexWord) it.next();   
        String lemma = iw.getLemma();
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
      throw new RiWordNetError();
    }
    return it;
  }

  // privates ==============================================
  
  protected String nextWord(Iterator it)
  {
    IndexWord iw = (IndexWord) it.next();   
    String lemma = iw.getLemma();

    if (wordnet._ignorable(lemma))
      return null;
    
    return lemma;
  } 
  
}