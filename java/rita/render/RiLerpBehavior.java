package rita.render;

import static rita.support.Constants.EventType.Lerp;
import rita.RiText;

public class RiLerpBehavior extends InterpolatingBehavior
{
  protected float value;

  public RiLerpBehavior(RiText rt, float start, float target, float duration) {
    this(rt, start, target, 0, duration);
  }
  
  public RiLerpBehavior(RiText rt, float start, float target, float delay, float duration) {
    super(rt, delay, duration);
    setType(Lerp);
    interpolater = new RiInterpolater(start, target, toOffsetMs(delay), toMs(duration));
    repeating = false;
  }
  
  public float getValue() {
    return value;
  }
  
  public void update() 
  {     
    if (duration <= 0 || completed || isPaused())   return;
    interpolater.update();
    value = interpolater.getValues()[0];    
    checkForCompletion();  
  }
  
  public void reset(float start, float target, float durationSec)
  {
    reset(start, target, 0, durationSec);
  }
    
  public void reset(float start, float target, float startOffsetSec, float durationSec)
  {
    resetTarget(new float[]{start}, new float[]{target}, startOffsetSec, durationSec);
  }
 
  public void getStartValueFromParent(RiText parent, Interpolater interpolater) {
    System.err.println("getStartValueFromParent: Invalid state!");
  }  
  
  public void updateParentValues(RiText rt, float[] values) {
    System.err.println("updateParentValues: Invalid state!");
  }
  
  public static void main(String[] args) throws InterruptedException
  {
    RiLerpBehavior e = new RiLerpBehavior(null, 1, 0, 2);
   
    long startTime = System.currentTimeMillis();
   
    while (!e.isCompleted()) {    
      e.update();
      System.out.println(e.getValue()+" running="+e.isRunning());
      Thread.sleep(10);
    }   
/*    System.out.println("DONE: "+e.getValue()+" "+RiTa.millis(startTime));

    // total should be ~5000
    System.out.println("TOTAL: "+(System.currentTimeMillis()-startTime));*/
  }

}// end

