package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * A task that locks a location on the map.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class LockLocationTask extends InfiniteTask {
	private final GameMap _map;
	private final Point _location;
	private boolean _locked;

	/**
	 * Locks the given location.
	 * 
	 * @param map The game map.
	 * @param location The location to lock.
	 */
	public LockLocationTask(final GameMap map, final Point location) {
		_map = map;
		_location = location;
		_locked = false;
	}

	@Override
	public void update(final Random random) throws GameUpdateException {
		if (_map.tryLockLocation(_location)) {
			_locked = true;
		}
	}

	@Override
	public boolean isFinished() {
		return _locked;
	}

	@Override
	public Movement getMovement() {
		return Movement.NONE;
	}

	@Override
	public void start() {
		// Ignore
	}

	@Override
	public int guessDuration() {
		return 0; // Cannot ensure that location lock is removed at any time.
	}
}