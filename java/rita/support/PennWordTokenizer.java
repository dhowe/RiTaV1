package rita.support;

import java.util.List;

import rita.RiTa;

// TODO:
// Add test:
//    Doesn't handle decimal numbers correctly! 
//    e.g., "5 cookies divided by 2 is 2.5 cookies."

// Should all these regexs should be compiled? yes

/**
 * Simple word tokenizer that tokenizes according to the Penn Treebank
 * conventions.
 */
public final class PennWordTokenizer implements TokenizerIF, Constants {

  // METHODS -------------------------------------------

  public String tokenizeInline(String words) {
    throw new RuntimeException("unimplemented!");
  }

  /**
   * Tokenizes the String according to the Penn Treebank conventions and stores
   * the result as a List in <code>result</code>
   */
  public void tokenize(String words, List result) {
    String[] tokens = tokenize(words);
    for (int i = 0; i < tokens.length; i++)
      result.add(tokens[i]);
  }

  /**
   * Tokenizes the String according to the Penn Treebank conventions.
   */
  public String[] tokenize(String words) {

    // TODO: these should all be compiled (see #547)
    words = words.replaceAll("([Ee])\\.([Gg])\\.", "_$1$2_");
    words = words.replaceAll("([Ii])\\.([Ee])\\.", "_$1$2_");
    
    words = words.replaceAll("([\\?!\"“‘\\.,;:@#$%&])", " $1 ");
    words = words.replaceAll("\\.\\.\\.", " ... ");
    words = words.replaceAll("\\s+", SP);
    words = words.replaceAll(",([^0-9])", " , $1");
    words = words.replaceAll("([^.])([.])([\\])}>\"'’]*)\\s*$", "$1 $2$3 ");
    words = words.replaceAll("([\\[\\](){}<>])", " $1 ");
    words = words.replaceAll("--", " -- ");
    words = words.replaceAll("$", SP);
    words = words.replaceAll("^", SP);
    words = words.replaceAll("([^'])' | '", "$1 ' ");
    words = words.replaceAll("([^’])’ ", "$1 ’ ");

    // DCH!! (changed from ^, only on a proper name(starting cap)?)
    words = words.replaceAll("'([SMD]) ", " '$1 ");

    if (RiTa.SPLIT_CONTRACTIONS) {
     
      words = words.replaceAll("([Cc])an['’]t", "$1an not");
      words = words.replaceAll("([Dd])idn['’]t", "$1id not");
      words = words.replaceAll("([CcWw])ouldn['’]t", "$1ould not");
      words = words.replaceAll("([Ss])houldn['’]t", "$1hould not");
      words = words.replaceAll(" ([Ii])t['’]s", " $1t is");
      words = words.replaceAll("n['’]t", " not ");
      words = words.replaceAll("['’]ve", " have ");
      words = words.replaceAll("['’]re", " are ");
    }

    // "Nicole I. Kidman" gets tokenized as "Nicole I . Kidman"
    words = words.replaceAll(" ([A-Z]) \\.", " $1. ");
    words = words.replaceAll("\\s+", SP);
    words = words.replaceAll("^\\s+", "");
    
    words = words.replaceAll("_([Ee])([Gg])_", "$1.$2.");
    words = words.replaceAll("_([Ii])([Ee])_", "$1.$2.");

    return words.split(SP);
  }

  public static void main(String[] args) {
    String text = "Dr. Chan is talking slowly with Mr. Cheng, and they're friends.";
    // , that's why this is our place).";//"McDonald's - it cannot
    // happen" , "McDonald's, it cannot happen" };
    // for (int j = 0; j < 1; j++)

    // String text = texts[j];
    PennWordTokenizer tk = new PennWordTokenizer();
    RiTa.SPLIT_CONTRACTIONS = false;
    String[] toks = tk.tokenize(text);
    System.out.print("[");
    for (int i = 0; i < toks.length; i++)
      System.out.print("'" + toks[i] + "' ");
    System.out.println("]");

    /*
     * tk.setSplitContractions(false);
     * 
     * List l = new ArrayList(); tk.tokenize(text, l); System.out.print("[");
     * for (int i = 0; i < l.size(); i++) System.out.print("'"+l.get(i)+"' ");
     * System.out.print("]");
     */
  }
}