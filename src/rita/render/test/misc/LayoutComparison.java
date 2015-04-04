package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class LayoutComparison extends PApplet {

	String[] txtArr = { 
	    "A huge lizard was discovered drinking out of the",
			"fountain today. It was not menacing anyone, it was",
			"just very thirsty. A small crowd gathered and",
			"whispered to one another, as though the lizard",
			"would understand them if they spoke in normal",
			"voices. The lizard seemed not even a little perturbed",
			"by their gathering. It drank and drank, its long",
			"forked tongue was like a red river hypnotizing the",
			"people, keeping them in a trance-like state. 'It's like",
			"a different town,' one of them whispered. 'Change is",
			"good,' the other one whispered back.", "",
			"    A huge lizard was discovered drinking out of the",
			"fountain today. It was not menacing anyone, it was",
			"just very thirsty. A small crowd gathered and",
			"whispered to one another, as though the lizard",
			"would understand them if they spoke in normal",
			"voices. The lizard seemed not even a little perturbed",
			"by their gathering. It drank and drank, its long",
			"forked tongue was like a red river hypnotizing the",
			"people, keeping them in a trance-like state. 'It's like",
			"a different town,' one of them whispered. 'Change is",
			"good,' the other one whispered back." };

  String txt = "A huge lizard was discovered drinking out of the fountain today. It was not menacing anyone, it was just very thirsty. A small crowd gathered and whispered to one another, as though the lizard would understand them if they spoke in normal voices. The lizard seemed not even a little perturbed by their gathering. It drank and drank, its long forked tongue was like a red river hypnotizing the people, keeping them in a trance-like state. 'It's like a different town,' one of them whispered. 'Change is good,' the other one whispered back.";

  float x=30,y=30,w=300,h=350;

  public void setup() {

    size(720, 500);
    txt += "<p/>" + txt; 
    RiText.defaults.paragraphLeading = 16;
    RiText.createLines(this, txt, x, y, w, h);
    RiText.createLines(this, txtArr, x+width/2, y, w, h);
    background(250);
    noFill();
    rect(x, y, w, h);
    rect(x+width/2, y, w, h);
    RiText.drawAll();
  }

}
