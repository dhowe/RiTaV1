package rita;

import java.io.*;
import java.util.*;


import rita.*;
import rita.json.JSONArray;
import rita.json.JSONException;
import rita.support.PosTagger;
import rita.support.TextNode;

/*
 * DH: NOT READY YET
 * Next: implement chaining off nouns and/or verbs
 */
public class RiTranslator
{
  static { RiTa.init(); }
  
  public static final String COOKIE_PATH = System.getProperty("user.home")
      + "/Library/Cookies/Cookies.plist";

  public static String TRANSLATE_URL = "http://translate.google.com/translate_a/ex?sl=en&tl=en&q=%QUERY%&utrans=";

  protected RiGoogler google;
  protected RiLexicon lexicon;
  
  String ignores = "athletics,football,archery,badminton,baseball,basketball,bowling,boxing,cricket,cycling,canoe,curling,climbing,diving,dancing,equestrian,football,fencing,gymnastics,golf,hockey,handball,hockey,skating,judo,karate,lacrosse,olympics,polo,rugby,running,skating,soccer,squash,swimming,sailing,skiing,surfing,softball,tennis,volleyball,wrestling,weightlifting,yoga,NBA,NFL,MLB";

  public RiTranslator()
  {
    this.google = new RiGoogler();
    google.cookiePath(COOKIE_PATH);
  }

  class Gloss
  {
    String sentence, source, url;

    public Gloss(JSONArray jsa)
    {
      if (jsa.length() != 5)
        throw new RuntimeException("Bad json data: " + jsa);
      
      try
      {
        sentence = jsa.getString(0);
        source = jsa.getString(1);
        url = jsa.getString(2);
        sentence = sentence.replaceAll("</?b>", "");
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
    }

    public String toString()
    {
      return "['" + sentence + "', " + source + ", " + url + "]";
    }

  }

  public String[] getSources(String s) throws JSONException
  {
    Gloss[] g = getGlosses(s);
    String[] res = new String[g.length];
    for (int i = 0; i < res.length; i++)
      res[i] = g[i].source;
    return res;
  }

  public String[] getSourceUrl(String s) throws JSONException
  {
    Gloss[] g = getGlosses(s);
    String[] res = new String[g.length];
    for (int i = 0; i < res.length; i++)
      res[i] = g[i].url;
    return res;
  }

  public String[] getSentences(String s) throws JSONException
  {
    Gloss[] g = getGlosses(s);
    String[] res = new String[g.length];
    for (int i = 0; i < res.length; i++)
      res[i] = g[i].sentence;
    return res;
  }

  public Gloss[] getGlosses(String[] s) throws JSONException
  {
    return getGlosses(RiTa.join(s, " "));
  }

  public Gloss[] getGlosses(String s)
  {
    String query = s.replaceAll("\\s+", "%20");
    String url = TRANSLATE_URL.replaceAll("%QUERY%", query);
    //System.out.println("URL: "+url);
    String data = google.fetch(url);

    JSONArray js = null;
    try
    {
      js = new JSONArray(data);
      js = firstIndex(js);
      js = firstIndex(js);
      if (js.length() < 5)
        throw new RuntimeException("Bad json: " + js);
    }
    catch (Exception e)
    {
      System.err.println("[WARN] No data for: '" + s + "'");
      return null;
    }

    Gloss[] glosses = new Gloss[js.length()];
    for (int j = 0; j < js.length(); j++)
    {
      try
      {
        glosses[j] = new Gloss(js.getJSONArray(j));
        //System.out.println(glosses[j].sentence + " -> "+RiTa.asList(extractNouns(glosses[j].sentence)));
      }
      catch (JSONException e)
      {
        throw new RuntimeException(e);
      }
    }
    
    return glosses;
  }

  public JSONArray firstIndex(JSONArray js) throws JSONException
  {
    if (js.length() < 1)
      throw new RuntimeException("Bad json: " + js);
    return js.getJSONArray(0);
  }
  


  private String[] extractNouns(String sent)
  {
    return extractNouns(sent, RiTa.STOP_WORDS);
  }
  
  private String[] extractNouns(String sent, String[] stopWords)
  {
    if (lexicon == null)
      lexicon = new RiLexicon();
    
    List nouns = new ArrayList();
    
    String[] words = RiTa.tokenize(sent);
    String[] posTags = RiTa.getPosTags(words);
    
    if (posTags.length != words.length)
      throw new RuntimeException("illegal");
    
    for (int i = 0; i < posTags.length; i++)
    {
      if (posTags[i].equals("nnps")) { 
        System.out.println("SKIP(nnps): "+words[i]);

        continue;
      }

      if (PosTagger.isNoun(posTags[i])) {
        if (lexicon.containsWord(words[i]))
          nouns.add(words[i]);
        else
          System.out.println("SKIP(!lex): "+words[i]);
 
      }
    }
    
    for (Iterator it = nouns.iterator(); it.hasNext();)
    {
      String str = (String) it.next();
      for (int i = 0; i < stopWords.length; i++)
      {
        if (str.equalsIgnoreCase(stopWords[i]))
          it.remove();
      }
    }
    
    return (String[]) nouns.toArray(new String[nouns.size()]);
  }
  
  /*private void _generate(TextNode node, int target)
  {
    if (node.size() >= target) return;
    
    Iterator it = node.childIterator();
    while (it.hasNext()) {
      _generate((TextNode) it.next(), target);
    } 
  }
  public String[] generate(String start, int sentences) 
  {
    List<String> result = new ArrayList<String>();
    _generate(start, sentences, result);
    return RiTa.strArr(result);
    //String[] result = new String[sentences];
  }
  
  private void _generate(String start, int numSentences, List<String> result) {

    Gloss[] choices = null;
    
    while (result.size() < numSentences) {
      
      choices = getGlosses(start);
      for (int i = 0; choices != null && i < choices.length; i++)
      {
        String[] nouns = extractNouns(choices[i].sentence);
        for (int j = 0; j < nouns.length; j++)
        {
          start = nouns[j];
          _generate(start, numSentences, result);
        }
      }
    }    
  }*/
  
  static Set duplicates = new HashSet(), previousCalls = new HashSet();
  
  void _generate(TextNode node, String word, int count) {

    boolean DBUG = false;
    
    if (node.depth() >= count) return;
    
    if (DBUG) System.out.println("CALL: "+word);
    
    Gloss[] glosses = getGlosses(word);
    
    if (glosses == null) return;
    
    RiTa.shuffle(glosses);
    previousCalls.add(word);
    
    for (int i = 0; i < glosses.length; i++)
    {
      String sent = glosses[i].sentence;
      if (duplicates.contains(sent)) continue;
      
      duplicates.add(sent); 
      
      TextNode child = node.addChild(sent);
      
      if (DBUG) System.out.println("ADDED: "+sent);
      
      String[] nouns = extractNouns(sent);
      RiTa.shuffle(nouns);
      
      if (DBUG) System.out.println("NOUNS: "+RiTa.asList(nouns));
      for (int j = 0; j < nouns.length; j++)
      {
        String noun = nouns[j].toLowerCase();
        
        if (!previousCalls.contains(noun) || ignores.indexOf(noun) > -1) {
          
          _generate(child, noun, count); // recurse
        }
      }
    }
  }
  
  static void append(File file, String[] lines) {
    try
    {
        String filename= "MyFile.txt";
        FileWriter fw = new FileWriter(filename,true); // append the new data
        for (int i = 0; i < lines.length; i++)
        {
          fw.write("add a line\n");
        }
        fw.close();
    }
    catch(IOException ioe)
    {
        System.err.println("IOException: " + ioe.getMessage());
    }
  }
  
  String[] generate(String word, int count) {
    
    TextNode root = TextNode.createRoot();
    this._generate(root, word, count);
    
    System.out.println("DONE\n\n");
    System.out.println(root.asTree()+"\n\n");
    
    TextNode[] path = root.longestPath();
    String[] result = new String[path.length];
    for (int i = 0; i < result.length; i++)
      result[i] = path[i].token();
    
    return result;
  }
    
  // TODO: output all 'count' length strings
  // TODO: append them to sentence file
  public static void main(String[] args)
  {
    RiTranslator tr = new RiTranslator();
    String[] tns = tr.generate("violence", 2);
    for (int i = 0; i < tns.length; i++)
    {
      System.out.println(i+") "+tns[i]);
    }
  }

}
