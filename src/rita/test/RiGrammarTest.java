package rita.test;

import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;

import java.io.*;

import org.junit.Test;

import rita.RiGrammar;
import rita.RiTaException;

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
    
    String rule = rg.getRule("<determiner>");
    equal(rule, "a [0.1] | the");
    
    rg = new RiGrammar();
    rg.loadFromFile("sentence2.json");
    for (int i = 0; i <100; i++)
      ok(rg.expand());
    
    rule = rg.getRule("<determiner>");
    equal(rule, "a [0.1] | the");
    
    rg = new RiGrammar();
    rg.load(sentenceGrammar);
    //rg.print();
    for (int i = 0; i <100; i++)
      ok(rg.expand());

    rule = rg.getRule("<determiner>");
    equal(rule, "a [0.1] | the");
    
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
      ok(rg.getRule("<rule1>"));
  
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
      ok(false);
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
    String e = "<start>\n  '<noun_phrase> <verb_phrase>' [1.0]\n<determiner>\n  'a' [0.1]\n  'the' [1.0]\n<noun_phrase>\n  '<determiner> <noun>' [1.0]\n<verb_phrase>\n  '<verb> <noun_phrase>' [0.1]\n  '<verb>' [1.0]\n<noun>\n  'woman' [1.0]\n  'man' [1.0]\n<verb>\n  'shoots' [1.0]\n";
    System.out.println(s);
    System.out.println(e);
    equal(s, e);

  }
  
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
    ok(rg.getRule("<start>"));
    
    rg.addRule("<start2>", "<pet>", 1);
    ok(rg.hasRule("<start2>"));
    ok(rg.getRule("<start2>"));

    rg.addRule("<start>", "<dog>", .3f);
    ok(rg.hasRule("<start>"));
    ok(rg.getRule("<start>"));

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

}
