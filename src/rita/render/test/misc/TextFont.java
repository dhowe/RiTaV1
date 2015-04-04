package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class TextFont extends PApplet {
	RiText rt1;
	RiText rt2;
	RiText rt3;
	RiText rt4;

	public void setup() {
		size(200, 200);
		noLoop();

		PFont pf = createFont("Batang", 48);

		rt1 = new RiText(this, "Bigger", 20, 50);
		// rt1.textFont(pf); IN RITAJS
		rt1.font(pf);

		rt2 = new RiText(this, "Smaller", 20, 100);
		rt2.font(pf);
		rt2.scale(32 / 48);

		PFont pf2 = createFont("Batang", 10);

		rt3 = new RiText(this, "Smallest", 20, 140);
		rt3.font(pf2);

		rt4 = new RiText(this, "Default", 20, 180);
	}

	public void draw() {
		background(255);
		rt1.draw();
		rt2.draw();
		rt3.draw();
		rt4.draw();
	}

}
