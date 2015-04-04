package rita.test.sketches;

import processing.core.PApplet;
import rita.RiText;

public class Picking extends PApplet
{
  String txt = "A huge lizard was discovered drinking out of the fountain today. It was not menacing anyone, it was just very thirsty. A small crowd gathered and whispered to one another, as though the lizard would understand them if they spoke in normal voices. The lizard seemed not even a little perturbed by their gathering. It drank and drank, its long forked tongue was like a red river hypnotizing the people, keeping them in a trance-like state. 'It's like a different town,' one of them whispered. 'Change is good,' the other one whispered back.";

  RiText[] rts;

  public void setup()
  {

    size(400, 400);
    RiText.defaultFont("Georgia", 16);
    rts = RiText.createWords(this, txt, 20, 20, 360, 360);
  }

  public void draw()
  {

    background(255);

    // returns an array, in case multiple RiTexts are picked
    RiText[] prt = RiText.picked(mouseX, mouseY);

    RiText picked = prt.length > 0 ? prt[0] : null;

    for (int i = 0; i < rts.length; i++)
    {

      rts[i].showBounds(picked == rts[i]);
      rts[i].draw();
    }
  }
}
