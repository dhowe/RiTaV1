package rita.test;

import static org.junit.Assert.fail;
import static rita.support.QUnitStubs.deepEqual;
import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.junit.Test;

import processing.core.PApplet;
import rita.*;
import rita.support.Constants;
import rita.support.Constants.StemmerType;

public class RiTaTest
{  
  @Test
  public void testStart()
  {
    RiTa.start(null);
    RiTa.start(this);
    RiTa.start(new PApplet());
  }
    
  @Test
  public void loadString_RelFile()
  {
    String s = RiTa.loadString("kafka.txt");
    ok(s != null && s.length() > 100000);
  }
  
  @Test
  public void loadString_AbsFile()
  {
    String s = RiTa.loadString("/Library/WebServer/Documents/testfiles/kafka.txt");
    ok(s != null && s.length() > 100000);
  }
  
  @Test
  public void loadString_Url()
  {
    String s = RiTa.loadString("http://localhost/testfiles/kafka.txt");
    ok(s != null && s.length() > 100000);
  }
  
  @Test
  public void loadString_FileAsUrl()
  {
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

    ok(RiTa.isQuestion("Are you done?")); // if "is" is true, "Are" should
    // also be True (NICE!)

    ok(RiTa.isQuestion("what is  this?")); // extra space
    ok(RiTa.isQuestion(" what is this? ")); // extra space
    ok(RiTa.isQuestion("what is   this?")); // extra double space
    ok(RiTa.isQuestion("what    is  this?")); // extra tab
    ok(RiTa.isQuestion("what is this? , where is that?"));
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
    ok(RiTa.isW_Question("how is it?"));
    ok(RiTa.isW_Question("will is it."));
    ok(RiTa.isW_Question("Where is it?"));
    ok(RiTa.isW_Question("How is it."));

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

    output = new String[] {};
    input = "\"The boy went fishing.\", he said. Then he went away.";
    expected = new String[] { "\"The boy went fishing.\", he said.", "Then he went away." };
    output = RiTa.splitSentences(input);
    deepEqual(output, expected);

    expected = new String[] {};
    output = new String[] {};
    input = "The dog";
    output = RiTa.splitSentences(input);
    expected = new String[] { input };
    deepEqual(output, expected);

    expected = new String[] {};
    output = new String[] {};
    input = "I guess the dog ate the baby.";
    output = RiTa.splitSentences(input);
    expected = new String[] { input };
    deepEqual(output, expected);

    expected = new String[] {};
    output = new String[] {};
    input = "Oh my god, the dog ate the baby!";
    output = RiTa.splitSentences(input);
    expected = new String[] { input };
    deepEqual(output, expected);

    expected = new String[] {};
    output = new String[] {};
    input = "Which dog ate the baby?";
    output = RiTa.splitSentences(input);
    expected = new String[] { input };
    deepEqual(output, expected);

    expected = new String[] {};
    output = new String[] {};
    input = "'Yes, it was a dog that ate the baby', he said.";
    output = RiTa.splitSentences(input);
    expected = new String[] { "\'Yes, it was a dog that ate the baby\', he said." };
    deepEqual(output, expected);

    // always do these three
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
          "A simple sentence.","(that's why this is our place).",
          "The boy, dressed in red, ate an apple.",
          "Dr. Chan is talking slowly with Mr. Cheng, and they're friends.",
          "The boy screamed, 'Where is my apple?'",
        //  "The boy screamed, \"Where is my apple?\"",
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
  }

  @Test
  public void testUntokenize()
  {
    equal(RiTa.untokenize(new String[0]), "");

    String expected = "The boy, dressed in red -- ate an apple.";
    String[] input = { "The", "boy", ",", "dressed", "in", "red", "--", "ate", "an",
        "apple", "." };
    String output = RiTa.untokenize(input);
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
/*    System.out.println(expected);
    System.out.println(output);*/
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
    equal(5.656854152679443, RiTa.distance(0, 0, 4, 4)); // 5.656854249492381
                                                         // inRitaJS
    equal(5.099019527435303, RiTa.distance(3, 3, 8, 4)); // 5.0990195135927845
                                                         // in RitaJS
  }

  @Test
  public void testGetPhonemesString()
  {
    String txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
    String result = RiTa.getPhonemes(txt);
    String answer = "dh-ax d-ao-g r-ae-n f-ae-s-t-er dh-ae-n dh-ax ah-dh-er d-ao-g . b-ah-t dh-ax ah-dh-er d-ao-g w-aa-z p-r-ih-t-iy-er .";
    //System.out.println("getPhonemes: " + result);
    equal(result, answer);
    
    result = RiTa.getPhonemes("The");
    answer = "dh-ax";
    equal(result, answer);

    result = RiTa.getPhonemes("The.");
    answer = "dh-ax .";
    equal(result, answer);

    result = RiTa.getPhonemes("The boy jumped over the wild dog.");
    answer = "dh-ax b-oy jh-ah-m-p-t ow-v-er dh-ax w-ay-l-d d-ao-g .";
    equal(result, answer);

    result = RiTa.getPhonemes("The boy ran to the store.");
    answer = "dh-ax b-oy r-ae-n t-uw dh-ax s-t-ao-r .";
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
    String answer = "dh-ax";
    equal(result, answer);

    input = new String[] { "The." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ax .";
    equal(result, answer);

    input = new String[] { "The", "boy", "jumped", "over", "the", "wild", "dog." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ax b-oy jh-ah-m-p-t ow-v-er dh-ax w-ay-l-d d-ao-g .";
    equal(result, answer);

    input = new String[] { "The boy ran to the store." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ax b-oy r-ae-n t-uw dh-ax s-t-ao-r .";
    equal(result, answer);

    input = new String[] { "The dog ran faster than the other dog.",
        "But the other dog was prettier." };
    result = RiTa.getPhonemes(input);
    answer = "dh-ax d-ao-g r-ae-n f-ae-s-t-er dh-ae-n dh-ax ah-dh-er d-ao-g . b-ah-t dh-ax ah-dh-er d-ao-g w-aa-z p-r-ih-t-iy-er .";
    //System.out.println("getPhonemes(array)" + result);
    equal(result, answer);

    input = new String[] { "" };
    result = RiTa.getPhonemes(input);
    answer = "";
    equal(result, answer);
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
    //System.out.println("getStresses" + result);
    equal(result, answer);

    result = RiTa.getStresses("to preSENT, to exPORT, to deCIDE, to beGIN");
    answer = "1 0/1 , 1 0/1 , 1 0/1 , 1 0/1";
    equal(result, answer);

    result = RiTa.getStresses("to present, to export, to decide, to begin");
    answer = "1 0/1 , 1 0/1 , 1 0/1 , 1 0/1";
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
    answer = "1 0/1 , 1 0/1 , 1 0/1 , 1 0/1";
    equal(result, answer);

    input = new String[] { "to present, to export, to decide, to begin" };
    result = RiTa.getStresses(input);
    answer = "1 0/1 , 1 0/1 , 1 0/1 , 1 0/1";
    equal(result, answer);

    input = new String[] { "The dog ran faster than the other dog.",
        "But the other dog was prettier." };
    result = RiTa.getStresses(input);
    answer = "0 1 1 1/0 1 0 1/0 1 . 1 0 1/0 1 1 1/0/0 .";
    //System.out.println("getStresses(array)" + result);
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
    String answer = "dh-ax d-ao-g r-ae-n f-ae-s/t-er dh-ae-n dh-ax ah-dh/er d-ao-g . b-ah-t dh-ax ah-dh/er d-ao-g w-aa-z p-r-ih-t/iy/er .";
    equal(result, answer);

    txt = "The emperor had no clothes on.";
    result = RiTa.getSyllables(txt);
    answer = "dh-ax eh-m-p/er/er hh-ae-d n-ow k-l-ow-dh-z aa-n .";
    equal(result, answer);

    txt = "The Laggin Dragon";
    result = RiTa.getSyllables(txt);
    answer = "dh-ax l-ae/g-ih-n d-r-ae-g/ax-n";
    equal(result, answer);
    
    txt = "the laggin dragon";
    result = RiTa.getSyllables(txt);
    //System.out.println(result);
    answer = "dh-ax l-ae/g-ih-n d-r-ae-g/ax-n";
    equal(result, answer);

    result = RiTa.getSyllables("@#$%&*()");
    answer = "@ # $ % & * ( )";
    //System.out.println(result);
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
    String answer = "dh-ax d-ao-g r-ae-n f-ae-s/t-er dh-ae-n dh-ax ah-dh/er d-ao-g . b-ah-t dh-ax ah-dh/er d-ao-g w-aa-z p-r-ih-t/iy/er .";
    equal(result, answer);

    txt = new String[] { "The", "emperor", "had", "no", "clothes", "on." };
    result = RiTa.getSyllables(txt);
    answer = "dh-ax eh-m-p/er/er hh-ae-d n-ow k-l-ow-dh-z aa-n .";
    equal(result, answer);

    txt = new String[] { "The", "Laggin", "Dragon" };
    result = RiTa.getSyllables(txt);
    //System.out.println(result);
    answer = "dh-ax l-ae/g-ih-n d-r-ae-g/ax-n";
    equal(result, answer);
    
    txt = new String[] { "the", "laggin", "dragon" };
    result = RiTa.getSyllables(txt);
    //System.out.println(result);
    answer = "dh-ax l-ae/g-ih-n d-r-ae-g/ax-n";
    equal(result, answer);

    txt = new String[] { "@#", "$%", "&*", "()" };
    result = RiTa.getSyllables(txt);
    answer = "@ # $ % & * ( )";
    System.out.println(result);
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
    
    resultArr = RiTa.getPosTags("Dave dances");
    answerArr = new String[] { "nnp", "vbz"};
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
    
    resultArr = RiTa.getPosTags("assenting"); // added to dict
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
    
    resultArr = RiTa.getPosTags("Dave");
    answerArr = new String[] {"nnp"};
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
    String[] result = RiTa.getPosTags("asfaasd", true); // TODO CHECK ANSWER
    String[] answer = new String[] { "n" };
    deepEqual(result, answer);

    result = RiTa.getPosTags("asfaasd", false);
    answer = new String[] { "nn" };
    deepEqual(result, answer);
    
    result = RiTa.getPosTags("testing", false);
    System.out.println(Arrays.asList(result));
    answer = new String[] { "vbg" };
    deepEqual(result, answer);
    
    result = RiTa.getPosTags("testy", false);
    System.out.println(Arrays.asList(result));
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

    result = RiTa.getPosTags("There is a cat.");
    answer = new String[] { "ex", "vbz", "dt", "nn", "." };
    //System.out.println(RiTa.asList(result));
    deepEqual(result, answer); // TODO check result

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
  public void testConjugate()
  {
    Map args = new HashMap();
    args.put("tense", RiTa.PAST_TENSE);
    args.put("number", RiTa.SINGULAR);
    args.put("person", RiTa.FIRST_PERSON);
    
    String c = RiTa.conjugate("swim", args);    
    equal(c, "swam"); // I swam

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

    args = new HashMap();
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

  @Test
  public void testStemString()
  {
    equal(RiTa.stem("cakes"), "cake");

    String[] tests = { "run", "runs", "running" };
    for (int i = 0; i < tests.length; i++)
    {
      equal(RiTa.stem(tests[i]), "run");
    }

    equal(RiTa.stem("gases"), "gase");
    equal(RiTa.stem("buses"), "buse");
    equal(RiTa.stem("happiness"), "happi");
    equal(RiTa.stem("terrible"), "terribl");

    String test = "Stemming is funnier than a bummer";
    String result = "Stem is funnier than a bummer";
    //System.out.println("testStemString: " + RiTa.stem(test));
    equal(RiTa.stem(test), result);
  }

  @Test
  public void testStemStringInt()
  {
    String type = RiTa.PORTER;

    // --------- Porter ---------------

    equal(RiTa.stem("cakes"), "cake");

    String[] tests = { "run", "runs", "running" };
    for (int i = 0; i < tests.length; i++)
    {
      equal(RiTa.stem(tests[i]), "run");
    }

    equal(RiTa.stem("gases"), "gase");
    equal(RiTa.stem("buses"), "buse");
    equal(RiTa.stem("happiness"), "happi");
    equal(RiTa.stem("terrible"), "terribl");

    String test = "Stemming is funnier than a bummer";
    String result = "Stem is funnier than a bummer";
    //System.out.println("testStemString1: " + RiTa.stem(test));

    equal(RiTa.stem(test), result);
    
    // --------- Pling ---------------

    type = RiTa.PLING;

    equal(RiTa.stem("cakes", type), "cake");

    tests = new String[] {"run", "runs" };
    for (int i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i],type), "run");
    }

    equal(RiTa.stem("gases",type), "gas");
    equal(RiTa.stem("buses",type), "bus");
    equal(RiTa.stem("happiness",type), "happiness");
    equal(RiTa.stem("terrible",type), "terrible");
    
    test = "Stemming is funnier than a bummer";
    result = "Stemming is funnier than a bummer";
    //System.out.println("testStemString2: " + RiTa.stem(test,type));
    equal(RiTa.stem(test, type), result);
    
    // --------- Lancaster ---------------
    
    type = RiTa.LANCASTER;


    tests = new String[] {"run", "runs", "running" };
    for (int i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i],type), "run");
    }
    
    equal(RiTa.stem("gases",type), "gas");
    equal(RiTa.stem("buses",type), "bus");
    equal(RiTa.stem("happiness",type), "happy");
    equal(RiTa.stem("terrible",type), "terr");

    test = "Stemming is funnier than a bummer says the sushi loving computer";
    result = "stem is funny than a bum say the sush lov comput";
    //System.out.println("testStemString3: " + RiTa.stem(test));

    equal(RiTa.stem(test,type), result);
    equal(RiTa.stem("cakes", type), "cak");
  }

  @Test
  public void testPluralize()
  {
    //System.out.println(RiTa.stem("dogs"));
    
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
    //System.out.println("pluralize" + RiTa.pluralize("series"));
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
  }

  @Test
  public void testSingularize()
  {
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
    //System.out.println("RiTa.singularize :" + RiTa.singularize("taxis"));
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
  public void testTimer()
  {
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
  }
  
  @Test
  public void testPauseTimer()
  {
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
  }
  @Test
  public void testStopTimer()
  {
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
  }
}

