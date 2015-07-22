/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.princeton.wndata;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.StringTokenizer;

import rita.RiTa;
import rita.wordnet.jwnl.util.TokenizerParser;
import rita.wordnet.jwnl.wndata.Exc;
import rita.wordnet.jwnl.wndata.FileDictionaryElementFactory;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Pointer;
import rita.wordnet.jwnl.wndata.PointerTarget;
import rita.wordnet.jwnl.wndata.PointerType;
import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.SynsetProxy;
import rita.wordnet.jwnl.wndata.Verb;
import rita.wordnet.jwnl.wndata.Word;

/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.<br>
 * <code>FileDictionaryElementFactory</code> that parses lines from the dictionary files distributed by the
 * WordNet team at Princeton's Cognitive Science department.
 */
public abstract class AbstractPrincetonFileDictionaryElementFactory implements FileDictionaryElementFactory {

	protected AbstractPrincetonFileDictionaryElementFactory() {
	  //System.out.println("AbstractPrincetonFileDictionaryElementFactory.AbstractPrincetonFileDictionaryElementFactory()");
	}

	public IndexWord createIndexWord(POS pos, String line) {
        
	  TokenizerParser tokenizer = new TokenizerParser(line, RiTa.SP);
    
    if (!tokenizer.hasMoreTokens()) return null;
      //throw new JWNLRuntimeException("Illegal tokenizer state for: "+line);
    
    String lemma = tokenizer.nextToken();//.replace('_', ' ');
    
    tokenizer.nextToken();  // pos
    tokenizer.nextToken();	// poly_cnt
    int pointerCount = tokenizer.nextInt();
    
    // TODO: can we do anything interesting with these?
    for (int i = 0; i < pointerCount; ++i) 
      tokenizer.nextToken();	// ptr_symbol
    
    int senseCount = tokenizer.nextInt();
    tokenizer.nextInt(); // tagged sense count
    long[] synsetOffsets = new long[senseCount];
    for (int i = 0; i < senseCount; i++) 
      synsetOffsets[i] = tokenizer.nextLong();
    
    return new IndexWord(lemma, pos, synsetOffsets);
	}

	public Synset createSynset(POS pos, String line) {
        TokenizerParser tokenizer = new TokenizerParser(line, RiTa.SP);

        long offset = tokenizer.nextLong();
        tokenizer.nextToken();	// lex_filenum
        String synsetPOS = tokenizer.nextToken();
        boolean isAdjectiveCluster = false;
        if (synsetPOS.equals("s"))
          isAdjectiveCluster = true;

        SynsetProxy proxy = new SynsetProxy(pos);

        int wordCount = tokenizer.nextHexInt();
        if (wordCount < 0) return null;
        Word[] words = new Word[wordCount];
        for (int i = 0; i < wordCount; i++) {
            String lemma = tokenizer.nextToken();
            tokenizer.nextHexInt(); // lex id
            words[i] = createWord(proxy, i, lemma);
        }

        int pointerCount = tokenizer.nextInt();
        Pointer[] pointers = new Pointer[pointerCount];
        for (int i = 0; i < pointerCount; i++) {
            String pt = tokenizer.nextToken();
            PointerType pointerType = PointerType.getPointerTypeForKey(pt);
            long targetOffset = tokenizer.nextLong();
            POS targetPOS = POS.getPOSForKey(tokenizer.nextToken());
            int linkIndices = tokenizer.nextHexInt();
            int sourceIndex = linkIndices / 256;
            int targetIndex = linkIndices & 255;
            PointerTarget source = (sourceIndex == 0) ? (PointerTarget) proxy : (PointerTarget) words[sourceIndex - 1];

            pointers[i] = new Pointer(source, i, pointerType, targetPOS, targetOffset, targetIndex);
        }

        if (pos == POS.VERB) {
            int verbFrameCount = tokenizer.nextInt();
            for (int i = 0; i < verbFrameCount; i++) {
                tokenizer.nextToken();	// "+"
                int frameNumber = tokenizer.nextInt();
                int wordIndex = tokenizer.nextHexInt();
                if (wordIndex > 0) {
                    ((MutableVerb) words[wordIndex - 1]).setVerbFrameFlag(frameNumber);
                } else {
                    for (int j = 0; j < words.length; ++j)
                        ((MutableVerb) words[j]).setVerbFrameFlag(frameNumber);
                }
            }
        }

        String gloss = null;
        int index = line.indexOf('|');
        if (index > 0) {
            gloss = line.substring(index + 2).trim();
        }

        BitSet verbFrames = new BitSet();
        for (int i = 0; i < words.length; i++)
            if (words[i] instanceof Verb)
                verbFrames.or(((Verb)words[i]).getVerbFrameFlags());

        Synset synset = new Synset(pos, offset, words, pointers, gloss, verbFrames, isAdjectiveCluster);
        proxy.setSource(synset);
        return proxy;
	}

	protected Word createWord(Synset synset, int index, String lemma) {
		if (synset.getPOS().equals(POS.VERB)) {
            return new MutableVerb(synset, index, lemma);
        } else {
            return new Word(synset, index, lemma);
        }
	}

	public Exc createExc(POS pos, String line) {
        StringTokenizer st = new StringTokenizer(line);
        String lemma = st.nextToken();//.replace('_', ' ');
        List exceptions = new ArrayList();
        while (st.hasMoreTokens()) {
            exceptions.add(st.nextToken());//.replace('_', ' '));
        }
        return new Exc(pos, lemma, exceptions);
	}
}