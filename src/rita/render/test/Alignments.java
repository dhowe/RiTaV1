package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.support.RiTimer;

public class Alignments extends PApplet {
  
	RiText rt, rt2, rt3;

	public void setup() {
	  
		size(400, 400);

    RiText.defaults.boundingBoxVisible = true;
		RiText.defaultFont(createFont("Times", 64));

		rt = new RiText(this,  "Left", 200,  70);
		rt.align(LEFT);
		rt2 = new RiText(this, "Center", 200, 200);
		rt2.align(CENTER);
		rt3 = new RiText(this, "Right", 200, 300);
		rt3.align(RIGHT);

		background(255);

		line(200, 0, 200, 400);

		RiText.drawAll();
	}


}
