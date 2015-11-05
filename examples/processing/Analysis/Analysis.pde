import rita.*;
import java.util.Map;

RiLexicon lexicon;

Map<String, String> map = new HashMap<String, String>();
String pos = " ", word = "";
String sy="", ph="", ss="";

Bubble[] bubbles = new Bubble[10]; 

void setup()
{
  size(600, 300);
  smooth();
  fill(30);
  textFont(createFont("Georgia", 36));

  // load Lexicon
  lexicon = new RiLexicon();
  RiTa.timer(this, 4.0);
  //initialize bubbles
  for (int i=0; i<bubbles.length; i++) {
    bubbles[i] = new Bubble("", 0);
  }
  
}//End of setup()


void draw()
{
  background(255);
  fill(56,66,90);
  //The word
  textAlign(LEFT);
  textSize(36);
  text(word, 80, 100);

  //pos Tag
  textSize(18);
  textLeading(17);

  text(pos, 20, 30);
  

  if (ph!="") {
    for (int i=0; i<bubbles.length; i++) {
      bubbles[i].draw();
      if (bubbles[i].t>100+2*i)
        bubbles[i].fall();
    }
  }
}//End of void draw()


void onRiTaEvent(RiTaEvent re) { // called every 4 sec by timer

  //if the word is more than 10 alphabets, get another one
  do {
    word = lexicon.randomWord();
  } while (word.length()>10);

  //all the features of one word
  map = (new RiString(word)).features();
  for (Map.Entry<String, String> entry : map.entrySet())
    println(entry.getValue());

  //get all the features seperately
  String[] temp =RiTa.getPosTags(word);
  if (matchPennTags(temp[0])!=null)
    pos = matchPennTags(temp[0]);
  else pos =" ";
  sy=RiTa.getSyllables(word);
  ph=RiTa.getPhonemes(word);
  ss=RiTa.getStresses(word);
  String[] phs = ph.split("-");

  //restart the bubbles
  for (int i=0; i<bubbles.length; i++) {
    bubbles[i].restart();
  }
  //update the bubbles to the new word
  for (int i=0; i<phs.length; i++) {
    bubbles[i].update(phs[i], i*50+100);
  }
  
  addStress(ss, sy, bubbles);
  addSyllabus(sy, bubbles);
  
}