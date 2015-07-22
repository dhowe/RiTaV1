package rita.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RuleList implements Constants
{
  // name -> Map(rule, prob)
  
  public Map<String, Map<String, Float>> prules;

  public RuleList()
  {
    prules = new HashMap<String, Map<String, Float>>();
  }

  public Iterator<String> iterator()
  {
    return prules.keySet().iterator();
  }

  public Set<String> keySet()
  {
    return prules.keySet();
  }

  public void addRule(String name, String rule, float weight) throws RuntimeException
  {
    Map<String, Float> temp;
    if (hasRule(name)) // we store multiple rules in existing map
    {
      temp = prules.get(name);
      temp.put(rule, weight);
    }
    else
    // we need a new rule/weight map
    {
      Map<String, Float> temp2 = new HashMap<String, Float>();
      temp2.put(rule, weight);
      prules.put(name, temp2);
    }
  }

  private String getStochasticRule(Map<String, Float> weightedRules)
  {
    String result = null;
    Map<String, Float> temp = weightedRules;
    Collection<Float> values = temp.values();
    Iterator<Float> it = values.iterator();
    float total = 0;
    double p = Math.random();
    while (it.hasNext())
    {
      total += it.next();
    }
    for (Iterator iterator = temp.entrySet().iterator(); iterator.hasNext();)
    {
      Map.Entry entry = (Map.Entry) iterator.next();
      if (p < (Float) entry.getValue() / total)
      {
        result = (String) entry.getKey();
        break;
      }
      p -= (Float) entry.getValue() / total;
    }
    return result;
  }

  public String doRule(String pre)
  {
    Map<String, Float> temp = prules.get(pre);
    if (temp == null) return null;
    if (temp.size() == 1)
    {
      Object[] result = temp.keySet().toArray();
      return (String) result[0];
    }
    return getStochasticRule(temp);
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();//"Rules:\n");
    String ch = " ";
    for (Iterator it = prules.entrySet().iterator(); it.hasNext();)
    {
      Map.Entry entrySet = (Map.Entry) it.next();
      ch = (String) entrySet.getKey();
      sb.append(ch);
      sb.append("\n");
      Map rules = (Map) entrySet.getValue();
      for (Iterator iterator = rules.entrySet().iterator(); iterator.hasNext();)
      {
        Map.Entry entry = (Map.Entry) iterator.next();
        String rule = (String) entry.getKey();
        sb.append("  ");
        sb.append("'"+rule+"'");
        Float weight = (Float) entry.getValue();
        sb.append(" [");
        sb.append(weight);
        sb.append("]\n");
      }
    }
    return sb.toString();
  }

  /**
   * Empty the collection
   */
  public void clear()
  {
    prules.clear();
  }

  public boolean hasRule(String pre)
  {
    return prules.containsKey(pre);
  }
  
  public String getRule(String name)
  {
    Map<String, Float> temp = prules.get(name);
    
    if (temp == null || temp.size()<1)
      return null;
    
    String[] rules = temp.keySet().toArray(EMPTY);
    
    if (temp.size() == 1) return rules[0];
    
    String result = E;
    for (int i = 0; i < rules.length; i++)
    {
       result += rules[i];
       Float pr = temp.get(rules[i]);
       if (pr != 1.0f)
         result += " ["+pr+"]";
       if (i < rules.length - 1) 
         result += " | ";
    }
    return result;
  }

  public void removeRule(String s)
  {
    prules.remove(s); // DH: added 1/3/13
  }

}
