package de.isibboi.agentsim.algorithm;

/**
 * A selector that selects elements based on their priority.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type.
 */
public interface PrioritySelector<T extends PriorityOrdered> extends Selector<T> {
	/**
	 * Sets the priority of the given element to the given value and updates the data structure.
	 * @param element The element.
	 * @param priority The new priority.
	 */
	void updatePriority(T element, int priority);
}