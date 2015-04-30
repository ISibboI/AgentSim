package de.isibboi.agentsim.algorithm;

import de.isibboi.agentsim.game.map.Point;

/**
 * Provides information about the map for a path finder.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public interface PathfindingMap {
	/**
	 * Returns true if the given location can be used for the path.
	 * Returns false if the given location is blocked in any way.
	 * @param location The location.
	 * @return True if the given location is free.
	 */
	boolean isTraversable(Point location);
}