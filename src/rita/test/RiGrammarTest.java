package rita.test;

import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import rita.*;

public class RiGrammarTest
{ 
  // TODO: add tests that these exists(actually should be taken from JSON actually)
  String[] functions = { "addRule", "clone", "expand", "expandFrom", "expandWith", "getGrammar",
      "getRule", "getRules", "hasRule", "print", "removeRule", "reset", "load", "loadFromFile" 
  };

  String sentenceGrammar = "{ \"<start>\" : \"<noun_phrase> <verb_phrase>\", \"<noun_phrase>\" : \"<determiner> <noun>\", \"<verb_phrase>\" : \"<verb> | <verb> <noun_phrase> [.1]\", \"<determiner>\" : \"a [.1] | the\", \"<noun>\" : \"woman | man\", \"<verb>\" : \"shoots\" }";

  @Test
  public void testRiGrammar()
  {
    RiGrammar rg = new RiGrammar();
    ok(rg);
  }
  
  @Test
  public void testSimpleExpand()
  {
    RiGrammar rg = new RiGrammar();
    rg.loadFromFile("sentence1.json");
    for (int i = 0; i <100; i++)
      ok(rg.expand());
    
    /*String rule = rg.getRule("<determiner>");
    equal(rule, "a [0.1] | the");
    
*/
    rg = new RiGrammar();
    rg.loadFromFile("sentence2.json");
    for (int i = 0; i <100; i++)
      ok(rg.expand());
  /*  
    rule = rg.getRule("<determiner>");
System.out.println("RiGrammarTest.testSimpleExpand()::"+rule);
    equal(rule, "a [0.1] | the");*/
   
/*if (1==1) return;*/
    
    rg = new RiGrammar();
    rg.load(sentenceGrammar);
    //rg.print();
    for (int i = 0; i <100; i++)
      ok(rg.expand());

/*    rule = rg.getRule("<determiner>");
    equal(rule, "a [0.1] | the");
    */
    rg.reset();
    rg.addRule("<start>", "<rule1>", 1);
    try
    {
      ok(rg.expand());
    }
    catch (RiTaException e)
    {
      ok(e);
    }
  }

  @Test
  public void testRiGrammarString()
  {
    RiGrammar rg = new RiGrammar(sentenceGrammar);
    ok(rg.hasRule("<verb>"));
    ok(rg.hasRule("<noun>"));
    ok(!rg.hasRule("adadf"));
  }

  @Test
  public void testLoadFromFile()
  {
    RiGrammar g = new RiGrammar();
    g.loadFromFile("sentence1.json");
    ok(!g.hasRule("{")); // empty
    ok(g.hasRule("<start>"));
    
    g = new RiGrammar();
    g.loadFromFile("sentence2.json");
    ok(!g.hasRule("{")); // empty
    ok(g.hasRule("<start>"));

    RiGrammar rg = new RiGrammar(); 
    rg.loadFromFile("haikuGrammar.json");
    //rg.print();
    ok(!rg.hasRule("")); // empty
    ok(!rg.hasRule("{")); // empty
    ok(rg.hasRule("<start>"));
    ok(rg.hasRule("<5-line>"));

    ok(!rg.hasRule("{")); // empty
    ok(rg.hasRule("<start>"));
    ok(rg.hasRule("<5-line>"));
  }
  
  /*
  @Test
  public void testLoadFromFileStringObject(){
    RiGrammar g = new RiGrammar();
    g.loadFromFile("sentence1.json", "expandShim", this);
    ok(!g.hasRule("{")); // empty
    ok(g.hasRule("<start>"));
  }
  public String expandShim(){ }
  */

  @Test
  public void testExpandFromFile()
  {
    RiGrammar g = new RiGrammar();
    g.loadFromFile("sentence1.json");
    ok(g.hasRule("<start>"));
    for (int i = 0; i < 10; i++)
    {
      String res = g.expand();
      ok(res);
    }
    
    g = new RiGrammar();
    g.loadFromFile("sentence2.json");
    ok(g.hasRule("<start>"));
    for (int i = 0; i < 10; i++)
    {
      String res = g.expand();
      //System.out.println("'"+res+"'");
      ok(res);
    }
  }
   
      
  @Test
  public void testExpand()
  {
      RiGrammar rg = new RiGrammar();
      
      rg.reset();
  
      rg.addRule("<start>", "pet");
      //rg.print();
      //System.out.println("Expand: '" + rg.expand() + "'");
      equal(rg.expand(), "pet");
  
      rg.addRule("<start>", "pet", 1);
      equal(rg.expand(), "pet");
      rg.addRule("<start>", "pet", 2);
      equal(rg.expand(), "pet");
  
      rg.reset();
      rg.addRule("<start>", "<pet>", 1);
      rg.addRule("<pet>", "dog", 1);
      //System.out.println("Expand: " + rg.expand());
      equal(rg.expand(), "dog");
  
      rg.reset();
      rg.addRule("<start>", "<rule1>", 1);
      rg.addRule("<rule1>", "cat", .4f);
      rg.addRule("<rule1>", "dog", .6f);
      rg.addRule("<rule1>", "boy", .2f);
      ok(rg.hasRule("<rule1>"));
  
      boolean found1 = false;
      boolean found2 = false;
      boolean found3 = false;
      for (int i = 0; i < 100; i++)
      {
        String res = rg.expand();
        // System.out.println("GOT: "+res);
        if (res.equals("cat"))
          found1 = true;
        else if (res.equals("dog"))
          found2 = true;
        else if (res.equals("boy"))
          found3 = true;
        
        ok(found1 || found2 || found3);
      }
      ok(found1);
      ok(found2);
      ok(found3);
  
      rg.reset();
      rg.addRule("<start>", "pet", 1);
      equal(rg.expand(), "pet");
  
      rg.reset();
      rg.addRule("<start>", "<pet>", 1);
      rg.addRule("<pet>", "dog", .7f);
      rg.addRule("<pet>", "cat", .3f);
  
      int d = 0, c = 0;
      for (int i = 0; i < 100; i++)
      {
        String r = rg.expand();
        if (r.equals("dog"))
          d++;
        if (r.equals("cat"))
          c++;
      }
      ok(d > 50); // d + ""
      ok(d < 90); // d + ""
      ok(c > 10); // g + ""
      ok(c < 50); // g + ""
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

  /*@Test
  public void testExpandWith()
  {
    // TODO: Implement this or remove from API

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
        System.out.println("error: " + r);
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
        System.out.println("error: " + r);
        missed = true;
      }
    }
    equal(missed, false);
    equal("MORE TESTS HERE", false);
  }*/

  @Test
  public void testGetGrammar()
  {  
    RiGrammar rg = new RiGrammar(sentenceGrammar);
    String s = rg.getGrammar();
    String e = "<start>\n  '<noun_phrase> <verb_phrase>' [1.0]\n<determiner>\n  'a' [0.1]\n  'the' [1.0]\n<noun_phrase>\n  '<determiner> <noun>' [1.0]\n<verb_phrase>\n  '<verb> <noun_phrase>' [0.1]\n  '<verb>' [1.0]\n<noun>\n  'woman' [1.0]\n  'man' [1.0]\n<verb>\n  'shoots' [1.0]";
    //System.out.println(s);System.out.println();System.out.println(e);
    equal(s, e);

  }
  
  /*
  @Test
  public void testGetRule()
  {
    RiGrammar rg = new RiGrammar(sentenceGrammar);
    String r = rg.getRule("<noun_phrase>");
    equal(r,"<determiner> <noun>");
  
    rg = new RiGrammar(sentenceGrammar);
    rg.loadFromFile("sentence2.json");
    r = rg.getRule("<noun_phrase>");    
    equal(r,"<determiner> <noun>");
    
    rg = new RiGrammar(sentenceGrammar);
    rg.loadFromFile("sentence1.json");
    r = rg.getRule("<noun_phrase>");    
    //System.out.println("'"+r+"'");
    equal(r,"<determiner> <noun>");

    rg.reset();
    rg.addRule("<rule1>", "<pet>", 1);
    equal(rg.getRule("<rule1>"),"<pet>");

    rg.reset();
    rg.addRule("<rule1>", "cat", .4f);
    rg.addRule("<rule1>", "dog", .6f);
    rg.addRule("<rule1>", "boy", .2f);
    String answer = "cat [0.4] | dog [0.6] | boy [0.2]";
    String result = rg.getRule("<rule1>");
    equal(result, answer);

    rg.reset();
    rg.addRule("rule1", "<pet>", 1);
    equal(rg.getRule("rule1"),"<pet>");

    rg.reset();
    
    equal("", rg.getRule("<start>"));
    equal("", rg.getRule("start"));

    RiGrammar[] g = {
        (new RiGrammar()).load(sentenceGrammar) , 
        (new RiGrammar()).loadFromFile("sentence1.json"),
        (new RiGrammar()).loadFromFile("sentence2.json")
    };
    for (int i = 0; i < g.length; i++)
    {
      String rule = g[i].getRule("<noun_phrase>");
      //System.out.println(i+"R='"+rule+"'");
      equal(rule,"<determiner> <noun>");
    }
    }
  */
    
               
  @Test
  public void testHasRule()
  {
    RiGrammar rg = new RiGrammar();
    rg.addRule("<rule1>", "<pet>");
    ok(rg.hasRule("<rule1>"));

    rg = new RiGrammar(); 
    rg.loadFromFile("haikuGrammar.json");
    //rg.print();
    ok(!rg.hasRule("")); // empty
    ok(rg.hasRule("<start>"));
    ok(rg.hasRule("<5-line>"));

/*    (rg = new RiGrammar()).setGrammarFromFile("haikuGrammar.g");
    //rg.print();
    ok(!rg.hasRule("{")); // empty
    ok(rg.hasRule("<start>"));
    ok(rg.hasRule("<5-line>"));*/
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

    RiGrammar[] g = {
        new RiGrammar(sentenceGrammar), 
        new RiGrammar().loadFromFile("sentence1.json"),
        new RiGrammar().loadFromFile("sentence2.json")
    };
    for (int i = 0; i < g.length; i++)
    {
      ok(g[i].addRule("<noun_phrase2>", "np2", .5f));
      ok(g[i].hasRule("<noun_phrase2>"));
    }
  }

  @Test
  public void testLoad()
  {
    RiGrammar rg = new RiGrammar();
    ok(!rg.hasRule("<verb"));
    rg.load(sentenceGrammar);
    rg.hasRule("<verb");
    for (int i = 0; i < 100; i++)
    {
      rg.expandFrom("<verb_phrase>");  
    }    
  }

  @Test
  public void testRemoveRule()
  {
    RiGrammar rg = new RiGrammar();
    rg.load(sentenceGrammar);
    ok(rg.hasRule("<noun>"));
    rg.removeRule("<noun>");
    ok(!rg.hasRule("<noun>"));
    
    RiGrammar[] g = {
        new RiGrammar(sentenceGrammar) , 
        new RiGrammar().loadFromFile("sentence1.json"),
        new RiGrammar().loadFromFile("sentence2.json")
    };
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
    RiGrammar rg = new RiGrammar();
    rg.load(sentenceGrammar);
    ok(rg.hasRule("<noun>"));
    rg.reset();
    ok(!rg.hasRule("<noun>"));
    
    RiGrammar[] g = {
        (new RiGrammar(sentenceGrammar)), 
        new RiGrammar().loadFromFile("sentence1.json"),
        new RiGrammar().loadFromFile("sentence2.json")
    };
    for (int i = 0; i < g.length; i++)
    {
      //g[i].print();
      ok(g[i].hasRule("<start>"));
      ok(g[i].reset());
      ok(!g[i].hasRule("<noun_phrase>"));
    }
  }
  

  //@Test
  public void testExecPattern()
  {
    String[] s = { 
        "\"<rule>\": \"<another> | `doit()` | dog\"",
        "\"<rule>\": \"<another> | `doit('dog')` | dog\"",
        "\"<rule>\": \"<another> | `doit('<noun>')` | dog\"",
    };
    //System.out.println(s);
    //Pattern p = Pattern.compile("(.*?)`[^`]+`(.*$)");
    Pattern p = Pattern.compile("(.*?)`([^`]+?\\([^\\)]*\\))`(.*)");
    for (int j = 0; j < s.length; j++)
    {
      Matcher matcher = p.matcher(s[j]);
      boolean matchFound = matcher.find();
      for (int i = 0; i <= matcher.groupCount(); i++)
      {
        System.out.println(i+") "+matcher.group(i));
      }
      System.out.println();
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
        //System.out.println(i+") "+res);
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
        //System.out.println(i+") "+res);
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
    for ( int i = 0; i < 10; i++) {
        String res = rg.expand();
        //System.out.println(i+") "+res);
        ok(res!=null && res.length()>0 && res.indexOf("`nofun()`")>-1);
    }

    rg.reset();

    rg.addRule("<start>", "<first> | <second>");
    rg.addRule("<first>", "the <pet> <action> were `nofun()`");
    rg.addRule("<second>", "the <action> of the `nofun()` <pet>");
    rg.addRule("<pet>", "<bird> | <mammal>");
    rg.addRule("<bird>", "hawk | crow");
    rg.addRule("<mammal>", "dog");
    rg.addRule("<action>", "cries | screams | falls");

    for ( int i = 0; i < 10; i++) {
        String res = rg.expand();
        //System.out.println(i+") "+res);
        ok(res!=null && res.length()>0 && res.indexOf("`nofun()`")>-1);
    }
    RiTa.SILENT = tmp;
  }
  
  @Test
  public void testExec1() { // throw Exception for now
    
    RiGrammar rg = new RiGrammar();
    rg.execDisabled = false;

    rg.reset();
    rg.addRule("<start>", "<first> | <second>");
    rg.addRule("<first>", "the <pet> <action> were `adj()`");
    rg.addRule("<second>", "the <action> of the `adj();` <pet>");
    rg.addRule("<pet>", "<bird> | <mammal>");
    rg.addRule("<bird>", "hawk | crow");
    rg.addRule("<mammal>", "dog");
    rg.addRule("<action>", "cries | screams | falls");

    for (int i = 0; i < 10; i++) {

      String res = rg.expand(this);
      //System.out.println(i+") "+res);
      ok(res!=null && res.length()>0 && res.indexOf("adj()")<0);
    }
    
  } String adj() { return Math.random() < .5 ? "hot" : "cold"; }
  
  @Test
  public void testExec2() { // throw Exception for now
    RiGrammar rg = new RiGrammar();
    rg.execDisabled = false;

    rg.addRule("<start>", "`getFloat();`");
    String expanded = rg.expand(this);
    //System.out.println(expanded);
    ok(expanded!=null);
    ok(Float.parseFloat(expanded));
    
    rg.addRule("<start>", "`getFloat()`");
    expanded = rg.expand(this);
    ok(expanded!=null);
    ok(Float.parseFloat(expanded));
    
    rg.addRule("<start>", "`getFloat()` + `getFloat()`");
    
    expanded = rg.expand(this);
    ok(expanded != null);
    String[] s = expanded.split(" + ");
    for (int i = 0; i < s.length; i++)
    {
      ok(Float.parseFloat(s[i]));
    }
    
  } float getFloat() { return (float) Math.random(); }

}
