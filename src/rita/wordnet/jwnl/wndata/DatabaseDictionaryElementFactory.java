package rita.wordnet.jwnl.wndata;


import java.sql.ResultSet;
import java.sql.SQLException;

import rita.wordnet.jwnl.util.factory.Createable;

public interface DatabaseDictionaryElementFactory extends Createable {
	/** Create an IndexWord from a row in the database. */
	public IndexWord createIndexWord(POS pos, String lemma, ResultSet rs) throws SQLException;
	/** Create a Synset from a row in the database. */
	public Synset createSynset(
            POS pos, long offset, ResultSet synset, ResultSet words, ResultSet pointers, ResultSet verbFrames)
            throws SQLException;
	/** Create an Exc from a row in the database. */
	public Exc createExc(POS pos, String derivation, ResultSet rs) throws SQLException;
}
