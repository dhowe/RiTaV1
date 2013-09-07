package rita.test.sketches;

import processing.core.PApplet;
import rita.RiText;
import rita.RiTextIF;

public class RandomFont extends PApplet
{
  
  String[] fonts = { "arial","times","verdana","courier"};
  
  public void setup()
  {       
    RiTextIF[] rts = RiText.createLetters(this, "transText 2013", 20, 20, 200);
    
    for (int i = 0; i < rts.length; i++)     
      rts[i].font(fonts[i%fonts.length]);
    
    RiText.drawAll();
  }
  
}
