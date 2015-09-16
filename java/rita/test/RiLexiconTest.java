package rita.test;

import static rita.support.QUnitStubs.deepEqual;
import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import rita.RiLexicon;
import rita.RiTa;

public class RiLexiconTest {
  
  static {
    //System.out.println(System.getProperty("java.version"));
    RiTa.SILENT = true;
    RiLexicon.SILENCE_LTS = true;
  }

/*// THIS CAUSES MAVEN TO FAIL
  @Test
  public void testLexicalDataMap() // TODO: check this in RiTaJS
  {
    Map obj = new HashMap();
    obj.put("wonderfullyy", "w-ah1-n-d er-f ax-l iy|rb");

    RiLexicon lex = new RiLexicon(obj);
    Map result = lex.lexicalData();

    ok(lex.containsWord("wonderfullyy"));
    // RiTa.out(result.get("wonderfullyy"));

    equal("w-ah1-n-d er-f ax-l iy|rb", result.get("wonderfullyy"));

    ok(!lex.containsWord("wonderful"));

    lex.reload(); // necessary for other tests
  }
 */
  
  @Test
  public void testAddWordStringStringString() {
    RiLexicon lex = new RiLexicon();

    lex.addWord("bananana", "b-ax-n ae1-n ax ax", "nn");
    ok(lex.containsWord("bananana"));

    lex.addWord("hehehe", "hh-ee1 hh-ee1 hh-ee1", "uh");
    ok(lex.containsWord("hehehe"));
    equal(lex.lexImpl.getPhonemes("hehehe", true), "hh-ee-hh-ee-hh-ee"); // TODO

    lex.addWord("HAHAHA", "hh-aa1 hh-aa1 hh-aa1", "uh");
    ok(lex.containsWord("HAHAHA"));
    equal(lex.lexImpl.getPhonemes("HAHAHA", true), "hh-aa-hh-aa-hh-aa"); // TODO

    lex = new RiLexicon();
    lex.addWord("", "", "");

    lex.reload(); // reset

    // TODO 3 parameters in RitaJS [DCH ??]
  }

  @Test
  public void testClear() {
    
    // TODO: check that these tests are the same in RiTaJS
    RiLexicon lex = new RiLexicon();
    ok(lex.containsWord("banana"));
    lex.removeWord("banana");
    ok(!lex.containsWord("banana"));

    lex.clear();
    ok(lex.size() == 0);
    lex.reload();

    ok(lex.containsWord("banana"));
    ok(lex.containsWord("funny"));

    Map obj = new HashMap();
    obj.put("wonderfullyy", new String[] { "w-ah1-n-d er-f ax-l iy", "rb" });
    lex.lexicalData(obj);
    ok(lex.size() == 1);
    deepEqual(lex.lexicalData(), obj);
    ok(!lex.containsWord("wonderfully"));
    ok(lex.containsWord("wonderfullyy"));

    lex.reload();
    ok(!lex.containsWord("wonderfullyy"));
    ok(lex.containsWord("wonderful"));

    Map result = new HashMap();
    lex = new RiLexicon();
    result = lex.lexicalData();
    ok(lex.containsWord("a"));
    ok(lex.containsWord("zooms"));
    ok(result.size() > 1000);
  }

  @Test
  public void testContainsWordString() {
    RiLexicon lex = new RiLexicon();

    ok(lex.containsWord("cat"));
    ok(lex.containsWord("cats"));
    ok(!lex.containsWord("cated"));
    ok(lex.containsWord("funny"));
    ok(lex.containsWord("shit"));
    ok(!lex.containsWord("123"));
    ok(!lex.containsWord("hellx"));
    ok(lex.containsWord("hello"));
    ok(lex.containsWord("HeLLo"));
    ok(lex.containsWord("HELLO"));
    ok(lex.containsWord("a"));
    ok(lex.containsWord("A"));
    ok(lex.containsWord("zooms"));
//
//    lex.lexicalData().put("hello", null);
//    ok(!lex.containsWord("hello"));
//    ok(!lex.containsWord(""));
  }

  @Test
  public void testAlliterationsString() {
    RiLexicon lex = new RiLexicon();
    String[] result = lex.alliterations("accidentally");

    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    ok(result.length > 1000);

    lex = new RiLexicon();
    result = lex.alliterations("apples");
    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    ok(result.length > 1000);

    result = new String[] {};
    result = lex.alliterations("dog");
    ok(result.length > 1000);

    result = new String[] {};
    result = lex.alliterations("URL");
    ok(result.length == 0);

    result = lex.alliterations("goxgle");
    ok(result.length > 100);

    result = new String[] {};
    result = lex.alliterations("no stress");
    // RiTa.out(result);
    ok(result.length == 0);

    result = new String[] {};
    result = lex.alliterations("#$%^&*");
    ok(result.length == 0);

    result = new String[] {};
    result = lex.alliterations("");
    ok(result.length == 0);

    // TODO: better tests
  }

  @Test
  public void testAlliterationsStringInt() {
    RiLexicon lex = new RiLexicon();
    String[] result = lex.alliterations("dog", 16);
    ok(result.length == 3);
    result = lex.alliterations("cat", 17);
    // System.out.println("RiLexiconTest.testAlliterationsInt() :: "+result.length);

    ok(result.length == 7);
    
    // TODO: better tests
  }


  @Test
  public void testLexicalData() {
    RiLexicon lex = new RiLexicon();
    Map result = new HashMap();
    result = lex.lexicalData();
    ok(result.size() > 1000);

    Map re = new HashMap();
    re = lex.lexicalData();
    String expected = "ey1|dt";
    String returned = (String) re.get("a");
    deepEqual(returned, expected);

    re = lex.lexicalData();
    returned = (String) re.get("the");
    expected = "dh-ax|dt";
    deepEqual(returned, expected);

    String answer = (String) result.get("aback");
    deepEqual(answer, "ax-b ae1-k|rb");

    // System.out.println("lexicalData containsKey('a'): "+result.containsKey("a"));
    // System.out.println("lexicalData : "+result.get("a"));
    answer = (String) result.get("a");
    deepEqual(answer, "ey1|dt");

//    lex.lexicalData().put("hello", null);
//    lex.containsWord("hello");
  }

  @Test
  public void testRandomWord() {
  
    
    RiLexicon lex = new RiLexicon();

    String result = lex.randomWord();
    for (int i = 0; i < 10; i++) {
      ok(result.length() > 0);
    }
  }

  @Test
  public void testRandomWordInt() {
    
    RiLexicon lex = new RiLexicon();

    for (int i = 0; i < 10; i++) {
      String result = lex.randomWord(3);

      // RiTa.out(result);

      ok(result.length() > 0);
      String syllables = RiTa.getSyllables(result);

      int num = syllables.split(RiTa.SYLLABLE_BOUNDARY).length;
      ok(num == 3);// "3 syllableCount: "
    }

    for (int i = 0; i < 10; i++) {
      String result = lex.randomWord(5);
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
    
    RiLexicon lex = new RiLexicon();

    String[] pos = { "nn", "nns", "jj", "jjr", "wp" };
    for (int j = 0; j < pos.length; j++) {
      for (int i = 0; i < 3; i++) {
	String result = lex.randomWord(pos[j]);
	String best = lex.lexImpl.getBestPos(result);
	// System.out.println(result+": "+pos[j]+" ?= "+best);
	equal(pos[j], best);
      }
    }
  }

  @Test
  public void testRandomWordStringInt() {
    
    RiLexicon lex = new RiLexicon();

    String[] pos = { "nn", "nns", "jj", "jjr" };

    for (int j = 0; j < pos.length; j++) {
      for (int k = 2; k < 5; k++) {
	String result = lex.randomWord(pos[j], k);
	String best = lex.lexImpl.getBestPos(result);
	// System.out.println(result+": "+pos[j]+" ?= "+best);
	equal(pos[j], best);

	String syllables = RiTa.getSyllables(result);
	int num = syllables.split(RiTa.SYLLABLE_BOUNDARY).length;
	ok(num == k);
      }
    }

    String result = lex.randomWord("wp", 5);
    equal(result, "");

    result = lex.randomWord("nn", 30);
    ok(result == "");
  }
  

  @Test
  public void testRhymesString() {
    
    RiLexicon lex = new RiLexicon();

    String[] result = lex.rhymes("apple");
    String[] answer = { "pineapple", "chapel", "grapple" };
    deepEqual(result, answer);

    result = lex.rhymes("bible");
    answer = new String[] { "libel", "tribal" };
    deepEqual(result, answer);

    result = lex.rhymes("goxgle");
    answer = new String[] {};
    deepEqual(result, answer);

    result = lex.rhymes("google");
    answer = new String[] { "bugle", "frugal" };
    // System.out.println(RiTa.asList(result));
    deepEqual(result, answer);

    result = lex.rhymes("happens in here");
    answer = new String[] {};
    deepEqual(result, answer);

    result = lex.rhymes("");
    answer = new String[] {};
    deepEqual(result, answer);

    result = lex.rhymes("apple.");
    answer = new String[] {};
    deepEqual(result, answer);
  }

  @Test
  public void testWords() {
    
    RiLexicon lex = new RiLexicon();
    String[] result = lex.words();
    ok(result.length > 30000);
  }
  
  @Test
  public void testWordsString() {
    RiLexicon lex = new RiLexicon();
    String[] result = lex.words("ee");
    
    for (int i = 0; i < result.length; i++)
      result[i].matches("^.*ee.*$");

    result = lex.words("tt");
    for (int i = 0; i < result.length; i++)
      result[i].matches("^.*tt.*$");
    
    result = lex.words("ee.*ee");
    String[] answer = new String[] { "freewheeling", "squeegee" };
    deepEqual(result, answer);

  }

  @Test
  public void testWordsStringBoolean() {

    RiLexicon lex = new RiLexicon();
    String[] result = lex.words("ee", true);
    String[] result2 = lex.words("ee", false);
    ok(result.length > 20);
    ok(result2.length > 20);
    
    boolean diff = false;
    for (int i = 0; i < 20; i++) {
      if (!result[i].equals(result2[i]))
	diff = true;
    }
    ok(diff);
    
    // verify not sorted unless specified
    result = lex.words("tt", true);
    result2 = lex.words("tt", false);
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
    RiLexicon lex = new RiLexicon();

    ok(!lex.isAdverb("swim"));
    ok(!lex.isAdverb("walk"));
    ok(!lex.isAdverb("walker"));
    ok(!lex.isAdverb("beautiful"));
    ok(!lex.isAdverb("dance"));
    ok(!lex.isAdverb("dancing"));
    ok(!lex.isAdverb("dancer"));

    // verb
    ok(!lex.isAdverb("wash"));
    ok(!lex.isAdverb("walk"));
    ok(!lex.isAdverb("play"));
    ok(!lex.isAdverb("throw"));
    ok(!lex.isAdverb("drink"));
    ok(!lex.isAdverb("eat"));
    ok(!lex.isAdverb("chew"));

    // adj
    ok(!lex.isAdverb("wet"));
    ok(!lex.isAdverb("dry"));
    ok(!lex.isAdverb("furry"));
    ok(!lex.isAdverb("sad"));
    ok(!lex.isAdverb("happy"));

    // n
    ok(!lex.isAdverb("dogs"));
    ok(!lex.isAdverb("wind"));
    ok(!lex.isAdverb("dolls"));
    ok(!lex.isAdverb("frogs"));
    ok(!lex.isAdverb("ducks"));
    ok(!lex.isAdverb("flowers"));
    ok(!lex.isAdverb("fish"));

    // adv
    ok(lex.isAdverb("truthfully"));
    ok(lex.isAdverb("kindly"));
    ok(lex.isAdverb("bravely"));
    ok(lex.isAdverb("doggedly"));
    ok(lex.isAdverb("sleepily"));
    ok(lex.isAdverb("excitedly"));
    ok(lex.isAdverb("energetically"));
    ok(lex.isAdverb("hard"));

    ok(!lex.isAdverb(""));

    try {
      ok(lex.isAdverb("banana split"));
      ok(false);
    } catch (Exception e) {
      ok(e);
    }

  }

  @Test
  public void testIsNounString() {
    RiLexicon lex = new RiLexicon();
    ok(lex.isNoun("swim"));
    ok(lex.isNoun("walk"));
    ok(lex.isNoun("walker"));
    ok(lex.isNoun("dance"));
    ok(lex.isNoun("dancing"));
    ok(lex.isNoun("dancer"));

    // verb
    ok(lex.isNoun("wash"));
    ok(lex.isNoun("walk"));
    ok(lex.isNoun("play"));
    ok(lex.isNoun("throw"));
    ok(lex.isNoun("drink"));
    ok(!lex.isNoun("eat"));
    ok(!lex.isNoun("chew"));

    // adj
    ok(!lex.isNoun("hard"));
    ok(!lex.isNoun("dry"));
    ok(!lex.isNoun("furry"));
    ok(!lex.isNoun("sad"));
    ok(!lex.isNoun("happy"));
    ok(!lex.isNoun("beautiful"));

    // n
    ok(lex.isNoun("dogs"));
    ok(lex.isNoun("wind"));
    ok(lex.isNoun("dolls"));
    ok(lex.isNoun("frogs"));
    ok(lex.isNoun("ducks"));
    ok(lex.isNoun("flowers"));
    ok(lex.isNoun("fish"));
    ok(lex.isNoun("wet"));
    ok(lex.isNoun("walk"));
    ok(lex.isNoun("drink"));

    // adv
    ok(!lex.isNoun("truthfully"));
    ok(!lex.isNoun("kindly"));
    ok(!lex.isNoun("bravely"));
    ok(!lex.isNoun("scarily"));
    ok(!lex.isNoun("sleepily"));
    ok(!lex.isNoun("excitedly"));
    ok(!lex.isNoun("energetically"));

    ok(!lex.isNoun(""));

    try {
      ok(lex.isNoun("banana split"));
    } catch (Exception e) {
      ok(e);
    }
  }

  @Test
  public void testIsVerbString() {
    RiLexicon lex = new RiLexicon();

    ok(lex.isVerb("dance"));
    ok(lex.isVerb("swim"));
    ok(lex.isVerb("walk"));
    ok(!lex.isVerb("walker"));
    ok(!lex.isVerb("beautiful"));
    ok(lex.isVerb("dancing"));
    ok(!lex.isVerb("dancer"));

    // verb
    ok(lex.isVerb("eat"));
    ok(lex.isVerb("chew"));
    ok(lex.isVerb("throw")); // +n
    ok(lex.isVerb("walk")); // +n
    ok(lex.isVerb("wash")); // +n
    ok(lex.isVerb("drink")); // +n
    ok(lex.isVerb("ducks")); // +n
    ok(lex.isVerb("fish")); // +n
    ok(lex.isVerb("dogs")); // +n
    ok(lex.isVerb("wind")); // +n
    ok(lex.isVerb("wet")); // +adj
    ok(lex.isVerb("dry")); // +adj

    // adj
    ok(!lex.isVerb("hard"));
    ok(!lex.isVerb("furry"));
    ok(!lex.isVerb("sad"));
    ok(!lex.isVerb("happy"));
    ok(lex.isVerb("wet")); // +adj
    ok(lex.isVerb("dry")); // +adj

    // n
    ok(!lex.isVerb("dolls"));
    ok(!lex.isVerb("frogs"));
    ok(!lex.isVerb("flowers"));
    ok(lex.isVerb("throw")); // +n
    ok(lex.isVerb("walk")); // +n
    ok(lex.isVerb("wash")); // +n
    ok(lex.isVerb("drink")); // +n
    ok(lex.isVerb("ducks")); // +n
    ok(lex.isVerb("fish")); // +n
    ok(lex.isVerb("dogs")); // +n
    ok(lex.isVerb("wind")); // +n

    // adv
    ok(!lex.isVerb("truthfully"));
    ok(!lex.isVerb("kindly"));
    ok(!lex.isVerb("bravely"));
    ok(!lex.isVerb("scarily"));
    ok(!lex.isVerb("sleepily"));
    ok(!lex.isVerb("excitedly"));
    ok(!lex.isVerb("energetically"));

    try {
      ok(lex.isVerb("banana split"));
    } catch (Exception e) {
      ok(e);
    }

  }

  @Test
  public void testIsAdjectiveString() {
    RiLexicon lex = new RiLexicon();

    ok(!lex.isAdjective("swim"));
    ok(!lex.isAdjective("walk"));
    ok(!lex.isAdjective("walker"));
    ok(lex.isAdjective("beautiful"));
    ok(!lex.isAdjective("dance"));
    ok(!lex.isAdjective("dancing"));
    ok(!lex.isAdjective("dancer"));

    // verb
    ok(!lex.isAdjective("wash"));
    ok(!lex.isAdjective("walk"));
    ok(!lex.isAdjective("play"));
    ok(!lex.isAdjective("throw"));
    ok(!lex.isAdjective("drink"));
    ok(!lex.isAdjective("eat"));
    ok(!lex.isAdjective("chew"));

    // adj
    ok(lex.isAdjective("hard"));
    ok(lex.isAdjective("wet"));
    ok(lex.isAdjective("dry"));
    ok(lex.isAdjective("furry"));
    ok(lex.isAdjective("sad"));
    ok(lex.isAdjective("happy"));
    ok(lex.isAdjective("kindly")); // +adv

    // n
    ok(!lex.isAdjective("dogs"));
    ok(!lex.isAdjective("wind"));
    ok(!lex.isAdjective("dolls"));
    ok(!lex.isAdjective("frogs"));
    ok(!lex.isAdjective("ducks"));
    ok(!lex.isAdjective("flowers"));
    ok(!lex.isAdjective("fish"));

    // adv
    ok(!lex.isAdjective("truthfully"));
    ok(!lex.isAdjective("bravely"));
    ok(!lex.isAdjective("scarily"));
    ok(!lex.isAdjective("sleepily"));
    ok(!lex.isAdjective("excitedly"));
    ok(!lex.isAdjective("energetically"));

    try {
      ok(lex.isAdjective("banana split"));
    } catch (Exception e) {
      ok(e);
    }
  }

  @Test
  public void testIsAlliterationStringString() {
    RiLexicon lex = new RiLexicon();

    ok(lex.isAlliteration("apple", "polo"));
    ok(lex.isAlliteration("polo", "apple")); // swap position
    ok(lex.isAlliteration("POLO", "apple")); // CAPITAL LETTERS
    ok(lex.isAlliteration("POLO", "APPLE")); // CAPITAL LETTERS
    ok(lex.isAlliteration("polo", "APPLE")); // CAPITAL LETTERS
    ok(!lex.isAlliteration("polo ", "APPLE")); // Word with space False
    ok(!lex.isAlliteration("polo    ", "APPLE")); // Word with tab space
    // False
    ok(lex.isAlliteration("this", "these"));
    ok(!lex.isAlliteration("solo", "tomorrow"));
    ok(!lex.isAlliteration("solo", "yoyo"));
    ok(!lex.isAlliteration("yoyo", "jojo"));

    try {
      lex.isAlliteration("", "");
    } catch (Exception e) {
      ok(e);
    }

  }

  @Test
  public void testGetRawPhonesStringBoolean() {
    RiLexicon lex = new RiLexicon();

    String s = lex.lexImpl.getRawPhones("dragging", false);
    equal(s, "d-r-ae1-g ih-ng");

    s = lex.lexImpl.getRawPhones("laggin", false);
    ok(s.length() == 0);

    s = lex.lexImpl.getRawPhones("laggin", true);
    equal(s, "l-ae1 g-ih1-n");

    s = lex.lexImpl.getRawPhones("yoyo", true);
    equal(s, "y-oy1 ow1"); // not sure about this

    s = lex.lexImpl.getRawPhones("yoyo", false);
    ok(s.length() == 0);

    s = lex.lexImpl.getRawPhones("wellow", false);
    ok(s.length() == 0);

    s = lex.lexImpl.getRawPhones("mellow", false);
    equal(s, "m-eh1-l ow");

    // s = lex.lexImpl.getRawPhones("mellow", true);
    // equal(s,"m-eh1-l ow"); Note: moved to KnownIssues
  }

  @Test
  public void testIsRhymeStringString() {
    // TODO: check all these
    RiLexicon lex = new RiLexicon();

    ok(lex.isRhyme("solo", "tomorrow"));
    ok(!lex.isRhyme("apple", "polo"));
    ok(!lex.isRhyme("this", "these"));

    ok(lex.isRhyme("solo", "tomorrow"));
    ok(lex.isRhyme("cat", "hat"));
    ok(lex.isRhyme("yellow", "mellow"));
    ok(lex.isRhyme("toy", "boy"));
    ok(lex.isRhyme("sieve", "give"));

    ok(!lex.isRhyme("solo", "yoyo"));
    ok(!lex.isRhyme("yoyo", "jojo"));
  }

  @Test
  public void testIsRhymeStringStringBoolean() {
    
    // TODO: check all these (more tests)
    RiLexicon lex = new RiLexicon();

    ok(!lex.isRhyme("solo", "yoyo", false));
    ok(!lex.isRhyme("yoyo", "jojo", false));
    ok(lex.isRhyme("yoyo", "jojo", true));

    ok(lex.isRhyme("solo", "tomorrow", true));

    ok(lex.isRhyme("tomorrow", "solo", true));
    ok(!lex.isRhyme("apple", "polo", true));
    ok(!lex.isRhyme("this", "these", false));

    ok(lex.isRhyme("solo", "tomorrow", true));
    ok(lex.isRhyme("cat", "hat", true));
    ok(lex.isRhyme("yellow", "mellow", false));
    ok(lex.isRhyme("toy", "boy", true));
    ok(lex.isRhyme("toy", "boy", false));
    ok(lex.isRhyme("sieve", "give", true));
    ok(lex.isRhyme("toy", "soy", true));
    ok(!lex.isRhyme("sieve", "mellow", false));
    ok(!lex.isRhyme("sieve", "mellow", true));

    ok(lex.isRhyme("toy", "hoy", true));
    // ok(lex.isRhyme("yellow", "wellow",true)); 
    
    //TODO: need example for correct use of LTS engine
  }

  @Test
  public void testRemoveWordString() {
    RiLexicon lex = new RiLexicon();
    int size1 = lex.size();
    ok(lex.containsWord("banana"));
    lex.removeWord("banana");
    ok(!lex.containsWord("banana"));

    ok(lex.containsWord("are")); // check that others r still there
    lex.removeWord("aaa");
    ok(!lex.containsWord("aaa"));

    lex.removeWord("");

    int size2 = lex.size();

    lex.reload();

    ok(size2 < lex.size());

    ok(lex.containsWord("a"));
    ok(lex.containsWord("zooms"));

    equal(size1, lex.size());

    lex = new RiLexicon();
    ok(lex.containsWord("a"));
    ok(lex.containsWord("zooms"));
  }
  
  @Test
  public void testSimilarByLetterString() {

    //System.out.println("testSimilarByLetterString");

    RiLexicon lex = new RiLexicon();
    String[] result = lex.similarByLetter("banana");
    String[] answer = { "lantana", "wanna", "manna", "cabana", "banal",
	"bonanza" };
    deepEqual(result, answer);

    result = lex.similarByLetter("tornado");
    answer = new String[] { "torpedo" };
    deepEqual(result, answer);

    result = lex.similarByLetter("ice");
    answer = new String[] { "mice", "ire", "dice", "rice", "icy", "vice",
	"lice", "nice", "iced", "ace" };
    deepEqual(result, answer);

    result = lex.similarByLetter("worngword");
    answer = new String[] { "foreword", "wormwood" };
    deepEqual(result, answer);

    result = lex.similarByLetter("worngword");
    answer = new String[] { "foreword", "wormwood" };
    deepEqual(result, answer);

    result = lex.similarByLetter("123");
    ok(result.length > 400);

    result = lex.similarByLetter("");
    answer = new String[] {};
    deepEqual(answer, result);

    /*
     * FOR ABOVE in RitaJS result = new String[]{}; result =
     * lex.similarByLetter("banana", 1, true); answer = new String[]{ "cabana"
     * }; deepEqual(result, answer);
     * 
     * result = new String[]{}; result = lex.similarByLetter("banana", 1,
     * false); answer = new String[]{ "banal", "bonanza", "cabana", "lantana",
     * "manna", "wanna" }; deepEqual(result, answer);
     */

  }

  @Test
  public void testSimilarByLetterStringInt() {
    RiLexicon lex = new RiLexicon();

    String[] result = new String[] {};
    result = lex.similarByLetter("happier", 1);
    String[] answer = new String[] { "hardier", "rapier", "happily", "hipper",
	"hopper", "dapper", "handier", "hippie", "hamper", "happiest", "happen" };
    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);

    result = new String[] {};
    result = lex.similarByLetter("ice", 1);
    answer = new String[] { "mice", "ire", "dice", "rice", "icy", "vice",
	"lice", "nice", "iced", "ace" };
    // for(int i=0;i<result.length;i++){
    // System.out.println(result[i]);
    // }
    deepEqual(result, answer);

  }

  @Test
  public void testSimilarByLetterStringIntBoolean() {
    RiLexicon lex = new RiLexicon();
    String[] result = new String[] {};
    result = lex.similarByLetter("ice", 2, true);
    ok(result.length > 10);

    result = new String[] {};
    result = lex.similarByLetter("ice", 0, true);
    String[] answer = new String[] { "ire", "icy", "ace" };
    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);

    result = new String[] {};
    result = lex.similarByLetter("happier", 1, false);
    answer = new String[] { "hardier", "rapier", "happily", "hipper", "hopper",
	"dapper", "handier", "hippie", "hamper", "happiest", "happen" };
    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);

    result = new String[] {};
    result = lex.similarByLetter("happier", 1, true);
    answer = new String[] { "hardier", "happily", "handier" };
    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);
  }

  @Test
  public void testSimilarBySoundString() {

    RiLexicon lex = new RiLexicon();

    String[] result = lex.similarBySound("worngword");
    String[] answer =  { "wayward", "wormwood", "watchword" };
    deepEqual(result, answer);

    result = lex.similarBySound("tornado");
    answer = new String[] { "torpedo" };
    deepEqual(result, answer);
    
    result = lex.similarBySound("try");
    answer = new String[] { "tie", "tried", "trite", "tree", "tries", "pry",
	"dry", "tribe", "true", "tripe", "cry", "wry", "tray", "fry", "rye" };
    deepEqual(result, answer);

    result = lex.similarBySound("happy");
    answer = new String[] { "hippie", "happier" };
    deepEqual(result, answer);

    result = lex.similarBySound("cat");
    ok(result.length > 10);

    result = lex.similarBySound("");
    answer = new String[] {};
    deepEqual(result, answer);

  }

  @Test
  public void testSimilarBySoundStringInt() {
    
    RiLexicon lex = new RiLexicon();

    String[] result = lex.similarBySound("happy", 1);
    String[] answer = new String[] { "hippie", "happier" };
    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);

    result = lex.similarBySound("happy", 0);
    answer = new String[] { "hippie", "happier" };
    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);

    result = lex.similarBySound("happy", 2);
    answer = new String[] { "hippie", "happier" };

    ok(result.length > 10);

    result = lex.similarBySound("try", 0);
    answer = new String[] { "tie", "tried", "trite", "tree", "tries", "pry",
	"dry", "tribe", "true", "tripe", "cry", "wry", "tray", "fry", "rye" };

    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);

    result = lex.similarBySound("try", 1);
    answer = new String[] { "tie", "tried", "trite", "tree", "tries", "pry",
	"dry", "tribe", "true", "tripe", "cry", "wry", "tray", "fry", "rye" };

    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    deepEqual(result, answer);

    result = lex.similarBySound("try", 2);
    answer = new String[] { "tie", "tried", "trite", "tree", "tries", "pry",
	"dry", "tribe", "true", "tripe", "cry", "wry", "tray", "fry", "rye" };

    for (int i = 0; i < result.length; i++) {
      // System.out.println(result[i]);
    }
    ok(result.length > 30);
  }

  @Test
  public void testSimilarBySoundAndLetterString() {
    RiLexicon lex = new RiLexicon();

    String[] result = lex.similarBySoundAndLetter("try");
    String[] answer = new String[] { "pry", "dry", "wry", "tray", "fry", "cry" };
    deepEqual(result, answer);

    result = lex.similarBySoundAndLetter("worngword");
    answer = new String[] { "wormwood" };
    deepEqual(result, answer);
    
    result = lex.similarBySoundAndLetter("daddy");
    answer = new String[] { "dandy" };
    deepEqual(result, answer);

    result = lex.similarBySoundAndLetter("banana");
    answer = new String[] { "bonanza" };
    deepEqual(result, answer);

    result = lex.similarBySoundAndLetter("tornado");
    answer = new String[] { "torpedo" };
    deepEqual(result, answer);
    
    result = lex.similarBySoundAndLetter("");
    deepEqual(result, new String[0]);
  }

  @Test
  public void testSize() {
    RiLexicon lex = new RiLexicon();
    ok(lex.size() > 30000);
  }

  @Test
  public void testReload() {
    RiLexicon lex = new RiLexicon();
    int originalSize = lex.size();
    
    ok(lex.containsWord("cat"));
    lex.removeWord("cat");
    ok(lex.containsWord("are"));
    lex.removeWord("are");
	
    int removeTwoWordSize = lex.size();
    ok(removeTwoWordSize < originalSize);
    lex.reload();
    ok(lex.size() == originalSize);
  }

  @Test
  public void testSubstringsString() {
    
    RiLexicon lex = new RiLexicon(); // only 1 per test needed

    String[] result2 = lex.substrings("headache");
    String[] answer2 = { "ache", "head" };
    deepEqual(result2, answer2);

    String[] result = lex.substrings("goxgle");
    ok(result.length == 0);

    result = lex.substrings("thousand");
    String[] answer = { "sand", "thou" };
    deepEqual(result, answer);

    String[] result3 = lex.substrings("exhibition");
    String[] answer3 = { "exhibit" };
    deepEqual(result3, answer3);

    String[] result4 = lex.substrings("hell");
    String[] answer4 = {};
    deepEqual(result4, answer4);

    String[] result5 = lex.substrings("hi");
    /*
     * for(int i=0; i<result5.length; i++){ System.out.println(result5[i]); }
     */
    String[] answer5 = {};
    deepEqual(result5, answer5);

    result = lex.substrings("");
    deepEqual(result, new String[0]);

  }

  @Test
  public void testSubstringsStringInt() {
    RiLexicon lex = new RiLexicon(); // only 1 per test needed

    String[] result = lex.substrings("thousand", 4); // min-length=4
    String[] answer = new String[] { "sand", "thou" };
    deepEqual(result, answer);

    result = lex.substrings("thousand", 5); // min-length=5
    answer = new String[] {};
    deepEqual(result, answer);

    String[] result2 = lex.substrings("headache", 3); // min-length=4
    String[] answer2 = new String[] { "ache", "head" };
    ;
    deepEqual(result2, answer2);

    result2 = lex.substrings("headache", 5); // min-length=5
    answer2 = new String[] {};
    deepEqual(result2, answer2);

    String[] result3 = lex.substrings("exhibition", 3);
    String[] answer3 = new String[] { "ion", "bit", "exhibit" };
    /*
     * for(int i=0; i<result3.length; i++){ //System.out.println(result3[i]); }
     */
    deepEqual(result3, answer3);

    result3 = lex.substrings("exhibition", 5);
    answer3 = new String[] { "exhibit" };
    deepEqual(result3, answer3);

  }

  @Test
  public void testSuperstringsString() {
    // superstringsByLetter(String input)

    RiLexicon lex = new RiLexicon();
    String[] result = lex.superstrings("superm");
    String[] answer = new String[] { "supermarket", "supermarkets" };
    deepEqual(result, answer);

    result = lex.superstrings("goxgle");
    deepEqual(result, new String[] {});

    result = lex.superstrings("puni");

    answer = new String[] { "punitive", "punishment", "punishable",
	"unpunished", "punishing", "impunity", "punishments", "punishes",
	"punished", "punish" };
    deepEqual(result, answer);

    result = lex.superstrings("");
    ok(result.length > 1000);
  }
}
