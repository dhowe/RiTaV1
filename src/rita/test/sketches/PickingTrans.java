package rita.test.sketches;

import processing.core.PApplet;
import rita.RiTa;
import rita.RiText;

public class PickingTrans extends PApplet
{
  RiText trans, picked;
  RiText[][] rts = new RiText[5][];

  public void setup()
  {
    size(400, 400);
    RiText.defaultFont("Georgia", 16);
    //RiText.defaults.showBounds = true;
    trans = new RiText(this);
    mouseClicked();
  }

  public void draw()
  {
    background(255);

    // returns an array, in case multiple RiTexts are picked
    RiText[] prt = RiText.picked(mouseX, mouseY);

    RiText over = prt.length > 0 ? prt[0] : null;
    
    if (over == null) { // nothing
      picked = null;
      fadeInAll();
      trans.alpha(0);
    }
    else if (over != picked) { // new picked
      picked = over;
      showTranslation();
    }
    // otherwise stays same picke
    
    RiText.drawAll();
  }

  public void showTranslation()
  {
    fadeOutAll();

    picked.showBounds(true);
    trans.alpha(255);
    String[] t = {"a", "word", "longer word"};
    trans.text(t[(int) random(t.length)]).align(RIGHT);
    trans.position(picked.x - 20, picked.y);
    System.out.println(trans.x+","+trans.y);
  }

  public void fadeOutAll()
  {
    for (int j = 0; j < rts.length; j++)
      for (int i = 0; i < rts[j].length; i++)
      {
        if (picked != rts[j][i])
          rts[j][i].alpha(0);
          //rts[j][i].fadeOut(.1f);
    }
  }
  
  public void fadeInAll()
  {
    for (int j = 0; j < rts.length; j++)
      for (int i = 0; i < rts[j].length; i++)
      {
        if (picked != rts[j][i])
          rts[j][i].alpha(255);
          //rts[j][i].fadeIn(.1f);
    }
  }

  public void mouseClicked()
  {
    for (int j = 0; j < rts.length; j++)
      RiText.dispose(rts[j]);
    
    int xOff = width - 100, yOff = 50;
    for (int j = 0; j < rts.length; j++)
    {
      yOff = 50;
      rts[j] = new RiText[4];
      for (int i = 0; i < rts[j].length; i++)
        rts[j][i] = new RiText(this, (char) (random(26) + 65), xOff, yOff += 50);
      xOff -= 50;
    }
  }

}
