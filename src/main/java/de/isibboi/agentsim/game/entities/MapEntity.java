package de.isibboi.agentsim.game.entities;

import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * An entity that has a location known by the game map.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public abstract class MapEntity implements Entity {
	private final EntityLocationManager _entityLocationManager;

	/**
	 * Creates a new map entity at the given location.
	 * @param entityLocationManager The entity location manager that manages the location of this entity.
	 */
	public MapEntity(final EntityLocationManager entityLocationManager) {
		_entityLocationManager = entityLocationManager;
	}

	/**
	 * Returns the current location of this entity.
	 * @return The location of this entity.
	 */
	public Point getLocation() {
		return _entityLocationManager.getLocation(this);
	}

	/**
	 * Sets the location of this entity.
	 * @param location The new location.
	 */
	public void setLocation(final Point location) {
		_entityLocationManager.setLocation(this, location);
	}

	/**
	 * Returns the game map this entity belongs to.
	 * @return The game map.
	 */
	public GameMap getMap() {
		return _entityLocationManager.getMap();
	}

	/**
	 * Returns the entity location manager.
	 * @return The entity location manager.
	 */
	public EntityLocationManager getEntityLocationManager() {
		return _entityLocationManager;
	}
}