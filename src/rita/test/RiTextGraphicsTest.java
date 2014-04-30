package rita.test;

import org.junit.After;
import org.junit.Test;

import rita.RiText;
import rita.render.test.*;

public class RiTextGraphicsTest // TODO: how to run these on JS tests?
{
  static String testPath = "/src/rita/render/test";
  static String homeDir = System.getProperty("user.home");
  static String project = homeDir + "/Documents/eclipse-workspace/RiTa";

  public static final String PATH = project + "/src/";

  @After // run this after each test
  public void cleanup()
  {
    RiText.resetDefaults();
  }

  @Test
  public void testSplitText()
  {
    String testName = "rita.render.test.SplitText";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }

  @Test
  public void testTextLayout()
  {
    String testName = "rita.render.test.TextLayout";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }

  @Test
  public void testWordsLettersLines()
  {
    String testName = "rita.render.test.WordsLettersLines";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }  
  @Test
  public void testFontVlwTest()
  {
    String testName = "rita.render.test.FontVlwTest";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }
  
  @Test
  public void testDefaultFont()
  {
    String testName = "rita.render.test.DefaultFont";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }
    
  @Test
  public void testLayoutWithBreaks()
  {
    String testName = "rita.render.test.LayoutWithBreaks";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }
    
  @Test
  public void testCreateLines()
  {
    String testName = "rita.render.test.CreateLines";
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

  @Test
  public void testSimplest()
  {
    String testName = "rita.render.test.Simplest";
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(testName);
  }
  
  @Test
  public void testAlpha()
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(Alpha.class.getName()); 
    // TODO: (Kenny) better to specify class names like this from now on 
  }


  public static void main(String[] args)
  {
    PixelCompare pc = new PixelCompare(PATH);
    //pc.generateRefImage(Alpha.class.getName());
    pc.visualDiff(Simplest.class.getName());
  }

}
