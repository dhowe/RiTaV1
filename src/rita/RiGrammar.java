package rita;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import rita.json.*;
import rita.support.*;

/*
 * TODO: Next:
 *  1) Update documentation for two ways
 *  2) Add variables for all the character in RiGrammar
 *  3) Change weighting punctuation ??
 *  
 *  Better debug feedback when not valid json
 *   
 * TODO: Re-add RiGrammarEditor
 */
public class RiGrammar implements Constants
{
  static { RiTa.init(); }
  
  public static final String PROB_PATTERN = "(.*[^ ]) *\\[([^]]+)\\](.*)";
  
  public static final String ENCODING = "UTF-8", START = "<start>";
  public static final String EXEC_CHAR  = "`", EXEC_POST = ")" + EXEC_CHAR;
  
  public RuleList rules;
  public int maxIterations = 100;
  public String fileName, startRule = START;
  public Pattern probabilityPattern;
  public RiGrammarEditor editor;

  public RiGrammar()
  {
    this(null);
  }
  
  public RiGrammar(String grammarAsString) 
  { 
    this.rules = new RuleList();
    if (grammarAsString != null)
      load(grammarAsString);
  }
  

  public RiGrammarEditor openEditor() {
      return openEditor(800, 600);
  }
  
  /**
   * Provides a live, editable view of a RiGrammar text file
   * that can be dynamically loaded into a sketch without
   * stopping and restarting it. 
   */
  public RiGrammarEditor openEditor(int width, int height) {
    if (editor == null) {
      editor = new RiGrammarEditor(this);
    }
    editor.setSize(width, height);
    editor.setVisible(true);
    return editor;
  }

  public RiGrammar loadFromFile(String grammarFileName)
  {
    if (grammarFileName != null)
    {
      this.fileName = grammarFileName;
      load(RiTa.loadString(grammarFileName));
    }
    
    return this;
  }
  
  // TODO: test and release?
  public RiGrammar loadFromFile(String grammarFileName, String callbackName/*, Object parent*/)
  {
    System.out.println("RiGrammar.loadFromFile(): ignoring callback in Java-mode.");

    return loadFromFile(grammarFileName);
    /*
    if (callbackName != null) {
      if (parent != null)
        RiTa._invoke(parent, RiTa._findCallback(parent, callbackName), new Object[0]);
      else
        throw new RuntimeException("Parent object must not be null! Did you mean to use 'this'?");
    }*/
  }
  
  /**
   * Takes a file containing an old-style grammar file and rewrites it to a new file as JSON.
   * Note: pass null for the 2nd argument if you only want the returned String. 
   * 
   * @return the JSON grammar as a String
   */
  public static String convertToJSON(String infile, String outfile)
  {
    return LegacyGrammar.asJSON(infile, outfile);
  }
  
  protected void setGrammarFromProps(MultiMap grammarRules) 
  {
    this.rules.clear();

    for (Iterator iterator = grammarRules.keySet().iterator(); iterator.hasNext();)
    {
      String key = (String) iterator.next();
      key.replaceAll(DQ, E).replaceAll(SQ, E);
      String[] theRules = grammarRules.get(key);
      for (int j = 0; j < theRules.length; j++)
      {
        addRule(key, theRules[j]);
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
          probabilityPattern = Pattern.compile(PROB_PATTERN);
        
        Matcher m = probabilityPattern.matcher(part);
        if (m.matches())
        {
          if (m.groupCount() == 3)
          {
            String probStr = m.group(2);
            // nothing after the weight is allowed
            part = m.group(1) + m.group(3);
            //String ignored = m.group(3);
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
      throw new RiTaException("Rule not found: "+rule+"\nRules:\n"+rules);
    
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

  String doRule(String name)
  {
    return rules.doRule(name);
  }
  
  public String getGrammar()
  {
    return rules.toString();
  }
  
  public String getRule(String name)
  {
    String rule = rules.getRule(name);
    return rule == null ? E : rule;
  }

  public boolean hasRule(String name)
  {
    return rules.hasRule(name);
  }
  
  public RiGrammar load(String grammarRulesAsString)
  {
    //return setGrammar(grammarRulesAsString, false);
    String s = parseAsJSON(grammarRulesAsString);

    if (s != null)
    {
      MultiMap mm = new MultiMap();
      mm.loadFromString(s);
      setGrammarFromProps(mm);
    }

    if (this.rules == null || this.rules.keySet().size() < 1)
      throw new RiTaException("Unable to parse valid grammar rules!");
    
    return this;
  }
  /*
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
  }*/

  protected String parseAsJSON(String grammarRulesAsString)
  {
    try
    {
      JSONObject json = new JSONObject(grammarRulesAsString);
      return toProperties(json);
    }
    catch (JSONException e)
    {
      throw new RiTaException
        ("Unable to parse grammar as JSON, please make sure it is valid");
    }
  }

  protected String toProperties(JSONObject json) throws JSONException
  {
    StringBuilder sb = new StringBuilder();
    
    Iterator keys = json.keys();
    
    while (keys.hasNext())
    {
      String key = (String) keys.next();
      Object o = json.get(key);
      String ruleStr = null;
      if (o instanceof JSONArray) {
        JSONArray jarr = json.getJSONArray(key);
        ruleStr = jarr.join("|");
      }
      else if (o instanceof String){
        ruleStr = (String) o;
      }
      else {
        throw new RiTaException("Unexpected type: "+o.getClass());
      }
      
      sb.append(key+'='+ruleStr.replaceAll("\"", "")+'\n');
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
        String expanded = doRule(name);
        String post = production.substring(idx+name.length());
        return pre + expanded + post;
      }
    }
    return E;
  }

  public static void main(String[] args)
  {
    RiGrammar rg = new RiGrammar();
    rg.loadFromFile("sentence1.json");
    rg.print();
    /*for (int i = 0; i < 5; i++)
    {
      System.out.println(i+") "+rg.expand());      
    }*/  
  }

}
