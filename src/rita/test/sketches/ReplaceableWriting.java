package rita.test.sketches;

import processing.core.PApplet;
import rita.*;

public class ReplaceableWriting extends PApplet
{
  RiText[] rts; 
  RiWordNet wordnet; 

  String text = "Last Wednesday we decided to visit the zoo. We arrived the next morning after we breakfasted, cashed in our passes and entered. We walked toward the first exhibits. I looked up at a giraffe as it stared back at me. I stepped nervously to the next area. One of the lions gazed at me as he lazed in the shade while the others napped. One of my friends first knocked then banged on the tempered glass in front of the monkey's cage. They howled and screamed at us as we hurried to another exhibit where we stopped and gawked at plumed birds. After we rested, we headed for the petting zoo where we petted wooly sheep who only glanced at us but the goats butted each other and nipped our clothes when we ventured too near their closed pen. Later, our tired group nudged their way through the crowded paths and exited the turnstiled gate. Our car bumped, jerked and swayed as we dozed during the relaxed ride home.";

  public void setup()
  {
    size(600, 400);   
    RiText.defaults.fontSize = 18; 
    wordnet = new RiWordNet("/WordNet-3.1", true, true);
    
    onRiTaEvent(null);
    
    RiTa.timer(this, 2f); 
  }

  public void draw()
  {
    background(250);
    RiText.drawAll();
  }

  //  replace a random word in the paragraph with one
  //  from wordnet with the same (basic) part-of-speech 
  void onRiTaEvent(RiTaEvent e)
  {   
    String[] words = text.split(" ");

    // loop from a random spot
    int count = (int)random(0, words.length);
    
    for (int i = count; i < words.length; i++) 
    {
      // only words of 3 or more chars
      if (words[i].length()<3) continue;
       
      // call a function to find a replacement
      String newWord = replaceWord(words[i]);
      if (newWord != null) {
      
        System.out.println("Replace: "+words[i]+" -> "+newWord);
        
        // and do the substitution
        text = text.replaceAll("\\b"+words[i]+"\\b", newWord);
        
        RiText.dispose(rts);   // clean up the old text
  
        // create a new RiText[] from 'text' starting at (30,50)
        rts = RiText.createLines(this, text, 50, 50, 500); 
        break;
      }
    }       
  }

  String replaceWord(String word)
  {
    String newStr=null, pos=wordnet.getBestPos(word.toLowerCase());        
    if (pos != null) 
    {
      // get the synset
      String[] syns = wordnet.getSynonyms(word, pos);

      // only words with >1 synonyms
      if (syns.length<2) return pos;

      // pick a random synonym
      int randIdx = (int)random(0, syns.length);
      newStr = syns[randIdx];

      if (Character.isUpperCase(word.charAt(0)))              
        newStr = RiTa.upperCaseFirst(newStr); // keep capitals     
    }
    
    return newStr;
  }
}
