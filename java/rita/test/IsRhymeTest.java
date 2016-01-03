package rita.test;

import static rita.support.QUnitStubs.*;

import org.junit.Test;

public class IsRhymeTest {

  @Test
  public void testIsRhyme() {
    
    String[] rhymes = { 
	"candle", "handle", 
	"fat", "cat" 
    };
    

    for (int i = 0; i < rhymes.length; i += 2) {
      ok(isRhyme(rhymes[i], rhymes[i+1]));
    }
    
    String[] notRhymes = {
	"not", "rhyme",
	"candle", "candle" 
    };
    
    for (int i = 0; i < notRhymes.length; i += 2) {
      ok(!isRhyme(rhymes[i], rhymes[i+1]));
    }
  }
  
  public boolean isRhyme(String a, String b) {
    return false;
  }

  public static void main(String[] args) {
    
    IsRhymeTest test = new IsRhymeTest();
    System.out.println(test.isRhyme("candle", "handle"));
  }
}
