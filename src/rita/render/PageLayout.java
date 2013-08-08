package rita.render;

import java.util.*;

import processing.core.*;
import rita.*;
import rita.support.Constants;
import rita.support.Rect;

public class PageLayout implements Constants
{ 
  public float paragraphIndent, paragraphLeading, textColor[];
  public boolean showPageNumbers, indentFirstParagraph;
  public int pageWidth, pageHeight, pageNo = 1;
  public RiTextIF header, footer;
  public Rect textRectangle;
  
  protected Stack words;
  protected RiTextIF lines[];
  protected PApplet _pApplet;
  
  public PageLayout(PApplet pApplet, int leftMargin, int topMargin, int rightMargin, int bottomMargin)
  {
    this(pApplet, leftMargin, topMargin, rightMargin, bottomMargin, pApplet.width, pApplet.height);
  }

  public PageLayout(PApplet pApplet, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int pageWidth, int pageHeight)
  {
    this(pApplet, new Rect(leftMargin, topMargin, pageWidth - (leftMargin + rightMargin), pageHeight - (topMargin + bottomMargin)), pageWidth, pageHeight);
  }

  public PageLayout(PApplet pApplet, Rect rect, int pageWidth, int pageHeight) {
    
    this._pApplet = (pApplet);
    this.textRectangle = rect;
    this.pageWidth = pageWidth;
    this.pageHeight = pageHeight;
    
    paragraphIndent = RiText.defaults.paragraphIndent;
    paragraphLeading = RiText.defaults.paragraphLeading;
    indentFirstParagraph = RiText.defaults.indentFirstParagraph;
  }

  public RiTextIF[] layoutFromFile(String fileName)
  {
    PFont font = RiText.defaultFont(_pApplet);
    return layoutFromFile(font, fileName, font.getSize() * RiText.defaults.leadingFactor);
  }
  
  public RiTextIF[] layoutFromFile(String fileName, float leading)
  {
    return layoutFromFile(RiText.defaultFont(_pApplet), fileName, leading);
  }
  
  /**
   * Creates an array of RiTextIF, one per line from the text loaded from the
   * specified 'fileName', and lays it out on the page according to the specified
   * font.
   */
  public RiTextIF[] layoutFromFile(PFont pf, String fileName, float leading)
  {
    String txt = RiTa.loadString(_pApplet, fileName);
    return layout(pf, txt.replaceAll("[\\r\\n]", " "), leading);
  }
  
  public RiTextIF[] layout(String text)
  {
    PFont font = RiText.defaultFont(_pApplet);
    return layout(font, text, font.getSize() * RiText.defaults.leadingFactor);
  }

  /**
   * Creates an array of RiTextIF, one per line from the input text
   * and lays it out on the page.
   */
  public RiTextIF[] layout(String text, float leading)
  {
    return layout(RiText.defaultFont(_pApplet), text, leading);
  }
  
  /**
   * Creates an array of RiTextIF, one per line from the input text
   * and lays it out on the page.
   */
  public RiTextIF[] layout(PFont pf, String text, float leading)
  {
    // System.out.println("RiPageLayout.layout("+text.length()+")");

    if (showPageNumbers) {
      footer(Integer.toString(pageNo));
    }

    if (text != null && text.length() > 0) {

      // remove any line breaks from the original
      text = text.replaceAll("\n", SP);
  
      // adds spaces around html tokens
      text = text.replaceAll(" ?(<[^>]+>) ?", " $1 ");
  
      this.words = new Stack();
  
      addToStack(text.split(SP));
      
      this.lines = renderPage(pf, leading);
    }
    else {
      this.lines = RiText.EMPTY_ARRAY;
    }
    
    return lines;
  }

  RiTextIF[] renderPage(PFont pf, float leading)
  {

    if (words.isEmpty()) return RiText.EMPTY_ARRAY;
    
    if (pf == null && _pApplet != null)
      pf = RiText.defaultFont(_pApplet);
      
    if (pf == null)
      throw new RiTaException("Null font passed to PageLayout.renderPage()");
    
    if (leading <= 0)
      leading = pf.getSize() * RiText.defaults.leadingFactor;
    
    leading = (int) leading; // same as JS
    
    float ascent = pf.ascent() * pf.getSize();
    float descent = pf.descent() * pf.getSize();
    float startX = textRectangle.x + 1;
    float currentY = textRectangle.y + ascent;
    float maxX = (textRectangle.x + textRectangle.w);
    float maxY = (textRectangle.y + textRectangle.h);
    boolean newParagraph = false, forceBreak = false, firstLine = true;
    
    //System.out.println("renderPage: font="+pf.getSize()+"/" + leading
      //+ " pIndent="+paragraphIndent+" indent1st="+indentFirstParagraph
      //+ " ascent="+ascent+" descent="+descent+" leading="+leading);
    
    List rlines = new ArrayList();
    StringBuilder sb = new StringBuilder();

    if (indentFirstParagraph) 
      startX += RiText.defaults.paragraphIndent;
    
    this._pApplet.textFont(pf);
        
    while (!words.isEmpty())
    {
      String next = (String) words.pop();
      
      if (next.length() == 0) continue;

      // check for HTML-style tags 
      if (next.startsWith("<") && next.endsWith(">"))
      {
        if (next.equals(NON_BREAKING_SPACE))
        {
          sb.append(SP);
        }
        else if (next.endsWith(PARAGRAPH_BREAK))
        {
          if (sb.length() > 0)  {   // case: paragraph break
            newParagraph = true;
          }
        }
        else if (next.endsWith(LINE_BREAK)) {
          forceBreak = true;
        }
        continue;
      }

      // re-calculate our X position
      float currentX = startX + _pApplet.textWidth(sb.toString() + next);

      // check it against the line-width 
      if (!newParagraph && !forceBreak && currentX < maxX)
      {
        sb.append(next + SP);
      }
      else 
      {
         // check yPosition for line break
        if (RiText._withinBoundsY(currentY, leading, maxY, descent))
        {
          float yPos = firstLine ? currentY : currentY + (int)leading; // or round?
          RiTextIF rt = newRiTextLine(sb, pf, startX, yPos);
          rlines.add(rt);
          
          currentY = newParagraph ? rt.y() + paragraphLeading : rt.y();
          startX = textRectangle.x + 1; // reset

          if (newParagraph) startX += RiText.defaults.paragraphIndent;
          
          sb.append(next + SP);
          
          newParagraph = false;
          forceBreak = false;
          firstLine = false;
        }
        else {
          words.push(next);
          break;
        }
      }
    }
    
    // check if leftover words can make a new line 
    if (RiText._withinBoundsY(currentY, leading, maxY, descent)) {
      
      // TODO: what if there is are tags in here -- is it possible?)
      rlines.add(newRiTextLine(sb, pf, textRectangle.x+1, leading + currentY));
    }
    else
      addToStack(sb.toString().split(SP)); // else save them for next time

    return (RiTextIF[]) rlines.toArray(RiText.EMPTY_ARRAY);
  }

  // add to word stack in reverse order
  private void addToStack(String[] tmp)
  {
    for (int i = tmp.length - 1; i >= 0; i--)
      words.push(tmp[i]);
  }
  
  public String remainingText()
  {
    return stackToString(words);
  }

  private String stackToString(Stack wrds)
  {
    if (wrds == null) return "";
    StringBuilder sb = new StringBuilder();
    while (!wrds.isEmpty())
    {
      if (sb.length() > 0)
        sb.append(SP);
      sb.append(wrds.pop());
    }
    return sb.toString();
  }
  
  RiTextIF newRiTextLine(StringBuilder sb, PFont pf, float xPos, float nextY)
  {
    String s = sb.toString();
    
    //System.out.println("PageLayout.newRiTextLine: '"+sb+"'");
    
    // strip trailing spaces
    while (s != null && s.length() > 0 && s.endsWith(SP))
      s = s.substring(0, s.length() - 1);
    
    RiTextIF rt = new RiText(this._pApplet, s, xPos, nextY, pf);
    if (textColor != null) rt.fill(textColor);
    
    sb.delete(0, sb.length()); // empty for reuse
    
    return rt;
  }

  public PageLayout copy()
  {
    if (_pApplet == null)
      throw new RuntimeException("Null pApplet!");
    PageLayout rpl = new PageLayout(_pApplet, textRectangle, pageWidth, pageHeight);
    rpl.pageNo = pageNo;
    rpl.paragraphIndent = paragraphIndent;
    rpl.paragraphLeading = paragraphLeading;
    rpl.pageHeight = pageHeight;
    rpl.indentFirstParagraph = indentFirstParagraph;
    rpl.pageNo =  pageNo;
    rpl.textColor = textColor;
    rpl.textRectangle = textRectangle;
    rpl.showPageNumbers = showPageNumbers;
    rpl.header = header != null ? header.copy() : null;
    rpl.footer = footer != null ? footer.copy() : null;
    if (lines != null)
    {
      rpl.lines = new RiTextIF[lines.length];
      for (int i = 0; i < lines.length; i++)
        rpl.lines[i] = lines[i].copy();
    }
    if (words != null)
    {
      for (Iterator it = this.words.iterator(); it.hasNext();)
        rpl.words.add(it.next());
    }
    return rpl;
  }

  public void dispose()
  {
    RiText.dispose(lines);
    RiText.dispose(header);
    RiText.dispose(footer);
  }
  
  public void drawGuides()
  {
    this.drawGuides(_pApplet.g);
  }

  public void drawGuides(PGraphics p)
  {
    p.line(textRectangle.x, 0, textRectangle.x, textRectangle.y + pageHeight);
    p.line(textRectangle.x + textRectangle.w, 0, textRectangle.x + textRectangle.w, textRectangle.y + pageHeight);
    p.line(0, textRectangle.y, textRectangle.x + pageWidth, textRectangle.y);
    p.line(0, textRectangle.y + textRectangle.h, textRectangle.x + pageWidth, textRectangle.y + textRectangle.h);
  }

  public int getTopMargin()
  {
    return (int) textRectangle.y;
  }

  public int getLeftMargin()
  {
    return (int) textRectangle.x;
  }

  public int getRightMargin()
  {
    return (int) (pageWidth - (textRectangle.x + textRectangle.w));
  }

  public int getBottomMargin()
  {
    return (int) (pageHeight - (textRectangle.y + textRectangle.h));
  }

  public void setPageWidth(int pageWidth)
  {
    this.pageWidth = pageWidth;
  }

  public void setPageHeight(int pageHeight)
  {
    this.pageHeight = pageHeight;
  }

  public void header(String headerText)
  {
    this.header = new RiText(_pApplet, headerText, textRectangle.x + textRectangle.w / 2f, textRectangle.y - 25);
    header.align(CENTER);
  }

  public void footer(String footerText)
  {
    this.footer = new RiText(_pApplet, footerText, 
        textRectangle.x + textRectangle.w / 2f, textRectangle.y + textRectangle.h + RiTa.PAGE_NO_OFFSET);
    footer.align(CENTER);
  }

  public RiTextIF[] getLines()
  {
    if (lines == null)
      throw new RiTaException("No text has been assigned to this layout(" + hashCode() + "), make sure to call render() or setLines() first!");
    return lines;
  }

  public String toString()
  {
    if (lines == null || lines.length<1)
      return "EMPTY!";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < lines.length; i++)
      sb.append(lines[i].text()+" ");
    return sb.toString().trim();
  }
}