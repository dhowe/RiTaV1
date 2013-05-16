package rita.render.test;

import processing.core.PApplet;
import rita.*;

public class CreateLinesSimple extends PApplet {
  
  int x = 50, y = 30, w = 350, h = 430;
	String txt = "A huge lizard was discovered drinking out of the fountain today. It was not menacing anyone, it was just very thirsty. A small crowd gathered and whispered to one another, as though the lizard would understand them if they spoke in normal voices. The lizard seemed not even a little perturbed by their gathering. It drank and drank, its long forked tongue was like a red river hypnotizing the people, keeping them in a trance-like state. 'It's like a different town,' one of them whispered. 'Change is good,' the other one whispered back.";

	public void setup() {

		size(900, 500);
    txt += "<p/>" + txt; 
    RiText.createLines(this, txt, x, y, w, h);
    RiText.defaults.indentFirstParagraph = true;
    RiText.defaults.paragraphIndent = 40;
    RiText.defaults.paragraphLeading = 10;
    RiText.createLines(this, txt, x+width/2, y, w, h, createFont("Georgia", 16), 20);
    background(250);
    noFill();
    rect(x, y, w, h);
    rect(x+width/2, y, w, h);
    RiText.drawAll();
	}

}
