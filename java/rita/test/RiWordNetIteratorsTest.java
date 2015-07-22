package rita.test;

import static org.junit.Assert.fail;
import static rita.support.QUnitStubs.SILENT;
import static rita.support.QUnitStubs.ok;
import static rita.support.QUnitStubs.println;
import static rita.support.QUnitStubs.setContains;
import static rita.support.QUnitStubs.setEqual;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import rita.RiTa;
import rita.RiWordNet;
import rita.wordnet.WordnetUtil;

/*
 * Compare results to: http://wordnetweb.princeton.edu/perl/webwn
 */
public class RiWordNetIteratorsTest
{
  static final String[] EMPTY = new String[0];

  // TODO: caching of result-lists instead of the iterators themselves is not doing much for performance
  @Test
  public void testIteratorString() 
  {
    String[] pos = { "n", "a", "r", "v", };
    Set s = new HashSet();
    for (int i = 0; i < pos.length; i++)
    {
      Iterator it = w.iterator(pos[i]);
      int numOfItems = 0;
      while (it.hasNext())
      {
        String word = (String) it.next();
        String wpos = w.getPosStr(word);
        s.add(wpos);
        ok(word);
        if (!wpos.contains(pos[i]))
          System.err.println("FAIL: " + word + "(" + wpos + ") does not contain: "
              + pos[i]);
        // ok(wpos.contains(pos[i]));
        numOfItems++;
      }
      // System.out.println(numOfItems+" of "+pos[i]);
      ok(numOfItems > 50);
    }
  }
  
  @Test
  public void testIgnoreCompoundWordsBoolean()
  {

    String[] expected = { "collectable", "constable", "eatable", "inevitable" };
    w.ignoreCompoundWords(true);
    String[] result = w.getEndsWith("table", "n", 4);
    setEqual(expected, result);

    String[] expected2 = { "actuarial table", "card table", "billiard table",
        "breakfast table" };
    w.ignoreCompoundWords(false);
    String[] result2 = w.getEndsWith("table", "n", 4);
    setEqual(expected2, result2);
    setEqual(EMPTY, w.getEndsWith("table", "j", 4));
  }

  @Test
  public void testOrFilterIntArrayStringArrayStringInt() 
  {
    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);

    int[] filters2 = { RiWordNet.CONTAINS };
    String[] patterns2 = { "azz" };
    String[] result2 = w.orFilter(filters2, patterns2, "n", 4);
    
    // printArr(result2);
    ok(result2.length <= 4);
    for (int i = 0; i < result2.length; i++)
      ok(result2[i].contains("azz"));

    int[] filters3 = { RiWordNet.STARTS_WITH, RiWordNet.CONTAINS };
    String[] patterns3 = { "ja", "azz" };
    String[] result3 = w.orFilter(filters3, patterns3, "n", 4);
    // printArr(result3);
    ok(result3.length <= 4);
    for (int i = 0; i < result3.length; i++)
      ok(result3[i].startsWith("ja") || result3[i].contains("azz"));

    int[] filters4 = { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
    String[] patterns4 = { "ax", "le" };
    String[] result4 = w.orFilter(filters4, patterns4, "n", 100);
    // printArr(result4);
    ok(result4.length <= 100);

    for (int i = 0; i < result4.length; i++)
    {
      ok(result4[i].startsWith("ax") || result4[i].endsWith("le"));
    }
    
    try
    {
      filters4 = new int[] { RiWordNet.STARTS_WITH };
      patterns4 = new String[] { "ax", "le" };
      w.orFilter(filters4, patterns4, "n", 100);
      fail("no exception");
    }
    catch (Throwable e)
    {
      ok(1);
    }
    
    try
    {
      filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
      patterns4 = new String[] { "ax" };
      w.orFilter(filters4, patterns4, "n", 100);
    }
    catch (Throwable e)
    {
      ok(1);
    }
    
    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    filters2 = new int[] { RiWordNet.CONTAINS };
    patterns2 = new String[] { "azz" };
    result2 = w.orFilter(filters2, patterns2, "n", 4);
    // printArr(result2);
    for (int i = 0; i < result2.length; i++)
    {
      ok(result2[i].contains("azz"));
      ok(result2.length <= 4);
    }

    filters3 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.CONTAINS };
    patterns3 = new String[] { "ja", "azz" };
    result3 = w.orFilter(filters3, patterns3, "n", 4);
    // printArr(result3);
    for (int i = 0; i < result3.length; i++)
    {
      ok(result3[i].startsWith("ja") || result3[i].contains("azz"));
      ok(result3.length <= 4);

    }

    filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
    patterns4 = new String[] { "ax", "le" };
    result4 = w.orFilter(filters4, patterns4, "n", 100);
    // printArr(result4);
    for (int i = 0; i < result4.length; i++)
    {
      ok(result4[i].startsWith("ax") || result4[i].endsWith("le"));
      ok(result4.length <= 100);
    }

    filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
    patterns4 = new String[] { "ax", "le" };
    setEqual(EMPTY, w.orFilter(filters4, patterns4, "j", 100));


    try
    {
      filters4 = new int[] { RiWordNet.STARTS_WITH };
      patterns4 = new String[] { "ax", "le" };
      w.orFilter(filters4, patterns4, "n", 100);
      fail("no exception");
    }
    catch (Throwable e)
    {
      ok(1);
    }
    
    try
    {
      filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
      patterns4 = new String[] { "ax" };
      w.orFilter(filters4, patterns4, "n", 100);
    }
    catch (Throwable e)
    {
      ok(1);
    }
    
    filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
    patterns4 = new String[] { "ax", "le" };
    setEqual(EMPTY, w.orFilter(filters4, patterns4, "j", 100));
  }

  @Test
  public void testOrFilterIntArrayStringArrayString()
  {
    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);

    int[] filters2 = { RiWordNet.CONTAINS };
    String[] patterns2 = { "azz" };
    String[] result2 = w.orFilter(filters2, patterns2, "n");
    // //printArr(result2);
    for (int i = 0; i < result2.length; i++)
    {
      ok(result2[i].contains("azz"));
    }

    int[] filters3 = { RiWordNet.STARTS_WITH, RiWordNet.CONTAINS };
    String[] patterns3 = { "ja", "azz" };
    String[] result3 = w.orFilter(filters3, patterns3, "n");
    // //printArr(result3);
    for (int i = 0; i < result3.length; i++)
    {
      ok(result3[i].startsWith("ja") || result3[i].contains("azz"));

    }

    int[] filters4 = { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
    String[] patterns4 = { "ax", "le" };
    String[] result4 = w.orFilter(filters4, patterns4, "n");
    // //printArr(result4);
    for (int i = 0; i < result4.length; i++)
    {
      ok(result4[i].startsWith("ax") || result4[i].endsWith("le"));

    }

    filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
    patterns4 = new String[] { "ax", "le" };
    setEqual(EMPTY, w.orFilter(filters4, patterns4, "j"));

    try
    {
      filters4 = new int[] { RiWordNet.STARTS_WITH };
      patterns4 = new String[] { "ax", "le" };
      w.orFilter(filters4, patterns4, "n");
      fail("no exception");
    }
    catch (Throwable e)
    {
      ok(1);
    }
    
    try
    {
      filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
      patterns4 = new String[] { "ax" };
      w.orFilter(filters4, patterns4, "n");
    }
    catch (Throwable e)
    {
      ok(1);
    }

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    filters2 = new int[] { RiWordNet.CONTAINS };
    patterns2 = new String[] { "azz" };
    result2 = w.orFilter(filters2, patterns2, "n");
    // //printArr(result2);
    for (int i = 0; i < result2.length; i++)
    {
      ok(result2[i].contains("azz"));
    }

    filters3 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.CONTAINS };
    patterns3 = new String[] { "ja", "azz" };
    result3 = w.orFilter(filters3, patterns3, "n");
    // //printArr(result3);
    for (int i = 0; i < result3.length; i++)
    {
      ok(result3[i].startsWith("ja") || result3[i].contains("azz"));

    }

    filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
    patterns4 = new String[] { "ax", "le" };
    result4 = w.orFilter(filters4, patterns4, "n");
    // //printArr(result4);
    for (int i = 0; i < result4.length; i++)
    {
      ok(result4[i].startsWith("ax") || result4[i].endsWith("le"));
    }

    try
    {
      filters4 = new int[] { RiWordNet.STARTS_WITH };
      patterns4 = new String[] { "ax", "le" };
      w.orFilter(filters4, patterns4, "n");
      fail("no exception");
    }
    catch (Throwable e)
    {
      ok(1);
    }
    
    try
    {
      filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
      patterns4 = new String[] { "ax" };
      w.orFilter(filters4, patterns4, "n");
    }
    catch (Throwable e)
    {
      ok(1);
    }
    
    filters4 = new int[] { RiWordNet.STARTS_WITH, RiWordNet.ENDS_WITH };
    patterns4 = new String[] { "ax", "le" };
    setEqual(EMPTY, w.orFilter(filters4, patterns4, "j"));

  }

  @Test
  public void testGetContainsStringStringInt()
  {

    String[] expected = { "attractiveness", "reactive depression", "active application",
        "active matrix screen", "benefactive role",
        "highly active antiretroviral therapy", "radioactive iodine excretion test",
        "surface-active agent", "active placebo", "inactiveness", "unattractiveness",
        "active trust", "interactive multimedia", "radioactive iodine test",
        "active air defense", "psychoactive drug", "refractive index",
        "active transport", "active citizen", "attractive feature", "active site",
        "high-level radioactive waste", "reactive schizophrenia", "attractive nuisance",
        "activewear", "primary subtractive color for light", "activeness",
        "primary subtractive colour for light", "refractiveness", "active agent",
        "attractive force", "c-reactive protein", "radioactive material",
        "radioactive iodine uptake test", "radioactive decay", "psychoactive substance",
        "low-level radioactive waste", "active immunity", "active voice",
        "radioactive waste", "radioactive dating", "radioactive dust", "active birth",
        "interactive multimedia system" };
    // printArr(w.getContains("active","n"));
    setContainsMulti(expected, "getContains", "active", "n", 3);

    String[] expected2 = { "kite tail", "avalokitesvara", "kitembilla", "greenockite",
        "avalokiteshvara", "swallow-tailed kite", "black kite", "kite balloon",
        "stunt kite", "box kite", "blatherskite", "melkite", "sport kite", "samarskite",
        "white-tailed kite", "hell-kite" };
    // printArr(w.getContains("kite","n"));
    setContainsMulti(expected2, "getContains", "kite", "n", 2);

    String[] expected5 = {};
    setContainsMulti(expected5, "getContains", "kittxx", "n", 2);

    String[] expected6 = { "hell-kite", "avalokitesvara", "black kite", "blatherskite",
        "avalokiteshvara", "white-tailed kite", "sport kite", "stunt kite",
        "kite balloon", "kite tail", "box kite", "hell-kite", "samarskite", "kitembilla",
        "melkite", "greenockite", "swallow-tailed kite" };
    setContainsMulti(expected6, "getContains", "kite", "n", 2000);

    setContains(w.getContains("kite", "a", 2), EMPTY);
    setContains(w.getContains("kitxx", "a", 2000), EMPTY);
  }

  @Test
  public void testGetContainsStringString()
  {
    String[] expected = { "reactive depression", "highly active antiretroviral therapy",
        "active immunity", "primary subtractive colour for light", "attractive feature",
        "radioactive iodine excretion test", "c-reactive protein", "radioactive dust",
        "radioactive decay", "radioactive dating", "active agent",
        "reactive schizophrenia", "active transport", "radioactive iodine uptake test",
        "active birth", "surface-active agent", "unattractiveness", "inactiveness",
        "psychoactive substance", "interactive multimedia system", "psychoactive drug",
        "attractive force", "activewear", "interactive multimedia",
        "low-level radioactive waste", "active trust", "active citizen",
        "radioactive material", "active site", "radioactive waste", "refractive index",
        "activeness", "attractiveness", "active placebo", "refractiveness",
        "primary subtractive color for light", "radioactive iodine test", "active voice",
        "high-level radioactive waste", "active air defense", "active matrix screen",
        "active application", "attractive nuisance", "benefactive role" };
    setEqualMulti(expected, "getContains", "active", "n");

    String[] expected2 = { "hell-kite", "melkite", "kitembilla", "blatherskite",
        "box kite", "stunt kite", "sport kite", "kite balloon", "greenockite",
        "kite tail", "swallow-tailed kite", "black kite", "white-tailed kite",
        "avalokiteshvara", "avalokitesvara", "samarskite" };
    // printArr(result2);
    setEqualMulti(expected2, "getContains", "kite", "n");

    String[] expected4 = {};
    setEqualMulti(expected4, "getContains", "kitxx", "n");

    String[] expected5 = { "expostulate", "postpose", "apostatize", "compost",
        "postdate", "apostrophise", "apostatise", "apostrophize", "signpost", "postmark",
        "postulate", "hypostatize", "riposte", "postpone", "change posture",
        "hypostatise", "posture" };
    setEqualMulti(expected5, "getContains", "post", "v");
    // printArr(w.getContains("post","v"));

    String[] expected7 = {};
    setEqualMulti(expected7, "getContains", "brutally", "r");
    // //printArr(w.getContains("brutally","r"));

    String[] expected6 = { "indefinite" };
    ;
    setEqualMulti(expected6, "getContains", "definite", "a");
    // //printArr(w.getContains("definite","a"))

    setEqualMulti(EMPTY, "getContains", "Definite", "a");
    setEqualMulti(EMPTY, "getContains", "DefIniTe", "a");
    setEqualMulti(EMPTY, "getContains", "definite ", "a");
    setEqualMulti(EMPTY, "getContains", " definite", "a");
    setEqualMulti(EMPTY, "getContains", " definite ", "a");

    // println(w.getContains("kite", "j"));
    setEqual(w.getContains("kite", "j"), EMPTY);
    setEqual(w.getContains("kitexxx", "j"), EMPTY);
  }

  @Test
  public void testGetEndsWithStringStringInt()
  {
    String[] expected = { "actuarial table", "graduated table", "inevitable",
        "pool table", "writing table", "conference table", "livery stable", "timetable",
        "billiard table", "gaming table", "turntable", "drawing table", "round table",
        "communion table", "refectory table", "tilt-top table", "cruciferous vegetable",
        "statistical table", "portable", "leafy vegetable", "training table",
        "console table", "snooker table", "king arthur's round table",
        "dining-room table", "pingpong table", "gateleg table", "pin table",
        "drafting table", "john constable", "dining table", "water table",
        "operating table", "worktable", "high table", "card table", "tip table",
        "plane table", "stable", "tip-top table", "collectable", "raw vegetable",
        "lord's table", "pedestal table", "coffee table", "parsons table",
        "knight of the round table", "pier table", "dressing table", "dinner table",
        "toilet table", "periodic table", "mortality table", "notable",
        "solanaceous vegetable", "roundtable", "drop-leaf table", "chief constable",
        "table-tennis table", "ping-pong table", "correlation table", "root vegetable",
        "work table", "decision table", "tea table", "breakfast table",
        "police constable", "cocktail table", "trestle table", "potable",
        "file allocation table", "constable", "julienne vegetable", "eatable",
        "kitchen table", "council table", "vegetable" };
    String[] expected2 = { "naris", "genus liparis", "vena maxillaris",
        "sutura intermaxillaris", "dracunculus vulgaris", "rubus flagellaris",
        "calluna vulgaris", "nervus ulnaris", "syringa vulgaris",
        "chorizagrotis auxiliaris", "aspalathus linearis", "genus baccharis",
        "arteria maxillaris", "genus phalaris", "lupus vulgaris", "hydrangea petiolaris",
        "certhia familiaris", "arteria ciliaris", "vena ulnaris", "carlina vulgaris",
        "iliamna ruvularis", "carassius vulgaris", "arteria angularis",
        "phaseolus angularis", "cenchrus ciliaris", "vena supratrochlearis",
        "homarus vulgaris", "vena testicularis", "genus agriocharis", "herb paris",
        "pyorrhea alveolaris", "genus capparis", "thymus vulgaris", "ascaris",
        "canis familiaris", "arteria ulnaris", "capparis", "sciurus vulgaris",
        "lysimachia vulgaris", "truncus atrioventricularis", "botaurus stellaris",
        "ratibida columnaris", "turdus pilaris", "keratosis follicularis",
        "primula vulgaris", "trochlearis", "prunella modularis", "petasites vulgaris",
        "genus paris", "alstonia scholaris", "nervus vestibulocochlearis",
        "university of paris", "lens culinaris", "posterior naris", "eleocharis",
        "genus eleocharis", "polistes annularis", "enterobius vermicularis",
        "silene vulgaris", "pylodictus olivaris", "keratosis pilaris",
        "vena auricularis", "vena angularis", "alectis ciliaris", "sistrurus miliaris",
        "genus hydrocharis", "passiflora ligularis", "idria columnaris",
        "vigna angularis", "berberis vulgaris", "arteria axillaris", "prunella vulgaris",
        "vena retromandibularis", "vena jugularis", "helianthus petiolaris",
        "ixodes scapularis", "liparis", "chamaecyparis", "petunia axillaris",
        "linosyris vulgaris", "barbarea vulgaris", "artemisia vulgaris",
        "articulatio temporomandibularis", "linaria vulgaris", "vena axillaris",
        "lepas fascicularis", "haastia pulvinaris", "acne vulgaris", "arteria basilaris",
        "genus ascaris", "polygala vulgaris", "phalaris", "arteria appendicularis",
        "merluccius bilinearis", "bambusa vulgaris", "passiflora quadrangularis",
        "baccharis pilularis", "senecio vulgaris", "agriocharis",
        "eleocharis acicularis", "vena appendicularis", "alnus vulgaris",
        "vena vestibularis", "saxifraga stellaris", "araucaria columnaris",
        "clematis verticillaris", "paris", "arteria auricularis", "lygus lineolaris",
        "chilopsis linearis", "triturus vulgaris", "arteria testicularis",
        "sabbatia stellaris", "cephalotus follicularis", "noctiluca miliaris",
        "colaptes caper collaris", "genus chamaecyparis", "nierembergia rivularis",
        "beta vulgaris vulgaris", "liparis liparis", "plaster of paris",
        "arctonyx collaris", "baccharis", "pulsatilla vulgaris", "pomoxis annularis",
        "ochotona collaris", "cyathea medullaris", "phaseolus vulgaris",
        "fouquieria columnaris", "senecio triangularis", "sabbatia angularis",
        "solea lascaris", "jacquinia armillaris", "vespula vulgaris", "beta vulgaris",
        "anterior naris", "polaris", "citrullus vulgaris", "aquilegia vulgaris",
        "sturnus vulgaris", "hydrocharis", "satureja vulgaris", "arteria alveolaris" };
    // printArr(w.getEndsWith("aris", "n"));
    setContainsMulti(expected2, "getEndsWith", "aris", "n", 4);

    // printArr(w.getEndsWith("table", "n"));
    setContainsMulti(expected, "getEndsWith", "table", "n", 5);
    setContainsMulti(expected, "getEndsWith", "table", "n", 1500);

    String[] expected3 = { "bring", "get moving", "get rolling", "keep going",
        "sight-sing", "hamstring", "sing", "hit the ceiling", "string", "sling", "fling",
        "get going", "cling", "get weaving", "unstring", "sting", "ping", "swing",
        "ding", "get cracking", "come into being", "ting", "have it coming", "sightsing",
        "send packing", "practice bundling", "ring", "spring", "wring", "wing" };
    // printArr(w.getEndsWith("ing", "v"));
    setContainsMulti(expected3, "getEndsWith", "ing", "v", 5);

    String[] expected4 = { "interspecies", "behind-the-scenes", "intra vires",
        "ultra vires", "offsides", "dressed to the nines", "isosceles",
        "in small stages", "in straitened circumstances", "in series", "intraspecies",
        "too big for one's breeches" };
    // printArr(w.getEndsWith("es", "a"));
    setContainsMulti(expected4, "getEndsWith", "es", "a", 5);

    String[] expected5 = { "under the circumstances", "when the time comes",
        "four times", "thousand times", "at times", "three times", "betimes",
        "two times", "in large quantities", "unawares", "besides", "six times",
        "for all practical purposes", "a million times", "as the crow flies", "ofttimes",
        "ultra vires", "a hundred times", "in spades", "for all intents and purposes",
        "oftentimes", "by small degrees", "in stages", "to all intents and purposes",
        "sometimes", "nine times", "by inches", "in circles" };
    // printArr(w.getEndsWith("es", "r"));
    setContainsMulti(expected5, "getEndsWith", "es", "r", 5);

    String[] expected6 = {};
    setContainsMulti(expected6, "getEndsWith", "qwes", "r", 10);

    setEqual(w.getEndsWith("razilll", "j", 40), EMPTY);
  }

  @Test
  public void testGetEndsWithStringString()
  {
    String[] expected = { "actuarial table", "graduated table", "inevitable",
        "pool table", "writing table", "conference table", "livery stable", "timetable",
        "billiard table", "gaming table", "turntable", "drawing table", "round table",
        "communion table", "refectory table", "tilt-top table", "cruciferous vegetable",
        "statistical table", "portable", "leafy vegetable", "training table",
        "console table", "snooker table", "king arthur's round table",
        "dining-room table", "pingpong table", "gateleg table", "pin table",
        "drafting table", "john constable", "dining table", "water table",
        "operating table", "worktable", "high table", "card table", "tip table",
        "plane table", "stable", "tip-top table", "collectable", "raw vegetable",
        "lord's table", "pedestal table", "coffee table", "parsons table",
        "knight of the round table", "pier table", "dressing table", "dinner table",
        "toilet table", "periodic table", "mortality table", "notable",
        "solanaceous vegetable", "roundtable", "drop-leaf table", "chief constable",
        "table-tennis table", "ping-pong table", "correlation table", "root vegetable",
        "work table", "decision table", "tea table", "breakfast table",
        "police constable", "cocktail table", "trestle table", "potable",
        "file allocation table", "constable", "julienne vegetable", "eatable",
        "kitchen table", "council table", "vegetable" };
    // printArr(w.getEndsWith("table", "n"));
    setEqualMulti(expected, "getEndsWith", "table", "n");

    String[] expected2 = { "naris", "genus liparis", "vena maxillaris",
        "sutura intermaxillaris", "dracunculus vulgaris", "rubus flagellaris",
        "calluna vulgaris", "nervus ulnaris", "syringa vulgaris",
        "chorizagrotis auxiliaris", "aspalathus linearis", "genus baccharis",
        "arteria maxillaris", "genus phalaris", "lupus vulgaris", "hydrangea petiolaris",
        "certhia familiaris", "arteria ciliaris", "vena ulnaris", "carlina vulgaris",
        "iliamna ruvularis", "carassius vulgaris", "arteria angularis",
        "phaseolus angularis", "cenchrus ciliaris", "vena supratrochlearis",
        "homarus vulgaris", "vena testicularis", "genus agriocharis", "herb paris",
        "pyorrhea alveolaris", "genus capparis", "thymus vulgaris", "ascaris",
        "canis familiaris", "arteria ulnaris", "capparis", "sciurus vulgaris",
        "lysimachia vulgaris", "truncus atrioventricularis", "botaurus stellaris",
        "ratibida columnaris", "turdus pilaris", "keratosis follicularis",
        "primula vulgaris", "trochlearis", "prunella modularis", "petasites vulgaris",
        "genus paris", "alstonia scholaris", "nervus vestibulocochlearis",
        "university of paris", "lens culinaris", "posterior naris", "eleocharis",
        "genus eleocharis", "polistes annularis", "enterobius vermicularis",
        "silene vulgaris", "pylodictus olivaris", "keratosis pilaris",
        "vena auricularis", "vena angularis", "alectis ciliaris", "sistrurus miliaris",
        "genus hydrocharis", "passiflora ligularis", "idria columnaris",
        "vigna angularis", "berberis vulgaris", "arteria axillaris", "prunella vulgaris",
        "vena retromandibularis", "vena jugularis", "helianthus petiolaris",
        "ixodes scapularis", "liparis", "chamaecyparis", "petunia axillaris",
        "linosyris vulgaris", "barbarea vulgaris", "artemisia vulgaris",
        "articulatio temporomandibularis", "linaria vulgaris", "vena axillaris",
        "lepas fascicularis", "haastia pulvinaris", "acne vulgaris", "arteria basilaris",
        "genus ascaris", "polygala vulgaris", "phalaris", "arteria appendicularis",
        "merluccius bilinearis", "bambusa vulgaris", "passiflora quadrangularis",
        "baccharis pilularis", "senecio vulgaris", "agriocharis",
        "eleocharis acicularis", "vena appendicularis", "alnus vulgaris",
        "vena vestibularis", "saxifraga stellaris", "araucaria columnaris",
        "clematis verticillaris", "paris", "arteria auricularis", "lygus lineolaris",
        "chilopsis linearis", "triturus vulgaris", "arteria testicularis",
        "sabbatia stellaris", "cephalotus follicularis", "noctiluca miliaris",
        "colaptes caper collaris", "genus chamaecyparis", "nierembergia rivularis",
        "beta vulgaris vulgaris", "liparis liparis", "plaster of paris",
        "arctonyx collaris", "baccharis", "pulsatilla vulgaris", "pomoxis annularis",
        "ochotona collaris", "cyathea medullaris", "phaseolus vulgaris",
        "fouquieria columnaris", "senecio triangularis", "sabbatia angularis",
        "solea lascaris", "jacquinia armillaris", "vespula vulgaris", "beta vulgaris",
        "anterior naris", "polaris", "citrullus vulgaris", "aquilegia vulgaris",
        "sturnus vulgaris", "hydrocharis", "satureja vulgaris", "arteria alveolaris" };
    // printArr(w.getEndsWith("aris", "n"));
    setEqualMulti(expected2, "getEndsWith", "aris", "n");

    String[] expected3 = { "bring", "get moving", "get rolling", "keep going",
        "sight-sing", "hamstring", "sing", "hit the ceiling", "string", "sling", "fling",
        "get going", "cling", "get weaving", "unstring", "sting", "ping", "swing",
        "ding", "get cracking", "come into being", "ting", "have it coming", "sightsing",
        "send packing", "practice bundling", "ring", "spring", "wring", "wing" };
    // printArr(w.getEndsWith("ing", "v"));
    setEqualMulti(expected3, "getEndsWith", "ing", "v");

    String[] expected4 = { "interspecies", "behind-the-scenes", "intra vires",
        "ultra vires", "offsides", "dressed to the nines", "isosceles",
        "in small stages", "in straitened circumstances", "in series", "intraspecies",
        "too big for one's breeches" };
    // printArr(w.getEndsWith("es", "a"));
    setEqualMulti(expected4, "getEndsWith", "es", "a");

    String[] expected5 = { "under the circumstances", "when the time comes",
        "four times", "thousand times", "at times", "three times", "betimes",
        "two times", "in large quantities", "unawares", "besides", "six times",
        "for all practical purposes", "a million times", "as the crow flies", "ofttimes",
        "ultra vires", "a hundred times", "in spades", "for all intents and purposes",
        "oftentimes", "by small degrees", "in stages", "to all intents and purposes",
        "sometimes", "nine times", "by inches", "in circles" };
    // printArr(w.getEndsWith("es", "r"));
    setEqualMulti(expected5, "getEndsWith", "es", "r");

    String[] expected6 = {};
    setEqualMulti(expected6, "getEndsWith", "qwes", "r");

    setEqual(w.getEndsWith("razilll", "j"), EMPTY);
  }

  @Test
  public void testGetStartsWithStringStringInt()
  {

    String[] expected = { "wearing", "weary willie", "weariness", "wearing away",
        "wearer", "wearable", "wearing apparel", "wear and tear" };
    // printArr(w.getStartsWith("wear", "n"));
    setContainsMulti(expected, "getStartsWith", "wear", "n", 5);

    String[] expected2 = { "young girl", "yolk", "young turk", "yob", "yosemite falls",
        "yogi", "yottabyte", "young man", "youthfulness", "yogacara", "yo-yo",
        "yosemite", "yobibyte", "youth", "youth subculture", "young carnivore", "yobo",
        "yoko ono", "yodeling", "yobibit", "young fish", "yorkshire pudding", "yowl",
        "yorktown", "yogurt", "youth movement", "yore", "yobbo", "york", "young buck",
        "youngstown", "yottabit", "youngness", "yorkshire fog", "yogi berra",
        "youngster", "yodel", "yokel", "youth culture", "young mammal", "yoke",
        "yom kippur war", "young lady", "youth-on-age", "yoghourt", "younker",
        "young person", "yorkshire", "yodh", "young woman", "yokohama", "yoruba",
        "yolk sac", "youth gang", "you-drive", "yom kippur", "young", "yosemite toad",
        "yoga", "yoghurt", "youth hostel", "yosemite national park", "yodeller",
        "young bird", "young's modulus", "yokuts", "youth crusade", "yorkshire terrier" };
    // printArr(w.getStartsWith("yo", "n"));
    setContainsMulti(expected2, "getStartsWith", "yo", "n", 4);

    String[] expected3 = { "yodel", "yoke", "yowl" };
    // printArr(w.getStartsWith("yo", "v"));
    setContainsMulti(expected3, "getStartsWith", "yo", "v", 10000);

    String[] expected4 = { "young-begetting", "yogic", "youthful", "yokelish", "yon",
        "younger", "yonder", "young-bearing", "youngish", "yokel-like", "young",
        "yogistic" };
    // printArr(w.getStartsWith("yo", "a"));
    setContainsMulti(expected4, "getStartsWith", "yo", "a", 1);

    String[] expected5 = { "you bet", "youthfully", "yon", "yonder", "you said it" };
    // printArr(w.getStartsWith("yo", "r"));
    setContainsMulti(expected5, "getStartsWith", "yo", "r", 5);

    setEqual(w.getStartsWith("razilll", "j", 40), EMPTY);
  }

  @Test
  public void testGetStartsWithStringString()
  {
    String[] expected = { "wearing", "weary willie", "weariness", "wearing away",
        "wearer", "wearable", "wearing apparel", "wear and tear" };
    // printArr(w.getStartsWith("wear", "n"));
    setEqualMulti(expected, "getStartsWith", "wear", "n");

    String[] expected2 = { "young girl", "yolk", "young turk", "yob", "yosemite falls",
        "yogi", "yottabyte", "young man", "youthfulness", "yogacara", "yo-yo",
        "yosemite", "yobibyte", "youth", "youth subculture", "young carnivore", "yobo",
        "yoko ono", "yodeling", "yobibit", "young fish", "yorkshire pudding", "yowl",
        "yorktown", "yogurt", "youth movement", "yore", "yobbo", "york", "young buck",
        "youngstown", "yottabit", "youngness", "yorkshire fog", "yogi berra",
        "youngster", "yodel", "yokel", "youth culture", "young mammal", "yoke",
        "yom kippur war", "young lady", "youth-on-age", "yoghourt", "younker",
        "young person", "yorkshire", "yodh", "young woman", "yokohama", "yoruba",
        "yolk sac", "youth gang", "you-drive", "yom kippur", "young", "yosemite toad",
        "yoga", "yoghurt", "youth hostel", "yosemite national park", "yodeller",
        "young bird", "young's modulus", "yokuts", "youth crusade", "yorkshire terrier" };
    // printArr(w.getStartsWith("yo", "n"));
    setEqualMulti(expected2, "getStartsWith", "yo", "n");

    String[] expected3 = { "yodel", "yoke", "yowl" };
    // printArr(w.getStartsWith("yo", "v"));
    setEqualMulti(expected3, "getStartsWith", "yo", "v");

    String[] expected4 = { "young-begetting", "yogic", "youthful", "yokelish", "yon",
        "younger", "yonder", "young-bearing", "youngish", "yokel-like", "young",
        "yogistic" };
    // printArr(w.getStartsWith("yo", "a"));
    setEqualMulti(expected4, "getStartsWith", "yo", "a");

    String[] expected5 = { "you bet", "youthfully", "yon", "yonder", "you said it" };
    // printArr(w.getStartsWith("yo", "r"));
    setEqualMulti(expected5, "getStartsWith", "yo", "r");

    setEqual(w.getStartsWith("razilll", "j"), EMPTY);
  }

  @Test
  public void testGetRegexMatchStringStringInt()
  {

    String[] expected = { "roundtable", "constable", "tilt-top table", "pingpong table",
        "solanaceous vegetable", "julienne vegetable", "round table", "kitchen table",
        "decision table", "lord's table", "timetable", "tip table", "ping-pong table",
        "gateleg table", "refectory table", "file allocation table",
        "cruciferous vegetable", "pin table", "stable", "coffee table",
        "drop-leaf table", "work table", "dressing table", "leafy vegetable",
        "conference table", "correlation table", "statistical table", "snooker table",
        "trestle table", "john constable", "vegetable", "king arthur's round table",
        "actuarial table", "billiard table", "raw vegetable", "drawing table",
        "plane table", "cocktail table", "tip-top table", "police constable",
        "parsons table", "high table", "tea table", "console table", "table",
        "card table", "worktable", "drafting table", "collectable", "livery stable",
        "mortality table", "dining-room table", "periodic table", "water table",
        "pool table", "chief constable", "breakfast table", "eatable", "communion table",
        "writing table", "pedestal table", "graduated table", "inevitable",
        "dinner table", "dining table", "turntable", "portable", "root vegetable",
        "toilet table", "potable", "notable", "pier table", "training table",
        "table-tennis table", "gaming table", "council table",
        "knight of the round table", "operating table" };
    setContainsMulti(expected, "getRegexMatch", ".*table", "n", 5);

    String[] expected2 = { "twin", "wain", "quin", "shin", "whin", "erin", "loin",
        "gain", "vein", "coin", "grin", "odin", "chin", "main", "ayin", "ruin", "spin",
        "cain", "asin", "join", "rain", "rein", "skin", "pain" };
    // //printArr(w.getRegexMatch(".[^A-Z]in","n"));
    setContainsMulti(expected2, "getRegexMatch", ".[^A-Z]in", "n", 5000);

    String[] expected3 = {};
    // //printArr(w.getRegexMatch("in[du]","n"));
    setContainsMulti(expected3, "getRegexMatch", "in[du]", "n", 5);

    String[] expected4 = { "sin", "din", "win", "gin", "pin", "tin", "fin", "bin" };
    // //printArr(w.getRegexMatch("[^A-Z]in","v"));
    setContainsMulti(expected4, "getRegexMatch", "[^A-Z]in", "v", 5);

    String[] expected5 = { "hell-for-leather", "either", "hither and thither",
        "one after the other", "altogether", "rather", "hither", "farther",
        "all together", "together", "thither", "further", "one after another" };
    // //printArr(w.getRegexMatch(".*ther","r"));
    setContainsMulti(expected5, "getRegexMatch", ".*ther", "r", 5);

    String[] expected6 = { "ain", "kin" };
    // //printArr(w.getRegexMatch("[^A-Z]in","a"));
    setContainsMulti(expected6, "getRegexMatch", "[^A-Z]in", "a", 5);

    setContains(w.getRegexMatch("[^A-Z]in", "o"), EMPTY);
    setContains(w.getRegexMatch("*****ther", "n"), EMPTY);
    setContains(w.getRegexMatch("*****ther", "p"), EMPTY);
  }

  @Test
  public void testGetRegexMatchStringString()
  {
    String[] expected = { "decision table", "correlation table", "round table",
        "plane table", "inevitable", "breakfast table", "mortality table",
        "drop-leaf table", "eatable", "coffee table", "pin table",
        "cruciferous vegetable", "leafy vegetable", "julienne vegetable",
        "writing table", "dining table", "billiard table", "statistical table",
        "knight of the round table", "actuarial table", "roundtable", "tea table",
        "king arthur's round table", "table-tennis table", "police constable",
        "john constable", "kitchen table", "drafting table", "high table",
        "operating table", "periodic table", "dining-room table", "pool table",
        "gaming table", "council table", "collectable", "tip-top table",
        "root vegetable", "cocktail table", "card table", "graduated table",
        "water table", "notable", "stable", "solanaceous vegetable", "constable",
        "dressing table", "conference table", "parsons table", "toilet table",
        "worktable", "tilt-top table", "pier table", "lord's table", "ping-pong table",
        "refectory table", "potable", "gateleg table", "tip table", "vegetable",
        "training table", "work table", "portable", "communion table",
        "file allocation table", "timetable", "livery stable", "trestle table",
        "drawing table", "chief constable", "dinner table", "pedestal table",
        "pingpong table", "console table", "raw vegetable", "snooker table", "table",
        "turntable" };
    // //printArr(w.getRegexMatch(".*table","n"));
    setEqualMulti(expected, "getRegexMatch", ".*table", "n");

    String[] expected2 = { "gain", "coin", "vein", "quin", "cain", "chin", "main",
        "pain", "shin", "ruin", "grin", "rein", "ayin", "asin", "join", "rain", "twin",
        "wain", "skin", "loin", "whin", "odin", "erin", "spin" };
    // //printArr(w.getRegexMatch(".[^A-Z]in","n"));
    setEqualMulti(expected2, "getRegexMatch", ".[^A-Z]in", "n");

    String[] expected3 = {};
    // //printArr(w.getRegexMatch("in[du]","n"));
    setEqualMulti(expected3, "getRegexMatch", "in[du]", "n");

    String[] expected4 = { "sin", "din", "win", "gin", "pin", "tin", "fin", "bin" };
    // //printArr(w.getRegexMatch("[^A-Z]in","v"));
    setEqualMulti(expected4, "getRegexMatch", "[^A-Z]in", "v");

    String[] expected5 = { "hell-for-leather", "either", "hither and thither",
        "one after the other", "altogether", "rather", "hither", "farther",
        "all together", "together", "thither", "further", "one after another" };
    // //printArr(w.getRegexMatch(".*ther","r"));
    setEqualMulti(expected5, "getRegexMatch", ".*ther", "r");

    String[] expected6 = { "ain", "kin" };
    // //printArr(w.getRegexMatch("[^A-Z]in","a"));
    setEqualMulti(expected6, "getRegexMatch", "[^A-Z]in", "a");

    setContains(w.getRegexMatch("[^A-Z]in", "o"), EMPTY);
    setContains(w.getRegexMatch("*****ther", "n"), EMPTY);
    setContains(w.getRegexMatch("*****ther", "p"), EMPTY);
  }

  @Test
  public void testGetSoundsLikeStringStringInt()
  {
    String[] expected = { "tubful", "tipple", "tiepolo", "t-bill", "tableau",
        "tabbouleh", "tea ball", "tube well", "tepal", "tivoli", "tubule", "tuvalu",
        "tabooli", "tupelo" };
    // //printArr(w.getSoundsLike("table", "n"));
    setContainsMulti(expected, "getSoundsLike", "table", "n", 3);

    String[] expected2 = { "pep", "pop", "pib", "pappa", "phobia", "pup", "pave", "pob",
        "pawpaw", "peba", "pupa", "papaya", "peavy", "poof", "peeve", "pub", "paba",
        "payoff", "papaia", "piaf", "puff", "pipe", "pope", "piaffe", "pouf", "poove",
        "pouffe", "poop", "poppy", "peep", "papua", "pap", "pooh-bah", "papaw", "pip",
        "papio", "pipa", "pavo", "peavey", "papa", "phoebe" };
    // //printArr(w.getSoundsLike("puppy", "n"));
    setContainsMulti(expected2, "getSoundsLike", "puppy", "n", 5);

    String[] expected3 = { "peeve", "peep", "pave", "pooh-pooh", "pay up", "pipe", "pip",
        "pee-pee", "pay off", "pop", "puff", "pup" };
    // //printArr(w.getSoundsLike("puppy", "v"));
    setContainsMulti(expected3, "getSoundsLike", "puppy", "v", 300);

    String[] expected5 = { "peppy", "puffy", "pop", "puff" };
    // //printArr(w.getSoundsLike("puppy", "a"));
    setContainsMulti(expected5, "getSoundsLike", "puppy", "a", 2);

    String[] expected6 = { "pop" };
    // //printArr(w.getSoundsLike("puppy", "r"));
    setContainsMulti(expected6, "getSoundsLike", "puppy", "r", 4);

    String[] expected7 = {};
    // //printArr(w.getSoundsLike("dacszff", "r"));
    setContainsMulti(expected7, "getSoundsLike", "dacszff", "r", 5);

    setContains(w.getSoundsLike("table", "o", 10), EMPTY);
  }

  @Test
  public void testGetSoundsLikeStringString()
  {
    String[] expected = { "tubful", "tipple", "tiepolo", "t-bill", "tableau",
        "tabbouleh", "tea ball", "tube well", "tepal", "tivoli", "tubule", "tuvalu",
        "tabooli", "tupelo" };
    // //printArr(w.getSoundsLike("table", "n"));
    setEqualMulti(expected, "getSoundsLike", "table", "n");

    String[] expected2 = { "pep", "pop", "pib", "pappa", "phobia", "pup", "pave", "pob",
        "pawpaw", "peba", "pupa", "papaya", "peavy", "poof", "peeve", "pub", "paba",
        "payoff", "papaia", "piaf", "puff", "pipe", "pope", "piaffe", "pouf", "poove",
        "pouffe", "poop", "poppy", "peep", "papua", "pap", "pooh-bah", "papaw", "pip",
        "papio", "pipa", "pavo", "peavey", "papa", "phoebe" };
    // //printArr(w.getSoundsLike("puppy", "n"));
    setEqualMulti(expected2, "getSoundsLike", "puppy", "n");

    String[] expected3 = { "peeve", "peep", "pave", "pooh-pooh", "pay up", "pipe", "pip",
        "pee-pee", "pay off", "pop", "puff", "pup" };
    // //printArr(w.getSoundsLike("puppy", "v"));
    setEqualMulti(expected3, "getSoundsLike", "puppy", "v");

    String[] expected5 = { "peppy", "puffy", "pop", "puff" };
    // //printArr(w.getSoundsLike("puppy", "a"));
    setEqualMulti(expected5, "getSoundsLike", "puppy", "a");

    String[] expected6 = { "pop" };
    // //printArr(w.getSoundsLike("puppy", "r"));
    setEqualMulti(expected6, "getSoundsLike", "puppy", "r");

    String[] expected7 = {};
    // //printArr(w.getSoundsLike("dacszff", "r"));
    setEqualMulti(expected7, "getSoundsLike", "dacszff", "r");

    setContains(w.getSoundsLike("table", "o"), EMPTY);
  }

  @Test
  public void testGetWildcardMatchStringStringInt()
  {
    String[] expected = { "tale", "tile", "tole" };
    // printArr(w.getWildcardMatch("t?le", "n")); // single-letter
    setContainsMulti(expected, "getWildcardMatch", "t?le", "n", 6);

    String[] expected2 = { "teasle", "tea table", "teakettle", "teasdale" };
    // printArr(w.getWildcardMatch("tea*le", "n")); // multiple-letter
    setContainsMulti(expected2, "getWildcardMatch", "tea*le", "n", 6);

    String[] expected3 = { "din", "min", "win", "yin", "fin", "kin", "sin", "pin", "gin",
        "tin", "qin", "bin", "lin", "hin" };
    // printArr(w.getWildcardMatch("?in", "n")); // single-letter
    setContainsMulti(expected3, "getWildcardMatch", "?in", "n", 4);

    String[] expected4 = { "safekeeping", "weather stripping", "stripping", "whipping",
        "scraping", "gulping", "strip cropping", "keeping", "beekeeping", "minesweeping",
        "landscaping", "table tipping", "shipping", "outcropping", "stovepiping",
        "griping", "showjumping", "blood typing", "double stopping", "limping",
        "cross-country jumping", "camping", "shopping", "overlapping", "dumping",
        "helping", "press clipping", "clopping", "looping", "stadium jumping",
        "chipping", "carping", "tapping", "tissue typing", "timekeeping", "piping",
        "coping", "stopping", "mapping", "leaping", "table rapping", "trapping",
        "double dipping", "trumping", "equipping", "peiping", "jumping", "hand clapping",
        "sleeping", "mopping", "thumping", "sweeping", "developing", "flapping",
        "snipping", "primping", "warping", "taping", "roping", "grouping", "weeping",
        "shaping", "yelping", "clapping", "pole jumping", "burping", "kidnapping",
        "bookkeeping", "teng hsiaoping", "ski jumping", "typing", "newspaper clipping",
        "wrapping", "peacekeeping", "creeping", "dripping", "calf roping", "rasping",
        "gift wrapping", "deng xiaoping", "grasping", "lapping", "chomping",
        "steer roping", "gossiping", "chromosome mapping", "supping",
        "single-entry bookkeeping", "double-entry bookkeeping", "horsewhipping",
        "weatherstripping", "table tapping", "cupping", "clipping", "teng hsiao-ping",
        "name-dropping", "stumping", "spirit rapping", "striping", "blacktopping",
        "touch typing", "topping", "housekeeping", "clumping", "popping", "walloping" };
    // //printArr(w.getWildcardMatch("*ping", "n")); // multiple-letter
    setContainsMulti(expected4, "getWildcardMatch", "*ping", "n", 5);

    String[] expected5 = { "din", "fin", "sin", "bin", "pin", "gin", "tin", "win" };
    // //printArr(w.getWildcardMatch("?in", "v")); // single-letter
    setContainsMulti(expected5, "getWildcardMatch", "?in", "v", 3);

    String[] expected6 = {};
    // //printArr(w.getWildcardMatch("*ping", "v")); // multiple-letter
    setContainsMulti(expected6, "getWildcardMatch", "*ping", "v", 2);

    String[] expected7 = { "kin", "ain" };
    // //printArr(w.getWildcardMatch("?in", "a")); // single-letter
    setContainsMulti(expected7, "getWildcardMatch", "?in", "a", 5);

    String[] expected8 = { "sweeping", "tripping", "thumping", "slipping", "dropping",
        "drooping", "high-stepping", "rasping", "sloping", "ripping", "whopping",
        "topping", "walloping", "unsleeping", "outward-developing", "grasping",
        "napping", "eye-popping", "gaping", "weeping", "enveloping", "groping",
        "nontelescoping", "developing", "shaping", "sleeping", "gripping", "plumping",
        "out or keeping", "strapping", "inward-developing", "peacekeeping",
        "downward-sloping", "whipping", "seeping", "nipping", "stooping" };
    // //printArr(w.getWildcardMatch("*ping", "a")); // multiple-letter
    setContainsMulti(expected8, "getWildcardMatch", "*ping", "a", 5);

    String[] expected9 = {};
    // //printArr(w.getWildcardMatch("?in", "r")); // single-letter
    setContainsMulti(expected9, "getWildcardMatch", "?in", "r", 3);

    String[] expected10 = { "dripping", "whopping", "piping", "sopping" };
    // //printArr(w.getWildcardMatch("*ping", "r")); // multiple-letter
    setContainsMulti(expected10, "getWildcardMatch", "*ping", "r", 20);

    setContains(w.getWildcardMatch("*ping", "j", 6), EMPTY);
  }

  @Test
  public void testGetWildcardMatchStringString()
  {
    String[] expected = { "tale", "tile", "tole" };
    // printArr(w.getWildcardMatch("t?le", "n")); // single-letter
    setEqualMulti(expected, "getWildcardMatch", "t?le", "n");

    String[] expected2 = { "teasle", "tea table", "teakettle", "teasdale" };
    // printArr(w.getWildcardMatch("tea*le", "n")); // multiple-letter
    setEqualMulti(expected2, "getWildcardMatch", "tea*le", "n");

    String[] expected3 = { "din", "min", "win", "yin", "fin", "kin", "sin", "pin", "gin",
        "tin", "qin", "bin", "lin", "hin" };
    // printArr(w.getWildcardMatch("?in", "n")); // single-letter
    setEqualMulti(expected3, "getWildcardMatch", "?in", "n");

    String[] expected4 = { "safekeeping", "weather stripping", "stripping", "whipping",
        "scraping", "gulping", "strip cropping", "keeping", "beekeeping", "minesweeping",
        "landscaping", "table tipping", "shipping", "outcropping", "stovepiping",
        "griping", "showjumping", "blood typing", "double stopping", "limping",
        "cross-country jumping", "camping", "shopping", "overlapping", "dumping",
        "helping", "press clipping", "clopping", "looping", "stadium jumping",
        "chipping", "carping", "tapping", "tissue typing", "timekeeping", "piping",
        "coping", "stopping", "mapping", "leaping", "table rapping", "trapping",
        "double dipping", "trumping", "equipping", "peiping", "jumping", "hand clapping",
        "sleeping", "mopping", "thumping", "sweeping", "developing", "flapping",
        "snipping", "primping", "warping", "taping", "roping", "grouping", "weeping",
        "shaping", "yelping", "clapping", "pole jumping", "burping", "kidnapping",
        "bookkeeping", "teng hsiaoping", "ski jumping", "typing", "newspaper clipping",
        "wrapping", "peacekeeping", "creeping", "dripping", "calf roping", "rasping",
        "gift wrapping", "deng xiaoping", "grasping", "lapping", "chomping",
        "steer roping", "gossiping", "chromosome mapping", "supping",
        "single-entry bookkeeping", "double-entry bookkeeping", "horsewhipping",
        "weatherstripping", "table tapping", "cupping", "clipping", "teng hsiao-ping",
        "name-dropping", "stumping", "spirit rapping", "striping", "blacktopping",
        "touch typing", "topping", "housekeeping", "clumping", "popping", "walloping" };
    // //printArr(w.getWildcardMatch("*ping", "n")); // multiple-letter
    setEqualMulti(expected4, "getWildcardMatch", "*ping", "n");

    String[] expected5 = { "din", "fin", "sin", "bin", "pin", "gin", "tin", "win" };
    // //printArr(w.getWildcardMatch("?in", "v")); // single-letter
    setEqualMulti(expected5, "getWildcardMatch", "?in", "v");

    String[] expected6 = {};
    // //printArr(w.getWildcardMatch("*ping", "v")); // multiple-letter
    setEqualMulti(expected6, "getWildcardMatch", "*ping", "v");

    String[] expected7 = { "kin", "ain" };
    // //printArr(w.getWildcardMatch("?in", "a")); // single-letter
    setEqualMulti(expected7, "getWildcardMatch", "?in", "a");

    String[] expected8 = { "sweeping", "tripping", "thumping", "slipping", "dropping",
        "drooping", "high-stepping", "rasping", "sloping", "ripping", "whopping",
        "topping", "walloping", "unsleeping", "outward-developing", "grasping",
        "napping", "eye-popping", "gaping", "weeping", "enveloping", "groping",
        "nontelescoping", "developing", "shaping", "sleeping", "gripping", "plumping",
        "out or keeping", "strapping", "inward-developing", "peacekeeping",
        "downward-sloping", "whipping", "seeping", "nipping", "stooping" };
    // //printArr(w.getWildcardMatch("*ping", "a")); // multiple-letter
    setEqualMulti(expected8, "getWildcardMatch", "*ping", "a");

    String[] expected9 = {};
    // //printArr(w.getWildcardMatch("?in", "r")); // single-letter
    setEqualMulti(expected9, "getWildcardMatch", "?in", "r");

    String[] expected10 = { "dripping", "whopping", "piping", "sopping" };
    // //printArr(w.getWildcardMatch("*ping", "r")); // multiple-letter
    setEqualMulti(expected10, "getWildcardMatch", "*ping", "r");
    setContains(w.getWildcardMatch("*ping", "j"), EMPTY);
  }

  @Test
  public void testFilterIntStringStringInt()
  {
    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);

    String[] expected10 = { "tablet" };
    String[] result10 = w.filter(RiWordNet.WILDCARD_MATCH, "table?", "n", 3);
    setEqual(expected10, result10);

    String[] expected11 = { "abatable nuisance", "actuarial table", "acceptableness" };
    String[] result11 = w.filter(RiWordNet.WILDCARD_MATCH, "*table*", "n", 3);
    // //printArr(result11);
    setEqual(expected11, result11);

    String[] expected = { "tabbouleh", "tableau", "t-bill" };
    String[] result = w.filter(RiWordNet.SOUNDS_LIKE, "table", "n", 3);
    // //println(result);
    setEqual(expected, result);

    String[] expected2 = { "table-mountain pine", "table-tennis bat",
        "table-tennis racquet" };
    String[] result2 = w.filter(RiWordNet.STARTS_WITH, "table", "n", 3);
    // //printArr(result2);
    setEqual(expected2, result2);

    String[] expected3 = { "bleat" };
    String[] result3 = w.filter(RiWordNet.ANAGRAMS, "table", "n", 3);
    // //printArr(result3);
    setEqual(expected3, result3);

    String[] expected4 = { "acceptableness", "abatable nuisance", "actuarial table" };
    String[] result4 = w.filter(RiWordNet.CONTAINS, "table", "n", 3);
    // //printArr(result4);
    setEqual(expected4, result4);

    String[] expected5 = {};
    String[] result5 = w.filter(RiWordNet.CONTAINS, "tableauu", "n", 3);
    // //printArr(result5);
    setEqual(expected5, result5);

    String[] expected6 = { "actuarial table", "breakfast table", "billiard table" };
    String[] result6 = w.filter(RiWordNet.ENDS_WITH, "table", "n", 3);
    // //printArr(result6);
    setEqual(expected6, result6);

    String[] expected8 = { "ayin", "asin", "cain" };
    String[] result8 = w.filter(RiWordNet.REGEX_MATCH, ".[^A-Z]in", "n", 3);
    // //printArr(result8);
    setEqual(expected8, result8);

    String[] expected9 = { ".22", "0", "'hood" };
    String[] result9 = w.filter(RiWordNet.SIMILAR_TO, "table", "n", 3);
    // //printArr(result9);
    setEqual(expected9, result9);

    String[] expected12 = {};
    String[] result12 = w.filter(RiWordNet.CONTAINS, "nahsuchword", "n", 3);
    // //printArr(result12);
    setEqual(expected12, result12);

    String[] expected14 = { "dining-room table", "palatableness", "intractableness",
        "vegetable sheep", "tableland", "japanese table pine", "water table",
        "table of contents", "permutableness", "comfortableness", "stablemate",
        "table knife", "ratables", "snooker table", "roundtable", "julienne vegetable",
        "stableman", "charitable trust", "drawing table", "uncomfortableness",
        "tip-top table", "table tennis", "excitable area", "billiard table", "worktable",
        "table mustard", "livery stable", "turntable", "table d'hote", "table salt",
        "vegetable ivory", "round table", "king arthur's round table",
        "inflatable cushion", "table tapping", "inevitable accident", "vegetable wax",
        "pedestal table", "periodic table", "acceptableness", "graduated table",
        "tablecloth", "tip table", "ping-pong table", "dressing table",
        "correlation table", "potable", "vegetable marrow", "timetable",
        "vegetable patch", "vegetable oil", "dinner table", "excitableness",
        "trestle table", "vegetable oyster", "tablet", "mutableness",
        "cruciferous vegetable", "portable", "unstableness", "solanaceous vegetable",
        "relocatable program", "card table", "adjustable spanner", "table napkin",
        "communion table", "tea table", "coffee table", "vegetable garden",
        "raw vegetable", "unsuitableness", "notable", "vegetable tallow",
        "inevitableness", "table lifting", "unpalatableness", "table mat",
        "vegetable sponge", "table talk", "console table", "vegetable matter",
        "stableboy", "council table", "high table", "mortality table", "tablespoonful",
        "disreputable person", "kitchen table", "irritable bowel syndrome",
        "statistical table", "augean stables", "stable", "tableware", "pool table",
        "coffee-table book", "writing table", "portable circular saw", "table tilting",
        "leafy vegetable", "stableness", "conference table", "hospitableness",
        "tablefork", "table wine", "tablemate", "table-tennis table", "work table",
        "chief constable", "dining table", "sleeping tablet", "tablespoon",
        "drop-leaf table", "stable gear", "round-table conference", "pier table",
        "inhospitableness", "file allocation table", "plane table",
        "vegetable hummingbird", "drafting table", "table service", "breakfast table",
        "pin table", "stable companion", "actuarial table", "table rapping",
        "table linen", "habitableness", "inevitable", "memorial tablet",
        "profitableness", "tableau vivant", "table saw", "constable", "decision table",
        "disreputableness", "italian vegetable marrow", "tabletop", "eatable",
        "stable factor", "parsons table", "police constable", "root vegetable",
        "suitableness", "tableau", "pingpong table", "table turning", "gaming table",
        "abatable nuisance", "charitableness", "table-mountain pine", "refectory table",
        "collectable", "unacceptableness", "table game", "tilt-top table",
        "gateleg table", "table tipping", "tractableness", "toilet table", "vegetable",
        "adjustable wrench", "portable computer", "training table", "table-tennis bat",
        "cocktail table", "operating table", "table lamp", "lord's table",
        "vegetable silk", "knight of the round table", "immutableness", "portable saw",
        "unprofitableness", "vegetable soup", "john constable", "tablet-armed chair",
        "table-tennis racquet" };
    String[] result14 = w.filter(RiWordNet.CONTAINS, "table", "n", 30000);
    // //printArr(result14);
    setEqual(expected14, result14);

    setContains(w.filter(RiWordNet.CONTAINS, "table", "e", 3), EMPTY);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected10 = new String[] { "tablet" };
    result10 = w.filter(RiWordNet.WILDCARD_MATCH, "table?", "n", 3);

    setEqual(expected10, result10);

    expected11 = new String[] { "collectable", "charitableness", "acceptableness" };
    result11 = w.filter(RiWordNet.WILDCARD_MATCH, "*table*", "n", 3);
    // //printArr(result11);
    setEqual(expected11, result11);

    expected = new String[] { "tabbouleh", "tableau", "t-bill" };
    result = w.filter(RiWordNet.SOUNDS_LIKE, "table", "n", 3);
    // //println(result);
    setEqual(expected, result);

    expected2 = new String[] { "tablecloth", "tablefork", "tableau" };
    result2 = w.filter(RiWordNet.STARTS_WITH, "table", "n", 3);
    // //printArr(result2);
    setEqual(expected2, result2);

    expected3 = new String[] { "bleat" };
    result3 = w.filter(RiWordNet.ANAGRAMS, "table", "n", 3);
    // //printArr(result3);
    setEqual(expected3, result3);

    expected4 = new String[] { "collectable", "acceptableness", "charitableness" };
    result4 = w.filter(RiWordNet.CONTAINS, "table", "n", 3);
    // //printArr(result4);
    setEqual(expected4, result4);

    expected5 = new String[] {};
    result5 = w.filter(RiWordNet.CONTAINS, "tableauu", "n", 3);
    // //printArr(result5);
    setEqual(expected5, result5);

    expected6 = new String[] { "constable", "collectable", "eatable" };
    result6 = w.filter(RiWordNet.ENDS_WITH, "table", "n", 3);
    // //printArr(result6);
    setEqual(expected6, result6);

    expected8 = new String[] { "ayin", "asin", "cain" };
    result8 = w.filter(RiWordNet.REGEX_MATCH, ".[^A-Z]in", "n", 3);
    // //printArr(result8);
    setEqual(expected8, result8);

    expected9 = new String[] { ".22", "0", "'hood" };
    result9 = w.filter(RiWordNet.SIMILAR_TO, "table", "n", 3);
    // //printArr(result9);
    setEqual(expected9, result9);

    expected12 = new String[] {};
    result12 = w.filter(RiWordNet.CONTAINS, "nahsuchword", "n", 3);
    // //printArr(result12);
    setEqual(expected12, result12);

    expected14 = new String[] { "unstableness", "tablecloth", "tractableness",
        "habitableness", "tabletop", "excitableness", "inevitable", "stable",
        "hospitableness", "disreputableness", "immutableness", "ratables",
        "tablespoonful", "suitableness", "worktable", "stableman", "notable",
        "mutableness", "tablet", "vegetable", "profitableness", "palatableness",
        "unacceptableness", "inhospitableness", "charitableness", "unpalatableness",
        "tableau", "stableness", "intractableness", "inevitableness",
        "uncomfortableness", "comfortableness", "collectable", "acceptableness",
        "roundtable", "tableland", "permutableness", "unprofitableness", "constable",
        "eatable", "tablespoon", "stableboy", "potable", "timetable", "tableware",
        "turntable", "tablefork", "tablemate", "portable", "stablemate", "unsuitableness" };
    result14 = w.filter(RiWordNet.CONTAINS, "table", "n", 30000);
    // //printArr(result14);
    setEqual(expected14, result14);

    setContains(w.filter(RiWordNet.CONTAINS, "table", "e", 3), EMPTY);

  }

  @Test
  public void testFilterIntStringString()
  {

    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);

    String[] expected10 = { "tablet" };
    String[] result10 = w.filter(RiWordNet.WILDCARD_MATCH, "table?", "n");
    // printArr(result10);
    setEqual(expected10, result10);

    String[] expected11 = { "table lifting", "constable", "eatable",
        "inevitable accident", "work table", "vegetable garden", "pool table",
        "julienne vegetable", "comfortableness", "cruciferous vegetable",
        "abatable nuisance", "table turning", "solanaceous vegetable",
        "uncomfortableness", "profitableness", "drawing table", "coffee table",
        "roundtable", "table salt", "lord's table", "acceptableness",
        "table-tennis table", "stable gear", "ping-pong table", "water table",
        "pier table", "stable companion", "inflatable cushion", "console table",
        "gateleg table", "actuarial table", "tablet-armed chair",
        "king arthur's round table", "vegetable marrow", "leafy vegetable",
        "root vegetable", "trestle table", "toilet table", "table mustard",
        "memorial tablet", "knight of the round table", "livery stable", "timetable",
        "vegetable sheep", "training table", "portable saw", "portable", "tablefork",
        "relocatable program", "plane table", "disreputableness", "card table",
        "charitableness", "raw vegetable", "tableau vivant", "parsons table",
        "tea table", "sleeping tablet", "vegetable wax", "potable", "vegetable oyster",
        "table linen", "table of contents", "unsuitableness", "snooker table",
        "drafting table", "notable", "mutableness", "coffee-table book",
        "inhospitableness", "conference table", "billiard table", "pedestal table",
        "italian vegetable marrow", "inevitable", "police constable", "vegetable sponge",
        "table-tennis bat", "table talk", "kitchen table", "augean stables",
        "adjustable wrench", "habitableness", "graduated table", "vegetable oil",
        "unacceptableness", "vegetable patch", "table d'hote", "tablet", "suitableness",
        "tip table", "collectable", "john constable", "pin table", "table knife",
        "mortality table", "dressing table", "intractableness", "round-table conference",
        "pingpong table", "chief constable", "communion table", "tablespoonful",
        "permutableness", "disreputable person", "tableau", "refectory table",
        "stablemate", "vegetable soup", "table game", "periodic table", "turntable",
        "immutableness", "file allocation table", "irritable bowel syndrome",
        "tableware", "ratables", "excitable area", "table service", "unprofitableness",
        "adjustable spanner", "stableman", "table napkin", "correlation table",
        "tablecloth", "operating table", "vegetable silk", "dining-room table",
        "unpalatableness", "stableness", "table wine", "table-mountain pine",
        "portable computer", "tip-top table", "tablemate", "cocktail table",
        "breakfast table", "japanese table pine", "dinner table", "drop-leaf table",
        "portable circular saw", "vegetable hummingbird", "stable", "table tapping",
        "stable factor", "council table", "tablespoon", "palatableness", "excitableness",
        "tilt-top table", "gaming table", "high table", "table-tennis racquet",
        "vegetable tallow", "worktable", "decision table", "table rapping",
        "hospitableness", "vegetable ivory", "stableboy", "table tipping",
        "table tennis", "tabletop", "inevitableness", "dining table", "vegetable",
        "writing table", "table tilting", "charitable trust", "unstableness",
        "round table", "vegetable matter", "statistical table", "table lamp",
        "tractableness", "tableland", "table mat", "table saw" };
    String[] result11 = w.filter(RiWordNet.WILDCARD_MATCH, "*table*", "n");
    // printArr(result11);
    setEqual(expected11, result11);

    String[] expected15 = { "drawing table", "stableman", "training table", "table game",
        "constable", "table lifting", "gaming table", "augean stables", "table tilting",
        "high table", "ping-pong table", "raw vegetable", "disreputableness",
        "dining table", "hospitableness", "adjustable spanner", "vegetable tallow",
        "worktable", "communion table", "charitableness", "table linen",
        "table of contents", "leafy vegetable", "portable computer", "tip table",
        "table-tennis bat", "root vegetable", "suitableness", "vegetable soup",
        "vegetable silk", "periodic table", "table service", "table turning", "stable",
        "vegetable", "police constable", "table tapping", "immutableness",
        "drafting table", "irritable bowel syndrome", "council table", "decision table",
        "table tennis", "uncomfortableness", "tableau", "graduated table", "tablefork",
        "tip-top table", "lord's table", "adjustable wrench", "inevitable accident",
        "palatableness", "potable", "vegetable sponge", "tilt-top table",
        "julienne vegetable", "table lamp", "tabletop", "round table", "breakfast table",
        "correlation table", "tea table", "stablemate", "tableware",
        "king arthur's round table", "charitable trust", "roundtable",
        "relocatable program", "pingpong table", "vegetable wax", "table mustard",
        "coffee-table book", "vegetable oyster", "vegetable hummingbird",
        "tableau vivant", "sleeping tablet", "statistical table", "profitableness",
        "collectable", "round-table conference", "mortality table", "coffee table",
        "permutableness", "pin table", "operating table", "stable companion", "portable",
        "eatable", "vegetable ivory", "gateleg table", "snooker table", "tablespoonful",
        "unacceptableness", "inflatable cushion", "cruciferous vegetable",
        "vegetable matter", "inhospitableness", "stable gear", "table-tennis table",
        "drop-leaf table", "knight of the round table", "refectory table", "plane table",
        "tablet", "table tipping", "intractableness", "unsuitableness", "vegetable oil",
        "table wine", "table-tennis racquet", "table saw", "dining-room table",
        "file allocation table", "tableland", "cocktail table", "trestle table",
        "tablemate", "portable circular saw", "stable factor", "actuarial table",
        "acceptableness", "writing table", "table napkin", "chief constable",
        "disreputable person", "vegetable patch", "mutableness", "toilet table",
        "tablet-armed chair", "portable saw", "tablespoon", "habitableness",
        "solanaceous vegetable", "excitable area", "table knife", "excitableness",
        "vegetable sheep", "vegetable marrow", "water table", "dinner table",
        "kitchen table", "work table", "japanese table pine", "table d'hote",
        "pedestal table", "unprofitableness", "stableness", "comfortableness",
        "dressing table", "conference table", "card table", "turntable", "table talk",
        "table rapping", "abatable nuisance", "stableboy", "pier table",
        "unpalatableness", "parsons table", "vegetable garden",
        "italian vegetable marrow", "table-mountain pine", "inevitable", "timetable",
        "tablecloth", "table salt", "pool table", "unstableness", "billiard table",
        "john constable", "inevitableness", "livery stable", "ratables", "console table",
        "tractableness", "notable", "memorial tablet", "table mat" };
    String[] result15 = w.filter(RiWordNet.WILDCARD_MATCH, "*table*", "n");
    // printArr(result15);
    setEqual(expected15, result15);

    String[] expected = { "tube well", "tubful", "tubule", "tea ball", "tipple",
        "tabbouleh", "tupelo", "t-bill", "tableau", "tabooli", "tivoli", "tiepolo",
        "tuvalu", "tepal" };
    String[] result = w.filter(RiWordNet.SOUNDS_LIKE, "table", "n");
    // //printArr(result);
    setEqual(expected, result);

    String[] expected2 = { "table wine", "tableland", "table linen", "tableware",
        "table mat", "tablet", "table-tennis racquet", "table talk",
        "table-tennis table", "tablespoon", "table-mountain pine", "table knife",
        "table game", "table mustard", "tablecloth", "table salt", "table lamp",
        "table napkin", "table-tennis bat", "table d'hote", "tableau vivant",
        "table turning", "table saw", "tabletop", "tablet-armed chair", "table tilting",
        "table service", "tablespoonful", "table tennis", "table rapping",
        "table lifting", "tablemate", "tablefork", "table of contents", "table tipping",
        "table tapping", "tableau" };
    String[] result2 = w.filter(RiWordNet.STARTS_WITH, "table", "n");
    // //printArr(result2);
    setEqual(expected2, result2);

    String[] expected3 = { "bleat" };
    String[] result3 = w.filter(RiWordNet.ANAGRAMS, "table", "n");
    // //printArr(result3);
    setEqual(expected3, result3);

    String[] expected4 = { "adjustable wrench", "round-table conference",
        "conference table", "periodic table", "coffee-table book", "table rapping",
        "trestle table", "portable", "turntable", "writing table", "vegetable matter",
        "stable companion", "adjustable spanner", "plane table", "table service",
        "vegetable wax", "charitable trust", "pin table", "dining-room table",
        "intractableness", "training table", "chief constable", "memorial tablet",
        "dressing table", "portable saw", "tablefork", "tablespoonful",
        "japanese table pine", "potable", "vegetable sponge", "worktable", "stable gear",
        "table of contents", "julienne vegetable", "tablecloth", "table-mountain pine",
        "knight of the round table", "vegetable tallow", "billiard table",
        "stable factor", "immutableness", "drawing table", "leafy vegetable",
        "hospitableness", "stablemate", "water table", "sleeping tablet",
        "king arthur's round table", "inhospitableness", "round table",
        "graduated table", "augean stables", "mortality table", "table-tennis table",
        "constable", "table mat", "work table", "solanaceous vegetable", "unstableness",
        "pool table", "decision table", "tablespoon", "pingpong table", "kitchen table",
        "table saw", "excitableness", "card table", "actuarial table", "table turning",
        "stable", "dining table", "unprofitableness", "operating table",
        "profitableness", "inflatable cushion", "permutableness", "roundtable",
        "pedestal table", "table tilting", "john constable", "table mustard",
        "tableland", "dinner table", "disreputable person", "communion table",
        "inevitableness", "high table", "table salt", "unpalatableness", "mutableness",
        "drafting table", "vegetable", "tip table", "tea table", "vegetable hummingbird",
        "eatable", "table d'hote", "cruciferous vegetable", "notable", "ping-pong table",
        "tablemate", "table talk", "vegetable marrow", "vegetable ivory",
        "parsons table", "habitableness", "drop-leaf table", "vegetable patch",
        "irritable bowel syndrome", "table game", "table linen", "vegetable sheep",
        "table-tennis racquet", "unsuitableness", "vegetable oyster", "tip-top table",
        "tablet-armed chair", "breakfast table", "suitableness", "refectory table",
        "table wine", "vegetable oil", "portable circular saw", "tableware",
        "uncomfortableness", "vegetable soup", "file allocation table", "timetable",
        "cocktail table", "tableau vivant", "table tipping", "tilt-top table",
        "coffee table", "collectable", "inevitable", "lord's table", "excitable area",
        "pier table", "console table", "table napkin", "relocatable program",
        "snooker table", "tablet", "police constable", "stableman", "root vegetable",
        "statistical table", "acceptableness", "gaming table", "stableness",
        "raw vegetable", "unacceptableness", "inevitable accident", "ratables",
        "disreputableness", "tractableness", "livery stable", "tabletop",
        "vegetable silk", "portable computer", "abatable nuisance", "comfortableness",
        "palatableness", "italian vegetable marrow", "table tapping", "charitableness",
        "toilet table", "council table", "correlation table", "stableboy",
        "table lifting", "table lamp", "table knife", "vegetable garden", "tableau",
        "table-tennis bat", "table tennis", "gateleg table" };
    String[] result4 = w.filter(RiWordNet.CONTAINS, "table", "n");
    // //printArr(result4);
    setEqual(expected4, result4);

    String[] expected5 = {};
    String[] result5 = w.filter(RiWordNet.CONTAINS, "tableauu", "n");
    // printArr(result5);
    setEqual(expected5, result5);

    String[] expected6 = { "collectable" };
    String[] result6 = w.filter(RiWordNet.ENDS_WITH, "ctable", "n");
    // printArr(result6);
    setEqual(expected6, result6);

    String[] expected8 = { "constable", "dinner table", "chief constable",
        "john constable", "notable", "breakfast table", "tip table", "parsons table",
        "trestle table", "drafting table", "council table", "plane table",
        "coffee table", "portable", "lord's table", "card table", "turntable",
        "raw vegetable", "file allocation table", "writing table",
        "cruciferous vegetable", "conference table", "work table", "communion table",
        "table", "dressing table", "tea table", "roundtable", "root vegetable",
        "pin table", "toilet table", "timetable", "cocktail table", "collectable",
        "periodic table", "knight of the round table", "kitchen table",
        "police constable", "pier table", "correlation table", "gateleg table",
        "dining table", "solanaceous vegetable", "graduated table", "training table",
        "dining-room table", "tilt-top table", "round table", "pool table",
        "gaming table", "operating table", "console table", "king arthur's round table",
        "billiard table", "decision table", "eatable", "table-tennis table",
        "leafy vegetable", "refectory table", "snooker table", "ping-pong table",
        "livery stable", "pingpong table", "water table", "statistical table",
        "julienne vegetable", "tip-top table", "vegetable", "actuarial table",
        "mortality table", "high table", "pedestal table", "inevitable", "worktable",
        "drop-leaf table", "drawing table", "potable", "stable" };
    String[] result8 = w.filter(RiWordNet.REGEX_MATCH, ".*table", "n");
    // //printArr(result8);
    setEqual(expected8, result8);

    String[] expected9 = { "volleyball", "clientele", "comestible", "convertible",
        "collage", "cape sable", "corbie gable", "eatable", "folk tale", "clientage",
        "coffee table", "colpocele", "plane table", "roundtable", "folktale",
        "pool table", "college", "vocable", "conventicle", "conjecture", "colleague",
        "collembola", "work table", "college boy", "telltale", "console table",
        "collectivism", "portable", "collocalia", "vegetable", "collect call",
        "pollen tube", "follicle", "tall tale", "collywobbles", "mollycoddle", "notable",
        "syllable", "collect", "conductance", "decolletage", "reflectance", "toll call",
        "collet", "corrective", "collecting", "colette", "round table", "collectible",
        "collegian", "connective", "potable", "collapse", "color tube", "combustible",
        "coax cable", "toilet table", "sociable", "spectacle", "collectivist",
        "collotype", "collective", "collector", "roll call", "molecule", "collection",
        "timetable", "bell gable", "constable", "worktable", "card table" };
    String[] result9 = w.filter(RiWordNet.SIMILAR_TO, "collectable", "n");
    // //printArr(result9);
    setEqual(expected9, result9);

    String[] expected12 = {};
    String[] result12 = w.filter(RiWordNet.CONTAINS, "nahsuchword", "n");
    // //printArr(result12);
    setEqual(expected12, result12);

    String[] expected14 = { "table rapping", "habitableness", "unstableness",
        "work table", "stableman", "tractableness", "tablespoon", "timetable",
        "table salt", "excitableness", "table mustard", "intractableness",
        "pingpong table", "drop-leaf table", "disreputable person", "table tilting",
        "vegetable matter", "pier table", "japanese table pine", "vegetable marrow",
        "portable saw", "graduated table", "table-tennis bat", "vegetable oyster",
        "breakfast table", "suitableness", "ratables", "drafting table",
        "periodic table", "correlation table", "sleeping tablet", "constable",
        "augean stables", "table-tennis racquet", "palatableness", "abatable nuisance",
        "actuarial table", "tableau", "tip table", "console table", "operating table",
        "unprofitableness", "round table", "table talk", "vegetable sponge",
        "disreputableness", "toilet table", "adjustable spanner", "plane table",
        "drawing table", "pool table", "billiard table", "inhospitableness", "notable",
        "irritable bowel syndrome", "vegetable soup", "table turning",
        "inevitable accident", "stable factor", "lord's table", "trestle table",
        "solanaceous vegetable", "coffee table", "portable", "tip-top table",
        "dinner table", "tablecloth", "unsuitableness", "writing table", "vegetable oil",
        "portable computer", "knight of the round table", "kitchen table", "stable",
        "council table", "tablemate", "tea table", "tableland", "tableware",
        "vegetable wax", "vegetable", "parsons table", "table saw", "gateleg table",
        "worktable", "vegetable sheep", "high table", "stable gear", "table linen",
        "turntable", "tablet-armed chair", "water table", "table d'hote", "table lamp",
        "dining table", "immutableness", "gaming table", "table wine", "collectable",
        "comfortableness", "vegetable tallow", "vegetable garden", "leafy vegetable",
        "pedestal table", "italian vegetable marrow", "police constable",
        "portable circular saw", "hospitableness", "table napkin", "unpalatableness",
        "table service", "refectory table", "relocatable program", "table knife",
        "mutableness", "mortality table", "cocktail table", "round-table conference",
        "tilt-top table", "statistical table", "training table", "tabletop",
        "conference table", "table mat", "charitableness", "stable companion",
        "vegetable hummingbird", "king arthur's round table", "tablefork",
        "tableau vivant", "cruciferous vegetable", "memorial tablet", "communion table",
        "eatable", "stableboy", "card table", "inflatable cushion", "table tipping",
        "table-tennis table", "dining-room table", "julienne vegetable", "livery stable",
        "root vegetable", "table tennis", "table game", "potable", "vegetable silk",
        "pin table", "stablemate", "tablespoonful", "acceptableness", "john constable",
        "table of contents", "permutableness", "vegetable ivory", "snooker table",
        "ping-pong table", "raw vegetable", "roundtable", "charitable trust",
        "uncomfortableness", "coffee-table book", "unacceptableness",
        "adjustable wrench", "inevitable", "table lifting", "profitableness",
        "dressing table", "vegetable patch", "inevitableness", "excitable area",
        "tablet", "file allocation table", "table tapping", "chief constable",
        "decision table", "stableness", "table-mountain pine" };
    String[] result14 = w.filter(RiWordNet.CONTAINS, "table", "n");
    // //printArr(result14);
    setEqual(expected14, result14);

    setContains(w.filter(RiWordNet.CONTAINS, "table", "e"), EMPTY);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected10 = new String[] { "tablet" };
    result10 = w.filter(RiWordNet.WILDCARD_MATCH, "table?", "n");
    // printArr(result10);
    setEqual(expected10, result10);

    expected11 = new String[] { "inevitable", "mutableness", "portable", "tablespoonful",
        "profitableness", "tableau", "potable", "tablespoon", "eatable", "tablefork",
        "tablemate", "disreputableness", "turntable", "unpalatableness", "stable",
        "uncomfortableness", "worktable", "tabletop", "immutableness", "acceptableness",
        "stableman", "constable", "unsuitableness", "hospitableness", "tablecloth",
        "roundtable", "stablemate", "tablet", "ratables", "timetable", "stableboy",
        "unacceptableness", "tableland", "unstableness", "tableware", "collectable",
        "suitableness", "notable", "unprofitableness", "stableness", "inhospitableness",
        "permutableness", "charitableness", "comfortableness", "intractableness",
        "habitableness", "inevitableness", "excitableness", "tractableness", "vegetable",
        "palatableness" };
    result11 = w.filter(RiWordNet.WILDCARD_MATCH, "*table*", "n");
    // //printArr(result11);
    setEqual(expected11, result11);

    expected15 = new String[] { "vegetable", "acceptableness", "tablemate", "timetable",
        "potable", "portable", "permutableness", "stable", "worktable",
        "disreputableness", "tablespoon", "constable", "intractableness",
        "tablespoonful", "unpalatableness", "charitableness", "turntable",
        "excitableness", "tractableness", "unacceptableness", "inevitableness",
        "notable", "suitableness", "immutableness", "uncomfortableness", "habitableness",
        "hospitableness", "tableware", "unsuitableness", "tableland", "stableboy",
        "collectable", "stableman", "unstableness", "mutableness", "stableness",
        "profitableness", "unprofitableness", "tablet", "eatable", "comfortableness",
        "palatableness", "tablefork", "tablecloth", "stablemate", "inevitable",
        "ratables", "roundtable", "tabletop", "inhospitableness", "tableau" };
    result15 = w.filter(RiWordNet.WILDCARD_MATCH, "*table*", "n");
    // //printArr(result15);
    setEqual(expected15, result15);

    expected = new String[] { "tubule", "tuvalu", "tabooli", "tiepolo", "tivoli",
        "t-bill", "tepal", "tipple", "tubful", "tabbouleh", "tableau", "tupelo" };
    result = w.filter(RiWordNet.SOUNDS_LIKE, "table", "n");
    // //printArr(result);
    setEqual(expected, result);

    expected2 = new String[] { "tableware", "tableland", "tablemate", "tablespoon",
        "tableau", "tabletop", "tablecloth", "tablefork", "tablet", "tablespoonful" };
    result2 = w.filter(RiWordNet.STARTS_WITH, "table", "n");
    // //printArr(result2);
    setEqual(expected2, result2);

    expected3 = new String[] { "bleat" };
    result3 = w.filter(RiWordNet.ANAGRAMS, "table", "n");
    // //printArr(result3);
    setEqual(expected3, result3);

    expected4 = new String[] { "unstableness", "notable", "tablemate", "stableness",
        "tablespoonful", "tablespoon", "eatable", "timetable", "tractableness",
        "immutableness", "portable", "stablemate", "tabletop", "unprofitableness",
        "ratables", "inevitable", "tableau", "potable", "inevitableness",
        "hospitableness", "vegetable", "stableman", "tableland", "stableboy",
        "worktable", "stable", "tablet", "excitableness", "tablefork", "unpalatableness",
        "comfortableness", "unsuitableness", "intractableness", "habitableness",
        "uncomfortableness", "constable", "tableware", "turntable", "permutableness",
        "palatableness", "unacceptableness", "acceptableness", "mutableness",
        "tablecloth", "inhospitableness", "disreputableness", "profitableness",
        "suitableness", "charitableness", "collectable", "roundtable" };
    result4 = w.filter(RiWordNet.CONTAINS, "table", "n");
    // //printArr(result4);
    setEqual(expected4, result4);

    expected5 = new String[] {};
    result5 = w.filter(RiWordNet.CONTAINS, "tableauu", "n");
    // //printArr(result5);
    setEqual(expected5, result5);

    expected6 = new String[] { "collectable" };
    result6 = w.filter(RiWordNet.ENDS_WITH, "ctable", "n");
    // //printArr(result6);
    setEqual(expected6, result6);

    expected8 = new String[] { "inevitable", "table", "potable", "stable", "eatable",
        "vegetable", "turntable", "constable", "notable", "portable", "collectable",
        "worktable", "timetable", "roundtable" };
    result8 = w.filter(RiWordNet.REGEX_MATCH, ".*table", "n");
    // //printArr(result8);
    setEqual(expected8, result8);

    expected9 = new String[] { "collectible", "folktale", "syllable", "collecting",
        "colleague", "collage", "decolletage", "collective", "telltale", "reflectance",
        "collotype", "worktable", "follicle", "collapse", "eatable", "collembola",
        "constable", "colpocele", "colette", "collectivist", "portable", "clientele",
        "collector", "vocable", "combustible", "corrective", "connective", "conductance",
        "conventicle", "timetable", "collection", "clientage", "sociable", "conjecture",
        "volleyball", "spectacle", "collect", "molecule", "notable", "college",
        "potable", "convertible", "vegetable", "collectivism", "collywobbles", "collet",
        "collocalia", "comestible", "collegian", "roundtable", "mollycoddle" };
    result9 = w.filter(RiWordNet.SIMILAR_TO, "collectable", "n");
    // //printArr(result9);
    setEqual(expected9, result9);

    expected12 = new String[] {};
    result12 = w.filter(RiWordNet.CONTAINS, "nahsuchword", "n");
    // //printArr(result12);
    setEqual(expected12, result12);

    expected14 = new String[] { "tableau", "stableness", "unprofitableness", "stableboy",
        "habitableness", "stableman", "hospitableness", "charitableness",
        "unpalatableness", "tablemate", "tablecloth", "ratables", "suitableness",
        "stable", "inevitable", "collectable", "roundtable", "tableland", "mutableness",
        "constable", "tablefork", "worktable", "unacceptableness", "excitableness",
        "acceptableness", "intractableness", "potable", "eatable", "notable",
        "tableware", "unstableness", "immutableness", "comfortableness",
        "inhospitableness", "vegetable", "stablemate", "disreputableness", "tablet",
        "inevitableness", "portable", "uncomfortableness", "permutableness",
        "unsuitableness", "tablespoonful", "tabletop", "tractableness", "turntable",
        "tablespoon", "timetable", "palatableness", "profitableness" };
    result14 = w.filter(RiWordNet.CONTAINS, "table", "n");
    // //printArr(result14);
    setEqual(expected14, result14);

    setContains(w.filter(RiWordNet.CONTAINS, "table", "e"), EMPTY);
  }

  // /////////////////////////// Dynamics ///////////////////////////////

  void setContainsMulti(String[] expected, String methodNm, int id, int count)
  {
    setContainsMulti(expected, methodNm, new Class[] { int.class, int.class }, new Object[] {
        id, count });
  }

  void setContainsMulti(String[] expected, String methodNm, String word, String pos, int count)
  {
    setContainsMulti(expected, methodNm, new Class[] { String.class, String.class,
        int.class }, new Object[] { word, pos, count });
  }

  void setContainsMulti(String[] expected, String methodNm, Class[] argTypes, Object[] args)
  {
    boolean ignoreCompoundsOrig = w.ignoreCompoundWords();
    boolean ignoreUppersOrig = w.ignoreUpperCaseWords();
    String[] result;
    Method m = RiTa._findMethod(w, methodNm, argTypes);
    try
    {
      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(false);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
        ok(!Arrays.asList(result).contains(args[0]));
      setContains(expected, result);

      w.ignoreCompoundWords(true);
      w.ignoreUpperCaseWords(false);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
        ok(!Arrays.asList(result).contains(args[0]));
      setContains(removeCompoundWords(expected), result);

      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(true);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
        ok(!Arrays.asList(result).contains(args[0]));
      setContains(removeUpperCaseWords(expected), result);

      w.ignoreCompoundWords(true);
      w.ignoreUpperCaseWords(true);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
        ok(!Arrays.asList(result).contains(args[0]));
      setContains(removeCompoundWords(removeUpperCaseWords(expected)), result);

    }
    catch (Throwable e)
    {
      throw new RuntimeException(e);
    }
    w.ignoreCompoundWords(ignoreCompoundsOrig);
    w.ignoreUpperCaseWords(ignoreUppersOrig);
  }

  void setEqualMulti(String[] expected, String methodNm, int id)
  {
    setEqualMulti(expected, methodNm, new Class[] { int.class }, new Object[] { id });
  }

  void setEqualMulti(String[] expected, String methodNm, String word, String pos)
  {
    setEqualMulti(expected, methodNm, new Class[] { String.class, String.class }, new Object[] {
        word, pos });
  }

  void setEqualMulti(String[] expected, String methodNm, Class[] argTypes, Object[] args)
  {
    boolean ignoreCompoundsOrig = w.ignoreCompoundWords();
    boolean ignoreUppersOrig = w.ignoreUpperCaseWords();
    String[] result;
    Method m = RiTa._findMethod(w, methodNm, argTypes);
    try
    {
      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(false);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
        ok(!Arrays.asList(result).contains(args[0]));
      setEqual(expected, result);

      w.ignoreCompoundWords(true);
      w.ignoreUpperCaseWords(false);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
        ok(!Arrays.asList(result).contains(args[0]));
      setEqual(removeCompoundWords(expected), result);

      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(true);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
        ok(!Arrays.asList(result).contains(args[0]));
      setEqual(removeUpperCaseWords(expected), result);

      w.ignoreCompoundWords(true);
      w.ignoreUpperCaseWords(true);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
        ok(!Arrays.asList(result).contains(args[0]));
      setEqual(removeCompoundWords(removeUpperCaseWords(expected)), result);
    }
    catch (Throwable e)
    {
      throw new RuntimeException(e);
    }
    w.ignoreCompoundWords(ignoreCompoundsOrig);
    w.ignoreUpperCaseWords(ignoreUppersOrig);
  }

  @Test
  public void testGetAnagramsStringStringInt()
  {
    String[] expected = { "thing" };
    // println(result);
    setContainsMulti(expected, "getAnagrams", "night", "n", 3);

    String[] expected2 = { "god" };
    // println(result2);
    setContainsMulti(expected2, "getAnagrams", "dog", "n", 3);

    String[] expected3 = { "bleat", };
    // println(result2);
    setContainsMulti(expected3, "getAnagrams", "table", "n", 3);

    setEqual(EMPTY, w.getAnagrams("night", "j", 3));
  }

  @Test
  public void testGetAnagramsStringString()
  {
    String[] expected = { "thing" };
    // println(result);
    setEqualMulti(expected, "getAnagrams", "night", "n");

    String[] expected2 = { "god" };
    // println(result2);
    setEqualMulti(expected2, "getAnagrams", "dog", "n");

    String[] expected3 = { "bleat", };
    // println(result2);
    setEqualMulti(expected3, "getAnagrams", "table", "n");

    setEqual(EMPTY, w.getAnagrams("night", "j"));
  }

  // ////////////////////////////// Helpers ///////////////////////////////////

  private static String[] removeUpperCaseWords(String[] s)
  {
    ArrayList<String> al = new ArrayList<String>();
    for (int i = 0; i < s.length; i++)
    {
      if (!WordnetUtil.startsWithUppercase(s[i]))
        al.add(s[i]);
    }
    return al.toArray(new String[0]);
  }

  private static String[] removeCompoundWords(String[] s)
  {
    ArrayList<String> al = new ArrayList<String>();
    for (int i = 0; i < s.length; i++)
    {
      if (!w.isCompound(s[i]))
        al.add(s[i]);
    }
    return al.toArray(new String[0]);
  }

  static RiWordNet w;
  static boolean preloadFilters;
  static
  {

    SILENT = false;
    long ts = System.currentTimeMillis();
    w = new RiWordNet("/WordNet-3.1");
    if (preloadFilters)
    {
      String[] pos = { "n", "a", "r", "v", };
      for (int i = 0; i < pos.length; i++)
        w.iterator(pos[i]); // force load filters, so slow (TODO: optimize!)
    }
    System.out.println("[INFO] Loaded in " + (System.currentTimeMillis() - ts) + "ms");
  }

  public static void main(String[] args)
  {
    println(new RiWordNet("/WordNet-3.1").ignoreCompoundWords(false).getSynset("medicare", "n"));
  }
}
