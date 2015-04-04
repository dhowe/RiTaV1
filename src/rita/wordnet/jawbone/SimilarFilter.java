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

/**
 * Provide a filter for search terms that only
 * accepts matches where the term passed to accept()
 * is similar to the string passed in the
 * constructor. <p>
 * 
 * Uses Levenshtein distance to determine similarity
 * 
 * @author mwallace
 * @version 1.0
 */
public final class SimilarFilter extends rita.wordnet.RiFilter
{

  /**
   * The maximum distance to allow matches.
   */
  private int maxScore = 0;
  
  private static int DEFAULT_MAX_SCORE = 5; // ????? base on length?
  
  public SimilarFilter(final String word, final boolean bIgnoreCase) {
    this(word, bIgnoreCase, DEFAULT_MAX_SCORE);
  }
  
  /**
   * Initializes the filter with the source term and
   * whether to ignore case on searches.
   * 
   * @param word the source term
   * @param bIgnoreCase whether to ignore the case of string comparisons
   * @param maxDistance the maximum distance between the two terms
   */
  public SimilarFilter(final String word,
                       final boolean bIgnoreCase,
                       final int maxDistance)
  {
    term = word;
    ignoreCase = bIgnoreCase;
    maxScore = maxDistance;
  }
  
  
  /**
   * Determines if the term matches the source term.
   * 
   * @param word the term to compare to the source term
   * @return whether the terms match
   */
  public boolean accept(final String word)
  {
    if (!super.accept(word)) return false;
    
    // Neither is null, so check how to compare the strings
    int score = 0;
    if (ignoreCase)
    {
      // Ignore the case
      score = getScore(term.toUpperCase(), word.toUpperCase());
    }
    else
    {
      // Consider the case
      score = getScore(term, word);
    }
    
    // Return whether the computed score is at or below the threshold
    return (score <= maxScore);
  }
  
  
  /**
   * Returns the minimum of three integers.
   * 
   * @param a the first number
   * @param b the second number
   * @param c the third number
   * @return the minimum of three numbers
   */
  private static int minimum(final int a, final int b, final int c)
  {
    return (Math.min(a, Math.min(b, c)));
  }
  
  
  // SHOULD USE MinEditDist class ?
  /**
   * Computes and returns the Levenshtein score to indicate
   * how similar the two strings are.
   * 
   * @param s First string to compare
   * @param t Second string to compare
   * @return the Levenshtein rating
   */
  private int getScore(final String s, final String t)
  {
    // Return the Levenshtein rating
    int[][] d; // matrix
    int n; // length of s
    int m; // length of t
    int i; // iterates through s
    int j; // iterates through t
    char s_i; // ith character of s
    char t_j; // jth character of t
    int cost; // cost
    
    // Step 1
    n = s.length();
    m = t.length();
    if (n == 0)
    {
      return m;
    }
    if (m == 0)
    {
      return n;
    }
    d = new int[n+1][m+1];
    
    // Step 2
    for (i = 0; i <= n; i++)
    {
      d[i][0] = i;
    }
    
    for (j = 0; j <= m; j++)
    {
      d[0][j] = j;
    }
    
    // Step 3
    for (i = 1; i <= n; i++)
    {
      s_i = s.charAt(i - 1);
      
      // Step 4
      for (j = 1; j <= m; j++)
      {
        t_j = t.charAt(j - 1);
        
        // Step 5
        if (s_i == t_j)
        {
          cost = 0;
        }
        else
        {
          cost = 1;
        }
        
        // Step 6
        d[i][j] = minimum(d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1] + cost);
      }
    }
    
    // Step 7
    return d[n][m];
  }
  
  public void setMaxDistance(int dist) {
    this.maxScore = dist;
  }

  public int getMaxScore()
  {
    return this.maxScore;
  }

  public void setMaxScore(int maxScore)
  {
    this.maxScore = maxScore;
  }
  
}// end
