package rita.wordnet.jwnl.dictionary.morph;


import java.util.Map;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.wndata.POS;

/** yet to be implemented */
public class RemovePrepPhrasesOperation implements Operation {
	public Object create(Map params) throws JWNLException {
		return new RemovePrepPhrasesOperation();
	}

	public boolean execute(POS pos, String lemma, BaseFormSet baseForm) {
		return false;
	}
}