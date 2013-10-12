package rita.wordnet;

import java.io.IOException;

/**
 * See the accompanying documentation for license information
 * @invisible
 * @author dhowe
 *
 */
public interface RandomAccessIF
{

  public abstract void close() throws IOException;

  public abstract int read() throws IOException;

  public abstract long length() throws IOException;

  public abstract void seek(long pos) throws IOException;

  public abstract long getFilePointer() throws IOException;

  public abstract String readLine() throws IOException;

}