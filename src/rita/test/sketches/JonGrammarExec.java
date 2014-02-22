package rita.test.sketches;

import processing.core.PApplet;
import rita.*;

public class JonGrammarExec extends PApplet
{
  RiGrammar grammar;
  RiLexicon lex;
  RiText rt;
  
  String[] tests = {
      "{ \"<start>\": \"`syllableCount(2)`\" }",
      "{ \"<start>\": \"`syllableCount(2.0)`\" }",
      
      "{ \"<start>\": \"`syllableCount('vbz',2.0)`\" }",
      "{ \"<start>\": \"`syllableCount('vbz',2)`\" }",
      
      "{ \"<start>\": \"`syllableCount(\\\"vbz\\\",2.0)`\" }",
      "{ \"<start>\": \"`syllableCount(\\\"vbz\\\",2)`\" }"
  };

  public void setup()
  {
    size(600, 200);

    RiText.defaults.alignment = CENTER;
    
    rt = new RiText(this, "callback-tests");    
    lex = new RiLexicon();
    grammar = new RiGrammar(this);
    
    for (int i = 0; i < tests.length; i++)
    {
      grammar.load(tests[i]);
      String s = grammar.expand();
      System.out.println("s="+s);
    }
  }

  public void draw()
  {
    background(230, 240, 255);
    rt.draw();
  }

  public void mouseClicked()
  {
    rt.text(grammar.expand());
  }
  
  // calbacks

  String syllableCount(String pos, int n)
  {
    return lex.randomWord(pos, n);
  }

  String syllableCount(int n)
  {
    String s = lex.randomWord(n);
    return s;
  }
  
  String syllableCount(String pos, float n)
  {
    return lex.randomWord(pos, (int) n);
  }

  String syllableCount(float n)
  {
    String s = lex.randomWord((int) n);
    return s;
  }

}
