package rita.support;

import processing.core.PFont;

public class Defaults // TODO: add to docs
{
  public static boolean mouseDraggable = false;

  public static float[] fill = { 0, 0, 0, 255 };
  public static int alignment = Constants.LEFT;
  public static int motionType = Constants.LINEAR;
  public static float scaleX = 1, scaleY = 1, scaleZ = 1;
  public static float rotateX = 0, rotateY = 0, rotateZ = 0;
  
  public static float fontSize= 14;
  public static float leadingFactor = 1.2f;
  public static String fontFamily= "Times New Roman";
  
  public static float paragraphIndent = 20;
  public static float paragraphLeading =  0;
  public static boolean indentFirstParagraph = false;

  public static boolean showBounds = false;
  public static float[] boundingStroke = { 0,0,0,255 };
  public static float boundingStrokeWeight = 1;

  public static PFont font;
  
}
