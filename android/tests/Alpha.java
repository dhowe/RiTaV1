package rita.render.test;

import processing.core.PApplet;
import rita.RiText;
import rita.test.PixelCompare;
import rita.test.RiTextGraphicsTest;

public class Alpha extends PApplet
{
  public void setup()
  {

    size(400, 400);
    background(255);

    RiText.defaultFont("Times", 64);

    for (int i = 0; i < 10; i++)
      new RiText(this, "alpha " + (i * 10), 100, 
          (i + 1) * 38).alpha(i * 10).draw();
  }

  public static void main(String[] args)
  {
    System.out.println(RiTextGraphicsTest.PATH);
    new PixelCompare(RiTextGraphicsTest.PATH)
      .generateRefImage(Alpha.class.getName());
  }

}
