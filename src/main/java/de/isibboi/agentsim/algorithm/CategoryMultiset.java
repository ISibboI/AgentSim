package de.isibboi.agentsim.algorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Multiset;

/**
 * A multiset of categories this object belongs to.
 * Is backed by a {@link CategoryGroup} object, which contains all possible categories this object may belong to.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class CategoryMultiset extends CategorySet implements Multiset<Category> {

	/**
	 * Creates a new object backed by the given category group.
	 * 
	 * @param categoryGroup The category group that backs this set.
	 */
	public CategoryMultiset(final CategoryGroup categoryGroup) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addAll(Collection<? extends Category> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int count(Object element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int add(Category element, int occurrences) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int remove(Object element, int occurrences) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setCount(Category element, int count) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setCount(Category element, int oldCount, int newCount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Category> elementSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<com.google.common.collect.Multiset.Entry<Category>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Category> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> elements) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean add(Category element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
}