package rita.render.test;

import processing.core.PApplet;
import rita.RiTa;
import rita.RiText;

public class RadomColor extends PApplet
{
	  RiText rt1,rt2,rt3,rt4;
  public void setup()
  {
    size(800, 400);

	RiTa.start(this);

    background(255);
    
    RiText.defaultFontSize(30);

    rt1 = new RiText("Random Color", 50,  50);
    
    rt2 = new RiText("Random Color With Alpha", 50,  150);
    
    rt3 = new RiText("Random Color", 50,  250);
    
    rt4 = new RiText("Random Color With Alpha", 50,  350);

  }
  
  public void draw(){
	  	background(255);
	    
		
		float[] color1 = RiText.randomColor(0,100,false);
		rt1.fill(color1);
		fill(0);
		text(color1.length,0,80);
		text(color1[0]+" , "+color1[1]+" , "+color1[2],50,80);
		
		float[] color2 = RiText.randomColor(0,100,true);
		rt2.fill(color2);
		fill(0);
		text(color2.length,0,180);
		text(color2[0]+" , "+color2[1]+" , "+color2[2]+" , "+color2[3],50,180);

		float[] color3 = RiText.randomColor(100,255,false);
		rt3.fill(color3);
		fill(0);
		text(color3.length,0,280);
		text(color3[0]+" , "+color3[1]+" , "+color3[2],50,280);
		
		float[] color4 = RiText.randomColor(100,255,true);
		rt4.fill(color4);
		fill(0);
		text(color4.length,0,380);
		text(color4[0]+" , "+color4[1]+" , "+color4[2]+" , "+color4[3],50,380);

	
		
	    RiText.drawAll();
	  
  }
}
