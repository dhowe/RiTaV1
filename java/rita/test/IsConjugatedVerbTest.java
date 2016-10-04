package rita.test;

import static rita.support.QUnitStubs.*;

import org.junit.Test;

import rita.RiLexicon;

public class IsConjugatedVerbTest {

  @Test
  public void testIsConjugatedVerb() {
    
    String[] remove = { 
	"abounded", "vbd",
	"abounding", "vbg",
	"abetted", "vbn vbd",
	"abetting", "vbg",
	"abused", "vbn jj vbd" ,
	"accentuated", "vbn vbd" ,
    };
    
    String[] keep = { 
	"abuse", "nn vb vbp" ,
	"accent", "nn vb" ,
	"accented", "vbn jj" ,
	"accents", "nns vbz" ,
	"accentuate", "vb" ,
	"accept", "vb vbp" ,
	"accounting", "nn vbg jj" ,
    };

    for (int i = 0; i < remove.length; i+=2) {

      ok( canRemove(remove[i] , remove[i+1]));
    }
    
    for (int i = 0; i < keep.length; i+=2) {

      ok( !canRemove(keep[i] , keep[i+1]));
    }
  }

  public boolean canRemove(String string, String string2) {

    // add code here
    
    return false;
  }

  public static void main(String[] args) {

    IsConjugatedVerbTest test = new IsConjugatedVerbTest();

  }
}
