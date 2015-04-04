package rita.support;

public interface RiProbable
{
  /**
   * Returns a probability value between 0 - 1
   */
  public float probability();
  
  /**
   * Returns the raw value from which probability will be calculated
   */
  public float rawValue();
}
