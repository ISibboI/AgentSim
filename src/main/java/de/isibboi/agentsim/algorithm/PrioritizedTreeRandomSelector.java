package de.isibboi.agentsim.algorithm;

import java.util.Collection;

/**
 * A prioritized random selector that uses a tree index structure for operations in logarithmic time.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type.
 */
public class PrioritizedTreeRandomSelector<T extends PriorityOrdered & Comparable<T>> implements PrioritySelector<T> {
	@Override
	public void add(final T element) {
		// TODO Auto-generated method stub

	}

	@Override
	public T select() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<T> getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePriority(final T element, final int priority) {
		// TODO Auto-generated method stub

	}

	private final class Node {
		private final T _data;
		private int _priority;

		/**
		 * The color of the red-black node. True means black.
		 */
		private boolean _black;

		private Node _leftChild;
		private Node _rightChild;

		/**
		 * Creates a new node.
		 * @param black The color of the node.
		 * @param data The data.
		 */
		public Node(final boolean black, final T data) {
			_black = black;
			_data = data;
			_priority = _data.getPriority();
		}

		public int getPriority() {
			return _priority;
		}

		public boolean isBlack() {
			return _black;
		}

		public Node getLeftChild() {
			return _leftChild;
		}

		public Node getRightChild() {
			return _rightChild;
		}
	}
}