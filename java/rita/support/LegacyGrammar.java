package rita.support;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rita.RiTa;
import rita.RiTaException;

/**
 * Original context-free grammar based loosely on Mike Cleron's 'rsg-grammar'
 * Included only for LegacyGrammar.toJSON(in,out);
 */
public class LegacyGrammar implements Constants
{
  public static final String EXEC_CHAR = "`";
  public static final String EXEC_POST = ");" + EXEC_CHAR; // make semi-colon optional?
  public static final String START_SYM = "<start>";
  public static final String OPEN_DEF = "{";
  public static final String CLOSE_DEF = "}";
  public static final String OPEN_QUANT = "[";
  public static final String CLOSE_QUANT = "]";
  public static final String END_PROD = "|";
  public static final String BR = "\r";
  public static final String OPEN_TOKEN = "<";
  public static final String CLOSE_TOKEN = ">";
  public static final String COMMENT = "#";

  static boolean DBUG_expandFrom = false;
  static final boolean DBUG_EW = false;
  static boolean DBUG_EXPAND = false;

  static boolean execEnabled = true;

  int lineWidth = 80;
  public Map definitions = new HashMap();
  boolean constrainLineWidth = false;
  protected boolean insertSpaces = true;

  private String grammarFile; // for error feedback
  private Object callbackHandler; // for exec callbacks
  private StringBuilder buffer;

  private LegacyGrammar(Object callbackParent)
  {
    this.callbackHandler = callbackParent;
  }

  /*  public LegacyGrammar(Object callbackParent, URL url)
  {
    this(callbackParent);
    try
    {
      parseDefinitions(new RuleParser(url.openStream()));
    }
    catch (IOException e)
    {
      throw new RiTaException("[ERROR] reading: " + e);
    }
  }

 public LegacyGrammar(Object callbackParent, CharSequence input)
  {
    this(callbackParent, getBAIS(input), null);
  }*/
/*
  public LegacyGrammar(String grammarFileName)
  {
    this.grammarFile = grammarFileName;
    parseDefinitions(new RuleParser(getBAIS(grammarFileName)));
  }*/
  
  public LegacyGrammar(InputStream is)
  {
    parseDefinitions(new RuleParser(is));
  }
  
/*  private LegacyGrammar(Object callbackParent, InputStream is, String grammarFileName)
  {
    this(callbackParent);
    
    // System.out.println("Grammar.Grammar("+callbackParent.getClass()+","+is+","+grammarFileName+")");
    if (is == null)
      throw new RiTaException("Grammar: null input stream!");
    
    this.grammarFile = grammarFileName;
    parseDefinitions(new RuleParser(is));
    // System.out.println("DEFS: "+definitions);
  }
*/
 /* private static ByteArrayInputStream getBAIS(String input)
  {
    try
    {
      // System.out.println("Grammar.getBAIS()");
      return new ByteArrayInputStream(input.toString().getBytes("UTF-8"));
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RiTaException(e);
    }
  }*/

  // Methods ------------------------------------------

  /**
   * Return all the text generated thus far, that is, since the last call to
   * expand(), expandFrom(), or expandWith().
   */
  public String getBuffer()
  {
    return buffer.toString();
  }

  /**
   * Resets the contents of the current buffer 
   */
  public synchronized void setBuffer(CharSequence contents)
  {
    if (buffer == null)
      buffer = new StringBuilder(512);
    else
      buffer.delete(0, buffer.length());
    buffer.append(contents);
  }

  // does not copy the buffer
  public LegacyGrammar copy()
  {
    LegacyGrammar g = new LegacyGrammar(callbackHandler);
    g.lineWidth = this.lineWidth;
    g.grammarFile = this.grammarFile;
    g.constrainLineWidth = this.constrainLineWidth;
    if (g.definitions == null)
      g.definitions = new HashMap();
    for (Iterator i = definitions.values().iterator(); i.hasNext();)
    {
      Definition d = (Definition) i.next();
      g.definitions.put(d.getName(), d.copy());
    }
    return g;
  }

  static boolean isLineBreak(String s)
  {
    if (s == null)
      return false;
    return (s.equals(BR) || s.equals("\n"));
  }

  public void dumpDefinitions()
  {
    System.out.println("Grammar: --------------------------------------------------");
    for (Iterator i = definitions.keySet().iterator(); i.hasNext();)
    {
      String name = (String) i.next();
      String def = rulesToString(getDefinition(name).rules);
      System.out.println(name + " -> " + def);
    }
    System.out.println("-----------------------------------------------------------");
  }

  // note: this won't display commas correctly...
  private String rulesToString(List rules)
  {
    String defs = rules.toString();
    defs = defs.replaceAll("\\], \\[", " | ");
    defs = defs.replaceAll("\\[\\[", "");
    defs = defs.replaceAll("\\]\\]", "");
    defs = defs.replaceAll("\\, ", " ");
    // System.out.println(defs);
    return defs;
  }

  /**
   * Returns a Map<String,String> of the current Definitions, with names as keys
   * and String-representations of each Definition as values.
   */
  public Map getDefinitions()
  {
    Map m = new HashMap();
    for (Iterator i = definitions.keySet().iterator(); i.hasNext();)
    {
      String name = (String) i.next();
      String def = rulesToString(getDefinition(name).rules);
      m.put(name, def);
    }
    return m;
  }

  public Definition getDefinition(String name)
  {
    // System.out.println(definitions.keySet());
    return (Definition) definitions.get(name);
  }

  public List getProductions(String defName)
  {
    return getDefinition(defName).rules;
  }

  public void addDefinition(String name, List l)
  {
    definitions.put(name, new Definition(name, l));
  }

  public void resetDefinition(String name, List l)
  {
    Definition d = (Definition) definitions.get(name);
    if (d == null || l.equals(d.rules))
    {
      return;
    }
    else
    {
      definitions.put(name, new Definition(name, l));
    }
  }

  private void parseDefinitions(RuleParser rp)
  {
    String token;
    while ((token = rp.getNextToken()) != null)
    {
      if (isLineBreak(token))
        continue;

      // skip until first definition
      if (token.equals(OPEN_DEF))
      {
        Definition definition = new Definition(rp);
        definitions.put(definition.getName(), definition);
      }
    }
  }

  public String expand()
  {
    return expand(START_SYM, false);
  }
  
  public String expand(boolean preserveBuffer)
  {
    return expand(START_SYM, preserveBuffer);
  }

  public synchronized String expand(String token, boolean preserveBuffer)
  {
    //System.out.println("Grammar.expand("+token+")");
    //StringBuilder buff = new StringBuilder(512);
    
    StringBuilder buff = null;
    if (preserveBuffer || buffer == null)
      buff = new StringBuilder(512);
    else
      buff = buffer.delete(0, buffer.length());
    
    this.expandString(token, buff);
    
    return buff.toString().trim();
  }

  /**
   * Iterative version of expand call...
   * 
   * @param str
   * @param buf
   */
  int es = 0;

  void expandString(String str, StringBuilder buf)
  {
    if (DBUG_EXPAND)
      System.out.println("  Grammar.expandString('" + str + "')");

    es++;
    int k = 0;
    while (str != null && str.trim().length() > 0)
    {

      str = str.trim();

      // System.out.println(es+"."+(++k)+"="+str);

      if (LegacyGrammar.isExecEnabled())
      { // handle exec() calls
        if (isExec(str))
          handleExec(str);
      }

      int nextTokenIdx = str.indexOf(OPEN_TOKEN);
      if (nextTokenIdx >= 0)
      {
        // at least one token still in string
        int nextEndIdx = str.indexOf(CLOSE_TOKEN);
        if (nextEndIdx < 0)
          throw new IllegalStateException("[ERROR] Illegal grammar token ('no end token'): '"
              + str + "'");

        String preToken = str.substring(0, nextTokenIdx);
        String token = str.substring(nextTokenIdx, nextEndIdx + 1);
        String postToken = str.substring(nextEndIdx + 1);

        if (DBUG_EXPAND)
          System.out.println("    PRE= '" + preToken + "' TOK='" + token + "' POST='"
              + postToken + "'");

        if (preToken.length() > 0)
          appendTerminals(preToken, buf); // PRE (term)

        expandToken(token, buf); // TOKEN (single)

        str = postToken;
      }
      else
      // no tokens left
      {
        appendTerminals(str, buf);
        str = null;
      }
    } // end while
  }

  boolean isExec(String term)
  {
    if (term.startsWith(EXEC_CHAR) & term.endsWith(EXEC_POST))
      return true;
    return false;
  }

  void appendTerminals(String term, StringBuilder buf)
  {
    // System.out.println("      Grammar.appendTerminals("+term+")");
    term = term.trim();
    if (term == null || term.length() < 1)
      return;

    char first = term.charAt(0);
    int charType = Character.getType(first);
    boolean noSpace = false;

    if (term.equals(BN))
    {
      // System.err.println("AMP: "+term);
      noSpace = true;
      term = BN + "  ";
    }
    else
    {
      noSpace = (charType == Character.OTHER_PUNCTUATION && first != '&');// &&
                                                                          // buf.length()>0);
    }
    // System.err.println("BUF: '"+buf+"' - "+buf.length());

    if (noSpace && buf.length() > 0 && buf.charAt(buf.length() - 1) == ' ')
    {
      if (DBUG_EXPAND)
        System.out.println("PUNCTUATION: " + term);
      buf.deleteCharAt(buf.length() - 1);
    }

    if (insertSpaces && !term.startsWith("\n"))
      term += " ";

    buf.append(term);

    if (DBUG_EXPAND)
      System.out.println("APPENDED: '" + term + "' buf='" + buf + "'");
  }

  String handleExec(String term)
  {
    term = term.substring(EXEC_CHAR.length());

    String methodNm = null;
    int parenIdx = term.indexOf("()");
    if (parenIdx > -1)
    {
      term = term.substring(0, parenIdx);
      methodNm = stripQuotes(term);
      // System.out.println("INVOKE="+methodNm);
      return invoke(callbackHandler, methodNm).toString();
    }

    parenIdx = term.indexOf('(');
    if (parenIdx < 0)
    {
      throw new RiTaException("Invalid exec string: "
          + "does not contain open-paren, str=" + term);
    }
    methodNm = term.substring(0, parenIdx);

    // System.out.println("METH="+methodNm);

    String argsStr = term.substring(parenIdx + 1);
    int closeIdx = argsStr.indexOf(LegacyGrammar.EXEC_POST);
    if (closeIdx < 0)
      throw new RiTaException("Invalid exec string: " + "does not end with "
          + LegacyGrammar.EXEC_POST + " str=" + argsStr);
    argsStr = argsStr.substring(0, closeIdx);

    // System.out.println("ARGS="+argsStr);

    String[] strs = argsStr.split(",");
    Object[] args = new Object[strs.length];

    for (int i = 0; i < strs.length; i++)
    {
      strs[i] = strs[i].trim();
      if (!strs[i].startsWith("\""))
      {
        if (isToken(strs[i]))
        {
          StringBuilder tmp = new StringBuilder();
          expandToken(strs[i], tmp);
          args[i] = tmp.toString().trim();
        }
        else
        { // handle primitives
          try
          {
            args[i] = Boolean.parseBoolean(strs[i]);
          }
          catch (Exception e)
          {
          }
          try
          {
            args[i] = Integer.parseInt(strs[i]);
          }
          catch (Exception e)
          {
          }
          try
          {
            args[i] = Float.parseFloat(strs[i]);
          }
          catch (Exception e)
          {
          }
        }
      }
      else
        args[i] = stripQuotes(strs[i]).trim();
    }
    // System.out.println("INVOKE: "+methodNm+"("+RiTa.asList(args)+")");
    Object result = invoke(callbackHandler, methodNm, args);
    return result == null ? "" : result.toString();
  }

  private boolean isToken(String str)
  {
    return (str.indexOf(LegacyGrammar.OPEN_TOKEN) == 0 && (str.indexOf(LegacyGrammar.CLOSE_TOKEN) == str.length() - 1));
  }

  private void expandToken(String token, StringBuilder buf)
  {
    
    if (false) {
      PrintStream out = null;
      try
      {
        out = new PrintStream(System.out, true, "UTF-8");
      }
      catch (UnsupportedEncodingException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println();
      for (int i = 0; i < token.length(); i++)
        out.println(i + "] " + token.charAt(i) + " " + (int) token.charAt(i));
      System.out.println();
      for (Iterator it = definitions.keySet().iterator(); it.hasNext();)
      {
        token = (String) it.next();
        for (int i = 0; i < token.length(); i++)
          out.println(i + ") " + token.charAt(i) + " " + (int) token.charAt(i));
        System.out.println();
      }
      System.out.println(); 
      System.out.println("Grammar.expandToken(" + token + ") " + definitions);
    }
    
    Definition def = getDefinition(token);
    if (def == null)
    {
      System.err.println("Grammar.expandToken() failed\n" + definitions);
      throw new IllegalStateException("[ERROR] Undefined token: '" + token
          + "' in grammar '" + grammarFile + "':\n" + definitions);
    }
    
    if (DBUG_EXPAND)
      System.out.println("EXPANDING TOKEN: " + def);

    try
    {
      def.expand(this, buf);
    }
    catch (StackOverflowError e1)
    {
      System.err.println("\n[ERROR] Grammar (" + grammarFile
          + ") generates an infinite loop: " + def + "\n        " + "Buffer: " + buf
          + "\n        If you think your grammar "
          + "is ok, you may want to try adjusting Java's stack-size");
      throw new RiTaException("StackOverflowError");
    }
  }

  private String stripQuotes(String term)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < term.length(); i++)
    {
      char c = term.charAt(i);
      if (c != '\'' && c != '\"' && c != '`') // smart-quotes?
        sb.append(c);

    }
    return sb.toString();
  }

  /**
   * Set to false (default=true) if you don't want to use the exec() mechanism
   * for callbacks to a parent class. Useful if you want to include backticks or
   * method calls as terminals in your grammar. Note: this must be called before
   * loading a grammar file.
   * 
   * @invisible
   */
  public static void setExecEnabled(boolean enableExec)
  {
    execEnabled = enableExec;
  }

  /**
   * Returns the state of the execEnabled flag
   * 
   * @invisible
   */
  public static boolean isExecEnabled()
  {
    return execEnabled;
  }

  /**
   * Expands the grammar after replacing an instance of the non-terminal
   * 'symbol' with the String in 'literal'.
   * <P>
   * Guarantees that 'literal' will be in the final expanded String, assuming at
   * least one instance of 'symbol' in the Grammar.
   * <p>
   * Note: this method has been debugged/refactored for v79, please make sure
   * you are using a current RiTa version.
   * 
   * @exception RiTaException
   *              if 'symbol' is not found.
   */
  public String expandWith(String literal, String symbol, boolean preserveBuffer)
  {
    if (DBUG_expandFrom)
      System.out.println("Grammar.expandFrom(" + literal + ", " + symbol + ")");

    // add delimeters to the symbol if necessary
    if (!symbol.startsWith(OPEN_TOKEN))
      symbol = OPEN_TOKEN + symbol;
    if (!symbol.endsWith(CLOSE_TOKEN))
      symbol = symbol + CLOSE_TOKEN;

    // make sure the rule exists in the grammar
    Set prodNames = definitions.keySet();
    if (!prodNames.contains(symbol))
    {
      if (DBUG_expandFrom)
        System.out.println("No matching definition for: " + literal + ": " + symbol);
      throw new RiTaException("[ERROR] No production matching: " + symbol + " in "
          + prodNames);
    }

    // make a copy of the grammar to mutate (yuck)
    LegacyGrammar g = copy();

    List replaceRules = new LinkedList();
    List tmp = new LinkedList();
    tmp.add(literal);
    replaceRules.add(tmp);

    while (!symbol.equals(START_SYM))
    {
      if (DBUG_EW)
        System.out.println("START: Replacing '" + symbol + "' with '" + replaceRules
            + "'");
      if (DBUG_EW)
        System.out.println("SEARCHING Values for '" + symbol + "'");
      if (DBUG_EW)
        System.out.println("  replaced: " + symbol + " -> " + getProductions(symbol));

      Definition d = g.getDefinition(symbol);
      d.setRules(replaceRules);
      if (DBUG_EW)
        System.out.println("  with " + d);

      Definition[] matches = findDefByRightSide(g, d.getName());
      if (matches == null || matches.length < 1)
      {
        dumpDefinitions();
        throw new RiTaException("Invalid state: no (non-recursive) rule contains: "
            + d.getName());
      }

      if (DBUG_EW)
        System.out.println("Found " + matches.length + " defs containing " + d.getName()
            + ":");
      if (DBUG_EW)
        for (int i = 0; i < matches.length; i++)
          if (DBUG_EW)
            System.out.println("  " + i + ")" + matches[i]);

      int rand = (int) (Math.random() * matches.length);
      Definition nextDef = (Definition) matches[rand];
      if (DBUG_EW)
        System.out.println("Selected Def: " + nextDef);

      // Ok so we have nextDef here and we know it contains our <target>
      // so we check each of its rules, and delete any that dont contain
      // <target>
      List prods = nextDef.getRules();
      for (Iterator i = prods.iterator(); i.hasNext();)
      {
        boolean match = false;
        List elements = (List) i.next();
        for (Iterator j = elements.iterator(); j.hasNext();)
        {
          String tok = (String) j.next();
          if (tok.contains(d.getName()))
          {
            match = true;
          }
        }
        if (!match)
          i.remove();
      }
      if (DBUG_EW)
        System.out.println("Post-Removal: " + nextDef);

      symbol = nextDef.getName();
      replaceRules = nextDef.getRules();
    }

    return g.expand(preserveBuffer);
  }

  /**
   * Looks up a non-terminal in all right-side definitions returns a List of the
   * Definition object that match.
   * 
   * @return Definition[] of matches
   */
  private static Definition[] findDefByRightSide(LegacyGrammar g, String nonTerminal)
  {
    // System.out.println("Grammar.findRuleByRightSide("+nonTerminal+")");
    List definitions = new LinkedList();
    Collection defs = g.definitions.values();
    for (Iterator i = defs.iterator(); i.hasNext();)
    {
      Definition def = (Definition) i.next();
      // System.out.println(" checking: "+def);

      // skip the terminal on the right side
      if (def.getName().equals(nonTerminal))
        continue;

      List l = def.findRuleContaining(nonTerminal);
      if (l != null)
      {
        if (DBUG_EW)
          System.out.println("FOUND: " + def);
        definitions.add(def);
      }
    }
    return (Definition[]) definitions.toArray(new Definition[definitions.size()]);
  }

  public static void testHistory(String[] args) throws MalformedURLException
  {
    File grammarDir = new File("file://" + System.getProperty("user.dir") + "/data");
    URL grammarUrl = new URL(grammarDir + "/poem.g");
    LegacyGrammar g = new LegacyGrammar(grammarUrl);
    // g.enableHistory(true);
    String current = "f";
    for (int i = 0; i < 1; i++)
    {
      System.err.println("---------------------------------------------");
      // current = current.replaceAll("f", "<F>");
      // System.err.println("EXPANDING: '"+current+"'");
      current = g.expand(false);
      System.err.println("RETURNED: '" + current + "'");
    }
    System.err.println("DONE...");
    // System.err.println("stack:  " +g.tokens);
    // System.err.println("history: "+g.history);
  }

  public boolean isInsertSpaces()
  {
    return this.insertSpaces;
  }

  public void setInsertSpaces(boolean insertSpaces)
  {
    this.insertSpaces = insertSpaces;
  }

  public String getGrammarFileName()
  {
    return grammarFile;
  }

  /** @deprecated */
  void expandStringRecursive(String str, StringBuilder buf)
  {
    if (DBUG_EXPAND)
      System.out.println("Grammar.expand('" + str + "')");

    str = str.trim();
    if (str == null || str.length() == 0)
      return;
    if (DBUG_EXPAND)
      System.err.println("EXPAND: " + str);
    int nextTokenIdx = str.indexOf(OPEN_TOKEN);

    if (nextTokenIdx < 0) // no tokens left
      appendTerminals(str, buf);

    // at least one token
    String preToken = str.substring(0, nextTokenIdx);
    int nextEndIdx = str.indexOf(CLOSE_TOKEN);
    if (nextEndIdx < 2)
      throw new IllegalStateException("[ERROR] Illegal grammar token ('no end token'): '"
          + str + "'");
    String token = str.substring(nextTokenIdx, nextEndIdx + 1);
    String postToken = str.substring(nextEndIdx + 1);

    if (DBUG_EXPAND)
      System.err.println("PRE= '" + preToken + "' TERM='" + token + "' POST='"
          + postToken + "'");

    appendTerminals(preToken, buf); // PRE (term)
    expandToken(token, buf); // TOKEN (single)
    expandString(postToken, buf); // POST (unknown)
  }

  public static void mainX(String[] args) throws MalformedURLException
  {
    File grammarDir = new File("file://" + System.getProperty("user.dir") + "/src/data");
    URL grammarUrl = new URL(grammarDir + "/examples/simplest.g");
    String[] results = new String[10];
    LegacyGrammar g = new LegacyGrammar(grammarUrl);

    // regular expands
    for (int i = 0; i < results.length; i++)
      results[i] = g.expand(false);
    for (int i = 0; i < results.length; i++)
      System.out.println(i + ") " + results[i]);
    System.out.println();

    // expands-withs
    for (int i = 0; i < results.length; i++)
      results[i] = g.expandWith("eat", "ZZZ", false);
    System.out.println();
    for (int i = 0; i < results.length; i++)
      System.out.println(i + ") " + results[i]);
  }

  public static void mainXXX(String[] args) throws MalformedURLException
  {
    if (1 == 1)
    {
      String s = "tree.g";
      s = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/data/poem.g";
      InputStream is = RiTa.openStream(s);
      // System.out.println(RiTa.asList(PApplet.loadStrings(is)));
      RuleParser rp = new RuleParser(is);
      String token;
      Map definitions = new HashMap();
      while ((token = rp.getNextToken()) != null)
      {
        System.out.println("tok: " + token);
        if (isLineBreak(token))
          continue;

        // skip until first definition
        if (token.equals(OPEN_DEF))
        {
          Definition definition = new Definition(rp);
          definitions.put(definition.getName(), definition);
        }
      }
      System.out.println(definitions);
    }

    else
    {
      String s = "{\n<start>\n  ü all good | all bad\n}";
      ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
      LegacyGrammar g = new LegacyGrammar(bais);
      for (int i = 0; i < 10; i++)
        System.out.println(g.expand(false));
    }
  }
  
  
  /** 
   * Calls 'methodName' on 'callee' Object with args via reflection
   * @return return value of method or null on error.
   * @invisible
   */
  public static Object invoke(Object callee, 
    String methodName, Class[] argTypes, Object[] args)
  { 
    Method m = null;
    try
    {
//System.out.println("INVOKE: "+callee.getClass()+"."+methodName+"(types="+asList(argTypes)+", vals="+asList(args)+")");      
      m = findMethod(callee, methodName, argTypes, args, true);
    }
    catch (RiTaException e) 
    {      
      throw e;
    }    
    return invoke(callee, m, args);
  }
  

  /** @invisible */
  public static Method findMethod(Object callee, String methodName, Class[] argTypes, Object[] args, boolean isPublic)
  {
    Method m = null;
    try 
    {
      if (callee instanceof Class) {  // static method
        if (isPublic) {
          m = ((Class)callee).getMethod(methodName,  argTypes);
        }
        else
          m = ((Class)callee).getDeclaredMethod(methodName,  argTypes);
      }
      else                       // non-static method
      {
        if (isPublic) 
        {
          m = callee.getClass().getMethod(methodName, argTypes);
        }
        else
          m = callee.getClass().getDeclaredMethod(methodName, argTypes);
      }
    } 
    catch (Exception e) {
      throw new RiTaException(e);       
    }    
    //if (!isPublic) m.setAccessible(true);  // re-add??    
    return m;
  }

  
  /** 
   * Calls 'method' on 'object' via reflection, passing 'args'
   * and inferring the Class[] of argument types by calls to 
   * {@link Class#getClass()} for each arg.
   * 
   * @return return value of method or null on error.
   * @invisible
   */
  public static Object invoke(Object callee, String methodName, Object[] args)
  { 
    if (args == null) return invoke(callee, methodName);    
    Class[] argTypes = new Class[args.length];
    for (int i = 0; i < args.length; i++)  {
      argTypes[i] = args[i].getClass();  
      if (argTypes[i]==Integer.class)
        argTypes[i] = Integer.TYPE;
      else if (argTypes[i]==Boolean.class)
        argTypes[i] = Boolean.TYPE;
      else if (argTypes[i]==Float.class)
        argTypes[i] = Float.TYPE;
      else if (argTypes[i]==Double.class)
        argTypes[i] = Double.TYPE;
      else if (argTypes[i]==Character.class)
        argTypes[i] = Character.TYPE; 
    }
    return invoke(callee, methodName, argTypes, args);
  }   
  
  /** 
   * Calls 'methodName' on 'callee' Object via reflection
   * @invisible
   * @return return value of method or null on error.
   */
  public static Object invoke(Object callee, String methodName)
  { 
    return invoke(callee, methodName, null, null);    
  }
  

  private static Object invoke(Object callee, Method m, Object[] args)
  {
    try 
    {
//System.out.println("INVOKE: "+callee+"."+m.getName()+"("+asList(args)+")");
      return m.invoke(callee, args);
    } 
    catch (Throwable e)
    {
      Throwable cause = e.getCause();
      while (cause != null) {
         e = cause;
         cause = e.getCause();
      }
      throw new RiTaException(e);      
    }
  }
  
  protected static String writeToFile(String fname, String content)
  {   
    try
    {
      FileWriter fw = new FileWriter(fname);
      fw.write((content == null) ? "null" : content);
      fw.flush();
      fw.close();
    }
    catch (Exception e)
    {
      throw new RiTaException(e);
    }
    
    return fname;
  }
  
  public static String asJSON(String infile, String outfile)
  {
    LegacyGrammar g = new LegacyGrammar(RiTa.openStream(infile));

    Map defs = g.getDefinitions();
    g.dumpDefinitions();
    StringBuilder sb = new StringBuilder();
    String IN = "  ", DQ ="\"", LB = "\n";
    sb.append("{"+LB+IN);
    String[] rules = (String[]) defs.keySet().toArray(new String[0]);
    for (int j = 0; j < rules.length; j++)
    {
      String rule = rules[j];
      String def = (String) defs.get(rule);
      sb.append(LB+IN+DQ+rule.trim()+DQ+": [");
      String[] parts = def.split("\\|");
      for (int i = 0; i < parts.length; i++)
      {
        sb.append(LB+IN+IN+DQ+parts[i]+DQ);
        if (i < parts.length-1)
          sb.append(",");
      }
      sb.append(LB+IN+"]");
      if (j < rules.length-1) 
        sb.append(",");
      sb.append(LB);
    }
    sb.append("}"+LB);    
    
    String result = sb.toString();
    if (outfile != null)
      writeToFile(outfile, result);

    return result;
  }
  
  public static void main(String[] args)
  {
    //String infile = "/Users/dhowe/Documents/eclipse-workspace/RiTa_Java/haiku.g";
    String infile = "/Users/dhowe/Documents/Processing/Linnea_sGrammar1/data/linneagrammar.g"; // problem??
    //infile = "/Users/dhowe/Documents/eclipse-workspace/CornellsYoke/src/data/newgrammar.txt";
    //String infile = "haikuGrammar.g";
    String outfile = "/Users/dhowe/Desktop/linneagrammar.json";
    //outfile = "/Users/dhowe/Documents/eclipse-workspace/CornellsYoke/src/data/newgrammar.json";
    String json = asJSON(infile, null);
    System.out.println(json);
  }

}// end
