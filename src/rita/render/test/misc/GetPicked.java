package rita.render.test.misc;

import processing.core.PApplet;
import rita.RiText;

public class GetPicked extends PApplet
{
  String txt = "A huge lizard was discovered drinking out of the fountain today. It was not menacing anyone, it was just very thirsty. A small crowd gathered and whispered to one another, as though the lizard would understand them if they spoke in normal voices. The lizard seemed not even a little perturbed by their gathering. It drank and drank, its long forked tongue was like a red river hypnotizing the people, keeping them in a trance-like state. 'It's like a different town,' one of them whispered. 'Change is good,' the other one whispered back.";

  public void setup()
  {
    size(400, 400);
    RiText.defaultFont("Georgia", 16);
    RiText.createWords(this, txt, 20, 20, 360, 360, 20);
  }

  public void draw()
  {
    background(255);
    RiText.drawAll();
  }

  public void mouseMoved()
  {
    RiText[] picks = RiText.picked(mouseX, mouseY);
    for (int i = 0; picks.length>0 && i < RiText.instances.size(); i++)
    {
      RiText rt = RiText.instances.get(i);
      rt.showBounds(rt == picks[0]);
    }
  }
}
