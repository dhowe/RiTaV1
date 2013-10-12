/**
 * 
 */
/*
 * Written by Mike Wallace (mfwallace at gmail.com).  Available
 * on the web site http://mfwallace.googlepages.com/.
 * 
 * Copyright (c) 2006 Mike Wallace.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package rita.wordnet.jawbone.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a filter for search terms that only
 * accepts matches where the parameter to accept()
 * contains at least one of the phrases in the
 * string used to initialize this class, where
 * a phrase is either a word delimited by spaces
 * or one or more words surrounded by quotes.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ContainsSomeFilter extends rita.wordnet.RiFilter
{
  /**
   * The list of terms.
   */
  private final List terms;
  
  
  /**
   * Default constructor.
   */
  private ContainsSomeFilter()
  {
    super();
    ignoreCase = false;
    terms = new ArrayList(0);
  }
  
  
  /**
   * Initializes the filter with the source term and
   * whether to ignore case on searches.
   * 
   * @param word the source term
   * @param bIgnoreCase whether to ignore the case of string comparisons
   */
  public ContainsSomeFilter(final String word,
                            final boolean bIgnoreCase)
  {
    super();
    
    // Save whether to ignore the case
    ignoreCase = bIgnoreCase;
    
    // Save the String parameter, after processing
    terms = parseIntoPhrases(word);
  }
  
  
  /**
   * Determines if the term matches the source term.
   * 
   * @param word the term to compare to the source term
   * @return whether the terms match
   */
  public boolean accept(final String word)
  {
    // Check the input
    if (word == null) return false;
    
    // See if we need to ignore case
    final String target = ((ignoreCase) ? word.toLowerCase() : word);
    
    // Iterate over the list of phrases to see if target
    // contains any of them
    boolean found = false;
    final int size = terms.size();
    for (int i = 0; (i < size) && (!found); ++i)
    {
      // See if the current array element contains target
      if (target.indexOf((String)terms.get(i)) >= 0)
        found = true;
    }
    
    // Return whether we found one of the strings
    return found;
  }
}
