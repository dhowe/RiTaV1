package rita.docgen;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import rita.RiTa;
import rita.RiTaException;

public class DictFromCMU {
  
  static int count = Integer.MAX_VALUE;
  static String header, footer, keepers = "|earful|schoolgirl|sugarcane|outdoorsman|exorcism|gigolo|google|";

  public static void main(String[] args) {
    
    //String dict = RiTa.loadString("java/rita/rita_dict.js");
    String[] rwords = RiTa.loadStrings("js/src/rita_dict.js");
    String[] cwords = RiTa.loadStrings("js/src/cmudict-0.6syll");    
    HashMap<String, String[]> rdata = parseRiTa(rwords);
    HashMap<String, String> cdata = parseCMU(cwords);
    SortedMap<String, String> newdata = new TreeMap<String, String>();
    int missing = 0;
    for (Iterator<String> it = rdata.keySet().iterator(); it.hasNext();) {
      String word = it.next();
      String[] rval = rdata.get(word);
      String ritaPhones = rval[0], ritaPos = rval[1];
      String cmuPhones = cdata.get(word);
      if (cmuPhones == null) {
	if (keepers.contains("|"+word+"|")) {
	  cmuPhones = ritaPhones;
	  //System.err.println("Keeping: "+word+" "+cmuPhones);
	}
	else
	  continue;
	//System.err.println("Missing: "+word+" "+ritaPhones);
      }
      cmuPhones = cmuPhones.replaceAll("[02]", "");
      String data = "['"+cmuPhones+"','"+ritaPos+"']";
      newdata.put(word, data);
      //System.out.println("'"+word+"':['"+cmuPhones+"','"+ritaPos+"'],");
    }
    System.out.println("---------------------------------------------");
    StringBuilder sb = new StringBuilder();
    sb.append(header+"\n");
    for (Iterator<String> it = newdata.keySet().iterator(); it.hasNext();) {
      String word = it.next();
      sb.append("'"+word+"':"+newdata.get(word));
      sb.append(it.hasNext() ? ",\n" : "\n");
    }
    sb.append(footer);
    System.out.println("Wrote: "+writeToFile("js/src/rita_newdict.js", sb.toString()));
    //System.out.println("Missing: "+missing);
  }

  private static HashMap<String, String> parseCMU(String[] words) {
    HashMap<String, String> cmu = new HashMap<String, String>();
    for (int i = 0; i < words.length; i++) {
      //System.out.println(words[i]);
      String[] parts = words[i].toLowerCase().trim().split("  +");
      if (parts.length != 2)
	throw new RuntimeException("Bad line: "+words[i]);
      String word = parts[0].trim();
      String value = parts[1].trim();
      String sylls = value.replaceAll(" - ", "/").replaceAll(" ", "-").replaceAll("/", " ");
      //System.out.println("|"+word+"|"+value+"|");
      cmu.put(word, sylls);
    }
    return cmu;
  }

  private static HashMap<String, String[]> parseRiTa(String[] words) {
    HashMap<String, String[]> rita = new HashMap<String, String[]>();
    header = words[0].trim();
    footer = words[words.length-1].trim();
    //System.out.println(header+"\n"+footer);
    for (int i = 1; i < Math.min(count,words.length-1); i++) {
      String[] parts = words[i].trim().split(":");
      if (parts.length != 2)
	throw new RuntimeException("Bad line: "+words[i]);
      String word = parts[0].replaceAll("'", "").trim();
      String value = parts[1].replaceAll("['\\[\\]]","").replaceAll(",$", "");
      parts = value.split(",");
      String phones = parts[0].replaceAll("^ ","");//replaceAll("(^ )|[012]","");//replaceAll("-", " ")
      String sylls = parts[1];
      //System.out.println(word+"||"+phones+"||"+sylls+"||");
      rita.put(word, new String[]{ phones, sylls });
    }
    return rita;
  }
  
  static String writeToFile(String fname, String content)
  {   
    try
    {
      FileWriter fw = new FileWriter(fname);
      fw.write((content == null) ? "" : content);
      fw.flush();
      fw.close();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    
    return fname;
  }
}
