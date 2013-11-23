package rita;

// TODO: Remove?
public class RiTaException extends RuntimeException
{
  public RiTaException(String s)
  {
    super(s);
  }

  public RiTaException(Throwable e)
  {
    super(e);
  }
  
  public RiTaException(String s, Throwable e)
  {
    super(s, e);
  }

  public RiTaException()
  {
    super();
  }
}
