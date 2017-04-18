package rita.support;

import rita.*;


/**
 * Enumerated types for various Part-of-Speech representations.
 * <p>
 * Includes QTag & Wordnet tags at present.
 */
public class RiPos implements Constants
{  
  /**  Part of Speech TYPE -> PENN FORMAT    */
  public static final int PENN    = 1;
  
  /**  Part of Speech TYPE -> Wordnet FORMAT */
  public static final int WORDNET = 2;

  // Types (WORDNET / GENERIC)  ===================================
  public static final RiPos N = new RiPos("n", "NOUN_KEY", WORDNET);
  public static final RiPos V = new RiPos("v", "VERB_KEY", WORDNET);
  public static final RiPos R = new RiPos("r", "ADVERB_KEY", WORDNET);
  public static final RiPos A = new RiPos("a", "ADJECTIVE_KEY", WORDNET);

  public static final RiPos CC = new RiPos("cc", "Coordinating conjunction", PENN);
  public static final RiPos CD = new RiPos("cd", "Cardinal number", PENN);
  public static final RiPos DT = new RiPos("dt", "Determiner", PENN);
  public static final RiPos EX = new RiPos("ex", "Existential there", PENN);
  public static final RiPos FW = new RiPos("fw", "Foreign word", PENN);
  public static final RiPos IN = new RiPos("in", "Preposition or subordinating conjunction", PENN);
  public static final RiPos JJ = new RiPos("jj", "Adjective", PENN);
  public static final RiPos JJR = new RiPos("jjr", "Adjective, comparative", PENN);
  public static final RiPos JJS = new RiPos("jjs", "Adjective, superlative", PENN);
  public static final RiPos LS = new RiPos("ls", "List item marker", PENN);
  public static final RiPos MD = new RiPos("md", "Modal", PENN);
  public static final RiPos NN = new RiPos("nn", "Noun, singular or mass", PENN);
  public static final RiPos NNS = new RiPos("nns", "Noun, plural", PENN);
  public static final RiPos NNP = new RiPos("nnp", "Proper noun, singular", PENN);
  public static final RiPos NNPS = new RiPos("nnps", "Proper noun, plural", PENN);
  public static final RiPos PDT = new RiPos("pdt", "Predeterminer", PENN);
  public static final RiPos POS = new RiPos("pos", "Possessive ending", PENN);
  public static final RiPos PRP = new RiPos("prp", "Personal pronoun", PENN);
  public static final RiPos PRP$ = new RiPos("prp$", "Possessive pronoun (prolog version PRP-S)", PENN);
  public static final RiPos RB = new RiPos("rb", "Adverb", PENN);
  public static final RiPos RBR = new RiPos("rbr", "Adverb, comparative", PENN);
  public static final RiPos RBS = new RiPos("rbs", "Adverb, superlative", PENN);
  public static final RiPos RP = new RiPos("rp", "Particle", PENN);
  public static final RiPos SYM = new RiPos("sym", "Symbol", PENN);
  public static final RiPos TO = new RiPos("to", "to", PENN);
  public static final RiPos UH = new RiPos("uh", "Interjection", PENN);
  public static final RiPos VB = new RiPos("vb", "Verb, base form", PENN);
  public static final RiPos VBD = new RiPos("vbd", "Verb, past tense", PENN);
  public static final RiPos VBG = new RiPos("vbg", "Verb, gerund or present participle", PENN);
  public static final RiPos VBN = new RiPos("vbn", "Verb, past participle", PENN);
  public static final RiPos VBP = new RiPos("vbp", "Verb, non-3rd person singular present", PENN);
  public static final RiPos VBZ = new RiPos("vbz", "Verb, 3rd person singular present", PENN);
  public static final RiPos WDT = new RiPos("wdt", "Wh-determiner", PENN);
  public static final RiPos WP = new RiPos("wp",   "Wh-pronoun", PENN);
  public static final RiPos WP$ = new RiPos("wp$", "Possessive wh-pronoun (prolog version WP-S)", PENN);
  public static final RiPos WRB = new RiPos("wrb", "Wh-adverb", PENN);

  //private static final List PENN_TAGS = Collections.unmodifiableList(Arrays.asList(
  private static final RiPos[] PENN_TAGS =  new RiPos[] { 
    		CC,CD,DT,EX,FW,
		IN,JJ,JJR,JJS,LS,MD,NN,NNS,
		NNP,NNPS,PDT,POS,PRP,PRP$,RB,
		RBR,RBS,RP,SYM,TO,UH,VB,VBD,
		VBG,VBN,VBP,VBZ,WDT,WP,WP$,WRB  };

  public static final RiPos[] NOUNS = { NN,NNS,NNP,NNPS };
  public static final RiPos[] VERBS = { VB,VBD,VBG,VBN,VBP,VBZ };
  public static final RiPos[] ADJ   = { JJ,JJR,JJS };
  public static final RiPos[] ADV   = { RB,RBR,RBS,WRB }; 

  public static final RiPos[] WORDNET_TAGS = { N, V, R, A };

  public static boolean isPennTag(String tag) {
    for (int i = 0; i < PENN_TAGS.length; i++) {
      if (PENN_TAGS[i].getTag().equals(tag))
         return true;
    }
    return false;
  }
  
  public static boolean isWordNetTag(String tag) {
    for (int i = 0; i < WORDNET_TAGS.length; i++) {
      if (WORDNET_TAGS[i].getTag().equals(tag))
         return true;
    }
    return false;
  }

  public static boolean isVerb(String tag) {
    for (int i = 0; i < VERBS.length; i++){
      if (VERBS[i].getTag().equals(tag))
         return true;
    }
    return false;
  }

  public static boolean isNoun(String tag) {
    for (int i = 0; i < NOUNS.length; i++){
      if (NOUNS[i].getTag().equals(tag))
         return true;
    }
    return false;
  }

  public static boolean isAdverb(String tag)
  {
    for (int i = 0; i < ADV.length; i++){
      if (ADV[i].getTag().equals(tag))
         return true;
    }
    return false;
  }
  
  public static boolean isAdj(String tag)
  {
    for (int i = 0; i < ADJ.length; i++){
      if (ADJ[i].getTag().equals(tag))
         return true;
    }
    return false;
  }

  
  // members variables    =============================
  
  private String tag, description, examples;
  
  // private constructors =============================
  
  private RiPos(String tag, String description, int type) 
  {
    this(tag, description, E, type);
  }
  
  private RiPos(String tag, String description, String examples, int type) 
  {
    this.tag = tag;
    this.description = description;
    this.examples = examples;
  }  
  
  public static RiPos fromWordnet(String label) {
    
    for (int i = 0; i < WORDNET_TAGS.length; i++) {
      
      RiPos pos = WORDNET_TAGS[i];
      if (pos.getTag().equals(label))
        return pos;
    }
    
    return null;
  }
  
  public static String posToWordNet(String pos)
  {
    if (pos == null || pos.length() < 1)
      return null;
    
    if (pos.equals("n") || isNoun(pos))
      return "n";
    if (pos.equals("v") || isVerb(pos))
      return "v";
    if (pos.equals("r") || isAdverb(pos))
      return "r";
    if (pos.equals("a") || isAdj(pos))
      return "a";
    
    return "-";
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
  
  public String toString() {
    return this.tag;
  }

  public int hashCode()
  {
    return this.tag.hashCode();
  }
  
  public boolean equals(Object obj)
  {
    return this.tag.equals(((RiPos)obj).getTag());
  }
  
  public static void main(String[] args) {
    RiLexicon rl = new RiLexicon();
    for (int i = 0; i < PENN_TAGS.length; i++) {
      System.out.print(PENN_TAGS[i].toString()+": ");
      System.out.println(rl.randomWord(PENN_TAGS[i].toString()));
    }
  }
  
}// end
