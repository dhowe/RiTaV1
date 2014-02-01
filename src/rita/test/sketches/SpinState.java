package rita.test.sketches;

import java.util.ArrayList;

import processing.core.PApplet;
import rita.*;

public class SpinState extends PApplet
{
  RiText[] rts;
  RiGrammar grammar;
  ArrayList buf = new ArrayList();

  public void setup()
  {
    size(600, 300);

    RiText.defaultFill(255);
    RiText.defaultFont("Times", 15);

    grammar = new RiGrammar(RiTa.loadString("mcgrammar.json", this));
    //grammar.print();
    
    keyReleased();
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
    //if (e.getKey() != ' ') return;
    if (key != ' ') return;
    
    int linesPerPage = 10;
    
    String line = "";
    if (buf.size() > 0)
    {
      for (int i = 0; i < min(buf.size(), linesPerPage); i++)
        line += buf.remove(0) + " ";
    }
    else
    {
      line = grammar.expand();
      line = line.replaceAll("\\)", "]").replaceAll("\\(", "[");
    }

    RiText.dispose(rts);
    rts = RiText.createLines(this, line, 45, 40, 480);
    
    System.out.println();
    for (int i = 0; i < linesPerPage; i++)
    {
      System.out.println(rts[i].text());
    }
    
    for (int i = linesPerPage; i < rts.length; i++)
    {
      buf.add(rts[i].text()); // save for next time
      RiText.dispose(rts[i]);
    }
  }
}
