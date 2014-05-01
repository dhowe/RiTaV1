package rita.render;

import java.util.Map;

import processing.core.PFont;
import rita.RiTa;
import rita.support.Constants;

public abstract class Defaults implements Constants 
{
  static { reset(); }

  public static int alignment, motionType;
  public static float scaleX = 1, scaleY = 1, scaleZ = 1; // array?
  public static float rotateX = 0, rotateY = 0, rotateZ = 0; // array?
  public static float fontSize, boundingStrokeWeight, leadingFactor = 1.2f;
  
  public static String fontFamily;
  
  public static float paragraphIndent, paragraphLeading;
  public static boolean showBounds, indentFirstParagraph;

  public static float[] fill, boundingStroke, boundingFill;

  public static Map fonts; // cache
  public static PFont font;
  
  private Defaults() // no instances
  {
  }
  
  public static void reset()
  {
    fonts = null;
    font = null;
    
    fill = new float[] { 0, 0, 0, 255 };
    boundingStroke = new float[] { 0, 0, 0 };
    boundingFill = new float[] { -1, -1, -1 };
    
    alignment = RiTa.LEFT;
    motionType = Constants.LINEAR;
    
    scaleX = 1;
    scaleY = 1;
    scaleZ = 1;
    
    rotateX = 0;
    rotateY = 0;
    rotateZ = 0;

    fontSize = DEFAULT_FONT_SIZE;
    leadingFactor = 1.2f;

    fontFamily = (RiTa.env() == RiTa.ANDROID) ? "Serif" : "Times New Roman";

    paragraphIndent = 30;
    paragraphLeading = 0;
    indentFirstParagraph = false;

    showBounds = false;

    boundingStrokeWeight = 1;
  }
}
