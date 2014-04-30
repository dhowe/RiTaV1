package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.support.RiTimer;

public class BoundingBoxes extends PApplet {
  
	public void setup() {
	  
		size(400, 400);

		RiTa.start(this);
		
    RiText.defaults.showBounds = true;
    RiText.defaults.fontSize = 64;
		
		new RiText("Left", 200,  100);
		new RiText("Center", 200, 200).align(CENTER);
		new RiText("Right", 200, 300).align(RIGHT);

		background(255);
		line(200, 0, 200, 400);
		
		RiText.drawAll();
	}
	
}
