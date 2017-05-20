package rita.test;

import static rita.support.QUnitStubs.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import rita.RiLexicon;
import rita.RiTa;

public class RiLexViaRiTa {
  
  static {
    RiTa.SILENT = RiLexicon.SILENCE_LTS = true;
  }
  
  @Test
  public void testAddWordStringStringString() {

    RiTa.addWord("bananana", "b-ah-n ae1-n ae1-n ah", "nn");
    ok(RiTa.containsWord("bananana"));

    RiTa.addWord("hehehe", "hh-ee1 hh-ee1 hh-ee1", "uh");
    ok(RiTa.containsWord("hehehe"));

    RiTa.addWord("HAHAHA", "hh-aa1 hh-aa1 hh-aa1", "uh");
    ok(RiTa.containsWord("HAHAHA"));

    RiTa.addWord("", "", "");

    RiTa.reload(); // reset
  }

  @Test
  public void testClear() {
    
    ok(RiTa.containsWord("banana"));
    ok(!RiTa.containsWord("wonderfullyy"));

    HashMap obj = new HashMap();
    obj.put("wonderfullyy", "w-ah1-n-d er-f ah-l iy | rb");
    RiTa.lexicalData(obj);
 
    ok(RiTa.size() == 1);
    deepEqual(RiTa.lexicalData(), obj);
    ok(!RiTa.containsWord("wonderfully"));
    ok(RiTa.containsWord("wonderfullyy"));

    RiTa.reload();
    
    ok(RiTa.containsWord("zoom"));
    ok(RiTa.containsWord("a"));
    ok(!RiTa.containsWord("wonderfullyy"));
    ok(RiTa.containsWord("wonderful"));
    
    ok(RiTa.containsWord("zoom"));
    ok(RiTa.containsWord("a"));
  }

  @Test
  public void testContainsWordString() {
    
    ok(RiTa.containsWord("cat"));
    ok(!RiTa.containsWord("cated"));
    ok(RiTa.containsWord("funny"));
    ok(RiTa.containsWord("shit"));
    ok(!RiTa.containsWord("123"));
    ok(!RiTa.containsWord("hellx"));
    ok(RiTa.containsWord("hello"));
    ok(RiTa.containsWord("HeLLo"));
    ok(RiTa.containsWord("HELLO"));

    // plurals
    ok(RiTa.containsWord("cats"));
    ok(RiTa.containsWord("boxes"));
    ok(RiTa.containsWord("teeth"));
    ok(RiTa.containsWord("apples"));
    ok(RiTa.containsWord("buses"));
    ok(RiTa.containsWord("oxen"));
    ok(RiTa.containsWord("theses"));
    ok(RiTa.containsWord("stimuli"));
    ok(RiTa.containsWord("crises"));
    ok(RiTa.containsWord("media"));
    //ok(RiTa.containsWord("prognoses")); // TODO: failing in java


    //vb* ?
    ok(RiTa.containsWord("runs"));
    ok(RiTa.containsWord("running"));
    ok(RiTa.containsWord("ran"));
    ok(RiTa.containsWord("moved"));
    ok(RiTa.containsWord("went"));
    ok(RiTa.containsWord("spent"));
  }

  @Test
  public void testAlliterationsString() {

    String[] result = new String[] {};
    result = RiTa.alliterations("dog");
    ok(result.length > 1000);
    
    result = RiTa.alliterations("cat");
    ok(result.length > 1000);

    result = RiTa.alliterations("apples");
    ok(result.length == 0);

    result = RiTa.alliterations("no stress");
    // RiTa.out(result);
    ok(result.length == 0);
    
//    result = RiTa.alliterations("URL");
//    System.out.println(result);
//    ok(result.length == 0);
    
    result = new String[] {};
    result = RiTa.alliterations("#$%^&*");
    ok(result.length == 0); 

    result = new String[] {};
    result = RiTa.alliterations("");
    ok(result.length == 0);

  }

  @Test
  public void testAlliterationsStringInt() {

    String[] result = RiTa.alliterations("dog", 15);
//    System.out.println(result[0]+" " +result[1]);
//    ok(result.length == 4);
    
    result = RiTa.alliterations("cat", 17);
//    System.out.println(result[0]+" " +result[1]);
//    System.out.println("RiLexiconTest.testAlliterationsInt() :: "+result.length);

    ok(result.length == 6);
    
    // TODO: better tests
  }


  @Test
  public void testLexicalData() {

    Map result = new HashMap();
    result = RiTa.lexicalData();
    ok(result.size() > 1000);

    Map re = new HashMap();
    re = RiTa.lexicalData();
    String expected = "ey1|dt";
    String returned = (String) re.get("a");
    deepEqual(returned, expected);

    re = RiTa.lexicalData();
    returned = (String) re.get("the");
    expected = "dh-ah|dt";
    deepEqual(returned, expected);

    // System.out.println("lexicalData containsKey('a'): "+result.containsKey("a"));
    // System.out.println("lexicalData : "+result.get("a"));
    String answer = (String) result.get("a");
    deepEqual(answer, "ey1|dt");

//    RiTa.lexicalData().put("hello", null);
//    RiTa.containsWord("hello");
  }

  @Test
  public void testRandomWord() {
  


    String result = RiTa.randomWord();
    for (int i = 0; i < 10; i++) {
      ok(result.length() > 0);
    }
  }

  @Test
  public void testRandomWordInt() {
    


    for (int i = 0; i < 10; i++) {
      String result = RiTa.randomWord(3);

      // RiTa.out(result);

      ok(result.length() > 0);
      String syllables = RiTa.getSyllables(result);

      int num = syllables.split(RiTa.SYLLABLE_BOUNDARY).length;
      ok(num == 3);// "3 syllableCount: "
    }

    for (int i = 0; i < 10; i++) {
      String result = RiTa.randomWord(5);
      String syllables = RiTa.getSyllables(result);
      int num = syllables.split(RiTa.SYLLABLE_BOUNDARY).length;
      if (num != 5)
	System.err.println("[WARN] '" + result
	    + "' has wrong syllable count 5 != " + num);
      ok(num == 5); // "5 syllableCount: "
    }
  }

  @Test
  public void testRandomWordString() {
    
    String[] pos = { "nn", "jj", "jjr", "wp" };
    for (int j = 0; j < pos.length; j++) {
      for (int i = 0; i < 3; i++) {
	String result = RiTa.randomWord(pos[j]);
	String best = RiTa.getBestPos(result);
	// System.out.println(result+": "+pos[j]+" ?= "+best);
	equal(pos[j], best);
      }
    }
  }

  @Test
  public void testRandomWordStringInt() {
    


    String[] pos = { "nn", "jj" };

    for (int j = 0; j < pos.length; j++) {
      for (int k = 2; k < 5; k++) {
	String result = RiTa.randomWord(pos[j], k);
	String best = RiTa.getBestPos(result);
	// System.out.println(result+": "+pos[j]+" ?= "+best);
	equal(pos[j], best);

	String syllables = RiTa.getSyllables(result);
	int num = syllables.split(RiTa.SYLLABLE_BOUNDARY).length;
	ok(num == k);
      }
    }

    String result = RiTa.randomWord("wp", 5);
    equal(result, "");

    result = RiTa.randomWord("nn", 30);
    ok(result == "");
  }
  

  @Test
  public void testRhymesString() {
    

    
    String[] result = RiTa.rhymes("apple");
    String[] answer = { "chapel", "grapple" };
    deepEqual(answer, result);

    result = RiTa.rhymes("bible");
    answer = new String[] { "libel", "tribal" };
    deepEqual(answer, result);
    
//    result = RiTa.rhymes("goxgle");
//    RiTa.out(result);
//    answer = new String[] {};
//    deepEqual(answer, result);
    
    result = RiTa.rhymes("google");
    answer = new String[] { "bugle", "frugal" };
    deepEqual(answer, result);
    
    result = RiTa.rhymes("happens in here"); // "hear" should NOT be a rhyme
    answer = new String[] { "insincere", "persevere", "career",
	"year", "reappear", "brigadier", "pioneer", "rear","profiteer", "commandeer", "near", "revere",
	"beer", "fear", "sneer", "conventioneer", "summiteer", "adhere", "veer", "volunteer",
	"sear", "sincere", "smear", "gear", "deer", "here", "marketeer", "queer",
	"financier", "cavalier", "rainier", "mutineer", "unclear", "pamphleteer",
	"disappear", "austere", "veneer", "domineer", "overhear", "auctioneer", "spear",
	"pier", "sphere", "peer", "cashier", "ear", "sheer", "steer", "dear", 
	"hear", "souvenir", "frontier", "chandelier", "shear", "clear", 
	"premier", "rehear", "engineer", "premiere", "cheer", "appear", 
	"oneyear", "severe", "mere", "interfere", "racketeer", "budgeteer"
    };
    
    deepEqual(answer, result);
    
    // test that we ignore trailing punctuation
    deepEqual(RiTa.rhymes("apple"), RiTa.rhymes("apple."));
    
    result = RiTa.rhymes("");
    answer = new String[] {};
    deepEqual(answer, result);
  }

  @Test
  public void testWords() {
    

    String[] result = RiTa.words();
    ok(result.length > 23000);
  }
  
  @Test
  public void testWordsString() {

    String[] result = RiTa.words("ee");
    
    for (int i = 0; i < result.length; i++)
      result[i].matches("^.*ee.*$");

    result = RiTa.words("tt");
    for (int i = 0; i < result.length; i++)
      result[i].matches("^.*tt.*$");
    
    result = RiTa.words("ee.*ee");
    String[] answer = new String[] { "freewheeling", "squeegee" };
    deepEqual(result, answer);

  }

  @Test
  public void testWordsStringBoolean() {


    String[] result = RiTa.words("ee", true);
    String[] result2 = RiTa.words("ee", false);
    ok(result.length > 20);
    ok(result2.length > 20);
    
    boolean diff = false;
    for (int i = 0; i < 20; i++) {
      if (!result[i].equals(result2[i]))
	diff = true;
    }
    ok(diff);
    
    // verify not sorted unless specified
    result = RiTa.words("tt", true);
    result2 = RiTa.words("tt", false);
    ok(result.length > 20);
    ok(result2.length > 20);
    diff = false;
    for (int i = 0; i < 20; i++) {
      if (!result[i].equals(result2[i]))
	diff = true;
    }
    
    ok(diff);
  }

  @Test
  public void testIsAdverbString() {


    ok(!RiTa.isAdverb("swim"));
    ok(!RiTa.isAdverb("walk"));
    ok(!RiTa.isAdverb("walker"));
    ok(!RiTa.isAdverb("beautiful"));
    ok(!RiTa.isAdverb("dance"));
    ok(!RiTa.isAdverb("dancing"));
    ok(!RiTa.isAdverb("dancer"));

    // verb
    ok(!RiTa.isAdverb("wash"));
    ok(!RiTa.isAdverb("walk"));
    ok(!RiTa.isAdverb("play"));
    ok(!RiTa.isAdverb("throw"));
    ok(!RiTa.isAdverb("drink"));
    ok(!RiTa.isAdverb("eat"));
    ok(!RiTa.isAdverb("chew"));

    // adj
    ok(!RiTa.isAdverb("wet"));
    ok(!RiTa.isAdverb("dry"));
    ok(!RiTa.isAdverb("furry"));
    ok(!RiTa.isAdverb("sad"));
    ok(!RiTa.isAdverb("happy"));

    // n
    ok(!RiTa.isAdverb("dogs"));
    ok(!RiTa.isAdverb("wind"));
    ok(!RiTa.isAdverb("dolls"));
    ok(!RiTa.isAdverb("frogs"));
    ok(!RiTa.isAdverb("ducks"));
    ok(!RiTa.isAdverb("flowers"));
    ok(!RiTa.isAdverb("fish"));

    // adv
    ok(RiTa.isAdverb("truthfully"));
    ok(RiTa.isAdverb("kindly"));
    ok(RiTa.isAdverb("bravely"));
    ok(RiTa.isAdverb("doggedly"));
    ok(RiTa.isAdverb("sleepily"));
    ok(RiTa.isAdverb("excitedly"));
    ok(RiTa.isAdverb("energetically"));
    ok(RiTa.isAdverb("hard"));

    ok(!RiTa.isAdverb(""));

    try {
      ok(RiTa.isAdverb("banana split"));
      ok(false);
    } catch (Exception e) {
      ok(e);
    }

  }

  @Test
  public void testIsNounString() {

    ok(RiTa.isNoun("swim"));
    ok(RiTa.isNoun("walk"));
    ok(RiTa.isNoun("walker"));
    ok(RiTa.isNoun("dance"));
    ok(RiTa.isNoun("dancing"));
    ok(RiTa.isNoun("dancer"));

    // verb
    ok(RiTa.isNoun("wash"));
    ok(RiTa.isNoun("walk"));
    ok(RiTa.isNoun("play"));
    ok(RiTa.isNoun("throw"));
    ok(RiTa.isNoun("drink"));
    ok(!RiTa.isNoun("eat"));
    ok(!RiTa.isNoun("chew"));

    // adj
    ok(!RiTa.isNoun("hard"));
    ok(!RiTa.isNoun("dry"));
    ok(!RiTa.isNoun("furry"));
    ok(!RiTa.isNoun("sad"));
    ok(!RiTa.isNoun("happy"));
    ok(!RiTa.isNoun("beautiful"));

    // n
    ok(RiTa.isNoun("dogs"));
    ok(RiTa.isNoun("wind"));
    ok(RiTa.isNoun("dolls"));
    ok(RiTa.isNoun("frogs"));
    ok(RiTa.isNoun("ducks"));
    ok(RiTa.isNoun("flowers"));
    ok(RiTa.isNoun("fish"));
    ok(RiTa.isNoun("wet"));
    ok(RiTa.isNoun("walk"));
    ok(RiTa.isNoun("drink"));

    // adv
    ok(!RiTa.isNoun("truthfully"));
    ok(!RiTa.isNoun("kindly"));
    ok(!RiTa.isNoun("bravely"));
    ok(!RiTa.isNoun("scarily"));
    ok(!RiTa.isNoun("sleepily"));
    ok(!RiTa.isNoun("excitedly"));
    ok(!RiTa.isNoun("energetically"));

    ok(!RiTa.isNoun(""));

    try {
      ok(RiTa.isNoun("banana split"));
    } catch (Exception e) {
      ok(e);
    }
  }

  @Test
  public void testIsVerbString() {


    ok(RiTa.isVerb("dance"));
    ok(RiTa.isVerb("swim"));
    ok(RiTa.isVerb("walk"));
    ok(!RiTa.isVerb("walker"));
    ok(!RiTa.isVerb("beautiful"));
    ok(RiTa.isVerb("dancing"));
    ok(!RiTa.isVerb("dancer"));

    // verb
    ok(RiTa.isVerb("eat"));
    ok(RiTa.isVerb("chew"));
    ok(RiTa.isVerb("throw")); // +n
    ok(RiTa.isVerb("walk")); // +n
    ok(RiTa.isVerb("wash")); // +n
    ok(RiTa.isVerb("drink")); // +n
//    ok(RiTa.isVerb("ducks")); // +n -> KnownIssue
    ok(RiTa.isVerb("fish")); // +n
//    ok(RiTa.isVerb("dogs")); // +n -> KnownIssue
    ok(RiTa.isVerb("wind")); // +n
    ok(RiTa.isVerb("wet")); // +adj
    ok(RiTa.isVerb("dry")); // +adj

    // adj
    ok(!RiTa.isVerb("hard"));
    ok(!RiTa.isVerb("furry"));
    ok(!RiTa.isVerb("sad"));
    ok(!RiTa.isVerb("happy"));
    ok(RiTa.isVerb("wet")); // +adj
    ok(RiTa.isVerb("dry")); // +adj

    // n
//    ok(!RiTa.isVerb("dolls"));
//    ok(!RiTa.isVerb("frogs"));
//    ok(!RiTa.isVerb("flowers")); -> KnownIssue
    
    // adv
    ok(!RiTa.isVerb("truthfully"));
    ok(!RiTa.isVerb("kindly"));
    ok(!RiTa.isVerb("bravely"));
    ok(!RiTa.isVerb("scarily"));
    ok(!RiTa.isVerb("sleepily"));
    ok(!RiTa.isVerb("excitedly"));
    ok(!RiTa.isVerb("energetically"));

    try {
      ok(RiTa.isVerb("banana split"));
    } catch (Exception e) {
      ok(e);
    }

  }

  @Test
  public void testIsAdjectiveString() {


    ok(!RiTa.isAdjective("swim"));
    ok(!RiTa.isAdjective("walk"));
    ok(!RiTa.isAdjective("walker"));
    ok(RiTa.isAdjective("beautiful"));
    ok(!RiTa.isAdjective("dance"));
    ok(!RiTa.isAdjective("dancing"));
    ok(!RiTa.isAdjective("dancer"));

    // verb
    ok(!RiTa.isAdjective("wash"));
    ok(!RiTa.isAdjective("walk"));
    ok(!RiTa.isAdjective("play"));
    ok(!RiTa.isAdjective("throw"));
    ok(!RiTa.isAdjective("drink"));
    ok(!RiTa.isAdjective("eat"));
    ok(!RiTa.isAdjective("chew"));

    // adj
    ok(RiTa.isAdjective("hard"));
    ok(RiTa.isAdjective("wet"));
    ok(RiTa.isAdjective("dry"));
    ok(RiTa.isAdjective("furry"));
    ok(RiTa.isAdjective("sad"));
    ok(RiTa.isAdjective("happy"));
    ok(RiTa.isAdjective("kindly")); // +adv

    // n
    ok(!RiTa.isAdjective("dogs"));
    ok(!RiTa.isAdjective("wind"));
    ok(!RiTa.isAdjective("dolls"));
    ok(!RiTa.isAdjective("frogs"));
    ok(!RiTa.isAdjective("ducks"));
    ok(!RiTa.isAdjective("flowers"));
    ok(!RiTa.isAdjective("fish"));

    // adv
    ok(!RiTa.isAdjective("truthfully"));
    ok(!RiTa.isAdjective("bravely"));
    ok(!RiTa.isAdjective("scarily"));
    ok(!RiTa.isAdjective("sleepily"));
    ok(!RiTa.isAdjective("excitedly"));
    ok(!RiTa.isAdjective("energetically"));

    try {
      ok(RiTa.isAdjective("banana split"));
    } catch (Exception e) {
      ok(e);
    }
  }

  @Test
  public void testIsAlliterationStringString() {


    ok(RiTa.isAlliteration("sally", "silly"));
    ok(RiTa.isAlliteration("sea", "seven"));
    ok(RiTa.isAlliteration("silly", "seven"));
    ok(RiTa.isAlliteration("sea", "sally"));
   
    ok(RiTa.isAlliteration("big", "bad"));
    ok(RiTa.isAlliteration("bad", "big")); // swap position
    
    ok(RiTa.isAlliteration("BIG", "bad")); // CAPITAL LETTERS
    ok(RiTa.isAlliteration("big", "BAD")); // CAPITAL LETTERS
    ok(RiTa.isAlliteration("BIG", "BAD")); // CAPITAL LETTERS
    ok(RiTa.isAlliteration("this", "these")); 
    ok(RiTa.isAlliteration("unsung", "sine")); 
    ok(RiTa.isAlliteration("job", "gene")); 
    ok(RiTa.isAlliteration("knife", "gnat")); 
    ok(RiTa.isAlliteration("knife", "naughty")); 
    ok(RiTa.isAlliteration("abet", "better")); 
    ok(RiTa.isAlliteration("psychology", "cholera")); 
    ok(RiTa.isAlliteration("consult", "sultan")); 
    ok(RiTa.isAlliteration("never", "knight")); 
    ok(RiTa.isAlliteration("cat", "kitchen")); 
    ok(RiTa.isAlliteration("monsoon", "super")); 
    
    ok(!RiTa.isAlliteration("big ", "bad")); // Word with space False
    ok(!RiTa.isAlliteration("big    ", "bad")); // Word with tab space
   
    ok(!RiTa.isAlliteration("octopus", "oblong"));//Vowels
    ok(!RiTa.isAlliteration("omen", "open"));
    ok(!RiTa.isAlliteration("amicable", "atmosphere"));
   
    // False
    ok(RiTa.isAlliteration("this", "these"));
    ok(!RiTa.isAlliteration("solo", "tomorrow"));
    ok(!RiTa.isAlliteration("solo", "yoyo"));
    ok(!RiTa.isAlliteration("yoyo", "jojo"));
    ok(!RiTa.isAlliteration("withdraw", "wind"));
    ok(!RiTa.isAlliteration("cat", "access"));
    
    
    try {
      RiTa.isAlliteration("", "");
    } catch (Exception e) {
      ok(e);
    }

  }

  @Test
  public void testGetRawPhonesStringBoolean() {
    


    String s = RiTa.getRawPhones("dragging", false);
    equal(s, "d-r-ae1 g-ih-ng");
    
    s = RiTa.getRawPhones("wellow", false);
    ok(s.length() == 0);

    s = RiTa.getRawPhones("mellow", false);
    equal(s, "m-eh1 l-ow");

    s = RiTa.getRawPhones("yoyo", false);
    ok(s.length() == 0);

    s = RiTa.getRawPhones("laggin", false);
    ok(s.length() == 0);
    
    s = RiTa.getRawPhones("streamer", false);
    equal(s, "s-t-r-iy1 m-er");// in dict


    // start using LTS rules
    // comparing results to 
    // http://www.speech.cs.cmu.edu/cgi-bin/cmudict for arpabets
    // http://lingorado.com/ipa/ for words not found in CMU dictionary above
    // http://www.thefreedictionary.com/ for syllable 
    
    // s = RiTa.getRawPhones("yo", true); Moved to KnownIssues
    // equal(s, "y-ow1");
    
    s = RiTa.getRawPhones("bing", true);
    equal(s, "b-ih1-ng");
    
    s = RiTa.getRawPhones("laggin", true);
    equal(s, "l-ae1 g-ih1-n");

    s = RiTa.getRawPhones("apple", true);
    equal(s, "ae1 p-ah-l");

    s = RiTa.getRawPhones("hello", true);
    equal(s, "hh-ah l-ow1");

    s = RiTa.getRawPhones("coder", true);
    equal(s, "k-ow1 d-er");

    s = RiTa.getRawPhones("washington", true);
    equal(s, "w-aa1 sh-ih-ng t-ah-n");
    
    // s = RiTa.getRawPhones("hawaii", true);
    // equal(s, "hh-ah0 w-ay1 iy2"); // Moved to KnownIssues
    
    s = RiTa.getRawPhones("alaska", true);
    equal(s, "ah l-ae1-s k-ah");
    
    // s = RiTa.getRawPhones("wikipedia", true);
    // equal(s, "w-ih1 k-iy0 p-iy2 d-iy2-ah0"); // Moved to KnownIssues
    
    // s = RiTa.getRawPhones("california", true);
    // equal(s, "k-ae2 l-ah0 f-ao1-r n-y-ah0"); // Moved to KnownIssues
    
    // s = RiTa.getRawPhones("elizabeth", true);
    // equal(s, "ih0 l-ih1-z ah0 b-ah0-th"); // Moved to KnownIssues

    // s = RiTa.lexImpl.getRawPhones("mellow", true);
    // equal(s,"m-eh1-l ow"); // Moved to KnownIssues

    // s = RiTa.getRawPhones("yoyo", true);
    // equal(s, "y-oy1 y-ow0"); // Moved to KnownIssues
  }

  @Test
  public void testIsRhymeStringString() {



//    
    ok(!RiTa.isRhyme("apple", "polo"));
    ok(!RiTa.isRhyme("this", "these"));

    ok(RiTa.isRhyme("cat", "hat"));
    ok(RiTa.isRhyme("yellow", "mellow"));
    ok(RiTa.isRhyme("toy", "boy"));
    ok(RiTa.isRhyme("sieve", "give"));

    ok(!RiTa.isRhyme("solo", "yoyo"));
    ok(!RiTa.isRhyme("yoyo", "jojo"));
    
    ok(RiTa.isRhyme("solo", "tomorrow"));
    ok(RiTa.isRhyme("tense", "sense"));
    ok(RiTa.isRhyme("crab", "drab"));
    ok(RiTa.isRhyme("shore", "more"));
    ok(RiTa.isRhyme("mouse", "house"));
    ok(!RiTa.isRhyme("hose", "house"));
  }

  @Test
  public void testIsRhymeStringStringBoolean() {



    ok(!RiTa.isRhyme("apple", "polo", true));
    ok(!RiTa.isRhyme("this", "these", false));

    ok(RiTa.isRhyme("cat", "hat", true));
    ok(RiTa.isRhyme("yellow", "mellow", false));
    ok(RiTa.isRhyme("toy", "boy", true));
    ok(RiTa.isRhyme("toy", "boy", false));
    ok(RiTa.isRhyme("sieve", "give", true));
    ok(RiTa.isRhyme("toy", "soy", true));
    ok(!RiTa.isRhyme("sieve", "mellow", false));
    ok(!RiTa.isRhyme("sieve", "mellow", true));

    ok(RiTa.isRhyme("solo", "tomorrow"));
    ok(RiTa.isRhyme("tense", "sense", false));
    ok(RiTa.isRhyme("crab", "drab", false));
    ok(RiTa.isRhyme("shore", "more", false));
    ok(RiTa.isRhyme("mouse", "house", false));
    ok(!RiTa.isRhyme("hose", "house", false));

    ok(!RiTa.isRhyme("solo", "yoyo", false));
    ok(!RiTa.isRhyme("solo", "yoyo", true)); // using LTS engine

    ok(!RiTa.isRhyme("toy", "hoy", false));
    ok(RiTa.isRhyme("toy", "hoy", true)); // using LTS engine
    
    ok(!RiTa.isRhyme("yo", "bro", false)); 
    ok(RiTa.isRhyme("yo", "bro", true)); // using LTS engine

    ok(!RiTa.isRhyme("swag", "grab", false));
    ok(!RiTa.isRhyme("swag", "grab", true)); // using LTS engine

    ok(!RiTa.isRhyme("drake", "rake", false));
    ok(RiTa.isRhyme("drake", "rake", true)); // using LTS engine


    ok(RiTa.isRhyme("yellow", "wellow", true)); 
    ok(RiTa.isRhyme("solo", "yolo", true));
  }
  

  @Test
  public void testIsRhyme() {



    String[] rhymes = { 
	"candle", "handle", 
	"fat", "cat",
	"apple", "grapple",
	"apple", "chapel",
	"libel", "tribal",
	"bugle", "frugal",
	"arrested", "contested",
	"savage", "ravage",
	"savage", "disparage",
	"savage", "cabbage"
    };

    for (int i = 0; i < rhymes.length; i += 2) {
      //System.out.println(rhymes[i] + " + "+rhymes[i+1]+" -> "+RiTa.isRhyme(rhymes[i], rhymes[i+1]));
      ok(RiTa.isRhyme(rhymes[i], rhymes[i+1]), rhymes[i]+"/"+rhymes[i+1]);
      ok(RiTa.isRhyme(rhymes[i+1], rhymes[i]), rhymes[i+1]+"/"+rhymes[i]);
    }
    
    String[] rhymeSet1 = new String[] { 
	"insincere", "persevere", "interfere",  // each should rhyme with the others
	"career",  "year", "reappear", "brigadier", "pioneer", "rear", "near",
	"beer", "fear", "sneer", "adhere", "veer", "volunteer", "pamphleteer",
	"sear", "sincere", "smear", "gear", "deer", "here", "queer",
	"financier", "cavalier", "rainier", "mutineer", "unclear", "racketeer",
	"disappear", "austere", "veneer", "overhear", "auctioneer", "spear",
	"pier", "sphere", "cashier", "ear", "steer",
	 "souvenir", "frontier", "chandelier", "shear", "clear",  "mere",
	"premier", "rehear", "engineer", "cheer", "appear", "severe",
    };

    for (int i = 0; i < rhymeSet1.length-1; i++) {
      for (int j = 0; j < rhymeSet1.length; j++) {
	
	if (i != j){
	  //System.out.println(rhymeSet1[i] + " + "+rhymeSet1[j]+" -> "+RiTa.isRhyme(rhymeSet1[i], rhymeSet1[j]));
	  ok(RiTa.isRhyme(rhymeSet1[i], rhymeSet1[j]));
	}
	else
	  ok(!RiTa.isRhyme(rhymeSet1[i], rhymeSet1[j]));
      }
    }
    
    String[] notRhymes = {
	"not", "rhyme",
	"deer", "dear",
	"candle", "candle" ,
	"hear","here",
	"premiere","premier",
	"peer","pear",
	"sheer","shear"
    };
    
    for (int i = 0; i < notRhymes.length; i += 2) {
      //System.out.println(notRhymes[i] + " + "+notRhymes[i+1]+" -> "+RiTa.isRhyme(notRhymes[i], notRhymes[i+1]));
      ok(!RiTa.isRhyme(notRhymes[i], notRhymes[i+1]));
      ok(!RiTa.isRhyme(notRhymes[i+1], notRhymes[i]));  // either way should be the same
    }
  }
  
  public void removeWord(String s) {
    RiTa.lexicalData().remove(s.toLowerCase());
  }

  @Test
  public void testRemoveWordString() {

    int size1 = RiTa.size();
    ok(RiTa.containsWord("banana"));
    removeWord("banana");
    ok(!RiTa.containsWord("banana"));

    ok(RiTa.containsWord("are")); // check that others r still there
    removeWord("aaa");
    ok(!RiTa.containsWord("aaa"));

    removeWord("");

    int size2 = RiTa.size();

    RiTa.reload();

    ok(size2 < RiTa.size());

    ok(RiTa.containsWord("a"));
    ok(RiTa.containsWord("zooms"));

    equal(size1, RiTa.size());

    ok(RiTa.containsWord("a"));
    ok(RiTa.containsWord("zooms"));
  }
  
  @Test
  public void testSimilarByLetterString() {

    //System.out.println("testSimilarByLetterString");


    String[] result = RiTa.similarByLetter("banana");
    String[] answer = { "manna", "cabana", "banal","bonanza" };
    deepEqual(result, answer);

    result = RiTa.similarByLetter("tornado");
    answer = new String[] { "torpedo" };
    deepEqual(result, answer);

    result = RiTa.similarByLetter("ice");
    answer = new String[] {"ire", "dice", "rice", "icy", "vice",
	"nice", "iced", "ace" };
    deepEqual(result, answer);

    result = RiTa.similarByLetter("worngword");
    answer = new String[] { "foreword", "wormwood" };
    deepEqual(result, answer);

    result = RiTa.similarByLetter("worngword");
    answer = new String[] { "foreword", "wormwood" };
    deepEqual(result, answer);

    result = RiTa.similarByLetter("123");
    ok(result.length > 400);

    result = RiTa.similarByLetter("");
    answer = new String[] {};
    deepEqual(answer, result);

    /*
     * FOR ABOVE in RitaJS result = new String[]{}; result =
     * RiTa.similarByLetter("banana", 1, true); answer = new String[]{ "cabana"
     * }; deepEqual(result, answer);
     * 
     * result = new String[]{}; result = RiTa.similarByLetter("banana", 1,
     * false); answer = new String[]{ "banal", "bonanza", "cabana", "lantana",
     * "manna", "wanna" }; deepEqual(result, answer);
     */

  }

  @Test
  public void testSimilarByLetterStringInt() {


    String[] result = new String[] {};
    result = RiTa.similarByLetter("happier", 1);
    String[] answer = new String[] { "hardier", "rapier", "happily", "hipper",
	"hopper", "dapper", "handier", "hippie", "hamper", "happiest", "happen" };
    deepEqual(result, answer);

    result = new String[] {};
    result = RiTa.similarByLetter("ice", 1);
    answer = new String[] {"ire", "dice", "rice", "icy", "vice",
	 "nice", "iced", "ace" };

    deepEqual(result, answer);

  }

  @Test
  public void testSimilarByLetterStringIntBoolean() {

    String[] result = new String[] {};
    result = RiTa.similarByLetter("ice", 2, true);
    ok(result.length > 10);

    result = new String[] {};
    result = RiTa.similarByLetter("ice", 0, true);
    String[] answer = new String[] { "ire", "icy", "ace" };
    deepEqual(result, answer);

    result = new String[] {};
    result = RiTa.similarByLetter("happier", 1, false);
    answer = new String[] { "hardier", "rapier", "happily", "hipper", "hopper",
	"dapper", "handier", "hippie", "hamper", "happiest", "happen" };

    deepEqual(result, answer);

    result = new String[] {};
    result = RiTa.similarByLetter("happier", 1, true);
    answer = new String[] { "hardier", "happily", "handier" };
    deepEqual(result, answer);
  }

  @Test
  public void testSimilarBySoundString() {



    String[] result = RiTa.similarBySound("worngword");
    String[] answer =  { "wayward", "wormwood", "watchword" };
    deepEqual(result, answer);

    result = RiTa.similarBySound("tornado");
    answer = new String[] { "torpedo" };
    deepEqual(result, answer);
    
    result = RiTa.similarBySound("try"); 
    answer = new String[] { "tie", "tried", "trite", "tree", "pry",
	"dry", "tribe", "true", "tripe", "cry", "wry", "tray", "fry", "rye" };
    deepEqual(result, answer);


    result = RiTa.similarBySound("happy");
    answer = new String[] { "hippie", "happier" };
    deepEqual(result, answer);

    result = RiTa.similarBySound("cat");
    ok(result.length > 10);

    result = RiTa.similarBySound("");
    answer = new String[] {};
    deepEqual(result, answer);

  }

  @Test
  public void testSimilarBySoundStringInt() {
    


    String[] result = RiTa.similarBySound("happy", 1);
    String[] answer = new String[] { "hippie", "happier" };
    deepEqual(result, answer);

    result = RiTa.similarBySound("happy", 0);
    answer = new String[] { "hippie", "happier" };
    deepEqual(result, answer);

    result = RiTa.similarBySound("happy", 2);
    answer = new String[] { "hippie", "happier" };

    ok(result.length > 10);
    
    result = RiTa.similarBySound("try", 0);
    answer = new String[] { "tie", "tried", "trite", "tree", "pry",
	"dry", "tribe", "true", "tripe", "cry", "wry", "tray", "fry", "rye" };
    deepEqual(result, answer);

    result = RiTa.similarBySound("try", 1);
    answer = new String[] { "tie", "tried", "trite", "tree", "pry",
	"dry", "tribe", "true", "tripe", "cry", "wry", "tray", "fry", "rye" };

    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);

    result = RiTa.similarBySound("try", 2);
    answer = new String[] { "tie", "tried", "trite", "tree", "pry",
	"dry", "tribe", "true", "tripe", "cry", "wry", "tray", "fry", "rye" };

    ok(result.length > 30);
  }

  @Test
  public void testSimilarBySoundAndLetterString() {


    String[] result = RiTa.similarBySoundAndLetter("try");
    String[] answer = new String[] { "pry", "dry", "wry", "tray", "fry", "cry" };
    deepEqual(result, answer);

    result = RiTa.similarBySoundAndLetter("worngword");
    answer = new String[] { "wormwood" };
    deepEqual(result, answer);
    
    result = RiTa.similarBySoundAndLetter("daddy");
    answer = new String[] {"dandy", "paddy" };
    deepEqual(result, answer);

    result = RiTa.similarBySoundAndLetter("banana");
    answer = new String[] { "bonanza" };
    deepEqual(result, answer);

    result = RiTa.similarBySoundAndLetter("tornado");
    answer = new String[] { "torpedo" };
    deepEqual(result, answer);
    
    result = RiTa.similarBySoundAndLetter("");
    deepEqual(result, new String[0]);
  }

  @Test
  public void testSize() {

    ok(RiTa.size() > 23000);
  }

  @Test
  public void testReload() {

    int originalSize = RiTa.size();
    
    ok(RiTa.containsWord("cat"));
    removeWord("cat");
    ok(RiTa.containsWord("are"));
    removeWord("are");
	
    int removeTwoWordSize = RiTa.size();
    ok(removeTwoWordSize < originalSize);
    RiTa.reload();
    ok(RiTa.size() == originalSize);
  }

  @Test
  public void testSubstringsString() {
    
 // only 1 per test needed

    String[] result2 = RiTa.substrings("headache");
    String[] answer2 = { "ache", "head" };
    deepEqual(result2, answer2);

    String[] result = RiTa.substrings("goxgle");
    ok(result.length == 0);

    result = RiTa.substrings("thousand");
    String[] answer = { "sand" };
    deepEqual(result, answer);

    String[] result3 = RiTa.substrings("exhibition");
    String[] answer3 = { "exhibit" };
    deepEqual(result3, answer3);

    String[] result4 = RiTa.substrings("hell");
    String[] answer4 = {};
    deepEqual(result4, answer4);

    String[] result5 = RiTa.substrings("hi");
    String[] answer5 = {};
    deepEqual(result5, answer5);

    result = RiTa.substrings("");
    deepEqual(result, new String[0]);
  }

  @Test
  public void testSubstringsStringInt() {
 // only 1 per test needed

    String[] result = RiTa.substrings("thousand", 4); // min-length=4
    String[] answer = new String[] { "sand" };
    deepEqual(result, answer);

    result = RiTa.substrings("thousand", 5); // min-length=5
    answer = new String[] {};
    deepEqual(result, answer);

    String[] result2 = RiTa.substrings("headache", 3); // min-length=4
    String[] answer2 = new String[] { "ache", "head" };
    ;
    deepEqual(result2, answer2);

    result2 = RiTa.substrings("headache", 5); // min-length=5
    answer2 = new String[] {};
    deepEqual(result2, answer2);

    String[] result3 = RiTa.substrings("exhibition", 3);
    String[] answer3 = new String[] { "ion", "bit", "exhibit" };
    deepEqual(result3, answer3);

    result3 = RiTa.substrings("exhibition", 5);
    answer3 = new String[] { "exhibit" };
    deepEqual(result3, answer3);

  }

  @Test
  public void testSuperstringsString() {
    // superstringsByLetter(String input)

    String[] result = RiTa.superstrings("superm");
    String[] answer = new String[] { "supermarket"};
    deepEqual(result, answer);

    result = RiTa.superstrings("goxgle");
    deepEqual(result, new String[] {});

    result = RiTa.superstrings("puni");

    answer = new String[] { "punitive", "punishment", "punishable",
	"unpunished", "punishing", "impunity", "punishes",
	"punished", "punish" };
    deepEqual(result, answer);

    result = RiTa.superstrings("");
    ok(result.length > 1000);
  }
}
