package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

// MOUSE-CLICKED PROBLEM??
public class TextTo extends PApplet {
  
	RiText rt;
	int clicks = 0;
	String[] txt = { "Click To Fade", "Fade On Click" };

	public void setup() {
	  
		size(400, 100);
		rt = new RiText(this, txt[0], 10, 70);
		rt.font("Arial", 60);
	}

	public void draw() {
	  
		background(255);
		rt.draw();		
	}

	public void onRiTaEvent(RiTaEvent re) {
	  System.out.println("TextTo.onRiTaEvent("+re+")");
	}
	
	public void mouseClicked() {

	  // swap texts
		String newText = clicks++ % 2 == 0 ? txt[1] : txt[0];

		// fade to the new over 2 secs
		rt.textTo(newText, 2f);
	}
}
