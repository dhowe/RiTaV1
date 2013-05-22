import rita.*;
import rita.wordnet.*;


RiText[] rts; 
RiWordnet wordnet; 
int charsPerLine = 60;

String test = "Last Wednesday we decided to visit the zoo. We arrived the next morning after we breakfasted, cashed in our passes and entered. We walked toward the first exhibits. I looked up at a giraffe as it stared back at me. I stepped nervously to the next area. One of the lions gazed at me as he lazed in the shade while the others napped. One of my friends first knocked then banged on the tempered glass in front of the monkeyâ€™s cage. They howled and screamed at us as we hurried to another exhibit where we stopped and gawked at plumed birds. After we rested, we headed for the petting zoo where we petted wooly sheep who only glanced at us but the goats butted each other and nipped our clothes when we ventured too near their closed pen. Later, our tired group nudged their way through the crowded paths and exited the turnstiled gate. Our car bumped, jerked and swayed as we dozed during the relaxed ride home.";

void setup()
{
  size(400, 400);
  RiText.setDefaultAlignment(LEFT);    
  wordnet = new RiWordnet(this);
  rts = RiText.createLines(this, test, 30, 50, charsPerLine);    
}

void draw()
{
  background(180);

  // substitute every 50 frames 
  if (frameCount % 50 == 1)   
    doSubstitution();
}

/*  replace a random word in the paragraph with one
 from wordnet with the same (basic) part-of-speech */
void doSubstitution()
{
  String[] words = test.split(" ");

  // loop from a random spot
  int count =  (int)random(0, words.length);
  for (int i = count; i < words.length; i++) 
  {
    // only words of 3 or more chars
    if (words[i].length()<3) continue;
      
    // get the pos
    String pos = wordnet.getBestPos(words[i].toLowerCase());        
    if (pos != null) 
    {
      // get the synset
      String[] syns = wordnet.getSynset(words[i], pos);
      if (syns == null) continue;

      // pick a random synonym
      String newStr = syns[(int)random(0, syns.length)];

      if (Character.isUpperCase(words[i].charAt(0)))              
        newStr = RiTa.upperCaseFirst(newStr); // keep capitals

      // and make a substitution
      test = RiText.regexReplace("\\b"+words[i]+"\\b", test, newStr);

      RiText.deleteAll();   // clean up the last batch

      // create a RiText[] from 'test' starting at (30,50) & going down
      rts = RiText.createLines(this, test, 30, 50, charsPerLine);      
      break;
    }
    if (count == words.length) count = 0;
  }       
}

