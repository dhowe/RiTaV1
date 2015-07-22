package rita.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**  
 * This is an implementation of a fixed-size (by default) or growable array
 * of Objects that implements the java.util.Set & Collection interfaces.
 * In fixed-size mode, a simple first-in, first-out(FIFO) strategy is used
 * to maintain maximum size. The backing structure is an Object[] for fast,
 * unsychronized access. Care should be taken when using this class in 
 * a multi-threaded application.
 * 
 */
public class HistoryQueue implements Set
{
  private static final boolean DBUG = false;
  
  private static final int DEFAULT_SIZE = 10;
  private static final boolean DEFAULT_GROWABLE = false;
  private static final boolean DEFAULT_ALLOW_DUPS = true;

  private Object[] data;
  private boolean growable;
  private int capacity, size;
  private boolean allowDuplicates;
  private int totalAdds;
  private long modifiedAt;

  public HistoryQueue()
  {    
    this(DEFAULT_SIZE);
  }
  
  public HistoryQueue(int capacity)
  { 
    this(capacity, DEFAULT_GROWABLE);
  }
  
  public HistoryQueue(Collection stringList, boolean growable)
  { 
    this(stringList.size(), growable);
    addAll(stringList);
  }
  
  public HistoryQueue(Object[] stringArr, boolean growable)
  { 
    this(stringArr.length, growable);
    add(stringArr);
  }
  
  public HistoryQueue(int capacity, boolean growable)
  {  
    if (capacity < 0) throw new 
      IllegalArgumentException("Illegal Capacity: "+capacity);
    this.growable = growable;
    this.modifiedAt = System.currentTimeMillis();
    this.allowDuplicates = DEFAULT_ALLOW_DUPS;
    this.data = new Object[capacity];
    if (DBUG) System.out.println("StringSet("+capacity+")");
  }
  
  // methods ----------------------------------------------------
  
  /** @return - a random memeber of the set  */
  public Object randomElement()
  {
    int choice = (int)(Math.random()*size());
    if (data[choice] == null)
      throw new RuntimeException("BROKEN METHOD: StringSet.randomElement()");
    return data[choice];
  }
  

  // OPTIMIZE: no copy necessary
  public void sort()
  {
    Object[] s = asArray();
    Arrays.sort(s);
    data = s;
  }
  
  // OPTIMIZE: no copy necessary
  public void shuffle()
  {
    Object[] s = asArray();
    Collections.shuffle(Arrays.asList(s));
    data = s;
  }
  
  // OPTIMIZE: no copy necessary
  public Object[] asArray()
  {
    Object[] arr = new Object[size];
    System.arraycopy(data,0,arr,0,size);
    return arr;
  }
  
  private boolean isDuplicate(Object s)
  {
    for (int i = 0; i < size; i++){
      if (data[i].equals(s))
        return true;
    }
    return false;
  }        

  /**
   * @param o
   */
  public boolean add(Object o)
  {      
    if (DBUG) System.out.println("Added: "+o);
    
    if (!allowDuplicates) 
      if (isDuplicate(o)) return false;
      
    if (growable) {
      ensureCapacity(size+1);  
    }
    else { // remove the oldest element
      if ((size+1) > data.length)
        remove(0);      
    }
    data[size++] = o;
    totalAdds++;
    if (DBUG) System.out.println("Size="+size);
    timeStamp();
    return true;
  }
  
  private void timeStamp()
  {
    modifiedAt = System.currentTimeMillis();
  }

  /**
   * Increases the capacity of this <tt>ArrayList</tt> instance, if
   * necessary, to ensure  that it can hold at least the number of
   * elements specified by the minimum capacity argument. 
   * @param minCapacity the desired minimum capacity.
   * NOTE: only called in add method
   */
  void ensureCapacity(int minCapacity) 
  {
    int oldCapacity = data.length;
//    System.out.print("minCapacity="+minCapacity+" ");
//    System.out.println("oldCapacity="+oldCapacity);
    if (minCapacity > oldCapacity) {
      Object[] oldData = data;
      int newCapacity = (oldCapacity * 3)/2 + 1;
      if (newCapacity < minCapacity)
        newCapacity = minCapacity;
      data = new Object[newCapacity];
      System.arraycopy(oldData, 0, data, 0, size);
      if (DBUG) System.out.println("Resized to: "+data.length);
    }
  }
  
  /**
   * @param s
   */
  public boolean addAll(Object[] s)
  {
    timeStamp();
    return add(s);
  }
  
  /**
   * Trims the capacity of this StringSet instance to be the
   * current size.  An application can use this operation to 
   * minimize the storage of an StringSet instance.
   */
  public void trimToSize() 
  {
    int oldCapacity = data.length;
    if (size < oldCapacity) {
      Object[] oldData = data;
      data = new Object[size];
      System.arraycopy(oldData, 0, data, 0, size);
    }
    timeStamp();
  }

  /**
   * @param c
   */
  public boolean addAll(Collection c)
  {
    for (Iterator i = c.iterator(); i.hasNext();) 
      this.add(i.next());
    timeStamp();
    return true;
  }

  public void clear()
  {
    for (int i = 0; i < size; i++)
      data[i] = null;    
    size = 0;
    timeStamp();
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof HistoryQueue))
      return false;
    Iterator e1 = iterator();
    Iterator e2 = ((HistoryQueue)o).iterator();
    while(e1.hasNext() && e2.hasNext()) {
      Object o1 = e1.next();
      Object o2 = e2.next();
      if (!(o1==null ? o2==null : o1.equals(o2)))
        return false;
    }
    return !(e1.hasNext() || e2.hasNext());
  }

  /**
   * @param index
   */
  public Object get(int index)
  {
    return data[index];
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    return data.hashCode();
  }

  /**
   * @param s
   */
  public int indexOf(Object s)
  {
    if (s != null && data!=null) {
      for (int i=0; i < data.length; i++) {
        if (data[i] != null) {
          if (data[i].equals(s)) 
            return i;
        }
      }
    }
    return -1;
  }

  public boolean isEmpty()
  {
    return (size == 0);
  }
  
  private class Itr implements Iterator
  {
    /* Index returned by subsequent call to next. */
    int cursor = 0;

    /* Index returned by last call to next or previous. */
    int lastRet = -1;

    public boolean hasNext() {
      return cursor != size();
    }
    public Object next() {
      try {
        Object next = get(cursor);
        lastRet = cursor++;
        return next;
      } catch(IndexOutOfBoundsException e) {
        throw new NoSuchElementException();
      }
    }
    public void remove(){    
      if (lastRet == -1)
        throw new IllegalStateException();
      try {
        HistoryQueue.this.remove(lastRet);
        if (lastRet < cursor)
          cursor--;
        lastRet = -1;
      } catch(IndexOutOfBoundsException e) {
        throw new ConcurrentModificationException();
      }
    }
  }// end Itr

  /**
   * @param s
   */
  public boolean remove(Object s)
  {
    boolean result = false;
    int idx = indexOf(s);
    if (idx > 0) {
      remove(idx);
      result =  true;
    }
    timeStamp();
    return result;
  }

  /** Returns an iterator over the Strings in proper sequence. */
  public Iterator iterator() { return new Itr(); }

    /**
   * Removes the oldest item in the queue and shifts any subsequent 
   * elements appropriately. Equivalent to remove(0); 
   * @return Object removed from the list or null if size == 0 
   */
  public Object removeNewest() 
  {
    timeStamp();
    return (size()>0) ? remove(size()-1) : null;
  }
  
  /**
   * Returns the oldest item in the queue. Equivalent to get(0). 
   * @return Object removed from the list or null if size == 0 
   */
  public Object getNewest() 
  {

    return (size()>0) ? get(size()-1) : null;
  } 
  
  /**
   * Removes the oldest item in the queue and shifts any subsequent 
   * elements appropriately. Equivalent to remove(0); 
   * @return Object removed from the list or null if size == 0 
   */
  public Object removeOldest() 
  {
    return (size()>0) ? remove(0) : null;
  }
  
  /**
   * Returns the oldest item in the queue. Equivalent to get(0). 
   * @return Object removed from the list or null if size == 0 
   */
  public Object getOldest() 
  {
    return (size()>0) ? get(0) : null;
  }
  
  /**
   * Removes the element at the specified index in this list
   * and shifts any subsequent elements to the left 
   * @return Object removed from the list or null if size == 0 
   */
  public Object remove(int index) 
  {
    //System.out.print("Remove("+index+")");
    if (size == 0) return null;
    if (index >= size) throw new
      IndexOutOfBoundsException("index >= "+size);
    Object oldValue = data[index];
    int numMoved = (size - index) - 1;
    //System.out.println("System.arraycopy(strings,"+
    //    (index+1)+",strings,"+index+","+numMoved+")");
    if (numMoved > 0)
      System.arraycopy(data, index+1, data, index, numMoved);
    data[--size] = null; // let gc do its work
    //System.out.println(" -> "+oldValue);
    timeStamp();
    return oldValue;
  }


  /**
   * @param c
   */
  public boolean removeAll(Collection c)
  {
    for (Iterator i = c.iterator(); i.hasNext();) 
      this.remove(i.next());
    timeStamp();
    return true;
  }

  /**
   * @param c
   */
  public boolean retainAll(Collection c)
  {
    for (Iterator i = c.iterator(); i.hasNext();) {
      Object s = i.next();
      int idx = indexOf(s);
      if (idx == -1) remove(s);
    }
    timeStamp();
    return true;
  }

  public int size()
  {
    return size;
  }

  /**
   * @param c
   */
  public boolean containsAll(Collection c)
  {
    for (Iterator i = c.iterator(); i.hasNext();) {
      Object s = i.next();
      if (!contains(s)) return false;
    }
    return true;
  }
  
  public Object[] toArray()
  {
    Object[] result = new Object[size];
    System.arraycopy(data, 0, result, 0, size);
    return result; 
  }
  
  public boolean isFull()
  {
    if (!growable && ((size+1) > data.length))
      return true;
    return false;
  }
  
  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer("[");
    for (Iterator i = iterator(); i.hasNext();) {
      sb.append("'");
      sb.append(i.next());
      sb.append("',");
    }
    sb.append("]");
    return sb.toString();
  }

  public int getCapacity()
  {
    return capacity;
  }

/*  public void setCapacity(int capacity)
  {
    this.capacity = capacity;
  }*/

  public boolean isGrowable()
  {
    return growable;
  }

  public void setGrowable(boolean growable)
  {
    this.growable = growable;
  }  

  public boolean allowDuplicates()
  {
    return allowDuplicates;
  }

  public void setAllowDuplicates(boolean allowDuplicates)
  {
    this.allowDuplicates = allowDuplicates;
  }

  public int getTotalAdds()
  {
    return totalAdds;
  }

  public boolean contains(Object o)
  {
    if (o == null) return false;
    for (int i = 0; i < data.length; i++) {
      Object next = data[i];
      //System.out.println(next);
      if (o.equals(next))
        return true;
    }
    return false;
  }

  public Object[] toArray(Object[] arg0)
  {
    return toArray();
  }
    
  public long lastModifiedAt()
  {
    return modifiedAt;
  }
  
  
/*  private static HistoryQueue setHistorySize(HistoryQueue history, int sz) {
    HistoryQueue nextHistory = new HistoryQueue(sz);
    for (Iterator it = history.iterator(); it.hasNext();)
    {
      nextHistory.add(it.next());
    }
    System.out.println("ORIG: "+history);
    history = nextHistory;
    return history;
  }*/
  
  public static void main(String[]args) {
    List l = new ArrayList();
    for (int i =1;i< 11;i++)
      l.add(new String("a"+i));
    HistoryQueue ss = new HistoryQueue(l, false);
    ss.add(new String("a"+12));
    System.out.println(ss);
    System.out.println("Size: "+ss.size());
/*    setHistorySize(ss, 5);
    System.out.println("NEW: "+ss+"\n");
    setHistorySize(ss, 7);
    System.out.println("NEW: "+ss);*/
  }



}// end



  

