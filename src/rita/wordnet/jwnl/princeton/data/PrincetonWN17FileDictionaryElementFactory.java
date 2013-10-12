/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.princeton.data;


import java.util.Map;

import rita.wordnet.jwnl.JWNLException;

/** <code>FileDictionaryElementFactory</code> that produces elements for the Princeton release of WordNet v 1.7 */
public class PrincetonWN17FileDictionaryElementFactory extends AbstractPrincetonFileDictionaryElementFactory {
	public PrincetonWN17FileDictionaryElementFactory() {
	}

	public Object create(Map params) throws JWNLException {
		return new PrincetonWN17FileDictionaryElementFactory();
	}
}
