package rita.render;

import rita.RiText;

public class TextColorFade extends InterpolatingBehavior
{
  public TextColorFade(RiText rt, float[] colors, float duration) {
    this(rt, colors, 0, duration);
  }
  
  public TextColorFade(RiText rt, float[] colors, float startTime, float duration) {
    
    super(rt, startTime, duration);
    this.interpolater = new RiInterpolater4D
      (rt.fill(), fillColorArray(colors), toOffsetMs(startTime), toMs(duration));
  }

  float[] fillColorArray(float[] colors)
  {
    float[] full = new float[4];
    
    for (int i = 0; i < colors.length; i++)
      full[i] = colors[i];
    
    switch (colors.length) {
      
      case 3: 
        full[3] = rt.alpha(); // DCH: changed from 255 ?
        break;
        
      case 2:
        full[1] = colors[0];
        full[2] = colors[0];
        full[3] = colors[1];
        break;
        
      case 1:
        full[1] = colors[0];
        full[2] = colors[0];
        full[3] = rt.alpha(); // DCH: changed from 255 ?
        break;
    }
    
    return full;
  }

  public void getStartValueFromParent(RiText parent, Interpolater interp) {
    
    interp.setStart(parent.fill());
  }  
  
  public void updateParentValues(RiText r, float[] values) {
    
     r.fill(values);
  }

}// end

