package rita.test;

import static rita.support.QUnitStubs.*;

import java.util.HashMap;

import org.junit.Test;

import rita.RiLexicon;
import rita.RiTa;

public class IsConjugatedVerbTest {
  
  RiLexicon lex = new RiLexicon();
  
  @Test
  public void testIsConjugatedVerb() {
    
    String[] remove = { 
	"abounded", "vbd",
	"abounding", "vbg",
	"accentuated", "vbn vbd" ,
    };
    
    String[] keep = { 
	"abuse", "nn vb vbp" ,
	"accent", "nn vb" ,
	"accented", "vbn jj",
	"accents", "nns vbz" ,
	"accentuate", "vb" ,
	"accept", "vb vbp" ,
	"accounting", "nn vbg jj" ,
	"abused", "vbn jj vbd" ,
    };
    
 
    for (int i = 0; i < remove.length; i+=2) {
      System.out.println(remove[i] + ":" + remove[i+1] + " " + canRemove(remove[i] , remove[i+1]));
      ok( canRemove(remove[i] , remove[i+1]));
    }
    
    for (int i = 0; i < keep.length; i+=2) {
      System.out.println(keep[i] + ":" + keep[i+1] + " " + canRemove(keep[i] , keep[i+1]));
      ok( !canRemove(keep[i] , keep[i+1]));
    }
    
    //getvb tests
    
    String[] list = { 
	"abounded", "vbd",
	"abounding", "vbg",
	"abounds", "vbz",
    };
    
    for (int i = 0; i < list.length; i+=2) {
      String result = getvb(list[i],list[i+1]);
      System.out.println(list[i] + ":" + result);
      equal(result, "abound");
    }
    
    String[] list1 = { 
	"accompanying", "vbg jj",
	"accompanied", "vbn vbd",
	"accompanies", "vbz",
    };
    
    for (int i = 0; i < list1.length; i+=2) {
      String result = getvb(list1[i],list1[i+1]);
      System.out.println(list1[i] + ":" + result);
      equal(result, "accompany");
    }
    
    String[] list2 = { 
	"abmitted", "vbd vbn jj",
	"abmitting", "vbg",
    };
    
    for (int i = 0; i < list2.length; i+=2) {
      String result = getvb(list2[i],list2[i+1]);
      System.out.println(list2[i] + ":" + result);
      equal(result, "abmit");
    }
    
    String[] removeSet = { 
	"abound", "vb",
	"abounded", "vbd",
	"abounding", "vbg",
	"abounds", "vbz",
       };
    
    String[] removeSet1 = { 
	"accompany", "vb vbp",
	"accompanied", "vbn vbd",
	"accompanies", "vbz",
       };
    
    String[] keepSet = { 
	"abbetted", "vbn vbd",
	"abetting", "vbg",
       };
    
    for (int i = 2; i < removeSet.length; i+=2) {
      System.out.println(removeSet[i] + ":" + removeSet[i+1] + " " + canRemove(removeSet[i] , removeSet[i+1]));
      ok( canRemove(removeSet[i] , removeSet[i+1]));
    }
    
    for (int i = 2; i < removeSet1.length; i+=2) {
      System.out.println(removeSet1[i] + ":" + removeSet1[i+1] + " " + canRemove(removeSet1[i], removeSet1[i+1]));
      ok( canRemove(removeSet1[i], removeSet1[i+1]));
    }
    
    for (int i = 0; i < keepSet.length; i+=2) {
      System.out.println(keepSet[i] + ":" + keepSet[i+1] + " " + canRemove(keepSet[i] , keepSet[i+1]));
//      ok( !canRemove(keepSet[i] , keepSet[i+1]));
    }
    
  }

  public boolean canRemove(String word, String pos) {
    
    if(pos.matches("^vb[a-z]( vb[a-z])*$")) {
      String vb= getvb(word, pos); 
      if(lex.containsWord(vb)) return true;
      else{
	if(!vb.endsWith("e") && lex.containsWord(vb+"e")) return true;
      }
    }
    
    return false;
  }
  
  public String getvb(String word, String pos){
    String vb="";
    
    if (pos.contains("vbz")){
      if (word.endsWith("ies"))  vb = word.replaceAll("ies$", "y");
      else if (word.endsWith("es")) vb = word.substring(0, word.length()-2);
      else if (word.endsWith("s"))  vb = word.substring(0, word.length()-1);
    }
    
    if (pos.contains("vbn") || pos.matches("vbd")){
      if (word.endsWith("ied"))  vb = word.replaceAll("ied$", "y");
      else if (word.endsWith("ed")) vb = word.substring(0, word.length()-2);
    }
    
    if (pos.contains("vbg") && word.endsWith("ing"))
       vb = word.substring(0, word.length()-3);
   
    //deal with repeated ending
    if(vb != "" && vb.charAt(vb.length()-1) == vb.charAt(vb.length()-2))  vb = vb.substring(0, vb.length()-1);
    return vb;
  }
  
  public static void main(String[] args) {
    
    IsConjugatedVerbTest test = new IsConjugatedVerbTest();

  }
}
