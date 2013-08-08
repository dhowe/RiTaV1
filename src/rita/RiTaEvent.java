package rita;

import java.lang.reflect.Method;
import java.util.*;

import rita.support.Constants;
import static rita.support.Constants.EventType.*;

public class RiTaEvent implements Constants
{
  static { RiTa.init(); }
  
  public EventType type;
  protected Object source;

  public RiTaEvent(Object source)
  {
    this(source, Unknown);
  }
  
  public RiTaEvent(Object source, EventType type)
  {
    this.source = source;
    this.type = type;//checkType(type);
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
    return "RiTaEvent[#"+this.hashCode()+" type="+
      type.name()+" src="+this.source.toString()+"]";
  }

/*  private static String typeToString(int type)
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
  */
  public Object source()
  {
    return source;
  }

  public String type()
  {
    return type.name();
  }
  
}
