package de.isibboi.agentsim.game.entities.ai.tasks;

import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * A task that locks a location on the map.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class LockLocationTask extends ZeroTimeTask {
	private final GameMap _map;
	private final Point _location;
	private final Entity _entity;
	private boolean _locked;

	/**
	 * Locks the given location.
	 * 
	 * @param map The game map.
	 * @param location The location to lock.
	 * @param entity The entity that locks the location.
	 */
	public LockLocationTask(final GameMap map, final Point location, final Entity entity) {
		_map = map;
		_location = location;
		_entity = entity;
		_locked = false;
	}

	@Override
	public void zeroTimeAction() {
		setSuccessful(_map.tryLockLocation(_location, _entity));
	}

	/**
	 * Returns the game map.
	 * @return The game map.
	 */
	public GameMap getMap() {
		return _map;
	}

	/**
	 * Returns the location to lock.
	 * @return The location to lock.
	 */
	public Point getLocation() {
		return _location;
	}

	/**
	 * Returns the entity that locks the location.
	 * @return The entity that locks the location.
	 */
	public Entity getEntity() {
		return _entity;
	}

	@Override
	public boolean wasSuccessful() {
		return true;
	}
}