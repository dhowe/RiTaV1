package rita.wordnet;

import java.io.*;
import java.net.URL;
import java.util.*;

import rita.RiTa;
import rita.RiWordNet;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

public class RiWordNetJWI implements RiWordNetIF
{
  static final String[] EA = {};

  IDictionary dict;
  boolean ignoreCompoundWords = false;
  boolean ignoreUpperCaseWords = false;
  boolean randomizeResults = false;

  public RiWordNetJWI(String wnhome)
  {
    // String wnhome = System.getenv("WNHOME");
    String path = wnhome + File.separator + "dict";
    try
    {
      URL url = new URL("file", null, path);
      dict = new Dictionary(url);
      dict.open();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  // Unimplemented

  @Override
  public String[] getAnagrams(String word, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getAnagrams(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getContains(String word, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getContains(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getEndsWith(String word, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getEndsWith(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getStartsWith(String word, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getStartsWith(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getRegexMatch(String pattern, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getRegexMatch(String pattern, String posStr)
  {
    return null;
  }

  @Override
  public String[] getSoundsLike(String pattern, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getSoundsLike(String pattern, String posStr)
  {
    return null;
  }

  @Override
  public String[] getWildcardMatch(String pattern, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getWildcardMatch(String pattern, String posStr)
  {
    return null;
  }

  @Override
  public String[] filter(int filterFlag, String word, String pos, int maxResults)
  {
    return null;
  }

  @Override
  public String[] filter(int filterFlag, String word, String pos)
  {
    return null;
  }

  @Override
  public String[] orFilter(int[] filterFlags, String[] words, String pos, int maxResults)
  {
    return null;
  }

  @Override
  public String[] orFilter(int[] filterFlag, String[] words, String pos)
  {
    return null;
  }

  @Override
  public String[] andFilter(int[] filterFlags, String[] words, String pos, int maxResults)
  {
    return null;
  }

  @Override
  public String[] andFilter(int[] filterFlag, String[] word, String pos)
  {
    return null;
  }

  @Override
  public int[] getSenseIds(String word, String posStr)
  {
    return null;
  }

  @Override
  public String getGloss(String word, String pos)
  {
    return null;
  }

  @Override
  public String[] getAllGlosses(String word, String pos)
  {
    return null;
  }

  @Override
  public String getGloss(int senseId)
  {
    return null;
  }

  @Override
  public String getDescription(int senseId)
  {
    return null;
  }

  @Override
  public String getDescription(String word, String pos)
  {
    return null;
  }

  @Override
  public String[] getExamples(String word, String pos)
  {
    return null;
  }

  @Override
  public String getRandomExample(String word, String pos)
  {
    return null;
  }

  @Override
  public String[] getExamples(int senseId)
  {
    return null;
  }

  @Override
  public String[] getAllExamples(String word, String pos)
  {
    return null;
  }

  @Override
  public String[] getSynonyms(int senseId, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getSynonyms(int id)
  {
    return null;
  }

  @Override
  public String[] getSynonyms(String word, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getSynonyms(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getAllSynonyms(String word, String posStr, int maxResults)
  {
    return null;
  }

  @Override
  public String[] getAllSynonyms(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getCommonParents(String word1, String word2, String pos)
  {
    return null;
  }

  @Override
  public int getCommonParent(int id1, int id2)
  {
    return 0;
  }

  @Override
  public String[] getSynset(String word, String pos)
  {
    return _getSynset(word, pos, false);
  }

  @Override
  public String[] getSynset(int id)
  {
    return null;
  }

  @Override
  public String[] getAllSynsets(String word, String posStr)
  {
    return null;
  }

  @Override
  public int getSenseCount(String word, String pos)
  {
    return 0;
  }

  @Override
  public String[] getHypernyms(int id)
  {
    return null;
  }

  @Override
  public String[] getAllHypernyms(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getHypernymTree(int id)
  {
    return null;
  }

  @Override
  public String[] getHyponyms(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getHyponyms(int id)
  {
    return null;
  }

  @Override
  public String[] getAllHyponyms(String word, String posStr)
  {
    return null;
  }

  @Override
  public String[] getHyponymTree(int id)
  {
    return null;
  }

  @Override
  public boolean isAdjective(String word)
  {
    return false;
  }

  @Override
  public boolean isAdverb(String word)
  {
    return false;
  }

  @Override
  public boolean isVerb(String word)
  {
    return false;
  }

  @Override
  public boolean isNoun(String word)
  {
    return false;
  }

  @Override
  public String[] getStems(String query, String pos)
  {
    return null;
  }

  @Override
  public boolean isStem(String word, String pos)
  {
    return false;
  }

  @Override
  public boolean exists(String word)
  {
    return false;
  }

  @Override
  public RiWordNetIF removeNonExistent(Collection words)
  {
    return null;
  }

  @Override
  public String[] getPos(String word)
  {
    return null;
  }

  @Override
  public String getPos(int id)
  {
    return null;
  }

  @Override
  public String getBestPos(String word)
  {
    return null;
  }

  @Override
  public String getRandomExample(String pos)
  {
    return null;
  }

  @Override
  public String[] getRandomExamples(String pos, int numExamples)
  {
    return null;
  }

  @Override
  public String[] getRandomWords(String pos, int count)
  {
    return null;
  }

  @Override
  public String getRandomWord(String pos)
  {
    return null;
  }

  @Override
  public String getRandomWord(String pos, boolean stemsOnly, int maxChars)
  {
    return null;
  }

  @Override
  public RiWordNetIF printHyponymTree(int senseId)
  {
    return null;
  }

  @Override
  public RiWordNetIF dumpHyponymTree(PrintStream ps, String word, String pos)
  {
    return null;
  }

  @Override
  public RiWordNetIF printHypernymTree(int senseId)
  {
    return null;
  }

  @Override
  public RiWordNetIF dumpHypernymTree(PrintStream ps, String word, String pos)
  {
    return null;
  }

  @Override
  public float getDistance(String lemma1, String lemma2, String pos)
  {
    return 0;
  }

  @Override
  public String[] getMeronyms(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getMeronyms(int id)
  {
    return null;
  }

  @Override
  public String[] getAllMeronyms(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getHolonyms(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getHolonyms(int id)
  {
    return null;
  }

  @Override
  public String[] getAllHolonyms(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getCoordinates(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getCoordinates(int id)
  {
    return null;
  }

  @Override
  public String[] getAllCoordinates(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getVerbGroup(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getVerbGroup(int id)
  {
    return null;
  }

  @Override
  public String[] getAllVerbGroups(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getDerivedTerms(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getDerivedTerms(int id)
  {
    return null;
  }

  @Override
  public String[] getAllDerivedTerms(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getAlsoSees(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getAlsoSees(int senseId)
  {
    return null;
  }

  @Override
  public String[] getAllAlsoSees(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getNominalizations(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getNominalizations(int id)
  {
    return null;
  }

  @Override
  public String[] getAllNominalizations(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getSimilar(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getSimilar(int id)
  {
    return null;
  }

  @Override
  public String[] getAllSimilar(String query, String pos)
  {
    return null;
  }

  @Override
  public String[] getHypernyms(String word, String posStr)
  {
    return null;
  }

  // /////////////////// IMPLEMENTED ONLY //////////////////////
  @Override
  public String[] getAntonyms(int id)
  {
    return null;
  }

  @Override
  public String[] getAllAntonyms(String word, String pos)
  {
    return null;
  }

  @Override
  public String[] getAntonyms(String word, String pos)
  {
    return null;
  }

  @Override
  public boolean randomizeResults()
  {
    return randomizeResults;
  }

  @Override
  public RiWordNetIF randomizeResults(boolean val)
  {
    randomizeResults = val;
    return this;
  }

  @Override
  public boolean ignoreCompoundWords()
  {
    return ignoreCompoundWords;
  }

  @Override
  public RiWordNetIF ignoreCompoundWords(boolean val)
  {
    ignoreCompoundWords = val;
    return this;
  }

  @Override
  public boolean ignoreUpperCaseWords()
  {
    return false;
  }

  @Override
  public RiWordNetIF ignoreUpperCaseWords(boolean val)
  {
    ignoreUpperCaseWords = val;
    return this;
  }

  @Override
  public Iterator iterator(String pos)
  {
    Iterator<IIndexWord> it = dict.getIndexWordIterator(getJWIPos(pos));
    return null;
  }

  // /////////////////// Helpers //////////////////////

  private boolean ignorable(String lemma)
  {
    if (ignoreCompoundWords && isCompound(lemma))
      return true;

    if (ignoreUpperCaseWords && WordnetUtil.startsWithUppercase(lemma))
      return true;

    return false;
  }

  boolean isCompound(String word)
  {
    return word.indexOf('_') > 0 || word.indexOf(' ') > 0;
  }

  private List<ISynsetID> _getFirstSynset(String word, String pos, Pointer p)
  {
    IIndexWord idxWord = dict.getIndexWord(word, getJWIPos(pos));
    IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
    IWord iword = dict.getWord(wordID);
    ISynset synset = iword.getSynset();
    List<ISynsetID> related = synset.getRelatedSynsets(p);
    for (ISynsetID id : related)
    {
      ISynset syn = dict.getSynset(id);
    }
    return related;
  }

  private ISynset _getFirstSynset(String word, String pos)
  {
    IIndexWord idxWord = dict.getIndexWord(word, getJWIPos(pos));
    IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
    IWord iword = dict.getWord(wordID);
    ISynset synset = iword.getSynset();
    synset.getRelatedSynsets(Pointer.ANTONYM);
    return synset;
  }

  public void trek(POS pos)
  {
    int tickNext = 0;
    int tickSize = 20000;
    int seen = 0;
    long t = System.currentTimeMillis();
    for (Iterator<IIndexWord> i = dict.getIndexWordIterator(pos); i.hasNext();)
      for (IWordID wid : i.next().getWordIDs())
      {
        seen += dict.getWord(wid).getSynset().getWords().size();
        if (seen > tickNext)
        {
          tickNext = seen + tickSize;
        }
      }
  }

  private ISynset _getSynsetByIndex(String word, String pos, int sense)
  {
    IIndexWord idxWord = dict.getIndexWord(word, getJWIPos(pos));
    IWordID wordID = idxWord.getWordIDs().get(sense);
    IWord iword = dict.getWord(wordID);
    return iword.getSynset();
  }

  private POS getJWIPos(String pos)
  {
    if (pos == null || pos.length() != 1)
      throw new RiWordNetError("Bad POS: " + pos);
    return POS.getPartOfSpeech(pos.charAt(0));
  }

  String[] _getSynset(String word, String pos, boolean includeOriginal)
  {
    // look up first sense of the word "dog"
    ISynset synset = _getFirstSynset(word, pos);

    Set<String> s = new HashSet<String>();
    for (IWord w : synset.getWords())
    {
      String lemma = w.getLemma();
      if (ignorable(lemma) || (includeOriginal || !word.equals(lemma)))
        s.add(lemma);
    }

    return s.toArray(EA);
  }

  public static void main(String[] args)
  {
    RiWordNet old = new RiWordNet("/WordNet-3.1");
    RiWordNetJWI wn = new RiWordNetJWI("/WordNet-3.1");

    RiTa.out(old.getSynset("dog", "n"));
    RiTa.out(wn.getSynset("dog", "n"));
  }
}
