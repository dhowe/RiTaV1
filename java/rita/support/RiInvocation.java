package rita.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import rita.RiTaException;

/**
 * A proxy invocation handler that enables dynamic typing for RiTa objects
 */
public class RiInvocation implements InvocationHandler 
{
  protected Object object;
  protected Class objectClass;
  
  protected RiInvocation(Object object) {
    this.object = object;
    this.objectClass = object.getClass();
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
  {    
    Method realMethod = null;
    try {
      realMethod = objectClass.getMethod(method.getName(), method.getParameterTypes());
      return realMethod.invoke(object, args);
    } 
    catch (Exception e) {
      String msg = null;
      if (e instanceof java.lang.IllegalAccessException)
        msg = "[ERROR] Make sure the class you are trying to "
          +"(dynamically) cast is publicly defined.\n          "
          +"----------------------------------------------------\n";
      throw new RiTaException(msg, e);        
    }     
  }

  /**
   * Causes object to implement the interface and returns an instance
   * of the object implementing interface even if
   * interface was not declared in object.getClass()'s implements
   * declaration.
   * 
   * This works as long as all methods declared in interface are
   * present in the object.
   * 
   * @param iface
   *          The Java class of the interface to implement
   * @param object
   *          The object to force to implement the interface
   *          
   * @return the object, but now implementing the interface
   */
  public static Object implement(Class iface, Object object) {
    return implement(new Class[] { iface }, new RiInvocation(object));
  }
  
  /**
   * Causes object to implement the listed interfaces. Is succesful
   * is all methods declared in the interfaces are present in the object.
   * 
   * @param ifaces
   *          an array of Java classes representing the interfaces to implement
   * @param object
   *          The object to force to implement the interfaces
   *          
   * @return the object, but now implementing the interfaces
   */
  static Object implement(Class[] ifaces, Object object) {
    Object o = Proxy.newProxyInstance
      (object.getClass().getClassLoader(), ifaces, new RiInvocation(object));
    //System.out.println("RiDynamicObject.implement() -> "+o);
    return o;
  }
  
}// end