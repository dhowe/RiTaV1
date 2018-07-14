/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rita.wordnet.jwnl.JWNL;

/**
 * A class to simplify the access to a set of <code>IndexWord</code>s, each
 * containing one part of speech of the same word. IndexWordSets are usually
 * created by a call to
 * {@link rita.wordnet.jwnl.dictionary.Dictionary#lookupAllIndexWords
 * Dictionary.lookupAllIndexWords}.
 */
public class IndexWordSet {
  /** Map of IndexWords in this set. */
  private Map _indexWords = new Hashtable(4, (float) 1.0);
  private String _lemma;

  public int hashCode() {
    return _indexWords.hashCode() + _lemma.hashCode();
  }

  public IndexWordSet(String lemma) {
    _lemma = lemma;
  }

  /** Add an IndexWord to this set */
  public void add(IndexWord word) {
    _indexWords.put(word.getPOS(), word);
  }

  /** Remove the IndexWord associated with <code>p</code> from this set. */
  public void remove(POS p) {
    _indexWords.remove(p);
  }

  /** Get the number of IndexWords in this set */
  public int size() {
    return _indexWords.size();
  }

  /** Get the IndexWord associated with <code>p</code>. */
  public IndexWord getIndexWord(POS p) {
    return (IndexWord) _indexWords.get(p);
  }

  /** Get an array of the IndexWords in this set. */
  public IndexWord[] getIndexWordArray() {
    IndexWord[] words = new IndexWord[_indexWords.size()];
    return (IndexWord[]) _indexWords.values().toArray(words);
  }

  /** Get a collection of the IndexWords in this set. */
  public Collection getIndexWordCollection() {
    return _indexWords.values();
  }

  /**
   * Get a set of all the parts-of-speech for which there is an IndexWord in
   * this set.
   */
  public Set getValidPOSSet() {
    return _indexWords.keySet();
  }

  /**
   * Return true if there is a word with part-of-speech <code>pos</code> in this
   * set.
   */
  public boolean isValidPOS(POS pos) {
    return _indexWords.containsKey(pos);
  }

  /**
   * Find out how many senses the word with part-of-speech <code>pos</code> has.
   */
  public int getSenseCount(POS pos) {
    return getIndexWord(pos).getSenseCount();
  }

  private transient String _cachedToString = null;

  public String toString() {
    if (_cachedToString == null) {
      String str = "";
      if (size() == 0) {
	str = JWNL.resolveMessage("DATA_TOSTRING_003");
      } else {
	StringBuffer buf = new StringBuffer();
	Iterator itr = getValidPOSSet().iterator();
	while (itr.hasNext()) {
	  buf.append(getIndexWord((POS) itr.next()).toString());
	}
	str = buf.toString();
      }
      _cachedToString = JWNL.resolveMessage("DATA_TOSTRING_004", str);
    }
    return _cachedToString;
  }

  public String getLemma() {
    return _lemma;
  }

  /**
   * It is assumed that IndexWordSets will only be created by calling
   * {@link rita.wordnet.jwnl.dictionary.Dictionary#lookupAllIndexWords
   * Dictionary.lookupAllIndexWords}, so all IndexWordSets with the same lemma
   * should be equal.
   */
  public boolean equals(Object object) {
    return (object instanceof IndexWordSet)
	&& getLemma().equals(((IndexWordSet) object).getLemma());
  }
}