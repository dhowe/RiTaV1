package rita.support;

import java.util.List;
import java.util.regex.Pattern;

import rita.RiTaException;

/**
 * A simple tokenizer for word boundaries with regular expression
 * support for custom-tokenizing.
 * 
 * @author dhowe
 */
public class RiTokenizer implements TokenizerIF, Constants
{  
  protected static int DEFAULT_TOKENIZER = PENN_WORD_TOKENIZER;

  private static RiTokenizer regex,tts,penn;
  
  protected TokenizerIF delegate;

  private RiTokenizer(TokenizerIF del) {
    this.delegate = del;
  }
   
  // --------------------------------------------------------------

  public static RiTokenizer getInstance() {
    return getInstance(DEFAULT_TOKENIZER);
  }

  public static RiTokenizer getRegexInstance(String regexPattern) {
    if (regexPattern == null) 
      throw new RiTaException("Regex pattern cannot be null");
    return getRegexInstance(Pattern.compile(regexPattern));
  }
  
  public static RiTokenizer getRegexInstance(Pattern regexPattern) {
    regex = new RiTokenizer(new RegexTokenizer().setRegex(regexPattern));
    return regex;   
  }
    
  /**
   * Valid types are: PENN_WORD_TOKENIZER, REGEX_TOKENIZER
   * @param type
   * @return RiTokenizer
   */
  public static RiTokenizer getInstance(int type) {
    
    switch (type) {
      case PENN_WORD_TOKENIZER:
        if (penn == null) 
          penn = new RiTokenizer(new PennWordTokenizer());
        return penn;
      case REGEX_TOKENIZER:     
        if (regex == null) 
          regex = new RiTokenizer(new RegexTokenizer());
        return regex;          
      default:
        throw new RiTaException("Unexpected Tokenizer Type: "+type);
    }
  }

  /**
   * Tokenizes the sentence into an array of words.
   */
  public String[] tokenize(String sentence)
  {
    if (sentence==null || sentence.length() < 1) 
      return EMPTY;
    return delegate.tokenize(sentence);
  }

  /**
   * Tokenizes the sentence into an array of words
   * and adds them to the result List
   */
  public void tokenize(String sentence, List result)
  {
    if (sentence==null || sentence.length() < 1) return;

    delegate.tokenize(sentence, result);
  }

}// end
