package rita.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ RiLexiconTest.class, RiStringTest.class,
    RiTextTest.class, RiTaTest.class, RiMarkovTest.class, RiGrammarTest.class,
    RiTaEventTest.class, RiWordNetTest.class, RiLexViaRiTa.class
// RiWordNetIteratorsTest.class, // TOO SLOW!
// KnownIssuesTest.class,
})
public class TestSuite {
  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(TestSuite.class);

    if (result.getFailures().size() == 0) {
      System.out.println("All tests successful !!!");
    } else {
      System.out.println("No. of failed test cases="
	  + result.getFailures().size());
      for (Failure failure : result.getFailures())
	System.out.println(failure.toString());
    }
  }

  public TestSuite() {
  }

}