package rita.support;

import java.util.ArrayList;
import java.util.List;

import rita.RiTa;
import rita.RiTaException;

/**
 * Simple pos-tagger for the RiTa libary using the Penn tagset. 
 * </pre>
 * The full Penn part-of-speech tag set:
 * <ul>
 * <li><b><code>cc</code> </b> coordinating conjunction
 * <li><b><code>cd</code> </b> cardinal number
 * <li><b><code>dt</code> </b> determiner
 * <li><b><code>ex</code> </b> existential there
 * <li><b><code>fw</code> </b> foreign word
 * <li><b><code>in</code> </b> preposition/subord. conjunction
 * <li><b><code>jj</code> </b> adjective
 * <li><b><code>jjr</code> </b> adjective, comparative
 * <li><b><code>jjs</code> </b> adjective, superlative
 * <li><b><code>ls</code> </b> list item marker
 * <li><b><code>md</code> </b> modal
 * <li><b><code>nn</code> </b> noun, singular or mass
 * <li><b><code>nns</code> </b> noun, plural
 * <li><b><code>nnp</code> </b> proper noun, singular
 * <li><b><code>nnps</code> </b> proper noun, plural
 * <li><b><code>pdt</code> </b> predeterminer
 * <li><b><code>pos</code> </b> possessive ending
 * <li><b><code>prp</code> </b> personal pronoun
 * <li><b><code>prp$</code> </b>i possessive pronoun
 * <li><b><code>rb</code> </b> adverb
 * <li><b><code>rbr</code> </b> adverb, comparative
 * <li><b><code>rbs</code> </b> adverb, superlative
 * <li><b><code>rp</code> </b> particle
 * <li><b><code>sym</code> </b> symbol (mathematical or scientific)
 * <li><b><code>to</code> </b> to
 * <li><b><code>uh</code> </b> interjection
 * <li><b><code>vb</code> </b> verb, base form
 * <li><b><code>vbd</code> </b> verb, past tense
 * <li><b><code>vbg</code> </b> verb, gerund/present participle
 * <li><b><code>vbn</code> </b> verb, past participle
 * <li><b><code>vbp</code> </b> verb, non-3rd ps. sing. present
 * <li><b><code>vbz</code> </b> verb, 3rd ps. sing. present
 * <li><b><code>wdt</code> </b> wh-determiner
 * <li><b><code>wp</code> </b> wh-pronoun
 * <li><b><code>wp$</code> </b> possessive wh-pronoun
 * <li><b><code>wrb</code> </b> wh-adverb
 * <li><b><code>#</code> </b> pound sign
 * <li><b><code>$</code> </b> dollar sign
 * <li><b><code>.</code> </b> sentence-final punctuation
 * <li><b><code>,</code> </b> comma
 * <li><b><code>:</code> </b> colon, semi-colon
 * <li><b><code>(</code> </b> left bracket character
 * <li><b><code>)</code> </b> right bracket character
 * <li><b><code>"</code> </b> straight double quote
 * <li><b><code>`</code> </b> left open single quote
 * <li><b><code>"</code> </b> left open double quote
 * <li><b><code>"</code> </b> right close single quote
 * <li><b><code>"</code> </b> right close double quote
 * <li><b><code>-</code> </b> dash
 * </ul>
*/
public class PosTagger implements Constants
{     
  protected static BrillPosTagger tagger = BrillPosTagger.getInstance();
  
  private static PosTagger instance;

  private PosTagger() {}  
  
  public static PosTagger getInstance() {
    if (instance == null)
      instance = new PosTagger();
    return instance;  
  } 

  /**
   * Takes an array of words of tags and returns a 
   * combined String of the form:
   *  <pre>"The/dt doctor/nn treated/vbd dogs/nns"</pre>
   * assuming a "/" as <code>delimiter</code>. 
   */
  public static String inlineTags
    (String[] tokenArray, String[] tagArray, String delimiter) 
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < tagArray.length; i++)
      if (tokenArray[i].length() > 0)
        sb.append(tokenArray[i] + delimiter + tagArray[i] + " ");
    return sb.toString().trim();
  }
    
  /**
   * Tags the array of words (as usual) with a part-of-speech from the Penn tagset, 
   * then returns the corresponding part-of-speech for WordNet from the set
   * { "n" (noun), "v"(verb), "a"(adj), "r"(adverb), "-"(other) } as a String. 
   * @see #tag
   */
  public String[] tagForWordNet(String[] tokenArray)
  { 
    String[] tags = tag(tokenArray);
    for (int i = 0; i < tags.length; i++) 
      tags[i] = toWordNet(tags[i]);
    return tags;
  }
  
  /**
   * Converts a part-of-speech String from the Penn tagset to the corresponding part-of-speech 
   * for WordNet from the set { "n" (noun), "v"(verb), "a"(adj), "r"(adverb), "-"(other) } as a String. 
   * If the pos is not found in the penn set, it is returned unchanged.
   */
  public static String toWordNet(String pos)
  {     
    if (pos!=null && pos.length()>0) {
      if (isNoun(pos))      return "n";
      if (isVerb(pos))      return "v";
      if (isAdverb(pos))    return "r";
      if (isAdjective(pos)) return "a";
    }
    return "-";
  } 
  

  /**
   * Takes an array of words and of tags and returns a 
   * combined String of the form:
   *  <pre>    "The/dt doctor/nn treated/vbd dogs/nns"</pre>
   */
  public static String inlineTags(String[] tokenArray, String[] tagArray) {
    return inlineTags(tokenArray, tagArray, SLASH);   // default delim
  }    
  
  /**
   * Takes a String of words and tags in the format:
   *  <pre>     The/dt doctor/nn treated/vbd dogs/nns</pre>
   * returns an array of the part-of-speech tags.
   * @param wordsAndTags
   */
  public static String[] parseTagString(String wordsAndTags) {
    List tags = new ArrayList();
    String[] data = wordsAndTags.split(SP);
    for (int i = 0; i < data.length; i++)
    {
      String[] tmp = data[i].split(SLASH);
      if (tmp == null || tmp.length != 2)
        throw new RiTaException("Bad tag format: "+data[i]);
      tags.add(tmp[1]);
    }
    return RiTa.strArr(tags);
  }
  
  /**
   * Returns true if <code>pos</code> is a verb
   */
  public static boolean isVerb(String pos)
  {
    return RiPos.isVerb(pos);
  }
  /**
   * Returns true if <code>partOfSpeech</code> is a noun
   */
  public static boolean isNoun(String partOfSpeech)
  {
    return RiPos.isNoun(partOfSpeech);
  }
  /**
   * Returns true if <code>pos</code> is an adverb
   */
  public static boolean isAdverb(String pos)
  {
    return RiPos.isAdverb(pos);
  }
  /**
   * Returns true if <code>pos</code> is an adjective
   */
  public static boolean isAdjective(String pos)
  {
    return RiPos.isAdj(pos);
  }

  /** Returns a String array of the most probable tags */
  public String[] tag(String[] wordArray)
  {
    checkArrayForSpaces(wordArray);
    return tagger.tag(wordArray);
  }

  /** 
   * Returns a String with pos-tags notated inline in the format:
   *  <pre>    "The/dt doctor/nn treated/vbd dogs/nns"</pre>
   */
  public String tagInline(String[] tokens)
  {
    checkArrayForSpaces(tokens);
    return tagger.tagInline(tokens); 
  }
  
  /** 
   * Tokenizes the input sentence using the defaultTokenizer
   * and returns a String with pos-tags notated inline 
   */
  public String tagInline(String sentence)
  {
    return tagger.tagInline(RiTa.tokenize(sentence));
  } 
  
  /** 
   * Loads a file, splits the input into sentences and returns 
   * a single String[] with all the pos-tags from the text. 
   */
  public String[] tagFile(String fileName)
  {
    return tagger.tagFile(fileName);
  }

  private void checkArrayForSpaces(String[] words)
  {
    for (int i = 0; i < words.length; i++)
    {
      if (words[i].contains(SP)) 
        throw new RiTaException("Expecting a single word"
            + " (with no spaces), but found: '"+words[i]+"'");
    }
  }
  
  public static void main(String[] args)
  {
    System.out.println(RiTa.asList(new PosTagger().tag(new String[] {"asdfaasd"})));
  }
  
}// end
