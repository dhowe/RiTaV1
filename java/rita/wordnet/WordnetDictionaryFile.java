package rita.wordnet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import rita.RiTa;
import rita.RiWordNet;
import rita.wordnet.jwnl.JWNL;
import rita.wordnet.jwnl.dictionary.file.DictionaryFile;
import rita.wordnet.jwnl.dictionary.file.DictionaryFileType;
import rita.wordnet.jwnl.princeton.file.PrincetonRandomAccessDictionaryFile;
import rita.wordnet.jwnl.wndata.POS;


/**
 * @invisible
 * @author dhowe
 * <p>See the accompanying documentation for license information 
 */
public class WordnetDictionaryFile extends PrincetonRandomAccessDictionaryFile
{
  private static final Map _posToExtMap;
  private static final Map _fileTypeToFileNameMap;
  
  private static final String NOUN_EXT = "noun";
  private static final String VERB_EXT = "verb";
  private static final String ADJECTIVE_EXT = "adj";
  private static final String ADVERB_EXT = "adv";
  private static boolean DBUG = false;

  static {
    //System.err.println("WordnetDictionaryFile.static : "+RiWordnet.pApplet);
    _posToExtMap = new HashMap(4, 1);
    _posToExtMap.put(POS.NOUN, NOUN_EXT);
    _posToExtMap.put(POS.VERB,  VERB_EXT);
    _posToExtMap.put(POS.ADJECTIVE, ADJECTIVE_EXT);
    _posToExtMap.put(POS.ADVERB, ADVERB_EXT);
    
    _fileTypeToFileNameMap = new HashMap(3, 1);
    _fileTypeToFileNameMap.put(DictionaryFileType.INDEX, new FileNames("idx", "index"));
    _fileTypeToFileNameMap.put(DictionaryFileType.DATA, new FileNames("dat", "data"));
    _fileTypeToFileNameMap.put(DictionaryFileType.EXCEPTION, new FileNames("exc", "exc"));
    
    if (DBUG) System.out.println("[INFO] "+JWNL.getVersion());//+" "+JWNL.getOS());
    //System.err.println("WordnetDictionaryFile.enclosing_method("+_posToExtMap+")");
  }
  
  public WordnetDictionaryFile() {
    super();
    //System.err.println("WordnetDictionaryFile.WordnetDictionaryFile()");
  }

  public WordnetDictionaryFile(RandomAccessIF file, POS pos, DictionaryFileType fileType) {
    super();
    this._pos = pos;
    
    // what about _file from AbstractDictionaryFile
    
    this.randomAccessFile = file;
    this._fileType = fileType;
  }
  
  public WordnetDictionaryFile(String path, POS pos, DictionaryFileType fileType) {
    this(path, pos, fileType, READ_ONLY);
  }
  
  public WordnetDictionaryFile(String path, POS pos, DictionaryFileType fileType, String permissions) {
    super(path, pos, fileType);
    _permissions = permissions;
  }

  public DictionaryFile newInstance(String path, POS pos, DictionaryFileType fileType) {
    //System.err.println("WordnetDictionaryFile.newInstance("/*+path+*/+","+pos+","+fileType+")");
    return new WordnetDictionaryFile(path, pos, fileType);
  }
  
  private static String getExtension(POS pos) {
    return (String)_posToExtMap.get(pos);
  }

  private static FileNames getFileNames(DictionaryFileType type) {
    return (FileNames)_fileTypeToFileNameMap.get(type);
  }
  
  private static String makeWindowsFilename(String posStr, String fileTypeStr) {
    return posStr + "." + fileTypeStr;
  }

  private static String makeNonWindowsFilename(String posStr, String fileTypeStr) {
    return fileTypeStr + "." + posStr;
  }
  
  protected String makeFilename() 
  {
    if (DBUG) System.err.print("WordnetDictionaryFile.makeFilename()");
    
    String s = null, posString = getExtension(getPOS());
    //double version = JWNL.getVersion().getNumber();
    
    if (getFileType() == DictionaryFileType.EXCEPTION) {// TEST THIS: || JWNL.getOS() == JWNL.WINDOWS) {
      
      s = makeWindowsFilename(posString, getFileNames(getFileType())._windowsFileTypeName);
      if (DBUG) System.err.println(" -> "+s);
      return s;
    }
    else {      
      s = makeNonWindowsFilename(posString, getFileNames(getFileType())._nonWindowsFileTypeName);
      if (DBUG) System.err.println(" -> "+s);     
    }
    //System.err.println("filename: "+s);
    return s;
  }
  
  public static String buildFilename(POS pos, DictionaryFileType getFileType) 
  {
    //if (DBUG) System.err.print("WordnetDictionaryFile.buildFilename()");
    String s = null, posString = getExtension(pos);

    if (getFileType == DictionaryFileType.EXCEPTION) {      
      s = makeWindowsFilename(posString, getFileNames(getFileType)._windowsFileTypeName);
      if (DBUG) System.err.println(" -> "+s);
      return s;
    }
    else {      
      s = makeNonWindowsFilename(posString, getFileNames(getFileType)._nonWindowsFileTypeName);
      if (DBUG) System.err.println(" -> "+s);     
    }
    //System.err.println("filename: "+s);
    return s;
  }
  


  private static final class FileNames 
  {
    String _windowsFileTypeName;
    String _nonWindowsFileTypeName;

    public FileNames(String windowsFileTypeName, String nonWindowsFileTypeName) {
      _windowsFileTypeName = windowsFileTypeName;
      _nonWindowsFileTypeName = nonWindowsFileTypeName;
    }
  }

  protected void openFile(File fileLoc) throws IOException 
  {
    //System.out.println("WordnetDictionaryFile.openFile("+fileLoc+")");
    if (!fileLoc.exists()) 
      throw new RiWordNetError("Couldn't find file: "+fileLoc);
    
    InputStream is = null;
    String fileName = fileLoc.getPath();
    if (RiWordNet.wordNetHome != null) {
      is = new FileInputStream(fileName);
    }
    else {
      is = RiTa.openStream(fileName);        
      if (is == null) {
        System.err.println("[ERROR] Unable to open bad stream: "+fileName);
        System.exit(1);
      }
    }
    randomAccessFile = new RandomAccessByteArray(fileName, is);
  } 
}
