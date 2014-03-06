/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata;


import java.io.IOException;
import java.util.Collections;
import java.util.List;

import rita.wordnet.jwnl.JWNL;

/**
 * Represents an entry in an exception file. Contains all of the exceptions
 * for the given lemma.
 */
public final class Exc implements DictionaryElement {
	static final long serialVersionUID = -5792651340274489357L;

	private POS _pos;
	/** The excepted word */
	private String _lemma;
	/** All the exceptions for <code>lemma</code>. */
	private List _exceptions;

	public Exc(POS pos, String lemma, List exceptions) {
		_pos = pos;
		_lemma = lemma;
		_exceptions = Collections.unmodifiableList(exceptions);
	}

	public DictionaryElementType getType() {
		return DictionaryElementType.EXCEPTION;
	}

	public POS getPOS() {
		return _pos;
	}

	/** @return String the excepted word. */
	public String getLemma() {
		return _lemma;
	}

	/** Get the exception at index <code>index</code>. */
	public String getException(int index) {
		return (String)getExceptions().get(index);
	}

	public int getExceptionsSize() {
		return getExceptions().size();
	}

	/** Get the collection of Exc objects in array form. */
	public String[] getExceptionArray() {
		return (String[])getExceptions().toArray(new String[_exceptions.size()]);
	}

	/** Get the List of exceptions. */
	public List getExceptions() {
		return _exceptions;
	}

	public Object getKey() {
		return getLemma();
	}

	public boolean equals(Object obj) {
		return (obj instanceof Exc) &&
			getLemma().equals(((Exc)obj).getLemma()) &&
			getExceptions().equals(((Exc)obj).getExceptions());
	}

	private transient String _cachedToString = null;

	public String toString() {
		if (_cachedToString == null) {
			_cachedToString =
				JWNL.resolveMessage("DATA_TOSTRING_001", new Object[] { getLemma(), getExceptionsAsString() });
		}
		return _cachedToString;
	}

	public int hashCode() {
		int hash = getLemma().hashCode();
		for (int i = 0; i < getExceptionsSize(); i++) {
			hash ^= getException(i).hashCode();
		}
		return hash;
	}

	private String _exceptionString = null;

	private String getExceptionsAsString() {
		if (_exceptionString == null) {
			String str = "";
			for (int i = 0;  i < getExceptionsSize(); i++) {
				str += getException(i);
				if (i != getExceptionsSize() - 1) {
					str += ", ";
				}
			}
			_exceptionString = str;
		}
		return _exceptionString;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// set POS to reference the static instance defined in the current runtime environment
		_pos = POS.getPOSForKey(_pos.getKey());
	}
}






