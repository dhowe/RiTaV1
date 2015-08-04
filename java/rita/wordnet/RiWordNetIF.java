package rita.wordnet;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

public interface RiWordNetIF
{
  public Iterator iterator(String pos);
  
  public String[] getAnagrams(String word, String posStr, int maxResults);
  
  public String[] getAnagrams(String word, String posStr);

  public String[] getContains(String word, String posStr, int maxResults);

  public String[] getContains(String word, String posStr);
  
  public String[] getEndsWith(String word, String posStr, int maxResults);
  
  public String[] getEndsWith(String word, String posStr);
  
  public String[] getStartsWith(String word, String posStr, int maxResults);
  
  public String[] getStartsWith(String word, String posStr);
  
  public String[] getRegexMatch(String pattern, String posStr, int maxResults);

  public String[] getRegexMatch(String pattern, String posStr);
  
  public String[] getSoundsLike(String pattern, String posStr, int maxResults);

  public String[] getSoundsLike(String pattern, String posStr);
  
  public String[] getWildcardMatch(String pattern, String posStr, int maxResults);
  
  public String[] getWildcardMatch(String pattern, String posStr);
  
  public String[] filter(int filterFlag, String word, String pos, int maxResults);
  
  public String[] filter(int filterFlag, String word, String pos);
  
  public String[] orFilter(int[] filterFlags, String[] words, String pos, int maxResults);
  
  public String[] orFilter(int[] filterFlag, String[] words, String pos);
  
  public String[] andFilter(int[] filterFlags, String[] words, String pos, int maxResults);
  
  public String[] andFilter(int[] filterFlag, String[] word, String pos);
  
  public int[] getSenseIds(String word, String posStr);
  
  public String getGloss(String word, String pos);
  
  public String[] getAllGlosses(String word, String pos);
  
  public String getGloss(int senseId);
  
  public String[] getExamples(String word, String pos);
  
  public String getRandomExample(String word, String pos);
  
  public String[] getExamples(int senseId);
  
  public String[] getAllExamples(String word, String pos);
  
  public String[] getSynonyms(int id, int maxResults);

  public String[] getSynonyms(int id);

  public String[] getSynonyms(String word, String posStr, int maxResults);
  
  public String[] getSynonyms(String word, String posStr);
  
  public String[] getAllSynonyms(String word, String posStr, int maxResults);

  public String[] getAllSynonyms(String word, String posStr);

  public String[] getCommonParents(String word1, String word2, String pos);

  public int getCommonParent(int id1, int id2);
  
  public String[] getSynset(String word, String pos);

  public String[] getSynset(int id);

  public String[] getAllSynsets(String word, String posStr);

  public int getSenseCount(String word, String pos);

  public String[] getAntonyms(String word, String pos);

  public String[] getAntonyms(int id);
  
  public String[] getAllAntonyms(String word, String pos);
  
  public String[] getHypernyms(String word, String posStr);
  
  public String[] getHypernyms(int id);
  
  public String[] getAllHypernyms(String word, String posStr);
  
  public String[] getHypernymTree(int id);
  
  public String[] getHyponyms(String word, String posStr);

  public String[] getHyponyms(int id);
  
  public String[] getAllHyponyms(String word, String posStr);

  public String[] getHyponymTree(int id);

  public boolean isAdjective(String word);

  public boolean isAdverb(String word);

  public boolean isVerb(String word);

  public boolean isNoun(String word);

  public String[] getStems(String query, String pos);

  public boolean isStem(String word, String pos);

  public boolean exists(String word);

  public RiWordNetIF removeNonExistent(Collection words);

  public String[] getPos(String word);
  
  public String getPos(int id);
  
  public String getBestPos(String word);
  
  public String getRandomExample(String pos);

  public String[] getRandomExamples(String pos, int numExamples);
  
  public String[] getRandomWords(String pos, int count);

  public String getRandomWord(String pos);
  
  public String getRandomWord(String pos, boolean stemsOnly, int maxChars);
  
  public RiWordNetIF printHyponymTree(int senseId);

  public RiWordNetIF dumpHyponymTree(PrintStream ps, String word, String pos); 

  public RiWordNetIF printHypernymTree(int senseId);

  public RiWordNetIF dumpHypernymTree(PrintStream ps, String word, String pos); 

  public float getDistance(String lemma1, String lemma2, String pos);
  
  public String[] getMeronyms(String query, String pos);
  
  public String[] getMeronyms(int id);

  public String[] getAllMeronyms(String query, String pos);
  
  public String[] getHolonyms(String query, String pos);
  
  public String[] getHolonyms(int id);

  public String[] getAllHolonyms(String query, String pos);

  public String[] getCoordinates(String query, String pos);
  
  public String[] getCoordinates(int id);
  
  public String[] getAllCoordinates(String query, String pos);

  public String[] getVerbGroup(String query, String pos);

  public String[] getVerbGroup(int id);

  public String[] getAllVerbGroups(String query, String pos);

  public String[] getDerivedTerms(String query, String pos);

  public String[] getDerivedTerms(int id);

  public String[] getAllDerivedTerms(String query, String pos);

  public String[] getAlsoSees(String query, String pos);
  
  public String[] getAlsoSees(int senseId);

  public String[] getAllAlsoSees(String query, String pos);

  public String[] getNominalizations(String query, String pos);

  public String[] getNominalizations(int id);

  public String[] getAllNominalizations(String query, String pos);
  
  public String[] getSimilar(String query, String pos);

  public String[] getSimilar(int id);

  public String[] getAllSimilar(String query, String pos);

  public boolean randomizeResults();

  public RiWordNetIF randomizeResults(boolean random);

  public boolean ignoreCompoundWords();

  public RiWordNetIF ignoreCompoundWords(boolean val);

  public boolean ignoreUpperCaseWords();

  public RiWordNetIF ignoreUpperCaseWords(boolean val);

}
