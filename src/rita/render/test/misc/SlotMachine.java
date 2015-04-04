package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class SlotMachine extends PApplet {

	int clicks = 0;
	RiText[] slots = new RiText[3];
	String[] items = { "cherry", "$$$$", "lemon", "seven", "rings" };

	public void setup() {

		size(400, 100);

		RiText.defaultFont(createFont("Arial", 32));

		// the initial layout
		int xOff = width / 2 - 100;
		for (int j = 0; j < 3; j++)
			slots[j] = new RiText(this, items[j], xOff += 50, 65);

		slots[0].textAlign(RIGHT);
		slots[1].textAlign(CENTER);

		// set a timer to call onRiTaEvent every .3 sec
		RiText.timer(this, .3f);
	}

	public void mouseClicked() {
		if (++clicks > 3) clicks = 0; // count clicks
	}

	public void onRiTaEvent(RiTaEvent re) {

		// set them all to red
		for (int i = 0; i < slots.length; i++)
			slots[i].fill(255, 0, 0);

		// lets some keep spinning
		if (clicks < 1)
			randomItem(slots[1]);
		if (clicks < 2)
			randomItem(slots[0]);
		if (clicks < 3)
			randomItem(slots[2]);
	}

	public void randomItem(RiText rt) {
	  
		// pick a random string & set it to black
		int randIdx = (int) Math.floor(random(slots.length));
		rt.text(items[randIdx]);
		rt.fill(0);
	}

	public void draw() {
	  
		background(221, 221, 204);
		RiText.drawAll();
	}

}
