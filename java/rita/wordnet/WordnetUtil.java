// $Id: WordnetUtil.java,v 1.2 2013/12/11 12:46:56 dev Exp $

package rita.wordnet;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.support.Constants;

public abstract class WordnetUtil implements Constants
{    
  public static URL getResourceURL(Class loc, String file) {
    
    try {     
      return loc.getResource(file);      
    } catch (Exception e) {     
      throw new RiWordNetError(e);
    }
  }
  
  public static InputStream getResourceStream(Class loc, String file) {
 
    try {
      URL url = getResourceURL(loc, file);
      if (url != null) {
        InputStream is = url.openStream();
        return is;
      }
      
      throw new RuntimeException("Unable to load file: "+file);  
    } 
    catch (Exception e) {     
      throw new RiWordNetError(e);
    }
  }

  public static boolean startsWithUppercase(String lemma)
  {
    return Character.isUpperCase(lemma.charAt(0));
  }

  protected static Pattern examplePattern;
  public static List parseExamples(String gloss)
  {
    if (gloss == null) return null;

    int idx = gloss.indexOf('\"');
    if (idx < 0)
      return null; // no quotation

    if (examplePattern == null)
      examplePattern = Pattern.compile("\"([\\w ;',-.?!:\\(\\)]+)\";?");

    List result = null;
    String example = gloss.substring(idx);
    Matcher lineMatch = examplePattern.matcher(example);
    for (int i = 0; lineMatch.find();)
    {
      String word = lineMatch.group(1);
      if (result == null)
        result = new LinkedList();
      result.add(word);
      //System.out.println(++i + ") '" + word + "'");
    }
    return result;
  }
  
  public static String parseGloss(String gloss)
  {
    if (gloss == null)
      return null;
    int idx = -1;
    if ((idx = gloss.indexOf('\"')) >= 0)
      return gloss.substring(0, idx - 2);
    return gloss;
  }
  

}// end
