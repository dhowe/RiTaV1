package rita.wordnet;

import java.util.ArrayList;
import java.util.List;

import rita.RiWordNet;
import rita.wordnet.jawbone.AnagramFilter;
import rita.wordnet.jawbone.ContainsFilter;
import rita.wordnet.jawbone.EndsWithFilter;
import rita.wordnet.jawbone.RegexFilter;
import rita.wordnet.jawbone.SimilarFilter;
import rita.wordnet.jawbone.SoundFilter;
import rita.wordnet.jawbone.StartsWithFilter;
import rita.wordnet.jawbone.TermFilter;
import rita.wordnet.jawbone.WildcardFilter;

// TODO: Need to document filters well in JSON (K)

/**
 * Abstract class implementing the Jawbone TermFilter interface.
 * See the accompanying documentation for license information
 */
public abstract class RiFilter implements TermFilter
{
  protected static boolean ignoreCase = false; // this smells...
  
  /**
   * The source term.
   */
  protected String term = null;
  
  /**
   * Determines if the term matches the source term. 
   * @param word the term to compare to the source term
   * @return true if the terms match
   */
  public boolean accept(String word) {
    
    return !(word == null || term == null || word.equals(term));
  }
  
  protected RiFilter() 
  {
    //System.err.println("[INFO] Creating Filter: "+getClass());
  }
  
  // do we cache the individual filters? 
  public static RiFilter create(int flag, String word) 
  {
    //System.err.println("RiFilter.create("+flag+", "+word+")");
    
    RiFilter filter = null;  
    
    //if ((flag & EXACT_MATCH) != 0)  {     
      //  filter = new ExactMatchFilter(word, ignoreCase);
    //}
    if ((flag & RiWordNet.ENDS_WITH) != 0) {
        // ensures only 1 filter at a time for now
        filter = new EndsWithFilter(word, ignoreCase);
    }
    else if ((flag & RiWordNet.STARTS_WITH) != 0) {
        filter = new StartsWithFilter(word, ignoreCase);
    }
    else if ((flag & RiWordNet.ANAGRAMS) != 0) {
        filter = new AnagramFilter(word, ignoreCase);
    }
/*    else if ((flag & CONTAINS_ALL) != 0) {
        filter = new ContainsAllFilter(word, ignoreCase);
    }
    else if ((flag & CONTAINS_SOME) != 0) {
        filter = new ContainsSomeFilter(word, ignoreCase);
    }*/
    else if ((flag & RiWordNet.CONTAINS) != 0) {
        filter = new ContainsFilter(word, ignoreCase);
    }
    else if ((flag & RiWordNet.SIMILAR_TO) != 0) {
        filter = new SimilarFilter(word, ignoreCase);
    }
    else if ((flag & RiWordNet.SOUNDS_LIKE) != 0) {
        filter = new SoundFilter(word, ignoreCase);
    }
    else if ((flag & RiWordNet.WILDCARD_MATCH) != 0) {
        filter = new WildcardFilter(word, ignoreCase);
    }
    else if ((flag & RiWordNet.REGEX_MATCH) != 0) {
        filter = new RegexFilter(word, ignoreCase);
    }    

    if (filter == null)
      throw new RiWordNetError("Invalid filter flag(s): "+flag);
    
    //filter.ignoreCase = ignoreCase;
    
    return filter;
  }
  
  /**
   * Parse the string into a list of phrases.  A phrase
   * is either a standalone word from the input string,
   * or a quoted string.
   * 
   * @param sInputTerm the string to parse
   * @return the list of phrases in the word
   */
  protected List parseIntoPhrases(final String sInputTerm)
  {
    // Check the input
    if (sInputTerm == null)
      return new ArrayList(0);
    
    final String sInput = sInputTerm.trim();
    
    if (sInput.length() < 1) 
      return new ArrayList(0);
    
    // Allocate an array to hold the items
    List list = new ArrayList();    
    
    // Build the list
    int i = 0;
    final int nLen = sInput.length();
    boolean inQuote = false;
    StringBuilder sb = new StringBuilder(200);
    while (i < nLen)
    {
      // Save the character
      char ch = sInput.charAt(i);
      
      // Check for a leading quote
      if (ch == '"')
      {
        // See if we're already inside a quote
        if (inQuote)
        {
          list.add(sb.toString());
        }
        
        inQuote = !inQuote;
        sb.setLength(0);
        ++i;
      }
      else if (ch == '\\')
      {
        // Go to the next character
        ++i;
        
        // See if we're at the end
        if (i >= nLen)
        {
          break;
        }
        else
        {
          ch = sInput.charAt(i);
          switch (ch)
          {
            case 'n': sb.append('\n');
                      break;
            case 't': sb.append('\t');
                      break;
            case 'r': sb.append('\r');
                      break;
            default : sb.append(ch);
                      break;
          }
        }
        
        ++i;
      }
      else if (ch == ' ')
      {
        // See if we're in quotes
        if (inQuote)
        {
          // Save the space
          sb.append(ch);
        }
        else
        {
          // Break on spaces, so save the current word, if
          // there is one
          if (sb.length() > 0)
          {
            list.add(sb.toString());
            sb.setLength(0);
          }
        }
        
        ++i;
      }
      else
      {
        // Just add the character
        sb.append(ch);
        ++i;
      }
    }
    
    // Check if sb has any leftover strings
    if (sb.length() > 0) list.add(sb.toString());
    
    return list;
  }

} // end
