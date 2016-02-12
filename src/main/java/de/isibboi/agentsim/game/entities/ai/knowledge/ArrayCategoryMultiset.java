package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.AbstractIterator;

/**
 * A multiset of categories this object belongs to.
 * Is backed by a {@link CategoryGroup} object, which contains all possible categories this object may belong to.
 * This implementation uses an array to store the element cardinality.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class ArrayCategoryMultiset extends AbstractSet<Category> implements CategoryMultiset {
	private static final Logger LOG = LogManager.getLogger(ArrayCategoryMultiset.class);

	private final CategoryGroup _categoryGroup;
	private final int[] _count;
	private int _size = 0;

	/**
	 * Creates a new object backed by the given category group.
	 * 
	 * @param categoryGroup The category group that backs this set.
	 */
	public ArrayCategoryMultiset(final CategoryGroup categoryGroup) {
		_categoryGroup = categoryGroup;
		_count = new int[categoryGroup.size()];
	}

	@Override
	public int size() {
		return _size;
	}

	@Override
	public void clear() {
		for (int i = 0; i < _count.length; i++) {
			_count[i] = 0;
		}

		_size = 0;
	}

	@Override
	public int count(final Object element) {
		ensureCategoryGroupContains(element);
		return _count[((Category) element).getId()];
	}

	@Override
	public int add(final Category element, final int occurrences) {
		ensureCategoryGroupContains(element);

		final int index = element.getId();
		final int result = _count[index];
		_count[index] += occurrences;
		_size += occurrences;
		return result;
	}

	@Override
	public int remove(final Object element, final int occurrences) {
		ensureCategoryGroupContains(element);

		final int index = ((Category) element).getId();
		final int result = _count[index];
		_count[index] -= Math.min(occurrences, result);
		_size -= Math.min(occurrences, result);
		return result;
	}

	@Override
	public int setCount(final Category element, final int count) {
		ensureCategoryGroupContains(element);

		final int index = element.getId();
		final int result = _count[index];
		_count[index] = count;
		_size += count - result;
		return result;
	}

	@Override
	public boolean setCount(final Category element, final int oldCount, final int newCount) {
		ensureCategoryGroupContains(element);
		final int index = element.getId();

		if (_count[index] == oldCount) {
			_count[index] = newCount;
			_size += newCount - oldCount;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Set<Category> elementSet() {
		return this;
	}

	@Override
	public Set<com.google.common.collect.Multiset.Entry<Category>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Category> iterator() {
		return new AbstractIterator<Category>() {
			private int _currentIndex = 0;
			private int _currentCount = -1;

			protected Category computeNext() {
				_currentCount++;

				while (_currentIndex < _count.length && _currentCount >= _count[_currentIndex]) {
					_currentCount = 0;
					_currentIndex++;
				}

				if (_currentIndex < _count.length && _currentCount < _count[_currentIndex]) {
					return _categoryGroup.getCategory(_currentIndex);
				} else {
					return endOfData();
				}
			}
		};
	}

	@Override
	public boolean contains(final Object element) {
		ensureCategoryGroupContains(element);

		return _count[((Category) element).getId()] > 0;
	}

	@Override
	public boolean add(final Category element) {
		add(element, 1);
		return true;
	}

	@Override
	public boolean remove(final Object element) {
		return remove(element, 1) != 0;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		boolean modified = false;

		for (Object o : c) {
			modified |= remove(o);
		}

		return modified;
	}

	/**
	 * An optimized version of {@link #removeAll(Collection)} for {@link CategoryMultiset}s.
	 * @param toRemove The elements to remove.
	 * @return True if the collection changed as an result of this operation.
	 */
	public boolean removeAll(final CategoryMultiset toRemove) {
		if (!_categoryGroup.equals(toRemove.getCategoryGroup())) {
			throw new IllegalArgumentException("Given CategoryMultiset is not backed by the same category group!");
		}

		final int oldSize = _size;

		for (int i = 0; i < _count.length; i++) {
			remove(_categoryGroup.getCategory(i), toRemove.count(_categoryGroup.getCategory(i)));
		}

		return oldSize != _size;
	}

	@Override
	public CategoryGroup getCategoryGroup() {
		return _categoryGroup;
	}

	/**
	 * Checks if the given element is in the given category group.
	 * 
	 * @param element The element.
	 * @throws IllegalArgumentException If the given object is not a category.
	 */
	private void ensureCategoryGroupContains(final Object element) {
		if (element instanceof Category) {
			ensureCategoryGroupContains((Category) element);
		} else {
			throw new IllegalArgumentException("Object is not a category!");
		}
	}

	/**
	 * Checks if the given element is in the given category group.
	 * @param element The element.
	 */
	private void ensureCategoryGroupContains(final Category element) {
		Objects.requireNonNull(element);

		if (!element.getCategoryGroup().equals(_categoryGroup)) {
			throw new IllegalArgumentException("Category does not belong to the category group that backs this set!");
		}
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append(getClass().getSimpleName());
		str.append('[');
		boolean appendedComma = false;

		for (int i = 0; i < _count.length; i++) {
			if (_count[i] > 0) {
				str.append(_categoryGroup.getCategory(i).getName());
				str.append(':');
				str.append(_count[i]);
				str.append(", ");
				appendedComma = true;
			}
		}

		if (appendedComma) {
			str.delete(str.length() - 2, str.length());
		}

		str.append(']');
		return str.toString();
	}
}