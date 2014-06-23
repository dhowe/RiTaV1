import rita.*;

 void setup() {
    
    size(200, 200);
    background(255);

    // create a RiText, set its color, and draw it
    RiText rt = new RiText(this, "SIMPLE");
    rt.fill(200,100,0).draw();

    line(rt.x, 0, rt.x, height);
    line(0, rt.y, width, rt.y);
  }
