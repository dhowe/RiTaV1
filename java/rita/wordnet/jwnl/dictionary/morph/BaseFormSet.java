package rita.wordnet.jwnl.dictionary.morph;


import java.util.ArrayList;
import java.util.List;

import rita.wordnet.jwnl.JWNLRuntimeException;

/** A group of possible base forms for a particular lemma */
public class BaseFormSet {
	private List _forms = new ArrayList();
	private int _index = -1;
	private boolean _allowDuplicates;

	public BaseFormSet() {
		this(false);
	}

	public BaseFormSet(boolean allowDuplicates) {
		_allowDuplicates = allowDuplicates;
	}

	public void add(String s) {
		if (_allowDuplicates || !_forms.contains(s)) {
			_forms.add(s);
		}
	}

	public void addAll(BaseFormSet forms) {
		if (_allowDuplicates) {
			_forms.addAll(forms._forms);
		} else {
			for (int i = 0; i < forms._forms.size(); i++) {
				add((String) forms._forms.get(i));
			}
		}
	}

	public String getForm(int index) {
		if (!isFormAvailable(index)) {
			throw new IllegalArgumentException(String.valueOf(index));
		}
		return (String) _forms.get(index);
	}

	public List getForms() {
		return _forms;
	}

	public boolean isCurrentFormAvailable() {
		return isFormAvailable(_index);
	}

	public String getCurrentForm() {
		if (!isCurrentFormAvailable()) {
			throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_012");
		}
		return getForm(_index);
	}

	public boolean isMoreFormsAvailable() {
		return isFormAvailable(_index + 1);
	}

	public String getNextForm() {
		if (!isMoreFormsAvailable()) {
			throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_013");
		}
		return getForm(++_index);
	}

	public int getIndex() {
		return _index;
	}

	public void setIndex(int index) {
		if (index < _forms.size())
			_index = index;
	}

	public int size() {
		return _forms.size();
	}

	private boolean isFormAvailable(int index) {
		return (index >= 0 && index < _forms.size());
	}
}