package rita.test.sketches;

import processing.core.PApplet;
import rita.RiTaEvent;
import rita.RiText;

public class TypewriterNoAudio extends PApplet
{
  RiText rt;
  int idx, tid;
  float y = 30;  
  String s = "This is a typewriter typing a line.";

  public void setup() 
  { 
    size(550,550);

    RiText.defaultFont("courier", 24);
    rt = new RiText(this,"", 20, y);
    tid = RiText.timer(this, .1f);
    //setInterval(onRiTaEvent,100);
  }

  void onRiTaEvent(RiTaEvent re) { 
    
    rt.text(s.substring(0, idx));
    
    if (idx++ == s.length()) { // a new line
      
      RiText.pauseTimer(tid, .8f);
      
      y = rt.y + 30;
      rt = new RiText(this, "", 20, y);
      //bell.play();
      idx = 0;
    }   
    else if (!rt.endsWith(" ")) 
      ;//key.play();    
  }

  public void draw() {
    background(255);
    RiText.drawAll();
  }



}
