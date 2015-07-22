package rita.wordnet.jwnl.dictionary.morph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.wndata.POS;

/**
 * Remove all aplicable suffixes from the word(s) and do a look-up.
 * This class accepts parameters in the form of:
 * <pre>
 *
 *  <param name="{part-of-speech}" value="|{suffix}={stemmed suffix}|..."/>
 *
 * </pre>
 * where suffix is the {suffix} to convert from, and {stemmed suffix} is
 * the suffix to convert to.
 */
public class DetachSuffixesOperation extends AbstractDelegatingOperation {
	public static final String OPERATIONS = "operations";

	private Map _suffixMap;

    protected AbstractDelegatingOperation getInstance(Map params) throws JWNLException {
        Map suffixMap = new HashMap();
		for (Iterator itr = params.values().iterator(); itr.hasNext();) {
			Param p = (Param) itr.next();
            POS pos = POS.getPOSForLabel(p.getName());
            if (pos != null) {
                suffixMap.put(pos, getSuffixArray(p.getValue()));
            }
		}
		return new DetachSuffixesOperation(suffixMap);
	}

	private String[][] getSuffixArray(String suffixes) throws JWNLException {
		StringTokenizer tokenizer = new StringTokenizer(suffixes, "|=", true);
		if (!"|".equals(tokenizer.nextToken())) {
			throw new JWNLException("DICTIONARY_EXCEPTION_028");
		}
		List suffixList = new ArrayList();
		while (tokenizer.hasMoreTokens()) {
			String next = tokenizer.nextToken();
			String first = "";
			String second = "";
			if (!"=".equals(next)) {
				first = next;
				tokenizer.nextToken();
			}
			next = tokenizer.nextToken();
			if (!"|".equals(next)) {
				second = next;
				tokenizer.nextToken();
			}
			suffixList.add(new String[] {first, second});
		}
		return (String[][]) suffixList.toArray(new String[suffixList.size()][]);
	}

	public DetachSuffixesOperation() {
	}

	public DetachSuffixesOperation(Map suffixMap) {
		_suffixMap = suffixMap;
	}

	protected String[] getKeys() {
		return new String[] {OPERATIONS};
	}

	public Map getSuffixMap() {
		return _suffixMap;
	}

	public void setSuffixMap(Map suffixMap) {
		_suffixMap = suffixMap;
	}

	public boolean execute(POS pos, String derivation, BaseFormSet forms) throws JWNLException {
		String[][] suffixArray = (String[][]) _suffixMap.get(pos);
		if (suffixArray == null) {
			return false;
		}

		boolean addedBaseForm = false;
		for (int i = 0; i < suffixArray.length; i++) {
			if (derivation.endsWith(suffixArray[i][0])) {
				String stem = derivation.substring(
                        0, derivation.length() - suffixArray[i][0].length()) + suffixArray[i][1];
				if (delegate(pos, stem, forms, OPERATIONS)) {
					addedBaseForm = true;
				}
			}
		}
		return addedBaseForm;
	}
}