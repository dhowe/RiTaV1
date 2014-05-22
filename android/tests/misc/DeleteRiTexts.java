package rita.render.test.misc;

import processing.core.PApplet;
import processing.event.MouseEvent;
import rita.RiText;

public class DeleteRiTexts extends PApplet {
  
	RiText rt1, rt2, rt3, rt4, rts[];

	public void setup() {

		size(600, 400);
		new RiText(this, "CLICK");
    createSome();
	}

	public void draw() {	
    background(255);
    RiText.drawAll();
	}

	public void createSome() {
	  
		RiText.dispose(rts);
		
		//System.out.println(rts); RiTa.out(rts);
		
		rt1 = new RiText(this, "One",   random(width - 50), 50);
		rt2 = new RiText(this, "Two",   random(width - 50), 70);
		rt3 = new RiText(this, "Three", random(width - 50), 90);
		rt4 = new RiText(this, "Four",  random(width - 50), 110);
		
		rts = new RiText[] { rt1, rt2, rt3, rt4 };
	}

	public void mouseClicked() {
	
  		createSome();
	}
}