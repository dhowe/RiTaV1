package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class MoveTo extends PApplet
{
  public void setup()
  {
    size(400, 400);
    
    RiText.defaultFont("Georgia", 30);

    RiText rt1 = new RiText(this, "ZIG");
    RiText rt2 = new RiText(this, "ZAG");

    rt2.motionType(RiTa.EASE_IN_OUT);

    moveToRandom(rt1);
    moveToRandom(rt2);
  }

  public void draw()
  {
    fill(255, 100); // leave trails
    rect(0, 0, width, height);

    RiText.drawAll();
  }

  public void onRiTaEvent(RiTaEvent re) {
	    
		if (re.type() == RiTa.MOVE_TO)
	     moveToRandom((RiText) re.source());
  }
  
  public void moveToRandom(RiText rt)
  {
    float newX = RiText.random(width - rt.textWidth()); 
    float newY = RiText.random(rt.textHeight(), height - 10);
    float dst = rt.distanceTo(newX, newY);
    
    rt.moveTo(newX, newY, dst / width);
  }

}
