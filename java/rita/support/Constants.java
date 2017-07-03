package rita.support;

public interface Constants extends EnglishConstants
{
 
  // ==== Features ============ 
  
  String SLASH = "/";
  String WORD_BOUNDARY = " ";
  String PHONEME_BOUNDARY = "-";
  String SYLLABLE_BOUNDARY = SLASH;  
  String SYLLABLES = "syllables";
  String PHONEMES = "phonemes";
  String STRESSES = "stresses";
  String POSLIST = "poslist";
  String MUTABLE = "mutable";
  String TOKENS = "tokens";
  String TEXT = "text";
  String POS = "pos";
  String ID = "id";

  // ==== Phonemes  ============== 
  
  int IPA = 2, ARPA = 1;
  String[] ALL_PHONES  = {
    "aa","ae","ah","ao","aw","ay","b","ch","d","dh",
    "eh","er","ey","f","g","hh","ih","iy","jh", "k","l",
    "m","n","ng","ow","oy","p","r","s","sh","t","th","uh",
    "uw","v","w","y","z","zh",
  };
  String IPA_STRESS = "ˈ";
  String IPA_2NDSTRESS = "ˌ";
  String VOWELS = "aeiou";
  
  // ==== Tokenizer  ============= 
  
  int REGEX_TOKENIZER = 2;
  int PENN_WORD_TOKENIZER = 4;
  
  // ==== Conjugator  ============ 

  String PERSON = "person";
  
  /** Specifies person as one of (first, second or third) */
  int FIRST_PERSON = 1;
  
  /** Specifies person as one of (first, second or third) */
  int SECOND_PERSON = 2;
  
  /** Specifies person as one of (first, second or third) */
  int THIRD_PERSON = 3;
  
  String TENSE = "tense";
  
  /** Specifies tense as one of (past, present or future) */
  int PAST_TENSE = 4;
  
  /** Specifies tense as one of (past, present or future) */
  int PRESENT_TENSE = 5;
  
  /** Specifies tense as one of (past, present or future) */
  int FUTURE_TENSE = 6;
  
  String NUMBER = "number";
  
  /** Specifies agreement as one of (singular or plural)  */
  int SINGULAR = 7;  
  
  /** Specifies agreement as one of (singular or plural)  */
  int PLURAL = 8;
  
  /**
   * Typically the declarative sentence, but in the current conjugator implementation,
   * used as the default form to the exclusion of the others.
   */
  int NORMAL=10;

  /** The INFINITIVE, e.g. <i>to eat an apple</i> */
  int INFINITIVE=11;

  /** Gerund form of the VP, e.g. <i>eating an apple</i> */
  int GERUND=12;

  /** The form, e.g. <I>eat an apple!</I> */
  int IMPERATIVE=13;

  /** Bare infinitive VP, e.g. <i>eat an apple</i>. */
  int BARE_INFINITIVE=14;

  /** Subjunctive form, e.g. <i>if I were a rich man</i>. */
  int SUBJUNCTIVE=15;
  
  // ======== STEMMING CONSTANTS ===================
  
  enum StemmerType {  Pling, Porter, Lancaster };
  String PLING = StemmerType.Pling.name();
  String PORTER = StemmerType.Porter.name();
  String LANCASTER = StemmerType.Lancaster.name();

  
 // ======== ANIMATION CONSTANTS ===================
  
  /** Specifies 'linear' as the motion type for moveXX() methods (default) */
  int LINEAR = 0;
  
  /** Specifies 'ease-in' as the motion type for moveXX() methods (quadratic)*/ 
  int EASE_IN = 1;
  
  /** Specifies 'ease-out' as the motion type for moveXX() methods (quadratic) */
  int EASE_OUT = 2;
  
  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (quadratic) */
  int EASE_IN_OUT = 3;
  
  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (cubic) */
  int EASE_IN_OUT_CUBIC = 4;
  
  /** Specifies 'ease-in' as the motion type for moveXX() methods (cubic) */
  int EASE_IN_CUBIC = 5;
  
  /** Specifies 'ease-out' as the motion type for moveXX() methods (cubic) */
  int EASE_OUT_CUBIC = 6;
  
  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (quartic) */
  int EASE_IN_OUT_QUARTIC = 7;
  
  /** Specifies 'ease-in' as the motion type for moveXX() methods (quartic) */
  int EASE_IN_QUARTIC = 8;
  
  /** Specifies 'ease-out' as the motion type for moveXX() methods (quartic) */
  int EASE_OUT_QUARTIC = 9; 
  
  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (circular) */
  int EASE_IN_OUT_SINE = 10;

  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (circular) */
  int EASE_IN_SINE = 11;

  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (circular) */
  int EASE_OUT_SINE = 12;
  
  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (circular) */
  int EASE_IN_OUT_EXPO = 13;

  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (circular) */
  int EASE_IN_EXPO = 14;

  /** Specifies 'ease-in/out' as the motion type for moveXX() methods (circular) */
  int EASE_OUT_EXPO = 15;
    
  // ==== RiTa ================  
  
  String DEFAULT_CALLBACK = "onRiTaEvent";
  String ALL_QUOTES = "\"“”’‘`'";
  String S_QUOTES = "’‘`'";// 7 ?
  String PUNCT_CHARS = ALL_QUOTES+"~\",;:!?)([].#\"\\!@$%&}<>|+=-_\\/*{^"; // add quotes?
  String ALL_PUNCT =  "[\\p{Punct}"+ALL_QUOTES+"]+";
  String PUNCT_PATT = "^(?:[\\p{Punct}"+ALL_QUOTES+"]*)((?:.)|(?:[\\w ].*?[\\w ]))(?:[\\p{Punct}"+ALL_QUOTES+"]*)$";
  String DATA_DELIM = "\\|";
  String DEFAULT_LEXICON = "rita_dict.js";
  String DEFAULT_LTS = "rita_lts.js";
  String DEFAULT_USER_ADDENDA_FILE = "rita_addenda.txt";
  String CMUDICT_COMMENT = "#";
  String LEXICON_DELIM = ":";
  String PHONE_DELIM = "[- ]";
  String NON_BREAKING_SPACE = "<sp/>";
  String PARAGRAPH_BREAK = "<p/>";
  String LINE_BREAK = "<br/>";
  String WS = "\\s+";
  String UTF8 = "UTF-8";

  char STRESSED   = '1',  UNSTRESSED = '0';
  boolean LOAD_USER_ADDENDA = false;

  String[] EMPTY = new String[0];
  String FS = "/", SP = " ", E = "", DQ = "\"", SQ = "'"; 
  String BN = "\n", BRN = "\r\n", DASH = "-", AMP = "&", EQ="=", USC = "_";
  String LP = "(", RP = ")", LB = "[", RB = "]", BS = "\\";
  
  /** offset to use for page numbers in the PageLayout footer*/
  int PAGE_NO_OFFSET = 35;
  
  float _DEFAULT_FONT_SIZE = 14;
  
  // ==== RiTaEvent ============ 
  
  enum EventType {  
    MoveTo,
    ColorTo,
    FadeIn,
    FadeOut,
    TextTo, 
    Timer,
    ScaleTo,
    RotateTo,
    TextEntered,
    Lerp,
    BoundingAlpha,
    Internal,
    DataLoaded,
    Unknown
  };

  String MOVE_TO = EventType.MoveTo.name();
  String COLOR_TO = EventType.ColorTo.name();
  String FADE_IN = EventType.FadeIn.name();
  String FADE_OUT = EventType.FadeOut.name();
  String TEXT_TO = EventType.TextTo.name();
  String TIMER = EventType.Timer.name();
  String SCALE_TO = EventType.ScaleTo.name();
  String ROTATE_TO = EventType.RotateTo.name();
  String TEXT_ENTERED = EventType.TextEntered.name();
  String LERP = EventType.Lerp.name();
  String BOUNDING_ALPHA = EventType.BoundingAlpha.name();
  String INTERNAL = EventType.Internal.name();
  String DATA_LOADED = EventType.DataLoaded.name();
  String UNKNOWN = EventType.Unknown.name();
}

