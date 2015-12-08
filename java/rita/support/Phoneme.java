// $Id: Phoneme.java,v 1.1 2013/05/13 14:02:08 dev Exp $

package rita.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rita.RiLexicon;
import rita.RiTa;

/**
 * Static utility methods for operations involving phonemes.
 * 
 * <pre>
 *         TIMIT-Phoneme   Example     Translation    (39 entries, 11 missing?)
 *         ========================================================
 *           AA            odd         AA D
 *           AE            at          AE T
 *           AH            hut         HH AH T
 *           AO            ought       AO T
 *           AW            cow         K AW
 *           AY            hide        HH AY D
 *           B             be          B IY
 *           CH            cheese      CH IY Z
 *           D             dee         D IY
 *           DH            thee        DH IY
 *           EH            Ed          EH D
 *           ER            hurt        HH ER T
 *           EY            ate         EY T
 *           F             fee         F IY
 *           G             green       G R IY N
 *           HH            he          HH IY
 *           IH            it          IH T
 *           IY            eat         IY T
 *           JH            gee         JH IY
 *           K             key         K IY
 *           L             lee         L IY
 *           M             me          M IY
 *           N             knee        N IY
 *           NG            ping        P IH NG
 *           OW            oat         OW T
 *           OY            toy         T OY
 *           P             pee         P IY
 *           R             read        R IY D
 *           S             sea         S IY
 *           SH            she         SH IY
 *           T             tea         T IY
 *           TH            theta       TH EY T AH
 *           UH            hood        HH UH D
 *           UW            two         T UW
 *           V             vee         V IY
 *           W             we          W IY
 *           Y             yield       Y IY L D
 *           Z             zee         Z IY
 *           ZH            seizure     S IY ZH ER
 *           
 *           
 *     SAMPA  IPA(hex), IPA(dec),           DESC                 29 Entries
 *     ======================================================================== 
 *     ('A',  '\u0251',  593,    "open back unrounded, Cardinal 5, Eng. start");
 *     ('{',  '\u00E6',  230,    "near-open front unrounded, Eng. trap");
 *     ('6',  '\u0250',  592,    "open schwa, Ger. besser");
 *     ('Q',  '\u0252',  594,    "open back rounded, Eng. lot");
 *     ('E',  '\u025B',  603,    "open-mid front unrounded, C3, Fr. m�me");
 *     ('@',  '\u0259',  601,    "schwa, Eng. banana");
 *     ('3',  '\u025C',  604,    "long mid central, Eng. nurse");
 *     ('I',  '\u026A',  618,    "lax close front unrounded, Eng. kit");
 *     ('O',  '\u0254',  596,    "open-mid back rounded, Eng. thought");
 *     ('2',  '\u00F8',  248,    "close-mid front rounded, Fr. deux");
 *     ('9',  '\u0153',  339,    "open-mid front rounded, Fr. neuf");
 *     ('&',  '\u0276',  630,    "open front rounded");
 *     ('U',  '\u028A',  650,    "lax close back rounded, Eng. foot");
 *     ('}',  '\u0289',  649,    "close central rounded, Swedish sju");
 *     ('V',  '\u028C',  652,    "open-mid back unrounded, Eng. strut");
 *     ('Y',  '\u028F',  655,    "lax [y], Ger. h�bsc");
 *     ('B',  '\u03B2',  946,    "voiced bilabial fricative, Sp. cabo");
 *     ('C',  '\u00E7',  231,    "voiceless palatal fricative, Ger. ich");
 *     ('D',  '\u00F0',  240,    "voiced dental fricative, Eng. then");
 *     ('G',  '\u0263',  611,    "voiced velar fricative, Sp. fuego");
 *     ('L',  '\u028E',  654,    "palatal lateral, It. famiglia");
 *     ('J',  '\u0272',  626,    "palatal nasal, Sp. a�o");
 *     ('N',  '\u014B',  331,    "velar nasal, Eng. thing");
 *     ('R',  '\u0281',  641,    "vd. uvular fric. or trill, Fr. roi");
 *     ('S',  '\u0283',  643,    "voiceless palatoalveolar fricative, Eng. ship");
 *     ('T',  '\u03B8',  952,    "voiceless dental fricative, Eng. thin");
 *     ('H',  '\u0265',  613,    "labial-palatal semivowel, Fr. huit");
 *     ('Z',  '\u0292',  658,    "vd. palatoalveolar fric., Eng. measure");
 *     ('?',  '\u0294',  660,    "glottal stop, Ger. Verein, also Danish st�d");
 * </pre>
 */
public abstract class Phoneme implements Constants {
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

  public static boolean isPhoneme(String phoneme) {
    for (int i = 0; i < ALL_PHONES.length; i++) {
      if (phoneme.equals(ALL_PHONES[i]))
	return true;
    }
    return false;
  }

  /**
   * Determines if the given phone is a vowel
   *
   * @param phone
   *          the phone to test
   *
   * @return <code>true</code> if phone is a vowel otherwise <code>false</code>.
   */
  static public boolean isVowel(String phone) {
    return VOWELS.indexOf(phone.substring(0, 1)) != -1;
  }

  /**
   * Determines if the given phone is a consonant
   *
   * @param phoneme
   *          the phone to test
   *
   * @return <code>true</code> if phone is a consonant otherwise
   *         <code>false</code>.
   */
  public static boolean isConsonant(String phoneme) {
    return !isVowel(phoneme); // ???
  }

  /**
   * Determines if the given phone represents a silent phone.
   *
   * @param phone
   *          the phone to test
   *
   * @return <code>true</code> if the phone represents a silent phone; otherwise
   *         <code>false</code>.
   */
  static public boolean isSilence(String phone) {
    return phone.equals("pau");
  }

  /**
   * Determines if there is a vowel in the remainder of the array, starting at
   * the given index.
   *
   * @param phones
   *          the set of phones to check
   * @param index
   *          start checking at this index
   *
   * @return <code>true</code> if a vowel is found; otherwise <code>false</code>
   *         .
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
   * @param phones
   *          the list of phones
   *
   * @return <code>true</code> if a vowel is found; otherwise <code>false</code>
   *         .
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
   * @param phone
   *          the phone of interest
   * 
   * @return an integer that classifies phone transitions
   */
  public static int getSonority(String phone) {
    if (isVowel(phone) || isSilence(phone)) {
      return 5;
    } else if (GLIDES_LIQUIDS.indexOf(phone.substring(0, 1)) != -1) {
      return 4;
    } else if (NASALS.indexOf(phone.substring(0, 1)) != -1) {
      return 3;
    } else if (VOICED_OBSTRUENTS.indexOf(phone.substring(0, 1)) != -1) {
      return 2;
    } else {
      return 1;
    }
  }

  /**
   * Converts an Arpabet phonemic transcription to an IPA phonemic
   * transcription. Note that, somewhat unusually, the stress symbol will
   * precede the vowel rather than the syllable. This is because Arpabet does
   * not mark syllable boundaries.
   *
   * Arpabet is the set of phonemes used by the CMU Pronouncing Dictionary. 
   * IPA is the International Phonetic Alphabet.
   *
   * @param The Arpabet phonemic transcription to convert.
   * @return The IPA equivalent of s.
   * @throws IllegalArgumentException
   *           if a phoneme is unknown.
   */
  public static String arpaToIPA(String s) throws IllegalArgumentException {
    String[] arpaPhonemes = s.trim().split("[ \\t]+");
    StringBuffer ipaPhonemes = new StringBuffer(s.length());

    for (String arpaPhoneme : arpaPhonemes) {
      char stressChar = arpaPhoneme.charAt(arpaPhoneme.length() - 1);
      if (stressChar == '0' || stressChar == '1' || stressChar == '2') {
	arpaPhoneme = arpaPhoneme.substring(0, arpaPhoneme.length() - 1);
	ipaPhonemes.append(arpaMap.get(Character.toString(stressChar)));
      }

      String ipaPhoneme = arpaMap.get(arpaPhoneme);
      if (ipaPhoneme == null) {
	throw new IllegalArgumentException();
      }
      ipaPhonemes.append(ipaPhoneme);
    }

    return ipaPhonemes.toString();
  }

  private static final Map<String, String> arpaMap;
  static {
    Map<String, String> amap = new HashMap<String, String>();
    amap.put("aa", "ɑ");
    amap.put("ae", "æ");
    amap.put("ah", "ʌ");
    amap.put("ao", "ɔ");
    amap.put("aw", "aʊ");
    amap.put("ax", "ə");
    amap.put("ay", "aɪ");
    amap.put("b", "b");
    amap.put("ch", "tʃ");
    amap.put("d", "d");
    amap.put("dh", "ð");
    //amap.put("dx", "?");
    amap.put("eh", "ɛ");
    amap.put("er", "ɚ");
    amap.put("ey", "eɪ");
    amap.put("f", "f");
    amap.put("g", "?");
    amap.put("hh", "h");
    amap.put("ih", "ɪ");
    amap.put("iy", "i");
    amap.put("jh", "dʒ");
    amap.put("k", "k");
    amap.put("l", "l");
    amap.put("m", "m");
    amap.put("ng", "ŋ");
    amap.put("n", "n");
    amap.put("ow", "oʊ");
    amap.put("oy", "ɔɪ");
    amap.put("p", "p");
    amap.put("r", "ɹ");
    amap.put("sh", "ʃ");
    amap.put("s", "s");
    amap.put("th", "θ");
    amap.put("t", "t");
    amap.put("uh", "ʊ");
    amap.put("uw", "u");
    amap.put("v", "v");
    amap.put("w", "w");
    amap.put("y", "j");
    amap.put("z", "z");
    amap.put("zh", "ʒ");
    arpaMap = Collections.unmodifiableMap(amap);
  }

  public static void main(String[] args) {
    int k = 2;

    if (k==0) {
      RiLexicon rl = new RiLexicon();
      String[] words = rl.words();
      List allPhones = Arrays.asList(RiTa.ALL_PHONES);
      for (int i = 0; i < words.length; i++) {
	String phonestr = RiTa.getPhonemes(words[i]);
	String[] phones = phonestr.split("-");
	String missing = "";
	for (int j = 0; j < phones.length; j++) {
	  if (!allPhones.contains(phones[j]))
	    missing += phones[j] + " ";
	}
	if (missing.length()>0)
	  System.out.println(words[i] + " :: "+missing);
      }
    }
    if (k==1) {
      String dict = RiTa.loadString("java/rita/rita_dict.js");
      for (int i = 0; i < RiTa.ALL_PHONES.length; i++) {
        int count = dict.length() - dict.replace(RiTa.ALL_PHONES[i], "").length();
        System.out.println(RiTa.ALL_PHONES[i]+": "+count);
      }
    }
    
    if (k==2) {
  
      System.out.println(ALL_PHONES.length);
      for (int i = 0; i < ALL_PHONES.length; i++) {
        if (!arpaMap.containsKey(ALL_PHONES[i]))
  	System.out.println(ALL_PHONES[i]);
      }
      System.out.println(arpaMap.keySet().size());
    }
  }

}// end
