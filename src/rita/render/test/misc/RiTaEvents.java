package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class RiTaEvents extends PApplet {
  
	RiText rt1;

	public void setup() {

		size(600, 200);

		rt1 = new RiText(this, "Fade", 130, 130);
		rt1.fontSize(120);
		rt1.fill(200, 0, 0, 255);
		rt1.fadeOut(4);

	}

	public void draw() {

	  background(0);
		rt1.draw();
	}

	public void onRiTaEvent(RiTaEvent e) {
	  
		println(e);
		rt1.text("Done");
		rt1.fill(200, 0, 0, 255);
	}

}
