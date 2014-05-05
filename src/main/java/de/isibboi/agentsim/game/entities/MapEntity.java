package de.isibboi.agentsim.game.entities;

import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * An entity that has a location known by the game map.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public abstract class MapEntity implements Entity {
	private final GameMap _map;
	private Point _location;
	
	/**
	 * Creates a new map entity at the given location.
	 * @param map The game map.
	 * @param location The location.
	 */
	public MapEntity(final GameMap map, final Point location) {
		_map = map;
		setLocation(location);
	}

	/**
	 * Sets the location of this entity. 
	 * @param location The location of this map.
	 */
	protected void setLocation(final Point location) {
		_map.updateLocation(this, _location, location);
		_location = location;
	}
	
	/**
	 * Returns the current location of this entity.
	 * @return The location of this entity.
	 */
	public Point getLocation() {
		return _location;
	}
	
	/**
	 * Returns the game map this entity belongs to.
	 * @return The game map.
	 */
	public GameMap getMap() {
		return _map;
	}
}