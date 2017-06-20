package rita.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.RiTa;


public class Singularizer implements Constants 
{    
  // privates --------------------
  private final static Matcher wordMatcher = Pattern.compile(ANY_STEM).matcher(E);
  private final static RegexRule defaultRule = DEFAULT_PLURAL_RULE;
  
  /**
   * Returns the regular or irregular plural form of <code>noun</code>. 
   */
  public static String singularize(String noun) 
  {
    boolean dbug = false;
    
    String result = null;
    
    wordMatcher.reset(noun);

    if (!wordMatcher.matches() || MODALS.contains(noun))
      return noun;
    
    for (int i = 0; i < SINGULAR_RULES.length; i++) {        
      
      RegexRule currentRule = SINGULAR_RULES[i];
      
      if (currentRule.applies(noun)) {
        
        if (dbug) System.out.print("applying rule "+i+" -> ");
        
        result = currentRule.fire(noun);
        
        if (dbug) System.out.println(result+"\n"+currentRule);
        
        break;
      }
    }

    if ((result == null) && (defaultRule != null)) {
      
      result = Stemmer.getInstance(StemmerType.Pling).stem(noun);
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
