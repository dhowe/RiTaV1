
import rita.*;

// Layout RiTexts in a line, as a row or words, or as single letters

String txt = "The dog ate the cat.";
RiText line1, line2[], line3[];

void setup()
{

  size(400, 400);

  RiText.defaults.fontSize = 32;
  RiText.defaults.showBounds = true;

  line1 = new RiText(this, txt, 64, 50); // lines
  line2 = RiText.createWords(this, txt, 64, 80); // words
  line3 = RiText.createLetters(this, txt, 64, 130); // letters





  RiText.defaults.showBounds = false;

  RiText.defaultFont("Times", 28);

  RiText rt = new RiText(this, txt, 64, 300);

  RiText[] letters = rt.splitLetters();
  for (int i = 0; i < letters.length; i++) {
    letters[i].y = rt.y + 30;
    float r = i*(256/letters.length);
    r = i % 2 == 1 ? r : 255-r;
    letters[i].fill(r, 255-r, 255);
  }

  RiText[] words = rt.splitWords();
  for (int i = 0; i < words.length; i++) {
    words[i].y = rt.y + 60;
    float r = i*(256/words.length);
    words[i].fill(r, 100, 255-r);
  }
  System.out.println(rt.textAscent());
  System.out.println(rt.textDescent());



  background(255);
  RiText.drawAll();
}


