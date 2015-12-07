package rita.test;

import static rita.support.QUnitStubs.equal;

import org.junit.Test;

import rita.support.Constants;
import rita.support.Phoneme;

public class PhonemeTests implements Constants
{
  @Test
  public void testArpaToIPA()
  {
    String[] tests = {
	"arpa1", "IPA1",
	"arpa2", "IPA2",
	"arpa3", "IPA3",
	// ...
    };
    for (int i = 0; i < tests.length; i += 2) {
      equal(Phoneme.arpaToIPA(tests[i]), tests[i+1]);
    }
  }
      
}
