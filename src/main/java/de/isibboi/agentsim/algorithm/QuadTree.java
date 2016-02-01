package de.isibboi.agentsim.algorithm;

import java.util.Objects;

import de.isibboi.agentsim.game.map.Point;

/**
 * A quad tree.
 * Always a square of side length power of two.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *	
 *@param <T> The element type.
 */
public class QuadTree<T> {
	private static final class MutablePoint {
		//CHECKSTYLE:OFF: VisibilityModifier
		public int _x;
		public int _y;

		//CHECKSTYLE:ON: VisibilityModifier

		/**
		 * Creates a new mutable point with the given location and transforms it with the given transformation.
		 * @param location The location.
		 * @param transformation The transformation.
		 */
		public MutablePoint(final Point location, final Point transformation) {
			_x = location.getX() + transformation.getX();
			_y = location.getY() + transformation.getY();
		}

		@Override
		public String toString() {
			return "[" + _x + "/" + _y + "]";
		}
	}

	/**
	 * A quad tree node.
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	protected interface Node<T> {
		/**
		 * Returns the node that stores the data for the given location.
		 * If the node doesn't exist, a new one is created.
		 * 
		 * @param location The location of the data.
		 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
		 * @return A node.
		 */
		Leaf<T> getOrCreateLeaf(MutablePoint location, int minQuadrantSideLength);

		/**
		 * Inserts the given element at the given location.
		 * @param location The location.
		 * @param element The element.
		 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
		 * @return The element that was at the given position before, or null.
		 */
		T insert(MutablePoint location, T element, int minQuadrantSideLength);

		/**
		 * Returns the amount of elements in this subtree.
		 * @return The size.
		 */
		int size();
	}

	private abstract static class AbstractNode<T> implements Node<T> {
		protected final int _quadrantSideLength;
		protected int _size = 0;

		/**
		 * Creates a new abstract node.
		 * 
		 * @param quadrantSideLength The side length of one quadrant of the node.
		 */
		public AbstractNode(final int quadrantSideLength) {
			_quadrantSideLength = quadrantSideLength;
		}

		@Override
		public int size() {
			return _size;
		}
	}

	/**
	 * A quad tree inner node.
	 * x-axis goes right, y-axis goes up.
	 * 
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	private static final class InnerNode<T> extends AbstractNode<T> {
		private AbstractNode<T> _upperLeft;
		private AbstractNode<T> _upperRight;
		private AbstractNode<T> _lowerLeft;
		private AbstractNode<T> _lowerRight;

		/**
		 * Creates a new empty node.
		 * 
		 * @param quadrantSideLength The side length of one quadrant of the node.
		 */
		public InnerNode(final int quadrantSideLength) {
			super(quadrantSideLength);
		}

		@Override
		public Leaf<T> getOrCreateLeaf(final MutablePoint location, final int minQuadrantSideLength) {
			AbstractNode<T> subNode = getOrCreateSubNode(location, minQuadrantSideLength);
			transformToSubNodeSpace(location);
			return subNode.getOrCreateLeaf(location, minQuadrantSideLength);
		}

		@Override
		public T insert(final MutablePoint location, final T element, final int minQuadrantSideLength) {
			AbstractNode<T> subNode = getOrCreateSubNode(location, minQuadrantSideLength);
			transformToSubNodeSpace(location);
			T result = subNode.insert(location, element, minQuadrantSideLength);

			if (result == null) {
				_size++;
			}

			return result;
		}

		/**
		 * Transforms the given mutable point to the space of its respective sub node.
		 * The transformation is made in place, no new objects are created, and the former state of location is lost.
		 * 
		 * @param location The location to transform.
		 */
		private void transformToSubNodeSpace(final MutablePoint location) {
			final int subQuadrantSideLength = _quadrantSideLength / 2;

			if (location._x >= 0) {
				location._x -= subQuadrantSideLength;
			} else {
				location._x += subQuadrantSideLength;
			}

			if (location._y >= 0) {
				location._y -= subQuadrantSideLength;
			} else {
				location._y += subQuadrantSideLength;
			}
		}

		/**
		 * Gets or creates the sub node that handles the given location.
		 * @param location The location.
		 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
		 * @return The sub node that handles the given location.
		 */
		private AbstractNode<T> getOrCreateSubNode(final MutablePoint location, final int minQuadrantSideLength) {
			if (location._y < 0) {
				if (location._x < 0) {
					if (_lowerLeft == null) {
						_lowerLeft = createNode(_quadrantSideLength / 2, minQuadrantSideLength);
					}

					return _lowerLeft;
				} else {
					if (_lowerRight == null) {
						_lowerRight = createNode(_quadrantSideLength / 2, minQuadrantSideLength);
					}

					return _lowerRight;
				}
			} else {
				if (location._x < 0) {
					if (_upperLeft == null) {
						_upperLeft = createNode(_quadrantSideLength / 2, minQuadrantSideLength);
					}

					return _upperLeft;
				} else {
					if (_upperRight == null) {
						_upperRight = createNode(_quadrantSideLength / 2, minQuadrantSideLength);
					}

					return _upperRight;
				}
			}
		}
	}

	private static final class Leaf<T> extends AbstractNode<T> {
		private final T[] _elements;

		/**
		 * Creates a new empty node.
		 * 
		 * @param quadrantSideLength The side length of a quadrant of the node.
		 */
		@SuppressWarnings("unchecked")
		public Leaf(final int quadrantSideLength) {
			super(quadrantSideLength);

			final int sideLength = quadrantSideLength * 2;
			_elements = (T[]) new Object[sideLength * sideLength];
		}

		@Override
		public Leaf<T> getOrCreateLeaf(final MutablePoint location, final int minQuadrantSideLength) {
			return this;
		}

		@Override
		public T insert(final MutablePoint location, final T element, final int minQuadrantSideLength) {
			final int index = locationToIndex(location);
			final T before = _elements[index];
			_elements[index] = element;

			if (before == null) {
				_size++;
			}

			return before;
		}

		/**
		 * Returns the element at the given location.
		 * @param location The location.
		 * @return The element at the given location.
		 */
		public T get(final MutablePoint location) {
			return _elements[locationToIndex(location)];
		}

		/**
		 * Transforms the given location to an array index.
		 * @param location The location.
		 * @return The array index that stores the data of the given location.
		 */
		private int locationToIndex(final MutablePoint location) {
			int index = location._x + _quadrantSideLength;
			index += (location._y + _quadrantSideLength) * 2 * _quadrantSideLength;

			return index;
		}
	}

	private final int _sideLength;
	private final int _minQuadrantSideLength;
	private final Node<T> _root;

	private final Point _rootLocationTransformation;

	/**
	 * Creates a new quad tree with the specified width and height.
	 * Width and height describe the area to subdivide.
	 * 
	 * @param sideLength The side length of the tree. Ceiled to power of two.
	 * @param minNodeSize The minimum size of a node. Below, content will be represented as arrays. Ceiled to power of two.
	 * @param rootLocation The location of the tree root.
	 */
	public QuadTree(final int sideLength, final int minNodeSize, final Point rootLocation) {
		_sideLength = ceilToPowerOfTwo(sideLength);
		_minQuadrantSideLength = ceilToPowerOfTwo(minNodeSize * 2);
		_root = createNode(_sideLength / 2);
		_rootLocationTransformation = new Point(-rootLocation.getX() - _sideLength / 2, -rootLocation.getY() - _sideLength / 2);
	}

	/**
	 * Inserts the given element at the given location.
	 * @param location The location.
	 * @param element The element.
	 * @return The element that was stored at this location before insertion, or {@code null}, if the location was empty before.
	 */
	public T insert(final Point location, final T element) {
		Objects.requireNonNull(element);

		final MutablePoint transformedLocation = new MutablePoint(location, _rootLocationTransformation);
		checkLocation(transformedLocation);

		return _root.insert(transformedLocation, element, _minQuadrantSideLength);
	}

	/**
	 * Returns the element at the given location.
	 * @param location The location.
	 * @return The element at the given location.
	 */
	public T get(final Point location) {
		final MutablePoint transformedLocation = new MutablePoint(location, _rootLocationTransformation);
		checkLocation(transformedLocation);

		final Leaf<T> leaf = getOrCreateLeaf(transformedLocation);
		return leaf.get(transformedLocation);
	}

	public T delete(final Point location) {
		return null;
	}

	/**
	 * Returns the amount of elements that is stored in the tree.
	 * @return The size of the tree.
	 */
	public int size() {
		return _root.size();
	}

	/**
	 * Returns the node that stores the data for the given location.
	 * If the node doesn't exist, a new one is created.
	 * 
	 * @param location The location of the data.
	 * @return A node.
	 */
	protected Leaf<T> getOrCreateLeaf(final MutablePoint location) {
		return _root.getOrCreateLeaf(location, _minQuadrantSideLength);
	}

	/**
	 * Creates a new node.
	 * Returns a leaf node, if side length is too small.
	 * 
	 * @param quadrantSideLength The side length of a quadrant of the node.
	 * @return A new node.
	 */
	private AbstractNode<T> createNode(final int quadrantSideLength) {
		return createNode(quadrantSideLength, _minQuadrantSideLength);
	}

	/**
	 * Creates a new node.
	 * Returns a leaf node, if side length is too small.
	 * 
	 * @param quadrantSideLength The side length of a quadrant of the node.
	 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
	 * @param <T> The element type.
	 * @return A new node.
	 */
	private static <T> AbstractNode<T> createNode(final int quadrantSideLength, final int minQuadrantSideLength) {
		if (quadrantSideLength >= minQuadrantSideLength) {
			return new InnerNode<T>(quadrantSideLength);
		} else {
			return new Leaf<T>(quadrantSideLength);
		}
	}

	/**
	 * Checks if the given location is in bounds.
	 * @param location The location.
	 * @throws IllegalArgumentException If the location is out of bounds.
	 */
	private void checkLocation(final MutablePoint location) {
		final int sideLengthHalf = _sideLength / 2;

		if (location._x < -sideLengthHalf || location._y < -sideLengthHalf || location._x >= sideLengthHalf || location._y >= sideLengthHalf) {
			throw new IllegalArgumentException("Location " + location + " is out of bounds!");
		}
	}

	/**
	 * Ceils the given number to the next power of two.
	 * 
	 * @param n The number.
	 * @return The ceiled number.
	 */
	private int ceilToPowerOfTwo(final int n) {
		if (n < 1) {
			throw new IllegalArgumentException("n is smaller than one!");
		}

		if (n > 1 << 30) {
			throw new IllegalArgumentException("n is bigger than 2^30!");
		}

		int result = n;
		int power = 0;

		while (result != 1) {
			result >>= 1;
			power++;
		}

		result <<= power;

		if (result == n) {
			return result;
		} else {
			return result << 1;
		}
	}

	/**
	 * Returns the side length of the tree.
	 * @return The side length of the tree.
	 */
	public int getSideLength() {
		return _sideLength;
	}
}