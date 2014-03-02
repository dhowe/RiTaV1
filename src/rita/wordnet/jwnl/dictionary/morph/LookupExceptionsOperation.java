package rita.wordnet.jwnl.dictionary.morph;


import java.util.Map;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.dictionary.Dictionary;
import rita.wordnet.jwnl.wndata.Exc;
import rita.wordnet.jwnl.wndata.POS;

/** Lookup the word in the exceptions file of the given part-of-speech. */
public class LookupExceptionsOperation implements Operation {
	public Object create(Map params) throws JWNLException {
		return new LookupExceptionsOperation();
	}

	public boolean execute(POS pos, String derivation, BaseFormSet form) throws JWNLException {
		Exc exc = Dictionary.getInstance().getException(pos, derivation);
		if (exc != null) {
			String[] exceptions = exc.getExceptionArray();
			for (int i = 0; i < exceptions.length; i++) {
				form.add(exceptions[i]);
			}
			return true;
		}
		return false;
	}
}