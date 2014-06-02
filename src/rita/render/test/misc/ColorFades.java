package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class ColorFades extends PApplet {

	public void setup() {
	  
		size(600, 400);
		
		RiText.defaults.showBounds = true;

		RiText rt1 = new RiText(this, "One", 30, 30);
		RiText rt2 = new RiText(this, "Two", 90, 90);
		RiText rt3 = new RiText(this, "Three", 180, 180);
		RiText rt4 = new RiText(this, "Four", 280, 280);

		rt1.fontSize(20);
		rt2.fontSize(60);
		rt3.fontSize(40);
		rt4.fontSize(120);

		rt1.fill(255, 0, 0);
		rt2.fill(0, 255, 255, 255);
		rt3.fill(255, 255, 0, 255);
		rt4.fill(255, 0, 255, 255);

		rt1.fadeOut(4, 0, true);
		rt2.fadeOut(2);
		rt3.colorTo(new float[] { 0, 0, 255, 255 }, 4);
		rt4.colorTo(new float[] { 0, 255, 0, 255 }, 4);
	}

	public void draw() {
		background(255);
		RiText.drawAll();
	}
	
	public void onRiTaEvent(RiTaEvent re) {
	  System.out.println("Done: "+re);
	}

}
