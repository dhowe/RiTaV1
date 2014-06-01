package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.test.PixelCompare;

public class ColorAndFade extends PApplet {
  
	public void setup() {

		size(400, 800);

		RiText.defaultFont("Times", 20);
		

		RiText rt = new RiText(this, "1.ColorTo gray in 2 sec", 100,  50);
		rt.colorTo(200, 2);
		rt.showBounds(true);

		float[] c = {0,0,255};
		RiText rt6 = new RiText(this, "2.ColorTo Bluein 2 sec" , 100, 100);
		rt6.colorTo(c,2);
		rt6.showBounds(true);
		new RiText(this, "3.ColorTo Blue 5s delay 5" , 100, 150).colorTo(c,5,2);

		RiText rt2 = new RiText(this, "4.fade out and in" , 100, 200);
		rt2.fadeOut(2);

		rt2.fadeIn(2,4);
		
		RiText rt3 = new RiText(this, "5.fade out delay 2s and in at 8s" , 100, 250);
		rt3.fadeOut(2,2);

		rt3.fadeIn(2,8);
		
		RiText rt4 = new RiText(this, "6.fade out and in at 4s" , 100, 300);
		rt4.showBounds(true); 
		rt4.fadeOut(2);
		rt4.fadeIn(2,4);

		RiText rt5 = new RiText(this, "7.fade out delay 2s and in at 8s" , 100, 350);
		rt5.showBounds(true);
		rt5.fadeOut(2,2);
		
		rt5.fadeIn(2,8);


		RiText rt7 = new RiText(this, "8.Null" , 100, 400);
		rt5.showBounds(true);
		rt7.textTo("8.Changed Text",3);
		
		
		RiText rt8 = new RiText(this, "9.Null" , 100, 450);

		rt8.textTo("9.Changed Text delay3s",3,5); 
		
		RiText rt9 = new RiText(this, "10.Null" , 100, 500);
		rt9.showBounds(true);
		rt9.textTo("10.Changed Text delay3s",3,5);	

		
		background(255);

		//RiText.drawAll();
	}
	
	public void draw() {
		
		background(255);
		//fill(0);
		//text(millis()+ "" ,10,750);
		RiText.drawAll();
	}
	

}
