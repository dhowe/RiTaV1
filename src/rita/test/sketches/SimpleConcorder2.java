package rita.test.sketches;

import java.util.Iterator;

import processing.core.PApplet;
import processing.data.IntList;
import processing.data.StringDict;
import rita.RiTa;

// http://social.msdn.microsoft.com/Forums/en-US/05512508-3049-4e2d-b438-37e717604dcb/algorithm-c?forum=Vsexpressvcs

public class SimpleConcorder2 extends PApplet
{
  public void setup()
  {       
    StringDict concordance = new StringDict();
    
    String text = RiTa.loadString("kafka.txt");
    String[] sentences = RiTa.splitSentences(text);
    
    for (int i = 0; i < sentences.length; i++)
    {
      String[] words = RiTa.tokenize(sentences[i]);
      for (int j = 0; j < words.length; j++)
      {
        String word = words[j];
        if  (word.length() > 3) {
          String sentList = concordance.get(word);
          if (sentList == null) 
            sentList = "";
          sentList += i+",";
          concordance.set(word, sentList);
        }
      }
    }

    concordance.sortKeys();
    String[] keys = concordance.keyArray();
    for (int i = 0; i < keys.length; i++)
      println(keys[i]+": "+concordance.get(keys[i]));
  }
  
}
