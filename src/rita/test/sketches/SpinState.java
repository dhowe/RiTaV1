package rita.test.sketches;

import java.util.ArrayList;

import processing.core.PApplet;
import rita.*;

public class SpinState extends PApplet
{

  int linesPerPage = 10;

  RiText[] rts;
  RiGrammar grammar;
  ArrayList buf = new ArrayList();

  public void setup()
  {
    size(600, 300);

    RiText.defaultFill(255);
    RiText.defaultFont("Times", 15);

    grammar = new RiGrammar(this);
    grammar.loadFrom("mcgrammar.json");
  }

  public void draw()
  {
    background(0);
    stroke(255);
    fill(0);
    rect(25, 20, 515, 240);
    RiText.drawAll();
  }

  public void keyReleased()
  {
    if (key == ' ')
      generate();
  }

  public void generate()
  {
    String line = "";
    if (buf.size() > 0)
    {
      String[] lines = new String[min(buf.size(), linesPerPage)];
      for (int i = 0; i < lines.length; i++)
        lines[i] = (String) buf.remove(0);
      
      System.out.println(lines.length + " lines, saving " + buf.size());

      RiText.dispose(rts);
      rts = RiText.createLines(this, lines, 45, 40, 480);

      if (buf.size() == 0)
      { // add ending

        rts = (RiText[]) append(rts, (new RiText(this, "* * *", 
            width / 2, rts[rts.length - 1].y + 50)).align(CENTER));
      }
    }
    else
    {
      buf.clear();
      RiText.dispose(rts);
      line = grammar.expand().replaceAll("\\)", "]").replaceAll("\\(", "[");
      rts = RiText.createLines(this, line, 45, 40, 480);
      for (int i = linesPerPage; i < rts.length; i++)
      {
        buf.add(rts[i].text());
        //println("hide: " + rts[i].text());
        rts[i].alpha(0);
      }
    }
  }
}
