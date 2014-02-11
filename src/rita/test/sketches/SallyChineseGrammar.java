package rita.test.sketches;

import processing.core.PApplet;
import rita.RiGrammar;
import rita.RiText;

public class SallyChineseGrammar extends PApplet
{
  RiText rt;
  RiGrammar rg;

  public void setup()
  {
    size(600,600);
    rt = new RiText(this, "click");
    rg = new RiGrammar();
    rg.loadFrom("sally.json").openEditor();
  } 
  public void draw() {
    background(255);
    rt.draw();
  }

  @Override
  public void mouseClicked()
  {
    rt.text(rg.expand()).draw();
  }
}
