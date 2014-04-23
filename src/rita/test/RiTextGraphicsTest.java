package rita.test;

import org.junit.Test;

import rita.render.test.PixelCompare;

public class RiTextGraphicsTest
{
  static final String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";

  @Test
  public void testSimplest()
  {
    String testName = "rita.render.test.Simplest";
    PixelCompare pc = new PixelCompare(testPath);
    pc.assertEqual(testName);
    //pc.generateRefFile(testName);
  }
  
  public static void main(String[] args)
  {
    PixelCompare pc = new PixelCompare(testPath);
    pc.visualDiff("rita.render.test.Simplest");
  }

}
