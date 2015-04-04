package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class FontRotation extends PApplet { 
  
  // TODO: all messed up, compare to JS and fix
  
  RiText rt;

  public void setup() {
      
    size(400, 200);
    RiText.defaultFont(createFont("Times", 80));
    rt = new RiText(this, "ingSpin");
    rt.showBounds(true);
    doRotate();
  }

  public void doRotate()
  {
    rt.rotateTo(2*PI, 3);  // rotate to .2 over 3 seconds
    
    //scale to fullsize over 2 seconds, after waiting 3 seconds  
    rt.rotateTo(0, 2, 3); 
  }

  // called when any behavior (e.g., scaleTo) completes
  void onRiTaEvent(RiTaEvent re) {
    rt = (RiText) re.source();
    if (rt.rotateZ() <= PI) doRotate();
    System.out.println("FontRotation.onRiTaEvent("+rt.rotateZ+")");
  }

  public void draw() {
    background(255);
    rt.draw();
  }
}
