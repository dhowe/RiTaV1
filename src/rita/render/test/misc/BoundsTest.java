package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class BoundsTest extends PApplet {
  
	RiText rt1, rt2, rt3, rt4, rt5, rt6, rt7;

	public void setup() {

		size(600, 700);

		RiText.defaults.showBounds = true;

		PFont pf = createFont("Arial", 36);

		rt1 = new RiText(this, "Bounding", 130, 100);
		rt1.font(pf);

		rt2 = new RiText(this, "wounze", 50, 240);

		rt3 = new RiText(this, "Bound in", 600, 170, pf);
		rt3.align(RIGHT);
		rt3.scale(72 / (float)pf.getSize());

		rt4 = new RiText(this, "Bound ing", 130, 200, pf);

		rt5 = new RiText(this, "Boundage", 180, 300);
		rt5.font(createFont("Times", 64));

		RiText.defaults.showBounds = false;
		
		RiText.defaultFont("Georgia", 50);
		
		rt6 = new RiText(this, "Ground Hog", 199, 450);

		rt7 = new RiText(this, "Scaling Fail", 20, 600);
		
		rt7.scale(2);
	}

	public void draw() {
		background(250);
		
		noFill();
		float[] bb = rt6.boundingBox();
		rect(bb[0], bb[1], bb[2], bb[3]);
		bb = rt7.boundingBox();
    rect(bb[0], bb[1], bb[2], bb[3]);
    
		RiText.drawAll();
	}

}
