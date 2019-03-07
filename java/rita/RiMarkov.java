package rita;

import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import rita.support.Constants;
import rita.support.TextNode;


/*
 * BUG: 
 * w' myTestFile2.txt:
 *   'Then the chief clerk called Good morning, I sit before the green window and the open crack in the door. Wilson.'
 *   'On the quiet and open morning, the cats sit before the green window and the open crack in the door. Wilson, who called me silly.
 * w' kafka.txt:
 *   ', in the entire flat and especially in the kitchen and just reads the paper or studies train timetables.'
 *   ', so don't keep trying to do it - although that much dampness also made Gregor ill and he lay flat on the couch, of course, moving it an'
 inch.
 */

public class RiMarkov implements Constants
{
  static { RiTa.init(); }
  
  public static String SS_REGEX = "\"?[A-Z][a-z\"',;`-]*";

  /** constant for max # of tries for a generation */
  public static int MAX_GENERATION_ATTEMPTS = 5000;

  protected static final Map EMPTY_MAP = new HashMap();
  protected static final String SS_DELIM = "D=l1m_";
  protected static final int MAX_PROB_MISSES = 100;

  public int minSentenceLength = 6, maxSentenceLength = 35,  N;
  public boolean printIgnoredText = false;
  
  protected TextNode root;
  //protected Set sentenceList;
  protected Stack pathTrace;
  protected List sentenceStarts;

  protected int wordsPerFile, tokenCount, skippedDups;
  protected boolean useSmoothing, ignoreCase, allowDuplicates;

  protected boolean removeQuotations = true, sentenceAware = true, addSpaces = true, profile = true;
  protected String rawText = "";
  private Object parent;
  
  /**
   * Construct a sentence-generating Markov chain (or n-gram) model
   */
  public RiMarkov(int nFactor)
  {
    this(nFactor, true, true);
  }
  
  public RiMarkov(Object parent, int nFactor)
  {
    this(parent, nFactor, true, true);
  }

  /**
   * Construct a sentence-generating Markov chain (or n-gram) model and set its
   * n-Factor and whether it will attempt to recognize (English) sentences
   */
  public RiMarkov(int nFactor, boolean recognizeSentences)
  {
    this(nFactor, recognizeSentences, true);
  }
  
  public RiMarkov(Object parent, int nFactor, boolean recognizeSentences)
  {
    this(parent, nFactor, recognizeSentences, true);
  }

  public RiMarkov(int nFactor, boolean recognizeSentences, boolean allowDuplicates) {
    
    this(null, nFactor, recognizeSentences, allowDuplicates);
  }
  
  /**
   * Construct a sentence-generating Markov chain (or n-gram) model and set its
   * n-Factor and whether it will attempt to recognize (English) sentences, and
   * allow duplicates in its output
   */
  public RiMarkov(Object parent, int nFactor, boolean recognizeSentences, boolean allowDuplicates)
  {
    if (nFactor < 1)
      throw new RiTaException("N-factor must be > 0");
    
    this.N = nFactor;
    this.parent = parent;
    this.sentenceAware = recognizeSentences;
    this.allowDuplicates = allowDuplicates;
    this.root = TextNode.createRoot(ignoreCase);
  }

  // loadFrom(URL)  ----------------------------------------------------------

  public RiMarkov loadFrom(URL url)
  {
    return loadFrom(url, 1, null);
  }
  
  public RiMarkov loadFrom(URL url, int multiplier)
  {
    return loadFrom(url, multiplier, null);
  }

  public RiMarkov loadFrom(URL url, int multiplier, String regex) // impl
  {
    return loadText(RiTa.loadString(url), multiplier, regex);
  }
  
  // loadFrom(URLs)  ----------------------------------------------------------

  public RiMarkov loadFrom(URL[] urls)
  {
    return this.loadFrom(urls, 1, null); 
  }
  
  public RiMarkov loadFrom(URL[] urls, int multiplier)
  {
    return this.loadFrom(urls, multiplier, null); 
  }
  
  public RiMarkov loadFrom(URL[] urls, int multiplier, String regex) // impl
  {
    return loadText(RiTa.loadString(urls), multiplier, regex);
  } 
  
  // loadFrom(Str)  ----------------------------------------------------------

  public RiMarkov loadFrom(String url)
  {
    return loadFrom(url, 1, null, this.parent);
  }
  
  public RiMarkov loadFrom(String url, int multiplier, String regex)
  {
    return loadFrom(url, multiplier, regex, this.parent);
  }
  
  public RiMarkov loadFrom(String url, int multiplier)
  {
    return loadFrom(url, multiplier, null, this.parent);
  }
  
  public RiMarkov loadFrom(String[] urls)
  {
    return this.loadFrom(urls, 1, null, this.parent);
  }

  public RiMarkov loadFrom(String[] urls, int multiplier)
  {
    return this.loadFrom(urls, multiplier, null, this.parent);
  }
  
  public RiMarkov loadFrom(String[] urls, int multiplier, String regex)
  {
    return this.loadFrom(urls, multiplier, regex, this.parent);
  }
  
  // -----
  
  public RiMarkov loadFrom(String url, Object aParent)
  {
    return loadFrom(url, 1, null, aParent);
  }
  
  public RiMarkov loadFrom(String url, int multiplier, Object aParent)
  {
    return loadFrom(url, multiplier, null, aParent);
  }
  
  public RiMarkov loadFrom(String url, int multiplier, String regex, Object aParent) // impl
  {
    if (aParent == null)
      return loadText(RiTa.loadString(url, "RiMarkov.loadFrom"), multiplier, regex);

    return loadText(RiTa.loadString(url, aParent), multiplier, regex);
  }
  
  public RiMarkov loadFrom(String[] urls, Object aParent)
  {
    return this.loadFrom(urls, 1, aParent); 
  }
  
  public RiMarkov loadFrom(String[] urls, int multiplier, Object aParent)
  {
    return this.loadFrom(urls, multiplier, null, aParent); 
  }
  
  public RiMarkov loadFrom(String[] urls, int multiplier, String regex, Object aParent)
  {
    if (aParent == null)
      return loadText(RiTa.loadString(urls, "RiMarkov.loadFrom"), multiplier, regex);

    return loadText(RiTa.loadString(urls, aParent), multiplier, regex);
  }

  // METHODS ----------------------------------------------------------
  
  public boolean printingIgnoredText()
  {
    return printIgnoredText;
  }

  public RiMarkov printIgnoredText(boolean print)
  {
    this.printIgnoredText = print;
    return this;
  }

  /**
   * Determines whether calls to generateSentence(s) will return sentences that
   * exist (character-for-character) in the input text.
   * <p>
   * Note: The trade-off here is between ensuring novel outputs and a potential
   * slow-down due to rejected outputs (b/c they exist in the input text.)
   */
  public boolean allowDuplicates()
  {
    return this.allowDuplicates;
  }

  /**
   * Continues generating tokens until a token matches 'regex', assuming the
   * length of the output is between min and maxLength (inclusive).
   */
  public String[] generateUntilZ(String regex, int minLength, int maxLength)
  {
    boolean dbug = false;

    int tries = 0, maxTries = 1000;
    List tokens = new ArrayList();
    OUT: while (++tries < maxTries)
    {

      if (dbug)
        System.out.println("  TRY # " + tries + "--------------------");
      TextNode mn = root.selectChild();
      if (mn == null || mn.token() == null)
        continue OUT;

      tokens.add(mn);
      while (tokens.size() < minLength)
      {

        mn = nextNodeForList(tokens);
        if (mn == null || mn.token() == null)
        { 
          // hit the end
          if (dbug)
            System.out.println("  NULL TOKEN after: " + tokens);
          tokens.clear(); // start over
          continue OUT;
        }
        tokens.add(mn);
      }

      // minLength is ok, look for an ender
      // System.out.println("  GOT MIN-LENGTH: "+tokens);

      String tok = mn.token();
      if (dbug)
        System.out.println("    CHECKING: " + mn);
      if (tok.matches(regex))
      {
        if (dbug)
          System.out.println("    OK (after " + tries + ")\n--------------------");
        break;
      }

      if (tokens.size() > maxLength)
      {
        if (dbug)
          System.out.println("    GIVING UP: " + tokens + "\n--------------------");
        tokens.clear();
        continue;
      }
    }
    return tokensToArray(tokens);
  }

  /**
   * Generates a string of <code>length</code> tokens from the model, stopping
   * when the 'until' token is generated.
   */
  public String[] generateUntil(String until)
  {
    return this.generateUntil(until, 1, Integer.MAX_VALUE);
  }

  /**
   * Generates a string of <code>length</code> tokens from the model, stopping
   * when the 'until' token is generated.
   */
  public String[] generateUntil(String until, int minLength)
  {
    return this.generateUntil(until, minLength, Integer.MAX_VALUE);
  }

  /**
   * Generates a string of
   * 
   * <pre>
   * length
   * </pre>
   * 
   * tokens from the model.
   */
  public String[] generateUntil(String regex, int minLength, int maxLength)
  {
    boolean dbug = false;
    if (dbug)
      System.out.println("RiMarkov.generateUntil(" + regex + "," + minLength + ","
          + maxLength + ")");

    int tries = 0, maxTries = 1000;
    List tokens = new ArrayList();
    OUT: while (++tries < maxTries)
    {

      while (tokens.size() < minLength)
      {
        if (!addNext(tokens))
          continue OUT;
      }

      if (dbug)
        System.out.println("Try#" + tries);// ", got min: '"+untokenize(tokens)+"'");

      while (tokens.size() <= maxLength)
      {

        String check = untokenize(tokens);
        // System.out.println("Checking("+regex+"): "+check);
        if (check.matches(".*" + regex + "$"))
        {
          if (dbug)
            System.out.println("  Hit: " + regex);
          break OUT;
        }
        if (!addNext(tokens))
          continue OUT;
        if (dbug)
          System.out.println("  " + untokenize(tokens));
      }

      tokens.clear();
    }

    // uh-oh, looks like we failed...
    int num = tokens.size();
    if (num < minLength || num > maxLength)
      onGenerationIncomplete(tries, num);

    return tokensToArray(tokens);
  }

  protected boolean addNext(List tokens)
  {
    TextNode mn = nextNodeForList(tokens);
    if (mn == null || mn.token() == null)
    { // hit the end
      // System.out.println("  FAILED: "+tokens);
      tokens.clear(); // start over
      return false;
    }
    tokens.add(mn);
    return true;
  }

  protected String untokenize(List tokens)
  {
    // System.out.println("RiMarkov.untokenize("+tokens+")");
    int i = 0;
    String[] s = new String[tokens.size()];
    for (Iterator it = tokens.iterator(); it.hasNext(); i++)
      s[i] = ((TextNode) it.next()).token();
    return RiTa.untokenize(s);
  }
  
  public String[] generateTokens(int targetNumber)
  {
    int tries = 0, maxTries = 1000;
    List tokens = new ArrayList();
    OUT: while (++tries < maxTries)
    {

      while (tokens.size() < targetNumber)
      {

        TextNode mn = nextNodeForList(tokens);
        if (mn == null || mn.token() == null)
        { // hit the end
          // System.err.println("FAILED: "+tokens);
          tokens.clear(); // start over
          continue OUT;
        }
        tokens.add(mn);
      }

      break; // looks good
    }

    // uh-oh, looks like we failed...
    if (tokens.size() < targetNumber)
      onGenerationIncomplete(tries, tokens.size());

    return tokensToArray(tokens);
  }

  protected String[] tokensToArray(List tokens)
  {
    int i = 0;
    String[] result = new String[tokens.size()];
    for (Iterator it = tokens.iterator(); it.hasNext();)
    {
      TextNode tn = (TextNode) it.next();
      if (tn.token() == null)
        continue;
      result[i++] = tn.token();
    }
    return result;
  }

  protected void onGenerationIncomplete(int tries, int successes)
  {
    if (!RiTa.SILENT)
      System.err.println("\n[WARN] RiMarkov failed to complete after " + tries
	  + " tries\n       Giving up after " + successes + " successful generations...\n");
  }

  // Methods -------------------------------------------------------

  /**
   * Load a text file into the model -- if using Processing, the file should be
   * in the sketch's data folder.
   * @param pApplet
   *          pass 'this' if using Processing, otherwise null should be fine
   * @param fileName
   *          name of file to load
   * @param multiplier
   *          weighting for tokens in the file;<br>
   *          a weight of 3 is equivalent to loading that file 3 times and gives
   *          each token 3x the probability of being chosen during generation.

  public RiMarkov loadFile(Object pApplet, String fileName, int multiplier)
  {
    long done, start = System.currentTimeMillis();

    String contents = RiTa.loadString(pApplet, fileName);

    if (profile)
    {
      done = System.currentTimeMillis() - start;
      if (!RiTa.SILENT)
        System.out.println("[INFO] Loaded '" + fileName + "' (" + contents.length()
            + " chars) in " + done / 1000d + "s");
      start = System.currentTimeMillis();
    }

    this.loadText(contents, multiplier);

    if (profile)
    {
      done = System.currentTimeMillis() - start;
      if (!RiTa.SILENT)
        System.out.println("[INFO] Loaded data into model in " + done / 1000d + "s");
    }
    return this;
  }   */

  public RiMarkov loadText(String text)
  {
    return this.loadText(text, 1);
  }

  public RiMarkov loadText(String text, String regex)
  {
    return this.loadText(text, 1, regex);
  }

  /**
   * Load a String into the model, splitting the text first into sentences, then
   * into words, according to the current regular expression.
   * 
   * @param multiplier
   *          Weighting for tokens in the String <br>
   * 
   *          A weight of 3 is equivalent to loading the text 3 times and gives
   *          each token 3x the probability of being chosen during generation.
   */
  public RiMarkov loadText(String text, int multiplier)
  {
    return this.loadText(text, multiplier, null);
  }

  /**
   * Load a String into the model, splitting the text first into sentences, then
   * into words, according to the supplied regular expression.
   * 
   * @param multiplier
   *          Weighting for tokens in the String <br>
   * 
   *          A weight of 3 is equivalent to loading the text 3 times and gives
   *          each token 3x the probability of being chosen during generation.
   */
  public RiMarkov loadText(String text, int multiplier, String regex)
  {
    if (text == null || text.length() < 1) 
      return this;
    
    this.rawText += text;

    if (sentenceAware)
    {
      loadSentences(text, multiplier, regex);
      
      if (sentenceStarts.size() > 0)
        return this;
      
      if (!RiTa.SILENT)
	System.err.println("[WARN] No sentences found, parsing as tokens");
    }
    
    return loadTokens(RiTa.tokenize(text, regex), multiplier);
  }

  /**
   * Returns the # of tokens (or words) loaded into the model
   */
  public int size()
  {
    return tokenCount;
  }

  /**
   * Loads an array of tokens (or words) into the model; each element in the
   * array must be a single token for proper construction of the model.
   * 
   * @param multiplier
   *          Weighting for tokens in the array <br>
   */
  public RiMarkov loadTokens(String[] tokens, int multiplier)
  {
    if (tokens == null || tokens.length < 1) return this;

    // setAddSpaces(addSpacesInBetween);

    multiplier = Math.max(multiplier, 1);
    
    for (int m = 0; m < multiplier; m++)
    {
      String[] toAdd;
      tokenCount += tokens.length;
      
      for (int k = 0; k < tokens.length; k++)
      {
        toAdd = new String[N];
        for (int j = 0; j < toAdd.length; j++)
        {
          if ((k + j) < tokens.length)
            toAdd[j] = (tokens[k + j] != null) ? tokens[k + j] : null;
          else
            toAdd[j] = null;
        }

        // hack to deal with multiplier...

        addSequence(toAdd);
      }
    }
    return this;
  }

  protected void addSequence(String[] toAdd)
  {
    // System.out.println(Util.asList(toAdd));
    TextNode node = root;
    for (int i = 0; i < toAdd.length; i++)
      if (node.token() != null)
        node = node.addChild(toAdd[i], useSmoothing ? 2 : 1);
  }

  /**
   * Outputs a String representing the models probability tree using the
   * supplied print stream (or System.out).
   * <p>
   * 
   * NOTE: this method will block for potentially long periods of time on large
   * models.
   * 
   * @param printStream
   *          where to send the output (default=System.out)
   * @param sort
   *          whether the tree is first sorted (by frequency) before being
   *          output
   */
  public RiMarkov print(PrintStream printStream, boolean sort)
  {
    printStream.println(root.asTree(sort));
    return this;
  }

  public RiMarkov print(boolean sort)
  {
    return print(System.out, sort);
  }

  public RiMarkov print(PrintStream pw)
  {
    return print(pw, false);
  }

  public RiMarkov print()
  {
    return print(System.out, false);
  }

  /**
   * Returns the TextNode representing the root of the model's tree, so that it
   * can be (manually) navigated.
   */
  public TextNode root()
  {
    return root;
  }

  /**
   * Returns the current n-value for the model
   */
  public int getN()
  {
    return this.N;
  }

  /**
   * Returns whether (add-1) smoothing is enabled for the model
   */
  public boolean useSmoothing()
  {
    return this.useSmoothing;
  }

  /**
   * Toggles whether (add-1) smoothing is enabled for the model. Should be
   * called before any data loading is done.
   */
  public RiMarkov useSmoothing(boolean useSmoothing)
  {
    if (this.root.hasChildren())
      throw new RiTaException("Invalid state: setUseSmoothing() "
          + "must be called before any data is added to the model");
    this.useSmoothing = useSmoothing;
    return this;
  }

  protected String nextToken(String[] tokens)
  {
    TextNode node = this.nextNodeForArr(tokens);
    return node == null ? null : node.token();
  }

  protected TextNode nextNodeForList(List previousTokens)
  {
    // Follow the seed path down the tree
    int firstLookupIdx = Math.max(0, previousTokens.size() - (N - 1));
    TextNode tn = (previousTokens.size() < 1) ? root.selectChild()
        : (TextNode) previousTokens.get(firstLookupIdx++);

    TextNode node = root.lookup(tn);
    for (int i = firstLookupIdx; i < previousTokens.size(); i++)
    {
      if (node != null)
        node = node.lookup((TextNode) previousTokens.get(i));
    }

    // Now select the next node
    TextNode result = selectChild(node, true);
    return result;
  }

  protected TextNode nextNodeForArr(String[] seed)
  {
    // Follow the seed path down the tree
    int firstLookupIdx = Math.max(0, seed.length - (N - 1));
    TextNode node = root.lookup(seed[firstLookupIdx++]);
    for (int i = firstLookupIdx; i < seed.length; i++)
    {
      if (node != null)
        node = node.lookup(seed[i]);
    }
    // Now select the next node
    TextNode result = selectChild(node, true);
    return result;
  }

  /**
   * Returns all possible next words (or tokens), ordered by probability, for
   * the given seed array, or null if none are found.
   * <p>
   * Note: seed arrays of any size (>0) may be input, but only the last n-1
   * elements will be considered.
   */
  public String[] getCompletions(String[] seed)
  {
    if (seed == null || seed.length == 0)
    {
      System.out.println("[WARN] Null (or zero-length) seed passed to getCompletions()");
      return EMPTY;
    }

    int firstLookupIdx = Math.max(0, seed.length - (N - 1));
    TextNode node = root.lookup(seed[firstLookupIdx++]);
    for (int i = firstLookupIdx; i < seed.length; i++)
    {
      if (node == null)
        return EMPTY;
      node = node.lookup(seed[i]);
    }

    if (node == null)
      return EMPTY;

    Collection c = node.childMap().values();
    if (c == null || c.size() < 1)
      return EMPTY;
    TextNode[] nodes = new TextNode[c.size()];
    nodes = (TextNode[]) c.toArray(nodes);
    Arrays.sort(nodes);

    String[] result = new String[nodes.length];
    for (int i = 0; i < result.length; i++)
      result[i] = nodes[i].token();
    return result;
  }

  protected TextNode selectChild(TextNode tn, boolean useProb)
  {
    return (tn == null) ? null : tn.selectChild(useProb);
  }

  /**
   * Returns true if the model contains the token in any position, else false.
   */
  public boolean containsChar(String token)
  {
    return root.lookup(token) != null;
  }

  /**
   * Returns the raw (unigram) probability for a token in the model, or 0 if it
   * does not exist
   */
  public float getProbability(String token)
  {
    if (root == null)
      throw new RiTaException("Model not initialized: root is null!");
    TextNode tn = root.lookup(token);
    if (tn == null)
      return 0;
    else
      return tn.probability();
  }

  /**
   * Returns the probability of obtaining a sequence of k character tokens were
   * k <= nFactor, e.g., if nFactor = 3, then valid lengths for the String
   * <code>tokens</code> are 1, 2 & 3.
   */
  public float getProbability(String[] tokens)
  {
    TextNode tn = findNode(tokens);
    if (tn == null)
      return 0;
    else
      return tn.probability();
  }

  /**
   * Returns an unordered list of possible words <i>w</i> that complete an
   * n-gram consisting of: pre[0]...pre[k], <i>w</i>, post[k+1]...post[n]. As an
   * example, the following call:
   * 
   * <pre>
   * getCompletions(new String[] { &quot;the&quot; }, new String[] { &quot;ball&quot; })
   * </pre>
   * 
   * will return all the single words that occur between 'the' and 'ball' in the
   * current model (assuming n > 2), e.g., ['red', 'big', 'bouncy']).
   * <p>
   * Note: For this operation to be valid, (pre.length + post.length) must be
   * strictly less than the model's nFactor, otherwise an exception will be
   * thrown.
   */
  public String[] getCompletions(String[] pre, String[] post)
  {
    if (pre == null || pre.length >= N)
      throw new RiTaException("Invalid pre array: " + RiTa.asList(pre));

    int postLen = post == null ? 0 : post.length;
    if (pre.length + postLen > N)
    {
      throw new RiTaException("Sum of pre.length" + " && post.length must be < N, was "
          + (pre.length + postLen));
    }

    TextNode tn = findNode(pre);
    if (tn == null)
      return null;

    List result = new ArrayList();
    Collection nexts = tn.childNodes();
    for (Iterator it = nexts.iterator(); it.hasNext();)
    {
      TextNode node = (TextNode) it.next();
      String[] test = appendToken(pre, node.token());
      if (test == null)
        continue;
      for (int i = 0; i < postLen; i++)
        test = appendToken(test, post[i]);
      if (findNode(test) != null)
        result.add(node.token());
    }
    return RiTa.strArr(result);
  }

  /**
   * Returns the full set of possible next tokens (as a HashMap: String -> Float
   * (probability)) given an array of tokens representing the path down the tree
   * (with length less than n). If the input array length is not less than n, or
   * the path cannot be found, or the endnode has no children, null is returned.
   * <p>
   * 
   * Note: As the returned Map represents the full set of possible next tokens,
   * the sum of its probabilities will always be equal 1.
   * 
   * @see #getProbability(String)
   */
  public Map getProbabilities(String[] path)
  {
    Map probs = new HashMap();

    if (path.length == 0 || path.length >= N)
      return null;

    TextNode tn = findNode(path);
    if (tn == null)
      return EMPTY_MAP;

    Collection nexts = tn.childNodes();
    for (Iterator iter = nexts.iterator(); iter.hasNext();)
    {
      TextNode node = (TextNode) iter.next();
      if (node != null)
      {
        String tok = node.token();
        float prob = getProbability(appendToken(path, tok));
        probs.put(tok, new Float(prob));
      }
    }
    return probs;
  }

  static String[] appendToken(String[] path, String token)
  {
    String[] fullPath = new String[path.length + 1];
    System.arraycopy(path, 0, fullPath, 0, path.length);
    fullPath[fullPath.length - 1] = token;
    return fullPath;
  }

  /**
   * Traverses the tree and returns the node at the end of <code>path</code>, or
   * null if the full path does not exist
   */
  protected TextNode findNode(String[] path)
  {
    // System.out.print("RiMarkov.findNode("+Util.asList(path)+")");
    if (path == null || path.length < 1)
      return null;
    TextNode[] nodes = nodesOnPath(path);
    TextNode tf = nodes != null ? nodes[nodes.length - 1] : null;
    // System.out.println(" :: "+tf);
    return tf;
  }

  /**
   * Traverses the tree and returns the node at the end of <code>path</code>, or
   * null if the full path does not exist. Will match on the last token, plus a
   * sentence end if <code>allowSentenceEnds</code> is true;
   */
  // protected TextNodeIF findNode(String[] path, boolean allowSentenceEnds)

  /**
   * Return the nodes on the <code>path</code> or null if the full path does not
   * exist
   */
  protected TextNode[] nodesOnPath(String[] path)
  {
    int numNodes = Math.min(path.length, N - 1);
    int firstLookupIdx = Math.max(0, path.length - (N - 1));
    TextNode node = (TextNode) root.lookup(path[firstLookupIdx++]);
    if (node == null)
      return null;

    int idx = 0; // found at least one good node
    TextNode[] nodes = new TextNode[numNodes];
    nodes[idx++] = node;
    for (int i = firstLookupIdx; i < path.length; i++)
    {
      node = node.lookup(path[i]);
      if (node == null)
        return null;
      nodes[idx++] = node;
    }
    return nodes;
  }

  protected boolean validSentenceStart(String word)// , String previousWord)
  {
    return (!sentenceAware || word.matches(SS_REGEX));
  }

  protected String clean(String sentence)
  {
    if (removeQuotations())
    {
      sentence = sentence.replaceAll("[\"��]", "");
      sentence = sentence.replaceAll("['`��] ", "");
      sentence = sentence.replaceAll(" ['`��]", "");
    }
    sentence = sentence.replaceAll("\\s+", " ");
    return sentence.trim();
  }

  /**
   * Loads an array of sentences into the model; each element in the array must
   * be a single sentence for proper parsing.
   */
  protected RiMarkov loadSentences(String text, int multiplier)
  {
    return this.loadSentences(text, multiplier, null);
  }

  /**
   * Loads an array of sentences into the model; each element in the array must
   * be a single sentence for proper parsing. After sentence splitting, the
   * input is tokenized into words using the specified regex.
   */
  protected RiMarkov loadSentences(String text, int multiplier, String regex)
  {
    String[] sentences = RiTa.splitSentences(text);
    
    //System.out.println("RiMarkov.loadSentences("+sentences.length+")");
    multiplier = Math.max(multiplier, 1); 
    
    List allWords = new ArrayList();

    if (sentenceStarts == null)
      sentenceStarts = new ArrayList();

    // do the cleaning/splitting first ---------------------
    for (int i = 0; i < sentences.length; i++)
    {
//      if (!allowDuplicates)
//      {
//        if (sentenceList == null)
//          sentenceList = new HashSet();
//        sentenceList.add(sentence);
//      }

      String[] tokens = RiTa.tokenize(clean(sentences[i]), regex);
      tokenCount += tokens.length;
      if (!validSentenceStart(tokens[0]))
      {
        if (printIgnoredText)
          System.out.println("[WARN] Skipping (bad sentence start): "
              + RiTa.asList(tokens));
        continue;
      }

      allWords.add(SS_DELIM + tokens[0]); // awful hack for sentences starts

      int j = 1;
      for (; j < tokens.length; j++)
        allWords.add(tokens[j]);
    }

    // ------------------------------------------------

    String[] words, toAdd;
    wordsPerFile += allWords.size();
    words = (String[]) allWords.toArray(new String[allWords.size()]);
    for (int i = 0; i < words.length; i++)
    {
      toAdd = new String[N]; // use arraycopy?
      for (int j = 0; j < N; j++)
      {
        if ((i + j) < words.length)
          toAdd[j] = words[i + j];
      }

      // hack to deal with multiplier...
      for (int j = 0; j < multiplier; j++)
        addSentenceSequence(toAdd);
    }

    // System.out.println("Starts: "+sentenceStarts);
    // System.out.println("[INFO] Processing complete: "+wordsPerFile+" words.");

    return this;
  }

  protected TextNode getSentenceStart()
  {
    if (!this.sentenceAware) {
      throw new RiTaException("getSentenceStart() can only "
        + "be called when the model is sentence-aware...");
    }
    
    if (sentenceStarts == null || sentenceStarts.size() < 1)
      throw new RiTaException("No sentence starts found! genSen=" + sentenceAware);

    int idx = (int) (RiTa.random() * sentenceStarts.size());
    String txt = (String) sentenceStarts.get(idx);
    
    return root.lookup(txt);
  }

  public String generate()
  {
    return this.generateSentence();
  }

  /**
   * Generates a sentence from the model.
   * <p>
   * Note: multiple sentences generated by this method WILL NOT follow the model
   * across sentence boundaries; thus the following two calls are NOT
   * equivalent:
   * 
   * <pre>
   *      String[] results = markov.generateSentences(10);
   *                and
   *      for (int i = 0; i < 10; i++)
   *        results[i] = markov.generateSentence();
   * </pre>
   * 
   * The latter will create 10 sentences with no explicit relationship between
   * one and the next; while the former will follow probabilities from one
   * sentence (across a boundary) to the next.
   */
  public String generateSentence()
  {
    return generateSentences(1)[0];
  }

  /**
   * Generates some # (one or more) of sentences from the model.
   * <P>
   * Note: multiple sentences generated by this method WILL follow the model
   * across sentence boundaries; thus the following two calls are NOT
   * equivalent:
   * 
   * <pre>
   *      String[] results = markov.generateSentences(10);
   *                and
   *      for (int i = 0; i < 10; i++)
   *        results[i] = markov.generateSentence();
   * </pre>
   * 
   * The latter will create 10 sentences with no explicit relationship between
   * one and the next; while the former will follow probabilities from one
   * sentence (across a boundary) to the next.
   */
  public String[] generateSentences(int numSentences)
  {
    if (!this.sentenceAware) {
      throw new RiTaException("generateSentences() can only be called"
        + " when the model is sentence-aware, otherwise use generateTokens()");
    }
    
    Set<String> result = new LinkedHashSet<String>();
    int totalTries = 0, wordsInSentence = 1, tries = 0;
    StringBuilder s = new StringBuilder(32);
    TextNode mn = firstToken(s);

    while (result.size() < numSentences)
    {
      if (wordsInSentence >= maxSentenceLength)
      { 
        if (printIgnoredText && !RiTa.SILENT)
          System.out.println("RiMarkov.generateSentences().rejected:: too long!");
        
        mn = firstToken(s);
        wordsInSentence = 1;
      }

      if (mn.isLeaf()) { 
        mn = tracePathFromRoot(mn);
        continue;
      }
      
      mn = nextNodeForNode(mn);
      
      if (mn.isSentenceStart())
      {
        if (wordsInSentence >= minSentenceLength)
        {
          String candidate = RiTa.untokenize(s.toString().split(SP)); 
          
          if (validateSentence(candidate))
          {
            // got one, store and reset the counters
            result.add(candidate);
            totalTries += tries;
            tries = 0;
          }
        }
        mn = firstToken(s);
        wordsInSentence = 1;
        continue;  // DCH added
      }
      
      // add the next word
      wordsInSentence++;
      s.append(mn.token() + SP);

      // check if its time to give up
      if (++tries >= MAX_GENERATION_ATTEMPTS)
      {
        onGenerationIncomplete(totalTries += tries, result.size());
        break; // give-up
      }
    }

    return RiTa.strArr(result);
  }

  protected TextNode firstToken(StringBuilder s)
  {
    s.delete(0, s.length());
    TextNode tmp = getSentenceStart();
    s.append(tmp.token() + SP);
    return tmp;
  }

  protected boolean validateSentence(String sent)
  {
    String[] tokens = RiTa.tokenize(sent);
    String first = tokens[0], last = tokens[tokens.length - 1];

    if (!first.matches("[A-Z]\\S*"))
    {
      if (printIgnoredText && !RiTa.SILENT)
        System.err.println("[INFO] Skipping: bad first char in '" + sent + "'");
      return false;
    }
    
    if (!RiTa.SILENT && !last.matches("[!?.]"))
    {
      System.out.println("[WARN] Bad last token: '" + last + "' in:\n  " + sent);
      return false;
    }
    
    //System.out.println(sent+": "+allowDuplicates+" "+this.rawText.indexOf(sent));
    
    if (!allowDuplicates &&  rawText.indexOf(sent) > -1) {

      if (++skippedDups >= MAX_GENERATION_ATTEMPTS)
      {
	// TODO: NEVER CALLED, add warning here?
        allowDuplicates = true;
      }
      
      if (printIgnoredText && !RiTa.SILENT)
        System.err.println("[WARN] Skipping input duplicate: " + sent);
      
      return false;
    }
    
    return true;
  }

  /*
   * Expects a regex with one capturing group and (repeatedly) removes the group
   * until there is no match

  protected String removeGroup(String sent, Pattern pat)
  {
    Matcher m = pat.matcher(sent);
    while (m.matches())
    {
      int x = m.start(1), y = m.end(1);
      sent = sent.substring(0, x) + sent.substring(y);
      m = pat.matcher(sent);
    }
    return sent;
  }   */

  /**
   * Chooses the next node (probabalistically) from the model.
   * 
   * @param current
   *          - Node that is the parent of the returned node
   */
  protected TextNode nextNodeOrig(TextNode current)
  {
    double selector = 0, pTotal = 0;
    Collection nodes = current.childNodes();
    while (true)
    {
      pTotal = 0;
      selector = RiTa.random();
      for (Iterator it = nodes.iterator(); it.hasNext();)
      {
        TextNode child = (TextNode) it.next();
        pTotal += child.probability();
        if (current.isRoot() && (sentenceAware && !child.isSentenceStart()))
          continue;
        if (selector < pTotal)
          return child;
      }
      throw new RuntimeException // should never happen
      ("PROB. MISS" + current + " total=" + pTotal + " selector=" + selector);
    }
  }

  protected TextNode nextNodeForNode(TextNode current)
  {
    int attempts = 0;
    double selector, pTotal = 0;
    Collection nodes = current.childNodes();
    
    while (true)
    {
      pTotal = 0;
      selector = RiTa.random();
      
      // System.out.println("current="+current+", selector="+selector);
      for (Iterator it = nodes.iterator(); it.hasNext();)
      {
        TextNode child = (TextNode) it.next();
        // System.out.println("child="+child);
        pTotal += child.probability();
        // System.out.println("pTotal="+pTotal);
        
        if (current.isRoot() && (sentenceAware && !child.isSentenceStart()))
        {
          // System.out.println("continuing...");
          continue;
        }
        if (selector < pTotal)
        {
          // System.out.println("returning "+child+"\n====================");
          return child;
        }
        // System.out.println("selector >= pTotal\n====================");
      }
      attempts++;
      
      System.err.println("[WARN] Prob. miss (#" + attempts + ") in RiMarkov.nextNode()."
          + " Make sure there are a sufficient\n       # of sentences"
          + " in the model that are longer than your minSentenceLength.");
      
      if (attempts == MAX_PROB_MISSES)
        throw new RuntimeException // should never happen
        ("PROB. MISS" + current + " total=" + pTotal + " selector=" + selector);
    }
  }

  //protected static Pattern SENTENCE_ENDING_GAP, PUNCTUATION_GAP;

  protected TextNode tracePathFromRoot(TextNode node)
  {
    if (pathTrace == null)
      pathTrace = new Stack();

    // (TODO: change this
    node.pathFromRoot(pathTrace);

    pathTrace.pop(); // ignore the first element
    TextNode mn = root;
    while (!pathTrace.isEmpty())
    {
      String search = (String) pathTrace.pop();
      mn = mn.lookup(search);
    }
    return mn;
  }

  protected void addSentenceSequence(String[] toAdd)
  {
    // System.out.println(Util.asList(toAdd));
    
    TextNode node = root;
    for (int i = 0; i < toAdd.length; i++)
    {
      if (toAdd[i] == null)
        continue;
      
      // System.out.println("  "+i+") "+toAdd[i]);
      
      if (node.token() != null)
      {
        String add = toAdd[i];
        if (add.startsWith(SS_DELIM))
        {
          add = add.substring(SS_DELIM.length()); // awful (use-RiString)
          TextNode parent = node;
          node = node.addChild(add, useSmoothing ? 2 : 1);
          node.isSentenceStart(true);
          if (parent.isRoot())
          {
            sentenceStarts.add(node.token());
            // System.out.println("adding Starter: "+node.getToken()+ " "+toAdd[i+1]);
          }
        }
        else
          node = node.addChild(add, useSmoothing ? 2 : 1);
      }
    }
  }

  /**
   * Returns whether the model will attempt to recognize (english-like)
   * sentences in the input text (default=true).
   */
  public boolean sentenceAware()
  {
    return this.sentenceAware;
  }

  /**
   * Sets whether the model will try to recognize (english-like) sentences in
   * its input (default=true).
   * 
   * public RiMarkov recognizeSentences(boolean recognizeSentences) { if
   * (tokenCount > 0 && recognizeSentences != this.recognizeSentences) throw new
   * RiTaException("recognizeSentences must be set *before* loading any text.");
   * this.recognizeSentences = recognizeSentences; return this; }
   */
  public boolean isIgnoringCase()
  {
    return this.ignoreCase;
  }
  
  public boolean ready()
  {
    return this.size() > 0;
  }

  public RiMarkov loadTokens(String[] tokens)
  {
    return loadTokens(tokens, 1);
  }

  public RiMarkov loadTokens(char[] tokens)
  {
    String[] s = new String[tokens.length];
    for (int i = 0; i < tokens.length; i++)
      s[i] = Character.toString(tokens[i]);
    return loadTokens(s, 1);
  }

  /** Tells the model whether to ignore various quotations types in the input */
  public void removeQuotations(boolean b)
  {
    this.removeQuotations = b;
  }

  /**
   * Tells whether the model is ignoring quotations found in the input (default=true)
   */
  public boolean removeQuotations()
  {
    return removeQuotations;
  }
  
  static String loadString(String fileName)
  {
    return RiTa.loadString(fileName, "RiMarkov.loadFrom");
  }
  
  public static void main(String[] args)
  {
    String sample = "One reason people lie is to achieve personal power. Achieving personal power is helpful for one who pretends to be more confident than he really is. For example, one of my friends threw a party at his house last month. He asked me to come to his party and bring a date. However, I did not have a girlfriend. One of my other friends, who had a date to go to the party with, asked me about my date. I did not want to be embarrassed, so I claimed that I had a lot of work to do. I said I could easily find a date even better than his if I wanted to. I also told him that his date was ugly. I achieved power to help me feel confident; however, I embarrassed my friend and his date. Although this lie helped me at the time, since then it has made me look down on myself.";
    String[] tokens = RiTa.tokenize(sample);
    System.out.println(new RiMarkov(3,false).loadTokens(tokens).size());
    System.out.println(new RiMarkov(3,true).loadTokens(tokens).size());
  }
  
}// end

