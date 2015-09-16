package rita.support;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class QUnitStubs {
  public static void ok(Object o) {
    assertNotNull(o);
  }

  public static void ok(int o) {
    assertTrue(o != 0);
  }

  public static void ok(float o) {
    assertTrue(o != 0);
  }

  public static void ok(double o) {
    assertTrue(o != 0);
  }

  public static void ok(boolean o) {
    assertTrue(o);
  }

  public static void ok(boolean o, String s) {
    if (!o)
      System.err.println("[FAIL] '" + s + "'");
    assertTrue(o);
  }

  public static void ok(char o) {
    assertTrue(o >= 0); // ?
  }

  // ///////////////////////////////////////////////////////

  public static void equal(Object o, Object p) {
    deepEqual(o, p);
  }

  public static void equal(int o, int p) {
    assertTrue(o == p);
  }

  public static void equal(char o, char p) {
    assertTrue(o == p);
  }

  public static void equal(double o, double p) {
    equal(o, p, 0);
  }

  public static void equal(double o, double p, double delta) {
    assertEquals(o, p, delta);
  }

  public static void equal(boolean o, boolean p, String s) {
    if (o != p)
      System.err.println("[FAIL] '" + s + "'");
    // System.out.println(s);
    assertTrue(o == p);
  }

  // ///////////////////////////////////////////////////////

  public static void deepEqual(double[] o, double[] p) {
    if (o != null && p != null && o.length == p.length) {
      for (int i = 0; i < p.length; i++)
	assertTrue(o[i] == p[i]);
    } else {
      assertTrue(o == null && p == null);
    }
  }

  public static void deepEqual(boolean[] o, boolean[] p) {
    if (o != null && p != null && o.length == p.length) {
      for (int i = 0; i < p.length; i++)
	assertTrue(o[i] == p[i]);
    } else {
      assertTrue(o == null && p == null);
    }
  }

  public static void deepEqual(float[] o, float[] p) {
    if (o != null && p != null && o.length == p.length) {
      for (int i = 0; i < p.length; i++)
	assertTrue(o[i] == p[i]);
    } else {
      assertTrue(o == null && p == null);
    }
  }

  public static void deepEqual(int[] o, int[] p) {
    if (o != null && p != null && o.length == p.length) {
      for (int i = 0; i < p.length; i++)
	assertTrue(o[i] == p[i]);
    } else {
      assertTrue(o == null && p == null);
    }
  }

  public static void deepEqual(Object exp, Object act) {

    if (exp instanceof Object[] && act instanceof Object[]) {

      Object[] o1 = Arrays.copyOf((Object[]) exp, ((Object[]) exp).length);
      Object[] o2 = Arrays.copyOf((Object[]) act, ((Object[]) act).length);
      Arrays.sort(o1);
      Arrays.sort(o2);
      assertArrayEquals(o1, o2);
    } else {
      assertTrue(
	  " expected: '" + exp + "' but found: '" + act + "'",
	  (exp == null && act == null)
	      || (exp != null && act != null && exp.equals(act)));
    }
  }

  // ///////////////////////////////////////////////////////

  public static void notEqual(Object o, Object p) {
    assertNotSame(o, p);
  }

  public static void notEqual(int o, int p) {
    assertTrue(o != p);
  }

  public static void notEqual(char o, char p) {
    assertTrue(o != p);
  }

  public static void notEqual(double o, double p) {
    assertTrue(o != p);
  }

  public static void notEqual(boolean o, boolean p) {
    assertTrue(o != p);
  }

  // ///////////////////////////////////////////////////////

  public static boolean SILENT = true;

  public static void println(List l) {
    println(l, 0);
  }

  public static void println(Object[] l) {
    println(l, 0);
  }

  public static void println(List l, boolean forOutput) {
    println(l, 0, forOutput);
  }

  public static void println(Object[] l, boolean forOutput) {
    println(l, 0, forOutput);
  }

  public static void println(Object l) {
    println(l, 0);
  }

  public static void print(int l) {
    print(l, 0);
  }

  public static void print(float l) {
    print(l, 0);
  }

  public static void print(boolean l) {
    print(l, 0);
  }

  public static void print(double l) {
    print(l, 0);
  }

  public static void println(List l, int k) {
    if (SILENT && k != 1)
      return;
    if (l == null) {
      System.out.println("NULL");
      return;
    }
    if (l.size() < 1) {
      System.out.println("[]");
      return;
    }
    int i = 0;
    for (Iterator it = l.iterator(); it.hasNext(); i++)
      System.out.println(i + ") '" + it.next() + "'");
  }

  public static void println(List l, int k, boolean forOutput) {
    if (!forOutput) {
      println(l, k);
      return;
    }
    if (SILENT && k != 1)
      return;
    if (l == null) {
      System.out.println("NULL");
      return;
    }
    if (l.size() < 1) {
      System.out.println("[]");
      return;
    }
    System.out.print("{ ");
    for (Iterator it = l.iterator(); it.hasNext();) {
      String item = it.next().toString().replaceAll("\"", "\\\\\"");
      System.out.print("\"" + item + "\",");
    }
    System.out.println(" }");
  }

  public static void println(Object[] l, int k) {
    if (SILENT && k != 1)
      return;
    if (l == null) {
      System.out.println("NULL");
      return;
    }
    if (l.length < 1) {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j] + "'");
  }

  public static void println(Object[] l, int k, boolean forOutput) {
    if (!forOutput) {
      println(l, k);
      return;
    }

    if (SILENT && k != 1)
      return;
    if (l == null) {
      System.out.println("NULL");
      return;
    }
    if (l.length < 1) {
      System.out.println("{}");
      return;
    }

    List asList = Arrays.asList(l);
    Collections.sort(asList);
    l = asList.toArray(new Object[0]);

    System.out.print("{ ");
    for (int j = 0; j < l.length; j++) {
      String item = l[j].toString().replaceAll("\"", "\\\\\"");
      System.out.print("\"" + item + "\",");
    }
    System.out.println(" };");
  }

  public static void println(int[] l, int k) {
    if (SILENT && k != 1)
      return;
    if (l == null) {
      System.out.println("NULL");
      return;
    }
    if (l.length < 1) {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j] + "'");
  }

  public static void println(float[] l, int k) {
    if (SILENT && k != 1)
      return;
    if (l == null) {
      System.out.println("NULL");
      return;
    }
    if (l.length < 1) {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j] + "'");
  }

  public static void println(boolean[] l, int k) {
    if (SILENT && k != 1)
      return;
    if (l == null) {
      System.out.println("NULL");
      return;
    }
    if (l.length < 1) {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j] + "'");
  }

  public static void println(double[] l, int k) {
    if (SILENT && k != 1)
      return;
    if (l == null) {
      System.out.println("NULL");
      return;
    }
    if (l.length < 1) {
      System.out.println("[]");
      return;
    }
    for (int j = 0; j < l.length; j++)
      System.out.println(j + ") '" + l[j] + "'");
  }

  public static void println(Object l, int k) {
    if (SILENT && k != 1)
      return;
    if (l instanceof boolean[])
      println((boolean[]) l, 0);
    else if (l instanceof int[])
      println((int[]) l, 0);
    else if (l instanceof float[])
      println((float[]) l, 0);
    else if (l instanceof double[])
      println((double[]) l, 0);
    else {
      System.out.println(l);
    }
  }

  public static void print(Object l, int k) {
    if (SILENT && k != 1)
      return;
    System.out.print(l);
  }

  // added 12/11/13

  /** Returns true if all items in subset are in superset */
  public static void setContains(Object[] superset, Object[] subset) {
    if (superset == subset || superset == null && subset == null)
      return;

    assertTrue(superset != null && subset != null);

    boolean ok = Arrays.asList(superset).containsAll(Arrays.asList(subset));
    if (!ok)
      dumpFail("setContains", superset, subset);
    assertTrue(ok);
  }

  static void dumpFail(String type, Object[] o1, Object[] o2) {
    System.err.println("\n\n====================  TEST FAILED (" 
	+ type + ") ====================");
    if (o1 != null && o2 != null) {
      Arrays.sort(o1);
      Arrays.sort(o2);
      for (int i = 0; i < Math.max(o1.length, o2.length); i++) {
	System.err.print(i + ") ");
	if (i < o1.length)
	  System.err.print(o1[i]);

	System.err.print(" =? ");

	if (i < o2.length)
	  System.err.print(o2[i]);

	if (i < o1.length && i < o2.length && !o1[i].equals(o2[i]))
	  System.err.print(" FAIL ***");

	System.err.println();
      }
    } else {
      System.err.println("Object[]#1=" + o1 + " Object[]#2=" + o2);
    }

    throw new RuntimeException("==========================================");
  }

  /**
   * Returns true if the item is in the superset (basically just
   * arrayContains())
   */
  public static void setContains(Object[] superset, Object item) {
    assertTrue(Arrays.asList(superset).contains(item));
  }

  public static void setEqual(Object[] a1, Object[] a2) {
    boolean ok = false;
    if (a1 != null && a2 != null) {
      Set s1 = new HashSet(), s2 = new HashSet();
      s1.addAll(Arrays.asList(a1));
      s2.addAll(Arrays.asList(a2));
      ok = s1.equals(s2);
    }
    if (!ok)
      dumpFail("setEqual", a1, a2);
    assertTrue(ok);
  }

  public static void printArr(Object[] l) {
    printArr(l, false);
  }
    
  public static void printArr(Object[] l, boolean ignoreSilent) {
    
    if (SILENT && !ignoreSilent)
      return;

    System.out.print("{ ");
    for (int j = 0; l != null && j < l.length; j++) {
      String item = l[j].toString().replaceAll("\"", "\\\\\"");
      System.out.print("\"" + item + "\"");
      if (j < l.length - 1)
	System.out.print(", ");
    }
    System.out.println(" };");
  }
}
