// $Id: Phoneme.java,v 1.1 2013/05/13 14:02:08 dev Exp $

package rita.support;

import java.util.*;

import rita.*;

/**
 * Static utility methods for operations involving phonemes.
 * 
 * <pre>
 *         Phoneme   	Example     Translation    (39 entries)
 *         ========================================================
 *           aa            odd         aa d
 *           ae            at          ae t
 *           ah            hut         hh ah t
 *           ao            ought       ao t
 *           aw            cow         k aw
 *           ay            hide        hh ay d
 *           b             be          b iy
 *           ch            cheese      ch iy z
 *           d             dee         d iy
 *           dh            thee        dh iy
 *           eh            ed          eh d
 *           er            hurt        hh er t
 *           ey            ate         ey t
 *           f             fee         f iy
 *           g             green       g r iy n
 *           hh            he          hh iy
 *           ih            it          ih t
 *           iy            eat         iy t
 *           jh            gee         jh iy
 *           k             key         k iy
 *           l             lee         l iy
 *           m             me          m iy
 *           n             knee        n iy
 *           ng            ping        p ih ng
 *           ow            oat         ow t
 *           oy            toy         t oy
 *           p             pee         p iy
 *           r             read        r iy d
 *           s             sea         s iy
 *           sh            she         sh iy
 *           t             tea         t iy
 *           th            theta       th ey t ah
 *           uh            hood        hh uh d
 *           uw            two         t uw
 *           v             vee         v iy
 *           w             we          w iy
 *           y             yield       y iy l d
 *           z             zee         z iy
 *           zh            seizure     s iy zh er
 * </pre>
 */
public abstract class Phoneme implements Constants {

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

  static HashMap<String, Integer> sonority;

  @SuppressWarnings("boxing")
  public static void initSonority() {

    sonority = new HashMap<String, Integer>();
    
    sonority.put("aa", 4);
    sonority.put("ae", 4);
    sonority.put("ah", 4);
    sonority.put("ao", 4);
    sonority.put("aw", 4);
    sonority.put("ay", 4);
    sonority.put("b", 0);
    sonority.put("ch", 0);
    sonority.put("d", 0);
    sonority.put("dh", 0);
    sonority.put("eh", 4);
    sonority.put("er", 4);
    sonority.put("ey", 4);
    sonority.put("f", 0);
    sonority.put("g", 0);
    sonority.put("hh", 0);
    sonority.put("ih", 4);
    sonority.put("iy", 4);
    sonority.put("jh", 0);
    sonority.put("k", 0);
    sonority.put("l", 2);
    sonority.put("m", 1);
    sonority.put("n", 1);
    sonority.put("ng", 1);
    sonority.put("ow", 4);
    sonority.put("oy", 4);
    sonority.put("p", 0);
    sonority.put("r", 2);
    sonority.put("s", 0);
    sonority.put("sh", 0);
    sonority.put("t", 0);
    sonority.put("th", 0);
    sonority.put("uh", 4);
    sonority.put("uw", 4);
    sonority.put("v", 0);
    sonority.put("w", 3);
    sonority.put("y", 3);
    sonority.put("z", 0);
    sonority.put("zh", 0);
    
    if (sonority.keySet().size() != ALL_PHONES.length)
      throw new RiTaException("Invalid sonority table!");
  }

  @SuppressWarnings("boxing")
  public static int getSonority(String phone) {
    if (sonority == null) initSonority();
    return (sonority.containsKey(phone)) ? sonority.get(phone) : -1;
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
    
    boolean needStress = true;
    
    if (syllables.length == 1) { // one-syllable words dont get stresses
      // syllables[0] = syllables[0].replaceAll("[\\d]", "");
      needStress = false;
    }

    for (int i = 0; i < syllables.length; i++) {
      
      String ipa = syllableToIPA(syllables[i], needStress);
      if (ipaPhones.length() > 0 && !ipa.startsWith(IPA_STRESS) && !ipa.startsWith(IPA_2NDSTRESS))
	ipa = " " + ipa;
      ipaPhones.append(ipa);
    }

    return ipaPhones.toString();
  }

  protected static String syllableToIPA(String arpaSyl, Boolean needStress) {

    boolean primarystressed = false;
    boolean secondarydStressed = false;
    
    // handle stressed vowel syllables see https://github.com/dhowe/RiTa/issues/296
    boolean isAAStressed = false;
    boolean isERStressed = false;
    boolean isIYStressed = false;
    boolean isAOStressed = false;
    boolean isUWStressed = false;
    
    boolean isAHStressed = false;
    boolean isAEStressed = false;
    StringBuffer ipaSyl = new StringBuffer();
    
    String[] arpaPhones = arpaSyl.trim().split(RiTa.PHONEME_BOUNDARY);
 
    for (int i = 0; i < arpaPhones.length; i++) {
      
      String arpaPhone = arpaPhones[i];
      //System.out.println(arpaPhone);
      
      char stress = arpaPhone.charAt(arpaPhone.length() - 1);
      
      if (stress == RiTa.UNSTRESSED) // no stress
        arpaPhone = arpaPhone.substring(0, arpaPhone.length() - 1);
      else if (stress == RiTa.STRESSED) { // primary stress
        arpaPhone = arpaPhone.substring(0, arpaPhone.length() - 1);
        primarystressed = true;

        if (arpaPhone.equals("aa")) isAAStressed = true;
        else if (arpaPhone.equals("er")) isUWStressed = true;
        else if (arpaPhone.equals("iy")) isIYStressed = true;
        else if (arpaPhone.equals("ao")) isUWStressed = true;
        else if (arpaPhone.equals("uw")) isUWStressed = true;
        
        else if (arpaPhone.equals("ah")) isAHStressed = true;
        else if (arpaPhone.equals("ae") && arpaPhones.length > 2 // 'at'
            && !arpaPhones[i > 0 ? i-1 : i].equals("th") // e.g. for 'thank', 'ae1' is always 'æ'
            && !arpaPhones[i > 0 ? i-1 : i].equals("dh") // 'that'
            && !arpaPhones[i > 0 ? i-1 : i].equals("m") // 'man'
            && !arpaPhones[i > 0 ? i-1 : i].equals("k")) // 'catnip'
          isAEStressed = true;
      }
      else if (stress == '2') {// secondary stress
	arpaPhone = arpaPhone.substring(0, arpaPhone.length() - 1);
	secondarydStressed = true;
	
	if (arpaPhone.equals("ah")) isAHStressed = true;
      }
      
      String IPASyl = phoneToIPA(arpaPhone);
      
      if (isAAStressed || isERStressed|| isIYStressed 
	  || isAOStressed || isUWStressed) IPASyl += "ː";
      
      else if (isAHStressed) IPASyl = "ʌ";
      else if (isAEStressed) IPASyl = "ɑː";
      
      isAAStressed = false;
      isERStressed = false;
      isIYStressed = false;
      isAOStressed = false;
      isUWStressed = false;
      
      isAHStressed = false;
      isAEStressed = false;
      
      ipaSyl.append(IPASyl);
    }
 
    if (needStress && primarystressed) ipaSyl.insert(0, IPA_STRESS);
    else if (needStress && secondarydStressed) ipaSyl.insert(0, IPA_2NDSTRESS);
    
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
    amap.put("aa", "ɑ"); // ɑ or ɒ
    amap.put("ae", "æ"); // ɑː or æ 
    amap.put("ah", "ə"); // ə for 'sofa', 'alone'; ʌ for 'but', 'sun'
    amap.put("ao", "ɔ");
    amap.put("aw", "aʊ");
    amap.put("ay", "aɪ");
    amap.put("b", "b");
    amap.put("ch", "tʃ");
    amap.put("d", "d");
    amap.put("dh", "ð");
    amap.put("eh", "ɛ");
    amap.put("er", "ə"); // ə or ɚ 
    amap.put("ey", "eɪ");
    amap.put("f", "f");
    amap.put("g", "g"); // g or ɡ (view the difference in notepad)
    amap.put("hh", "h");
    amap.put("ih", "ɪ");
    amap.put("iy", "i");
    amap.put("jh", "dʒ");
    amap.put("k", "k");
    amap.put("l", "l");
    amap.put("m", "m");
    amap.put("ng", "ŋ");
    amap.put("n", "n");
    amap.put("ow", "əʊ"); // əʊ for NAmE; or oʊ in BrE
    amap.put("oy", "ɔɪ");
    amap.put("p", "p");
    amap.put("r", "ɹ"); // r or ɹ
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
    int k = 5;

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
	System.out.println((i+1)+". "+ RiTa.ALL_PHONES[i] + ": " + count);
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
    if (k == 5) {
      String[] words = new RiLexicon().words();
      int num = Math.min(words.length, 100000), misses = 0;

      RiLexicon.enabled = false;
      String[] ltsPhones = new String[num];
      for (int i = 0; i < num; i++) {
	String word = words[i];
	ltsPhones[i] = mapToArpa(RiTa.getPhonemes(word));
      }
      RiLexicon.enabled = true;
      String[] ritaPhones = new String[num];
      for (int i = 0; i < num; i++) {
	String word = words[i];
	ritaPhones[i] = RiTa.getPhonemes(word);
      }
      for (int i = 0; i < num; i++) {
	if (!ltsPhones[i].equals(ritaPhones[i])) {
//	  System.out.println(words[i]);
//	  System.out.println(ritaPhones[i]);
//	  System.out.print(ltsPhones[i]);
//	  System.out.println("\t(lts)\n");
	  misses++;
	}
	if (ltsPhones[i].equals(words[i]) && words[i].length() > 1) 
	  System.out.print("\""+words[i]+"\",");
      }
      System.out.println();
      System.out.println((misses/(float)num)*100f+"% error-rate");
    }
  }

  private static String mapToArpa(String phonemes) {
    String[] phones = phonemes.split("-");
    for (int i = 0; i < phones.length; i++) {
      if (phones[i].equals("ax")) phones[i] = "ah";
    }
    return RiTa.join(phones,"-");
  }
  
}// end
