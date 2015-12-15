package rita.render;

import java.util.Map;

import processing.core.PFont;
import rita.RiTa;
import rita.support.Constants;

public abstract class Defaults implements Constants 
{
  static { reset(); }

  public static int _originalDefaultFontSize = 14;
  
  public static int alignment, motionType;
  public static float fontSize, boundingStrokeWeight, leadingFactor = 1.2f;
  
  public static String fontFamily;
  
  public static float paragraphIndent, paragraphLeading;
  public static boolean showBounds, indentFirstParagraph;

  public static float[] fill, boundingStroke, boundingFill, rotateXYZ, scaleXYZ;

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
    
    scaleXYZ = new float[] { 1, 1, 1 };
    rotateXYZ = new float[] { 0, 0, 0 };

    fontSize = _DEFAULT_FONT_SIZE;
    leadingFactor = 1.2f;
    boundingStrokeWeight = 1;
    paragraphIndent = 30;
    paragraphLeading = 0;
    
    fontFamily = (RiTa.env() == RiTa.ANDROID) ? "Serif" : "Times New Roman";

    indentFirstParagraph = false;
    showBounds = false;
  }
}
