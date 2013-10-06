package rita;

import java.util.List;
import java.util.Map;

public interface RiTextIF extends rita.support.Constants
{
  public RiTextIF draw();
  public RiTextIF copy();   
  public RiTextIF stopBehavior(int id); 
  public RiTextIF stopBehaviors(); 
  
  public float[] boundingBox();
  public float[] center(); 
  public float charOffset(int i);
  public float wordOffset(int i);
  
  public boolean contains(String s); 
  public float distanceTo(float x, float y); 
  public RiTextIF[] splitLetters(); 
  public RiTextIF[] splitWords(); 

  public float textHeight(); 
  public float textAscent(); 
  public float textDescent(); 
  public float textWidth(); 
   
  public int fadeIn(float seconds, float delay); 
  public int fadeOut(float seconds, float delay,  boolean destroyOnComplete); 
  public int moveTo(float newX, float newY, float seconds, float delay); 
  public int colorTo(float[] colors, float seconds, float delay, EventType _type); 
  public int rotateTo(float angleInRadians, float seconds, float delay); 
  public int scaleTo(float theScale, float seconds, float delay); 
  public int textTo(String newText, float seconds);
  
  // Setters / Getters
  
  public RiTextIF font(String name, float size);
  public RiTextIF fontSize(float f); 
  public float fontSize(); 
  
  public RiTextIF align(int i);
  public int align();
  
  public RiTextIF alpha(float a); 
  public float alpha(); 
  
  public RiTextIF rotate(float r);
  public RiTextIF rotate(float rx, float ry, float rz);
  public float[] rotate(); 
  
  public RiTextIF scale(float s);
  public RiTextIF scale(float x, float y, float z);
  public float[] scale(); 
  
  public RiTextIF fill(float r, float g, float b, float a);
  public RiTextIF fill(float r, float g, float b);
  public RiTextIF fill(float g, float a);
  public RiTextIF fill(float g);
  public RiTextIF fill(float[] color);
  public float[] fill(); 
  
  public RiTextIF text(String s); 
  public String text(); 
   
  public boolean isVisible();
  
  public RiTextIF showBounds(boolean b); 
  public boolean showBounds(); 
  
  public RiTextIF motionType(int type); 
  public int motionType();
  
  public RiTextIF position(float x, float y); 
  public RiTextIF position(float x, float y, float z); 
  public float[] position(); 
  
  // from RiString =====================
  
  RiTextIF replaceFirst(String regex, String t);
  RiTextIF replaceLast(String regex, String t);
  RiTextIF replaceAll(String regex, String t);
  //RiTextIF[] split(String regex);
  
  boolean startsWith(String cs);
  boolean endsWith(String cs); 
  boolean equalsIgnoreCase(String cs);

  
  // these return 'this'
  RiTextIF trim();
  RiTextIF toLowerCase();
  RiTextIF toUpperCase();
  
  RiTextIF removeChar(int idx);
  RiTextIF insertChar(int idx, char c);
  RiTextIF replaceChar(int idx, String s);
  
  RiTextIF removeWord(int wordIdx);
  RiTextIF insertWord(int wordIdx, String s);
  RiTextIF replaceWord(int wordIdx, String s);
  
  // java-style overrides
  RiTextIF concat(String cs);
  RiTextIF concat(RiTextIF cs);
  
  // equivalent to js-style getter/setter
  
  int indexOf(String s);  
  RiTextIF analyze();
  String charAt(int idx);
  String get(String s);
  Map features();
  //char[] toCharArray();
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
  
  // Note: these added for RiText refactor
  public List behaviors();
  public Object getPApplet();
  public boolean autodraw();
  public float rotateZ();
  public float[] boundingBoxFill();
  public float[] boundingBoxStroke();
  public RiTextIF boundingBoxFill(float[] bbf);
  public RiTextIF boundingBoxStroke(float[] bbs);
  public RiTextIF scale(float[] values);
  public RiTextIF rotateZ(float f);
  public float x();
  public float y();
  
  // ??
  public Object font();
  public RiTextIF font(Object pf);

}
