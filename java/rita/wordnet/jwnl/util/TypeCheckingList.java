/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.util;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import rita.wordnet.jwnl.JWNLRuntimeException;

/**
 * Wrapper for a list that checks the type of arguments before putting them in the list.
 * It also does type-checking on methods which iterate over the list so that they fail
 * fast if the argument is not of the correct type.
 */
public class TypeCheckingList implements List, DeepCloneable {
	private List _list;
	private Class _type;

	public TypeCheckingList(Class type) {
		this(new ArrayList(), type);
	}

	public TypeCheckingList(List backingList, Class type) {
		init(backingList, type);
	}

	/**
	 * Create a new Type checking list that checks for type <var>type</var>, but only if <var>parentType</var> is
	 * equal to, a super class/interface of, or an interface implemented by <var>type</var>.
	 */
	protected TypeCheckingList(List backingList, Class type, Class parentType) {
		if (!parentType.isAssignableFrom(type)) {
            throw new JWNLRuntimeException("UTILS_EXCEPTION_001", new Object[] { type, parentType });
        }
		init(backingList, type);
	}

	private void init(List backingList, Class type) {
		_type = type;
		if (!backingList.isEmpty()) {
			typecheck(backingList);
        }
		_list = backingList;
	}

	public Class getType() {
		return _type;
	}

	private List getList() {
		return _list;
	}

	public Object clone() throws CloneNotSupportedException {
		return new TypeCheckingList(copyBackingList(), getType());
	}

	/** Make a copy of the wrapped list - used by subclasses when the overriding the clone method */
	protected List copyBackingList() throws CloneNotSupportedException {
		try {
			Method cloneMethod = getList().getClass().getMethod("clone", (Class[])null);
			return (List)cloneMethod.invoke(getList(), (Object[])null);
		} catch (Exception ex) {
			throw new CloneNotSupportedException();
		}
	}

	public Object deepClone() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	// object methods

	public boolean equals(Object obj) {
		return (obj instanceof TypeCheckingList) && super.equals(obj);
	}

	// methods that do type-checking

	public boolean add(Object o) {
		typecheck(o);
		return getList().add(o);
	}

	public void add(int index, Object o) {
		typecheck(o);
		getList().add(index, o);
	}

	public boolean addAll(Collection c) {
		typecheck(c);
		return getList().addAll(c);
	}

	public boolean addAll(int index, Collection c) {
		typecheck(c);
		return getList().addAll(index, c);
	}

	public boolean contains(Object o) {
		try {
			typecheck(o);
			return getList().contains(o);
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean containsAll(Collection c) {
		try {
			typecheck(c);
			return getList().containsAll(c);
		} catch (Exception ex) {
			return false;
		}
	}

	public Object set(int index, Object element) {
		typecheck(element);
		return getList().set(index, element);
	}

	public int indexOf(Object o) {
		try {
			typecheck(o);
			return getList().indexOf(o);
		} catch (Exception ex) {
			return -1;
		}
	}

	public int lastIndexOf(Object o) {
		try {
			typecheck(o);
			return getList().lastIndexOf(o);
		} catch (Exception ex) {
			return -1;
		}
	}

	public boolean remove(Object o) {
		try {
			typecheck(o);
			return getList().remove(o);
		} catch (Exception ex) {
			return false;
		}
	}

	// listIterator methods

	public ListIterator listIterator() {
		return getTypeCheckingListIterator();
	}

	public ListIterator listIterator(int index) {
		return getTypeCheckingListIterator(index);
	}

	protected TypeCheckingListIterator getTypeCheckingListIterator() {
		return getTypeCheckingListIterator(0);
	}

	protected TypeCheckingListIterator getTypeCheckingListIterator(int index) {
		return new TypeCheckingListIterator(getList().listIterator(index));
	}

	// pass-through methods

	public int size() {
		return getList().size();
	}

	public boolean isEmpty() {
		return getList().isEmpty();
	}

	public Iterator iterator() {
		return getList().iterator();
	}

	public Object[] toArray() {
		return getList().toArray();
	}

	// type-checking happens already with this method, so we don't have to explicitly do it
	public Object[] toArray(Object[] a) {
		return getList().toArray(a);
	}

	// type-checking for fail-fast doesn't really improve the performance of this method
	public boolean removeAll(Collection c) {
		return getList().removeAll(c);
	}

	// type-checking for fail-fast doesn't really improve the performance of this method
	public boolean retainAll(Collection c) {
		return getList().retainAll(c);
	}

	public void clear() {
		getList().clear();
	}

	public Object get(int index) {
		return getList().get(index);
	}

	public Object remove(int index) {
		return getList().remove(index);
	}

	public List subList(int fromIndex, int toIndex) {
		return getList().subList(fromIndex, toIndex);
	}

	// type checking methods

	private void typecheck(Object obj) {
		if (!getType().isInstance(obj)) {
			throw new JWNLRuntimeException("UTILS_EXCEPTION_003", getType());
		}
	}

	private Collection _lastCheckedCollection = null;

	private void typecheck(Collection c) {
		if (c != _lastCheckedCollection)
			for (Iterator iterator = c.iterator(); iterator.hasNext();)
				typecheck(iterator.next());
		_lastCheckedCollection = c;
	}

	public final class TypeCheckingListIterator implements ListIterator {
		private ListIterator _itr;

		/** Create a TypeCheckingListIterator from a ListIterator.*/
		private TypeCheckingListIterator(ListIterator itr) {
			_itr = itr;
		}

		public Class getType() {
			return TypeCheckingList.this.getType();
		}

		// Methods that do type-checking.

		public void set(Object o) {
			typecheck(o);
			getListIterator().set(o);
		}

		public void add(Object o) {
			typecheck(o);
			getListIterator().add(o);
		}

		// Pass-through methods

		public boolean hasNext() {
			return getListIterator().hasNext();
		}

		public Object next() {
			return getListIterator().next();
		}

		public boolean hasPrevious() {
			return getListIterator().hasPrevious();
		}

		public Object previous() {
			return getListIterator().previous();
		}

		public int nextIndex() {
			return getListIterator().nextIndex();
		}

		public int previousIndex() {
			return getListIterator().previousIndex();
		}

		public void remove() {
			getListIterator().remove();
		}

		private ListIterator getListIterator() {
			return _itr;
		}
	}
}