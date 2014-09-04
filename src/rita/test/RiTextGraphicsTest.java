package rita.test;

import org.junit.After;
import org.junit.Test;

import rita.RiText;
import rita.render.test.*;
import rita.render.test.misc.TextLayout;

public class RiTextGraphicsTest // TODO: how to run these on JS tests?
{
  static String testPath = "/src/rita/render/test";
  static String homeDir = System.getProperty("user.home");
  static String project = homeDir + "/Documents/eclipse-workspace/RiTa";

  public static final String PATH = project + "/src/";

  @After
  // run this after each test
  public void cleanup()
  {
    RiText.resetDefaults();
  }

  @Test
  public void testBoundingBoxAndAlpha()
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(BoundingBoxAndAlpha.class.getName());
  }

  @Test
  public void testBoundingBoxFeatures()
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(BoundingBoxFeatures.class.getName());
  }

  @Test
  public void testCreateLinesAndLayoutWithBreaks()
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(CreateLinesAndLayoutWithBreaks.class.getName());
  }
  
  @Test
  public void testDefaultFontAndFontVlwTtf()
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(DefaultFontAndFontVlwTtf.class.getName());
  }
  
  @Test
  public void testDisposeAll()
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(DisposeAll.class.getName());
  }

  @Test
  public void testSimplest()
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(Simplest.class.getName());
  }

  @Test
  public void testWordsLettersLines()
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.assertEqual(WordsLettersLines.class.getName());
  }

  public static void main(String[] args)
  {
    String clz = BoundingBoxAndAlpha.class.getName();
    PixelCompare pc = new PixelCompare(PATH);
    //pc.generateRefImage(clz);
    //pc.visualDiff(clz);
  }

}
