package rita.support;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.RiTa;
import rita.RiTaException;

/**
 * A node in a graph containing text and some # of child TextNodes  
 */
public class TextNode implements Comparable, RiProbable
{
  static DecimalFormat formatter = new DecimalFormat(".###");
  
  /** the # of tokens processed */
  public static int totalTokens;
  
  private int count=0;  
  protected Map children;
  protected TextNode parent;
  protected String token, lookup;
  protected boolean ignoreCase, isSentenceStart;    
  
  public static TextNode createRoot() 
  {   
    return createRoot(false);
  }
  
  public static TextNode createRoot(boolean ignoreCase) 
  {    
    TextNode tn =  new TextNode(null, null);
    tn.ignoreCase = ignoreCase;
    return tn;
  }  

  TextNode(TextNode parent, String token) 
  {    
    this.token = token;    
    this.parent = parent;  
    
    if (parent != null)
      this.ignoreCase = parent.ignoreCase;
    
    if (ignoreCase && token != null) {
      String lc = token.toLowerCase();
      if (!lc.equals(token))
        lookup = lc;
    }
//System.out.println("NEW: "+this);    
  }

  public TextNode addChild(String newToken) 
  {      
    return this.addChild(newToken, 1);
  } 
    
  public TextNode addChild(String newToken, int initialCount) 
  { 
    // create the child map for this Node
    if (children == null) 
      children = new HashMap();     
    
    TextNode node = lookup(newToken);

    //  add first instance of this token 
    if (node == null) {
      String key = getLookupKey(newToken);
      children.put(key, node = new TextNode(this, newToken));  
      node.count = initialCount;
    }
    else {         
      node.increment(); // up the frequency
    }
    
    return node;
  }

  public String token()
  {
    return isRoot() ? "ROOT" : ((ignoreCase && token != null) ? token.toLowerCase() : token);
  }

  public int count()
  {
    return count;
  }

  public int increment()
  {
    totalTokens++;
    count = count + 1;
    return count;
  }

  public String toString()
  {
    String result = "["+token();
    if (!isRoot()) { 
      result += " (" + count + "," + 
        formatter.format(probability()) + "%)"; 
    }
    return result +"]";    
  }

  public boolean isRoot()
  {
    return parent == null;
  }
  
  
  public int depth()
  { 
    int count = 0;
    TextNode mn = this;
    while (true) {
      if (mn.isRoot()) return count;
      mn = (TextNode)mn.parent;
      count++;
    }   
  }
  
  /*
   * Includes this node, but not the root

  public String[] pathToRoot()
  {      
    Stack s = new Stack();
    pathFromRoot(s);
    List l = new ArrayList();
    l.push(getRoot());
    while (!s.isEmpty()) 
      l.add(s.pop());
    return (String[]) l.toArray(new String[l.size()]);
  }   */
  
  
  /**
   * Returns the intermediate tokens along the path, not including the root, nor this node
   */
  public String[] pathToRoot()
  { 
    List result = new ArrayList();
    pathToRoot(result);
    return RiTa.strArr(result);
  }
  
  /**
   * Returns the intermediate tokens along the path, not including the root, nor this node
   */
  public String[] pathFromRoot()
  { 
    List result = new ArrayList();
    pathToRoot(result);
    Collections.reverse(result);
    return RiTa.strArr(result);
  }
  
  private void pathToRoot(List l)
  { 
    if (this.isRoot()) return;
    
    TextNode mn = this;
    while (true) {
      mn = (TextNode)mn.parent;
      if (mn.isRoot()) break;
      l.add(mn.token());  
                
    }
  }
  

  public TextNode[] leaves() {
    List<TextNode>l = new ArrayList<TextNode>();
    findLeaves(this, l);
    return l.toArray(new TextNode[l.size()]);
  }

  void findLeaves(TextNode mn, List<TextNode> l) {
    //System.out.println("TextNode.findLeaves("+mn.token+")");
    if (mn.isLeaf()) {
      l.add(mn);
      return;
    }
    
    for (Iterator<TextNode> it = mn.childIterator(); it.hasNext();)
    {
      TextNode node = it.next();
      findLeaves(node, l);
    }
  }

  
  /**
   * (TODO: unintuitive: change/remove this -- use above)
   * Note: includes this node, but not the root
   */  
  public void pathFromRoot(Stack result)
  {       
    TextNode mn = this;
    if (result == null)
      result = new Stack();
    
    while (true) {
      if (mn.isRoot()) break;
      result.push(mn.token());      
      mn = (TextNode)mn.parent;          
    }   
  }
  
  public TextNode getRoot()
  {       
    TextNode mn = this;
    while (true) {
      if (mn.isRoot()) return mn;
      mn = (TextNode)mn.parent;          
    }   
  }

  public int uniqueCount()
  {
    if (children == null)
      return 0;
    return children.size();
  }

  //   total count for all children at this level    
  int siblingCount()
  {
    if (isRoot()) {
      System.err.println("WARN: Sibling count on ROOT!");
      return 1;
    }
    
    if (parent == null)
      throw new RuntimeException("Null parent for: "+token);
    
    int sum = 0;
    for (Iterator i = parent.childIterator(); i.hasNext();) {
      TextNode node = (TextNode)i.next();
      if (node != null) sum += node.count();
    } 
    return sum;
  }

  public float probability() {
    return count/(float)siblingCount();
  }
  
  public Iterator childIterator()
  {
    if (children == null)
      children = new HashMap();
    return children.values().iterator();
  }

  /**
   * Returns the # of children for the node
   */
  public int size()
  {
    return (children==null) ? 0 : children.size();
  }

  public int compareTo(Object o)
  {
    float nf1 = count();
    float nf2 = ((TextNode) o).count();
    if (nf1 == nf2) // return lex-order on ties (?)
      return token.compareTo(((TextNode) o).token());
    return nf1 < nf2 ? 1 : -1;
  }

  public TextNode lookup(String tokenToLookup)
  {
    if (children == null || tokenToLookup == null || tokenToLookup.length()<1) 
      return null;    
    String key = getLookupKey(tokenToLookup);    
    return (TextNode)children.get(key);
  }

  public TextNode lookup(TextNode tokenToLookup)
  {   
    if (tokenToLookup == null) return null;
    return (TextNode)children.get(getLookupKey(tokenToLookup.token()));    
  }
  
  String getLookupKey(String key)
  {
    return (ignoreCase && key != null) ? key.toLowerCase() : key; 
  }

  public int longestPathLength() {
    
    if (this.isLeaf()) return 0;
    
    int longest = 0;
    Iterator it = childIterator();
    while (it.hasNext())
    {
      TextNode node = (TextNode) it.next();
      int lp = node.longestPathLength();
      if (lp > longest)
        longest = lp;
    }
    
    return 1 + longest;
  }
  
  public TextNode[] longestPath() {
    List l = new ArrayList();
    longestPath(l);
    return (TextNode[]) l.toArray(new TextNode[l.size()]);
  }
  
  public void longestPath(List l) {
    
    if (!this.isRoot())
      l.add(this);
    
    if (this.isLeaf()) return;
    
    int longest = -1;
    TextNode longestNode = null;
    Iterator it = childIterator();
    
    while (it.hasNext())
    {
      TextNode node = (TextNode) it.next();
      int lp = node.longestPathLength();
      if (lp > longest) {
        longest = lp;
        longestNode = node;
      }
    }
    
    List tmp = new ArrayList();
    longestNode.longestPath(tmp);
    //System.out.println(token+".add("+tmp+")");
    
    for (it = tmp.iterator(); it.hasNext();)
      l.add(it.next());
  }
  
  
  public Collection childNodes()
  {
    if (children == null) return null;
    return children.values();
  }
  
  private String childrenToString
    (TextNode mn, String str, int depth, boolean sort) 
  {
    List l = new ArrayList(mn.children.values());
    
    if (l.size() < 1) return str;
    
    if (sort) Collections.sort(l);
 
    String indent = "\n";
    for (int j = 0; j < depth; j++) 
      indent += "  ";
    
    TextNode node = null;
    Iterator i =  l.iterator();
    while (i.hasNext()) 
    {
      node = (TextNode)i.next();
      
      if (node == null) break;
      
      String tok = node.token();      
      if (tok != null) {         
        if (tok.equals("\n"))
          tok = "\\n";
        else if (tok.equals("\r"))
          tok = "\\r";
        else if (tok.equals("\t"))
          tok = "\\t";
        else if (tok.equals("\r\n"))
          tok = "\\r\\n";
      }
      
      str += indent +"'"+tok+"'";
      
      if (node.count == 0) 
        throw new Error("ILLEGAL FREQ: "+node.count+" -> "+mn.token+","+node.token);
      
      if (!node.isRoot())
        str += " ["+node.count + ",p=" +formatter.format(node.probability()) + "]->{";
      
      //if (node.isSentenceStart) str += "[START]";
      
      if (node.size() > 0) // recursive call
        str = childrenToString(node, str, depth+1, sort);
      
      else 
        str += "}";
    }
    
    indent = "\n";
    for (int j = 0; j < depth-1; j++) 
      indent += "  ";
    
    return str + indent + "}";
  }
  
  public String asTree() 
  {
    return asTree(true);
  }
  
  public String asTree(boolean sort) 
  {
    String s = token()+" ";
    if (!isRoot()) 
      s+= "("+count+")->"; 
    s += "{";
    if (!isLeaf())
      return childrenToString(this, s, 1, sort);
    return s + "}";
  }
  
  public boolean isLeaf()
  {    
    return children == null || children.size() == 0;
  }
  
  public void ignoresCase(boolean b) {
    if (!isRoot()) throw new RiTaException
      ("Illegal to set the ignore-case flag on any Node but the root");
    this.ignoreCase = true;    
  }
  

  public boolean ignoresCase()
  {
    return ignoreCase;
  }
  

  public TextNode selectChild() 
  {
    return this.selectChild(true);
  }

  public TextNode selectChild(boolean probabalisticSelect) 
  {
    return this.selectChild((String)null, probabalisticSelect);
  }
  
  public TextNode selectChild(String regex, boolean probabalisticSelect) {
    if (children == null) return null;
    Collection c = (regex != null) ? childNodes(regex) : children.values();
    return selectChild(c, probabalisticSelect);
  }
  
  protected TextNode selectChild(Collection c, boolean probabalisticSelect) 
  {
    return (TextNode)select(c, probabalisticSelect);
  }

  public Collection childNodes(String regex)
  {
    Matcher m = null;
    List tmp = null;
    if (children == null || children.size()==0)
      return null;
    Pattern p = Pattern.compile(regex);    
    for (Iterator i = childIterator(); i.hasNext();)
    {
      TextNode tn = (TextNode) i.next();
      m = p.matcher(tn.token());
      if (m.matches()) {
        if (tmp == null) 
          tmp = new LinkedList();
        tmp.add(tn);    
      }
    }
    return tmp;
  }    

  public boolean hasChildren(String regex)
  {
    return childNodes(regex).size() > 0;
  }

  public boolean hasChildren()
  {
    return children != null && children.size() > 0;
  }

  public Map childMap()
  {
    return children;
  }

  public boolean isSentenceStart()
  {
    return this.isSentenceStart;
  }

  public void isSentenceStart(boolean isSentenceStart)
  {
    this.isSentenceStart = isSentenceStart;
  }

  /**
   *  To satisfy the RiProbable interface; simply returns the count here
   */
  public float rawValue()
  {
    return count();
  }

  public static RiProbable select(Collection c, boolean probabalisticSelect) {
    
    return (RiProbable) (probabalisticSelect ? probabalisticSelect(c) : RiTa.randomItem(c));
  }
  
  public static RiProbable probabalisticSelect(Collection c)
  {    
    switch (c.size()) 
    {
      case  0:  return null;
      
      case  1:  return (RiProbable)c.iterator().next();
      
      default: // pick from multiple children
      {
        // select based on frequency
        double pTotal = 0, selector = Math.random();
        for (Iterator iter = c.iterator(); iter.hasNext();)
        {
          RiProbable pr = (RiProbable) iter.next();
          pTotal += pr.probability();
          if (selector < pTotal)           
            return pr;
        }
      }
      throw new RiTaException("Invalid State in RiTa.probabalisticSelect()");
    }   
  }

  
  public static void main(String[] args)
  {
/*  TextNode rt = createRoot(true);
    TextNode i = rt.addChild("I");
    TextNode j = rt.addChild("J");
    System.out.println(rt.asTree(true)); 
    System.out.println(rt.lookup(j));
    System.out.println(rt.lookup("J"))*/;
    
    
    TextNode root = createRoot();
    TextNode i = root.addChild("I");
    TextNode i2 = root.addChild("I");
    TextNode j = root.addChild("J");
    System.out.println(i2.probability() == 2/3f);
    System.out.println(j.probability()  == 1/3f);
    System.out.println(i.probability()  == 2/3f);
    TextNode k = j.addChild("K");
    TextNode l = k.addChild("L");
    System.out.println(root.asTree());
    System.out.println(RiTa.asList(k.pathFromRoot()));
    System.out.println(RiTa.asList(l.pathToRoot()));
    System.out.println(RiTa.asList(l.pathFromRoot()));
    //System.out.println(root.longestPathLength());
    System.out.println(RiTa.asList(root.longestPath()));
    System.out.println(l.depth());
    System.out.println(RiTa.asList(root.leaves()));
  }

}// end
