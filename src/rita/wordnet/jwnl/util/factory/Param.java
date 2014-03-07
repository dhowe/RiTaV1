/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.util.factory;

import rita.wordnet.jwnl.JWNLException;

/** Represents a parameter in a properties file. Paremeters can be nested. */
public interface Param {
	String getName();
	String getValue();
	void addParam(Param param);
	Object create() throws JWNLException;
}