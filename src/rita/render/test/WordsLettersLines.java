package rita.render.test;

import processing.core.PApplet;
import rita.RiText;
import rita.test.PixelCompare;

public class WordsLettersLines extends PApplet
{

	// Layout RiTexts in a line, as a row or words, or as single letters

	String txt = "The dog ate the cat.";
	RiText line1, line2[], line3[];

	public void setup()
	{

		size(800, 600);
		background(255);
		RiText.defaultFontSize(32); 
		RiText.defaults.showBounds = true;

		line1 = new RiText(this, txt, 64, 50); // lines
		line2 = RiText.createWords(this, txt, 64, 80); // words
		line3 = RiText.createLetters(this, txt, 64, 130); // letters

		RiText.defaults.showBounds = false;

		RiText.defaultFont("Times", 28);

		RiText rt = new RiText(this,txt,100,300);

		RiText[] letters = rt.splitLetters();
		for (int i = 0; i < letters.length; i++) {
			letters[i].y = rt.y + 30;
			float r = i*(256/letters.length);
			r = i % 2 == 1 ? r : 255-r;
			letters[i].fill(r, 255-r, 255);
		}

		RiText[] words = rt.splitWords();
		for (int i = 0; i < words.length; i++) {
			words[i].y = rt.y + 60;
			float r = i*(256/words.length);
			words[i].fill(r, 100, 255-r);
		}

		RiText rt2 = new RiText(this,txt,400,50);
		for (int i = 0; i < txt.length(); i++) {
			line(rt2.charOffset(i),10,rt2.charOffset(i),50);
		}
		rect(400,56-rt2.textHeight(),rt2.textWidth(),rt2.textHeight());
		
		RiText rt3 = new RiText(this,txt,400,150);
		for (int i = 0; i < rt.wordCount(); i++) {
			line(rt3.wordOffset(i),100,rt3.wordOffset(i),150);
		}

		rect(400,156-rt3.textHeight(),rt3.textWidth(),rt3.textHeight());
		
		String txt2 = "The fat leo and the ele.";
		
		RiText rt4 = new RiText(this,txt2,400,250);
		for (int i = 0; i < txt2.length(); i++) {
			line(rt4.charOffset(i),210,rt4.charOffset(i),230);
		}
		rect(400,256-rt4.textHeight(),rt4.textWidth(),rt4.textHeight());
		
		RiText rt5 = new RiText(this,txt2,400,350);
		for (int i = 0; i < rt5.wordCount(); i++) {
			line(rt5.wordOffset(i),300,rt5.wordOffset(i),350);
		}

		rect(400,356-rt5.textHeight(),rt5.textWidth(),rt5.textHeight());
		
 
		RiText rt6 = new RiText(this,txt2,100,500);
		rt6.showBounds(true);
			line(50,500,50, 500 -rt6.textAscent());
			line(80,500,80, 500 +rt6.textDescent());
		
			
			RiText rt7 = new RiText(this,txt,100,550);
			rt7.showBounds(true);
				line(50,550,50, 550 -rt7.textAscent());
				line(80,550,80, 550 +rt7.textDescent());
			

		

		fill(0);

		text(rt.distanceTo(rt3),(rt.x +rt3.x)/2,(rt.y +rt3.y)/2	);	
		line(rt.x,rt.y,rt3.x,rt3.y);
		
		text(rt.distanceTo(0,400),(rt.x )/2-35,(rt.y +400)/2+30	);	
		line(rt.x,rt.y,0,400);
		
		RiText.drawAll();

	}

	public static void main(String[] args)
	{
		String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
		new PixelCompare(testPath).generateRefImage(WordsLettersLines.class.getName());
	}
}
