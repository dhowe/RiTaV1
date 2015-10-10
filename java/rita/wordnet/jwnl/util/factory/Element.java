package rita.wordnet.jwnl.util.factory;


import java.lang.reflect.Method;
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

	 public void install() throws JWNLException {
	   
    try {
      Class installClass = Class.forName(_className);
      Method method = installClass.getMethod("installStatic", Map.class);
      method.invoke(null, _paramMap);
      
      //Installable installable = (Installable) .newInstance();
      //installable.install(_paramMap);
    } catch (Exception ex) {
      //ex.printStackTrace();
      throw new JWNLException("UTILS_EXCEPTION_005", _className, ex);
    }
  }
	 
    /** If the class is installable, this method will install it using the parameters */
	public void installOrig() throws JWNLException {
		try {
			Installable installable = (Installable) Class.forName(_className).newInstance();
			installable.install(_paramMap);
		} catch (Exception ex) {
			throw new JWNLException("UTILS_EXCEPTION_005", _className, ex);
		}
	}
}
