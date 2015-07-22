package rita.wordnet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rita.RiWordNet;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;


/**
 * A filter that accepts based upon both the word and its gloss.
 * 
 * @invisible
 * @author dhowe
 */
public class WordnetGlossFilter extends WordnetFilters
{     
  public WordnetGlossFilter(RiWordNet wl) {
    super(wl);
  }

  protected List filter(int filterType, POS pos, int maxResults)
  {
    return this.filter(RiGlossFilter.create(filterType, null), pos, maxResults); 
  }
  

  protected List filter(int filterType, POS pos)
  {
    return this.filter(RiGlossFilter.create(filterType, null), pos, Integer.MAX_VALUE); 
  }
  

  protected List filter(RiGlossFilter filter, POS pos, int maxResults)
  {
    if (pos == null) return null;
  
    Set results = new HashSet();
    Iterator it = iterator(pos);
    WHILE: while (it.hasNext())
    {
      String lemma = nextWord(it);
      if (iw == null) 
        throw new RuntimeException("NULL IW!");
      if (lemma == null || iw == null) 
        continue WHILE;
      //System.out.print("lemma: "+lemma);
      String gloss = nextGloss();
      if (gloss == null) 
        continue WHILE;
      //System.out.println("   "+gloss);
      String result = filter.accept(lemma, gloss);
      if (result != null) {
        
        results.add(result);
        if (results.size()  >= maxResults)  //hmmmm?
          break WHILE;
      }
    }
    List l = new ArrayList(results.size());
    for (Iterator i = results.iterator(); i.hasNext();)
      l.add(i.next());    
    return l;
  }
 
  private IndexWord iw = null; // yuk
/*  private String nextWord(Iterator it)
  {
    this.iw = (IndexWord) it.next();   
    String lemma = iw.getLemma();
    if (lemma == null) return null;
    if (ignoreCompoundWords && RiWordNet.isCompound(lemma))
      return null;
    if (ignoreUpperCaseWords && WordnetUtil.startsWithUppercase(lemma))
      return null;

    return lemma;
  }*/
  
  /**
   * returns the 1st gloss it finds from any sense of the word/pos,
   * or null if none is found...
   */ 
  private String nextGloss()
  {   
    if (this.iw == null) return null;
    int senseCount = iw.getSenseCount();
    if (senseCount < 1) return null;
    String gloss = null;
    Synset[] syns = new Synset[senseCount];
    FOR: for (int i = 0; i < syns.length; i++) {
      try {
        syns[i] = iw.getSense(i + 1);
        if (syns[i] != null) {
          gloss = syns[i].getGloss();
          if (gloss != null) break FOR;
        }
      } 
      catch (JWNLException e) {
        throw new RiWordNetError(e);
      }
    }
    this.iw = null;
    return gloss;
  }
  
  public static void main(String[] args) throws Exception
  {     
    RiWordNet wl = new RiWordNet(null);
    WordnetGlossFilter wf = new WordnetGlossFilter(wl);
    Iterator i = wf.iterator(POS.NOUN);
    String lemma = wf.nextWord(i);
    String gloss = wf.nextGloss();
    System.out.println(lemma +" / "+gloss);
  }

  
}