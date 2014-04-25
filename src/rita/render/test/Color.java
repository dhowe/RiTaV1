package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.test.PixelCompare;

public class Color extends PApplet {
  
	public void setup() {

		size(400, 800);
		
		// TODO: Kenny: fill() is the method to set the color
		// TODO: colorTo() works, but won't work for a test 
		// until I add a function to delay writing the image.

		  
		RiText.defaultFont("Times", 20);
		
		// TODO colorTo seems not working
		RiText rt = new RiText(this, "ColorTo 10 in 5 sec", 100,  100);
		rt.colorTo(200, 5);
		System.out.println(rt.alpha());
		float[] c = {0,0,255};
		new RiText(this, "ColorTo Blue" , 100, 200).colorTo(c,2);
		new RiText(this, "ColorTo Blue 5s delay 2" , 100, 300).colorTo(c,5,2);

		//TODO color not implemented yet
		/*
		RiText rt2 = new RiText(this, "Color Gray", 100,  400);
		rt2.fill(200, 5);
		System.out.println(rt2.alpha());
		float[] c = {0,0,255};
		new RiText(this, "Color Blue" , 100, 500).color(c,200);
		new RiText(this, "Color Blue" , 100, 600).color(c,105);
*/


		background(255);

		RiText.drawAll();
	}
	


}
