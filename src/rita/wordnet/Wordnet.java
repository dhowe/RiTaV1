package rita.wordnet;


/**
 * Constants for the Wordnet package
 * @invisible
 * @author dhowe
 * <p>See the accompanying documentation for license information
 */
public interface Wordnet
{
  // Filter flags ----------------------------------
  public static final int ANAGRAMS =       1; 
  public static final int CONTAINS =       8; 
  public static final int ENDS_WITH =      16;
  public static final int REGEX_MATCH  =   64;
  public static final int STARTS_WITH =    128;  
  public static final int SIMILAR_TO =     256;
  public static final int SOUNDS_LIKE =    512;
  public static final int WILDCARD_MATCH = 1024;
  public static final int HAS_EXAMPLE    = 2048;
  
  static final int CONTAINS_ALL =   2;  // later
  static final int CONTAINS_SOME =  4;  // later
  static final int EXACT_MATCH =    32; // not used
  
  public static final String WORDNET_ARCHIVE = "wdict.dat";
  
  public static final String SYNSET_DELIM=":", QQ="";
  public static final String DEFAULT_CONF = "file_properties.xml";      
   
  public static final int[] ALL_FILTERS = {    
    ENDS_WITH,STARTS_WITH, ANAGRAMS, CONTAINS_ALL,
    CONTAINS_SOME,CONTAINS,SIMILAR_TO, SOUNDS_LIKE,
    WILDCARD_MATCH,REGEX_MATCH// HAS_EXAMPLE
  };
  
}// end