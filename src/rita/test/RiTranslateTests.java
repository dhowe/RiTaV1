package rita.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.jsoup.*;
import org.jsoup.Connection.Method;

import rita.RiTaException;

public class RiTranslateTests
{
  // URL for audio (mp3): http://translate.google.com/translate_tts?tl=en&q=dog
  
  static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
  
  /*private static void loadCookiesFromDisk() // load firefox cookies via sqlite
  {
    try
    {
      // String dir = System.getProperty("user.dir");
      Class.forName("org.sqlite.JDBC");
      java.sql.Connection con = DriverManager.getConnection("jdbc:sqlite:"+COOKIE_PATH);
      Statement statement = con.createStatement();
      ResultSet rs = statement.executeQuery("select value from cookies where host_key='.google.com' and name='PREF'");
      System.out.println("rs=" + rs);
      while (rs.next()) {
        System.out.println(rs.getString("value"));
      }
      String[] cols = {"id","baseDomain","appId","inBrowserElement","name","value","host","path","expiry","lastAccessed","creationTime","isSecure","isHttpOnly"};
      while (rs.next()) {
        for (int i = 0; i < cols.length; i++)
        {
          System.out.println(cols[i]+": "+rs.getString(cols[i]));
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }*/
  
  private static void fetchCookies(String url) {
    org.jsoup.Connection.Response res = null;
    try
    {
      // This will get you the response.
      res = Jsoup.connect(url).
          //data("loginField", "login@login.com", "passField", "pass1234").
          method(Method.GET).userAgent(DEFAULT_USER_AGENT).execute();

      // This will get you cookies
      Map<String, String> cookies = res.cookies();
      Map<String, String> headers = res.headers();
      //System.out.println(cookies);
      //System.out.println(headers);
      /*for (Iterator it = headers.keySet().iterator(); it.hasNext();)
      {
        String header = (String) it.next();
        System.out.println(header+": "+headers.get(header));
      }
     */
      //if (1==1) return;

      String url2 = "http://translate.google.com/#en/en/violence";

      // And this is the easiest way I've found to remain in session
      //Document doc = Jsoup.connect(url2).cookies(cookies).get();
      Connection conn = Jsoup.connect(url2).method(Method.GET)
          .referrer("https://www.google.com.hk/#q=translate");
      //conn.userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1");
      conn.userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");
      conn.ignoreHttpErrors(true);
      /*for (Iterator it = headers.keySet().iterator(); it.hasNext();)
      {
        String header = (String) it.next();
        conn.header(header, headers.get(header));
      }*/
      res = conn.userAgent(DEFAULT_USER_AGENT).execute();
      String body = res.body();
      System.err.println(body);
      body = body.replaceAll("rel=stylesheet href=\"/",
                             "rel=stylesheet href=\"http:\\/\\/translate.google.com/");
      
      FileWriter fw = new FileWriter("/Users/dhowe/Desktop/google.html");
      fw.write(body);
      fw.flush();
      fw.close();
    }
    catch (IOException e)
    {
      throw new RiTaException(e);
    }
  }
  
  public static void main(String[] args)
  {
  
  }
}
