package de.isibboi.agentsim.algorithm;

import de.isibboi.agentsim.game.map.Point;

/**
 * A blockade map that uses a boolean array for traversability information.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class BooleanBlockadeMap implements BlockadeMap {
	private final boolean[][] _map;

	/**
	 * Creates a new pathfinding map with the given boolean map.
	 * @param map The map.
	 */
	public BooleanBlockadeMap(final boolean[][] map) {
		_map = map;
	}

	@Override
	public boolean isBlocked(final Point location) {
		if (location.getX() >= 0 && location.getY() >= 0 && location.getX() < _map.length) {
			if (location.getY() < _map[location.getX()].length) {
				return _map[location.getX()][location.getY()];
			}
		}

		return true;
	}
}