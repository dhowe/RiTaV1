package rita.render;

import rita.RiTextIF;
import rita.support.*;
import static rita.support.Constants.EventType.*;

public class ScaleBehavior extends InterpolatingBehavior
{
  public ScaleBehavior(RiTextIF rt, float scaleXYZ, float duration) {
    this(rt, new float[]{scaleXYZ,scaleXYZ,scaleXYZ}, 0, duration);
  }
  
  public ScaleBehavior(RiTextIF rt, float scaleXYZ, float startTimeOffset, float duration) {
    this(rt, new float[]{scaleXYZ, scaleXYZ, scaleXYZ}, startTimeOffset, duration);
  }
  
  public ScaleBehavior(RiTextIF rt, float[] scaleXYZ, float duration) 
  {
    this(rt, scaleXYZ, 0, duration);
  }

  public ScaleBehavior(RiTextIF rt, float[] scaleXYZ, float startTimeOffset, float duration) 
  {
    super(rt, startTimeOffset, duration); 
    this.setType(ScaleTo);
    this.interpolater = new RiInterpolater3D
      (rt.scale(), scaleXYZ, toOffsetMs(startOffset), (int)(duration*1000));  
  }
  
  public void getStartValueFromParent(RiTextIF parent, Interpolater interpolater) {
    //System.out.println("ScaleBehavior.getStartValueFromParent("+RiTa.asList(parent.getScale())+") @ "+RiTa.millis());
    interpolater.setStart(parent.scale());
  }  
  
  public void updateParentValues(RiTextIF rt, float[] values) {
     //System.out.println(this+".updateParentValues("+RiTa.asList(values)+")");
     rt.scale(values);
  }

}// end

