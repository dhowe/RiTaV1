package rita.wordnet.jwnl.wndata.relationship;


import java.util.List;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.wndata.IndexWord;
import rita.wordnet.jwnl.wndata.PointerType;
import rita.wordnet.jwnl.wndata.PointerUtils;
import rita.wordnet.jwnl.wndata.Synset;
import rita.wordnet.jwnl.wndata.list.PointerTargetNode;
import rita.wordnet.jwnl.wndata.list.PointerTargetNodeList;
import rita.wordnet.jwnl.wndata.list.PointerTargetTree;
import rita.wordnet.jwnl.wndata.list.PointerTargetTreeNode;
import rita.wordnet.jwnl.wndata.list.PointerTargetTreeNodeList;

public class RelationshipFinder {
	private static final int DEFAULT_ASYMMETRIC_SEARCH_DEPTH = Integer.MAX_VALUE;
	private static final int DEFAULT_SYMMETRIC_SEARCH_DEPTH = 2;

	private static final RelationshipFinder INSTANCE = new RelationshipFinder();

	public static RelationshipFinder getInstance() {
		return INSTANCE;
	}

	private RelationshipFinder() {
	}

	/**
	 * Looks at whether the target word is one of the words in one of the synsets
	 * of the source word.
	 * @return int the sense of the source word that contains the target word
	 */
	public int getImmediateRelationship(IndexWord sourceWord, IndexWord targetWord) throws JWNLException {
		Synset[] senses = sourceWord.getSenses();
		String lemma = targetWord.getLemma();
		for (int i = 0; i < senses.length; i++) {
			if (senses[i].containsWord(lemma)) {
				return i + 1;
			}
		}
		return -1;
	}

	/**
	 * Find all relationships of type <var>type</var> between <var>sourceSynset</var> and <var>targetSynset</var>.
	 * This method creates a symmetric or asymmetric relationship based on whether <var>type</var> is symmetric.
	 */
	public RelationshipList findRelationships(
	    Synset sourceSynset, Synset targetSynset, PointerType type) throws JWNLException {

		boolean b =  type.isSymmetric();
		//System.out.println("isSymmetric="+b);
		if (b)
		  return findSymmetricRelationships(sourceSynset, targetSynset, type);
		else
		  return findAsymmetricRelationships(sourceSynset, targetSynset, type);
	}

	/**
	 * Find all relationships of type <var>type</var> between <var>sourceSynset</var> and <var>targetSynset</var>
	 * to depth <var>depth</var>. This method creates a symmetric or asymmetric relationship based on
	 * whether <var>type</var> is symmetric.
	 */
	public RelationshipList findRelationships(
	    Synset sourceSynset, Synset targetSynset, PointerType type, int depth) throws JWNLException {

		return (type.isSymmetric()) ?
		    findSymmetricRelationships(sourceSynset, targetSynset, type, depth) :
		    findAsymmetricRelationships(sourceSynset, targetSynset, type, depth);
	}

	/**
	 * Finds the asymmetric relationship(s) between two words. A relationship is
	 * asymmetric if its type is asymmetric (i.e. it's not its own inverse).
	 */
	private RelationshipList findAsymmetricRelationships(
	    Synset sourceSynset, Synset targetSynset, PointerType type) throws JWNLException {

		return findAsymmetricRelationships(sourceSynset, targetSynset, type, 1000);
	}

	/**
	 * Finds the asymmetric relationship(s) between two words. A relationship is
	 * asymmetric if its type is asymmetric (i.e. it's not its own inverse).
	 */
	private RelationshipList findAsymmetricRelationships(
	    Synset sourceSynset, Synset targetSynset, PointerType type, int depth) throws JWNLException {

		// We run the reversal function on the trees to get linear (non-branching)
		// paths from the source word to its deepest ancestor (i.e. if there are
		// multiple relations from a single word anywhere in the path, the reversal
		// function will break them down into multiple, linear paths).
		PointerTargetNodeList[] sourceRelations = new PointerTargetTree(
		    sourceSynset, PointerUtils.getInstance().makePointerTargetTreeList(sourceSynset, type, depth)).reverse();
		PointerTargetNodeList[] targetRelations = new PointerTargetTree(
		    targetSynset, PointerUtils.getInstance().makePointerTargetTreeList(targetSynset, type, depth)).reverse();

		RelationshipList relationships = new RelationshipList();
		// Do an exhaustive search for relationships
		for (int i = 0; i < sourceRelations.length; i++) {
			for (int j = 0; j < targetRelations.length; j++) {
				Relationship relationship = findAsymmetricRelationship(
				    sourceRelations[i], targetRelations[j], type, sourceSynset, targetSynset);
				if (relationship != null) {
					relationships.add(relationship);
				}
			}
		}
		return relationships;
	}

	/**
	 * Find a relationship between two asymmetric lists ordered from deepest
	 * to shallowest ancestor. Each node has it's PointerType set to the kind of
	 * relationship one need to follow to get from it to the next node in the list.
	 * Take the dog/cat relationship. To get to carnivore, a hypernym relationship
	 * must be used to get from dog to carnivore, but then a hyponym relationship
	 * must be used to get from carnivore to cat. The list will look like this:
	 * dog(hyper) -> canine(hyper) -> carnivore(hypo) -> feline(hypo) -> cat(hypo).
	 * In this instance, cat's PointerType is meaningless, but is kept to facilitate
	 * things like reversing the relationship (which just involves setting each node's
	 * pointer type to the symmetric type of its current type.
	 */
	private Relationship findAsymmetricRelationship(
	    PointerTargetNodeList sourceNodes, PointerTargetNodeList targetNodes,
	    PointerType type, Synset sourceSynset, Synset targetSynset) {

		// If the deepest ancestors of the words are not the same,
		// then there is no relationship between the words.
		if (!sourceNodes.get(0).equals(targetNodes.get(0))) return null;

		PointerTargetNodeList relationship = new PointerTargetNodeList();
		int targetStart = 0;
		int commonParentIndex = 0;
		for (int i = sourceNodes.size() - 1; i >= 0; i--) {
			PointerTargetNode testNode = (PointerTargetNode)sourceNodes.get(i);
			int idx = targetNodes.indexOf(testNode);
			if (idx >= 0) {
				targetStart = idx;
				break;
			} else {
				relationship.add(testNode.clone());
				commonParentIndex++;
			}
		}
		for (int i = targetStart; i < targetNodes.size(); i++) {
			PointerTargetNode node = (PointerTargetNode)((PointerTargetNode)targetNodes.get(i)).clone();
			node.setType(type.getSymmetricType());
			relationship.add(node);
		}
		return new AsymmetricRelationship(type, relationship, commonParentIndex, sourceSynset, targetSynset);
	}

	/**
	 * A symmetric relationship is one whose type is symmetric (i.e. is it's own
	 * inverse. An example of a symmetric relationship is synonomy.
	 */
	private RelationshipList findSymmetricRelationships(
	    Synset sourceSynset, Synset targetSynset, PointerType type) throws JWNLException {

		return findSymmetricRelationships(sourceSynset, targetSynset, type, DEFAULT_SYMMETRIC_SEARCH_DEPTH);
	}

	/** A symmetric relationship is one whose type is symmetric (i.e. is it's own inverse). */
	private RelationshipList findSymmetricRelationships(
	    final Synset sourceSynset, final Synset targetSynset, PointerType type, int depth) throws JWNLException {

		PointerTargetTree tree = new PointerTargetTree(
		    sourceSynset, PointerUtils.getInstance().makePointerTargetTreeList(sourceSynset, type, null, depth, false));

		PointerTargetTreeNodeList.Operation opr = new PointerTargetTreeNodeList.Operation() {
			public Object execute(PointerTargetTreeNode testNode) {
				if (targetSynset.equals(testNode.getPointerTarget())) {
					return testNode;
				}
				return null;
			}
		};
		List l = tree.getAllMatches(opr);

		RelationshipList list = new RelationshipList();
		for (int i = 0; i < l.size(); i++) {
			PointerTargetNodeList nodes = findSymmetricRelationship((PointerTargetTreeNode)l.get(i), type);
			list.add(new SymmetricRelationship(type, nodes, sourceSynset, targetSynset));
		}
		return list;
	}

	/**
	 * Build a relationsip from <var>node</var> back to it's root ancestor and
	 * then reverse the list.
	 */
	private PointerTargetNodeList findSymmetricRelationship(PointerTargetTreeNode node, PointerType type) {
		PointerTargetNodeList list = new PointerTargetNodeList();
		buildSymmetricRelationshipList(list, node);
		list = list.reverse();
		// set the root's pointer type
		((PointerTargetNode)list.get(0)).setType(type);
		return list;
	}

	/** Build the relationship. */
	private void buildSymmetricRelationshipList(PointerTargetNodeList list, PointerTargetTreeNode node) {
		list.add(node.getPointerTarget(), node.getType());
		if (node.getParent() != null) {
			buildSymmetricRelationshipList(list, node.getParent());
		}
	}

    /*
    public RelationshipList findRelationships(Synset source, Synset target) {
        Map sourceNodes = new HashMap();
        Map targetNodes = new HashMap();
        Map matches = new HashMap();

        Map sourceResults = expand(source, sourceNodes, null, false);
        Map targetResults = expand(target, targetNodes, null, true);
        findMatches(sourceResults, targetResults, matches);

        for (int i = 0; i < 10; i++) {
            sourceResults = expand(sourceResults, sourceNodes, false);
            targetResults = expand(targetResults, targetNodes, true);
            findMatches(sourceResults, targetResults, matches);
        }

        RelationshipList rl = new RelationshipList();

        for (Iterator itr = matches.entrySet().iterator(); itr.hasNext();) {
            Map.Entry entry = (Map.Entry) itr.next();
            Node sourceNode = (Node) entry.getKey();
            Node targetNode = (Node) entry.getValue();
            PointerTarget[] relationship = new PointerTarget[sourceNode.depth + targetNode.depth];
            while (sourceNode != null) {
                relationship[sourceNode.depth] = sourceNode.ptr.getSource();
                sourceNode = sourceNode.prev;
            }
            int targetDepth = targetNode.depth;
            while (targetNode != null) {
                relationship[sourceNode.depth + (targetDepth - targetNode.depth) + 1] = targetNode.ptr.getTarget();
                targetNode = targetNode.prev;
            }
            rl.add(Relationship(null, new PointerTargetNodeList(relationship), source, target));
        }

        return rl;
    }

    private Map expand(Synset s, Map nodes, Node parent, boolean reflexiveOnly) {
        Pointer[] ptrs = s.getPointers();
        Map results = new HashMap();
        for (int i = 0; i < ptrs.length; i++) {
            if (!reflexiveOnly || ptrs[i].getType().getSymmetricType() != null) {
                Long key =  new Long(ptrs[i].getTargetOffset());
                if (!nodes.containsKey(key)) {
                    results.put(key, new Node(ptrs[i], parent));
                }
            }
        }
        parent.expanded = true;
        nodes.putAll(results);
        return results;
    }

    private Map expand(Map expandNodes, Map allNodes, boolean reflexiveOnly) {
        Map results = new HashMap();
        for (Iterator itr = expandNodes.values().iterator(); itr.hasNext();) {
            Node parent = (Node) itr.next();
            results.putAll(expand(parent.ptr.getTarget(), allNodes, parent, reflexiveOnly));
        }
        return results;
    }

    private void findMatches(Map sourceResults, Map targetResults, Map matches) {
        Set sourceSet = sourceResults.keySet();
        Set targetSet = targetResults.keySet();
        for (Iterator itr = sourceSet.iterator(); itr.hasNext();) {
            Long offset = (Long) itr.next();
            if (targetSet.contains(offset)) {
                Node source = (Node) sourceResults.get(offset);
                Node target = (Node) targetResults.get(offset);
                matches.put(source, target);
            }
        }
    }

    private static class Node {
        Pointer ptr;
        Node prev;
        int depth;
        boolean expanded = false;

        public Node(Pointer ptr, Node prev) {
            this.ptr = ptr;
            this.prev = prev;
            this.depth = prev.depth + 1;
        }
    }
    */
}