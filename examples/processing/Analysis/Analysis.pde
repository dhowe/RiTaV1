import rita.*;

RiLexicon lexicon;
String pos="", word="", sy, ph, ss;
java.util.Map<String, String> map = new HashMap<String, String>();
Bubble[] bubbles = new Bubble[10];
StringDict tagsDict;
color[] colors;

void setup()
{
  size(600, 300);

  smooth();
  noStroke();
  textFont(createFont("Georgia", 36));

  colors = colorGradient();

  // load Lexicon
  lexicon = new RiLexicon();

  // initialize bubbles
  for (int i = 0; i < bubbles.length; i++) {
    bubbles[i] = new Bubble("", 0);
  }

  // start a timer
  RiTa.timer(this, 4.0);
}


void draw()
{
  background(255);
  noStroke();

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
  fill(tagColor(pos));
  textSize(18);
  textLeading(17);
  text(pos.toUpperCase(), 20, 30);

  if (ph != "") {
    for (int i = 0; i < bubbles.length; i++) {
      bubbles[i].draw();
      bubbles[i].fall(i);
    }
  }
}


void onRiTaEvent(RiTaEvent re) { // called every 4 sec by timer

  // if word is > than 10 letters, pick another
  do {
    word = lexicon.randomWord();
  }
  while (word.length() > 10);


  // get various features
  sy = RiTa.getSyllables(word);
  ph = RiTa.getPhonemes(word);
  ss = RiTa.getStresses(word);

  // get the wordNet-style pos-tags
  String[] tags = RiTa.getPosTags(word, true);
  String tagName = tags != null ? tagName(tags[0]) : null;
  pos = tagName != null ? tagName : " ";

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
