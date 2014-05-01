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
	public void testAlpha()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(Alpha.class.getName()); 
	}


	@Test
	public void testBoundingBoxes()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(BoundingBoxes.class.getName());
	}
	

	@Test
	public void testBoundingBoxFeatures()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(BoundingBoxFeatures.class.getName());
	}


	@Test
	public void testCreateLines()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(CreateLines.class.getName());
	}
	
	@Test
	public void testDefaultFont()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(DefaultFont.class.getName());
	}
	
	@Test
	public void testFontVlwTest()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(FontVlwTest.class.getName());
	}

	@Test
	public void testLayoutWithBreaks()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(LayoutWithBreaks.class.getName());
	}
	
	@Test
	public void testSimplest()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(Simplest.class.getName());
	}
	
	@Test
	public void testSplitText()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(SplitText.class.getName());
	}

	@Test
	public void testTextLayout()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(TextLayout.class.getName());
	}

	@Test
	public void testWordsLettersLines()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(WordsLettersLines.class.getName());
	}  

	public static void main(String[] args)
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.generateRefImage(BoundingBoxFeatures.class.getName());
		//pc.assertEqual(BoundingBoxes.class.getName());
	}

}
