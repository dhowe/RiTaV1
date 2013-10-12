package rita.wordnet;

import java.util.ArrayList;
import java.util.List;

import rita.wordnet.jawbone.filter.*;


/**
 * Abstract class implementing the Jawbone TermFilter interface.
 * @invisible
 * @author dhowe
 * See the accompanying documentation for license information
 */
public abstract class RiFilter implements Wordnet, TermFilter
{
  protected static boolean ignoreCase = false;
  
  /**
   * Determines if the term matches the source term. 
   * @param word the term to compare to the source term
   * @return true if the terms match
   */
  public abstract boolean accept(String word);
  
  protected RiFilter() 
  {
    //System.err.println("[INFO] Creating Filter: "+getClass());
  }
  
  // do we cache the individual filters? 
  public static RiFilter create(int flag, String word) 
  {
    //System.err.println("RiFilter.create("+flag+", "+word+")");
    
    RiFilter filter = null;  
    if ((flag & EXACT_MATCH) != 0)  {     
        filter = new ExactMatchFilter(word, ignoreCase);
    }
    else if ((flag & ENDS_WITH) != 0) {
      if (filter == null) 
        // ensures only 1 filter at a time for now
        filter = new EndsWithFilter(word, ignoreCase);
    }
    else if ((flag & STARTS_WITH) != 0) {
      if (filter == null)
        filter = new StartsWithFilter(word, ignoreCase);
    }
    else if ((flag & ANAGRAMS) != 0) {
      if (filter == null)
        filter = new AnagramFilter(word, ignoreCase);
    }
    else if ((flag & CONTAINS_ALL) != 0) {
      if (filter == null)
        filter = new ContainsAllFilter(word, ignoreCase);
    }
    else if ((flag & CONTAINS_SOME) != 0) {
      if (filter == null)
        filter = new ContainsSomeFilter(word, ignoreCase);
    }
    else if ((flag & CONTAINS) != 0) {
      if (filter == null)
        filter = new ContainsFilter(word, ignoreCase);
    }
    else if ((flag & SIMILAR_TO) != 0) {
      if (filter == null)
        filter = new SimilarFilter(word, ignoreCase);
    }
    else if ((flag & SOUNDS_LIKE) != 0) {
      if (filter == null)
        filter = new SoundFilter(word, ignoreCase);
    }
    else if ((flag & WILDCARD_MATCH) != 0) {
      if (filter == null)
        filter = new WildcardFilter(word, ignoreCase);
    }
    else if ((flag & REGEX_MATCH) != 0) {
      if (filter == null)
        filter = new RegexFilter(word, ignoreCase);
    }    

    if (filter == null)
      throw new WordnetError("Invalid filter flag(s): "+flag);
    
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
