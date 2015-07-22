package rita.support;

import static rita.support.Constants.StemmerType.Lancaster;
import static rita.support.Constants.StemmerType.Pling;
import static rita.support.Constants.StemmerType.Porter;

import java.text.DecimalFormat;

import rita.RiTa;
import rita.RiTaException;

// TODO: test with word-list (+stems) from here:
// http://snowball.tartarus.org/algorithms/porter/diffs.txt


// TODO: load rules files as class resources ***********
// TODO: pkg correct rita.dict in jar file

/**
 * A simple set of stemmers for extracting base roots from a word by 
 * removing prefixes and suffixes. For example, the words 'run', 'runs',  
 * and 'running' all have "run" as their stem.<pre>
    String[] tests = { "run", "runs", "running" };
    RiStemmer stem = new RiStemmer(this);
    for (int i = 0; i < tests.length; i++)
      System.out.println(stem.stem(tests[i]));
 * </pre>
 * This class provides a # of implementations, each specified by a type constant.<br>
 * For example, to use the Lancaster (or Paice-Husk) algorithm instead of the Porter (the default),
 * create the stemmer as follows:
 * <pre>
 * RiStemmer stem = new RiStemmer(this, LANCASTER_STEMMER);
 * </pre>
 * For a comparison of the various algorithms, see <br>
 *   http://www.comp.lancs.ac.uk/computing/research/stemming/Links/algorithms.htm
 */
public class Stemmer implements StemmerIF, Constants
{
  private StemmerIF delegate;
  
  private static Stemmer lancaster, porter, pling;

  public static Stemmer getInstance()
  {
    return getInstance(PORTER); // default
  }
  
  public static Stemmer getInstance(String theType)
  {
    StemmerType result = Porter;
    for (StemmerType st : StemmerType.values())
    {
      if (st.name().equals(theType))
        result = st;
    }
    return getInstance(result);
  }
  
  public static Stemmer getInstance(StemmerType type)
  {
    return getInstance(type.ordinal());
  }
  
  public static Stemmer getInstance(int stemmerType)
  {
    if (stemmerType == Pling.ordinal()) {
        if (pling == null) 
          pling = new Stemmer(stemmerType);
        return pling;
    }
    else if (stemmerType == Porter.ordinal()) {
        if (porter == null) 
          porter = new Stemmer(stemmerType);
        return porter;
    }
    else if (stemmerType == Lancaster.ordinal()) {
        if (lancaster == null) 
          lancaster = new Stemmer(stemmerType);
        return lancaster;
    }
    else 
        throw new RiTaException("Unexpected stemmer type: "+stemmerType);
  }
  
  public Stemmer(int stemmerType)
  {
    if (stemmerType == Pling.ordinal()) {
      delegate = new PlingStemmer();
    }
    else if (stemmerType == Porter.ordinal()) {
      delegate = new Porter2Stemmer();
    }
    else if (stemmerType == Lancaster.ordinal()) {
      delegate = new LancasterStemmer();
    }
    else 
      throw new RiTaException("Unexpected stemmer type: "+stemmerType);
  }
    
  /** 
   * Returns the concrete stemmer (delegate) object that actually does the work
   */
  public StemmerIF getStemmerImpl() {
    return delegate;
  }

  /**
   * Extracts base roots by removing prefixes and suffixes. 
   * For example, the words 'run', 'runs', 'ran', and 'running' all have "run" as their stem.
   */
  public String stem(String word)
  {
    return word.contains(SP) ? stem(RiTa.tokenize(word)) : delegate.stem(word);
  }
  
  /**
   * Extracts base roots from an array of words by removing prefixes and suffixes. 
   * For example, the words 'run', 'runs', 'ran', and 'running' all have "run" as their stem.
   */
  public String stem(String[] words)
  {
    String[] result = new String[words.length];
    for (int i = 0; i < words.length; i++)
    {
      result[i] = delegate.stem(words[i]); 
    }
    return RiTa.untokenize(result);
  }
  
  /**
   * Extracts base roots from a word by removing prefixes and suffixes, using the POS specified as context. 
   * For example, the words 'run', 'runs', 'ran', and 'running' all have "run"
   * as their stem
  public String stem(String word, String pos)
  {
    return stemmer.stem(s) 
    RiPosTagger.isNoun(pos) ? 
        nounStemmer.stem(word) : genericStemmer.stem(word);
  }*/

  private static void testUnchanging(Stemmer stemmer)
  {
    stemmer.test("locomote", "locomote");
    stemmer.test("idle", "idle");
    stemmer.test("juvenile", "juvenile");
    stemmer.test("ingenue", "ingenue");
    stemmer.test("service", "service");
    stemmer.test("creature", "creature");
    stemmer.test("device", "device");
    stemmer.test("lagerphone", "lagerphone");
    stemmer.test("force", "force");
    stemmer.test("desire", "desire");
    stemmer.test("province", "province");
    stemmer.test("signalise", "signalise");
    stemmer.test("formulate", "formulate");
    stemmer.test("cognise", "cognise");
    stemmer.test("communicate", "communicate");
    stemmer.test("tangle", "tangle");
    stemmer.test("motorcycle", "motorcycle");
    stemmer.test("synchronise", "synchronise");
    stemmer.test("admeasure", "admeasure");
    stemmer.test("gauge", "gauge");
    stemmer.test("intertwine", "intertwine");
    stemmer.test("precede", "precede");
    stemmer.test("situate", "situate");
    stemmer.test("automobile", "automobile");
    stemmer.test("enumerate", "enumerate");
    stemmer.test("determine", "determine");
    stemmer.test("disagree", "disagree");
    stemmer.test("agree", "agree");
    stemmer.test("mobile", "mobile");
    stemmer.test("machine", "machine");
    stemmer.test("locate", "locate");
    stemmer.test("hearse", "hearse");
    stemmer.test("translate", "translate");
    stemmer.test("endure", "endure");
    stemmer.test("secure", "secure");
    stemmer.test("straddle", "straddle");
    stemmer.test("desire", "desire");
    stemmer.test("populate", "populate");
    stemmer.test("cringle", "cringle");
    stemmer.test("corroborate", "corroborate");
    stemmer.test("substantiate", "substantiate");
  }
  
  private void test(String expected, String... tests) {
    for (int i = 0; i < tests.length; i++)
      System.out.println(test(expected, tests[i]));
  }
  
  private static float runTests(Stemmer stemmer, String[] data)
  {
    int fails = 0;
    for (int i = 0; i < data.length; i++) {
      String[] parts = data[i].split("\\s+");
      if (!stemmer.test(parts[1] ,parts[0]))
        fails++;
    }
    return (data.length-fails)/(float)data.length;
  }
  

  protected boolean test(String expected, String test) {
    String key = /*clean*/(test.toLowerCase());
    String val = stem(key);
    //System.out.println("lookup.put(\""+key+"\", "+"\""+val+"\");");
    if (!val.equals(expected)) {
      //System.err.println("    FAIL: stem('"+test+"') returned '"+ val+"', expecting '"+expected+"'");
      //System.err.print("      Stemmers:  Pling(n)='"+new PlingStemmer().stem(test)+"'");
      //System.err.print(", Porter(*)='"+new PorterStemmer().stem(test)+"'");
      if (exitOnFail) System.exit(1);
      return false;
    }
    return true;
  }
  
  private static boolean exitOnFail = false;
  
  public static void main(String[] args)
  {
    exitOnFail = false;
    DecimalFormat DF = new DecimalFormat("#.#");
    
    Stemmer stemmer = null;
    
    String[] data = RiTa.loadStrings("diffs.txt"); // missing!
    
    System.out.println("Loaded: "+data.length);
    
     // ------------------------------------------------
    stemmer = new Stemmer(Porter.ordinal());
    System.err.println("\nTesting stemmer-class="+stemmer.getStemmerImpl());
    System.err.println(stemmer.getStemmerImpl()+" result="+DF.format(runTests(stemmer, data)*100)+"%");
    // ------------------------------------------------
    stemmer = new Stemmer(Pling.ordinal());
    System.err.println("\nTesting stemmer-class="+stemmer.getStemmerImpl());
    System.err.println(stemmer.getStemmerImpl()+" result="+DF.format(runTests(stemmer, data)*100)+"%");
    // ------------------------------------------------
    stemmer = new Stemmer(Lancaster.ordinal());
    System.err.println("\nTesting stemmer-class="+stemmer.getStemmerImpl());
    System.err.println(stemmer.getStemmerImpl()+" result="+DF.format(runTests(stemmer, data)*100)+"%");
    // ------------------------------------------------
  }

}// end
