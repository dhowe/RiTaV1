package test.rita;

import static test.QUnitStubs.equal;
import static test.QUnitStubs.ok;

import java.io.*;

import org.junit.Test;

import rita.RiGrammar;

public class RiGrammarTest
{ 
  String[] functions = { "addRule", "clone", "expand", "expandFrom", "expandWith",
      "getRule", "getRules", "hasRule", "print", "removeRule", "reset", "setGrammar" };

  String sentenceGrammar = "{ \"<start>\" : \"<noun_phrase> <verb_phrase>\", \"<noun_phrase>\" : \"<determiner> <noun>\", \"<verb_phrase>\" : \"<verb> | <verb> <noun_phrase> [.1]\", \"<determiner>\" : \"a [.1] | the\", \"<noun>\" : \"woman | man\", \"<verb>\" : \"shoots\" }";

  @Test
  public void testRiGrammar()
  {
    RiGrammar rg = new RiGrammar();
    ok(rg);
  }
  
  @Test
  public void testSentenceGrammar()
  {
    RiGrammar rg = new RiGrammar();
    rg.setGrammar(sentenceGrammar);
    //rg.print();
    for (int i = 0; i <100; i++)
      ok(rg.expand());
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
  public void testSetGrammarFromFile()
  {
    RiGrammar g = new RiGrammar();
    g.setGrammarFromFile("grammar.json");
    ok(g.hasRule("<start>"));
      
    RiGrammar rg = new RiGrammar(); 
    rg.setGrammarFromFile("haikuGrammar.json");
    //rg.print();
    ok(!rg.hasRule("")); // empty
    ok(rg.hasRule("<start>"));
    ok(rg.hasRule("<5-line>"));

    rg = new RiGrammar();
    rg.setGrammarFromFile("haikuGrammar.g");
    //rg.print();
    ok(!rg.hasRule("{")); // empty
    ok(rg.hasRule("<start>"));
    ok(rg.hasRule("<5-line>"));
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

    //System.out.println(rg.hasRule("<mammal>"));
    equal(rg.expandFrom("<mammal>"), "dog");

    for (int i = 0; i < 100; i++)
    {
      String res = rg.expandFrom("<bird>");
      ok(res.equals("hawk") || res.equals("crow"));
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
  public void testGetRule()
  {
    RiGrammar rg = new RiGrammar();

    rg.reset();
    rg.addRule("<rule1>", "<pet>", 1);
    ok(rg.getRule("<rule1>"));

    rg.reset();
    rg.addRule("<rule1>", "cat", .4f);
    rg.addRule("<rule1>", "dog", .6f);
    rg.addRule("<rule1>", "boy", .2f);
    ok(rg.getRule("<rule1>"));

    rg.reset();
    rg.addRule("rule1", "<pet>", 1);
    ok(rg.getRule("rule1"));

    rg.reset();

    try
    {
      rg.getRule("<start>");
    }
    catch (Exception e)
    {
      ok(e);
    }

    try
    {
      rg.getRule("start");
    }
    catch (Exception e)
    {
      ok(e);
    }
    
    RiGrammar[] g = {
        (new RiGrammar()).setGrammar(sentenceGrammar) , 
        (new RiGrammar()).setGrammarFromFile("grammar.json"),
        (new RiGrammar()).setGrammarFromFile("grammar.g")
    };
    for (int i = 0; i < g.length; i++)
    {
      ok(g[i].getRule("<noun_phrase>"));
    }
  }
    
               
  @Test
  public void testHasRule()
  {
    RiGrammar rg = new RiGrammar();
    rg.addRule("<rule1>", "<pet>");
    ok(rg.hasRule("<rule1>"));

    rg = new RiGrammar(); 
    rg.setGrammarFromFile("haikuGrammar.json");
    //rg.print();
    ok(!rg.hasRule("")); // empty
    ok(rg.hasRule("<start>"));
    ok(rg.hasRule("<5-line>"));

    (rg = new RiGrammar()).setGrammarFromFile("haikuGrammar.g");
    //rg.print();
    ok(!rg.hasRule("{")); // empty
    ok(rg.hasRule("<start>"));
    ok(rg.hasRule("<5-line>"));
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
        new RiGrammar().setGrammarFromFile("grammar.json"),
        new RiGrammar().setGrammarFromFile("grammar.g")
    };
    for (int i = 0; i < g.length; i++)
    {
      ok(g[i].addRule("<noun_phrase2>", "np2", .5f));
      ok(g[i].hasRule("<noun_phrase2>"));
    }
  }

  @Test
  public void testSetGrammar()
  {
    RiGrammar rg = new RiGrammar();
    ok(!rg.hasRule("<verb"));
    rg.setGrammar(sentenceGrammar);
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
    rg.setGrammar(sentenceGrammar);
    ok(rg.hasRule("<noun>"));
    rg.removeRule("<noun>");
    ok(!rg.hasRule("<noun>"));
    
    RiGrammar[] g = {
        new RiGrammar(sentenceGrammar) , 
        new RiGrammar().setGrammarFromFile("grammar.json"),
        new RiGrammar().setGrammarFromFile("grammar.g")
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
    rg.setGrammar(sentenceGrammar);
    ok(rg.hasRule("<noun>"));
    rg.reset();
    ok(!rg.hasRule("<noun>"));
    
    RiGrammar[] g = {
        (new RiGrammar(sentenceGrammar)), 
        new RiGrammar().setGrammarFromFile("grammar.json"),
        new RiGrammar().setGrammarFromFile("grammar.g")
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
