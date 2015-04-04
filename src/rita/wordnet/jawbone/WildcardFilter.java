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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Provide a filter for search terms that only
 * accepts matches where the term passed to accept()
 * matches the wildcard-string passed in the
 * constructor.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class WildcardFilter extends rita.wordnet.RiFilter
{
  /**
   * The list of substrings in the string with wildcards.
   */
  private List fields = new ArrayList(8);
  
  /**
   * Initializes the filter with the source term and
   * whether to ignore case on searches.
   * 
   * @param word the source term
   * @param bIgnoreCase whether to ignore the case of string comparisons
   */
  public WildcardFilter(final String word, final boolean bIgnoreCase)
  {
    
    this.ignoreCase = bIgnoreCase;
    this.term = word;
    if (term.endsWith("*"))
      term = term.substring(0, term.length()-1);
    if (term.startsWith("*"))
      term = term.substring(1);
    parsePattern(word);
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
    
    
    // Neither is null, so compare the strings
    return matchPattern(word);
  }
  
  
  /**
   * Returns whether target fits the pattern in pat.
   *
   * @param target The target string to compare against the pattern
   * @return whether target fits the pattern in pat
   */
  public boolean matchPattern(final String target)
  {
    if (ignoreCase)
    {
      // Check the inputs.  If no pattern, assume it's a match.
      if ((fields == null) || (fields.size() == 0))
      {
        return true;
      }

      // If we reach here, pattern is non-null and non-empty.  If target
      // is null or empty, consider it a non-match.
      if ((target == null) || (target.length() == 0))
      {
        return false;
      }

      return matchString(target.toUpperCase());
    }

    return matchString(target);
  }
  
  
  /**
   * Returns whether target fits the pattern in pat.  This
   * method assumes a match should be case-sensitive.
   *
   * @param target The target string to compare against the pattern
   * @return whether target fits the pattern in pat
   */
  private boolean matchString(final String target)
  {
    // Store whether a match was found.  The initial value
    // doesn't matter since we handle all cases below.
    boolean match = false;
    
    // Check the inputs.  If no pattern, assume it's a match.
    if ((fields == null) || (fields.size() == 0))
    {
      match = true;
    }
    else
    {
      // If we reach here, pattern is non-null and non-empty.  If target
      // is null or empty, consider it a non-match.
      if ((target == null) || (target.length() == 0))
      {
        match = false;
      }
      else
      {
        // Check for just a *, or no * at all
        if (fields.size() == 1)
        {
          // Just one element in the list, meaning pat was either '*' or had
          // no wildcards.
          final String pat = (String)fields.get(0);
          if (null == pat)
          {
            // Just a * in the input, so it matches
            match = true;
          }
          else
          {
            // No *, so return whether they're equal
            match = equalsWild(target, pat);
          }
        }
        else
        {
          // OK, call the pattern finder
          match = findMatch(0, target, 0);
        }
      }
    }
    
    // Return whether a match was found
    return match;
  }
  
  
  /**
   * This is a recursive method that does the work
   * of checking for a match.  In the unoptimized
   * version, this should only be called by the method
   * matchFound.
   *
   * @param nCurrPart The current part of the list we're checking
   * @param target The string to compare against
   * @param nCurrIndex The current index of target
   * @return whether the pattern matches the target string
   */
  private boolean findMatch(final int nCurrPart,
                            final String target,
                            final int nCurrIndex)
  {
    // Default return value
    boolean found = false;
    
    // Check if we're looking past the end of the list
    if (nCurrPart >= fields.size())
    {
      // We hit the end, so return.  I don't think this code
      // is ever reached, but it's here just in case.
      return true;
    }
    
    // Save the current string in its own variable
    String part = (String)fields.get(nCurrPart);
    
    // Check if we're trying to find a match past the end of target
    if (nCurrIndex >= target.length())
    {
      // If 'part' is * (null), return true; else, no match.
      return (part == null);
    }

    // Check if we're on a *
    if (part == null)
    {
      // We hit a *, so we're either at the start of the string, or at the end
      if (nCurrPart == 0)
      {
        // It starts with *, so start looking with the next element of list
        return (findMatch(1, target, 0)); 
      }
      // We're at the end of the list, so assume it matches
      // (pattern ends with '*')
      return true;
    }

    // See if we're looking at the last field
    if (nCurrPart == (fields.size() - 1))
    {
      // We are, so return whether the target string ends with this string
      return (endsWithWild(target, part));
    }
    
    // Save the length
    final int nLen = part.length();
    
    // Find the next occurrence of s[nCurrPart], starting after
    // the current index
    int foundIndex = indexOfWild(target, part, nCurrIndex);
    
    // Keep looking until the subsequent s partitions are all found
    while ((foundIndex >= 0) && (!found))
    {
      // If there was no wildcard before 'part' (nCurrPart = 0), and
      // the string was found after the point we started looking, then
      // return false.
      if ((foundIndex > nCurrIndex) && (nCurrPart == 0))
      {
        return false;
      }
      
      // Find the next occurrence of the next s elements, starting after
      // the end of the current match
      found = findMatch((nCurrPart + 1), target, (foundIndex + nLen));
      
      // If no match found, find the next occurrence of part in target
      if (!found)
      {
        // Store where it was found (if at all)
        foundIndex = indexOfWild(target, part, ++foundIndex);
      }
    }
    
    // Return whether a match was found (in target) for the current element
    // of list and all subsequent elements of list.
    return found;
  }
  
  
  /**
   * This method determines whether two strings are equal.
   * The 'part' argument is allowed to have a '?', which
   * is interpreted to mean any single character.
   *
   * @param target The target string to compare with
   * @param part The string with zero or more '?' characters
   * @return whether the two strings match
   */
  private boolean equalsWild(final String target,
                             final String part)
  {
    boolean bEquals = true;
    
    // Check the input strings
    if ((target == null) || (part == null))
    {
      return false;
    }
    
    // Check if part has a wildcard
    if (part.indexOf("?") < 0)
    {
      // No wildcard, so call the String::indexOf() function
      return target.equals(part);
    }
    
    // part has a wildcard
    // Check the length
    if (target.length() != part.length())
    {
      // Lengths are different
      return false;
    }
    
    // The lengths are the same, so check each character
    final int nLen = target.length();
    for (int nIndex = 0; (nIndex < nLen) && (bEquals); ++nIndex)
    {
      // Save the current character in each string
      char targetChar = target.charAt(nIndex);
      char partChar = part.charAt(nIndex);
      
      // Check for a mismatch
      if ((partChar != '?') && (partChar != targetChar))
      {
        // The characters don't match, and the current part character
        // is not a question mark, so the strings don't match
        bEquals = false;
      }
    }
    
    // Return whether the strings are equal
    return bEquals;
  }
  
  
  /**
   * This method determines whether 'target' ends with 'part'.
   * The 'part' argument is allowed to have a '?', which is
   * interpreted to mean any single character.
   *
   * @param target The target string to compare with
   * @param part The string with zero or more '?' characters
   * @return whether target ends with part
   */
  private boolean endsWithWild(final String target,
                               final String part)
  {
    // Check the input strings
    if ((target == null) || (part == null))
    {
      return false;
    }
    
    // Check if part has a wildcard
    if (part.indexOf("?") < 0)
    {
      // No wildcard, so call the String::indexOf() function
      return target.endsWith(part);
    }
    
    // part has a wildcard
    // Check the length
    if (target.length() < part.length())
    {
      // The string isn't long enough
      return false;
    }
    
    // Get the end of the target string
    String targetEnd = target.substring(target.length() - part.length());
    
    // Return whether targetEnd equals part
    return equalsWild(targetEnd, part);
  }
  
  
  /**
   * This method checks for the existence of the string 'part'
   * within the string 'target', starting at target[fromIndex].
   * The 'part' argument is allowed to have a '?', which is
   * interpreted to mean any single character.
   *
   * @param target The target string to compare with
   * @param part The string with zero or more '?' characters
   * @param fromIndex the starting index of target
   * @return the index at which part exists within target
   */
  private int indexOfWild(final String target,
                          final String part,
                          final int fromIndex)
  {
    // Declare needed variables
    boolean bFound = false;
    int nFoundIndex = fromIndex;
    
    // Check the input strings
    if ((target == null) || (part == null) || (fromIndex < 0))
    {
      return -1;
    }
    
    // Check if part has a wildcard
    if (part.indexOf("?") < 0)
    {
      // No wildcard, so call the String::indexOf() function
      return target.indexOf(part, fromIndex);
    }
    
    // part has a wildcard
    // Check the starting index
    final int nTargetLen = target.length();
    final int nPartLen = part.length();
    if ((nPartLen + fromIndex) > nTargetLen)
    {
      // The starting index is too high
      return -1;
    }
    
    // Check for the existence of part as a substring in target
    for (int nIndex = fromIndex; (!bFound) &&
         ((nPartLen + nIndex) <= nTargetLen); ++nIndex)
    {
      String targetSub = target.substring(nIndex, (nIndex + nPartLen));
      if (equalsWild(targetSub, part))
      {
        nFoundIndex = nIndex;
        bFound = true;
      }
    }
    
    // Check if not found
    if (!bFound)
    {
      return -1;
    }
    
    // Return the index that part was found at in target
    return nFoundIndex;
  }
  
  
  /**
   * This method separates a String containing one or more
   * wildcards ('*' or '?') into a List of substrings.  Each
   * element of the List is the substring of 'pat' between
   * wildcards (e.g., "ab*cd" is two elements: "ab" and "cd").
   * Also, if pat starts with a wildcard, the first element
   * of the list will be null.  If pat ends with a wildcard,
   * the last element of the list will be null.
   *
   * @param pat the string to parse into a list
   */
  protected void parsePattern(final String pat)
  {
    // Check the input.  If no pattern, return null.
    if ((pat == null) || (pat.length() == 0))
    {
      return;
    }
    
    // If pat begins with *, add null to the list.
    if (pat.startsWith("*"))
    {
      fields.add(null);
    }
    
    // Parse the input string (skip over consecutive adjacent *)
    StringTokenizer tokenizer = new StringTokenizer(pat, "*");
    while (tokenizer.hasMoreTokens())
    {
      // Get the token.  If we're ignoring case, convert to uppercase.
      if (ignoreCase)
      {
        fields.add(tokenizer.nextToken().toUpperCase());
      }
      else
      {
        fields.add(tokenizer.nextToken());
      }
    }
    
    // If the string ends with a *, and there's at least one non-* before it,
    // add null to the end of the list.
    if ((fields.size() > 1) ||
        ((fields.size() == 1) && (fields.get(0) != null)))
    {
      if (pat.endsWith("*"))
      {
        fields.add(null);
      }
    }
  }
}
