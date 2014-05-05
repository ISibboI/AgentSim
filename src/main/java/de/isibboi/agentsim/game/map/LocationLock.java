package de.isibboi.agentsim.game.map;

/**
 * Locks a location on the map.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class LocationLock {
	private final GameMap _map;
	private Point _location;
	
	/**
	 * Creates a new {@link LocationLock}.
	 * @param map The game map.
	 */
	public LocationLock(final GameMap map) {
		_map = map;
	}

	/**
	 * Locks the given location on the map if it is not locked.
	 * 
	 * @param location The location.
	 * @return True if the location was successfully locked, false otherwise.
	 */
	public boolean lockLocation(final Point location) {
		if (_map.isLocationLocked(location)) {
			return false;
		}
		
		_location = location;
		_map.lockLocation(location);
		
		return true;
	}
	
	/**
	 * Unlocks a locked location, if it was locked.
	 */
	public void unlockLocation() {
		if (_location != null) {
			_map.unlockLocation(_location);
		} else {
			throw new IllegalStateException("No location locked!");
		}
	}
}
