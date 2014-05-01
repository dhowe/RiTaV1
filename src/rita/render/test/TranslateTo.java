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
	RiText rt7;
	RiText rt8;
	RiText rt9;
	int counter;
  public void setup()
  {
    size(800, 800);
    
    RiText.defaultFont("Georgia", 30);

    RiText rt1 = new RiText(this, "ZIG");

    RiText rt2 = new RiText(this, "ZAG");

    rt2.motionType(RiTa.EASE_IN_OUT);

    moveTo(rt1);
    moveTo(rt2);

    rt3 = new RiText(this, "ZOP");
    rt3.position(0, 550);
   
    rt3.moveTo(550, 0, 5,5);

    rt4 = new RiText(this, "TURN",330,100);
    rt4.rotate(50);

    rt5 = new RiText(this, "TURN Delay",150,500);
    rt5.rotateTo(2,6,5);
    
    rt6 = new RiText(this, "TURNNNN",width/2,height/2);
    rotateTo(rt6);
    
    rt7 = new RiText(this, "Bigger",width-200,height-500);
    rt7.scale(1.5f);
    
    rt8 = new RiText(this, "Taller",width-200,height-400);
    rt8.scale(1.5f,2f);
    
    rt9 = new RiText(this, "O",width-200,height-300);
    rt9.scaleTo(5,2);
    
    RiText rt10 = new RiText(this, "O delay",width-200,height-200);
    rt10.scaleTo(5,2,5);
    
    
  }

  public void draw()
  {
    fill(255, 100); // leave trails
    rect(0, 0, width, height);
    counter+=10;
    fill(0);
    text((int)rt3.position()[0] + ", "+  (int)rt3.position()[1] ,100,580);
    
    text(rt4.rotate()[0] + "",430,100);
    RiText.drawAll();
    
    if(counter > width   ){
		  counter=0;
	  }
  }

  public void onRiTaEvent(RiTaEvent re) {

		if (re.type() == RiTa.MOVE_TO)
	     moveTo((RiText) re.source());
		
		if (re.type() == RiTa.ROTATE_TO)
		     rotateTo((RiText) re.source());
		
  }
  
  public void moveTo(RiText rt)
  {
	
    float newX = counter;
    float newY = counter;
    float dst = rt.distanceTo(newX, newY);

    rt.moveTo(newX, newY, dst / width);
  }
  
  public void rotateTo(RiText rt)
  {

    float newRot = counter/PI;

    rt.rotateTo(newRot,1);
  }
  


}
