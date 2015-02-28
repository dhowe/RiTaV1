package rita.support;

import java.lang.reflect.Method;

import rita.RiTa;
import rita.RiTaException;

/**
 * Implements dynamic typing (eg 'duck-typing') for RiTa objects -- allowing programs to treat
 * objects from separate hierarchies and packages as the same, assuming they
 * implement the specified interfaces (checked only at runtime)
 * 
 * Example:
 * <pre>Class pclass = Class.forName("processing.core.PApplet");
        if (pclass.isInstance(duck)) {
          
          PAppletIF p = (PAppletIF)RiDynamic.cast(duck, PAppletIF.class);
          p.loadStrings(fileName);
        }
 *</pre>
 */
public class RiDynamic
{  
  protected Object delegate;

  /* public RiDynamic(Class dynamicProxyClass, Class iface) {
    this((Object)dynamicProxyClass, iface);
  }
  public RiDynamic(Object dynamicProxy, Class iface) {
    this(dynamicProxy, new Class[]{ iface });
  } */
  
  public RiDynamic(Class dynamicProxyClass, Class ... ifaces) {
    this((Object)dynamicProxyClass, ifaces);
  }

  public RiDynamic(Object dynamicProxy, Class ... ifaces) {
    dynamicProxy = multiGetClass(dynamicProxy);
    //System.out.println("RiDynamicObject.RiDynamicObject2("+dynamicProxy+","+RiTa.asList(ifaces)+")");
    for (int i = 0; i < ifaces.length; i++) {
      if (!instanceOf(ifaces[i], dynamicProxy)) 
        throw new RiTaException(dynamicProxy.getClass()
            +" cannot dynamically implement "+ifaces[i].getCanonicalName());
    }    
    delegate = RiInvocation.implement(ifaces, dynamicProxy);  
  } 

  /**
   * Indicates if an object is a Dynamic (DuckTyped) instance of an interface. 
   * 
   * @param iface The interface to implement
   * @param object The object to test
   * @return true if every method in the interface is present in the object, 
   *         otherwise false
   */
  public static boolean instanceOf(Class iface, Object object) {
    //System.out.println("RiDynamicType.instanceOf("+object+")"); 
    final Method[] methods = iface.getMethods();
    Class candClass = object.getClass();
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      //System.out.println(i+") "+methods[i]);
      try {
        candClass.getMethod(method.getName(), method.getParameterTypes());        
      } catch (NoSuchMethodException e) {
        //System.err.println("[ERROR] no match for method: "+method.getName()+"("+RiTa.asList(method.getParameterTypes())+");");
        return false;     
      } catch (Throwable e) {
        throw new RiTaException("Unexpected Exception! no match for method: "+method.getName()+"("+RiTa.asList(method.getParameterTypes())+");");
      }        
    }
    return true;
  }
  
  private static Object multiGetClass(Object o) {
    try {
      if (o instanceof Class) 
        return ((Class)o).newInstance();     
      return o;      
    } catch (Exception e) {
      throw new RiTaException(e);
    }   
  } 

  public Object getDelegate() {
    return delegate;
  }
  
  /**
   * Returns the passed in Object after dynamically casting
   * it to the specified interfaces.<br>
   * Note: 'toCast' must be a an instance of a public class.
   */
  public static Object cast(Object toCast, Class ... ifaces) {
    return new RiDynamic(toCast, ifaces).getDelegate();
  }
  
  public static void main(String[] args) {
    Object o = new Object() {
      public void run() {
        System.out.println("run() called");
      }
    };
    RiDynamic rdo = new RiDynamic(o, Runnable.class);
    Runnable r = (Runnable)rdo.delegate;
    r.run();
  }
}// end
