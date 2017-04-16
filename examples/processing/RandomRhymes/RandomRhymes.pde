import rita.*;

String syns="", word = "";
RiLexicon lexicon;

void setup()
{
  size(300, 300);
  fill(255);
  textFont(createFont("Georgia", 36));

  lexicon = new RiLexicon();
  RiTa.timer(this, 2); // every 2 sec
}

void draw()
{
  background(40);

  textAlign(RIGHT);
  textSize(36);
  text(word, 280, 40);

  textSize(14);
  textLeading(17);
  textAlign(LEFT);
  text(syns, 30, 73);
}

void onRiTaEvent(RiTaEvent re) { // called by timer

  String[] tmp = {};
  while (tmp.length < 3) {

    word = lexicon.randomWord();
    tmp = lexicon.rhymes(word);
  }

  text(word, 280, 40); // max of 13 words
  syns = RiTa.join(subset(tmp, 0, min(tmp.length, 13)), "\n");
}
