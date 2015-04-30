package de.isibboi.agentsim.algorithm;

import de.isibboi.agentsim.game.map.Point;

/**
 * Provides information about blocked places on a map.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public interface BlockadeMap {
	/**
	 * Returns true if the given location can is blocked.
	 * 
	 * @param location The location.
	 * @return True if the given location is blocked.
	 */
	boolean isBlocked(Point location);
}