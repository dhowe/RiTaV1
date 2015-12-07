import rita.*;

RiLexicon lexicon;
int maxWordLength = 12;
String pos="", word="", sy, ph, ss;
Bubble[] bubbles = new Bubble[maxWordLength];
StringDict tagsDict;
color[] colors;

void setup()
{
  size(600, 300);

  noStroke();
  textFont(createFont("Georgia", 36));

  // load Lexicon
  lexicon = new RiLexicon();

  colors = colorGradient();

  // initialize bubbles
  for (int i = 0; i < bubbles.length; i++)
    bubbles[i] = new Bubble();

  // start a timer
  RiTa.timer(this, 4.0);
}


void draw()
{
  background(255);

  // float gap = width/((float)colors.length+1);
  // for (int i = 0; i < colors.length; i++) {
  //   fill(colors[i]);
  //   ellipse((colors.length-i) * gap, height-2*gap, gap, gap);
  // }

  // the word
  fill(56, 66, 90);
  textAlign(LEFT);
  textSize(36);
  text(word, 80, 100);

  // pos Tag
  fill(100);
  textSize(14);
  text(pos.toUpperCase(), 20, 30);

  for (int i = 0; i < bubbles.length; i++)
    bubbles[i].draw(i);
}


void onRiTaEvent(RiTaEvent re) { // called every 4 sec by timer

  // random word with <= 12 letters
  do {
    word = lexicon.randomWord();
  }
  while (word.length() > maxWordLength);

  // get various features
  sy = RiTa.getSyllables(word);
  ph = RiTa.getPhonemes(word);
  ss = RiTa.getStresses(word);

  // get (WordNet-style) pos-tags
  String[] tags = RiTa.getPosTags(word, true);
  pos = tagName(tags[0]);

  // restart the bubbles
  for (int i = 0; i < bubbles.length; i++) {
    bubbles[i].reset();
  }

  // update the bubbles for the new word
  String[] phs = ph.split("-");
  for (int i = 0; i < phs.length; i++) {
    bubbles[i].update(phs[i], i*50+100);
  }

  addStresses(ss, sy, bubbles);
  addSyllables(sy, bubbles);
}
