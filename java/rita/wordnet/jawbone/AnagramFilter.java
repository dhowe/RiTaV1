package rita.wordnet.jawbone;

import java.util.Arrays;

import rita.RiTa;

/**
 * Provide a filter for search terms that only
 * accepts matches where the parameter to accept()
 * is an anagram of the source term (passed in the
 * constructor).
 * @invisible
 */
public final class AnagramFilter extends rita.wordnet.RiFilter
{
  /**
   * The source term.
   */
  private final String chars;
  
  /**
   * Initializes the filter with the source term and
   * whether to ignore case on searches.
   * 
   * @param word the source term
   * @param bIgnoreCase whether to ignore the case of string comparisons
   */
  public AnagramFilter(final String word, final boolean bIgnoreCase)
  {
    super();
    
    this.term = word;
    
    // Save whether to ignore the case
    ignoreCase = bIgnoreCase;
    
    // Save the String parameter, after processing
    this.chars = buildData(word, ignoreCase);
  }
  
  
  /**
   * Determines if the term matches the source term.
   * 
   * @param theWord the term to compare to the source term
   * @return whether the terms match
   */
  public boolean accept(final String theWord)
  {
    if (!super.accept(theWord)) return false;

    //if (this.word.equals(theWord)) return false;
    
    // Save the String parameter, after processing
    final String data = buildData(theWord, ignoreCase);
    
    // Return whether the strings are equal
    return (chars.equals(data));
  }
  
  
  /**
   * Convert the argument into a String of the non-space
   * characters in the term, with the characters sorted.
   * 
   * @param term the string to sort
   * @param ic whether to ignore the string's case
   * @return the normalized string
   */
  private static String buildData(final String term, final boolean ic)
  {
    // Check the term
    if (term == null)
    {
      return RiTa.E;
    }
    
    // Get the string as an array
    char[] chars;
    if (ic)
    {
      // Get the string as lower-case (ignore case)
      chars = term.toLowerCase().toCharArray();
    }
    else
    {
      // Get the string as-is (don't ignore case)
      chars = term.toCharArray();
    }
    
    // Sort the data
    Arrays.sort(chars);
    
    // Put the non-space chars in an array
    final int size = chars.length;
    char[] data = new char[size];
    int j = 0;
    
    for (int i = 0; i < size; ++i)
    {
      // Only add non-spaces
      if (chars[i] != ' ')
      {
        data[j++] = chars[i];
      }
    }
    
    // Build the string
    String value = new String(data, 0, j);
    
    // Return the built string
    return value;
  }
}
