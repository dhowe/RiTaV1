package rita.test;

import static rita.support.QUnitStubs.deepEqual;
import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.notEqual;
import static rita.support.QUnitStubs.ok;

import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

import rita.RiString;
import rita.RiTa;
import rita.RiText;

public class RiTextTest
{ 
  @Test
  public void testAnalyze()
  {
    // TODO: re-add all the commented tests below!
    RiText rt = new RiText(null, "the laggin dragon");
    rt.analyze();
    rt.features();
    ok(rt.features());
    
    // delegates to RiString
  }

  @Test
  public void testCharAt() // C
  {
    RiText rs = new RiText(null, "The dog was white");

    String result = rs.charAt(0);
    equal(result, "T");

    result = rs.charAt(5);
    notEqual(result, "O");

    result = rs.charAt(5);
    notEqual(result, "*");

    result = rs.charAt(-12); // out of range character
    //System.out.println("charAt :" + result);
    equal(result, "o");
    
    result = rs.charAt(200); // out of range character
    equal(result, "e");
    
  }

  @Test
  public void testConcatString()
  {
    RiText rs = new RiText(null, "The dog was white");
    String rs2 = "The dog was not white";
    RiText result = rs.concat(rs2);
    equal(result.text(), "The dog was whiteThe dog was not white");

    rs = new RiText(null, " The dog was white ");
    rs2 = "The dog was not white ";
    result = rs.concat(rs2);
    equal(result.text(), " The dog was white The dog was not white ");

    rs = new RiText(null, "#$#@#$@#");
    rs2 = "The dog was not white ";
    result = rs.concat(rs2);
    equal(result.text(), "#$#@#$@#The dog was not white ");

    rs = new RiText(null, "The dog was white");
    rs2 = "The dog was not white";
    rs.concat(rs2);
    equal(rs.text(), "The dog was whiteThe dog was not white");

    rs = new RiText(null, " The dog was white ");
    rs2 = "The dog was not white ";
    rs.concat(rs2);
    equal(rs.text(), " The dog was white The dog was not white ");

    rs = new RiText(null, "#$#@#$@#");
    rs2 = "The dog was not white ";
    rs.concat(rs2);
    equal(rs.text(), "#$#@#$@#The dog was not white ");
  }

  @Test
  public void testEndsWith()
  {
    // check that these are ok --------------
    RiText rs = new RiText(null, "girls");
    boolean result = rs.endsWith("s");
    ok(result);

    rs = new RiText(null, "closed");
    result = rs.endsWith("ed");
    ok(result);

    rs = new RiText(null, "The dog was white");
    result = rs.endsWith("white");
    ok(result);

    rs = new RiText(null, "");
    result = rs.endsWith("");
    ok(result);
  }

  @Test
  public void testEqualsIgnoreCaseString()
  {
    // check that these are ok ---------------
    RiText rs = new RiText(null, "closed");
    boolean result = rs.equalsIgnoreCase("Closed");
    ok(result);

    rs = new RiText(null, "There is a cat.");
    result = rs.equalsIgnoreCase("TheRe Is a cAt.");
    ok(result);

    rs = new RiText(null, "THere iS a Cat.");
    result = rs.equalsIgnoreCase("TheRe Is a cAt.");
    ok(result);

    rs = new RiText(null, "");
    result = rs.equalsIgnoreCase("");
    ok(result);
  }

  @Test
  public void testGet() // test on analyze()
  {
    RiText rs = new RiText(null, "The laggin dragon");
    rs.analyze();
    
      String ph = rs.get(RiTa.PHONEMES); 
      String sy = rs.get(RiTa.SYLLABLES);
      String st = rs.get(RiTa.STRESSES); 

      equal(ph,"dh-ah l-ae-g-ih-n d-r-ae-g-ah-n");
      equal(sy,"dh-ah l-ae/g-ih-n d-r-ae/g-ah-n"); 
      equal(st, "0 1/1 1/0");
  }

  @Test
  public void testFeatures() // TODO: no test in RITAJS (add to both)
  {
    RiText rs = new RiText(null, "Returns the array of words.");
    Map features = rs.features();
    
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
  
    features = null;
    
    rs = new RiText(null, "Returns the array of words.");
    rs.analyze();
    ok(rs.get(RiTa.SYLLABLES));
    ok(rs.get(RiTa.PHONEMES));
    ok(rs.get(RiTa.STRESSES));
    ok(rs.get(RiTa.TEXT));
    ok(rs.get(RiTa.TOKENS));
    ok(rs.get(RiTa.POS));
  }

  @Test
  public void testIndexOf()
  {
    // check that these are ok ---------------
    RiText rs = new RiText(null, "Returns the array of words.");
    int result = rs.indexOf("e");
    equal(result, 1);

    rs = new RiText(null, "Returns the array of words .");
    result = rs.indexOf("a");
    equal(result, 12);

    rs = new RiText(null, "s ."); // space
    result = rs.indexOf(" ");
    equal(result, 1);

    rs = new RiText(null, "s  ."); // double space
    result = rs.indexOf("  ");
    equal(result, 1);

    rs = new RiText(null, " abc"); // space
    result = rs.indexOf(" ");
    equal(result, 0);

    rs = new RiText(null, "  abc"); // double space
    result = rs.indexOf("  ");
    equal(result, 0);

    rs = new RiText(null, "   abc"); // tap space
    result = rs.indexOf("   ");
    equal(result, 0);

    rs = new RiText(null, "Returns the array of words .");
    result = rs.indexOf("array");
    equal(result, 12);

    rs = new RiText(null, "Returns the array of words.");
    result = rs.indexOf(",");
    equal(result, -1);

    /*
     * rs = new RiText(null,
     * "Returns the array of words. Returns the array of words."); result =
     * rs.indexOf("a", 13); equal(result, 15);
     * 
     * rs = new RiText(null,
     * "Returns the array of words. Returns the array of words?"); result =
     * rs.indexOf("array", 13); equal(result, 40);
     */

    rs = new RiText(null, "Returns the array of words. Returns the array of words.");
    result = rs.indexOf("");
    equal(result, 0);

  }

  @Test
  public void testInsertWord()
  {
    // check that these are ok ---------------
    RiText rs = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    RiText result = rs.insertWord(4, "then");
    equal(result.text(), rs.text());
    
    // TODO: next: fix whitespace regex!!

    rs = new RiText(null, "inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(0, "He");
    String rss2 = "He inserts at wordIdx and shifts each subsequent word accordingly.";
    //System.out.println("testInsertWordAt :'"+rs.text() + "'");
    equal(rs.text(), rss2);

    rs = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(1, "newWord");
    RiText rs2 = new RiText(null, "Inserts newWord at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(1, "newWord and newWords");
    rs2 = new RiText(null, "Inserts newWord and newWords at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    result = rs.insertWord(5, "");
    rs2 = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    equal(result.text(), rs2.text());

    rs = new RiText(null,  "Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, " "); // space
    rs2 = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, "  "); // tab space
    rs2 = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    //System.out.println(rs);
    equal(rs.text(), rs2.text());

    // not sure what to do about this one, either it OR the next one will fail
    // either way
    /* TODO: reconsider */
    rs = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, "**");
    rs2 = new RiText(null, "Inserts at wordIdx and shifts ** each subsequent word accordingly.");
    equal(rs.text(), rs2.text()); // "testing the (private) joinWords() actually [currently failing]"

    rs = new RiText(null, "Inserts at wordIdx shifting each subsequent word accordingly.");
    rs.insertWord(3, ",");
    rs2 = new RiText(null, "Inserts at wordIdx, shifting each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiText(null, "Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(-2, "newWord"); 
    equal("Inserts at wordIdx and shifts each subsequent word newWord accordingly.", rs.text());
  }

  @Test
  public void testLastIndexOf()
  {
    // check that these are ok --- ------------
    RiText rs = new RiText(null, "Start at first character. Start at last character.");
    int result = rs.lastIndexOf("r");
    equal(result, 48);

    rs = new RiText(null, "Start at first character. Start at last character.");
    result = rs.lastIndexOf("Start");
    equal(result, 26);

    rs = new RiText(null, "Start at first character. Start at last character.");
    result = rs.lastIndexOf("start");
    equal(result, -1);

    /*
     * rs = new RiText(null,
     * "Start at first character. Start at last character."); result =
     * rs.lastIndexOf("a", 12); equal(result, 6);
     * 
     * rs = new RiText(null,
     * "Start at first character. Start at last character."); result =
     * rs.lastIndexOf("at", 12); equal(result, 6);
     */

    rs = new RiText(null, "Start at first character. Start at last character.");
    result = rs.lastIndexOf("");
    equal(result, rs.length()); // should be 50 or -1? 50(DCH)
  }

  @Test
  public void testLength()
  {
    RiText rs = new RiText(null, "S");
    int result = rs.length();
    equal(result, 1);

    rs = new RiText(null, "s "); // space
    result = rs.length();
    equal(result, 2);

    rs = new RiText(null, "s" + '\t'); // tab space
    result = rs.length();
    equal(result, 2);

    rs = new RiText(null, " s "); // 2 space
    result = rs.length();
    equal(result, 3);

    rs = new RiText(null, '\t' + "s" + '\t'); // 2 tab space
    result = rs.length();
    equal(result, 3);

    rs = new RiText(null, "s b");
    result = rs.length();
    equal(result, 3);

    rs = new RiText(null, "s b.");
    result = rs.length();
    equal(result, 4);

    rs = new RiText(null, "s b ."); // space
    result = rs.length();
    equal(result, 5);

    rs = new RiText(null, "><><><#$!$@$@!$");
    result = rs.length();
    equal(result, 15);

    rs = new RiText(null, "");
    result = rs.length();
    equal(result, 0);

    // no error checks needed
  }

  @Test
  public void testPos()
  {
    // check that these are ok ---------------
    String txt = "The dog"; // tmp: move to RiTa.tests
    String[] words = RiTa.tokenize(txt);
    deepEqual(words, new String[] { "The", "dog" });

    words = RiTa.tokenize("closed"); // tmp: move to RiTa.tests
    deepEqual(words, new String[] { "closed" });

    RiText rs = new RiText(null, "asdfaasd");
    String[] result = rs.pos();
    deepEqual(result, new String[] { "nn" });

    rs = new RiText(null, "clothes");
    result = rs.pos();
    deepEqual(result, new String[] { "nns" });

//    rs = new RiText(null, "There is a cat.");//KnownIssues
//    result = rs.pos();
//    //System.out.println(RiTa.asList(result));
//    deepEqual(new String[] { "ex", "vbz", "dt", "nn", "."}, result);

    rs = new RiText(null, "The boy, dressed in red, ate an apple.");
    result = rs.pos();
    deepEqual(new String[] { "dt", "nn", ",", "vbn", "in", "jj", ",", "vbd", "dt", "nn", "." }, result);
  }

  @Test
  public void testPosAt()
  {
    // check that these are ok ---------------
    RiText rs = new RiText(null, "The emperor had no clothes on.");
    String result = rs.posAt(4);
    equal("nns", result);
 
    rs = new RiText(null, "There is a cat.");
    result = rs.posAt(3);
    equal("nn", result);
    
    
    //out of range test
    
    rs = new RiText(null, "There is a cat.");
    result = rs.posAt(-3);
    //System.out.println(result);
    equal("dt", result);
    
    
    rs = new RiText(null, "There is a cat.");
    result = rs.posAt(300);
    equal(".", result);
  }

  @Test
  public void testRemoveChar()
  {
    RiText rs = new RiText(null, "The dog was white");
    rs.removeChar(1);
    equal(rs.text(), "Te dog was white");

    rs = new RiText(null, "The dog was white");
    //System.out.println("testRemoveCharAt"+rs.length());
    rs.removeChar(rs.length() - 1);
    equal(rs.text(), "The dog was whit");

    rs = new RiText(null, "The dog was white");
    rs.removeChar(0);
    //System.out.println("testRemoveCharAt :"+rs.text());
    equal(rs.text(), "he dog was white");

    rs = new RiText(null, "The dog was white");
    rs.removeChar(-1);
    equal(rs.text(), "The dog was whit");

    rs = new RiText(null, "The dog was white.");
    rs.removeChar(rs.length() - 1);
    equal(rs.text(), "The dog was white");
    
    //out of range tests
    
    rs = new RiText(null, "The dog was white");
    rs.removeChar(-1);
    equal(rs.text(), "The dog was whit");

    rs = new RiText(null, "The dog was white");
    rs.removeChar(1000);
    equal(rs.text(), "The dog was whit");

    rs = new RiText(null, "The dog was white");
    rs.removeChar(rs.length());
  }
  
  
  @Test
  public void testReplaceChar()
  {
    RiText rs = new RiText(null, "Who are you?");
    rs.replaceChar(2, "");
    equal(rs.text(), "Wh are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceChar(2, "e");
    equal(rs.text(), "Whe are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceChar(2, "ere");
    equal(rs.text(), "Where are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceChar(11, "!!");
    equal(rs.text(), "Who are you!!");

    rs = new RiText(null, "Who are you?");
    rs.replaceChar(0, "me");
    equal(rs.text(), "meho are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceChar(-1, "me");
    equal(rs.text(), "Who are youme");

    rs = new RiText(null, "Who are you?");
    rs.replaceChar(rs.length(), "me");
    //System.out.println(rs);
    equal(rs.text(), "Who are youme");
    
    
    //out of range test
    rs = new RiText(null,"Who are you?"); 
   // System.out.println("replaceCharAt :" + rs.text());
    rs.replaceChar(-1, "me");
    equal(rs.text(), "Who are youme"); // TODO: shouldn't this go back from the end?
    
    rs = new RiText(null, "Who are you?"); 
    //System.out.println("replaceCharAt :" + rs.text());
    rs.replaceChar(10000, "me");
    equal(rs.text(), "Who are youme");
    


  }

  @Test
  public void testReplaceFirst()
  {
    RiText rs = new RiText(null, "Who are you?");
    rs.replaceFirst("e", "E");
    equal(rs.text(), "Who arE you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceFirst("o", "O");
    equal(rs.text(), "WhO are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceFirst("Who", "Where");
    equal(rs.text(), "Where are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceFirst("notExist", "Exist");
    equal(rs.text(), "Who are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceFirst("Who are", "Dare");
    equal(rs.text(), "Dare you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceFirst("Who aRe", "Dare");
    equal(rs.text(), "Who are you?");

    rs = new RiText(null, "Who are you? Who are you?");
    rs.replaceFirst("Who are", "Dare");
    equal(rs.text(), "Dare you? Who are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceFirst("\\?", "?!");
    equal(rs.text(), "Who are you?!");

    rs = new RiText(null, "Who are you?");
    rs.replaceFirst("", "");
    equal(rs.text(), "Who are you?");
  }

  @Test
  public void testReplaceAll()
  {
    // replaceAll(regex, replacement);
    RiText rs = new RiText(null, "Who are you? Who is he? Who is it?");
    equal(rs.replaceAll("e", "E").text(), "Who arE you? Who is hE? Who is it?");

    rs = new RiText(null, "Who are you? Who is he? Who is it?");
    equal(rs.replaceAll("Who", "O").text(), "O are you? O is he? O is it?");

    rs = new RiText(null, "Whom is he? Where is he? What is it?");
    equal(rs.replaceAll("Wh*", "O").text(), "Oom is he? Oere is he? Oat is it?");

    // rs = new RiText(null, "Who are you? Who is he? Who is it?");
    // equal(rs.replaceAll(rs, "Where is the text").text(),
    // "Where is the text");

    // rs = new RiText(null, "Who are you?");
    // equal(rs.replaceAll(rs, "Where are you!").text(), "Where are you!?");

    rs = new RiText(null, "%^&%&?");
    equal(rs.replaceAll("%^&%&?", "!!!").text(), "%^&%&?");

    rs = new RiText(null, "Who are you?");
    equal(rs.replaceAll("notExist", "Exist").text(), "Who are you?");

    rs = new RiText(null, "Who are you?");
    equal(rs.replaceAll("", "").text(), "Who are you?");

    rs = new RiText(null, "");
    equal(rs.replaceAll("", "").text(), "");

  }

  @Test
  public void testReplaceWord()
  {

    RiText rs = new RiText(null, "Who are you?");
    equal(rs.replaceWord(0, "What").text(), "What are you?");

    rs = new RiText(null, "Who are you?");
    rs.replaceWord(3, "!!"); //
    equal(rs.text(), "Who are you!!"); // nice! this is a strange one...
    
    rs = new RiText(null, "Who are you?");
    rs.replaceWord(-1, "!");
    //System.out.println("TEST:"+rs);
    equal(rs.text(), "Who are you!");
    
    rs = new RiText(null, "Who are you?");
    rs.replaceWord(2, "What");
    equal("Who are What?", rs.text());
    
    //out of range test
    
    rs = new RiText(null,"Who are you?"); 
    rs.replaceWord(-1, "asfasf"); //negative number
    //System.out.println(rs);
    equal(rs.text(), "Who are you asfasf");
    
    rs = new RiText(null,"Who are you?"); 
    rs.replaceWord(-2, "asfasf"); //negative number
    equal(rs.text(), "Who are asfasf?");
    
    rs = new RiText(null,"Who are you?"); 
    rs.replaceWord(20, "asfasf");
    equal(rs.text(), "Who are you asfasf");
    
  }

  @Test
  public void testSlice()
  {
    RiText rs = new RiText(null, "Hello world!");;
    String s = rs.slice(1,5);
    equal(s, "ello");
    
    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    s=rs.slice(1, 3);
    equal(s, "he");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    s=rs.slice(-3, -2);
    equal(s, "f");

    //out of range test
    
    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    s=rs.slice(10, 10);
    equal(s, "");
    
    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    s=rs.slice(15, 500);
    equal(s, "Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiText(null, "!@#$%^&**()");
    s=rs.slice(2, 5);
    equal(s, "#$%");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    s=rs.slice(15, 500);
    equal(s, "Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiText(null, "The Australian");
    s=rs.slice(-5, -3);
    equal(s, "al");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    s=rs.slice(500, 501);
    //System.out.println(s);
    equal(s, ".");
  }
  
  /*
  @Test
  public void testSplit()
  {
    RiText rs = new RiText(null, "Who are you?"); 
    RiText[] result = rs.split();

    RiText[] answer = { new RiText(null, "Who"), 
        new RiText(null, "are"), new RiText(null, "you?") };
   
    for (int i = 0; i < answer.length; i++)
    {
      equal(result[i].text(), answer[i].text());
    }
  }
  
  @Test
  public void testSplitString()
  {
    RiText rs = new RiText(null, "Who are you?");
    RiText[] result = rs.split("are");
    RiText[] answer = new RiText[] { new RiText(null, "Who "), new RiText(null, " you?") };
    for (int i = 0; i < answer.length; i++)
    {
      equal(result[i].text(), answer[i].text());
    }

    rs = new RiText(null, "Who are you?");
    result = rs.split("\\?");
    answer = new RiText[] { new RiText(null, "Who are you")  };
    for (int i = 0; i < answer.length; i++)
    {
      equal(result[i].text(), answer[i].text());
    }

    rs = new RiText(null, "Who are you?");
    result = rs.split("W");
    answer = new RiText[] { new RiText(null, "ho are you?") };
    //deepEqual(result, answer);
    
    for (int i = 0; i < answer.length; i++)
    {
      equal(result[i].text(), answer[i].text());
    }

    rs = new RiText(null, "Who are you?");
    result = rs.split("abc");
    answer = new RiText[] { new RiText(null, "Who are you?") };
    for (int i = 0; i < answer.length; i++)
    {
      equal(result[i].text(), answer[i].text());
    }

    rs = new RiText(null, "Who are you?");
    result = rs.split("");
    String[] chars = { "W", "h", "o", " ", "a", "r", "e", " ", "y", "o", "u", "?" };
    answer = new RiText[chars.length];
    for (int i = 0; i < chars.length; i++)
      answer[i] = new RiText(null, chars[i]);
    for (int i = 0; i < answer.length; i++)
      equal(result[i].text(), answer[i].text());
  }*/

  @Test
  public void testStartsWith()
  {
    RiText rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("T"));

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("The"));

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(!rs.startsWith("Aus"));

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(!rs.startsWith("*"));

    rs = new RiText(null, "*The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("*"));

    rs = new RiText(null, " The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith(" ")); // single space
    rs = new RiText(null, "  The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("  ")); // double space
    rs = new RiText(null, "   The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("  ")); // tab space
    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("")); // fail here should not be true -- DCH: this is ok --
                           // everything matches ""
  }

  @Test
  public void testSubstring()
  {
    RiText rs = new RiText(null, "The Australian Pavilion.");
    String result = rs.substring(1, 3);
    equal(result, "he");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substring(15, 500);
    equal(result, "Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substring(15);
    equal(result, "Pavilion at the Venice Biennale is getting a much-needed facelift.");
    
    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substring(10, 10);
    equal(result, "");

    rs = new RiText(null, "!@#$%^&**()");
    result = rs.substring(2, 5);
    equal(result, "#$%");

    rs = new RiText(null, "Start at first character.");
    result = rs.substring(1, 5);
    equal(result, "tart");

    rs = new RiText(null, "Start at first character.");
    result = rs.substring(0, 1);
    equal(result, "S");

    rs = new RiText(null, "Start at first character.");
    result = rs.substring(0, 1);
    equal(result, "S");

//out of range test
    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substring(-2, 3);
    //System.out.println(result);
    equal(rs.substring(-2, 3), " Australian Pavilion at the Venice Biennale is getting a much-needed facelif");
    
    rs = new RiText(null, "The Australian Pavilion.");
    result = rs.substring(3, 1);
    equal(result, "he");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substring(500);
    


    /*
     * rs = new RiText(null, "Start at first character."); rs.substring(3);
     * equal(rs.text(), "rt at first character.");
     */
   
  }

  @Test
  public void testSubstr()
  {
    RiText rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    String result = rs.substr(1, 3);
    equal(result, "he ");
    
    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substr(-2, 3);
    equal(result, "t.");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substr(15, 500);
    equal(result, "Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substr(500, 501);
    //System.out.println("RE:"+result);
    equal(result, ".");

    rs = new RiText(null, "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    result = rs.substr(10, 10);
    equal(result, "lian Pavil");

    rs = new RiText(null, "!@#$%^&**()");
    result = rs.substr(2, 5);
    equal(result, "#$%^&");
  }

  /*@Test
  public void testToCharArray()
  {
    RiText rs = new RiText(null, "The Australian Pavilion.");
    char[] result = rs.toCharArray();
    char[] answer = { 'T', 'h', 'e', ' ', 'A', 'u', 's', 't', 'r', 'a', 'l', 'i', 'a',
        'n', ' ', 'P', 'a', 'v', 'i', 'l', 'i', 'o', 'n', '.' };
    for (int i = 0; i < result.length; i++)
    {
      ok(result[i]==answer[i]);
    }
  }*/

  @Test
  public void testToLowerCase() 
  {
    RiText rs = new RiText(null, "The Australian Pavilion.");
    rs.toLowerCase();
    equal("the australian pavilion.", rs.text());

    rs = new RiText(null, "the Australian pavilion.");
    rs.toLowerCase();
    equal("the australian pavilion.", rs.text());

    rs = new RiText(null, ")(*(&^%%$!#$$%%^))");
    rs.toLowerCase();
    equal(")(*(&^%%$!#$$%%^))", rs.text());

    // no error checks needed
  }

  @Test
  public void testToUpperCase()
  {
    RiText rs = new RiText(null, "The Australian Pavilion.");
    equal("THE AUSTRALIAN PAVILION.", rs.toUpperCase().text());

    rs = new RiText(null, ")(*(&^%%$!#$$%%^))");
    rs.toUpperCase();
    equal(")(*(&^%%$!#$$%%^))", rs.text());

    // no error checks needed
  }

  @Test
  public void testEqualsString()
  {
    RiText rs = new RiText(null, "Start at first character. ");
    RiText rs2 = new RiText(null, "Start at first character. ");
    ok(rs.equals(rs2.text()));
    
    String s = "Start at first character. ";
    ok(rs.equals(s));  
    
    rs = new RiText(null, "");
    rs2 = new RiText(null, "");
    ok(rs.equals(""));
    ok(!rs.equals(null));
  }

  @Test
  public void testTrim()
  {
    // check that these are ok ---------------
    RiText rs = new RiText(null, "Start at first character. ");
    equal(rs.trim().text(), "Start at first character.");

    rs = new RiText(null, " Start at first character.");
    equal(rs.trim().text(), "Start at first character.");

    rs = new RiText(null, "     Start at first character.   "); // tabs
    equal(rs.trim().text(), "Start at first character.");

    rs = new RiText(null, "     Start at first character.    "); // spaces/tabs
    equal(rs.trim().text(), "Start at first character.");
    
    rs = new RiText(null, "Start at first character.\t"); //\t
    equal(rs.trim().text(), "Start at first character.");
    
    rs = new RiText(null, "\t\t\tStart at first character.\t"); //\t
    equal(rs.trim().text(), "Start at first character.");

    // no error checks needed
  }

  @Test
  public void testWordAt()
  {
    // check that these are ok ---------------
    RiText rs = new RiText(null, "Returns the word at wordIdx using the default WordTokenizer.");
    String result = rs.wordAt(0);
    equal(result, "Returns");

    result = rs.wordAt(1);
    equal(result, "the");

    result = rs.wordAt(9);
    equal(result, ".");

    result = rs.wordAt(500);
    equal(result, ".");

    result = rs.wordAt(-5);
    equal(result, "using");

    rs = new RiText(null, "");
    result = rs.wordAt(0);
    equal(result, "");
  }

  @Test
  public void testWordCount()
  {
    // check that these are ok --- ------------
    RiText rs = new RiText(null, "Returns the word at wordIdx using the default WordTokenizer.");
    int result = rs.wordCount();
    equal(result, 10); // correct, according to WordTokenizer, need to try with
                       // RegexTokenizer
    rs = new RiText(null, "Returns the word.Returns the word. Returns the word .");
    result = rs.wordCount();
    equal(result, 12);

    rs = new RiText(null, "   Returns the word at wordIdx , using the default WordTokenizer."); // space
    result = rs.wordCount();
    equal(result, 11);

    rs = new RiText(null, "   Returns the word at wordIdx , using the default WordTokenizer.  "); // tab
                                                                                                  // space
    result = rs.wordCount();
    equal(result, 11);

    rs = new RiText(null, "");
    result = rs.wordCount();
    equal(result, 0);

  }

  @Test
  public void testWords()
  {
    // check that these are ok ---------------
    RiText rs = new RiText(null, "Returns the array of words.");
    String[] result = rs.words();
    String[] answer = { "Returns", "the", "array", "of", "words", "." };
    deepEqual(result, answer);

    rs = new RiText(null, "The boy, dressed in red, ate an array.");
    result = rs.words();
    answer = new String[] { "The", "boy", ",", "dressed", "in", "red", ",", "ate", "an",
        "array", "." };
    deepEqual(result, answer);

    rs = new RiText(null, "Returns the array of words .");
    result = rs.words();
    answer = new String[] { "Returns", "the", "array", "of", "words", "." };
    deepEqual(result, answer);

    rs = new RiText(null, "The boy, dressed in red , ate an array?");
    result = rs.words();
    answer = new String[] { "The", "boy", ",", "dressed", "in", "red", ",", "ate", "an",
        "array", "?" };
    deepEqual(result, answer);

    rs = new RiText(null, "");
    result = rs.words();
    answer = new String[] {};
    deepEqual(result, answer);

    // no error checks needed
  }

  @Test
  public void testMatch()
  {
    RiText rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    String[] result = rs.match("ain");
    deepEqual(result, new String[] { "ain", "ain", "ain" });

    rs = new RiText(null, "Watch out for the rock!");
    result = rs.match("r?or?");
    deepEqual(result, new String[] { "o", "or", "ro" });

    rs = new RiText(null, "abc!");
    result = rs.match("r?or?");
    deepEqual(result, new String[] {}); 
    
    rs = new RiText(null, "Letter !>D? hello 213331123");
    result = rs.match("[A-Za-z]");
  //  for(int i =0;i<result.length;i++){
   // 	System.out.println(result[i]);
   // }
    deepEqual(result, new String[] { "L", "e", "t","t", "e", "r" ,"D","h","e","l","l","o"});
    
    rs = new RiText(null, "Letter !>D? hello 213331123");
    result = rs.match("\\W");
  //  for(int i =0;i<result.length;i++){
  //  	System.out.println(result[i]);
  //  }
    deepEqual(result, new String[] { " ", "!", ">","?"," "," "});
    
    rs = new RiText(null, "Letter !>D? hello 213331123");
    result = rs.match("[^0-9]");
 //   for(int i =0;i<result.length;i++){
 //   	System.out.println(result[i]);
 //   }
    deepEqual(result, new String[] { "L", "e", "t","t", "e", "r"," ","!",">","D","?" ," ","h","e","l","l","o"," "});

    rs = new RiText(null, "!@#$%^&*()__+");
    result = rs.match("X|Z");
 //   for(int i =0;i<result.length;i++){
 //   	System.out.println(result[i]);
 //   }
    deepEqual(result, new String[] { });

    rs = new RiText(null, "!@#$%^&*()__+");
    result = rs.match("!|Z");
 //   for(int i =0;i<result.length;i++){
 //   	System.out.println(result[i]);
 //   }
    deepEqual(result, new String[] { "!"});

    
    //case-insensitive tests
    rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain", Pattern.CASE_INSENSITIVE);
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
    
    rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain");
    deepEqual(result, new String[] { "ain", "ain", "ain" });
  //TODO not sure how to test these
   /* rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("i", Pattern.CANON_EQ); 
       for(int i =0;i<result.length;i++){
       	System.out.println(result[i]);
       }
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
    
    rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain", Pattern.COMMENTS);
    for(int i =0;i<result.length;i++){
       	System.out.println(result[i]);
       }
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
    
    rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain", Pattern.DOTALL);
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
    
    rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain", Pattern.LITERAL);
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
    
    rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain", Pattern.MULTILINE);
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
    
    rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain", Pattern.UNICODE_CASE);
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
    
    rs = new RiText(null, "The rain in SPAIN stays mainly in the plain");
    result = rs.match("ain", Pattern.UNIX_LINES);
    deepEqual(result, new String[] { "ain", "AIN", "ain", "ain" });
    
   */  


  }

  @Test
  public void testConcatRiText()
  {
    RiText rs = new RiText(null, "The dog was white");
    RiString rs2 = new RiString("The dog was not white");
    RiText result = rs.concat(rs2);
    //System.out.println(result);
    equal(result.text(), "The dog was whiteThe dog was not white");

    rs = new RiText(null, " The dog was white ");
    rs2 = new RiString("The dog was not white ");
    result = rs.concat(rs2);
    equal(result.text(), " The dog was white The dog was not white ");

    rs = new RiText(null, "#$#@#$@#");
    rs2 = new RiString("The dog was not white ");
    result = rs.concat(rs2);
    equal(result.text(), "#$#@#$@#The dog was not white ");

  }
 
  @Test 
  public void testCopy() { 
    
    RiText rt = new RiText("hello", 100, 270);

    rt.fill(101, 102, 103, 104);
    rt.showBounds(true);

    RiText rt2 = rt.copy();
    equal(rt.x, rt2.x);
    equal(rt.y, rt2.y);
    equal(rt.z, rt2.z);
    equal(rt.alpha(), rt2.alpha());
    equal(rt.text(), rt2.text());
    equal(rt.align(), rt2.align());
    
    //equal(rt.autodraw(), rt2.autodraw()); not in API
    equal(rt.behaviors, rt2.behaviors);
    equal(rt.charAt(3), rt2.charAt(3));

    deepEqual(rt.boundingBox(),rt2.boundingBox());

    deepEqual(rt.fill(), rt2.fill());
    
    
    rt = new RiText(" space tabSpace	AbCd 1234 ", 5, 10);

    rt.fill(101, 102, 103, 104);
    rt.showBounds(true);

    rt2 = rt.copy();
    equal(rt.x, rt2.x);
    equal(rt.y, rt2.y);
    equal(rt.z, rt2.z);
    equal(rt.alpha(), rt2.alpha());
    equal(rt.text(), rt2.text());
    equal(rt.align(), rt2.align());
    
    //equal(rt.autodraw(), rt2.autodraw());
    equal(rt.behaviors, rt2.behaviors);
    equal(rt.charAt(3), rt2.charAt(3));

    deepEqual(rt.boundingBox(),rt2.boundingBox());

    deepEqual(rt.fill(), rt2.fill());
    
    
    
    
    RiText rt3 = new RiText("!@#$%^&*()_ GHFHJJJ hjhjjh", 15, 20);
    
    rt3.fill(1, 100, 23);
    rt3.showBounds(false);

    RiText rt4 = rt3.copy();
    equal(rt4.x, rt3.x);
    equal(rt4.y, rt3.y);
    equal(rt4.z, rt3.z);
    equal(rt4.alpha(), rt3.alpha());
    equal(rt4.text(), rt3.text());
    equal(rt4.align(), rt3.align());

    //equal(rt4.autodraw(), rt3.autodraw());
    equal(rt4.behaviors, rt3.behaviors);
    equal(rt4.charAt(3), rt3.charAt(3));

    deepEqual(rt4.boundingBox(),rt3.boundingBox());

    deepEqual(rt4.fill(), rt3.fill());
    
  }

}
