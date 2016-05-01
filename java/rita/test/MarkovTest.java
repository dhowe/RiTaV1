package rita.test;

import rita.RiMarkov;
import rita.RiTa;

public class MarkovTest {
  
  static String tag = "Bt ";

  public static void main(String[] args) {
    RiMarkov markov = new RiMarkov(2);
    markov.loadText("My name is James and I am a software developer");
    String[] sentences = markov.generateSentences(1);
    RiTa.out(sentences);
  }
  public static void mainx(String[] args) {
    
    RiMarkov rm = new RiMarkov(3, true, false);
    rm.loadText(prepareText("/Users/dhowe/Desktop/corpus.txt"));
    String[] sentences = rm.generateSentences(10);
    for (int i = 0; i < sentences.length; i++) {
      sentences[i] = changeCase(sentences[i], false);
      System.out.println(sentences[i]);
    }
  }
  
  static String prepareText(String fileName) {
    String text = RiTa.loadString(fileName);
    text = text.replaceAll("<[^>]*>", tag);
    text = text.replaceAll("[\\r\\n ]+", " ");
    text = changeCase(text, true);
    return text;
  }

  private static String changeCase(String text, boolean lower) {
    int start = lower ? 97 : 65; 
    for (int i = 0; i < 26; i++) {
      String c = Character.toString((char)(start + i));
      text = text.replaceAll(tag + c, tag + (lower ?
	  c.toLowerCase() : c.toUpperCase()));
    }
    return text;
  }
}
