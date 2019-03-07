package rita.render;

import rita.RiText;

// TODO: factor out this class
public abstract class TextMotion extends InterpolatingBehavior
{
  //public float wwyw = 0;  // wiggle while you wait (for NTM only) 
  
  public TextMotion(RiText rt, float startTimeOffset, float duration) {
    super(rt, startTimeOffset, duration); 
  }
  
  public void update() 
  { 
    if (duration <= 0 || completed || isPaused())  
      return;
    
    /*// wiggle crap (tmp)
    if (wwyw > 0 && isWaiting()) {
      if (RiTa.random() < .333) {
        rt.x += RiTa.random() > .5 ? -.5 : .5;
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
 
}// end
