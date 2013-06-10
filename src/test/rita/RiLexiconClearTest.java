package test.rita;

import static test.QUnitStubs.deepEqual;
import static test.QUnitStubs.equal;
import static test.QUnitStubs.ok;

import org.junit.Test;

import rita.*;

public class RiLexiconClearTest
{
  @Test
  public void testAddWord()
  {
    RiLexicon lex = new RiLexicon();
    
    ok(lex.containsWord("zooms"));

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
    System.out.println(lex.size());
    
    lex.reload();
  }

  @Test
  public void testRemoveWord()
  {
    RiLexicon lex = new RiLexicon();

    int size = lex.size();

    ok(lex.containsWord("banana"));
    lex.removeWord("banana");
    ok(!lex.containsWord("banana"));

    lex.removeWord("a");
    ok(!lex.containsWord("a"));
    ok(lex.containsWord("are")); // check that others r still there
    lex.removeWord("aaa");
    ok(!lex.containsWord("aaa"));

    lex.removeWord("");

    lex.reload();

    System.out.println(size+"=?"+lex.size());
    
    ok(lex.containsWord("a"));
    ok(lex.containsWord("zooms"));

    
    equal(size, lex.size());
  }
/*
  @Test
  public void testPos()
  {
    RiString rs = new RiString("asdfaasd");
    String[] result = rs.pos();
    String[] answer = new String[] { "nn" };
    //System.out.println(RiTa.asList(result));
    deepEqual(result, answer);
    
    rs = new RiString("cat");
    result = rs.pos();
    //System.out.println(RiTa.asList(result));
    answer = new String[] { "nn" };
    deepEqual(result, answer);

    rs = new RiString("clothes");
    result = rs.pos();
    answer = new String[] { "nns" };
    //System.out.println(RiTa.asList(result));

    deepEqual(result, answer);

    rs = new RiString("There is a cat.");
    //System.out.println(rs.features()); 
    result = rs.pos();
    answer = new String[] { "ex", "vbz", "dt", "nn", "." };
    //System.out.println(RiTa.asList(result));
    if (!result[2].equals("dt")) {
      System.out.println("==========================");
      System.out.println(RiTa.asList(result));
      System.out.println(rs.features());
      System.out.println(rs.pos());
      System.out.println("==========================");
    }
    deepEqual(result, answer);

    rs = new RiString("The boy, dressed in red, ate an apple.");
    result = rs.pos();

    answer = new String[] {
        "dt", "nn", ",", "vbn", "in", "jj", ",", "vbd", "dt", "nn", "." 
     };
    //System.out.println(RiTa.asList(result));

    deepEqual(result, answer);
  }*/
}
