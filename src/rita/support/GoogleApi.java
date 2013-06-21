package rita.support;

import java.util.*;


import rita.RiTa;
import rita.RiTaException;
import rita.json.*;

public class GoogleApi extends GoogleDirect
{
  protected String apiKey, cxKey;

  public GoogleApi(String apiKey, String cxKey)
  {
    this.apiKey = apiKey;
    this.cxKey = cxKey;
  }
  
  // START New methods ------------------------------------

  
  public String[] getImageURLs(String query, boolean thumbnails)
  {
    //System.out.println("GoogleApi.getImageURLs("+query+","+thumbnails+")");
    
    SearchType oldType = this.type; // save current search type
    
    this.type = SearchType.IMAGE;
    String[] res = null;
    try
    {
      JSONArray items = fetchJSONItems(query);
      res = new String[items.length()];
      for (int i = 0; i < res.length; i++)
      {
        if (thumbnails) {
          JSONObject image = ((JSONObject) items.get(i)).getJSONObject("image");
          res[i] = image.getString("thumbnailLink");
        }
        else
          res[i] = ((JSONObject) items.get(i)).getString("link");
      }
    }
    catch (JSONException e)
    {
      throw new RiTaException(e);
    }
    finally {
      
      this.type = oldType; // restore search type
    }
    
    return res;
  }
  
  public String[] getResultLinks(String query)
  {
    String[] res = null;
    try
    {
      JSONArray items = fetchJSONItems(query);
      res = new String[items.length()];
      for (int i = 0; i < res.length; i++)
      {
        res[i] = ((JSONObject) items.get(i)).getString("link");
      }
    }
    catch (JSONException e)
    {
      throw new RiTaException(e);
    }
    return res;
  }
  
  public String[] getResultText(String query)
  {
    String[] res = null;
    try
    {
      JSONArray items = fetchJSONItems(query);
      res = new String[items.length()];
      for (int i = 0; i < res.length; i++)
      {
        res[i] = ((JSONObject) items.get(i)).getString("snippet");
      }
    }
    catch (JSONException e)
    {
      throw new RiTaException(e);
    }
    return res;
  }
  
  // END New methods ------------------------------------
 
  /**
   * Uses Google's "fill in the blank" completion feature to return the subsequent words matched in a search...
   * @param words the term to search
   * @param maxWords the max number of words to return (default=infinity)
   */
  public String[] getCompletions(String words, int maxWords)
  {
    //System.out.println("GoogleApi.getCompletions("+words+")");
    
    words = quotifyQuery(words);
    String parseRE = buildRegex(words);
    
    String[] res = null;
    try
    {
      JSONArray items = fetchJSONItems(words);
      res = new String[items.length()];
      for (int i = 0; i < res.length; i++)
      {
        JSONObject jso = (JSONObject) items.get(i);
        //System.out.println(jso.names());
        res[i] = jso.getString("snippet");
      }
    }
    catch (JSONException e)
    {
      throw new RiTaException(e);
    }

    List<String> result = new ArrayList<String>();
    for (int i = 0; i < res.length; i++)
    {
      String s = res[i];

      if ((s = parseRef(s, parseRE)) != null) {
        String toAdd = replaceEntities(s).trim();
        if (toAdd.length()>1 && !result.contains(toAdd)) {
          result.add(toAdd);
        }
      }
    }
    
    String[] arr = result.toArray(new String[result.size()]);
    joinSubArrays(arr, maxWords);
  
    Set<String> s = new HashSet<String>();
    for (int i = 0; i < arr.length; i++)
      s.add(arr[i]);
       
    return (String[]) s.toArray(new String[s.size()]);
  }
    
  /**
   * Returns the number of hits via Google for the search query. To obtain an
   * exact match, place your query in quotes, e.g. <pre>
   *   int k = gp.getCount("\"attained their momentum\"");
   * </pre>
   * @param query The string to be searched for.
   * @return The number of hits Google returned for the search query.
   */
  public int getCount(String query)
  {
    // TODO: use cache here
    
    String url = makeRESTQuery(query);
    String json = fetch(url);
    try
    {
      JSONObject jso = new JSONObject(json);
      JSONObject request = jso.getJSONObject("queries");
      JSONArray queries = request.getJSONArray("request");
      JSONObject meta = (JSONObject) queries.get(0);
      return meta.getInt("totalResults");
    }
    catch (JSONException e)
    {
      throw new RiTaException(e);
    }
  }
  
  public GoogleDirect searchType(String searchType)
  {
    if (searchType.equalsIgnoreCase(SearchType.BOOKS.name())) 
      throw new RiTaException("No book search provided with apiKey: "+searchType);
    return super.searchType(searchType);
  }

  public String apiKey()
  {
    return apiKey;
  }

  // HELPERS ----------------------------------------------------------
  
  protected JSONArray fetchJSONItems(String query) throws JSONException
  {
    String url = makeRESTQuery(query);
    String json = fetch(url);
    JSONObject jso = new JSONObject(json);
    JSONArray items = jso.getJSONArray("items");
    //System.out.println(((JSONObject) items.get(0)).names());
    return items;
  }
    
  protected String makeRESTQuery(String query)
  {
    String url = "https://www.googleapis.com/customsearch/v1?";
    Map<String,String> params = new HashMap<String,String>();
    params.put("start", Integer.toString(startIndex));    
    params.put("googleHost", googleHost);
    params.put("key", apiKey);
    params.put("cx", cxKey);
    params.put("alt", "json");
    params.put("q", query.replaceAll("\\s+", "+"));
    if (type != SearchType.SEARCH)
      params.put("searchType", type.name().toLowerCase());
    
    StringBuilder sb = new StringBuilder();
    for (Iterator it = params.keySet().iterator(); it.hasNext();)
    {
      String key = (String) it.next();
      String val = params.get(key);
      sb.append(key+EQ+val);
      if (it.hasNext())
        sb.append(AMP); 
    }
    return url+sb.toString();
  }
    
  public static void main(String[] args)
  {
    GoogleApi rg = new GoogleApi("AIzaSyDk9EBvYsQghuB7BBOGvGXL0pk4_SB0KTw", "012236010653754878383:ttpt0505nn8");
    System.out.println(RiTa.asList(rg.getImageURLs("ambidextrous scallywags", false)));
//    System.out.println(rg.getCount("\"ambidextrous scallywags\""));
  }

}
