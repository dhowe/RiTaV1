package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class SplitWords extends PApplet {
  
  int x = 90, y = 50, w = 350, h = 500;
	String txt = "The lizard was not menacing anyone, it was just very thirsty.";

	public void setup() {

		size(720, 100);
		RiText.defaultFont("Times", 28);
		RiText rt = new RiText(this,txt,10,40);
		RiText[] words = rt.splitWords();
		for (int i = 0; i < words.length; i++) {
		  words[i].y = 70;
		  words[i].fill(RiText.randomColor());
		}
		
		background(250);
    RiText.drawAll();
	}

}
