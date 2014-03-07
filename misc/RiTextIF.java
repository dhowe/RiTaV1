package rita;

import java.util.Map;

public interface RiTextIF
{
  public RiText draw();
  public RiText copy();   
  public RiText stopBehavior(int id); 
  public RiText stopBehaviors(); 
  
  public float[] boundingBox();
  public float[] center(); 
  public float charOffset(int i);
  public float wordOffset(int i);
  
  public boolean contains(String s); 
  public float distanceTo(float x, float y); 
  public RiText[] splitLetters(); 
  public RiText[] splitWords(); 

  public float textHeight(); 
  public float textAscent(); 
  public float textDescent(); 
  public float textWidth(); 
   
  public int fadeIn(float seconds, float delay); 
  public int fadeOut(float seconds, float delay,  boolean destroyOnComplete); 
  public int moveTo(float newX, float newY, float seconds, float delay); 
  public int colorTo(float[] colors, float seconds, float delay, int type); 
  public int rotateTo(float angleInRadians, float seconds, float delay); 
  public int scaleTo(float theScale, float seconds, float delay); 
  public int textTo(String newText, float seconds);
  
  // Setters / Getters
  
  public PFont font(); // ???
  public RiText font(String name, float size);
  public RiText fontSize(float f); 
  public float fontSize(); 
  
  public RiText align(int i);
  public int align();
  
  public RiText alpha(float a); 
  public float alpha(); 
  
  public RiText rotate(float r);
  public RiText rotate(float rx, float ry, float rz);
  public float[] rotate(); 
  
  public RiText scale(float s);
  public RiText scale(float x, float y, float z);
  public float[] scale(); 
  
  public RiText color(float r, float g, float b, float a);
  public RiText color(float[] color);
  public float[] color(); 
  
  public RiText text(String s); 
  public String text(); 
   
  public boolean isVisible();
  
  public RiText showBoundingBox(boolean b); 
  public boolean showBoundingBox(); 
  
  public RiText motionType(int type); 
  public int motionType();
  
  public RiText position(float x, float y); 
  public RiText position(float x, float y, float z); 
  public float[] position(); 
  
  // from RiString =====================
  
  RiText replaceFirst(String regex, String t);
  RiText replaceLast(String regex, String t);
  RiText replaceAll(String regex, String t);
  //RiText[] split(String regex);
  
  boolean startsWith(String cs);
  boolean endsWith(String cs); 
  boolean equalsIgnoreCase(String cs);

  
  // these return 'this'
  RiText trim();
  RiText toLowerCase();
  RiText toUpperCase();
  
  RiText removeCharAt(int idx);
  RiText insertCharAt(int idx, char c);
  RiText replaceCharAt(int idx, String s);
  
  RiText removeWordAt(int wordIdx);
  RiText insertWordAt(int wordIdx, String s);
  RiText replaceWordAt(int wordIdx, String s);
  
  // java-style overrides
  RiText concat(String cs);
  RiText concat(RiText cs);
  
  // equivalent to js-style getter/setter
  
  int indexOf(String s);  
  RiText analyze();
  char charAt(int idx);
  String get(String s);
  Map features();
  char[] toCharArray();
  int lastIndexOf(String s);
  int length();
  int wordCount();
  
  String posAt(int idx);
  String slice(int i, int j); // ?
  String substring(int i, int j);
  String substr(int i, int j); // ?
  String wordAt(int idx);
  
  String[] pos();
  String[] words();
  String[] match(String regex);
  
}
