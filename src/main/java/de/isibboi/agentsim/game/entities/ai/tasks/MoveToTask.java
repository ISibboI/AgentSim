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

	private final Point _target;
	private final MapEntity _entity;
	private final BlockadeMap _blockadeMap;

	private final Queue<Movement> _movementQueue = new LinkedList<>();

	/**
	 * Creates an new task that moves the given Entity to the given target.
	 * 
	 * @param target The target point on the map.
	 * @param entity The entity to move.
	 * @param blockadeMap The map as seen by the entity.
	 */
	public MoveToTask(final Point target, final MapEntity entity, final BlockadeMap blockadeMap) {
		_target = target;
		_entity = entity;
		_blockadeMap = blockadeMap;
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		_movementQueue.remove();

		if (_movementQueue.isEmpty() && !wasSuccessful()) {
			LOG.trace("Finished " + this + ". Enitity is at " + _entity.getLocation() + ", but should be at " + _target + ".");
		}
	}

	@Override
	public boolean isFinished() {
		return _movementQueue.isEmpty();
	}

	@Override
	public Movement getMovement() {
		return _movementQueue.element();
	}

	@Override
	public int guessDuration() {
		// TODO add guess if task was not started yet.
		return _movementQueue.size();
	}

	@Override
	public void eventFailure() {
		// Ignore.
	}

	@Override
	public void eventInformationUpdated() {
		_movementQueue.clear();
		PathfindingAlgorithm pathfinder = new AStarPathfinder();
		List<Movement> path = pathfinder.findPath(_entity.getLocation(), _target, _blockadeMap);

		if (path != null) {
			_movementQueue.addAll(path);
		} else {
			LOG.trace("Could not find a valid path.");
		}

		if (_movementQueue.isEmpty()) {
			_movementQueue.add(Movement.NONE);
		}
	}

	@Override
	public void start() {
		eventInformationUpdated();
	}

	@Override
	public boolean wasSuccessful() {
		return _entity.getLocation().equals(_target);
	}
}