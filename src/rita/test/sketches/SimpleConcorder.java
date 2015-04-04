package rita.test.sketches;

import java.util.*;

import processing.core.PApplet;
import processing.data.IntList;
import rita.RiTa;

// http://social.msdn.microsoft.com/Forums/en-US/05512508-3049-4e2d-b438-37e717604dcb/algorithm-c?forum=Vsexpressvcs

public class SimpleConcorder extends PApplet
{
  public void setup()
  {       
    Map concordance = new TreeMap();
    
    String text = RiTa.loadString("kafka.txt");
    String[] sentences = RiTa.splitSentences(text);
    
    for (int i = 0; i < sentences.length; i++)
    {
      String[] words = RiTa.tokenize(sentences[i]);
      for (int j = 0; j < words.length; j++)
      {
        String word = words[j];
        if (!RiTa.isStopWord(word)) { 
        
          IntList il = (IntList) concordance.get(word);
          if (il == null) il = new IntList();
          il.append(i);
          
          concordance.put(word, il); 
        }
      }
    }

    for (Iterator it = concordance.keySet().iterator(); it.hasNext();)
    {
      String word = (String) it.next();
      int[] idxs = ((IntList) concordance.get(word)).array();
      print(word+" { ");
      for (int i = 0; i < idxs.length; i++)
        print(idxs[i]+", ");
      println(" }");
    }
  }
  
}
