package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class GrammarExec extends PApplet
{
  String haikuGrammar = 
    "{\"<start>\": \"`store(<5>)` % <5-line> % `fetch()`\","
    + "\"<5-line>\": \"<1> <3> <1> |<1> <1> <3> | <2> <3> | <3> <2> | <3> <1> <1>\","
    + "\"<1>\": \"red | white | black | sky | dawns | breaks | falls | leaf | rain | pool | my | your | sun | clouds | blue | green | night | day | dawn | dusk | birds | fly | grass | tree | branch | through | hell | zen | smile | gray | wave | sea | through | sound | mind | smoke | cranes | fish\","
    + "\"<2>\": \"drifting | purple | mountains | skyline | city | faces | toward | empty | buddhist | temple | japan | under | ocean | thinking | zooming | rushing | over | rice field | rising | falling | sparkling | snowflake\","
    + "\"<3>\": \"`adj()` farms | `adj()` springs | `adj()` boats | `adj()` rain | `adj()` snow\","
    + "\"<5>\": \"resolutional | non-elemental | rolling foothills rise | toward mountains higher | out over this country | in the springtime again\"}";

  RiGrammar grammar;
  RiText[] rts = new RiText[3];

  private String stored;

  public void setup()
  {
    size(650, 200);

    RiText.defaultFont("Georgia", 30);
    RiText.defaults.alignment = CENTER;

    rts[0] = new RiText(this, "click to", width / 2, 75);
    rts[1] = new RiText(this, "generate", width / 2, 110);
    rts[2] = new RiText(this, "a haiku", width / 2, 145);

    grammar = new RiGrammar(haikuGrammar, this);
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
  
  public String fetch()
  {
    return this.stored;
  }
  
  public void draw()
  {
    background(230, 240, 255);
    for (int k = 0; k < rts.length; k++)
      rts[k].draw();
  }

  public void mouseClicked()
  {
    String result = grammar.expand();
    String[] lines = result.split("%");
    for (int i = 0; i < rts.length; i++)
    {
      rts[i].text(lines[i]);
    }
  }
}
