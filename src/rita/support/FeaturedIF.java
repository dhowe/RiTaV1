package rita.support;

import java.util.Map;
import java.util.Set;

public interface FeaturedIF
{
  public abstract Map features();  

  public abstract Set getAvailableFeatures();

  public abstract String getFeature(String name);
  
  public abstract boolean hasFeature(String name);

  public abstract void setFeature(String name, String value);

  public abstract String text();
}