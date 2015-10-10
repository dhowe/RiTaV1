import rita.*;

/*
 * Note: Java-only, requires a local WordNet installation
 * Grab one here: http://rednoise.org/rita-archive/WordNet-3.1.zip
 * install it, and update the path below 
 */
 
String syns="", word = "";
RiWordNet wordnet;

void setup() 
{
  size(300, 300);    

  fill(255);
  textFont(createFont("Georgia", 36));

  // load wordnet, ignoring compound & uppercase words
  wordnet = new RiWordNet("/WordNetX-3.1", true, true);
  
  RiTa.timer(this, 2.0);
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
  text(syns, 30, 66);
}

void onRiTaEvent(RiTaEvent re) { // called every 2 sec by timer   

  String[] tmp = {};    
  while (tmp.length < 3) {

    word = wordnet.getRandomWord("n");
    text(word, 280, 40);
    tmp = wordnet.getAllSynonyms(word, "n", 13);
  }    
  
  java.util.Arrays.sort(tmp); // alphabetize the list
  syns = RiTa.join(tmp, "\n");
}