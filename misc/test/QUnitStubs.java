package rita.wordnet.test;



import static org.junit.Assert.*;

import java.util.*;

public class QUnitStubs
{
  public static void ok(Object o)
  {
    assertNotNull(o);
  }
  public static void ok(int o)
  {
    assertTrue(o != 0);
  }
  public static void ok(float o)
  {
    assertTrue(o != 0);
  }
  public static void ok(double o)
  {
    assertTrue(o != 0);
  }
  public static void ok(boolean o)
  {
    assertTrue(o);
  }

  public static void ok(boolean o,String s)
  {
    if (!o) System.err.println("[FAIL] '"+s+"'");
    assertTrue(o);
  }
  public static void ok(char o)
  {
    assertTrue(o>=0); // ?
  }
  
  /////////////////////////////////////////////////////////
  
  public static void equal(Object o, Object p)
  {
    deepEqual(o, p);
  }
  public static void equal(int o, int p)
  {
    assertTrue(o==p);
  }
  public static void equal(char o, char p)
  {
    assertTrue(o==p);
  }
  public static void equal(double o, double p)
  {
    equal(o,p,0); 
  }
  public static void equal(double o, double p, double delta)
  {
    assertEquals(o,p,delta); 
  }
  public static void equal(boolean o, boolean p, String s)
  {
    if (!o==p) System.err.println("[FAIL] '"+s+"'");
    //System.out.println(s);
    assertTrue(o==p);
  }

  /////////////////////////////////////////////////////////

  public static void deepEqual(double[] o, double[] p)
  {
    if (o !=null && p != null && o.length==p.length) {
      for (int i = 0; i < p.length; i++)
          assertTrue(o[i]==p[i]);
    }
    else {
      assertTrue(o==null && p==null);
    }
  }
  
  public static void deepEqual(boolean[] o, boolean[] p)
  {
    if (o !=null && p != null && o.length==p.length) {
      for (int i = 0; i < p.length; i++)
          assertTrue(o[i]==p[i]);
    }
    else {
      assertTrue(o==null && p==null);
    }
  }
  
  public static void deepEqual(float[] o, float[] p)
  {
    if (o !=null && p != null && o.length==p.length) {
      for (int i = 0; i < p.length; i++)
          assertTrue(o[i]==p[i]);
    }
    else {
      assertTrue(o==null && p==null);
    }
  }

  public static void deepEqual(int[] o, int[] p)
  {
    if (o !=null && p != null && o.length==p.length) {
      for (int i = 0; i < p.length; i++)
          assertTrue(o[i]==p[i]);
    }
    else {
      assertTrue(o==null && p==null);
    }
  }
  
  /** Returns true if all items in subset are in superset */
  public static void setContains(Object[] superset, Object[] subset)
  {
    if (superset == subset || superset == null && subset == null)
      return;
    
    assertTrue(Arrays.asList(superset).containsAll(Arrays.asList(subset)));
  }
  
  /** Returns true if all items in subset are in superset */
  public static void setContains(Object[] superset, Object item)
  {
    assertTrue(Arrays.asList(superset).contains(item));
  }
  
  public static void setEqual(Object[] a1, Object[] a2)
  {
    if (a1 == a2 || a1 == null && a2 == null)
      return;
    Set s1 = new HashSet(),  s2 = new HashSet();
    s1.addAll(Arrays.asList(a1));
    s2.addAll(Arrays.asList(a2));
    assertTrue(s1==s2 || s1.equals(s2));
  }
  
  public static void deepEqual(Object o, Object p)
  {
    if (o instanceof Object[] && p instanceof Object[])
      assertArrayEquals((Object[])o, (Object[])p);
    else {
      assertTrue((o==null && p==null) || (o!=null && p!=null && o.equals(p)));
    }
  }

  /////////////////////////////////////////////////////////
  
  public static void notEqual(Object o, Object p)
  {
    assertNotSame(o, p);
  }
  public static void notEqual(int o, int p)
  {
    assertTrue(o!=p); 
  }
  public static void notEqual(char o, char p)
  {
    assertTrue(o!=p); 
  }
  public static void notEqual(double o, double p)
  {
    assertTrue(o!=p); 
  }
  public static void notEqual(boolean o, boolean p)
  {
    assertTrue(o!=p); 
  }

  
  /////////////////////////////////////////////////////////

  public static boolean SILENT = true;

  public static void println(List l) { println(l,0); }
  public static void println(Object[]  l) { println(l,0); }
  public static void println(Object l) { println(l,0); }
  public static void print(int l) { print(l,0); }
  public static void print(float l) { print(l,0); }
  public static void print(boolean l) { print(l,0); }
  public static void print(double l) { print(l,0); }
  
  public static void println(List l, int k)
  {
    if (SILENT && k != 1) return;
    if (l == null || l.size() <1) {
      System.out.println("[]");
      return;
    }
    int i = 0;
    for (Iterator it = l.iterator(); it.hasNext(); i++)
      System.out.println(i + ") '" + it.next()+"'");
  }

  public static void println(Object[] l, int k)
  {
    if (SILENT && k != 1) return;
    if (l == null || l.length <1)     {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j]+"'");
  }
  
  public static void println(int[] l, int k)
  {
    if (SILENT && k != 1) return;
    if (l == null || l.length <1) System.out.println("[]");
    else for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j]+"'");
  }
  
  public static void println(float[] l, int k)
  {
    if (SILENT && k != 1) return;
    if (l == null || l.length <1) System.out.println("[]");
    else for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j]+"'");
  }
  
  public static void println(boolean[] l, int k)
  {
    if (SILENT && k != 1) return;
    if (l == null || l.length <1) System.out.println("[]");
    else for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j]+"'");
  }
  
  public static void println(double[] l, int k)
  {
    if (SILENT && k != 1) return;
    if (l == null || l.length <1) System.out.println("[]");
    else for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j]+"'");
  }

  public static void println(Object l, int k)
  {
    if (SILENT && k != 1) return;
    if (l instanceof boolean[]) println((boolean[])l,0);
    else if (l instanceof int[]) println((int[])l,0);
    else if (l instanceof float[]) println((float[])l,0);
    else if (l instanceof double[]) println((double[])l,0);
    else {
      System.out.println(l);
    }
  }
  
  public static void print(Object l, int k)
  {
    if (SILENT && k != 1) return;
    System.out.print(l);
  }
  
  public static void printArr(Object[] l)
  {
    if (SILENT) return;
    System.out.print("{ ");
    for (int j = 0; l != null && j < l.length; j++) {
      System.out.print("\"" + l[j]+"\"");
      if (j<l.length-1)
        System.out.print(", ");
    }
    System.out.println(" }");
  }
}
