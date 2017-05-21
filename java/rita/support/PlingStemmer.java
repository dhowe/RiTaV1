package rita.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/** 
  This class is closely based on the PlingStemmer stemmer implementation 
  included in the Java Tools pacakge (see http://mpii.de/yago-naga/javatools).
  It is licensed under the Creative Commons Attribution License 
  (see http://creativecommons.org/licenses/by/3.0).

  Stems an English noun (plural or singular) to its singular
  form, including irregular forms, e.g.,  "firemen"->"fireman"
  and "appendices"->"appendix" 
 
  There are a number of word forms that can either be plural or singular.
  Examples include "physics" (the science or the plural of "physic" (the
  medicine)), "quarters" (the housing or the plural of "quarter" (1/4))
  or "people" (the singular of "peoples" or the plural of "person"). In
  these cases, the stemmer assumes the word is a plural form and returns
  the singular form. The methods isPlural, isSingular and isPluralAndSingular
  can be used to differentiate the cases.<P>

  The PlingStemmer uses material from <A HREF=http://wordnet.princeton.edu/>WordNet</A>.<P>
*/
public class PlingStemmer implements StemmerIF, Constants
{
  /** Tells whether a noun is plural. */
  public boolean isPlural(String s) {
    return (!s.equals(stem(s)));
  }

  /** Tells whether a word form is singular. Note that a word can be both plural and singular */
  public boolean isSingular(String s) {
    return (SINGULAR_AND_PLURAL.contains(s.toLowerCase()) || !isPlural(s));
  }  

  /** 
   * Tells whether a word form is the singular form of one word and at
   * the same time the plural form of another.
   */
  public boolean isSingularAndPlural(String s) {
    return (SINGULAR_AND_PLURAL.contains(s.toLowerCase()));
  }  
 
  /** Cuts a suffix from a string (that is the number of chars given by the suffix) */
  private static String cut(String s, String suffix) {
    return (s.substring(0,s.length()-suffix.length()));
  }

  /** Returns true if a word is probably not Latin */
  private static boolean noLatin(String s) {
    return (s.indexOf('h')>0 || s.indexOf('j')>0 || s.indexOf('k')>0 ||
           s.indexOf('w')>0 || s.indexOf('y')>0 || s.indexOf('z')>0 ||
           s.indexOf("ou")>0 || s.indexOf("sh")>0 || s.indexOf("ch")>0 ||
           s.endsWith("aus"));
  }

  /** Returns true if a word is probably Greek */
  private static boolean greek(String s) {
    return (s.indexOf("ph")>0 || s.indexOf('y')>0 && s.endsWith("nges"));
  }

  /** Caches the last noun */
  private String lastNoun=null;
  
  /** Caches the stemmed form of lastNoun*/
  private String lastNounStemmed=null;

  /** Stems an English noun */
  public String stem(String s) 
  {
     if (s==null) return null;
     
     // Look up cached
     if(s.equals(lastNoun)) return (lastNounStemmed);
     
     lastNoun = s;

     // Handle irregular ones
     String irreg = irregular.get(s);
     if(irreg!=null) return (lastNounStemmed=irreg);

     // -on to -a
     if(categoryON_A.contains(s)) return (lastNounStemmed=cut(s,"a")+"on");

     // -um to -a
     if(categoryUM_A.contains(s)) return (lastNounStemmed=cut(s,"a")+"um");

     // -x to -ices
     if(categoryIX_ICES.contains(s)) return (lastNounStemmed=cut(s,"ices")+"ix");

     // -o to -i
     if(categoryO_I.contains(s)) return (lastNounStemmed=cut(s,"i")+"o");

     // -se to ses
     if(categorySE_SES.contains(s)) return (lastNounStemmed=cut(s,"s"));

     // -is to -es
     if(categoryIS_ES.contains(s) || s.endsWith("theses")) return (lastNounStemmed=cut(s,"es")+"is");

     // -us to -i
     if(categoryUS_I.contains(s)) return (lastNounStemmed=cut(s,"i")+"us");
     
     //Wrong plural (?)
     if(s.endsWith("uses") && (categoryUS_I.contains(cut(s,"uses")+"i") ||
	 s.equals("genuses") || s.equals("corpuses"))) return (lastNounStemmed=cut(s,"es"));

     // -ex to -ices
     if(categoryEX_ICES.contains(s)) return (lastNounStemmed=cut(s,"ices")+"ex");

     // Words that do not inflect in the plural
     if(s.endsWith("ois") || s.endsWith("itis") || category00.contains(s) || categoryICS.contains(s)) return (lastNounStemmed=s);

     // -en to -ina
     // No other common words end in -ina
     if(s.endsWith("ina")) return (lastNounStemmed=cut(s,"en"));

     // -a to -ae
     // No other common words end in -ae (special case for 'pleae')
     if(s.endsWith("ae") && !s.equals("pleae")) return (lastNounStemmed=cut(s,"e"));
     
     // -a to -ata
     // No other common words end in -ata
     if(s.endsWith("ata")) return (lastNounStemmed=cut(s,"ta"));

     // trix to -trices
     // No common word ends with -trice(s)
     if(s.endsWith("trices")) return (lastNounStemmed=cut(s,"trices")+"trix");

     // -us to -us
     //No other common word ends in -us, except for false plurals of French words
     //Catch words that are not latin or known to end in -u
     if(s.endsWith("us") && !s.endsWith("eaus") && !s.endsWith("ieus") && !noLatin(s)
        && !categoryU_US.contains(s)) return (lastNounStemmed=s);

     // -tooth to -teeth
     // -goose to -geese
     // -foot to -feet
     // -zoon to -zoa
     //No other common words end with the indicated suffixes
     if(s.endsWith("teeth")) return (lastNounStemmed=cut(s,"teeth")+"tooth");
     if(s.endsWith("geese")) return (lastNounStemmed=cut(s,"geese")+"goose");
     if(s.endsWith("feet")) return (lastNounStemmed=cut(s,"feet")+"foot");
     if(s.endsWith("zoa")) return (lastNounStemmed=cut(s,"zoa")+"zoon");
     
     // -men to -man
     // -firemen to -fireman
     // etc.  (DCH)
     // TODO: add to rita-js
     if(s.endsWith("men")) return (lastNounStemmed=cut(s,"men")+"man");
     
     // -martinis to -martini
     // -bikinis to -bikini
     // etc.  (DCH)
     // TODO: add to rita-js
     if(s.endsWith("inis")) return (lastNounStemmed=cut(s,"inis")+"ini");

     // -children to -child
     // -schoolchildren to -schoolchild
     // etc.  (DCH)
     // TODO: add to rita-js
     if(s.endsWith("children")) return (lastNounStemmed=cut(s,"ren"));
     
     // -eau to -eaux
     //No other common words end in eaux
     if(s.endsWith("eaux")) return (lastNounStemmed=cut(s,"x"));

     // -ieu to -ieux
     //No other common words end in ieux
     if(s.endsWith("ieux")) return (lastNounStemmed=cut(s,"x"));

     // -nx to -nges
     // Pay attention not to kill words ending in -nge with plural -nges
     // Take only Greek words (works fine, only a handfull of exceptions)
     if(s.endsWith("nges") && greek(s)) return (lastNounStemmed=cut(s,"nges")+"nx");

     // -[sc]h to -[sc]hes
     //No other common word ends with "shes", "ches" or "she(s)"
     //Quite a lot end with "che(s)", filter them out
     if(s.endsWith("shes") || s.endsWith("ches") && !categoryCHE_CHES.contains(s)) return (lastNounStemmed=cut(s,"es"));

     // -ss to -sses
     // No other common singular word ends with "sses"
     // Filter out those ending in "sse(s)"
     if(s.endsWith("sses") && !categorySSE_SSES.contains(s) && !s.endsWith("mousses")) return (lastNounStemmed=cut(s,"es"));

     // -x to -xes
     // No other common word ends with "xe(s)" except for "axe"
     if(s.endsWith("xes") && !s.equals("axes")) return (lastNounStemmed=cut(s,"es"));

     // -[nlw]ife to -[nlw]ives
     //No other common word ends with "[nlw]ive(s)" except for olive
     if(s.endsWith("nives") || s.endsWith("lives") && !s.endsWith("olives") ||
        s.endsWith("wives")) return (lastNounStemmed=cut(s,"ves")+"fe");

     // -[aeo]lf to -ves  exceptions: valve, solve
     // -[^d]eaf to -ves  exceptions: heave, weave
     // -arf to -ves      no exception
     if(s.endsWith("alves") && !s.endsWith("valves") ||
        s.endsWith("olves") && !s.endsWith("solves") ||
        s.endsWith("eaves") && !s.endsWith("heaves") && !s.endsWith("weaves") ||
        s.endsWith("arves") ) return (lastNounStemmed=cut(s,"ves")+"f");

     // -y to -ies
     // -ies is very uncommon as a singular suffix
     // but -ie is quite common, filter them out
     if(s.endsWith("ies") && !categoryIE_IES.contains(s)) return (lastNounStemmed=cut(s,"ies")+"y");

     // -o to -oes
     // Some words end with -oe, so don't kill the "e"
     if(s.endsWith("oes") && !categoryOE_OES.contains(s)) return (lastNounStemmed=cut(s,"es"));

     // -s to -ses
     // -z to -zes
     // no words end with "-ses" or "-zes" in singular
     if(s.endsWith("ses") || s.endsWith("zes") ) return (lastNounStemmed=cut(s,"es"));

     // - to -s
     if(s.endsWith("s") && !s.endsWith("ss") && !s.endsWith("is")) return (lastNounStemmed=cut(s,"s"));

     return (lastNounStemmed=s);
  }
  
  /** Test routine */
  public static void main(String[] argv) throws Exception {   
    PlingStemmer ps = new PlingStemmer();
    System.out.println("Enter an English noun in plural form and press ENTER");
    BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
    while(true) {
      String w=in.readLine();
      System.out.println(ps.stem(w));
      if(ps.isPlural(w)) System.out.println("  (Plural)");
      if(ps.isSingular(w)) System.out.println("  (Singular)");
    }
  }
}
