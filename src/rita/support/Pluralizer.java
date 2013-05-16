package rita.support;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple pluralizer for nouns. Pass it a stemmed noun
 * (see RiStemmer) and it will return the plural form.
 * Uses a combination of letter-based rules and a lookup 
 * table of irregular exceptions, e.g., 'appendix' -> 'appendices'
 * 
    Note: this implementation is based closely follows rules found in the MorphG package,
    further described here:<p>
      Minnen, G., Carroll, J., and Pearce, D. (2001). Applied Morphological Processing of English.
      Natural Language Engineering 7(3): 207--223.    
 */
public class Pluralizer implements Constants 
{    
  private static Pluralizer instance;
  
  // privates --------------------
  private Matcher wordMatcher;
  private RegexRule[] rules;
  private RegexRule defaultRule;

  public static Pluralizer getInstance() {
    if (instance == null) 
      instance = new Pluralizer();
    return instance;
  }
  
  private Pluralizer() {
    this.defaultRule = DEFAULT_PLURAL_RULE;
    this.wordMatcher = Pattern.compile(ANY_STEM).matcher("blablabla");
    this.rules = PLURAL_RULES;
  }
  
  // statics ----------------
  private static List MODALS = Arrays.asList(new String[] 
    { "shall", "would", "may", "might", "ought", "should" });

  private static final String ANY_STEM = "^((\\w+)(-\\w+)*)(\\s((\\w+)(-\\w+)*))*$";
  private static final String C = "[bcdfghjklmnpqrstvwxyz]";
  private static final String VL = "[lraeiou]";
  
	private static final RegexRule DEFAULT_PLURAL_RULE = new RegexRule(ANY_STEM, 0, "s", 2);

	private static final RegexRule[] PLURAL_RULES = new RegexRule[] {
	 
	    NULL_PLURALS,
			new RegexRule("^(piano|photo|solo|ego|tobacco|cargo|golf|grief)$", 0,"s"),
			new RegexRule("^(wildlife)$", 0, "s"),
			new RegexRule(C + "o$", 0, "es"),
			new RegexRule(C + "y$", 1, "ies"),
	    new RegexRule("^ox$", 0, "en"),
			new RegexRule("([zsx]|ch|sh)$", 0, "es"),
			new RegexRule(VL + "fe$", 2, "ves"),
			new RegexRule(VL + "f$", 1, "ves"),
			new RegexRule("(eu|eau)$", 0, "x"),
			new RegexRule("(man|woman)$", 2, "en"),

			new RegexRule("money$", 2, "ies"),
			new RegexRule("person$", 4, "ople"),
			new RegexRule("motif$", 0, "s"),
			new RegexRule("^meninx|phalanx$", 1, "ges"),
			new RegexRule("(xis|sis)$", 2, "es"),
			new RegexRule("schema$", 0, "ta"),
			new RegexRule("^bus$", 0, "ses"),
			new RegexRule("child$", 0, "ren"),
			new RegexRule("^(curi|formul|vertebr|larv|uln|alumn|signor|alg)a$", 0,"e"),
			new RegexRule("^corpus$", 2, "ora"),
			new RegexRule("^(maharaj|raj|myn|mull)a$", 0, "hs"),
			new RegexRule("^aide-de-camp$", 8, "s-de-camp"),
			new RegexRule("^apex|cortex$", 2, "ices"),
			new RegexRule("^weltanschauung$", 0, "en"),
			new RegexRule("^lied$", 0, "er"),
			new RegexRule("^tooth$", 4, "eeth"),
			new RegexRule("^[lm]ouse$", 4, "ice"),
			new RegexRule("^foot$", 3, "eet"),
			new RegexRule("femur", 2, "ora"),
			new RegexRule("goose", 4, "eese"),
			new RegexRule("(human|german|roman)$", 0, "s"),
			new RegexRule("(crisis)$", 2, "es"),
			new RegexRule("^(monarch|loch|stomach)$", 0, "s"),
			new RegexRule("^(taxi|chief|proof|ref|relief|roof|belief)$", 0, "s"),
			new RegexRule("^(co|no)$", 0, "'s"),

			// Latin stems
			new RegexRule("^(memorandum|bacterium|curriculum|minimum|"
					+ "maximum|referendum|spectrum|phenomenon|criterion)$", 2,"a"),
			new RegexRule("^(appendix|index|matrix)", 2, "ices"),
			new RegexRule("^(stimulus|alumnus)$", 2, "i"),
	};

	/**
	 * Returns the regular or irregular plural form of <code>noun</code>. 
   */
  public String pluralize(String noun) 
  {
    boolean dbug = false;
    
    wordMatcher.reset(noun);

    if (!wordMatcher.matches())
      return noun;

    if (MODALS.contains(noun))
      return noun;

    String result = null;
    for (int i = 0; i < PLURAL_RULES.length; i++) {         
      RegexRule currentRule = PLURAL_RULES[i];
      if (currentRule.applies(noun)) {
        if (dbug)System.out.print("applying rule "+i+" -> ");
        result = currentRule.fire(noun);
        if (dbug)System.out.println(result);
        break;
      }
    }

    if ((result == null) && (defaultRule != null)) {
      result = defaultRule.fire(noun);
      if (dbug)System.out.println("applying default: -> "+result);

    }

    return result;
  }  

}// end
