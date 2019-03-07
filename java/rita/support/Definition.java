package rita.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import rita.RiTa;
import rita.RiTaException;

public class Definition
{
  String symbol;
  List rules; // List of String-Lists

  Definition() {
    this.rules = new ArrayList();
  }

  public Definition(RuleParser scanner) {
    this();
    
    String ruleName = scanner.getNextToken();
    while (LegacyGrammar.isLineBreak(ruleName))
      ruleName = scanner.getNextToken();
    
    //System.out.println("TOK: "+ruleName);
    
    // store the name
    this.symbol = ruleName;
    
    // read each rule until end-def -> '}'
    while (readRule(scanner)) {  /* nothing */  }
  }

  public Definition(String symbol, List prods) {
    this();
    this.symbol = symbol;
    this.rules = prods;
  }

  /**
   * Finds a rule by checking (in random order) each rule
   * for a token containing <code>searchToken</code>
   * 
   * OLD: for a token exactly matching <code>searchToken</code>
   */
  List findRuleContaining(String searchToken)
  {
    Collections.shuffle(rules);
    for (Iterator i = rules.iterator(); i.hasNext();)
    {
      List l = (List) i.next();
      // System.out.println(" RULE: "+l);
      for (Iterator j = l.iterator(); j.hasNext();)
      {
        String token = (String) j.next();
        //System.out.println("   TOKEN: "+token);
        if (token.indexOf(searchToken)>-1)
        {
          // System.out.println(" MATCH!: "+l);
          return l;
        }
        // System.out.println(" Miss: "+l);
      }
    }
    return null;
  }

  /**
   * Replaces String 'toReplace' with 'replaceWith' in
   * all production that contain it
   */
  private void replaceToken(String toReplace, String replaceWith)
  {
    for (int i = 0; i < rules.size(); i++)
    {
      List production = (List) rules.get(i);
      for (int j = 0; j < production.size(); j++)
      {
        String token = (String) production.get(j);
        if (token.equals(toReplace))
          production.set(j, replaceWith);
      }
    }
  }// end

  
  /**
   * Executes all replaces in the list of productions and deletes all those that
   * do not match
   * 
   * NOTE: a destructive operation, generally run only on copies...
   */
  void replaceAndDelete(String toReplace, String replaceWith)
  {
    // delete all productions not containing toReplace
    for (Iterator i = rules.iterator(); i.hasNext();)
    {
      boolean matched = false;
      List ruleBody = (List) i.next();
      for (int j = 0; j < ruleBody.size(); j++)
      {
        String token = (String) ruleBody.get(j);
        if (token.equals(toReplace)) {
          matched = true;
          break;
        }
      }
      if (!matched) i.remove();
    }

    // now do the replaces for those that match
    replaceToken(toReplace, replaceWith);
  }

  public Definition copy()
  {
    Definition d = new Definition();
    d.symbol = this.symbol;
    d.rules = new ArrayList();
    for (Iterator i = rules.iterator(); i.hasNext();)
    {
      List prod = (List) i.next();
      List cprod = new ArrayList();
      int k = 0;
      for (Iterator j = prod.iterator(); j.hasNext(); k++)
      {
        String tok = (String) j.next();
        cprod.add(tok);
      }
      d.rules.add(cprod);
    }
    return d;
  }

  public String toString()
  {
    String s = symbol + " -> ";
    for (Iterator i = rules.iterator(); i.hasNext();)
      s += i.next();
    return s;// +">";
  }

  private boolean readRule(RuleParser rp)
  {
    List l = new ArrayList();
    boolean comment = false;
    int multiplier = 1;   

    while (true)
    {
      String token = rp.getNextToken();
      
      
//System.out.println("LegacyGrammar.token='"+token+"'");
      
      
      if (token == null) 
        throw new RiTaException("Null Token!");

      // check for line end
      if (LegacyGrammar.isLineBreak(token))
      {               
        comment = false; 
        continue;
      }      
      
      // check for comment
      if (comment || isComment(token)) {
        comment = true;
        continue;
      }      

      // check for multiplier
      if (token.startsWith(LegacyGrammar.OPEN_QUANT))
      {
        if (l.size() > 0)
          throw new RiTaException("[ERROR] Misplaced quantifier character: "+
            LegacyGrammar.OPEN_QUANT+"' or '"+LegacyGrammar.CLOSE_QUANT+"; quantifiers must " +
            " come first in rule, found at token:'... " + token + " ...'");
        multiplier = handleMultiplier(token);
        continue;
      }
      
      // handle end of rule:  '|'
      if (token.equals(LegacyGrammar.END_PROD) || token.equals(LegacyGrammar.CLOSE_DEF))
      {                
        if (l.size() > 0) {
          addRule(l, multiplier);
        }
        return token.equals(LegacyGrammar.END_PROD); // only false on end-of-definition
      }
      
      // add token to the current rule
      l.add(token);
    }
  }

  private boolean isComment(String l)
{   
    return (l.startsWith(LegacyGrammar.COMMENT));
  }
  
  /**
   * Removes all rules from this Definition
   */
  public void clearRules()
  {
    this.rules.clear();
  }  
  
  /**
   * Adds a rule (a List of String tokens) <code>multiplier</code> times.
   * Returns true if the rule was successfully added, else false
   */
  public boolean addRule(List l, int multiplier)
  {
    //System.out.println("ADDING RULE: "+ l);
    for (int i = 0; i < multiplier; i++)
      rules.add(l);
    return true;
  }
  
  private int handleMultiplier(String token)
  {
    int result = -1;

    if (!token.endsWith(LegacyGrammar.CLOSE_QUANT))
      throw new RiTaException("[ERROR] Misplaced quantifier character "+
          "('"+LegacyGrammar.OPEN_QUANT+"' or '"+LegacyGrammar.CLOSE_QUANT+"')" +
        " quantifiers must end with '"+ LegacyGrammar.CLOSE_QUANT +
        "' - at token:'... " + token + " ...'");

    try
    {
      token = token.substring(1, token.length() - 1);
      result = Integer.parseInt(token);
    } 
    catch (Exception e) {
      throw new RiTaException("[ERROR] Misplaced quantifier character "+
          "('"+LegacyGrammar.OPEN_QUANT+"' or '"+LegacyGrammar.CLOSE_QUANT+"')" +
          " quantifiers must contain an integer, e.g., '[26]')" +
          "' - at token:'... " + token + " ...'");
    }

    return result;
  }

/*  private static boolean isMultiplier(String token)
  {
    return token.matches("-?[0-9]+");
  }*/

  public String getName()
  {
    return symbol;
  }

  public void expand(LegacyGrammar grammar, StringBuilder StringBuilder)
  {
    if (rules.size() < 1)
      throw new RiTaException
        ("[ERROR] No productions for rule: '"+symbol+ "'");

    int i = (int) (RiTa.random() * rules.size());
    List toExpand = (List) rules.get(i);
//System.out.println("   "+toExpand);
    
    if (LegacyGrammar.DBUG_EXPAND) System.out.println("CHOSE: " + toExpand);

    int j = 0;
    Iterator e = toExpand.iterator();
    while (e.hasNext())
    {
      String prod = (String) e.next();
      if (prod.startsWith(LegacyGrammar.COMMENT)
          || prod.startsWith(LegacyGrammar.OPEN_QUANT))
        continue;
      
//System.out.println(++j+") "+prod);

      // System.err.println("prod="+prod);
      if (prod == null)
        throw new RuntimeException("[ERROR] Null production returned: '"
            + symbol + "'->'" + StringBuilder + "'");

      if (LegacyGrammar.isExecEnabled()) {
//System.out.println("Definition.expand() prod="+prod);
        if (grammar.isExec(prod)) { 
          prod = grammar.handleExec(prod);
          if (prod == null) return;
        }
      }
      
      // OPT: check for token here, else append immediately
      if (prod.indexOf(LegacyGrammar.OPEN_TOKEN) >= 0)
        grammar.expandString(prod, StringBuilder);
      else
        grammar.appendTerminals(prod, StringBuilder);
    }
  }
  

  public List getRules()
  {
    return this.rules;
  }
  
  public void setRules(List l)
  {
    this.rules = l;
  }


  public String ruleToString(int i)
  {
    return RiTa.join((List) (rules.get(i)), " ");
  }  

  public void addRule(String[] ruleTokens)
  {
    addRule(ruleTokens, 1);
  }
  
  public void addRule(String[] ruleTokens, int multiplier)
  {
    addRule(Arrays.asList(ruleTokens), multiplier);
  }
  
  public static void main(String[] args)
  {
    //System.out.println(isMultiplier("-345"));
  }


}// end
