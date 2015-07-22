package rita.wordnet.jwnl.util.factory;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rita.wordnet.jwnl.JWNLException;

public class ParamList implements Param {
	private String _name;
	private List _params = new ArrayList();

	public ParamList(String name) {
		_name = name;
	}

	public ParamList(String name, Param[] params) {
		_name = name;
		for (int i = 0; i < params.length; i++) {
			addParam(params[i]);
		}
	}

	public String getName() {
		return _name;
	}

	public String getValue() {
		throw new UnsupportedOperationException();
	}

	public void addParam(Param param) {
		_params.add(param);
	}

	public List getParams() {
		return _params;
	}

	public Object create() throws JWNLException {
		List params = getParams();
		List results = new ArrayList(params.size());
		for (Iterator itr = params.iterator(); itr.hasNext();) {
			results.add(((Param)itr.next()).create());
		}
		return results;
	}
}