package rita.test;

import static rita.support.QUnitStubs.deepEqual;
import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.notEqual;
import static rita.support.QUnitStubs.ok;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import rita.RiLexicon;
import rita.RiString;
import rita.RiTa;
import rita.support.Constants;

public class RiStringTest implements Constants {
  static {
    // RiLexicon.enabled = false;
    RiTa.SILENT = true;
    RiTa.SILENT_LTS = true; // UNCOMMENT TO TEST WITHOUT LEXICON, USING ONLY LTS
  }

  @Test
  public void testSubSequence() {
    testSubstring(); // OK, should be the same
  }

  @Test
  public void testSet() {
    RiString rs = new RiString("Mom & Dad");
    rs.set("Id", "1000");
    equal(rs.get("Id"), "1000");

    ok(rs.features().get(RiTa.PHONEMES) == null); // this should NOT create
						  // default features

    ok(rs.get(RiTa.PHONEMES) != null); // this should create default features

    Map<String, String> features = rs.features();
    ok(features.get(RiTa.PHONEMES) != null);

    rs.text("Dad & Mom"); // reset all original features, but not those set() by
			  // user

    equal(features.get(RiTa.PHONEMES), null); // OK: has been reset
    equal(rs.get("Id"), "1000"); // OK: has not been reset
  }

  @Test
  public void testAnalyze() {
    Map<String, String> features = new RiString(
	"Mom and Dad, waiting for the car, ate a steak.").analyze().features();

    ok(features);
    // RiTa.out(features);

    int numWords = features.get(TOKENS).split(" ").length;
    equal(numWords, features.get(STRESSES).split(" ").length);
    equal(numWords, features.get(PHONEMES).split(" ").length);
    equal(numWords, features.get(SYLLABLES).split(" ").length);
    equal(numWords, features.get(POS).split(" ").length);

    if (!RiLexicon.enabled)
      return; // Not using lexicon, further tests should fail

    equal(features.get(PHONEMES),
	"m-aa-m ae-n-d d-ae-d , w-ey-t-ih-ng f-ao-r dh-ah k-aa-r , ey-t ey s-t-ey-k .");
    equal(features.get(SYLLABLES),
	"m-aa-m ae-n-d d-ae-d , w-ey/t-ih-ng f-ao-r dh-ah k-aa-r , ey-t ey s-t-ey-k .");
    equal(features.get(STRESSES), "1 1 1 , 1/0 1 0 1 , 1 1 1 .");

    String txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
    RiString rs = new RiString(txt);
    rs.analyze();
    features = rs.features();
    // RiTa.out(features);
    ok(features);

    equal(features.get(PHONEMES),
	"dh-ah d-ao-g r-ae-n f-ae-s-t-er dh-ae-n dh-ah ah-dh-er d-ao-g . b-ah-t dh-ah ah-dh-er d-ao-g w-aa-z p-r-ih-t-iy-er .");
    equal(features.get(SYLLABLES),
	"dh-ah d-ao-g r-ae-n f-ae/s-t-er dh-ae-n dh-ah ah/dh-er d-ao-g . b-ah-t dh-ah ah/dh-er d-ao-g w-aa-z p-r-ih/t-iy/er .");
    // System.err.println("STRESSES: "+features.get(STRESSES));
    equal(features.get(STRESSES), "0 1 1 1/0 1 0 1/0 1 . 1 0 1/0 1 1 1/0/0 .");

    RiString ri = new RiString("The laggin dragon");
    ri.analyze();
    features = ri.features();
    ok(features);

    equal(features.get(PHONEMES), "dh-ah l-ae-g-ih-n d-r-ae-g-ah-n");
    equal(features.get(SYLLABLES), "dh-ah l-ae/g-ih-n d-r-ae/g-ah-n");
    equal(features.get(STRESSES), "0 1/1 1/0");

     features = new RiString("Tomatoes and apricots").analyze().features();
     ok(features);
     // LTS engine has the British pronounciation
     equal(features.get(PHONEMES), "t-aa-m-ah-t-ow-z ae-n-d ae-p-r-ah-k-aa-t-s");
     equal(features.get(SYLLABLES),
 	"t-aa/m-ah/t-ow-z ae-n-d ae/p-r-ah/k-aa-t-s");
     equal(features.get(STRESSES), "1/0/0 1 1/0/0");

    features = new RiString("dog").analyze().features();
    ok(features);
    equal(features.get(PHONEMES), "d-ao-g");
    equal(features.get(SYLLABLES), "d-ao-g");
    equal(features.get(STRESSES), "1");

    features = new RiString(".").analyze().features();
    ok(features);

    equal(features.get(PHONEMES), ".");
    equal(features.get(SYLLABLES), ".");
    equal(features.get(STRESSES), ".");
  }

  @Test
  public void testCharAt() {

    RiString rs = new RiString("The dog was white");

    String result = rs.charAt(0);
    equal(result, "T");

    result = rs.charAt(5);
    equal(result, "o");

    result = rs.charAt(4);
    notEqual(result, "o");

    result = rs.charAt(-1);
    // System.out.println("charAt :" + result);
    equal(result, "e");

    result = rs.charAt(-12);
    // System.out.println("charAt :" + result);
    equal(result, "o");

    result = rs.charAt(200);
    equal(result, "e");
  }

  @Test
  public void testConcatRiString() {
    RiString rs = new RiString("The dog was white");
    RiString rs2 = new RiString("The dog was not white");
    RiString result = rs.concat(rs2);
    equal(result.text(), "The dog was whiteThe dog was not white");

    rs = new RiString(" The dog was white ");
    rs2 = new RiString("The dog was not white ");
    result = rs.concat(rs2);
    equal(result.text(), " The dog was white The dog was not white ");

    rs = new RiString("#$#@#$@#");
    rs2 = new RiString("The dog was not white ");
    result = rs.concat(rs2);
    equal(result.text(), "#$#@#$@#The dog was not white ");
  }

  @Test
  public void testConcatString() {
    RiString rs = new RiString("The dog was white");
    String rs2 = ("The dog was not white");
    RiString result = rs.concat(rs2);
    equal(result.text(), "The dog was whiteThe dog was not white");

    rs = new RiString(" The dog was white ");
    rs2 = ("The dog was not white ");
    result = rs.concat(rs2);
    equal(result.text(), " The dog was white The dog was not white ");

    rs = new RiString("#$#@#$@#");
    rs2 = ("The dog was not white ");
    result = rs.concat(rs2);
    equal(result.text(), "#$#@#$@#The dog was not white ");
  }

  @Test
  public void testEndsWith() {
    RiString rs = new RiString("girls");
    boolean result = rs.endsWith("s");
    ok(result);

    rs = new RiString("closed");
    result = rs.endsWith("ed");
    ok(result);

    rs = new RiString("The dog was white");
    result = rs.endsWith("white");
    ok(result);

    rs = new RiString("");
    result = rs.endsWith("");
    ok(result);
  }

  @Test
  public void testEqualsObject() {
    RiString rs = new RiString("closed");
    RiString rs2 = new RiString("closed");
    boolean result = rs.equals(rs2);
    ok(result);

    rs = new RiString("closed");
    rs2 = new RiString("Closed");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("clOsed");
    rs2 = new RiString("closed");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("There is a cat.");
    rs2 = new RiString("There is a cat.");
    result = rs.equals(rs2);
    ok(result);

    rs = new RiString("There is a cat.");
    rs2 = new RiString("There is a cat. ");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("There is a cat.");
    rs2 = new RiString("There is a cat");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("There is a cat.");
    rs2 = new RiString("");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("");
    rs2 = new RiString("");
    result = rs.equals(rs2);
    ok(result);
  }

  @Test
  public void testEqualsString() {
    RiString rs = new RiString("closed");
    boolean result = rs.equals("closed");
    ok(result);

    rs = new RiString("closed");
    result = rs.equals("Closed");
    ok(!result);

    rs = new RiString("clOsed");
    result = rs.equals("closed");
    ok(!result);

    rs = new RiString("There is a cat.");
    result = rs.equals("There is a cat.");
    ok(result);

    rs = new RiString("There is a cat.");
    result = rs.equals("There is a cat. ");
    ok(!result);

    rs = new RiString("There is a cat.");
    result = rs.equals("There is a cat");
    ok(!result);

    rs = new RiString("There is a cat.");
    result = rs.equals("");
    ok(!result);
  }

  @Test
  public void testEqualsIgnoreCaseString() {
    RiString rs = new RiString("closed");
    boolean result = rs.equalsIgnoreCase("Closed");
    ok(result);

    rs = new RiString("There is a cat.");
    result = rs.equalsIgnoreCase("TheRe Is a cAt.");
    ok(result);

    rs = new RiString("THere iS a Cat.");
    result = rs.equalsIgnoreCase("TheRe Is a cAt.");
    ok(result);

    rs = new RiString("THere iS a Cat.");
    String rs2 = "THere iS a Cat.";
    result = rs.equalsIgnoreCase(rs2);
    ok(result);

    rs = new RiString("THere iS a Cat.");
    rs2 = "THere iS not a Cat.";
    result = rs.equalsIgnoreCase(rs2);
    ok(!result);

    rs = new RiString("");
    result = rs.equalsIgnoreCase("");
    ok(result);
  }

  @Test
  public void testGet() {
    RiString rs = new RiString("The laggin dragon").analyze();
    String ph = rs.get(RiTa.PHONEMES); // TODO
    String sy = rs.get(RiTa.SYLLABLES);
    String st = rs.get(RiTa.STRESSES);

    ok(ph);
    ok(sy);
    ok(st);

    if (!RiLexicon.enabled)
      return; // Not using lexicon, further tests should fail

    equal(ph, "dh-ah l-ae-g-ih-n d-r-ae-g-ah-n");
    // System.out.println(sy);
    equal(sy, "dh-ah l-ae/g-ih-n d-r-ae/g-ah-n");
    // in RITAJS equal(sy, "dh-ah l-ae/g-ih-n d-r-ae-g/ax-n");

    equal(st, "0 1/1 1/0");

    // fail("Needs more"); refer to tests in analyze()

    rs = new RiString("Tomatoes and apricots").analyze();
    ph = rs.get(RiTa.PHONEMES);
    sy = rs.get(RiTa.SYLLABLES);
    st = rs.get(RiTa.STRESSES);

    ok(ph);
    ok(sy);
    ok(st);

    if (!RiLexicon.enabled)
      return;

    // LTS engine has the British pronounciation
    equal(ph, "t-aa-m-ah-t-ow-z ae-n-d ae-p-r-ah-k-aa-t-s");
    equal(sy, "t-aa/m-ah/t-ow-z ae-n-d ae/p-r-ah/k-aa-t-s");
    equal(st, "1/0/0 1 1/0/0");
  }

  @Test
  public void testFeatures() {
    RiString rs = new RiString("Returns the array of words.").analyze();
    Map<String, String> features = rs.features();
    ok(features);
    ok(features.containsKey(RiTa.TEXT));
    ok(features.containsKey(RiTa.SYLLABLES));
    ok(features.containsKey(RiTa.PHONEMES));
    ok(features.containsKey(RiTa.STRESSES));
    ok(features.containsKey(RiTa.TOKENS));
    ok(features.containsKey(RiTa.POS));
    ok(rs.get(RiTa.SYLLABLES));
    ok(rs.get(RiTa.PHONEMES));
    ok(rs.get(RiTa.STRESSES));
    ok(rs.get(RiTa.TEXT));
    ok(rs.get(RiTa.TOKENS));
    ok(rs.get(RiTa.POS));
  }

  @Test
  public void testIndexOf() {
    RiString rs = new RiString("Returns the array of words.");
    int result = rs.indexOf("e");
    equal(result, 1);

    rs = new RiString("Returns the array of words .");
    result = rs.indexOf("a");
    equal(result, 12);

    rs = new RiString("s ."); // space
    result = rs.indexOf(" ");
    equal(result, 1);

    rs = new RiString("s  ."); // double space
    result = rs.indexOf("  ");
    equal(result, 1);

    rs = new RiString("s    ."); // tab space
    result = rs.indexOf("   ");
    equal(result, 1);

    rs = new RiString(" abc"); // space
    result = rs.indexOf(" ");
    equal(result, 0);

    rs = new RiString("  abc"); // double space
    result = rs.indexOf("  ");
    equal(result, 0);

    rs = new RiString(" abc"); // tab space
    result = rs.indexOf("   ");
    equal(result, -1);

    rs = new RiString("Returns the array of words .");
    result = rs.indexOf("array");
    equal(result, 12);

    rs = new RiString("Returns the array of words.");
    result = rs.indexOf(",");
    equal(result, -1);

    /*
     * rs = new RiString(
     * "Returns the array of words. Returns the array of words."); result =
     * rs.indexOf("a", 13); equal(result, 15);
     * 
     * rs = new RiString(
     * "Returns the array of words. Returns the array of words?"); result =
     * rs.indexOf("array", 1); equal(result, 40);
     */

    rs = new RiString(
	"Returns the array of words. Returns the array of words.");
    result = rs.indexOf("");
    equal(result, 0);
  }

  @Test
  public void testInsertWord() {
    RiString rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(4, "then");
    String rs3 = "Inserts at wordIdx and then shifts each subsequent word accordingly.";
    equal(rs.text(), rs3);

    rs = new RiString(
	"inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(0, "He");
    rs3 = "He inserts at wordIdx and shifts each subsequent word accordingly.";
    equal(rs.text(), rs3);

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(1, "newWord");
    RiString rs2 = new RiString(
	"Inserts newWord at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(1, "newWord and newWords");
    rs2 = new RiString(
	"Inserts newWord and newWords at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, " ");
    rs2 = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    ok(rs.text().equals(rs2.text()));

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, " "); // space
    rs2 = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, "  "); // tab space
    rs2 = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    // not sure what to do about this one, either it OR the next one will fail
    // either way
    /* TODO: reconsider */
    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, "**");
    rs2 = new RiString(
	"Inserts at wordIdx and shifts ** each subsequent word accordingly.");
    // System.out.println("'"+rs.text()+"'\n'"+rs2.text()+"'");
    equal(rs.text(), rs2.text()); // "testing the (private) joinWords() actually
				  // [currently failing]"

    rs = new RiString(
	"Inserts at wordIdx shifting each subsequent word accordingly.");
    rs.insertWord(3, ",");
    rs2 = new RiString(
	"Inserts at wordIdx, shifting each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(2, "newWord"); // error -- (no change to original string);
    equal(rs.text(), rs.text());

    // out of range test
    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(500, "hey");
    rs2 = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly. hey");
    equal(rs.text(), rs2.text());

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(-500, "hey");
    rs2 = new RiString(
	"hey Inserts at wordIdx and shifts each subsequent word accordingly.");
    // System.out.println("RESULT: "+rs.text());
    equal(rs.text(), rs2.text());

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(500, "");
    rs2 = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(-500, "");
    rs2 = new RiString(
	"Inserts at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

  }

  @Test
  public void testLastIndexOf() {
    RiString rs = new RiString(
	"Start at first character. Start at last character.");
    int result = rs.lastIndexOf("r");
    equal(result, 48);

    rs = new RiString("Start at first character. Start at last character.");
    result = rs.lastIndexOf("Start");
    equal(result, 26);

    rs = new RiString("Start at first character. Start at last character.");
    result = rs.lastIndexOf("start");
    equal(result, -1);

    /*
     * rs = new RiString("Start at first character. Start at last character.");
     * result = rs.lastIndexOf("a", 12); equal(result, 6);
     * 
     * rs = new RiString("Start at first character. Start at last character.");
     * result = rs.lastIndexOf("at", 12); equal(result, 6);
     */
    // TODO 2 parameter is not implemented
    // TODO extra / wrong parameter test

    rs = new RiString("Start at first character. Start at last character.");
    result = rs.lastIndexOf("");
    equal(result, rs.length()); // should be 50 or -1? 50(DCH)

  }

  @Test
  public void testLength() {
    RiString rs = new RiString("S");
    int result = rs.length();
    equal(result, 1);

    rs = new RiString("s "); // space
    result = rs.length();
    equal(result, 2);

    rs = new RiString("s" + '\t'); // tab space
    result = rs.length();
    equal(result, 2);

    rs = new RiString(" s "); // 2 space
    result = rs.length();
    equal(result, 3);

    rs = new RiString('\t' + "s" + '\t'); // 2 tab space
    result = rs.length();
    equal(result, 3);

    rs = new RiString("s b");
    result = rs.length();
    equal(result, 3);

    rs = new RiString("s b.");
    result = rs.length();
    equal(result, 4);

    rs = new RiString("s b ."); // space
    result = rs.length();
    equal(result, 5);

    rs = new RiString("><><><#$!$@$@!$");
    result = rs.length();
    equal(result, 15);

    rs = new RiString("");
    result = rs.length();
    equal(result, 0);
  }

  @Test
  public void testMatch() // TODO: check these results against JS [C]
  {
    RiString rs = new RiString("The rain in SPAIN stays mainly in the plain");
    String[] result = rs.match("ain");
    // System.out.println(RiTa.asList(result));
    deepEqual(result, new String[] { "ain", "ain", "ain" });

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain");
    deepEqual(result, new String[] { "ain", "ain", "ain" });

    rs = new RiString("Watch out for the rock!");
    result = rs.match("r?or?");
    deepEqual(result, new String[] { "o", "or", "ro" });

    rs = new RiString("abc!");
    result = rs.match("r?or?");
    deepEqual(result, new String[] {});

    rs = new RiString("Letter !>D? hello 213331123");
    result = rs.match("[A-Za-z]");
    // for(int i =0;i<result.length;i++){
    // System.out.println(result[i]);
    // }
    deepEqual(result, new String[] { "L", "e", "t", "t", "e", "r", "D", "h",
	"e", "l", "l", "o" });

    rs = new RiString("Letter !>D? hello 213331123");
    result = rs.match("\\W");
    // for(int i =0;i<result.length;i++){
    // System.out.println(result[i]);
    // }
    deepEqual(result, new String[] { " ", "!", ">", "?", " ", " " });

    rs = new RiString("Letter !>D? hello 213331123");
    result = rs.match("[^0-9]");
    // for(int i =0;i<result.length;i++){
    // System.out.println(result[i]);
    // }
    deepEqual(result, new String[] { "L", "e", "t", "t", "e", "r", " ", "!",
	">", "D", "?", " ", "h", "e", "l", "l", "o", " " });

    rs = new RiString("!@#$%^&*()__+");
    result = rs.match("X|Z");
    // for(int i =0;i<result.length;i++){
    // System.out.println(result[i]);
    // }
    deepEqual(result, new String[] {});

    rs = new RiString("!@#$%^&*()__+");
    result = rs.match("!|Z");
    // for(int i =0;i<result.length;i++){
    // System.out.println(result[i]);
    // }
    deepEqual(result, new String[] { "!" });

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain", Pattern.CASE_INSENSITIVE); // gi
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
  }

  @Test
  public void testPos() {
    RiString rs = new RiString("clothes");
    String[] result = rs.pos();
    String[] answer = new String[] { "nns" };
    deepEqual(answer, result);

    rs = new RiString("teeth");
    result = rs.pos();
    answer = new String[] { "nns" };
    deepEqual(answer, result);

    rs = new RiString("asdfaasd");
    result = rs.pos();
    answer = new String[] { "nn" };
    // System.out.println(RiTa.asList(result));
    deepEqual(answer, result);

    rs = new RiString("cat");
    result = rs.pos();
    // System.out.println(RiTa.asList(result));
    answer = new String[] { "nn" };
    deepEqual(answer, result);

    if (!RiLexicon.enabled)
      return; // Not using lexicon, further tests should fail

    // rs = new RiString("There is a cat.");
    // result = rs.pos();
    // answer = new String[] { "ex", "vbz", "dt", "nn", "." };
    //
    // if (1==0)
    // {
    // System.out.println("==========================");
    // System.out.println(RiTa.asList(answer));
    // System.out.println(Arrays.asList(rs.pos()));
    // System.out.println("==========================");
    // }
    // deepEqual(answer, result);// KnownIssues

    rs = new RiString("The boy, dressed in red, ate an apple.");
    result = rs.pos();

    answer = new String[] { "dt", "nn", ",", "vbn", "in", "jj", ",", "vbd",
	"dt", "nn", "." };
    // System.out.println(RiTa.asList(result));

    deepEqual(answer, result);
  }

  @Test
  public void testPosAt() {

    RiString rs = new RiString("The emperor had no clothes on.");
    String result = rs.posAt(4);
    equal("nns", result);

    rs = new RiString("She bought a few knives.");
    result = rs.posAt(4);
    equal("nns", result);

    rs = new RiString("There is a cat.");
    result = rs.posAt(3);
    equal("nn", result);

    rs = new RiString("There is a cat.");
    result = rs.posAt(2);
    equal("dt", result);

    // out of range test

    rs = new RiString("There is a cat.");
    result = rs.posAt(-3);
    equal("dt", result);

    rs = new RiString("There is a cat.");
    result = rs.posAt(-1);

    equal(".", result);

    rs = new RiString("There is a cat.");
    result = rs.posAt(300);
    // System.out.println("res="+result);
    equal(".", result);
  }

  @Test
  public void testRemoveChar() {
    RiString rs = new RiString("The dog was white");
    rs.removeChar(1);
    equal(rs.text(), "Te dog was white");

    rs = new RiString("The dog was white");
    rs.removeChar(rs.length() - 1);
    equal(rs.text(), "The dog was whit");

    rs = new RiString("The dog was white");
    rs.removeChar(0);
    equal(rs.text(), "he dog was white");

    rs = new RiString("The dog was white.");
    rs.removeChar(rs.length() - 1);
    equal(rs.text(), "The dog was white");

    // out of range tests

    rs = new RiString("The dog was white");
    rs.removeChar(-1);
    equal(rs.text(), "The dog was whit");

    rs = new RiString("The dog was white");
    rs.removeChar(1000);
    equal(rs.text(), "The dog was whit");

    rs = new RiString("The dog was white");
    rs.removeChar(rs.length());
    equal(rs.text(), "The dog was whit");
  }

  @Test
  public void testReplaceChar() {
    RiString rs = new RiString("Who are you?");
    rs.replaceChar(2, "");
    // System.out.println(rs.text());
    equal(rs.text(), "Wh are you?");

    rs = new RiString("Who are you?");
    rs.replaceChar(2, "e");
    equal(rs.text(), "Whe are you?");

    rs = new RiString("Who are you?");
    rs.replaceChar(2, "ere");
    equal(rs.text(), "Where are you?");

    rs = new RiString("Who are you?");
    rs.replaceChar(11, "!!");
    equal(rs.text(), "Who are you!!");

    rs = new RiString("Who are you?");
    rs.replaceChar(0, "me");
    equal(rs.text(), "meho are you?");

    // out of range test
    rs = new RiString("Who are you?");
    // System.out.println("replaceCharAt :" + rs.text());
    rs.replaceChar(-1, "me");
    equal(rs.text(), "Who are youme");

    rs = new RiString("Who are you?");
    // System.out.println("replaceCharAt :" + rs.text());
    rs.replaceChar(10000, "me");
    equal(rs.text(), "Who are youme");

  }

  @Test
  public void testReplaceFirst() // TODO: check these results against JS [C]
  {
    RiString rs = new RiString("Who are you?");
    rs.replaceFirst("e", "E");
    equal(rs.text(), "Who arE you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("o", "O");
    equal(rs.text(), "WhO are you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("Who", "Where");
    equal(rs.text(), "Where are you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("notExist", "Exist");
    equal(rs.text(), "Who are you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("Who are", "Dare");
    equal(rs.text(), "Dare you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("Who aRe", "Dare");
    equal(rs.text(), "Who are you?");

    rs = new RiString("Who are you? Who are you?");
    rs.replaceFirst("Who are", "Dare");
    equal(rs.text(), "Dare you? Who are you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("", "");
    equal(rs.text(), "Who are you?");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst("ain", "ane");
    equal(rs.text(), "The rane in SPAIN stays mainly in the plain");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst("ain", "oll");
    equal(rs.text(), "The roll in SPAIN stays mainly in the plain");

    rs = new RiString("Watch out for the rock!");
    rs.replaceFirst("r?or?", "a");
    equal(rs.text(), "Watch aut for the rock!");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst("in", "");
    equal(rs.text(), "The ra in SPAIN stays mainly in the plain");

    // rs = new RiString("Who are you?"); rs.replaceFirst("?", "?!"); // illegal
    // regex
    rs = new RiString("Who are you?");
    rs.replaceFirst("\\?", "?!");
    equal(rs.text(), "Who are you?!");
  }

  @Test
  public void testReplaceAll() // TODO: check these results against JS [C]
  {
    RiString rs = new RiString("Who are you? Who is he? Who is it?");
    equal(rs.replaceAll("e", "E").text(), "Who arE you? Who is hE? Who is it?");

    rs = new RiString("Who are you? Who is he? Who is it?");
    equal(rs.replaceAll("Who", "O").text(), "O are you? O is he? O is it?");

    rs = new RiString("Whom is he? Where is he? What is it?");
    equal(rs.replaceAll("Wh*", "O").text(),
	"Oom is he? Oere is he? Oat is it?");

    rs = new RiString("%^&%&?");
    equal(rs.replaceAll("%^&%&?", "!!!").text(), "%^&%&?");

    rs = new RiString("Who are you?");
    equal(rs.replaceAll("notExist", "Exist").text(), "Who are you?");

    rs = new RiString("Who are you?");
    equal(rs.replaceAll("", "").text(), "Who are you?");

    rs = new RiString("");
    equal(rs.replaceAll("", "").text(), "");

    rs = new RiString("gray sky or grey sea or great tree");
    equal(rs.replaceAll("gr[ae]y", "jay").text(),
	"jay sky or jay sea or great tree");

    rs = new RiString("skipping quality quick question qat qx");
    equal(rs.replaceAll("q[^u]", "yay").text(),
	"skipping quality quick question yayt yay");
  }

  @Test
  public void testReplaceWord() {
    RiString rs = new RiString("Who are you?");
    rs.replaceWord(2, ""); // nice! this too...
    equal(rs.text(), "Who are?");

    rs = new RiString("Who are you?");
    rs.replaceWord(2, "What");
    equal(rs.text(), "Who are What?");

    rs = new RiString("Who are you?");
    rs.replaceWord(0, "What");
    equal(rs.text(), "What are you?");

    rs = new RiString("Who are you?");
    rs.replaceWord(3, "!!");
    equal(rs.text(), "Who are you!!"); // nice! this is a strange one...

    // out of range test

    rs = new RiString("Who are you?");
    rs.replaceWord(-1, "asfasf"); // negative number
    equal(rs.text(), "Who are you asfasf");

    rs = new RiString("Who are you?");
    rs.replaceWord(-2, "asfasf"); // negative number
    equal(rs.text(), "Who are asfasf?");

    rs = new RiString("Who are you?");
    rs.replaceWord(20, "asfasf");
    equal(rs.text(), "Who are you asfasf");
  }

  @Test
  public void testSlice() {
    RiString rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(1, 3), "he");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(-3, -2), "f");

    rs = new RiString("!@#$%^&**()");
    equal(rs.slice(2, 5), "#$%");

    rs = new RiString("The Australian");
    equal(rs.slice(-5, -3), "al");

    // out of range test

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(10, 10), "");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    // System.out.println("slice:" + rs.slice(15, 500));
    equal(rs.slice(15, 500),
	"Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    // System.out.println("slice: " + rs.slice(500, 501));
    equal(rs.slice(500, 501), ".");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(3, 1), "he");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(-2, 3),
	" Australian Pavilion at the Venice Biennale is getting a much-needed facelif");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(-2, -3), "f");
  }

  @Test
  public void testSplit() {
    RiString rs, result[], answer[];

    rs = new RiString("Who are you?");
    result = rs.split("\\?");
    answer = new RiString[] { new RiString("Who are you") };
    // System.out.println(RiTa.asList(answer));
    // System.out.println(result.length +" ? "+ answer.length);
    equal(result.length, answer.length);
    equal(result.length, 1);
    equal(result[0].text(), answer[0].text());

    rs = new RiString("Who are you?");
    result = rs.split("W");
    answer = new RiString[] { new RiString("ho are you?") };
    // System.out.println(RiTa.asList(answer));
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split(" ");
    answer = new RiString[] { new RiString("Who"), new RiString("are"),
	new RiString("you?") };
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split(" ");
    answer = new RiString[] { new RiString("Who"), new RiString("are"),
	new RiString("you?") };
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("are");
    answer = new RiString[] { new RiString("Who "), new RiString(" you?") };
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("abc");
    answer = new RiString[] { new RiString("Who are you?") };
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("");
    // System.out.println(RiTa.asList(result));
    answer = new RiString[result.length];
    String[] chars = { "W", "h", "o", " ", "a", "r", "e", " ", "y", "o", "u",
	"?" };
    for (int i = 0; i < chars.length; i++)
      answer[i] = new RiString(chars[i]);

    equal(result, answer);

    // 2 parameter
    /*
     * rs = new RiString("Who are you?"); result = rs.split("", 3); answer = new
     * RiString[] { RiString("W"), RiString("h"), RiString("o") };
     * deepEqual(result, answer);
     * 
     * rs = new RiString("Who are you?"); result = rs.split("", 0); answer = {};
     * deepEqual(result, answer);
     * 
     * rs = new RiString("Who are you?"); result = rs.split("", 100); answer =
     * new RiString[] {}; chars = new char[] { "W", "h", "o", " ", "a", "r",
     * "e", " ", "y", "o", "u", "?"}; for ( int i = 0; i < chars.length; i++) {
     * answer[i] = RiString(chars[i]); } deepEqual(result, answer);
     */

  }

  @Test
  public void testStartsWith() {
    RiString rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("T"));

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("The"));

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(!rs.startsWith("Aus"));

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(!rs.startsWith("*"));

    rs = new RiString(
	"*The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("*"));

    rs = new RiString(
	" The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith(" ")); // single space

    rs = new RiString(
	"  The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("  ")); // double space

    rs = new RiString(
	" The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(!rs.startsWith("  ")); // tab space
  }

  @Test
  public void testSubstring() {
    RiString rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(-2, 3),
	" Australian Pavilion at the Venice Biennale is getting a much-needed facelif");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(1, 3), "he");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(10, 10), "");

    rs = new RiString("!@#$%^&**()");
    equal(rs.substring(2, 5), "#$%");

    rs = new RiString("Start at first character.");
    equal(rs.substring(1, 5), "tart");

    rs = new RiString("Start at first character.");
    equal(rs.substring(0, 1), "S");

    rs = new RiString("Start at first character.");
    equal(rs.substring(0, 1), "S");

    // out of range test

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(3, 1), "he");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(15, 500),
	"Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(500, 501), ".");

    rs = new RiString("Start at first character.");
    equal(rs.substring(3), "rt at first character.");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(1),
	"he Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");

    equal(rs.substring(-2), "t.");
  }

  @Test
  public void testSubstr() {
    RiString rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substr(1, 3), "he ");

    rs = new RiString(
	"The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substr(10, 10), "lian Pavil");

    rs = new RiString("!@#$%^&**()");
    equal(rs.substr(2, 5), "#$%^&");

    try {
      rs = new RiString(
	  "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
      rs.substr(-2, 3);

      rs = new RiString(
	  "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
      rs.substr(15, 500);

      rs = new RiString(
	  "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
      rs.substr(500, 501);

    } catch (Exception e) {
      ok(e);
    }

  }

  @Test
  public void testText() {
    RiString rs = new RiString("this door is closed");
    String result = rs.text();
    equal(result, "this door is closed");

    rs = new RiString("this door, is closed.*&)*^");
    result = rs.text();
    equal(result, "this door, is closed.*&)*^");

    rs = new RiString("   this door    , is closed.");
    result = rs.text();
    equal(result, "   this door    , is closed.");

    rs = new RiString("this Door is closed");
    result = rs.text();
    notEqual(result, "this door is closed");

    rs = new RiString("");
    result = rs.text();
    equal(result, "");

    // RiString text(String s);
    rs = new RiString("this door is closed");
    RiString result2 = rs.text("this door is opened");
    equal(result2.text(), "this door is opened");

    rs = new RiString("this door, is closed.*&)*^");
    result2 = rs.text("this door, is not closed.*&)*^");
    equal(result2.text(), "this door, is not closed.*&)*^");

    rs = new RiString("   this door    , is closed.");
    result2 = rs.text("   this door    , isn't closed.");
    equal(result2.text(), "   this door    , isn't closed.");

    rs = new RiString("this Door is closed");
    result2 = rs.text("this stupid Door is closed");
    notEqual(result2, "this stupid Door is closed");

    rs = new RiString("");
    result2 = rs.text("!");
    equal(result2.text(), "!");

  }

  @Test
  public void testToCharArray() {
    RiString rs = new RiString("The Australian Pavilion.");
    char[] result = rs.toCharArray();
    char[] answer = { 'T', 'h', 'e', ' ', 'A', 'u', 's', 't', 'r', 'a', 'l',
	'i', 'a', 'n', ' ', 'P', 'a', 'v', 'i', 'l', 'i', 'o', 'n', '.' };
    for (int i = 0; i < result.length; i++)
      equal(result[i], answer[i]);
    // deepEqual(result, answer); // TODO: deepEquals seems to be broken for
    // char[]

    rs = new RiString("h5i5 5%^&*() tab space	");
    result = rs.toCharArray();
    answer = new char[] { 'h', '5', 'i', '5', ' ', '5', '%', '^', '&', '*', '(',
	')', ' ', 't', 'a', 'b', ' ', 's', 'p', 'a', 'c', 'e', '	' };
    for (int i = 0; i < result.length; i++)
      equal(result[i], answer[i]);

  }

  @Test
  public void testToLowerCase() {
    RiString rs = new RiString("The Australian Pavilion.");
    rs.toLowerCase();
    equal("the australian pavilion.", rs.text());

    rs = new RiString("the Australian pavilion.");
    rs.toLowerCase();
    equal("the australian pavilion.", rs.text());

    rs = new RiString(")(*(&^%%$!#$$%%^))");
    rs.toLowerCase();
    equal(")(*(&^%%$!#$$%%^))", rs.text());
  }

  @Test
  public void testToUpperCase() {
    RiString rs = new RiString("The Australian Pavilion.");
    rs.toUpperCase();
    equal("THE AUSTRALIAN PAVILION.", rs.text());

    rs = new RiString(")(*(&^%%$!#$$%%^))");
    rs.toUpperCase();
    equal(")(*(&^%%$!#$$%%^))", rs.text());

  }

  @Test
  public void testTrim() {
    RiString rs = null;

    rs = new RiString("Start at first character. ");
    equal(rs.trim(), new RiString("Start at first character."));

    rs = new RiString(" Start at first character.");
    equal(rs.trim(), new RiString("Start at first character."));

    rs = new RiString("     Start at first character.   "); // tabs
    equal(rs.trim(), new RiString("Start at first character."));

    rs = new RiString("     Start at first character.    "); // spaces/tabs
    equal(rs.trim(), new RiString("Start at first character."));
  }

  @Test
  public void testWordAt() {
    RiString rs = new RiString(
	"Returns the word at wordIdx using the default WordTokenizer.");
    String result = rs.wordAt(0);
    equal(result, "Returns");

    result = rs.wordAt(-5);
    // System.out.println("result="+result);
    equal(result, "using");

    result = rs.wordAt(1);
    equal(result, "the");

    result = rs.wordAt(9);
    equal(result, ".");

    result = rs.wordAt(-1);
    equal(result, ".");

    result = rs.wordAt(500);
    equal(result, ".");

    rs = new RiString("");
    result = rs.wordAt(0);
    equal(result, "");

    result = rs.wordAt(-5);
    // System.out.println("result="+result);
    equal(result, "");
  }

  @Test
  public void testWordCount() {
    RiString rs = new RiString(
	"Returns the word at wordIdx using the default WordTokenizer.");
    int result = rs.wordCount();
    equal(result, 10); // correct, according to WordTokenizer, need to try with
		       // RegexTokenizer

    rs = new RiString("Returns the word.Returns the word. Returns the word .");
    result = rs.wordCount();
    equal(result, 12);

    rs = new RiString(
	"   Returns the word at wordIdx , using the default WordTokenizer."); // space
    result = rs.wordCount();
    equal(result, 11);

    rs = new RiString(
	" Returns the word at wordIdx , using the default WordTokenizer.  "); // tab
									      // space
    result = rs.wordCount();
    equal(result, 11);

    rs = new RiString("");
    result = rs.wordCount();
    // System.out.println("wordcount: "+result);
    equal(result, 0);
  }

  @Test
  public void testWords() {
    RiString rs = new RiString("Returns the array of words.");
    String[] result = rs.words();
    String[] answer = { "Returns", "the", "array", "of", "words", "." };
    deepEqual(result, answer);

    rs = new RiString("The boy, dressed in red, ate an array.");
    result = rs.words();
    answer = new String[] { "The", "boy", ",", "dressed", "in", "red", ",",
	"ate", "an", "array", "." };
    deepEqual(result, answer);

    rs = new RiString("Returns the array of words .");
    result = rs.words();
    answer = new String[] { "Returns", "the", "array", "of", "words", "." };
    deepEqual(result, answer);

    rs = new RiString("The boy, dressed in red , ate an array?");
    result = rs.words();
    answer = new String[] { "The", "boy", ",", "dressed", "in", "red", ",",
	"ate", "an", "array", "?" };
    deepEqual(result, answer);

    rs = new RiString("");
    result = rs.words();
    deepEqual(result, new String[0]);
  }

  @Test
  public void testCopy() {
    RiString rs = new RiString("copy cat");
    RiString rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("copy dogs.");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("cOPy dOgs.");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("!@#$%^&*()_+");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("!@#$sadas*()_+");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("copy cat");
    rs.analyze();
    rs2 = rs.copy();
    deepEqual(rs.features(), rs2.features());

    rs = new RiString("copy cat");
    rs.set("myFeatureName", "myFeatureValue");
    rs2 = rs.copy();
    equal(rs.get("myFeatureName"), rs2.get("myFeatureName"));
  }

  @Test
  public void testStringify() {

    String[][][] data = new String[][][] { { { "2" }, {}, { "ao" }, { "r" } },
	{ { "0" }, { "g" }, { "ah" }, {} }, { { "0" }, { "n" }, { "ah" }, {} },
	{ { "1" }, { "z" }, { "ey" }, {} },
	{ { "0" }, { "sh" }, { "ah" }, { "n", "z" } } };
    String out = "ao2-r g-ah0 n-ah0 z-ey1 sh-ah0-n-z";
    equal(RiString.stringify(data), out);
  }
  /*
   * 
   * @Test public void testSyllabifyString() { String test =
   * "ao2-r-g-ah0-n-ah0-z-ey1-sh-ah0-n-z"; String result =
   * RiString.syllabify(test); deepEqual("ao2-r g-ah0 n-ah0 z-ey1 sh-ah0-n-z",
   * result); }
   * 
   * @Test public void testSyllabifyArray() { String[] test =
   * "ao2-r-g-ah0-n-ah0-z-ey1-sh-ah0-n-z".split("-"); String result =
   * RiString.syllabify(test); deepEqual("ao2-r g-ah0 n-ah0 z-ey1 sh-ah0-n-z",
   * result);
   * 
   * test = "m-aa1-m".split("-"); //System.out.println(Arrays.asList(test));
   * result = RiString.syllabify(test); deepEqual("m-aa1-m", result); }
   * 
   * @Test public void testSyllabifyBatch() {
   * 
   * String[][] data = new String[][] { { "d-eh1-n-l-ih0-n-jh-er0",
   * "d-eh1-n l-ih0-n jh-er0" }, { "d-uw1-ah0-l", "d-uw1 ah0-l" }, {
   * "d-ih2-s-ah0-l-aw1-d", "d-ih2 s-ah0 l-aw1-d" }, { "d-aa1-d-z", "d-aa1-d-z"
   * }, { "d-r-ao1-l-z", "d-r-ao1-l-z" }, { "d-ay0-ae1-n-ah0", "d-ay0 ae1 n-ah0"
   * }, { "ey1-t-f-ow2-l-d", "ey1-t f-ow2-l-d" }, { "eh1-m-t-iy0-d",
   * "eh1-m t-iy0-d" }, { "ih0-r-ey1-s", "ih0 r-ey1-s" }, { "eh1-v-r-ah0-n",
   * "eh1-v r-ah0-n" }, { "f-ae1-l-k", "f-ae1-l-k" }, { "f-eh1-n-w-ey2",
   * "f-eh1-n w-ey2" }, { "f-ih1-sh-k-ih2-l", "f-ih1-sh k-ih2-l" }, {
   * "f-ao1-r-b-ih0-d-ah0-n", "f-ao1-r b-ih0 d-ah0-n" }, { "f-r-eh1-n-t-s",
   * "f-r-eh1-n-t-s" }, { "g-ae1-l-b-r-ey2-th", "g-ae1-l b-r-ey2-th" }, {
   * "zh-ih0-l-eh1-t", "zh-ih0 l-eh1-t" }, { "jh-ih1-n-iy0", "jh-ih1 n-iy0" }, {
   * "g-aa0-n-z-aa1-l-ah0-z", "g-aa0-n z-aa1 l-ah0-z" }, {
   * "g-r-iy1-n-f-iy2-l-d", "g-r-iy1-n f-iy2-l-d" }, { "g-ih0-t-aa1-r-z",
   * "g-ih0 t-aa1-r-z" }, { "hh-ae1-m-er0-ih0-ng", "hh-ae1 m-er0 ih0-ng" }, {
   * "hh-ae1-t-ih0-n-d-ao0-r-f", "hh-ae1 t-ih0-n d-ao0-r-f" }, {
   * "hh-eh1-m-ih0-ng-w-ey2", "hh-eh1 m-ih0-ng w-ey2" }, {
   * "hh-ih1-ng-k-m-ah0-n", "hh-ih1-ng-k m-ah0-n" }, { "hh-ow1-n-eh0-k",
   * "hh-ow1 n-eh0-k" }, { "hh-ah1-l-d", "hh-ah1-l-d" }, { "ih0-l-uw1-zh-ah0-n",
   * "ih0 l-uw1 zh-ah0-n" }, { "ih0-n-f-ae2-ch-uw0-ey1-sh-ah0-n",
   * "ih0-n f-ae2 ch-uw0 ey1 sh-ah0-n" }, { "ih0-n-t-er1-n-ah0-l-ay2-z",
   * "ih0-n t-er1 n-ah0 l-ay2-z" }, { "ih0-z-ae1-n-s-k-iy0-z",
   * "ih0 z-ae1-n s-k-iy0-z" }, { "jh-ow0-hh-ae1-n-ah0-s",
   * "jh-ow0 hh-ae1 n-ah0-s" }, { "k-ae1-r-ah0-m", "k-ae1 r-ah0-m" }, {
   * "k-aa1-k-iy0", "k-aa1 k-iy0" }, { "n-ey1-v", "n-ey1-v" } }; for (int i = 0,
   * l = data.length; i < l; i++) { String res = RiString.syllabify(data[i][0]);
   * equal(res, data[i][1]); } }
   */
  // /////////////////////////////////////////////////////////////////////

  public static void main(String[] args) {
    /*
     * new RiString(".").analyze().features(); if (1==1) return;
     */
    Result result = JUnitCore.runClasses(RiStringTest.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
  }
}
