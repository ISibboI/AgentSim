package de.isibboi.agentsim.algorithm;

/**
 * A type that has a priority.
 * The priority is always greater than or equal to zero.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface Prioritized {
	/**
	 * Returns the priority of this object.
	 * @return A value between {@code 0} and {@code Integer.MAX_VALUE}.
	 */
	int getPriority();
}