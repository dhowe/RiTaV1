  package rita;

import java.util.Map;

/**
 * @exclude
 * @author dhowe
 *
 */
public interface RiLexiconIF
{
  public int size();
  public boolean containsWord(String s);
    
  public RiLexicon lexicalData(Map m);
  public Map lexicalData();
  
  public String randomWord();
  public String randomWord(String pos);
  public String randomWord(int syllableCount);
  public String randomWord(String pos, int syllableCount);
  
  public String[] rhymes(String s);
  
  public String[] words();
  public String[] words(String regex);
  public String[] words(String regex, boolean randomize);
  
  public boolean isAdverb(String s);
  public boolean isNoun(String s);
  public boolean isVerb(String s);
  public boolean isAdjective(String s);
  public boolean isAlliteration(String s, String t);
  public boolean isRhyme(String s, String t);
  
  public String[] alliterations(String s);
  public String[] alliterations(String s, int minLength); 
  
  public String[] similarByLetter(String s);
  public String[] similarByLetter(String s, int minEditDist);
  public String[] similarByLetter(String s, int minEditDist, boolean preserveLength);
  
  public String[] similarBySound(String s);
  public String[] similarBySound(String s, int minEditDist);
  
  public String[] similarBySoundAndLetter(String s);
  //public String[] similarBySoundAndLetter(String s, int minEditDist);
  
  public String[] substrings(String s);
  public String[] substrings(String s, int minLength);
  public String[] superstrings(String s);
}
