package rita.docgen;

import java.io.FileWriter;
import java.util.*;

import rita.RiTa;

public class DictFromCMU {
  
  static String DICT_FILE = "js/src/rita_dict.js";
  static String SYLL_FILE = "js/src/cmudict-0.6syll";
  static String OUTPUT_FILE = "/tmp/rita_newerdict.js"; // change me
  
  static String keepers = "|earful|schoolgirl|sugarcane|outdoorsman|exorcism|gigolo|google|";
  static String header, footer;
  static int pruned = 0;
  
  public static void main(String[] args) {
    
    HashMap rdata = parseRiTaDict(RiTa.loadStrings(DICT_FILE));
    SortedMap newdata = generate(rdata);
    
    System.out.println("Pruning : "+pruned);
    System.out.println("Wrote: "+writeToFile(OUTPUT_FILE, mapToString(newdata)));
  }

  private static SortedMap generate(HashMap rdata) {
    
    String[] cwords = RiTa.loadStrings(SYLL_FILE);    
    HashMap<String, String> cdata = parseCMU(cwords);
    SortedMap<String, String> newdata = new TreeMap<String, String>();
    
    for (Iterator<String> it = rdata.keySet().iterator(); it.hasNext();) {
      
      String word = it.next();
      String[] rval = (String[]) rdata.get(word);
      String ritaPhones = rval[0], ritaPos = rval[1];
      String cmuPhones = cdata.get(word);
      
      // remove words not in CMU, unless in keepers
      if (cmuPhones == null) {
	if (!keepers.contains("|"+word+"|")) 
	  continue;	  
	
	System.out.println("KEEP(missing): "+word);
	cmuPhones = ritaPhones;
      }
      
      if (isPluralNounWithDifferentSingular(rdata, word, ritaPos)) {
	continue;
      }
      
      // Strip non-primary-stresses and add to hash
      cmuPhones = cmuPhones.replaceAll("[02]", "");
      newdata.put(word, "['"+cmuPhones+"','"+ritaPos+"']");
      
      //System.out.println("'"+word+"':['"+cmuPhones+"','"+ritaPos+"'],");
    }    
    return newdata;
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
    
    if (pos.matches("^vbz")) {  
      // 
    }
    
    if (pos.matches("^vb[a-z]( vb[a-z])*$")) {
	String sing = RiTa.singularize(word);
	if (word.endsWith("s")) 
	  sing = word.replaceAll("s$", "");
	if (rdata.containsKey(sing)) {
	  //System.out.println("PRUNE: "+ritaPos+": "+word);
	  pruned++;
	}
	else {
	  System.err.println("KEEP: "+pos+": "+word+"/"+sing);
	}
    }
    return false;
  }
  
  static boolean isPluralNounWithDifferentSingular(HashMap rdata, String word, String pos) {
    
    if (pos.equals("nns")) {
      
	String singular = RiTa.singularize(word);
	String[] posTags = (String[]) rdata.get(singular);
	  
	// only if the singular is a different entry in the dict 
	if (!word.equals(singular) && rdata.containsKey(singular)) {
	 
	  // only if it isn't also a verb
	  if (!(posTags[1].contains("vb ") || posTags[1].endsWith("vb"))) {
	    return true;
	  }
	  //else System.out.println("KEEP(nns): "+word+" / "+singular+" -> "+ posTags[1]);
	}
	else {
	  
	  pruned++;
	  System.out.println("REMOVE(nns): "+word);
	}
    }    
    return false;
  }

  private static HashMap<String, String> parseCMU(String[] words) {
    
    HashMap<String, String> cmu = new HashMap<String, String>();
    for (int i = 0; i < words.length; i++) {
      String[] parts = words[i].toLowerCase().trim().split("  +");
      if (parts.length != 2)
	throw new RuntimeException("Bad line: "+words[i]);
      String word = parts[0].trim(), value = parts[1].trim();
      String sylls = value.replaceAll(" - ", "/").replaceAll(" ", "-").replaceAll("/", " ");
      cmu.put(word, sylls);
    }
    return cmu;
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
}
