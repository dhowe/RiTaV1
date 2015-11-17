package rita;

import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.json.JSONArray;
import rita.json.JSONException;
import rita.json.JSONObject;
import rita.support.Constants;
import rita.support.YAMLParser;

public class RiGrammar
{
  public static String START_RULE = "<start>";
  
  static final String E = "";
  static final Pattern PROB_PATT = Pattern.compile("(.*[^\\s])\\s*\\[([0-9.]+)\\](.*)");
  static final Pattern EXEC_PATT = Pattern.compile("(.*?)(`[^`]+?\\(.*?\\);?`)(.*)");

  public YAMLParser yamlParser;
  public Map<String, Map<String, Float>> _rules;
  public Object grammarUrl, parent;
  public Pattern probabilityPattern;
  //public RiGrammarEditor editor;
  public boolean execDisabled;
  public int maxIterations = 1000;

  static boolean yamlWarning;

  public RiGrammar()
  {
    this(null);
  }

  public RiGrammar(Object parent)
  {
    this(null);
    if (!(parent instanceof String))
      this.parent = parent;
  }
  
  public RiGrammar(String grammarAsString)
  {
    this(grammarAsString, null);
  }
  
  public RiGrammar(String grammarAsString, Object pApplet)
  {
    if (pApplet != null)
      this.parent = pApplet;
    
    this._rules = new HashMap<String, Map<String, Float>>();
    
    try
    {
      this.yamlParser = new YAMLParser();
    }
    catch (RiTaException e)
    {
      // no YamlParser, just warn once...
      if (!RiTa.SILENT && !yamlWarning) { 
        
        System.out.println("[WARN] No YAML parser found. If you want to use YAML grammars, include snakeyaml-X.YZ.jar (from https://code.google.com/p/snakeyaml/) in your classpath.");
        yamlWarning = true;
      }
    }
    
    if (grammarAsString != null)
      this.load(grammarAsString);
  }

  public RiGrammar loadFrom(String file, Object pApplet)
  {
    this.grammarUrl = file;
    this.parent = pApplet;
    return load(RiTa.loadString(file, pApplet));
  }
  
  public RiGrammar loadFrom(String file)
  {
    this.grammarUrl = file;

    return load(parent == null ? 
        RiTa.loadString(file, Constants.BN, "RiGrammar.loadFrom")
        : RiTa.loadString(file, parent, Constants.BN));
  }

  public RiGrammar loadFrom(URL url)
  {
    this.grammarUrl = url;
    return load(RiTa.loadString(url));
  }

  /*public RiGrammarEditor openEditor() {
    
    return openEditor(-1, -1);
  }
  
  public RiGrammarEditor openEditor(int width, int height) {
    
    return openEditor((Object)null, width, height);
  }
  
  public RiGrammarEditor openEditor(String fileName, int width, int height) {
    return openEditor(fileName, width, height);

  }
  public RiGrammarEditor openEditor(URL url, int width, int height) {
    return openEditor(url, width, height);
  }
  
   *
   * Provides a live, editable view of a RiGrammarPort text file that can be
   * dynamically loaded into a sketch without stopping and restarting it.
   *
  protected RiGrammarEditor openEditor(Object fileOrUrl, int width, int height)
  {
    if (fileOrUrl != null) {
      
      if (fileOrUrl instanceof String)
        loadFrom((String) fileOrUrl, parent);
      
      else if (fileOrUrl instanceof URL)
        loadFrom((URL) fileOrUrl);
    }
    
    boolean invalid = grammarUrl == null && _rules.size() > 0;
    
    if (editor == null)
      editor = new RiGrammarEditor(this);

    if (width > -1 && height > -1)
      editor.setSize(width, height);
    
    editor.setVisible(true);
    
    //System.out.println("RiGrammar.openEditor()" + _rules);
    
    if (invalid) {
      
      System.err.println("[WARN] To use the editor, you need"
          + " to load your grammar from a file or URL!");
      editor.setVisible(false);
    }
        
    return editor;
  }*/

  /**
   * Returns true if there is at least one valid rule in the object, else false
   */
  public boolean ready()
  {
    return this._rules.size() > 0;
  }
  
  public RiGrammar load(String grammarRulesAsString)
  {
    //System.out.println("RiGrammar.load("+grammarRulesAsString+")\n");
    
    this.reset();
    
    if (grammarRulesAsString == null || grammarRulesAsString.length()<1)
      throw new RiTaException("RiGrammar: no grammar rules found!");
    
    if (yamlParser != null) {
      
       return load(yamlParser.yamlToJSON(grammarRulesAsString));
    }

    try // otherwise trying parsing as simple JSON
    {
      return load(new JSONObject(grammarRulesAsString));
    }
    catch (JSONException e)
    {
      throw new RiTaException
        ("Grammar appears to be invalid YAML/JSON, please check it!"
            + "(http://yamllint.com/)\n\n"+ grammarRulesAsString +"\n", e);
    }
  }
  
  // NOTE: this method is implemented in earlier versions (not currently used)
  // public RiGrammar load(processing.data.JSONObject json);
  
  public RiGrammar load(JSONObject json) 
  {
    Iterator keys = json.keys();

    while (keys.hasNext())
    {
      String key = (String) keys.next();
      Object o = json.get(key);

      String ruleStr = "";
      
      if (o instanceof JSONArray)
      {
        JSONArray jarr = json.getJSONArray(key);
        for (int i = 0; i < jarr.length(); i++) {
        
          ruleStr += jarr.getString(i) + "|";
        }
        
        //ruleStr = jarr.join("|"); // has bug
      }
      else if (o instanceof String)
      {
        ruleStr = ((String) o);
      }
      else
      {
        throw new RiTaException("Unexpected type: " + o.getClass());
      }

      addRule(key, ruleStr);
    }
    
    return this;
  }
  
  public RiGrammar removeRule(String name)
  {
    this._rules.remove(name);
    return this;
  }

  protected RiGrammar _copy() // NIAPI
  {
    RiGrammar tmp = new RiGrammar();
    for (String name : _rules.keySet())
    {
      tmp._rules.put(name, this._rules.get(name));
    }
    return tmp;
  }

  public RiGrammar addRule(String name, String rule)
  {
    return addRule(name, rule, 1);
  }

  private static final String OR_PATT = "\\s*\\|\\s*";
  
  public RiGrammar addRule(String name, String ruleStr, float weight)
  {
    boolean dbug = false;

    if (dbug) log("addRule: " + name + " -> '" + ruleStr + "'       [" + weight + "]");

    String[] ruleset = ruleStr.split(OR_PATT);

    for (int i = 0; i < ruleset.length; i++)
    {
      String rule = ruleset[i];
      float prob = weight;
      String[] m = testExec(PROB_PATT, rule);

      if (m != null/* && m.length >= 4*/) // found weighting
      {
        if (dbug)
        {
          log("Found weight for " + rule);
          for (i = 0; i < m.length; i++)
            log("  " + i + ") '" + m[i] + "'");
        }

        rule = m[1] + m[3];
        prob = Float.parseFloat(m[2]);

        if (dbug)
          log("weight=" + prob + " rule='" + rule + "'");
      }

      if (this.hasRule(name))
      {

        if (dbug) log("rule exists");
        Map<String, Float> map = this._rules.get(name);
        map.put(rule, prob);
      }
      else
      {
        Map<String, Float> temp2 = new HashMap<String, Float>();
        temp2.put(rule, prob);
        this._rules.put(name, temp2);
        if (dbug)
          log("added rule: " + name);
      }
    }

    return this;
  }

  void log(String string)
  {
    System.out.println(string);
  }

  public RiGrammar reset()
  {
    _rules.clear();
    return this;
  }

  String doRule(String pre)
  {
    int cnt = 0;
    String name = E;
    Map<String, Float> rules = this._rules.get(/*this._normalizeRuleName*/(pre));

    if (rules == null)
      return null;

    for (String s : rules.keySet())
    {
      name = s;
      cnt++;
    }

    if (cnt < 1)
      return null;

    return (cnt == 1) ? name : this._getStochasticRule(rules);
  }

  public String getGrammar()
  {
    StringBuilder s = new StringBuilder();
    for (String name : this._rules.keySet())
    {
      s.append(name + "\n");
      Map<String, Float> choices = this._rules.get(name);
      for (String p : choices.keySet())
      {
        s.append("  '" + p + "' [" + choices.get(p) + "]\n");
      }
    }
    return RiTa.chomp(s.toString());
  }

  public RiGrammar print()
  {
    return print(System.out);
  }

  public boolean hasRule(String name)
  {
    return this._rules.containsKey((name));
  }

  public String expandWith(String literal, String symbol)
  {
    RiGrammar gr = this._copy();

    boolean match = false;
    for (String name : gr._rules.keySet())
    {
      if (name.equals(symbol))
      {
        Map<String, Float> obj = new HashMap<String, Float>();
        obj.put(literal, 1.0f);
        gr._rules.put(name, obj);
        match = true;
      }
    }

    if (!match)
      log("Error: Rule '" + symbol + "' not found in grammar");

    // TODO: tmp, awful hack,-write this correctly
    int tries, maxTries = 1000;
    for (tries = 0; tries < maxTries; tries++)
    {
      String s = gr.expand();
      if (s.indexOf(literal) > -1)
        return s;
    }

    log("[ERROR] RiGrammarPort failed to complete after " + tries + " tries\n");
    return null;
  }

  public String expand()
  {
    return expandFrom(START_RULE);
  }
  
  public String expand(Object callbackListener)
  {
    return expandFrom(START_RULE, callbackListener);
  }
  
  public String expandFrom(String rule)
  {
    return expandFrom(rule, null);
  }
  
  public String expandFrom(String rule, Object callbackListener) {
    
    // System.out.println("RiGrammar.expandFrom("+rule+")");
    
    if (!this.hasRule(rule))
      throw new RiTaException("Rule not found: "+rule+"\nRules:\n"+_rules);
    
    if (callbackListener == null)
      callbackListener = parent;
    
    int tries = 0;
    while (++tries < maxIterations)
    {
      String next = expandRule(rule);
      
      if (next != null && next.length()> 0) { // matched a rule
        
        rule = next;
        continue;
      }
      
      // we're done with rules
      
      if (this.execDisabled) break; // return
            
      // now check for back-ticked strings to eval      
      String result = this.checkExec(rule, callbackListener);
      if (result == null) {
        
        break;
      }
        
      rule = result;
    } 
    
    if (tries >= maxIterations && !RiTa.SILENT) 
      System.out.println("[WARN] max number of iterations reached: "+maxIterations);

    return RiTa.unescapeHTML(rule);  // after any execs
  }

  // TODO: doesn't handle recursive rules: see GH issue #3
  protected String checkExec(String rule, Object callbackListener)
  {
    String[] parts = testExec(EXEC_PATT, rule);
    
    if (parts == null || parts.length < 2) {
      
      return null; // return - nothing to eval
    }
    
    if (parts.length > 2 && parts[2].length() > 0) {
      
      String theCall = parts[2];
      if (countTicks(theCall) != 2) {
        
        System.err.println("[WARN] Unable to parse recursive exec: "+theCall+"...");
        return null;
      }
          
//System.err.println("RiGrammar.EXEC: "+parts.length+" / "+theCall);

      String callResult = E;
      try
      {
        callResult = handleExec(theCall, callbackListener);
      }
      catch (Exception e)
      {
        if (!RiTa.SILENT) { 
          System.err.println("[WARN] "+e.getMessage());
        }
        return null;
      }
              
      rule = parts[1] + callResult;
          
      if (parts.length > 3)
        rule += parts[3];
    }
    
    return rule;
  }

  private int countTicks(String theCall)
  {
    int count = 0;
    for (int i = 0; i < theCall.length(); i++)
    {
      if (theCall.charAt(i) == '`')
        count ++;
    }
    return count;
  }
 
  private String handleExec(String thePart, Object callee)
  {
    boolean dbug = false;
    
    if (thePart == null) return null; // should never happen
    
    if (dbug) System.out.println("RiGrammar.handleExec("+thePart+")");
    
    String toReturn = E, function = null;
    try
    {
      function = thePart.trim().replaceAll("^`", E)
          .replaceAll("`$", E).replaceAll(";$", E);
      
      if (function == null || function.length() < 1) return null;
          
      String[] args = testExec(Pattern.compile("\\((.*?)\\)"), thePart); // TODO: make constant
     
      if (args.length != 2)
        throw new RiTaException("Unable to parse args in back-ticked call: "+thePart);
      
      if (callee == null) 
        throw new RiTaException("\nFound what appears to be a callback:\n  "
           + thePart + "\nbut no callee object was supplied.\n\nPerhaps you "
           + "meant RiGrammar.expand(this)?\n");
  
      function = function.replaceAll("\\(.*?\\)", E);
      
      if (dbug) System.out.println("RiGrammar.function="+function);
      
      Object[] argsArr = null;
      
      if (!args[1].equals(E))  { // found args
        
        if (dbug) System.out.println("RiGrammar.args[1]="+args[1]);
        
        argsArr = formatArgs(args[1]);
        
        //if (dbug) System.out.println("RiGrammar.argsArr"+RiTa.asList(argsArr));
      }
      
      if (dbug)System.out.println("RiGrammar.invoke: "+function+"("+
          (RiTa.asList(argsArr).toString()).replaceAll("[\\[\\]]", E)+");");
      
      Object callResult = RiTa.invoke(callee, function, argsArr);

      if (callResult != null) toReturn = callResult.toString();
      
      if (dbug) System.out.println("RiGrammar.result: "+toReturn);
      
      return toReturn;
    }
    catch (Exception e)
    {
      if (!RiTa.SILENT) System.err.println("[WARN] Error invoking "
          + thePart+":\n"+RiTa.stackToString(e));
      
      return thePart;
    }
  }
  
  private Object[] formatArgs(String argsStr) {
    
    boolean dbug = false;
    
    if (dbug) System.out.println("RiGrammar.formatArgs("+argsStr+")");

    String[] strs = argsStr.split(",");

    Object[] args = new Object[strs.length];

    if (dbug)System.out.println("RiGrammar.numArgs="+strs.length);

    for (int i = 0; i < strs.length; i++)
    {
      strs[i] = strs[i].trim();

      String arg = strs[i].replaceAll("[\"']", E);
      
      args[i] = arg;
      if (dbug)System.out.println("arg: "+i+": "+args[i]);

      if (strs[i].equals(arg)) { // no change  
        
        try
        {
          args[i] = Integer.parseInt(arg);
          if (dbug)System.out.println("  Integer: "+args[i]);
          continue;
        }
        catch (Exception e) {}
        
        try
        {
          args[i] = Float.parseFloat(arg);
          if (dbug)System.out.println("  Float: "+args[i]);
          continue;
        }
        catch (Exception e) { }
     
        if (arg.equals("true") || arg.equals("false")) {
          try
          {
              args[i] = Boolean.parseBoolean(arg);
              if (dbug) System.out.println("  Boolean: "+args[i]);
              continue;
          }
          catch (Exception e) { }
        }
      }
      else {
        
        if (dbug)System.out.println("  changed to: "+arg+"  ("+strs[i] + " != " + arg+")");

      }
    }
    
    return args;
  }
  
  String expandRule(String prod)
  {
    boolean dbug = false;

    if (dbug) log("_expandRule(" + prod + ")");

    for (String name : this._rules.keySet())
    {
      int idx = prod.indexOf(name);
      
      if (dbug) log("    name="+name+"  entry="+_rules.get(name)+"  prod="+prod+"  idx="+idx);

      if (idx >= 0)
      {
        String pre = prod.substring(0, idx);
        String expanded = this.doRule(name);
        String post = prod.substring(idx + name.length());

        if (dbug) log("    pre="+pre+" exp="+expanded+" post="+post+" res="+(pre+expanded+post));

        return pre + expanded + post;
      }
    }

    return null; // no rules matched
  }


  private String _getStochasticRule(Map<String, Float> weightedRules)
  {
    String result = null;
    Map<String, Float> temp = weightedRules;
    Collection<Float> values = temp.values();
    Iterator<Float> it = values.iterator();
    float total = 0;
    double p = Math.random();
    while (it.hasNext())
    {
      total += it.next();
    }
    for (Iterator iterator = temp.entrySet().iterator(); iterator.hasNext();)
    {
      Map.Entry entry = (Map.Entry) iterator.next();
      if (p < (Float) entry.getValue() / total)
      {
        result = (String) entry.getKey();
        break;
      }
      p -= (Float) entry.getValue() / total;
    }
    return result;
  }

  public static String[] testExec(Matcher matcher)
  {
    List<String> result = null;
    boolean matchFound = matcher.find();

    if (matchFound)
    {
      // Get all groups for this match
      for (int i = 0; i <= matcher.groupCount(); i++)
      {
        if (result == null)
          result = new ArrayList<String>();
        result.add(matcher.group(i));
      }
    }

    return result == null ? null : result.toArray(new String[0]);
  }
  
  public static String[] testExec(Pattern p, String input)
  {
    return testExec(p.matcher(input));
  }

  public RiGrammar print(PrintStream ps)
  {
    ps.println("------------------------------");
    ps.println(this.getGrammar());
    ps.println("------------------------------");
    return this;
  }

  public static void mainX(String[] a)
  {
    String sentenceGrammar = "{ \"<start>\" : \"<noun_phrase> <verb_phrase>\", \"<noun_phrase>\" : \"<determiner> <noun>\", \"<verb_phrase>\" : \"<verb> | <verb> <noun_phrase> [.1]\", \"<determiner>\" : \"a [.1] | the\", \"<noun>\" : \"woman | man\", \"<verb>\" : \"shoots\" }";

    RiGrammar rg = new RiGrammar();
    //rg.load(sentenceGrammar);
    rg.load(RiTa.loadString("haikuGrammar2.json", null));
    for (int i = 0; i < 5; i++)
    {
      System.out.println(i + ") " + rg.expandFrom("<1>"));
    }
  }
  
  public static void main(String[] args)
  {
    RiGrammar rg = new RiGrammar("{\"a\" : \"b\"}");
    rg.loadFrom("haikuGrammar.yaml");
    System.out.println(rg.expand());
    //rg.openEditor();
    
    if (1==0) {
      rg.load(RiTa.loadString("haikuGrammar.json", null));
      System.out.println("'"+rg.expandFrom("<1>")+"'");
      //rg.setGrammar(RiTa.loadString("haikuGrammar2.json", null));
      //System.out.println("'"+rg.expandFrom("<1>")+"'");
    }
  }
  
}
