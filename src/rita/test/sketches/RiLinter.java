package rita.test.sketches;

import rita.RiTa;
import rita.RiTaException;

import com.googlecode.jslint4java.*;

// next: add boolean option to allow single-quoted keys/values and/or add quotes?
public class RiLinter
{
  public static void main(String[] args)
  {
    System.out.println(RiTa.asList(lint("{ \"a\": hello }")));
  }

  public static String[] lintFile(final String filePath) 
  {  
    return lint(RiTa.loadString(filePath));
  }
  
  public static String[] lint(final String json) 
  {  
    try
    {
      //String fileContent = RiTa.loadString(filePath);
      JSLintBuilder builder = new JSLintBuilder();
      JSLint jsLint = builder.fromDefault();
      
      JSLintResult result = jsLint.lint("test.js", json);
      int i = 0;
      String[] errors = new String[result.getIssues().size()];
      for (Issue issue : result.getIssues()) {
          errors[i++] = issue.toString();
      }
      return errors;
    }
    catch (Exception e)
    {
      throw new RiTaException(e);
    }    
  }
}