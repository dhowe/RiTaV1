package rita.render.test.misc;

import processing.core.PApplet;
import rita.RiText;

public class PixelXPosition extends PApplet {
  
	float y;
	int numWords;
	String txt = "The dog ate the cat.";
	RiText line1;

	public void setup() {
	  
	  size(400, 100);
	  
    RiText.defaults.showBounds = true;
		RiText.defaultFont("arial", 30);

		line1 = new RiText(this, txt, 64, 50);
		String[] tx = txt.split(" ");
		numWords = tx.length;

		drawIt();
	}

	public void drawIt() {
		background(200);

		line1.draw();

		stroke(255);
		for (int i = 0; i < txt.length(); i++) {
			int ly = (int) line1.charOffset(i);
			line(ly, 28, ly, height);
		}

		stroke(255, 0, 0);
		for (int i = 0; i < numWords; i++) {
			int ly = (int) line1.wordOffset(i);
			line(ly, 0, ly, 55);
		}
	}

}
