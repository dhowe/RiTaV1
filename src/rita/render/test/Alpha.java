package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.support.RiTimer;

public class Alpha extends PApplet {
  
	public void setup() {
	  
		size(400, 400);

  
		RiText.defaultFont("Times", 64);
		
		RiText rt = new RiText(this, "Alpha 50", 100,  100);
		rt.alpha(50);
		System.out.println(rt.alpha());
		new RiText(this, "Alpha 00" , 100, 200).alpha(0);
		
		new RiText(this, "Alpha 100" , 100, 300).alpha(100);

		background(255);

		RiText.drawAll();
		
		
	}


}
