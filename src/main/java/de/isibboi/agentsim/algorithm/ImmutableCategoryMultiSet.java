package de.isibboi.agentsim.algorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * An immutable {@link ArrayCategoryMultiset} that throws an {@link UnsupportedOperationException} for every modification attempt.
 * Read queries work as normal.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class ImmutableCategoryMultiSet implements CategoryMultiset {
	private final CategoryMultiset _decorated;

	/**
	 * Creates a new {@link ImmutableCategoryMultiSet} that provides read access to the given {@link CategoryMultiset}.
	 * 
	 * @param categoryMultiset The decorated {@link CategoryMultiset}.
	 */
	public ImmutableCategoryMultiSet(final CategoryMultiset categoryMultiset) {
		_decorated = categoryMultiset;
	}

	@Override
	public int count(final Object element) {
		return _decorated.count(element);
	}

	@Override
	public int add(final Category element, final int occurrences) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public int remove(final Object element, final int occurrences) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public int setCount(final Category element, final int count) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public boolean setCount(final Category element, final int oldCount, final int newCount) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public Set<Category> elementSet() {
		return _decorated.elementSet();
	}

	@Override
	public Set<com.google.common.collect.Multiset.Entry<Category>> entrySet() {
		return _decorated.entrySet();
	}

	@Override
	public Iterator<Category> iterator() {
		return new ImmutableIterator<>(_decorated.iterator());
	}

	@Override
	public boolean contains(final Object element) {
		return _decorated.contains(element);
	}

	@Override
	public boolean containsAll(final Collection<?> elements) {
		return _decorated.containsAll(elements);
	}

	@Override
	public boolean add(final Category element) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public boolean remove(final Object element) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public int size() {
		return _decorated.size();
	}

	@Override
	public boolean isEmpty() {
		return _decorated.isEmpty();
	}

	@Override
	public Object[] toArray() {
		return _decorated.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return _decorated.toArray(a);
	}

	@Override
	public boolean addAll(final Collection<? extends Category> c) {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}

	@Override
	public CategoryGroup getCategoryGroup() {
		return _decorated.getCategoryGroup();
	}
}