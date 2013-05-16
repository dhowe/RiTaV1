package rita.support;

import rita.support.Constants;
import rita.support.GoogleDirect;
 
public interface RiGooglerIF extends Constants
{
  public String fetch(String query);
  
  public int   getCount(String query);
  public float getBigram(String word1, String word2);
  public float getWeightedBigram(String[] sentence); // remove?
  
  public String[] getCompletions(String words);
  public String[] getCompletions(String pre, String post);
  public String[] getCompletions(String words, int maxWords);

  public String[] getResultLinks(String query);
  public String[] getResultText(String query);
  public String[] getImageURLs(String query);
  public String[] getImageURLs(String query, boolean thumbnails);

  // Setters & Getters
  
  public RiGooglerIF searchType(String type);
  public String searchType();  // books, images, search...
  
  public RiGooglerIF userAgent(String userAgent);
  public String userAgent();

  public RiGooglerIF cookie(String googleCookie);
  public String cookie();
  
  public RiGooglerIF cookiePath(String path);
  public String cookiePath();
  
  public RiGooglerIF startIndex(int startIndex);
  public int startIndex();
}
