package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

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
public class MoveToTask implements Task {
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
		}
	}

	@Override
	public void start() {
		eventInformationUpdated();
	}
}