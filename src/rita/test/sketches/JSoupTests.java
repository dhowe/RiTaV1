package rita.test.sketches;

import java.io.IOException;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import processing.core.PApplet;

public class JSoupTests extends PApplet
{

  @Override
  public void setup()
  {
    String url = "https://twitter.com/search?q=%23heartbleed&src=typd";
    try
    {
      Document doc = Jsoup.connect(url).get();
      Elements tweets = doc.select("p.js-tweet-text.tweet-text");
      for (Element tweet : tweets) {
        System.out.println(tweet);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    System.exit(1);
  }

  @Override
  public void draw()
  {
  }
  
  static String getRedirectUrl(String url) {
    HttpURLConnection con=null;
    int responseCode=-1;
    try
    {
      con = (HttpURLConnection)(new URL( url ).openConnection());
      con.setInstanceFollowRedirects( false );
      con.connect();
      responseCode = con.getResponseCode();
      if (responseCode < 300 || responseCode > 399)
        return url;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return con.getHeaderField( "Location" );
  }
    
  public static void main(String[] args)
  {
    System.out.println(getRedirectUrl("http://twitpic.com/show/thumb/e0pf1d"));
  }
}
