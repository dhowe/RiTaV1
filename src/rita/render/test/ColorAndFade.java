package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.test.PixelCompare;

public class ColorAndFade extends PApplet {

	RiText rt,rt2,rt3,rt4,rt5,rt6,rt7,rt8,rt9,rt10;

	public void setup() {

		size(400, 800);

		RiText.defaultFont("Times", 20);


		rt = new RiText(this, "1.ColorTo gray in 2 sec", 100,  50);
		rt.colorTo(200, 2);
		rt.showBounds(true);

		float[] c = {0,0,255};
		rt6 = new RiText(this, "2.ColorTo Bluein 2 sec" , 100, 100);
		rt6.colorTo(c,2);
		rt6.showBounds(true);
		rt10 = new RiText(this, "3.ColorTo Blue 5s delay 2s" , 100, 150);
		rt10.colorTo(c,5,2);

		rt2 = new RiText(this, "4.fade out and in" , 100, 200);
		rt2.fadeOut(2);

		rt2.fadeIn(2,4);

		rt4 = new RiText(this, "5.fade out and in at 4s" , 100, 250);
		rt4.showBounds(true); 
		rt4.fadeOut(2);
		rt4.fadeIn(2,4);


		rt3 = new RiText(this, "6.fade out delay 2s and in at 8s" , 100, 300);
		rt3.fadeOut(2,2);

		rt3.fadeIn(2,8);


		rt5 = new RiText(this, "7.fade out delay 2s and in at 8s" , 100, 350);
		rt5.showBounds(true);
		rt5.fadeOut(2,2);

		rt5.fadeIn(2,8);


		rt7 = new RiText(this, "8.Null" , 100, 400);
		rt5.showBounds(true);
		rt7.textTo("8.Changed Text",3);


		rt8 = new RiText(this, "9.Null" , 100, 450);

		rt8.textTo("9.Changed Text delay3s",3,5); 

		rt9 = new RiText(this, "10.Null" , 100, 500);
		rt9.showBounds(true);
		rt9.textTo("10.Changed Text delay3s",3,5);	



		background(255);

		//RiText.drawAll();
	}

	public void draw() {

		background(255);
		fill(255,0,0);
		//text(millis()+ "" ,10,750);
		text(rt.isVisible()+"",50,50);
		text(rt6.isVisible()+"",50,100);
		text(rt10.isVisible()+"",50,150);
		text(rt2.isVisible()+"",50,200);
		text(rt4.isVisible()+"",50,250);
		text(rt3.isVisible()+"",50,300);
		text(rt5.isVisible()+"",50,350);
		text(rt7.isVisible()+"",50,400);
		text(rt8.isVisible()+"",50,450);
		text(rt9.isVisible()+"",50,500);
		RiText.drawAll();
	}


}
