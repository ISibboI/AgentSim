package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Entity;
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
	private final Entity _entity;

	/**
	 * Creates a task that unlocks the location locked by the given {@code LockLocationTask}.
	 * @param lockLocationTask The {@code LockLocationTask}
	 */
	public UnlockLocationTask(final LockLocationTask lockLocationTask) {
		_map = lockLocationTask.getMap();
		_location = lockLocationTask.getLocation();
		_entity = lockLocationTask.getEntity();
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
		_map.unlockLocation(_location, _entity);
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