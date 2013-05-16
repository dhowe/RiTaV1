package rita.support;

import java.util.*;

/**
 * Enumerated types for various Part-of-Speech representations.
 * <p>
 * Includes QTag & Wordnet tags at present.
 */
public class RiPos
{  
  
  /**  Part of Speech TYPE -> PENN FORMAT    */
  public static final int PENN    = 1;
  
  /**  Part of Speech TYPE -> Wordnet FORMAT */
  public static final int WORDNET = 2;

  /**  Part of Speech TYPE -> JET FORMAT     */
  public static final int JET     = 3;
  
  /**  Part of Speech TYPE -> Glink FORMAT    */
  public static final int GLINK   = 4;
  
  /**  Part of Speech TYPE -> AL FORMATS    */
  public static final int ALL   = 0;
  
  // Types (ALL)             ======================================
  public static final RiPos UNKNOWN = new RiPos("???", "UNKNOWN", ALL);
  
  // Types (WORDNET / GENERIC)  ===================================
  public static final RiPos N = new RiPos("N", "NOUN_KEY", WORDNET);
  public static final RiPos V = new RiPos("V", "VERB_KEY", WORDNET);
  public static final RiPos R = new RiPos("R", "ADVERB_KEY", WORDNET);
  public static final RiPos A = new RiPos("A", "ADJECTIVE_KEY", WORDNET);

  public static final RiPos PENN_CC = new RiPos("CC", "Coordinating conjunction", PENN);
  public static final RiPos PENN_CD = new RiPos("CD", "Cardinal number", PENN);
  public static final RiPos PENN_DT = new RiPos("DT", "Determiner", PENN);
  public static final RiPos PENN_EX = new RiPos("EX", "Existential there", PENN);
  public static final RiPos PENN_FW = new RiPos("FW", "Foreign word", PENN);
  public static final RiPos PENN_IN = new RiPos("IN", "Preposition or subordinating conjunction", PENN);
  public static final RiPos PENN_JJ = new RiPos("JJ", "Adjective", PENN);
  public static final RiPos PENN_JJR = new RiPos("JJR", "Adjective, comparative", PENN);
  public static final RiPos PENN_JJS = new RiPos("JJS", "Adjective, superlative", PENN);
  public static final RiPos PENN_LS = new RiPos("LS", "List item marker", PENN);
  public static final RiPos PENN_MD = new RiPos("MD", "Modal", PENN);
  public static final RiPos PENN_NN = new RiPos("NN", "Noun, singular or mass", PENN);
  public static final RiPos PENN_NNS = new RiPos("NNS", "Noun, plural", PENN);
  public static final RiPos PENN_NNP = new RiPos("NNP", "Proper noun, singular", PENN);
  public static final RiPos PENN_NNPS = new RiPos("NNPS", "Proper noun, plural", PENN);
  public static final RiPos PENN_PDT = new RiPos("PDT", "Predeterminer", PENN);
  public static final RiPos PENN_POS = new RiPos("POS", "Possessive ending", PENN);
  public static final RiPos PENN_PRP = new RiPos("PRP", "Personal pronoun", PENN);
  public static final RiPos PENN_PRP$ = new RiPos("PRP$", "Possessive pronoun (prolog version PRP-S)", PENN);
  public static final RiPos PENN_RB = new RiPos("RB", "Adverb", PENN);
  public static final RiPos PENN_RBR = new RiPos("RBR", "Adverb, comparative", PENN);
  public static final RiPos PENN_RBS = new RiPos("RBS", "Adverb, superlative", PENN);
  public static final RiPos PENN_RP = new RiPos("RP", "Particle", PENN);
  public static final RiPos PENN_SYM = new RiPos("SYM", "Symbol", PENN);
  public static final RiPos PENN_TO = new RiPos("TO", "to", PENN);
  public static final RiPos PENN_UH = new RiPos("UH", "Interjection", PENN);
  public static final RiPos PENN_VB = new RiPos("VB", "Verb, base form", PENN);
  public static final RiPos PENN_VBD = new RiPos("VBD", "Verb, past tense", PENN);
  public static final RiPos PENN_VBG = new RiPos("VBG", "Verb, gerund or present participle", PENN);
  public static final RiPos PENN_VBN = new RiPos("VBN", "Verb, past participle", PENN);
  public static final RiPos PENN_VBP = new RiPos("VBP", "Verb, non-3rd person singular present", PENN);
  public static final RiPos PENN_VBZ = new RiPos("VBZ", "Verb, 3rd person singular present", PENN);
  public static final RiPos PENN_WDT = new RiPos("WDT", "Wh-determiner", PENN);
  public static final RiPos PENN_WP = new RiPos("WP",   "Wh-pronoun", PENN);
  public static final RiPos PENN_WP$ = new RiPos("WP$", "Possessive wh-pronoun (prolog version WP-S)", PENN);
  public static final RiPos PENN_WRB = new RiPos("WRB", "Wh-adverb", PENN);

  //private static final List PENN_TAGS = Collections.unmodifiableList(Arrays.asList(
  private static final RiPos[] PENN_TAGS =  new RiPos[] { 
    PENN_CC,PENN_CD,PENN_DT,PENN_EX,PENN_FW,
		PENN_IN,PENN_JJ,PENN_JJR,PENN_JJS,PENN_LS,PENN_MD,PENN_NN,PENN_NNS,
		PENN_NNP,PENN_NNPS,PENN_PDT,PENN_POS,PENN_PRP,PENN_PRP$,PENN_RB,
		PENN_RBR,PENN_RBS,PENN_RP,PENN_SYM,PENN_TO,PENN_UH,PENN_VB,PENN_VBD,
		PENN_VBG,PENN_VBN,PENN_VBP,PENN_VBZ,PENN_WDT,PENN_WP,PENN_WP$,PENN_WRB,UNKNOWN };

  public static final RiPos[] PENN_NOUNS = { PENN_NN,PENN_NNS,PENN_NNP,PENN_NNPS };
  public static final RiPos[] PENN_VERBS = { PENN_VB,PENN_VBD,PENN_VBG,PENN_VBN,PENN_VBP,PENN_VBZ };
  public static final RiPos[] PENN_ADJ   = { PENN_JJ,PENN_JJR,PENN_JJS };
  public static final RiPos[] PENN_ADV   = { PENN_RB,PENN_RBR,PENN_RBS,PENN_RP }; 

  private static final List WORDNET_TAGS = Collections.unmodifiableList
    (Arrays.asList( new RiPos[] { N, V, R, A } ));

  public static boolean isPennTag(String tag) {
    for (int i = 0; i < PENN_TAGS.length; i++){
      if (PENN_TAGS[i].getTag().equalsIgnoreCase(tag))
         return true;
    }
    return false;
  }

  public static boolean isVerb(String tag) {
    for (int i = 0; i < PENN_VERBS.length; i++){
      if (PENN_VERBS[i].getTag().equalsIgnoreCase(tag))
         return true;
    }
    return false;
  }

  public static boolean isNoun(String tag) {
    for (int i = 0; i < PENN_NOUNS.length; i++){
      if (PENN_NOUNS[i].getTag().equalsIgnoreCase(tag))
         return true;
    }
    return false;
  }

  public static boolean isAdverb(String tag)
  {
    for (int i = 0; i < PENN_ADV.length; i++){
      if (PENN_ADV[i].getTag().equalsIgnoreCase(tag))
         return true;
    }
    return false;
  }
  
  public static boolean isAdj(String tag)
  {
    for (int i = 0; i < PENN_ADJ.length; i++){
      if (PENN_ADJ[i].getTag().equalsIgnoreCase(tag))
         return true;
    }
    return false;
  }
  
  //public static boolean isPluralNoun(String tag)  {
    //return isNoun(tag) && (tag.endsWith("s") || tag.endsWith("S"));
  //}
  
  // members variables    =============================
  
  private String tag, description, examples;
  private int type;
  
  // private constructors =============================
  
  private RiPos(String tag, String description, int type) 
  {
    this(tag, description, QQ, type);
  }
  
  private RiPos(String tag, String description, String examples, int type) 
  {
    this.tag = tag;
    this.description = description;
    this.examples = examples;
    this.type = type;
  }  

  
  /** 
   * Returns true if <code>pos</code> exists in the group <code>choices</code>
   */
  public static boolean in(String pos, RiPos[] choices)
  {
    for (int i = 0; i < choices.length; i++) 
      if (pos.equalsIgnoreCase(choices[i].toString()))
        return true;
    return false;
  }  
    
  public static RiPos fromWordnet(String label) {
    for (Iterator iter = WORDNET_TAGS.iterator(); iter.hasNext();) {
      RiPos pos = (RiPos) iter.next();
      //System.out.println(pos);
      if (pos.getTag().equalsIgnoreCase(label))
        return pos;
    }
    return null;
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
    RiPos p = (RiPos)obj;
    
    // match primary tag
    if (!getTag().equals(p.getTag()))
      return false;
    
    // match POS type 
    if (getType() != p.getType())
      return false;
    
    return true;
  }

  public static RiPos fromPenn(String buf)
  {
    throw new RuntimeException("RE-IMPLEMENT ME!!!!!!!");
  }
  
  private static final String QQ = "";
  
  public static void main(String[] args)
  {   
   // System.out.println(RiPos.getWordnetKey(net.didion.jwnl.data.POS.ADVERB));
  }

  
}// end

/* 
Full Set of QTag tags:
---------------------------
    !
    "
    (
    )
    ,
    -
    .
    ...
    :
    ;
    ?
    ???
    BE
    BED
    BEDZ
    BEG
    BEM
    BEN
    BER
    BEZ
    CC
    CD
    CD21
    CD22
    CS
    CS21
    CS22
    CS31
    CS32
    CS33
    CS41
    CS42
    CS43
    CS44
    DO
    DOD
    DOG
    DON
    DOZ
    DT
    EX
    FW
    HV
    HVD
    HVG
    HVN
    HVZ
    IN
    IN21
    IN22
    IN31
    IN32
    IN33
    IN41
    IN42
    IN43
    IN44
    JJ
    JJ21
    JJ22
    JJ31
    JJ32
    JJ33
    JJR
    JJS
    MD
    MD21
    MD22
    NN
    NN21
    NN22
    NN31
    NN32
    NN33
    NNS
    NNS21
    NNS22
    NP
    NPS
    OD
    PDT
    PN
    PN21
    PN22
    POS
    PP
    PP$
    PPX
    PPX21
    PPX22
    RB
    RB21
    RB22
    RB31
    RB32
    RB33
    RB41
    RB42
    RB43
    RB44
    RBR
    RBS
    RP
    SYM
    TO
    UH
    UH21
    UH22
    VB
    VBD
    VBG
    VBN
    VBZ
    WDT
    WDT31
    WDT32
    WDT33
    WP
    WP$
    WRB
    WRB31
    WRB32
    WRB33
    XNOT
*/
