package rita.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.RiTa;

/**
 * A simple pluralizer for nouns. 
 * Uses a combination of letter-based rules and a lookup 
 * table of irregular exceptions, e.g., 'appendix' -> 'appendices'
 * 
    Note: this implementation is closely follows rules found in the MorphG package,
    further described here:<p>
      Minnen, G., Carroll, J., and Pearce, D. (2001). Applied Morphological Processing of English.
      Natural Language Engineering 7(3): 207--223.    
 */
public class Pluralizer implements Constants 
{    
  // privates --------------------
  private final static Matcher wordMatcher = Pattern.compile(ANY_STEM).matcher(E);
  private final static RegexRule defaultRule = DEFAULT_PLURAL_RULE;
  
	/**
	 * Returns the regular or irregular plural form of <code>noun</code>. 
   */
  public static String pluralize(String noun) 
  {
    boolean dbug = false;
    
    String result = null;
    
    wordMatcher.reset(noun);

    if (!wordMatcher.matches() || MODALS.contains(noun))
      return noun;
    
    for (int i = 0; i < PLURAL_RULES.length; i++) {        
      
      RegexRule currentRule = PLURAL_RULES[i];
      
      if (currentRule.applies(noun)) {
        
        if (dbug)System.out.print("applying rule "+i+" -> ");
        
        result = currentRule.fire(noun);
        
        if (dbug)System.out.println(result+"\n"+currentRule);
        
        break;
      }
    }

    if ((result == null) && (defaultRule != null)) {
      
      result = defaultRule.fire(noun);
      
      if (dbug)System.out.println("applying default: -> "+result);
    }

    return result;
  }  
  
  public static void main(String[] args)
  {
    System.out.println(Pluralizer.pluralize("crisis"));
    System.out.println(Pluralizer.pluralize("thesis"));
    System.out.println(Pluralizer.pluralize("stimuli"));
    System.out.println(Pluralizer.pluralize("alumni"));
    System.out.println(Pluralizer.pluralize("corpus"));
    System.out.println(Pluralizer.pluralize("memory"));
    System.out.println(RiTa.stem("memory"));
  }

}// end
