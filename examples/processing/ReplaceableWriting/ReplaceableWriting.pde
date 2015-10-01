import rita.*;

/*
 * Note: Java-only, requires a local WordNet installation
 * Grab one here: http://rednoise.org/rita-archive/WordNet-3.1.zip
 * install it, and update the path below 
 */

RiWordNet wordnet; 

String text = "Last Wednesday we decided to visit the zoo. We arrived the next morning after we breakfasted, cashed in our passes and entered. We walked toward the first exhibits. I looked up at a giraffe as it stared back at me. I stepped nervously to the next area. One of the lions gazed at me as he lazed in the shade while the others napped. One of my friends first knocked then banged on the tempered glass in front of the monkey's cage. They howled and screamed at us as we hurried to another exhibit where we stopped and gawked at plumed birds. After we rested, we headed for the petting zoo where we petted wooly sheep who only glanced at us but the goats butted each other and nipped our clothes when we ventured too near their closed pen. Later, our tired group nudged their way through the crowded paths and exited the turnstiled gate. Our car bumped, jerked and swayed as we dozed during the relaxed ride home.";

void setup()
{
  size(600, 400);   

  // load wordnet, ignoring compound & uppercase words
  wordnet = new RiWordNet("/WordNet-3.1", true, true);   

  RiTa.timer(this, 2f);
}

void draw() {
  fill(0);
  textSize(16);
  textLeading(20);
  background(250);
  text(text, 50, 30, 500, 10000);
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
    if (words[i].length() < 3) continue;

    String pos = wordnet.getBestPos(words[i].toLowerCase());  

    if (pos != null) 
    {
      // get the synset
      String[] syns = wordnet.getSynonyms(words[i], pos);

      // only words with >1 synonyms
      if (syns.length<2) continue;

      // pick a random synonym
      int randIdx = (int)random(0, syns.length);
      String newStr = syns[randIdx];

      if (Character.isUpperCase(words[i].charAt(0)))              
        newStr = RiTa.upperCaseFirst(newStr); // keep capitals

      //println("replace: "+words[i]+" -> "+newStr);

      // and make a substitution
      text = text.replaceAll("\\b"+words[i]+"\\b", newStr);

      break;
    }
  }
}     