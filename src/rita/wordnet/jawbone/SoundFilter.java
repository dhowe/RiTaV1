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
 * Provide a filter for search terms that only accepts matches where the term
 * passed to accept() sounds like the term passed in the constructor.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class SoundFilter extends rita.wordnet.RiFilter
{
  /**
   * The soundex code for source term.
   */
  private String termCode = null;

  /**
   * Initializes the filter with the source term and whether to ignore case on
   * searches.
   * 
   * @param word
   *          the source term
   * @param bIgnoreCase
   *          whether to ignore the case of string comparisons
   */
  public SoundFilter(final String word, final boolean bIgnoreCase)
  {
    term = word;
    termCode = getSoundexCode(word);
    ignoreCase = bIgnoreCase;
  }

  /**
   * Determines if the term matches the source term.
   * 
   * @param word
   *          the term to compare to the source term
   * @return whether the terms match
   */
  public boolean accept(final String word)
  {
    if (!super.accept(word))
      return false;

    // Compute the soundex code
    final String wordCode = getSoundexCode(word);

    // Neither is null, so check how to compare the strings
    if (ignoreCase)
    {
      // Ignore the case
      return (wordCode.equalsIgnoreCase(termCode));
    }

    // Consider the case
    return (wordCode.equals(termCode));

  }

  /**
   * Returns the integer value for a character.
   * 
   * @param ch
   *          the character to get the value for
   * @return the value for the specified character
   */
  private static int getIntValue(final char ch)
  {
    int n = 0;

    switch (ch)
    {
      case 'b':
      case 'f':
      case 'p':
      case 'v':
      {
        n = 1;
        break;
      }

      case 'c':
      case 'g':
      case 'j':
      case 'k':
      case 'q':
      case 's':
      case 'x':
      case 'z':
      {
        n = 2;
        break;
      }

      case 'd':
      case 't':
      {
        n = 3;
        break;
      }

      case 'l':
      {
        n = 4;
        break;
      }

      case 'm':
      case 'n':
      {
        n = 5;
        break;
      }

      case 'r':
      {
        n = 6;
        break;
      }

      default:
        n = 0;
    }

    return n;
  }

  /**
   * Calculates the Soundex code for a string.
   * 
   * @param sInput
   *          the input string
   * @return the Soundex code for the string
   */
  private static String getSoundexCode(final String sInput)
  {
    // Check the input
    if ((sInput == null) || (sInput.length() < 1))
    {
      return "";
    }

    // Declare our string variable to hold the soundex code
    StringBuilder buf = new StringBuilder(10);

    // The first character of the string is the start
    // of the soundex code
    buf.append(sInput.charAt(0));

    // Convert the string to lower case
    final String sWord = sInput.toLowerCase();

    // Save the value of the first character, to check
    // for duplicates later
    int nPrevValue = getIntValue(sWord.charAt(0));

    // Initialize this variable
    int nCurrValue = -1;

    // Save the length of the string
    final int nLen = sWord.length();

    // Iterate over each character in the word, until
    // we have enough to fill the soundex code (the
    // form is A999 - a character followed by 3 digits).
    for (int i = 1; (i < nLen) && (buf.length() < 4); ++i)
    {
      // Get the integer value for the current character
      nCurrValue = getIntValue(sWord.charAt(i));

      // Make sure the current value is not a duplicate of
      // the previous value, and the current value is non-zero
      if ((nCurrValue != nPrevValue) && (nCurrValue != 0))
      {
        buf.append(Integer.toString(nCurrValue));
      }

      // Save the current value as the previous value
      nPrevValue = nCurrValue;
    }

    // Check the length of the string
    int nSize = buf.length() - 4;
    if (nSize < 0)
    {
      // The string is too short, so append zeros
      while (nSize < 0)
      {
        buf.append("0");
        ++nSize;
      }
    }

    // Return the generated soundex code for the input string
    return buf.toString();
  }
}
