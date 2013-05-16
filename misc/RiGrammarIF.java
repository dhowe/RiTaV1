package rita;

import java.util.Map;

public interface RiGrammarIF
{
  public String expand();
  public String expandFrom(String s); 
  public String expandWith(String s, String t);
  public boolean hasRule(String s);
  public String getRule(String s); 
  
  public RiGrammar addRule(String name, String ruleStr);
  public RiGrammar addRule(String name, String ruleStr, float weight);
  public RiGrammar setGrammar(String s);
  public RiGrammar removeRule(String s);
  public RiGrammar print();
  public RiGrammar reset();
}
