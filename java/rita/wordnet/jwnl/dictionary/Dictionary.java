/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary;

import java.util.Iterator;

import rita.RiWordNet;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.util.factory.Installable;
import rita.wordnet.jwnl.wndata.Exc;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.IndexWordSet;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;


/**
 * Abstract representation of a WordNet dictionary.
 * See the architecture documentation for information on subclassing Dictionary.
 */
public abstract class Dictionary implements Installable {
	
	/** The singleton instance of the dictionary to be used throughout the system. */
	private static Dictionary _dictionary = null;

	public static Dictionary getInstance() {
		return _dictionary;
	}

	protected static void setDictionary(Dictionary dictionary) {
		_dictionary = dictionary;
	}

	public static void uninstall() {
		if (_dictionary != null) {
			_dictionary.close();
			_dictionary = null;
		}
	}

	/**
	 * Prepares the lemma for being used in a lookup operation.
	 * Specifically, this method trims whitespace and converts the lemma
	 * to lower case.
	 * @param lemma the lemma to be prepared
	 * @return String the prepared lemma
	 */
	protected static String prepareQueryString(String lemma) {
		return lemma.trim().toLowerCase();
	}   

	private MorphologicalProcessor _morph = null;

	/**
	 * Create a Dictionary that does not do morphological processing.
	 */
	protected Dictionary() {}

	/**
	 * Create a Dictionary using the specified MorphologicalProcessor.
	 */
	protected Dictionary(MorphologicalProcessor morph) {
		_morph = morph;
	}

	/**
	 * Return an Iterator over all the IndexWords of part-of-speech
	 * <var>pos</var> in the database.
	 * @param	pos	The part-of-speech
	 * @return An iterator over <code>IndexWord</code>s
	 */
	public abstract Iterator getIndexWordIterator(POS pos) throws JWNLException;

	/**
	 * Return an Iterator over all the IndexWords of part-of-speech <var>pos</var>
	 * whose lemmas contain <var>substring</var> as a substring.
	 * @param pos The part-of-speech.
	 * @return An iterator over <code>IndexWord</code>s.

	public abstract Iterator getIndexWordIterator(POS pos, String substring) throws JWNLException;
   */
	
	/**
	 * Look up a word in the database. The search is case-independent,
	 * and phrases are separated by spaces ("look up", not "look_up").
	 * Note: this method does not subject <var>lemma</var> to any
	 * morphological processing. If you want this, use {@link #lookupIndexWord(POS, String)}.
	 * @param pos The part-of-speech.
	 * @param lemma The orthographic representation of the word.
	 * @return An IndexWord representing the word, or <code>null</code> if
	 * 			no such entry exists.
	 */
	public abstract IndexWord getIndexWord(POS pos, String lemma) throws JWNLException;

  public abstract IndexWord getRandomIndexWord(POS pos) throws JWNLException;

	/**
	 * Return an Iterator over all the Synsets of part-of-speech <var>pos</var>
	 * in the database.
	 * @param pos The part-of-speech.
	 * @return An iterator over <code>Synset</code>s.
	 */
	public abstract Iterator getSynsetIterator(POS pos) throws JWNLException;

	/**
	 * Return the <code>Synset</code> at offset <code>offset</code> from the database.
	 * @param pos The part-of-speech file to look in
	 * @param offset The offset of the synset in the file
	 * @return A synset containing the parsed line from the database
	 */
	public abstract Synset getSynsetAt(POS pos, long offset) throws JWNLException;

	/**
	 * Return an Iterator over all the Exceptions in the database.
	 * @param	pos	the part-of-speech
	 * @return	Iterator An iterator over <code>String</code>s
	 */
	public abstract Iterator getExceptionIterator(POS pos) throws JWNLException;

	/**
	 * Lookup <code>derivation</code> in the exceptions file of part-of-speech <code>
	 * pos</code> and return an Exc object containing the results.
	 * @param pos the exception file to look in
	 * @param derivation the word to look up
	 * @return Exc the Exc object
	 */
	public abstract Exc getException(POS pos, String derivation) throws JWNLException;

	/** Shut down the dictionary */
	public abstract void close();

	public MorphologicalProcessor getMorphologicalProcessor() {
		return _morph;
	}

	/**
	 * Main word lookup procedure. First try a normal lookup. If that doesn't work,
	 * try looking up the stemmed form of the lemma.
	 * @param pos the part-of-speech of the word to look up
	 * @param lemma the lemma to look up
	 * @return IndexWord the IndexWord found by the lookup procedure, or null
	 *				if an IndexWord is not found
	 */
	public IndexWord lookupIndexWord(POS pos, String lemma) throws JWNLException {

	  if (pos == null || lemma == null) return null;
	  
		//lemma = prepareQueryString(lemma);
		IndexWord word = getIndexWord(pos, lemma);
		if (word == null && RiWordNet.useMorphologicalProcessor) {
		  MorphologicalProcessor mp = getMorphologicalProcessor();
		  if (mp != null)     
			  word = mp.lookupBaseForm(pos, lemma);
		}
		
		return word;
	}

	/**
	 * Return a set of <code>IndexWord</code>s, with each element in the set
	 * corresponding to a part-of-speech of <var>word</var>.
	 * @param lemma the word for which to lookup senses
	 * @return An array of IndexWords, each of which is a sense of <var>word</var>
	 */
	public IndexWordSet lookupAllIndexWords(String lemma) throws JWNLException {
		//lemma = prepareQueryString(lemma);
		IndexWordSet set = new IndexWordSet(lemma);
		for (Iterator itr = POS.getAllPOS().iterator(); itr.hasNext();) {
			IndexWord current = lookupIndexWord((POS)itr.next(), lemma);
			if (current != null) set.add(current);
		}
		return set;
	}
}