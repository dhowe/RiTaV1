package rita.support;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/*
try
  {
    System.getProperties().save(new FileOutputStream("/tmp/P5Properties.xml"), "");
    System.out.println("done");
  }
  catch (Exception e1)
  {      
    e1.printStackTrace();
  }
*/
public class CompareProperties
{
  public static String EXISTING_PROPERTY_FILE = "/tmp/P5Properties.xml";

  static HashMap<String, String> getMap(Properties p)
  {
    HashMap<String, String> m = new HashMap<String, String>();
    Enumeration e = p.propertyNames();

    while (e.hasMoreElements())
    {
      String key = (String) e.nextElement();
      String value = p.getProperty(key);
      m.put(key, value);
    }
    return m;
  }
  
  public static void main(String[] args) throws IOException
  {
    Properties p = System.getProperties();
    HashMap<String, String> m1 = getMap(p);
    
    Properties p2 = new Properties();
    p2.load(new FileInputStream(EXISTING_PROPERTY_FILE));

    HashMap<String, String> m2 = getMap(p2);
    
    System.out.println("Sizes: "+m1.size() + " and "+m2.size());
    ArrayList notInM1 = new ArrayList();
    ArrayList notInM2 = new ArrayList();
    ArrayList different = new ArrayList();
    for (Iterator it = m1.keySet().iterator(); it.hasNext();)
    {
      String key = (String) it.next();
      String val = (String) m1.get(key);
      if (!m2.containsKey(key))
        notInM2.add(key+"="+val); 
      String val2 = (String) m2.get(key);
      if (!val.equals(val2))
        different.add("  "+key+":\n      1) "+val+" (current)\n      2) "+val2+" (saved)");
    }
    for (Iterator it = m2.keySet().iterator(); it.hasNext();)
    {
      String key = (String) it.next();
      String val = (String) m2.get(key);
      if (!m1.containsKey(key))
        notInM1.add(key+"="+val); 
    }
    
    System.out.println("\nMissing from current: ");
    for (Iterator it = notInM1.iterator(); it.hasNext();)
    {
      System.out.println("  "+it.next());
    }

    System.out.println("\nMissing from saved: ");
    for (Iterator it = notInM2.iterator(); it.hasNext();)
    {
      System.out.println("  "+it.next());
    }
    
    System.out.println("\nConflicting values: ");
    for (Iterator it = different.iterator(); it.hasNext();)
    {
      System.out.println("  "+it.next());
    }
    
    System.out.println("\n\nEncoding: " + System.getProperty("file.encoding"));
    System.out.println("Charset: " + java.nio.charset.Charset.defaultCharset());
  }
}
