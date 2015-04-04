package rita.support;

import java.util.Map;

import rita.RiGrammar;
import rita.RiTaException;
import rita.json.JSONObject;

public class YAMLParser
{
  public static String SNAKEYAML = "org.yaml.snakeyaml.Yaml";

  static String sentenceGrammarJSON = "{ \"<start>\" : \"<noun_phrase> <verb_phrase>\", \"<noun_phrase>\" : \"<determiner> <noun>\", \"<verb_phrase>\" : \"<verb> | <verb> <noun_phrase> [.1]\", \"<determiner>\" : \"a [.1] | the\", \"<noun>\" : \"woman | man\", \"<verb>\" : \"shoots\" }";
  static String sentenceGrammarYAML = "<start> : <noun_phrase> <verb_phrase>\n<noun_phrase>: <determiner> <noun>\n<verb_phrase> : <verb> | <verb> <noun_phrase> [.1]\n<noun>: woman | man\n<determiner>: a [.1] | the\n<verb>: shoots";
  static String sentenceGrammarYAML2 = "<start> : <noun_phrase> <verb_phrase>\n<noun_phrase>: <determiner> <noun>\n<verb_phrase> : \n  - <verb> \n  - <verb> <noun_phrase> [.1]\n<noun>: \n  - woman\n  - man\n<determiner>: \n  - a [.1] \n  - the\n<verb>: shoots";

  private static Object parserImpl;

  public YAMLParser()
  {
    try
    {
      parserImpl = Class.forName(SNAKEYAML).newInstance();
    }
    catch (Exception e)
    {
      throw new RiTaException("Unable to find SnakeYAML classes!\n"
          + "Make sure to include snakeyaml-X.YZ.jar in your classpath.");
    }
  }
  
  public static void main(String[] args)
  {
    String[] s = { sentenceGrammarJSON, sentenceGrammarYAML,sentenceGrammarYAML2 };
    for (int j = 0; j < s.length; j++)
    {
      JSONObject jso = new YAMLParser().yamlToJSON(s[j]);
      RiGrammar rg = new RiGrammar();
      rg.load(jso);
      for (int i = 0; i < 10; i++)
      {
        System.out.println(rg.expand());  
      }
      System.out.println();
    }
  }
  
  public Map yamlToMap(String yaml)
  {
    return parserImpl != null ? (Map)yamlToObject(yaml) : null;
  }
    
  public JSONObject yamlToJSON(String yaml)
  {
    return parserImpl != null ? new JSONObject(yamlToMap(yaml)) : null;
  }
  
  private static Object yamlToObject(String yaml)
  {
    try
    {
      LoadableIF p = (LoadableIF) RiDynamic.cast(parserImpl, LoadableIF.class);
      Object loaded = p.load(yaml);
      return loaded;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
}
