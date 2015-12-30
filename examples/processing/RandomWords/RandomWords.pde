import rita.*;

RiLexicon lexicon;
String syns = "", word = "";

void setup() 
{
  size(300, 200);    
  fill(255);
  textFont(createFont("Georgia", 36));

  lexicon = new RiLexicon();
  RiTa.timer(this, 2.0);
}

void draw() 
{  
  background(40);

  textAlign(CENTER);
  textSize(36);
  text(word, width/2, height/2);
}

void onRiTaEvent(RiTaEvent re) { // called every 2 sec by timer   
    word = lexicon.randomWord();
}