package rita.test;

import static org.junit.Assert.fail;
import static rita.support.QUnitStubs.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import rita.*;

public class RiTaTest
{  
  public static boolean REMOTE_TESTING = false;
  
  static {
    RiTa.SILENT_LTS = true;
    
    // only if set as in the env
    String doRemotes = System.getenv("RITA_DO_REMOTE") ;
    if ((doRemotes != null && doRemotes.equals("true"))) 
      REMOTE_TESTING = true;

    // but never on TravisCI
    String isCI = System.getenv("CI") ;
    if ((isCI != null && isCI.equals("true"))) 
      REMOTE_TESTING = false;
    
    if (!REMOTE_TESTING)
      System.out.println("[INFO] Skipping remote URL tests...");
  }
  
  @Before
  public void initialize() {
    RiTa.SILENT = false;
    RiLexicon.enabled = true;
  }
 
  @Test
  public void testStart()
  {
    //RiTa.start(null);
    //RiTa.start(this);
    //RiTa.start(new PApplet());
  }

  @Test
  public void loadString_RelFile()
  {
    String s = RiTa.loadString("kafka.txt");
    ok(s.indexOf("\n") > 0);
    ok(s != null && s.length() > 100000);
  }
  
  @Test
  public void loadString_RelFileLBC()
  {
    String s = RiTa.loadString("kafka.txt", " ");
    ok(s.indexOf("\n") == -1);
    ok(s != null && s.length() > 100000);
  }
  
  @Test
  public void loadString_AbsFile()
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
    String s = RiTa.loadString("/Library/WebServer/Documents/testfiles/kafka.txt");
    ok(s != null && s.length() > 100000);
  }
  
  @Test
  public void loadString_Url()
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
    String s = RiTa.loadString("http://localhost/testfiles/kafka.txt");
    ok(s != null && s.length() > 100000);
  }
  
  @Test
  public void loadString_FileAsUrl()
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
    URL url = null;
    try
    {
      url = new URL("file:///Library/WebServer/Documents/testfiles/kafka.txt");
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    String s = RiTa.loadString(url);
    ok(s != null && s.length() > 100000);
  }
  
  @Test
  public void loadString_RelFileMulti()
  {
    String[] files = { "sentence1.json", "sentence2.json" };
    String s = RiTa.loadString(files, null);
    ok(s != null && s.length() > 500);
  }
  
  @Test
  public void loadString_AbsFileMulti()
  {
    String[] files = { "sentence1.json", "sentence2.json" };
    String s = RiTa.loadString(files, null);
    ok(s != null && s.length() > 500);
  }
  
  @Test
  public void loadString_UrlStrMulti()
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
    String[] files = {  
        "http://localhost/testfiles/sentence1.json",
        "http://localhost/testfiles/sentence2.json" 
    };
    String s = RiTa.loadString(files, null);
    ok(s != null && s.length() > 500);
  }
  
  @Test
  public void loadString_UrlMulti()
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
    URL[] urls = null;
    
    try {
      urls = new URL[] {  
        new URL("http://localhost/testfiles/sentence1.json"),
        new URL("http://localhost/testfiles/sentence2.json") 
    };} catch (MalformedURLException e){e.printStackTrace();}
    
    String s = RiTa.loadString(urls);
    ok(s != null && s.length() > 500);
  }
  
  @Test
  public void loadUrl()
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
    URL url = null;
    try
    {
      url = new URL("http://localhost/testfiles/kafka.txt");
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    String s = RiTa.loadString(url);
    ok(s != null && s.length() > 100000);
  }
  
  // TODO: testUnescapeHTML
  
  @Test
  public void testIsAbbreviation()
  {

    ok(RiTa.isAbbreviation("Dr."));
    ok(RiTa.isAbbreviation("dr.")); // T in processing

    ok(!RiTa.isAbbreviation("DR.")); // F in Processing.lowercase is true
    // but uppercase is false
    ok(!RiTa.isAbbreviation("Dr. ")); // space
    ok(!RiTa.isAbbreviation(" Dr.")); // space
    ok(!RiTa.isAbbreviation("  Dr.")); // double space
    ok(!RiTa.isAbbreviation("Dr.  ")); // double space
    ok(!RiTa.isAbbreviation("   Dr.")); // tab space
    ok(!RiTa.isAbbreviation("Dr.    ")); // tab space
    ok(!RiTa.isAbbreviation("Dr"));
    ok(!RiTa.isAbbreviation("Doctor"));
    ok(!RiTa.isAbbreviation("Doctor."));

    ok(RiTa.isAbbreviation("Prof."));
    ok(RiTa.isAbbreviation("prof.")); // T in processing
    ok(!RiTa.isAbbreviation("PRFO.")); // F in Processing. lowercase is true
    // but uppercase is false
    ok(!RiTa.isAbbreviation("PrFo.")); // F in Processing. lowercase is true
    // but uppercase is false
    ok(!RiTa.isAbbreviation("Professor"));
    ok(!RiTa.isAbbreviation("professor"));
    ok(!RiTa.isAbbreviation("PROFESSOR"));
    ok(!RiTa.isAbbreviation("Professor."));

    ok(!RiTa.isAbbreviation("@#$%^&*()"));

    ok(!RiTa.isAbbreviation(""));
    ok(!RiTa.isAbbreviation(null));
    // ok(!RiTa.isAbbreviation(undefined));
    // ok(!RiTa.isAbbreviation(1));
  }

  @Test
  public void testIsQuestion()
  {
    ok(RiTa.isQuestion("WHAT"));
    ok(RiTa.isQuestion("What"));
    ok(RiTa.isQuestion("what"));
    ok(RiTa.isQuestion("what?"));
    ok(RiTa.isQuestion("what is this"));
    ok(RiTa.isQuestion("what is this?"));
    ok(RiTa.isQuestion("Does it?"));
    ok(RiTa.isQuestion("Is this yours?"));

    // "How", "If", "Who", "Is", "Could", "Might", "Does", "Are", "Have"
    
    ok(RiTa.isQuestion("Are you done?")); // if "is" is true, "Are" should
    // also be True (NICE!)

    ok(RiTa.isQuestion("what is  this?")); // extra space
    ok(RiTa.isQuestion(" what is this? ")); // extra space
    ok(RiTa.isQuestion("what is   this?")); // extra double space
    ok(RiTa.isQuestion("what    is  this?")); // extra tab
    ok(RiTa.isQuestion("what is this? , where is that?"));
    ok(RiTa.isQuestion("Have you a smoke?"));
    ok(RiTa.isQuestion("How is it going?"));
    
    ok(!RiTa.isQuestion("That is not a toy This is an apple"));
    ok(!RiTa.isQuestion("string"));
    ok(!RiTa.isQuestion("?"));
    ok(!RiTa.isQuestion(""));
  }

  @Test
  public void testIsSentenceEnd()
  {
    String word = "The dog ate the small baby. Then it threw up.";
    String[] words = word.split(" ");
    ok(RiTa.isSentenceEnd(words[5], words[6])); // true
    ok(!RiTa.isSentenceEnd(words[3], words[4])); // false
    ok(!RiTa.isSentenceEnd(words[6], words[7])); // false
    // ok(!RiTa.isSentenceEnd('','')); // false
  }

  @Test
  public void testIsW_Question()
  {

    ok(RiTa.isW_Question("What the"));
    ok(RiTa.isW_Question("What is it"));
    ok(RiTa.isW_Question("will is it."));
    ok(RiTa.isW_Question("Where is it?"));
    ok(RiTa.isW_Question("When is it?"));
    ok(RiTa.isW_Question("Why is it?"));
    
    ok(!RiTa.isW_Question("how is it?"));
    ok(!RiTa.isW_Question("How is it."));
    ok(!RiTa.isW_Question("Does it?"));
    ok(!RiTa.isW_Question("Is this yours?"));
    ok(!RiTa.isW_Question("Are you done?"));
    ok(!RiTa.isW_Question(""));
  }

  @Test
  public void testIsPunctuation()
  {

    // TODO do it need to add those invalid character ?
    ok(!RiTa.isPunctuation("What the"));
    ok(!RiTa.isPunctuation("What ! the"));
    ok(!RiTa.isPunctuation(".#\"\\!@i$%&}<>"));

    ok(RiTa.isPunctuation("!"));

    ok(!RiTa.isPunctuation("! ")); // space
    ok(!RiTa.isPunctuation(" !")); // space
    ok(!RiTa.isPunctuation("!  ")); // double space
    ok(!RiTa.isPunctuation("  !")); // double space
    ok(!RiTa.isPunctuation("!  ")); // tab space
    ok(!RiTa.isPunctuation("   !")); // tab space
    ok(RiTa.isPunctuation("?"));
    ok(RiTa.isPunctuation("?!"));
    ok(RiTa.isPunctuation("."));
    ok(RiTa.isPunctuation(".."));
    ok(RiTa.isPunctuation("..."));
    ok(RiTa.isPunctuation("...."));
    ok(RiTa.isPunctuation("%..."));
    ok(RiTa.isPunctuation("$"));
    ok(RiTa.isPunctuation("%"));
    ok(RiTa.isPunctuation("&"));
    ok(RiTa.isPunctuation("^"));
    ok(RiTa.isPunctuation(","));
    ok(RiTa.isPunctuation("$%&^,"));
    ok(RiTa.isPunctuation("@"));

    String pun = ",;:!?)([].#\"\\!@$%&}<>|+=-_\\/*{^";
    String[] punct = pun.split("");
    for (int i = 1; i < punct.length; i++)
    { 
      // skipped the first one as it is a space
      ok(RiTa.isPunctuation(punct[i]));
    }

    // TODO: also test multiple characters strings here ****
    pun = "\"`'";
    punct = pun.split("");
    for (int i = 1; i < punct.length; i++)
    {
      ok(RiTa.isPunctuation(punct[i]));
    }

    pun = "\"`',;:!?)([].#\"\\!@$%&}<>|+=-_\\/*{^";
    punct = pun.split("");
    for (int i = 1; i < punct.length; i++)
    {
      ok(RiTa.isPunctuation(punct[i]));
    }

    // TODO: and here...
    String nopun = "Helloasdfnals  FgG   \t kjdhfakjsdhf askjdfh aaf98762348576";
    String[] nopunct = nopun.split("");
    for (int i = 1; i < nopunct.length; i++)
    {
      ok(!RiTa.isPunctuation(nopunct[i]));
    }

    ok(!RiTa.isPunctuation(""));

  }

  @Test
  public void testRandomOrdering()
  {
    int[] result = RiTa.randomOrdering(5);
    equal(result.length, 5);

    result = new int[] {};
    result = RiTa.randomOrdering(50);
    equal(result.length, 50);
  }


  @Test
  public void testSplitSentences()
  {
    // TODO: check Penn-Treebank splitting rules
    
    String input = "Stealth's Open Frame, OEM style LCD monitors are designed for special mounting applications. The slim profile packaging provides an excellent solution for building into kiosks, consoles, machines and control panels. If you cannot find an off the shelf solution call us today about designing a custom solution to fit your exact needs.";
    String[] expected = {
        "Stealth's Open Frame, OEM style LCD monitors are designed for special mounting applications.",
        "The slim profile packaging provides an excellent solution for building into kiosks, consoles, machines and control panels.",
        "If you cannot find an off the shelf solution call us today about designing a custom solution to fit your exact needs." 
    };
    String[] output = RiTa.splitSentences(input);
    //for (int i = 0; i < output.length; i++)
      //System.out.println("testSplitSentences :" + output[i] + "'");
    deepEqual(output, expected);

    input = "\"The boy went fishing.\", he said. Then he went away.";
    expected = new String[] { "\"The boy went fishing.\", he said.", "Then he went away." };
    output = RiTa.splitSentences(input);
    deepEqual(output, expected);

    input = "The dog";
    output = RiTa.splitSentences(input);
    expected = new String[] { input };
    deepEqual(output, expected);

    input = "I guess the dog ate the baby.";
    output = RiTa.splitSentences(input);
    expected = new String[] { input };
    deepEqual(output, expected);

    input = "Oh my god, the dog ate the baby!";
    output = RiTa.splitSentences(input);
    expected = new String[] { input };
    deepEqual(output, expected);

    input = "Which dog ate the baby?";
    output = RiTa.splitSentences(input);
    expected = new String[] { input };
    deepEqual(output, expected);

    input = "Yes, it was a dog that ate the baby!?. I am sad.";
    output = RiTa.splitSentences(input);
    expected = new String[] { "Yes, it was a dog that ate the baby!?.", "I am sad." };

    input = "Yes, it was a dog that ate the baby!. I am sad.";
    output = RiTa.splitSentences(input);
    expected = new String[] { "Yes, it was a dog that ate the baby!.", "I am sad." };

    input = "Yes, it was a dog that ate the baby!.? I am sad.";
    output = RiTa.splitSentences(input);
    expected = new String[] { "Yes, it was a dog that ate the baby!.?", "I am sad." };
    deepEqual(output, expected);

    input = "'Yes, it was a dog that ate the baby', he said.";
    output = RiTa.splitSentences(input);
    expected = new String[] { "\'Yes, it was a dog that ate the baby\', he said." };
    deepEqual(output, expected);

    // (for XML-marked text) next char is < 
    input = "<para>Here is an example of how to include some text that contains many <literal><</literal> and <literal>&</literal> symbols. The sample text is a fragment of <acronym>XHTML</acronym>. The surrounding text (<para> and <programlisting>) are from DocBook.</para>";
    output = RiTa.splitSentences(input);
    expected = new String[] { 
	"<para>Here is an example of how to include some text that contains many <literal><</literal> and <literal>&</literal> symbols.", 
	"The sample text is a fragment of <acronym>XHTML</acronym>.", 
	"The surrounding text (<para> and <programlisting>) are from DocBook.</para>"};
    deepEqual(output, expected);

    //  Q: or A: at start of sentence 
    input = "Q: Do I need a visa to visit Hong Kong? A: Visitors from most countries can enter Hong Kong without a visa for periods of seven to 180 days, depending on nationality.";
    output = RiTa.splitSentences(input);
    expected = new String[] { 
	"Q: Do I need a visa to visit Hong Kong?", 
	"A: Visitors from most countries can enter Hong Kong without a visa for periods of seven to 180 days, depending on nationality."};
    deepEqual(output, expected);

    // double initial (X.Y.) -> middle of sentence
    input = "Joanne \"Jo\" Rowling, OBE FRSL[2] (born 31 July 1965),[1] pen names J. K. Rowling and Robert Galbraith, is a British novelist best known as the author of the Harry Potter fantasy series.";
    output = RiTa.splitSentences(input);
    expected = new String[] { 
	"Joanne \"Jo\" Rowling, OBE FRSL[2] (born 31 July 1965),[1] pen names J. K. Rowling and Robert Galbraith, is a British novelist best known as the author of the Harry Potter fantasy series."};
    deepEqual(output, expected);

    // last char not "." -> middle of sentence
    input = "Completing Project.D is of the utmost importance!";
    output = RiTa.splitSentences(input);
    expected = new String[] { 
	"Completing Project.D is of the utmost importance!"};
    deepEqual(output, expected);
    
    // last char not "." -> middle of sentence
    input = "Will there be other factors such as temperature, humidity etc. affect the result?";
    output = RiTa.splitSentences(input);
    expected = new String[] { 
	"Will there be other factors such as temperature, humidity etc. affect the result?"};
    deepEqual(output, expected);
    
    // single upper-case alpha + "." -> middle of sentence
    input = "Chopper and Monkey D. Luffy were friends. Now they are not.";
    output = RiTa.splitSentences(input);
    expected = new String[] { 
	"Chopper and Monkey D. Luffy were friends.",
	"Now they are not."};
    deepEqual(output, expected);
    
    input = "-@.576";
    output = RiTa.splitSentences(input);
    expected = new String[] { "-@.576" };
    deepEqual(output, expected);

    input = "";
    output = RiTa.splitSentences(input);
    expected = new String[] {  };
    deepEqual(output, expected);

    // deepEqual(RiTa.splitSentences(""), [""]);

  }

  @Test
  public void testStripPunctuation()
  {
    // TODO: Some punctuations are not here
    String res = RiTa.stripPunctuation("$%He%^&ll,o,");
    equal(res, "Hello");

    equal(RiTa.stripPunctuation(""), "");

    equal(RiTa.stripPunctuation("Hel_lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel;lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel:lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel'lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel/lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel\"lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel-lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel`lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel?lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel.lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel+lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel*lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel&lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel$lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel(lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel)lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel@lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel[lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel]lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel{lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel}lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel\\lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel%lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel:lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel;lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel<lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel>lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel^lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel|lo"), "Hello");
    equal(RiTa.stripPunctuation("Hel~lo"), "Hello");
    
    res = RiTa.stripPunctuation("'!@$%&}<>|+=-_\\\\/*{^He&^ll,o!@$%&}<>|+=-_/*{^"); // removed
    equal(res, "Hello");
  }

  @Test
  public void testTrimPunctuation()
  {
    String res = RiTa.trimPunctuation("$%He&^ll,o,");
    equal(res, "He&^ll,o");
    res = RiTa.trimPunctuation("`He&^ll,o!@$%&}<>|+=-_/*{^"); 
    equal(res, "He&^ll,o");
    res = RiTa.trimPunctuation("!@$%&}<>|+=-_/*{^He&^ll,o!@$%&}<>|+=-_/*{^"); 
    equal(res, "He&^ll,o");
    deepEqual(RiTa.trimPunctuation(""), "");
  }

  @Test
  public void testTokenizeAndBack()
  {
      String[] testStrings = {
          "A simple sentence.",
          "(that's why this is our place).",
          "The boy, dressed in red, ate an apple.",
          "Dr. Chan is talking slowly with Mr. Cheng, and they're friends.",
          "The boy screamed, 'Where is my apple?'",
          "The boy screamed, \"Where is my apple?\"",
          "He can't didn't couldn't shouldn't wouldn't eat.",
          "Shouldn't he eat?",
          "It's not that I can't.",
          "We've found the cat.",
          "We didn't find the cat."
      };
      
      for (int i = 0; i < testStrings.length; i++) {
        String[] tokens= RiTa.tokenize(testStrings[i]);
        String output = RiTa.untokenize(tokens);
        equal(testStrings[i], output);
      } 
  }

  @Test
  public void testTokenize()
  {
    String input = "The boy, dressed in red, ate an apple.";
    String[] expected = { "The", "boy", ",", "dressed", "in", "red", ",", "ate", "an",
        "apple", "." };
    String[] output = RiTa.tokenize(input);
    deepEqual(output, expected);

    input = "The boy screamed, 'Where is my apple?'";
    output = new String[] {};
    expected = new String[] { "The", "boy", "screamed", ",", "'Where", "is", "my","apple", "?", "'" };
    output = RiTa.tokenize(input);
    deepEqual(output, expected);

    input = "why? Me?huh?!";
    output = new String[] {};
    expected = new String[] { "why", "?", "Me", "?", "huh", "?", "!" };
    output = RiTa.tokenize(input);
    deepEqual(output, expected);

    input = "123 123 1 2 3 1,1 1.1 23.45.67 22/05/2012 12th May,2012";
    output = new String[] {};
    expected = new String[] { "123", "123", "1", "2", "3", "1", ",", "1", "1", ".", "1",
        "23", ".", "45", ".", "67", "22/05/2012", "12th", "May", ",", "2012" };
    output = RiTa.tokenize(input);
    deepEqual(output, expected);

    // TODO: check Penn-Treebank tokenizer rules & add some more edge cases
    
    String[] inputs = { "A simple sentence.", "that's why this is our place)." };
    Object[] outputs = { new String[] { "A", "simple", "sentence", "." },
        new String[] { "that's", "why", "this", "is", "our", "place", ")", "." } }; 

    ok(inputs.length == outputs.length);

    for (int i = 0; i < inputs.length; i++)
    {
      String[] result = RiTa.tokenize(inputs[i]);
      deepEqual(result, outputs[i]);
    }

    deepEqual(RiTa.tokenize(""), new String[] { });

    output = new String[] {};
    input = "Dr. Chan is talking slowly with Mr. Cheng, and they're friends."; 
    expected = new String[] { "Dr", ".", "Chan", "is", "talking", "slowly", "with", "Mr",
        ".", "Cheng", ",", "and", "they're", "friends", "." };
    output = RiTa.tokenize(input);
    //System.out.println(RiTa.asList(expected));
    //System.out.println(RiTa.asList(output));
    deepEqual(output, expected);
    
    // contractions -------------------------

    String txt1 = "Dr. Chan is talking slowly with Mr. Cheng, and they're friends."; // strange but same as RiTa-java
    String txt2 = "He can't didn't couldn't shouldn't wouldn't eat.";
    String txt3 = "Shouldn't he eat?";
    String txt4 = "It's not that I can't.";
    String txt5 = "We've found the cat.";
    String txt6 = "We didn't find the cat.";

    RiTa.SPLIT_CONTRACTIONS = false;
    deepEqual(RiTa.tokenize(txt1), new String[] { "Dr", ".", "Chan", "is", "talking", "slowly", "with", "Mr", ".", "Cheng", ",", "and", "they're", "friends", "."});
    deepEqual(RiTa.tokenize(txt2), new String[] { "He", "can't", "didn't", "couldn't", "shouldn't", "wouldn't", "eat", "."});
    deepEqual(RiTa.tokenize(txt3), new String[] { "Shouldn't", "he", "eat", "?"});
    deepEqual(RiTa.tokenize(txt4), new String[] { "It's", "not", "that", "I", "can't", "."});
    deepEqual(RiTa.tokenize(txt5), new String[] { "We've", "found", "the", "cat", "."});
    deepEqual(RiTa.tokenize(txt6), new String[] { "We", "didn't", "find", "the", "cat", "."});

    RiTa.SPLIT_CONTRACTIONS = true;
    deepEqual(RiTa.tokenize(txt1), new String[] { "Dr", ".", "Chan", "is", "talking", "slowly", "with", "Mr", ".", "Cheng", ",", "and", "they", "are", "friends", "."});
    deepEqual(RiTa.tokenize(txt2), new String[] { "He", "can", "not", "did", "not", "could", "not", "should", "not", "would", "not", "eat", "."});
    deepEqual(RiTa.tokenize(txt3), new String[] { "Should","not", "he", "eat", "?"});
    deepEqual(RiTa.tokenize(txt4), new String[] { "It", "is", "not", "that", "I", "can", "not", "."});
    deepEqual(RiTa.tokenize(txt5), new String[] { "We","have", "found", "the", "cat", "."});
    deepEqual(RiTa.tokenize(txt6), new String[] { "We", "did", "not", "find", "the", "cat", "."});
    
    RiTa.SPLIT_CONTRACTIONS = false;
  }

  @Test
  public void testUntokenize()
  {
    String input[], output, expected;
    
    equal(RiTa.untokenize(new String[0]), "");
    
    input = new String[] { "She", "screamed", ":", "\"", "Oh", "God", "!", "\""};
    expected = "She screamed: \"Oh God!\"";
    output = RiTa.untokenize(input);
    //System.out.println(expected);
    //System.out.println(output);
    deepEqual(output, expected);
    
    expected = "The boy, dressed in red -- ate an apple.";
    input = new String[] { "The", "boy", ",", "dressed", "in", "red", "--", "ate", "an", "apple", "." };
    output = RiTa.untokenize(input);
    deepEqual(output, expected);
    
    expected = "The boy screamed, \"Where is my apple?\"";
    input = new String[] { "The", "boy", "screamed", ",", "\"Where", "is", "my", "apple",
        "?", "\"" };
    output = RiTa.untokenize(input);
    deepEqual(output, expected);
    
    expected = "The boy screamed, 'Where is my apple?'";
    input = new String[] { "The", "boy", "screamed", ",", "'Where", "is", "my", "apple",
        "?", "'" };
    output = RiTa.untokenize(input);

    deepEqual(output, expected);

    expected = "The boy, dressed in red, ate an apple.";
    input = new String[] { "The", "boy", ",", "dressed", "in", "red", ",", "ate", "an",
        "apple", "." };
    output = RiTa.untokenize(input);
    deepEqual(output, expected);

    expected = "The boy screamed, 'Where is my apple?'";
    input = new String[] { "The", "boy", "screamed", ",", "'Where", "is", "my", "apple",
        "?", "'" };
    output = RiTa.untokenize(input);
    //System.out.println(output);
    deepEqual(output, expected);

    expected = "The boy screamed, \"Where is my apple?\"";
    output = RiTa.untokenize(RiTa.tokenize(expected));
    deepEqual(output, expected);
    
    expected = "Dr. Chan is talking slowly with Mr. Cheng, and they're friends."; 
    input = new String[] { "Dr", ".", "Chan", "is", "talking", "slowly", "with", "Mr",
        ".", "Cheng", ",", "and", "they're", "friends", "." };
    output = RiTa.untokenize(input);
    deepEqual(output, expected);

    input = new String[] { "why", "?", "Me", "?", "huh", "?", "!" };
    expected = "why? Me? huh?!";
    output = RiTa.untokenize(input);
    deepEqual(output, expected);

    input = new String[] { "123", "123", "1", "2", "3", "1", ",", "1", "1", ".", "1",
        "23", ".", "45", ".", "67", "22/05/2012", "12th", "May", ",", "2012" };
    expected = "123 123 1 2 3 1, 1 1. 1 23. 45. 67 22/05/2012 12th May, 2012";
    output = RiTa.untokenize(input);
    deepEqual(output, expected);
  }

  @Test
  public void testTrim()
  {
    equal(RiTa.trim(""), "");
    equal(RiTa.trim(" "), "");
    equal(RiTa.trim("hello "), "hello");
    equal(RiTa.trim("hel'lo "), "hel'lo");
    equal(RiTa.trim(" hel o"), "hel o");
    equal(RiTa.trim(" hello "), "hello");
    equal(RiTa.trim("'hell' "), "'hell'");
    equal(RiTa.trim("'hello    "), "'hello"); // tab
    equal(RiTa.trim("  hello  "), "hello"); // multiple
    equal(RiTa.trim("  hello    "), "hello"); // mixed
  }

  @Test
  public void testEnv()
  {
    ok(RiTa.env() == RiTa.JAVA);
  }
  
  @Test
  public void testDistance()
  {
    equal(1, RiTa.distance(1, 3, 2, 3));
    equal(28, RiTa.distance(30, 1, 2, 1));
    equal(5.656854152679443, RiTa.distance(0, 0, 4, 4)); 
    equal(5.099019527435303, RiTa.distance(3, 3, 8, 4));
  }

  @Test
  public void testGetPhonemesStringIPA()
  {
    RiTa.PHONEME_TYPE = RiTa.IPA;
    
    String[] words = {
	"become", "bɪˈkʌm",
	"parsley", "ˈpɑːɹs li",
	"catnip", "ˈkætˈnɪp",
	"garlic", "ˈgɑːɹ lɪk",
	"dill", "dɪl",
    };

    for (int i = 0; i < words.length; i+=2) {
      equal(words[i+1], RiTa.getPhonemes(words[i]));
    }
    
    RiTa.PHONEME_TYPE = RiTa.ARPA;
  }
  
  @Test
  public void testGetPhonemesStringArrayIPA()
  {
    RiTa.PHONEME_TYPE = RiTa.IPA;

    String[][] inputs = {
	{"become"},
	{"parsley"},
	{"catnip"},
	{"garlic"},
	{"dill"},
    };
    
    String[] outputs = {
	"bɪˈkʌm",
	"ˈpɑːɹs li",
	"ˈkætˈnɪp",
	"ˈgɑːɹ lɪk",
	"dɪl",
    };
    
    for (int i = 0; i < inputs.length; i++) {
      equal(outputs[i], RiTa.getPhonemes(inputs[i]));
    }
    
    RiTa.PHONEME_TYPE = RiTa.ARPA;
  }

  @Test
  public void testGetPhonemesString()
  {
    String txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
    String result = RiTa.getPhonemes(txt);
    String answer = "dh-ah d-ao-g r-ae-n f-ae-s-t-er dh-ae-n dh-ah ah-dh-er d-ao-g . b-ah-t dh-ah ah-dh-er d-ao-g w-aa-z p-r-ih-t-iy-er .";
    equal(result, answer);
    
    result = RiTa.getPhonemes("The");
    answer = "dh-ah";
    equal(result, answer);

    result = RiTa.getPhonemes("The.");
    answer = "dh-ah .";
    equal(result, answer);
    
    result = RiTa.getPhonemes("flowers");
    answer = "f-l-aw-er-z";
    equal(result, answer);
    
    result = RiTa.getPhonemes("mice");
    answer = "m-ay-s";
    equal(result, answer);

    result = RiTa.getPhonemes("The boy jumped over the wild dog.");
    answer = "dh-ah b-oy jh-ah-m-p-t ow-v-er dh-ah w-ay-l-d d-ao-g .";
    equal(result, answer);

    result = RiTa.getPhonemes("The boy ran to the store.");
    answer = "dh-ah b-oy r-ae-n t-uw dh-ah s-t-ao-r .";
    equal(result, answer);

    result = RiTa.getPhonemes("");
    answer = "";
    equal(result, answer);
  }

  @Test
  public void testGetPhonemesStringArray()
  {
    String[] input = { "The" };
    String result = RiTa.getPhonemes(input);
    String answer = "dh-ah";
    equal(result, answer);

    input = new String[] { "The." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ah .";
    equal(result, answer);

    input = new String[] { "The", "boy", "jumped", "over", "the", "wild", "dog." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ah b-oy jh-ah-m-p-t ow-v-er dh-ah w-ay-l-d d-ao-g .";
    equal(result, answer);

    input = new String[] { "The boy ran to the store." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ah b-oy r-ae-n t-uw dh-ah s-t-ao-r .";
    equal(result, answer);

    input = new String[] { "The dog ran faster than the other dog.",
        "But the other dog was prettier." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ah d-ao-g r-ae-n f-ae-s-t-er dh-ae-n dh-ah ah-dh-er d-ao-g . b-ah-t dh-ah ah-dh-er d-ao-g w-aa-z p-r-ih-t-iy-er .";
    equal(result, answer);

    input = new String[] { "" };
    result = RiTa.getPhonemes(input);
    answer = "";
    equal(result, answer);
  }
  
  @Test
  public void testGetPhonemesStringArrayLTS() // TODO: outputs generally do not match (see KnownIssues)
  {
    RiLexicon.enabled = false;
    
    String[] input = { "The" };
    String result = RiTa.getPhonemes(input);
    String answer = "dh-ah";
    equal(result, answer);

    input = new String[] { "The." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ah .";
    equal(result, answer);
    
    input = new String[] { "the" };
    result = RiTa.getPhonemes(input);
    answer = "dh-ah";
    equal(result, answer);
    
    input = new String[] { "" };
    result = RiTa.getPhonemes(input);
    answer = "";
    equal(result, answer);
    
    // TODO: add a few longer tests
    
    RiLexicon.enabled = true;
  }
  
  @Test
  public void testGetStressesStringLTS()
  {
    RiLexicon.enabled = false;

    // TODO: See KnownIssueTests
    equal(RiTa.getStresses(""), "");
    
    RiLexicon.enabled = true;
  }

  @Test
  public void testGetStressesString()
  {
    String result = RiTa.getStresses("The emperor had no clothes on");
    String answer = "0 1/0/0 1 1 1 1";
    equal(result, answer);

    result = RiTa.getStresses("The emperor had no clothes on.");
    answer = "0 1/0/0 1 1 1 1 .";
    equal(result, answer);

    result = RiTa.getStresses("The emperor had no clothes on. The King is fat.");
    answer = "0 1/0/0 1 1 1 1 . 0 1 1 1 .";
    equal(result, answer);

    result = RiTa.getStresses("to preSENT, to exPORT, to deCIDE, to beGIN");
    answer = "1 1/0 , 1 1/0 , 1 0/1 , 1 0/1";
    equal(result, answer);

    result = RiTa.getStresses("to present, to export, to decide, to begin");
    answer = "1 1/0 , 1 1/0 , 1 0/1 , 1 0/1";
    equal(result, answer);

    String txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
    result = RiTa.getStresses(txt);
    answer = "0 1 1 1/0 1 0 1/0 1 . 1 0 1/0 1 1 1/0/0 .";
    equal(result, answer);

    result = RiTa.getStresses("");
    answer = "";
    equal(result, answer);
  }

  @Test
  public void testGetStressesStringArray()
  {
    String[] input = { "The emperor had", "no clothes on" };
    String result = RiTa.getStresses(input);
    String answer = "0 1/0/0 1 1 1 1";
    equal(result, answer);

    input = new String[] { "The", "emperor", "had", "no", "clothes", "on." };
    result = RiTa.getStresses(input);
    answer = "0 1/0/0 1 1 1 1 .";
    equal(result, answer);

    input = new String[] { "to preSENT,", "to exPORT,", "to deCIDE,", "to beGIN" };
    result = RiTa.getStresses(input);
    answer = "1 1/0 , 1 1/0 , 1 0/1 , 1 0/1";
    equal(result, answer);

    input = new String[] { "to present, to export, to decide, to begin" };
    result = RiTa.getStresses(input);
    answer = "1 1/0 , 1 1/0 , 1 0/1 , 1 0/1";
    equal(result, answer);

    input = new String[] { "The dog ran faster than the other dog.",
        "But the other dog was prettier." };
    result = RiTa.getStresses(input);
    answer = "0 1 1 1/0 1 0 1/0 1 . 1 0 1/0 1 1 1/0/0 .";
    equal(result, answer);

    input = new String[] { "" };
    result = RiTa.getStresses(input);
    answer = "";
    equal(result, answer);
  }

  @Test
  public void testGetSyllablesString()
  {
    String txt = "The dog ran faster than the other dog. But the other dog was prettier.";
    String result = RiTa.getSyllables(txt);
    String answer = "dh-ah d-ao-g r-ae-n f-ae/s-t-er dh-ae-n dh-ah ah/dh-er d-ao-g . b-ah-t dh-ah ah/dh-er d-ao-g w-aa-z p-r-ih/t-iy/er .";
    equal(result, answer);

    txt = "The emperor had no clothes on.";
    result = RiTa.getSyllables(txt);
    answer = "dh-ah eh-m/p-er/er hh-ae-d n-ow k-l-ow-dh-z aa-n .";
    equal(result, answer);

    txt = "The Laggin Dragon";
    result = RiTa.getSyllables(txt);
    answer = "dh-ah l-ae/g-ih-n d-r-ae/g-ah-n";
    equal(result, answer);
    
    txt = "the laggin dragon";
    result = RiTa.getSyllables(txt);
    answer = "dh-ah l-ae/g-ih-n d-r-ae/g-ah-n";
    equal(result, answer);

    result = RiTa.getSyllables("@#$%&*()");
    answer = "@ # $ % & * ( )";
    equal(result, answer);

    result = RiTa.getSyllables("");
    answer = "";
    equal(result, answer);
  }

  @Test
  public void testGetSyllablesStringArray()
  {

    String[] txt = { "The dog ran faster than the other dog.",
        "But the other dog was prettier." };
    String result = RiTa.getSyllables(txt);
    String answer = "dh-ah d-ao-g r-ae-n f-ae/s-t-er dh-ae-n dh-ah ah/dh-er d-ao-g . b-ah-t dh-ah ah/dh-er d-ao-g w-aa-z p-r-ih/t-iy/er .";
    equal(result, answer);

    txt = new String[] { "The", "emperor", "had", "no", "clothes", "on." };
    result = RiTa.getSyllables(txt);
    answer = "dh-ah eh-m/p-er/er hh-ae-d n-ow k-l-ow-dh-z aa-n .";
    equal(result, answer);

    txt = new String[] { "The", "Laggin", "Dragon" };
    result = RiTa.getSyllables(txt);
    answer = "dh-ah l-ae/g-ih-n d-r-ae/g-ah-n";
    equal(result, answer);
    
    txt = new String[] { "the", "laggin", "dragon" };
    result = RiTa.getSyllables(txt);
    answer = "dh-ah l-ae/g-ih-n d-r-ae/g-ah-n";
    equal(result, answer);

    txt = new String[] { "@#", "$%", "&*", "()" };
    result = RiTa.getSyllables(txt);
    answer = "@ # $ % & * ( )";
    equal(result, answer);

    txt = new String[] { "" };
    result = RiTa.getSyllables("");
    answer = "";
    equal(result, answer);
  }

  @Test
  public void testGetWordCountString()
  {

    int result = RiTa.getWordCount("123 1231 hi");
    deepEqual(result, 3);

    result = RiTa.getWordCount("The boy screamed, 'Where is my apple?'");
    deepEqual(result, 10);

    result = RiTa.getWordCount("one two three.");
    deepEqual(result, 4);

    result = RiTa.getWordCount("I guess the dog ate the baby.");
    deepEqual(result, 8);

    result = RiTa.getWordCount("Oh my god, the dog ate the baby!");
    deepEqual(result, 10);

    result = RiTa.getWordCount("Which dog ate the baby?");
    deepEqual(result, 6);

    result = RiTa.getWordCount("\'Yes, it was a dog that ate the baby\', he said.");
    deepEqual(result, 15);
  } 

  @Test
  public void testGetPosTagsStringArrayBoolean()
  {
    // String[] getPosTags(String[] words, boolean useWordnetTags)

    String[] txtArr = new String[] { "There", "is", "a", "cat." };
    String[] resultArr = RiTa.getPosTags(txtArr, true);
    String[] answerArr = { "-", "v", "-", "n" };
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("He dances");
    answerArr = new String[] { "prp", "vbz"};
    deepEqual(answerArr, resultArr); 
    
    resultArr = RiTa.getPosTags("Elephants dance");
    answerArr = new String[] { "nns", "vbp" };
    deepEqual(answerArr, resultArr);
    
    resultArr = RiTa.getPosTags("Dave dances");
    answerArr = new String[] { "nnp", "vbz"};
    deepEqual(answerArr, resultArr); 
    
    resultArr = RiTa.getPosTags("the top seed");
    answerArr = new String[] {"dt", "jj", "nn"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("by illegal means");
    answerArr = new String[] { "in", "jj", "nn" };
    deepEqual(answerArr, resultArr);
    
    resultArr = RiTa.getPosTags("biped");
    answerArr = new String[] {"nn"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("greed");
    answerArr = new String[] {"nn"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("creed");
    answerArr = new String[] {"nn"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("weed");
    answerArr = new String[] {"nn"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("freed");
    answerArr = new String[] { "jj" };
    deepEqual(answerArr, resultArr);
    
    resultArr = RiTa.getPosTags("mammal"); // special case
    answerArr = new String[] {"nn"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("morning");
    answerArr = new String[] {"nn"};
    deepEqual(answerArr,resultArr);

    resultArr = RiTa.getPosTags("running");
    answerArr = new String[] {"vbg"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("asserting"); 
    answerArr = new String[] {"vbg"};
    deepEqual(answerArr,resultArr);

    resultArr = RiTa.getPosTags("asfaasd");
    answerArr = new String[] {"nn"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("clothes");
    answerArr = new String[] {"nns"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("innings");
    answerArr = new String[] {"nns"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("teeth");
    answerArr = new String[] {"nns"};
    deepEqual(answerArr,resultArr);
    
    resultArr = RiTa.getPosTags("Dave");
    answerArr = new String[] {"nnp"};
    deepEqual(answerArr,resultArr);

    resultArr = RiTa.getPosTags("They feed the cat");
    answerArr = new String[] {"prp", "vbp", "dt", "nn"};
    deepEqual(answerArr,resultArr);
    
    txtArr = new String[] { "There", "is", "a", "cat." };
    resultArr = RiTa.getPosTags(txtArr, false);
    answerArr = new String[] { "ex", "vbz", "dt", "nn" };
    deepEqual(answerArr,resultArr);

    txtArr = new String[] { "The", "boy,", "dressed", "in", "red,", "ate", "an", "apple." };
    resultArr = RiTa.getPosTags(txtArr, true);
    answerArr = new String[] { "-", "n", "v", "-", "n", "v", "-", "n" };
    deepEqual(resultArr, answerArr);

    txtArr = new String[] { "The", "boy,", "dressed", "in", "red,", "ate", "an", "apple." };
    resultArr = RiTa.getPosTags(txtArr, false);
    answerArr = new String[] { "dt", "nn", "vbn", "in", "nn", "vbd", "dt", "nn" };
    deepEqual(resultArr, answerArr);

    txtArr = new String[] {"The","dog","ran","faster","than","the","other","dog.",};
    resultArr = RiTa.getPosTags(txtArr, true);
    answerArr = new String[] { "-", "n", "v", "r", "-", "-", "a", "n" };
    deepEqual(resultArr, answerArr);

    txtArr = new String[] { "The", "dog", "ran", "faster", "than", "the", "other", "dog." };
    resultArr = RiTa.getPosTags(txtArr, false);
    answerArr = new String[] { "dt", "nn", "vbd", "rbr", "in", "dt", "jj", "nn" };
    deepEqual(resultArr, answerArr);
    
    resultArr = RiTa.getPosTags("the boy dances");
    answerArr = new String[] {"dt", "nn", "vbz"};
    deepEqual(answerArr,resultArr); 
    
    resultArr = RiTa.getPosTags("he dances");
    answerArr = new String[] {"prp", "vbz"};
    deepEqual(answerArr,resultArr); 

    txtArr = new String[] { "" };
    resultArr = RiTa.getPosTags(txtArr, true);
    answerArr = new String[] { "-" };
    deepEqual(resultArr, answerArr);

    txtArr = new String[] { "" };
    resultArr = RiTa.getPosTags(txtArr, false);
    answerArr = new String[] { "" };
    deepEqual(resultArr, answerArr);   
  }
  
  @Test
  public void testGetPosTagsStringArrayBooleanSNS()
  {
    String[] checks = {"emphasis","stress","discus","colossus","fibrosis","digitalis","pettiness","mess",
           "cleanliness", "orderliness", "bronchitis", "preparedness", "highness"  };
    for(int i=0; i < checks.length; i++) {
      //System.out.println(checks[i]+": "+RiTa.getPosTags(checks[i], false)[0]);
      deepEqual(RiTa.getPosTags(checks[i], false), new String[] { "nn" });
    }
  }

  @Test
  public void testGetPosTagsCompareStringAndArray()
  {
    String s = "There is a cat.";
    String[] txtArr = RiTa.tokenize(s);
    String[] resultArr = RiTa.getPosTags(txtArr, true);
    String[] result = RiTa.getPosTags(s, true);

    deepEqual(resultArr, result);

    resultArr = RiTa.getPosTags(txtArr, false);
    result = RiTa.getPosTags(s, false);
    deepEqual(resultArr, result);

    //System.out.println(":" + RiTa.getPosTagsInline(s));
    //System.out.println(":" + RiTa.getPosTagsInline(txtArr));
    deepEqual(RiTa.getPosTagsInline(txtArr), RiTa.getPosTagsInline(s));
  }

  @Test
  public void testGetPosTagsStringBoolean()
  {
    String[] result = RiTa.getPosTags("asfaasd", true); // default to n on unknown word
    String[] answer = new String[] { "n" };
    deepEqual(result, answer);

    result = RiTa.getPosTags("asfaasd", false);
    answer = new String[] { "nn" }; // default to nn on unknown word
    deepEqual(result, answer);
    
    result = RiTa.getPosTags("testing", false);
    //System.out.println(Arrays.asList(result));
    answer = new String[] { "vbg" };
    deepEqual(result, answer);
    
    result = RiTa.getPosTags("testy", false);
    //System.out.println(Arrays.asList(result));
    answer = new String[] { "jj" };
    deepEqual(result, answer);

    result = RiTa.getPosTags("clothes", true);
    answer = new String[] { "n" };
    deepEqual(result, answer);

    result = RiTa.getPosTags("clothes", false);
    answer = new String[] { "nns" };
    deepEqual(result, answer);

    result = RiTa.getPosTags("There is a cat.", true);
    answer = new String[] { "-", "v", "-", "n", "-" };
    deepEqual(result, answer); // TODO check result

    result = RiTa.getPosTags("There is a cat.", false);
    //System.out.println("result=" + RiTa.asList(result));

    answer = new String[] { "ex", "vbz", "dt", "nn", "." };
    deepEqual(result, answer); // TODO check result

    result = RiTa.getPosTags("", true);
    answer = new String[] { };

    deepEqual(result, answer);

    result = RiTa.getPosTags("", false);
    answer = new String[] {  };
    deepEqual(result, answer);
  }

  @Test
  public void testGetPosTagsStringArray()
  {

    // String[] getPosTags(String[] words)

    String[] txtArr = new String[] { "There", "is", "a", "cat." };
    String[] resultArr = RiTa.getPosTags(txtArr);
    String[] answerArr = { "ex", "vbz", "dt", "nn", };
    deepEqual(resultArr, answerArr);
    
    String[] txtArr2 = new String[] { "There", "is", "a", "shitty", "cat." };
    resultArr = RiTa.getPosTags(txtArr2);
    answerArr = new String[] { "nnp", "vbz"};
    String[] answerArr2 = { "ex", "vbz", "dt", "jj", "nn", };
    deepEqual(answerArr2, answerArr2); 

    resultArr = new String[] {};
    txtArr = new String[] { "The", "dog", "ran", "faster", "than", "the", "other", "dog." };
    resultArr = RiTa.getPosTags(txtArr);
    answerArr = new String[] { "dt", "nn", "vbd", "rbr", "in", "dt", "jj", "nn", };
    deepEqual(resultArr, answerArr);

    txtArr = new String[] { "" };
    resultArr = RiTa.getPosTags(txtArr);
    deepEqual(resultArr, new String[] { "" } );
  }

  @Test
  public void testGetPosTagsString()
  {
    String[] result = RiTa.getPosTags("asfaasd");
    String[] answer = new String[] { "nn" };
    //System.out.println(RiTa.asList(result));
    deepEqual(result, answer);

    result = RiTa.getPosTags("clothes");
    answer = new String[] { "nns" };
    //System.out.println(RiTa.asList(result));
    deepEqual(result, answer);

//    result = RiTa.getPosTags("There is a cat.");
//    answer = new String[] { "ex", "vbz", "dt", "nn", "." };
//    //System.out.println(RiTa.asList(result));
//    deepEqual(result, answer); // TODO check result
    
    result = RiTa.getPosTags("I am a boy.");
    answer = new String[] { "prp", "vbp", "dt", "nn", "." };
    //System.out.println(RiTa.asList(result));
    deepEqual(result, answer); // TODO check result
    
//    result = RiTa.getPosTags("He is a boy.");
//    answer = new String[] { "prp", "vbz", "dt", "nn", "." };
//    //System.out.println(RiTa.asList(result));
//    deepEqual(result, answer); // TODO check result

    String txtArr = "bronchitis" ;
    String[] resultArr = RiTa.getPosTags(txtArr, false);
    String[] answerArr = new String[] { "nn" };
    deepEqual(resultArr, answerArr);
    
    txtArr ="orderliness" ;
    resultArr = RiTa.getPosTags(txtArr, false);
    answerArr = new String[] { "nn" };
    deepEqual(resultArr, answerArr);
    
    txtArr =  "cleanliness" ; 
    resultArr = RiTa.getPosTags(txtArr, false);
    answerArr = new String[] { "nn" };
    deepEqual(resultArr, answerArr);
    
    result = RiTa.getPosTags("");
    answer = new String[] { };
    //System.out.println(RiTa.asList(result));
    deepEqual(result, answer);
    
    deepEqual(RiTa.getPosTags("he"), new String[] { "prp" });
    
    // Tests for verb conjugation

    deepEqual(RiTa.getPosTags("is"), new String[] { "vbz" });
    deepEqual(RiTa.getPosTags("am"), new String[] { "vbp" });
    deepEqual(RiTa.getPosTags("be"), new String[] { "vb"  });
    
    result = RiTa.getPosTags("There is a cat.");
    answer = new String[] { "ex", "vbz", "dt", "nn", "." };
    deepEqual(result, answer); 

    result = RiTa.getPosTags("There was a cat.");
    answer = new String[] { "ex", "vbd", "dt", "nn", "." };
    deepEqual(result, answer);
    
    result = RiTa.getPosTags("I am a cat.");
    answer = new String[] { "prp", "vbp", "dt", "nn", "." };
    deepEqual(result, answer);
    
    result = RiTa.getPosTags("I was a cat.");
    answer = new String[] { "prp", "vbd", "dt", "nn", "." };
    deepEqual(result, answer);
    
    deepEqual(RiTa.getPosTags("outnumber"), new String[] { "vb" });
    deepEqual(RiTa.getPosTags("outnumbers"), new String[] { "vbz" }); 
    deepEqual(RiTa.getPosTags("I outnumber you"), new String[] { "prp", "vbp", "prp", });
    deepEqual(RiTa.getPosTags("He outnumbers us"), new String[] { "prp", "vbz",  "prp"});
    deepEqual(RiTa.getPosTags("I outnumbered you"), new String[] { "prp", "vbd", "prp" });
    deepEqual(RiTa.getPosTags("She outnumbered us"), new String[] { "prp", "vbd", "prp"});
    
    deepEqual(RiTa.getPosTags("flunk"), new String[] {  "vb" });
    deepEqual(RiTa.getPosTags("flunks"), new String[] {  "vbz" });
    deepEqual(RiTa.getPosTags("He flunks the test"), new String[] { "prp", "vbz",  "dt", "nn"});
    deepEqual(RiTa.getPosTags("I flunked the test"), new String[] { "prp", "vbd",  "dt", "nn"});
    deepEqual(RiTa.getPosTags("She flunked the test"), new String[] { "prp", "vbd",  "dt", "nn"});
  }

  @Test
  public void testGetPosTagsInlineStringArray()
  {

    String[] txtArr = new String[] { "asfaasd" };
    String result = RiTa.getPosTagsInline(txtArr);
    String answer = "asfaasd/nn";
    deepEqual(result, answer);

    txtArr = new String[] { "clothes" };
    result = RiTa.getPosTagsInline(txtArr);
    answer = "clothes/nns";
    deepEqual(result, answer);
    
    txtArr = new String[] { "teeth" };
    result = RiTa.getPosTagsInline(txtArr);
    answer = "teeth/nns";
    deepEqual(result, answer);

    txtArr = new String[] { "There", "is", "a", "cat." };
    result = RiTa.getPosTagsInline(txtArr);
    answer = "There/ex is/vbz a/dt cat./nn";
    deepEqual(result, answer);

    txtArr = new String[] { "" };
    result = RiTa.getPosTagsInline(txtArr);
    answer = "";
    deepEqual(result, answer);
  }

  @Test
  public void testGetPosTagsInlineString()
  {
    String result = RiTa.getPosTagsInline("asfaasd");
    String answer = "asfaasd/nn";
    deepEqual(result, answer);

    result = RiTa.getPosTagsInline("clothes");
    answer = "clothes/nns";
    deepEqual(result, answer);

    result = RiTa.getPosTagsInline("There is a cat.");
    answer = "There/ex is/vbz a/dt cat/nn ./.";
    //System.out.println(result);
    deepEqual(result, answer);

    // TODO: is this different than RiTaJS -- if so add to issues...
    result = RiTa.getPosTagsInline("The boy, dressed in red, ate an apple.");
    answer = "The/dt boy/nn ,/, dressed/vbn in/in red/jj ,/, ate/vbd an/dt apple/nn ./.";
    deepEqual(result, answer);

    // TODO: is this different than RiTaJS -- if so add to issues... (doesnt seem quite right)
    String txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
    result = RiTa.getPosTagsInline(txt);
    answer = "The/dt dog/nn ran/vbd faster/rbr than/in the/dt other/jj dog/nn ./. But/cc the/dt other/jj dog/nn was/vbd prettier/jjr ./.";
    //System.out.println(result);

    deepEqual(result, answer);

    result = RiTa.getPosTagsInline("");
    answer = "";
    deepEqual(result, answer);
  }
  
  @Test
  public void testConcordance()
  {
    Map<String, Integer> data = RiTa.concordance("The dog ate the cat");
    equal(data.size(),5);
    ok(data.get("the")==1);
    ok(data.get("The")==1);
    equal(data.get("THE"),null);
    
    Map<String, Object> args = new HashMap<String, Object>();
    args.put("ignoreCase", false);
    args.put("ignoreStopWords", false);
    args.put("ignorePunctuation", false);
    args.put("wordsToIgnore", null);
    
    data = RiTa.concordance("The dog ate the cat", args);
    equal(data.size(),5);
    ok(data.get("the")==1);
    ok(data.get("The")==1);
    equal(data.get("THE"),null);
    
    args.put("ignoreCase", true);
    data = RiTa.concordance("The dog ate the cat", args);
    equal(data.size(),4);
    ok(data.get("the")==2);
    equal(data.get("The"),null);
    equal(data.get("THE"),null);
    
    // test with larger text with all false 
    args.put("ignoreCase", false);
    args.put("ignorePunctuation", false);
    args.put("ignoreStopWords", false);
    String txt = RiTa.loadString("kafka.txt");

    data = RiTa.concordance(txt, args);
    ok(data.get("Gregor")==199);
    ok(data.get("Gregor") + data.get("Gregor's") == 298);
    ok(data.get("sister") == 96);
    ok(data.get("sister") + data.get("sister's") == 101);
    ok(data.get("here")==19);
    ok(data.get("the") == 1097);
    ok(data.get("The") == 51);
    ok(data.get(",")==1292);
    ok(data.get(".")==737);
    
    // test all true
    int nUppercaseFather = data.get("Father");
    int nLowercaseFather = data.get("father");
    args.put("ignoreCase", true);
    args.put("ignorePunctuation", true);
    args.put("ignoreStopWords", true);
    data = RiTa.concordance(txt, args);
    ok(data.get("gregor") + data.get("gregor's") == 298);
    ok(data.get("sister") + data.get("sister's") == 101);
    equal(data.get("here"),null);
    equal(data.get("the"),null);
    equal(data.get(","),null);
    equal(data.get("."),null);
    ok(data.get("father") == nUppercaseFather + nLowercaseFather);
    
    // test ignoreCase
    args.put("ignoreCase", true);
    args.put("ignorePunctuation", false);
    args.put("ignoreStopWords", false);
    data = RiTa.concordance(txt, args);
    ok(data.get("father") == nUppercaseFather + nLowercaseFather);

    // test ignorePunctuation
    args.put("ignoreCase", false);
    args.put("ignorePunctuation", true);
    args.put("ignoreStopWords", false);
    data = RiTa.concordance(txt, args);
    equal(data.get(","),null);
    equal(data.get("."),null);

    // test ignoreStopWords
    args.put("ignoreCase", false);
    args.put("ignorePunctuation", false);
    args.put("ignoreStopWords", true);
    data = RiTa.concordance(txt, args);
    equal(data.get("here"),null);
    equal(data.get("the"),null);
    
    // test ignoreStopWords and ignorePunctuation
    args.put("ignoreCase", false);
    args.put("ignorePunctuation", true);
    args.put("ignoreStopWords", true);
    data = RiTa.concordance(txt, args);
    equal(data.get(","),null);
    equal(data.get("."),null);
    equal(data.get("here"),null);
    equal(data.get("the"),null);
    
    // test ignoreStopWords and ignoreCase
    args.put("ignoreCase", true);
    args.put("ignorePunctuation", false);
    args.put("ignoreStopWords", true);
    data = RiTa.concordance(txt, args);
    ok(data.get("father") == nUppercaseFather + nLowercaseFather);
    equal(data.get("here"),null);
    equal(data.get("the"),null);
    
    // test ignorePunctuation and ignoreCase
    args.put("ignoreCase", true);
    args.put("ignorePunctuation", true);
    args.put("ignoreStopWords", false);
    data = RiTa.concordance(txt, args);
    ok(data.get("father") == nUppercaseFather + nLowercaseFather);
    equal(data.get(","),null);
    equal(data.get("."),null);
    
    // test wordsToIgnore
    args.put("wordsToIgnore", new String[]{"father", "sister"}); 
    args.put("ignoreCase", false);
    args.put("ignorePunctuation", false);
    args.put("ignoreStopWords", false);
    data = RiTa.concordance(txt, args);
    equal(data.get("father"),null);
    equal(data.get("sister"),null);
    
    // test that result is sorted by frequency
    boolean isDescending = true;
    int current = -1; // -1 indicate 'current' at starting value 
    for (String entry : data.keySet()) {      
      if (current == -1)
	current = data.get(entry);
      else if (current < data.get(entry)) {
	isDescending = false;
	break;
      }
      else
	current = data.get(entry);
    }
    equal(isDescending,true);
  }
  
  @Test
  public void testKwic()
  {
    String s = "The dog ate the cat. The bear Ate the honey";
    String[] lines = RiTa.kwic(s,"ate");
    equal(lines.length,1);
    //RiTa.out(lines);
    Map m = new HashMap();
    m.put("ignoreCase", true);
    lines = RiTa.kwic(s,"ate",m);
    //RiTa.out(lines);
    equal(lines.length,2);
    
    // test ignorePunctuation
    m.put("ignorePunctuation", false);
    String txt = RiTa.loadString("kafka.txt");
    lines = RiTa.kwic(txt,",",m);
    equal(lines.length,1091);
    m.put("ignorePunctuation", true);
    lines = RiTa.kwic(txt,",",m);
    equal(lines.length,0);

    // test ignoreCase
    m.put("wordCount", 4);
    m.put("ignoreCase", true);
    lines = RiTa.kwic(txt,"eventually",m);
    equal(lines.length,2);
    m.put("ignoreCase", false);
    lines = RiTa.kwic(txt,"eventually",m);
    equal(lines.length,1);
    
    // test ignoreStopWords
    lines = RiTa.kwic(txt,"here",m);
    equal(lines.length,19);
    m.put("ignoreStopWords", true);
    lines = RiTa.kwic(txt,"here",m);
    equal(lines.length,0);

    // test wordCount
    m.put("wordCount", 6);
    m.put("ignoreCase", false);
    lines = RiTa.kwic(txt,"sister",m);
    for (int i = 0; i < lines.length; i++) {
      int length = RiTa.tokenize(lines[i]).length;
      equal(length,6 + 1 + 6);
    }

    // test wordsToIgnore
    m.put("wordsToIgnore", new String[]{"father", "sister"}); 
    lines = RiTa.kwic(txt,"father",m);
    equal(lines.length,0);
    lines = RiTa.kwic(txt,"sister",m);
    equal(lines.length,0);
    
    // test against issue #169
    m.put("wordCount", 5);
    lines = RiTa.kwic(txt,"door",m);
    equal(lines.length,86);
  }
  
  @Test
  public void testConjugate()
  {
    Map<String, Comparable> args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PAST_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.FIRST_PERSON);
    
    String c = RiTa.conjugate("swim", args);    
    equal(c, "swam"); // I swam

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PRESENT_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.THIRD_PERSON);

    String[] a = { "swims", "needs", "opens" };
    String[] s = { "swim", "need", "open" };
    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PRESENT_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.THIRD_PERSON);
    args.put("passive", true);

    a = new String[] { "is swum", "is needed", "is opened" };
    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PAST_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.FIRST_PERSON);
    
    c = RiTa.conjugate("swim", args);
    equal(c, "swam"); // I swam

    s = new String[] { "swim", "need", "open" };
    a = new String[] { "swam", "needed", "opened" };

    ok(a.length == s.length);

    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }
    
    try
    {
      c = RiTa.conjugate("", args);
      fail("no exception");
    }
    catch (RiTaException e)
    {
      ok(e);
    }

    equal("swum", RiTa.getPastParticiple("swim"));

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PAST_TENSE);
    args.put("number", RiTa.PLURAL);
    args.put("person", RiTa.SECOND_PERSON);

    a = new String[] { "swam", "needed", "opened"  };
    ok(a.length == s.length);

    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.FUTURE_TENSE);
    args.put("number", RiTa.PLURAL);
    args.put("person", RiTa.SECOND_PERSON);

    a = new String[] { "will swim", "will need", "will open"  };

    ok(a.length == s.length);

    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PAST_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.THIRD_PERSON);

    a = new String[] { "swam", "needed", "opened", };
    ok(a.length == s.length);
    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PAST_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.THIRD_PERSON);
    args.put("form", RiTa.INFINITIVE);

    a = new String[] { "to swim", "to need", "to open",};
    ok(a.length == s.length);
    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PAST_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.THIRD_PERSON);
    args.put("passive", true);

    s = new String[] { "scorch", "burn", "hit", };
    a = new String[] { "was scorched", "was burned", "was hit", };
    ok(a.length == s.length);
    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      //System.out.println("GOT: "+c+" EXPECTED: "+a[i]);

      equal(c, a[i]);
    }

    s = new String[] { "swim", "need", "open",  };

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PRESENT_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.THIRD_PERSON);
    args.put("form", RiTa.INFINITIVE);
    args.put("progressive", true);

    a = new String[] { "to be swimming", "to be needing", "to be opening", };
    ok(a.length == s.length);
    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PRESENT_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.THIRD_PERSON);
    args.put("form", RiTa.INFINITIVE);
    args.put("perfect", true);

    a = new String[] { "to have swum", "to have needed", "to have opened", };
    ok(a.length == s.length);
    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

    args = new HashMap<String, Comparable>();
    args.put("tense", RiTa.PAST_TENSE);
    args.put("number", RiTa.PLURAL);
    args.put("person", RiTa.SECOND_PERSON);

    equal(RiTa.conjugate("barter", args), "bartered");
    equal(RiTa.conjugate("run", args), "ran");

    s = new String[] { "compete", "complete", "eject" };
    a = new String[] { "competed", "completed", "ejected" };
    ok(a.length == s.length);
    for (int i = 0; i < s.length; i++)
    {
      c = RiTa.conjugate(s[i], args);
      equal(c, a[i]);
    }

  }

  @Test
  public void testGetPastParticiple()
  {
    equal(RiTa.getPastParticiple("sleep"), "slept");
    equal(RiTa.getPastParticiple("withhold"), "withheld");
    equal(RiTa.getPastParticiple("cut"), "cut");
    equal(RiTa.getPastParticiple("go"), "gone");
    equal(RiTa.getPastParticiple("swim"), "swum");
    equal(RiTa.getPastParticiple("would"), "would");
    equal(RiTa.getPastParticiple("might"), "might");
    equal(RiTa.getPastParticiple("speak"), "spoken");
    equal(RiTa.getPastParticiple("break"), "broken");
    equal(RiTa.getPastParticiple("shine"), "shone");
    equal(RiTa.getPastParticiple("drink"), "drunk");
    equal(RiTa.getPastParticiple("study"), "studied");
    equal(RiTa.getPastParticiple("awake"), "awoken");
    equal(RiTa.getPastParticiple("become"), "became");
    equal(RiTa.getPastParticiple("drink"), "drunk"); 
    equal(RiTa.getPastParticiple("plead"), "pleaded");
    equal(RiTa.getPastParticiple("run"), "run");
    equal(RiTa.getPastParticiple("shine"), "shone"); // or shined
    equal(RiTa.getPastParticiple("shrink"), "shrunk"); // or shrunken
    equal(RiTa.getPastParticiple("stink"), "stunk");
    equal(RiTa.getPastParticiple("study"), "studied");
    equal(RiTa.getPastParticiple(""), ""); 
  }

  @Test
  public void testGetPresentParticiple()
  {
    equal(RiTa.getPresentParticiple("sleep"), "sleeping");
    equal(RiTa.getPresentParticiple("withhold"), "withholding");
    equal(RiTa.getPresentParticiple("cut"), "cutting");
    equal(RiTa.getPresentParticiple("go"), "going");
    equal(RiTa.getPresentParticiple("run"), "running");
    equal(RiTa.getPresentParticiple("speak"), "speaking");
    equal(RiTa.getPresentParticiple("break"), "breaking");
    equal(RiTa.getPresentParticiple("become"), "becoming");
    equal(RiTa.getPresentParticiple("plead"), "pleading");
    equal(RiTa.getPresentParticiple("awake"), "awaking");
    equal(RiTa.getPresentParticiple("study"), "studying");
    equal(RiTa.getPresentParticiple("lie"), "lying");
    equal(RiTa.getPresentParticiple("swim"), "swimming");
    equal(RiTa.getPresentParticiple("run"), "running");
    equal(RiTa.getPresentParticiple("dig"), "digging");
    equal(RiTa.getPresentParticiple("set"), "setting");
    equal(RiTa.getPresentParticiple("speak"), "speaking");
    equal(RiTa.getPresentParticiple("bring"), "bringing");
    equal(RiTa.getPresentParticiple("speak"), "speaking");
    //System.out.println("getPresentParticiple:'"+RiTa.getPresentParticiple("study ")+ "'");
    equal(RiTa.getPresentParticiple(""), "");
  }

  // For correct results on Porter/Lancaster, see http://text-processing.com/demo/stem/

  @Test
  public void testStemString()
  {
    ok("tested in testStemStringString()");
  }

  public void testStemPorter()
  {
    String type = RiTa.PORTER;

    // --------- Porter ---------------

    equal(RiTa.stem("cakes"), "cake");

    String[] tests = { "run", "runs", "running" };
    for (int i = 0; i < tests.length; i++)
    {
      equal(RiTa.stem(tests[i],type), "run");
    }
    
    tests = new String[]{ "hide", "hides", "hiding" };
    for (int i = 0; i < tests.length; i++)
    {
      equal(RiTa.stem(tests[i],type), "hide");
    }

    
    tests = new String[]{ "take", "takes", "taking" };
    for (int i = 0; i < tests.length; i++)
    {
      equal(RiTa.stem(tests[i],type), "take");
    }

    equal(RiTa.stem("gases",type), "gase");
    equal(RiTa.stem("buses",type), "buse");
    equal(RiTa.stem("happiness",type), "happi");
    equal(RiTa.stem("joyful",type), "joy");
    equal(RiTa.stem("terrible",type), "terribl");

    String test = "Stemming is funnier than a bummer";
    String result = "Stem is funnier than a bummer";
    //System.out.println("testStemString1: " + RiTa.stem(test));
    equal(RiTa.stem(test,type), result);
  }
  
  // For correct results on Porter/Lancaster, see http://text-processing.com/demo/stem/
  @Test
  public void testStemStringString()
  {
    // TODO: port over to JS (goal: remove Porter or Lancaster)
    testStemPorter();
    testStemLancaster();
    testStemPling();
  }

  private void testStemLancaster() {
    
    // --------- Lancaster ---------------
    
    String type = RiTa.LANCASTER;

    String[] tests = new String[] {"run", "runs", "running" };
    for (int i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i],type), "run");
    }
    
    tests = new String[]{ "hide", "hides", "hiding" };
    for (int i = 0; i < tests.length; i++)
    {
      equal(RiTa.stem(tests[i],type), "hid");
    }

    tests = new String[]{ "take", "takes", "taking" };
    for (int i = 0; i < tests.length; i++)
    {
      equal(RiTa.stem(tests[i],type), "tak");
    }
    
    tests = new String[]{ "become", "becomes", "becoming" };
    for (int i = 0; i < tests.length; i++)
    {
      equal(RiTa.stem(tests[i],type), "becom");
    }
    
    equal(RiTa.stem("gases",type), "gas");
    equal(RiTa.stem("buses",type), "bus");
    equal(RiTa.stem("happiness",type), "happy");
    equal(RiTa.stem("terrible",type), "terr");

    String test = "Stemming is funnier than a bummer says the sushi loving computer";
    String result = "stem is funny than a bum say the sush lov comput";
    //System.out.println("testStemString3: " + RiTa.stem(test));

    equal(RiTa.stem(test,type), result);
    equal(RiTa.stem("cakes", type), "cak");    
  }

  private void testStemPling() {
    
    // --------- Pling ---------------

    String type = RiTa.PLING;

    equal(RiTa.stem("cakes", type), "cake");

    String[] tests = new String[] {"run", "runs", "running" };
    equal(RiTa.stem(tests[0],type), "run");
    equal(RiTa.stem(tests[1],type), "run");
    equal(RiTa.stem(tests[2],type), "running");
    
    tests = new String[]{ "take", "takes", "taking" };
    equal(RiTa.stem(tests[0],type), "take");
    equal(RiTa.stem(tests[1],type), "take");
    equal(RiTa.stem(tests[2],type), "taking");
    
    tests = new String[]{ "hide", "hides", "hiding" };
    equal(RiTa.stem(tests[0],type), "hide");
    equal(RiTa.stem(tests[1],type), "hide");
    equal(RiTa.stem(tests[2],type), "hiding");

    tests = new String[]{ "become", "becomes", "becoming" };
    equal(RiTa.stem(tests[0],type), "become");
    equal(RiTa.stem(tests[1],type), "become");
    equal(RiTa.stem(tests[2],type), "becoming");
    
    equal(RiTa.stem("gases",type), "gas");
    equal(RiTa.stem("buses",type), "bus");
    equal(RiTa.stem("happiness",type), "happiness");
    equal(RiTa.stem("terrible",type), "terrible");
    
    String test = "Stemming is funnier than a bummer";
    String result = "Stemming is funnier than a bummer";
    //System.out.println("testStemString2: " + RiTa.stem(test,type));
    equal(RiTa.stem(test, type), result);
    
  }

  @Test
  public void testPluralize()
  {
    equal("eyes", RiTa.pluralize("eye"));
    equal("blondes", RiTa.pluralize("blonde"));
    equal("blondes", RiTa.pluralize("blond"));
    equal("dogs", RiTa.pluralize("dog"));
    equal("feet", RiTa.pluralize("foot"));
    equal("men", RiTa.pluralize("man"));
    equal("teeth", RiTa.pluralize("tooth"));
    equal("cakes", RiTa.pluralize("cake"));
    equal("kisses", RiTa.pluralize("kiss"));
    equal("children", RiTa.pluralize("child"));

    equal("randomwords", RiTa.pluralize("randomword"));
    equal("lice", RiTa.pluralize("louse"));

    equal("sheep", RiTa.pluralize("sheep"));
    equal("shrimps", RiTa.pluralize("shrimp"));
    equal("series", RiTa.pluralize("series"));
    equal("mice", RiTa.pluralize("mouse"));

    equal("", RiTa.pluralize(""));

    equal(RiTa.pluralize("tomato"), "tomatoes");
    equal(RiTa.pluralize("toe"), "toes");

    equal(RiTa.pluralize("deer"), "deer");
    equal(RiTa.pluralize("ox"), "oxen");

    equal(RiTa.pluralize("tobacco"), "tobacco");
    equal(RiTa.pluralize("cargo"), "cargo");
    equal(RiTa.pluralize("golf"), "golf");
    equal(RiTa.pluralize("grief"), "grief");
    equal(RiTa.pluralize("wildlife"), "wildlife");
    equal(RiTa.pluralize("taxi"), "taxis");
    equal(RiTa.pluralize("Chinese"), "Chinese");
    equal(RiTa.pluralize("bonsai"), "bonsai");
    
    equal(RiTa.pluralize("gas"), "gases");
    equal(RiTa.pluralize("bus"), "buses");
    
    equal("beautifuls", RiTa.pluralize("beautiful"));
    
    equal("crises", RiTa.pluralize("crisis"));
    equal("theses", RiTa.pluralize("thesis"));
    equal("apotheses", RiTa.pluralize("apothesis"));
    equal("stimuli", RiTa.pluralize("stimulus"));
    equal("alumni", RiTa.pluralize("alumnus"));
    equal("corpora", RiTa.pluralize("corpus"));
    
    equal("women", RiTa.pluralize("woman"));
    equal("men", RiTa.pluralize("man"));
    equal("congressmen", RiTa.pluralize("congressman"));
    equal("aldermen", RiTa.pluralize("alderman"));
    equal("freshmen", RiTa.pluralize("freshman"));
    
    equal("bikinis", RiTa.pluralize("bikini")); 
    equal("martinis", RiTa.pluralize("martini"));
    equal("menus", RiTa.pluralize("menu"));
    equal("gurus", RiTa.pluralize("guru"));
    
    equal("media", RiTa.pluralize("medium"));
    equal("concerti", RiTa.pluralize("concerto"));
    equal("termini", RiTa.pluralize("terminus"));
    
  }

  @Test
  public void testSingularize()
  {
    equal("pleae", RiTa.singularize("pleae"));
    equal("eye", RiTa.singularize("eyes"));
    equal("blonde", RiTa.singularize("blondes"));
    
    equal(RiTa.singularize("bonsai"), "bonsai");
  
    equal(RiTa.singularize("chiefs"), "chief");
    equal(RiTa.singularize("monarchs"), "monarch");
    equal(RiTa.singularize("lochs"), "loch");
    equal(RiTa.singularize("stomachs"), "stomach");

    equal(RiTa.singularize("people"), "person");
    equal(RiTa.singularize("monies"), "money");
    equal(RiTa.singularize("vertebrae"), "vertebra");
    equal(RiTa.singularize("humans"), "human");
    equal(RiTa.singularize("germans"), "german");
    equal(RiTa.singularize("romans"), "roman");

    equal(RiTa.singularize("memoranda"), "memorandum");
    equal(RiTa.singularize("data"), "datum");
    equal(RiTa.singularize("appendices"), "appendix");
    equal(RiTa.singularize("theses"), "thesis");
    equal(RiTa.singularize("alumni"), "alumnus");

    equal(RiTa.singularize("solos"), "solo");
    equal(RiTa.singularize("music"), "music");

    equal(RiTa.singularize("oxen"), "ox");
    equal(RiTa.singularize("solos"), "solo");
    equal(RiTa.singularize("music"), "music");

    equal(RiTa.singularize("tobacco"), "tobacco");
    equal(RiTa.singularize("cargo"), "cargo");
    equal(RiTa.singularize("golf"), "golf");
    equal(RiTa.singularize("grief"), "grief");

    equal(RiTa.singularize("cakes"), "cake");

    equal("dog", RiTa.singularize("dogs"));
    equal("foot", RiTa.singularize("feet"));
    equal("tooth", RiTa.singularize("teeth"));
    equal("kiss", RiTa.singularize("kisses"));
    equal("child", RiTa.singularize("child"));
    equal("randomword", RiTa.singularize("randomwords"));
    equal("deer", RiTa.singularize("deer"));
    equal("sheep", RiTa.singularize("sheep"));
    equal("shrimp", RiTa.singularize("shrimps"));
    equal("", RiTa.singularize(""));
    equal(RiTa.singularize("tomatoes"), "tomato");
    equal(RiTa.singularize("photos"), "photo");

    equal(RiTa.singularize("toes"), "toe");

    equal("series", RiTa.singularize("series"));
    equal("ox", RiTa.singularize("oxen"));
    equal("man", RiTa.singularize("men"));
    equal("mouse", RiTa.singularize("mice"));
    equal("louse", RiTa.singularize("lice"));
    equal("child", RiTa.singularize("children"));
    equal(RiTa.singularize("chinese"), "chinese");
    equal(RiTa.singularize("taxis"), "taxi");
    
    equal(RiTa.singularize("gases"), "gas");
    equal(RiTa.singularize("buses"), "bus");
    equal(RiTa.singularize("happiness"), "happiness");
    
    equal(RiTa.singularize("crises"), "crisis");
    equal(RiTa.singularize("theses"), "thesis");
    equal(RiTa.singularize("apotheses"), "apothesis");
    equal(RiTa.singularize("stimuli"), "stimulus");
    equal(RiTa.singularize("alumni"), "alumnus");
    equal(RiTa.singularize("corpora"), "corpus");
    
    equal("man", RiTa.singularize("men"));
    equal("woman", RiTa.singularize("women"));
    equal("congressman", RiTa.singularize("congressmen")); 
    equal("alderman", RiTa.singularize("aldermen"));
    equal("freshman", RiTa.singularize("freshmen"));
    equal("fireman", RiTa.singularize("firemen"));
    equal("grandchild", RiTa.singularize("grandchildren"));
    equal("menu", RiTa.singularize("menus"));
    equal("guru", RiTa.singularize("gurus"));
    
    equal("medium", RiTa.singularize("media"));
    equal("concerto", RiTa.singularize("concerti"));
    equal("terminus", RiTa.singularize("termini"));
  }

  @Test
  public void testRandomInt()
  {

    // int random(int max)
    int answer = RiTa.random(50);
    ok(answer >= 0);
    int answer2 = RiTa.random(50);
    ok(answer2 < 50);

    answer = RiTa.random(1);
    ok(answer == 0);

    answer = RiTa.random(2);
    ok(answer >= 0);
    answer2 = RiTa.random(2);
    ok(answer2 < 2);

    answer = RiTa.random(0);
    ok(answer == 0);
  }

  @Test
  public void testRandomIntInt()
  {
    // int random(int min, int max)
    float answer = RiTa.random(10, 34);
    ok(answer >= 10);
    float answer2 = RiTa.random(10, 34);
    ok(answer2 < 34);

    answer = RiTa.random(1, 2);
    ok(answer >= 1);
    answer2 = RiTa.random(1, 2);
    ok(answer2 < 2);

    answer = RiTa.random(1, 3);
    ok(answer >= 1);
    answer2 = RiTa.random(1, 3);
    ok(answer2 < 3);

    answer = RiTa.random(0, 0);
    ok(answer == 0);

    answer = RiTa.random(5, 1); //swap "min > max"
    ok(answer >= 1);
    answer2 = RiTa.random(0, 0);
    ok(answer2 < 5);
  }

  @Test
  public void testRandomItemList()
  {
    String[] toks = RiTa.tokenize("The boy, dressed in red, ate an apple.!?");
    for (int i = 0; i < toks.length * 2; i++) {
      ok(Arrays.asList(toks).contains(RiTa.randomItem(Arrays.asList(toks))));
    }
    
    toks = RiTa.tokenize("The boy, dressed in red, ate an apple.!?");
    for (int i = 0; i < toks.length * 2; i++) {
      ok(Arrays.asList(toks).contains(RiTa.randomItem(Arrays.asList(toks))));
    }
  }

  @Test
  public void testRandomItemCollection()
  {
    String[] toks = RiTa.tokenize("The boy, dressed in red, ate an apple.!?");
    Collection<String> collection = new ArrayList<String>();
    for (int i = 0; i < toks.length; i++) {
      collection.add(toks[i]);
    }
    for (int i = 0; i < toks.length * 2; i++) {
      ok(Arrays.asList(toks).contains(RiTa.randomItem(collection)));
    }
    
    toks = RiTa.tokenize("The quick brown fox jumps over the lazy dog.");
    collection = new ArrayList<String>();
    for (int i = 0; i < toks.length; i++) {
      collection.add(toks[i]);
    }
    for (int i = 0; i < toks.length * 2; i++) {
      ok(Arrays.asList(toks).contains(RiTa.randomItem(collection)));
    }
  }

  @Test
  public void testRandomItemObjectArray()
  {
    String[] toks = RiTa.tokenize("The boy, dressed in red, ate an apple.!?");
    for (int i = 0; i < toks.length * 2; i++) {
      ok(Arrays.asList(toks).contains(RiTa.randomItem(toks)));
    }

    toks = RiTa.tokenize("The quick brown fox jumps over the lazy dog.");
    for (int i = 0; i < toks.length * 2; i++) {
      ok(Arrays.asList(toks).contains(RiTa.randomItem(toks)));
    }
  }

  @Test
  public void testRandomFloat()
  {
    // float random(float max)
    float answer = RiTa.random(12.3f);
    ok(answer >= 0);
    float answer2 = RiTa.random(12.3f);
    ok(answer2 < 12.3);

    answer = RiTa.random(1.1f);
    ok(answer >= 0);
    answer2 = RiTa.random(1.1f);
    ok(answer2 < 1.1);

    answer = RiTa.random(1.2f);
    ok(answer >= 0);
    answer2 = RiTa.random(1.2f);
    ok(answer2 < 1.2);

    answer = RiTa.random(0f);
    ok(answer >= 0);
    answer2 = RiTa.random(0f);
    ok(answer2 <= 0);

  }

  @Test
  public void testRandomFloatFloat()
  {
    // float random(float min, float max)

    float answer = RiTa.random(3.4f, 3.6f);
    ok(answer >= 3.4);
    float answer2 = RiTa.random(3.4f, 3.6f);
    ok(answer2 < 3.6);

    answer = RiTa.random(1.1f, 1.2f);
    ok(answer >= 1.1);
    answer2 = RiTa.random(1.1f, 1.2f);
    ok(answer2 < 1.2);

    answer = RiTa.random(1.1f, 1.3f);
    ok(answer >= 1.1);
    answer2 = RiTa.random(1.1f, 1.3f);
    ok(answer2 < 1.3);

    answer = RiTa.random(0f, 0f);
    ok(answer == 0);

    answer = RiTa.random(5.1f, 1.1f); //TODO   "min > max"
    ok(answer >= 1.1f);
    answer2 = RiTa.random(0f, 0f);
    ok(answer2 < 5.1f);
  }

  @Test
  public void testRandom()
  {
    // float random()

    float answer = RiTa.random();
    ok(answer >= 0);
    float answer2 = RiTa.random();
    ok(answer2 < 1);
  }
   
  @Test
  public void testMinEditDistanceString()
  {
    String str1 = "The dog", str2 = "The cat";
    equal(RiTa.minEditDistance(str1, str2, false), 3);
    equal(RiTa.minEditDistance(str1, str2, true), (float) 3 / 7);
    
    str1 = "fefnction"; str2 = "faunctional";
    equal(RiTa.minEditDistance(str1, str2, false), 4);
    equal(RiTa.minEditDistance(str1, str2, true), (float) 4 / 11);
    
    str1 = "intention"; str2 = "execution";
    equal(RiTa.minEditDistance(str1, str2, false), 5);
    equal(RiTa.minEditDistance(str1, str2, true), (float) 5 / 9);

    str1 = "The dog"; str2 = "";
    equal(RiTa.minEditDistance(str1, str2, false), 7);
    equal(RiTa.minEditDistance(str1, str2, true), 1);
  }
  
  @Test
  public void testMinEditDistanceArray()
  {
    String[] arr1 = {"The", "dog", "ate"},
      arr2 = {"The", "cat", "ate"};
    equal(RiTa.minEditDistance(arr1, arr2, false), 1);
    equal(RiTa.minEditDistance(arr1, arr2, true), (float) 1 / 3);

    arr1 = new String[]{"The", "dog", "ate"};
    arr2 = new String[0];
    equal(RiTa.minEditDistance(arr1, arr2, false), 3);
    equal(RiTa.minEditDistance(arr1, arr2, true), 1);
    
    arr1 = new String[]{"fefnction", "intention", "ate"};
    arr2 = new String[]{"faunctional", "execution", "ate"};
    equal(RiTa.minEditDistance(arr1, arr2, false), 2);
    equal(RiTa.minEditDistance(arr1, arr2, true), (float) 2 / 3);
  }
  
  /*@Test
  public void testTimer() // failing in travis
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
        
    final EventListener el = new EventListener();
    RiTa.timer(el, .1f);
    try
    {
      Thread.sleep(500); // wait for it...
    }
    catch (InterruptedException e)
    {
    }

    ok(el.i ==5);
  }*/
  
  /*@Test
  public void testPauseTimer() // failing in travis
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
    
    final EventListener el = new EventListener();
    int id = RiTa.timer(el, .1f);
    try
    {
      Thread.sleep(300); // wait for it...
    }
    catch (InterruptedException e)
    {
    }
    RiTa.pauseTimer(id, .3f);
    try
    {
      Thread.sleep(600); // wait for it...
    }
    catch (InterruptedException e)
    {
    }
    //System.out.println(el.i);
    ok(el.i ==6);
  }*/
  
  
  /*@Test
   public void testStopTimer() // failing in travis
  {
    if (!REMOTE_TESTING) {
      ok("skip for remote testing");
      return;
    }
    final EventListener el = new EventListener();
    int id = RiTa.timer(el, .1f);
    try
    {
      Thread.sleep(500); // wait for it...
    }
    catch (InterruptedException e)
    {
    }
    RiTa.stopTimer(id);
    try
    {
      Thread.sleep(500); // wait for it...
    }
    catch (InterruptedException e)
    {
    }
    //System.out.println(el.i);
    ok(el.i == 5);
  }
  class EventListener {
    int i = 0;
    void onRiTaEvent(RiTaEvent re) { ++i; }
  }*/
}

