package rita.support;

import java.util.List;

public interface TokenizerIF
{
  public String[] tokenize(String sentence);
  
  public void tokenize(String sentence, List result);
}
