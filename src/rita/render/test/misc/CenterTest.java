package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class CenterTest extends PApplet {
  
	RiText rt;

	public void setup() {

		size(400, 200);

		RiText.defaults.showBounds = true;

		rt = new RiText(this, "Centerpoint", 130, 100);
		rt.font(createFont("Arial", 36));

		background(250);
		RiText.drawAll();
		
		line(0,rt.y, width,rt.y);
		noStroke();
		fill(200,0,0);
		float[] c = rt.center();
		ellipse(c[0],c[1], 5,5);	
	}

}
