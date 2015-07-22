package rita.wordnet;

import java.util.Iterator;
import java.util.List;

import rita.RiWordNet;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.dictionary.Dictionary;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.IndexWordSet;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.PointerType;
import rita.wordnet.jwnl.wndata.PointerUtils;
import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.list.PointerTargetNode;
import rita.wordnet.jwnl.wndata.relationship.AsymmetricRelationship;
import rita.wordnet.jwnl.wndata.relationship.RelationshipFinder;
import rita.wordnet.jwnl.wndata.relationship.RelationshipList;


/**
  Note: these relationships only hold for nouns and verbs 
  @invisible
  @author dhowe
  <p>See the accompanying documentation for license information 
 */
public class WordnetDistance //extends SentenceDistance 
{
	private static final POS[] wordTypes = { POS.NOUN };
	
	private Dictionary dictionary;

	public WordnetDistance(Dictionary wordnetDict) 
	{
     for (int i = 0; i <= MAX_STRING_LENGTH; i++) {
       dist[i][0] = i;
     }
     for (int j = 0; j <= MAX_STRING_LENGTH; j++) {
       dist[0][j] = j;
     }	
		 this.dictionary = wordnetDict;
	}
  private static final int MAX_STRING_LENGTH = 500;

  private String[][] str = new String[2][];
  private float[][] dist = new float[MAX_STRING_LENGTH + 1][MAX_STRING_LENGTH + 1];

  public float getSentenceDistance(String string1, String string2) 
  {
    float cost = 0.0f;

    str[0] = string1.split(" ");
    str[1] = string2.split(" ");

    for (int i = 1; i <= str[0].length; i++) {
      for (int j = 1; j <= str[1].length; j++) {
        try {
          cost = getLemmaDistance(str[0][i - 1], str[1][j - 1]);
        } 
        catch (Exception e) {
          cost = 1.0f;
        }

        dist[i][j] = Math.min(
            Math.min(dist[i - 1][j] + 1.0f, dist[i][j - 1] + 1.0f), dist[i - 1][j - 1] + cost);
      }
    }

    return dist[str[0].length][str[1].length];
  }

/*  public float getLemmaDistance(String lemma1, String lemma2) 
  {
    return (lemma1.equals(lemma2)) ? 0.0f : 1.0f;
  }*/

	// get distance between two lemmas
	public float getLemmaDistance(String lemma1, String lemma2) 
	{
		IndexWordSet WORDSET1, WORDSET2;
		IndexWord WORD1, WORD2;

		float d;
		float smallestD = 1.0f;
		POS p;

		if (lemma1.equals(lemma2)) {
			smallestD = 0.0f;
		} else {
			try {
				// get complete definition for each word (all POS, all senses)
				WORDSET1 = this.dictionary.lookupAllIndexWords(lemma1);
				WORDSET2 = this.dictionary.lookupAllIndexWords(lemma2);

				// for each POS listed in wordTypes...
				for (int i = 0; i < wordTypes.length; i++) {
					p = wordTypes[i];

					if (WORDSET1.isValidPOS(p) && WORDSET2.isValidPOS(p)) {
						WORD1 = WORDSET1.getIndexWord(p);
						WORD2 = WORDSET2.getIndexWord(p);

						// get distance between words based on this POS
						d = getWordDistance(WORD1, WORD2);
						if (d < smallestD) {
							smallestD = d;
						}
					}
				}
			} catch (JWNLException e) {
				System.err.println("Error in WordNet module: " + e);
				return 1.0f;
			}
		}

		return smallestD;
	}

	// get distance between words that are the same POS
	private float getWordDistance(IndexWord start, IndexWord end) throws JWNLException
	{
		RelationshipList relList;
		AsymmetricRelationship rel;
		int cpIndex, relLength, depth, depthRootCp, depthCpLeaf;
		float distance, newDistance;
		PointerTargetNode cpNode;
		Synset cpSynset;
		List cpHypListList;

		distance = 1.0f;
		
		// for each pairing of word senses...
		for (int i = 1; i <= start.getSenseCount(); i++) 
		{
			for (int j = 1; j <= end.getSenseCount(); j++) 
			{
				// get list of relationships between words (usually only one, I think)
				try {
					relList = RelationshipFinder.getInstance().findRelationships
					  (start.getSense(i), end.getSense(j), PointerType.HYPERNYM);
					//System.out.println("WordnetDistance.getWordDistance().RelList="+relList);
				} 
				catch (Exception e) {
				  //System.out.println("WordnetDistance.getWordDistance().Exception: "+e.getMessage());
					continue;
				}
				
				int k = 0;
				
				// calculate distance for each one
				for (Iterator relListItr = relList.iterator(); relListItr.hasNext(); k++) 
				{
					rel = (AsymmetricRelationship) relListItr.next();
					
					cpIndex = rel.getCommonParentIndex();
					
					relLength = rel.getDepth();
					
					// distance between items through CP (depth of furthest word from CP)
					depthCpLeaf = Math.max(relLength - cpIndex, cpIndex); 

					// get the CPI node
					cpNode = (PointerTargetNode) rel.getNodeList().get(cpIndex);

          // get the synset of the CPI node
					cpSynset = cpNode.getSynset();
          
					// get all the hypernyms of the CP synset
					// returns a list of hypernym chains. probably always one chain, but  better safe...
					cpHypListList = (PointerUtils.getInstance().getHypernymTree(cpSynset,50)).toList();

					// get shortest depth from root to CP
					depthRootCp = -1;
					for (Iterator cpHypListListItr = cpHypListList.iterator(); cpHypListListItr.hasNext();) {
						depth = ((List) cpHypListListItr.next()).size();
						if (depthRootCp == -1) {
							depthRootCp = depth;
						} else {
							if (depth < depthRootCp) {
								depthRootCp = depth;
							}
						}
					}

					newDistance = (float) depthCpLeaf / (depthRootCp + depthCpLeaf);
					
					if (newDistance < distance)
						distance = newDistance;
				}
			}
		}

		return distance;
	}
	
	public static void main(String[] args) throws JWNLException {
		RiWordNet c = new RiWordNet("/WordNet-3.1");
		WordnetDistance wd = new WordnetDistance(c.getDictionary());
		IndexWord iw1 = c.getDictionary().getIndexWord(POS.ADJECTIVE, "happy");
		IndexWord iw2 = c.getDictionary().getIndexWord(POS.ADJECTIVE, "sad");
		System.out.println(wd.getWordDistance(iw1, iw2));
		//System.out.println(wd.getLemmaDistance("happy", "sad"));
	}
}
