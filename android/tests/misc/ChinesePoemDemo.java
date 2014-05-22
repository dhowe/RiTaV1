package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.RiText;


public class ChinesePoemDemo extends PApplet {
  
  String poem = "空山不見人 但聞人語響 返景入深林 復照青苔上";
  String[] trans = {
    "    On empty slopes", "we see nobody,", "    Yet we can hear", "men’s echoes phrases :", " ", "    Returning light", "enters the deep woods", "    And shines again", "on the green mosses."};

  public void setup()
{
  size(400, 600);
  PFont titleFont = loadFont("BiauKai-42.vlw"); 
  PFont transFont = loadFont("Baskerville-16.vlw"); 
  PFont translatorFont = loadFont("Baskerville-Italic-14.vlw"); 
  RiText.defaultFont("LiSong Pro-32.vlw",32);
  RiText author = new RiText(this, "王維", 80, 60);
  RiText title = new RiText(this, "鹿  柴", 80, 100);
  RiText translator = new RiText(this, "‘Deer Park’ by Wang Wei • translation Arthur Cooper", 120, 560);
  author.font(titleFont);
  title.font(titleFont);
  translator.font(translatorFont);
  // Start at (40, 80) & break 'poem' into     // lines, each no more than 50 chars wide
  RiText.createLines(this, poem, 80, 160, 300, 300);       
  RiText[] transLines = RiText.createLines(this, trans, 120, 330); // respect line endings
  for (int i = 0; i < transLines.length; i++) {
    transLines[i].font(transFont);
  }
}

  public void draw() {
  background(255);
  RiText.drawAll();
}



}