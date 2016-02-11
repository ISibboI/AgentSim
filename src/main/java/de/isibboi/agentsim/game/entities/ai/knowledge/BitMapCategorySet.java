package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.AbstractSet;
import java.util.BitSet;
import java.util.Iterator;

/**
 * A set of categories this object belongs to.
 * Is backed by a {@link CategoryGroup} object, which contains all possible categories this object may belong to.
 * This implementation uses a bitmap to store the category memberships.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class BitMapCategorySet extends AbstractSet<Category> implements CategorySet {
	private final CategoryGroup _categoryGroup;
	private final BitSet _set;

	/**
	 * Creates a new empty {@link BitMapCategorySet} backed by the given {@link CategoryGroup}.
	 * @param categoryGroup The category group.
	 */
	public BitMapCategorySet(final CategoryGroup categoryGroup) {
		_categoryGroup = categoryGroup;
		_set = new BitSet(categoryGroup.size());
	}

	@Override
	public CategoryGroup getCategoryGroup() {
		return _categoryGroup;
	}

	@Override
	public int size() {
		return _set.cardinality();
	}

	@Override
	public Iterator<Category> iterator() {
		return new Iterator<Category>() {
			private int _current = 0;

			@Override
			public boolean hasNext() {
				if (!_set.get(_current)) {
					searchNext();
				}

				return _current != size();
			}

			@Override
			public Category next() {
				searchNext();

				return _categoryGroup.getCategory(_current);
			}

			private void searchNext() {
				do {
					_current++;
				} while (_current < _set.size() && !_set.get(_current));
			}
		};
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof BitMapCategorySet) {
			BitMapCategorySet b = (BitMapCategorySet) o;

			return _categoryGroup.equals(b._categoryGroup) && _set.equals(b._set);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return _categoryGroup.hashCode() ^ _set.hashCode();
	}

	@Override
	public boolean contains(final Object o) {
		if (o instanceof Category) {
			return _set.get(((Category) o).getId());
		} else {
			return false;
		}
	}

	@Override
	public boolean add(final Category e) {
		final int index = e.getId();
		final boolean result = !_set.get(index);
		_set.set(index);
		return result;
	}

	@Override
	public boolean remove(final Object o) {
		if (o instanceof Category) {
			final int index = ((Category) o).getId();
			final boolean result = _set.get(index);
			_set.clear(index);
			return result;
		} else {
			return false;
		}
	}

	@Override
	public void clear() {
		_set.clear();
	}
}