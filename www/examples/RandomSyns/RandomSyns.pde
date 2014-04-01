import rita.*;
import java.util.*;

/*
 * Random Synonyms from WordNet
 * Note: Java-only, requires a local WordNet installation
 */

RiText rt, rts[];
RiWordNet wordnet;

void setup() 
{
  size(300, 300);    
  
  RiText.defaultFill(255);
  RiText.defaultFont("arial", 14);
  
  (rt = new RiText(this, "random", 280, 40)).align(RIGHT).fontSize(36);

  wordnet = new RiWordNet("/WordNet-3.1", false, false);
  RiTa.timer(this, 2.0); 
}

void draw() 
{  
  background(40);
  RiText.drawAll();
}

void onRiTaEvent(RiTaEvent re) { // called every 2 seconds by timer   
  
    String[] tmp = {};    
    while (tmp.length<1) {
      
      String word = wordnet.getRandomWord("n");
      rt.text(word);
      tmp = wordnet.getAllSynonyms(word, "n", 13);
    }    
    Arrays.sort(tmp); // alphabetize the list
    
    RiText.dispose(rts);
    rts = RiText.createLines(this, tmp, 30, 73);
}
