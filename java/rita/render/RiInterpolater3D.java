package rita.render;

import rita.RiTa;

public class RiInterpolater3D extends RiInterpolater2D
{
  RiInterpolater z;  

/*  public RiAnimator3D(float[] initialXYZ, float[] targetXYZ, int duration) {
    this(initialXYZ[0], initialXYZ[1], initialXYZ[2],
         targetXYZ[0], targetXYZ[1], targetXYZ[2], 0, duration);
  }*/
  
  public RiInterpolater3D(float[] initialXYZ, float[] targetXYZ, int startTime, int duration) {
    super(initialXYZ, targetXYZ, startTime, duration);
    this.z = new RiInterpolater(initialXYZ[2], targetXYZ[2], startTime, duration);
  }
  
/*  public RiAnimator3D(float initialX, float initialY, float initialZ, float targetX, float targetY, float targetZ, int duration) {
    this(initialX, initialY, initialZ, targetX, targetY, targetZ, 0, duration);
  }*/
  
/*  public RiAnimator3D(float initialX, float initialY, float initialZ, float targetX, float targetY, float targetZ, int startTimeOffset, int duration) {
    super(initialX, initialY, targetX, targetY, startTimeOffset, duration);
    this.z = new RiAnimator(initialZ, targetZ, startTimeOffset, duration);
  }  */
  
  public void setStart(float[] startValues)
  {
    checkMinLen(3, startValues); 
    this.x.setStart(startValues[0]);  
    this.y.setStart(startValues[1]);     
    this.z.setStart(startValues[2]);
  }
  
  public void reset(float[] start, float[] target, int startOffsetMs, int durationMs)
  {
    checkMinLen(3, start, target);
    super.reset(start, target, startOffsetMs, durationMs);
    this.z.reset(start[2], target[2], startOffsetMs, durationMs);
  }
  
  public float[] getTarget() {
    return new float[] { x.getTargetValue(), y.getTargetValue(), z.getTargetValue() };
  }

  public boolean update()
  {
    boolean xy = super.update();
    boolean andZ = z.update();
    // no short-circuit
    return xy || andZ;
  }

  public final float z()
  {
    return z.getValue();
  }

  public void setMotionType(int motionType)
  {
     super.setMotionType(motionType);
     z.setMotionType(motionType);
  }
  
  public void stop()
  {
    super.stop();
    z.stop();
  }
  
  public void finish()
  {
    super.finish();
    z.finish();
  }
  
  private float[] values;
  public float[] getValues()
  {
    if (values==null) 
      values = new float[3];
    values[0] = x.getValue();
    values[1] = y.getValue();
    values[2] = z.getValue();
    return values;
  }

  public static void main(String[] args) throws InterruptedException
  {
    long startTime = System.currentTimeMillis();
    RiInterpolater3D e = new RiInterpolater3D(new float[]{  0,  0,   0 }, 
                                      new float[]{ 100, 50, 20 },
                                      500, 2000);
    for (int i = 0; i < 2; i++)
    {
      while (!e.isCompleted()) {    
        Thread.sleep(10);
        e.update();
        //System.out.println(RiTa.asList(e.getValues()));
      }   
      System.out.println(RiTa.asList(e.getValues())+" "+RiTa.millis(startTime));  
      if (i < 1) {
        Thread.sleep(1000);
        e.reset(new float[]{0, 0, 0}, new float[]{ 200, 300, 10 }, 500, 2000);
      }
    }
    // total should be ~6000
    System.out.println("TOTAL: "+(System.currentTimeMillis()-startTime));
  }
  
}// end 
