package rita;

import java.util.Map;

public interface RiMarkovIF
{
  public int getN(); 
  public int size();  
  
  public String[] generateTokens(int num);
  
  public String[] generateUntil(String until);
  public String[] generateUntil(String until, int minLength);
  public String[] generateUntil(String until, int minLength, int maxLength);
  
  public String[] getCompletions(String[] pre);
  public String[] getCompletions(String[] pre, String[] post);
  
  public Map getProbabilities(String[] path); 
  public float getProbability(String[] path); 
  
  public RiMarkovIF loadTokens(String[] tokens); 
  public RiMarkovIF loadTokens(String[] tokens, int multiplier);
  
  
  public RiMarkovIF loadText(String text);
  public RiMarkovIF loadText(String text, int multiplier);
  public RiMarkovIF loadText(String text, String regex);
  public RiMarkovIF loadText(String text, int multiplier, String regex);
  
  
  public boolean recognizeSentences(); // no setter, only in constructor
  public boolean allowDuplicates();  
  
  public boolean useSmoothing();
  public RiMarkovIF useSmoothing(boolean b);
  
  public RiMarkovIF print();
}
