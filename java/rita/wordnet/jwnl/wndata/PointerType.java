/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.util.Resolvable;

/**
 * Instances of this class enumerate the possible WordNet pointer types,
 * and are used to label <code>PointerType</code>s. Each <code>PointerType</code>
 * carries additional information: a human-readable label, an optional reflexive
 * type that labels links pointing the opposite direction, an encoding of
 * parts-of-speech that it applies to, and a short string that represents it in
 * the dictionary files.
 */
public final class PointerType implements Serializable 
{
	static final long serialVersionUID = 220886251671304256L;

  // Flags for tagging a pointer type with the POS types it apples to.
	private static final int N = 1;
	private static final int V = 2;
	private static final int ADJ = 4;
	private static final int ADV = 8;
	private static final int LEXICAL = 16;

	// All categories
  
	public static final PointerType ANTONYM = new PointerType("ANTONYM", "ANTONYM_KEY", N | V | ADJ | ADV | LEXICAL);
  public static final PointerType CATEGORY = new PointerType("CATEGORY_DOMAIN", "CATEGORY_DOMAIN_KEY", N | V | ADJ | ADV | LEXICAL);
  public static final PointerType REGION = new PointerType("REGION_DOMAIN", "REGION_DOMAIN_KEY", N | V | ADJ | ADV | LEXICAL);
  public static final PointerType USAGE = new PointerType("USAGE_DOMAIN", "USAGE_DOMAIN_KEY", N | V | ADJ | ADV | LEXICAL);

	// Nouns and Verbs

	public static final PointerType HYPERNYM = new PointerType("HYPERNYM", "HYPERNYM_KEY", N | V);
	public static final PointerType HYPONYM = new PointerType("HYPONYM", "HYPONYM_KEY", N | V);
  public static final PointerType NOMINALIZATION = new PointerType("NOMINALIZATION", "NOMINALIZATION_KEY", N | V);

	// Nouns and Adjectives

	public static final PointerType ATTRIBUTE = new PointerType("ATTRIBUTE", "ATTRIBUTE_KEY", N | ADJ);
	public static final PointerType SEE_ALSO = new PointerType("ALSO_SEE", "ALSO_SEE_KEY", N | V | ADJ | LEXICAL);

    // Nouns

  public static final PointerType MEMBER_HOLONYM = new PointerType("MEMBER_HOLONYM", "MEMBER_HOLONYM_KEY", N);
  public static final PointerType SUBSTANCE_HOLONYM = new PointerType("SUBSTANCE_HOLONYM", "SUBSTANCE_HOLONYM_KEY", N);
  public static final PointerType PART_HOLONYM = new PointerType("PART_HOLONYM", "PART_HOLONYM_KEY", N);
  public static final PointerType MEMBER_MERONYM = new PointerType("MEMBER_MERONYM", "MEMBER_MERONYM_KEY", N);
  public static final PointerType SUBSTANCE_MERONYM = new PointerType("SUBSTANCE_MERONYM", "SUBSTANCE_MERONYM_KEY", N);
  public static final PointerType PART_MERONYM = new PointerType("PART_MERONYM", "PART_MERONYM_KEY", N);
  public static final PointerType CATEGORY_MEMBER = new PointerType("CATEGORY_MEMBER", "CATEGORY_MEMBER_KEY", N);
  public static final PointerType REGION_MEMBER = new PointerType("REGION_MEMBER", "REGION_MEMBER_KEY", N);
  public static final PointerType USAGE_MEMBER = new PointerType("USAGE_MEMBER", "USAGE_MEMBER_KEY", N);

	// Verbs

	public static final PointerType ENTAILMENT = new PointerType("ENTAILMENT", "ENTAILMENT_KEY", V);
	public static final PointerType ENTAILED_BY = new PointerType("ENTAILED_BY", "ENTAILED_BY_KEY", V);
	public static final PointerType CAUSE = new PointerType("CAUSE", "CAUSE_KEY", V);
	public static final PointerType VERB_GROUP = new PointerType("VERB_GROUP", "VERB_GROUP_KEY", V);

	// Adjectives
	public static final PointerType SIMILAR_TO = new PointerType("SIMILAR", "SIMILAR_KEY", ADJ);
	public static final PointerType PARTICIPLE_OF = new PointerType("PARTICIPLE_OF", "PARTICIPLE_OF_KEY", ADJ | LEXICAL);
	public static final PointerType PERTAINYM = new PointerType("PERTAINYM", "PERTAINYM_KEY", ADJ | LEXICAL);

	// Adverbs
	public static final PointerType DERIVED = new PointerType("DERIVED", "DERIVED_KEY", ADV);

	/** A list of all <code>PointerType</code>s. */
	private static final List ALL_TYPES = Collections.unmodifiableList(Arrays.asList(new PointerType[] {
        ANTONYM, HYPERNYM, HYPONYM, ATTRIBUTE, SEE_ALSO, ENTAILMENT, ENTAILED_BY, CAUSE, VERB_GROUP,
        MEMBER_MERONYM, SUBSTANCE_MERONYM, PART_MERONYM, MEMBER_HOLONYM, SUBSTANCE_HOLONYM, PART_HOLONYM,
        SIMILAR_TO, PARTICIPLE_OF, DERIVED, NOMINALIZATION, CATEGORY, REGION, USAGE, CATEGORY_MEMBER,
        REGION_MEMBER, USAGE_MEMBER
  }));

  private static final Map POS_TO_MASK_MAP = new HashMap();
  private static final Map KEY_TO_POINTER_TYPE_MAP = new HashMap();

  private static boolean _initialized = false;

  public static void initialize()
  {
    if (!_initialized)
    {
      POS_TO_MASK_MAP.put(POS.NOUN, new Integer(N));
      POS_TO_MASK_MAP.put(POS.VERB, new Integer(V));
      POS_TO_MASK_MAP.put(POS.ADJECTIVE, new Integer(ADJ));
      POS_TO_MASK_MAP.put(POS.ADVERB, new Integer(ADV));

      for (Iterator itr = ALL_TYPES.iterator(); itr.hasNext();)
      {
        PointerType pt = (PointerType) itr.next();
        KEY_TO_POINTER_TYPE_MAP.put(pt.getKey(), pt);
        
        //System.out.println(pt.getKey()+" -> "+JWNL.resolveMessage(pt.toString()));
      }
      _initialized = true;
    }
  }

  static
  {
    setSymmetric(ANTONYM, ANTONYM);
    setSymmetric(HYPERNYM, HYPONYM);
    setSymmetric(MEMBER_MERONYM, MEMBER_HOLONYM);
    setSymmetric(SUBSTANCE_MERONYM, SUBSTANCE_HOLONYM);
    setSymmetric(PART_MERONYM, PART_HOLONYM);
    setSymmetric(SIMILAR_TO, SIMILAR_TO);
    setSymmetric(ATTRIBUTE, ATTRIBUTE);
    setSymmetric(VERB_GROUP, VERB_GROUP);
    setSymmetric(ENTAILMENT, ENTAILED_BY);
    setSymmetric(CATEGORY, CATEGORY_MEMBER);
    setSymmetric(REGION, REGION_MEMBER);
    setSymmetric(USAGE, USAGE_MEMBER);
    setSymmetric(NOMINALIZATION, NOMINALIZATION);
  }

  /**
   * Returns true if <var>type</var> is a symmetric pointer type (it is its own
   * symmetric type).
   */
  public static boolean isSymmetric(PointerType type)
  {
    return type.symmetricTo(type);
  }

  /**
   * Return the <code>PointerType</code> whose key matches <var>key</var>.
   */
  public static PointerType getPointerTypeForKey(String key)
  {
    PointerType pt = (PointerType) KEY_TO_POINTER_TYPE_MAP.get(key);
    //if (pt == null) 
      //throw new RuntimeException("null pointer-type for: "+key+"\n"+KEY_TO_POINTER_TYPE_MAP);
    //System.out.println("KEY: "+key+" -> "+pt);
    return pt;
  }

  public static List getAllPointerTypes()
  {
    return ALL_TYPES;
  }

  public static List getAllPointerTypesForPOS(POS pos)
  {
    List types = new ArrayList();
    for (Iterator itr = ALL_TYPES.iterator(); itr.hasNext();)
    {
      PointerType pt = (PointerType) itr.next();
      if (pt.appliesTo(pos))
      {
        types.add(pt);
      }
    }
    return Collections.unmodifiableList(types);
  }

  /** Set <var>a</var> as <var>b</var>'s symmetric type, and vice versa. */
  private static void setSymmetric(PointerType a, PointerType b)
  {
    a._symmetricType = b;
    b._symmetricType = a;
  }

  private static int getPOSMask(POS pos)
  {
    return ((Integer) POS_TO_MASK_MAP.get(pos)).intValue();
  }

  private Resolvable _label;

  private Resolvable _key;

  private int _flags;

  /** The PointerType that is the revers of this PointerType */
  private PointerType _symmetricType;

  private PointerType(String label, String key, int flags) {
    _label = new Resolvable(label);
    _key = new Resolvable(key);
    _flags = flags;
  }

  private transient String _cachedToString = null;

  public String toString()
  {
    if (_cachedToString == null)
    {
      _cachedToString = /*JWNL.resolveMessage("DATA_TOSTRING_011",*/
        Arrays.asList(new Object[] { getLabel(), getKey(), getFlagsAsString() }).toString();
    }
    return _cachedToString;
  }

  public String getKey()
  {
    return _key.toString();
  }

  public String getLabel()
  {
    return _label.toString();
  }

  /** Whether or not this PointerType can be associated with <code>pos</code> */
  public boolean appliesTo(POS pos)
  {
    return (_flags & getPOSMask(pos)) != 0;
  }

  public boolean isSymmetric()
  {
    return symmetricTo(this);
  }

  /** Returns true if <var>type</var> is symmetric to this pointer type. */
  public boolean symmetricTo(PointerType type)
  {
    return getSymmetricType() != null && getSymmetricType().equals(type);
  }

  /** Returns the pointer type that is symmetric to this type. */
  public PointerType getSymmetricType()
  {
    return _symmetricType;
  }

  public int hashCode()
  {
    return getLabel().hashCode();
  }

  private String _flagStringCache = null;

  private String getFlagsAsString()
  {
    if (_flagStringCache == null)
    {
      String str = "";
      if ((_flags & N) != 0)
        str += JWNL.resolveMessage("NOUN") + ", ";
      if ((_flags & V) != 0)
        str += JWNL.resolveMessage("VERB") + ", ";
      if ((_flags & ADJ) != 0)
        str += JWNL.resolveMessage("ADJECTIVE") + ", ";
      if ((_flags & ADV) != 0)
        str += JWNL.resolveMessage("ADVERB") + ", ";
      if ((_flags & LEXICAL) != 0)
        str += JWNL.resolveMessage("LEXICAL") + ", ";
      _flagStringCache = str.substring(0, str.length() - 2);
    }
    return _flagStringCache;
  }
}