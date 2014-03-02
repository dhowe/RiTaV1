/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.princeton.file;

import java.io.File;
import java.io.IOException;

import rita.wordnet.RandomAccessIF;
import rita.wordnet.jwnl.JWNLRuntimeException;
import rita.wordnet.jwnl.dictionary.file.DictionaryFile;
import rita.wordnet.jwnl.dictionary.file.DictionaryFileType;
import rita.wordnet.jwnl.wndata.POS;




/**
 * A <code>RandomAccessDictionaryFile</code> that accesses files
 * named with Princeton's dictionary file naming convention.
 */
public class PrincetonRandomAccessDictionaryFile extends AbstractPrincetonRandomAccessDictionaryFile {
	/** Read-only file permission. */
	public static final String READ_ONLY = "r";
	/** Read-write file permission. */
	public static final String READ_WRITE = "rw";

	/** The random-access file. */
	public RandomAccessIF randomAccessFile = null;
  
	/** The file permissions to use when opening a file. */
	protected String _permissions;

	public DictionaryFile newInstance(String path, POS pos, DictionaryFileType fileType) {
		return new PrincetonRandomAccessDictionaryFile(path, pos, fileType);
	}

	public PrincetonRandomAccessDictionaryFile() {}

	public PrincetonRandomAccessDictionaryFile(String path, POS pos, DictionaryFileType fileType) {
		this(path, pos, fileType, READ_ONLY);
	}
	
	public PrincetonRandomAccessDictionaryFile(String path, POS pos, DictionaryFileType fileType, String permissions) {
		super(path, pos, fileType);
		_permissions = permissions;
	}

	public String readLine() throws IOException {
    if (isOpen()) {
        return randomAccessFile.readLine();
    } else {
        throw new JWNLRuntimeException("PRINCETON_EXCEPTION_001");
    }
	}

	public void seek(long pos) throws IOException  {
		randomAccessFile.seek(pos);
	}

	public long getFilePointer() throws IOException {
		return randomAccessFile.getFilePointer();
	}
	
	public boolean isOpen() {
		return randomAccessFile != null;
	}
		
	public void close() {
		try {
			randomAccessFile.close();
		} catch (Exception ex) {
		} finally {
			randomAccessFile = null;
		}
	}

	protected void openFile(File path) throws IOException {
    if (1==1) throw new RuntimeException("unimplemented!!!!");
		//_file = new RandomAccessFile(path, _permissions);
	}

	public long length() throws IOException {
		return randomAccessFile.length();
	}

	public int read() throws IOException {
		return randomAccessFile.read();
	}
}