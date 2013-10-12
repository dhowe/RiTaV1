package rita.wordnet.jwnl.util.factory;

public class NameValueParam extends AbstractValueParam {
	private String _name;
	private String _value;

	public NameValueParam(String name, String value) {
		_name = name;
		_value = value;
	}

	public NameValueParam(String name, String value, Param[] params) {
		super(params);
		_name = name;
		_value = value;
	}

	public String getName() {
		return _name;
	}

	public String getValue() {
		return _value;
	}
}