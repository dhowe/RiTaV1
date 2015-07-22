/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.util.Iterator;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.wndata.list.PointerTargetNode;
import rita.wordnet.jwnl.wndata.list.PointerTargetNodeList;
import rita.wordnet.jwnl.wndata.list.PointerTargetTree;
import rita.wordnet.jwnl.wndata.list.PointerTargetTreeNode;
import rita.wordnet.jwnl.wndata.list.PointerTargetTreeNodeList;

/**
 * A singleton containing methods for performing various pointer operations.
 * 
 * A pointer from one synset/word to another connotes a relationship between those words. 
 * The type of the relationship is specified by the type
 * of pointer. 
 */
public final class PointerUtils {

	public static final int INFINITY = 1000;
	private static final PointerUtils INSTANCE = new PointerUtils();
	
	private boolean overflowError = false; // hack for JWNL bug 

	public static PointerUtils getInstance() {
		return INSTANCE;
	}

	private PointerUtils() {}

	/** Get the immediate parents of <code>synset</code> */
	public PointerTargetNodeList getDirectHypernyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.HYPERNYM);
	}

	/** Get all of the ancestors of <code>synset</code> */
	public PointerTargetTree getHypernymTree(Synset synset) throws JWNLException {
		return getHypernymTree(synset, INFINITY);
	}

	/** Get all of the ancestors of <code>synset</code> to depth <code>depth</code> */
	public PointerTargetTree getHypernymTree(Synset synset, int depth) throws JWNLException {
	  PointerTargetTreeNodeList pttl = makePointerTargetTreeList(synset, PointerType.HYPERNYM, depth);
		return new PointerTargetTree(synset, pttl);
	}

	/** Get the immediate children of <code>synset</code> */
	public PointerTargetNodeList getDirectHyponyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.HYPONYM);
	}

	/** Get all of the children of <code>synset</code> */
	public PointerTargetTree getHyponymTree(Synset synset) throws JWNLException {
		return getHyponymTree(synset, INFINITY);
	}

	/** Get all of the children of <code>synset</code> to depth <code>depth</code> */
	public PointerTargetTree getHyponymTree(Synset synset, int depth) throws JWNLException {
		return new PointerTargetTree(synset, makePointerTargetTreeList(synset, PointerType.HYPONYM, depth));
	}

	//
	// other general operations
	//

	/** Get <code>synset</code>'s siblings (the hyponyms of its hypernyms) */
	public PointerTargetNodeList getCoordinateTerms(Synset synset) throws JWNLException {
		PointerTargetNodeList list = new PointerTargetNodeList();
		for (Iterator itr = getDirectHypernyms(synset).iterator(); itr.hasNext();) {
			list.addAll(getPointerTargets(((PointerTargetNode) itr.next()).getSynset(), PointerType.HYPONYM));
		}
		return list;
	}

	/** Get the words that mean the opposite of <code>synset</code> */
	public PointerTargetNodeList getAntonyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.ANTONYM);
	}

	/** Get the words that mean the opposite of <code>synset</code> and the immediate synonyms of those words */
	public PointerTargetTree getExtendedAntonyms(Synset synset) throws JWNLException {
		return getExtendedAntonyms(synset, 1);
	}

	/** Find all antonyms of <code>synset</code>, and all synonyms of those antonyms to depth <code>depth</code>. */
	public PointerTargetTree getExtendedAntonyms(Synset synset, int depth) throws JWNLException {
		PointerTargetTreeNodeList list = new PointerTargetTreeNodeList();
		if (synset.getPOS() == POS.ADJECTIVE) {
			PointerTargetNodeList antonyms = getAntonyms(synset);
			list = makePointerTargetTreeList(antonyms, PointerType.SIMILAR_TO, PointerType.ANTONYM, depth, false);
		}
		return new PointerTargetTree(new PointerTargetTreeNode(synset, list, null));
	}

	/** Get the immediate antonyms of all words that mean the same as <code>synset</code>. */
	public PointerTargetTree getIndirectAntonyms(Synset synset) throws JWNLException {
		return getIndirectAntonyms(synset, 1);
	}

	/** Get the antonyms of all words that mean the same as <code>synset</code> to depth <code>depth</code>.*/
	public PointerTargetTree getIndirectAntonyms(Synset synset, int depth) throws JWNLException {
		PointerTargetTreeNodeList list = new PointerTargetTreeNodeList();
		if (synset.getPOS() == POS.ADJECTIVE) {
			PointerTargetNodeList synonyms = getSynonyms(synset);
			list = makePointerTargetTreeList(synonyms, PointerType.ANTONYM, PointerType.ANTONYM, depth, false);
		}
		return new PointerTargetTree(new PointerTargetTreeNode(synset, list, null));
	}

	/** Get the attributes of <code>synset</code> */
	public PointerTargetNodeList getAttributes(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.ATTRIBUTE);
	}

	/** Find what words are related to <code>synset</code> */
	public PointerTargetNodeList getAlsoSees(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.SEE_ALSO);
	}

	/** Find all See Also relations to depth <code>depth</code>.*/
	public PointerTargetTree getAlsoSeeTree(Synset synset, int depth) throws JWNLException {
		return new PointerTargetTree(synset, makePointerTargetTreeList(synset, PointerType.SEE_ALSO, depth));
	}

	//
	// noun operations
	//

	/** Get meronyms of <code>synset</code>. */
	public PointerTargetNodeList getMeronyms(Synset synset) throws JWNLException {
		PointerTargetNodeList list = new PointerTargetNodeList();
		list.addAll(getPartMeronyms(synset));
		list.addAll(getMemberMeronyms(synset));
		list.addAll(getSubstanceMeronyms(synset));
		return list;
	}

	/** Get part meronyms of <code>synset</code> */
	public PointerTargetNodeList getPartMeronyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.PART_MERONYM);
	}

	/** Get member meronyms of <code>synset</code> */
	public PointerTargetNodeList getMemberMeronyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.MEMBER_MERONYM);
	}

	/** Get substance meronyms of <code>synset</code> */
	public PointerTargetNodeList getSubstanceMeronyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.SUBSTANCE_MERONYM);
	}

	/** Get meronyms of <code>synset</code> and of all its ancestors */
	public PointerTargetTree getInteritedMeronyms(Synset synset) throws JWNLException {
		return getInheritedMeronyms(synset, INFINITY, INFINITY);
	}

	/**
	 * Get meronyms of each synset, to depth <code>pointerDepth</code> starting at
	 * <code>synset</code> and going for all of <code>synset</code>'s ancestors to depth
	 * <code>ancestorDepth</code>.
	 */
	public PointerTargetTree getInheritedMeronyms(Synset synset, int pointerDepth, int ancestorDepth)
	    throws JWNLException {
		PointerType[] types = new PointerType[3];
		types[0] = PointerType.PART_MERONYM;
		types[1] = PointerType.MEMBER_MERONYM;
		types[2] = PointerType.SUBSTANCE_MERONYM;
		return makeInheritedTree(synset, types, null, pointerDepth, ancestorDepth, false);
	}

	/** Get part meronyms of <code>synset</code> and of all its ancestors */
	public PointerTargetTree getInheritedPartMeronyms(Synset synset) throws JWNLException {
		return getInheritedPartMeronyms(synset, INFINITY, INFINITY);
	}

	/**
	 * Get part meronyms of each synset, to depth <code>pointerDepth</code>, starting at
	 * <code>synset</code> and going for all of <code>synset</code>'s ancestors to depth
	 * <code>ancestorDepth</code>.
	 */
	public PointerTargetTree getInheritedPartMeronyms(Synset synset, int pointerDepth, int ancestorDepth)
	    throws JWNLException {
		return makeInheritedTree(synset, PointerType.PART_MERONYM, null, pointerDepth, ancestorDepth);
	}

	/** Get member meronyms of synset and of its ancestors */
	public PointerTargetTree getInheritedMemberMeronyms(Synset synset) throws JWNLException {
		return getInheritedMemberMeronyms(synset, INFINITY, INFINITY);
	}

	/**
	 * Get member meronyms of each synset, to depth <code>pointerDepth</code>, starting at
	 * <code>synset</code> and going for all of <code>synset</code>'s ancestors to depth
	 * <code>ancestorDepth</code>.
	 */
	public PointerTargetTree getInheritedMemberMeronyms(Synset synset, int pointerDepth, int ancestorDepth)
	    throws JWNLException {
		return makeInheritedTree(synset, PointerType.MEMBER_MERONYM, null, pointerDepth, ancestorDepth);
	}

	/** Get substance meronyms of <code>synset</code> and of its ancestors */
	public PointerTargetTree getInheritedSubstanceMeronyms(Synset synset) throws JWNLException {
		return getInheritedSubstanceMeronyms(synset, INFINITY, INFINITY);
	}

	/**
	 * Get substance meronyms of each synset, to depth <code>pointerDepth</code>, starting at
	 * <code>synset</code> and going for all of <code>synset</code>'s ancestors to depth
	 * <code>ancestorDepth</code>.
	 */
	public PointerTargetTree getInheritedSubstanceMeronyms(Synset synset, int pointerDepth, int ancestorDepth)
	    throws JWNLException {
		return makeInheritedTree(synset, PointerType.SUBSTANCE_MERONYM, null, pointerDepth, ancestorDepth);
	}

	/** Get holonyms of <code>synset</code> */
	public PointerTargetNodeList getHolonyms(Synset synset) throws JWNLException {
		PointerTargetNodeList list = new PointerTargetNodeList();
		list.addAll(getPartHolonyms(synset));
		list.addAll(getMemberHolonyms(synset));
		list.addAll(getSubstanceHolonyms(synset));
		return list;
	}

	/** Get part holonyms of <code>synset</code> */
	public PointerTargetNodeList getPartHolonyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.PART_HOLONYM);
	}

	/** Get member holonyms of <code>synset</code> */
	public PointerTargetNodeList getMemberHolonyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.MEMBER_HOLONYM);
	}

	/** Get substance holonyms of <code>synset</code> */
	public PointerTargetNodeList getSubstanceHolonyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.SUBSTANCE_HOLONYM);
	}

	/** Get holonyms of <code>synset</code> and of all its ancestors */
	public PointerTargetTree getInheritedHolonyms(Synset synset) throws JWNLException {
		return getInheritedHolonyms(synset, INFINITY, INFINITY);
	}

	/**
	 * Get holonyms of each synset, to depth <code>pointerDepth</code>, starting at <code>synset</code>
	 * and going for all of <code>synset</code>'s ancestors to depth <code>ancestorDepth</code>.
	 */
	public PointerTargetTree getInheritedHolonyms(Synset synset, int pointerDepth, int ancestorDepth)
	    throws JWNLException {
		PointerType[] types = new PointerType[3];
		types[0] = PointerType.PART_HOLONYM;
		types[1] = PointerType.MEMBER_HOLONYM;
		types[2] = PointerType.SUBSTANCE_HOLONYM;
		return makeInheritedTree(synset, types, null, pointerDepth, ancestorDepth, false);
	}

	/** Get part holonyms of <code>synset</code> and of all its ancestors */
	public PointerTargetTree getInheritedPartHolonyms(Synset synset) throws JWNLException {
		return getInheritedPartHolonyms(synset, INFINITY, INFINITY);
	}

	/**
	 * Get part holonyms of each synset, to depth <code>pointerDepth</code>, starting at <code>synset</code>
	 * and going for all of <code>synset</code>'s ancestors to depth <code>ancestorDepth</code>.
	 */
	public PointerTargetTree getInheritedPartHolonyms(Synset synset, int pointerDepth, int ancestorDepth)
	    throws JWNLException {
		return makeInheritedTree(synset, PointerType.PART_HOLONYM, null, pointerDepth, ancestorDepth);
	}

	/** Get member holonyms of <code>synset</code> and of all its ancestors */
	public PointerTargetTree getInheritedMemberHolonyms(Synset synset) throws JWNLException {
		return getInheritedMemberHolonyms(synset, INFINITY, INFINITY);
	}

	/**
	 * Get member holonyms of each synset, to depth <code>pointerDepth</code>, starting at <code>synset</code>
	 * and going for all of <code>synset</code>'s ancestors to depth <code>ancestorDepth</code>.
	 */
	public PointerTargetTree getInheritedMemberHolonyms(Synset synset, int pointerDepth, int ancestorDepth)
	    throws JWNLException {
		return makeInheritedTree(synset, PointerType.MEMBER_HOLONYM, null, pointerDepth, ancestorDepth);
	}

	/** Get substance holonyms of <code>synset</code> and of all its ancestors */
	public PointerTargetTree getInheritedSubstanceHolonyms(Synset synset) throws JWNLException {
		return getInheritedSubstanceHolonyms(synset, INFINITY, INFINITY);
	}

	/**
	 * Get substance holonyms of each synset, to depth <code>pointerDepth</code>, starting at <code>synset</code>
	 * and going for all of <code>synset</code>'s ancestors to depth <code>ancestorDepth</code>.
	 */
	public PointerTargetTree getInheritedSubstanceHolonyms(Synset synset, int pointerDepth, int ancestorDepth)
	    throws JWNLException {
		return makeInheritedTree(synset, PointerType.SUBSTANCE_HOLONYM, null, pointerDepth, ancestorDepth);
	}

	//
	// Verb Operations
	//

	/** Find direct entailments of <code>synset</code> */
	public PointerTargetNodeList getEntailments(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.ENTAILMENT);
	}

	/** Find all entailments for <code>synset</code> */
	public PointerTargetTree getEntailmentTree(Synset synset) throws JWNLException {
		return getEntailmentTree(synset, INFINITY);
	}

	/** Find all entailments for <code>synset</code> to depth <code>depth</code> */
	public PointerTargetTree getEntailmentTree(Synset synset, int depth) throws JWNLException {
		return new PointerTargetTree(synset, makePointerTargetTreeList(synset, PointerType.ENTAILMENT, depth));
	}

	/** Find direct entailed bys of <code>synset</code> */
	public PointerTargetNodeList getEntailedBy(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.ENTAILED_BY);
	}

	/** Find all entailed bys of <code>synset</code>. */
	public PointerTargetTree getEntailedByTree(Synset synset) throws JWNLException {
		return getEntailedByTree(synset, INFINITY);
	}

	/** Find all entailed bys of <code>synset</code> to depth <code>depth</code>. */
	public PointerTargetTree getEntailedByTree(Synset synset, int depth) throws JWNLException {
		return new PointerTargetTree(synset, makePointerTargetTreeList(synset, PointerType.ENTAILED_BY, depth));
	}

	/** Find direct cause links of <code>synset</code> */
	public PointerTargetNodeList getCauses(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.CAUSE);
	}

	/** Find all cause links for <code>synset</code>.*/
	public PointerTargetTree getCauseTree(Synset synset) throws JWNLException {
		return getCauseTree(synset, INFINITY);
	}

	/** Find all cause links for <code>synset</code> to depth <code>depth</code>.*/
	public PointerTargetTree getCauseTree(Synset synset, int depth) throws JWNLException {
		return new PointerTargetTree(synset, makePointerTargetTreeList(synset, PointerType.CAUSE, depth));
	}

	/** Get the group that this verb belongs to. */
	public PointerTargetNodeList getVerbGroup(Synset synset) throws JWNLException {
		// We need to go through all this hastle because
		// 1. a verb does not always have links to all the verbs in its group
		// 2. two verbs in the same group sometimes have reciprocal links, and we want
		//    to make sure that each verb synset appears in the final list only once

		PointerTargetNodeList nodes = new PointerTargetNodeList();
		nodes.add(new PointerTargetNode(synset, PointerType.VERB_GROUP));
		int maxIndex = 0;
		int index = -1;
		do {
			index++;
			PointerTargetNode node = (PointerTargetNode) nodes.get(index);
			for (Iterator itr = getPointerTargets(node.getSynset(), PointerType.VERB_GROUP).iterator(); itr.hasNext();) {
				PointerTargetNode testNode = (PointerTargetNode) itr.next();
				if (!nodes.contains(testNode)) {
					nodes.add(testNode);
					maxIndex++;
				}
			}
		} while (index < maxIndex);

		return nodes;
	}

	//
	// Adjective Operations
	//

	/** Find participle of links of <code>synset</code> */
	public PointerTargetNodeList getParticipleOf(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.PARTICIPLE_OF);
	}

	/** Find derrived links of <code>synset</code> */
	public PointerTargetNodeList getDerived(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.DERIVED);
	}

	/**
	 * Get the synonyms for <code>synset</code>. This is meant for adjectives. Synonyms to
	 * nouns and verbs are just their hypernyms.
	 */
	public PointerTargetNodeList getSynonyms(Synset synset) throws JWNLException {
		return getPointerTargets(synset, PointerType.SIMILAR_TO);
	}

	/** Get all the synonyms of <code>synset</code> to depth <code>depth</code>. */
	public PointerTargetTree getSynonymTree(Synset synset, int depth) throws JWNLException {
		return new PointerTargetTree(synset, makePointerTargetTreeList(synset, PointerType.SIMILAR_TO, null, depth, false));
	}

	// General relation functions

	/** Get all the pointer targets of <var>synset</var> of type <var>type</var>. */
	private PointerTargetNodeList getPointerTargets(Synset synset, PointerType type) throws JWNLException {
	  PointerTarget[] ptn = synset.getTargets(type);
		return new PointerTargetNodeList(ptn);
	}

	/**
	 * Make a nested list of pointer targets to the default depth, starting at <code>synset</code>. Each
	 * level of the list is related to the previous level by a pointer of type <var>searchType</var>.
	 */
	public PointerTargetTreeNodeList makePointerTargetTreeList(Synset set, PointerType searchType)
	    throws JWNLException {
		return makePointerTargetTreeList(set, searchType, INFINITY);
	}

	/**
	 * Make a nested list of pointer targets to depth <var>depth</var>, starting at <code>synset</code>. Each
	 * level of the list is related to the previous level by a pointer of type <var>searchType</var>.
	 */
	public PointerTargetTreeNodeList makePointerTargetTreeList(Synset set, PointerType searchType, int depth)
	    throws JWNLException {
		return makePointerTargetTreeList(set, searchType, null, depth, true);
	}

	/**
	 * Make a nested list of pointer targets to depth <var>depth</var>, starting at <code>synset</code>. Each
	 * level of the list is related to the previous level by a pointer of type <var>searchType</var>.
	 * @param labelType the type used to label each pointer target in the tree
	 * @param allowRedundancies if true, duplicate items will be included in the tree
	 */
	public PointerTargetTreeNodeList makePointerTargetTreeList(Synset set, PointerType searchType,
	                                                           PointerType labelType, int depth,
	                                                           boolean allowRedundancies) throws JWNLException {
		PointerType[] searchTypes = new PointerType[1];
		searchTypes[0] = searchType;
		return makePointerTargetTreeList(set, searchTypes, labelType, depth, allowRedundancies);
	}

	/**
	 * Make a nested list of pointer targets to the default depth, starting at <code>synset</code>. Each
	 * level of the list is related to the previous level by one of the pointer types specified by
	 * <var>searchTypes</var>.
	 */
	public PointerTargetTreeNodeList makePointerTargetTreeList(Synset set, PointerType[] searchTypes)
	    throws JWNLException {
		return makePointerTargetTreeList(set, searchTypes, INFINITY);
	}

	/**
	 * Make a nested list of pointer targets to depth <var>depth</var>, starting at <code>synset</code>. Each
	 * level of the list is related to the previous level by one of the pointer types specified by
	 * <var>searchTypes</var>.
	 */
	public PointerTargetTreeNodeList makePointerTargetTreeList(Synset set, PointerType[] searchTypes, int depth)
	    throws JWNLException {
		return makePointerTargetTreeList(set, searchTypes, null, depth, true);
	}

	/**
	 * Make a nested list of pointer targets to depth <var>depth</var>, starting at <code>synset</code>. Each
	 * level of the list is related to the previous level by one of the pointer types specified by
	 * <var>searchTypes</var>.
	 * @param labelType the type used to label each pointer target in the tree
	 * @param allowRedundancies if true, duplicate items will be included in the tree
	 */
	public PointerTargetTreeNodeList makePointerTargetTreeList(Synset synset, PointerType[] searchTypes,
	                                                           PointerType labelType, int depth,
	                                                           boolean allowRedundancies) throws JWNLException {
		return makePointerTargetTreeList(synset, searchTypes, labelType, depth, allowRedundancies, null);
	}

	/**
	 * Make a nested list of pointer targets to depth <var>depth</var>, starting at each <code>synset</code> in
	 * <var>list</var>. Each level of the list is related to the previous level by a pointer of type
	 * <var>searchType</var>.
	 * @param labelType the type used to label each pointer target in the tree
	 * @param allowRedundancies if true, duplicate items will be included in the tree
	 */
	public PointerTargetTreeNodeList makePointerTargetTreeList(PointerTargetNodeList list, PointerType searchType,
	                                                           PointerType labelType, int depth,
	                                                           boolean allowRedundancies) throws JWNLException {
		PointerType[] searchTypes = new PointerType[1];
		searchTypes[0] = searchType;
		return makePointerTargetTreeList(list, searchTypes, labelType, depth, allowRedundancies);
	}

	/**
	 * Make a nested list of pointer targets to depth <var>depth</var>, starting at each <code>synset</code> in
	 * <var>list</var>. Each level of the list is related to the previous level by one of the pointer types specified
	 * by <var>searchTypes</var>.
	 * @param labelType the type used to label each pointer target in the tree
	 * @param allowRedundancies if true, duplicate items will be included in the tree
	 */
	public PointerTargetTreeNodeList makePointerTargetTreeList(PointerTargetNodeList list, PointerType[] searchTypes,
	                                                           PointerType labelType, int depth,
	                                                           boolean allowRedundancies) throws JWNLException {
		PointerTargetTreeNodeList treeList = new PointerTargetTreeNodeList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			PointerTargetNode node = (PointerTargetNode) itr.next();
			treeList.add(node.getPointerTarget(),
			             makePointerTargetTreeList(node.getSynset(), searchTypes, labelType, depth, allowRedundancies),
			             labelType);
		}
		return treeList;
	}

	private PointerTargetTreeNodeList makePointerTargetTreeList(Synset synset, PointerType[] searchTypes,
	    PointerType labelType, int depth, boolean allowRedundancies, PointerTargetTreeNode parent) 
	  throws JWNLException 
	{
    depth--;
    PointerTargetTreeNodeList list = new PointerTargetTreeNodeList();
    for (int i = 0; i < searchTypes.length; i++)
    {
      PointerType type = searchTypes[i];
      PointerTargetNodeList targets = new PointerTargetNodeList(synset.getTargets(type));

      if (targets.size() > 0)
      {
        for (Iterator itr = targets.iterator(); itr.hasNext();)
        {
          PointerTargetTreeNode node = new PointerTargetTreeNode
            (((PointerTargetNode) itr.next()).getPointerTarget(), labelType == null ? type : labelType, parent);
          
          if (allowRedundancies || !list.contains(node))
          {
            if (depth != 0)
            {
              if (overflowError && parent != null && (synset == parent.getSynset()))
              {
                System.out.println("[WARN] Infinite loop found in db for synset: "+ synset + " aborting...");
                overflowError = false; // hack: caught an overflow, reset boolean
                return list;
              }
              PointerTargetTreeNodeList pttnl = null;
              /*try
              {*/
                pttnl = makePointerTargetTreeList(node.getSynset(), searchTypes, labelType, depth, allowRedundancies, node);
     /*         }
              catch (Throwable e)
              {
                throw new WordnetError(e);
              }*/
              node.setChildTreeList(pttnl);
            }
            list.add(node);
          }
        }
      }
    }
		return list;
	}

	/**
	 * Create a hypernym tree starting at <var>synset</var>, and add to each node a nested list pointer targets of type
	 * <var>searchType</var>, starting at the node's pointer target. This method uses the default depths.
	 */
	public PointerTargetTree makeInheritedTree(Synset synset, PointerType searchType) throws JWNLException {
		return makeInheritedTree(synset, searchType, null, INFINITY, INFINITY);
	}

	/**
	 * Create a hypernym tree starting at <var>synset</var>, and add to each node a nested list pointer targets of type
	 * <var>searchType</var>, starting at the node's pointer target.
	 * @param pointerDepth the depth to which to search for each pointer list
	 * @param ancestorDepth the depth to which to go to in the hypernym list
	 */
	public PointerTargetTree makeInheritedTree(Synset synset, PointerType searchType, PointerType labelType,
	                                           int pointerDepth, int ancestorDepth) throws JWNLException {
		return makeInheritedTree(synset, searchType, labelType, pointerDepth, ancestorDepth, true);
	}

	/**
	 * Create a hypernym tree starting at <var>synset</var>, and add to each node a nested list pointer targets of type
	 * <var>searchType</var>, starting at the node's pointer target.
	 * @param pointerDepth the depth to which to search for each pointer list
	 * @param ancestorDepth the depth to which to go to in the hypernym list
	 * @param allowRedundancies if true, duplicate items are allowed in the list
	 */
	public PointerTargetTree makeInheritedTree(Synset synset, PointerType searchType, PointerType labelType,
	                                           int pointerDepth, int ancestorDepth, boolean allowRedundancies)
	    throws JWNLException {
		PointerType[] searchTypes = new PointerType[1];
		searchTypes[0] = searchType;
		return makeInheritedTree(synset, searchTypes, labelType, pointerDepth, ancestorDepth, allowRedundancies);
	}

	/**
	 * Create a hypernym tree starting at <var>synset</var>, and add to each node a nested list pointer targets of
	 * the types specified in <var>searchTypes</var>, starting at the node's pointer target. This method uses the
	 * default depths.
	 */
	public PointerTargetTree makeInheritedTree(Synset synset, PointerType[] searchTypes) throws JWNLException {
		return makeInheritedTree(synset, searchTypes, null, INFINITY, INFINITY);
	}

	/**
	 * Create a hypernym tree starting at <var>synset</var>, and add to each node a nested list pointer targets of
	 * the types specified in <var>searchTypes</var>, starting at the node's pointer target.
	 * @param pointerDepth the depth to which to search for each pointer list
	 * @param ancestorDepth the depth to which to go to in the hypernym list
	 */
	public PointerTargetTree makeInheritedTree(Synset synset, PointerType[] searchTypes, PointerType labelType,
	                                           int pointerDepth, int ancestorDepth) throws JWNLException {
		return makeInheritedTree(synset, searchTypes, labelType, pointerDepth, ancestorDepth, true);
	}

	/**
	 * Create a hypernym tree starting at <var>synset</var>, and add to each node a nested list pointer targets of
	 * the types specified in <var>searchTypes</var>, starting at the node's pointer target.
	 * @param pointerDepth the depth to which to search for each pointer list
	 * @param ancestorDepth the depth to which to go to in the hypernym list
	 * @param allowRedundancies if true, duplicate items are allowed in the list
	 */
	public PointerTargetTree makeInheritedTree(Synset synset, PointerType[] searchTypes, PointerType labelType,
	                                           int pointerDepth, int ancestorDepth, boolean allowRedundancies)
	    throws JWNLException {
		PointerTargetTree hypernyms = getHypernymTree(synset, INFINITY);
		return makeInheritedTree(hypernyms, searchTypes, labelType, pointerDepth, ancestorDepth, allowRedundancies);
	}

	/**
	 * Turn an existing tree into an inheritance tree.
	 * @param tree the tree to convert
	 * @param searchTypes the pointer types to include in the pointer lists
	 * @param labelType the <code>PointerType</code> with which to label each pointer
	 * @param pointerDepth the depth to which to search for each pointer list
	 * @param ancestorDepth the depth to which to go to in <code>tree</code>
	 * @param allowRedundancies if true, duplicate items are allowed in the list
	 */
	public PointerTargetTree makeInheritedTree(PointerTargetTree tree, PointerType[] searchTypes,
	                                           PointerType labelType, int pointerDepth, int ancestorDepth,
	                                           boolean allowRedundancies) throws JWNLException {
		PointerTargetTreeNode root = tree.getRootNode();
		root.setPointerTreeList(makePointerTargetTreeList(root.getSynset(), searchTypes, labelType, pointerDepth, allowRedundancies));
		root.setChildTreeList(makeInheritedTreeList(root.getChildTreeList(), searchTypes, labelType, pointerDepth,
		                                            ancestorDepth, allowRedundancies));
		return new PointerTargetTree(root);
	}

	/**
	 * Turn an existing tree list into an inheritance tree list.
	 * @param list the tree list to convert
	 * @param searchTypes the pointer types to include in the pointer lists
	 * @param labelType the <code>PointerType</code> with which to label each pointer
	 * @param pointerDepth the depth to which to search for each pointer list
	 * @param ancestorDepth the depth to which to go to in <code>tree</code>
	 * @param allowRedundancies if true, duplicate items are allowed in the list
	 */
	public PointerTargetTreeNodeList makeInheritedTreeList(PointerTargetTreeNodeList list,
	                                                       PointerType[] searchTypes, PointerType labelType,
	                                                       int pointerDepth, int ancestorDepth,
	                                                       boolean allowRedundancies) throws JWNLException {
		ancestorDepth--;
		PointerTargetTreeNodeList inherited = new PointerTargetTreeNodeList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			PointerTargetTreeNode node = (PointerTargetTreeNode) itr.next();
			if (allowRedundancies || !inherited.contains(node)) {
				if (ancestorDepth == 0) {
					inherited.add(node.getPointerTarget(),
					              null,
					              makePointerTargetTreeList(node.getSynset(), searchTypes, labelType, pointerDepth, allowRedundancies),
					              PointerType.HYPERNYM);
				} else {
					inherited.add(node.getPointerTarget(),
					              makeInheritedTreeList(node.getChildTreeList(), searchTypes, labelType,
					                                    pointerDepth, ancestorDepth, allowRedundancies),
					              makePointerTargetTreeList(node.getSynset(), searchTypes, labelType, pointerDepth, allowRedundancies),
					              PointerType.HYPERNYM);
				}
			}
		}
		return inherited;
	}

  public void setOverflowError(boolean overflowError) {
    this.overflowError = overflowError;
  }
}