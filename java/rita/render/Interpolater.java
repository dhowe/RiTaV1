package rita.render;

import rita.RiTa;

/**
 * Provides a variety of static interpolation methods each of which 
 * take 4 values: (millisSoFar, startValue, changeSoFar, totalDuration).
 */
public abstract class Interpolater
{
  public abstract boolean update();
  
  public abstract void stop();
  
  public abstract void finish();

  public abstract float[] getValues();
  
  public abstract float[] getTarget();

  public abstract void setStart(float[] f);
  
  //public abstract void reset(float durationSec);
  
  public abstract void reset
    (float[] start, float[] target, int startOffsetMs, int durationMs);
  
  public abstract void setMotionType(int motionType);
    
  public abstract boolean isCompleted();
  
  public abstract boolean isRunning();
  
  // these fast but fugly equations are from Penner -------------
  
  public static float linear(float t, float b, float c, float d) {
    return t * (c / d) + b;
  }  
    
  public static float easeInQuad(float t, float b, float c, float d) {
    return c * (t /= d) * t + b;
  }
  
  public static float easeOutQuad(float t, float b, float c, float d) {
    return -c * (t /= d) * (t - 2) + b;
  }
  
  public static float easeInOutQuad(float t, float b, float c, float d) {
    if ((t /= d / 2) < 1) return c /2 * t * t + b;
    return -c / 2 * ((--t) * (t - 2) - 1) + b;
  }
  
  public static float easeInCubic(float t, float b, float c, float d) {
    return (float)(c * Math.pow (t/d, 3) + b);
  }
  
  public static float easeOutCubic(float t, float b, float c, float d) {
    return (float)(c * (Math.pow (t/d-1, 3) + 1) + b);
  }
  
  public static float easeInOutCubic(float t, float b, float c, float d) {
    if ((t /= d / 2) < 1)
      return (float)(c / 2 * Math.pow (t, 3) + b);
    return (float)(c / 2 * (Math.pow (t-2, 3) + 2) + b);
  }
  
  public static float easeInQuart(float t, float b, float c, float d) {
    return (float)(c * Math.pow (t / d, 4) + b);
  }
  
  public static float easeOutQuart(float t, float b, float c, float d) {
    return (float)(-c * (Math.pow (t / d-1, 4) - 1) + b);
  }
  
  public static float easeInOutQuart(float t, float b, float c, float d) {
    if ((t /= d/2) < 1)
      return (float)(c / 2 * Math.pow (t, 4) + b);
    return (float)(-c / 2 * (Math.pow (t-2, 4) - 2) + b);
  }
  
  public static float easeInSine(float t, float b, float c, float d) {
    return (float) (c * (1 - Math.cos(t / d * (Math.PI / 2))) + b);
  }

  public static float easeOutSine(float t, float b, float c, float d) {
    return (float) (c * Math.sin(t / d * (Math.PI / 2)) + b);
  }

  public static float easeInOutSine(float t, float b, float c, float d) {
    return (float) (c / 2 * (1 - Math.cos(Math.PI * t / d)) + b);
  }

  public static float easeInCirc(float t, float b, float c, float d) {
    return (float) ( -c * (Math.sqrt(1 - (t /= d)*t) - 1) + b); // hmm??
  }

  public static float easeOutCirc(float t, float b, float c, float d) {
    return (float) (c * Math.sqrt(1 - (t-d )* (t-d) / (d*d)) + b);
  }

  public static float easeInOutCirc(float t, float b, float c, float d) {
    if (t < d / 2) 
      return (float)(-c / 2 * (Math.sqrt(1 - 4*t*t/(d*d)) - 1) + b);
    return (float)(c / 2 * (Math.sqrt(1 - 4*(t-d)*(t-d)/(d*d)) + 1) + b);
  }
  
  public static float easeInExpo(float t, float b, float c, float d) {
    int flip = 1;
    if (c < 0) {
      flip *= -1;
      c *= -1;
    }
    return (float)(flip * (Math.exp(Math.log(c)/d * t)) + b);
  }

  public static float easeOutExpo(float t, float b, float c, float d) {
    int flip = 1;
    if (c < 0) {
      flip *= -1;
      c *= -1;
    }
    return (float)(flip * (-Math.exp(-Math.log(c)/d * (t-d)) + c + 1) + b);
  }

  public static float easeInOutExpo(float t, float b, float c, float d) {
    int flip = 1;
    if (c < 0) {
      flip *= -1;
      c *= -1;
    }
    if (t < d/2) 
      return (float)(flip * (Math.exp(Math.log(c/2) / (d/2) * t)) + b);
    return (float)(flip * (-Math.exp(-2*Math.log(c/2) / d * (t-d)) + c + 1) + b);
  }
  
  protected static void checkMinLen(int minLength, float[] initial, float[] target)
  {
    if (initial.length < minLength || target.length < minLength)
      throw new IllegalArgumentException
        ("Expecting 2 arrays of size "+minLength+
         ", but found sizes: "+initial.length+" & "+target.length);
  }
  
  protected static void checkMinLen(int minLength, float[] data)
  {
    if (data.length < minLength)
      throw new IllegalArgumentException("Expecting array of size "
        + minLength + ", but instead found size: "+data.length);
  }
  
  public static void main(String[] args) throws InterruptedException
  {
    RiInterpolater e = new RiInterpolater(-1, 0, 1000, 2000);
   
    long startTime = System.currentTimeMillis();
   
    while (!e.isCompleted()) {    
      Thread.sleep(10);
      e.update();
      
      //System.out.println(e.getValues()[0]+" running="+e.isRunning());
    }   
    System.out.println("DONE: "+RiTa.asList(e.getValues())+" "+RiTa.millis(startTime));

    // total should be ~5000
    System.out.println("TOTAL: "+(System.currentTimeMillis()-startTime));
  }
  
}// end
