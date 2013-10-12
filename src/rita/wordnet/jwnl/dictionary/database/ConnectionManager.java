package rita.wordnet.jwnl.dictionary.database;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import rita.wordnet.jwnl.JWNLException;

public class ConnectionManager {
	private String _driverClass;
	private String _url;
	private String _userName;
	private String _password;
    private boolean _registered;

	public ConnectionManager(String driverClass, String url, String userName, String password) {
		_driverClass = driverClass;
		_url = url;
		_userName = userName;
		_password = password;
	}

	public Query getQuery(String sql) throws SQLException, JWNLException {
		return new Query(sql, getConnection());
	}

	public Connection getConnection() throws SQLException, JWNLException  {
		registerDriver();
		if (_userName == null ) {
			return DriverManager.getConnection(_url);
		} else {
			return DriverManager.getConnection(
					_url, _userName, (_password != null) ? _password : "");
		}
	}

	private void registerDriver() throws JWNLException {
		if (!_registered) {
			try {
				Driver driver = (Driver) Class.forName(_driverClass).newInstance();
				DriverManager.registerDriver(driver);
				_registered = true;
			} catch (Exception ex) {
				throw new JWNLException("DICTIONARY_EXCEPTION_024", ex);
			}
		}
	}
}