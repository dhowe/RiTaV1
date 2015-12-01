import rita.*;

RiText[] rt = new RiText[5];

void setup() {

    size(900, 560);
    background(255);
    RiTa.start(this); // not needed in JS

    //RiText.defaults.showBounds = true;
      RiText.defaultFontSize(20);
    
      for(int i=0; i< rt.length; i++){
           rt[i] = new RiText("BoundingBox " +i + " ", 200, 100+ 20*i);
      }

    RiText.boundingBox(rt);
  
    RiText.drawAll();
  
}

void draw() {

}
