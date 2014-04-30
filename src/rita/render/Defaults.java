package rita.render;

import java.util.Map;

import processing.core.PFont;
import rita.RiTa;
import rita.support.Constants;

public abstract class Defaults implements Constants 
{
  public static boolean mouseDraggable = false;

  public static float[] fill = { 0, 0, 0, 255 };
  public static int alignment = RiTa.LEFT;
  public static int motionType = Constants.LINEAR;
  public static float scaleX = 1, scaleY = 1, scaleZ = 1;
  public static float rotateX = 0, rotateY = 0, rotateZ = 0;
  
  public static float fontSize = DEFAULT_FONT_SIZE;
  public static float leadingFactor = 1.2f;
  
  public static String fontFamily = (RiTa.env() == RiTa.ANDROID) ? "Serif" : "Times New Roman";
  
  public static float paragraphIndent = 30;
  public static float paragraphLeading =  0;
  public static boolean indentFirstParagraph = false;

  public static boolean showBounds = false;
  public static float[] boundingStroke = { 0,0,0,255 };
  public static float boundingStrokeWeight = 1;

  public static Map fonts; // cache
  public static PFont font;
  
  private Defaults() // no instances
  {
  }
  
  public static void reset()
  {
    fonts = null;
    font = null;
    
    mouseDraggable = false;

    fill = new float[] { 0, 0, 0, 255 };
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
    boundingStroke = new float[] { 0, 0, 0, 255 };
    boundingStrokeWeight = 1;
  }
}
