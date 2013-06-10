import rita.*;
// Layout RiTexts in a line, as a row or words, or as single letters

String txt = "The dog ate the cat.";
RiText line1, line2[], line3[];

void setup() {
  
  size(400, 200);

  RiText.defaultFont("Georgia", 32);
  RiText.defaults.showBounds = true;

  line1 = new RiText(this, txt, 64, 100); // lines

  line2 = line1.splitWords(); // words

  line1.y = 150;
  line3 = line1.splitLetters(); // letters

  setColors();
}

void draw() {

  background(255);

  line1.y = 50 + frameCount % 2; // wiggle
  line2[line2.length - 1].y = 100 + frameCount % 4;
  line3[line3.length - 2].y = 150 + frameCount % 7;

  RiText.drawAll();
}

void setColors() {

  for (int i = 0; i < line2.length; i++)
    line2[i].fill(50 + random(155), 50 + random(155), 0);

  for (int i = 0; i < line3.length; i++)
    line3[i].fill(50 + random(155), 50 + random(155), 0);
}
