package rita.wordnet.jwnl.princeton.wndata;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import rita.wordnet.jwnl.wndata.DatabaseDictionaryElementFactory;
import rita.wordnet.jwnl.wndata.Exc;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Pointer;
import rita.wordnet.jwnl.wndata.PointerType;
import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.SynsetProxy;
import rita.wordnet.jwnl.wndata.Verb;
import rita.wordnet.jwnl.wndata.Word;
/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
public abstract class AbstractPrincetonDatabaseDictionaryElementFactory implements DatabaseDictionaryElementFactory {
    public IndexWord createIndexWord(POS pos, String lemma, ResultSet rs) throws SQLException {
        List offsets = new ArrayList();
        while (rs.next()) {
            offsets.add(new Long(rs.getLong(1)));
        }
        if (offsets.isEmpty()) return null;

        long[] offsetArray = new long[offsets.size()];
        Iterator itr = offsets.iterator();
        for (int i = 0; itr.hasNext(); i++) {
            offsetArray[i] = ((Long) itr.next()).longValue();
        }

        return new IndexWord(lemma, pos, offsetArray);
    }

    public Synset createSynset(
            POS pos, long offset, ResultSet synset, ResultSet words, ResultSet pointers, ResultSet verbFrames)
            throws SQLException {
        synset.next();
        boolean isAdjectiveCluster = synset.getBoolean(1);
        String gloss = synset.getString(2);

        SynsetProxy proxy = new SynsetProxy(pos);

        List wordList = new ArrayList();
        while (words.next()) {
            String lemma = words.getString(1);
            int index = words.getInt(2);
            wordList.add(createWord(proxy, index, lemma));
        }

        List pointerList = new ArrayList();
        while (pointers.next()) {
            PointerType type = PointerType.getPointerTypeForKey(pointers.getString(1));
            long targetOffset = pointers.getLong(2);
            POS targetPOS = POS.getPOSForKey(pointers.getString(3));
            int sourceIndex = pointers.getInt(4);
            int targetIndex = pointers.getInt(5);
            pointerList.add(new Pointer(proxy, sourceIndex, type, targetPOS, targetOffset, targetIndex));
        }

        while (verbFrames.next()) {
            int frameNumber = verbFrames.getInt(1);
            int wordIndex = verbFrames.getInt(2);
            if (wordIndex > 0) {
                ((MutableVerb) wordList.get(wordIndex - 1)).setVerbFrameFlag(frameNumber);
            } else {
                for (Iterator itr = wordList.iterator(); itr.hasNext();) {
                    ((MutableVerb) itr.next()).setVerbFrameFlag(frameNumber);
                }
            }
        }

        BitSet verbFrameBits = new BitSet();
        for (Iterator itr = wordList.iterator(); itr.hasNext();) {
            Word word = (Word) itr.next();
            if (word instanceof Verb) {
                verbFrameBits.or(((Verb) words).getVerbFrameFlags());
            }
        }

        proxy.setSource(new Synset(
                pos, offset, (Word[]) wordList.toArray(new Word[wordList.size()]),
                (Pointer[]) pointerList.toArray(new Pointer[pointerList.size()]),
                gloss, verbFrameBits, isAdjectiveCluster));

        return proxy;
    }

    protected Word createWord(Synset synset, int index, String lemma) {
        if (synset.getPOS().equals(POS.VERB)) {
            return new MutableVerb(synset, index, lemma);
        } else {
            return new Word(synset, index, lemma);
        }
    }

    public Exc createExc(POS pos, String derivation, ResultSet rs) throws SQLException {
        List exceptions = new ArrayList();
        while (rs.next()) {
            exceptions.add(rs.getString(1));
        }
        return new Exc(pos, derivation, exceptions);
    }
}