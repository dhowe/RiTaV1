package rita.test.sketches;

import processing.core.PApplet;
import rita.*;

public class PhraseTest extends PApplet
{
  class RiPhrase extends RiText {
    
    RiText child;
    String translation;
    
    public RiPhrase(PApplet p, String c1, String trans)
    {
      this(p, c1, null, trans);
    }
    
    public RiPhrase(PApplet p, String c1, String c2, String trans)
    {
      super(p, c1);
      this.text(c1);
      if (c2 != null) {
        child = new RiText(p, c2);
      }
      this.translation = trans;
    }
        
    public RiText draw()
    {  
      super.draw();
      child.draw();
      return this;
    }
    
    public boolean contains(float mx, float my)
    {
      return super.contains(mx, my) || child.contains(mx, my);
    }
    
    public RiText alpha(float alpha)
    {
      if (child != null)
        child.alpha(alpha);
      return super.alpha(alpha);
    }
    
    public void childOffsetY(float yOff)
    {
       if (child != null)
        child.y = this.y + yOff;
    }
  }
  
  RiPhrase phrase;
  public void setup()
  {
    size(400, 400);
    RiText.defaultFont("Georgia", 16);
    phrase = new RiPhrase(this, "A", "B", "trans");
    phrase.childOffsetY(-20);
  }

  public void draw()
  {
    background(255);
    phrase.alpha(phrase.contains(mouseX, mouseY) ? 150 : 255);
      
    phrase.draw();
  }


}
