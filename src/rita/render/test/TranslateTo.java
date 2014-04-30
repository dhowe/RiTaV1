package rita.render.test;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class TranslateTo extends PApplet
{
	RiText rt3;
	RiText rt4;
	RiText rt5;
	RiText rt6;
	int counter;
  public void setup()
  {
    size(400, 400);
    
    RiText.defaultFont("Georgia", 30);

    RiText rt1 = new RiText(this, "ZIG");
    RiText rt2 = new RiText(this, "ZAG");

    rt2.motionType(RiTa.EASE_IN_OUT);

    moveTo(rt1);
    moveTo(rt2);

    rt3 = new RiText(this, "ZOP");
    rt3.position(0, 380);
   
    rt3.moveTo(380, 0, 5,5);

    rt4 = new RiText(this, "TURN",150,100);
    rt4.rotate(50);

    rt5 = new RiText(this, "TURN Delay",150,300);
    rt5.rotateTo(2,6,5);
    
    rt6 = new RiText(this, "TURNNNN",width/2,height/2);
    rotateTo(rt6);
    
  }

  public void draw()
  {
    fill(255, 100); // leave trails
    rect(0, 0, width, height);
    counter+=10;
    fill(0);
    text(rt3.position()[0] + ", "+  rt3.position()[1] ,100,380);
    
    text(rt4.rotate()[0] + "",100,100);
    RiText.drawAll();
  }

  public void onRiTaEvent(RiTaEvent re) {

		if (re.type() == RiTa.MOVE_TO)
	     moveTo((RiText) re.source());
		
		if (re.type() == RiTa.ROTATE_TO)
		     rotateTo((RiText) re.source());
		
  }
  
  public void moveTo(RiText rt)
  {
	  if(counter > width   ){
		  counter=0;
	  }
    float newX = counter;
    float newY = counter;
    float dst = rt.distanceTo(newX, newY);

    rt.moveTo(newX, newY, dst / width);
  }
  
  public void rotateTo(RiText rt)
  {
	  if(counter > width ){
		  counter=0;
	  }
    float newRot = counter/PI;

    rt.rotateTo(newRot,1);
  }

}
