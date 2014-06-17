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

    RiText rt1 = new RiText(this, "Move",750,750);
    rt1.moveTo(200,50,2);

    rt3 = new RiText(this, "Move Delay2s");
    rt3.position(200, 250);
   
    rt3.moveTo(550, 50, 2,2);

    rt4 = new RiText(this, "Rotate",100,300);
    rt4.rotate(50).fill(255,0,0);

    rt5 = new RiText(this, "RotateTo Delay2s",500,300);
    rt5.fill(255,0,0);
    rt5.rotateTo(-2,2,2);
    
    rt6 = new RiText(this, "RotateTo",300,300);
    rt6.fill(255,0,0);
    rt6.rotateTo(0.5f,1);
    
    rt7 = new RiText(this, "Bigger",50,500);
    rt7.fill(0,200,0);
    rt7.scale(1.5f);
    
    rt8 = new RiText(this, "Taller",300,500);
    rt8.fill(0,200,0);
    rt8.scale(1.5f,2f);
    
    rt9 = new RiText(this, "ScaleTo",50,700);
    rt9.fill(0,200,0);
    rt9.scaleTo(1.5f,2);
    
    RiText rt10 = new RiText(this, "ScaleTo Delay2s",300,700);
    rt10.fill(0,200,0);
    rt10.scaleTo(1.5f,2,2);

  }

  public void draw()
  {

	  
    fill(255, 100); // leave trails
    rect(0, 0, width, height);
    
    fill(0,0,255);
    text((int)rt3.position()[0]+", "+(int)rt3.position()[1],(int)rt3.position()[0],(int)rt3.position()[1]+50);
    
    text(rt5.rotate()[0] + "",500,300+100);
    
    text(RiText.timer(this,3),50,50);

    RiText.drawAll();
    

  }

  public void changeBg(){
	  
	  
  }
  public void onRiTaEvent(RiTaEvent re) {

	/*	if (re.type() == RiTa.MOVE_TO)
	     moveTo((RiText) re.source());
		
		if (re.type() == RiTa.ROTATE_TO)
		     rotateTo((RiText) re.source());
		*/
  }
  


  


}
