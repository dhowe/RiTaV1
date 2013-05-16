package rita;

import java.lang.reflect.Method;
import java.util.*;

import rita.support.Constants;

public class RiTaEvent implements Constants
{
  // USE enum instead?
  private static final Map<Integer, String> TypeStrings = new HashMap<Integer, String>();
  
  static {
    // TODO: move to Constants?
    TypeStrings.put(RiTa.UNKNOWN,   "UNKNOWN");
    TypeStrings.put(RiTa.MOVE_TO,   "MOVE_TO");
    TypeStrings.put(RiTa.COLOR_TO,  "COLOR_TO");
    TypeStrings.put(RiTa.FADE_IN,   "FADE_IN");
    TypeStrings.put(RiTa.FADE_OUT,  "FADE_OUT");
    TypeStrings.put(RiTa.TEXT_TO,   "TEXT_TO");
    TypeStrings.put(RiTa.TIMER,     "TIMER");
    TypeStrings.put(RiTa.SCALE_TO,  "SCALE_TO");
    TypeStrings.put(RiTa.ROTATE_TO, "ROTATE_TO");
    TypeStrings.put(RiTa.COLOR_TO,  "COLOR_TO");
    TypeStrings.put(RiTa.TEXT_ENTERED, "TEXT_ENTERED");
    TypeStrings.put(RiTa.LERP,      "LERP");
  }
  
  
  public int type;
  protected Object source;

  public RiTaEvent(Object source)
  {
    this(source, RiTa.UNKNOWN);
  }
  
  public RiTaEvent(Object source, int type)
  {
    this.source = source;
    this.type = checkType(type);
  }
  
  public boolean fire(Object parent) {
    
    return this.fire(parent, false);
  }
  
  public boolean fire(Object parent, boolean isPublic) {
    
    // appears isPublic can always be false...
    
    Method callback = RiTa._findMethod
      (parent, DEFAULT_CALLBACK, new Class[] { RiTaEvent.class }, isPublic);
    
    return (callback != null) ? fire(parent, callback) : false;
  }
  
  public boolean fire(Object parent, Method callback) {
    
    if (parent != null && callback != null) {
      
      if (callback.getParameterTypes().length > 0)
        RiTa._invoke(parent, callback, new Object[]{ this });
      else 
        RiTa._invoke(parent, callback, new Object[0]);
      
      return true;
    }
    return false;
  }
  
  @Override
  public String toString()
  {
    return "RiTaEvent[#"+this.hashCode()+" type="+typeToString(type)+"("+type+") src="+this.source.toString()+"]";
  }

  private static String typeToString(int type)
  {
    String s = TypeStrings.get(type);
    return s != null ? s : TypeStrings.get(RiTa.UNKNOWN);
  }

  protected int checkType(int type)
  {
    boolean match = false; 
    for (Iterator<Integer> it = TypeStrings.keySet().iterator(); it.hasNext();)
    {
      if (it.next() == type) {
          match = true;
          break;
      }
    }  
    if (!match) 
      throw new RiTaException("Invalid Event Type: "+type); 
    return type;
  }
  
  public Object source()
  {
    return source;
  }

  public int type()
  {
    return type;
  }
  
}
