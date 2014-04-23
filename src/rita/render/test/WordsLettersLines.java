package rita.render.test;

import processing.core.PApplet;
import rita.RiText;
import rita.test.PixelCompare;

public class WordsLettersLines extends PApplet {

	// Layout RiTexts in a line, as a row or words, or as single letters

	String txt = "The dog ate the cat.";
	RiText line1, line2[], line3[];

	public void setup() {
	  
		size(400, 200);

    RiText.defaultFont("Georgia", 32);
    RiText.defaults.showBounds = true;
  
    line1 = new RiText(this, txt, 64, 150); // a line
    line2 = RiText.createWords(this, txt, 64, 40); // words
    line3 = RiText.createLetters(this, txt, 64, 82); // letters
	}

	public void draw() {

		background(255);
		RiText.drawAll();
	}
	
	public static void main(String[] args) {
    String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
    new PixelCompare(testPath).generateRefFile(WordsLettersLines.class.getName());
  }
}
