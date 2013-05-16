package rita;

import rita.support.*;

/**
 * @exclude
 * DCH: NOT READY YET
 */
public class RiGoogler implements RiGooglerIF
{
  public static final String DEFAULT_COOKIE_PATH 
    = System.getProperty("user.home") + "/Library/Cookies/Cookies.plist";

  static  { String s = RiTa.VERSION; }
  
  RiGooglerIF delegate;

  public RiGoogler()
  {
    this.delegate = new GoogleDirect(); 
  }
  
  public RiGoogler(String apiKey, String cxKey)
  {
    this.delegate = new GoogleApi(apiKey, cxKey);
  }
  
  // delegates
  
  public RiGooglerIF startIndex(int startIndex)
  {
    return delegate.startIndex(startIndex);
  }

  public int startIndex()
  {
    return delegate.startIndex();
  }
  
  public String fetch(String query)
  {
    return delegate.fetch(query);
  }
  
  public int getCount(String query)
  {
    return delegate.getCount(query);
  }

  public float getBigram(String word1, String word2)
  {
    return delegate.getBigram(word1, word2);
  }

  public float getWeightedBigram(String[] sentence)
  {
    return delegate.getWeightedBigram(sentence);
  }

  public String[] getCompletions(String words)
  {
    return delegate.getCompletions(words);
  }

  public String[] getCompletions(String pre, String post)
  {
    return delegate.getCompletions(pre, post);
  }

  public String[] getCompletions(String words, int maxWords)
  {
    return delegate.getCompletions(words, maxWords);
  }

  public RiGooglerIF searchType(String type)
  {
    return delegate.searchType(type);
  }

  public String searchType()
  {
    return delegate.searchType();
  }

  public RiGooglerIF userAgent(String userAgent)
  {
    return delegate.userAgent(userAgent);
  }

  public String userAgent()
  {
    return delegate.userAgent();
  }

  public RiGooglerIF cookie(String googleCookie)
  {
    return delegate.cookie(googleCookie);
  }

  public String cookie()
  {
    return delegate.cookie();
  }

  public RiGooglerIF cookiePath(String path)
  {
    return delegate.cookiePath(path);
  }

  public String cookiePath()
  {
    return delegate.cookiePath();
  }

  public String[] getImageURLs(String query)
  {
    return delegate.getImageURLs(query);
  }
  
  public String[] getImageURLs(String query, boolean thumbnails)
  {
    return delegate.getImageURLs(query, thumbnails);
  }

  public String[] getResultLinks(String query)
  {
    return delegate.getResultLinks(query);
  }

  public String[] getResultText(String query)
  {
    return delegate.getResultText(query);
  }
  
}
