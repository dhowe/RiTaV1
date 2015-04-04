package rita.render.test;

import processing.core.PApplet;
import rita.*;
import rita.support.RiTimer;

public class BoundingBoxAndAlpha extends PApplet {

	public void setup() {

		size(900, 660);

		RiTa.start(this);

		RiText.defaults.showBounds = true;
		RiText.defaultFontSize(64);
    
		new RiText("Default", 200,  80);
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
		RiText.defaultFill(200);
		new RiText("D.Fill_200", 50,  500);
		RiText.defaultFill(10);
		new RiText("D.Fill_10", 50,  550);
		RiText.defaultFill(200,50);
		new RiText("D.Fill_200_50A", 220,  500);
		RiText.defaultFill(200,0);
		new RiText("D.Fill_200_0A", 220,  550);
		RiText.defaultFill(255,0,0);
		new RiText("D.Fill_RED", 450,  500);

		fill(0);
		float[] df = RiText.defaultFill();
		text(df[0]+","+df[1]+","+df[2]+","+df[3],450,  600);
		RiText.defaultFill(0,0,255);
		new RiText("D.Fill_BLUE", 450,  550);
		RiText.defaultFill(255,0,0,100);
		new RiText("D.Fill_RED_100A", 650,  500);
		RiText.defaultFill(255,0,0,0); // invisible
		new RiText("D.Fill_RED_0A", 650,  550);

		df = RiText.defaultFill();
		fill(0);
		text(df[0]+","+df[1]+","+df[2]+","+df[3],650,  600);
		
	
		RiText.drawAll();
	}


}
