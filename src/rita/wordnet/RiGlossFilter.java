package rita.wordnet;

import rita.RiWordNet;

/**
 * @invisible
 * @author dhowe
 */
public abstract class RiGlossFilter
{
  protected static boolean ignoreCase = false;
    
  public abstract String accept(String lemma, String gloss);
  
  // accepts nothing without the gloss
  public boolean accept(String lemma)
  {
    return false;
  }
  
  public static RiGlossFilter create(int flag, String word) 
  {
    //System.err.println("RiFilter.create("+flag+", "+word+")");
    
    RiGlossFilter filter = null;  
    if ((flag & RiWordNet.HAS_EXAMPLE) != 0) {
        filter = new WordnetExampleFilter(ignoreCase);
    }

    if (filter == null)
      throw new RiWordNetError("Invalid filter flag(s): "+flag);
    
    return filter;
  }
  

  
}// end
