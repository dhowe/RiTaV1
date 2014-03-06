/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata.list;


import java.io.PrintStream;
import java.util.ListIterator;

import rita.wordnet.jwnl.JWNLRuntimeException;
import rita.wordnet.jwnl.util.TypeCheckingList;

/** A printer for displaying the contents of a node list. */
public abstract class NodePrinter {
	private PrintStream _defaultStream = System.out;
	private int _defaultIndent = 0;

	public NodePrinter() {
	}

	public NodePrinter(int defaultIndent) {
		_defaultIndent = defaultIndent;
	}

	public NodePrinter(PrintStream defaultStream) {
		_defaultStream = defaultStream;
	}

	public NodePrinter(PrintStream defaultStream, int defaultIndent) {
		_defaultStream = defaultStream;
		_defaultIndent = defaultIndent;
	}

	/**
     * Print the contents of the given node, indenting it <var>indent</var> spaces.
     * In each recurisive call to print, <var>indent</var> should be incremented by
     * <var>indentIncrement</var>.
     */
	protected abstract void print(PrintStream stream, Node node, int indent, int indentIncrement);

	/** Print the contents of <var>itr</var> using the default indent */
	public void print(TypeCheckingList.TypeCheckingListIterator itr) {
		print(itr, _defaultStream);
	}

    /** Print the contents of <var>itr</var> to the given stream */
	public void print(TypeCheckingList.TypeCheckingListIterator itr, PrintStream stream) {
		print(itr, stream, _defaultIndent);
	}

    /** Print the contents of <var>itr</var> to the given stream indenting each line <var>indent</var> spaces. */
    public void print(TypeCheckingList.TypeCheckingListIterator itr, PrintStream stream, int indent) {
        print(itr, stream, indent, indent);
    }

	/** Print the contents of <var>itr</var> indenting each line <var>indent</var> spaces. */
	public void print(TypeCheckingList.TypeCheckingListIterator itr, int indent) {
		print(itr, indent, indent);
	}

    /**
     * Print the contents of <var>itr</var> to the default stream. Indent the first line <var>indent</var>
     * spaces. Each level of nesting will be printed intented <var>indentIncrement</var> spaces more than
     * the previous level of nesting.
     */
    public void print(TypeCheckingList.TypeCheckingListIterator itr, int indent, int indentIncrement) {
        print(itr, _defaultStream, indent, indentIncrement);
    }

    /** Print the contents of <var>itr</var> to the given stream. Indent the first line <var>indent</var>
     * spaces. Each level of nesting will be printed intented <var>indentIncrement</var> spaces more than
     * the previous level of nesting.
     */
	public void print(TypeCheckingList.TypeCheckingListIterator itr, PrintStream stream, int indent, int indentIncrement) {
		NodeListIteratorWrapper pItr = new NodeListIteratorWrapper(itr);
		// Find out where we currently are in the iterator
		int curNode = pItr.currentIndex();
		// Move to the first node in the iterator
		pItr.moveToBeginning();
		// print all the nodes
		while (itr.hasNext()) {
			print(stream, pItr.nextNode(), indent, indentIncrement);
		}
		// go back to our original position
		pItr.moveTo(curNode);
	}

	/** Wrapper for a NodeListIterator that allows the next pointer to be moved to any index. */
	private static final class NodeListIteratorWrapper {
		private ListIterator _itr;

		public NodeListIteratorWrapper(TypeCheckingList.TypeCheckingListIterator itr) {
			if (!Node.class.isAssignableFrom(itr.getType())) {
				throw new JWNLRuntimeException("DATA_EXCEPTION_003", new Object[] { Node.class, itr.getType() });
			}
			_itr = itr;
		}

		public Node nextNode() {
			return (Node)_itr.next();
		}

		public Node previousNode() {
			return (Node)_itr.previous();
		}

		public int currentIndex() {
			return _itr.nextIndex() - 1;
		}

		/** Moves the iterator to a point in the iterator where the next index is <code>index</code>. */
		public int moveTo(int index) {
			if (currentIndex() < index) {
				while (currentIndex() < index && _itr.hasNext()) {
					_itr.next();
				}
			} else if (currentIndex() > index) {
				while (currentIndex() > index && _itr.hasPrevious()) {
					_itr.previous();
				}
			}
			return currentIndex();
		}

		/** Move to the initial position in the list (where nextNode() returns the first node in the list */
		public void moveToBeginning() {
			moveTo(-1);
		}

		/** Move to the first postion in the iterator */
		public void moveToFirst() {
			moveTo(0);
		}

		/** Move to the last position in the iterator */
		public void moveToLast() {
			// just give it a really big number since it will stop
			// when it gets to the end of the list
			moveTo(Integer.MAX_VALUE);
		}
	}
}

