package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class SimpleTimer extends PApplet {

	RiText rt;

	int count = 0; // utf-8 chars too
	String[] txt = { "GOD", "DOG" };

	public void setup() {

		size(200, 100);
		rt = new RiText(this, txt[0], 10, 80);
		rt.font("Courier", 100);

		// set timer to call onRiTaEvent every half sec.
		RiText.timer(this, .5f);
	}

	public void onRiTaEvent(RiTaEvent re) {

	  
	  background(221, 221, 204);
	  
		rt.text(count++ % 2 == 0 ? txt[1] : txt[0]);
    System.out.println("SimpleTimer.onRiTaEvent("+rt.text()+")");

		rt.draw();
	}

}
