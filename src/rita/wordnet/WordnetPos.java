package rita.wordnet;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import rita.wordnet.jwnl.data.POS;


/**
 * Enumerated types for various Part-of-Speech representations.
 * <p>
 * Includes QTag & Wordnet tags at present.
 * @invisible 
 * @author dhowe
 * <p>See the accompanying documentation for license information
 */
public class WordnetPos

{   /**  Part of Speech TYPE -> QTAG FORMAT    */
  public static final int QTAG    = 5;
  
  /**  Part of Speech TYPE -> PENN FORMAT    */
  public static final int PENN    = 1;
  
  /**  Part of Speech TYPE -> Wordnet FORMAT */
  public static final int WORDNET = 2;
  
  /**  Part of Speech TYPE -> AL FORMATS    */
  public static final int ALL   = 0;
  
  // Types (ALL)             ======================================
  public static final WordnetPos UNKNOWN = new WordnetPos("???", "UNKNOWN", ALL);
  
  // Types (WORDNET / GENERIC)  ===================================
  public static final WordnetPos N = new WordnetPos("N", "NOUN_KEY", WORDNET);
  public static final WordnetPos V = new WordnetPos("V", "VERB_KEY", WORDNET);
  public static final WordnetPos R = new WordnetPos("R", "ADVERB_KEY", WORDNET);
  public static final WordnetPos A = new WordnetPos("A", "ADJECTIVE_KEY", WORDNET);

  private static final List WORDNET_TAGS = Collections.unmodifiableList
    (Arrays.asList( new WordnetPos[] { N, V, R, A } ));

  
  // members variables    =============================
  
  private String tag, description, examples;
  private int type;
  
  // private constructors =============================
  
  private WordnetPos(String tag, String description, int type) 
  {
    this(tag, description, QQ, type);
  }
  
  private WordnetPos(String tag, String description, String examples, int type) 
  {
    this.tag = tag;
    this.description = description;
    this.examples = examples;
    this.type = type;
  }  
 
  private static boolean in(String pos, WordnetPos[] choices)
  {
    for (int i = 0; i < choices.length; i++) 
      if (pos.equalsIgnoreCase(choices[i].toString()))
        return true;
    return false;
  }  

  static boolean isVerb(String pos, WordnetPos[] verbs)
  {
    return (in(pos, verbs));
  }

  static boolean isNoun(String pos, WordnetPos[] nouns)
  {
    return (in(pos, nouns));
  }

  static boolean isAdverb(String pos, WordnetPos[] advs)
  {
    return (in(pos, advs));
  }

  static boolean isAdjective(String pos, WordnetPos[] adjs)
  {
    return (in(pos, adjs));
  }
   
  // static methods (Wordnet)    =============================
   
  public static List wordNetTags() {
    return WORDNET_TAGS;
  }
      
	public static POS getPos(String pos) 
  {
		if (pos.equalsIgnoreCase(N.tag)) return rita.wordnet.jwnl.data.POS.NOUN;
    if (pos.equalsIgnoreCase(V.tag)) return rita.wordnet.jwnl.data.POS.VERB;
    if (pos.equalsIgnoreCase(R.tag)) return rita.wordnet.jwnl.data.POS.ADVERB;
    if (pos.equalsIgnoreCase(A.tag)) return rita.wordnet.jwnl.data.POS.ADJECTIVE;
    return null;
	}  

  public static WordnetPos fromWordnet(POS pos) 
  {
    return fromWordnet(pos.getLabel());
  }
  
  public static WordnetPos fromWordnet(String label) {
    for (Iterator iter = WORDNET_TAGS.iterator(); iter.hasNext();) {
      WordnetPos pos = (WordnetPos) iter.next();
      //System.out.println(pos);
      if (pos.getTag().equalsIgnoreCase(label))
        return pos;
    }
    return null;
  }
  
  public static boolean isWordnet(String pos) {
    //System.out.println("Pos.isWordnet("+pos+")");
    List wnps = rita.wordnet.jwnl.data.POS.getAllPOS();
    for (Iterator iter = wnps.iterator(); iter.hasNext();) {
      POS wnpos = (POS) iter.next();
      if (wnpos.getKey().equalsIgnoreCase(pos)) 
        return true;
    }
    return false;
  }

  // static methods (Wordnet) =============================
  
  public static POS getWordnetPOSForLabel(String label) {
    return rita.wordnet.jwnl.data.POS.getPOSForLabel(label);
  }
  
  public static POS getWordnetPOSForKey(String key) {
    return rita.wordnet.jwnl.data.POS.getPOSForKey(key);
  }
  
  public static String getWordnetLabel(POS pos) {
    return pos.getLabel();
  }
  
  public static String getWordnetKey(POS pos) {
    return pos.getKey();
  }
  
  // getter methods       ================================
  
  public String getDescription() {
    return this.description;
  }

  public String getExamples() {
    return this.examples;
  }
  
  public String getTag() {
    return this.tag;
  }

  public int getType() {
    return this.type;
  }
  
  public String toString() {
    return this.tag;
  }
  
  public boolean equals(Object obj)
  {
    WordnetPos p = (WordnetPos)obj;
    
    // match primary tag
    if (!getTag().equals(p.getTag()))
      return false;
    
    // match POS type 
    if (getType() != p.getType())
      return false;
    
    return true;
  }
  
  private static final String QQ = "";
  
  public static void main(String[] args)
  {   
    System.out.println(WordnetPos.getWordnetKey(rita.wordnet.jwnl.data.POS.ADVERB));
  }
  
}// end

