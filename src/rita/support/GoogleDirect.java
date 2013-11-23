package rita.support;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.ParserDelegator;

import rita.*;

// Safari(Mac)  Cookie File:  ~/Library/Cookies/Cookies.plist"
// Chrome(Mac)  Cookie DB:  ~/Library/Application\ Support/Google/Chrome/Default/Cookies (SqlLite)
// Firefox(Mac) Cookie DB:  ~/Library/Application\ Support/Firefox/Profiles/9te24c3b.default/cookies.sqlite (SqlLite)

public class GoogleDirect implements RiGooglerIF
{
  enum SearchType { SEARCH, BOOKS, IMAGE };
  
  protected SearchType type = SearchType.SEARCH;
  
  protected static boolean dbugFetch = false;
  protected static boolean cacheEnabled = false;
  protected static Pattern[] patResult;
  protected static Map cache;

  protected int numCalls, startIndex = 1;
  protected String cookie=DEFAULT_COOKIE, userAgent=DEFAULT_USER_AGENT;
  protected String cookiePath, googleHost = "google.com";
  
  public GoogleDirect()
  {
    if (patResult == null) {
      patResult = new Pattern[RESULTS_PATS.length];
      for (int i = 0; i < patResult.length; i++)
      {
        patResult[i] = Pattern.compile(RESULTS_PATS[i]);
      }
    }
  }
  
  // START methods ------------------------------------

  public String[] getImageURLs(String query, boolean thumbnails)
  {
    if (thumbnails) 
      throw new RiTaException("Method only supported with the Google API");
    
    SearchType oldType = this.type; // save current search type
    
    this.type = SearchType.IMAGE;
    
    final String linkPatt = "&imgurl=([^&]+)&";
    final List links = new ArrayList();
    try
    {
      customParse(query, new HTMLEditorKit.ParserCallback() // an inner class
      {
        boolean inDiv, isLink;
        
        public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
          //System.out.println("found: "+t);
          if (t == Tag.DIV) {
            String clss = (String) a.getAttribute(Attribute.CLASS);
            inDiv = clss != null && clss.equals("rg_di");
          }
          if (inDiv && t == Tag.A) {
            String address = (String) a.getAttribute(Attribute.HREF);
            String link = parseRef(address, linkPatt);
            links.add(link);
          }
        }
        public void handleEndTag(Tag t, int pos) {
          if (t == Tag.DIV) {
            inDiv = false;
          }
        }
      });
    }
    catch (Throwable e)
    {
      throw new RiTaException(e);
    }
    finally 
    {  
      this.type = oldType; // restore search type
    }
    
    return RiTa.strArr(links);
  }
  
  public String[] getResultLinks(String query)
  {
    final List links = new ArrayList();
    try
    {
      customParse(query, new HTMLEditorKit.ParserCallback() // an inner class
        {
          boolean isLink = false;
          public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
            //System.out.println("found: "+t);
            if (isLink && t == Tag.A) {
              String address = (String) a.getAttribute(Attribute.HREF);
              links.add(address);  
            }
            if (t == Tag.H3) {
              String clss = (String) a.getAttribute(Attribute.CLASS);
              isLink = clss.equals("r");
            }
          }
          public void handleEndTag(Tag t, int pos) {
            if (t == Tag.H3 || t == Tag.A) isLink = false;
          }
        }
      );
      return RiTa.strArr(links);
    }
    catch (Throwable e)
    {
      throw new RiTaException(e);
    }
  }
  
  public String[] getResultText(String query)
  {
    final List links = new ArrayList();
    try
    {
      customParse(query, new HTMLEditorKit.ParserCallback() // an inner class
      {
        String link;
        boolean isH3 = false, isA = false;
        public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
          if (t == Tag.H3) {
            String clss = (String) a.getAttribute(Attribute.CLASS);
            isH3 = clss.equals("r");
          }
          if (isH3 && t == Tag.A) {
            isA = true;
            link = "";
          }
        }
        public void handleText(char[] data, int pos)
        {
          if (isA) {
            link += new String(data);
          }
        }
        public void handleEndTag(Tag t, int pos) {
          if (t == Tag.H3) isH3 = false;
          if (isH3 && t == Tag.A) {
            isA = false;
            links.add(link);
          }
        }
      });
      return RiTa.strArr(links);
    }
    catch (Throwable e)
    {
      throw new RiTaException(e);
    }
  }

  public String[] getImageURLs(String query)
  {
    return getImageURLs(query, false);
  }
  
  // END New methods ------------------------------------

  
  public String fetch(String queryURL)
  {
    String line = E;
    BufferedReader in = null;
    try
    {
      URL url = new URL(queryURL);
      
      if (dbugFetch) System.out.println("URL: " + queryURL);
      
      HttpURLConnection conn = (HttpURLConnection) (url.openConnection());
      setRequestHeaders(conn);

      numCalls ++;
      try
      {
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      }
      catch (FileNotFoundException e)
      {
        throw new RiTaException("Google: page not found (404):\n" + queryURL);
      }
      catch (Exception e)
      {
        if (e != null) {
          String message = e.getMessage();
          if (message.startsWith(FORBIDDEN_503))
            throw new RiTaException("Google request rejected(503) for:\n" + queryURL);
        }
        throw new RiTaException(e);
      }
      
      StringBuffer sb = new StringBuffer();
      if (in != null) {
        while ((line = in.readLine()) != null)
          sb.append(line + '\n');
      }
      return sb.toString();
    }
    catch (Exception e)
    {
      if (e instanceof RiTaException) {
        try
        {
          throw e;
        }
        catch (Exception e1)
        {
          e1.printStackTrace();
        }
      }
      if (!RiTa.SILENT)
        System.err.println("EXCEPTION: " + e.getClass() + " query=" + queryURL);
      throw new RiTaException("query=" + queryURL, e);
    }
    finally
    {
      try
      {
        if (in != null)
          in.close();
      }
      catch (IOException e){}
      in = null;
    }
  }

  /** 
   * Returns the bigram coherence for the word pair where
   * coherence(w1, w2) = count(w1 + w2)/(count(w1) + count(w2))
   * [from Gervas]
   */
  public float getBigram(String word1, String word2)
  { 
    // System.out.println("RiGoogler.getBigram("+"\""+word1+" "+word2+"\""+")");
    boolean DBUG = false;
    
    long pair = getCount("\""+word1+" "+word2+"\"");
    
    //if (!DBUG && pair==0) return 0;
    
    long first = getCount(word1);
    long last = getCount(word2);    

    if (DBUG) {
      
      System.out.println("getCount1("+word1+") = "+first);
      System.out.println("getCount2("+word2+") = "+last);
      System.out.println("getPair(\""+word1+" "+word2+"\") = "+pair);
      
      if (pair==0) {
        
        if (DBUG)System.out.println("getBigram("+word1+","+word2+") = 0");
        return 0;
      }
    }
        
    float val = pair / (float)(first+last);
    
    if (DBUG)System.out.println("getBigram("+word1+","+word2+") = "+val);
    
    return val;
  }
  
  /**
   * Returns the product of the avg value of all bigram pairs 
   * and the minimum bigram value in the sentence. Equivalent to 
   * (but more efficient than): getBigramAvg(s) * getBigramMin(s)
   */
  public float getWeightedBigram(String[] sentence)
  {    
    boolean DBUG = false;
    float sum = 0;
    float minVal = Float.MAX_VALUE;
    for (int i = 1; i < sentence.length; i++)  {
      float bg = getBigram(sentence[i-1], sentence[i]);
      if (bg == 0) return 0; 
      if (bg < minVal) minVal = bg;
      sum += bg;
    }
    float avg = sum/(float)(sentence.length-1);
    if (DBUG)System.out.println("avg="+avg+" / min="+minVal);
    return avg * minVal;
  }

  /**
   * Uses Google's "fill in the blank" completion feature to return the missing word(s) in a search...
   * @param words the term to search before the asterisk
   */
  public String[] getCompletions(String words)
  {
    return this.getCompletions(words, Integer.MAX_VALUE);
  }
  
  /**
   * Uses Google's "fill in the blank" completion feature to return the missing word(s) in a search...
   * @param pre the term to search before the asterisk
   * @param post the term to search after the asterisk
   */
  public String[] getCompletions(String pre, String post)
  {
    return getCompletions(pre.trim()+" * "+post.trim());
  }
      
  public String[] getCompletionsOnPage(String words)
  { 
    // DCH: WORKING HERE :  Mar 2, 2013
    
    String[] links = getResultLinks(words);
    for (int i = 0; i < links.length; i++)
    {
      System.out.println("FETCH EACH PAGE AND THEN PARSE FOR words!!");
    }
    return null;
  }
  
  /**
   * Uses Google's "fill in the blank" completion feature to return the subsequent words matched in a search...
   * @param words the term to search
   * @param maxWords the max number of subsequent words to return (default=infinity)
   */
  public String[] getCompletions(String words, int maxWords)
  {
    //System.out.println("RiGoogleSearch.getCompletions("+words+","+maxWords+")");

    words = quotifyQuery(words);
    String parseRE = buildRegex(words);
    String queryURL = makeCompQuery(words);
    
    //System.out.println("QUERY: "+queryURL);
    
    String html = fetch(queryURL);   
    
    List parsed = parseRefs(html, "<em>([^<]+)</em>");
    
    List<String> result = new ArrayList<String>();
    for (Iterator it = parsed.iterator(); it.hasNext();)
    {
      String s = (String) it.next();
      
      //if (s.contains("*")) continue;

      if ((s = parseRef(s, parseRE)) != null) {
        String toAdd = replaceEntities(s).trim();
        
        if (toAdd.length()>1 && !result.contains(toAdd)) {
          result.add(toAdd);
        }
      }
    }
    
    String[] arr = result.toArray(new String[result.size()]);
    joinSubArrays(arr, maxWords);
    
    if (dbugFetch && arr.length < 1)
    {
       // problem! write html to file & exit
      throw new RiTaException("No results found for query: '" 
          + queryURL +"'\n  writing HTML to "+writeToFile
          ("google-"+type.name().toLowerCase()+"-out.html",html)); 
    }
    
    Set<String> s = new HashSet<String>();
    for (int i = 0; i < arr.length; i++)
      s.add(arr[i]);
    
    return (String[]) s.toArray(new String[s.size()]);
  }

  String buildRegex(String words)
  {
    String raw = words.replaceAll("[\"]", E).replaceAll("\\s+", SP);
    String parseRE = raw.replaceAll(SP, ".+?").replaceAll("\\*", "(.*)");
    //System.out.println("RE: "+parseRE+" from: '"+raw+"'");
    return parseRE;
  }

  String quotifyQuery(String words)
  {
    if (!words.startsWith("\"")) words = DQ + words;
    if (!words.endsWith("\"")) words = words + DQ;
    if (!words.contains("*"))
      words = words.replaceAll("\"$"," *\"");
    return words;
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
    if (cacheEnabled) // check cache
    {
      if (cache == null)
        cache = new HashMap();
      Integer tmp = (Integer) cache.get(cacheKey(query));
      if (tmp != null)
        return tmp.intValue();
    }

    // fetch html && check against patterns
    String queryURL = makeQuery(query);
    String html = fetch(queryURL);
    int result = checkPatterns(html);

    if (result >= 0)
    {
      if (cacheEnabled) // add to cache
        cache.put(cacheKey(query), new Integer(result));
    }
    else if (dbugFetch)
    {
       // problem! write html to file & exit
      throw new RiTaException("No value (" + result + 
          ") found for " + "query: '"+ query +"'\n  writing HTML to "+
          writeToFile("google-"+type.name().toLowerCase()+"-out.html",html)); 
    }

    return result; 
  }

  public GoogleDirect searchType(String searchType)
  {
    SearchType[] values = SearchType.values();
    for (int i = 0; i < values.length; i++)
    {
      if (values[i].name().equalsIgnoreCase(searchType))
        this.type = values[i];
    }
    throw new RiTaException("Invalid type: "+searchType);
  }

  public String searchType()
  {
    return type.name();
  }

  public GoogleDirect userAgent(String userAgent)
  {
    this.userAgent = userAgent;
    return this;
  }

  public String userAgent()
  {
    return this.userAgent;
  }

  public GoogleDirect cookie(String cookieString)
  {
    this.cookie = cookieString;
    return this;
  }

  public String cookie()
  {
    return cookie;
  }

  public GoogleDirect cookiePath(String path)
  {
    if (path != null && path.equals(cookiePath)) 
      return this;
    
    this.cookiePath = path;

    return (path != null) ? loadCookies(path) : this;
  }

  public String cookiePath()
  {
    return this.cookiePath;
  }
  
  // HELPERS ----------------------------------------------------------
  
  protected GoogleDirect loadCookies(String path)
  {   
    String cStr = E;
    
    if (path != null) {

      try {
        new File(path).exists();
      }
      catch (Exception e) {
        
        System.err.println("[WARN] Unable to find cookie file at: "
          +path+"\n"+(e==null?E:e.getMessage()));
        return this;
      }

      String contents = RiTa.loadString(path);

      //System.out.println(contents);
  
      String[] cookies = contents.split(END_DICT);
      List cmaps = createCookieMaps(cookies, true);
  
      for (Iterator iterator = cmaps.iterator(); iterator.hasNext();)
      {
        Map cookie = (Map) iterator.next();
        String name = (String)cookie.get(NAME);
        cStr += name+"="+cookie.get(VALUE)+"; ";
      }
      
      if (!RiTa.SILENT) System.out.println("[INFO] Using Cookie: '"+cStr+"'");
    }
    
    return this.cookie(cStr);
  }
  
  protected void setRequestHeaders(HttpURLConnection conn) 
  {
    conn.setRequestProperty("User-Agent", userAgent);
    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
    conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
    conn.setRequestProperty("Connection", "keep-alive");
    conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    conn.setRequestProperty("Cookie", cookie);
    conn.setRequestProperty("DNT", "1");
  } 
  
  private List parseRefs(String html, String regex)
  {
    Matcher matcher = Pattern.compile(regex,Pattern.CASE_INSENSITIVE).matcher(html);

    // Get all groups for this pattern
    List matches = new ArrayList();
    while (matcher.find()) {
      String url = matcher.group(1);
      url = decodeUrl(url);
      if (url != null) matches.add(url);
    }
    
    if (matches.size()<1) {
      System.err.println("[WARN] parseRef("+regex+") failed to match...");
    }

    return matches;
  }
  
  protected String parseRef(String html, String regex)
  {
    Matcher matcher = Pattern.compile(regex,Pattern.CASE_INSENSITIVE).matcher(html);
    List matches = new ArrayList();
    if (matcher.find())
    {
      String url = matcher.group(1);
      if (1==2) System.err.println("RAW(1): " + url);
      return decodeUrl(url);
    }
    
    return null;
  }
  
  private String decodeUrl(String url)
  {
    if (url != null) {
      try
      {
        return URLDecoder.decode(url, "UTF8");
      }
      catch (UnsupportedEncodingException e)
      {
        System.err.println(e.getMessage());
      }
    }
    return url;
  }
  
  private List createCookieMaps(String[] cookies, boolean googleOnly)
  {
    List l = new ArrayList();
    for (int i = 0; i < cookies.length; i++)
    {
      String key = null;
      boolean gotDict = false;
      Map m = new HashMap();
      
      String[] lines = cookies[i].split("\n");
      
      for (int j = 0; j < lines.length; j++)
      {
        String line = lines[j].trim();
        if (line.equals(DICT)) {
          gotDict = true;
          continue;
        }
        if (gotDict) {
          
          if (line.startsWith(KEY)) {
            
            key = line.replaceAll(END_KEY, E);
          }
          else if (line.startsWith(STRING) && key != null) {
            
            m.put(key, line.replaceAll(END_STRING, E));
            key = null;
          }
        }
      }
      if (m.size() > 0) { 
        
        String domain = (String) m.get(DOMAIN);
        if (domain != null) {
          if (!googleOnly || domain.endsWith("google.com")) {
            // System.out.println(l.size()+") "+m);
            l.add(m);
          }
        }
      }
    }
    
    if (!RiTa.SILENT && googleOnly && l.size()<1)
      System.out.println("[WARN] No google cookie found");
    
    return l;
  }
  
  /** Returns input String with XML/HTML entities replaced */
  protected static String replaceEntities(String input)
  {    
    return EntityLookup.getInstance().unescape(input);
  }
  
  private String cacheKey(String query)
  {
    return type.name().toLowerCase()+"_"+query;
  }
  
  private int checkPatterns(String line)
  {
    if (line.indexOf(FORBIDDEN_403) > -1)
      throw new RiTaException("Google request rejected(403): " + line);

    if (line.indexOf("<p>... but your computer or network may be sending automated queries.") > -1)
      throw new RiTaException("Google request rejected(Sorry): " + line);
    
    int result = -1;

    // try the no-result patterns
    for (int i = 0; i < NO_RESULTS.length; i++)
    {
      if (line.indexOf(NO_RESULTS[i]) > -1)
      {
        result = 0;
        break;
      }
    }

    if (result < 0) // no match as yet
    {
      // try our set of count patterns
      for (int i = 0; i < patResult.length; i++)
      {
        result = regexCheck(patResult[i].matcher(line));
        if (result > 0) break;
      }
    }
    return result;
  }
  
  private int regexCheck(Matcher m)
  {
    int result = 0;
    if (m.find()) {
      
      String countOld = m.group(1);
      StringBuilder countNew = new StringBuilder();
      for (int i = 0; i < countOld.length(); i++) {
        char c = countOld.charAt(i);
        if (c >= '0' && c <= '9')
          countNew.append(c);
      }
      
      long l = Long.parseLong(countNew.toString());
      result = (l > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)l;
    }
    
    return result;
  }
  
  protected static void joinSubArrays(String[] arr, int len)
  {
    for (int i = 0; i < arr.length; i++)
    {
      String[] wds = arr[i].split(SP);
      String[] dst = new String[Math.min(len, wds.length)];
      System.arraycopy(wds, 0, dst, 0, dst.length);
      arr[i] = RiTa.join(dst);
    }
  }
  
  protected String makeCompQuery(String query)
  {
    //String type = searchBooks ? "books" : "search";
    query = query.replaceAll(DQ, "%22");
    query = query.replaceAll(SP, "+");
    String queryURL = "http://www.google.com/"+type.name().toLowerCase()
      +"?sugexp=chrome,mod=8&sourceid=chrome&ie=UTF-8&q="+query;
    if (startIndex != 1) queryURL += "&start="+startIndex;

    return queryURL.replaceAll("%2B", "+");    
  }
  
  private String makeQuery(String query)
  {
    //System.out.println("RiGoogleSearch.makeQuery("+query+", type="+type+")");
    
    query = query.replaceAll("\"", "%22").replaceAll(" ", "+");
    
    String queryURL = "http://www.google.com/" + type.name().toLowerCase()
    + "?hl=en&safe=off&q=" + query + "&btnG=Google+Search";
  
    if (type == SearchType.IMAGE) { 

      //queryURL = "http://images.google.com/search?q="+query+"&num=30&hl=en&tbm=isch&sout=1&biw=1457&bih=1209";
      queryURL = "http://www.google.com/search?hl=en&site=imghp&tbm=isch&source=hp&biw=1513&bih=819&q="+query+"&oq="+query;
    }
    
    if (startIndex != 1) queryURL += "&start="+startIndex;

    return queryURL.replaceAll("%2B", "+");
  }

  static String writeToFile(String fname, String content)
  {   
    // file:///Users/dhowe/Documents/eclipse-workspace/RiTaLibrary/google-[type]-out.html
    String htmlStr = (content == null) ? "null" : content;
    try
    {
      FileWriter fw = new FileWriter(fname);
      fw.write(htmlStr.toString());
      fw.flush();
      fw.close();
    }
    catch (Exception e)
    {
      System.err.println("Error writing file: "+fname+"\n\n");
      //e.printStackTrace();
    }
    
    return fname;
  }
  
  public RiGooglerIF customParse(String query, HTMLEditorKit.ParserCallback pcb) 
  {
    try
    {
      String sq = makeQuery(query);
      StringReader sr = new StringReader(fetch(sq));
      return this.customParse(sr, pcb);
    }
    catch (Exception e)
    {
      throw new RiTaException(e);
    }
  }
  
  protected RiGooglerIF customParse(Reader r, HTMLEditorKit.ParserCallback pcb) throws IOException
  {
    new ParserDelegator().parse(r, pcb, true);
    return this;
  }
  
  public int startIndex()
  {
    return startIndex;
  }

  public GoogleDirect startIndex(int startIndex)
  {
    this.startIndex = startIndex;
    return this;
  }

  static final String DEFAULT_COOKIE = "PREF=ID=906b29e43377853e:FF=0:TM=1362318896:LM=1362318897:S=LDTg297FWJkZf649";
  static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5";
  
  private static final String FORBIDDEN_503 = "Server returned HTTP response code: 503";
  private static final String FORBIDDEN_404 = "<title>Error 404 (Not Found)!!1</title>";
  private static final String FORBIDDEN_403 = "<title>403 Forbidden</title>";
  private static final String[] NO_RESULTS = { "No results found for", "</b> - did not match any documents." };

  private static final String[] RESULTS_PATS = { 
    " <b>1</b> - <b>(?:[0-9]|10)</b> of (?:about )?<b>([0-9,]+)</b>", "([0-9,]+) results?<" 
  };

  public static void main(String[] args)
  {
    String query = "The dog";
    GoogleDirect rg = new GoogleDirect();
    rg.cookiePath("/Users/dhowe/Library/Cookies/Cookies.plist");
    String[] completions = rg.getCompletions(query,1);
    for (int i = 0; i < completions.length; i++)
    {
      System.out.println(completions[i]);
    }
  }
}
