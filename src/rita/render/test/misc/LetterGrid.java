package rita.render.test.misc;

import processing.core.PApplet;
import rita.RiTaEvent;
import rita.RiText;

public class LetterGrid extends PApplet
{
  public void setup()
  {
    size(200, 200);

    RiText.defaultFill(255);
    RiText.defaults.alignment = CENTER;
    RiText.defaultFont("Arial", 36);
    
    RiText[] rt = new RiText[6*6];
    
    for (int i = 0, k = 0; i < 6; i++)
    {
      for (int j = 0; j < 6; j++, k++)
      {
        String c = Character.toString((char) (65 + k)); // letters
        rt[k] = new RiText(this, c, 24 + j * 30, 32 + i * 30);
        if (k < 26)
        {

          if ("AEIOU".indexOf(c) >= 0)                   // vowels
          { 
            rt[k].colorTo(new float[] { 204 }, 1);
          }
        }
        else
        {
          rt[k].text(Character.toString((char) (k + 22))); // numbers
          rt[k].fill(153);
        }
      }
    }
  }

  public void draw()
  {
    background(0);
    RiText.drawAll();
  }
  
  public void onRiTaEvent(RiTaEvent re) {

    RiText src = (RiText)re.source();
    src.colorTo(new float[] { random(100,255), random(100,255), 0}, 1);
  }

}