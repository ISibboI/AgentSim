package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * A multiset of categories this object belongs to.
 * Is backed by a {@link CategoryGroup} object, which contains all possible categories this object may belong to.
 * This implementation uses an array to store the element cardinality.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class ArrayCategoryMultiset extends AbstractSet<Category> implements CategoryMultiset {
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
		if (element instanceof Category) {
			return _count[((Category) element).getId()];
		} else {
			return 0;
		}
	}

	@Override
	public int add(final Category element, final int occurrences) {
		final int index = element.getId();
		final int result = _count[index];
		_count[index] += occurrences;
		return result;
	}

	@Override
	public int remove(final Object element, final int occurrences) {
		final int index = ((Category) element).getId();
		final int result = _count[index];
		_count[index] -= Math.min(occurrences, result);
		return result;
	}

	@Override
	public int setCount(final Category element, final int count) {
		final int index = element.getId();
		final int result = _count[index];
		_count[index] = count;
		return result;
	}

	@Override
	public boolean setCount(final Category element, final int oldCount, final int newCount) {
		final int index = element.getId();

		if (_count[index] == oldCount) {
			_count[index] = newCount;
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
		return new Iterator<Category>() {
			private int _currentIndex;
			private int _currentCount;

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Category next() {
				// TODO Auto-generated method stub
				return null;
			}

			public void searchNext() {
				_currentCount++;
				
				if (_currentCount >= _count[_currentIndex]) {
					_currentCount = 0;
					_currentIndex++;
				}
				do {

					if () {
						_currentCount = 0;
						_currentIndex++;
					} else {
						break;
					}
				} while (_currentIndex < _count.length);
			}
		};
	}

	@Override
	public boolean contains(final Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> elements) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean add(final Category element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(final Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CategoryGroup getCategoryGroup() {
		return _categoryGroup;
	}
}