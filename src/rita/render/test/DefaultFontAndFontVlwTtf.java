package rita.render.test;

import rita.RiText;
import rita.test.PixelCompare;

// TODO: add warning message to JS code about vlw files...
public class DefaultFontAndFontVlwTtf extends processing.core.PApplet
{
	RiText rt;
	public void setup()
	{
		size(800, 600);

		RiText.defaults.alignment = CENTER;
		//RiText.defaults.showBounds = true;

		///////////////////// statics ////////////////////////
		RiText.defaultFont("Courier");
		rt = new RiText(this, "CourierDefault(14)", 200, 50);

		RiText.defaultFont("Courier", 18);  
		rt = new RiText(this, "CourierDefault-18", 200, 100);

		RiText.defaultFont("Ziggurat32.vlw");
		rt = new RiText(this, "Default-Zig-vlw(32)", 200, 160);
		//text(rt.fontSize(), 200, 200);

		RiText.defaultFont("wendy.ttf");
		rt = new RiText(this, "wendy-ttf(14)", 100, 210);

		///////////////////// instances ////////////////////////

		rt = new RiText(this, "Courier(14)", 100, 250);
		rt.font("Courier");

		rt = new RiText(this, "Courier18", 100, 300);
		rt.font("Courier", 18);

		rt = new RiText(this, "Zig-Vlw(32)", 200, 370);
		rt.font("Ziggurat32.vlw");
		text(rt.fontSize(), 200, 390);

		rt = new RiText(this, "Zig-Vlw(50)", 200, 475);
		rt.font("Ziggurat32.vlw");
		rt.fontSize(50);
		text(rt.fontSize(), 200, 520);

		rt = new RiText(this, "wendy-20", 600, 500);
		rt.font("wendy.ttf");
		rt.fontSize(20); 

		rt = new RiText(this, "wendy(14)", 600, 540); 

		RiText.defaultFont("Batang", 100);

		rt = new RiText(this, "Batang-100", 490, 280);

		rt = new RiText(this, "Batang-14", 530, 20);
		rt.font("Batang", 14);

		rt = new RiText(this, "Batang-24", 430, 80);
		rt.scale(24 / 100f);

		rt = new RiText(this, "Arial-50", 700, 170);
		rt.font("Arial", 50);

		rt = new RiText(this, "Times-60", 680, 400);
		rt.font("Times New Roman", 60);

		RiText.defaultFont("Georgia", 32);

		rt = new RiText(this, "Georgia(32)", 700, 80);

		RiText.dispose(new RiText(this, "disposed and should not appear", 400, 200));


		RiText.drawAll();
	}

	public static void main(String[] args) {
		String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
		new PixelCompare(testPath).generateRefImage(DefaultFontAndFontVlwTtf.class.getName());
	}
}
