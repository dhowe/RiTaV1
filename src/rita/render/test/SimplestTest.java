package rita.render.test;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import processing.core.PImage;
import rita.RiTa;
import rita.RiTaException;

public class SimplestTest extends Simplest
{
  public static final String refPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/rita/render/test/imageRefs/";
  public static final String tmpPath = "/tmp/";

  int owidth, oheight;
  boolean compareImages = false;
  PImage expected, actual, diff;

  public void setup()
  {
    super.setup();
    owidth = width;
    oheight = height;
    System.out.println("SimplestTest.setup() ");
    testAsImage();
  }

  public void draw()
  {
    if (compareImages) {
      renderDiffImages(owidth);
      return;
    }
    
    super.draw();
  }

  private void renderDiffImages(float sketchWidth)
  {
    background(200);

    image(expected, 5, 5);
    image(actual,     sketchWidth+10, 5);
    image(expected, 2*sketchWidth+15, 5);
    image(diff,     2*sketchWidth+15, 5);
    
    textInRect("expected", 5, 5);
    textInRect("received",sketchWidth+10, 5);
    textInRect("difference",2*sketchWidth+15, 5);   
  }

  private void textInRect(String t, float x, float y)
  {
    noStroke();
    textSize(8);
    fill(230);
    rect(x, y, 40,10);
    fill(0);
    text(t, x+5, y+7);
  }

  public void testAsImage()
  {
    System.out.println("SimplestTest.testAsImage()");
    String imgName = getClass().getName() + ".png";
    File exp = new File(refPath + imgName);
    File act = new File(tmpPath + imgName);
    if (!exp.exists())
    {
      System.err.println("[WARN] No ref-image exists... writing one now");
      String epath = exp.getAbsolutePath();
      saveFrame(epath);
      image(loadImage(epath), 0, 0);
      fill(200, 0, 0);
      text("[rendered]", 10, 20);
    }
    else
    {
      saveFrame(act.getAbsolutePath());
      boolean ok = compareImages(exp, act);
      if (!ok)
      {
        println("Images for " + imgName + " are not pixel-identical");
        // assertTrue("Images for " + imgName + " are not pixel-identical", ok);
        setSize(owidth*3+20, height+10);
        expected = loadImage(exp.getAbsolutePath());
        actual = loadImage(act.getAbsolutePath());
        diff = loadImageDiff(exp, act);
        compareImages = true;
      }
      else
      {
        System.out.println("Test passed.");
      }
    }
  }

  static PImage loadImageDiff(File expectedFile, File resultFile)
  {
    return new PImage(getImageDiff(expectedFile, resultFile));
  }
    
  static BufferedImage getImageDiff(File expectedFile, File resultFile)
  {
    BufferedImage actualImg = loadBImage(resultFile);
    BufferedImage expectImg = loadBImage(expectedFile);
    return getDifference(actualImg, expectImg);
  }
    
  static void writePNG(BufferedImage im, String path) {
    try
    {
      ImageIO.write(im, "PNG", new File(path));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  static BufferedImage getDifference(BufferedImage im1, BufferedImage im2)
  {
    return getDifference(im1, im2, 0);
  }
  
  static BufferedImage getDifference(BufferedImage im1, BufferedImage im2, double threshold)
  {
    int errc = RiTa.pack(255, 0, 255, 0); 
    BufferedImage resultImage = new BufferedImage(im1.getWidth(), im2.getHeight(), BufferedImage.TYPE_INT_ARGB);

    for (int h = 0; h < im1.getHeight(); h++)
    {
      for (int w = 0; w < im1.getWidth(); w++)
      {
        int rgb1   = im1.getRGB(w, h);
        int red1   = 0xff & (rgb1 >> 16);
        int green1 = 0xff & (rgb1 >> 8);
        int blue1  = 0xff & rgb1;

        int rgb2    = im2.getRGB(w, h);
        int red2    = 0xff & (rgb2 >> 16);
        int green2  = 0xff & (rgb2 >> 8);
        int blue2   = 0xff & rgb2;

        // euclidian distance to estimate the similarity
        double dist = Math.sqrt(Math.pow(red1 - red2, 2.0)
            + Math.pow(green1 - green2, 2.0)
            + Math.pow(blue1 - blue2, 2.0));
        
        int pix = (dist > threshold) ? errc : 0;
        resultImage.setRGB(w, h, pix);
        
      } // w
    } // h

    return resultImage;
  }

  static BufferedImage loadBImage(File resultFile)
  {
    try
    {
      return ImageIO.read(resultFile);
    }
    catch (IOException e)
    {
      throw new RiTaException(e);
    }
  }
  
  static int countDiff(BufferedImage im1, BufferedImage im2)
  {
    return countDiff(im1, im2, 0);
  }
  
  static int countDiff(BufferedImage im1, BufferedImage im2, double threshold)
  {
    int diffPix = 0;
    for (int h = 0; h < im1.getHeight(); h++)
    {
      for (int w = 0; w < im1.getWidth(); w++)
      {
        int rgb1   = im1.getRGB(w, h);
        int red1   = 0xff & (rgb1 >> 16);
        int green1 = 0xff & (rgb1 >> 8);
        int blue1  = 0xff & rgb1;

        int rgb2    = im2.getRGB(w, h);
        int red2    = 0xff & (rgb2 >> 16);
        int green2  = 0xff & (rgb2 >> 8);
        int blue2   = 0xff & rgb2;

        // euclidian distance to estimate the simil.
        double dist = Math.sqrt(Math.pow(red1 - red2, 2.0)
            + Math.pow(green1 - green2, 2.0)
            + Math.pow(blue1 - blue2, 2.0));
        
        if (dist > threshold)
        {
          diffPix++;
        }
        
      } // w
    } // h

    return diffPix;
  }
    
  static boolean compareImages(File expectedFile, File resultFile)
  {
    System.out.println("Comparing:\n  " + expectedFile + "\nvs.\n  " + resultFile);

    BufferedImage actualImg = loadBImage(resultFile);
    BufferedImage expectedImg = loadBImage(expectedFile);

    assertTrue("Images are not the same size", actualImg.getWidth() == expectedImg.getWidth()
        && actualImg.getHeight() == expectedImg.getHeight());

    int x = 0, y = 0, bad = 0;
    OUTER: for (y = 0; y < expectedImg.getHeight(); y++)
    {
      for (x = 0; x < expectedImg.getWidth(); x++)
      {
        int expectedPixel = expectedImg.getRGB(x, y);
        int actualPixel = actualImg.getRGB(x, y);
        if (actualPixel != expectedPixel)
        {
          bad++;
          break OUTER; // count the bad pixels? no
        }
      }
    }

    // assertTrue("Images for are not pixel-identical,"
    // +" first different pixel at: " + x + "," + y, bad==0);

    return bad == 0;
  }
}
