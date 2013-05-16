package rita.support;

import processing.core.PFont;

public class Defaults
{
  public static boolean mouseDraggable = false;

  public static float[] rgba = { 0, 0, 0, 255 };
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
  
  public static float[] boundingBoxStroke = { 0,0,0,255 };
  public static boolean boundingBoxVisible = false;
  public static float boundingBoxStrokeWeight = 1;

  public static PFont font;
  
  public void color(float r, float g, float b, float alpha)
  {
    rgba[0] = r;
    rgba[1] = g;
    rgba[2] = b;
    rgba[3] = alpha;
  }

  public void color(float gray)
  {
    color(gray, gray, gray, 255);
  }

  public void color(float gray, float alpha)
  {
    color(gray, gray, gray, alpha);
  }

  public void color(float r, float g, float b)
  {
    color(r, g, b, 255);
  }
  
}
