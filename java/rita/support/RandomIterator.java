package rita.support;

import java.util.*;

import rita.RiTa;

/**
 * A set iterator that supports randomized start positions
 */
public class RandomIterator implements Iterator 
{
    private Set set;
    private Iterator it;
    private int size, start, count;
    
    /** returns the last start position */
    public int getStartPosition() {
      return start;
    }

    /**  returns the # of elements in the backing set */
    public int getSize() {
      return size;
    }
    
    public RandomIterator() {      
      this(new HashSet());
    }
    
    public RandomIterator(Set s) {            
      set = s;      
      size = set.size();
      reset();
    }
    
    public void reset() {
      reset(RiTa.random(size));
    }
    
    public void reset(int startPos) {
      if (startPos>0 && startPos>=size)
        throw new NoSuchElementException(startPos+">="+size);
      count = 0;
      start = startPos;    
      int i = 0;
      it = set.iterator();
      while (++i<startPos)
        it.next();        
    }
    
    public boolean hasNext() {
      return count < size;
    }


    public String next() {
      if (!it.hasNext()) {
        it = set.iterator();
      }
      if (++count>size)
        throw new NoSuchElementException(""+size);
      //return (String)set.iterator().next();
      return (String)it.next();
    }

    public void remove() {
      it.remove();
    }
    
    public static void main(String[] args) {
      Set s = new TreeSet();
      s.add("dog");
      s.add("cat");
      s.add("boy");
      s.add("man");
      Iterator i = new RandomIterator(s);
      while (i.hasNext())
        System.out.println(i.next());
    }
    
}// end