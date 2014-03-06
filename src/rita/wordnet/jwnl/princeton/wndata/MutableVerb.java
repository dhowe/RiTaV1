package rita.wordnet.jwnl.princeton.wndata;


import java.util.BitSet;

import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.Verb;

/**
 * Wrapper for a verb that allows the VerbFrame flags to be set after the Verb
 * is created.
 */
class MutableVerb extends Verb {
	public MutableVerb(Synset synset, int index, String lemma) {
		super(synset, index, lemma, new BitSet());
	}

	public void setVerbFrameFlag(int fnum) {
		getVerbFrameFlags().set(fnum);
	}
}
