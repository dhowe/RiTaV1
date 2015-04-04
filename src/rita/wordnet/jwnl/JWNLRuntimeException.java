/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl;



/** Base level runtime exception used by JWNL. Tries to resolve the message using JWNL.resolveMessage. */
public class JWNLRuntimeException extends RuntimeException {
	public JWNLRuntimeException(String key) {
		super(JWNL.resolveMessage(key));
	}

	public JWNLRuntimeException(String key, Object arg) {
		super(JWNL.resolveMessage(key, arg));
	}

	public JWNLRuntimeException(String key, Object[] args) {
		super(JWNL.resolveMessage(key, args));
	}

	public JWNLRuntimeException(String key, Throwable cause) {
		super(JWNL.resolveMessage(key), cause);
	}

	public JWNLRuntimeException(String key, Object[] args, Throwable cause) {
		super(JWNL.resolveMessage(key, args), cause);
	}

	public JWNLRuntimeException(String key, Object arg, Throwable cause) {
		super(JWNL.resolveMessage(key, arg), cause);
	}
}