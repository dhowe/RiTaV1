package rita;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import rita.json.JSONException;
import rita.json.JSONObject;
import rita.support.*;

/*
 * TODO: Re-add RiGrammarEditor
 */
public class RiGrammar implements Constants
{
  public static final String ENCODING = "UTF-8", START = "<start>";
  public static final String PROB_PATTERN = "(.*[^ ]) *\\[([^]]+)\\](.*)";
  public static final String EXEC_CHAR  = "`", EXEC_POST = ");" + EXEC_CHAR;
  
  public RuleList rules;
  public int maxIterations = 100;
  public String fileName, startRule = START;
  public Pattern probabilityPattern;

  public RiGrammar()
  {
    this(null);
  }
  
  public RiGrammar(String grammarAsString) 
  { 
    this.rules = new RuleList();
    setGrammar(grammarAsString);
  }
  

  public RiGrammar setGrammarFromFile(String grammarFileName)
  {
    if (grammarFileName != null)
    {
      this.fileName = grammarFileName;
      String gram = RiTa.loadString(null, grammarFileName);
      setGrammar(gram, fileName.endsWith(".json"));
    }
    return this;
  }
  
  protected void setGrammarFromProps(MultiMap grammarRules) 
  {
    this.rules.clear();

    for (Iterator iterator = grammarRules.keySet().iterator(); iterator.hasNext();)
    {
      String key = (String) iterator.next();
      key.replaceAll(DQ, E).replaceAll(SQ, E);
      String[] rules = grammarRules.get(key);
      for (int j = 0; j < rules.length; j++)
      {
        addRule(key, rules[j]);
      }
    }
  }

  public RiGrammar addRule(String name, String rule)
  {
    return addRule(name, rule, 1);
  }
  
  public RiGrammar addRule(String key, String rule, float prob)
  {
    String[] parts = rule.split("\\s*\\|\\s*"); 
    for (int i = 0; i < parts.length; i++)
    {
      String part = parts[i];
      float weight = prob;
      if (part != null && part.trim().length() > 0)
      {
        if (probabilityPattern == null)
        {
          probabilityPattern = Pattern.compile(PROB_PATTERN);
        }
        Matcher m = probabilityPattern.matcher(part);
        if (m.matches())
        {
          if (m.groupCount() == 3)
          {
            String probStr = m.group(2);
            // nothing after the weight is allowed
            part = m.group(1) + m.group(3);
            String ignored = m.group(3);
            weight = Float.parseFloat(probStr);
          }
          else
          {
            System.err.println("[WARN] Invalid rule: " + part + " -> " + m.groupCount());
          }
        }
        rules.addRule(key, part, weight);
      }
    }
    
    return this;
  }
 
  public String expand()
  {
    return expandFrom(START);
  }
  
  public String expandFrom(String rule)
  {
    if (!rules.hasRule(rule))
      throw new RiTaException("Definition not found: "+rule+"\nRules:\n"+rules);
    
    int iterations = 0;
    while (++iterations < maxIterations)
    {
      String next = expandRule(rule);
      if (next == null || next.length()==0) 
        break;
      rule = next;
    }
    
    if (iterations >= maxIterations) 
      System.out.println("[WARN] max number of iterations reached: "+maxIterations);

    return rule;  
  }

  // TODO: 
  public String expandWith(String literalString, String ruleName)
  {
    // make sure the rule exists in the grammar
    if (!hasRule(ruleName))
      throw new RiTaException(ruleName+" not found in current rules: "+rules);
    
    return "Not yet implemented...";
  }

  public String getRule(String name)
  {
    return rules.getRule(name);
  }

  public boolean hasRule(String name)
  {
    return rules.hasRule(name);
  }
  
  public RiGrammar setGrammar(String grammarRulesAsString)
  {
    return setGrammar(grammarRulesAsString, false);
  }
  
  private RiGrammar setGrammar(String grammarRulesAsString, boolean forceJSON)
  {
    if (grammarRulesAsString != null)
    {
      String s = parseAsJSON(grammarRulesAsString);
      
      if (s == null && !forceJSON) {
        
        s = grammarRulesAsString;
      }

      if (s != null)
      {
        MultiMap mm = new MultiMap();
        mm.loadFromString(s);
        setGrammarFromProps(mm);
      }

      if (this.rules == null || this.rules.keySet().size() < 1)
        throw new RiTaException("Unable to parse valid grammar rules!");
    }

    return this;
  }

  protected String parseAsJSON(String grammarRulesAsString)
  {
    try
    {
      JSONObject json = new JSONObject(grammarRulesAsString);
      return toProperties(json);
    }
    catch (JSONException e)
    {
      System.out.println("[WARN] Grammar is not valid JSON");
      return null;  
    }
  }

  protected String toProperties(JSONObject json) throws JSONException
  {
    StringBuilder sb = new StringBuilder();
    Iterator keys = json.keys();
    while (keys.hasNext())
    {
      String key = (String) keys.next();
      String rule = json.getString(key);
      sb.append(key+'='+rule+'\n');
    }
    //System.out.println("props:\n"+sb+"\n\n");
    return sb.toString();
  }

  public RiGrammar removeRule(String s)
  {
    rules.removeRule(s);
    return this;
  }

  public RiGrammar print()
  {
    return print(System.out);
  }
  
  public RiGrammar print(PrintStream ps) // TODO: compare output to RiTaJS, make sure they're same
  {
    ps.println(rules.toString());
    return this;
  }

  public RiGrammar reset()
  {
    rules.clear();
    return this;
  }
  
  /* returns null if no expansion can be found */
  String expandRule(String production)
  {
    for (Iterator it = rules.iterator(); it.hasNext();)
    {
      String name = (String) it.next();

      int idx = production.indexOf(name);
      if (idx >= 0)  
      {
        String pre = production.substring(0, idx);
        String expanded = getRule(name);
        String post = production.substring(idx+name.length());
        return pre + expanded + post;
      }
    }
    return E;
  }
  
  public static void main(String[] args)
  {
    RiGrammar rg = new RiGrammar();
    rg.setGrammarFromFile("grammar.g");
    for (int i = 0; i < 5; i++)
    {
      System.out.println(i+") "+rg.expand());      
    }
    
  }
}
