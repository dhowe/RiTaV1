package rita.render.test;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;
import rita.test.PixelCompare;

public class CreateLinesAndLayoutWithBreaks extends PApplet {

	String[] txt = { 
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

	float x=30, y=30, w=400-60, h=623;
	float x2=420, y2=30, w2=400-60, h2=623;
	float x3=810, y3=30, w3=400-60, h3=h2/2;

	public void setup() {

		size(1200, 700);
		background(250);

		RiText.defaultFont("Georgia", 16);

		RiText.createLines(this, txt, x, y); // preserve line-breaks (no w,h)

		RiText.createLines(this, txt, x2, y2,w2,h2); // don't preserve line-breaks
		rect(x2, y2, w2, h2);


		RiText.createLines(this, txt, x3, y3,w3,h3); // don't preserve line-breaks
		rect(x3, y3, w3, h3);


		RiText.createLines(this, "A huge lizard.", x3, y3+400,w3,64); // single line
		rect(x3, y3+400, w3, 64);

		RiText.defaultFont("Georgia", 32);

		RiText.createLines(this, "A huge lizard.", x3, y3+500, w3, 64); // single line
		rect(x3, y3+500, w3, 64);

		RiText.drawAll();
	}

	public static void main(String[] args)
	{
		String testPath = "/RiTa/src/";///Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
		new PixelCompare(testPath).generateRefImage(CreateLinesAndLayoutWithBreaks.class.getName());
	}
}
