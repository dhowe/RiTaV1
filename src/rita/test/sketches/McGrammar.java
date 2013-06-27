package rita.test.sketches;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.event.KeyEvent;
import rita.RiGrammar;
import rita.RiText;

public class McGrammar extends PApplet
{
  RiText[] rts;
  RiGrammar grammar;
  ArrayList buf = new ArrayList();

  public void setup()
  {
    size(600, 300);

    RiText.defaultFont("Times", 20);

    grammar = new RiGrammar();
    grammar.setGrammarFromFile("mcgrammar.json");
    keyReleased();
  }

  public void draw()
  {
    background(0);
    stroke(255);
    rect(25, 20, 550, 260);
    RiText.drawAll();
  }

  public void keyReleased(KeyEvent e)
  {
    if (e.getKey() != ' ') return;
    
    int linesPerPage = 10;
    
    String line = "";
    if (buf.size() > 0)
    {
      for (int i = 0; i < min(buf.size(), linesPerPage); i++)
        line += buf.remove(0) + " ";
    }
    else
    {
      for (int i = 0; i < 10; i++)
        line += grammar.expand();
    }

    RiText.dispose(rts);
    rts = RiText.createLines(this, line, 35, 30, 530);
    
    for (int i = linesPerPage; i < rts.length; i++)
    {
      buf.add(rts[i].text()); // save for next time
      RiText.dispose(rts[i]);
    }
  }
}
