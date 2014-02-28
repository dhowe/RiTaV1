// $Id: WordnetError.java,v 1.1 2013/10/12 12:04:34 dev Exp $

package rita.wordnet;

/**
 * Simple tagged RuntimeException for library errors
 * @invisible
 * @author dhowe
 * <p>See the accompanying documentation for license information
 */
public class RiWordNetError extends RuntimeException
{
  private static final String STATIC = "(static)";
  private static final String CLASS = "[CLASS]";
  private static final String ERROR = "[ERROR]";
  private static final String SPC = " ";
  public static final String QQ = "", CR = "\n"; // sys-prop?

  public RiWordNetError() { super(); }

  public RiWordNetError(String message)
  {
    super(tagMessage(null, message));
  }

  public RiWordNetError(Throwable cause)
  {
    super(cause);
  }
  
  public RiWordNetError(String message, Throwable cause)
  {
    super(tagMessage(null, message), cause);
  }
  
  public RiWordNetError(Object thrower, Throwable cause) 
  {
     super(tagMessage(thrower),cause); 
  }
  
  public RiWordNetError(Object thrower, String message, Throwable cause)
  {
    super(tagMessage(thrower, message), cause);
  }
  
  public RiWordNetError(Object thrower, String message)
  {
    super(tagMessage(thrower, message));
  }
  
  private static String tagMessage(Object thrower)
  {
    String msg = QQ;
    if (thrower != null) 
      msg += CR+CLASS+SPC+((thrower instanceof Class) 
      ? (thrower+SPC+STATIC) : (thrower.getClass()+QQ));
    //else msg += CR+CLASS+SPC+"unknown";      
    return msg;
  }
  
  private static String tagMessage(Object thrower, String msg)
  {
    if (msg.startsWith(ERROR+SPC))
      msg = msg.substring(ERROR.length()+1);// tmp   
    msg = CR+SPC+ERROR+SPC+msg+tagMessage(thrower);   
    return msg;
  }

  public static void main(String[] args)
  {
    throw new RiWordNetError("test error", new RuntimeException("root cause"));
  }

}// end
