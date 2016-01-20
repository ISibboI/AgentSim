package de.isibboi.agentsim.game.entities.ai.intends;

import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.entities.ai.tasks.MoveToTask;
import de.isibboi.agentsim.game.map.Point;

/**
 * Handles priorities and the age of an intend.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class AbstractIntend implements Intend {
	private final int _creationTime;
	private final int _priority;

	/**
	 * Creates a new abstract intend with the given creation tick.
	 * @param tick The current tick.
	 * @param priority The priority of the intend.
	 */
	public AbstractIntend(final int tick, final int priority) {
		_creationTime = tick;
		_priority = priority;
	}

	@Override
	public int getPriority() {
		return _priority;
	}

	@Override
	public int getLastUpdateTime() {
		return _creationTime;
	}

	/**
	 * Returns the path to a point that can be reached from start and that is right next to target.
	 * 
	 * @param start The point to start walking from.
	 * @param target The point to access.
	 * @param goblin The goblin to move.
	 * @return The path to a point that can be reached from start and is right next to target, or null, if no such point exists.
	 */
	protected MoveToTask searchAccessPoint(final Point start, final Point target, final Goblin goblin) {
		for (Point neighbour : target.getNeighbours()) {
			if (goblin.getAI().getBlockadeMap().isBlocked(neighbour)) {
				continue;
			}

			MoveToTask path = new MoveToTask(start, neighbour, goblin, goblin.getAI().getBlockadeMap());

			if (path.hasPath()) {
				return path;
			}
		}

		return null;
	}
}