package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class PickAndDrag extends PApplet
{
  float xOff, yOff;
  RiText current;

  public void setup()
  {
    size(400, 400);
    RiText.defaultFont("Georgia", 16);
    RiText.createWords(this, "A huge lizard", 20, 20, 360, 360);
  }

  public void draw() {  
	    background(255);
	    for ( int i = 0; i < RiText.instances.size(); i++) {
	        RiText rt = RiText.instances.get(i);
	        rt.showBounds(current == rt);
	        rt.draw();
	    }
  }

  public void mousePressed()
  {
    RiText[] picks = RiText.picked(mouseX, mouseY);
    if (picks.length > 0)
    {
      current = picks[0];
      xOff = mouseX - current.x;
      yOff = mouseY - current.y;
    }
  }

  public void mouseReleased()
  {
    current = null;
  }

  public void mouseDragged()
  {
    if (current != null)
    {
      current.x = mouseX - xOff;
      current.y = mouseY - yOff;
    }
  }
}
