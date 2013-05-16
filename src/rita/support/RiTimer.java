package rita.support;

import java.lang.reflect.Method;
import java.util.*;

import rita.*;

/**
 * A basic timer implementation to which one can pass
 * a PApplet, a RiTaEventListener, or any other object
 * that implements the method: onRiTaEvent(RiTaEvent re) 
 * <p>
 * Note: uses dynamic casting via the RiDynamicType object.
 * <p>A typical use in Processing might be:<pre>
    void setup(RiTaEvent re)
    {
      new RiTimer(this, 1f);
      
        OR
        
      RiTimer.start(this, 1f);  
    }
    
    public void onRiTaEvent(RiTaEvent re)
    {
      // called every 1 second
    }
    </pre>
    or, if (outside of Processing) onRiTaEvent(re) was in another class (e.g., MyApplet):<pre>
    public class MyApplet extends Applet 
    {
      RiTimer timer;
      public void init()
      {
        timer = new RiTimer(this, 1f);
      }
      
      public void onRiTaEvent(RiTaEvent re)
      {
        // called every 1 second
      }
    } </pre>   
 * @author dhowe
 */
public class RiTimer implements Constants
{
  protected static List<RiTimer> timers = new ArrayList<RiTimer>();

  protected static int idGen = 0;
   
  protected Timer internalTimer;
  protected boolean paused, isDaemon;
  protected float period;
  protected Object parent;
  protected Method callback;
  private int id;
    
  public RiTimer(Object parent, float period) {
    
    this(parent, period, null);
  }
  
  public RiTimer(Object parent, float period, String callbackName) {
    
    this.parent = parent;
    this.period = period;
    this.id = ++idGen;
    this.callback = findCallback(callbackName);
    init(0, period);
    timers.add(this);
  }

  public Method findCallback(String callbackName)
  {
    try
    {
      return (callbackName == null) ? 
         RiTa._findMethod(parent, DEFAULT_CALLBACK, new Class[] { RiTaEvent.class }, false)
         : RiTa._findMethod(parent, callbackName, new Class[] {}, false);
    }
    catch (RiTaException e)
    {
      System.err.println("[WARN] Expected callback not found: "
          + RiTa.shortName(parent)+"."+DEFAULT_CALLBACK+"();");
      return null;
    }
  }
  
  public String toString()
  {
    return "RiTimer#"+id;
  }
    
  private void init(float startOffset, float thePeriod)
  {
    final RiTimer rt = this;
    (internalTimer = new Timer(isDaemon)).schedule(new TimerTask()
    {
      public void run()
      {
        new RiTaEvent(rt, RiTa.TIMER).fire(parent, callback);
      }
    }, (long) (Math.max(0, startOffset * 1000)), (long) (thePeriod * 1000));
  }

  public void stop() {
    internalTimer.cancel();
  }

  public RiTimer pause(boolean b) {
    if (b) {
      internalTimer.cancel();
    }
    else {
      init(0, period);  
    }
    return this;
  }

  public void pauseFor(float seconds) {
    internalTimer.cancel();
    init(seconds, period);
  }
    
  public int id()
  {
    return id;
  }
  
  public static RiTimer findById(int id)
  {
    for (Iterator<RiTimer>it = timers.iterator(); it.hasNext();)
    {
      RiTimer rt = it.next();
      if (rt.id() == id)
        return rt;
    }
    return null;
  }

  public static void main(String[] args) throws InterruptedException
  {
    RiTimer rt = new RiTimer(new Object() {
      public void onRiTaEvent(RiTaEvent rte) {
        System.out.println(rte.source()+" :: "+System.currentTimeMillis()/1000);
      }
    }, 1);
    System.out.println(rt.id());
    RiTimer rt2 = new RiTimer(new Object() {
      public void dynFun() {
        System.out.println(System.currentTimeMillis()/1000);
      }
    }, 1, "dynFun");
    System.out.println(rt2);

  }
  
}// end
