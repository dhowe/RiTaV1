package rita.wordnet.jwnl.dictionary.morph;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.util.factory.Createable;
import rita.wordnet.jwnl.wndata.POS;

public interface Operation extends Createable {
	/**
	 * Execute the operation.
	 * @param pos
	 * @param lemma
	 * @param baseForms BaseFormSet to which all discovered base forms should
	 *        be added.
	 * @return true if at least one base form was discovered by the operation and
	 *         added to <var>baseForms</var>.
	 * @throws JWNLException
	 */
	boolean execute(POS pos, String lemma, BaseFormSet baseForms) throws JWNLException;
}