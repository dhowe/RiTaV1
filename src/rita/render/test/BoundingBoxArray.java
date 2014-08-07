package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.support.RiTimer;

public class BoundingBoxArray extends PApplet {


RiText[] rt = new RiText[5];

public void setup() {

		size(900, 560);
		background(255);
		RiTa.start(this); // not needed in JS

		//RiText.defaults.showBounds = true;
    	RiText.defaultFontSize(20);
    
    	for(int i=0; i< rt.length; i++){
    			rt[i] = new RiText("BoundingBox " +i + " ", 200, 100+ 20*i);
    	}
//TODO not exist yet
	//	RiText.boundingBox(rt);

	
		RiText.drawAll();
	
}

}
