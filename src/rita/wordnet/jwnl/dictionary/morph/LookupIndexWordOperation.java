package rita.wordnet.jwnl.dictionary.morph;


import java.util.Map;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.dictionary.Dictionary;
import rita.wordnet.jwnl.wndata.POS;

public class LookupIndexWordOperation implements Operation {
	public Object create(Map params) throws JWNLException {
		return new LookupIndexWordOperation();
	}

	public boolean execute(POS pos, String lemma, BaseFormSet baseForms) throws JWNLException {
		if (Dictionary.getInstance().getIndexWord(pos, lemma) != null) {
			baseForms.add(lemma);
			return true;
		}
		return false;
	}
}