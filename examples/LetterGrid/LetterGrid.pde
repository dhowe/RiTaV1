import rita.*;

void setup()
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
      char c = (char) (65 + k); // letters
      
      rt[k] = new RiText(this, c, 24 + j * 30, 32 + i * 30);
      if (k < 26)
      {
        if (rt[k].match("[AEIOU]").length>0)                  // vowels
          rt[k].colorTo(new float[] { 204 }, 1.0);
      }
      else
      {
        rt[k].text((char) (k + 22)); 						// numbers
        rt[k].fill(153);
      }
    }
  }
}

void draw()
{
  background(0);
  RiText.drawAll();
}

void onRiTaEvent(RiTaEvent e) {
  
  RiText src = (RiText)e.source();
  src.colorTo(new float[] { 
    random(100, 255), random(100, 255), 0
  }, 1);
}