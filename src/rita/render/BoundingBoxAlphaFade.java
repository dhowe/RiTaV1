package rita.render;

import rita.RiTextIF;
import rita.support.*;
import static rita.support.Constants.EventType.*;

// fades fill and stroke of bounding box
public class BoundingBoxAlphaFade extends InterpolatingBehavior
{
  public BoundingBoxAlphaFade(RiTextIF rt, float newAlpha, float duration) {
    this(rt, newAlpha, 0, duration);
  }
  
  public BoundingBoxAlphaFade(RiTextIF rt, float newAlpha, float startTime, float duration) {
    super(rt, startTime, duration);    
    float[] fill = rt.boundingBoxFill();
    float[] stroke = rt.boundingBoxStroke();
    this.interpolater = new RiInterpolater2D(new float[] { fill[3], stroke[3] }, 
      new float[] { newAlpha, newAlpha }, toOffsetMs(startTime), toMs(duration));
    //System.out.println("BoundingBoxAlphaFade("+rt+", ["+fill[3]+","+stroke[3] +"], ["+newAlpha+","+newAlpha+"], "+startTime+", "+duration+")");
    this.type = BoundingAlpha;
  }
  
  public void getStartValueFromParent(RiTextIF parent, Interpolater interpolater) {
    float[] bbf = rt.boundingBoxFill();
    float[] bbs = rt.boundingBoxStroke();
    interpolater.setStart(new float[]{ bbf[3], bbs[3] });
  }  
  
  public void updateParentValues(RiTextIF rt, float[] values) 
  {
    //fill
    float[] bbf = rt.boundingBoxFill();
    bbf[3] = values[0];
    rt.boundingBoxFill(bbf);
    
    //stroke
    float[] bbs = rt.boundingBoxStroke();
    bbs[3] = values[0];
    rt.boundingBoxStroke(bbs);
  }

}// end

