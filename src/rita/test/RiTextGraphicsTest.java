package rita.test;



import org.junit.Test;

public class RiTextGraphicsTest
{
	static String workingdirectory = System.getProperty("user.dir"); 

	//static final String PATH = "/Users/dhowe/Documents/eclipse-workspace/RiTa/src/";
	static final String PATH = workingdirectory +"/src/";

	@Test
	public void testBoundingBoxes()
	{
		String testName = "rita.render.test.BoundingBoxes";
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
	public void testDefaultFont()
	{
		String testName = "rita.render.test.DefaultFont";
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
	public void testLayoutWithBreaks()
	{
		String testName = "rita.render.test.LayoutWithBreaks";
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




	// continue with this pattern for all tests in package

	public static void main(String[] args)
	{
		String testName = "rita.render.test.BoundingBoxes";
		PixelCompare pc = new PixelCompare(PATH);
		pc.generateRefFile(testName);
		//pc.visualDiff(testName);


	}

}
