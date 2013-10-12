// $Id: WordnetUtil.java,v 1.1 2013/10/12 12:04:34 dev Exp $

package rita.wordnet;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import rita.RiWordnet;

/** 
 * @invisible
 * @author dhowe
 * <p>See the accompanying documentation for license information
 */
public abstract class WordnetUtil
{ 
  static final String QQ="", BN="\n", SPC=" "; 
  static final int DEFAULT_NUM_STRINGS = 100;
  static String OS_SLASH = "/";
  
  //static final String DATA_DIR_PREFIX = "data";
  static final boolean DBUG = false;
    
  static {
    try {
      OS_SLASH = System.getProperty("file.separator");
    } 
    catch (RuntimeException e) {
      System.err.println("[WARN] Unable to determine OS_SLASH, using '"+OS_SLASH+"'");
    }
  }
  
  static String getOS() {    
    String os = System.getProperty("os.name");
    for (int i = 0; i < OSs.length; i++)
      if (os.toLowerCase().indexOf(OSs[i]) >= 0)
        return OSs[i];
    System.err.println("[WARN] Undefined-OS: "+os);
    return OS_UNDEFINED;
  }

  // OS String constants  
  static final String MAC = "mac";
  static final String LINUX = "linux";
  static final String WINDOWS = "windows";   
  static final String OS_UNDEFINED = "undefined";
  static final String[] OSs = { WINDOWS, MAC, LINUX };
  
  /**
   * Returns a String rep. of Exception and stacktrace (only elements with line numbers)
   * @param e
   * @return
   */  
  static String exceptionToString(Throwable e) {
    return exceptionToString(e, true);
  }
  
  /**
   * Returns a String rep. of Exception type and stacktrace 
   * @param e
   * @param miniStack - if true, returns only elements w line numbers
   * @return
   */
  static String exceptionToString(Throwable e, boolean miniStack) {
    if (e == null) return "null";
    StringBuffer s = new StringBuffer(e+"\n");
    StackTraceElement[] stes = e.getStackTrace();
    for (int i = 0; i < stes.length; i++)
    {
      String ste = stes[i].toString();
      if (ste.matches(".*[0-9]+\\)"))
        s.append("    "+ste+WordnetUtil.BN);
    }
    return s.toString();
  }
  
  /**
   * Removes a random element from a Set, maintaining the ordering
   * @param set
   * @return null if Set is null or empty, else removed item
   * TODO: OPT (in place)
   */
  static Object removeRandom(Set set)
  { 
    if (set == null || set.size()==0) return null;
    List tmp = new ArrayList(set.size());
    tmp.addAll(set);
    System.out.println();
    Object o = removeRandom(tmp);      
    for (Iterator i = set.iterator(); i.hasNext();)
      if (i.next()==o)i.remove();
    return o;
  }
  
  /**
   * Removes a random element from a List, maintaining the ordering
   * @param list
   * @return null if List is null or empty, else removed item
   */
  static Object removeRandom(List list)
  { 
    if (list == null || list.size()==0) return null;
    int rand = (int)(Math.random()*list.size());
    return list.remove(rand);
  }
  
  static String cwd()
  {
    return System.getProperty("user.dir");
  }
  
  static String join(String[] input, char delim)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < input.length; i++) {
/*      if (i < input.length-1) {   ??????????????????  TEST!!
        if (input[i].endsWith(".") && !input[i+1].endsWith(" "))
          input[i+1] = " " + input[i+1];
      }*/
      sb.append(input[i]);
      if (i < input.length-1) 
        sb.append(delim);
    }
    return sb.toString();
  }
 
  public static String join(List l, String delim)
  {
    StringBuffer sb = new StringBuffer();
    for (Iterator i = l.iterator(); i.hasNext();) {
      sb.append(i.next());      
      if (i.hasNext())
        sb.append(delim);
    }
    return sb.toString();
  }

  public static boolean contains(String full, String search)
  {
    if (full == null) return false; 
    return (full.indexOf(search) > -1);
  }

  static boolean contains(StringBuffer full, char[] search)
  {
    if (full == null || search == null)
      return false;
    for (int i = 0; i < search.length; i++) {
      if (full.indexOf(Character.toString(search[i])) > -1)
        return true;
    }
    return false;
  }
  
  public static String replace(String src, char c, char r)
  {
    if (src.indexOf(c) < 0) return src;
    StringBuffer buffer = new StringBuffer(src);
    for (int i = 0; i < buffer.length(); i++)
      if (buffer.charAt(i) == c)
        buffer.replace(i, i + 1, (r + QQ));
    return buffer.toString();
  }

  
  /**
   * Converts space-delimited String into array of Strings
   * @param full String to be split
   * @return result String[] resulting from split of full 
   *   (Zero length array if source string is null)
   */
  static String[] split(String full)
  {
    return split(full, " ");
  }

  /**
   * Converts delimited CharSequence into array of Strings
   * @param full String to be split
   * @param delim Delimiter string
   * @return result String[] resulting from split of full 
   *   (Zero length array if full is null)
   */
  static String[] split(CharSequence full, String delim)
  {
    String[] result;
    StringTokenizer st;
    if (full != null) {
      st = new StringTokenizer(full.toString(), delim);
      result = new String[st.countTokens()];
      for (int index = 0; index < result.length; index++)
        result[index] = st.nextToken();
    }
    else 
      result = new String[0];
    return result;
  }

  /**
   * Joins Array of String into space-delimited String.
   * @param full - Array of Strings to be joined
   * @return String containing elements of String[] or ""
   */
  static String join(Object[] full)
  {
    return join(full, SPC);
  }

  /**
   * Joins Array of String into delimited String.
   * @param full - Array of Strings to be joined
   * @param delim - Delimiter to parse elements in resulting String
   * @return String containing elements of String[] or "" if null
   */
  static String join(Object[] full, String delim)
  {
    String result = QQ;

    if (full != null) {
      for (int index = 0; index < full.length; index++) {
        if (index == full.length - 1)
          result += full[index];
        else
          result += full[index] + delim;
      }
    }
    return result;
  }

  static String[] loadStringsLocal(String name)
  {   
    if (!printedNullParentWarning) { // hack, just print this once
    	System.err.println("[WARN] Null PApplet passed to WordnetUtil.loadStrings("+name+")");
      printedNullParentWarning = true;
    }    
    InputStream is = openStreamLocal(name);    
    return loadStrings(is);
  }

  // need to handle URLs here...
  static InputStream openStreamLocal(String streamName)
  {
    String[] guesses = { "src"+OS_SLASH+"data", "data", "" };
    
    InputStream is = null;
    try // check for url first  (from PApplet)
    {
      URL url = new URL(streamName);
      is = url.openStream();

    } catch (MalformedURLException mfue) {
      // not a url, that's fine
    } catch (FileNotFoundException fnfe) {
      // Java 1.5 likes to throw this when URL not available. (fix for 0119)
      // http://dev.processing.org/bugs/show_bug.cgi?id=403
    } catch (Throwable e) {
      throw new WordnetError(e);
    }
    
    if (is != null && streamName.endsWith(".gz")) {
      try {
        is = new GZIPInputStream(is);
      } 
      catch (Throwable e1) {
        throw new WordnetError("Unable to load archive: "+streamName, e1);
      }
    }
    
    if (is != null) return is;
    
    for (int i = 0; i < guesses.length; i++) {
    	String guess =  guesses[i] +  OS_SLASH + /*DATA_DIR_PREFIX + OS_SLASH +*/ streamName;
    	System.err.print("[INFO] Trying "+guess);
    	try {
    		is = new FileInputStream(guess);
    		System.err.println("... OK");
    	} 
    	catch (FileNotFoundException e) {
    		System.err.println("... failed");
    	}
    	if (is != null) break;
    }
    
    if (is == null)
      throw new WordnetError("Unable to create stream for: "+streamName);
    
    return is;
  }	
  static boolean printedNullParentWarning = false;
 
  public static URL getResourceURL(Class loc, String file) {
    try {     
      return loc.getResource(file);      
    } catch (Exception e) {     
      throw new WordnetError(e);
    }
  }
  
  public static InputStream getResourceStream(Class loc, String file) {
  //System.err.println("WNUtil.getResourceStream("+loc+", "+file+")");
    try {
      URL url = getResourceURL(loc, file);
      if (url != null) {
        InputStream is = url.openStream();
        //System.err.println("  IS: "+is);
        return is;
      }
      else 
        throw new RuntimeException("Unable to load file: "+file);
    } catch (Exception e) {     
      throw new WordnetError(e);
    }
  }



  
  private static String[] loadStrings(InputStream input) {
    return loadStrings(input, DEFAULT_NUM_STRINGS);
  }
  
  // from PApplet?
  private static String[] loadStrings(InputStream input, int numLines) {
    if (input == null) throw new WordnetError("Null input stream!");
    try {
      BufferedReader reader =
        new BufferedReader(new InputStreamReader(input));

      String lines[] = new String[numLines];
      int lineCount = 0;
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (lineCount == lines.length) {
          String temp[] = new String[lineCount << 1];
          System.arraycopy(lines, 0, temp, 0, lineCount);
          lines = temp;
        }
        lines[lineCount++] = line;
      }
      reader.close();

      if (lineCount == lines.length) {
        return lines;
      }

      // resize array to appropriate amount for these lines
      String output[] = new String[lineCount];
      System.arraycopy(lines, 0, output, 0, lineCount);
      return output;

    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  static String replace(String src, String r, String s)
  {
    int rLength = r.length();
    StringBuffer buffer = new StringBuffer(src);
    for (int i = 0; (i + rLength) <= buffer.length(); i++)
      if ((buffer.substring(i, i + rLength)).equals(r))
        buffer.replace(i, i + rLength, s);
    return buffer.toString();
  }

  static boolean contains(String full, String search, boolean ignoreLast)
  {
    if (full == null || search == null)
      return false;
    if (ignoreLast) {
      int idx = full.indexOf(search);
      return (idx >= 0 && idx < full.length() - 1);
    }
    return (full.indexOf(search) >= 0);
  }

  static boolean contains(String full, char search)
  {
    if (full == null || full.length()!=1) return false;
    
    return (full.indexOf(Character.toString(search)) > -1);
  }

  static boolean contains(String full, char[] search)
  {
    if (full == null || search == null)
      return false;
    for (int i = 0; i < search.length; i++) {
      if (full.indexOf(Character.toString(search[i])) > -1)
        return true;
    }
    return false;
  }

  static boolean contains(String full, String[] search)
  {
    if (full == null || search == null || search.length ==0)
      return false; //?
    for (int i = 0; i < search.length; i++)
      if (full.indexOf(search[i]) >= 0)
        return true;
    return false;
  }

  static boolean isPunct(CharSequence full)
  {
    if (full == null || full.length() > 1)
      return false;
    
    for (int i = 0; i < PUNCTUATION.length; i++) {
      if (full.charAt(0) == PUNCTUATION[i])
        return true;
    }
    return false;
  }

  static String replace(String src, int startIdx, int length, String replace)
  {
    String start = src.substring(0, startIdx);
    String end = src.substring(startIdx + length);  
    return start + replace + end;
  }
  
  
  static final char[] PUNCTUATION = {',', ';', ':', '!', '?', 
    ')', '(','[', ']', '.', '#', '"', '\'', '!', '@', '$', '%', '&', 
    '}', '<', '>', '|', '+', '=', '-', '_', '\\', '/', '*', '{', '^'};
  
  public static List asList(Set s)
  {
    List l = new ArrayList();
    if (s == null) return l;
    l.addAll(s);
    return l;
  }
  
  public static List asList(int[] ints)
  {
    List l = new ArrayList();
    if (ints == null) return l;
    for (int i = 0; i < ints.length; i++)
      l.add(new Integer(ints[i]));
    return l;
  }
  

  public static List asList(long[] longs)
  {
    List l = new ArrayList();
    if (longs == null) return l;
    for (int i = 0; i < longs.length; i++)
      l.add(new Long(longs[i]));
    return l;
  }
  
  public static List asList(float[] floats)
  { 
    List l = new ArrayList();
    if (floats == null) return l;
    for (int i = 0; i < floats.length; i++)
      l.add(new Float(floats[i]));
    return l;
  }
  
  public static List asList(Object[] o)
  {    
    if (o == null || o.length < 1)
      return new ArrayList();
    return Arrays.asList(o);
  }
 
  //////////////////////////////////////////////////////////
  
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
      if (DBUG)
        System.out.println(++i + ") '" + word + "'");
    }
    return result;
  }
  
  public static String parseDescription(String gloss)
  {
    if (gloss == null)
      return null;
    int idx = -1;
    if ((idx = gloss.indexOf('\"')) >= 0)
      return gloss.substring(0, idx - 2);
    return gloss;
  }
  
  public static void main(String[] args) throws FileNotFoundException
  {  
/*    String[] s = {"the boy", "girl", "the ho"};
    System.out.println(WordnetUtil.asList(s));*/
    System.out.println(getResourceStream(WordnetUtil.class, RiWordnet.WORDNET_ARCHIVE));
  }


}// end
