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

package rita.wordnet.jawbone;

import java.util.regex.Pattern;

/**
 * Provide a filter for search terms that only
 * accepts matches where the term passed to accept()
 * matches the regular expression string passed in
 * the constructor.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class RegexFilter extends rita.wordnet.RiFilter
{
  /**
   * The pattern for the source term.
   */
  private Pattern pattern = null;  
  
  /**
   * Initializes the filter with the source term and
   * whether to ignore case on searches.
   * 
   * @param word the source term
   * @param bIgnoreCase whether to ignore the case of string comparisons
   */
  public RegexFilter(final String word, final boolean bIgnoreCase)
  {
    // Check the input
    if (word == null)
    {
      pattern = null;
    }
    else
    {
      // Check if we're ignoring case
      if (bIgnoreCase)
      {
        pattern = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
      }
      else
      {
        pattern = Pattern.compile(word);
      }
    }
  }
  
  
  /**
   * Determines if the term matches the source term.
   * 
   * @param word the term to compare to the source term
   * @return whether the terms match
   */
  public boolean accept(final String word)
  {
    // Check the two terms for nullness
    if ((word == null) && (pattern == null))
    {
      // They're both null
      return true;
    }
    else if ((word == null) || (pattern == null))
    {
      // One is null, the other is not
      return false;
    }
    
    // Use the regex package to compare words
    return (pattern.matcher(word).matches());
  }
}
