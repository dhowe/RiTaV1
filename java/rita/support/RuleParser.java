package rita.support;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;

import rita.RiTa;
import rita.RiTaException;


/**
 * Simple stream-parser set-up to identify grammar definitions
 */
public class RuleParser
{
  private StreamTokenizer tokenizer;

  public RuleParser(InputStream istream)
  {
    //System.out.println(streamToString(istream));
    if (istream == null)
      throw new RiTaException("RuleParser: null input stream!");

    Reader r = new BufferedReader(new InputStreamReader(istream));
    this.tokenizer = new StreamTokenizer(r);
        //new ByteArrayInputStream(grammarRulesAsString.getBytes(UTF_8));
      //(new BufferedReader(new UnicodeInputStream(istream));
    this.initTokenizer();
  }  
  
  private void initTokenizer()
  {
    String string = "[]<>\"/*&%$#!():;'.,_-0123456789`ü";
    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);
      tokenizer.ordinaryChar(c);
      tokenizer.wordChars(c, c);
    }    
    tokenizer.ordinaryChar('\r');
    tokenizer.ordinaryChar('\n');
  }

  int count=0;
  String nextToken = null;
  public String getNextToken()
  {    
    // do we have one waiting?
    if (nextToken != null) {
      String tmp = nextToken;
      nextToken = null;
      return tmp;
    }
        
    // lets check the stream
    String result = nextStreamToken();
    
//System.out.println((count++)+") '"+result+"'"); 
     
    if (LegacyGrammar.isExecEnabled())      
      result = checkForExec(result);
    
//System.out.println((count++)+") '"+result+"'");  
    
    return result;
  }

  private String checkForExec(String result) 
  {
    if (result != null) { 
      if (result.contains(LegacyGrammar.EXEC_CHAR)) {
        while (!result.contains(LegacyGrammar.EXEC_POST)) {
          String tmp = nextStreamToken();
          if (tmp == null)
            throw new RiTaException("Unterminated exec() call: "+result);
          result += " "+tmp;
        }          
        result = preProcessExec(result);  
      }
    }
    return result;
  }

  private String preProcessExec(String result) {
//System.out.print("RuleParser.preProcessExec("+result+")");
    int postIdx = result.indexOf(LegacyGrammar.EXEC_POST);
    if (postIdx < 0)
      throw new RiTaException("Unterminated exec call? string="+result);
    if (postIdx != result.length()- LegacyGrammar.EXEC_POST.length()) {
      //throw new RiTaException("Invalid exec call? string="+result);
      String pre = result.substring(0,postIdx+LegacyGrammar.EXEC_POST.length());
      String post = result.substring(postIdx+LegacyGrammar.EXEC_POST.length());
      result = pre; 
      if (post.length()>0) // save for next round
        nextToken = (nextToken == null) ? post : " " + post;
    }
//System.out.println(" - > "+result);
    return result;
  }


  private String nextStreamToken() 
  {    
    if (tokenizer == null)  return null;
    
    String result = null;       
    int i = -1;
    try {   
      i = tokenizer.nextToken();
      
    } 
    catch (IOException ie) {
      throw new RiTaException(ie.getMessage()+", token="+i);
    }
    
    switch (i)
    {
      case StreamTokenizer.TT_NUMBER:
        result = String.valueOf(tokenizer.nval);
        break;
      case StreamTokenizer.TT_WORD:
        result = tokenizer.sval;
        //System.out.println("Dame:"+result);
        break;
      case StreamTokenizer.TT_EOL:
        result = "\n";
        break;
      case StreamTokenizer.TT_EOF:
        result = null; 
        break;
      default: {
        result = String.valueOf((char)i);
      }
    }
    //System.out.println("RuleParser.nextStreamToken() -> "+result);
    return result;
  }
  
  // hack
  static public String streamToString(InputStream input) {
    try {
      BufferedReader reader =
        new BufferedReader(new InputStreamReader(input, "UTF-8"));

      String lines[] = new String[100];
      int lineCount = 0;
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (lineCount == lines.length) {
          String temp[] = new String[lineCount << 1];
          System.arraycopy(lines, 0, temp, 0, lineCount);
          lines = temp;
        }
        lines[lineCount++] = line;
      }
      reader.close();

      if (lineCount == lines.length) {
        return RiTa.join(lines,' ');
      }

      // resize array to appropriate amount for these lines
      String output[] = new String[lineCount];
      System.arraycopy(lines, 0, output, 0, lineCount);
      return RiTa.join(output,' ');

    } catch (IOException e) {
      e.printStackTrace();
      //throw new RuntimeException("Error inside loadStrings()");
    }
    return null;
  }
}