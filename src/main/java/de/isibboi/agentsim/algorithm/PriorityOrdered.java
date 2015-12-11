package de.isibboi.agentsim.algorithm;

/**
 * A type that has a priority.
 * The priority is always greater than or equal to zero.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface PriorityOrdered {
	/**
	 * Returns the priority of this object.
	 * @return A value between {@code 0} and {@code Integer.MAX_VALUE}.
	 */
	int getPriority();

	/**
	 * Sets the priority of this object.
	 * @param priority The priority between {@code 0} and {@code Integer.MAX_VALUE}.
	 */
	void setPriority(int priority);
}
