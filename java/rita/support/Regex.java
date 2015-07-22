package rita.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for regular expression matching.
 *  
 *  Defaults to case-sensitive, multi-line matching.
 * 
 *  RegExps:
 *    http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html
 * 
 *  Groups beginning with (? are pure, non-capturing groups that do
 *  not capture text and do not count towards the group total.
 *                
 *  ---------------------------- FLAGS -----------------------------
 *  Embedded          Compiled                    Meaning *
    (?i)      Pattern.CASE_INSENSITIVE  Enables case-insensitive matching.
    (?d)      Pattern.UNIX_LINES        Enables Unix lines mode.
    (?m)      Pattern.MULTILINE         Enables multi line mode.
    (?s)      Pattern.DOTALL            Enables "." to match line terminators.
    (?u)      Pattern.UNICODE_CASE      Enables Unicode-aware case folding.
    (?x)      Pattern.COMMENTS          Permits white space and comments in the pattern.
    ---       Pattern.CANON_EQ          Enables canonical equivalence.
    ----------------------------------------------------------------
 */
public class Regex
{  
  private boolean caseInsensitive = false;
  private boolean multiLine = true;
  private Map patterns;
  private int flags = 0;
  
  private static Regex instance; 
  public static Regex getInstance()
  {
    if (instance == null)
      instance = new Regex();
    return instance;
  }
  
  private Regex() {
    this.patterns = new HashMap();  
  }
  
  /**
   * Returns a matcher for the given pattern and string
   * @param pattern
   * @param testStr
   */
  public Matcher getMatcher(String pattern, String testStr) {
    return getPattern(pattern).matcher(testStr);
  }

  private Pattern getPattern(String pattern)
  {
    Pattern pat = (Pattern)patterns.get(pattern);
    if (pat == null) {      
      pat = Pattern.compile(pattern, getFlags());
      patterns.put(pattern, pat);
    }
    return pat;
  }
  
  /**
   * Regex.split(" ?search ?", "first Search me, then search your Search History")));
   *   returns ["first", "me, then", "your", "History"]
   * @param pattern
   * @param testStr
   */
  public String[] split(String pattern, String testStr) {
    return getPattern(pattern).split(testStr);
  }
  
  /**
   * Regex.test("&nbsp;", "<br>&nbsp;<br>") returns true
   * Regex.test("search", "clear your existing Search History") 
   *   returns true if ignore-case is true
   * @param pattern
   * @param input
   */
  public boolean test(String pattern, String input) {
    return getMatcher(pattern, input).find();
  }
  
  /**
   * Regex.matches("a*b", "aaaaaaaaaab") returns true
   * Regex.matches("a*b", "caaaaaaaabc") returns false
   * @param pattern
   * @param testStr
   */
  public boolean matches(String pattern, String testStr) {
    return getMatcher(pattern, testStr).matches();
  }
  
  /**
   * <pre>
   *     in = "sdfakjsdhfljksh Fee! Fie! Foe! Fum! sdfakjsdhfljksh Fee! Fie! Foe! Fum!";
         pat = "((F[a-z]{2}! ){4})+";
         Regex.groups(pat, in) returns ["Fee! Fie! Foe! Fum!", "Fee! Fie! Foe! Fum!"]
     </pre>
   *   
   * @param pattern
   * @param testStr
   */
  public String[] groups(String pattern, String testStr) {
    //System.out.println("RegEx.groups(/"+pattern+"/, '"+testStr+"');");
    String[] groups = null;
    Matcher m = getMatcher(pattern, testStr);
    if (m.find()) {
      //System.out.println("group: "+m.group());
      int groupCount = m.groupCount();
      //System.out.println("match! "+groupCount+" group(s)");
      groups = new String[groupCount+1];
      for (int i = 0; i <= groupCount ; i++) {
        //System.out.println("group "+i+": "+m.group(i));
        groups[i] = m.group(i); 
      }
    }
   // else System.out.println("no match");
    return groups;
  }
  
  /**
   * Regex.replace("&nbsp;", "[aa]&nbsp;[bb]", " ") returns '[aa] [bb]'
   * @param pattern
   * @param fullStr
   * @param replaceStr
   */
  public String replace(String pattern, String fullStr, String replaceStr) {
    //System.out.println("Regex.replace("+fullStr+")");
    return getMatcher(pattern, fullStr).replaceAll(replaceStr);
  }
  
  /**
   * Set flags to override boolean vars (caseInsensitive & multiLine)
   * with a custom bitmask
   * @param flags
   */
  public void setFlags(int flags) {
    this.flags = flags;
    patterns.clear();
  }
  
  private int getFlags() { // hideous
    if (flags != 0) return flags;
    if (caseInsensitive && multiLine)
      return Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;
    if (caseInsensitive) return Pattern.CASE_INSENSITIVE;
    if (multiLine) return Pattern.MULTILINE;
    return flags;
  }
  
  public boolean isCaseInsensitive(){
    return caseInsensitive;
  }

  public void setCaseInsensitive(boolean caseInsensitive){
    this.caseInsensitive = caseInsensitive;
    patterns.clear();
  }

  public boolean isMultiLine(){
    return multiLine;
  }

  public void setMultiLine(boolean multiLine) {
    this.multiLine = multiLine;
    patterns.clear();
  }
    
  public static void main(String[] args)
  {
    Regex regex = Regex.getInstance();
    String in, pat;
    String[] grps = null; 
    System.out.println(regex.matches("frisson", "a frisson of surprise shot through him"));
    System.out.println(regex.matches("a*b", "aaaaaaaaaab"));
    System.out.println(regex.replace("&nbsp;", "<br>&nbsp;<br>", " "));
    System.out.println(regex.test("&nbsp;", "<br>&nbsp;<br>"));
    System.out.println("search? "+regex.test("search", "clear your existing Search History")+"\n");
    System.out.println("split? "+Arrays.asList
      (regex.split(" ?search ?", "first Search me, then search your Search History")));

    in = "sdfakjsdhfljksh Fee! Fie! Foe! Fum! sdfakjsdhfljksh Fee! Fie! Foe! Fum!";
    pat = "((F[a-z]{2}! ){4})+";
    grps = regex.groups(pat, in);    
    System.out.println(grps == null ? 
      "null" : grps.length+"] "+Arrays.asList(grps));
    System.out.println("-----------------------------");
    grps = regex.groups("([a-e|f-z])", "a");    
    System.out.println(grps == null ? 
      "null" : grps.length+"] "+Arrays.asList(grps));
 }
  
}// end
