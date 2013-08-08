package rita.test;

import processing.core.PApplet;
import rita.RiText;

public class HeadlessRiText extends PApplet
{
  RiText rt; 
  boolean headless = false;
  
  public void setup()
  {
    rt = new RiText(null, "hello", 100, 100);
  }
  
  public void draw()
  {
    rt.x++;
    System.out.println(rt+" "+rt.x+","+rt.y);
  }
}
