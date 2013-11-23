package rita.support;

import java.io.*;
import java.util.*;

import rita.RiTa;
import rita.RiTaException;

/**
 * Properties type Map (String->String[]) holding multiple values for each Key
 * When loaded from a file, follows Properties-style formatting conventions,
 * allowing '=', ':', '=>' and '->' as delimiters.  
 * @author dhowe
 */
public class MultiMap
{  
  private static final String UTF_8 = "UTF-8";

  private static final String EQ = "=", E = "";
  
  public Map<String, String[]> data = new HashMap<String, String[]>();
  
  public MultiMap() 
  {
    this(null);
  }
  
  public MultiMap(String fileName) 
  {

    if (fileName != null) {
      String contents = RiTa.loadString(fileName);
      loadFromString(contents);
    }
  }

  public MultiMap loadFromString(String s)
  {
    data.clear();
    
    s = s.trim().replaceAll("\\\\[\\t ]+", "\\\\"); // TODO: WHY: DO WE WANT/NEED THIS? ??
    s = s.replaceAll("=>", EQ);                     // TODO: Add tests for this class 
    s = s.replaceAll("->", EQ); 
    s = s.replaceAll(":",  EQ);
    
    try
    {
      InputStream is = new ByteArrayInputStream(s.getBytes(UTF_8));
      return load(new InputStreamReader(is, UTF_8));
    }
    catch (IOException e)
    {
      throw new RiTaException(e.getMessage());
    }
  }
  
  private synchronized MultiMap load(Reader reader) 
  {
    int NONE = 0, SLASH = 1, UNICODE = 2, CONTINUE = 3, KEY_DONE = 4, IGNORE = 5;
    int mode = NONE, unicode = 0, count = 0;
    char nextChar, buf[] = new char[40];
    int offset = 0, keyLength = -1, intVal;
    boolean firstChar = true;
    BufferedReader br = new BufferedReader(reader);
    try
    {
      while (true)
      {
        intVal = br.read();
        if (intVal == -1)
          break;
        nextChar = (char) intVal;

        if (offset == buf.length)
        {
          char[] newBuf = new char[buf.length * 2];
          System.arraycopy(buf, 0, newBuf, 0, offset);
          buf = newBuf;
        }
        if (mode == UNICODE)
        {
          int digit = Character.digit(nextChar, 16);
          if (digit >= 0)
          {
            unicode = (unicode << 4) + digit;
            if (++count < 4)
            {
              continue;
            }
          }
          else if (count <= 4)
          {
            // luni.09=Invalid Unicode sequence: illegal character
            throw new IllegalArgumentException("luni.09");
          }
          mode = NONE;
          buf[offset++] = (char) unicode;
          if (nextChar != '\n' && nextChar != '\u0085')
          {
            continue;
          }
        }
        if (mode == SLASH)
        {
          mode = NONE;
          switch (nextChar)
          {
            case '\r':
              mode = CONTINUE; // Look for a following \n
              continue;
            case '\u0085':
            case '\n':
              mode = IGNORE; // Ignore whitespace on the next line
              continue;
            case 'b':
              nextChar = '\b';
              break;
            case 'f':
              nextChar = '\f';
              break;
            case 'n':
              nextChar = '\n';
              break;
            case 'r':
              nextChar = '\r';
              break;
            case 't':
              nextChar = '\t';
              break;
            case 'u':
              mode = UNICODE;
              unicode = count = 0;
              continue;
          }
        }
        else
        {
          switch (nextChar)
          {
            case '#':
            case '!':
              if (firstChar)
              {
                while (true)
                {
                  intVal = br.read();
                  if (intVal == -1)
                    break;
                  nextChar = (char) intVal; 
                  if (nextChar == '\r' || nextChar == '\n' || nextChar == '\u0085')
                    break;
                }
                continue;
              }
              break;
            case '\n':
              if (mode == CONTINUE)
              { // Part of a \r\n sequence
                mode = IGNORE; // Ignore whitespace on the next line
                continue;
              }
              // fall into the next case
            case '\u0085':
            case '\r':
              mode = NONE;
              firstChar = true;
              if (offset > 0 || (offset == 0 && keyLength == 0))
              {
                if (keyLength == -1)
                {
                  keyLength = offset;
                }
                String temp = new String(buf, 0, offset);
                add(temp.substring(0, keyLength), temp.substring(keyLength));
              }
              keyLength = -1;
              offset = 0;
              continue;
            case '\\':
              if (mode == KEY_DONE)
              {
                keyLength = offset;
              }
              mode = SLASH;
              continue;
            case ':':
            case '=':
              if (keyLength == -1)
              { 
                // if parsing the key
                mode = NONE;
                keyLength = offset;
                continue;
              }
              break;
          }
          if (Character.isWhitespace(nextChar))
          {
            if (mode == CONTINUE)
            {
              mode = IGNORE;
            }
            // if key length == 0 or value length == 0
            if (offset == 0 || offset == keyLength || mode == IGNORE)
            {
              continue;
            }
            if (keyLength == -1)
            { // if parsing the key
              mode = KEY_DONE;
              continue;
            }
          }
          if (mode == IGNORE || mode == CONTINUE)
          {
            mode = NONE;
          }
        }
        firstChar = false;
        if (mode == KEY_DONE)
        {
          keyLength = offset;
          mode = NONE;
        }
        buf[offset++] = nextChar;
      }
      if (mode == UNICODE && count <= 4)
      {
        // luni.08=Invalid Unicode sequence: expected format \\uxxxx
        throw new IllegalArgumentException("luni.08");
      }
      if (keyLength == -1 && offset > 0)
      {
        keyLength = offset;
      }
      if (keyLength >= 0)
      {
        String temp = new String(buf, 0, offset);
        String key = temp.substring(0, keyLength);
        String value = temp.substring(keyLength);
        if (mode == SLASH)
        {
          value += "\u0000";
        }
        add(key, value);
      }
    }
    catch (IOException e)
    {
     throw new RiTaException(e);
    }
    return this;
  }
  
  /** 
   * Adds val to the array for key, or creates a new 1-element 
   * array for val if no such key exists 
   */
  public MultiMap add(String key, String val)
  {
    String[] finalVal = null;
    String[] s = data.get(key);
    if (s != null) {
      finalVal = new String[s.length+1]; 
      System.arraycopy(s, 0, finalVal, 0, s.length);
      finalVal[finalVal.length-1] = val;
    }
    else {
      finalVal = new String[] { val };
    }
    data.put(key, finalVal);
    
    return this;
  }

  public MultiMap clear()
  {
    data.clear();
    return this;
  }

  public boolean containsKey(String key)
  {
    return data.containsKey(key);
  }

  public String[] get(String key)
  {
    return data.get(key);
  }
  
  /** 
   * Looks up the key and returns the element at the specified
   *  index or null if the key doesnt exist 
   */
  public String getAt(String key, int index)
  {
    String[] s = data.get(key);
    if (s == null) return null;
    return s[index];
  }
  
  /** Returns number of elements for the key */
  public int length(String key)
  {
    return data.get(key).length;
  }

  public boolean isEmpty()
  {
    return data.isEmpty();
  }

  public Set<String> keySet()
  {
    return data.keySet();
  }

  public String[] put(String key, String[] value)
  {
    return data.put(key, value);
  }

  public int size()
  {
    return data.size();
  }

  public Collection<String[]> values()
  {
    return data.values();
  }

  public String toString()
  {
    String s = E;
    for (Iterator it = keySet().iterator(); it.hasNext();)
    {
      String key= (String) it.next();
      s += (key+"="+RiTa.asList(get(key)));
      if (it.hasNext()) s += ", ";
    }
    return s;
  }
  
  public String[] remove(String key)
  {
    return data.remove(key);
  }
  
  public static void main(String[] args)
  {
    String s = "greetings: hello | goodbye";
    MultiMap mm = new MultiMap();
    mm.load(new StringReader(s));
    System.out.println("MAP: "+mm);
    mm.add("rule1", "val1");
    mm.add("rule1", "val1");
    mm.add("rule1", "val2");
    mm.add("rule2", "val3");
    System.out.println("MAP: "+mm);
    System.out.println("ARR: "+Arrays.asList(mm.get("rule1")));
    System.out.println("RM:  "+Arrays.asList(mm.remove("rule1")));
    System.out.println("MAP: "+mm);
  }

}
