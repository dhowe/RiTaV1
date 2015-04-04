/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.wndata.list;


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import rita.wordnet.jwnl.wndata.PointerTarget;
import rita.wordnet.jwnl.wndata.PointerType;

/** A list of <code>PointerTargetTreeNode</code>s. */
public class PointerTargetTreeNodeList extends PointerTargetNodeList {
	private static final NodePrinter PRINTER =
		new NodePrinter(2) {
			public void print(PrintStream stream, Node node, int indent, int indentIncrement) {
				PointerTargetTreeNode n = (PointerTargetTreeNode)node;
				char c[] = new char[indent >= 0 ? indent : 0];
				Arrays.fill(c, ' ');
				stream.println(new String(c) + n);
				if (n.hasValidChildTreeList()) {
					n.getChildTreeList().print(stream, indent + indentIncrement, indentIncrement);
				}
			}
		};

	public PointerTargetTreeNodeList() {
		this(new LinkedList());
	}

	public PointerTargetTreeNodeList(LinkedList list) {
		super(list, PointerTargetTreeNode.class);
	}

	public void add(PointerTarget target) {
		add(new PointerTargetTreeNode(target));
	}

	public void add(PointerTarget target, PointerType type) {
		add(new PointerTargetTreeNode(target, type));
	}

	public void add(PointerTarget target, PointerType type, PointerTargetTreeNode parent) {
		add(new PointerTargetTreeNode(target, type, parent));
	}

	public void add(PointerTarget target, PointerTargetTreeNodeList childTreeList, PointerType type) {
		add(new PointerTargetTreeNode(target, childTreeList, type));
	}

	public void add(PointerTarget target, PointerTargetTreeNodeList childTreeList,
					PointerType type, PointerTargetTreeNode parent) {
		add(new PointerTargetTreeNode(target, childTreeList, type, parent));
	}

	public void add(PointerTarget target, PointerTargetTreeNodeList childTreeList,
					PointerTargetTreeNodeList pointerTreeList, PointerType type) {
		add(new PointerTargetTreeNode(target, childTreeList, pointerTreeList, type));
	}

	public void add(PointerTarget target, PointerTargetTreeNodeList childTreeList,
					PointerTargetTreeNodeList pointerTreeList, PointerType type, PointerTargetTreeNode parent) {
		add(new PointerTargetTreeNode(target, childTreeList, pointerTreeList, type, parent));
	}

	protected NodePrinter getNodePrinter() {
		return PRINTER;
	}

	/**
	 * Walk the list and all the children of each node in the list and
	 * perform the operation <code>opr</code> on each node. Continues until
	 * either opr returns a non-null value, or it reaches the last node in the list.
	 */
	public Object getFirstMatch(Operation opr) {
		ListIterator itr = listIterator();
		for (Object obj = null; itr.hasNext() && obj == null;) {
			PointerTargetTreeNode node = (PointerTargetTreeNode)itr.next();
			obj = opr.execute(node);
			if (obj != null) {
				return obj;
			} else if (node.hasValidChildTreeList()) {
				return node.getChildTreeList().getFirstMatch(opr);
			}
		}
		return null;
	}

	/**
	 * Walk the list and perform the operation <code>opr</code> on each node.
	 * Searches the list exhaustively and return a List containing all nodes
	 * that are returned by <code>opr</code>.
	 */
	public List getAllMatches(Operation opr) {
		List list = new ArrayList();
		getAllMatches(opr, list);
		return list;
	}

	/** Get all matches and add them to <var>matches</var> */
	public void getAllMatches(Operation opr, List matches) {
		for (ListIterator itr = listIterator(); itr.hasNext();) {
			PointerTargetTreeNode node = (PointerTargetTreeNode)itr.next();
			Object obj = opr.execute(node);
			if (obj != null) {
				matches.add(obj);
			}
			if (node.hasValidChildTreeList()) {
				node.getChildTreeList().getAllMatches(opr, matches);
			}
		}
	}

	/**
	 * Find the first node in the list that is equal to <code>node</code>.
	 * <code>node</code> is considered to match a node in the list
	 * if they contain equal pointer targets and are of the same type.
	 */
	public PointerTargetTreeNode findFirst(PointerTargetTreeNode node) {
		Object obj = getFirstMatch(new FindNodeOperation(node));
		return obj == null ? null : (PointerTargetTreeNode)obj;
	}

	/** Find all occurances of <code>node</code> within the list. */
	public PointerTargetTreeNode[] findAll(PointerTargetTreeNode node) {
		List v = getAllMatches(new FindNodeOperation(node));
		if (v == null) {
			return null;
		} else {
			return (PointerTargetTreeNode[])v.toArray(new PointerTargetTreeNode[v.size()]);
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return new PointerTargetTreeNodeList((LinkedList)copyBackingList());
	}

	public Object deepClone() throws UnsupportedOperationException {
		PointerTargetTreeNodeList list = new PointerTargetTreeNodeList();
		for (Iterator itr = iterator(); itr.hasNext();) {
			list.add(((PointerTargetTreeNode)itr.next()).deepClone());
		}
		return list;
	}

	/** Operation that is performed on the nodes of a tree or list. */
	public interface Operation {
		/** Execute the operation on the given node */
		public Object execute(PointerTargetTreeNode node);
	}

	/** Operation that is used for finding the specified node in a tree. */
	public static class FindNodeOperation implements Operation {
		private PointerTargetTreeNode _node;

		public FindNodeOperation(PointerTargetTreeNode node) {
			_node = node;
		}

		public Object execute(PointerTargetTreeNode testNode) {
			if (_node.equals(testNode)) {
				return testNode;
			}
			return null;
		}
	}

	/** Operation that is used for finding the node(s) in a tree that have the specified <code>PointerTarget</code>. */
	public static class FindTargetOperation implements Operation {
		private PointerTarget _target;

		public FindTargetOperation(PointerTarget target) {
			_target = target;
		}

		public Object execute(PointerTargetTreeNode node) {
			if (node.getPointerTarget().equals(_target)) {
				return node;
			}
			return null;
		}
	}
}