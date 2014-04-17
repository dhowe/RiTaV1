package rita.render.test;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import rita.RiTaException;

public class SimplestTest extends Simplest
{
  public static final String refPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/rita/render/test/imageRefs/";
  public static final String tmpPath = "/tmp/";

  public void setup()
  {
    System.out.println("SimplestTest.setup()");
    super.setup();
    testAsImage();
  }

  public void testAsImage()
  {
    System.out.println("SimplestTest.testAsImage()");
    String imgName = getClass().getName() + ".png";
    File ref = new File(refPath + imgName);
    File test = new File(tmpPath + imgName);
    if (!ref.exists())
    {
      System.err.println("[WARN] No ref-image exists... writing one now");
      saveFrame(ref.getAbsolutePath());
      image(loadImage(ref.getAbsolutePath()), 0, 0);
      fill(200, 0, 0);
      text("[rendered]", 10, 20);
    }
    else
    {
      saveFrame(test.getAbsolutePath());
      compareImages(imgName, ref, test);
      System.exit(1);
    }
  }

  private static void compareImages(String imgName, File expected, File result)
  {
    System.out.println("Comparing:\n  " + expected + "\nvs.\n  " + result);

    BufferedImage actualImage = null;
    BufferedImage expectedImage = null;
    try
    {
      actualImage = ImageIO.read(result);
      expectedImage = ImageIO.read(expected);
    }
    catch (IOException e)
    {
      throw new RiTaException(e);
    }

    assertTrue("Images are not the same size", actualImage.getWidth() == expectedImage.getWidth()
        && actualImage.getHeight() == expectedImage.getHeight());

    boolean pixelsEqual = true;
    int x = 0;
    int y = 0;

    OUTER: for (y = 0; y < expectedImage.getHeight(); y++)
    {
      for (x = 0; x < expectedImage.getWidth(); x++)
      {
        int expectedPixel = expectedImage.getRGB(x, y);
        int actualPixel = actualImage.getRGB(x, y);
        if (actualPixel != expectedPixel)
        {
          pixelsEqual = false;
          break OUTER;
        }
      }
    }

    assertTrue("Images for " + imgName
        + " are not pixel-identical, first different pixel at: " + x + "," + y, pixelsEqual);
    
    System.out.println("Test passed.");
  }
}
