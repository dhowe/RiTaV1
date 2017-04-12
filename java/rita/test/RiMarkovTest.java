package rita.test;

import static org.junit.Assert.fail;
import static rita.support.QUnitStubs.deepEqual;
import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import rita.*;


/*
 * TODO:
 *    loadTokens() with generateSents=true ??
 */
public class RiMarkovTest
{
  
  static {
    RiTa.SILENT = true;
  }
  
  String sample = "One reason people lie is to achieve personal power. Achieving personal power is helpful for one who pretends to be more confident than he really is. For example, one of my friends threw a party at his house last month. He asked me to come to his party and bring a date. However, I did not have a girlfriend. One of my other friends, who had a date to go to the party with, asked me about my date. I did not want to be embarrassed, so I claimed that I had a lot of work to do. I said I could easily find a date even better than his if I wanted to. I also told him that his date was ugly. I achieved power to help me feel confident; however, I embarrassed my friend and his date. Although this lie helped me at the time, since then it has made me look down on myself.",
      SP = " ", E = " ";

  String sample2 = "One reason people lie is to achieve personal power. "
      + "Achieving personal power is helpful for one who pretends to "
      + "be more confident than he really is. For example, one of my "
      + "friends threw a party at his house last month. He asked me to "
      + "come to his party and bring a date. However, I did not have a "
      + "girlfriend. One of my other friends, who had a date to go to the "
      + "party with, asked me about my date. I did not want to be embarrassed, "
      + "so I claimed that I had a lot of work to do. I said I could easily find"
      + " a date even better than his if I wanted to. I also told him that his "
      + "date was ugly. I achieved power to help me feel confident; however, I "
      + "embarrassed my friend and his date. Although this lie helped me at the "
      + "time, since then it has made me look down on myself. After all, I did "
      + "occasionally want to be embarrassed.";

  @Test
  public void testRiMarkovInt()
  {
    ok(new RiMarkov(4));
    ok(new RiMarkov(3));

    
    try { new RiMarkov(-1); ok(false);} catch (Exception e) { ok(e); }
  }

  @Test
  public void testGetN()
  {
    RiMarkov rm;
    for (int i = 1; i < 10; i++)
    {
      rm = new RiMarkov(i);
      equal(rm.getN(), i);
    }
  }      
  
  @Test
  public void testLoadFromFile()
  {
    RiMarkov rm1 = new RiMarkov(4);
    rm1.loadFrom("kafka.txt");
    String[] sents = rm1.generateSentences(100);
    ok(sents.length==100);
    
    for (int j = 0; j < sents.length; j++) {
      //System.out.println(j+") "+sents[j]);
      String[] words = sents[j].split(" ");
      ok(!RiTa.isAbbreviation(words[words.length-1]));
      ok(sents[j]);
    }
  }
  
  @Test
  public void testLoadFromFileMulti()
  {
    RiMarkov rm1 = new RiMarkov(3);
    rm1.loadFrom(new String[]{"kafka.txt","wittgenstein.txt"});
    //System.out.println("size:"+rm1.size());
    ok(rm1.size() > 21000);
    String[] sents = rm1.generateSentences(100);
    ok(sents.length==100);
    
    for (int j = 0; j < sents.length; j++) {
      //System.out.println(j+") "+sents[j]);
      String[] words = sents[j].split(" ");
      ok(!RiTa.isAbbreviation(words[words.length-1]));
      ok(sents[j]);
    }
  }

  
  public void testLoadFromUrl() // Note: will fail without network
  {
    RiMarkov rm = new RiMarkov(4);
    rm.loadFrom("http://rednoise.org/testfiles/kafka-short.txt");
    String[] sents = rm.generateSentences(10);
    ok(sents.length==10);
    
    for (int j = 0; j < sents.length; j++) {
      //System.out.println(j+") "+sents[j]);
      String[] words = sents[j].split(" ");
      ok(!RiTa.isAbbreviation(words[words.length-1]));
      ok(sents[j]);
    }
  }

  public void testLoadFromUrl2() // skipped
  {
    RiMarkov rm1 = new RiMarkov(4);
    try
    {
      rm1.loadFrom(new URL("http://rednoise.org/testfiles/kafka.txt"));
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    String[] sents = rm1.generateSentences(100);
    ok(sents.length==100);
    
    for (int j = 0; j < sents.length; j++) {
      //System.out.println(j+") "+sents[j]);
      String[] words = sents[j].split(" ");
      ok(!RiTa.isAbbreviation(words[words.length-1]));
      ok(sents[j]);
    }
  }
  
  @Test
  public void testGenerateSentencesInt()
  {
    boolean dbug = false;
    
    if (dbug)System.out.println();

    RiMarkov rm1 = new RiMarkov(4, true, true);
    rm1.loadText(sample);
    for (int i = 0; i < 3; i++) {
      String[] sents = rm1.generateSentences(3);
      ok(sents.length==3);
      for (int j = 0; j < sents.length; j++) {
        if (dbug)System.out.println(i+"."+j+") "+sents[j]);
        ok(sents[j]);
      }
    }
    
    if (dbug)System.out.println();
    
    RiMarkov r = new RiMarkov(4, true, false);
    r.loadText(sample2);
    for (int i = 0; i < 10; i++)
    {
      String s = r.generateSentences(1)[0];
      if (dbug)System.out.println(i+") '"+s+"'");
      ok(s);
    }
    

    if (dbug)System.out.println();
    
    RiMarkov rm = new RiMarkov(4, true, true);
    rm.loadText(sample2);
    
    for (int i = 0; i < 10; i++)
    {
      String s = rm.generateSentences(1)[0];
      if (dbug)System.out.println(i+") '"+s+"'");
      ok(s);
    }
    
    try { new RiMarkov(4, false).generateSentences(10); 
      ok(false); }catch (Exception e) { ok(e); }
  }
  
  @Test
  public void testRecognizeSentences()
  {
    RiMarkov rm = new RiMarkov(4, false);
    ok(!rm.sentenceAware());
    rm = new RiMarkov(4);
    ok(rm.sentenceAware());
    rm = new RiMarkov(4, true);
    ok(rm.sentenceAware());
  }
  
  @Test
  public void testGenerateTokens()
  {
    RiMarkov rm = new RiMarkov(4);
    rm.loadTokens(RiTa.tokenize(sample));
    for (int i = 0; i < 10; i++)
    {
      String[] arr = rm.generateTokens(4);
      String res = RiTa.untokenize(arr);
      //System.out.println(i+") "+res);
      ok(sample.contains(res));
      equal(arr.length, 4);
    }
    
    rm = new RiMarkov(3);
    rm.loadTokens(RiTa.tokenize(sample));
    for (int i = 0; i < 10; i++)
    {
      String[] arr = rm.generateTokens(4);
      String res = RiTa.untokenize(arr);
      String[] front = new String[3];
      System.arraycopy(arr, 0, front, 0, 3);
      String[] back = new String[3];
      System.arraycopy(arr, 0, front, 0, 3);
      System.arraycopy(arr, 1, back, 0, 3);
      String frontStr = RiTa.untokenize(front);
      String backStr = RiTa.untokenize(back);
      //System.out.println(i+") "+res +" -> "+sample.contains(frontStr)+" && "+sample.contains(backStr));
      ok(sample.contains(frontStr));
      ok(sample.contains(backStr));
      equal(arr.length, 4);
    }
  }

  @Test
  public void testGenerateUntilString()
  {
    RiMarkov rm = new RiMarkov(3);
    rm.loadTokens(RiTa.tokenize(sample2));
    for (int i = 0; i < 10; i++)
    {
      String[] s = rm.generateUntil("power");
      String sent = RiTa.untokenize(s);
      //System.out.println(i+") "+sent);
      //ok(sent.endsWith("power"));
    }
  }

  @Test
  public void testGenerateUntilStringInt()
  {
    RiMarkov rm = new RiMarkov(3);
    rm.loadTokens(RiTa.tokenize(sample2));
    for (int i = 0; i < 10; i++)
    {
      String[] s = rm.generateUntil("power",10);
      ok(s.length >= 4);
      String sent = RiTa.untokenize(s);
      //System.out.println(i+") "+sent);
      ok(sent.endsWith("power"));
    }
  }

  @Test
  public void testGenerateUntilStringIntInt()
  {
    RiMarkov rm = new RiMarkov(4,false);
    rm.loadTokens(RiTa.tokenize(sample));

    for (int i = 0; i < 10; i++)
    {
      String[] arr = rm.generateUntil("[\\.\\?\\!]", 4, 9);
      String res = RiTa.untokenize(arr);

      ok(arr.length >= 4 && arr.length <= 20);

//System.out.println(i+") "+res);

      int n = rm.getN();
      String combine, partial[];
      for (int j = 0; j < arr.length - n; j++)
      {
        partial = new String[n];
        System.arraycopy(arr, j, partial, 0, n); 
        combine = RiTa.untokenize(partial);

//System.out.println("  "+i+"."+j+"] "+combine);

        ok(sample.indexOf(combine) > -1);
      }
    }

    String[] words = rm.generateUntil("_NOT_IN_TEXT_", 4, 20);
    ok(words.length==0);
  }

  @Test
  public void testGetCompletionsStringArray()
  {
    RiMarkov rm = new RiMarkov(4);
    rm.loadTokens(RiTa.tokenize(sample));

    String[] res = rm.getCompletions("people lie is".split(" "));
    deepEqual(res, new String[] { "to" });

    res = rm.getCompletions("One reason people lie is".split(" "));
    deepEqual(res, new String[] { "to" });

    res = rm.getCompletions("personal power".split(" "));
    deepEqual(res, new String[] { ".", "is" });

    res = rm.getCompletions(new String[] { "to", "be", "more" });
    deepEqual(res, new String[] { "confident" });

    res = rm.getCompletions(new String[] { "I" }); // testing the sort
    String[] expec = { "did", "achieved", "also", "claimed", "could", "embarrassed",
        "had", "said", "wanted" };
    deepEqual(res, expec);

    res = rm.getCompletions(new String[] { "XXX" });
    //System.out.println("getCompletions :" + res.length);
    deepEqual(res, new String[0]);
  }

  @Test
  public void testGetCompletionsStringArrayStringArray()
  {
    RiMarkov rm = new RiMarkov(4);
    rm.loadTokens(RiTa.tokenize(sample2));

    String[] res = new String[] {};
    res = rm.getCompletions(new String[] { "I" }, new String[] { "not" });
    deepEqual(res, new String[] { "did" });

    res = rm.getCompletions(new String[] { "achieve" }, new String[] { "power" });
    deepEqual(res, new String[] { "personal" });

    res = rm.getCompletions(new String[] { "to", "achieve" }, new String[] { "power" });
    deepEqual(res, new String[] { "personal" });

    res = rm.getCompletions(new String[] { "achieve" }, new String[] { "power" });
    deepEqual(res, new String[] { "personal" });

    res = rm.getCompletions(new String[] { "I", "did" });
    deepEqual(res, new String[] { "not", "occasionally" }); // sort

    // ////////////////////
    res = rm.getCompletions(new String[] { "I", "did" }, new String[] { "want" });
    deepEqual(res, new String[] { "not", "occasionally" });
  }

  @Test
  public void testGetProbabilities()
  {
    /*
     * //single RiMarkov rm = new RiMarkov(3);
     * rm.loadTokens(RiTa.tokenize(sample));
     * 
     * String[] checks = {"reason", "people", "personal", "the", "is"}; expec =
     * [ { people:1.0}, {lie:1.0},{power:1.0},{time:0.5,
     * party:0.5},{to:0.33333334, '.':0.33333334, helpful:0.33333334} ];
     * 
     * var keys = Object.keys(expec);
     * 
     * for ( var i = 0; i < checks.length; i++) {
     * 
     * var res = rm.getProbabilities(checks[i]); console.log(checks[i]+":");
     * 
     * equal(Object.keys(res).length, Object.keys(expec[i]).length);
     * 
     * var answer = []; for (var key in res) { answer.push(key);
     * console.log("  "+key+" -> "+res[key]); } deepEqual(Object.keys(res),
     * answer); }
     * 
     * var res = rm.getProbabilities("XXX"); deepEqual(res,{});
     */

    // array
    RiMarkov rm2 = new RiMarkov(4);
    rm2.loadTokens(RiTa.tokenize(sample2));

    Map res2 = rm2.getProbabilities("the".split(" "));
    Map expec = new HashMap();
    expec.put("time", 0.5f);
    expec.put("party", 0.5f);
    //System.out.println("testGetProbabilities res2  " + res2.getClass()+" "+res2);
    //System.out.println("testGetProbabilities expec " + expec.getClass()+" "+expec);
    deepEqual(res2, expec);

    res2 = new HashMap();
    expec = new HashMap();
    res2 = rm2.getProbabilities("people lie is".split(" "));
    expec.put("to", 1.0f);
    deepEqual(res2, expec);

    res2 = new HashMap();
    expec = new HashMap();
    res2 = rm2.getProbabilities(new String[] { "is" });
    expec.put("to", 0.3333333333333333f);
    expec.put(".", 0.3333333333333333f);
    expec.put("helpful", 0.3333333333333333f);
    deepEqual(res2, expec);

    expec = new HashMap();
    res2 = rm2.getProbabilities("personal power".split(" "));
    expec.put(".",0.5f);
    expec.put("is", 0.5f);
    deepEqual(res2, expec);

    expec = new HashMap();
    res2 = rm2.getProbabilities(new String[] { "to", "be", "more" });
    expec.put("confident", 1.0f);
    deepEqual(res2, expec);


    expec = new HashMap();
    res2 = rm2.getProbabilities(new String[] { "XXX" });
    
    deepEqual(res2, expec);
    expec = new HashMap();
    res2 = rm2.getProbabilities(new String[] { "personal", "XXX" });
    deepEqual(res2, expec);

    expec = new HashMap();
    res2 = rm2.getProbabilities(new String[] { "I", "did" });
    expec.put("not", 0.6666666666666666f);
    expec.put("occasionally", 0.3333333333333333f);
    deepEqual(res2, expec);

  }

  @Test
  public void testGetProbability()
  {
    // single
    /*
     * String[] tokens = RiTa.tokenize("the dog ate the boy the"); RiMarkov rm =
     * new RiMarkov(3); rm.loadTokens(tokens); //rm.print();
     * 
     * equal( rm.getProbability("the"), .5); equal( rm.getProbability"dog"),
     * 1/6); equal( rm.getProbability("cat"), 0);
     * 
     * tokens = RiTa.tokenize("the dog ate the boy that the dog found."); rm =
     * new RiMarkov(3); rm.loadTokens(tokens); //rm.print();
     * 
     * equal( rm.getProbability("the"), .3); equal( rm.getProbability("dog"),
     * .2); equal( rm.getProbability("cat"), 0);
     * 
     * rm = new RiMarkov(3); rm.loadTokens(RiTa.tokenize(sample)); equal(
     * rm.getProbability("power"), 0.017045454545454544);
     */
    // array
    RiMarkov rm = new RiMarkov(3);
    rm.loadTokens(RiTa.tokenize(sample));

    String[] check = "personal power is".split(SP);
    equal(rm.getProbability(check), 0.3333333432674408f);

    check = "personal powXer is".split(SP);
    //System.out.println("getProbability" + rm.getProbability(check));
    equal(rm.getProbability(check), 0f);

    check = "someone who pretends".split(SP);
    equal(rm.getProbability(check), 1 / 2f);

    equal(rm.getProbability(new String[] {}), 0f);
  }

  @Test
  public void testPrint()
  {
    // TODO: compare to RiTaJS print()
    String[] words = "The dog ate the cat".split(" ");
    RiMarkov rm = new RiMarkov(3);
    rm.loadTokens(words);
    //rm.print();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    rm.print(ps);
    try
    {
      baos.close();
      ok(baos.toString("UTF8").contains("ROOT {"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    equal(rm.getProbability(new String[] { "The" }), 0.2f);
  }

  @Test
  public void testLoadTextStringString()
  {
    // TODO: Test all parameter combos !

    {
      String words = "The dog ate the cat";

      RiMarkov rm = new RiMarkov(3, false);
      rm.loadText(words, "\\s+");
      equal(rm.getProbability(new String[] { "The" }), 0.2f);
    }
    
    {
      String words = "the dog ate the cat";

      RiMarkov rm = new RiMarkov(3,false);
      rm.loadText(words, "\\s+");
      equal(rm.getProbability(new String[] { "the" }), 0.4f);
    }
    
    {
      String words = "The dog ate the cat";

      RiMarkov rm = new RiMarkov(3,true);
      rm.loadText(words, "\\s+");
      equal(rm.getProbability(new String[] { "The" }), 0.2f);
    }
    
    {
      String words = "the dog ate the cat";
      RiMarkov rm = new RiMarkov(3,true);
      rm.loadText(words, "\\s+");
      //rm.print();
      equal(rm.getProbability(new String[] { "the" }), 0.4f);
      // TODO: broken with loadSentences()
    }
    
    {
      String words = "The dog ate the cat.";

      RiMarkov rm = new RiMarkov(3,false);
      rm.loadText(words, "\\s+");
      equal(rm.getProbability(new String[] { "The" }), 0.2f);
    }
    
    {
      String words = "the dog ate the cat.";

      RiMarkov rm = new RiMarkov(3,false);
      rm.loadText(words, "\\s+");
      equal(rm.getProbability(new String[] { "the" }), 0.4f);
    }
    
    {
      String words = "The dog ate the cat.";

      RiMarkov rm = new RiMarkov(3,true);
      rm.loadText(words, "\\s+");
      //rm.print();

      equal(rm.getProbability(new String[] { "The" }), 0.2f);
    }
    
    {
      String words = "the dog ate the cat.";
      RiMarkov rm = new RiMarkov(3,true);
      rm.loadText(words, "\\s+");
      equal(rm.getProbability(new String[] { "the" }), 0.4f);
      // TODO: broken with loadSentences()
    }
  }

  @Test
  public void testLoadTextString()
  {
    {
      String words = "The dog ate the cat";

      RiMarkov rm = new RiMarkov(3,false);
      rm.loadText(words);
      //rm.print();
      equal(rm.getProbability(new String[] { "The" }), 0.2f);
    }
    
    {
      String words = "the dog ate the cat";

      RiMarkov rm = new RiMarkov(3,false);
      rm.loadText(words);
      //rm.print();
      equal(rm.getProbability(new String[] { "the" }), 0.4f);
    }
    
    {
      String words = "The dog ate the cat.";

      RiMarkov rm = new RiMarkov(3,true);
      rm.loadText(words);
      //rm.print();
      equal(rm.getProbability(new String[] { "The" }), 1/6f);
    }
    
    {
      String words = "The dog ate the cat. A boy ate the hat.";
      RiMarkov rm = new RiMarkov(3,true);
      rm.loadText(words);
      equal(rm.getProbability(new String[] { "the" }), 1/6f);
    }
  }

  @Test
  public void testSize()
  {
    String[] tokens = RiTa.tokenize(sample);
    RiMarkov rm = new RiMarkov(3, false);
    rm.loadTokens(tokens);
    ok(rm.size() == tokens.length);
    
    rm = new RiMarkov(3, true);
    rm.loadTokens(tokens);
    ok(rm.size() == tokens.length);
    
    String[] sents = RiTa.splitSentences(sample);
    rm = new RiMarkov(3);
    rm.loadSentences(sents);
    ok(rm.size() == tokens.length);
  }

  @Test
  public void testLoadTextStringInt()
  {
    {
      String words = "The dog ate the cat";

      RiMarkov rm = new RiMarkov(3,false);
      rm.sentenceAware();
      rm.loadText(words);
      rm.loadText("leopard",5);
      //rm.print();
      equal(rm.getProbability(new String[] { "The" }), 0.1f);
    }
    
    {
      String words = "the dog ate the cat";

      RiMarkov rm = new RiMarkov(3,false);
      rm.loadText(words);
      rm.loadText("leopard",5);

      //rm.print();
      equal(rm.getProbability(new String[] { "the" }), 0.2f);
    }
    
    {
      String words = "The dog ate the cat.";

      RiMarkov rm = new RiMarkov(3,true);
      rm.sentenceAware();
      rm.loadText(words);
      rm.loadText("Bobby ate.",2);
      equal(rm.getProbability(new String[] { "The" }), 1/12f);
    }
    
    {
      String words = "The dog ate The cat.";
      RiMarkov rm = new RiMarkov(3,true);
      rm.sentenceAware();
      rm.loadText(words);
      rm.loadText("Bobby ate.",2);
      //rm.print();

      equal(rm.getProbability(new String[] { "The" }), 1/6f);
    }
  }
  

  @Test
  public void testLoadTextStringIntString()
  {
    {
      String words = "The dog ate the cat";

      RiMarkov rm = new RiMarkov(3,false);
      rm.sentenceAware();
      rm.loadText(words, "\\s+");
      rm.loadText("A leopard got so fat", 2, "\\s+");
      //rm.print();
      equal(rm.getProbability(new String[] { "The" }), 1/15f);
    }
    
    {
      String words = "the dog ate the cat";

      RiMarkov rm = new RiMarkov(3,false);
      rm.loadText(words,"\\s+");
      rm.loadText("leopard",5,"\\s+");

      //rm.print();
      equal(rm.getProbability(new String[] { "the" }), 0.2f);
    }
    
    {
      String words = "The dog ate the white cat.";

      RiMarkov rm = new RiMarkov(3,true);
      rm.sentenceAware();
      rm.loadText(words, "\\s+");
      rm.loadText("Bobby ate it.",2, "\\s+");
      equal(rm.getProbability(new String[] { "The" }), 1/12f);
    }
    
    {
      String words = "The dog ate The white cat.";
      RiMarkov rm = new RiMarkov(3,true);
      rm.sentenceAware();
      rm.loadText(words,"\\s+");
      rm.loadText("Bobby ate it.", 2, "\\s+");
      //rm.print();

      equal(rm.getProbability(new String[] { "The" }), 1/6f);
    } 
  }
  
  @Test
  public void testLoadTokensStringArray()
  {
    String[] tokens = RiTa.tokenize(sample);
    RiMarkov rm = new RiMarkov(3);
    rm.loadTokens(tokens);
    ok(rm.size() == tokens.length);
    
    rm = new RiMarkov(3);
    for (int i = 0; i < 5; i++)
      rm.loadTokens(tokens);
    ok(rm.size() == tokens.length*5);
  }

  @Test
  public void testLoadTokensChars() 
  {
    char[] tokens = sample.toCharArray();
    RiMarkov rm = new RiMarkov(3,false);
    rm.loadTokens(tokens);
    ok(rm.size() == tokens.length);
    
    rm = new RiMarkov(3,false);
    for (int i = 0; i < 5; i++)
      rm.loadTokens(tokens);
    ok(rm.size() == tokens.length*5);
  }

  
  @Test
  public void testLoadTokensStringArrayInt() 
  {
    String[] tokens = RiTa.tokenize(sample);
    String[] tokens2 = RiTa.tokenize(sample2);
    RiMarkov rm = new RiMarkov(3);
    rm.loadTokens(tokens,2);
    rm.loadTokens(tokens2,7);
    ok(rm.size() == tokens.length*2+ tokens2.length*7);
  }
  
}
