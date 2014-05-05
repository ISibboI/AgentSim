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
	 * Unlocks the locked location, if it is locked. Must not be called if no location is locked.
	 */
	public void unlockLocation() {
		if (_location != null) {
			_map.unlockLocation(_location);
			_location = null;
		} else {
			throw new IllegalStateException("No location locked!");
		}
	}

	/**
	 * Unlocks the location, if it is locked. May be called if no location is locked.
	 */
	public void unlockLocationIfLocked() {
		if (_location != null) {
			unlockLocation();
		}
	}
}
