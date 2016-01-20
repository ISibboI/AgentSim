package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.algorithm.AStarPathfinder;
import de.isibboi.agentsim.algorithm.BlockadeMap;
import de.isibboi.agentsim.algorithm.PathfindingAlgorithm;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.MapEntity;
import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.map.Point;

/**
 * A task that makes an entity move to a specific point.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class MoveToTask extends AbstractTask {
	private static final Logger LOG = LogManager.getLogger(MoveToTask.class);

	private final Point _start;
	private final Point _target;
	private final MapEntity _entity;

	private final Queue<Movement> _movementQueue = new LinkedList<>();
	private final double _totalDuration;

	/**
	 * Creates an new task that moves the given Entity to the given target.
	 * 
	 * @param start The starting point on the map.
	 * @param target The target point on the map.
	 * @param entity The entity to move.
	 * @param blockadeMap The map of obstacles as seen by the entity.
	 */
	public MoveToTask(final Point start, final Point target, final MapEntity entity, final BlockadeMap blockadeMap) {
		_start = start;
		_target = target;
		_entity = entity;

		PathfindingAlgorithm pathfinder = new AStarPathfinder();
		List<Movement> path = pathfinder.findPath(_start, _target, blockadeMap);

		if (path != null) {
			_movementQueue.addAll(path);
		} else {
			LOG.trace("Could not find a valid path.");
		}

		_totalDuration = _movementQueue.size();
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		if (isFinished()) {
			throw new IllegalStateException("Cannot update finished task!");
		}

		if (_movementQueue.size() == _totalDuration && !_entity.getLocation().equals(_start)) {
			throw new IllegalStateException("MoveToTask started, but entity is not at starting position!");
		}

		setMovement(_movementQueue.poll());
	}

	@Override
	public boolean isFinished() {
		return _movementQueue.isEmpty();
	}

	@Override
	public int guessDuration() {
		return _movementQueue.size();
	}

	@Override
	public void eventFailure() {
		// Ignore.
	}

	@Override
	public void eventInformationUpdated() {
		// Ignore.
	}

	@Override
	public boolean wasSuccessful() {
		boolean result = _entity.getLocation().equals(_target);

		if (!result) {
			LOG.trace("Finished " + this + ". Enitity is at " + _entity.getLocation() + ", but should be at " + _target + ".");
			throw new IllegalStateException("Movement finished, but entity is at wrong position!");
		}

		return result;
	}

	@Override
	public double getProgress() {
		return _movementQueue.size() / _totalDuration;
	}
}