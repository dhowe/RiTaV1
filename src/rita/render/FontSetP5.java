package rita.render;

import processing.core.PApplet;
import processing.core.PFont;

public class FontSetP5
{
  private static final String DEFAULT_FONT_FAMILY = "Times";
  private static final int DEFAULT_FONT_SIZE = 14;
  
  protected static PFont defaultFont;
  
  protected PFont font;
  protected PApplet pApplet;

  public FontSetP5(PApplet p, PFont pf)
  {
    if (defaultFont == null) 
      initDefaultFont(p);
    this.pApplet = p;
    this.font(pf);
  }

  protected static void initDefaultFont(PApplet p)
  {
    defaultFont = p.createFont(DEFAULT_FONT_FAMILY, DEFAULT_FONT_SIZE);
  }
    
  // instances ============================================================

  public void font(PFont pf)
  {
    if (pf != null)
      this.font = pf;
  }

  public PFont font()  // may never be null
  {
    return this.font;
  }

  public float fontSize() 
  {
    return this.font.getSize();
  }

  public static void defaultFont(PFont pf)
  {
    if (pf != null)
      defaultFont = pf;
  }

  public static PFont defaultFont() // may be null
  {
    return defaultFont;
  }

  public static float defaultFontSize()
  {
    return defaultFont != null ? defaultFont.getSize() : DEFAULT_FONT_SIZE;
  }
  
}
