// $Id: Featured.java,v 1.2 2013/05/14 08:51:16 dev Exp $

package rita.support;

import java.util.*;

import rita.*;


/**
 * Superclass for objects to which features (String->String key-value pairs) can be attached
 */
public class Featured implements FeaturedIF, Constants 
{
  // need to rethink featureIds to integrate with RiObject Ids
  private int id = -1;
  protected Map features = new HashMap();

  public Featured() {
  }
  
  public String toString()
  {
    return features.toString();
  }
  
  public void setFeatures(Map features)
  {
    this.features = features;
  }
  
  /* (non-Javadoc)
   * @see rita.feature.FeaturedIF#hasFeature(java.lang.String)
   */
  public boolean hasFeature(CharSequence name) {
    if (name == null) return false;
    if (name.equals(ID)) return true;
    if (features == null) return false;
    return features.containsKey(name.toString());
  }  
  
  /* (non-Javadoc)
   * @see rita.feature.FeaturedIF#getFeatures()
   */
  public Map features() 
  {        
    return features;
  }
  
  /* (non-Javadoc)
   * @see rita.feature.FeaturedIF#getAvailableFeatures()
   */
  public Set getAvailableFeatures() { 
    return features.keySet();
  }
  
  public String text() { 
    return (String)features.get(TEXT);
  }
  
  /* (non-Javadoc)
   * @see rita.feature.FeaturedIF#getFeature(java.lang.CharSequence)
   */
  public String getFeature(CharSequence name) {
    if (name == null) return null;
    if (features == null) return null;
    return (String)features.get(name.toString());
  }
  
  /* (non-Javadoc)
   * @see rita.feature.FeaturedIF#addFeature(java.lang.CharSequence, java.lang.CharSequence)
   */
  public void addFeature(CharSequence name, CharSequence value) { 
    this.features.put(name.toString(), value.toString());
  }

  public static void addFeature(Map featureList, CharSequence name, CharSequence value) { 
    if (featureList == null) {
      featureList = new HashMap();
    }
    featureList.put(name.toString(), value.toString());
  }
  
  /* (non-Javadoc)
   * @see rita.feature.FeaturedIF#removeFeature(java.lang.CharSequence)
   */
  public void removeFeature(CharSequence name) {
    if (features == null) return;
    features.remove(name.toString());
  }
  
  /* (non-Javadoc)
   * @see rita.feature.FeaturedIF#clearFeatures()
   */
  public void clearFeatures() {
    if (features != null) {
      String id = getFeature(ID);
      features.clear();
      features.put(ID, id);
    }
  }
  
  public void appendFeature(String name, String value)
  {
    String origVal = getFeature(name.toString());    
    if (origVal != null) {
      if (!origVal.endsWith(" ")) 
        origVal += " "; 
      value = origVal + value;
    }
    addFeature(name, value.toString());    
  }
  
  public static String asFeature(boolean val) {
    return val ? "true" : "false";
  }
   
  public static String asFeature(List l) {
    if (l.size()==1) {      
      String s= (String)l.get(0);
      return s;
    }    
    return RiTa.join(l, WORD_BOUNDARY);
  }
  
  public static String asFeature(Object[] l) {
    if (l.length==1) return (String)l[0];
    return RiTa.join(l, WORD_BOUNDARY);
  }  
  
  public static String asFeature(List l, String delim) {
    if (l.size()==1) return (String)l.get(0);
    return RiTa.join(l, delim);
  }
  
  public static String asFeature(Object[] l, String delim) {
    if (l.length==1) return (String)l[0];
    return RiTa.join(l, delim);
  }



  public static String[] getFeatures(FeaturedIF[] words, String feature)
  {
    String[] s = new String[words.length];
    for (int i = 0; i < s.length; i++)
      s[i] = words[i].getFeature(feature);
    return s;
  }

  public static FeaturedIF[] fromStrings(String[] words)
  {    
    return RiString.fromStrings(words);
  }

  
}// end
