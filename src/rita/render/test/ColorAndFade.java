package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.test.PixelCompare;

public class ColorAndFade extends PApplet {
  
	public void setup() {

		size(400, 800);
		
		// TODO: Kenny: fill() is the method to set the color
		// TODO: colorTo() works, but won't work for a test 
		// until I add a function to delay writing the image.

		  
		RiText.defaultFont("Times", 20);
		
		// TODO colorTo seems not working
		RiText rt = new RiText(this, "ColorTo gray in 2 sec", 100,  100);
		rt.colorTo(200, 2);
		System.out.println(rt.alpha());
		float[] c = {0,0,255};
		new RiText(this, "ColorTo Bluein 2 sec" , 100, 200).colorTo(c,2);
		new RiText(this, "ColorTo Blue 5s delay 5" , 100, 300).colorTo(c,5,2);



		RiText rt2 = new RiText(this, "fade out and in" , 100, 400);
		rt2.fadeOut(2);

		RiText rt3 = new RiText(this, "fade out delay and in" , 100, 500);
		rt3.fadeOut(2,2);
		
		
		rt2.fadeIn(2,4);
		rt3.fadeIn(2,8);

		
		background(255);

		//RiText.drawAll();
	}
	
	public void draw() {
		
		background(255);
		fill(0);
		//text(millis()+ "" ,10,750);
		RiText.drawAll();
	}
	

}
