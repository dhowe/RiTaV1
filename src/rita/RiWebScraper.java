package rita;

import java.io.IOException;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @exclude
 * NOT READY YET
 */
public class RiWebScraper
{ 
  static { RiTa.init(); }
  
  static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5";
  
  private static final String TIMEOUT = "timeout";
  private static final String REFERRER = "referrer";
  private static final String USER_AGENT = "userAgent";
  private static final String FOLLOW_REDIRECTS = "followRedirects";
  private static final String IGNORE_CONTENT_TYPE = "ignoreContentType";
  private static final String IGNORE_HTTP_ERRORS = "ignoreHttpErrors";
  
  public Connection conn;
  public Document doc;
  public Map<String, String> options, cookies, headers;

  public RiWebScraper()
  {
    options = new HashMap<String, String>();
    cookies = new HashMap<String, String>();
    headers = new HashMap<String, String>();
  }

  public RiWebScraper connect(String url)
  {
    conn = Jsoup.connect(url);
    return this;
  }

  public RiWebScraper userAgent(String userAgent)
  {
    options.put(USER_AGENT, userAgent);
    return this;
  }
  
  public RiWebScraper referrer(String referrer)
  {
    options.put(REFERRER, referrer);
    return this;
  }


  public RiWebScraper timeout(int millis)
  {
    options.put(TIMEOUT, "" + millis);
    return this;
  }

  public RiWebScraper followRedirects(boolean followRedirects)
  {
    options.put(FOLLOW_REDIRECTS, followRedirects + "");
    return this;
  }

  public RiWebScraper ignoreHttpErrors(boolean ignoreHttpErrors)
  {
    options.put(IGNORE_HTTP_ERRORS, ignoreHttpErrors + "");
    return this;
  }

  public RiWebScraper ignoreContentType(boolean ignoreContentType)
  {
    options.put(IGNORE_CONTENT_TYPE, ignoreContentType + "");
    return this;
  }

  public RiWebScraper data(String key, String value)
  {
    return this;
  }

  public RiWebScraper data(Map<String, String> data)
  {
    return this;
  }

  public RiWebScraper data(String... keyvals)
  {
    return this;
  }

  public RiWebScraper header(String name, String value)
  {
    headers.put(name, value);
    return this;
  }

  public RiWebScraper cookie(String name, String value)
  {
    cookies.put(name, value);
    return this;
  }

  public RiWebScraper cookies(Map<String, String> cookies)
  {
    cookies.putAll(cookies);
    return this;
  }

  private RiWebScraper prepareConnection()
  {
    if (conn == null)
      throw new RiTaException("Null Connection: Call connect(url) first!");
    
    setCookies().setHeaders().setOptions();
    
    return this;
  }

  private RiWebScraper setOptions()
  {
    if (options.containsKey(TIMEOUT)) {
      conn.timeout(Integer.parseInt(options.get(TIMEOUT)));
    }  
    if (options.containsKey(REFERRER)) {
      conn.referrer(options.get(REFERRER));
    }  
    if (options.containsKey(USER_AGENT)) {
      conn.userAgent(options.get(USER_AGENT));
    }  
    if (options.containsKey(FOLLOW_REDIRECTS)) {
      conn.followRedirects(Boolean.parseBoolean(options.get(FOLLOW_REDIRECTS)));
    }
    if (options.containsKey(IGNORE_HTTP_ERRORS)) {
      conn.ignoreHttpErrors(Boolean.parseBoolean(options.get(IGNORE_HTTP_ERRORS)));
    }
    if (options.containsKey(IGNORE_CONTENT_TYPE)) {
      conn.ignoreContentType(Boolean.parseBoolean(options.get(IGNORE_CONTENT_TYPE)));
    } 
    return this;
  }

  private RiWebScraper setHeaders()
  {
    for (Iterator it = headers.keySet().iterator(); it.hasNext();)
    {
      String key = (String) it.next();
      conn.header(key, headers.get(key));
    }
    return this;
  }

  private RiWebScraper setCookies()
  {
    for (Iterator it = cookies.keySet().iterator(); it.hasNext();)
    {
      String key = (String) it.next();
      conn.cookie(key, cookies.get(key));
    }
    return this;
  }

  public String get(/*String url*/)
  {
    prepareConnection();//.connect(url);

    try
    {
      doc = conn.get();
    }
    catch (IOException e)
    {
      System.out.println(conn.response());
      throw new RiTaException(e);
    }

    return doc.outerHtml();
  }
    
  public String post(/*String url*/)
  {
    prepareConnection();//.connect(url);

    try
    {
      doc = conn.post();
    }
    catch (IOException e)
    {
      throw new RiTaException(e);
    }

    return doc.outerHtml();
  }

  public static void main(String[] args)
  {
    /*fetchCookies("http://google.com");
    //loadCookiesFromDisk();
    if (1==1) return;*/



    RiWebScraper scraper = new RiWebScraper();
    scraper.header("User-Agent", DEFAULT_USER_AGENT).
      header("Accept-Language", "en-US,en.q=0.8").
      header("Accept-Charset", "ISO-8859-1,utf-8.q=0.7,*.q=0.3").
      header("Connection", "keep-alive").
      header("Accept","text/html,application/xhtml+xml,application/xml.q=0.9,*/*.q=0.8").
      //header("Cookie", cookie).
      header("DNT", "1");
    //scraper.cookie("PREF", value);
    System.out.println(scraper.connect("http://rednoise.org/wdmtest.html").get());
  }

}
