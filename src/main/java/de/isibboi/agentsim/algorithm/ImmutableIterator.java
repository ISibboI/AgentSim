package de.isibboi.agentsim.algorithm;

import java.util.Iterator;

/**
 * An iterator that deactivates the {@link #remove()} method.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type.
 */
public class ImmutableIterator<T> implements Iterator<T> {
	private final Iterator<T> _decorated;

	/**
	 * Creates a new object.
	 * @param iterator The iterator to decorate.
	 */
	public ImmutableIterator(final Iterator<T> iterator) {
		_decorated = iterator;
	}

	@Override
	public boolean hasNext() {
		return _decorated.hasNext();
	}

	@Override
	public T next() {
		return _decorated.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("CategoryMultiset is read-only!");
	}
}
