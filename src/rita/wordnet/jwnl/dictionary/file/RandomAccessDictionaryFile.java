/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary.file;

import java.io.IOException;

/** <code>DictionaryFile</code> that reads lines from a random-access text file. */
public interface RandomAccessDictionaryFile extends DictionaryFile {
    /** Read a byte from the file */
    public int read() throws IOException;

    /** Read a line from the file */
    public String readLine() throws IOException;

	/** Go to postion <var>pos</var> in the file.*/
	public void seek(long pos) throws IOException;

	/** Get the current position of the file pointer.*/
	public long getFilePointer() throws IOException;

    /** Get the length, in bytes, of the file */
	public long length() throws IOException;

	// Offset caching functions

    /** Move the file pointer so that its next line offset is <var>nextOffset</var> */
	public void setNextLineOffset(long previousOffset, long nextOffset);

	/** Return true if <code>offset</code> is the previous offset. */
	public boolean isPreviousLineOffset(long offset);

    /** Get the byte offset of the next line (after the position of the file pointer) */
	public long getNextLineOffset();
}
