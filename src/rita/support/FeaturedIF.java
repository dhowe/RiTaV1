package rita.support;

import java.util.Map;
import java.util.Set;

public interface FeaturedIF
{
  public abstract Map features();  

  public abstract Set getAvailableFeatures();

  public abstract String getFeature(CharSequence name);
  
  public abstract boolean hasFeature(CharSequence name);

  public abstract void addFeature(CharSequence name, CharSequence value);
  
  //public abstract void setFeatures(Map features);

  //public abstract void removeFeature(CharSequence name);

  //public abstract void clearFeatures();

  public abstract String text();
}