package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class PulpGrammar extends PApplet
{
  RiGrammar grammar;
  RiText[] rts = new RiText[1];

  public void setup()
  {
    size(600, 300);

    RiText.defaultFont("Georgia", 30);
    RiText.defaults.alignment = CENTER;

    rts[0] = new RiText(this, "click", width/2, 100);

    grammar = new RiGrammar();
    grammar.loadFile("pulp.json", this);
  }

  public void draw()
  {
    background(240);
    RiText.drawAll();
  }

  public void mouseClicked()
  {
    String result = grammar.expand();
    RiText.dispose(rts);
    rts = RiText.createLines(this, result + ".", width/2, 40);
    
    // uppercase first letter of sentence
    rts[0].replaceChar(0, rts[0].charAt(0).toUpperCase());
  }
}
