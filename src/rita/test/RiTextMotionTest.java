package rita.test;

import org.junit.After;
import org.junit.Test;

import rita.RiText;
import rita.render.test.*;


public class RiTextMotionTest // TODO: how to run these on JS tests?
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
	public void testColorAndFade()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(ColorAndFade.class.getName());
	}
	
	@Test
	public void testMotionTypes()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(MotionTypes.class.getName());

	}
	
	@Test
	public void testTranslateToAndBehaviors()
	{
		PixelCompare pc = new PixelCompare(PATH);
		pc.assertEqual(TranslateToAndBehaviors.class.getName());

	}
	
  public static void main(String[] args)
  {
    PixelCompare pc = new PixelCompare(PATH);
    pc.generateRefImage(MotionTypes.class.getName());
    //pc.assertEqual(Alpha.class.getName());
  }

}
