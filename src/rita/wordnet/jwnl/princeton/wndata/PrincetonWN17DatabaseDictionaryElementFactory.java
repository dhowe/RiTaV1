package rita.wordnet.jwnl.princeton.wndata;


import java.util.Map;

import rita.wordnet.jwnl.JWNLException;

public class PrincetonWN17DatabaseDictionaryElementFactory extends AbstractPrincetonDatabaseDictionaryElementFactory {
	public Object create(Map params) throws JWNLException {
		return new PrincetonWN17DatabaseDictionaryElementFactory();
	}
}
