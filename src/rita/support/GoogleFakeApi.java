package rita.support;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;


import rita.RiTa;
import rita.json.*;

/**
 * To use this class, you will need an apiKey (from Google developers site) and a cxKey 
 * for a custom search engine that you've created (http://www.google.com/cse/manage/create)
 * @author dhowe
 *
 */
public class GoogleFakeApi extends GoogleApi
{
  public GoogleFakeApi(String apiKey, String cxKey)
  {
    super(apiKey, cxKey);
  }
 
  @Override
  public String fetch(String queryURL)
  {
    System.out.println("fetch("+queryURL+")");
    
    return  (type == SearchType.IMAGE) ?
      RiTa.loadString("/Users/dhowe/Documents/eclipse-workspace/RiTaLibraryCompat/library-template/src/data/google-imgs.json")
      : RiTa.loadString("/Users/dhowe/Documents/eclipse-workspace/RiTaLibraryCompat/library-template/src/data/google.json");
  }

  public static void main(String[] args)
  {
    GoogleFakeApi rg = new GoogleFakeApi("AIzaSyDk9EBvYsQghuB7BBOGvGXL0pk4_SB0KTw", "012236010653754878383:ttpt0505nn8");
    //System.out.println(RiTa.asList(rg.getResultLinks("ambidextrous scallywags")));
    RiTa.out(rg.getImageURLs("ambidextrous scallywags", false));
  }

}
