/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.princeton.file;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import rita.wordnet.jwnl.JWNLRuntimeException;
import rita.wordnet.jwnl.dictionary.file.DictionaryFile;
import rita.wordnet.jwnl.dictionary.file.DictionaryFileType;
import rita.wordnet.jwnl.dictionary.file.ObjectDictionaryFile;
import rita.wordnet.jwnl.wndata.POS;

/** <code>ObjectDictionaryFile</code> that accesses files names with the Princeton dictionary file naming convention. */
public class PrincetonObjectDictionaryFile extends AbstractPrincetonDictionaryFile implements ObjectDictionaryFile {
	private File _file = null;
	private ObjectInputStream _in = null;
	private ObjectOutputStream _out = null;

	public PrincetonObjectDictionaryFile() {}

	public DictionaryFile newInstance(String path, POS pos, DictionaryFileType fileType) {
		return new PrincetonObjectDictionaryFile(path, pos, fileType);
	}

	public PrincetonObjectDictionaryFile(String path, POS pos, DictionaryFileType fileType) {
		super(path, pos, fileType);
	}

	public boolean isOpen() {
		return (_file != null);
	}
		
	public void close() {
		try {
			if (canRead())
				getInputStream().close();
			if (canWrite())
				getOutputStream().close();
		} catch (Exception ex) {
		} finally {
			_in = null;
			_out = null;
			_file = null;
		}
	}
	
	/** Open the input and output streams. */
	public void openStreams() throws IOException {
		if (!canWrite())
			openOutputStream();
		if (!canRead())
			openInputStream();
	}
	
	private void openOutputStream() throws IOException {
		_out = new ObjectOutputStream(new FileOutputStream(_file));
	}

	private void openInputStream() throws IOException {
		_in = new ObjectInputStream(new FileInputStream(_file));
	}

	public ObjectInputStream getInputStream() throws IOException {
		if (!canRead()) openInputStream();
		return _in;
	}

	public ObjectOutputStream getOutputStream() throws IOException {
		if (!canWrite()) openOutputStream();
		return _out;
	}
	
	public boolean canRead() {
		return _in != null;
	}
	
	public boolean canWrite() {
		return _out != null;
	}
	
	public Object readObject() throws IOException, ClassNotFoundException {
		if (isOpen() && canRead()) {
            return getInputStream().readObject();
        } else {
            throw new JWNLRuntimeException("PRINCETON_EXCEPTION_001");
        }
	}
			
	public void writeObject(Object obj) throws IOException {
		if (isOpen() && canWrite()) {
            getOutputStream().writeObject(obj);
        } else {
            throw new JWNLRuntimeException("PRINCETON_EXCEPTION_002");
        }
	}

	/**
	 * Here we try to be intelligent about opening streams.
	 * If the file does not already exist, we assume that we are going
	 * to be creating it and writing to it, otherwise we assume that
	 * we are going to be reading from it. If you want the other stream
	 * open, you must do it explicitly by calling <code>openStreams</code>.
	 */
	protected void openFile(File path) throws IOException {
		_file = path;
		if (!_file.exists()) {
			_file.createNewFile();
			openOutputStream();
		} else {
			openInputStream();
		}
	}
}