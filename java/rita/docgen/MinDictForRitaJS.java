package rita.docgen;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import rita.RiTa;

public class MinDictForRitaJS {
  
  static String WORD_FILE = "js/src/1000.txt";
  static String DICT_FILE = "js/src/rita_dict.js";
  static String OUTPUT_FILE = "/tmp/rita_dict_1000.js"; // change me
  
  static String header, footer;
  static int entries = 0;
  static int deleted = 0;
  
  static String failed = "";
  static String vbx = "";
  
  public static void main(String[] args) {
    String[] words = RiTa.loadStrings(WORD_FILE);
    
    HashMap rdata = parseRiTaDict(RiTa.loadStrings(DICT_FILE));
    SortedMap newdata = generate(rdata, words);
    System.out.println("--------------------");
    System.out.println("List : "+ words.length);
    System.out.println("Words : "+ entries);
    System.out.println("Deleted : " + deleted);
    System.out.println("Ignore words: " + failed);
    System.out.println("VB*: " + vbx);
    System.out.println("");
    
    System.out.println("Wrote: "+writeToFile(OUTPUT_FILE, mapToString(newdata)));
  }

  private static SortedMap generate(HashMap rdata, String[] words) {
    SortedMap<String, String> newdata = new TreeMap<String, String>();
    
    for (int i = 0; i < words.length; i++) {
      String word = words[i].toLowerCase();
      String[] rval = (String[]) rdata.get(word);
      if(rval == null){
	 System.out.println("Warning!Word not found: " + word);
	 failed += word + " ";
	 deleted ++;
	 
      }
      else{
	 if(!isConjugatedVerb(word,rval[1])){
	    System.out.print(entries+1 + " " + word + " ");
	    newdata.put(word, "['"+ rval[0] +"','"+ rval[1] +"']");
	    System.out.println(rval[0] + rval[1]);
	    entries ++;
	  }else  deleted ++;
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
  
  static boolean isConjugatedVerb(String word, String pos) {
    
    if (pos.matches("^vb[a-z]( vb[a-z])*$")) {
	String sing = RiTa.singularize(word);
//	System.err.println("Special: "+pos+": "+word+"/"+sing);
	vbx += word + " ";
	return true;
    }
    return false;
  }
  
  }