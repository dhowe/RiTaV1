package rita.wordnet.jwnl.util.factory;


import java.util.HashMap;
import java.util.Map;

import rita.wordnet.jwnl.JWNLException;

/** Represents an installable element in a properties file */
public class Element {
	private String _className;
	private Map _paramMap = new HashMap();

	public Element(String className) {
		_className = className;
	}

	public Element(String className, Param[] params) {
		this(className);
		for (int i = 0; i < params.length; i++) {
			addParam(params[i]);
		}
	}

	public void addParam(Param param) {
		_paramMap.put(param.getName(), param);
	}

    /** If the class is installable, this method will install it using the parameters */
	public void install() throws JWNLException {
		try {
			Installable installable = (Installable) Class.forName(_className).newInstance();
			installable.install(_paramMap);
		} catch (Exception ex) {
			throw new JWNLException("UTILS_EXCEPTION_005", _className, ex);
		}
	}
}
