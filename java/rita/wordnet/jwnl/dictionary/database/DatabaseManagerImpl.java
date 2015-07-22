package rita.wordnet.jwnl.dictionary.database;


import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.util.factory.Createable;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.wndata.POS;

public class DatabaseManagerImpl implements DatabaseManager, Createable {
    public static final String DRIVER = "driver";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static final String LEMMA_FOR_INDEX_WORD_ID_SQL =
            "SELECT iw.lemma " +
            "FROM IndexWord iw " +
            "WHERE iw.pos = ? AND iw.index_word_id = ?";

    private static final String SYNSET_IDS_FOR_INDEX_WORD_SQL =
            "SELECT iws.synset_id " +
            "FROM IndexWordSynset iws, IndexWord iw " +
            "WHERE iws.index_word_id = iw.index_word_id AND iw.pos = ?  AND iw.lemma = ?";

    private static final String COUNT_INDEX_WORDS_SQL = 
            "SELECT MIN(index_word_id), MAX(index_word_id) FROM indexword WHERE pos = ?";
    
    private static final String ALL_LEMMAS_SQL =
            "SELECT lemma FROM IndexWord WHERE pos = ?";

    private static final String ALL_LEMMAS_LIKE_SQL =
            "SELECT lemma FROM IndexWord WHERE pos = ? AND lemma LIKE ?";

    private static final String SYNSET_SQL =
            "SELECT is_adj_cluster, gloss FROM Synset WHERE pos = ? AND file_offset = ?";

    private static final String SYNSET_WORD_SQL =
            "SELECT sw.word, sw.word_index " +
            "FROM Synset s, SynsetWord sw " +
            "WHERE s.synset_id = sw.synset_id AND s.pos = ? AND s.file_offset = ?";

    private static final String SYNSET_POINTER_SQL =
            "SELECT sp.pointer_type, sp.target_offset, sp.target_pos, sp.source_index, sp.target_index " +
            "FROM Synset s, SynsetPointer sp " +
            "WHERE s.synset_id = sp.synset_id AND s.pos = ? AND s.file_offset = ?";

    private static final String SYNSET_VERB_FRAME_SQL =
            "SELECT svf.frame_number, svf.word_index " +
            "FROM Synset s, SynsetVerbFrame svf " +
            "WHERE s.synset_id = svf.synset_id AND s.pos = ? AND s.file_offset = ?";

    private static final String ALL_SYNSETS_SQL =
            "SELECT offset FROM Synset WHERE pos = ?";

    private static final String EXCEPTION_SQL =
            "SELECT exception FROM Exception WHERE pos = ? AND lemma = ?";

    private static final String ALL_EXCEPTIONS_SQL =
            "SELECT lemma FROM Exception WHERE pos = ?";

    private static final Random _rand = new Random(new Date().getTime());

    private ConnectionManager _connectionManager;
    private Map _minMaxIds = new HashMap();

    public DatabaseManagerImpl() {
    }

    public DatabaseManagerImpl(ConnectionManager connectionManager) {
        _connectionManager = connectionManager;
    }

    public Object create(Map params) throws JWNLException {
        String driverClassName = ((Param) params.get(DRIVER)).getValue();
        String url = ((Param) params.get(URL)).getValue();
        String userName = params.containsKey(USERNAME) ? ((Param) params.get(USERNAME)).getValue() : null;
        String password = params.containsKey(PASSWORD) ? ((Param) params.get(PASSWORD)).getValue() : null;
        return new DatabaseManagerImpl(new ConnectionManager(driverClassName, url, userName, password));
    }

    public Query getIndexWordSynsetsQuery(POS pos, String lemma) throws JWNLException {
        return createPOSStringQuery(pos, lemma, SYNSET_IDS_FOR_INDEX_WORD_SQL);
    }

    public Query getIndexWordLemmasQuery(POS pos) throws JWNLException {
        return createPOSQuery(pos, ALL_LEMMAS_SQL);
    }

    public Query getIndexWordLemmasQuery(POS pos, String substring) throws JWNLException {
        return createPOSStringQuery(pos, "%" + substring + "%", ALL_LEMMAS_LIKE_SQL);
    }

    public Query getRandomIndexWordQuery(POS pos) throws JWNLException {
        MinMax minMax = (MinMax) _minMaxIds.get(pos);
        if (minMax == null) {
            Query query = createPOSQuery(pos, COUNT_INDEX_WORDS_SQL);
            try {
                query.execute();
                query.getResults().next();
                minMax = new MinMax(query.getResults().getInt(1), query.getResults().getInt(2));
                _minMaxIds.put(pos, minMax);
            } catch (SQLException ex) {
                throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
            } finally {
                if (query != null) {
                    query.close();
                }
            }
        }
        int id = minMax.getMin() + _rand.nextInt(minMax.getMax() - minMax.getMin());
        return createPOSIdQuery(pos, id, LEMMA_FOR_INDEX_WORD_ID_SQL);
    }

    private class MinMax {
        private int _min;
        private int _max;

        public MinMax(int min, int max) {
            _min = min;
            _max = max;
        }

        public int getMin() {
            return _min;
        }

        public int getMax() {
            return _max;
        }
    }

    public Query getSynsetQuery(POS pos, long offset) throws JWNLException {
        return createPOSOffsetQuery(pos, offset, SYNSET_SQL);
    }

    public Query getSynsetWordQuery(POS pos, long offset) throws JWNLException {
        return createPOSOffsetQuery(pos, offset, SYNSET_WORD_SQL);
    }

    public Query getPointerQuery(POS pos, long offset) throws JWNLException {
        return createPOSOffsetQuery(pos, offset, SYNSET_POINTER_SQL);
    }

    public Query getVerbFrameQuery(POS pos, long offset) throws JWNLException {
        return createPOSOffsetQuery(pos, offset, SYNSET_VERB_FRAME_SQL);
    }

    public Query getSynsetsQuery(POS pos) throws JWNLException {
        return createPOSQuery(pos, ALL_SYNSETS_SQL);
    }

    public Query getExceptionQuery(POS pos, String derivation) throws JWNLException {
        return createPOSStringQuery(pos, derivation, EXCEPTION_SQL);
    }

    public Query getExceptionsQuery(POS pos) throws JWNLException {
        return createPOSQuery(pos, ALL_EXCEPTIONS_SQL);
    }

    private Query createPOSQuery(POS pos, String sql) throws JWNLException {
        Query query = null;
        try {
            query = _connectionManager.getQuery(sql);
            query.getStatement().setString(1, pos.getKey());
            return query;
        } catch (SQLException ex) {
            if (query != null) {
                query.close();
            }
            throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
        }
    }

    private Query createPOSStringQuery(POS pos, String str, String sql) throws JWNLException {
        Query query = null;
        try {
            query = _connectionManager.getQuery(sql);
            query.getStatement().setString(1, pos.getKey());
            query.getStatement().setString(2, str);
            return query;
        } catch (SQLException ex) {
            if (query != null) {
                query.close();
            }
            throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
        }
    }

    private Query createPOSOffsetQuery(POS pos, long offset, String sql) throws JWNLException {
        Query query = null;
        try {
            query = _connectionManager.getQuery(sql);
            query.getStatement().setString(1, pos.getKey());
            query.getStatement().setLong(2, offset);
            return query;
        } catch (SQLException ex) {
            if (query != null) {
                query.close();
            }
            throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
        }
    }

    private Query createPOSIdQuery(POS pos, int id, String sql) throws JWNLException {
        Query query = null;
        try {
            query = _connectionManager.getQuery(sql);
            query.getStatement().setString(1, pos.getKey());
            query.getStatement().setInt(2, id);
            return query;
        } catch (SQLException ex) {
            if (query != null) {
                query.close();
            }
            throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
        }
    }
}