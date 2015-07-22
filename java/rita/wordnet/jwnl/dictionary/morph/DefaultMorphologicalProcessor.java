/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary.morph;


import java.util.List;
import java.util.Map;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.JWNLRuntimeException;
import rita.wordnet.jwnl.dictionary.Dictionary;
import rita.wordnet.jwnl.dictionary.MorphologicalProcessor;
import rita.wordnet.jwnl.dictionary.POSKey;
import rita.wordnet.jwnl.util.cache.Cache;
import rita.wordnet.jwnl.util.cache.LRUCache;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.util.factory.ParamList;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;

/**
 * Default implementation of <code>MorphologicalProcessor</code>. This isn't a true
 * morpological analyzer (it doesn't figure out all the characteristics of each word
 * it processes). This is basically a stemmer that uses WordNet exception files instead
 * of complex stemming rules. It also tries to be intelligent by removing delimiters and
 * doing concatanation.
 */
public class DefaultMorphologicalProcessor implements MorphologicalProcessor {
	/** Parameter that determines the size of the base form cache */
	public static final String CACHE_CAPACITY = "cache_capacity";
	/** Parameter that determines the operations this morphological processor will perform */
	public static final String OPERATIONS = "operations";

	private static final int DEFAULT_CACHE_CAPACITY = 1000;

	private Cache _lookupCache;;
	private Operation[] _operations;

	public DefaultMorphologicalProcessor() {
	}

	public DefaultMorphologicalProcessor(Operation[] operations) {
		this(operations, DEFAULT_CACHE_CAPACITY);
	}

	public DefaultMorphologicalProcessor(Operation[] operations, int cacheCapacity) {
		_lookupCache = new LRUCache(cacheCapacity);
		_operations = operations;
	}

	public Object create(Map params) throws JWNLException {
		ParamList operationParams = (ParamList) params.get(OPERATIONS);
		if (operationParams == null) {
			throw new JWNLException("DICTIONARY_EXCEPTION_026");
		}
		List operations = (List)operationParams.create();
		Operation[] operationArray = (Operation[])operations.toArray(new Operation[operations.size()]);

		Param param = (Param) params.get(CACHE_CAPACITY);
		int capacity = (param == null) ?
		    DEFAULT_CACHE_CAPACITY : new Integer(param.getValue()).intValue();

		return new DefaultMorphologicalProcessor(operationArray, capacity);
	}

	/**
	 * Lookup the base form of a word. Given a lemma, finds the WordNet
	 * entry most like that lemma. This function returns the first base form
	 * found. Subsequent calls to this function with the same part-of-speech
	 * and word will return the same base form. To find another base form for
	 * the pos/word, call lookupNextBaseForm.
	 * @param pos the part-of-speech of the word to look up
	 * @param derivation the word to look up
	 * @return IndexWord the IndexWord found during lookup
	 */
	public IndexWord lookupBaseForm(POS pos, String derivation) throws JWNLException {
		// See if we've already looked this word up
		LookupInfo info = getCachedLookupInfo(new POSKey(pos, derivation));
		if (info != null && info.getBaseForms().isCurrentFormAvailable()) {
			// get the last base form we retrieved. if you want
			// the next possible base form, use lookupNextBaseForm
			return Dictionary.getInstance().getIndexWord(pos, info.getBaseForms().getCurrentForm());
		} else {
			return lookupNextBaseForm(pos, derivation, info);
		}
	}

	private void cacheLookupInfo(POSKey key, LookupInfo info) {
		_lookupCache.put(key, info);
	}

	private LookupInfo getCachedLookupInfo(POSKey key) {
		return (LookupInfo) _lookupCache.get(key);
	}

	/**
	 * Lookup the next base form of a pos/word pair. If a base form has not
	 * yet been found for the pos/word, it will find the first base form,
	 * otherwise it will find the next base form.
	 * @param pos the part-of-speech of the word to look up
	 * @param derivation the word to look up
	 * @return IndexWord the IndexWord found during lookup, or null if an IndexWord is not found
	 */
	private IndexWord lookupNextBaseForm(POS pos, String derivation, LookupInfo info) throws JWNLException {
		if (derivation.equals("") || derivation == null) {
			return null;
		}

		String str = null;
		if (info == null) {
			POSKey key = new POSKey(pos, derivation);
			info = getCachedLookupInfo(key);
			if (info == null) {
				info = new LookupInfo(pos, derivation, _operations);
				cacheLookupInfo(key, info);
			}
		}

		// if we've already found another possible base form, return that one
		if (info.getBaseForms().isMoreFormsAvailable()) {
			str = info.getBaseForms().getNextForm();
		} else {
			while (str == null && info.isNextOperationAvailable() && !info.executeNextOperation());
			if (info.getBaseForms().isMoreFormsAvailable()) {
				str = info.getBaseForms().getNextForm();
			}
		}

		return (str == null) ? null : Dictionary.getInstance().getIndexWord(pos, str);
	}

	public List lookupAllBaseForms(POS pos, String derivation) throws JWNLException {
		LookupInfo info = getCachedLookupInfo(new POSKey(pos, derivation));
		if (info == null) {
			info = new LookupInfo(pos, derivation, _operations);
			cacheLookupInfo(new POSKey(pos, derivation), info);
		}
		int index = info.getBaseForms().getIndex();
		while (info.isNextOperationAvailable()) {
			lookupNextBaseForm(pos, derivation, info);
		}
		info.getBaseForms().setIndex(index);
		return info.getBaseForms().getForms();
	}

	private class LookupInfo {
		private POS _pos;
		private String _derivation;
		private BaseFormSet _baseForms;
		private Operation[] _operations;
		private int _currentOperation;

		public LookupInfo(POS pos, String derivation, Operation[] operations) {
			_pos = pos;
			_derivation = derivation;
			_operations = operations;
			_baseForms = new BaseFormSet();
			_currentOperation = -1;
		}

		public boolean isNextOperationAvailable() {
			return _currentOperation + 1 < _operations.length;
		}

		public boolean executeNextOperation() throws JWNLException {
			if (!isNextOperationAvailable()) {
				throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_027");
			}
			Operation oper = _operations[++_currentOperation];
			return oper.execute(_pos, _derivation, _baseForms);
		}

		public BaseFormSet getBaseForms() {
			return _baseForms;
		}
	}
}