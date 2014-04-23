package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.test.PixelCompare;

public class SplitText extends PApplet {
  
  int x = 90, y = 50, w = 350, h = 500;
	String txt = "The lizard was not menacing anyone, it was just very thirsty.";

	public void setup() {

		size(720, 100);
		RiText.defaultFont("Times", 28);
		
		RiText rt = new RiText(this,txt,10,25);
		
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
		
		background(250);
    RiText.drawAll();
	}
	
	public static void main(String[] args)
  {
    String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
    new PixelCompare(testPath).generateRefFile(SplitText.class.getName());
  }

}
