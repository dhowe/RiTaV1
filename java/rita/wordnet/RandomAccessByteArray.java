package rita.wordnet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.stream.MemoryCacheImageInputStream;

/**
 * See the accompanying documentation for license information
 * @invisible
 * @author dhowe
 */
public class RandomAccessByteArray
 implements RandomAccessIF
{ 
  protected MemoryCacheImageInputStream mis;
  protected String name = "unknown";
  protected long length = -1L;
  
  public RandomAccessByteArray(InputStream is) {
    this(null, is);
  }

  public RandomAccessByteArray(String name, InputStream is) {
    if (name != null) this.name = name;   
    try {
      this.length = is.available(); 
/*      if (is instanceof GZIPInputStream) {
        if (1==1) throw new RuntimeException("GZIPs not supported!");
        this.length = slowLength(is); //never use..
      }*/
      //System.err.println("RandomAccessByteArray: length="+length);
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
    this.mis = new MemoryCacheImageInputStream(is);
  }
  
  public RandomAccessByteArray(String name, byte[] data) {
    if (name != null) this.name = name; 
    this.length = data.length;
    this.mis = new MemoryCacheImageInputStream
      (new ByteArrayInputStream(data));      
  }
  
  public String toString()
  {
    return name;
  }
  
  // profile the length determination...
  private void determineLength(InputStream is) {
    long start = System.currentTimeMillis();
    System.err.print("length of '"+name+"'=");
    try {
      length = is.available();
    } catch (IOException e) {
      e.printStackTrace();
    }
    long elapsed = System.currentTimeMillis()-start;
    System.err.println(length+" time="+elapsed/1000d+"s");
  }

  /* (non-Javadoc)
   * @see wn.RandomAccessIF#close()
   */
  public void close() throws IOException {
    mis.close();
  }
  
  /* (non-Javadoc)
   * @see wn.RandomAccessIF#read()
   */
  public int read() throws IOException {
    return mis.read();
  }
  
  /* (non-Javadoc)
   * @see wn.RandomAccessIF#length()
   */
  public long length() throws IOException {
    return this.length;// > 1 ? length : mis.length();
  } 
  
  /* (non-Javadoc)
   * @see wn.RandomAccessIF#seek(long)
   */
  public void seek(long pos) throws IOException {
    mis.seek(pos);
  } 
  
  /* (non-Javadoc)
   * @see wn.RandomAccessIF#getFilePointer()
   */
  public long getFilePointer() throws IOException {
    return mis.getStreamPosition();
  }

  /* (non-Javadoc)
   * @see wn.RandomAccessIF#readLine()
   */
  public String readLine() throws IOException {
    return mis.readLine();
  }
}
