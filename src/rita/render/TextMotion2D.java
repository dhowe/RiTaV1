package rita.render;

import rita.*;
import rita.support.Interpolater;
import rita.support.RiInterpolater2D;

public class TextMotion2D extends InterpolatingBehavior 
{      
  private TextMotion2D(RiTextIF rt, float startTimeOffset, float duration) {
    super(rt, startTimeOffset, duration); 
  }
  
  public void update() 
  { 
    if (duration <= 0 || completed || isPaused())  
      return;
    
    /*// wiggle crap (tmp)
    if (wwyw > 0 && isWaiting()) {
      if (Math.random() < .333) {
        rt.x += Math.random() > .5 ? -.5 : .5;
        rt.y += .5;
        interpolater.setStart(rt.position());
      }
      return;
    }
    */
    if (interpolater.update()) // true if running 
    {
      if (!initd) {
        initd = true; // do once
        getStartValueFromParent(rt, interpolater);
      }
      updateParentValues(rt, interpolater.getValues());
    }
    checkForCompletion();
  }
  
  public TextMotion2D(RiTextIF rt, float[] targetXY, float duration) 
  {
    this(rt, targetXY, 0, duration);
  }
  
  public TextMotion2D(RiTextIF rt, float newX, float newY, float duration) 
  {
    this(rt, new float[]{newX, newY}, 0, duration);
  }

  public TextMotion2D(RiTextIF rt, float[] targetXY, float startTimeOffset, float duration) 
  {
    super(rt, startTimeOffset, duration); 
    this.interpolater = new RiInterpolater2D
      (rt.position(), targetXY, toOffsetMs(startOffset), (int)(duration*1000));  
    setMotionType(rt.motionType()); 
    setType(MOVE_TO);
  }
      
  public void updateParentValues(RiTextIF rxt, float[] values) {
     rxt.position(values[0], values[1]);
  }

  public void getStartValueFromParent(RiTextIF parent, Interpolater inter) {
    inter.setStart(parent.position());
  }

 /* public static void main(String[] args) throws InterruptedException
  {
    long startTime = System.currentTimeMillis();
    RiTextIF rt = new RiText(null, "hello", 500, 50);
    TextMotion2D e = new TextMotion2D(rt, new float[] { 0, 0 }, 2f);
    while (!e.isCompleted()) {    
      Thread.sleep(30);
      e.update();
      System.out.println(rt.x+","+rt.y);
    }   
    System.out.println(rt.x+","+rt.y+" "+RiTa.millis(startTime));  
  }*/

}// end

