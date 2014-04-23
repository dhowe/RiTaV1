package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class RecursiveExec extends PApplet
{
  String stored, grammar = "{\"<start>\": \"`store(`adj()`)` % hello % hello\" }";

  public void setup()
  {
    new RiGrammar(grammar).expand(this);
  }

  public String adj()
  {
    //System.out.println("GrammarExec.adj()");
    return Math.random() < .5 ? "hotter" : "colder";
  }
  
  public String store(String s)
  {
    //System.out.println("GrammarExec.store("+s+")");
    this.stored = s;
    return s;
  }
}
