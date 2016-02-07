package de.isibboi.agentsim.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.SortedSet;

import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.util.Util;

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
public class QuadTree<T extends Categorized & Prioritized & TemporalVariant> {
	/**
	 * An entry of a {@link QuadTree}.
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 *
	 * @param <T> The element type.
	 */
	public static final class Entry<T> {
		private Point.Builder _location;
		private final T _element;

		/**
		 * Creates a new entry.
		 * @param location The location.
		 * @param element The element.
		 */
		public Entry(final Point.Builder location, final T element) {
			_location = location;
			_element = element;
		}

		/**
		 * Returns the location of the entry.
		 * @return The location of the entry.
		 */
		public Point getLocation() {
			return _location.build();
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
	private interface Node<T extends Categorized & Prioritized & TemporalVariant> extends MultiCategorized, Prioritized {
		/**
		 * Returns the node that stores the data for the given location.
		 * If the node doesn't exist, a new one is created.
		 * 
		 * @param location The location of the data.
		 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
		 * @param categoryGroup The category group of the elements.
		 * @return A node.
		 */
		Leaf<T> getOrCreateLeaf(Point.Builder location, int minQuadrantSideLength, CategoryGroup categoryGroup);

		/**
		 * Inserts the given element at the given location.
		 * 
		 * @param location The location.
		 * @param element The element.
		 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
		 * @return The element that was at the given position before, or null.
		 */
		T insert(Point.Builder location, T element, int minQuadrantSideLength);

		/**
		 * Returns the element at the given location
		 * @param location The location.
		 * @return The element at the given location, or null, if no such element exists.
		 */
		T get(Point.Builder location);

		/**
		 * Removes the element at the given location.
		 * 
		 * @param location The location.
		 * @return The element that was at the given location, or null, if no such element exists.
		 */
		T delete(Point.Builder location);

		/**
		 * Selects the element number n from this tree.
		 * @param n The element number.
		 * @return The element number n.
		 */
		Entry<T> select(int n);

		/**
		 * Selects the elements with the given indices from this tree.
		 * 
		 * @param indices The element indices.
		 * @param offset The offset of the indices.
		 * @param result The entries with the given indices. Output parameter.
		 * @return A sublist view of result containing the added elements.
		 */
		List<Entry<T>> select(SortedSet<Integer> indices, int offset, List<Entry<T>> result);

		/**
		 * Returns the amount of elements in this subtree.
		 * @return The size.
		 */
		int size();
	}

	private abstract static class AbstractNode<T extends Categorized & Prioritized & TemporalVariant> implements Node<T> {
		protected final int _quadrantSideLength;
		protected int _size = 0;
		protected final CategoryMultiset _categories;
		protected int _priority = 0;

		/**
		 * Creates a new abstract node.
		 * 
		 * @param quadrantSideLength The side length of one quadrant of the node.
		 * @param categoryGroup The category group of the elements.
		 */
		AbstractNode(final int quadrantSideLength, final CategoryGroup categoryGroup) {
			_quadrantSideLength = quadrantSideLength;
			_categories = new CategoryMultiset(categoryGroup);
		}

		@Override
		public int size() {
			return _size;
		}

		@Override
		public CategoryMultiset getCategoryMultiset() {
			return _categories;
		}

		@Override
		public int getPriority() {
			return _priority;
		}
	}

	/**
	 * A quad tree inner node.
	 * x-axis goes right, y-axis goes up.
	 * 
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	private static final class InnerNode<T extends Categorized & Prioritized & TemporalVariant> extends AbstractNode<T> {
		private AbstractNode<T> _upperLeft;
		private AbstractNode<T> _upperRight;
		private AbstractNode<T> _lowerLeft;
		private AbstractNode<T> _lowerRight;

		/**
		 * Creates a new empty node.
		 * 
		 * @param quadrantSideLength The side length of one quadrant of the node.
		 * @param categoryGroup The category group of the elements.
		 */
		InnerNode(final int quadrantSideLength, final CategoryGroup categoryGroup) {
			super(quadrantSideLength, categoryGroup);
		}

		@Override
		public Leaf<T> getOrCreateLeaf(final Point.Builder location, final int minQuadrantSideLength, final CategoryGroup categoryGroup) {
			AbstractNode<T> subNode = getOrCreateSubNode(location, minQuadrantSideLength, categoryGroup);
			transformToSubNodeSpace(location);
			return subNode.getOrCreateLeaf(location, minQuadrantSideLength, categoryGroup);
		}

		@Override
		public T insert(final Point.Builder location, final T element, final int minQuadrantSideLength) {
			AbstractNode<T> subNode = getOrCreateSubNode(location, minQuadrantSideLength, element.getCategoryMultiset().getCategoryGroup());
			transformToSubNodeSpace(location);
			T result = subNode.insert(location, element, minQuadrantSideLength);

			if (result == null) {
				_size++;
				_categories.addAll(element.getCategoryMultiset());
				_priority += element.getPriority();
			}

			return result;
		}

		@Override
		public T get(final Point.Builder location) {
			AbstractNode<T> subNode = getSubNode(location);

			if (subNode == null) {
				return null;
			}

			transformToSubNodeSpace(location);
			return subNode.get(location);
		}

		@Override
		public T delete(final Point.Builder location) {
			AbstractNode<T> subNode = getSubNode(location);

			if (subNode == null) {
				return null;
			}

			transformToSubNodeSpace(location);
			T deleted = subNode.delete(location);

			if (deleted != null) {
				_size--;
				_categories.removeAll(deleted.getCategoryMultiset());
				_priority -= deleted.getPriority();
			}

			clearEmptySubNodes();

			return deleted;
		}

		@Override
		public Entry<T> select(final int n) {
			int index = n;
			final int subQuadrantSideLength = _quadrantSideLength / 2;

			if (_upperLeft != null) {
				if (index < _upperLeft.size()) {
					Entry<T> result = _upperLeft.select(index);

					result._location.setX(result._location.getX() - subQuadrantSideLength);
					result._location.setY(result._location.getY() + subQuadrantSideLength);

					return result;
				} else {
					index -= _upperLeft.size();
				}
			}

			if (_upperRight != null) {
				if (index < _upperRight.size()) {
					Entry<T> result = _upperRight.select(index);

					result._location.setX(result._location.getX() + subQuadrantSideLength);
					result._location.setY(result._location.getY() + subQuadrantSideLength);

					return result;
				} else {
					index -= _upperRight.size();
				}
			}

			if (_lowerLeft != null) {
				if (index < _lowerLeft.size()) {
					Entry<T> result = _lowerLeft.select(index);

					result._location.setX(result._location.getX() - subQuadrantSideLength);
					result._location.setY(result._location.getY() - subQuadrantSideLength);

					return result;
				} else {
					index -= _lowerLeft.size();
				}
			}

			if (_lowerRight != null) {
				if (index < _lowerRight.size()) {
					Entry<T> result = _lowerRight.select(index);

					result._location.setX(result._location.getX() + subQuadrantSideLength);
					result._location.setY(result._location.getY() - subQuadrantSideLength);

					return result;
				} else {
					index -= _lowerRight.size();
				}
			}

			throw new IndexOutOfBoundsException(n + " is out of bounds.");
		}

		@Override
		public List<Entry<T>> select(final SortedSet<Integer> indices, final int offset, final List<Entry<T>> result) {
			if (indices.isEmpty()) {
				return Collections.emptyList();
			}

			final int subQuadrantSideLength = _quadrantSideLength / 2;
			int modifiedOffset = offset;
			final int firstAddedElement = result.size();

			if (_upperLeft != null) {
				SortedSet<Integer> subSet = indices.subSet(modifiedOffset, modifiedOffset + _upperLeft.size());

				if (subSet.size() > 0) {
					List<Entry<T>> addedElements = _upperLeft.select(subSet, modifiedOffset, result);

					for (Entry<T> entry : addedElements) {
						entry._location.setX(entry._location.getX() - subQuadrantSideLength);
						entry._location.setY(entry._location.getY() + subQuadrantSideLength);
					}
				}

				modifiedOffset += _upperLeft.size();
			}

			if (_upperRight != null) {
				SortedSet<Integer> subSet = indices.subSet(modifiedOffset, modifiedOffset + _upperRight.size());

				if (subSet.size() > 0) {
					List<Entry<T>> addedElements = _upperRight.select(subSet, modifiedOffset, result);

					for (Entry<T> entry : addedElements) {
						entry._location.setX(entry._location.getX() + subQuadrantSideLength);
						entry._location.setY(entry._location.getY() + subQuadrantSideLength);
					}
				}

				modifiedOffset += _upperRight.size();
			}

			if (_lowerLeft != null) {
				SortedSet<Integer> subSet = indices.subSet(modifiedOffset, modifiedOffset + _lowerLeft.size());

				if (subSet.size() > 0) {
					List<Entry<T>> addedElements = _lowerLeft.select(subSet, modifiedOffset, result);

					for (Entry<T> entry : addedElements) {
						entry._location.setX(entry._location.getX() - subQuadrantSideLength);
						entry._location.setY(entry._location.getY() - subQuadrantSideLength);
					}
				}

				modifiedOffset += _lowerLeft.size();
			}

			if (_lowerRight != null) {
				SortedSet<Integer> subSet = indices.subSet(modifiedOffset, modifiedOffset + _lowerRight.size());

				if (subSet.size() > 0) {
					List<Entry<T>> addedElements = _lowerRight.select(subSet, modifiedOffset, result);

					for (Entry<T> entry : addedElements) {
						entry._location.setX(entry._location.getX() + subQuadrantSideLength);
						entry._location.setY(entry._location.getY() - subQuadrantSideLength);
					}
				}

				modifiedOffset += _lowerRight.size();
			}

			if (indices.last() > modifiedOffset) {
				throw new IndexOutOfBoundsException(indices.last() + " is out of bounds.");
			}

			return result.subList(firstAddedElement, result.size());
		}

		/**
		 * Transforms the given mutable point to the space of its respective sub node.
		 * The transformation is made in place, no new objects are created, and the former state of location is lost.
		 * 
		 * @param location The location to transform.
		 */
		private void transformToSubNodeSpace(final Point.Builder location) {
			final int subQuadrantSideLength = _quadrantSideLength / 2;

			if (location.getX() >= 0) {
				location.setX(location.getX() - subQuadrantSideLength);
			} else {
				location.setX(location.getX() + subQuadrantSideLength);
			}

			if (location.getY() >= 0) {
				location.setY(location.getY() - subQuadrantSideLength);
			} else {
				location.setY(location.getY() + subQuadrantSideLength);
			}
		}

		/**
		 * Gets or creates the sub node that handles the given location.
		 * @param location The location.
		 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
		 * @param categoryGroup The category group of the elements.
		 * @return The sub node that handles the given location.
		 */
		private AbstractNode<T> getOrCreateSubNode(final Point.Builder location, final int minQuadrantSideLength, final CategoryGroup categoryGroup) {
			if (location.getY() < 0) {
				if (location.getX() < 0) {
					if (_lowerLeft == null) {
						_lowerLeft = createNode(_quadrantSideLength / 2, minQuadrantSideLength, categoryGroup);
					}

					return _lowerLeft;
				} else {
					if (_lowerRight == null) {
						_lowerRight = createNode(_quadrantSideLength / 2, minQuadrantSideLength, categoryGroup);
					}

					return _lowerRight;
				}
			} else {
				if (location.getX() < 0) {
					if (_upperLeft == null) {
						_upperLeft = createNode(_quadrantSideLength / 2, minQuadrantSideLength, categoryGroup);
					}

					return _upperLeft;
				} else {
					if (_upperRight == null) {
						_upperRight = createNode(_quadrantSideLength / 2, minQuadrantSideLength, categoryGroup);
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
		private AbstractNode<T> getSubNode(final Point.Builder location) {
			if (location.getY() < 0) {
				if (location.getX() < 0) {
					return _lowerLeft;
				} else {
					return _lowerRight;
				}
			} else {
				if (location.getX() < 0) {
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

	@SuppressWarnings("unchecked")
	private static final class Leaf<T extends Categorized & Prioritized & TemporalVariant> extends AbstractNode<T> {
		private final Object[] _elements;

		/**
		 * Creates a new empty node.
		 * 
		 * @param quadrantSideLength The side length of a quadrant of the node.
		 * @param categoryGroup The category group of the elements.
		 */
		Leaf(final int quadrantSideLength, final CategoryGroup categoryGroup) {
			super(quadrantSideLength, categoryGroup);

			final int sideLength = quadrantSideLength * 2;
			_elements = new Object[sideLength * sideLength];
		}

		@Override
		public Leaf<T> getOrCreateLeaf(final Point.Builder location, final int minQuadrantSideLength, final CategoryGroup categoryGroup) {
			return this;
		}

		@Override
		public T insert(final Point.Builder location, final T element, final int minQuadrantSideLength) {
			final int index = locationToIndex(location);
			final T before = (T) _elements[index];
			_elements[index] = element;

			if (before == null) {
				_size++;
				_categories.addAll(element.getCategoryMultiset());
				_priority += element.getPriority();
			}

			return before;
		}

		/**
		 * Returns the element at the given location.
		 * 
		 * @param location The location.
		 * @return The element at the given location.
		 */
		@Override
		public T get(final Point.Builder location) {
			return (T) _elements[locationToIndex(location)];
		}

		@Override
		public T delete(final Point.Builder location) {
			final int index = locationToIndex(location);
			final T deleted = (T) _elements[index];
			_elements[index] = null;

			if (deleted != null) {
				_size--;
				_categories.removeAll(deleted.getCategoryMultiset());
				_priority -= deleted.getPriority();
			}

			return deleted;
		}

		@Override
		public Entry<T> select(final int n) {
			int index = 0;

			for (int i = 0; i < _elements.length; i++) {
				if (_elements[i] != null) {
					if (index == n) {
						return new Entry<>(indexToLocation(i), (T) _elements[i]);
					} else {
						index++;
					}
				}
			}

			throw new IndexOutOfBoundsException(n + " is out of bounds!");
		}

		@Override
		public List<Entry<T>> select(final SortedSet<Integer> indices, final int offset, final List<Entry<T>> result) {
			if (indices.isEmpty()) {
				return Collections.emptyList();
			}

			int currentArrayIndex = 0;
			int currentElementIndex = offset;
			final int firstAddedElement = result.size();

			for (int searchIndex : indices) {
				for (; currentArrayIndex < _elements.length; currentArrayIndex++) {
					if (_elements[currentArrayIndex] != null) {
						if (currentElementIndex == searchIndex) {
							result.add(new Entry<>(indexToLocation(currentArrayIndex), (T) _elements[currentArrayIndex]));
							currentElementIndex++;
							currentArrayIndex++;
							break;
						}

						currentElementIndex++;
					}
				}

				if (currentArrayIndex == _elements.length) {
					throw new IndexOutOfBoundsException(searchIndex + " is out of bounds!");
				}
			}

			return result.subList(firstAddedElement, result.size());
		}

		/**
		 * Transforms the given location to an array index.
		 * @param location The location.
		 * @return The array index that stores the data of the given location.
		 */
		private int locationToIndex(final Point.Builder location) {
			int index = location.getX() + _quadrantSideLength;
			index += (location.getY() + _quadrantSideLength) * 2 * _quadrantSideLength;

			return index;
		}

		/**
		 * Transforms the given array index to a location.
		 * @param index The array index.
		 * @return The location that points to the given array index.
		 */
		private Point.Builder indexToLocation(final int index) {
			final int doubleQuadrantSideLength = _quadrantSideLength * 2;
			final Point.Builder result = new Point.Builder();

			result.setX(index % doubleQuadrantSideLength - _quadrantSideLength);
			result.setY(index / doubleQuadrantSideLength - _quadrantSideLength);

			return result;
		}
	}

	private final int _sideLength;
	private final int _minQuadrantSideLength;
	private final CategoryGroup _categoryGroup;
	private final Node<T> _root;

	/**
	 * The transformation from the input coordinate space to the root nodes coordinate space.
	 */
	private final Point _rootLocationTransformation;

	/**
	 * Creates a new quad tree with the specified width and height.
	 * Width and height describe the area to subdivide.
	 * 
	 * @param sideLength The side length of the tree. Ceiled to power of two.
	 * @param minNodeSize The minimum size of a node. Below, content will be represented as arrays. Ceiled to power of two.
	 * @param rootLocation The location of the tree root.
	 * @param categoryGroup The category group of the elements.
	 */
	public QuadTree(final int sideLength, final int minNodeSize, final Point rootLocation, final CategoryGroup categoryGroup) {
		_sideLength = ceilToPowerOfTwo(sideLength);
		_minQuadrantSideLength = ceilToPowerOfTwo(minNodeSize * 2);
		_categoryGroup = categoryGroup;
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

		final Point.Builder transformedLocation = getTransformedLocation(location);
		checkLocation(transformedLocation);

		return _root.insert(transformedLocation, element, _minQuadrantSideLength);
	}

	/**
	 * Returns the element at the given location.
	 * @param location The location.
	 * @return The element at the given location.
	 */
	public T get(final Point location) {
		final Point.Builder transformedLocation = getTransformedLocation(location);
		checkLocation(transformedLocation);

		return _root.get(transformedLocation);
	}

	/**
	 * Deletes the element at the given location.
	 * @param location The location.
	 * @return The deleted element, or null, if no such element exists.
	 */
	public T delete(final Point location) {
		final Point.Builder transformedLocation = getTransformedLocation(location);
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
		Entry<T> result = _root.select(random.nextInt(size()));
		result._location.sub(_rootLocationTransformation);
		return result;
	}

	/**
	 * Selects a collection of {@code n} distinct random entries from this tree.
	 * Each entry is returned with approximately the same probability.
	 * 
	 * @param n The amount of entries to select.
	 * @return A collection of {@code n} distinct random entries.
	 */
	public Collection<Entry<T>> selectDistinctRandomElements(final int n) {
		return selectDistinctRandomElements(n, new Random());
	}

	/**
	 * Selects a collection of {@code n} distinct random entries from this tree.
	 * Each entry is returned with approximately the same probability.
	 * 
	 * @param n The amount of entries to select.
	 * @param random The source of randomness.
	 * @return A collection of {@code n} distinct random entries.
	 */
	public Collection<Entry<T>> selectDistinctRandomElements(final int n, final Random random) {
		if (n > size()) {
			throw new IllegalArgumentException("Cannot return more values than those that exist.");
		}

		List<Entry<T>> result = new ArrayList<>(n);
		_root.select(Util.getSortedDistinctRandomNumbers(n, size()), 0, result);

		for (Entry<T> entry : result) {
			entry._location.sub(_rootLocationTransformation);
		}

		return result;
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
	protected Leaf<T> getOrCreateLeaf(final Point.Builder location) {
		return _root.getOrCreateLeaf(location, _minQuadrantSideLength, _categoryGroup);
	}

	/**
	 * Creates a new node.
	 * Returns a leaf node, if side length is too small.
	 * 
	 * @param quadrantSideLength The side length of a quadrant of the node.
	 * @return A new node.
	 */
	private AbstractNode<T> createNode(final int quadrantSideLength) {
		return createNode(quadrantSideLength, _minQuadrantSideLength, _categoryGroup);
	}

	/**
	 * Creates a new node.
	 * Returns a leaf node, if side length is too small.
	 * 
	 * @param quadrantSideLength The side length of a quadrant of the node.
	 * @param minQuadrantSideLength The minimum quadrant side length of an inner node.
	 * @param categoryGroup The category group of the elements.
	 * @param <T> The element type.
	 * @return A new node.
	 */
	private static <T extends Categorized & Prioritized & TemporalVariant> AbstractNode<T> createNode(final int quadrantSideLength, final int minQuadrantSideLength,
			final CategoryGroup categoryGroup) {
		if (quadrantSideLength >= minQuadrantSideLength) {
			return new InnerNode<T>(quadrantSideLength, categoryGroup);
		} else {
			return new Leaf<T>(quadrantSideLength, categoryGroup);
		}
	}

	/**
	 * Checks if the given location is in bounds.
	 * @param location The location.
	 * @throws IllegalArgumentException If the location is out of bounds.
	 */
	private void checkLocation(final Point.Builder location) {
		final int sideLengthHalf = _sideLength / 2;

		if (location.getX() < -sideLengthHalf || location.getY() < -sideLengthHalf || location.getX() >= sideLengthHalf || location.getY() >= sideLengthHalf) {
			throw new IllegalArgumentException("Location " + location + " is out of bounds!");
		}
	}

	/**
	 * Transforms the given location to the root nodes coordinate space.
	 * @param location The location.
	 * @return The location transformed to the root nodes coordinate space.
	 */
	private Point.Builder getTransformedLocation(final Point location) {
		Point.Builder result = new Point.Builder(location);
		result.add(_rootLocationTransformation);
		return result;
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