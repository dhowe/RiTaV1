package rita.render;

import rita.RiText;
import rita.support.*;
import static rita.support.Constants.EventType.*;

// fades fill and stroke of bounding box
public class BoundingBoxAlphaFade extends InterpolatingBehavior
{
  public BoundingBoxAlphaFade(RiText rt, float newAlpha, float duration) {
    this(rt, newAlpha, 0, duration);
  }
  
  public BoundingBoxAlphaFade(RiText rt, float newAlpha, float startTime, float duration) {
    super(rt, startTime, duration);    
    float[] fill = rt.boundingFill();
    float[] stroke = rt.boundingStroke();
    this.interpolater = new RiInterpolater2D(new float[] { fill[3], stroke[3] }, 
      new float[] { newAlpha, newAlpha }, toOffsetMs(startTime), toMs(duration));
    //System.out.println("BoundingBoxAlphaFade("+rt+", ["+fill[3]+","+stroke[3] +"], ["+newAlpha+","+newAlpha+"], "+startTime+", "+duration+")");
    this.type = BoundingAlpha;
  }
  
  public void getStartValueFromParent(RiText parent, Interpolater interp) {
    float[] bbf = rt.boundingFill();
    float[] bbs = rt.boundingStroke();
    interp.setStart(new float[]{ bbf[3], bbs[3] });
  }  
  
  public void updateParentValues(RiText rt, float[] values) 
  {
    //fill
    float[] bbf = rt.boundingFill();
    bbf[3] = values[0];
    rt.boundingFill(bbf);
    
    //stroke
    float[] bbs = rt.boundingStroke();
    //bbs[3] = values[0];
    rt.boundingStroke(bbs);
  }

}// end

