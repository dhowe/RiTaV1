package rita.render.test;

import processing.core.PApplet;
import rita.RiText;

public class Alignments extends PApplet
{
  public void setup()
  {
    size(800, 400);

    RiText.defaults.showBounds = true;
    
    new RiText(this, "Left", 200, 100).align(LEFT);
    new RiText(this, "Center", 200, 200).align(CENTER).fontSize(32);
    new RiText(this, "Right", 200, 300).align(RIGHT).fontSize(80);
   
    RiText.defaultFontSize(48);

    new RiText(this, "Left", 400, 100).align(LEFT);
    new RiText(this, "Center", 400, 200).align(CENTER);
    new RiText(this, "Right", 400, 300).align(RIGHT);
    
    RiText.defaultFont("Times", 64);

    new RiText(this, "Left", 650, 100).align(LEFT);
    new RiText(this, "Center", 650, 200).align(CENTER);
    new RiText(this, "Right", 650, 300).align(RIGHT);

    background(255);

    line(200, 0, 200, 400);
    line(400, 0, 400, 400);
    line(650, 0, 650, 400);

    RiText.drawAll();
  }
}
