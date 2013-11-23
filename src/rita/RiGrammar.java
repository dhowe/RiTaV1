package rita;

import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.json.*;
import rita.support.EntityLookup;
import rita.support.GrammarIF;

public class RiGrammar implements GrammarIF
{
  public static final String OR_PATT = "\\s*\\|\\s*", E = "";
  public static final String STRIP_TICKS = "`([^`]*)`"; // global
  public static final String START_RULE = "<start>", OPEN_RULE_CHAR = "<", CLOSE_RULE_CHAR = ">";

  public static final Pattern PROB_PATT = Pattern.compile("(.*[^\\s])\\s*\\[([0-9.]+)\\](.*)");
  public static final Pattern EXEC_PATT = Pattern.compile("(.*?)(`[^`]+?\\(.*?\\);?`)(.*)");

  public Map<String, Map<String, Float>> _rules;
  public int maxIterations = 100;
  public String fileName, startRule = START_RULE;
  public Pattern probabilityPattern;
  public RiGrammarEditor editor;
  public boolean execDisabled;
  public Object parent;

  public RiGrammar()
  {
    this(null);
  }

  public RiGrammar(String grammarAsString)
  {
    this._rules = new HashMap<String, Map<String, Float>>();
    if (grammarAsString != null)
      this.load(grammarAsString);
  }

  public RiGrammar loadFromFile(String url)
  {
    return load(RiTa.loadString(url));
  }

  /**
   * Provides a live, editable view of a RiGrammarPort text file that can be
   * dynamically loaded into a sketch without stopping and restarting it.
   */
  public RiGrammarEditor openEditor(int width, int height)
  {

    if (editor == null)
    {
      // editor = new RiGrammarEditor(this);
      throw new RuntimeException("Editor not included in this version...");

    }
    editor.setSize(width, height);
    editor.setVisible(true);
    
    return editor;
  }

  public RiGrammar load(String grammarRulesAsString)
  {
    // System.out.println("RiGrammarPort.JSONparse(grammarRulesAsString)\n");
    this.reset();
    
    try
    {
      return load(new JSONObject(grammarRulesAsString));
    }
    catch (JSONException e)
    {
      throw new RiTaException("Grammar appears to be invalid JSON, please check it!", e);
    }
  }

  public GrammarIF removeRule(String name)
  {
    //name = this._normalizeRuleName(name);
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

  public RiGrammar load(JSONObject json)
  {
    Iterator keys = json.keys();

    while (keys.hasNext())
    {
      String key = (String) keys.next();
      Object o = json.get(key);
      String ruleStr = null;
      if (o instanceof JSONArray)
      {
        JSONArray jarr = json.getJSONArray(key);
        ruleStr = jarr.join("|");
      }
      else if (o instanceof String)
      {
        ruleStr = /*escapeEntities*/((String) o);
      }
      else
      {
        throw new RiTaException("Unexpected type: " + o.getClass());
      }

      //sb.append(key + ": " + ruleStr.replaceAll("\"", "") + '\n');
      addRule(key, ruleStr);
    }

    //String result = unescapeEntities(sb.toString());
    //System.out.println("props:\n" + result + "\n\n");

    return this;
  }
/*
  private String escapeEntities(String s) // yuk
  {
    return s.replaceAll(":", "&58;").replaceAll("\\|", "&#124;").replaceAll("\\[", "&#91;").replaceAll("\\]", "&#93;");
  }

  private String unescapeEntities(String s) // yuk
  {
    return s.replaceAll("&58;", ":").replaceAll("&#124;", "|").replaceAll("&#91;", "[").replaceAll("&#93;", "]");
  }
*/
  public RiGrammar addRule(String name, String rule)
  {
    return addRule(name, rule, 1);
  }

  /*
   * public RiGrammarPort addRule(String name, String[] rules, float[] weights)
   * {
   * 
   * }
   */
  public RiGrammar addRule(String name, String ruleStr, float weight)
  {
    boolean dbug = false;

    //name = this._normalizeRuleName(name);

    if (dbug) log("addRule: " + name + " -> '" + ruleStr + "'       [" + weight + "]");

    String[] ruleset = ruleStr.split(RiGrammar.OR_PATT);

    for (int i = 0; i < ruleset.length; i++)
    {
      String rule = ruleset[i];
      float prob = weight;
      String[] m = exec(PROB_PATT, rule);

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

  public GrammarIF print()
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

    // TODO: tmp, awful hack, write this correctly
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
    return expandFrom(START);
  }
  
  public String expand(Object callbackListener)
  {
    return expandFrom(START, callbackListener);
  }
  
  public String expandFrom(String rule)
  {
    return expandFrom(rule, null);
  }
  
  public String expandFrom(String rule, Object callbackListener)
  {
    if (callbackListener != null)
      this.parent = callbackListener;
    
    if (!this.hasRule(rule))
      throw new RiTaException("Rule not found: "+rule+"\nRules:\n"+_rules);
    
    int iterations = 0;
    while (++iterations < maxIterations)
    {
      String next = expandRule(rule);

      if (next == null || next.length() < 1)
      {
        // we're done, check for back-ticked strings to eval
        if (!this.execDisabled)
        {

          Matcher matcher = EXEC_PATT.matcher(rule);
          String[] parts = exec(matcher);

          if (parts != null)
          {

            if (parts.length < 2)
              return rule;

            rule = parts[1];

            boolean modified = false;
            if (parts.length > 2)
            {

              String callResult = handleExec(parts[2], callbackListener);
              
              modified = callResult != null; // dirty?

              rule += modified ? callResult : parts[2];
            }

            if (parts.length > 3)
              rule += parts[3];

            if (rule != null && modified)
              continue; // bc the call might have returned a new rule

            break;
          }
        }

        break;
      }
      
      rule = next;
    }
    
    if (iterations >= maxIterations) 
      System.out.println("[WARN] max number of iterations reached: "+maxIterations);

    return EntityLookup.getInstance().unescape(rule);  // after any execs
  }

  private String handleExec(String thePart, Object callee)
  {
    String function = thePart.trim().replaceAll("^`", E)
        .replaceAll("`$", E).replaceAll(";$", E).replaceAll("\\(\\)$", E);
    
    //System.out.println("Trying func="+function);

    try
    {
      if (parent == null) throw new RiTaException("Found a callback("
          +thePart+"), but no callee! Consider using: riGrammar.expand(callee);");

      return RiTa.invoke(callee, function).toString();
    }
    catch (Exception e)
    {
      if (!RiTa.SILENT) System.err.println(e);
      
      return null;
    }

  }
  
  String expandRule(String prod)
  {
    boolean dbug = false, trimSpace = false;

    ArrayList<String> result = new ArrayList<String>();

    if (trimSpace)
      prod = prod.trim();

    if (dbug)log("_expandRule(" + prod + ")");

    for (String name : this._rules.keySet())
    {

      Map<String, Float> entry = this._rules.get(/*this._normalizeRuleName*/(name));

      int idx = prod.indexOf(name);
      if (dbug) log("  name="+name+"  entry="+entry+"  prod="+prod+"  idx="+idx);

      if (idx >= 0)
      {

        String pre = prod.substring(0, idx);
        String expanded = this.doRule(name);
        String post = prod.substring(idx + name.length());

        if (trimSpace)
        {
          pre = pre.trim();
          post = post.trim();
          expanded = expanded.trim();
        }

        if (dbug) log("  pre="+pre+" exp="+expanded+" post="+post+" res="+(pre+expanded+post));

        result.add(pre);
        result.add(expanded);
        result.add(post);

        String ok = E;
        for (Iterator it = result.iterator(); it.hasNext();) {
          ok += (String) it.next();
        }

        if (dbug) log("Returns: " + ok);

        if (trimSpace) ok = ok.trim();

        return ok;
      }

      // do the exec check here, in while loop()
    }

    // what happens if we get here? no expansions left, return?

    return null;
  }

  /*String _normalizeRuleName(String pre)
  {
    if (pre != null && pre.length() > 0) {

      if (!pre.startsWith(RiGrammar.OPEN_RULE_CHAR))
        pre = RiGrammar.OPEN_RULE_CHAR + pre;
  
      if (!pre.endsWith(RiGrammar.CLOSE_RULE_CHAR))
        pre += RiGrammar.CLOSE_RULE_CHAR;
    }
    
    return pre;
  }*/

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

  public RiGrammarEditor openEditor()
  {
    return openEditor(-1, -1);
  }

  static String[] exec(Matcher matcher)
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
  
  static String[] exec(Pattern p, String input)
  {
    return exec(p.matcher(input));
  }

  public GrammarIF print(PrintStream ps)
  {
    ps.println("------------------------------");
    ps.println(this.getGrammar());
    ps.println("------------------------------");
    return this;
  }

  public static void main(String[] a)
  {
    String sentenceGrammar = "{ \"<start>\" : \"<noun_phrase> <verb_phrase>\", \"<noun_phrase>\" : \"<determiner> <noun>\", \"<verb_phrase>\" : \"<verb> | <verb> <noun_phrase> [.1]\", \"<determiner>\" : \"a [.1] | the\", \"<noun>\" : \"woman | man\", \"<verb>\" : \"shoots\" }";

    RiGrammar rg = new RiGrammar();
    rg.load(sentenceGrammar);
    //rg.loadFromFile("sentence1.json");
    //rg.print();
    if (1==1) return;
    for (int i = 0; i < 5; i++)
    {
      System.out.println(i + ") " + rg.expand());
    }
  }
  
}
