package rita.wordnet;

import java.util.List;

import rita.RiWordNet;
import rita.wordnet.jwnl.wndata.POS;



/**
 * @invisible
 * @author dhowe
 */
public class WordnetExampleFilter extends RiGlossFilter
{
  /**
   * Initializes the filter with a boolean determining
   * whether to ignore case on searches.
   * 
   * @param bIgnoreCase whether to ignore the case of string comparisons
   */
  public WordnetExampleFilter(final boolean bIgnoreCase)
  {
    ignoreCase = bIgnoreCase;
  }
  
  /**
   * Signals accept by parsing and returning the example
   * phrases from the gloss, else null.
   */
  public String accept(String lemma, String gloss)
  {
    List l = WordnetUtil.parseExamples(gloss);
    if (l != null && l.size()>0) 
      return (String)l.get(0);
    return null; 
  }
  
  /** @invisible */
  public static void main(String[] args) throws Exception
  {      
    generateExampleFile(POS.VERB);
  }
  
  /** @invisible */
  public static void generateExampleFile(POS pos) throws Exception
  {      
    RiWordNet wl = new RiWordNet(null);
    WordnetGlossFilter wf = new WordnetGlossFilter(wl);
    List l = wf.filter(RiWordNet.HAS_EXAMPLE, pos);
    System.out.println(System.getProperty("user.dir"));
/*    FileWriter fw = new FileWriter("../RiTa/src/data/examples/wordnet."+pos.getLabel()+".phrases.txt");
    for (Iterator it = l.iterator(); it.hasNext();) 
      //System.out.println(it.next());
      fw.append((String)it.next()+"\n");
    fw.flush();
    fw.close();
    System.out.println("RESULTS: "+l.size());*/
    
  }
  
}// end
