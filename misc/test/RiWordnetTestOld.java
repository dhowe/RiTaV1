package rita.wordnet.test;

import static org.junit.Assert.fail;
import static rita.wordnet.test.QUnitStubs.*;

import org.junit.After;
import org.junit.Test;

import rita.RiWordnet;

/**
 * Compare results to: http://wordnetweb.princeton.edu/perl/webwn
 * 
 * Documentation for methods here: 
 * 
 * Later: subclasses for each installation-mode (local, installed, remote)
 */
public class RiWordnetTestOld
{
  protected static RiWordnet wordnet;

  static {
    
    SILENT = false;
    long ts = System.currentTimeMillis();
    wordnet = new RiWordnet();
    System.out.println("[INFO] Loaded in "+(System.currentTimeMillis()-ts)+"ms");
  }
  
  @Test
  public void testGetSenseIdsStringString()
  {
    int[] expected = { 92124272, 910172934, 99919605, 93614083, 92989061, 92986962, 92130460, 9903174 };
    int[] result = wordnet.getSenseIds("cat", "n");
    //for (int i = 0; i < result.length; i++)
      //print(result[i]+", ");
    println(result);
    equal(result.length, 8);
    deepEqual(expected, result);
  }
  
  @Test
  public void testGetHypernymsStringString()
  {
    String[] results = wordnet.getHypernyms("carrot", "n");
    println(results);
    ok(1);
    //fail("Not yet implemented");
  }

  @Test
  public void testGetAnagramsStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAnagramsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetContainsStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetContainsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetEndsWithStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetEndsWithStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetStartsWithStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetStartsWithStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetRegexMatchStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetRegexMatchStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSoundsLikeStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSoundsLikeStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetWildcardMatchStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetWildcardMatchStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testFilterIntStringPOSInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testFilterIntStringPOS()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testOrFilterIntArrayStringArrayPOSInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testOrFilterIntArrayStringArrayPOS()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testDispose()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testSetWordnetHome()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSenseIdsIndexWord()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetGlossStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllGlosses()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetGlossInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetDescriptionInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetDescriptionStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetExamplesCharSequenceCharSequence()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAnyExample()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetExamplesInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllExamples()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllSynonymsIntInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllSynonymsInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSynonymsStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSynonymsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllSynonymsStringStringInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllSynonymsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetCommonParents()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetCommonParent()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSynsetStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSynsetStringStringBoolean()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSynsetInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllSynsets()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSenseCount()
  {
    fail("Not yet implemented");
  }
  
  @Test
  public void testGetAntonymsStringString()
  {
    
    deepEqual(wordnet.getAntonyms("day","n"), null);
    deepEqual(wordnet.getAntonyms("night","n"), new String[]{"day"});
    
    deepEqual(wordnet.getAntonyms("left", "a"), new String[]{"right"});
    deepEqual(wordnet.getAntonyms("right", "a"), new String[]{"left"});
    
    deepEqual(wordnet.getAntonyms("full", "a"), new String[]{"empty"});
    deepEqual(wordnet.getAntonyms("empty", "a"), new String[]{"full"});
    
    deepEqual(wordnet.getAntonyms("quickly", "r"), new String[]{"slowly"});
    deepEqual(wordnet.getAntonyms("slowly", "r"), new String[]{"quickly"});
  }

  @Test
  public void testGetAntonymsInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllAntonyms()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetHypernymsInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllHypernyms()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetHypernymTree()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetHyponymsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetHyponymsInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllHyponyms()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetHyponymTree()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIsAdjective()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIsAdverb()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIsVerb()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIsNoun()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetStems()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIsStem()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testExists()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testRemoveNonExistent()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testLookupIndexWord()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetPosString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetPosInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetPosStr()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetBestPos()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testPtnlToStrings()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetRandomExample()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetRandomExamples()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetRandomWords()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetRandomWordCharSequence()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetRandomWordCharSequenceBooleanInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIsCompound()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetDistance()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetMeronymsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetMeronymsInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllMeronyms()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetHolonymsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetHolonymsInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllHolonyms()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetCoordinatesStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetCoordinatesInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllCoordinates()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetVerbGroupStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetVerbGroupInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllVerbGroups()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetDerivedTermsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetDerivedTermsInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllDerivedTerms()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAlsoSeesStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAlsoSeesInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllAlsoSees()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetNominalizationsStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetNominalizationsInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllNominalizations()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSimilarStringString()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSimilarInt()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAllSimilar()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIsIgnoringCompoundWords()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIgnoreCompoundWords()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIsIgnoringUpperCaseWords()
  {
    fail("Not yet implemented");
  }

  @Test
  public void testIgnoreUpperCaseWords()
  {
    fail("Not yet implemented");
  }
  
  @Test
  public void testIterator()
  {
    fail("Not yet implemented");
  }
  
  @After
  public void tearDown()
  {
    wordnet = null;
  }

}
