package rita.test;

import static rita.support.QUnitStubs.*;

import org.junit.Test;
import rita.RiLexicon;

public class IsRhymeTest {

  @Test
  public void testIsRhyme() {
    
    String[] rhymes = { 
	"candle", "handle", 
	"fat", "cat",
	"apple", "grapple",
	"apple", "chapel",
	"libel", "tribal",
	"bugle", "frugal",
	"arrested", "contested"
//	"fetid", "bedded",
//	"fetid", "tepid" // ?
    };

    for (int i = 0; i < rhymes.length; i += 2) {
      System.out.println(rhymes[i] + " + "+rhymes[i+1]+" -> "+isRhyme(rhymes[i], rhymes[i+1]));
      ok(isRhyme(rhymes[i], rhymes[i+1]), rhymes[i]+"/"+rhymes[i+1]);
      ok(isRhyme(rhymes[i+1], rhymes[i]), rhymes[i+1]+"/"+rhymes[i]);
    }
    
    String[] rhymeSet1 = new String[] { 
	"insincere", "persevere", "interfere",  // each should rhyme with the others
	"career",  "year", "reappear", "brigadier", "pioneer", "rear", "near",
	"beer", "fear", "sneer", "adhere", "veer", "volunteer", "pamphleteer",
	"sear", "sincere", "smear", "gear", "deer", "here", "queer",
	"financier", "cavalier", "rainier", "mutineer", "unclear", "racketeer",
	"disappear", "austere", "veneer", "overhear", "auctioneer", "spear",
	"pier", "sphere", "cashier", "ear", "steer",
	 "souvenir", "frontier", "chandelier", "shear", "clear",  "mere",
	"premier", "rehear", "engineer", "cheer", "appear", "severe",
    };

    for (int i = 0; i < rhymeSet1.length-1; i++) {
      for (int j = 0; j < rhymeSet1.length; j++) {
	
	if (i != j){
	  System.out.println(rhymeSet1[i] + " + "+rhymeSet1[j]+" -> "+isRhyme(rhymeSet1[i], rhymeSet1[j]));
	  ok(isRhyme(rhymeSet1[i], rhymeSet1[j]));
	}
	else
	  ok(!isRhyme(rhymeSet1[i], rhymeSet1[j]));
      }
    }
    
    String[] notRhymes = {
	"not", "rhyme",
	"deer", "dear",
	"candle", "candle" ,
	"hear","here",
	"premiere","premier",
	"peer","pear",
	"sheer","shear"
    };
    
    for (int i = 0; i < notRhymes.length; i += 2) {
      System.out.println(notRhymes[i] + " + "+notRhymes[i+1]+" -> "+isRhyme(notRhymes[i], notRhymes[i+1]));
      ok(!isRhyme(notRhymes[i], notRhymes[i+1]));
      ok(!isRhyme(notRhymes[i+1], notRhymes[i]));  // either way should be the same
    }
  }
  
  public boolean isRhyme(String a, String b) {
    
    RiLexicon lex = new RiLexicon();
    
    boolean res = lex.isRhyme(a,b,true);

    return res;
  }

  public static void main(String[] args) {
    
    IsRhymeTest test = new IsRhymeTest();
    
  }
}
