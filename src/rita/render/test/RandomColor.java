package rita.render.test;

import processing.core.PApplet;
import rita.RiTa;
import rita.RiText;

public class RandomColor extends PApplet
{
	RiText rt1,rt2,rt3,rt4;
	public void setup()
	{
		size(800, 700);

		RiTa.start(this);

		background(255);

		RiText.defaultFontSize(30);

		rt1 = new RiText("Random Color", 50,  50);

		rt2 = new RiText("Random Color With Alpha", 50,  150);

		rt3 = new RiText("Random Color", 50,  250);

		rt4 = new RiText("Random Color With Alpha", 50,  350);

	}

	public void draw(){
		background(255);


		float[] color1 = RiText.randomColor(0,100,false);
		rt1.fill(color1);
		fill(0);
		text(color1.length,0,80);
		text(color1[0]+" , "+color1[1]+" , "+color1[2],50,80);

		float[] color2 = RiText.randomColor(0,100,true);
		rt2.fill(color2);
		fill(0);
		text(color2.length,0,180);
		text(color2[0]+" , "+color2[1]+" , "+color2[2]+" , "+color2[3],50,180);

		float[] color3 = RiText.randomColor(100,255,false);
		rt3.fill(color3);
		fill(0);
		text(color3.length,0,280);
		text(color3[0]+" , "+color3[1]+" , "+color3[2],50,280);

		float[] color4 = RiText.randomColor(100,255,true);
		rt4.fill(color4);
		fill(0);
		text(color4.length,0,380);
		text(color4[0]+" , "+color4[1]+" , "+color4[2]+" , "+color4[3],50,380);

		fill(0);
		float rand1 = RiText.random();
		text( rand1,0,480);
		text((RiText.random() < 1 && rand1> 0 )+"",100,480);

		float rand2 = RiText.random(2.2f);
		text( rand2,0,580);
		text((rand2 < 2.2 && rand2 > 0 )+"",100,580);

		fill(0);
		float rand3 = RiText.random(2.2f,5.5f);
		text(rand3,0,680);
		text((rand3 < 5.5 && rand3 > 2.2 )+"",100,680);

		
		

		RiText.drawAll();

	}
}
