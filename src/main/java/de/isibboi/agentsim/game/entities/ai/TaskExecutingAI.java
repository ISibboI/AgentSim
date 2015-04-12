package de.isibboi.agentsim.game.entities.ai;

import java.util.LinkedList;
import java.util.Queue;

import de.isibboi.agentsim.game.entities.Attributes;
import de.isibboi.agentsim.game.map.Point;

/**
 * An AI with a task queue. If the first task is finished, it is disposed and the next task is executed.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class TaskExecutingAI implements AI {
	private Queue<Task> _taskQueue;

	/**
	 * Creates a new object.
	 */
	public TaskExecutingAI() {
		_taskQueue = new LinkedList<>();
	}

	@Override
	public void eventMoveTo(final Point location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(final Attributes attributes) {
		// TODO Auto-generated method stub

	}

	@Override
	public Point getMovement() {
		// TODO Auto-generated method stub
		return null;
	}
}