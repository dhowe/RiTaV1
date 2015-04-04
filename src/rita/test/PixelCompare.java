package rita.test;

import static org.junit.Assert.assertTrue;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PImage;
import rita.*;
import rita.render.Defaults;

public class PixelCompare
{
  public static String refDirName = "imageRefs/";
  public static String tmpPath = "/tmp/" + refDirName;
  
  File refFile;
  PApplet applet;
  String testPath;
  int sketchWidth, sketchHeight;
  PImage expected, actual, diff;
    
  public PixelCompare(String testPath)
  {
    RiText.resetDefaults();
    this.testPath = testPath;
  }

  public boolean compare(String testClass) {
    return compare(testClass, 0);
  }

  public boolean compare(final String testClass, final float threshold) 
  {
    setReferenceFile(testClass);
    assertTrue("No ref image at " + refFile.getAbsolutePath(), existsRefImage());
    File actualFile = runSketch(testClass);
    return compareImages(refFile, actualFile, threshold);
  }

  private File runSketch(String testClass)
  {
    applet = startApplet(testClass);
    File actualFile = new File(tmpPath + testClass + ".png");
    applet.saveFrame(actualFile.getAbsolutePath());
    assertTrue("Unable to write tmp image at " + refFile.getAbsolutePath(), actualFile.exists());
    applet.noLoop();
    return actualFile;
  }
  
  public void generateRefImage(String testName)
  {
    setReferenceFile(testName);
    final String epath = refFile.getAbsolutePath();
    assertTrue("RefImage already exists: " + epath, !refFile.exists());
    this.applet = startApplet(testName, true); // move next line to UI
    this.applet.frame.setTitle("Press s to save as reference image");
    System.out.println("Press 's' to save this reference image to:\n  " + epath);
    this.applet.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e)
      {
        if (e.getKeyChar() == 's') {
          applet.saveFrame(epath);
          System.out.println("Wrote refImage: "+epath);
        }
    }});
    assertTrue("Unable to write refImage!", !refFile.exists());
  }
  
  private boolean existsRefImage()
  {
    return (refFile != null && refFile.exists());
  }
  
  private String getShortName(String testClass) {
    
    return testClass.substring(testClass.lastIndexOf('.')+1);
  }
  
  private String getPackage(String testClass) {

    return testClass.substring(0,testClass.lastIndexOf('.'));
  }
  
  private void setReferenceFile(String testClass) {
    
    String testName = getShortName(testClass);
    String testPkg = getPackage(testClass);
    String epath = testPath + testPkg.replaceAll("\\.", "\\/") + '/'+ refDirName;
    refFile = new File(epath + testName + ".png");
  }

  private PApplet startApplet(String testClass)
  {
    return startApplet(testClass, false);
  }
  
  private PApplet startApplet(String testClass, boolean visible)
  {
    String testName = getShortName(testClass);
    String testPkg = getPackage(testClass);
    PApplet papp = startApplet(testPkg, testName, visible);
    return papp;
  }
    
  private PApplet startApplet(String testPkg, String testName, boolean visible)
  {
    PApplet testApplet = null;
    try
    {
      Class clss = Class.forName(testPkg+"."+testName);
      testApplet = (PApplet) clss.newInstance();
      PApplet.runSketch(new String[] { testApplet.getClass().getName() }, testApplet);
      testApplet.frame.setVisible(visible);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return testApplet;
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
    
    BufferedImage resultImage = new BufferedImage
          (im2.getWidth(), im2.getHeight(), BufferedImage.TYPE_INT_ARGB);

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

        // euclidian distance to estimate the similarity
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
   
  boolean compareImages(File expectFile, File actualFile)
  {
      return compareImages(expectFile, actualFile, 0);
  }
    
  @SuppressWarnings("hiding")
  boolean compareImages(File refFile, File genFile, float threshold)
  {
    //System.out.println("Comparing:\n  " + expectFile + "\nvs.\n  " + actualFile);

    BufferedImage refImg = loadBImage(refFile);
    BufferedImage genImg = loadBImage(genFile);
    
    assertTrue("Reference image is null", refImg != null);
    assertTrue("Generated image is null", genImg != null);
    
    assertTrue("Images are not the same size, Reference: " + 
        refImg.getWidth() + "," + refImg.getHeight() + " Generated: " 
        + genImg.getWidth() + "," + genImg.getHeight() + " ["+genFile.getName()+"]", 
        genImg.getWidth() == refImg.getWidth() && genImg.getHeight() == refImg.getHeight());
    
    int diffpix = countDiff(refImg, genImg, threshold);
    
    return (diffpix == 0);
  }

  public void assertEqual(String testName)
  {
    this.assertEqual(testName, 0);
  }
    
  public void assertEqual(final String testName, final float threshold)
  {
    assertTrue("Image mismatch: "+testName,  compare(testName, threshold));
  }
        
  public void visualDiff(String testClass)
  {
    setReferenceFile(testClass);
    assertTrue("No ref image at " + refFile.getAbsolutePath(), existsRefImage());
    applet = startApplet(testClass);
    File actualFile = new File(tmpPath + testClass + ".png");
    applet.saveFrame(actualFile.getAbsolutePath());
    expected = applet.loadImage(refFile.getAbsolutePath());
    actual = applet.loadImage(actualFile.getAbsolutePath());
    
    /*assertTrue("Reference image is null", expected != null);
    assertTrue("Generated image is null", actual != null);
    
    assertTrue("Images are not the same size, Reference: " + 
        expected.width + "," + expected.height + " Generated: " 
        + actual.width + "," + actual.height + " ["+testClass+"]", 
        actual.width == expected.width && actual.height == expected.height);
    */
    diff = loadImageDiff(refFile, actualFile);
    //System.out.println((expected+"\n"+actual+"\n"+diff));
    sketchWidth = applet.width; 
    sketchHeight = applet.height;
    applet.frame.setVisible(false);
    DiffView viewer = createDiffView();
    PApplet.runSketch(new String[]{ viewer.getClass().getName() }, viewer);
  }
  
  protected DiffView createDiffView() {
    
    DiffView instance = null;
    try
    {
      Class<PixelCompare.DiffView> clazz = PixelCompare.DiffView.class;

      Constructor<PixelCompare.DiffView> ctor = clazz.getConstructor(getClass());
      instance = ctor.newInstance(this);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return instance;
  }
  
  class DiffView extends PApplet {
    
    public DiffView() {}
    
    public void setup()
    {
      super.setup();
      size(sketchWidth*3+20, sketchHeight+10);
    }
    
    public void draw() {

      background(200);
  
      image(expected, 5, 5);
      image(actual,     sketchWidth+10, 5);
      image(expected, 2*sketchWidth+15, 5);
      image(diff,     2*sketchWidth+15, 5);
      
      textInRect("expected", 5, 5);
      textInRect("received",sketchWidth+10, 5);
      textInRect("difference",2*sketchWidth+15, 5);   
    }
  
    void textInRect(String t, float x, float y)
    {
      noStroke();
      textSize(8);
      fill(230);
      rect(x, y, 50,10);
      fill(0);
      text(t, x+5, y+7);
    }   
  }
  
  public static void main(String[] args)
  {
    String testPath = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
    
    PixelCompare pat = new PixelCompare(testPath);
    pat.visualDiff("rita.render.test.Simplest");
  }

}
