package rita.test;

import static org.junit.Assert.fail;
import static rita.support.QUnitStubs.*;

import java.util.Iterator;
import java.util.List;

import org.junit.*;

import rita.support.GoogleDirect;

public class RiGooglerTest
{
  private static final String COOKIE_PATH = "/Users/dhowe/Library/Cookies/Cookies.plist";
  private static GoogleDirect google;
  
  static {
     google = new GoogleDirect();
     google.cookiePath(null, COOKIE_PATH);
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
    String query = "that must have";

    String[] completions = google.getCompletions(query);
    ok(completions.length>9);
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
    String query = "The dog";
    String[] completions;
    
    completions = google.getCompletions(query,1);
    ok(completions.length>9);
    for (int i = 0; i < completions.length; i++)
    {
      //System.out.println(completions[i]);
      equal(completions[i].matches("\\w"), false, completions[i]);
      equal(completions[i].contains(" "),  false, completions[i]);
    }
    println(completions);

    
    completions = google.getCompletions(query,2);
    println(completions);
    ok(completions.length>9);
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
    println(links);
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
