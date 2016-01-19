package rita.test;

import static rita.support.QUnitStubs.equal;

import org.junit.Test;

import rita.RiLexicon;
import rita.support.Constants;
import rita.support.Phoneme;

public class PhonemeTests implements Constants {

  @Test
  public void testWordToIPA1() {
    
    // 20 different examples without using LTS 
    String[] words = { "become", "parsley", "garlic", "fall", "frost", "you", "going",
	"should", "say", "how", "now", "coat", "ratio", "trade", "treat", "begin",
	"end", "encounter", "range", "step"
    };
    String[] results = { "bɪˈkʌm", "ˈpɑːɹs li", "ˈgɑːɹ lɪk", "fɔːl", "fɹɔːst", "juː", "ˈgəʊ ɪŋ",
	"ʃʊd", "seɪ", "haʊ", "naʊ", "kəʊt", "ˈɹeɪ ʃi əʊ", "tɹeɪd", "tɹiːt", "bɪˈgɪn",
	"ɛnd", "ɪnˈkaʊn tə", "ɹeɪndʒ", "stɛp"
    };

    RiLexicon rl = new RiLexicon();
    for (int i = 0; i < words.length; i++) {
      String data = rl.getRawPhones(words[i], true);
      // System.out.println(i + ") " + words[i] + " -> " + data + " -> " + results[i]);
      equal(results[i], Phoneme.arpaToIPA(data));
    }
  }
  
  @Test
  public void testWordToIPA2() {
    
    for (int i = 0; i < tests.length; i += 2) {

      // test with stresses
      String ipa = Phoneme.arpaToIPA(tests[i]);
      // System.out.println("expected " + tests[i + 1] + ", got " + ipa);
      equal(tests[i + 1], ipa);
      
      // test without stresses
      tests[i] = tests[i].replaceAll("[\\d]", "");
      String noStress = tests[i + 1].replaceAll(Phoneme.IPA_STRESS, "")
	  .replaceAll(Phoneme.IPA_2NDSTRESS, "").replaceAll("ː", "");
      ipa = Phoneme.arpaToIPA(tests[i]);
      if (tests[i].contains("ah") || tests[i].contains("ae"))
	org.junit.Assume.assumeTrue(true);
      else
	equal(noStress.replaceAll(" ", ""), ipa.replaceAll(" ", ""));
    }
  }
  
  @Test
  public void testStressedVowelSyl() {
    // handle stressed vowel syllables  

    String[] words = { 
	"partisan", "afar", "ah",			// aa1
	"absurd", "adverse", "alert",			// er1
	"treat", "eve", "evening", "exceed",		// iy1
	"adjourn", "adore", "advertorial",		// ao1
	"illusory", "aloof", "amuse"};			// uw1
    
    // compare the result on http://www.thefreedictionary.com/
    String[] results = { 
	"ˈpɑːɹ tə zən", "əˈfɑːɹ", "ɑː",
	"əbˈsəːd", "ædˈvəːs", "əˈləːt",
	"tɹiːt", "iːv", "ˈiːv nɪŋ", "ɪkˈsiːd",
	"əˈdʒəːn", "əˈdɔːɹ", "æd vəˈtɔː ɹi əl",
	"ɪˈluː sə i", "əˈluːf", "əˈmjuːz"};

    RiLexicon rl = new RiLexicon();
    for (int i = 0; i < words.length; i++) {
      String data = rl.getRawPhones(words[i], true);
      // System.out.println(i + ") " + words[i] + " -> " + data + " -> " + results[i]);
      equal(results[i], Phoneme.arpaToIPA(data));
    }
  }
  
  @Test
  public void testAH() {
    // ə for 'sofa', 'alone'; ʌ for 'but', 'sun'
    // if “uh” sound is in an unstressed syllable, it’s /ə/
    // if “uh” sound is occurring in a stressed syllable, it’s /ʌ/
    
    String[] words = { "alone", "but", "sun", };
    String[] results = { "əˈləʊn", "bʌt", "sʌn" };

    RiLexicon rl = new RiLexicon();
    for (int i = 0; i < words.length; i++) {
      String data = rl.getRawPhones(words[i], true);
      // System.out.println(i + ") " + words[i] + " -> " + data + " -> " + results[i]);
      equal(results[i], Phoneme.arpaToIPA(data));
    }
  }
  
  @Test
  public void testAmericanPronounciation() {
    // test when to use 'ɑː' or 'æ' 
    // if 'ae1' replace 'ae1' with 'ɑː' instead of 'æ' with a few exceptions
    
    String[] words = { "staff", "fast", "ask", "at", "thank", "that", "man", "catnip" };
    String[] results = { "stɑːf", "fɑːst", "ɑːsk", "æt", "θæŋk", "ðæt", "mæn", "ˈkætˈnɪp" };

    RiLexicon rl = new RiLexicon();
    for (int i = 0; i < words.length; i++) {
      String data = rl.getRawPhones(words[i], true);
      // System.out.println(i + ") " + words[i] + " -> " + data + " -> " + results[i]);
      equal(results[i], Phoneme.arpaToIPA(data));
    }
  }

  static String[] tests2 = { "that", "ðæt", "however", "haʊˈevəʳ", "difficult",
      "ˈdɪfɪkəlt", "another", "əˈnʌðəʳ", "you", "ju:", "again", "əˈgen",
      "which", "wɪtʃ", "world", "wɜ:ʳld", "their", "ðeəʳ", "area", "ˈeəriə",
      "about", "əˈbaʊt", "psychology", "saɪˈkɒlədʒi", "photo", "ˈfoʊtoʊ",
      "course", "kɔ:ʳs", "should", "ʃʊd", "company", "ˈkʌmpəni", "people",
      "ˈpi:pəl", "under", "ˈʌndəʳ", "also", "ˈɔ:lsoʊ", "problem", "ˈprɒbləm",
      "between", "bɪˈtwi:n", "never", "ˈnevəʳ", "many", "ˈmeni", "service",
      "ˈsɜ:ʳvɪs", "thicker", "ˈθɪkəʳ", "something", "ˈsʌmθɪŋ", "child",
      "tʃaɪld", "place", "pleɪs", "hear", "hɪəʳ", "point", "pɔɪnt", "system",
      "ˈsɪstəm", "provide", "prəˈvaɪd", "group", "gru:p", "large", "lɑ:ʳdʒ",
      "number", "ˈnʌmbəʳ", "general", "ˈdʒenərəl", "always", "ˈɔ:lweɪz",
      "head", "hed", "next", "nekst", "information", "ɪnfəʳ", "ˈmeɪʃən",
      "quick", "kwɪk", "question", "ˈkwestʃən", "nervous", "ˈnɜ:ʳvəs",
      "business", "ˈbɪznɪs", "local", "ˈloʊkəl", "power", "ˈpaʊəʳ", "during",
      "ˈdjʊərɪŋ", "change", "tʃeɪndʒ", "although", "ɔ:lˈðoʊ", "move", "mu:v",
      "who", "hu:", "book", "bʊk", "example", "ɪgˈzæmpəl", "development",
      "dɪˈveləpmənt", "rather", "ˈræðəʳ", "young", "jʌŋ", "social", "ˈsoʊʃəl",
      "national", "ˈnæʃənəl", "write", "raɪt", "water", "ˈwɔ:təʳ", "percent",
      "pəʳ", "ˈsent", "yet", "jet", "guest", "gest", "perhaps", "pəʳ", "ˈhæps",
      "both", "boʊθ", "until", "ʌnˈtɪl", "every", "ˈevri", "control",
      "kənˈtroʊl", "month", "mʌnθ", "include", "ɪnˈklu:d", "important",
      "ɪmˈpɔ:ʳtənt", "believe", "bɪˈli:v", "allow", "əˈlaʊ", "person",
      "ˈpɜ:ʳsən", "stand", "stænd", "once", "wʌns", "idea", "aɪˈdi:ə",
      "police", "pəˈli:s", "character", "ˈkærɪktəʳ", "lose", "lu:z", "result",
      "rɪˈzʌlt", "position", "pəˈzɪʃən", "happen", "ˈhæpən", "industry",
      "ˈɪndəstri", "friend", "frend", "major", "ˈmeɪdʒəʳ", "carry", "ˈkæri",
      "build", "bɪld", "awful", "ˈɔ:fəl", "language", "ˈlæŋgwɪdʒ", "early",
      "ˈɜ:ʳli", "international", "ɪntəʳ", "ˈnæʃənəl", "view", "vju:", "else",
      "els", "himself", "hɪmˈself", "yeah", "jeə", "xerox", "ˈzɪərɒks",
      "center", "ˈsentəʳ", "report", "rɪˈpɔ:ʳt", "enough", "ɪˈnʌf",
      "political", "pəˈlɪtɪkəl", "calm", "kɑ:m", "law", "lɔ:", "color",
      "ˈkʌləʳ", "ghost", "goʊst", "lure", "lʊəʳ", "modest", "ˈmɒdɪst", "knife",
      "naɪf" };

  static String[] tests = {
      //"b-ih k-ah1-m", "bɪˈkʌm",

      // from http://web.stanford.edu/class/linguist238/fig04.01.pdf
      "p-aa1-r-s l-iy0", "ˈpɑːɹs li", 
      "k-ae1-t n-ih0-p", "ˈkæt nɪp", 
      "b-ey1", "beɪ", 
      "d-ih1-l", "dɪl", 
      "g-aa1-r l-ih0-k", "ˈgɑːɹ lɪk", 
      "m-ih1-n-t", "mɪnt", 
      "n-ah1-t m-eh2-g", "ˈnʌtˌmɛg", 
      "jh-ih1-n s-eh2-ng", "ˈdʒɪnˌsɛŋ", 
      "f-eh1 n-l", "ˈfɛ nl", 
      "s-ey1-jh", "seɪdʒ", 
      "hh-ey1 z-ah0-l n-ah2-t", "ˈheɪ zəlˌnʌt", 
      "s-k-w-aa1-sh", "skwɑːʃ", 
      "ae0-m b-r-ow1 zh-ah0", "æmˈbɹəʊ ʒə", 
      "l-ih1 k-r ih0-sh", "ˈlɪ kɹ ɪʃ", 
      "k-iy1 w-iy0", "ˈkiː wi",
      "y-uw1", "juː", 
      "hh-ao1-r-s r-ae2 d-ih0-sh", "ˈhɔːɹsˌɹæ dɪʃ",
      "ah1 ow", "ˈʌ əʊ",
      "b-ah1 t-er0", "ˈbʌ tə",
      "th-ih1-s ah0-l", "ˈθɪs əl",

      // from https://en.wikipedia.org/wiki/arpabet
      "ao1-f", "ɔːf", 
      "f-ao1-l", "fɔːl", "f-r-ao1-s-t", "fɹɔːst",
      "f-aa1 dh-er", "ˈfɑː ðə", "k-aa1-t", "kɑːt",
      "b-iy1", "biː", "sh-iy1", "ʃiː",
      "n-uw1", "nuː", "f-uw1-d", "fuːd",
      "r-eh1-d", "ɹɛd", "m-eh1-n", "mɛn",
      "b-ih1-g", "bɪg", "w-ih1-n", "wɪn",
      "sh-uh1-d", "ʃʊd", "k-uh1-d", "kʊd",
      "b-ah1-t", "bʌt", "s-ah1-n", "sʌn",
      "s-ow1 f-ah0", "ˈsəʊ fə", 
      "ah0 l-ow1-n", "əˈləʊn",
      "d-ih0 s-k-ah1-s", "dɪˈskʌs",
      "ae1-t", "æt", 
      "f-ae1-s-t", "fɑːst",
      "s-ey1", "seɪ", "ey1-t", "eɪt",
      "m-ay1", "maɪ", "w-ay1", "waɪ", "r-ay1-d", "ɹaɪd",
      "sh-ow1", "ʃəʊ", "k-ow1-t", "kəʊt",
      "hh-aw1", "haʊ", "n-aw1", "naʊ",
      "b-oy1", "bɔɪ", "t-oy1", "tɔɪ",
      "hh-er0", "hə", "b-er1-d", "bəːd", "hh-er1-t", "həːt", 
      "n-er1-s", "nəːs",
      "k-aw1 er-d", "ˈkaʊ əd",
      "eh1-r", "ɛɹ", "w-eh1-r", "wɛɹ", "hh-eh1-r", "hɛɹ",
      "k-y-uh1-r", "kjʊɹ", "b-y-uh1 r-ow0", "ˈbjʊ ɹəʊ", 
      "d-ih0 t-er1", "dɪˈtəː",
      "m-ao1-r", "mɔːɹ", "b-ao1-r-d", "bɔːɹd", "k-ao1-r-d", "kɔːɹd",
      "l-aa1-r-jh", "lɑːɹdʒ", "hh-aa1-r-d", "hɑːɹd",
      "iy1-r", "iːɹ", "n-ih1-r", "nɪɹ",
      "f-l-aw1 r", "ˈflaʊ ɹ",
      "p-ey1", "peɪ", "b-ay1", "baɪ", "t-ey1-k", "teɪk", "d-ey1", "deɪ",
      "k-iy1", "kiː", "g-ow1", "gəʊ",
      "ch-eh1-r", "tʃɛɹ",
      "jh-ah1-s-t", "dʒʌst", "jh-ih1-m", "dʒɪm",
      "f-ao1-r", "fɔːɹ",
      "v-eh1 r-iy0", "ˈvɛ ɹi",
      "th-ae1-ng-k-s", "θæŋks", // ɑː or æ 
      "th-er1-z d-ey2", "ˈθəːzˌdeɪ", // or "ˈθɜrzdi"
      "dh-ae1-t", "ðæt", // ɑː or æ 
      "dh-ah0", "ðə", 
      "dh-eh1-m", "ðɛm",
      "s-ey1", "seɪ",
      "z-uw1", "zuː",
      "sh-ow1", "ʃəʊ",
      "m-eh1 zh-er0", "ˈmɛ ʒə", 
      "p-l-eh1 zh-er", "ˈplɛ ʒə",
      "hh-aw1-s", "haʊs",
      "m-ae1-n", "mæn",
      "n-ow1", "nəʊ", 
      "b-ah1 t-ah0-n", "ˈbʌ tən", 
      "s-ih1-ng", "sɪŋ",
      "l-ey1-t", "leɪt", 
      "b-aa1-t ah0-l", "ˈbɑːt əl", 
      "r-ah1-n", "ɹʌn",
      "y-eh1-s", "jɛs", 
      "w-ey1", "weɪ" };

  public static void main(String[] args) {

    String[] words = { "become", "parsley", "catnip", "garlic" };
    String[] results = { "bɪˈkʌm", "ˈpɑɹsli", "ˈkætnɪp", "ˈgɑɹlɪk" };

    RiLexicon rl = new RiLexicon();
    for (int i = 0; i < words.length; i++) {
      System.out.println(i + ") " + Phoneme.arpaToIPA(rl.getRawPhones(words[i], true)));
    }
  }
}