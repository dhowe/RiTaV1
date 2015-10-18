import rita.*;
import java.util.Map;

RiLexicon lexicon;
RiString rs;

Map<String, String> map = new HashMap<String, String>();
String features =" stresses: \n pos: \n tokens: \n text: \n phonemes: \n syllables: ";
String syns="", word = "";

void setup() 
{
  size(400, 300);    
  smooth();
  fill(255);
  textFont(createFont("Georgia", 36));

  // load Lexicon
  lexicon = new RiLexicon();
  RiTa.timer(this, 2.0);
}


void draw() 
{  
  background(40);

  textAlign(RIGHT);
  textSize(36);
  text(word, width-20, 40);
  
  textSize(14);
  textLeading(17);
  textAlign(LEFT);
  text(features, 20, 80);
  text(syns, 100, 80);
}


void onRiTaEvent(RiTaEvent re) { // called every 2 sec by timer   
  
  syns="";
  
  word = lexicon.randomWord();
  rs = new RiString(word);
  map=rs.features();

for (Map.Entry<String, String> entry : map.entrySet())
    syns+=entry.getValue() + "\n";
}