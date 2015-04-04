package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class FontScaling extends PApplet {
  
	RiText rt;

	public void setup() {

		size(400, 200);
		
		RiText.defaultFont("Big Caslon", 110);
		rt = new RiText(this, "SCALE", 0, 150);

		doScale();
	}

	public void draw() {
	  
		background(255);
		rt.draw();
	}

	public void doScale() {
	  
		rt.scaleTo(0.2f, 3); // scale to .2 over 3 seconds

		// scale to fullsize over 2 seconds, after waiting 3 seconds
		rt.scaleTo(1, 2, 3);
	}

	// called when any behavior (e.g., scaleTo) completes
	public void onRiTaEvent(RiTaEvent re) {

		rt = (RiText) re.source();
		if (rt.scaleX >= 1)
			doScale();
	}

}
