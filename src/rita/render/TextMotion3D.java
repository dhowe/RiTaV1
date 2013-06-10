package rita.render;

import rita.RiTextIF;
import rita.support.Interpolater;
import rita.support.RiInterpolater3D;
import static rita.support.Constants.EventType.*;

public class TextMotion3D extends InterpolatingBehavior
{
  public TextMotion3D(RiTextIF rt, float x, float y, float z, float duration) {
    this(rt, new float[]{x,y,z}, 0, duration);
  }
  
  public TextMotion3D(RiTextIF rt, float[] targetXYZ, float duration) 
  {
    this(rt, targetXYZ, 0, duration);
  }

  public TextMotion3D(RiTextIF rt, float[] targetXYZ, float startTimeOffset, float duration) 
  {
    super(rt, startTimeOffset, duration); 
    this.interpolater = new RiInterpolater3D
      (rt.position(), targetXYZ, toOffsetMs(startOffset), (int)(duration*1000));  
    setMotionType(rt.motionType()); 
    setType(MoveTo);
  }

  public void getStartValueFromParent(RiTextIF parent, Interpolater interpolater) {
    interpolater.setStart(parent.position());
  }  
  
  public void updateParentValues(RiTextIF rt, float[] values) {
     rt.position(values[0], values[1], values[2]);
  }

}// end

