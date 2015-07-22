package rita.support;

/*  
 author:     Christopher O'Neill
 date:   Sep 2000
 comments:   The Paice/Husk Stemmer Translated from Pascal
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import rita.RiTaException;

public class LancasterStemmer implements StemmerIF
{
  private static String RULES = "lancaster-rules.txt";
  
  private static Vector ruleTable; // array of rules
  private static int[] ruleIndex; // index to above
  
  private boolean preStrip;

  public LancasterStemmer()
  {
    preStrip = false;
    if (ruleTable == null)
      initialize();
  }

  private void initialize()
  {
    ruleTable = new Vector();
    BufferedReader br = null;
    try {
      InputStream is = LetterToSound.class.getResourceAsStream(RULES);
      br = new BufferedReader(new InputStreamReader(is));
    }
    catch (Exception e1)
    {
      throw new RiTaException("Unable to load rules: ",e1);
    }
    
    int ruleCount = 0;
    int j = 0;

    // Acquire each rule in turn. They each take up one line

    String line = " ";
    try
    {
      while ((line = br.readLine()) != null)
      {
        ruleCount++;
        j = 0;
        String rule = new String();
        rule = "";
        while ((j < line.length()) && (line.charAt(j) != ' '))
        {
          rule += line.charAt(j);
          j++;
        }
        ruleTable.addElement(rule);
      }
    }
    catch (Exception e) {
      throw new RiTaException(e);
    }
    // try to close file, file is not needed again so if can't close don't
    // exit
    try {
      br.close();
    }
    catch (Exception e) {
      System.err.println("LancasterStemmer: error closing file, "+e.getMessage());
    }

    // Now assign the number of the first rule that starts with each letter
    // (if any) to an alphabetic array to facilitate selection of sections
    ruleIndex = new int[26];
     
    char ch = 'a';
    for (j = 0; j < 25; j++)
    {
      ruleIndex[j] = 0;
    }

    for (j = 0; j < (ruleCount - 1); j++)
    {
      while (((String) ruleTable.elementAt(j)).charAt(0) != ch)
      {
        ch++;
        ruleIndex[charCode(ch)] = j;
      }
    }
  }

  private int firstVowel(String word, int last)
  {
    int i = 0;
    if ((i < last) && (!(vowel(word.charAt(i), 'a'))))
    {
      i++;
    }
    if (i != 0)
    {
      while ((i < last) && (!(vowel(word.charAt(i), word.charAt(i - 1)))))
      {
        i++;
      }
    }
    if (i < last)
    {
      return i;
    }
    return last;
  }

  private String stripSuffixes(String word)
  {
    // integer variables 1 is positive, 0 undecided, -1 negative equiverlent of
    // pun vars positive undecided negative
    int ruleok = 0;
    int cont = 0;
    // integer varables
    int pll = 0; // position of last letter
    int xl; // counter for nuber of chars to be replaced and length of stemmed
            // word if rule was aplied
    int pfv; // poition of first vowel
    int prt; // pointer into rule table
    int ir; // index of rule
    int iw; // index of word
    // char variables
    char ll; // last letter
    // String variables eqiverlent of tenchar variables
    String rule = ""; // varlable holding the current rule
    String stem = ""; // string holding the word as it is being stemmed this is
                      // returned as a stemmed word.
    // boolean varable
    boolean intact = true; // intact if the word has not yet been stemmed to
                           // determin a requirement of some stemming rules

    // set stem = to word
    stem = clean(word.toLowerCase());

    // set the position of pll to the last letter in the string
    pll = 0;
    // move through the word to find the position of the last letter before a
    // non letter char
    while ((pll + 1 < stem.length()) && ((stem.charAt(pll + 1) >= 'a') && (stem.charAt(pll + 1) <= 'z')))
    {
      pll++;
    }

    if (pll < 1)
    {
      cont = -1;
    }
    // find the position of the first vowel
    pfv = firstVowel(stem, pll);

    iw = stem.length() - 1;

    // repeat until continue == negative ie. -1
    while (cont != -1)
    {
      cont = 0; // SEEK RULE FOR A NEW FINAL LETTER
      ll = stem.charAt(pll); // last letter

      // Check to see if there are any possible rules for stemming
      if ((ll >= 'a') && (ll <= 'z'))
      {
        prt = ruleIndex[charCode(ll)]; // pointer into rule-table
      }
      else
      {
        prt = -1;// 0 is a vaild rule
      }
      if (prt == -1)
      {
        cont = -1; // no rule available
      }

      if (cont == 0)
      // THERE IS A POSSIBLE RULE (OR RULES) : SEE IF ONE WORKS
      {
        rule = (String) ruleTable.elementAt(prt); // Take first rule
        while (cont == 0)
        {
          ruleok = 0;
          if (rule.charAt(0) != ll)
          {
            // rule-letter changes
            cont = -1;
            ruleok = -1;
          }
          ir = 1; // index of rule: 2nd character
          iw = pll - 1; // index of word: next-last letter

          // repeat untill the rule is not undecided find a rule that is
          // acceptable
          while (ruleok == 0)
          {
            if ((rule.charAt(ir) >= '0') && (rule.charAt(ir) <= '9')) // rule fully matched
            {
              ruleok = 1;
            }
            else if (rule.charAt(ir) == '*')
            {
              // match only if word intact
              if (intact)
              {
                ir = ir + 1; // move forwards along rule
                ruleok = 1;
              }
              else
              {
                ruleok = -1;
              }
            }
            else if (rule.charAt(ir) != stem.charAt(iw))
            {
              // mismatch of letters
              ruleok = -1;
            }
            else if (iw <= pfv)
            {
              // insufficient stem remains
              ruleok = -1;
            }
            else
            {
              // move on to compare next pair of letters
              ir = ir + 1; // move forwards along rule
              iw = iw - 1; // move backwards along word
            }
          }

          // if the rule that has just been checked is valid
          if (ruleok == 1)
          {
            // CHECK ACCEPTABILITY CONDITION FOR PROPOSED RULE
            xl = 0; // count any replacement letters
            while (!((rule.charAt(ir + xl + 1) >= '.') && (rule.charAt(ir + xl + 1) <= '>')))
            {
              xl++;
            }
            xl = pll + xl + 48 - ((int) (rule.charAt(ir)));
            // position of last letter if rule used
            if (pfv == 0)
            {
              // if word starts with vowel...
              if (xl < 1)
              {
                // ...minimal stem is 2 letters
                ruleok = -1;
              }
              else
              {
                // ruleok=1; as ruleok must alread be positive to reach this
                // stage
              }
            }
            // if word start swith consonant...
            else if ((xl < 2) | (xl < pfv))
            {
              ruleok = -1;
              // ...minimal stem is 3 letters...
              // ...including one or more vowel
            }
            else
            {
              // ruleok=1; as ruleok must alread be positive to reach this stage
            }
          }
          // if using the rule passes the assertion tests
          if (ruleok == 1)
          {
            // APPLY THE MATCHING RULE
            intact = false;
            // move end of word marker to position...
            // ... given by the numeral.
            pll = pll + 48 - ((int) (rule.charAt(ir)));
            ir++;
            stem = stem.substring(0, (pll + 1));

            // append any letters following numeral to the word
            while ((ir < rule.length()) && (('a' <= rule.charAt(ir)) && (rule.charAt(ir) <= 'z')))
            {
              stem += rule.charAt(ir);
              ir++;
              pll++;
            }

            // if rule ends with '.' then terminate
            if ((rule.charAt(ir)) == '.')
            {
              cont = -1;
            }
            else
            {
              // if rule ends with '>' then Continue
              cont = 1;
            }
          }
          else
          {
            // if rule did not match then look for another
            prt = prt + 1; // move to next rule in RULETABLE
            rule = (String) ruleTable.elementAt(prt);
            if (rule.charAt(0) != ll)
            {
              // rule-letter changes
              cont = -1;
            }
          }
        }
      }
    }
    return stem;
  }

  private boolean vowel(char ch, char prev)
  {
    switch (ch) {
    case 'a':
    case 'e':
    case 'i':
    case 'o':
    case 'u':
      return true;
    case 'y': {
      switch (prev) {
      case 'a':
      case 'e':
      case 'i':
      case 'o':
      case 'u':
        return false;
      default:
        return true;
      }
    }
    default:
      return false;
    }
  }

  private int charCode(char ch)
  {
    return ((int) ch) - 97;
  }

  private String stripPrefixes(String str)
  {
    String[] prefixes = { "kilo", "micro", "milli", "intra", "ultra", "mega", "nano", "pico", "pseudo" };

    int last = prefixes.length;

    for (int i = 0; i < last; i++)
    {
      if ((str.startsWith(prefixes[i])) && (str.length() > prefixes[i].length()))
      {
        str = str.substring(prefixes[i].length());
        return str;
      }
    }
    return str;
  }

  private String clean(String str)
  {
    int last = str.length();
    String temp = "";
    for (int i = 0; i < last; i++)
    {
      if ((str.charAt(i) >= 'a') & (str.charAt(i) <= 'z'))
      {
        temp += str.charAt(i);
      }
    }
    return temp;
  }

  public String stem(String str)
  {
    // str = str.toLowerCase(); //change all letters in the input to lowercase
    // str = Clean(str); // remove all chars from string that are not a letter
    // or a digit (why digit?)
    if ((str.length() > 3) && (preStrip)) // if str's length is greater than 2
                                          // then remove prefixes
    {
      str = stripPrefixes(str);
    }
    if (str.length() > 3) // if str is not null remove suffix
    {
      str = stripSuffixes(str);
    }
    return str;
  } // stripAffixes

    
} // class

