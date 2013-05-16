// $Id: Splitter.java,v 1.1 2013/05/13 14:02:07 dev Exp $

package rita.support;

import java.util.ArrayList;
import java.util.List;

import rita.RiTa;

/**
 * A simple interface for different sentence splitters.<P>
 * Note: Adapted from JET & OAK
 */
public class Splitter implements Constants
{  
  public static final boolean DBUG = false;
  public static final int MAX_CHARS_PERS_SENTENCE = 384;
  public static final int MIN_CHARS_PERS_SENTENCE = 8;
	static final boolean ADD_SENTENCE_TAGS = false;
  
  private boolean removeQuotations = false;
  private static Splitter instance ;
  
  private Splitter() {}

  public static Splitter getInstance() {
    if (instance == null)
      instance = new Splitter();
    return instance;
  }
  
  public String[] splitSentences(String text) 
  {    
    List l = new ArrayList();
    splitSentences(l, text);
    return (String[])l.toArray(new String[l.size()]);
  }
  
  /**
   *  Splits the data in <code>text</code> into sentences. <P>
   *  We split after a period if the following token is capitalized, 
   *  and the preceding token is not a known not-sentence-ending 
   *  abbreviation (such as a title) or a single capital letter.
   */
  public List splitSentences(List sentences, String text) 
  {
    if (text == null || text.length()==0) return sentences;
    
    boolean startOfSentence = true;
    
    int cursor=0, tokenCount=0, end = text.length();
    int sentenceEnd=0, sentenceStart=0, nextTokenStart;
    String currentToken = null, nextToken;
    
    //  advance 'position' to first non-blank
    while ((cursor < end) && Character.isWhitespace(text.charAt(cursor))) 
      cursor++;
    
    if (sentences == null)
      throw new IllegalArgumentException("Null list param!");
    
    //  if all blank (or empty span), exit
    if (cursor >= end) System.out.println
      ("WARN: Null or empty argument: <"+text+">");    

    if (ADD_SENTENCE_TAGS) sentences.add("<SENTENCES>"); 
    
    while (cursor < end) 
    {
      nextTokenStart = cursor;
      while ((cursor < end) && !Character.isWhitespace(text.charAt(cursor))) 
        cursor++;
      
      nextToken = text.substring(nextTokenStart, cursor);
      tokenCount++;
      
      // advance to next non-blank
      while ((cursor < end) && Character.isWhitespace(text.charAt(cursor))) 
        cursor++;
      
      if (isSentenceEnd(currentToken, nextToken, startOfSentence) 
          || isDatelineEnd(currentToken, tokenCount)) 
      {
        sentenceEnd = nextTokenStart;
        addSentence(text.substring(sentenceStart, sentenceEnd), sentences);
        sentenceStart = sentenceEnd;
        startOfSentence = true;
      } 
      else { 
        startOfSentence = false;
      }
      
      currentToken = nextToken;      
    }
    sentenceEnd = end;   
    
    // working here on abbreviations as EOS... ???
    if (sentenceStart != sentenceEnd)
      addSentence(text.substring(sentenceStart, sentenceEnd), sentences);
    
    if (ADD_SENTENCE_TAGS) sentences.add("</SENTENCES>");// tmp
    
    if (DBUG) System.out.println("SentenceParser.Rejected " + 
      rejectCount+"/"+sentences.size()+" sentences");
    
    // hack for empty returns ???
    if (sentences.size()<1 && text.length()>0)
      sentences.add(text);
    
    return sentences;
  }

  private int rejectCount = 0;
  
/*  public static boolean isAbbreviation(String token)
  {
    return abbreviations.contains(token); // case??
  }
*/
  /*// TODO: Rewrite - pass the whole sentence,
  // and do lookup on the last word & POS?
  public static boolean isSentenceEnd(String word)
  {
    if (word == null || word.length() < 1)
      throw new IllegalArgumentException("'" + word + "'");

    char c = word.charAt(word.length()-1);    
    if (c != '.' && c != '?')
      return false;

    // some OK cases ("... said the Mrs..", "... says I..);
    if (word.endsWith("..") || word.equals("I.") || word.endsWith("'s."))
      return true; // remove???
    
    // punctuation (tmp?)
    if (word.equals("''.") || word.equals("``."))
      return false;

    // titles
    if (word.equalsIgnoreCase("Mrs."))
      return false;
    if (word.equalsIgnoreCase("Mr."))
      return false;
    if (word.equalsIgnoreCase("Ms."))
      return false;
    if (word.equalsIgnoreCase("Jr."))
      return false;
    if (word.equalsIgnoreCase("Sr."))
      return false;
    if (word.equalsIgnoreCase("Dr."))
      return false;

    // misc
    if (word.equalsIgnoreCase("Corp."))
      return false;
    if (word.equalsIgnoreCase("Co."))
      return false;
    if (word.equalsIgnoreCase("Tech."))
      return false;
    if (word.equalsIgnoreCase("Inc."))
      return false;
    if (word.equalsIgnoreCase("P.m."))
      return false;
    if (word.equalsIgnoreCase("A.m."))
      return false;
    if (word.equalsIgnoreCase("U.s."))
      return false;
    if (word.equalsIgnoreCase("St."))
      return false;
    if (word.equalsIgnoreCase("Apt."))
      return false;
    if (word.equalsIgnoreCase("Etc."))
      return false;
    if (word.equalsIgnoreCase("Ave."))
      return false;
    if (word.equalsIgnoreCase("Pl."))
      return false;
    if (word.equalsIgnoreCase("Blvd."))
      return false;
    if (word.equalsIgnoreCase("Rd."))
      return false;
    if (word.equalsIgnoreCase("Ln."))
      return false;
    if (word.equalsIgnoreCase("E.G.T."))
      return false; // tmp

    // months
    if (word.equalsIgnoreCase("Jan."))
      return false;
    if (word.equalsIgnoreCase("Feb."))
      return false;
    if (word.equalsIgnoreCase("Mar."))
      return false;
    if (word.equalsIgnoreCase("Apr."))
      return false;
    if (word.equalsIgnoreCase("Jun."))
      return false;
    if (word.equalsIgnoreCase("Jul."))
      return false;
    if (word.equalsIgnoreCase("Aug."))
      return false;
    if (word.equalsIgnoreCase("Sept."))
      return false;
    if (word.equalsIgnoreCase("Sep."))
      return false;
    if (word.equalsIgnoreCase("Nov."))
      return false;
    if (word.equalsIgnoreCase("Oct."))
      return false;
    if (word.equalsIgnoreCase("Dec."))
      return false;

    // country & state abbreviations
    if (word.equalsIgnoreCase("Ariz."))
      return false;

    // time
    if (word.equalsIgnoreCase("a."))
      return false; // am
    if (word.equalsIgnoreCase("p."))
      return false; // pm

    // initials  (A., B., C., etc.)
    if (word.length() == 2 && Character.isUpperCase(word.charAt(0)))
      return false;

    return true;
  }*/
  
  /**
   * Returns true if <I>currentToken</I> is the final token of a sentence.<P>
   * This is a simplified version of the OAK/JET sentence splitter.
   */
  public static boolean isSentenceEnd(String currentToken, String nextToken)
  {
    return isSentenceEnd(currentToken, nextToken, false); 
  }
  
  /**
   * Returns true if <I>currentToken</I> is the final token of a sentence.<P>
   * This is a simplified version of the OAK/JET sentence splitter.
   */
  public static boolean isSentenceEnd
    (String currentToken, String nextToken, boolean startOfSentence)
  {
    if (currentToken == null)
      return false;
    
    int cTL = currentToken.length();
    
    // token is a mid-sentence abbreviation (mainly, titles) --> middle of sent
    if (RiTa.isAbbreviation(currentToken))
      return false;
    
    if (cTL > 1 && isIn(currentToken.charAt(0), "`'\"([{<")
        && RiTa.isAbbreviation(currentToken.substring(1)))
      return false;

    if (cTL > 2 && ((currentToken.charAt(0) == '\'' 
      && currentToken.charAt(1) == '\'') || (currentToken.charAt(0) == '`' 
      && currentToken.charAt(1) == '`')) && RiTa.isAbbreviation(currentToken.substring(2)))
    {
      return false;
    }
    
    char currentToken0 = currentToken.charAt(cTL - 1);
    char currentToken1 = (cTL > 1) ? currentToken.charAt(cTL - 2) : ' ';
    char currentToken2 = (cTL > 2) ? currentToken.charAt(cTL - 3) : ' ';
    
    int nTL = nextToken.length();
    char nextToken0 = nextToken.charAt(0);
    char nextToken1 = (nTL > 1) ? nextToken.charAt(1) : ' ';
    char nextToken2 = (nTL > 2) ? nextToken.charAt(2) : ' ';

    // nextToken does not begin with an upper case,
    // [`'"([{<] + upper case, `` + upper case, or < -> middle of sent.
    if (!  (Character.isUpperCase(nextToken0) 
        || (Character.isUpperCase(nextToken1) && isIn(nextToken0, "`'\"([{<"))
        || (Character.isUpperCase(nextToken2) && ((nextToken0 == '`' && nextToken1 == '`') 
        || (nextToken0 == '\'' && nextToken1 == '\'')))
        ||  nextToken.equals("_") || nextToken0 == '<'))
      return false;

    // ends with ?, !, [!?.]["'}>)], or [?!.]'' -> end of sentence
    if (currentToken0 == '?'
        || currentToken0 == '!'
        || (isIn(currentToken1, "?!.") && isIn(currentToken0, "\"'}>)"))
        || (isIn(currentToken2, "?!.") && currentToken1 == '\'' && currentToken0 == '\''))
      return true;
      
    // last char not "." -> middle of sentence
    if (currentToken0 != '.') return false;

    // -- added to handle Q. / A. in news wire ---------
    // Q. or A. at start of sentence --> end of sentence
    // (so 'Q.' or 'A.' is treated as a 1-word sentence)
    if (startOfSentence && (currentToken.equalsIgnoreCase("Q.") 
      || currentToken.equalsIgnoreCase("A."))) 
    {
      return true;
    }

    // single upper-case alpha + "." -> middle of sentence
    if (cTL == 2 && Character.isUpperCase(currentToken1))
      return false;

    // double initial (X.Y.) -> middle of sentence << added for ACE
    if (cTL == 4 && currentToken2 == '.'
        && (Character.isUpperCase(currentToken1) && Character
            .isUpperCase(currentToken.charAt(0))))
      return false;

    // U.S. or U.N. -> middle of sentence
    //if (currentToken.equals("U.S.") || currentToken.equals("U.N."))
      //return false; // dch
      
    //f (Util.isAbbreviation(currentToken)) return false;
    
    // (for XML-marked text) next char is < -> end of sentence
    if (nextToken0 == '<')
      return true;
    
    return true;
  }
  
  private static boolean isIn(char c, String s)
  {
    return s.indexOf(c) >= 0;
  }
  
  /*static Set abbreviations;
  static {// titles
    abbreviations = new HashSet();
    Set tmp = new HashSet();
    tmp.add("Adm.");
    tmp.add("Capt.");
    tmp.add("Cmdr.");
    tmp.add("Col.");
    tmp.add("Dr.");
    tmp.add("Gen.");
    tmp.add("Gov.");
    tmp.add("Lt.");
    tmp.add("Maj.");
    tmp.add("Messrs.");
    tmp.add("Mr.");
    tmp.add("Mrs.");
    tmp.add("Ms.");
    tmp.add("Prof.");
    tmp.add("Rep.");
    tmp.add("Reps.");
    tmp.add("Rev.");
    tmp.add("Sen.");
    tmp.add("Sens.");
    tmp.add("Sgt.");
    tmp.add("Sr.");
    tmp.add("St.");

    // abbreviated first names
    tmp.add("Alex."); // dch
    tmp.add("Benj.");
    tmp.add("Chas.");

    // other tmp
    tmp.add("a.k.a.");
    tmp.add("c.f.");
    tmp.add("i.e.");
    tmp.add("e.g.");
    tmp.add("vs.");
    tmp.add("v.");

    Iterator it = tmp.iterator();
    while (it.hasNext())
      abbreviations.add(((String) it.next()).toLowerCase());
  }*/
  
  private void addSentence(String toAdd, List l)
  {
    int len = toAdd.length();
    if (len > MAX_CHARS_PERS_SENTENCE || len < MIN_CHARS_PERS_SENTENCE) { 
      //System.out.println("BAD: "+clean(toadd));
      rejectCount++;
      return;
    }
    l.add(clean(toAdd).trim());
  }
  
  // strips out line breaks in sentences)
  private String clean(String sentence) {
    if (removeQuotations) {
      sentence = sentence.replaceAll("[\"��]", E);
      sentence = sentence.replaceAll("['`��] ", E);
      sentence = sentence.replaceAll(" ['`��]", E);
    }
    sentence = sentence.replaceAll("\\s+", SP);
    return sentence;
  }
   
  // a '_' within the first 5 characters is treated as the end of a dateline
  private static boolean isDatelineEnd (String currentToken, int tokenCount)
  {
    return currentToken != null && currentToken.equals("_") && tokenCount <= 5;
  } 
  
  /**
   * Tells the parser whether to trim single and double quotes
   * from input text.
   * @param removeQuotations
   */
  public void setTrimQuotations(boolean removeQuotations)
  {
    this.removeQuotations = removeQuotations;    
  }

  /**
   * Returns whether the parser is trimming single and double quotes
   * from input text.
   */
  public boolean isRemovingQuotations()
  {
    return this.removeQuotations;
  }
  
  public static void main(String[] args)
  {
    String txt = "That is to say, the wind blew from the worst possible direction " +
      "for landing at the Lighthouse. Yes, he did say disagreeable things, Mrs Ramsay" +
      " admitted; it was odious of him to rub this in, and make James still more " +
      "disappointed; but at the same time, she would not let them laugh at him. ''The" +
      " atheist,'' they called him; 'the little atheist.' Rose mocked him; Prue mocked" +
      " him; Andrew, Jasper, Roger mocked him; even old Mr. Badger without a tooth in his" +
      " head had bit him, for being (as Nancy put it) the hundred and tenth young man to " +
      "chase them all the way up to the Hebrides when it was ever so much nicer to be " +
      "alone. And this is'nt the last\none in the group. \"My name is X\", he said.";
        
    txt = "He gave the bone to Mr. Wilson. They shook hands and left the U.S. for Russia.";
    txt = "The dog";
    Splitter sp = new Splitter();
    //System.out.println("ignoreQuotedText: "+sp.removeQuotations);
    String[] sentences = sp.splitSentences(txt);
    System.out.println("LENGTH: "+sentences.length);
    for (int i = 0; i < sentences.length; i++)
      System.out.println(i+") "+sentences[i]);  
  }

}// end
