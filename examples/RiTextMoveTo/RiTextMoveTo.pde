
import rita.*;

RiText rt;

void setup() {

  // start in the upper left corner
  rt = new RiText(this, "hello", 0, 10);
  
  // measure the width of the text
  float tw = rt.textWidth();
  
  // move to the lower right over 2 sec
  rt.moveTo(width-tw, height, 2);    
}

void draw() {

  background(255);
  rt.draw();
}