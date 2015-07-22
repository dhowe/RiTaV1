package rita.wordnet.jwnl.dictionary;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.dictionary.database.DatabaseManager;
import rita.wordnet.jwnl.dictionary.database.Query;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.wndata.DatabaseDictionaryElementFactory;
import rita.wordnet.jwnl.wndata.DictionaryElement;
import rita.wordnet.jwnl.wndata.Exc;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;

public class DatabaseBackedDictionary extends AbstractCachingDictionary {
    /**
     * <code>MorphologicalProcessor</code> class install parameter. The value should be the
     * class of <code>MorphologicalProcessor</code> to use.
     */
    public static final String MORPH = "morphological_processor";
    public static final String DICTIONARY_ELEMENT_FACTORY = "dictionary_element_factory";
    public static final String DATABASE_MANAGER = "database_manager";

    private DatabaseDictionaryElementFactory _elementFactory;
    private DatabaseManager _dbManager;

    public DatabaseBackedDictionary() {
    }

    private DatabaseBackedDictionary(
            MorphologicalProcessor morph,
            DatabaseDictionaryElementFactory elementFactory,
            DatabaseManager dbManager) {
        super(morph);
        _elementFactory = elementFactory;
        _dbManager = dbManager;
    }

    public void install(Map params) throws JWNLException {
        Param param = (Param) params.get(MORPH);
        MorphologicalProcessor morph =
                (param == null) ? null : (MorphologicalProcessor) param.create();

        param = (Param) params.get(DICTIONARY_ELEMENT_FACTORY);
        DatabaseDictionaryElementFactory factory =
                (param == null) ? null : (DatabaseDictionaryElementFactory) param.create();

        param = (Param) params.get(DATABASE_MANAGER);
        DatabaseManager manager = (param == null) ? null : (DatabaseManager) param.create();

        setDictionary(new DatabaseBackedDictionary(morph, factory, manager));
    }

    public IndexWord getIndexWord(POS pos, String lemma) throws JWNLException {
        //lemma = prepareQueryString(lemma);
        IndexWord word = null;
        if (lemma.length() > 0) {
            if (isCachingEnabled()) {
                word = getCachedIndexWord(new POSKey(pos, lemma));
            }
            if (word == null) {
                Query query = null;
                try {
                    query = _dbManager.getIndexWordSynsetsQuery(pos, lemma);
                    word = _elementFactory.createIndexWord(pos, lemma, query.execute());
                    if (word != null && isCachingEnabled()) {
                        cacheIndexWord(new POSKey(pos, lemma), word);
                    }
                } catch (SQLException e) {
                    throw new JWNLException("DICTIONARY_EXCEPTION_023", e);
                } finally {
                    query.close();
                }
            }
        }
        return word;
    }

    public Iterator getIndexWordIterator(POS pos) throws JWNLException {
        Query query = _dbManager.getIndexWordLemmasQuery(pos);
        return new IndexWordIterator(pos, query);
    }

    public Iterator getIndexWordIterator(POS pos, String substring) throws JWNLException {
        Query query = _dbManager.getIndexWordLemmasQuery(pos, substring);
        return new IndexWordIterator(pos, query);
    }

    public IndexWord getRandomIndexWord(POS pos) throws JWNLException {
        Query query = _dbManager.getRandomIndexWordQuery(pos);
        String lemma = null;

        try {
            query.execute();
            query.getResults().next();
            lemma = query.getResults().getString(1);
        } catch (SQLException ex) {
            throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
        } finally {
            query.close();
        }

        return getIndexWord(pos, lemma);
    }

    public Synset getSynsetAt(POS pos, long offset) throws JWNLException {
        Synset synset = null;
        if (isCachingEnabled()) {
            synset = getCachedSynset(new POSKey(pos, offset));
        }
        if (synset == null) {
            Query query = null;
            Query wordQuery = null;
            Query pointerQuery = null;
            Query verbFrameQuery = null;
            try {
                query = _dbManager.getSynsetQuery(pos, offset);
                wordQuery = _dbManager.getSynsetWordQuery(pos, offset);
                pointerQuery = _dbManager.getPointerQuery(pos, offset);
                verbFrameQuery = _dbManager.getVerbFrameQuery(pos, offset);
                synset = _elementFactory.createSynset(pos, offset, query.execute(), wordQuery.execute(),
                        pointerQuery.execute(), verbFrameQuery.execute());
                if (synset != null && isCachingEnabled()) {
                    cacheSynset(new POSKey(pos, offset), synset);
                }
            } catch (SQLException e) {
                throw new JWNLException("DICTIONARY_EXCEPTION_023", e);
            } finally {
                query.close();
                wordQuery.close();
                pointerQuery.close();
                verbFrameQuery.close();
            }
        }
        return synset;
    }

    public Iterator getSynsetIterator(POS pos) throws JWNLException {
        Query query = _dbManager.getSynsetsQuery(pos);
        return new SynsetIterator(pos, query);
    }

    public Exc getException(POS pos, String derivation) throws JWNLException {
        Exc exc = null;
        if (isCachingEnabled()) {
            exc = getCachedException(new POSKey(pos, derivation));
        }
        if (exc == null) {
            Query query = null;
            try {
                query = _dbManager.getExceptionQuery(pos, derivation);
                exc = _elementFactory.createExc(pos, derivation, query.execute());
                if (exc != null && isCachingEnabled()) {
                    cacheException(new POSKey(pos, derivation), exc);
                }
            } catch (SQLException e) {
                throw new JWNLException("DICTIONARY_EXCEPTION_023", e);
            } finally {
                query.close();
            }
        }
        return exc;
    }

    public Iterator getExceptionIterator(POS pos) throws JWNLException {
        Query query = _dbManager.getExceptionsQuery(pos);
        return new ExceptionIterator(pos, query);
    }

    public void close() {
    }

    private abstract class DatabaseElementIterator implements Iterator {
        private POS _pos;
        private Query _lemmas;
        private boolean _advanced = false;
        private boolean _hasNext = false;

        protected DatabaseElementIterator(POS pos, Query query) {
            _pos = pos;
            _lemmas = query;
        }

        public boolean hasNext() {
            if (!_advanced) {
                _advanced = true;
                try {
                    _hasNext = getResults().next();
                } catch (SQLException e) {
                    _hasNext = false;
                }
            }
            if (!_hasNext) {
                _lemmas.close();
            }
            return _hasNext;
        }

        public Object next() {
            if (hasNext()) {
                _advanced = false;
                try {
                    return createElement();
                } catch (Exception e) {
                    return null;
                }
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        protected abstract DictionaryElement createElement() throws Exception;

        protected POS getPOS() {
            return _pos;
        }

        protected ResultSet getResults() throws SQLException {
            if (!_lemmas.isExecuted()) {
                _lemmas.execute();
            }
            return _lemmas.getResults();
        }

        protected void finalize() throws Throwable {
            _lemmas.close();
        }
    }

    private class IndexWordIterator extends DatabaseElementIterator {
        public IndexWordIterator(POS pos, Query query) {
            super(pos, query);
        }

        protected DictionaryElement createElement() throws Exception {
            String lemma = getResults().getString(1);
            return getIndexWord(getPOS(), lemma);
        }
    }

    private class SynsetIterator extends DatabaseElementIterator {
        public SynsetIterator(POS pos, Query query) {
            super(pos, query);
        }

        protected DictionaryElement createElement() throws Exception {
            long offset = getResults().getLong(1);
            return getSynsetAt(getPOS(), offset);
        }
    }

    private class ExceptionIterator extends DatabaseElementIterator {
        public ExceptionIterator(POS pos, Query query) {
            super(pos, query);
        }

        protected DictionaryElement createElement() throws Exception {
            String derivation = getResults().getString(1);
            return getException(getPOS(), derivation);
        }
    }
}