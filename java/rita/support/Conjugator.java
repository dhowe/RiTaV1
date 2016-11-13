package rita.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.RiTaException;

// TODO: add forms: INFINITIVE, GERUND, MODAL, etc.
//       handle complements?

// Enumerate incompatible combinations 

/**
 * Handles verb conjugation based on tense, person, number for simple, passive,
 * progressive, and perfect forms. An example:
 * 
 * <pre>
 * RiConjugator rc = new RiConjugator(this);
 * rc.setNumber(&quot;plural&quot;);
 * rc.setPerson(&quot;2nd&quot;);
 * rc.setTense(&quot;past&quot;);
 * rc.setPassive(true);
 * rc.setPerfect(true);
 * rc.setProgressive(false);
 * String c = rc.conjugate(&quot;announce&quot;);
 * </pre>
 * 
 * <br>
 * Note: this implementation is based closely on rules found in the MorphG
 * package, further described here:
 * <p>
 * Minnen, G., Carroll, J., and Pearce, D. (2001). Applied Morphological
 * Processing of English. Natural Language Engineering 7(3): 207--223.
 */
public class Conjugator implements Constants {
  
  public static final boolean DBUG = false;

  private static final String PAST_TENSE_RULE = "pastTense";
  private static final String PRESENT_TENSE_RULE = "presentTense";
  private static final String PAST_PARTICIPLE_RULE = "pastParticiple";
  private static final String PRESENT_PARTICIPLE_RULE = "presentParticiple";

  private static final String CONS = "[bcdfghjklmnpqrstvwxyz]";
  private static final String ANY_STEM = "^((\\w+)(-\\w+)*)(\\s((\\w+)(-\\w+)*))*$";
  private static final String VERBAL_PREFIX = "((be|with|pre|un|over|re|mis|under|out|up|fore|for|counter|co|sub)(-?))";

  private Map ruleMap;
  private boolean perfect, progressive, passive, allowsTense, interrogative;
  private int tense = PRESENT_TENSE, person = FIRST_PERSON, number = SINGULAR;
  private String particle, modal; // modal, eg "must"
  private int form = NORMAL; // other forms?? GERUND, INFINITIVE
  private Matcher matcher;

  public Conjugator() {

    allowsTense = true;
    matcher = Pattern.compile(ANY_STEM).matcher("");
    this.createRuleMap();
  }

  private void createRuleMap() {
    ruleMap = new HashMap();
    ruleMap.put(PAST_TENSE_RULE, new Rule("PAST_TENSE", DEFAULT_PAST_RULE,
	PAST_TENSE_RULES));
    ruleMap.put(PRESENT_TENSE_RULE, new Rule("PRESENT_TENSE",
	DEFAULT_PRESENT_TENSE, PRESENT_TENSE_RULES));
    ruleMap.put(PAST_PARTICIPLE_RULE, new Rule("PAST_PARTICIPLE",
	DEFAULT_PP_RULE, PAST_PARTICIPLE_RULES));
    ruleMap.put(PRESENT_PARTICIPLE_RULE, new Rule("ING_FORM", DEFAULT_ING_RULE,
	ING_FORM_RULES));
  }

  class Rule {

    public RegexRule defaultRule;
    public RegexRule[] rules;
    public String name;
    public boolean doubling = true;

    public Rule(String name, RegexRule defaultRule, RegexRule[] rules) {

      this.defaultRule = defaultRule;
      this.rules = rules;
      // Arrays.sort(rules);
      this.name = name;
      if (name.equals("PRESENT_TENSE"))
	doubling = false;
    }

    public String toString() {
      return name;
    }
  }// end Rule

  private String doubleFinalConsonant(String word) {
    StringBuffer buffer = new StringBuffer(word);
    buffer.append(buffer.charAt(buffer.length() - 1));
    return buffer.toString();
  }

  private Rule getRule(String name) {
    return (Rule) ruleMap.get(name);
  }

  private String render(String morphForm) {
    return particle != null ? morphForm + " " + particle : morphForm;
  }

  /**
   * Processes the baseform of this <code>Verb</code> to obtain the present
   * tense form for a specific <code>Person</code> and <code>Number</code>
   * combination. The default behaviour is to assume that all present tense
   * forms except for 3rd person singular are identical to the baseform. This is
   * only every not the case with the verb <I> be</I> which has different forms
   * for different person and number values (<I>am</I>, <I>are</I> etc).
   */
  private String getPresent(String baseForm) {
    // int p = this.person, n = this.number;
    return getPresent(baseForm, person, number);
  }

  /**
   * Processes the baseform of this <code>Verb</code> to obtain the present
   * tense form for a specific <code>Person</code> and <code>Number</code>
   * combination. The default behaviour is to assume that all present tense
   * forms except for 3rd person singular are identical to the baseform. This is
   * only every not the case with the verb <I> be</I> which has different forms
   * for different person and number values (<I>am</I>, <I>are</I> etc).
   */
  public String getPresent(String baseForm, int person, int number) {
    // int p = this.person, n = this.number;
    // String baseForm = this.head; // is this right? or getBaseForm()?

    if ((person == THIRD_PERSON) && (number == SINGULAR))
      return apply(getRule(PRESENT_TENSE_RULE), baseForm);

    else if (baseForm.equalsIgnoreCase("be")) {
      if (number == SINGULAR)
	switch (person) {
	case FIRST_PERSON:
	  return render("am");

	case SECOND_PERSON:
	  return render("are");

	case THIRD_PERSON:
	  return render("is");

	default:
	  return ""; // makes java happy -- not needed
	}

      return render("are");
    } else
      return render(baseForm);
  }

  private String apply(Rule rule, String verb) {
    boolean dbug = false;

    String baseForm = getBaseForm(verb);

    matcher.reset(baseForm);

    if (!matcher.matches())
      return baseForm;

    if (MODALS.contains(baseForm))
      return baseForm;

    String result = null;

    if (dbug)
      System.out.println("RULE: " + rule);

    RegexRule _defaultRule = rule.defaultRule;
    RegexRule[] _rules = rule.rules;

    for (int i = 0; i < _rules.length; i++) {

      RegexRule currentRule = _rules[i];

      if (dbug)
	System.out.println("  '" + baseForm + "'\n  checking: " + currentRule);

      if (currentRule.applies(baseForm)) {

	if (dbug)
	  System.out.println("  HIT: " + currentRule);

	result = currentRule.fire(baseForm);
	break;
      }
    }

    if ((result == null) && (_defaultRule != null)) {
      if (isDoubling(rule, verb)) {

	if (dbug)
	  System.out.println();

	baseForm = doubleFinalConsonant(baseForm);
      }

      if (dbug)
	System.out.println("  DEFAULT: " + _defaultRule);

      result = _defaultRule.fire(baseForm);
    }

    if (dbug)
      System.out.println("  RESULT: " + result);

    return result;
  }

  /*
   * Note: always true for verbs in the list (TODO: check for nouns!)
   * 
   * REturns whether this rule should apply consonant doubling to lexical items
   * that take it. This applies in the case of the verb inflection rules, for
   * example, but not for noun pluralisation. Thus, the verb <I>jet</I> becomes
   * <I>jet<U>t</U>ing</I> in the present participle, but the noun <I>jet</I> is
   * pluralised as <I>jets</I>.
   */
  private boolean isDoubling(Rule r, String verb) {
    return r.doubling && VERB_CONS_DOUBLING.contains(verb);
  }

  // VERB_FORMS ---------------------------------------

  private static RegexRule DEFAULT_ING_RULE = new RegexRule(ANY_STEM, 0, "ing",
      2);
  private static RegexRule DEFAULT_PAST_RULE = new RegexRule(ANY_STEM, 0, "ed",
      2);
  private static RegexRule DEFAULT_PP_RULE = new RegexRule(ANY_STEM, 0, "ed", 2);
  private static RegexRule DEFAULT_PRESENT_TENSE = new RegexRule(ANY_STEM, 0,
      "s", 2);

  /** Default pattern-action rules for ING-Form. */
  private static RegexRule[] ING_FORM_RULES = new RegexRule[] {
      new RegexRule(CONS + "ie$", 2, "ying", 1),
      new RegexRule("[^ie]e$", 1, "ing", 1),
      new RegexRule("^bog-down$", 5, "ging-down", 0),
      new RegexRule("^chivy$", 1, "vying", 0),
      new RegexRule("^gen-up$", 3, "ning-up", 0),
      new RegexRule("^trek$", 1, "cking", 0),
      new RegexRule("^ko$", 0, "'ing", 0),
      new RegexRule("^(age|be)$", 0, "ing", 0),
      new RegexRule("(ibe)$", 1, "ing", 0), };

  /** List of PastParticiple rules. */
  private static RegexRule[] PAST_PARTICIPLE_RULES = new RegexRule[] {

      new RegexRule(CONS + "y$", 1, "ied", 1),
      new RegexRule("^" + VERBAL_PREFIX + "?(bring)$", 3, "ought", 0),
      new RegexRule("^" + VERBAL_PREFIX
	  + "?(take|rise|strew|blow|draw|drive|know|give|sake|"
	  + "arise|gnaw|grave|grow|hew|know|mow|see|sew|throw|"
	  + "partake|prove|saw|quartersaw|shake|shew|show|shrive|"
	  + "sightsee|strew|strive)$", 0, "n", 0),

      new RegexRule("^" + VERBAL_PREFIX + "?[gd]o$", 0, "ne", 1),

      new RegexRule("^(beat|eat|be|fall)$", 0, "en", 0),
      new RegexRule("^(have)$", 2, "d", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?bid$", 0, "den", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?[lps]ay$", 1, "id", 1),
      new RegexRule("^behave$", 0, "d", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?have$", 2, "d", 1),
      new RegexRule("(sink|slink|drink|shrink|stink)$", 3, "unk", 0),
      new RegexRule("(([sfc][twlp]?r?|w?r)ing|hang)$", 3, "ung", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?(shear|swear|bear|wear|tear)$", 3,
	  "orn", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?(bend|spend|send|lend)$", 1, "t", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?(weep|sleep|sweep|creep|keep$)$",
	  2, "pt", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?(sell|tell)$", 3, "old", 0),
      new RegexRule("^(outfight|beseech)$", 4, "ought", 0),
      new RegexRule("^bethink$", 3, "ought", 0),
      new RegexRule("^buy$", 2, "ought", 0),
      new RegexRule("^aby$", 1, "ought", 0),
      new RegexRule("^tarmac", 0, "ked", 0),
      new RegexRule("^abide$", 3, "ode", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?(speak|(a?)wake|break)$", 3,
	  "oken", 0),
      new RegexRule("^backbite$", 1, "ten", 0),
      new RegexRule("^backslide$", 1, "den", 0),
      new RegexRule("^become$", 3, "ame", 0),
      new RegexRule("^begird$", 3, "irt", 0),
      new RegexRule("^outlie$", 2, "ay", 0),
      new RegexRule("^rebind$", 3, "ound", 0),
      new RegexRule("^relay$", 2, "aid", 0),
      new RegexRule("^shit$", 3, "hat", 0),
      new RegexRule("^bereave$", 4, "eft", 0),
      new RegexRule("^foreswear$", 3, "ore", 0),
      new RegexRule("^overfly$", 1, "own", 0),
      new RegexRule("^beget$", 2, "otten", 0),
      new RegexRule("^begin$", 3, "gun", 0),
      new RegexRule("^bestride$", 1, "den", 0),
      new RegexRule("^bite$", 1, "ten", 0),
      new RegexRule("^bleed$", 4, "led", 0),
      new RegexRule("^bog-down$", 5, "ged-down", 0),
      new RegexRule("^bind$", 3, "ound", 0),
      new RegexRule("^(.*)feed$", 4, "fed", 0),
      new RegexRule("^breed$", 4, "red", 0),
      new RegexRule("^brei", 0, "d", 0),
      new RegexRule("^bring$", 3, "ought", 0),
      new RegexRule("^build$", 1, "t", 0),
      new RegexRule("^come", 0, "", 0),
      new RegexRule("^catch$", 3, "ught", 0),
      new RegexRule("^chivy$", 1, "vied", 0),
      new RegexRule("^choose$", 3, "sen", 0),
      new RegexRule("^cleave$", 4, "oven", 0),
      new RegexRule("^crossbreed$", 4, "red", 0),
      new RegexRule("^deal", 0, "t", 0),
      new RegexRule("^dow$", 1, "ught", 0),
      new RegexRule("^dream", 0, "t", 0),
      new RegexRule("^dig$", 3, "dug", 0),
      new RegexRule("^dwell$", 2, "lt", 0),
      new RegexRule("^enwind$", 3, "ound", 0),
      // new PatternRule("^feed$", 4, "fed", 0),
      new RegexRule("^feel$", 3, "elt", 0),
      new RegexRule("^flee$", 2, "ed", 0),
      new RegexRule("^floodlight$", 5, "lit", 0),
      new RegexRule("^fly$", 1, "own", 0),
      new RegexRule("^forbear$", 3, "orne", 0),
      new RegexRule("^forerun$", 3, "ran", 0),
      new RegexRule("^forget$", 2, "otten", 0),
      new RegexRule("^fight$", 4, "ought", 0),
      new RegexRule("^find$", 3, "ound", 0),
      new RegexRule("^freeze$", 4, "ozen", 0),
      new RegexRule("^gainsay$", 2, "aid", 0),
      new RegexRule("^gin$", 3, "gan", 0),
      new RegexRule("^gen-up$", 3, "ned-up", 0),
      new RegexRule("^ghostwrite$", 1, "ten", 0),
      new RegexRule("^get$", 2, "otten", 0),
      new RegexRule("^grind$", 3, "ound", 0),
      new RegexRule("^hacksaw", 0, "n", 0),
      // new PatternRule("^handfeed$", 4, "fed", 0),
      new RegexRule("^hear", 0, "d", 0),
      new RegexRule("^hold$", 3, "eld", 0),
      new RegexRule("^hide$", 1, "den", 0),
      new RegexRule("^honey$", 2, "ied", 0),
      new RegexRule("^inbreed$", 4, "red", 0),
      new RegexRule("^indwell$", 3, "elt", 0),
      new RegexRule("^interbreed$", 4, "red", 0),
      new RegexRule("^interweave$", 4, "oven", 0),
      new RegexRule("^inweave$", 4, "oven", 0),
      new RegexRule("^ken$", 2, "ent", 0),
      new RegexRule("^kneel$", 3, "elt", 0),
      new RegexRule("^lie$", 2, "ain", 0),
      new RegexRule("^leap$", 0, "t", 0),
      new RegexRule("^learn$", 0, "t", 0),
      new RegexRule("^lead$", 4, "led", 0),
      new RegexRule("^leave$", 4, "eft", 0),
      new RegexRule("^light$", 5, "lit", 0),
      new RegexRule("^lose$", 3, "ost", 0),
      new RegexRule("^make$", 3, "ade", 0),
      new RegexRule("^mean", 0, "t", 0),
      new RegexRule("^meet$", 4, "met", 0),
      new RegexRule("^misbecome$", 3, "ame", 0),
      new RegexRule("^misdeal$", 2, "alt", 0),
      new RegexRule("^mishear$", 1, "d", 0),
      new RegexRule("^mislead$", 4, "led", 0),
      new RegexRule("^misunderstand$", 3, "ood", 0),
      new RegexRule("^outbreed$", 4, "red", 0),
      new RegexRule("^outrun$", 3, "ran", 0),
      new RegexRule("^outride$", 1, "den", 0),
      new RegexRule("^outshine$", 3, "one", 0),
      new RegexRule("^outshoot$", 4, "hot", 0),
      new RegexRule("^outstand$", 3, "ood", 0),
      new RegexRule("^outthink$", 3, "ought", 0),
      new RegexRule("^outgo$", 2, "went", 0),
      new RegexRule("^overbear$", 3, "orne", 0),
      new RegexRule("^overbuild$", 3, "ilt", 0),
      new RegexRule("^overcome$", 3, "ame", 0),
      new RegexRule("^overfly$", 2, "lew", 0),
      new RegexRule("^overhear$", 2, "ard", 0),
      new RegexRule("^overlie$", 2, "ain", 0),
      new RegexRule("^overrun$", 3, "ran", 0),
      new RegexRule("^override$", 1, "den", 0),
      new RegexRule("^overshoot$", 4, "hot", 0),
      new RegexRule("^overwind$", 3, "ound", 0),
      new RegexRule("^overwrite$", 1, "ten", 0),

      new RegexRule("^rebuild$", 3, "ilt", 0),
      new RegexRule("^red$", 3, "red", 0),
      new RegexRule("^redo$", 1, "one", 0),
      new RegexRule("^remake$", 3, "ade", 0),
      // new RegexRule("^run$", 3, "run", 0), // DH
      // new RegexRule("^rerun$", 3, "ran", 0), // DH
      new RegexRule("^resit$", 3, "sat", 0),
      new RegexRule("^rethink$", 3, "ought", 0),
      new RegexRule("^rewind$", 3, "ound", 0),
      new RegexRule("^rewrite$", 1, "ten", 0),
      new RegexRule("^ride$", 1, "den", 0),
      new RegexRule("^reeve$", 4, "ove", 0),
      new RegexRule("^sit$", 3, "sat", 0),
      new RegexRule("^shoe$", 3, "hod", 0),
      new RegexRule("^shine$", 3, "one", 0),
      new RegexRule("^shoot$", 4, "hot", 0),
      new RegexRule("^ski$", 1, "i'd", 0),
      new RegexRule("^slide$", 1, "den", 0),
      new RegexRule("^smite$", 1, "ten", 0),
      new RegexRule("^seek$", 3, "ought", 0),
      new RegexRule("^spit$", 3, "pat", 0),
      new RegexRule("^speed$", 4, "ped", 0),
      new RegexRule("^spellbind$", 3, "ound", 0),
      new RegexRule("^spoil$", 2, "ilt", 0),
      new RegexRule("^spotlight$", 5, "lit", 0),
      new RegexRule("^spin$", 3, "pun", 0),
      new RegexRule("^steal$", 3, "olen", 0),
      new RegexRule("^stand$", 3, "ood", 0),
      new RegexRule("^stave$", 3, "ove", 0),
      new RegexRule("^stride$", 1, "den", 0),
      new RegexRule("^strike$", 3, "uck", 0),
      new RegexRule("^stick$", 3, "uck", 0),
      new RegexRule("^swell$", 3, "ollen", 0),
      new RegexRule("^swim$", 3, "wum", 0),
      new RegexRule("^teach$", 4, "aught", 0),
      new RegexRule("^think$", 3, "ought", 0),
      new RegexRule("^tread$", 3, "odden", 0),
      new RegexRule("^typewrite$", 1, "ten", 0),
      new RegexRule("^unbind$", 3, "ound", 0),
      new RegexRule("^underbuy$", 2, "ought", 0),
      // new PatternRule("^underfeed$", 4, "fed", 0),
      new RegexRule("^undergird$", 3, "irt", 0),
      new RegexRule("^undergo$", 1, "one", 0),
      new RegexRule("^underlie$", 2, "ain", 0),
      new RegexRule("^undershoot$", 4, "hot", 0),
      new RegexRule("^understand$", 3, "ood", 0),
      new RegexRule("^unfreeze$", 4, "ozen", 0),
      new RegexRule("^unlearn", 0, "t", 0),
      new RegexRule("^unmake$", 3, "ade", 0),
      new RegexRule("^unreeve$", 4, "ove", 0),
      new RegexRule("^unstick$", 3, "uck", 0),
      new RegexRule("^unteach$", 4, "aught", 0),
      new RegexRule("^unthink$", 3, "ought", 0),
      new RegexRule("^untread$", 3, "odden", 0),
      new RegexRule("^unwind$", 3, "ound", 0),
      new RegexRule("^upbuild$", 1, "t", 0),
      new RegexRule("^uphold$", 3, "eld", 0),
      new RegexRule("^upheave$", 4, "ove", 0),
      new RegexRule("^waylay$", 2, "ain", 0),
      new RegexRule("^whipsaw$", 2, "awn", 0),
      // new PatternRule("^winterfeed$", 4, "fed", 0),
      new RegexRule("^withhold$", 3, "eld", 0),
      new RegexRule("^withstand$", 3, "ood", 0),
      new RegexRule("^win$", 3, "won", 0),
      new RegexRule("^wind$", 3, "ound", 0),
      new RegexRule("^weave$", 4, "oven", 0),
      new RegexRule("^write$", 1, "ten", 0),
      new RegexRule("^trek$", 1, "cked", 0),
      new RegexRule("^ko$", 1, "o'd", 0),
      new RegexRule("^win$", 2, "on", 0),

      new RegexRule("e$", 0, "d", 1), // DH

      // null past forms
      new RegexRule(
	  "^"
	      + VERBAL_PREFIX
	      + "?(cast|thrust|typeset|cut|bid|upset|wet|bet|cut|"
	      + "hit|hurt|inset|let|cost|burst|beat|beset|set|upset|hit|"
	      + "offset|put|quit|wed|typeset|wed|spread|split|slit|read|run|shut|shed)$",
	  0, "", 0) };

  /** List of Past Tense rules. */
  private static RegexRule[] PAST_TENSE_RULES = new RegexRule[] {
      new RegexRule("^(reduce)$", 0, "d", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?[pls]ay$", 1, "id", 1),
      new RegexRule(CONS + "y$", 1, "ied", 1),
      new RegexRule("^(fling|cling|hang)$", 3, "ung", 0),
      new RegexRule("(([sfc][twlp]?r?|w?r)ing)$", 3, "ang", 1),
      new RegexRule("^" + VERBAL_PREFIX + "?(bend|spend|send|lend|spend)$", 1,
	  "t", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?lie$", 2, "ay", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?(weep|sleep|sweep|creep|keep)$", 2,
	  "pt", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?(sell|tell)$", 3, "old", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?do$", 1, "id", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?dig$", 2, "ug", 0),
      new RegexRule("^behave$", 0, "d", 0),
      new RegexRule("^(have)$", 2, "d", 0),
      new RegexRule("(sink|drink)$", 3, "ank", 0),
      new RegexRule("^swing$", 3, "ung", 0),
      new RegexRule("^be$", 2, "was", 0),
      new RegexRule("^outfight$", 4, "ought", 0),
      new RegexRule("^tarmac", 0, "ked", 0),
      new RegexRule("^abide$", 3, "ode", 0),
      new RegexRule("^aby$", 1, "ought", 0),
      new RegexRule("^become$", 3, "ame", 0),
      new RegexRule("^begird$", 3, "irt", 0),
      new RegexRule("^outlie$", 2, "ay", 0),
      new RegexRule("^rebind$", 3, "ound", 0),
      new RegexRule("^shit$", 3, "hat", 0),
      new RegexRule("^bereave$", 4, "eft", 0),
      new RegexRule("^foreswear$", 3, "ore", 0),
      new RegexRule("^bename$", 3, "empt", 0),
      new RegexRule("^beseech$", 4, "ought", 0),
      new RegexRule("^bethink$", 3, "ought", 0),
      new RegexRule("^bleed$", 4, "led", 0),
      new RegexRule("^bog-down$", 5, "ged-down", 0),
      new RegexRule("^buy$", 2, "ought", 0),
      new RegexRule("^bind$", 3, "ound", 0),
      new RegexRule("^(.*)feed$", 4, "fed", 0),
      new RegexRule("^breed$", 4, "red", 0),
      new RegexRule("^brei$", 2, "eid", 0),
      new RegexRule("^bring$", 3, "ought", 0),
      new RegexRule("^build$", 3, "ilt", 0),
      new RegexRule("^come$", 3, "ame", 0),
      new RegexRule("^catch$", 3, "ught", 0),
      new RegexRule("^clothe$", 5, "lad", 0),
      new RegexRule("^crossbreed$", 4, "red", 0),
      new RegexRule("^deal$", 2, "alt", 0),
      new RegexRule("^dow$", 1, "ught", 0),
      new RegexRule("^dream$", 2, "amt", 0),
      new RegexRule("^dwell$", 3, "elt", 0),
      new RegexRule("^enwind$", 3, "ound", 0),
      // new PatternRule("^feed$", 4, "fed", 0),
      new RegexRule("^feel$", 3, "elt", 0),
      new RegexRule("^flee$", 3, "led", 0),
      new RegexRule("^floodlight$", 5, "lit", 0),
      new RegexRule("^arise$", 3, "ose", 0),
      new RegexRule("^eat$", 3, "ate", 0),
      new RegexRule("^awake$", 3, "oke", 0),
      new RegexRule("^backbite$", 4, "bit", 0),
      new RegexRule("^backslide$", 4, "lid", 0),
      new RegexRule("^befall$", 3, "ell", 0),
      new RegexRule("^begin$", 3, "gan", 0),
      new RegexRule("^beget$", 3, "got", 0),
      new RegexRule("^behold$", 3, "eld", 0),
      new RegexRule("^bespeak$", 3, "oke", 0),
      new RegexRule("^bestride$", 3, "ode", 0),
      new RegexRule("^betake$", 3, "ook", 0),
      new RegexRule("^bite$", 4, "bit", 0),
      new RegexRule("^blow$", 3, "lew", 0),
      new RegexRule("^bear$", 3, "ore", 0),
      new RegexRule("^break$", 3, "oke", 0),
      new RegexRule("^choose$", 4, "ose", 0),
      new RegexRule("^cleave$", 4, "ove", 0),
      new RegexRule("^countersink$", 3, "ank", 0),
      new RegexRule("^drink$", 3, "ank", 0),
      new RegexRule("^draw$", 3, "rew", 0),
      new RegexRule("^drive$", 3, "ove", 0),
      new RegexRule("^fall$", 3, "ell", 0),
      new RegexRule("^fly$", 2, "lew", 0),
      new RegexRule("^flyblow$", 3, "lew", 0),
      new RegexRule("^forbid$", 2, "ade", 0),
      new RegexRule("^forbear$", 3, "ore", 0),
      new RegexRule("^foreknow$", 3, "new", 0),
      new RegexRule("^foresee$", 3, "saw", 0),
      new RegexRule("^forespeak$", 3, "oke", 0),
      new RegexRule("^forego$", 2, "went", 0),
      new RegexRule("^forgive$", 3, "ave", 0),
      new RegexRule("^forget$", 3, "got", 0),
      new RegexRule("^forsake$", 3, "ook", 0),
      new RegexRule("^forspeak$", 3, "oke", 0),
      new RegexRule("^forswear$", 3, "ore", 0),
      new RegexRule("^forgo$", 2, "went", 0),
      new RegexRule("^fight$", 4, "ought", 0),
      new RegexRule("^find$", 3, "ound", 0),
      new RegexRule("^freeze$", 4, "oze", 0),
      new RegexRule("^give$", 3, "ave", 0),
      new RegexRule("^geld$", 3, "elt", 0),
      new RegexRule("^gen-up$", 3, "ned-up", 0),
      new RegexRule("^ghostwrite$", 3, "ote", 0),
      new RegexRule("^get$", 3, "got", 0),
      new RegexRule("^grow$", 3, "rew", 0),
      new RegexRule("^grind$", 3, "ound", 0),
      // new PatternRule("^handfeed$", 4, "fed", 0),
      new RegexRule("^hear$", 2, "ard", 0),
      new RegexRule("^hold$", 3, "eld", 0),
      new RegexRule("^hide$", 4, "hid", 0),
      new RegexRule("^honey$", 2, "ied", 0),
      new RegexRule("^inbreed$", 4, "red", 0),
      new RegexRule("^indwell$", 3, "elt", 0),
      new RegexRule("^interbreed$", 4, "red", 0),
      new RegexRule("^interweave$", 4, "ove", 0),
      new RegexRule("^inweave$", 4, "ove", 0),
      new RegexRule("^ken$", 2, "ent", 0),
      new RegexRule("^kneel$", 3, "elt", 0),
      new RegexRule("^^know$$", 3, "new", 0),
      new RegexRule("^leap$", 2, "apt", 0),
      new RegexRule("^learn$", 2, "rnt", 0),
      new RegexRule("^lead$", 4, "led", 0),
      new RegexRule("^leave$", 4, "eft", 0),
      new RegexRule("^light$", 5, "lit", 0),
      new RegexRule("^lose$", 3, "ost", 0),
      new RegexRule("^make$", 3, "ade", 0),
      new RegexRule("^mean$", 2, "ant", 0),
      new RegexRule("^meet$", 4, "met", 0),
      new RegexRule("^misbecome$", 3, "ame", 0),
      new RegexRule("^misdeal$", 2, "alt", 0),
      new RegexRule("^misgive$", 3, "ave", 0),
      new RegexRule("^mishear$", 2, "ard", 0),
      new RegexRule("^mislead$", 4, "led", 0),
      new RegexRule("^mistake$", 3, "ook", 0),
      new RegexRule("^misunderstand$", 3, "ood", 0),
      new RegexRule("^outbreed$", 4, "red", 0),
      new RegexRule("^outgrow$", 3, "rew", 0),
      new RegexRule("^outride$", 3, "ode", 0),
      new RegexRule("^outshine$", 3, "one", 0),
      new RegexRule("^outshoot$", 4, "hot", 0),
      new RegexRule("^outstand$", 3, "ood", 0),
      new RegexRule("^outthink$", 3, "ought", 0),
      new RegexRule("^outgo$", 2, "went", 0),
      new RegexRule("^outwear$", 3, "ore", 0),
      new RegexRule("^overblow$", 3, "lew", 0),
      new RegexRule("^overbear$", 3, "ore", 0),
      new RegexRule("^overbuild$", 3, "ilt", 0),
      new RegexRule("^overcome$", 3, "ame", 0),
      new RegexRule("^overdraw$", 3, "rew", 0),
      new RegexRule("^overdrive$", 3, "ove", 0),
      new RegexRule("^overfly$", 2, "lew", 0),
      new RegexRule("^overgrow$", 3, "rew", 0),
      new RegexRule("^overhear$", 2, "ard", 0),
      new RegexRule("^overpass$", 3, "ast", 0),
      new RegexRule("^override$", 3, "ode", 0),
      new RegexRule("^oversee$", 3, "saw", 0),
      new RegexRule("^overshoot$", 4, "hot", 0),
      new RegexRule("^overthrow$", 3, "rew", 0),
      new RegexRule("^overtake$", 3, "ook", 0),
      new RegexRule("^overwind$", 3, "ound", 0),
      new RegexRule("^overwrite$", 3, "ote", 0),
      new RegexRule("^partake$", 3, "ook", 0),
      new RegexRule("^" + VERBAL_PREFIX + "?run$", 2, "an", 0),
      new RegexRule("^ring$", 3, "ang", 0),
      new RegexRule("^rebuild$", 3, "ilt", 0),
      new RegexRule("^red", 0, "", 0),
      new RegexRule("^reave$", 4, "eft", 0),
      new RegexRule("^remake$", 3, "ade", 0),
      new RegexRule("^resit$", 3, "sat", 0),
      new RegexRule("^rethink$", 3, "ought", 0),
      new RegexRule("^retake$", 3, "ook", 0),
      new RegexRule("^rewind$", 3, "ound", 0),
      new RegexRule("^rewrite$", 3, "ote", 0),
      new RegexRule("^ride$", 3, "ode", 0),
      new RegexRule("^rise$", 3, "ose", 0),
      new RegexRule("^reeve$", 4, "ove", 0),
      new RegexRule("^sing$", 3, "ang", 0),
      new RegexRule("^sink$", 3, "ank", 0),
      new RegexRule("^sit$", 3, "sat", 0),
      new RegexRule("^see$", 3, "saw", 0),
      new RegexRule("^shoe$", 3, "hod", 0),
      new RegexRule("^shine$", 3, "one", 0),
      new RegexRule("^shake$", 3, "ook", 0),
      new RegexRule("^shoot$", 4, "hot", 0),
      new RegexRule("^shrink$", 3, "ank", 0),
      new RegexRule("^shrive$", 3, "ove", 0),
      new RegexRule("^sightsee$", 3, "saw", 0),
      new RegexRule("^ski$", 1, "i'd", 0),
      new RegexRule("^skydive$", 3, "ove", 0),
      new RegexRule("^slay$", 3, "lew", 0),
      new RegexRule("^slide$", 4, "lid", 0),
      new RegexRule("^slink$", 3, "unk", 0),
      new RegexRule("^smite$", 4, "mit", 0),
      new RegexRule("^seek$", 3, "ought", 0),
      new RegexRule("^spit$", 3, "pat", 0),
      new RegexRule("^speed$", 4, "ped", 0),
      new RegexRule("^spellbind$", 3, "ound", 0),
      new RegexRule("^spoil$", 2, "ilt", 0),
      new RegexRule("^speak$", 3, "oke", 0),
      new RegexRule("^spotlight$", 5, "lit", 0),
      new RegexRule("^spring$", 3, "ang", 0),
      new RegexRule("^spin$", 3, "pun", 0),
      new RegexRule("^stink$", 3, "ank", 0),
      new RegexRule("^steal$", 3, "ole", 0),
      new RegexRule("^stand$", 3, "ood", 0),
      new RegexRule("^stave$", 3, "ove", 0),
      new RegexRule("^stride$", 3, "ode", 0),
      new RegexRule("^strive$", 3, "ove", 0),
      new RegexRule("^strike$", 3, "uck", 0),
      new RegexRule("^stick$", 3, "uck", 0),
      new RegexRule("^swim$", 3, "wam", 0),
      new RegexRule("^swear$", 3, "ore", 0),
      new RegexRule("^teach$", 4, "aught", 0),
      new RegexRule("^think$", 3, "ought", 0),
      new RegexRule("^throw$", 3, "rew", 0),
      new RegexRule("^take$", 3, "ook", 0),
      new RegexRule("^tear$", 3, "ore", 0),
      new RegexRule("^transship$", 4, "hip", 0),
      new RegexRule("^tread$", 4, "rod", 0),
      new RegexRule("^typewrite$", 3, "ote", 0),
      new RegexRule("^unbind$", 3, "ound", 0),
      new RegexRule("^unclothe$", 5, "lad", 0),
      new RegexRule("^underbuy$", 2, "ought", 0),
      // new PatternRule("^underfeed$", 4, "fed", 0),
      new RegexRule("^undergird$", 3, "irt", 0),
      new RegexRule("^undershoot$", 4, "hot", 0),
      new RegexRule("^understand$", 3, "ood", 0),
      new RegexRule("^undertake$", 3, "ook", 0),
      new RegexRule("^undergo$", 2, "went", 0),
      new RegexRule("^underwrite$", 3, "ote", 0),
      new RegexRule("^unfreeze$", 4, "oze", 0),
      new RegexRule("^unlearn$", 2, "rnt", 0),
      new RegexRule("^unmake$", 3, "ade", 0),
      new RegexRule("^unreeve$", 4, "ove", 0),
      new RegexRule("^unspeak$", 3, "oke", 0),
      new RegexRule("^unstick$", 3, "uck", 0),
      new RegexRule("^unswear$", 3, "ore", 0),
      new RegexRule("^unteach$", 4, "aught", 0),
      new RegexRule("^unthink$", 3, "ought", 0),
      new RegexRule("^untread$", 4, "rod", 0),
      new RegexRule("^unwind$", 3, "ound", 0),
      new RegexRule("^upbuild$", 3, "ilt", 0),
      new RegexRule("^uphold$", 3, "eld", 0),
      new RegexRule("^upheave$", 4, "ove", 0),
      new RegexRule("^uprise$", 3, "ose", 0),
      new RegexRule("^upspring$", 3, "ang", 0),
      new RegexRule("^go$", 2, "went", 0),
      // new PatternRule("^winterfeed$", 4, "fed", 0),
      new RegexRule("^wiredraw$", 3, "rew", 0),
      new RegexRule("^withdraw$", 3, "rew", 0),
      new RegexRule("^withhold$", 3, "eld", 0),
      new RegexRule("^withstand$", 3, "ood", 0),
      new RegexRule("^wake$", 3, "oke", 0),
      new RegexRule("^win$", 3, "won", 0),
      new RegexRule("^wear$", 3, "ore", 0),
      new RegexRule("^wind$", 3, "ound", 0),
      new RegexRule("^weave$", 4, "ove", 0),
      new RegexRule("^write$", 3, "ote", 0),
      new RegexRule("^trek$", 1, "cked", 0),
      new RegexRule("^ko$", 1, "o'd", 0), new RegexRule("^bid", 2, "ade", 0),
      new RegexRule("^win$", 2, "on", 0),
      new RegexRule("^swim", 2, "am", 0),

      new RegexRule("e$", 0, "d", 1), // default?

      // null past forms
      new RegexRule(
	  "^"
	      + VERBAL_PREFIX
	      + "?(cast|thrust|typeset|cut|bid|upset|wet|bet|cut|"
	      + "hit|hurt|inset|let|cost|burst|beat|beset|set|upset|hit|"
	      + "offset|put|quit|wed|typeset|wed|spread|split|slit|read|run|shut|shed|lay)$",
	  0, "", 0)

  };

  /** Pattern-action rules to handle exceptional 3SG form in present tense. */
  private static RegexRule[] PRESENT_TENSE_RULES = new RegexRule[] {
      new RegexRule("^aby$", 0, "es", 0),
      new RegexRule("^bog-down$", 5, "s-down", 0),
      new RegexRule("^chivy$", 1, "vies", 0),
      new RegexRule("^gen-up$", 3, "s-up", 0),
      new RegexRule("^prologue$", 3, "gs", 0),
      new RegexRule("^picknic$", 0, "ks", 0),
      new RegexRule("^ko$", 0, "'s", 0), new RegexRule("[osz]$", 0, "es", 1),
      new RegexRule("^have$", 2, "s", 0),
      new RegexRule(CONS + "y$", 1, "ies", 1), new RegexRule("^be$", 2, "is"),
      new RegexRule("([zsx]|ch|sh)$", 0, "es", 1) };
  /*
	*//** The IN g_ form. */
  /*
   * private static final InflectionRule ING_FORM = new InflectionRule(
   * "presentParticiple", true, VerbInflection.DEFAULT_ING_RULE,
   * VerbInflection.ING_FORM_RULES);
   *//** The PAS t_ participle. */
  /*
   * private static final InflectionRule PAST_PARTICIPLE = new InflectionRule(
   * "pastParticiple", true, VerbInflection.DEFAULT_PP_RULE,
   * VerbInflection.PAST_PARTICIPLE_RULES);
   *//** The PAS t_ tense. */
  /*
   * private static final InflectionRule PAST_TENSE = new InflectionRule(
   * "pastTense", true, VerbInflection.DEFAULT_PAST_RULE,
   * VerbInflection.PAST_TENSE_RULES);
   *//** The PRESEN t_ tense. */
  /*
   * private static final InflectionRule PRESENT_TENSE = new InflectionRule(
   * "presentTense", false, VerbInflection.DEFAULT_PRESENT_TENSE,
   * VerbInflection.PRESENT_TENSE_RULES);
   */

  static List AUXILIARIES = Arrays.asList(new String[] { "do", "have", "be" });

  static List VERB_CONS_DOUBLING = Arrays.asList(new String[] { "abat", "abet",
      "abhor", "abut", "accur", "acquit", "adlib", "admit", "aerobat",
      "aerosol", "agendaset", "allot", "alot", "anagram", "annul", "appal",
      "apparel", "armbar", "aver", "babysit", "airdrop", "appal", "blackleg",
      "bobsled", "bur", "chum", "confab", "counterplot", "curet", "dib",
      "backdrop", "backfil", "backflip", "backlog", "backpedal", "backslap",
      "backstab", "bag", "balfun", "ballot", "ban", "bar", "barbel", "bareleg",
      "barrel", "bat", "bayonet", "becom", "bed", "bedevil", "bedwet",
      "beenhop", "befit", "befog", "beg", "beget", "begin", "bejewel",
      "bemedal", "benefit", "benum", "beset", "besot", "bestir", "bet",
      "betassel", "bevel", "bewig", "bib", "bid", "billet", "bin", "bip",
      "bit", "bitmap", "blab", "blag", "blam", "blan", "blat", "bles", "blim",
      "blip", "blob", "bloodlet", "blot", "blub", "blur", "bob", "bodypop",
      "bog", "booby-trap", "boobytrap", "booksel", "bootleg", "bop", "bot",
      "bowel", "bracket", "brag", "brig", "brim", "bud", "buffet", "bug",
      "bullshit", "bum", "bun", "bus", "but", "cab", "cabal", "cam", "can",
      "cancel", "cap", "caracol", "caravan", "carburet", "carnap", "carol",
      "carpetbag", "castanet", "cat", "catcal", "catnap", "cavil", "chan",
      "chanel", "channel", "chap", "char", "chargecap", "chat", "chin", "chip",
      "chir", "chirrup", "chisel", "chop", "chug", "chur", "clam", "clap",
      "clearcut", "clip", "clodhop", "clog", "clop", "closet", "clot", "club",
      "co-occur", "co-program", "co-refer", "co-run", "co-star", "cob",
      "cobweb", "cod", "coif", "com", "combat", "comit", "commit", "compel",
      "con", "concur", "confer", "confiscat", "control", "cop", "coquet",
      "coral", "corbel", "corral", "cosset", "cotransmit", "councel",
      "council", "counsel", "court-martial", "crab", "cram", "crap", "crib",
      "crop", "crossleg", "cub", "cudgel", "cum", "cun", "cup", "cut", "dab",
      "dag", "dam", "dan", "dap", "daysit", "de-control", "de-gazet", "de-hul",
      "de-instal", "de-mob", "de-program", "de-rig", "de-skil", "deadpan",
      "debag", "debar", "debug", "decommit", "decontrol", "defer", "defog",
      "deg", "degas", "deinstal", "demit", "demob", "demur", "den", "denet",
      "depig", "depip", "depit", "der", "deskil", "deter", "devil", "diagram",
      "dial", "dig", "dim", "din", "dip", "disbar", "disbud", "discomfit",
      "disembed", "disembowel", "dishevel", "disinter", "dispel", "disprefer",
      "distil", "dog", "dognap", "don", "doorstep", "dot", "dowel", "drag",
      "drat", "driftnet", "distil", "egotrip", "enrol", "enthral", "extol",
      "fulfil", "gaffe", "golliwog", "idyl", "inspan", "drip", "drivel",
      "drop", "drub", "drug", "drum", "dub", "duel", "dun", "dybbuk", "earwig",
      "eavesdrop", "ecolabel", "eitherspigot", "electroblot", "embed", "emit",
      "empanel", "enamel", "endlabel", "endtrim", "enrol", "enthral",
      "entrammel", "entrap", "enwrap", "equal", "equip", "estop", "exaggerat",
      "excel", "expel", "extol", "fag", "fan", "farewel", "fat", "featherbed",
      "feget", "fet", "fib", "fig", "fin", "fingerspel", "fingertip", "fit",
      "flab", "flag", "flap", "flip", "flit", "flog", "flop", "fob", "focus",
      "fog", "footbal", "footslog", "fop", "forbid", "forget", "format",
      "fortunetel", "fot", "foxtrot", "frag", "freefal", "fret", "frig",
      "frip", "frog", "frug", "fuel", "fufil", "fulfil", "fullyfit", "fun",
      "funnel", "fur", "furpul", "gab", "gad", "gag", "gam", "gambol", "gap",
      "garot", "garrot", "gas", "gat", "gel", "gen", "get", "giftwrap", "gig",
      "gimbal", "gin", "glam", "glenden", "glendin", "globetrot", "glug",
      "glut", "gob", "goldpan", "goostep", "gossip", "grab", "gravel", "grid",
      "grin", "grip", "grit", "groundhop", "grovel", "grub", "gum", "gun",
      "gunrun", "gut", "gyp", "haircut", "ham", "han", "handbag", "handicap",
      "handknit", "handset", "hap", "hareleg", "hat", "headbut", "hedgehop",
      "hem", "hen", "hiccup", "highwal", "hip", "hit", "hobnob", "hog", "hop",
      "horsewhip", "hostel", "hot", "hotdog", "hovel", "hug", "hum", "humbug",
      "hup", "hushkit", "hut", "illfit", "imbed", "immunblot", "immunoblot",
      "impannel", "impel", "imperil", "incur", "infer", "infil", "inflam",
      "initial", "input", "inset", "instil", "inter", "interbed", "intercrop",
      "intercut", "interfer", "instal", "instil", "intermit", "japan", "jug",
      "kris", "manumit", "mishit", "mousse", "mud", "interwar", "jab", "jag",
      "jam", "jar", "jawdrop", "jet", "jetlag", "jewel", "jib", "jig",
      "jitterbug", "job", "jog", "jog-trot", "jot", "jut", "ken", "kennel",
      "kid", "kidnap", "kip", "kissogram", "kit", "knap", "kneecap", "knit",
      "knob", "knot", "kor", "label", "lag", "lam", "lap", "lavel", "leafcut",
      "leapfrog", "leg", "lem", "lep", "let", "level", "libel", "lid", "lig",
      "lip", "lob", "log", "lok", "lollop", "longleg", "lop", "lowbal", "lug",
      "mackerel", "mahom", "man", "map", "mar", "marshal", "marvel", "mat",
      "matchwin", "metal", "micro-program", "microplan", "microprogram",
      "milksop", "mis-cal", "mis-club", "mis-spel", "miscal", "mishit",
      "mislabel", "mit", "mob", "mod", "model", "mohmam", "monogram", "mop",
      "mothbal", "mug", "multilevel", "mum", "nab", "nag", "nan", "nap", "net",
      "nightclub", "nightsit", "nip", "nod", "nonplus", "norkop", "nostril",
      "not", "nut", "nutmeg", "occur", "ocur", "offput", "offset", "omit",
      "ommit", "onlap", "out-general", "out-gun", "out-jab", "out-plan",
      "out-pol", "out-pul", "out-put", "out-run", "out-sel", "outbid",
      "outcrop", "outfit", "outgas", "outgun", "outhit", "outjab", "outpol",
      "output", "outrun", "outship", "outshop", "outsin", "outstrip",
      "outswel", "outspan", "overcrop", "pettifog", "photostat", "pouf",
      "preset", "prim", "pug", "ret", "rosin", "outwit", "over-commit",
      "over-control", "over-fil", "over-fit", "over-lap", "over-model",
      "over-pedal", "over-pet", "over-run", "over-sel", "over-step",
      "over-tip", "over-top", "overbid", "overcal", "overcommit",
      "overcontrol", "overcrap", "overdub", "overfil", "overhat", "overhit",
      "overlap", "overman", "overplot", "overrun", "overshop", "overstep",
      "overtip", "overtop", "overwet", "overwil", "pad", "paintbal", "pan",
      "panel", "paperclip", "par", "parallel", "parcel", "partiescal", "pat",
      "patrol", "pedal", "peewit", "peg", "pen", "pencil", "pep", "permit",
      "pet", "petal", "photoset", "phototypeset", "phut", "picket", "pig",
      "pilot", "pin", "pinbal", "pip", "pipefit", "pipet", "pit", "plan",
      "plit", "plod", "plop", "plot", "plug", "plumet", "plummet", "pod",
      "policyset", "polyfil", "ponytrek", "pop", "pot", "pram", "prebag",
      "predistil", "predril", "prefer", "prefil", "preinstal", "prep",
      "preplan", "preprogram", "prizewin", "prod", "profer", "prog", "program",
      "prop", "propel", "pub", "pummel", "pun", "pup", "pushfit", "put",
      "quarel", "quarrel", "quickskim", "quickstep", "quickwit", "quip",
      "quit", "quivertip", "quiz", "rabbit", "rabit", "radiolabel", "rag",
      "ram", "ramrod", "rap", "rat", "ratecap", "ravel", "re-admit", "re-cal",
      "re-cap", "re-channel", "re-dig", "re-dril", "re-emit", "re-fil",
      "re-fit", "re-flag", "re-format", "re-fret", "re-hab", "re-instal",
      "re-inter", "re-lap", "re-let", "re-map", "re-metal", "re-model",
      "re-pastel", "re-plan", "re-plot", "re-plug", "re-pot", "re-program",
      "re-refer", "re-rig", "re-rol", "re-run", "re-sel", "re-set", "re-skin",
      "re-stal", "re-submit", "re-tel", "re-top", "re-transmit", "re-trim",
      "re-wrap", "readmit", "reallot", "rebel", "rebid", "rebin", "rebut",
      "recap", "rechannel", "recommit", "recrop", "recur", "recut", "red",
      "redril", "refer", "refit", "reformat", "refret", "refuel", "reget",
      "regret", "reinter", "rejig", "rekit", "reknot", "relabel", "relet",
      "rem", "remap", "remetal", "remit", "remodel", "reoccur", "rep", "repel",
      "repin", "replan", "replot", "repol", "repot", "reprogram", "rerun",
      "reset", "resignal", "resit", "reskil", "resubmit", "retransfer",
      "retransmit", "retro-fit", "retrofit", "rev", "revel", "revet", "rewrap",
      "rib", "richochet", "ricochet", "rid", "rig", "rim", "ringlet", "rip",
      "rit", "rival", "rivet", "roadrun", "rob", "rocket", "rod", "roset",
      "rot", "rowel", "rub", "run", "runnel", "rut", "sab", "sad", "sag",
      "sandbag", "sap", "scab", "scalpel", "scam", "scan", "scar", "scat",
      "schlep", "scrag", "scram", "shall", "sled", "smut", "stet", "sulfuret",
      "trepan", "unrip", "unstop", "whir", "whop", "wig", "scrap", "scrat",
      "scrub", "scrum", "scud", "scum", "scur", "semi-control", "semi-skil",
      "semi-skim", "semiskil", "sentinel", "set", "shag", "sham", "shed",
      "shim", "shin", "ship", "shir", "shit", "shlap", "shop", "shopfit",
      "shortfal", "shot", "shovel", "shred", "shrinkwrap", "shrivel", "shrug",
      "shun", "shut", "side-step", "sideslip", "sidestep", "signal", "sin",
      "sinbin", "sip", "sit", "skid", "skim", "skin", "skip", "skir", "skrag",
      "slab", "slag", "slam", "slap", "slim", "slip", "slit", "slob", "slog",
      "slop", "slot", "slowclap", "slug", "slum", "slur", "smit", "snag",
      "snap", "snip", "snivel", "snog", "snorkel", "snowcem", "snub", "snug",
      "sob", "sod", "softpedal", "son", "sop", "spam", "span", "spar", "spat",
      "spiderweb", "spin", "spiral", "spit", "splat", "split", "spot", "sprag",
      "spraygun", "sprig", "springtip", "spud", "spur", "squat", "squirrel",
      "stab", "stag", "star", "stem", "sten", "stencil", "step", "stir",
      "stop", "storytel", "strap", "strim", "strip", "strop", "strug", "strum",
      "strut", "stub", "stud", "stun", "sub", "subcrop", "sublet", "submit",
      "subset", "suedetrim", "sum", "summit", "sun", "suntan", "sup",
      "super-chil", "superad", "swab", "swag", "swan", "swap", "swat", "swig",
      "swim", "swivel", "swot", "tab", "tag", "tan", "tansfer", "tap", "tar",
      "tassel", "tat", "tefer", "teleshop", "tendril", "terschel", "th'strip",
      "thermal", "thermostat", "thin", "throb", "thrum", "thud", "thug",
      "tightlip", "tin", "tinsel", "tip", "tittup", "toecap", "tog", "tom",
      "tomorrow", "top", "tot", "total", "towel", "traget", "trainspot",
      "tram", "trammel", "transfer", "tranship", "transit", "transmit",
      "transship", "trap", "travel", "trek", "trendset", "trim", "trip",
      "tripod", "trod", "trog", "trot", "trousseaushop", "trowel", "trup",
      "tub", "tug", "tunnel", "tup", "tut", "twat", "twig", "twin", "twit",
      "typeset", "tyset", "un-man", "unban", "unbar", "unbob", "uncap",
      "unclip", "uncompel", "undam", "under-bil", "under-cut", "under-fit",
      "under-pin", "under-skil", "underbid", "undercut", "underlet",
      "underman", "underpin", "unfit", "unfulfil", "unknot", "unlip",
      "unlywil", "unman", "unpad", "unpeg", "unpin", "unplug", "unravel",
      "unrol", "unscrol", "unsnap", "unstal", "unstep", "unstir", "untap",
      "unwrap", "unzip", "up", "upset", "upskil", "upwel", "ven", "verbal",
      "vet", "victual", "vignet", "wad", "wag", "wainscot", "wan", "war",
      "water-log", "waterfal", "waterfil", "waterlog", "weasel", "web", "wed",
      "wet", "wham", "whet", "whip", "whir", "whiteskin", "whiz", "whup",
      "wildcat", "win", "windmil", "wit", "woodchop", "woodcut", "wor",
      "worship", "wrap", "wiretap", "yen", "yak", "yap", "yarnspin", "yip",
      "yodel", "zag", "zap", "zig", "zig-zag", "zigzag", "zip", "ztrip",
      "hand-bag", "hocus", "hocus-pocus" });

  static List MODALS = Arrays.asList(new String[] { "shall", "would", "may",
      "might", "ought", "should" });

  static List SYMBOLS = Arrays.asList(new String[] { "!", "?", "$", "%", "*",
      "+", "-", "=" });

  static Conjugator conjugator;

  /*
   * Sets the form for this conjugation. REPLACE!
   * 
   * public void setForm(int theForm) { form = theForm; // can't have PAST or
   * FUTURE with gerunds or infinitives if ((form == GERUND) || (form ==
   * INFINITIVE) || (form == BARE_INFINITIVE)) tense = PRESENT_TENSE; }
   */

  public Conjugator handleArgs(Map args) {
    if (args == null || args.size() < 1)
      return this;

    // ------------------ handle arguments ------------------

    Object anumber = args.get("number");
    this.number = (anumber != null) ? (Integer) (anumber) : SINGULAR;

    Object aperson = args.get("person");
    this.person = (aperson != null) ? (Integer) (aperson) : FIRST_PERSON;

    Object atense = args.get("tense");
    this.tense = (atense != null) ? (Integer) (atense) : PRESENT_TENSE;

    Object aform = args.get("form");
    this.form = (aform != null) ? (Integer) (aform) : NORMAL;

    Object apassive = args.get("passive");
    this.passive = (apassive != null) ? (Boolean) (apassive) : false;

    Object aprogressive = args.get("progressive");
    this.progressive = (aprogressive != null) ? (Boolean) (aprogressive)
	: false;

    Object ainterrogative = args.get("interrogative");
    this.interrogative = (ainterrogative != null) ? (Boolean) (ainterrogative)
	: false;

    Object aperfect = args.get("perfect");
    this.perfect = (aperfect != null) ? (Boolean) (aperfect) : false;

    return this;
  }

  /**
   * Conjugates the verb based on the current state of the conjugator
   * 
   * @see #setNumber(int)
   * @see #setPerson(int)
   * @see #setTense(int)
   * @see #setPassive(boolean)
   * @see #setProgressive(boolean)
   * @see #setPerfect(boolean)
   */
  public String conjugate(String theVerb) {

    if (theVerb == null || theVerb.length() < 1)
      throw new RiTaException("Make sure to set"
	  + " the head verb before calling conjugate()");

    Stack conjStack = new Stack();

    // VP will be realised as perfective if the verb is modal and past
    boolean modalPast = false;

    // compute modal -- this affects tense
    String actualModal = null;

    if (form == INFINITIVE) {
      actualModal = "to";
    } 
    else if (allowsTense) // ( form.allowsTense() ) {
    {
      if (tense == FUTURE_TENSE && modal == null) {
	actualModal = "will";
      } else if (modal != null) {
	actualModal = modal;
	if (tense == PAST_TENSE)
	  modalPast = true;
      }
    }

    // start off with main verb
    String frontVG = checkVerb(theVerb);

    // passive
    if (passive) {

      String pp = getPastParticiple(frontVG);
      conjStack.push(pp);
      frontVG = "be"; // conjugate
    }

    // progressive
    if (progressive) {

      conjStack.push(getPresentParticiple(frontVG));
      frontVG = "be"; // conjugate
    }

    // perfect
    if (perfect || modalPast) {

      conjStack.push(getPastParticiple(frontVG));
      frontVG = "have";
    }

    if (actualModal != null) {

      conjStack.push(getBaseForm(frontVG));
      frontVG = null;
    }

    // now inflect frontVG (if it exists) and push it on restVG
    if (frontVG != null) {

      if (form == GERUND) {// gerund - use ING form
	conjStack.push(getPresentParticiple(frontVG));
      }
      // / when could this happen, examples???
      else if (!allowsTense
	  || (interrogative && !frontVG.equals("be") && conjStack.isEmpty())) {
	conjStack.push(getBaseForm(frontVG));
      } else {
	String verbForm = getVerbForm(getBaseForm(frontVG), tense, person,
	    number);
	conjStack.push(verbForm);
      }
    }

    // add modal, and we're done
    if (actualModal != null)
      conjStack.push(actualModal);

    String s = "";
    for (Iterator it = conjStack.iterator(); it.hasNext();)
      s = it.next() + " " + s;

    return s.trim();
  }

  private String getVerbForm(String theVerb, int theTense, int thePerson,
      int theNumber) {
    switch (theTense) {
    case PRESENT_TENSE:
      return getPresent(theVerb, thePerson, theNumber);
    case PAST_TENSE:
      return getPast(theVerb, thePerson, theNumber);
    default:
      return getBaseForm(theVerb);
    }
  }

  public String getPast(String theVerb, int pers, int numb) {
    if (theVerb.equalsIgnoreCase("be")) {
      switch (numb) {
      case SINGULAR:
	switch (pers) {

	case THIRD_PERSON:
	  return render("was");
	case SECOND_PERSON:
	  return render("were");
	
	}
	  break;
      case PLURAL:
	return render("were");
      }
    }
    return getPast(theVerb);
  }

  // --------------------------------

  public String getPast(String v) {
    return apply(getRule(PAST_TENSE_RULE), v);
  }

  private String getBaseForm(String v) {
    return (particle != null) ? v + " " + particle : v;
  }

  public String getPresentParticiple(String v) {
    return apply(getRule(PRESENT_PARTICIPLE_RULE), v);
  }

  public String getPastParticiple(String v) {
    return apply(getRule(PAST_PARTICIPLE_RULE), v);
  }

  private String checkVerb(String theVerb) {
    theVerb = theVerb.toLowerCase();
    if (theVerb.equalsIgnoreCase("am") || theVerb.equalsIgnoreCase("are")
	|| theVerb.equalsIgnoreCase("is") || theVerb.equalsIgnoreCase("was")
	|| theVerb.equalsIgnoreCase("were")) {
      theVerb = "be";
    }
    return theVerb;
  }

  public String toString() {
    String s = "\nConjugator:\n";
    s += "  ---------------------\n";
    // s += "Coordinate = "+isCoordinate()+"\n";
    // s += "Negated = "+isNegated()+"\n";
    s += "  Passive = " + isPassive() + "\n";
    s += "  Perfect = " + isPerfect() + "\n";
    s += "  Progressive = " + isProgressive() + "\n";
    s += "  ---------------------\n";
    // s += "Category = "+getCategory()+"\n";
    // s += "Complements = "+getComplements()+"\n";
    // s += "Form = "+getForm()+"\n";
    s += "  Number = " + getNumber() + "\n";
    s += "  Person = " + getPerson() + "\n";
    s += "  Tense = " + getTense() + "\n";
    s += "  ---------------------\n";
    return s;
  }

  /**
   * Sets the number for the conjugation, from one of the constants: [SINGULAR,
   * PLURAL]
   */
  public void setNumber(int numberConstant) {
    this.number = numberConstant;
  }

  /**
   * Sets the person for the conjugation, from one of the constants:
   * [FIRST_PERSON, SECOND_PERSON, THIRD_PERSON]
   */
  public void setPerson(int personConstant) {
    this.person = personConstant;
  }

  /**
   * Sets the tense for the conjugation, from one of the constants: [PAST_TENSE,
   * PRESENT_TENSE, FUTURE_TENSE]
   */
  public void setTense(int tenseConstant) {
    this.tense = tenseConstant;
  }

  private CONST singular = new CONST("singular", SINGULAR);
  private CONST plural = new CONST("plural", PLURAL);
  private CONST[] numbers = { singular, plural };

  private CONST firstPerson = new CONST("1st", FIRST_PERSON);
  private CONST secondPerson = new CONST("2nd", SECOND_PERSON);
  private CONST thirdPerson = new CONST("3rd", THIRD_PERSON);
  private CONST[] persons = { firstPerson, secondPerson, thirdPerson };

  private CONST pastTense = new CONST("past", PAST_TENSE);
  private CONST presentTense = new CONST("present", PRESENT_TENSE);
  private CONST futureTense = new CONST("future", FUTURE_TENSE);
  private CONST[] tenses = { pastTense, presentTense, futureTense };

  /**
   * Returns a String representing the current tense from one of (past, present,
   * future)
   */
  public String getTense() {
    for (int i = 0; i < tenses.length; i++)
      if (tenses[i].equals(tense))
	return tenses[i].name;
    throw new RiTaException("Invalid state: tense=" + tense);
  }

  /**
   * Returns a String representing the current person from one of (first,
   * second, third)
   */
  public String getPerson() {
    for (int i = 0; i < persons.length; i++)
      if (persons[i].equals(person))
	return persons[i].name;
    throw new RiTaException("Invalid state: person=" + person);
  }

  /**
   * Returns a String representing the current number from one of (singular,
   * plural)
   */
  public String getNumber() {
    for (int i = 0; i < numbers.length; i++)
      if (numbers[i].equals(number)) {
	return numbers[i].name;
      }
    throw new RiTaException("Invalid state: number=" + number);
  }

  /**
   * Sets the number from one of (singular, plural)
   */
  public void setNumber(String numberString) {
    for (int i = 0; i < numbers.length; i++)
      if (numbers[i].equals(numberString)) {
	setNumber(numbers[i].val);
	return;
      }
    throw new RiTaException("Unexpected value for number: " + numberString
	+ ", must be one of " + Arrays.asList(numbers));
  }

  /**
   * Sets the number from one of (first, second, third)
   */
  public void setPerson(String personString) {
    for (int i = 0; i < persons.length; i++)
      if (persons[i].equals(personString)) {
	setPerson(persons[i].val);
	return;
      }
    throw new RiTaException("Unexpected value for person: " + personString
	+ ", must be one of " + Arrays.asList(persons));
  }

  /**
   * Sets the number from one of (past, present, future)
   */
  public void setTense(String tenseString) {
    for (int i = 0; i < tenses.length; i++)
      if (tenses[i].equals(tenseString)) {
	setTense(tenses[i].val);
	return;
      }
    throw new RiTaException("Unexpected value for tense: " + tenseString
	+ ", must be one of " + Arrays.asList(tenses));
  }

  class CONST {
    int val;
    String name;

    public CONST(String name, int val) {
      this.name = name;
      this.val = val;
    }

    public boolean equals(String s) {
      return name.equals(s);
    }

    public boolean equals(int i) {
      return val == i;
    }

    public String toString() {
      return name;
    }
  }// end

  /**
   * Sets whether the conjugation should use passive tense
   */
  public void setPassive(boolean passive) {
    this.passive = passive;
  }

  /**
   * Sets whether the conjugation should use perfect tense
   */
  public void setPerfect(boolean perfect) {
    this.perfect = perfect;
  }

  /**
   * Sets whether the conjugation should use progressive tense
   */
  public void setProgressive(boolean progressive) {
    this.progressive = progressive;
  }

  /**
   * A convenience method to set number, person, & tense in one call
   */
  public void setState(String number, String person, String tense) {
    setNumber(number);
    setPerson(person);
    setTense(tense);
  }

  /**
   * A convenience method to set number, person, & tense in one call
   */
  public void setState(int numberConstant, int personConstant, int tenseConstant) {
    setNumber(numberConstant);
    setPerson(personConstant);
    setTense(tenseConstant);
  }

  /**
   * A convenience method to set number, person, & tense, then conjugate, all in
   * one call
   */
  public String conjugate(String number, String person, String tense, String verb) {
    // System.out.println("RiConjugator.conjugate("+number+","+person+","+tense+","+verb+")");
    setState(number, person, tense);
    return conjugate(verb);
  }

  /**
   * A convenience method to set number, person, & tense, then conjugate, all in
   * one call
   */
  public String conjugate(int numberConstant, int personConstant,
      int tenseConstant, String verb) {
    setState(numberConstant, personConstant, tenseConstant);
    return conjugate(verb);
  }

  /**
   * Returns whether the conjugation will use passive tense
   */
  public boolean isPassive() {
    return passive;
  }

  /**
   * Returns whether the conjugation will use perfect tense
   */
  public boolean isPerfect() {
    return perfect;
  }

  /**
   * Returns whether the conjugation will use progressive tense
   */
  public boolean isProgressive() {
    return progressive;
  }

  /**
   * Returns all unique possible conjugations of the specified verb
   * 
   * public void conjugateAll(String theVerb, Set result) { String[] tmp =
   * conjugateAll(theVerb); for (int i = 0; i < tmp.length; i++) {
   * result.add(tmp[i]); } }
   */

  /**
   * Returns all possible conjugations of the specified verb (may contain
   * duplicates)
   * 
   * public String[] conjugateAll(String theVerb) {
   * 
   * List tmp = new ArrayList(); this.setVerb(theVerb); for (int i = 0; i <
   * tenses.length; i++) { setTense(tenses[i].val); for (int j = 0; j <
   * numbers.length; j++) { setNumber(numbers[j].val); for (int k = 0; k <
   * persons.length; k++) { setPerson(persons[k].val); for (int l = 0; l < 2;
   * l++) { setPassive(l==0 ? true : false); for (int m = 0; m < 2; m++) {
   * setProgressive(m==0 ? true : false); for (int n = 0; n < 2; n++) {
   * setPerfect(n==0 ? true : false); tmp.add(conjugate()); if (DBUG)
   * System.out.println(this); } } } } } } return RiTa.strArr(tmp); }
   */

  public static void main(String[] args) {
    String[] s = { "swim", "need", "open" };
    Conjugator rc = new Conjugator();

    rc.setNumber("singular");
    rc.setPerson("3rd");
    rc.setTense("present");

    String[] a = { "swims", "needs", "opens" };

    for (int i = 0; i < s.length; i++) {
      String c = rc.conjugate(s[i]);
      // System.out.println(c+" -> "+(c.equals(a[i])?"PASS":"FAIL"));
    }

    // rc = new RiConjugator();
    rc.setTense("past");
    rc.setNumber("singular");
    rc.setPerson("3rd");
    rc.setPassive(true);

    String[] a2 = { "is swum", "is needed", "is opened" };

    for (int i = 0; i < s.length; i++) {
      String c = rc.conjugate(s[i]);
      System.out.println(c + " -> " + (c.equals(a2[i]) ? "PASS" : "FAIL"));
    }

    if (1 == 1)
      return;

    // rc.setPassive(true);
    // rc.setPerfect(true);
    // rc.setProgressive(true);
    /*
     * String c = rc.conjugate(s); System.out.println(c);
     */

    String[] ss = { "swim", "need", "open" };
    String[] aa = { "will swim", "will need", "will open" };

    rc.setTense("future");

    for (int i = 0; i < ss.length; i++) {
      String c = rc.conjugate(ss[i]);
      // System.out.println(i+")got: "+c);
      // System.out.println(c.equals(aa[i])?"OK":"FAIL");
    }
    // System.out.println(rc.getPastParticiple("swim"));
    // ystem.out.println(rc.getPastParticiple("run"));
    // System.out.println(Arrays.asList(c));
  }

}// end