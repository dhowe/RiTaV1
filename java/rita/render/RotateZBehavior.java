package rita.render;

import static rita.support.Constants.EventType.RotateTo;
import rita.RiText;

public class RotateZBehavior extends InterpolatingBehavior
{
  public RotateZBehavior(RiText rt, float rotateZ, float duration) {
    this(rt, rotateZ, 0, duration);
  }
  
  public RotateZBehavior(RiText rt, float rotateZ, float startTimeOffset, float duration) {

    super(rt, startTimeOffset, duration); 
    this.setType(RotateTo);
    this.interpolater = new RiInterpolater
      (rt.rotateZ(), rotateZ, toOffsetMs(startOffset), (int)(duration*1000));  
  }
  
  public void getStartValueFromParent(RiText r, Interpolater interp) {
    //System.out.println("ScaleBehavior.getStartValueFromParent("+r.rotateZ()+") @ "+RiTa.millis());
    interp.setStart(new float[] { r.rotateZ() });
  }  
  
  public void updateParentValues(RiText theR, float[] values) {
    //System.out.println(this+".updateParentValues("+values[0]+")");
    theR.rotateZ(values[0]);
  }

}// end

