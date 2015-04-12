package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

/**
 * Represents the map as known to a specific entity. Two entities can share their map knowledge.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class MapKnowledge {
	/**
	 * Contains the last update times for the fields. If the last update time is zero, the field value is invalid.
	 */
	private final long[][] _lastUpdate;
	private final Material[][] _materials;
	private final GameMap _map;

	/**
	 * Creates a new empty mapKnowledge.
	 * @param width The width of the map.
	 * @param height The height of the map.
	 * @param map The game map.
	 */
	public MapKnowledge(final int width, final int height, final GameMap map) {
		_lastUpdate = new long[width][height];
		_materials = new Material[width][height];
		_map = map;
	}

	/**
	 * Updates the knowledge about the material at the specified location.
	 * @param location The location.
	 */
	public void updateLocation(final Point location) {
		_lastUpdate[location.getX()][location.getY()] = _map.getAge();
		_materials[location.getX()][location.getY()] = _map.getMaterialAt(location);
	}

	/**
	 * Updates a certain pattern of locations starting from the base location.
	 * 
	 * If {@code base} is {@code (2, 4)}, for example, and pattern contains {@code (1, 1)}, then the location {@code (3, 5)} is updated.
	 * 
	 * @param base The location where the pattern should be applied.
	 * @param pattern The pattern to be applied.
	 */
	public void updateLocation(final Point base, final Iterable<Point> pattern) {
		for (Point direction : pattern) {
			updateLocation(base.add(direction));
		}
	}
}
