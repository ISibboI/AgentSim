package de.isibboi.agentsim.algorithm;

import java.util.Comparator;

/**
 * Compares {@link PriorityOrdered} objects.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class PriorityComparator implements Comparator<PriorityOrdered> {
	@Override
	public int compare(final PriorityOrdered t1, final PriorityOrdered t2) {
		return t1.getPriority() - t2.getPriority();
	}

}
