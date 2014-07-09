package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class CreateLetters extends PApplet {

	int x = 50, y = 50, w = 400, h = 400;
	String txt = "A huge lizard was discovered drinking out of the fountain today. It was not menacing anyone, it was just very thirsty. A small crowd gathered and whispered to one another, as though the lizard would understand them if they spoke in normal voices. The lizard seemed not even a little perturbed by their gathering. It drank and drank, its long forked tongue was like a red river hypnotizing the people, keeping them in a trance-like state. 'It's like a different town,' one of them whispered. 'Change is good,' the other one whispered back.";

	public void setup() {

		size(500, 500);

    RiText.defaults.paragraphIndent = 40;
    RiText.defaults.paragraphLeading = 5;
    RiText.defaults.indentFirstParagraph = true;
    RiText.defaults.showBounds = true;
		RiText.defaultFont("Batang", 16);
		txt += "<p/>" + txt; // add a paragraph
		mouseReleased();
	}

	public void draw() {
		background(255);
		noFill();
		rect(x, y, w, h);
		RiText.drawAll();
	}

	public void mousePressed() {
		x = mouseX;
		y = mouseY;
	}

	public void mouseDragged() {

		w = Math.abs(mouseX - x);
		h = Math.abs(mouseY - y);
	}

	public void mouseReleased() {

		RiText.disposeAll();
		RiText[] rts = RiText.createLetters(this, txt, x, y, w, h);
		for (int i = 0; i < rts.length; i++) {
			rts[i].fill(RiText.randomColor());
		}
	}

}
