package rita.wordnet.jwnl.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/** A ResourceBundle that is a proxy to multiple ResourceBundles. */
public class ResourceBundleSet extends ResourceBundle {
	private Locale _locale = Locale.getDefault();
	private List _resources = new ArrayList();

	public ResourceBundleSet(String resource) {
		addResource(resource);
	}

	public ResourceBundleSet(String[] resources) {
		for (int i = 0; i < resources.length; i++)
			addResource(resources[i]);
	}

	public void addResource(String resource) {
		_resources.add(resource);
	}

	public String[] getResources() {
		return (String[])_resources.toArray(new String[_resources.size()]);
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	protected Object handleGetObject(String key) {
		for (Iterator itr = _resources.iterator(); itr.hasNext();) {
			try {
				ResourceBundle bundle = getBndl((String)itr.next());
				String msg = bundle.getString(key);
				if (msg != null) return msg;
			} catch (Exception ex) {
			}
		}
		return key;
	}

	public Enumeration getKeys() {
		return new Enumeration() {
			private Iterator _itr = _resources.iterator();
			private Enumeration _currentEnum;

			public boolean hasMoreElements() {
				if (_currentEnum == null || !_currentEnum.hasMoreElements()) {
					if (_itr.hasNext()) {
						_currentEnum = getBndl((String)_itr.next()).getKeys();
					}
				}
				if (_currentEnum != null) {
					return _currentEnum.hasMoreElements();
				}
				return false;
			}


			public Object nextElement() {
				return _currentEnum.nextElement();
			}
		};
	}

	private ResourceBundle getBndl(String bundle) {
		return ResourceBundle.getBundle(bundle, _locale);
	}
}