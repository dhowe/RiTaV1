package rita.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

// basics from javanlp
/**
 * Utility operations that can be performed on Sets.
 */
public final class SetOp {
  /**
   * Creates and returns a new SortedSet with the elements from 'set', using the
   * specified Comparator
   */
  public static SortedSet sort(Set set, Comparator comp) {

    SortedSet ss = new TreeSet(comp);
    ss.addAll(set);
    return ss;
  }

  /** Creates and returns a new SortedSet with the elements from 'set' */
  public static SortedSet sort(Set set) {

    return new TreeSet(set);
  }

  /** Converts a Set to a new String[], assuming all elements are Strings */
  public static String[] toStringArray(Set<String> set) {
    
    return toStringArray(set, false);
  }

  public static String[] toStringArray(Set<String> set, boolean shuffled) {

    if (set == null || set.size() == 0)
      return new String[0];

    Iterator<String> i = set.iterator();

    if (shuffled) {

      List<String> l = new ArrayList<String>(set);
      Collections.shuffle(l);
      i = l.iterator();
    }

    int idx = 0;
    String[] result = new String[set.size()];
    while (i.hasNext()) {
      result[idx++] = (String) i.next();
    }

    return result;
  }

  /**
   * Returns the set cross product of s1 and s2, as <code>Pair</code>s
   */
  public static Set cross(Set s1, Set s2) {
    Set s = new HashSet();
    for (Iterator i = s1.iterator(); i.hasNext();) {
      Object o1 = i.next();
      for (Iterator j = s2.iterator(); j.hasNext();) {
	Object o2 = j.next();
	s.add(new ObjectPair(o1, o2));
      }
    }
    return s;
  }

  /** Set difference s1 - s2 */
  public static Set diff(Set s1, Set s2) {
    Set s = new HashSet();
    for (Iterator i = s1.iterator(); i.hasNext();) {
      Object o = i.next();
      if (!s2.contains(o))
	s.add(o);
    }
    return s;
  }

  /** Set union */
  public static Set union(Set s1, Set s2) {
    Set s = new HashSet();
    s.addAll(s1);
    s.addAll(s2);
    return s;
  }

  /** Set intersection */
  public static Set intersection(Set s1, Set s2) {
    Set s = new HashSet();
    s.addAll(s1);
    s.retainAll(s2);
    return s;
  }

  /** returns the powerset of a set */
  public static Set powerSet(Set s) {
    if (s.isEmpty()) {
      Set h = new HashSet();
      Set h0 = new HashSet(0);
      h.add(h0);
      return h;
    }

    Iterator i = s.iterator();
    Object elt = i.next();
    s.remove(elt);
    Set pow = powerSet(s);
    Set pow1 = powerSet(s);
    for (Iterator j = pow1.iterator(); j.hasNext();) {
      Set t = new HashSet((Set) j.next());
      t.add(elt);
      pow.add(t);
    }
    s.add(elt);
    return pow;

  }

  public static void main(String[] args) {

    Set h = new HashSet();
    h.add("a");
    h.add("z");
    h.add("c");
    System.out.println(sort(h));

    System.out.println(Arrays.asList(toStringArray(h)));
    for (int i = 0; i < 10; i++)
      System.out
	  .println(" " + i + ") " + Arrays.asList(toStringArray(h, true)));

    Set pow = powerSet(h);
    System.out.println(pow);
  }

} // end

class ObjectPair implements Comparable<Object> {
  
  public Object first, second;

  public ObjectPair(Object first, Object second) {
    this.first = first;
    this.second = second;
  }

  public String toString() {
    return "(" + first + "," + second + ")";
  }

  public boolean equals(Object o) {
    if (o instanceof ObjectPair) {
      ObjectPair p = (ObjectPair) o;
      return (first == null ? p.first == null : first.equals(p.first))
	  && (second == null ? p.second == null : second.equals(p.second));
    } else {
      return false;
    }
  }

  public int hashCode() {
    return (((first == null) ? 0 : first.hashCode()) << 16)
	^ ((second == null) ? 0 : second.hashCode());
  }

  @SuppressWarnings("unchecked")
  public int compareTo(Object o) {
    ObjectPair another = (ObjectPair) o;
    int comp = ((Comparable<Object>) first).compareTo(another.first);
    if (comp != 0) {
      return comp;
    } else {
      return ((Comparable<Object>) second).compareTo(another.second);
    }
  }

}// end