package rita.render;

import rita.RiTa;

public class RiInterpolater2D extends Interpolater
{
  RiInterpolater x, y;
  
  public RiInterpolater2D(float[] initial, float[] target, int startTime, int duration) {
    checkMinLen(2, initial, target);
    initInterpValues(initial[0], initial[1], target[0], target[1], startTime, duration);
  }
    
  private void initInterpValues(float initialX, float initialY, float targetX, float targetY, int startTime, int duration) {
    this.x = new RiInterpolater(initialX, targetX, startTime, duration);
    this.y = new RiInterpolater(initialY, targetY, startTime, duration);
  }
  
  public void setStart(float[] startValues)
  {
    checkMinLen(2, startValues); 
    this.x.setStart(startValues[0]);  
    this.y.setStart(startValues[1]);
  }
  
  public boolean update()
  {
    boolean xRunning = x.update(); 
    boolean yRunning = y.update();
    return xRunning || yRunning;
  }

  public final float getTargetX() {
    return x.getTargetValue();
  }

  public final float getTargetY() {
    return y.getTargetValue();
  }
  
  public float[] getTarget() {
    return new float[] {
      x.getTargetValue(), y.getTargetValue()
    };
  }

  public final float x()
  {
    return x.getValue();
  }

  public final float y()
  {
    return y.getValue();
  }
  
  public boolean isCompleted()
  {   
    boolean okX = x.isCompleted();
    boolean okY = y.isCompleted();
    boolean done = okY && okX;
    return done;
  }
  
  private float[] values = null;
  
  public float[] getValues()
  {
    if (values==null) values = new float[2];
    values[0] = x.getValue();
    values[1] = y.getValue();
    return values;
  }  

  public void finish()
  {
    x.finish();
    y.finish();
  }

  public void stop()
  {
    x.stop();
    y.stop();
  }

  public void setMotionType(int motionType)
  {
    x.setMotionType(motionType);
    y.setMotionType(motionType);
  }
    
  public void reset(float[] start, float[] target, int startOffsetMs, int durationMs)
  {
    stop();
    checkMinLen(2, start, target);
    this.x.reset(start[0], target[0], startOffsetMs, durationMs);
    this.y.reset(start[1], target[1], startOffsetMs, durationMs);
  }
  
  public boolean isRunning()
  {
    return x.isRunning() || y.isRunning();
  }

  public static void main(String[] args) throws InterruptedException
  {
    Interpolater e = new RiInterpolater2D
      (new float[]{0, 200}, new float[]{100, 200}, 0, 2000);
    
    Interpolater f = new RiInterpolater2D
      (new float[]{100, 200}, new float[]{0, 200}, 3000, 2000);
    
    long startTime = System.currentTimeMillis();
   
    boolean completed = false;
    while (!completed) {    
      Thread.sleep(10);
      boolean eDone = e.update();
      boolean fDone = f.update();
      completed = eDone && fDone;
      System.out.println(e.getValues()[0]+" :: "+f.getValues()[0]+" running="+e.isRunning());
    }   
    System.out.println("DONE: "+RiTa.asList(e.getValues())+" "+RiTa.millis(startTime));

    // total should be ~5000
    System.out.println("TOTAL: "+(System.currentTimeMillis()-startTime));
  }

}// end