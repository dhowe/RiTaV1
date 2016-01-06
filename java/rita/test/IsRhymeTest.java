package rita.test;

import static rita.support.QUnitStubs.*;

import org.junit.Test;

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
	"arrested", "contested",
	"fetid", "bedded",
	"fetid", "tepid" // ?
    };

    for (int i = 0; i < rhymes.length; i += 2) {
      System.out.println(rhymes[i] + " + "+rhymes[i+1]+" -> "+isRhyme(rhymes[i], rhymes[i+1]));
      ok(isRhyme(rhymes[i], rhymes[i+1]));
      
      ok(isRhyme(rhymes[i+1], rhymes[i])); // either way should be the same
    }
    
    String[] rhymeSet1 = new String[] { 
	"insincere", "persevere", "interfere",  // each should rhyme with the others
	"career",  "year", "reappear", "brigadier", "pioneer", "rear", "near",
	"beer", "fear", "sneer", "adhere", "veer", "volunteer", "pamphleteer",
	"sear", "sincere", "smear", "gear", "deer", "here", "queer", "dear",
	"financier", "cavalier", "rainier", "mutineer", "unclear", "racketeer",
	"disappear", "austere", "veneer", "overhear", "auctioneer", "spear",
	"pier", "sphere", "peer", "cashier", "ear", "sheer", "steer", "year",
	"hear", "souvenir", "frontier", "chandelier", "shear", "clear",  "mere",
	"premier", "rehear", "engineer", "premiere", "cheer", "appear", "severe",
    };

    for (int i = 0; i < rhymeSet1.length; i++) {
      for (int j = 0; j < rhymeSet1.length; j++) {
	if (i != j)
	  ok(isRhyme(rhymes[i], rhymes[j]));
	else
	  ok(!isRhyme(rhymes[i], rhymes[j]));
      }
    }
    
    String[] notRhymes = {
	"not", "rhyme",
	"deer", "dear",
	"candle", "candle" 
    };
    
    for (int i = 0; i < notRhymes.length; i += 2) {
      ok(!isRhyme(rhymes[i], rhymes[i+1]));
      ok(!isRhyme(rhymes[i+1], rhymes[i]));  // either way should be the same
    }
  }
  
  public boolean isRhyme(String a, String b) {
    
    // add implementation here
    
    return false;
  }

  public static void main(String[] args) {
    
    IsRhymeTest test = new IsRhymeTest();
    System.out.println(test.isRhyme("candle", "handle"));
  }
}
