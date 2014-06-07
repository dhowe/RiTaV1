package rita.render.test.misc;

import processing.core.PApplet;
import rita.*;

public class GrammarExecRhyme extends PApplet
{
  String grammar = "{" + "\"<start>\": \"The `store(<obj>)` ate the `rhyme()`\","
      + "\"<obj>\": \"cap | lap | tap | trap | map | strap | wrap\"" + "}";

  RiGrammar rg;
  RiText rt;
  String stored;

  public void setup()
  {
    size(400, 200);

    RiText.defaultFontSize(24); 
    RiText.defaults.alignment = CENTER;

    rt = new RiText(this, "click", width / 2, 75);
    rg = new RiGrammar(this);
    rg.load(grammar);
  }

  public void draw()
  {
    background(230, 240, 255);
    rt.draw();
  }

  public void mouseClicked()
  {
    String result = rg.expand();
    rt.text(result);
  }

  String rhyme()
  {
    String choice = stored;
    while (choice.equals(stored))
      choice = rg.expandFrom("<obj>");
    return choice;
  }

  String store(String s)
  {
    stored = s;
    return stored;
  }
}
