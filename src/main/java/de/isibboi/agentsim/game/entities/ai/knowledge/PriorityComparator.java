package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.Comparator;

/**
 * Compares {@link Prioritized} objects.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class PriorityComparator implements Comparator<Prioritized> {
	@Override
	public int compare(final Prioritized t1, final Prioritized t2) {
		return t1.getPriority() - t2.getPriority();
	}

}
