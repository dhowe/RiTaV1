package rita.test;

import static org.junit.Assert.fail;
import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;
import static rita.support.QUnitStubs.println;

import org.junit.Test;

import rita.support.RiGoogler;
import rita.support.RiGooglerIF;

public class RiGooglerApiTest
{
  private static final String COOKIE_PATH = RiGoogler.DEFAULT_COOKIE_PATH;

  private static RiGooglerIF google;
  
  static {
    //google = new GoogleApi(GoogleLogin.API_KEY, GoogleLogin.CX_KEY);
  }
  
  @Test
  public void testFetch()
  {
    String html = google.fetch("https://www.google.com/search?q=ambidextrous+scallywags");
    ok(html);
  }

  @Test
  public void testGetCount()
  {
    String query = "big dog";
    int count = google.getCount(query);
    ok(count>1000);
  }

  @Test
  public void testGetBigram()
  {
    float count = google.getBigram("big","dog");
    ok(count>0);
  }

  @Test
  public void testGetWeightedBigram()
  {

    float count = google.getWeightedBigram(new String[]{"The", "big","dog"});
    println(count);
    ok(count>0);
  }

  @Test
  public void testGetCompletionsString()
  {
    String query = "bieber is a";
    String[] completions = google.getCompletions(query);
    ok(completions.length>1);
    println(completions);
  }

  @Test
  public void testGetCompletionsStringString()
  {
    String[] completions = google.getCompletions("the girl","the dog");
    println(completions); 
    ok(completions.length>1);
  }

  @Test
  public void testGetCompletionsStringInt()
  {
    String query = "bieber is a";
    String[] completions;
    
    completions = google.getCompletions(query,1);
    println(completions);
    ok(completions.length>1);
    for (int i = 0; i < completions.length; i++)
    {
      //System.out.println(completions[i]);
      equal(completions[i].matches("\\w"), false, completions[i]);
      equal(completions[i].contains(" "),  false, completions[i]);
    }
    
    completions = google.getCompletions(query,2);
    println(completions);
    ok(completions.length>1);
    for (int i = 0; i < completions.length; i++)
    {
      equal(completions[i].matches("\\w"), false, completions[i]);
      ok(countChar(completions[i], ' ')<2, completions[i]);
    }    
  }
  
  public static int countChar(String input, char toMatch)
  {
    int count = 0;
    for (int i = 0; i < input.length(); i++)
      if (input.charAt(i) == toMatch)
        count++;
    return count;
  }

  @Test
  public void testGetResultLinks()
  {

    String query = "\"lasted a good\"";
    String[] links = google.getResultLinks(query);
    println(links);
    ok(links.length>9);
  }

  @Test
  public void testGetResultText()
  {
    String query = "\"a good man\"";
    String[] links = google.getResultText(query);
    println(links);
    ok(links.length>9);  
  }

  @Test
  public void testGetImageURLsString()
  {
    String query = "\"that must have lasted a good\"";
    String[] links = google.getImageURLs(query);
    println(links);
    ok(links.length>9);
  }

  @Test
  public void testGetImageURLsStringBoolean()
  {
    String query = "\"that must have lasted a good\"";
    String[] links = google.getImageURLs(query, true);
    println(links,1);

    ok(links.length>9);
  }
  
  // ------------------------Setters/Getters-------------------------

  @Test
  public void testSearchTypeString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testSearchType()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testUserAgentString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testUserAgent()
  {
    fail("Not yet implemented");
  }
  
  @Test
  public void testCookiePathString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testCookiePath()
  {
    equal(google.cookiePath(),COOKIE_PATH);      
  }
  
  @Test
  public void testCookieString()
  {
    String cookie = google.cookie();
    google.cookie("test");
    equal(google.cookie(),"test");
    google.cookie(cookie);
  }

  @Test
  public void testCookie()
  {
    String cookie = google.cookie();
    google.cookie("test");
    equal(google.cookie(),"test");
    google.cookie(cookie);  
  }
    
}
