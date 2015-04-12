package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * A task that unlocks a location on the map.
 * 
 * @author Sebastian Schmidt
 * @since 
 *
 */
public class UnlockLocationTask extends InfiniteTask {
	private final GameMap _map;
	private final Point _location;

	/**
	 * Locks the given location.
	 * 
	 * @param map The game map.
	 * @param location The location to lock.
	 */
	public UnlockLocationTask(final GameMap map, final Point location) {
		_map = map;
		_location = location;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public Movement getMovement() {
		return Movement.NONE;
	}

	@Override
	public void start() {
		_map.unlockLocation(_location);
	}

	@Override
	public void update(final Random random) throws GameUpdateException {
		// Ignore
	}

	@Override
	public int guessDuration() {
		return 0; // Cannot ensure that location lock is removed at any time.
	}
}