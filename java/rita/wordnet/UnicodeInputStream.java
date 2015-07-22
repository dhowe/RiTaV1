package rita.wordnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * @invisible
 */
public class UnicodeInputStream extends InputStream
{
  private static final String DEFAULT_ENCODING = "UTF-8";
  public static final int BOM_SIZE = 4;
  
  private boolean initd;
  private int BOMOffset = -1;
  private String defaultEnc, encoding;
  private PushbackInputStream internalIn;

  public UnicodeInputStream(InputStream in)
  {
    this(in, DEFAULT_ENCODING);
  }
    
  public UnicodeInputStream(InputStream in, String defaultEnc)
  {
    internalIn = new PushbackInputStream(in, BOM_SIZE);
    this.defaultEnc = defaultEnc;
    try {
      init();
    }
    catch (IOException e) {
      throw new RiWordNetError(e);
    }
  }

  public String getDefaultEncoding()
  {
    return defaultEnc;
  }

  public String getEncoding()
  {
    if (!initd)
    {
      try {
        init();
      }
      catch (IOException ex)  {
        IllegalStateException ise = new IllegalStateException("Init method failed.");
        ise.initCause(ise);
        throw ise;
      }
    }
    return encoding;
  }

  /**
   * Read-ahead four bytes and check for BOM marks. Extra bytes are unread back
   * to the stream, only BOM bytes are skipped.
   */
  protected void init() throws IOException
  {
    if (initd) return;

    int unread = -1;
    byte bom[] = new byte[BOM_SIZE];
    int n = internalIn.read(bom, 0, bom.length);

    if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF))
    {
      encoding = "UTF-32BE";
      unread = n - 4;
    }
    else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00))
    {
      encoding = "UTF-32LE";
      unread = n - 4;
    }
    else if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF))
    {
      encoding = "UTF-8";
      unread = n - 3;
    }
    else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF))
    {
      encoding = "UTF-16BE";
      unread = n - 2;
    }
    else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE))
    {
      encoding = "UTF-16LE";
      unread = n - 2;
    }
    else
    {
//System.out.println("NO ENCODING: using default="+defaultEnc);
      
      // Unicode BOM mark not found, unread all bytes
      encoding = defaultEnc;
      unread = n;
    }
    BOMOffset = BOM_SIZE - unread;
    if (unread > 0)
      internalIn.unread(bom, (n - unread), unread);

//System.out.println("ENCODING: "+encoding);

    initd = true;
  }

  public void close() throws IOException
  {
    initd = true;
    internalIn.close();
  }

  public int read() throws IOException
  {
    initd = true;
    return internalIn.read();
  }

  public int getBOMOffset()
  {
    return BOMOffset;
  }

}