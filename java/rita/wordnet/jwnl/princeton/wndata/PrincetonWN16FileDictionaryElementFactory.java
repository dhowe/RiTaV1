/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.princeton.wndata;


import java.util.Map;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.wndata.Adjective;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.Word;

/** <code>FileDictionaryElementFactory</code> that produces elements for Princeton's release of WordNet v 1.6 */
public class PrincetonWN16FileDictionaryElementFactory extends AbstractPrincetonFileDictionaryElementFactory {
  
	public PrincetonWN16FileDictionaryElementFactory() {
	  //System.out.println("PrincetonWN16FileDictionaryElementFactory.PrincetonWN16()");
	}

	public Object create(Map params) throws JWNLException {
		return new PrincetonWN16FileDictionaryElementFactory();
	}

	protected Word createWord(Synset synset, int index, String lemma) {
		
	  if (synset.getPOS().equals(POS.ADJECTIVE)) {
			
		  Adjective.AdjectivePosition adjectivePosition = Adjective.NONE;
			
			if (lemma.charAt(lemma.length() - 1) == ')' && lemma.indexOf('(') > 0) {
			  
				int lparen = lemma.indexOf('(');
				String marker = lemma.substring(lparen + 1, lemma.length() - 1);
				adjectivePosition = Adjective.getAdjectivePositionForKey(marker);
				lemma = lemma.substring(0, lparen);
			}
			
			return new Adjective(synset, index, lemma, adjectivePosition);
		} 

		return super.createWord(synset, index, lemma);
	}
}
