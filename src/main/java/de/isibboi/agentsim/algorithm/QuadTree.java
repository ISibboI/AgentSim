package de.isibboi.agentsim.algorithm;

import java.util.Objects;
import java.util.Random;

import de.isibboi.agentsim.game.map.Point;

/**
 * A quad tree.
 * Always a square of side length power of two.
 * 
 * It should be space efficient and fast. I hope it will be.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *	
 * @param <T> The element type.
 */
// Ideas for optimisation:
//  * Replace recursion with loops. Dirty, but causes less stack operations.
//  * Implement the data structure in C and use JNI. Probably a big bunch of work.
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

		/**
		 * Creates a new mutable point at location (0/0).
		 */
		public MutablePoint() {
			_x = 0;
			_y = 0;
		}

		@Override
		public String toString() {
			return "[" + _x + "/" + _y + "]";
		}

		/**
		 * Returns a {@link Point} representing this {@link MutablePoint}.
		 * @return A {@link Point} representing this {@link MutablePoint}.
		 */
		public Point toPoint() {
			return new Point(_x, _y);
		}
	}

	/**
	 * An entry of a {@link QuadTree}.
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 *
	 * @param <T> The element type.
	 */
	public static final class Entry<T> {
		private final Point _location;
		private final T _element;

		/**
		 * Creates a new entry.
		 * @param location The location.
		 * @param element The element.
		 */
		public Entry(final Point location, final T element) {
			_location = location;
			_element = element;
		}

		/**
		 * Returns the location of the entry.
		 * @return The location of the entry.
		 */
		public Point getLocation() {
			return _location;
		}

		/**
		 * Returns the element of the entry.
		 * @return The element of the entry.
		 */
		public T getElement() {
			return _element;
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
		 * 
		 * @param location The location.
		 * @param element The element.
		 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
		 * @return The element that was at the given position before, or null.
		 */
		T insert(MutablePoint location, T element, int minQuadrantSideLength);

		/**
		 * Removes the element at the given location.
		 * 
		 * @param location The location.
		 * @return The element that was at the given location, or null, if no such element exists.
		 */
		T delete(MutablePoint location);

		/**
		 * Selects the element number n from this tree.
		 * @param n The element number.
		 * @return The element number n.
		 */
		Entry<T> select(int n);

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

		@Override
		public T delete(final MutablePoint location) {
			AbstractNode<T> subNode = getSubNode(location);

			if (subNode == null) {
				return null;
			}

			transformToSubNodeSpace(location);
			T result = subNode.delete(location);

			if (result != null) {
				_size--;
			}

			clearEmptySubNodes();

			return result;
		}

		@Override
		public Entry<T> select(final int n) {
			int index = n;

			if (_upperLeft != null) {
				if (index < _upperLeft.size()) {
					return _upperLeft.select(index);
				} else {
					index -= _upperLeft.size();
				}
			}

			if (_upperRight != null) {
				if (index < _upperRight.size()) {
					return _upperRight.select(index);
				} else {
					index -= _upperRight.size();
				}
			}

			if (_lowerLeft != null) {
				if (index < _lowerLeft.size()) {
					return _lowerLeft.select(index);
				} else {
					index -= _lowerLeft.size();
				}
			}

			if (_lowerRight != null) {
				if (index < _lowerRight.size()) {
					return _lowerRight.select(index);
				} else {
					index -= _lowerRight.size();
				}
			}

			throw new IndexOutOfBoundsException(n + " is out of bounds.");
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

		/**
		 * Gets the sub node that handles the given location.
		 * @param location The location.
		 * @return The sub node that handles the given location.
		 */
		private AbstractNode<T> getSubNode(final MutablePoint location) {
			if (location._y < 0) {
				if (location._x < 0) {
					return _lowerLeft;
				} else {
					return _lowerRight;
				}
			} else {
				if (location._x < 0) {
					return _upperLeft;
				} else {
					return _upperRight;
				}
			}
		}

		/**
		 * Removes sub nodes that are empty.
		 */
		private void clearEmptySubNodes() {
			if (_lowerLeft != null && _lowerLeft.size() == 0) {
				_lowerLeft = null;
			}

			if (_lowerRight != null && _lowerRight.size() == 0) {
				_lowerRight = null;
			}

			if (_upperLeft != null && _upperLeft.size() == 0) {
				_upperLeft = null;
			}

			if (_upperRight != null && _upperRight.size() == 0) {
				_upperRight = null;
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

		@Override
		public T delete(final MutablePoint location) {
			final int index = locationToIndex(location);
			final T before = _elements[index];
			_elements[index] = null;

			if (before != null) {
				_size--;
			}

			return before;
		}

		@Override
		public Entry<T> select(final int n) {
			int index = 0;

			for (T element : _elements) {
				if (element != null) {
					if (index == n) {
						return new Entry<>(indexToLocation(index).toPoint(), element);
					} else {
						index++;
					}
				}
			}

			throw new IndexOutOfBoundsException(n + " is out of bounds.");
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

		/**
		 * Transforms the given array index to a location.
		 * @param index The array index.
		 * @return The location that points to the given array index.
		 */
		private MutablePoint indexToLocation(final int index) {
			final int doubleQuadrantSideLength = _quadrantSideLength * 2;
			final MutablePoint result = new MutablePoint();

			result._x = index % doubleQuadrantSideLength - _quadrantSideLength;
			result._y = index / doubleQuadrantSideLength - _quadrantSideLength;

			return result;
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

	/**
	 * Deletes the element at the given location.
	 * @param location The location.
	 * @return The deleted element, or null, if no such element exists.
	 */
	public T delete(final Point location) {
		final MutablePoint transformedLocation = new MutablePoint(location, _rootLocationTransformation);
		checkLocation(transformedLocation);

		return _root.delete(transformedLocation);
	}

	/**
	 * Selects a random entry from this tree.
	 * Each entry is returned with approximately the same probability.
	 * @return A random entry.
	 */
	public Entry<T> selectRandomElement() {
		return selectRandomElement(new Random());
	}

	/**
	 * Selects a random entry from this tree.
	 * Each entry is returned with approximately the same probability.
	 * 
	 * @param random The source of randomness.
	 * @return A random entry.
	 */
	public Entry<T> selectRandomElement(final Random random) {
		return _root.select(random.nextInt(size()));
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