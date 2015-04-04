package rita.wordnet.jwnl.util.factory;

public class ValueParam extends AbstractValueParam {
	private String _value;

	public ValueParam(String value) {
		_value = value;
	}

	public ValueParam(String value, Param[] params) {
		super(params);
		_value = value;
	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public String getValue() {
		return _value;
	}
}