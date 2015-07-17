package rita.wordnet;

import rita.RiTa;

/**
 * Simple tagged RuntimeException for library errors
 */
public class RiWordNetError extends RuntimeException
{
  private static final String STATIC = "(static)";
  private static final String CLASS = "[CLASS]";
  private static final String ERROR = "[ERROR]", QQ = "";

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
      msg += RiTa.BN+CLASS+RiTa.SP+((thrower instanceof Class) 
      ? (thrower+RiTa.SP+STATIC) : (thrower.getClass()+QQ));
    //else msg += RiTa.BN+CLASS+RiTa.SP+"unknown";      
    return msg;
  }
  
  private static String tagMessage(Object thrower, String msg)
  {
    if (msg.startsWith(ERROR+RiTa.SP))
      msg = msg.substring(ERROR.length()+1);// tmp   
    msg = RiTa.BN+RiTa.SP+ERROR+RiTa.SP+msg+tagMessage(thrower);   
    return msg;
  }

  public static void main(String[] args)
  {
    throw new RiWordNetError("test error", new RuntimeException("root cause"));
  }

}// end
