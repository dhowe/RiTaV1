package rita.test;

import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import rita.RiGrammar;
import rita.RiLexicon;
import rita.RiTa;
import rita.RiTaException;
import rita.support.YAMLParser;

// TODO: should NOT load lexicon

public class RiGrammarTest
{ 
  private static boolean WITHOUT_YAML = false;

  static String sentenceGrammarJSON = "{ \"<start>\" : \"<noun_phrase> <verb_phrase>.\", \"<noun_phrase>\" : \"<determiner> <noun>\", \"<verb_phrase>\" : \"<verb> | <verb> <noun_phrase> [.1]\", \"<determiner>\" : \"a [.1] | the\", \"<noun>\" : \"woman | man\", \"<verb>\" : \"shoots\" }";
  static String sentenceGrammarYAML = "<start> : <noun_phrase> <verb_phrase>.\n<noun_phrase>: <determiner> <noun>\n<verb_phrase> : <verb> | <verb> <noun_phrase> [.1]\n<noun>: woman | man\n<determiner>: a [.1] | the\n<verb>: shoots";
  static String sentenceGrammarYAML2 = "<start> : <noun_phrase> <verb_phrase>.\n<noun_phrase>: <determiner> <noun>\n<verb_phrase> : \n  - <verb> \n  - <verb> <noun_phrase> [.1]\n<noun>: \n  - woman\n  - man\n<determiner>: \n  - a [.1] \n  - the\n<verb>: shoots";

  static String[] sentenceGrammars = { sentenceGrammarJSON, sentenceGrammarYAML, sentenceGrammarYAML2 };
  static String[] sentenceGrammarFiles = { "sentence1.json", "sentence2.json", "sentence1.yaml", "sentence2.yaml" };
  static String[] haikuGrammarFiles = { "haikuGrammar.json", "haikuGrammar2.json", "haikuGrammar.yaml", "haikuGrammar2.yaml" };

  static {
    
    RiTa.SILENT = true;
    
    if (WITHOUT_YAML) {
      
      YAMLParser.SNAKEYAML = null;
      sentenceGrammars = new String[]{ sentenceGrammarJSON };
      sentenceGrammarFiles = new String[]{ "sentence1.json", "sentence2.json" };
      haikuGrammarFiles = new String[]{ "haikuGrammar.json", "haikuGrammar2.json" };
    }
  }
  
  @Test
  public void testRiGrammar()
  {
    RiGrammar rg = new RiGrammar();
    ok(rg);
  }
  
  @Test
  public void testRiGrammar2()
  {
    for (int j = 0; j < sentenceGrammars.length; j++)
    {
      RiGrammar rg = new RiGrammar(sentenceGrammars[j]);
      ok(!rg.hasRule("<verb"));
      rg.hasRule("<verb");
      for (int i = 0; i < 100; i++)
        rg.expandFrom("<verb_phrase>");
    }
  }
  
  @Test
  public void testSetGrammar()
  {
    for (int j = 0; j < sentenceGrammars.length; j++)
    {
      RiGrammar rg = new RiGrammar();
      rg.load(sentenceGrammars[j]);
      ok(!rg.hasRule("<verb"));
      rg.hasRule("<verb");
      for (int i = 0; i < 100; i++)
      {
        rg.expandFrom("<verb_phrase>");  
      }    
    }
  }
  
  @Test
  public void testRiGrammarString()
  {
    for (int j = 0; j < sentenceGrammars.length; j++)
    {
      RiGrammar rg = new RiGrammar(sentenceGrammars[j]);
      ok(rg.hasRule("<verb>"));
      ok(rg.hasRule("<noun>"));
      ok(!rg.hasRule("adadf"));
    }
  }
  
  @Test
  public void testExpand()
  {
    
    String gr = "<start>: <rule1>\n<rule1>: cat [.4] | dog [.6] | boy [.2]";
    RiGrammar rg1 = new RiGrammar(gr);
    ok(rg1.hasRule("<rule1>"));
  
    int c1 = 0, c2 = 0, c3 = 0;
    for (int i = 0; i < 100; i++)
    {
      String res = rg1.expand();
      
      ok(res.equals("cat") || res.equals("dog") || res.equals("boy"));
      
      if (res.equals("cat"))
        c1++;
      else if (res.equals("dog"))
        c2++;
      else if (res.equals("boy"))
        c3++;
    }
    ok(c1>0 && c2>0 && c3>0); // found all

    //System.out.println(c1+", "+c2+", "+c3);
    /////////////////////////////////////////////////////////////////

    for (int j = 0; j < sentenceGrammarFiles.length; j++)
    {
      RiGrammar rg = new RiGrammar();
      rg.load(RiTa.loadString(sentenceGrammarFiles[j]));
      for (int i = 0; i < 10; i++)
        ok(rg.expand());
    }

    for (int j = 0; j < sentenceGrammars.length; j++)
    {
      RiGrammar rg = new RiGrammar(sentenceGrammars[j]);    
      for (int i = 0; i <10; i++)
        ok(rg.expand());
    }
    
    for (int j = 0; j < haikuGrammarFiles.length; j++)
    {
      RiGrammar rg = new RiGrammar();
      rg.load(RiTa.loadString(haikuGrammarFiles[j]));    
      for (int i = 0; i <10; i++)
        ok(rg.expand());
    }
    
    RiGrammar rg = new RiGrammar();   
    rg.addRule("<start>", "pet");
    equal(rg.expand(), "pet");
  
    rg.addRule("<start>", "pet", 1);
    equal(rg.expand(), "pet");
    rg.addRule("<start>", "pet", 2);
    equal(rg.expand(), "pet");
  
    rg.reset();
    rg.addRule("<start>", "<pet>", 1);
    rg.addRule("<pet>", "dog", 1);
    equal(rg.expand(), "dog");
  
    /////////////////////////////////////////////////////////////////
    
    rg.reset();
    rg.addRule("<start>", "<rule1>", 1);
    rg.addRule("<rule1>", "cat", .4f);
    rg.addRule("<rule1>", "dog", .6f);
    rg.addRule("<rule1>", "boy", .2f);
    ok(rg.hasRule("<rule1>"));
  
    boolean found1 = false, found2 = false, found3 = false;
    for (int i = 0; i < 100; i++)
    {
      String res = rg.expand();
      
      ok(res.equals("cat") || res.equals("dog") || res.equals("boy"));
      
      if (res.equals("cat"))
        found1 = true;
      else if (res.equals("dog"))
        found2 = true;
      else if (res.equals("boy"))
        found3 = true;
    }
    
    ok(found1 && found2 && found3); // found all


    /////////////////////////////////////////////////////////////////
    
    rg.reset();
    rg.addRule("<start>", "<rule1>", 1);
    rg.addRule("<rule1>", "cat | dog | boy");
    ok(rg.hasRule("<rule1>"));
  
    found1 = false; found2 = false; found3 = false;
    for (int i = 0; i < 100; i++)
    {
      String res = rg.expand();
      
      ok(res.equals("cat") || res.equals("dog") || res.equals("boy"));
      
      if (res.equals("cat"))
        found1 = true;
      else if (res.equals("dog"))
        found2 = true;
      else if (res.equals("boy"))
        found3 = true;
    }
    ok(found1 && found2 && found3); // found all
    
    /////////////////////////////////////////////////////////////////
    
    rg.reset();
    rg.addRule("<start>", "pet", 1);
    equal(rg.expand(), "pet");
  
    rg.reset();
    rg.addRule("<start>", "the <pet> ran.", 1);
    rg.addRule("<pet>", "dog", .7f);
    for (int i = 0; i < 10; i++)
      equal(rg.expand(), "the dog ran.");
    
    rg.reset();
    rg.addRule("<start>", "the <pet>.", 1);
    rg.addRule("<pet>", "dog", .7f);
    rg.addRule("<pet>", "cat", .3f);
  
    int d = 0, c = 0;
    for (int i = 0; i < 100; i++)
    {
      String r = rg.expand();
      if (r.equals("the dog."))
        d++;
      if (r.equals("the cat."))
        c++;
    }
    ok(d > 50); // d + ""
    ok(d < 90); // d + ""
    ok(c > 10); // g + ""
    ok(c < 50); // g + ""

  }
  
  @Test
  public void testSpecialChars() {

    RiGrammar rg; String res;
    int len = WITHOUT_YAML ? 1 : 2; // tmp-hack 
    
    String[] s = new String[] { "{ \"<start>\": \"hello &#124; name\" }", "<start> : hello &#124; name" };
    for (int j = 0; j < len; j++)
    {
      rg = new RiGrammar(s[j]);
      res = rg.expand();
      ok(res.equals("hello | name"));
    }

    s = new String[] { "{ \"<start>\": \"hello: name\" }", "<start> : \"hello: name\"" };
    for (int j = 0; j < len; j++)
    {
      rg = new RiGrammar(s[j]);
      res = rg.expand();
      // println("res="+res);
      ok(res.equals("hello: name"));
    }
    
    s = new String[] { "{ \"<start>\": \"&lt;start&gt;\" }", "<start>: \"&lt;start&gt;\"" };
    for (int j = 0; j < len; j++)
    {
      rg = new RiGrammar(s[j]);
      res = rg.expand();
      // println(res);
      ok(res.equals("<start>"));
    }

    s = new String[] { "{ \"<start>\": \"I don&#96;t want it.\" }", "<start>: I don&#96;t want it." };
    for (int j = 0; j < len; j++)
    {
      rg = new RiGrammar(s[j]);
      res = rg.expand();
      // println(res);
      ok(res.equals("I don`t want it."));
    }

    s = new String[] { "{ \"<start>\": \"&#39;I really don&#39;t&#39;\" }", "<start>: \"&#39;I really don&#39;t&#39;\"" };
    for (int j = 0; j < len; j++)
    {
      rg = new RiGrammar(s[j]);
      res = rg.expand();
      ok(res.equals("'I really don't'"));
    }

    s = new String[] { "{ \"<start>\": \"hello | name\" }", "<start> : hello | name"};
    for (int j = 0; j < len; j++)
    {
      rg = new RiGrammar(s[j]);
      for (int i = 0; i < 10; i++)
      {
        res = rg.expand();
        ok(res.equals("hello") || res.equals("name"));
      }
    }
    
    s = new String[] { "{ \"<start>\": \"&#8220;hello!&#8221;\" }" };
    rg = new RiGrammar(s[0]);
    res = rg.expand();
    ok(res.equals("“hello!”"));
  }
    
  @Test
  public void testRiGrammarExec()
  {
    Pattern re = Pattern.compile("(.*?)(`[^`]+?\\(.*?\\);?`)(.*)");

    String str = "`hello()`";
    String[] res = RiGrammar.testExec(re, str);
    res = splice01(res);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");

    // /for ( int i = 0; i < res.length; i++) {
    equal(res, new String[] { "", "`hello()`", "" });

    println("===========================");

    str = "`hello(and)`";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello(and)`", "" });

    println("===========================");

    str = "`hello('and')`";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello('and')`", "" });

    println("===========================");

    str = "`hello(\"and\")`";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello(\"and\")`", "" });

    println("===========================");

    str = "and `hello()` there";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "and ", "`hello()`", " there" });

    println("===========================");

    str = "and `hello()` there `you()`";
    res = RiGrammar.testExec(re, str);
    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "and ", "`hello()`", " there `you()`" });

    println("===========================");

    str = "and `hello()`";
    res = RiGrammar.testExec(re, str);
    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "and ", "`hello()`", "" });

    println("===========================");

    str = "`hello()` there `you()`";
    res = RiGrammar.testExec(re, str);
    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello()`", " there `you()`" });

    println("===========================");

    str = "`hello();`";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello();`", "" });

    println("===========================");

    str = "`hello(and);`";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello(and);`", "" });

    println("===========================");

    str = "`hello('and');`";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello('and');`", "" });

    println("===========================");

    str = "`hello(\"and\");`";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello(\"and\");`", "" });

    println("===========================");

    str = "and `hello();` there";
    res = RiGrammar.testExec(re, str);

    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "and ", "`hello();`", " there" });

    println("===========================");

    str = "and `hello();` there `you();`";
    res = RiGrammar.testExec(re, str);
    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "and ", "`hello();`", " there `you();`" });

    println("===========================");

    str = "and `hello();`";
    res = RiGrammar.testExec(re, str);
    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "and ", "`hello();`", "" });

    println("===========================");

    str = "`hello();` there `you();`";
    res = RiGrammar.testExec(re, str);
    for (int i = 0; i < res.length; i++)
      println("'" + res[i] + "'");
    res = splice01(res);
    equal(res, new String[] { "", "`hello();`", " there `you();`" });
  }


  @Test
  public void testLoadFrom()
  {
    for (int j = 0; j < sentenceGrammarFiles.length; j++)
    {
      RiGrammar rg = new RiGrammar();
      rg.loadFrom(sentenceGrammarFiles[j]);
      ok(!rg.hasRule("{")); // empty
      ok(rg.hasRule("<start>"));
    }
    
    for (int j = 0; j < haikuGrammarFiles.length; j++)
    {
      RiGrammar rg = new RiGrammar(); 
      rg.loadFrom(haikuGrammarFiles[j]);
      
      //rg.print();
      ok(!rg.hasRule("")); // empty
      ok(!rg.hasRule("{")); // empty
      ok(rg.hasRule("<start>"));
      ok(rg.hasRule("<5-line>"));
  
      ok(!rg.hasRule("{")); // empty
      ok(rg.hasRule("<start>"));
      ok(rg.hasRule("<5-line>"));
    }
  }

  @Test
  public void testExpandFromFile()
  {
    for (int j = 0; j < sentenceGrammarFiles.length; j++)
    {
      RiGrammar rg = new RiGrammar();

      rg.load(RiTa.loadString(sentenceGrammarFiles[j]));
      ok(rg.hasRule("<start>"));
      for (int i = 0; i < 10; i++)
      {
        String res = rg.expand();
        ok(res);
      }
    }
  }

  @Test
  public void testExpandFrom()
  {
    RiGrammar rg = new RiGrammar();

    rg.reset();
    rg.addRule("<start>", "<pet>", 1);
    rg.addRule("<pet>", "<bird> | <mammal>", 1);
    rg.addRule("<bird>", "hawk | crow", 1);
    rg.addRule("<mammal>", "dog", 1);

    equal(rg.expandFrom("<mammal>"), "dog");
    
    for (int i = 0; i < 100; i++)
    {
      String res = rg.expandFrom("<bird>");
      println(i+": "+res);
      ok(res.equals("hawk") || res.equals("crow"));
    }
    
    try {
      rg.expandFrom("mammal");
      ok("No exception!!!");
    }
    catch (RiTaException e) {
      ok(e);
    }
  }
  
  @Test
  public void testMultilinesYAML()
  {
    for (int j = 0; j < sentenceGrammarFiles.length; j++)
    {
      if (sentenceGrammarFiles[j].endsWith(".yaml"))
      {
        RiGrammar rg = new RiGrammar();
        rg.load(RiTa.loadString(sentenceGrammarFiles[j]));
        equal(rg.expandFrom("<multiline>"),"This is my very long string that wraps three lines");
      }
    }
  }

  // TODO: Implement this or remove from API

  /*@Test
  public void testExpandWith()
  {

    RiGrammar rg = new RiGrammar();

    rg.reset();

    rg.addRule("<start>", "the <pet> | the <action> of the <pet>", 1);
    rg.addRule("<pet>", "<bird> | <mammal>", 1);
    rg.addRule("<bird>", "hawk | crow | screamer", 1);
    rg.addRule("<mammal>", "dog", 1);
    rg.addRule("<action>", "cries | screams | falls", 1);

    String r = rg.expandWith("screams", "<action>");

    String str = "";
    boolean missed = false;
    for (int i = 0; i < 100; i++)
    {
      r = rg.expandWith("screams", "<action>");
      if (r.indexOf("screams") < 1)
      {
        str = r;
        println("error: " + r);
        missed = true;
      }
    }
    equal(missed, false);

    str = "";
    missed = false;
    for (int i = 0; i < 100; i++)
    {
      r = rg.expandWith("dog", "<pet>");
      if (r.indexOf("dog") < 1)
      {
        str = r;
        println("error: " + r);
        missed = true;
      }
    }
    equal(missed, false);
    equal("MORE TESTS HERE", false);
  }*/

  public void testGetGrammar() // not sure how to test this, as the order is always different
  {
    for (int j = 0; j < sentenceGrammars.length; j++)
    {
      RiGrammar rg = new RiGrammar(sentenceGrammars[j]);

      String s = rg.getGrammar();
      String e = "<start>\n  '<noun_phrase> <verb_phrase>.' [1.0]\n<determiner>\n  'a' [0.1]\n  'the' [1.0]\n<noun_phrase>\n  '<determiner> <noun>' [1.0]\n<verb_phrase>\n  '<verb> <noun_phrase>' [0.1]\n  '<verb>' [1.0]\n<noun>\n  'woman' [1.0]\n  'man' [1.0]\n<verb>\n  'shoots' [1.0]";
      println(s);println();println(e);
      equal(s, e);
    }
  }
               
  @Test
  public void testHasRule()
  {
    RiGrammar rg = new RiGrammar();
    rg.addRule("<rule1>", "<pet>");
    ok(rg.hasRule("<rule1>"));

    for (int j = 0; j < haikuGrammarFiles.length; j++)
    {
      rg = new RiGrammar(); 
      rg.load(RiTa.loadString(haikuGrammarFiles[j]));
      ok(!rg.hasRule("")); // empty
      ok(rg.hasRule("<start>"));
      ok(rg.hasRule("<5-line>"));
    }
  }

  @Test
  public void testAddRule()
  {
    RiGrammar rg = new RiGrammar();
    rg.reset();
    
    rg.addRule("<start>", "<pet>");
    ok(rg.hasRule("<start>"));
    
    rg.addRule("<start2>", "<pet>", 1);
    ok(rg.hasRule("<start2>"));

    rg.addRule("<start>", "<dog>", .3f);
    ok(rg.hasRule("<start>"));

    RiGrammar[] g = createGrammarArray();

    for (int i = 0; i < g.length; i++)
    {
      ok(g[i].addRule("<noun_phrase2>", "np2", .5f));
      ok(g[i].hasRule("<noun_phrase2>"));
    }
  }

  @Test
  public void testRemoveRule()
  {
    for (int j = 0; j < sentenceGrammars.length; j++)
    {
      RiGrammar rg = new RiGrammar(sentenceGrammars[j]);
      ok(rg.hasRule("<noun>"));
      rg.removeRule("<noun>");
      ok(!rg.hasRule("<noun>"));
    }

    RiGrammar[] g = createGrammarArray();

    for (int i = 0; i < g.length; i++)
    {
      ok(g[i].removeRule("<noun_phrase>"));
      ok(!g[i].hasRule("<noun_phrase>"));
    }
  }

  @Test
  public void testPrint()
  {
    RiGrammar rg = new RiGrammar();
    rg.reset();
    rg.addRule("<start>", "<first> | <second>", 1);
    rg.addRule("<first>", "the <pet> <action> of ...", 1);
    rg.addRule("<second>", "the <action> of the <pet> were ...", 1);
    rg.addRule("<pet>", "<bird> | <mammal>", 1);
    rg.addRule("<bird>", "hawks | crows", 1);
    rg.addRule("<mammal>", "dogs", 1);
    rg.addRule("<action>", "cries | screams | falls", 1);
    //rg.print();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    rg.print(ps);
    try
    {
      baos.close();
      ok(baos.toString("UTF8").contains(" the "));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    // TODO: check this against RiTaJS
  }

  @Test
  public void testReset()
  {
    for (int j = 0; j < sentenceGrammars.length; j++)
    {
       RiGrammar rg = new RiGrammar(sentenceGrammars[j]);
        ok(rg.hasRule("<noun>"));
        rg.reset();
        ok(!rg.hasRule("<noun>"));
    }
    
    RiGrammar[] g = createGrammarArray();
    for (int i = 0; i < g.length; i++)
    {
      //g[i].print();
      ok(g[i].hasRule("<start>"));
      ok(g[i].reset());
      ok(!g[i].hasRule("<noun_phrase>"));
    }
  }

  private RiGrammar[] createGrammarArray()
  {
    ArrayList al = new ArrayList();
    for (int i = 0; i < sentenceGrammars.length; i++)
      al.add(new RiGrammar(sentenceGrammars[i]));
    for (int i = 0; i < sentenceGrammarFiles.length; i++)
      al.add(new RiGrammar().load(RiTa.loadString(sentenceGrammarFiles[i])));
    return (RiGrammar[]) al.toArray(new RiGrammar[0]);
  }

  //@Test
  public void testExecPattern()
  {
    String[] s = { 
        "\"<rule>\": \"<another> | `doit()` | dog\"",
        "\"<rule>\": \"<another> | `doit('dog')` | dog\"",
        "\"<rule>\": \"<another> | `doit('<noun>')` | dog\"",
    };
    Pattern p = Pattern.compile("(.*?)`([^`]+?\\([^\\)]*\\))`(.*)");
    for (int j = 0; j < s.length; j++)
    {
      Matcher matcher = p.matcher(s[j]);
      boolean matchFound = matcher.find();
      for (int i = 0; i <= matcher.groupCount(); i++)
        println(i+") "+matcher.group(i));
      println();
      ok(matchFound);
    }

    s = new String[] { 
        "<rule>: <another> | `doit()` | dog",
        "<rule>: <another> | `doit('dog')` | dog",
        "<rule>: <another> | `doit('<noun>')` | dog",
    };
    //println(s);
    p = Pattern.compile("(.*?)`([^`]+?\\([^\\)]*\\))`(.*)");
    for (int j = 0; j < s.length; j++)
    {
      Matcher matcher = p.matcher(s[j]);
      boolean matchFound = matcher.find();
      for (int i = 0; i <= matcher.groupCount(); i++)
        println(i+") "+matcher.group(i));
      println();
      ok(matchFound);
    }
  }


  @Test
  public void testExecIgnores() {
    
    RiGrammar rg = new RiGrammar(); // do nothing
    rg.execDisabled = false;
    
    rg.addRule("<start>", "<first> | <second>");
    rg.addRule("<first>", "the <pet> <action> were 'adj()'");
    rg.addRule("<second>", "the <action> of the 'adj()' <pet>");
    rg.addRule("<pet>", "<bird> | <mammal>");
    rg.addRule("<bird>", "hawk | crow");
    rg.addRule("<mammal>", "dog");
    rg.addRule("<action>", "cries | screams | falls");

    for ( int i = 0; i < 10; i++) {
        String res = rg.expand();
        println(i+") "+res);
        ok(res!=null && res.length()>0);
        ok(res.indexOf("'adj()'")>-1);
    }
  
    rg.reset();
    
    rg.addRule("<start>", "<first> | <second>");
    rg.addRule("<first>", "the <pet> <action> were `adj()'");
    rg.addRule("<second>", "the <action> of the `adj()' <pet>");
    rg.addRule("<pet>", "<bird> | <mammal>");
    rg.addRule("<bird>", "hawk | crow");
    rg.addRule("<mammal>", "dog");
    rg.addRule("<action>", "cries | screams | falls");

    for ( int i = 0; i < 10; i++) {
        String res = rg.expand();
        println(i+") "+res);
        ok(res!=null && res.length()>0);
        ok(res.indexOf("`adj()'")>-1);
    }
  
    rg.reset();
    
    rg.addRule("<start>", "<first> | <second>");
    rg.addRule("<first>", "the <pet> <action> were `nofun()`");
    rg.addRule("<second>", "the <action> of the `nofun()` <pet>");
    rg.addRule("<pet>", "<bird> | <mammal>");
    rg.addRule("<bird>", "hawk | crow");
    rg.addRule("<mammal>", "dog");
    rg.addRule("<action>", "cries | screams | falls");

    boolean tmp = RiTa.SILENT;
    RiTa.SILENT = true;
 
    for ( int i = 0; i < 5; i++) {
        String res = rg.expand();
        //println(i+") "+res);
        println(i+") "+res);
        ok(res!=null && res.length()>0 && res.indexOf(" `nofun()`")>-1);
    }

    for ( int i = 0; i < 5; i++) {
        String res = rg.expand(this);
        println(i+") "+res);
        ok(res!=null && res.length()>0 && res.indexOf(" `nofun()`")>-1);
    }

    RiTa.SILENT = tmp;
  }
  
  @Test
  public void testExec1() {
    
    RiGrammar rg = new RiGrammar();
    rg.execDisabled = false;

    rg.reset();
    rg.addRule("<start>", "<first> | <second>");
    rg.addRule("<first>", "the <pet> <action> were `temp()`");
    rg.addRule("<second>", "the <action> of the `temp();` <pet>");
    rg.addRule("<pet>", "<bird> | <mammal>");
    rg.addRule("<bird>", "hawk | crow");
    rg.addRule("<mammal>", "dog");
    rg.addRule("<action>", "cries | screams | falls");

    for (int i = 0; i < 10; i++) {

      String res = rg.expand(this);
      //println(i+") "+res);
      ok(res!=null && res.length()>0 && res.indexOf("(")<0 && res.indexOf(")")<0);
    }
    
  } String temp() { return Math.random() < .5 ? "hot" : "cold"; }
  
  @Test
  public void testExec2() { 
    RiGrammar rg = new RiGrammar();
    rg.execDisabled = false;

    rg.addRule("<start>", "`getFloat();`");
    String expanded = rg.expand(this);
    //println(expanded);
    ok(expanded!=null);
    ok(Float.parseFloat(expanded));
    
    rg.addRule("<start>", "`getFloat()`");
    expanded = rg.expand(this);
    ok(expanded!=null);
    ok(Float.parseFloat(expanded));
    
    rg.addRule("<start>", "`getFloat()` + `getFloat()`");
    
    expanded = rg.expand(this);
    ok(expanded != null);
    String[] s = expanded.split(" \\+ ");
    for (int i = 0; i < s.length; i++)
    {
      ok(Float.parseFloat(s[i]));
    }
  } 
  
  @Test
  public void testExecArgs() { 
    
    RiGrammar rg = new RiGrammar();
    rg.execDisabled = false;
    rg.addRule("<start>", "`getFloat()`");
    for (int i = 0; i < 10; i++) {

      String res = rg.expandFrom("<start>", this);
      ok(res!=null && res.length()>0&& res.indexOf("(")<0 && res.indexOf(")")<0);
      ok(Float.parseFloat(res));
    }
    
    rg.reset();
    rg.addRule("<start>", "`adj(2)`");
    for (int i = 0; i < 10; i++) {

      String res = rg.expandFrom("<start>", this);
      ok(res!=null && res.length()>0 && res.indexOf("(")<0 && res.indexOf(")")<0);
    }
    
    rg.reset();
    rg.addRule("<start>", "`adj(2.0)`");
    for (int i = 0; i < 10; i++) {

      String res = rg.expandFrom("<start>", this);
      ok(res!=null && res.length()>0 && res.indexOf("(")<0 && res.indexOf(")")<0);
      ok(Float.parseFloat(res)); 
    }
    
    rg.reset();
    rg.addRule("<start>", "`adj(true)`");
    for (int i = 0; i < 10; i++) {
      String res = rg.expandFrom("<start>", this);
      //println(i+")"+res);
      ok(res!=null && res.length()>0 && Boolean.parseBoolean(res));
    }
  } 
  
  // callbacks
  String adj(int num) { return new RiLexicon().randomWord("jj", num); }
  float adj(float num) { return getFloat(); }
  boolean adj(boolean num) { return true; }
  float getFloat() { return (float) Math.random(); }
  
  @Test
  public void testExecArgs2() { 
    
    String[] tests = {
        "{ \"<start>\": \"`rw(2)`\" }", 
        "{ \"<start>\": \"`rw(2.0)`\" }", 

        "{ \"<start>\": \"`rw(vbz,2.0)`\" }", 
        "{ \"<start>\": \"`rw(vbz,2)`\" }",
        
        "{ \"<start>\": \"`rw('vbz',2.0)`\" }", 
        "{ \"<start>\": \"`rw('vbz',2)`\" }", 

        "{ \"<start>\": \"`rw(\\\"vbz\\\",2.0)`\" }", 
        "{ \"<start>\": \"`rw(\\\"vbz\\\",2)`\" }",
        
        "{ \"<start>\": \"`rw(vbz, 2.0)`\" }", 
        "{ \"<start>\": \"`rw(vbz, 2)`\" }",
        
        "{ \"<start>\": \"`rw('vbz', 2.0)`\" }", 
        "{ \"<start>\": \"`rw('vbz', 2)`\" }", 

        "{ \"<start>\": \"`rw(\\\"vbz\\\", 2.0)`\" }", 
        "{ \"<start>\": \"`rw(\\\"vbz\\\", 2)`\" }"
    };
    
    RiGrammar rg = new RiGrammar(this);
    rg.execDisabled = false;
    
    for (int i = 0; i < tests.length; i++)
    {
      rg.load(tests[i]);
      String s = rg.expand();
      ok(s != null && s.length()>0 && s.indexOf("(")<0 && s.indexOf(")")<0);
      //println("the word is "+s);
    }
    
    String[] tests2 = {
        "<start>: the word is `rw(2)`", 
        "<start>: the word is `rw(2.0)`", 

        "<start>: the word is `rw(vbz)`", 
        "<start>: the word is `rw(vbz)`",
        
        "<start>: the word is `rw('vbz')`", 
        "<start>: the word is `rw('vbz')`",
        
        "<start>: the word is `rw(\"vbz\")`", 
        "<start>: the word is `rw(\"vbz\")`",
        
        "{ <start>: the word is `rw(2)` }", 
        "{ <start>: the word is `rw(2.0)` }", 

        "{ <start>: the word is `rw(vbz)` }", 
        "{ <start>: the word is `rw(vbz)` }",
        
        "{ <start>: the word is `rw('vbz')` }", 
        "{ <start>: the word is `rw('vbz')` }",
        
        "{ <start>: the word is `rw(\"vbz\")` }", 
        "{ <start>: the word is `rw(\"vbz\")` }",
    };
    
    println();
    
    rg = new RiGrammar(this);
    rg.execDisabled = false;
    
    for (int i = 0; i < tests2.length; i++)
    {
      rg.load(tests2[i]);
      String s = rg.expand();
      println(s);
      ok(s != null && s.length()>0 && s.indexOf("(")<0 && s.indexOf(")")<0);
    }
  }
  
  // callbacks
  String rw(String pos) { return new RiLexicon().randomWord(pos, 2); }
  String rw(String pos, int n) { return new RiLexicon().randomWord(pos, n); }
  String rw(int n)  { return new RiLexicon().randomWord(n); }
  String rw(String pos, float n)  { return new RiLexicon().randomWord(pos, (int) n);  }
  String rw(float n) { return new RiLexicon().randomWord((int) n); }

  void println(String string)
  {
    if (!RiTa.SILENT) System.out.println(string);
  }
  
  // helpers
  void println() { this.println("\n"); }

  static String[] splice01(String[] o)
  {
    if (o == null || o.length < 1) return o;
    String[] res = new String[o.length-1];
    for (int i = 0; i < res.length; i++)
    {
      res[i] = o[i+1];
    }
    return res;
  }
  
  public static void main(String[] args)
  {
    new RiGrammarTest().testGetGrammar();
  }
}
