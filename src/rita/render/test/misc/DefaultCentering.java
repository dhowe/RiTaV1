package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class DefaultCentering extends PApplet {
	
  RiText rt1, rt2;
	float tw;

	public void setup() {

		size(600, 400);
    RiText.defaults.showBounds = true;

		rt1 = new RiText(this, "This text should be auto-centered");
		tw = rt1.textWidth();

		rt2 = new RiText(this, "this is manual");
		rt2.font("Batang", 24);
		rt2.x = width / 2 - rt2.textWidth() / 2;
		rt2.y += 50;

	}

	public void draw() {
		background(255);

		rt2.draw();
		rt1.draw();
		
		line(width / 2, 0, width / 2, height);
		line(width / 2 - tw / 2, 0, width / 2 - tw / 2, height);
		line(width / 2 + tw / 2, 0, width / 2 + tw / 2, height);
	}

}
