package rita.wordnet.test;

import java.io.FileNotFoundException;

import rita.RiTa;
import rita.RiWordnet;
import rita.wordnet.WordnetUtil;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.data.*;
import rita.wordnet.jwnl.data.relationship.RelationshipFinder;

public class AFewRandomTests
{
  /** @invisible */
  public static void mainXC(String[] args) throws Exception
  {
    System.err.println("[INFO] RiWordnet.main...");
    String[] s = null;
    RiWordnet wl = new RiWordnet();
    s = wl.getSynset("dog", "n");
    if (s == null)
      System.out.println("[]");
    else
      for (int j = 0; j < s.length; j++)
        System.out.println(s[j]);
    if (1 == 1)
      return;
    // String[] s = null;
    // RiWordnet wl = new RiWordnet(null);
    wl = new RiWordnet("c:\\WordNet-3.0");
    wl.ignoreCompoundWords(false);
    s = wl.getSynset("nonce", "n", true);
    if (s == null)
      System.out.println("[]");
    for (int j = 0; s != null && j < s.length; j++)
      System.out.println(s[j]);
    System.out.println(wl.getRandomWord("n"));

    if (1 == 1)
      System.exit(1);

    int[] ids = wl.getSenseIds("os", "n");
    for (int i = 0; i < ids.length; i++)
    {
      System.out.print(ids[i] + ") ");
      s = wl.getSynset(ids[i]);
      if (s == null)
        System.out.print("[]");
      for (int j = 0; s != null && j < s.length; j++)
        System.out.print(s[j] + " ");
      System.out.println();
    }

    ;

    System.out.println(wl.getPosStr("walk"));
    System.out.println(wl.getSenseCount("walk", "v"));
    // System.out.println(wl.getHyponyms("cat","n"));
    // s = wl.getExamples("n", 10);

    for (int j = 0; j < s.length; j++)
      System.out.println(s[j]);
    for (int i = 0; i < RiWordnet.ALL_FILTERS.length; i++)
    {
      s = wl.filter(RiWordnet.ALL_FILTERS[i], "cat", POS.NOUN);
      for (int j = 0; j < s.length; j++)
        System.out.println(s[j]);
      System.out.println("------------------------");
    }

    ids = wl.getSenseIds("cat", "n");
    for (int j = 0; j < ids.length; j++)
    {
      s = wl.getHyponyms(ids[j]);
      System.err.print(ids[j] + ": " + wl.getDescription(ids[j]));
      System.err.print(" (hyponyms: ");
      for (int i = 0; s != null && i < s.length; i++)
        System.err.print(s[i] + " ");
      System.err.println(")\n");
    }
    System.err.println("-----------------------------------");
    s = wl.getHyponyms("cat", "n");
    System.err.println("First Sense: " + WordnetUtil.asList(s));

    System.err.println("-----------------------------------");
    s = wl.getAllHyponyms("cat", "n");
    System.err.println("\nAll Senses: " + WordnetUtil.asList(s));

    System.err.println("-----------------------------------");
    for (int j = 0; j < ids.length; j++)
    {
      s = wl.getHyponymTree(ids[j]);
      if (s == null)
        continue;
      System.err.println("\nTree: " + WordnetUtil.asList(s));
    }

    wl.dumpHyponymTree(System.err, "cat", "n");
    wl.dumpHypernymTree(System.err, "cat", "n");
  }

  /*
   * TODO: shorten this crazy-long class... make getWordIterator start at a
   * different offset each time! ** handle verb frames..
   * 
   * Add -> String[] getAllRelations(word,pos), getAllRelations(id),
   * getAllRelations(word) maybe?
   * 
   * Add binary methods hasRelationship(a,b) getRelationshipStrength(a,b),
   * 
   * Test getDistance(a,b)
   * 
   * TO-CHECK: JavaNLP, Jawbone, JWI(MIT) & Wordnet::Similarity for other
   * methods (see firefox-bookmarks-apis) [also maybe a faster iterator?]
   * 
   * Add hypo & hypernym tree methods with DEPTH?
   */
  /** @invisible */
  public static void mainX(String[] args) throws Exception
  {
    // Locale.setDefault(Locale.GERMAN);
    // System.err.println("[INFO] RiWordnet.main..."+Locale.getDefault());
    String[] s = null;
    RiWordnet wl = new RiWordnet();
    /*
     * s = wl.getSynset("dog","n"); //System.out.println(syn.toString()); if
     * (s==null) System.out.println("[]"); else for (int j = 0; j < s.length;
     * j++) System.out.println(s[j]);
     */
    for (int i = 0; i < 20; i++)
    {
      String n1 = wl.getRandomWord("n");
      String n2 = wl.getRandomWord("n");
      System.out.println(n1 + "/" + n2 + ": " + wl.getDistance(n1, n2, "n"));
    }
  }

  /** @throws JWNLException 
   * @invisible */
  public static void mainZ(String[] args) throws JWNLException
  {
    RiWordnet wordnet = new RiWordnet(null);
    
    System.out.println(wordnet.jwnlDict.lookupIndexWord(POS.NOUN, "ws2008"));
    if (1==1) System.exit(1);
    IndexWord keep = wordnet.lookupIndexWord(POS.VERB, "keep");
    IndexWord jog = wordnet.lookupIndexWord(POS.VERB, "jog");
    Synset t = jog.getSense(1);
    Synset k = keep.getSense(14);
    System.out.println(k);
    //RelationshipFinder.getInstance().findRelationships(t, k, PointerType.HYPERNYM);
    for (int i = 1; i <= keep.getSenseCount(); i++) {
      Synset s = keep.getSense(i);
      System.out.println(i+") "+s);
      RelationshipFinder.getInstance().findRelationships
        (s, t, PointerType.HYPERNYM);
    }
    
    if (1 == 1) return;
    System.out.println(wordnet.getSenseCount("keep", "v"));
    
/*    IndexWord[] iws = wordnet.getSenseCount("keep", "v");
    for (int i = 0; i < iws.length; i++)
    {
      System.out.println(i+") "+iws[i]);
    }*/
    
/*    if (1 == 1) 
    {
      
      Iterator it = null;
      for (int i = 0; i < 5; i++)
      {
        long start = System.currentTimeMillis();
        it = wordnet.iterator("v");
        System.out.println((System.currentTimeMillis()-start)/1000d+"s");
        while (it.hasNext())
        {
          Object o = it.next();
          System.out.println(o);
        }
        System.out.println((System.currentTimeMillis()-start)/1000d+"s");
      }
      return;
    }*/
    String word = "dog";
    // String pos = "n";
    String pos = RiWordnet.NOUN;

    // An array for any results we get
    String[] result;

    // First we can look at all of the senses
    int[] ids = wordnet.getSenseIds(word, pos);
System.out.println("RiWordnet.main().ids="+ids.length);
    for (int j = 0; j < ids.length; j++)
    {
      try
      {
        System.out.println("ids[j] " + ids[j]);

        result = wordnet.getHypernymTree(ids[j]);
        
        if (result == null) {
          System.out.println("RESULT=null");
          continue;
        }

        System.out.println("#" + ids[j] + ":");

        for (int i = 0; i < result.length; i++)
        {
          System.out.println(result[i]);
        }
      }
      catch (Exception e)
      {
        System.out.println("Error: " + e);
      }
    }
  }

  private static void mainZZ(String args[])  
  {
    RiWordnet w = new RiWordnet();
    w.ignoreUpperCaseWords(true);
    w.ignoreCompoundWords(true);

    String[] t = w.getPos("hair");
    for (int i = 0; i < t.length; i++)
    {
      System.out.println(t[i]);
    }
  }
  
  private static void mainXXX(String args[])
  {
    RiWordnet w = new RiWordnet();
    
    double dist = w.getDistance("keep", "training", "v");
    System.out.println("dist="+dist);
    if (1==1) return;;
  }
    
  public static void mainVV(String[] args) throws JWNLException, FileNotFoundException
  {
    
/*    String confFile = "/Users/dhowe/Desktop/file_properties.xml";
    
    File f = new File(confFile);
    InputStream is = WordnetUtil.getResourceStream(getClass(), confFile);
    
    //InputStream is = RiWordnet.class.getResourceAsStream("file_properties.xml");
    InputStream is = new FileInputStream(new File(confFile));
    System.err.println("[INFO] Initializing Wordnet: stream='" + is + "'");
    JWNL.initialize(is);*/
    RiWordnet w = new RiWordnet(null);
    String test = "computer_science";
    boolean b = w.exists(test);
    System.out.println(b);
    
  }
  
  public static void main(String args[])
  {
    RiWordnet rw;
    System.out.println(RiTa.cwd());
    //rw = new RiWordnet("/WordNet-3.1");
    //rw = new RiWordnet();
    rw = new RiWordnet("http://rednoise.org/wordnet31");

    String test = "table_service";
    boolean b = rw.exists(test);
    System.out.println(b + ": "+rw.getPosStr(test) + " " + WordnetUtil.asList(rw.getSynonyms(test, "n")));
    
    test = "table-service";
    b = rw.exists(test);
    System.out.println(b + ": "+rw.getPosStr(test)+ " " + WordnetUtil.asList(rw.getSynonyms(test, "n")));

    test = "table";
    b = rw.exists(test);
    System.out.println(b + ": "+rw.getPosStr(test) + " " + WordnetUtil.asList(rw.getSynonyms(test, "n")));
    
    test = "service";
    b = rw.exists(test);
    System.out.println(b + ": "+rw.getPosStr(test)+ " " + WordnetUtil.asList(rw.getSynonyms(test, "n")));
    
    test = "table service";
    b = rw.exists(test);
    System.out.println(b);
  }
  
}
