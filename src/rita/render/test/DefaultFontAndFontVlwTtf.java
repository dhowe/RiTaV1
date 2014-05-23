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

	  ///////////////////// statics ////////////////////////

	  RiText.defaultFont("Courier");
	  rt = new RiText(this, "CourierDefault", 200, 50);

	  RiText.defaultFont("Courier", 18);  
	  rt = new RiText(this, "CourierDefault-18", 200, 100);

	  RiText.defaultFont("Ziggurat32.vlw");
	  rt = new RiText(this, "Default-Zig-vlw", 200, 170);
	  //System.out.println(RiText.defaults.fontFamily+"/"+RiText.defaults.fontSize+
	  //"\n"+rt.font()+"/"+ ((PFont)rt.font()).getSize()+"/fontSize="+rt.fontSize());
	  RiText.defaultFont("mono0755.ttf");
	  rt = new RiText(this, "mono0755-ttf", 200, 210);
	  ///////////////////// instances ////////////////////////

	  rt = new RiText(this, "Courier", 200, 250);
	  rt.font("Courier");

	  rt = new RiText(this, "Courier18", 200, 300);
	  rt.font("Courier", 18);

	  rt = new RiText(this, "Zig-Vlw", 200, 370);
	  rt.font("Ziggurat32.vlw");

	  rt = new RiText(this, "Zig-Vlw", 200, 450);


	  text(rt.fontSize(), 200, 400);

	  rt.font("Ziggurat32.vlw");
	  rt.fontSize(50);

	  text(rt.fontSize(), 200, 480);




	  rt = new RiText(this, "mono0755", 200, 520);
	    rt.font("mono0755.ttf");
	    rt.fontSize(20);
	  rt = new RiText(this, "mono0755", 200, 560);


	  RiText.defaultFont("Batang", 100);

	  rt = new RiText(this, "Batang-80", 540, 280);

	  rt = new RiText(this, "Batang-14", 530, 20);
	  rt.font("Batang", 14);

	  rt = new RiText(this, "Batang-24", 530, 80);
	  rt.scale(24 / 100f);

	  rt = new RiText(this, "Arial-50", 700, 170);
	  rt.font("Arial", 50);

	  rt = new RiText(this, "Times-60", 680, 400);
	  rt.font("Times New Roman", 60);

	  RiText.defaultFont("Georgia", 32);

	  rt = new RiText(this, "Default", 700, 80);




	  RiText.drawAll();
  }
  
  public static void main(String[] args) {
    String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
    new PixelCompare(testPath).generateRefImage(DefaultFontAndFontVlwTtf.class.getName());
  }
}
