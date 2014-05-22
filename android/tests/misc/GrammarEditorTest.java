package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class GrammarEditorTest extends PApplet {

  RiText[] rts;
  RiGrammar grammar;

  public void setup() 
  {
    size(600, 200);
    
    RiText.defaults.alignment = CENTER;
    RiText.defaultFont("Times", 24);
    
    rts = new RiText[3];
    rts[0] = new RiText(this, "click to", width / 2, 85);
    rts[1] = new RiText(this, "generate", width / 2, 110);
    rts[2] = new RiText(this, "a haiku",  width / 2, 135);
    
    grammar = new RiGrammar();
    grammar.loadFrom("haikuGrammar2.json", this);
    grammar.openEditor(width, 800);    
  }
  
  public void mouseClicked() {
    
    String result = grammar.expand();
    
    //System.out.println(result);
    
    String[] lines = result.split("%");
    
    for (int i = 0; i < rts.length; i++) {
     
      rts[i].textTo(lines[i].trim(), 1.0f);
    }
  }

  public void draw() {
    
    background(255);
    RiText.drawAll();
  }
  
  public void onRiTaEvent(RiTaEvent re) {
    //System.out.println("GrammarEditorTest.onRiTaEvent("+re+")");
  }
    
}
