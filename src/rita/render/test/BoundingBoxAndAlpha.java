package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.support.RiTimer;

public class BoundingBoxAndAlpha extends PApplet {

	public void setup() {

		size(900, 660);

		RiTa.start(this);

		RiText.defaults.showBounds = true;
		RiText.defaults.fontSize = 64;

		new RiText("Default", 200,  100);
		new RiText("Center", 200, 200).align(CENTER);
		new RiText("Right", 200, 300).align(RIGHT);
		new RiText("Left", 200, 400).align(LEFT);

		background(255);
		line(200, 0, 200, height);


		RiText.defaultFont("Times", 32);

		for (int i = 0; i < 11; i++)
			new RiText(this, "alpha " + (i * 10), 400, 
					(i + 1) * 38).alpha(i * 10).draw();

		RiText.defaults.showBounds = false;

		RiText.defaultFont("Times",22);

		for (int i = 0; i < 11; i++)
			new RiText(this, "alpha " + (i * 10), 600, 
					(i + 1) * 38).alpha(i * 10).draw();

		RiText rt = new RiText("D.Fill_200", 50,  500);
		rt.defaultFill(200);
		RiText rt5 = new RiText("D.Fill_10", 50,  550);
		rt5.defaultFill(10);
		RiText rt2 = new RiText("D.Fill_200_50A", 220,  500);
		rt2.defaultFill(200,50);
		RiText rt6 = new RiText("D.Fill_200_0A", 220,  550);
		rt6.defaultFill(200,0);
		RiText rt3 = new RiText("D.Fill_RED", 450,  500);
		rt3.defaultFill(255,0,0);
		fill(0);
		text(rt3.defaultFill()[0]+","+rt3.defaultFill()[1]+","+rt3.defaultFill()[2]+","+rt3.defaultFill()[3],450,  600);
		RiText rt8 = new RiText("D.Fill_BLUE", 450,  500);
		rt8.defaultFill(0,0,255);
		RiText rt4 = new RiText("D.Fill_RED_100A", 650,  500);
		rt4.defaultFill(255,0,0,100);
		RiText rt7 = new RiText("D.Fill_RED_0A", 650,  550);
		rt7.defaultFill(255,0,0,0);
		fill(0);
		text(rt7.defaultFill()[0]+","+rt7.defaultFill()[1]+","+rt7.defaultFill()[2]+","+rt7.defaultFill()[3],650,  600);
		
		/*
		RiText rt9 = new RiText("Random Color", 50,  650);
		RiText.randomColor();
		rt9.fill(RiText.randomColor(0,100,false));
		fill(0);
		text(rt7.defaultFill()[0]+","+rt7.defaultFill()[1]+","+rt7.defaultFill()[2]+","+rt7.defaultFill()[3],50,  650);
		*/
		
		RiText.drawAll();
	}


}
