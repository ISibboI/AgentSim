package de.isibboi.agentsim.algorithm;

import java.util.Collection;

/**
 * A set with a specific rule for the order of the elements that are returned.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type.
 */
public interface Selector<T> {
	/**
	 * Adds an element to the selector.
	 * @param element The element.
	 */
	void add(T element);

	/**
	 * Returns an element from the selector, determined by its selection rule.
	 * @return An element previously added to the selector.
	 */
	T select();

	/**
	 * Returns true if the selector is empty.
	 * That is, if a call to {@link #select()} would return null.
	 * @return True if the selector is empty.
	 */
	boolean isEmpty();

	/**
	 * Returns a collection containing the data of the selector.
	 * @return An immutable collection.
	 */
	Collection<T> getData();
}