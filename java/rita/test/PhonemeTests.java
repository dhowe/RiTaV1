package rita.test;

import static rita.support.QUnitStubs.equal;

import org.junit.Test;

import rita.RiTa;
import rita.support.Constants;
import rita.support.Phoneme;

public class PhonemeTests implements Constants
{
  @Test
  public void testArpaToIPA()
  {
    String[] tests = {
	"b ih k ah1 m", "bɪˈkʌm"
    };
	
    for (int i = 0; i < tests.length; i += 2) {
      
      // test with stresses
      String ipa = Phoneme.arpaToIPA(tests[i]);
      System.out.println("expected "+tests[i+1]+", got "+ipa);
      equal(tests[i+1], ipa);
      
      // test without stresses
      tests[i] = tests[i].replaceAll("[\\d]", "");
      tests[i+1] = tests[i+1].replaceAll(Phoneme.IPA_STRESS, "");
      ipa = Phoneme.arpaToIPA(tests[i]);
      System.out.println("expected "+tests[i+1]+", got "+ipa);
      equal(tests[i+1], ipa);
    }
  }

}
