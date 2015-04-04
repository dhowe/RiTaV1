package rita.support;

import java.util.Map;
import java.util.Set;

public interface FeaturedIF
{
  public abstract Map<String,String> features();  

  public abstract Set<String> getAvailableFeatures();

  public abstract String getFeature(String name);
  
  public abstract boolean hasFeature(String name);

  public abstract void setFeature(String name, String value);

  public abstract String text();
}