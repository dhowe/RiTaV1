
package rita.docgen;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.RiTa;

public class DictWithoutConjugatedVerbs {
  
  static String DICT_FILE = "js/src/rita_dict.js";
  static String OUTPUT_FILE = "/tmp/rita_dict_new.js"; // change me
  
  static String header, footer;
  static int matches = 0;
  static int deleted = 0;
  static int entries = 0;
  
  static Matcher dublicateLetter;

  public static void main(String[] args) {
    
    HashMap rdata = parseRiTaDict(RiTa.loadStrings(DICT_FILE));
    SortedMap newdata = generate(rdata);
    System.out.println("--------------------");
    System.out.println("Original entries : "+ rdata.size());
    System.out.println("Matches : "+ matches);
    System.out.println("Deleted : " + deleted);
    System.out.println("Entries left : " + entries);
    
    System.out.println("");
    
    System.out.println("Wrote: "+writeToFile(OUTPUT_FILE, mapToString(newdata)));
  }

  private static SortedMap generate(HashMap rdata) {
    SortedMap<String, String> newdata = new TreeMap<String, String>();
    
    for (Iterator<String> it = rdata.keySet().iterator(); it.hasNext();){
      String word = it.next();
      String[] rval = (String[]) rdata.get(word);
      String phones = rval[0];
      String pos = rval[1];
	
      if(!isConjugatedVerb(rdata, word, rval[1])){
	  newdata.put(word, "['"+ phones +"','"+ pos +"']");
	  entries ++;
      }else {
//	System.out.println("[Delete] " + word + ":" + pos );
	deleted ++;
      }

      }
  
    return newdata;
  }
  
  private static HashMap<String, String[]> parseRiTaDict(String[] words) {
    HashMap<String, String[]> rita = new HashMap<String, String[]>();
    header = words[0].trim();
    footer = words[words.length-1].trim();
    for (int i = 1; i < words.length-1; i++) {
      String[] parts = words[i].trim().split(":");
      if (parts.length != 2)
	throw new RuntimeException("Bad line: "+words[i]);
      String word = parts[0].replaceAll("'", "").trim();
      String value = parts[1].replaceAll("['\\[\\]]","").replaceAll(",$", "");
      parts = value.split(",");
      String phones = parts[0].replaceAll("^ ","");
      String sylls = parts[1];
      rita.put(word, new String[]{ phones, sylls });
    }
    return rita;
  }
  

  static String writeToFile(String fname, String content) {   
    try {
      FileWriter fw = new FileWriter(fname);
      fw.write((content == null) ? "" : content);
      fw.flush();
      fw.close();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    return fname;
  }
  
  private static String mapToString(SortedMap newdata) {
    
    StringBuilder sb = new StringBuilder();
    sb.append(header+"\n");
    for (Iterator<String> it = newdata.keySet().iterator(); it.hasNext();) {
      String word = it.next();
      sb.append("'"+word+"':"+newdata.get(word));
      sb.append(it.hasNext() ? ",\n" : "\n");
    }
    sb.append(footer);
    return sb.toString();
  }
  
  static boolean isConjugatedVerb(HashMap rdata, String word, String pos) {
    //only matches words whose tags consist only of vb*
    if(pos.matches("^vb[a-z]( vb[a-z])*$")) {
      matches++;
      String vb= getvb(word, pos); 
      if(vb == ""){
	System.err.println("[Keep] Can't get the vb of word:" + word + " " + pos);
	 /** Case 1: vbp
	  *  Case 2: stand - stood
	    **/
	return false;
      }
      
      if(rdata.containsKey(vb)) return true;
      else{
	//deal with vb that should end with "e" Ex: removed -> remove
	//any better solution?
	if(!vb.endsWith("e") && rdata.containsKey(vb+"e")) return true;
      }
      System.out.println("[Keep] " + word + " "+ vb + " " + pos );
    }
    
    return false;
  }
  
  static String getvb(String word, String pos){
    String vb="";
    
    //3rd person singular present
    if (pos.contains("vbz")){
      if (word.endsWith("ies"))  vb = word.replaceAll("ies$", "y");
      else if (word.endsWith("es")) vb = word.substring(0, word.length()-2);
      else if (word.endsWith("s"))  vb = word.substring(0, word.length()-1);
    }
    
    //past tense or past participle
    if (pos.contains("vbn") || pos.matches("vbd")){
      if (word.endsWith("ied"))  vb = word.replaceAll("ied$", "y");
      else if (word.endsWith("ed")) vb = word.substring(0, word.length()-2);
    }
    
    //present participle
    if (pos.contains("vbg") && word.endsWith("ing"))
       vb = word.substring(0, word.length()-3);
   
    //non-3rd person singular present
    /** Can we convert the only vbp ones to vb?
     * Special cases:
     * --not sure how to handle these
     * damped:vbn vbd vbp  
     * obtained:vbn vbd vbp
     * totaled:vbd vbn vbp
     * helped:vbd vbn vbp
    **/
    if (pos.contains("vbp"))
       System.err.println("!" + word + ":" + pos);


    //deal with repeated ending
    if(vb != "" && vb.charAt(vb.length()-1) == vb.charAt(vb.length()-2))  vb = vb.substring(0, vb.length()-1);
    return vb;
  }
  
  }
