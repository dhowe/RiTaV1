package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.support.RiTimer;

public class DisposeAll extends PApplet {

	public void setup() {

		size(800, 700);

		RiTa.start(this);

		RiText.defaults.showBounds = true;
		RiText.defaultFontSize(64); 

		new RiText("Default", 200,  100);

		background(255);


		RiText.defaultFont("Times", 32);

		for (int i = 0; i < 11; i++)
			new RiText(this, "alpha " + (i * 10), 400, 
					(i + 1) * 38).alpha(i * 10);

		RiText.defaults.showBounds = false;

		RiText.defaultFont("Times",32);

		for (int i = 0; i < 11; i++)
			new RiText(this, "alpha " + (i * 10), 600, 
					(i + 1) * 38).alpha(i * 10);
		
	    RiText rt =   new RiText("dispose me", 350, 650);
	    RiText rt2 =  new RiText("dispose me", 550, 650);
	    rt2.showBounds(true);
	    
	    RiText[] rtArray = new RiText[11];
	        for (int i = 0; i < 11; i++)
	     		rtArray[i] = new RiText(this, "alpha " + (i * 10), 200, (i + 1) * 38).alpha(i * 10);
	    
	    background(255);
	    RiText rt3 =  new RiText("this should be the only one showing", 50, 650);
	    rt3.draw();
	  
	    RiText.disposeAll();
	    
	    RiText[] rtArray2 =new RiText[11];
	        for (int i = 0; i < 11; i++)
	     		rtArray2[i] = new RiText(this, "alpha " + (i * 10), 300, (i + 1) * 38).alpha(i * 10);
	     		
	    RiText rt4;
	    rt4 = new RiText(this, "dispose me " , 400, 100);
	    RiText.dispose(rtArray2); 
	    RiText.dispose(rt4); 
	     
	    RiText.drawAll();
	}


}
