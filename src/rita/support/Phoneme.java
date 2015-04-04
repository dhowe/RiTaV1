// $Id: Phoneme.java,v 1.1 2013/05/13 14:02:08 dev Exp $

package rita.support;

import java.util.List;

// should this be an enumerated type?
/**
 * Static utility methods for operations involving phonemes.
 * <pre>
        TTS-Phoneme   Example     Translation    (39 entries, 11 missing?)
        ========================================================
          AA            odd         AA D
          AE            at          AE T
          AH            hut         HH AH T
          AO            ought       AO T
          AW            cow         K AW
          AY            hide        HH AY D
          B             be          B IY
          CH            cheese      CH IY Z
          D             dee         D IY
          DH            thee        DH IY
          EH            Ed          EH D
          ER            hurt        HH ER T
          EY            ate         EY T
          F             fee         F IY
          G             green       G R IY N
          HH            he          HH IY
          IH            it          IH T
          IY            eat         IY T
          JH            gee         JH IY
          K             key         K IY
          L             lee         L IY
          M             me          M IY
          N             knee        N IY
          NG            ping        P IH NG
          OW            oat         OW T
          OY            toy         T OY
          P             pee         P IY
          R             read        R IY D
          S             sea         S IY
          SH            she         SH IY
          T             tea         T IY
          TH            theta       TH EY T AH
          UH            hood        HH UH D
          UW            two         T UW
          V             vee         V IY
          W             we          W IY
          Y             yield       Y IY L D
          Z             zee         Z IY
          ZH            seizure     S IY ZH ER
          
          
    SAMPA  IPA(hex), IPA(dec),           DESC                 29 Entries
    ======================================================================== 
    ('A',  '\u0251',  593,    "open back unrounded, Cardinal 5, Eng. start");
    ('{',  '\u00E6',  230,    "near-open front unrounded, Eng. trap");
    ('6',  '\u0250',  592,    "open schwa, Ger. besser");
    ('Q',  '\u0252',  594,    "open back rounded, Eng. lot");
    ('E',  '\u025B',  603,    "open-mid front unrounded, C3, Fr. m�me");
    ('@',  '\u0259',  601,    "schwa, Eng. banana");
    ('3',  '\u025C',  604,    "long mid central, Eng. nurse");
    ('I',  '\u026A',  618,    "lax close front unrounded, Eng. kit");
    ('O',  '\u0254',  596,    "open-mid back rounded, Eng. thought");
    ('2',  '\u00F8',  248,    "close-mid front rounded, Fr. deux");
    ('9',  '\u0153',  339,    "open-mid front rounded, Fr. neuf");
    ('&',  '\u0276',  630,    "open front rounded");
    ('U',  '\u028A',  650,    "lax close back rounded, Eng. foot");
    ('}',  '\u0289',  649,    "close central rounded, Swedish sju");
    ('V',  '\u028C',  652,    "open-mid back unrounded, Eng. strut");
    ('Y',  '\u028F',  655,    "lax [y], Ger. h�bsc");
    ('B',  '\u03B2',  946,    "voiced bilabial fricative, Sp. cabo");
    ('C',  '\u00E7',  231,    "voiceless palatal fricative, Ger. ich");
    ('D',  '\u00F0',  240,    "voiced dental fricative, Eng. then");
    ('G',  '\u0263',  611,    "voiced velar fricative, Sp. fuego");
    ('L',  '\u028E',  654,    "palatal lateral, It. famiglia");
    ('J',  '\u0272',  626,    "palatal nasal, Sp. a�o");
    ('N',  '\u014B',  331,    "velar nasal, Eng. thing");
    ('R',  '\u0281',  641,    "vd. uvular fric. or trill, Fr. roi");
    ('S',  '\u0283',  643,    "voiceless palatoalveolar fricative, Eng. ship");
    ('T',  '\u03B8',  952,    "voiceless dental fricative, Eng. thin");
    ('H',  '\u0265',  613,    "labial-palatal semivowel, Fr. huit");
    ('Z',  '\u0292',  658,    "vd. palatoalveolar fric., Eng. measure");
    ('?',  '\u0294',  660,    "glottal stop, Ger. Verein, also Danish st�d");</pre>
 */
public abstract class Phoneme 
{
  /**
   * Vowels
   */
  static final private String VOWELS = "aeiou";

  /**
   * Glides/Liquids
   */
  static final private String GLIDES_LIQUIDS = "wylr";

  /**
   * Nasals
   */
  static final private String NASALS = "nm";

  /**
   * Voiced Obstruents
   */
  static final private String VOICED_OBSTRUENTS = "bdgjlmnnnrvwyz";
  
  public static final String[] TTS_PHONEMES  = {
    "aa","ae","ah","ao","aw","ax","axr","ay","b","ch","d","dh",
    "dx","eh","el","em","en","er","ey","f","g","hh","hv","ih",
    "iy","jh","k","l","m","n","nx","ng","ow","oy","p","r","s",
    "sh","t","th","uh","uw","v","w","y","z","zh","pau","h#","brth"
  };

  public static boolean isPhoneme(String phoneme) 
  {
    for (int i = 0; i < TTS_PHONEMES.length; i++) {
      if (phoneme.equals(TTS_PHONEMES[i]))
        return true; 
    }
    return false;
  }
  
  /**
   * Determines if the given phone is a vowel
   *
   * @param phone the phone to test
   *
   * @return <code>true</code> if phone is a vowel
   *    otherwise <code>false</code>. 
   */
  static public boolean isVowel(String phone) {
      return VOWELS.indexOf(phone.substring(0,1)) != -1;
  }

    /**
   * Determines if the given phone is a consonant
   *
   * @param phoneme the phone to test
   *
   * @return <code>true</code> if phone is a consonant
   *    otherwise <code>false</code>. 
   */
  public static boolean isConsonant(String phoneme) 
  {
    return !isVowel(phoneme); // ???
  }
  
  /**
   * Determines if the given phone represents a silent phone.
   *
   * @param phone the phone to test
   *
   * @return <code>true</code> if the phone represents a silent
   *    phone; otherwise <code>false</code>. 
   */
  static public boolean isSilence(String phone) {
    return phone.equals("pau");
  }
  
  /**
   * Determines if there is a vowel in the remainder of the array, 
   * starting at the given index.
   *
   * @param phones the set of phones to check
   * @param index start checking at this index
   *
   * @return <code>true</code> if a vowel is found; 
   *    otherwise <code>false</code>. 
   */
  static public boolean hasVowel(String[] phones, int index) {
    for (int i = index; i < phones.length; i++) {
      if (isVowel(phones[i]))
        return true;
    }
    return false;
  }
  
  /**
   * Determines if there is a vowel in given list of phones.
   *
   * @param phones the list of phones
   *
   * @return <code>true</code> if a vowel is found; 
   *    otherwise <code>false</code>. 
   */
  static public boolean hasVowel(List phones) {
    for (int i = 0; i < phones.size(); i++) {
      if (isVowel((String) phones.get(i))) 
        return true;
    }
    return false;
  }
      

  /**
   * Determines the sonority for the given phone.
   * 
   * @param phone the phone of interest
   * 
   * @return an integer that classifies phone transitions
   */
  public static int getSonority(String phone) 
  {
    if (isVowel(phone) || isSilence(phone)) {
        return 5;
    } else if (GLIDES_LIQUIDS.indexOf(phone.substring(0,1)) != -1) {
        return 4; 
    } else if (NASALS.indexOf(phone.substring(0,1)) != -1) {
        return 3;
    } else if (VOICED_OBSTRUENTS.indexOf(phone.substring(0,1)) != -1) {
        return 2;
    } else {
        return 1;
    }
  } 

  public static void main(String[] args)
  {
    System.out.println(TTS_PHONEMES.length);
  }
  
}// end
