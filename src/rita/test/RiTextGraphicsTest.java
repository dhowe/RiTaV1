package rita.test;

import org.junit.Test;

public class RiTextGraphicsTest
{
  static final String PATH = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";

  @Test
  public void testSimplest()
  {
    String testName = "rita.render.test.Simplest";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }
  
    @Test
  public void testBoundingBoxes()
  {
    String testName = "rita.render.test.BoundingBoxes";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }
    
  // continue with this pattern for all tests in package
  
  public static void main(String[] args)
  {
    String testName = "rita.render.test.Simplest";
    PixelCompare pc = new PixelCompare(PATH);
    //pc.generateRefFile(testName);
    pc.visualDiff(testName);
  }

}
