package rita;

import static rita.support.Constants.EventType.ColorTo;
import static rita.support.Constants.EventType.FadeIn;
import static rita.support.Constants.EventType.FadeOut;
import static rita.support.Constants.EventType.Internal;
import static rita.support.Constants.EventType.TextTo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import rita.render.Defaults;
import rita.render.InterpolatingBehavior;
import rita.render.PageLayout;
import rita.render.Rect;
import rita.render.RiTextBehavior;
import rita.render.RotateZBehavior;
import rita.render.ScaleBehavior;
import rita.render.TextColorFade;
import rita.render.TextMotion2D;
import rita.render.TextMotion3D;
import rita.support.BehaviorListener;
import rita.support.Constants;
import rita.support.Regex;

// TODO: add version of bounding-box that is tight (based on actual chars in string)?
// TODO: add vertical align option
// TODO: remove all handling of .vlw files 
    
// NEXT: assign scale-bounding problem to K, 

/**
 * RiTa's text display object. Wraps an instance of RiString to provide utility
 * methods for typography and animation.
 */
public class RiText implements Constants //RiTextIF
{
  public static Defaults defaults;

  static
  {
    RiTa.init();

    RiTa.LEFT = PApplet.LEFT;
    RiTa.UP = PApplet.UP;
    RiTa.RIGHT = PApplet.RIGHT;
    RiTa.DOWN = PApplet.DOWN;
    RiTa.CENTER = PApplet.CENTER;
  }

  public static final RiText[] EMPTY_ARRAY = new RiText[0];

  // Statics ============================================================

  protected static boolean DBUG_INFO = false;

  public static final List<RiText> instances = new ArrayList<RiText>();
  
  protected static boolean behaviorWarningsDisabled;
  public static boolean callbacksDisabled = false;

  // Members ============================================================

  public int motionType;

  public float scaleX = 1, scaleY = 1, scaleZ = 1;

  public float rotateX, rotateY, rotateZ;

  public List behaviors;

  /* Current delegate for this text */
  public RiString text;

  /**
   * Current x-position of this text
   */
  public float x;

  /**
   * Current y-position of this text
   */
  public float y;

  /**
   * Current z-position of this text
   */
  public float z;

  protected float fillR = defaults.fill[0], fillG = defaults.fill[1];
  protected float fillB = defaults.fill[2], fillA = defaults.fill[3];
  protected float bbStrokeR, bbStrokeG, bbStrokeB;
  protected float bbFillR=-1, bbFillG=-1, bbFillB=-1;
  protected Rect boundingBox, screenBoundingBox;

  protected float /*fontSize,*/ bbStrokeWeight;
  protected boolean boundingBoxVisible;
  protected int alignment;

  public PApplet pApplet;

  protected PFont font;
  protected RiText textToCopy;
  protected boolean autodraw;
  protected String fontFamily;

  protected float fontSizeAdjustment; // when font is different than its size

  public RiText()
  {
    this(null, E);
  }

  public RiText(String text)
  {
    this(null, text, Float.MIN_VALUE, Float.MIN_VALUE);
  }

  public RiText(char character)
  {
    this(null, Character.toString(character), Float.MIN_VALUE, Float.MIN_VALUE, defaults.alignment);
  }

  public RiText(float startXPos, float startYPos)
  {
    this(null, E, startXPos, startYPos, defaults.alignment);
  }

  public RiText(char character, float startXPos, float startYPos)
  {
    this(null, Character.toString(character), startXPos, startYPos, defaults.alignment);
  }

  public RiText(String text, float startXPos, float startYPos)
  {
    this(null, text, startXPos, startYPos, defaults.alignment);
  }

  public RiText(String text, float startXPos, float startYPos, PFont font)
  {
    this(null, text, startXPos, startYPos, defaults.alignment, font);
  }

  public RiText(String text, float xPos, float yPos, int alignment)
  {
    this(null, text, xPos, yPos, alignment, null);
  }

  public RiText(String text, float xPos, float yPos, int alignment, PFont theFont)
  {
    this(null, text, xPos, yPos, alignment, theFont);
  }

  // DEPRECATED METHODS TAKING 'THIS'

  public RiText(PApplet pApplet)
  {
    this(pApplet, E);
  }

  public RiText(PApplet pApplet, String text)
  {
    this(pApplet, text, Float.MIN_VALUE, Float.MIN_VALUE);
  }

  public RiText(PApplet pApplet, char character)
  {
    this(pApplet, Character.toString(character), Float.MIN_VALUE, Float.MIN_VALUE, defaults.alignment);
  }

  public RiText(PApplet pApplet, float startXPos, float startYPos)
  {
    this(pApplet, E, startXPos, startYPos, defaults.alignment);
  }

  public RiText(PApplet pApplet, char character, float startXPos, float startYPos)
  {
    this(pApplet, Character.toString(character), startXPos, startYPos, defaults.alignment);
  }

  public RiText(PApplet pApplet, String text, float startXPos, float startYPos)
  {
    this(pApplet, text, startXPos, startYPos, defaults.alignment);
  }

  public RiText(PApplet pApplet, String text, float startXPos, float startYPos, PFont font)
  {
    this(pApplet, text, startXPos, startYPos, defaults.alignment, font);
  }

  public RiText(PApplet pApplet, String text, float xPos, float yPos, int alignment)
  {
    this(pApplet, text, xPos, yPos, alignment, null);
  }

  /**
   * Creates a new RiText object base-aligned at x='xPos', y='yPos', with
   * 'alignment' from one of (RiTa.LEFT, RiTa.CENTER, RiTa.RIGHT), using font
   * specified by 'theFont'.
   */
  public RiText(PApplet pApplet, String text, float xPos, float yPos, int alignment, PFont theFont)
  {
    this.pApplet = (pApplet != null) ? pApplet : globalContext();

    this.setDefaults();

    if (theFont != null)
    {
      assignFont(theFont);
      //this.fontSize = theFont.getSize();
    }

    this.text(text);
    instances.add(this);
    this.textMode(alignment);
    this.verifyFont();
    
    this.x = (xPos == Float.MIN_VALUE) ? screenCenterX() : xPos;
    this.y = (yPos == Float.MIN_VALUE) ? screenCenterY() : yPos;
  }

  private PApplet globalContext()
  {
    if (RiTa.context != null && !(RiTa.context instanceof PApplet))
      throw new RiTaException("Unexpected context: " + RiTa.context);

    return (PApplet) RiTa.context;
  }

  protected void setDefaults()
  {
    this.alignment = defaults.alignment;
    this.motionType = defaults.motionType;
    this.boundingBoxVisible = defaults.showBounds;
    this.bbStrokeWeight = defaults.boundingStrokeWeight;
    this.boundingStroke(defaults.boundingStroke);
    this.boundingFill(defaults.boundingFill);
    //this.fontSize = defaults.fontSize;
  }

  protected void verifyFont()
  {
    if (pApplet == null) return;

    if (this.font == null)   // if we don't yet have a font, use default
    {
      this.fontSizeAdjustment = 0;
      
      PFont defFont = defaultFont(pApplet);
      
      // check that defaults.fontSize hasn't been changed
      if (defFont.getSize() != defaults.fontSize)
        handleVlwFontScale(defFont, defaults.fontSize);
      
      assignFont(defFont, defaults.fontFamily);

      if (this.font == null)
        throw new RiTaException("Unable to verify font!!");
    }

    pApplet.textFont(font); // All moved from render
    pApplet.textAlign(alignment);
    if (this.fontSizeAdjustment > 0)
      pApplet.textSize(fontSizeAdjustment);
  }

  /** Returns the point representing the center of the RiText's bounding box */
  public float[] center()
  {
    float[] bb = this.boundingBox();
    return new float[] { bb[0] + bb[2]/2f,  (bb[1]-textAscent()) + bb[3]/2f};
  }

  public static final PFont defaultFont(PApplet p) 
  {
    return _defaultFont(p, defaults.fontSize);
  }
  
  protected static final PFont _defaultFont(PApplet p, float size)
  {
    PFont pf = defaults.font;

    if (pf == null || (size > 0 && size != defaults.fontSize))
    {
      String fontName = defaults.fontFamily;

      pf = fontFromString(p, fontName, size); 
      
      if (pf != null)
      {
        defaults.font = pf;
      }
      else
      {
        String msg = "Unable to use font, with name='" + fontName + "'";
        if (size > -1) msg += " and size=" + size;
        
        throw new RiTaException(msg);
      }
    }
    
    return pf;
  }

  public static final void defaultFontSize(float size)
  {
    if (defaults.fontSize != size)  {
    
      defaults.fontSize = size;
      defaults.font = null;
    }
  }
  
  public static final float defaultFontSize()
  {
    return defaults.fontSize;
  }
  
  public static final void defaultFont(PFont font)
  {
    defaults.fontFamily = font.getName();
    defaults.fontSize = font.getSize();
    defaults.font = font;
  }

  public static final void defaultFont(String name)
  {
    float size = name.endsWith(".vlw") ? -1 : defaults._originalDefaultFontSize; 
    defaultFont(name, size);
  }

  public static final void defaultFont(String name, float size)
  {
    defaults.fontFamily = name;
    defaults.fontSize = size;
    defaults.font = null;
  }

  public static float[] defaultFill()
  {
    return defaults.fill;
  }

  public static void defaultFill(float r, float g, float b, float alpha)
  {
    defaults.fill[0] = r;
    defaults.fill[1] = g;
    defaults.fill[2] = b;
    defaults.fill[3] = alpha;
  }

  public static void defaultFill(float gray)
  {
    defaultFill(gray, gray, gray, 255);
  }

  public static void defaultFill(float gray, float alpha)
  {
    defaultFill(gray, gray, gray, alpha);
  }

  public static void defaultFill(float r, float g, float b)
  {
    defaultFill(r, g, b, 255);
  }

  private float screenCenterX()
  {
    return (pApplet != null) ? screenCenterX(pApplet.g) : -1;
  }

  private float screenCenterX(PGraphics p)
  {
    if (p == null)
      return -1;
    float cx = p.width / 2;
    if (alignment == RiTa.LEFT)
      cx -= (textWidth() / 2f);
    else if (alignment == RiTa.RIGHT)
      cx += (textWidth() / 2f);
    return cx;
  }

  private float screenCenterY()
  {
    return (pApplet != null) ? pApplet.height / 2 : -1;
  }

  public float boundingStrokeWeight()
  {
    return bbStrokeWeight;
  }

  // ------------------------- Colors -----------------------------

  /**
   * Sets the text fill color according to a single hex number.
   */
  public RiText fillHex(int hexColor)
  {
    this.fill(unhex(hexColor));
    return this;

  }

  protected static final float[] unhex(int hexColor)
  {
    // note: not handling alphas...
    int r = hexColor >> 16;
    int temp = hexColor ^ r << 16;
    int g = temp >> 8;
    int b = temp ^ g << 8;
    return new float[] { r, g, b, 255 };
  }

  /**
   * Set the text color for this object
   * 
   * @param r
   *          red component (0-255)
   * @param g
   *          green component (0-255)
   * @param b
   *          blue component (0-255)
   * @param a
   *          alpha component (0-255)
   */
  public RiText fill(float r, float g, float b, float alpha)
  {
    this.fillR = r;
    this.fillG = g;
    this.fillB = b;
    this.fillA = alpha;

    return this;
  }

  public RiText fill(float g)
  {
    this.fillR = g;
    this.fillG = g;
    this.fillB = g;

    return this;
  }

  public RiText fill(float g, float a)
  {
    return this.fill(g, g, g, a);
  }

  public RiText fill(float r, float g, float b)
  {
    this.fillR = r;
    this.fillG = g;
    this.fillB = b;
    return this;
  }

  /**
   * Set the text color for this object (r,g,b,a) from 0-255
   */
  public RiText fill(float[] color)
  {
    float r = color[0], g = 0, b = 0, a = fillA;
    switch (color.length)
    {
      case 4:
        g = color[1];
        b = color[2];
        a = color[3];
        break;
      case 3:
        g = color[1];
        b = color[2];
        break;
      case 2:
        g = color[0];
        b = color[0];
        a = color[1];
        break;
    }
    return this.fill(r, g, b, a);
  }

  /**
   * Set the bounding-box (or background) color for this object
   * 
   * @param r
   *          red component (0-255)
   * @param g
   *          green component (0-255)
   * @param b
   *          blue component (0-255)

   */
  public RiText boundingFill(float r, float g, float b/*, float alpha*/)
  {
    this.bbFillR = r;
    this.bbFillG = g;
    this.bbFillB = b;
    //this.bbFillA = alpha;
    return this;

  }

  /**
   * Set the current boundingBoxFill color for this object, applicable only when
   * <code>showBoundingBox(true)</code> has been called.
   */
  public RiText boundingFill(float[] color)
  {
    bbFillR = color[0];
    bbFillG = color[1];
    bbFillB = color[2];
/*    bbFillA = 255;
    if (color.length > 3)
      this.bbFillA = color[3];*/
    
    if (color.length == 4 && !RiTa.SILENT)
      System.err.println("[WARN] alpha not valid for boundingBoxFill"
          + " (this property is inherited from the RiText itself)");
    return this;

  }

  public RiText boundingFill(float gray)
  {
    return this.boundingFill(gray, gray, gray);
  }

/*  public RiText boundingFill(float gray, float alpha)
  {
    return this.boundingFill(gray, gray, gray, alpha);
  }*/
  
/*  public RiText boundingFill(float r, float g, float b) { 
     return
     this.boundingFill(r, g, b, 255); 
  }
*/
  /**
   * Set the stroke color for the bounding-box of this object, assuming it has
   * been set to visible.
   * 
   * @param r
   *          red component (0-255)
   * @param g
   *          green component (0-255)
   * @param b
   *          blue component (0-255)
   * @param alpha
   *          transparency (0-255)
   */
  public RiText boundingStroke(float r, float g, float b/* , float alpha */)
  {
    this.bbStrokeR = r;
    this.bbStrokeG = g;
    this.bbStrokeB = b;
    // this.bbStrokeA = alpha;
    return this;
  }

  public RiText boundingStroke(float gray)
  {
    return this.boundingStroke(gray, gray, gray);
  }

  /*
   * public RiText boundingStroke(float gray, float alpha) { return
   * this.boundingStroke(gray, gray, gray, alpha); }
   */

  /*
   * public RiText boundingStroke(float r, float g, float b) { return
   * this.boundingStroke(r, g, b, 255); }
   */

  /**
   * Returns the current text color (r,g,b,a) for this object
   */
  public float[] fill()
  { // yuck
    return new float[] { fillR, fillG, fillB, fillA };
  }

  /**
   * Returns the current bounding box fill color (r,g,b) for this object
   */
  public float[] boundingFill()
  { // yuck
    return new float[] { bbFillR, bbFillG, bbFillB };
  }

  /**
   * Returns the current bounding box stroke color for this object
   * 
   */
  public float[] boundingStroke()
  { // yuck
    return new float[] { bbStrokeR, bbStrokeG, bbStrokeB/*, fillA*/ };
  }

  /**
   * Set the current boundingBoxStroke color for this object, applicable only
   * when <code>showBoundingBox(true)</code> has been called.
   */
  public RiText boundingStroke(float[] color)
  {
    bbStrokeR = color[0];
    bbStrokeG = color[1];
    bbStrokeB = color[2];
    // bbStrokeA = 255;
    if (color.length == 4 && !RiTa.SILENT)
      System.err.println("[WARN] alpha not valid for boundingBoxStroke"
          + " (this property is inherited from the RiText itself)");
    // this.bbStrokeA = color[3];
    return this;
  }

  /**
   * Set the current alpha trasnparency for this object (0-255))
   * 
   * @param alpha
   */
  public RiText alpha(float alpha)
  {
    this.fillA = alpha;
    return this;
  }

  /**
   * Returns the fill alpha value (transparency)
   */
  public float alpha()
  {
    return fillA;
  }

  // -------------------- end colors ----------------------

  /**
   * Checks if the input point is inside the bounding box
   */
  public boolean contains(float mx, float my)
  {
    this.updateBoundingBox();
    return (boundingBox.contains(mx - x, my - y));
  }

  /**
   * Draw the RiText object at current x,y,color,font,alignment, etc.
   */
  public RiText draw()
  {
    this.update();
    this.render(getPApplet().g);
    return this;
  }

  /**
   * Draw the RiText object at current x,y,color,font,alignment, etc. on the
   * specified PGraphics object
   */
  public RiText draw(PGraphics p)
  {
    PGraphics pg = p != null ? p : getPApplet().g;
    this.update();
    this.render(pg);
    return this;
  }

  /**
   * Override in subclasses to do custom rendering
   * <p>
   * Note: It is generably preferable to override this method rather than the
   * draw() method in subclasses to ensure proper maintenance of contained
   * objects.
   */
  protected void render()
  {
    render(getPApplet().g);
  }

  /**
   * Override in subclasses to do custom rendering
   * <p>
   * Note: It is generally preferable to override this method rather than the
   * draw() method in subclasses to ensure proper maintenance of contained
   * objects.
   */
  protected void render(PGraphics p)
  {
    if (p == null || this.font == null)
      throw new RiTaException("Null renderer in RiText.render(), font="+this.font);

    if (text == null || text.length() == 0)
      return;
    
    // TODO: add check/flag to return if offscreen?

    if (textToCopy != null)
      textToCopy.draw();

    // translate & draw at 0,0
    p.smooth();
    p.pushStyle();
    p.pushMatrix(); // --------------

    doAffineTransforms(p);

    if (boundingBoxVisible)
      this.drawBoundingBox(p);

    p.fill(fillR, fillG, fillB, fillA);

    //p.textFont(font); // moved to verifyFont()
    //p.textAlign(alignment);
    p.text(text.text(), 0, 0);

    p.popMatrix(); // --------------
    p.popStyle();
  }

  protected void doAffineTransforms(PGraphics p)
  {
    float[] bb = boundingBox();
    float centerX = bb[2] / 2f;
    float centerY = bb[3] / 2f;

    p.translate(x, y);
    p.translate(centerX, -centerY);
    p.rotate(rotateZ);
    p.translate(-centerX, +centerY);
    p.scale(scaleX, scaleY);
  }

  static boolean is3D(PGraphics p)
  {
    try
    {
      return (p instanceof processing.opengl.PGraphicsOpenGL); // ??
    }
    catch (Throwable e)
    {
      return false;
    }
  }

  protected void drawBoundingBox(PGraphics p)
  {
    if (bbFillR < 0 && bbFillG < 0 && bbFillB < 0)
      p.noFill();
    else
      p.fill(bbFillR, bbFillG, bbFillB, fillA);

    p.stroke(bbStrokeR, bbStrokeG, bbStrokeB, fillA);
    if (bbStrokeWeight > 0)
      p.strokeWeight(bbStrokeWeight);
    else
      p.noStroke();

    p.rectMode(PApplet.CORNER);
    p.rect(boundingBox.x, boundingBox.y, boundingBox.w, boundingBox.h);
  }

  /**
   * Returns a field for field copy of this object
   */
  public RiText copy()
  {
    return copy(this);
  }

  /**
   * public RiText mouseEvent(MouseEvent e) { float mx = e.getX(); float my =
   * e.getY();
   * 
   * switch (e.getAction()) { case MouseEvent.PRESS: if (mouseDraggable &&
   * !hidden && contains(mx, my)) { isDragging = true; this.mouseXOff = mx - x;
   * this.mouseYOff = my - y; } break; case MouseEvent.RELEASE:
   * 
   * if (mouseDraggable && contains(mx, my)) { isDragging = false;
   * pauseBehaviors(false); } break; case MouseEvent.CLICK: break;
   * 
   * case MouseEvent.DRAG: if (isDragging && contains(mx, my)) { x = mx -
   * mouseXOff; y = my - mouseYOff; } break; case MouseEvent.MOVE: break; }
   * return this; }
   */

  /**
   * Returns the current text width in pixels
   */
  public float textWidth()
  {
    float result = 0;

    String txt = text != null ? text.text() : null;

    if (txt == null)
    {
      if (!printedTextWidthWarning)
      {
        System.err.println("[WARN] textWidth() called for null text!");
        printedTextWidthWarning = true;
      }
      return result; // hmm?
    }

    this.verifyFont();

    if (this.pApplet != null)
      result = pApplet.textWidth(txt);// * scaleX;

    return result;
  }

  static boolean printedTextWidthWarning;

  /**
   * Returns the height for the current font in pixels (including ascenders and
   * descenders)
   */
  public float textHeight()
  {
    return (textAscent() + textDescent()) * scaleY;
    // return (_pApplet.textAscent() + _pApplet.textDescent()) * scaleY;
  }

  protected void update()
  {
    if (x == Float.MIN_VALUE && text.text() != null)
      x = screenCenterX();

    this.updateBehaviors();

    if (boundingBoxVisible && text.text() != null)
      this.updateBoundingBox();
  }

  /**
   * Sets the animation <code>motionType</code> for this for moveTo() or
   * moveBy() methods on this object, set via one of the following constants: <br>
   * <ul>
   * <li>RiText.LINEAR
   * <li>RiText.EASE_IN
   * <li>RiText.EASE_OUT
   * <li>RiText.EASE_IN_OUT
   * <li>RiText.EASE_IN_OUT_CUBIC
   * <li>RiText.EASE_IN_CUBIC
   * <li>RiText.EASE_OUT_CUBIC;
   * <li>RiText.EASE_IN_OUT_QUARTIC
   * <li>RiText.EASE_IN_QUARTIC
   * <li>RiText.EASE_OUT_QUARTIC;
   * <li>RiText.EASE_IN_OUT_SINE
   * <li>RiText.EASE_IN_SINE
   * <li>RiText.EASE_OUT_SINE
   * </ul>
   * 
   * @param mtype
   */
  public RiText motionType(int mtype)
  {
    this.motionType = mtype;
    return this;
  }

  /**
   * Returns the <code>motionType</code> for this object,
   */
  public int motionType()
  {
    return this.motionType;
  }

  /**
   * Move to new absolute x,y (or x,y,z) position over 'time' seconds
   * <p>
   * Note: uses the current <code>motionType</code> for this object.
   * 
   * @return the unique id for this behavior
   */
  public int moveTo(float newX, float newY, float seconds)
  {
    return this.moveTo(new float[] { newX, newY }, seconds, 0);
  }

  /**
   * Move to new absolute x,y (or x,y,z) position over 'time' seconds
   * <p>
   * Note: uses the current <code>motionType</code> for this object, starting at
   * 'startTime' seconds in the future
   * 
   * @return the unique id for this behavior
   */
  public int moveTo(float newX, float newY, float seconds, float startTime)
  {
    return this.moveTo(new float[] { newX, newY }, seconds, startTime);
  }

  /**
   * Move to new absolute x,y (or x,y,z) position over 'time' seconds
   * <p>
   * Note: uses the current <code>motionType</code> for this object, starting at
   * 'startTime' seconds in the future
   * 
   * @return the unique id for this behavior
   */
  public int moveTo(final float[] newPosition, final float seconds, final float startTime)
  {
    String err3d = "Invalid newPosition.length for moveTo(),"
        + " expected 2 (or 3 in 3d mode), but found: " + newPosition.length;

    InterpolatingBehavior moveTo = null;
    if (!is3D(pApplet.g) || newPosition.length == 2) // 2d
    {
      if (newPosition.length != 2)
        throw new RiTaException(err3d);
      moveTo = new TextMotion2D(this, newPosition, startTime, seconds);
    }
    else
    // 3d
    {
      if (newPosition.length != 3)
        throw new RiTaException(err3d + "\nPerhaps you wanted moveTo3D()?");
      moveTo = new TextMotion3D(this, newPosition, startTime, seconds);
      // moveTo.resetTarget(new float[] {x,y,z}, newPosition, startTime,
      // seconds);
    }
    moveTo.setMotionType(motionType);

    addBehavior(moveTo);

    return moveTo.getId();
  }

  /**
   * Move to new position by x,y offset over the duration specified by
   * 'seconds', starting at 'startTime' seconds in the future
   * <p>
   * 
   * @return the unique id for this behavior
   */
  public int moveBy(float xOffset, float yOffset, float seconds, float startTime)
  {
    return (is3D(pApplet.g)) ? this.moveBy(new float[] { xOffset, yOffset, 0 }, seconds, startTime)
        : this.moveBy(new float[] { xOffset, yOffset }, seconds, startTime);
  }

  /**
   * Move to new position by x,y offset over the duration specified by
   * 'seconds'.
   * <p>
   * 
   * @return the unique id for this behavior
   */
  public int moveBy(float xOffset, float yOffset, float seconds)
  {
    return this.moveBy(xOffset, yOffset, seconds, 0);
  }

  /**
   * Move to new position by x,y offset over the duration specified by
   * 'seconds'.
   * <p>
   * Note: uses the current <code>motionType</code> for this object.
   * 
   * @return the unique id for this behavior
   */
  public int moveBy(float[] posOffset, float seconds, float startTime)
  {

    boolean is3d = is3D(pApplet.g);
    float[] newPos = is3d ? new float[3] : new float[2];

    if (posOffset.length != newPos.length)
    {
      throw new RiTaException("Expecting a 2d array(or 3 in 3d) "
          + "for the 1st argument, but found: " + RiTa.asList(posOffset));
    }
    newPos[0] = x + posOffset[0];
    newPos[1] = y + posOffset[1];
    if (newPos.length > 2)
      newPos[2] = posOffset.length > 2 ? z += posOffset[2] : z;
    return this.moveTo(newPos, seconds, startTime);
  }

  /**
   * Returns true if the object is offscreen
   */
  public boolean isOffscreen()
  {
    return isOffscreen(pApplet.g);
  }

  boolean isOffscreen(PGraphics p)
  {
    // System.err.println(text+" - offscreen? ("+x+","+y+")");
    return (x < 0 || x >= p.width) || (y < 0 || y >= p.height);
  }

  // Scale methods ----------------------------------------

  /**
   * Scales object to 'newScale' over 'time' seconds, starting at 'startTime'
   * seconds in the future
   * <p>
   * Note: uses linear interpolation unless otherwise specified. Returns the Id
   * of the RiTextBehavior object used for the scale.
   */
  public int scaleTo(float newScale, float seconds, float startTime)
  {
    return scaleTo(newScale, newScale, newScale, seconds, startTime);
  }

  /**
   * Scales object to 'newScale' over 'time' seconds, starting immediately.
   * <p>
   * Note: uses linear interpolation unless otherwise specified. Returns the Id
   * of the RiTextBehavior object used for the scale.
   */
  public int scaleTo(float newScale, float seconds)
  {
    return scaleTo(newScale, newScale, newScale, seconds);
  }

  /**
   * Scales object to {scaleX, scaleY, scaleZ} over 'time' seconds. Note: uses
   * linear interpolation unless otherwise specified. Returns the Id of the
   * RiTextBehavior object used for the scale.
   */
  @SuppressWarnings("hiding")
  public int scaleTo(float scaleX, float scaleY, float scaleZ, float seconds)
  {
    return scaleTo(scaleX, scaleY, scaleZ, seconds, 0);
  }

  /**
   * Scales object to {scaleX, scaleY, scaleZ} over 'time' seconds, starting at
   * 'startTime' seconds in the future.
   * <p>
   * Returns the Id of the RiTextBehavior object used for the scale. Note: uses
   * linear interpolation unless otherwise specified.
   */
  public int scaleTo(final float newScaleX, final float newScaleY, final float newScaleZ, final float seconds, final float delay)
  {
    ScaleBehavior scaleTo = new ScaleBehavior(this, new float[] { newScaleX, newScaleY,
        newScaleZ }, delay, seconds);
    scaleTo.setMotionType(LINEAR);
    addBehavior(scaleTo);
    return scaleTo.getId();
  }

  public int rotateTo(float angleInRadians, float seconds)
  {
    return rotateTo(angleInRadians, seconds, 0);
  }

  // TODO: add axis for 3D ??
  public int rotateTo(float angleInRadians, float seconds, float delay)
  {
    RotateZBehavior rotateTo = new RotateZBehavior(this, angleInRadians, delay, seconds);
    rotateTo.setMotionType(LINEAR);
    addBehavior(rotateTo);
    return rotateTo.getId();
  }

  // Fade methods -----------------------------------------

  /**
   * Fades in current text over <code>seconds</code> starting at
   * <code>startTime</code>. Interpolates from the current color {r,g,b,a} to
   * {r,g,b,255}.
   * 
   * @param startTime
   *          time in future to start
   * @param seconds
   *          time for fade
   * @return a unique id for this behavior
   */
  public int fadeIn(float seconds, float startTime)
  {
    float[] col = { fillR, fillG, fillB, 255 };
    return _colorTo(col, seconds, startTime, FadeIn, false);
  }

  public int fadeIn(float seconds)
  {
    return this.fadeIn(seconds, 0);
  }

  /**
   * Fades out current text over <code>seconds</code> starting at
   * <code>startTime</code>. Interpolates from the current color {r,g,b,a} to
   * {r,g,b,0}.
   * 
   * @param seconds
   *          time for fade
   * @param startTime
   *          time in future to start
   * @param removeOnComplete
   *          destroys the object when the behavior completes
   * @return the unique id for this behavior
   */
  public int fadeOut(float seconds, float startTime, boolean removeOnComplete)
  {
    return this._fadeOut(seconds, startTime, removeOnComplete, FadeOut);
  }

  protected int _fadeOut(float seconds, float startTime, boolean removeOnComplete, EventType type)
  {
    float[] col = { fillR, fillG, fillB, 0 };
    // if (isBoundingBoxVisible()) // fade bounding box too
    // addBehavior(new BoundingBoxAlphaFade(this, 0, startTime, seconds));
    return _colorTo(col, seconds, startTime, type, removeOnComplete);
  }

  public int fadeOut(float seconds, float startTime)
  {
    return this.fadeOut(seconds, startTime, false);
  }

  public int fadeOut(float seconds, boolean removeOnComplete)
  {
    return this.fadeOut(seconds, 0, removeOnComplete);
  }

  public int fadeOut(float seconds)
  {
    return this.fadeOut(seconds, false);
  }

  protected synchronized int _colorTo(final float[] color, final float seconds, final float startTime, final EventType type, final boolean disposeWhenFinished)
  {
    // System.out.println(this+"._colorTo("+RiTa.asList(color)+")");

    /*if (boundingBoxVisible && (type == EventType.FadeIn || type == EventType.FadeOut))
    {
      if (color[3] >= 255 || color[3] < 1) // hack to fade bounding box too
        addBehavior(new BoundingBoxAlphaFade(this, color[3], startTime, seconds));
    }*/

    TextColorFade colorFade = new TextColorFade(this, color, startTime, seconds);
    colorFade.setType(type);

    if (disposeWhenFinished)
    {
      colorFade.addListener(new BehaviorListener()
      {
        public void behaviorCompleted(RiTextBehavior behavior)
        {
          dispose(behavior.getParent());
        }
      }); // disposes the RiText after fadeOut
    }

    addBehavior(colorFade);

    return colorFade.getId();
  }

  public int colorTo(float[] colors, float seconds, float delay, EventType type)
  {
    return this._colorTo(colors, seconds, delay, type, false);
  }

  /**
   * Transitions to 'color' (rgba) over 'seconds' starting at 'startTime'
   * seconds in the future
   * 
   * @param seconds
   *          time for fade
   * @return a unique id for this behavior
   */
  public int colorTo(float r, float g, float b, float a, float seconds)
  {
    return this.colorTo(new float[] { r, g, b, a }, seconds);
  }

  public int colorTo(float[] color, float seconds)
  {
    return this.colorTo(color, seconds, 0);
  }

  public int colorTo(float gray, float seconds)
  {
    return this.colorTo(new float[] { gray, gray, gray, this.fillA }, seconds);
  }

  public int colorTo(float[] color, float seconds, float startTime)
  {
    return this._colorTo(color, seconds, startTime, ColorTo, false);
  }

  /**
   * Fades out the current text and fades in the <code>newText</code> over
   * <code>seconds</code> starting immediately
   * 
   * @return the unique id for this behavior
   */
  public int textTo(final String newText, final float seconds)
  {
    return textTo(newText, seconds, 0);
  }

  /**
   * Fades out the current text and fades in the <code>newText</code> over
   * <code>seconds</code> starting at 'startTime' seconds in the future
   * 
   * @param newText
   *          to be faded in
   * @param startTime
   *          # of seconds in the future that the fade will start
   * @param seconds
   *          time for fade
   * @return the unique id for this behavior
   */
  public int textTo(final String newText, final float seconds, final float startTime)
  {
    // grab the alpha if needed
    float startAlpha = 0, endAlpha = fillA;
    if (textToCopy != null)
    {
      startAlpha = textToCopy.alpha();
      dispose(textToCopy); // stop any currents
    }

    // use the copy to fade out
    textToCopy = RiText.copy(this);
    textToCopy._fadeOut(seconds/2f, startTime, false, Internal); // fadeIn (DCH: changed 2nd param from 0?)

    // and use 'this' to fade in
    this.text(newText).alpha(startAlpha);
    float[] col = { fillR, fillG, fillB, endAlpha }; // fadeIn (DCH: changed 4th param from 255?)
    return _colorTo(col, seconds * .95f, startTime, TextTo, false);
  }

  /**
   * Call to remove a RiText from the current sketch (and from existence),
   * cleaning up whatever resources it may have held
   */
  public static synchronized void dispose(RiText rt)
  {
    if (rt != null)
    {
      PApplet p = ((RiText) rt).getPApplet();
      if (p != null && rt.autodraw())
      {
        try
        {
          p.unregisterMethod("draw", rt);
        }
        catch (Throwable e)
        {
          System.err.println("[WARN] Error unregistering draw() for " + rt.text());
        }
      }
      ((RiText) rt)._dispose();
    }
  }

  protected synchronized void _dispose()
  {
    // visible(false);

    if (text != null)
      RiString.dispose(text);

    if (behaviors != null)
    {
      for (int i = 0; i < behaviors.size(); i++)
      {
        RiTextBehavior rtb = (RiTextBehavior) behaviors.get(i);
        rtb.delete();
      }
      behaviors.clear();
      behaviors = null;
    }

    instances.remove(this);
  }

  public static synchronized void disposeAll()
  {
    dispose(instances);
  }

  public static synchronized void dispose(RiText[] c)
  {
    if (c == null)
      return;
    for (int i = 0; i < c.length; i++)
    {
      if (c[i] != null)
      {
        dispose(c[i]);
        c[i] = null;
      }
    }
    c = null;
  }

  public static synchronized void dispose(List l)
  {
    if (l == null)
      return;

    while (l.size() > 0)
    {
      RiText p = (RiText) l.remove(0);
      dispose(p);
    }
  }

  // ///////////////////////////////////////////////

  /**
   * Returns all existing instances of RiText objects in an array
   */
  public static RiText[] getInstances()
  {
    return instances.toArray(new RiText[instances.size()]);
  }

  /**
   * Returns all RiTexts that contain the point x,y or null if none do.
   * <p>
   * Note: this will return an array even if only one item is picked, therefore,
   * you should generally use it as follows:
   * 
   * <pre>
   *   RiText picked = null;
   *   RiText[] rts = RiText.getPicked(mx, my);
   *   if (rts != null)
   * picked = rts[0];
   * 
   * <pre>
   * @return RiText[] 1 or more RiTexts containing
   * the point, or null if none do.
   */
  public static final RiText[] picked(float x, float y)
  {
    List pts = null;
    for (int i = 0; i < instances.size(); i++)
    {
      RiText rt = instances.get(i);
      if (rt.contains(x, y))
      {
        if (pts == null)
          pts = new ArrayList();
        pts.add(rt);
      }
    }
    if (pts == null || pts.size() == 0)
      return EMPTY_ARRAY;

    return (RiText[]) pts.toArray(new RiText[pts.size()]);
  }

  // end statics ----------------------------------------------

  /**
   * Fades all visible RiText objects.
   */
  public static final void fadeAllOut(float seconds)
  {
    for (Iterator i = instances.iterator(); i.hasNext();)
    {
      RiText p = (RiText) i.next();
      p.fadeOut(seconds);
    }
  }

  /**
   * Fades in all RiText objects over the specified duration
   */
  public static final void fadeAllIn(float seconds)
  {
    for (Iterator i = instances.iterator(); i.hasNext();)
    {
      RiText p = (RiText) i.next();
      p.fadeIn(seconds);
    }
  }

  // getters / setters ----------------------------------------------

  protected static PFont checkFontCache(String fontFileName, float sz)
  {
    if (Defaults.fonts == null)
      Defaults.fonts = new HashMap();
    PFont pf = (PFont) Defaults.fonts.get(fontFileName + sz);
    // System.out.println("CacheCheck: "+fontFileName+sz+" -> "+(pf!=null));
    return pf;
  }

  /**
   * Returns the font specified after loading it and setting it as the current
   * font.
   */
  public PFont loadFont(String fontFileName)
  {
    return loadFont(fontFileName, -1);
  }

  /**
   * Returns the font specified after loading it and setting the current font
   * size.
   */
  public PFont loadFont(String fontFileName, float size)
  {
    PFont pf = _loadVlwFont(getPApplet(), fontFileName, size);
    this.font(pf/*, size*/);
    return pf;
  }

  // size is only used for cache
  protected static PFont _loadVlwFont(PApplet p, String fontFileName, float size)
  {
    PFont pf = checkFontCache(fontFileName, size);
    if (pf == null)
    {
      InputStream is = null;

      // try the filesystem...
      try
      {
        if (p != null)
        {
          // System.out.println("RiText._loadFont() -> Trying: "+fontFileName);
          is = p.createInput(fontFileName);
        }

        if (is == null)
          is = RiTa.openStream(fontFileName);

        pf = new PFont(is);
      }
      catch (Throwable e)
      {
        String errStr = "Could not load font '" + fontFileName + "'. Make "
            + "sure that the font\nhas been copied to the data folder"
            + " of your sketch\nError=" + e.getMessage();
        
        throw new RiTaException(errStr);
      }
      finally
      {
        if (is != null)
          try
          {
            is.close();
          }
          catch (IOException e) { }
      }
      
      cacheFont(fontFileName, size, pf); // add to cache
    }
    
    return pf;
  }

  protected static void cacheFont(String fontFileName, float fontSz, PFont pf)
  {
    // System.out.println("caching: "+fontFileName+fontSz+"->"+pf);
    if (Defaults.fonts == null)
      Defaults.fonts = new HashMap();
    Defaults.fonts.put(fontFileName + fontSz, pf);
  }

  protected static PFont _createFont(PApplet p, String fontName, float size)
  {
    PFont pf = checkFontCache(fontName, size);
    // System.out.println("Checking cache: "+fontName+"-"+sz);
    if (pf == null)
    {
      // System.out.println("Creating font: "+fontName+"-"+sz);
      pf = p.createFont(fontName, size);
      cacheFont(fontName, size, pf);
    }
    return pf;
  }

  /**
   * Set the current boundingBox stroke-weight for this object
   */
  public RiText boundingStrokeWeight(float r)
  {
    this.bbStrokeWeight = r;
    return this;
  }

  /**
   * Returns the current text
   */
  public String text()
  {
    return (text == null) ? null : text.text();
  }

  /**
   * Sets the current text to this String
   */
  public RiText text(String _text)
  {
    if (this.text == null)
      this.text = new RiString(_text);
    else
      this.text.text(_text);
    return this;
  }

  /**
   * Sets boolean flag to show or hide the object public RiText
   * visible(boolean visible) { this.hidden = !visible; return this; }
   */

  /**
   * Sets the current text to the character
   */
  public RiText text(char ch)
  {
    return this.text(Character.toString(ch));
  }

  public String toString()
  {
    return "RiText['" + this.text() + "']";
  }

  /**
   * Returns true if the objects alpha is positive
   */
  public boolean isVisible()
  {
    return this.fillA > 0;
  }

  /** @exclude */
  protected synchronized void updateBehaviors()
  {
    for (int i = 0; behaviors != null && i < behaviors.size(); i++)
    {
      RiTextBehavior rtb = (RiTextBehavior) behaviors.get(i);
      if (rtb == null)
      {
        behaviors.remove(rtb);
        continue;
      }
      rtb.update();
    }
  }

  /**
   * Add a new behavior to this RiText's run queue
   */
  protected synchronized RiTextBehavior addBehavior(RiTextBehavior behavior)
  {
    if (behaviors == null)
      this.behaviors = new ArrayList();
    if (!behaviors.contains(behavior))
      this.behaviors.add(behavior);
    return behavior;
  }

  /**
   * Remove a Behavior from the RiText's run queue
   */
  protected RiText removeBehavior(RiTextBehavior behavior)
  {
    if (behaviors != null)
    {
      behaviors.remove(behavior);
      behavior.delete();
    }
    return this;
  }

  /**
   * Immediately marks all Behaviors in the RiText's run queue as complete and
   * causes them to fire their<code>behaviorCompleted()</code> methods.
   */
  protected void completeBehaviors()
  {
    if (behaviors == null)
      return;
    for (int i = 0; i < behaviors.size(); i++)
      ((RiTextBehavior) behaviors.get(i)).finish();
  }// NEEDs MORE TESTING!

  /**
   * Pauses (or unpauses) all Behaviors in the RiText's run queue
   */
  protected synchronized void pauseBehaviors(boolean paused)
  {
    if (behaviors == null)
      return;
    for (int i = 0; i < behaviors.size(); i++)
    {
      RiTextBehavior tb = (RiTextBehavior) behaviors.get(i);
      tb.setPaused(paused);
    }
  }

  /**
   * Remove all Behaviors from the RiText's run queue
   * 
   */
  protected synchronized void removeBehaviors()
  {
    if (behaviors == null)
      return;
    for (int i = 0; i < behaviors.size(); i++)
    {
      RiTextBehavior tb = (RiTextBehavior) behaviors.get(i);
      this.removeBehavior(tb);
    }
  }

  /**
   * Sets the position for the current RiText
   */
  public RiText position(float px, float py)
  {
    this.x = px;
    this.y = py;
    return this;
  }

  /**
   * Sets the 3d position for the current RiText
   */
  @SuppressWarnings("hiding")
  public RiText position(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }

  /**
   * Returns a list of behaviors for the object.
   */
  List behaviorList()
  {
    return this.behaviors;
  }

  /**
   * Returns a list of behaviors of the specified type for this object, where
   * type is generally one of (MOVE, FADE_IN, FADE_OUT, FADE_TO_TEXT, SCALE_TO,
   * etc.)
   */
  RiTextBehavior[] behaviorsByType(int type)
  {
    List l = RiTextBehavior.selectByType(behaviors, type);
    return (RiTextBehavior[]) l.toArray(new RiTextBehavior[l.size()]);
  }

  /**
   * Returns the behavior corresponding to the specified 'id'.
   */
  static RiTextBehavior behaviorById(int id)
  {
    return RiTextBehavior.getBehaviorById(id);
  }

  /**
   * Returns the current alignment (default=LEFT)
   */
  public int textAlign()
  {
    return this.alignment;
  }

  /**
   * Sets text alignment mode for the object.
   * 
   * @param align
   *          (RiTa.CENTER, RiTa.RIGHT, RiTa.LEFT[default])
   */
  public RiText textAlign(int align)
  {
    this.textMode(align);
    return this;
  }

  /**
   * Sets text alignment mode for the object.
   * 
   * @param align
   *          (RiTa.CENTER, RiTa.RIGHT, RiTa.LEFT[default])
   */
  public void textMode(int align)
  {
    if (align != RiTa.LEFT && align != RiTa.RIGHT && align != RiTa.CENTER)
      throw new RiTaException("Illegal alignment: use RiTa.LEFT, RiTa.CENTER, or RiTa.RIGHT");

    this.alignment = align;
  }

  public static float[] boundingBox(RiText rt) // add-to-api?
  {
    return boundingBox(rt);
  }
  
  /**
   * Returns an array (x,y,w,h) representing the aggregate
   * bounding box for all the ritexts in the array
   */
  public static float[] boundingBox(RiText[] rts) // add-to-api?
  {
    float minX=Float.MAX_VALUE, 
      maxX=-Float.MAX_VALUE, 
      minY=Float.MAX_VALUE, 
      maxY=-Float.MAX_VALUE;
      
    for (int i = 0; i < rts.length; i++) {
      
      float[] bb = rts[i].boundingBox();
      if (bb[0] < minX) minX = bb[0];
      if (bb[1] < minY) minY = bb[1];
      if (bb[0]+bb[2] > maxX) maxX = bb[0]+bb[2];
      if (bb[1]+bb[3] > maxY) maxY = bb[1]+bb[3];
    }
    
    return new float[] {minX, minY, maxX-minX, maxY-minY };
  }
  
  /**
   * Returns a rectangle representing the current screen position of the
   * bounding box
   */
  public float[] boundingBox()
  {
    updateBoundingBox();

    if (screenBoundingBox == null) // cached
      screenBoundingBox = new Rect();

    try
    {
      screenBoundingBox.set((x + boundingBox.x), (y/*boundingBox.y*/), (boundingBox.w * scaleX), (boundingBox.h * scaleY));
      // rotate and scale?
    }
    catch (Throwable e)
    {
      System.err.println(screenBoundingBox);
      System.err.println(boundingBox);
      e.printStackTrace();
    }

    return screenBoundingBox.asArray();
  }

  /**
   * Converts and returns a list of RiTexts as a RiText[]
   */
  protected static RiText[] toArray(List result)
  {
    return (RiText[]) result.toArray(EMPTY_ARRAY);
  }

  /**
   * Returns number of characters in the contained String
   */
  public int length()
  {
    return text.length();
  }

  public float wordOffset(int wordIdx)
  {
    return wordOffsetWith(this.font, words(), wordIdx);
  }

  protected float wordOffsetWith(PFont pfont, int wordIdx, String delim)
  {
    String[] words = text.text().split(delim);
    return this.wordOffsetWith(pfont, words, wordIdx);
  }

  protected float wordOffsetWith(Object pfont, String[] words, int wordIdx)
  {
    if (wordIdx >= words.length)
    {
      throw new IllegalArgumentException("\nBad wordIdx=" + wordIdx + " for "
          + RiTa.asList(words));
    }

    if (pfont == null)
      verifyFont();
    else
      pApplet.textFont((PFont) pfont);

    float xPos = this.x;
    if (wordIdx > 0)
    {
      String[] pre = new String[wordIdx];
      System.arraycopy(words, 0, pre, 0, pre.length);
      String preStr = RiTa.join(pre, SP) + SP;
      float tw = -1;
      if (pApplet != null)
        tw = pApplet.textWidth(preStr);
      if (alignment == RiTa.LEFT)
        xPos = this.x + tw;
      else if (alignment == RiTa.RIGHT)
        xPos = this.x - tw;
      else
        throw new RiTaException(badAlignMessage2(alignment));
    }
    return xPos;
  }

  /**
   * Returns the x-position (in pixels) for the character at 'charIdx'.
   */
  public float charOffset(int charIdx)
  {
    return positionForChar(defaultFont(pApplet), charIdx);
  }

  /**
   * Returns the x-position (in pixels) for the character at 'charIdx'.
   * 
   * @param pf
   */
  protected float positionForChar(PFont pf, int charIdx)
  {
    if (charIdx <= 0)
      return x;
    if (charIdx > length()) // -1?
      charIdx = length();
    String sub = text().substring(0, charIdx);
    pApplet.textFont(pf);
    return x + pApplet.textWidth(sub);
  }

  /** @exclude */
  public PApplet getPApplet()
  {
    if (pApplet == null)
      pApplet = globalContext();

    if (pApplet == null)
    {
      // changed DCH: 4.25.14
      throw new RiTaException("Null renderer! You must call "
          + "'RiTa.start(this);' at the top of you program...\n");
    }

    return pApplet;
  }

  // ========================= STATICS =============================

  /**
   * Immediately pauses all Behaviors in the RiText's run queue
   */
  protected static synchronized void pauseAllBehaviors(boolean paused)
  {
    RiText[] cts = RiText.getInstances();
    for (int i = 0; i < cts.length; i++)
      cts[i].pauseBehaviors(paused);
  }

  /**
   * Pops the last value off the array, disposes it, and returns the new array
   * (shortened by one element).
   * <p>
   * If there are no elements in the array, the original array is returned
   * unchanged.
   */
  protected static RiText[] popArray(RiText[] rts)
  {
    if (rts == null || rts.length < 1)
      return rts;
    RiText[] tmp = new RiText[rts.length - 1];
    System.arraycopy(rts, 0, tmp, 0, tmp.length);
    RiText.dispose(rts[rts.length - 1]);
    return tmp;
  }

  /**
   * Shifts the first value off the array, disposes it, and returns the new
   * array (shortened by one element).
   * <p>
   * If there are no elements in the array, the original array is returned
   * unchanged.
   */
  protected static RiText[] shiftArray(RiText[] rts)
  {
    if (rts == null || rts.length < 1)
      return rts;
    RiText[] tmp = new RiText[rts.length - 1];
    System.arraycopy(rts, 1, tmp, 0, tmp.length);
    return tmp;
  }

  protected static void constrainLines(List<RiText> ritexts, float y, float h, float leading)
  {
    float ascent = ritexts.get(0).textAscent();
    float descent = ritexts.get(0).textDescent();
    // System.out.println("RiText.constrainLines().ascent="+ascent+" descent="+descent+" leading="+leading);
    float maxY = y + h, currentY = y + ascent;

    RiText next = null;
    Iterator<RiText> it = ritexts.iterator();

    // set y-pos for those that fit
    while (it.hasNext())
    {
      next = it.next();
      next.position(next.x(), currentY);
      if (!_withinBoundsY(currentY, leading, maxY, descent))
        break;
      currentY += leading;
    }

    // then remove/delete the rest
    while (it.hasNext())
    {
      next = it.next();
      RiText.dispose(next);
      it.remove();
    }
  }

  public static boolean _withinBoundsY(float currentY, float leading, float maxY, float descent)
  {
    return _withinBoundsY(currentY, leading, maxY, descent, false);
  }
  
  public static boolean _withinBoundsY(float currentY, float leading, float maxY, float descent, boolean firstLine)
  {
    if (!firstLine)
      return currentY + leading <= maxY - descent;
    return currentY <= maxY - descent;
  }

  /**
   * Utility method to do regex replacement on a String
   * 
   * @param patternStr
   *          regex
   * @param fullStr
   *          String to check
   * @param replaceStr
   *          String to insert
   * @see Pattern
   */
  protected static String regexReplace(String patternStr, String fullStr, String replaceStr)
  {
    return Regex.getInstance().replace(patternStr, fullStr, replaceStr);
  }

  /**
   * Utility method to test whether a String partially matches a regex pattern.
   * 
   * @param patternStr
   *          regex String
   * @param fullStr
   *          String to check
   * @see Pattern
   */
  protected static boolean regexMatch(String patternStr, String fullStr)
  {
    return Regex.getInstance().test(patternStr, fullStr);
  }

  /**
   * Return the current font for this object
   */
  public Object font()
  {
    return font;
  }

  /**
   * Returns a 2 or 3-dimensional array with the objects x,y, or x,y,z position
   * (depending on the renderer)
   */
  public float[] position()
  {
    if (is3D(pApplet.g))
      return new float[] { x, y, z };
    return new float[] { x, y, };
  }

  /**
   * Returns a 3-dimensional array with the objects x,y,z scale (1=100% or
   * unscaled)
   */
  public float[] scale()
  {
    return new float[] { scaleX, scaleY, scaleZ };
  }

  /**
   * Draws all (visible) RiText objects
   */
  protected static void drawAll(PGraphics p)
  {
    for (int i = 0; i < instances.size(); i++)
    {
      RiText rt = instances.get(i);
      if (rt != null)
        rt.draw(p);
    }
  }

  protected static void drawAll(RiText[] rts, PGraphics p)
  {
    if (rts == null)
      return;
    for (int i = 0; i < rts.length; i++)
    {
      if (rts[i] != null)
        rts[i].draw(p);
    }
  }

  /** Draws all (visible) RiText objects */
  public static final void drawAll()
  {
    drawAll((PGraphics) null);
  }

  public static final void drawAll(RiText[] rts)
  {
    drawAll(rts, null);
  }

  /**
   * Returns a Map of all the features (key-value pairs) exists for this RiText
   */
  public Map features()
  {
    return text.features();
  }

  /**
   * Returns the character at the specified index
   */
  public String charAt(int index)
  {
    return text.charAt(index);
  }

  /**
   * Returns a new character sequence that is a subsequence of this sequence.
   */
  protected CharSequence subSequence(int start, int end)
  {
    return text.subSequence(start, end);
  }

  // rotate and scale =======================

  /**
   * Rotate the object via affine transform. This is same as rotateZ, but for 2D
   */
  public RiText rotate(float rotate)
  {
    this.rotateZ = rotate;
    return this;
  }

  /**
   * Gets the x-rotation for the object
   */
  public float rotateX()
  {
    return this.rotateX;
  }

  /**
   * Gets the y-rotation for the object
   */
  public float rotateY()
  {
    return this.rotateY;
  }

  /**
   * Gets the z-rotation for the object
   */
  public float rotateZ()
  {
    return this.rotateZ;
  }

  /**
   * Sets the x-rotation for the object
   */
  public RiText rotateX(float rotate)
  {
    this.rotateX = rotate;
    return this;
  }

  /**
   * Sets the y-rotation for the object
   */
  public RiText rotateY(float rotate)
  {
    this.rotateY = rotate;
    return this;
  }

  /**
   * Sets the z-rotation for the object
   * 
   */
  public RiText rotateZ(float rotate)
  {
    this.rotateZ = rotate;
    return this;
  }

  /**
   * Sets the x-scale for the object
   */
  public RiText scaleX(float scale)
  {
    this.scaleX = scale;
    return this;
  }

  /**
   * Sets the y-scale for the object
   */
  public RiText scaleY(float scale)
  {
    this.scaleY = scale;
    return this;
  }

  /**
   * Sets the z-scale for the object
   */
  public RiText scaleZ(float scale)
  {
    this.scaleZ = scale;
    return this;
  }

  /**
   * Uniformly scales the object on all dimensions (x,y,z)
   */
  public RiText scale(float scale)
  {
    scaleX = scaleY = scaleZ = scale;
    return this;
  }
  
  /**
   *  Scales the object on dimensions (x,y)
   */
  public RiText scale(float sX, float sY)
  {
    scale(new float[] { sX, sY});
    return this;
  }

  /**
   * Scales the object on all dimensions (x,y,z)
   * 
   * @exclude
   */
  public RiText scale(float sX, float sY, float sZ)
  {
    scale(new float[] { sX, sY, sZ });
    return this;
  }

  /**
   * Scales the object on either 2 or 3 dimensions (x,y,[z])
   */
  public RiText scale(float[] scales)
  {
    if (scales.length < 2)
      throw new RiTaException("scale(float[]) requires at least 2 values!");

    if (scales.length > 1)
    {
      scaleX = scales[0];
      scaleY = scales[1];
    }

    if (scales.length > 2)
      scaleZ = scales[2];

    return this;
  }

  /**
   * Returns the distance between the center points of the two RiTexts.
   */
  public float distanceTo(RiText rt)
  {
    float[] p1 = center();
    float[] p2 = rt.center();
    return distance(p1[0], p1[1], p2[0], p2[1]);
  }

  /**
   * Deletes the character at at the specified character index ('idx'). If the
   * specified 'idx' is less than xero, or beyond the length of the current
   * text, there will be no effect.
   */
  public RiText removeChar(int idx)
  {
    text.removeChar(idx);
    return this;
  }

  /**
   * Replaces the character at the specified character index ('idx') with the
   * 'replaceWith' character.
   * <p>
   * If the specified 'idx' is less than zero, or beyond the length of the
   * current text, there will be no effect.
   */
  public RiText replaceChar(int idx, char replaceWith)
  {
    text.replaceChar(idx, replaceWith);
    return this;
  }

  /**
   * Replaces the character at 'idx' with 'replaceWith'. If the specified 'idx'
   * is less than xero, or beyond the length of the current text, there will be
   * no effect.
   */
  public RiText replaceChar(int idx, String replaceWith)
  {
    text.replaceChar(idx, replaceWith);
    return this;
  }

  /**
   * Inserts the character at the specified character index ('idx'). If the
   * specified 'idx' is less than zero, or beyond the length of the current
   * text, there will be no effect.
   */
  public RiText insertChar(int idx, char toInsert)
  {
    text.insertChar(idx, toInsert);
    return this;
  }

  /*
   * TODO: add lazy(er) updates
   * what about rotate and scales? 
   */
  protected void updateBoundingBox()
  {
    verifyFont(); // we need this here (really!)

    if (boundingBox == null)
      boundingBox = new Rect();

    if (font == null)
      return;

    float fontSz = font.getSize();
    if (this.fontSizeAdjustment > 0)
      fontSz = fontSizeAdjustment;
    
    float ascent = font.ascent() * fontSz; // fix for P5 bug
    float descent = font.descent() * fontSz;
    float bbw = textWidth();
    float bbh = ascent + descent;

    // offsets from RiText.x/y
    float bbx = -bbw / 2f;
    float bby = -ascent;

    if (alignment == RiTa.LEFT)
      bbx += bbw / 2f;// + bbPadding / 2f;

    else if (alignment == RiTa.CENTER)
      ; // ok as is

    else if (alignment == RiTa.RIGHT)
      bbx -= bbw / 2f;// + bbPadding / 2f;

    else
      throw new RiTaException(badAlignMessage3(alignment));

    boundingBox.set(bbx, bby, bbw, bbh);
  }

  // hmmm, need to rethink, maybe use reflection?
  /**
   * Returns a field for field copy of <code>toCopy</code>
   */
  protected static RiText copy(RiText toCopy)
  {
    RiText rt = new RiText(toCopy.pApplet);
    rt.font = toCopy.font;
    rt.fontFamily = toCopy.fontFamily;
    rt.fontSizeAdjustment = toCopy.fontSizeAdjustment;

    rt.behaviors = toCopy.behaviors; // deep or shallow?
    //rt.text = new RiString(toCopy.text.text());
    rt.text = toCopy.text.copy();
    rt.autodraw = toCopy.autodraw;

    rt.x = toCopy.x;
    rt.y = toCopy.y;
    rt.z = toCopy.z;

    rt.fillR = toCopy.fillR;
    rt.fillG = toCopy.fillG;
    rt.fillB = toCopy.fillB;
    rt.fillA = toCopy.fillA;

    rt.boundingBoxVisible = toCopy.boundingBoxVisible;
    rt.bbFillR = toCopy.bbFillR;
    rt.bbFillG = toCopy.bbFillG;
    rt.bbFillB = toCopy.bbFillB;
    
    rt.bbStrokeR = toCopy.bbStrokeR;
    rt.bbStrokeG = toCopy.bbStrokeG;
    rt.bbStrokeB = toCopy.bbStrokeB;
    rt.bbStrokeWeight = toCopy.bbStrokeWeight;

    rt.alignment = toCopy.alignment;
    rt.motionType = toCopy.motionType;

    rt.scaleX = toCopy.scaleX;
    rt.scaleY = toCopy.scaleY;
    rt.scaleZ = toCopy.scaleZ;

    rt.rotateX = toCopy.rotateX;
    rt.rotateY = toCopy.rotateY;
    rt.rotateZ = toCopy.rotateZ;
/*
    // add the features , no, this happens in RiString
    Map m = toCopy.features(), features = rt.features();
    for (Iterator it = m.keySet().iterator(); it.hasNext();)
    {
      CharSequence key = (CharSequence) it.next();
      features.put(key, m.get(key));
    }*/
    
    return rt;
  }

  public RiText showBounds(boolean b)
  {
    this.boundingBoxVisible = b;
    return this;
  }

  public boolean showBounds()
  {
    return boundingBoxVisible;
  }

  public int align()
  {
    return this.alignment;
  }

  public static final float distance(float x1, float y1, float x2, float y2)
  {
    float dx = x1 - x2, dy = y1 - y2;
    return (float) Math.sqrt(dx * dx + dy * dy);
  }

  public static int timer(float period)
  { // for better error msg
    throw new RiTaException("Missing parent object -- did you mean: RiText.timer(this, "
        + period + ");");
  }

  public static final int timer(Object parent, float period)
  {
    return RiTa.timer(parent, period);
  }

  public static final int timer(Object parent, float period, String callback)
  {
    return RiTa.timer(parent, period, callback);
  }
    
  public static final void stopTimer(int idx)
  {
    RiTa.stopTimer(idx);
  }

  public static final void pauseTimer(int idx, boolean b)
  {
    RiTa.pauseTimer(idx, b);
  }

  public static final void pauseTimer(int idx, float pauseFor)
  {
    RiTa.pauseTimer(idx, pauseFor);
  }

  public static final float[] randomColor()
  {
    return randomColor(0,256, false);
  }
  
  public static final float[] randomColor(float max)
  {
    return randomColor(0, max, false);
  }
  
  public static final float[] randomColor(float max, boolean includeAlpha)
  {
    return randomColor(0, max, includeAlpha);
  }
  
  public static final float[] randomColor(float min, float max, boolean includeAlpha)
  {
    return !includeAlpha ? new float[] { random(min,max), random(min,max), random(min,max) }
      : new float[] { random(min,max), random(min,max), random(min,max), random(min,max) };
  }

  public static final float random()
  {
    return random(0f, 1f);
  }
    
  public static final int random(int f)
  {
    return (int) random((float) f);
  }

  public static final int random(int f, int g)
  {
    return (int) random((float) f, (float) g);
  }

  static Random internalRandom;

  public static final float random(float high)
  {
    if (high == 0)
      return 0;

    // internal random number object
    if (internalRandom == null)
      internalRandom = new Random();

    float value = 0;
    do
    {
      // value = (float)Math.random() * howbig;
      value = internalRandom.nextFloat() * high;
    }
    while (value == high);

    return value;
  }

  public static final float random(float low, float high)
  {
    if (low >= high)
      return low;
    float diff = high - low;
    return random(diff) + low;
  }

  public RiText stopBehavior(int id)
  {
    RiTextBehavior.getBehaviorById(id).stop();
    return this;
  }

  public RiText stopBehaviors()
  {
    if (behaviors != null)
    {
      for (int i = 0; i < behaviors.size(); i++)
      {
        RiTextBehavior tb = (RiTextBehavior) behaviors.get(i);
        this.removeBehavior(tb);
      }
    }
    return this;
  }

  public boolean contains(String s)
  {
    return this.text.text().contains(s); // delegate?
  }

  public float distanceTo(float px, float py)
  {
    return PApplet.dist(this.x, this.y, px, py);
  }

  public float textAscent()
  {
    verifyFont();
    return font.ascent() * font.getSize(); // fix for bug in PApplet.textAscent
  }

  public float textDescent()
  {
    verifyFont();
    return font.descent() * font.getSize();
  }

  public float fontSize()
  {
    if (this.fontSizeAdjustment > 0)
      return fontSizeAdjustment;
    return this.font != null ? this.font.getSize() : defaults.fontSize;
  }

  public RiText align(int i)
  {
    this.alignment = i;
    return this;
  }

  public RiText rotate(float rx, float ry, float rz)
  {
    this.rotateX = rx;
    this.rotateY = ry;
    this.rotateZ = rz;
    return this;
  }

  public float[] rotate()
  {
    return new float[] { this.rotateZ, this.rotateY, this.rotateZ };
  }

  public RiText analyze()
  {
    text.analyze();
    return this;
  }

  public RiText concat(String cs)
  {
    text.concat(cs);
    return this;
  }

  public RiText concat(RiString cs)
  {
    text.concat(cs);
    return this;
  }

  public boolean endsWith(String suffix)
  {
    return text.endsWith(suffix);
  }

  /**
   * Returns true iff the supplied String matches the text for this RiText
   * ignoring case
   */
  public boolean equalsIgnoreCase(String cs)
  {
    return cs != null && text.equalsIgnoreCase(cs);
  }

  /**
   * Returns true iff the supplied String matches the text for this RiText
   */
  public boolean equals(String cs)
  {
    return cs != null && text.equals(cs);
  }

  public String get(String featureName)
  {
    return text.get(featureName);
  }
  
  public RiText set(String name, String value)
  {
    text.set(name, value);
    return this;
  }

  public int indexOf(String s)
  {
    return text.indexOf(s);
  }

  public int lastIndexOf(String s)
  {
    return text.lastIndexOf(s);
  }

  public RiText insertWord(int wordIdx, String newWord)
  {
    text.insertWord(wordIdx, newWord);
    return this;
  }

  public String[] pos()
  {
    return text.pos();
  }

  public String[] pos(boolean useWordNetTags)
  {
    return text.pos(useWordNetTags);
  }

  public String posAt(int wordIdx)
  {
    return text.posAt(wordIdx);
  }

  public String posAt(int wordIdx, boolean useWordNetTags)
  {
    return text.posAt(wordIdx, useWordNetTags);
  }

  public RiText replaceWord(int wordIdx, String newWord)
  {
    text.replaceWord(wordIdx, newWord);
    return this;
  }

  public RiText replaceFirst(String regex, String replacement)
  {
    text.replaceFirst(regex, replacement);
    return this;
  }

  public RiText replaceAll(String regex, String replacement)
  {
    text.replaceAll(regex, replacement);
    return this;
  }

  public String slice(int i, int j)
  {
    return text.slice(i, j);
  }

  /**
   * Splits the current object into an array of RiTexts, one per word,
   * maintaining the x and y position of each. Note: In most cases the original
   * RiText should be disposed manually to avoid text a doubling effect (via
   * RiText.dispose(originalRiText)).
   */
  public RiText[] splitWords()
  {
    Object pf = font();
    List result = new ArrayList();
    String[] txts = text().split(SP);
    for (int i = 0; i < txts.length; i++)
    {
      if (txts[i] != null && txts[i].length() > 0)
      {
        float xPos = wordOffsetWith(pf, txts, i);
        result.add(new RiText(pApplet, txts[i], xPos, this.y));
      }
    }
    return toArray(result);
  }

  /**
   * Splits the current object into an array of RiTexts, one per letter,
   * maintaining the x and y position of each. Note: In most cases the original
   * RiText should be disposed manually to avoid text a doubling effect (via
   * RiText.dispose(originalRiText)).
   */
  public RiText[] splitLetters()
  {
    Object pf = font();
    if (pApplet != null)
      pApplet.textFont((PFont) pf);

    String measure = E;
    List result = new ArrayList();
    char[] chars = text.toCharArray();
    for (int i = 0; i < chars.length; i++)
    {
      if (chars[i] != ' ')
      {
        float tw = pApplet != null ? pApplet.textWidth(measure) : 0;
        result.add(new RiText(pApplet, chars[i], this.x + tw, this.y));
      }
      measure += chars[i];
    }
    return toArray(result);
  }

  /*
   * List l = new ArrayList(); String[] chars = splitChars(); for (int i = 0; i
   * < chars.length; i++) { float mx = positionForChar(pf, i); l.add(new
   * RiText(_pApplet, chars[i], mx, y, pf)); } return (RiText[]) l.toArray(new
   * RiText[l.size()]);
   */

  public boolean startsWith(String cs)
  {
    return text.startsWith(cs);
  }

  public String substring(int start, int end)
  {
    return text.substring(start, end);
  }

  public String substring(int start)
  {
    return text.substring(start);
  }

  public String substr(int i, int j)
  {
    return text.substr(i, j);
  }

  /*
   * public char[] toCharArray() { return text.toCharArray(); }
   */

  public RiText toLowerCase()
  {
    text.toLowerCase();
    return this;
  }

  public RiText toUpperCase()
  {
    text.toUpperCase();
    return this;
  }

  public RiText trim()
  {
    text.trim();
    return this;
  }

  public String wordAt(int wordIdx)
  {
    return text.wordAt(wordIdx);
  }

  public int wordCount()
  {
    return text.wordCount();
  }

  public String[] words()
  {
    return text.words();
  }

  public String[] match(String s)
  {
    return text.match(s);
  }

  public String[] match(String s, int flags)
  {
    return text.match(s, flags);
  }

  public RiText removeWord(int idx)
  {
    text.removeWord(idx);
    return this;
  }

  public RiText concat(RiText cs)
  {
    text.concat(cs.text);
    return this;
  }

  // //// 12 methods for each

  // TODO: should take an optional array of RiTexts ?
  public static RiText[] createLines(PApplet p, String txt, float x, float y)
  {
    float maxWidth = p.width;

    if (defaults.alignment == RiTa.LEFT)
      maxWidth = p.width - x;
    else if (defaults.alignment == RiTa.RIGHT)
      maxWidth = p.width - (p.width - x); // x?
    else
      throw new RiTaException(badAlignMessage2(defaults.alignment));

    return createLines(p, txt, x, y, maxWidth, Float.MAX_VALUE);
  }
  
  protected static String badAlignMessage2(int supplied) {
    
    return "Only RiTa.LEFT("+RiTa.LEFT+") & RiTa.RIGHT("+
        RiTa.RIGHT+") alignments supported, found: "+supplied;
  }
  
  protected static String badAlignMessage3(int supplied) {
    
    return "Only RiTa.LEFT("+RiTa.LEFT+"), RiTa.CENTER("+RiTa.CENTER+
        ") & RiTa.RIGHT("+RiTa.RIGHT+") alignments supported, found: "+supplied;
  }

  public static RiText[] createLines(PApplet p, String txt, float x, float y, float w)
  {
    return createLines(p, txt, x, y, w, Float.MAX_VALUE);
  }

  public static final RiText[] createLines(PApplet p, String txt, float x, float y, float w, float h)
  {
    return createLines(p, txt, x, y, w, h, defaultFont(p));
  }

  public static final RiText[] createLines(PApplet p, String txt, float x, float y, float w, float h, float lead)
  {
    return createLines(p, txt, x, y, w, h, defaultFont(p), lead);
  }

  public static final RiText[] createLines(PApplet p, String txt, float x, float y, float w, float h, PFont pf)
  {
    return createLines(p, txt, x, y, w, h, pf, computeLeading(pf));
  }

  public static final RiText[] createLines(PApplet p, String txt, float x, float y, float w, float h, PFont pf, float lead)
  {
    // System.out.println("RiText.createLines("+txt.length()+", "+x+", "+y+", "+w+", "+h+", "+lead+");");

    if (txt.length() == 0)
      return EMPTY_ARRAY;

    if (w <= 0)
      w = Float.MAX_VALUE;
    if (h <= 0)
      h = Float.MAX_VALUE;

    PageLayout rp = new PageLayout(p, new Rect(x, y, w, h), p.width, p.height);
    rp.paragraphIndent = defaults.paragraphIndent;

    return rp.layout(pf, txt, lead);
  }

  // arrays

  public static final RiText[] createLines(PApplet p, String[] lines, float x, float y)
  {
    PFont pf = defaultFont(p); // no width, so respect the line-breaks in the
                               // array
    return layoutArray(p, pf, lines, x, y, Float.MAX_VALUE, computeLeading(pf));
  }

  public static final RiText[] createLines(PApplet p, String[] lines, float x, float y, float w)
  {
    return createLines(p, lines, x, y, w, Float.MAX_VALUE);
  }

  public static final RiText[] createLines(PApplet p, String[] lines, float x, float y, float w, float h)
  {
    return createLines(p, lines, x, y, w, h, defaultFont(p));
  }

  public static final RiText[] createLines(PApplet p, String[] lines, float x, float y, float w, float h, PFont pf)
  {
    return createLines(p, RiTa.join(lines, SP), x, y, w, h, pf, computeLeading(pf));
  }

  public static final RiText[] createLines(PApplet p, String[] lines, float x, float y, float w, float h, float lead)
  {
    return createLines(p, RiTa.join(lines, SP), x, y, w, h, defaultFont(p), lead);
  }

  /*
   * public static final RiText[] createLines(PApplet p, String[] lines, float
   * x, float y, float w, float h, PFont pf, float lead) { return layoutArray(p,
   * pf, lines, x, y, h, lead); // width is ignored here }
   */

  // ////

  public static RiText[] createWords(PApplet p, String txt, float x, float y)
  {
    return createWords(p, txt, x, y, p.width - x, Float.MAX_VALUE);
  }

  public static RiText[] createWords(PApplet p, String txt, float x, float y, float w)
  {
    return createWords(p, txt, x, y, w, Float.MAX_VALUE);
  }

  public static final RiText[] createWords(PApplet p, String txt, float x, float y, float w, float h)
  {
    return createWords(p, txt, x, y, w, h, defaultFont(p));
  }

  public static final RiText[] createWords(PApplet p, String txt, float x, float y, float w, float h, float lead)
  {
    return createWords(p, txt, x, y, w, h, defaultFont(p), lead);
  }

  public static final RiText[] createWords(PApplet p, String txt, float x, float y, float w, float h, PFont pf)
  {
    return createWords(p, txt, x, y, w, h, pf, computeLeading(pf));
  }

  public static final RiText[] createWords(PApplet p, String txt, float x, float y, float w, float h, PFont pf, float lead)
  {
    // System.out.println("RiText.createWords("+txt.length()+", "+x+", "+y+", "+w+", "+h+")");
    return linesToWords(createLines(p, txt, x, y, w, h, pf, lead)).toArray(EMPTY_ARRAY);
  }

  //

  public static final RiText[] createWords(PApplet p, String[] lines, float x, float y)
  {
    return linesToWords(createLines(p, lines, x, y)).toArray(EMPTY_ARRAY);
  }

  public static final RiText[] createWords(PApplet p, String[] lines, float x, float y, float w)
  {
    return createWords(p, lines, x, y, w, Float.MAX_VALUE, defaultFont(p));
  }

  public static final RiText[] createWords(PApplet p, String[] lines, float x, float y, float w, float h)
  {
    return createWords(p, lines, x, y, w, h, defaultFont(p));
  }

  public static final RiText[] createWords(PApplet p, String[] lines, float x, float y, float w, float h, PFont pf)
  {
    return createWords(p, lines, x, y, w, h, pf, computeLeading(pf));
  }

  public static final RiText[] createWords(PApplet p, String[] lines, float x, float y, float w, float h, float lead)
  {
    return createWords(p, lines, x, y, w, h, defaultFont(p), lead);
  }

  public static final RiText[] createWords(PApplet p, String[] lines, float x, float y, float w, float h, PFont pf, float lead)
  {

    return linesToWords(createLines(p, RiTa.join(lines, SP), x, y, w, h, pf, lead)).toArray(EMPTY_ARRAY);
  }

  // ////

  public static RiText[] createLetters(PApplet p, String txt, float x, float y)
  {
    return createLetters(p, txt, x, y, p.width - x, Float.MAX_VALUE);
  }

  public static RiText[] createLetters(PApplet p, String txt, float x, float y, float w)
  {
    return createLetters(p, txt, x, y, w, Float.MAX_VALUE);
  }

  public static final RiText[] createLetters(PApplet p, String txt, float x, float y, float w, float h)
  {
    return createLetters(p, txt, x, y, w, h, defaultFont(p));
  }

  public static final RiText[] createLetters(PApplet p, String txt, float x, float y, float w, float h, float lead)
  {
    return createLetters(p, txt, x, y, w, h, defaultFont(p), lead);
  }

  public static final RiText[] createLetters(PApplet p, String txt, float x, float y, float w, float h, PFont pf)
  {
    return createLetters(p, txt, x, y, w, h, pf, computeLeading(pf));
  }

  public static final RiText[] createLetters(PApplet p, String txt, float x, float y, float w, float h, PFont pf, float lead)
  {
    return linesToLetters(createLines(p, txt, x, y, w, h, pf, lead)).toArray(EMPTY_ARRAY);
  }

  // /

  public static final RiText[] createLetters(PApplet p, String[] lines, float x, float y)
  {
    return linesToLetters(createLines(p, lines, x, y)).toArray(EMPTY_ARRAY);
  }

  public static final RiText[] createLetters(PApplet p, String[] lines, float x, float y, float w)
  {
    return createLetters(p, lines, x, y, w, Float.MAX_VALUE, defaultFont(p));
  }

  public static final RiText[] createLetters(PApplet p, String[] lines, float x, float y, float w, float h)
  {
    return createLetters(p, lines, x, y, w, h, defaultFont(p));
  }

  public static final RiText[] createLetters(PApplet p, String[] lines, float x, float y, float w, float h, PFont pf)
  {
    return createLetters(p, lines, x, y, w, h, pf, computeLeading(pf));
  }

  public static final RiText[] createLetters(PApplet p, String[] lines, float x, float y, float w, float h, float lead)
  {
    return createLetters(p, lines, x, y, w, h, defaultFont(p), lead);
  }

  public static final RiText[] createLetters(PApplet p, String[] lines, float x, float y, float w, float h, PFont pf, float lead)
  {
    return linesToLetters(createLines(p, RiTa.join(lines, SP), x, y, w, h, pf, lead)).toArray(EMPTY_ARRAY);
  }

  // ////

  public static void resetDefaults()
  {
    Defaults.reset();
  }

  /*
   * Ignores any HTML markup and layouts the lines exactly as they are...
   */
  private static RiText[] layoutArray(PApplet pApplet, PFont font, String[] lines, float x, float y, float h, float lead)
  {
    // System.out.println("createLinesByCount("+RiTa.asList(lines)+","+startX+","+startY+","+leading+")");

    if (lines == null || lines.length < 1)
      return EMPTY_ARRAY;

    List<RiText> ritexts = new LinkedList<RiText>();

    for (int i = 0; i < lines.length; i++)
      ritexts.add(new RiText(pApplet, lines[i], x + 1, y).font(font));

    constrainLines(ritexts, y, h, lead);

    return ritexts.toArray(EMPTY_ARRAY);
  }

  private static List<RiText> linesToWords(RiText[] rlines)
  {
    List<RiText> result = new ArrayList();
    for (int i = 0; rlines != null && i < rlines.length; i++)
    {
      RiText[] rts = rlines[i].splitWords();
      PFont pf = (PFont) rts[0].font();
      for (int j = 0; j < rts.length; j++)
        result.add(rts[j].font(pf));
      RiText.dispose(rlines[i]);
    }
    return result;
  }

  private static List<RiText> linesToLetters(RiText[] rlines)
  {
    List<RiText> result = new ArrayList();
    for (int i = 0; rlines != null && i < rlines.length; i++)
    {
      RiText[] rts = rlines[i].splitLetters();
      PFont pf = (PFont) rts[0].font();
      for (int j = 0; j < rts.length; j++)
        result.add(rts[j].font(pf));
      RiText.dispose(rlines[i]);
    }
    return result;
  }

  private static float computeLeading(PFont pf)
  {
    return pf.getSize() * defaults.leadingFactor;
  }

  public boolean autodraw()
  {
    return autodraw;
  }

  public float x()
  {
    return x;
  }

  public float y()
  {
    return y;
  }
  
  ////////////////////////////////// Font-nonsense //////////////////////////////////
  

  public RiText fontSize(float sz)
  {
    verifyFont();
    
    if (this.font.getSize() == sz) {
      
      this.fontSizeAdjustment = 0;
      return this; // nothing to do
    }

    handleVlwFontScale(this.font, sz);

    return this;
  } 
  
  public RiText font(String name, float size)
  {
//System.out.println("RiText.font("+name+","+size+")");
    return this.font(name).fontSize(size);
  }
    
  protected String computeFontName(String defaultName) {
    
    String name = null;
    
    if (this.font != null)  // try font
      name = this.font.getName();
    
    if (name == null) // try font-family
      name = this.fontFamily;  
    
    if (name == null && defaultName != null) // try arg
      name = defaultName;  
    
    if (name == null)  
      throw new RiTaException("Unable to compute name for current font: "+this.font+" size="+this.font.getSize()+" ps="+this.font.getPostScriptName()); // tmp-remove
    
    return name;
  }
    
  public RiText font(Object pf)
  {
    this.fontSizeAdjustment = 0;
    if (pf instanceof String)
    {
      String fname = (String) pf;
      PFont pfont = fontFromString(pApplet, fname);
      return assignFont(pfont, fname);
    }
    
    if (!(pf instanceof PFont))
      throw new RiTaException("Expected PFont, but found: " + pf.getClass());    
        
    return assignFont((PFont) pf);
  }

  protected static PFont fontFromString(PApplet p, String fname)
  {
    if (fname == null) 
      throw new RiTaException("Null fontName in fontFromString()!");    

    return fname.endsWith(".vlw") ? _loadVlwFont(p, fname, -1)
      : _createFont(p, fname, RiText.defaults.fontSize);
  }
  
  protected static PFont fontFromString(PApplet p, String fname, float fsize)
  {
    if (fname == null) 
      throw new RiTaException("Null fontName in fontFromString()!");    

    return fname.endsWith(".vlw") ? _loadVlwFont(p, fname, fsize)
      : _createFont(p, fname, fsize);
  }
  

  /////////////////////////////////////////////////////////////////////////////////
  // CASE: we have a valid pfont but user wants to change size
  /////////////////////////////////////////////////////////////////////////////////
  protected void handleVlwFontScale(PFont pf, float fsize)
  {
    String name = pf.getName() != null ? pf.getName() 
        : pf.toString().replaceAll(".*?@","@");
    
    if (fsize > pf.getSize()) {
     
      System.err.println("[WARN] The font '"+name+"' has size="
          + pf.getSize() + ", but fontSize of "+fsize+" was requested. " 
          + "Upscaled bitmap fonts are not likely to render well...");
    }
    
    this.fontSizeAdjustment = fsize; // hack
  }

  protected RiText assignFont(PFont pf) {
    
    return this.assignFont(pf, null);
  }
  
  protected RiText assignFont(PFont pf, String fname)
  {   
    this.font = pf;
    this.fontFamily = computeFontName(fname);

    return this;
  }

}// end

