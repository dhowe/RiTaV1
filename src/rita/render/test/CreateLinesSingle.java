package rita.render.test;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;
import rita.test.PixelCompare;

public class CreateLinesSingle extends PApplet {
	    
	public void setup() {

		size(400, 400);
		background(250);

		RiText.defaultFont("Georgia", 32);

		RiText[] rts = RiText.createLines(this, "A huge lizard.",0,0,400,64); // single line
		rect(0, 0,400,64);

		if (rts.length > 0)
			System.out.println("y="+rts[0].y);

		new RiText(this, "A huge lizard.",0,25).fill(0,64);
		RiText.drawAll();
	}

}
