package rita.test;

import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;

import org.junit.Test;

import rita.RiLexicon;

public class RiLexiconClearTest
{
  @Test
  public void testAddWordStringStringString()
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

  public void removeWord(RiLexicon lex, String s) {
    lex.lexicalData().remove(s.toLowerCase());
  }
  
  @Test
  public void testRemoveWordString()
  {
    RiLexicon lex = new RiLexicon();

    int size = lex.size();

    ok(lex.containsWord("banana"));
    removeWord(lex, "banana");
    ok(!lex.containsWord("banana"));

    removeWord(lex, "a");
    ok(!lex.containsWord("a"));
    ok(lex.containsWord("are")); // check that others r still there
    removeWord(lex, "aaa");
    ok(!lex.containsWord("aaa"));

    removeWord(lex, "");

    lex.reload();

    System.out.println(size+"=?"+lex.size());
    
    ok(lex.containsWord("a"));
    ok(lex.containsWord("zooms"));

    
    equal(size, lex.size());
  }

}
