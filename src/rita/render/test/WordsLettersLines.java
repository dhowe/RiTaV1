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

    size(400, 200);

    RiText.defaults.fontSize = 32;
    RiText.defaults.showBounds = true;

    line1 = new RiText(this, txt, 64, 50); // lines
    line2 = RiText.createWords(this, txt, 64, 80); // words
    line3 = RiText.createLetters(this, txt, 64, 130); // letters

    background(255);
    RiText.drawAll();
  }

  public static void main(String[] args)
  {
    String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
    new PixelCompare(testPath).generateRefImage(WordsLettersLines.class.getName());
  }
}
