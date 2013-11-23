package rita.render.test;

import processing.core.PApplet;
import rita.*;

public class DefaultFont extends PApplet {



	public void setup() {

		size(600, 400);
		
	  RiText rt;

		RiText.defaultFont("Batang", 100);

		rt = new RiText(this, "Batang-100", 0, 280);

		rt = new RiText(this, "Batang-14", 30, 20);
		rt.font("Batang", 14);

		rt = new RiText(this, "Batang-24", 60, 80);
		rt.scale(24 / 100f);

		rt = new RiText(this, "Arial-50", 180, 170);
		rt.font("Arial", 50);

		rt = new RiText(this, "Times-100", 0, 400);
		rt.font("Times New Roman", 100);

		RiText.defaultFont("Georgia", 32);

		rt = new RiText(this, "Default", 400, 80);
		
		background(255);
		RiText.drawAll();
	}

}
