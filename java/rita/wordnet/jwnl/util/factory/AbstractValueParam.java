package rita.wordnet.jwnl.util.factory;


import java.util.HashMap;
import java.util.Map;

import rita.wordnet.jwnl.JWNLException;

public abstract class AbstractValueParam implements Param {
	private Map _paramMap = new HashMap();

	protected AbstractValueParam() {
	}

	protected AbstractValueParam(Param[] params) {
		for (int i = 0; i < params.length; i++) {
			addParam(params[i]);
		}
	}

	public void addParam(Param param) {
		_paramMap.put(param.getName(), param);
	}

	/**
	 * If the value of this parameter is a class name, and that class is creatable, this method will create
	 * an instance of it using this Param's parameters.
	 */
	public Object create() throws JWNLException {
		try {
			Class clazz = Class.forName(getValue());
			Createable creatable = (Createable) clazz.newInstance();
			return creatable.create(_paramMap);
		} catch (Exception ex) {
			throw new JWNLException("JWNL_EXCEPTION_004", getValue(), ex);
		}
	}
}
