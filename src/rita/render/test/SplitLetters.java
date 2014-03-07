package rita.render.test;

import processing.core.PApplet;
import rita.*;

public class SplitLetters extends PApplet { // combine with splitWords as 1 test
  
  int x = 90, y = 50, w = 350, h = 500;
	String txt = "The lizard was not menacing anyone, it was just very thirsty.";

	public void setup() {

		size(720, 100);
		RiText.defaultFont("Times", 28);
		RiText rt = new RiText(this,txt,10,40);
		RiText[] letters = rt.splitLetters();
		for (int i = 0; i < letters.length; i++) {
		  letters[i].y = 70;
		  letters[i].fill(RiText.randomColor());
		}
		
		background(250);
    RiText.drawAll();
	}

}
