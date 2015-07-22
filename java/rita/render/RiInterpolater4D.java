package rita.render;

import rita.RiTa;

/**
 * A 4d interpolater (e.g., for color, etc)
 */
public class RiInterpolater4D extends RiInterpolater3D
{
  RiInterpolater a;  

  public RiInterpolater4D(float[] initial, float[] target, int startTime, int duration) {
    super(initial, target, startTime, duration);
    this.a = new RiInterpolater(initial[3], target[3], startTime, duration);
  }

  public void reset(float[] initial, float[] target, int startOffsetMs, int durationMs)
  {
    checkMinLen(4, initial, target);
    super.reset(initial, target, startOffsetMs, durationMs);
    this.a.reset(initial[3], target[3], startOffsetMs, durationMs);
  }
  
  public void setStart(float[] startValues)
  {
    super.setStart(startValues);
    if (startValues.length != 4) 
      throw new IllegalArgumentException("Expected array size=4, " +
        "but found size="+startValues.length+"!!");
    this.x.setStart(startValues[0]);  
    this.y.setStart(startValues[1]);     
    this.z.setStart(startValues[2]);
    this.a.setStart(startValues[3]);
  }

  public boolean update()
  {
    boolean xyz = super.update();
    boolean andA = a.update(); 
    // no short-circuit
    return xyz || andA;
  }

  public final float r(){
    return x.getValue();
  }
  
  public final float g(){
    return y.getValue();
  }
  
  public final float b(){
    return z.getValue();
  }
  
  public final float a(){
    return a.getValue();
  }

  public void setMotionType(int motionType){
     super.setMotionType(motionType);
     a.setMotionType(motionType);
  }
  
  public float[] getTarget() {
    return new float[] { x.getTargetValue(), y.getTargetValue(), z.getTargetValue(), a.getTargetValue() };
  }
  
  private float[] values;
  public float[] getValues()
  {
    if (values==null)
      values = new float[4];
    values[0] = x.getValue();
    values[1] = y.getValue();
    values[2] = z.getValue();
    values[3] = a.getValue();
    return values;
  }
  
  public void stop()
  {
    super.stop();
    a.stop();
  }
  
  public void finish()
  {
    super.finish();
    a.finish();
  }
  
  public static void main(String[] args) throws InterruptedException
  {
    float[] start = {255, 0, 0, 0};
    float[] targ  = {0, 50, 210, 255,};
    RiInterpolater4D e = new RiInterpolater4D(start, targ,  500, 2000);
    long startTime = System.currentTimeMillis();
    for (int j = 0; j < 2; j++)
    {
      while (!e.isCompleted()) {    
        Thread.sleep(10);
        e.update();
        //System.out.println(RiTa.asList(e.getValues()));
      }   
      System.out.println(RiTa.asList(e.getValues())+" "+RiTa.millis(startTime));  
      if (j < 1) {
        Thread.sleep(1000);
        e.reset(new float[]{0, 0, 0, 0}, new float[]{ 200, 300, 10, 5000 }, 500, 2000);
      }       
    }
    // total should be ~6000
    System.out.println("TOTAL: "+(System.currentTimeMillis()-startTime));
  }
  
}// end 
