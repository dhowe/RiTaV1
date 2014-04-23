package rita.render.test;

import rita.RiText;
import rita.test.PixelCompare;

// TODO: add warning message to JS code about vlw files...
public class FontVlwTest extends processing.core.PApplet
{
  RiText rt;
  public void setup()
  {
    size(400,400);

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
    
    ///////////////////// instances ////////////////////////

    rt = new RiText(this, "Courier", 200, 250);
    rt.font("Courier");
    
    rt = new RiText(this, "Courier18", 200, 300);
    rt.font("Courier", 18);
    
    rt = new RiText(this, "Zig-Vlw", 200, 370);
    rt.font("Ziggurat32.vlw");
    
    RiText.drawAll();
  }
  
  public static void main(String[] args) {
    String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
    new PixelCompare(testPath).generateRefFile(FontVlwTest.class.getName());
  }
}
