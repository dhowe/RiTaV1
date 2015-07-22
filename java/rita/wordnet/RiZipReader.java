package rita.wordnet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import rita.RiTa;
import rita.RiWordNet;

/**
 * Maps resources included in a Zip or Jar into a hash<p>
 * 
 * TODO: add constructor flag to allow lazy reading of zip entrys
 * 
 * @invisible
 * <p>See the accompanying documentation for license information
 */
public class RiZipReader
{
  public boolean dbug=false;
  
  private Map fileSizes;  
  private Map fileContents;
  private String zipfileName;

  public RiZipReader(String archiveName) {
    this(archiveName, null);
  }
  
  public RiZipReader(String archiveName, InputStream is) {
    //System.err.println("RiZipReader.RiZipReader("+is+")");
    this.zipfileName = archiveName; 
    if (is != null) this.init(is);
    RiWordNet.zipReader = this; //ugh!
  }

  public void dispose() 
  {
    if (fileSizes != null) {
      fileSizes.clear();
      fileSizes = null;      
    }
    if (fileContents != null) {
      fileContents.clear();
      fileContents = null;      
    }    
  }
  
  public Iterator iterator()
  {
    if (fileContents == null) init();
    return fileContents.keySet().iterator();
  }
  
  /**
   * Extracts a jar resource as byte[].
   * @param name - a resource name.
   */
  public byte[] getResource(String name)
  {
   // System.err.println("RiZipReader.getResource("+name+")"); // tmp
    if (fileContents == null) this.init();
    return (byte[]) fileContents.get(name);
  }

  private void init() { this.init(null); }  
  
  /**
   * initializes internal hash tables with Jar file resources.
   */
  private void init(InputStream is)
  {
    this.fileContents = new Hashtable();
    
    try {        
      if (is == null) is = RiTa.openStream(zipfileName);
      
      ZipInputStream zis = openZipStream(is);
      
      ZipEntry ze = null;
      while ((ze = zis.getNextEntry()) != null)
      {
        if (ze.isDirectory()) continue;
        if (dbug)
          System.out.println("file: " + ze.getName() + " "+ze.getSize()+" bytes");
        int size = (int) ze.getSize();
        if (size == -1) {// unknown size {
          if (fileSizes == null) this.cacheFileSizes();
          size = ((Integer) fileSizes.get(ze.getName())).intValue();
        }
        byte[] b = new byte[(int) size];
        //int rb = origGetBytes(zis, size, b);
        int rb = 0;
        int chunk = 0;
        while (((int) size - rb) > 0)
        {
          chunk = zis.read(b, rb, (int) size - rb);
          if (chunk == -1)
          {
            break;
          }
          rb += chunk;
        }
        
        // add to internal resource hashtable
        fileContents.put(ze.getName(), b);
        if (dbug) System.out.println(ze.getName() +
          " size="+size+ ", csize=" + ze.getCompressedSize());
      }
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private int origGetBytes(ZipInputStream zis, int size, byte[] b) throws IOException
  {
    int rb = 0;
    int chunk = 0;
    while (((int) size - rb) > 0)
    {
      chunk = zis.read(b, rb, (int) size - rb);
      if (chunk == -1)
      {
        break;
      }
      rb += chunk;
    }
    return rb;
  }
  
  protected boolean readByteArray
    (InputStream in, byte[] array, int offset, int size) throws IOException
  {
     int off = offset;
     int left = size;
   
     while( left > 0 )
     {
        int read = in.read( array, off, left );
   
        // See if we hit the end-of-file
        if( read == -1 )
           return false;
   
        off += read;
        left -= read;
     }
   
     // We have read the entire length, so return true
     return true;
  }


  private ZipInputStream openZipStream(InputStream is)
  {    
    //System.err.println("RiZipReader.openZipStream("+is+")");

    if (is instanceof JarInputStream) {      
      System.err.println("[WARN] found JarInputStream: "+is);
      return new ZipInputStream(is);
    }
    else if (is instanceof GZIPInputStream) {
      System.err.println("[WARN] found GZIPInputStream: "+is);
      return (ZipInputStream) is;
    }
    if (is instanceof ZipInputStream) {
      System.err.println("ZipInputStream: "+is);
      return (ZipInputStream) is;
    }
    else if (is instanceof InputStream) {
      //System.err.println(" InputStream: "+is);
      return new ZipInputStream(is);
    }    
    throw new RiWordNetError("Unable to open zipStream for "+zipfileName);
  }

  private void cacheFileSizes()
  {   
    System.err.println("[WARN] ZipResources: loading size cache... ");
    this.fileSizes = new HashMap();
    try
    {
      InputStream is = RiTa.openStream(zipfileName);
      ZipInputStream zis = openZipStream(is);
 
      ZipEntry zipentry = zis.getNextEntry();
      while (zipentry != null)
      {
        if (dbug)
          System.out.println(dumpZipEntry(zipentry));
        fileSizes.put(zipentry.getName(), new Integer((int) zipentry.getSize()));
        zis.closeEntry();
        zipentry = zis.getNextEntry();
      }
      zis.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Dumps a zip entry into a string.
   * @param ze a ZipEntry
   */
  private String dumpZipEntry(ZipEntry ze)
  {
    StringBuffer sb = new StringBuffer();
    if (ze.isDirectory())
    {
      sb.append("d ");
    } else
    {
      sb.append("f ");
    }
    if (ze.getMethod() == ZipEntry.STORED)
    {
      sb.append("stored   ");
    } else
    {
      sb.append("defaulted ");
    }
    sb.append(ze.getName());
    sb.append("\t");
    sb.append("" + ze.getSize());
    if (ze.getMethod() == ZipEntry.DEFLATED)
    {
      sb.append("/" + ze.getCompressedSize());
    }
    return (sb.toString());
  }
  
  public static void mainx(String[] args) throws IOException
  {
    RiZipReader zr = new RiZipReader("examples/mthesaur.dat");
    for (Iterator iter = zr.iterator(); iter.hasNext();)
    {
      String fname = (String) iter.next();
      byte[] buf = zr.getResource(fname);
      RandomAccessByteArray rab = new RandomAccessByteArray(fname, buf);
      try
      {
        System.err.println(fname+": "+rab.length()+" bytes");
      } 
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }    
  }
  
  public static void main(String[] args) throws IOException
  {
    System.err.println(System.getProperty("java.vm.version"));
    RiWordNet wordnet = new RiWordNet(null);
    String[] result = wordnet.getAllHyponyms("cat","n");    
    System.err.println(result==null?"null":Arrays.asList(result)+"");
  }
} 

