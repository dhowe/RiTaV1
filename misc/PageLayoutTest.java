package rita.render.test;

import processing.core.PApplet;
import rita.RiText;
import rita.support.Rect;

public class PageLayoutTest extends PApplet {

  String file = "/Users/dhowe/Documents/eclipse-workspace/Readers2013/src/data/cayley/poeticCaption.txt";
  int x=40, y=40, w=430, h=640;
  
  public void setup() {

		size(1000, 720);
		
    RiText.defaultFont("Times", 20);

    PageLayout pl = new PageLayout(this, new Rect(x,y,w,h), width/2, height);
    pl.layoutFromFile(file,0);
    String txt = pl.remainingText();
    
    PageLayout pl2 = new PageLayout(this, new Rect(width/2+x,y,w,h), width/2, height);
    pl2.layout(txt);
    System.out.println(pl2.remainingText());
    
    noFill();
	}

	public void draw() {
		background(250);
		rect(x,y,w,h);
		rect(width/2+x,y,w,h);
		RiText.drawAll();
	}

}
