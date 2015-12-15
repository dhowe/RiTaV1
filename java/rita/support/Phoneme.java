// $Id: Phoneme.java,v 1.1 2013/05/13 14:02:08 dev Exp $

package rita.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rita.RiLexicon;
import rita.RiString;
import rita.RiTa;

/**
 * Static utility methods for operations involving phonemes.
 * 
 * <pre>
 *         Phoneme   	Example     Translation    (39 entries)
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
 * </pre>
 */
public abstract class Phoneme implements Constants {

  static final private String VOWELS = "aeiou";
  static final private String GLIDES_LIQUIDS = "wylr";
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
    if (isVowel(phone)) {
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
   * Arpabet is the set of phonemes used by the CMU Pronouncing Dictionary. IPA
   * is the International Phonetic Alphabet.
   *
   * @param phones
   *          The Arpabet phonemic transcription to convert: 
   * @return The IPA equivalent of s.
   * @throws IllegalArgumentException
   *           if a phoneme is unknown.
   */
  public static String arpaToIPA(String phones) throws IllegalArgumentException {

    //System.out.println("Phoneme.arpaToIPA("+phones+")");
    
    String[] syllables = phones.trim().split(RiTa.WORD_BOUNDARY);
    StringBuffer ipaPhones = new StringBuffer();

    for (int i = 0; i < syllables.length; i++) {
      
      ipaPhones.append(syllableToIPA(syllables[i]));
    }
    
    return ipaPhones.toString();
  }

  protected static String syllableToIPA(String arpaSyl) {

    boolean stressed = false;
    StringBuffer ipaSyl = new StringBuffer();
    
    String[] arpaPhones = arpaSyl.trim().split(RiTa.PHONEME_BOUNDARY);
    
    for (int i = 0; i < arpaPhones.length; i++) {
      
      String arpaPhone = arpaPhones[i];
      char stress = arpaPhone.charAt(arpaPhone.length() - 1);
      if (stress == RiTa.STRESSED || stress == '2') {
	
        arpaPhone = arpaPhone.substring(0, arpaPhone.length() - 1);
        stressed = true; // TODO: what if we have an actual number?
      }
      
      ipaSyl.append(phoneToIPA(arpaPhone));
    }
 
    if (stressed) ipaSyl.insert(0, IPA_STRESS);
    
    return ipaSyl.toString();
  }

  protected static String phoneToIPA(String arpaPhone) {
    String ipaPhoneme = arpaMap.get(arpaPhone);
    if (ipaPhoneme == null) {
      throw new IllegalArgumentException("Unexpected Phoneme: " + arpaPhone);
    }
    return ipaPhoneme;
  }

  private static final Map<String, String> arpaMap;
  static {
    Map<String, String> amap = new HashMap<String, String>();
    amap.put("aa", "ɑ");
    amap.put("ae", "æ");
    amap.put("ah", "ʌ");
    amap.put("ao", "ɔ");
    amap.put("aw", "aʊ");
    amap.put("ay", "aɪ");
    amap.put("b", "b");
    amap.put("ch", "tʃ");
    amap.put("d", "d");
    amap.put("dh", "ð");
    amap.put("eh", "ɛ");
    amap.put("er", "ɚ");
    amap.put("ey", "eɪ");
    amap.put("f", "f");
    amap.put("g", "g");
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
    int k = 0;

    if (k == 0) {
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
	if (missing.length() > 0)
	  System.out.println(words[i] + " :: " + missing);
      }
    }
    if (k == 1) {
      String dict = RiTa.loadString("java/rita/rita_dict.js");
      for (int i = 0; i < RiTa.ALL_PHONES.length; i++) {
	int count = dict.length()
	    - dict.replace(RiTa.ALL_PHONES[i], "").length();
	System.out.println(RiTa.ALL_PHONES[i] + ": " + count);
      }
    }

    if (k == 2) {

      System.out.println(ALL_PHONES.length);
      for (int i = 0; i < ALL_PHONES.length; i++) {
	if (!arpaMap.containsKey(ALL_PHONES[i]))
	  System.out.println(ALL_PHONES[i]);
      }
      System.out.println(arpaMap.keySet().size());
    }
    if (k == 3) {
      RiLexicon rl = new RiLexicon();
      String[] words = rl.words();
      HashMap<String, String> syllables = new HashMap<String, String>();
      for (int i = 0; i < Math.min(words.length, 20); i++) {
	String word = words[i];
	String sylls = RiTa.getSyllables(word);
	syllables.put(word, sylls);
      }
      System.out.println("=========================================");
      RiLexicon.enabled = false;
      for (int i = 0; i < Math.min(words.length, 20); i++) {
	String word = words[i];
	String ltsSylls = RiTa.getSyllables(word);
	String sylss = syllables.get(word);
	boolean match = ltsSylls.equals(sylss);
	System.out.println(i + ") " + word + ": "
	    + (match ? "OK" : ltsSylls + " != " + sylss));
      }
    }

  }

}// end
