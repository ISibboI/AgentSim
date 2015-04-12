package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Movement;

/**
 * A task that executes a list of other task one after the other.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class CompositeTask implements Task {
	private final Queue<Task> _taskQueue;
	private final int _duration;

	/**
	 * Creates a new composite task executing the given queue.
	 * @param tasks The tasks to execute.
	 */
	public CompositeTask(final Iterable<Task> tasks) {
		_taskQueue = new LinkedList<>();
		int duration = 0;

		for (Task task : tasks) {
			_taskQueue.add(task);
			duration += task.guessDuration();
		}

		_duration = duration;
	}

	@Override
	public void update(final Random random) throws GameUpdateException {
		_taskQueue.peek().update(random);

		if (_taskQueue.peek().isFinished()) {
			_taskQueue.remove();

			if (!_taskQueue.isEmpty()) {
				_taskQueue.peek().start();
			}
		}
	}

	@Override
	public boolean isFinished() {
		return _taskQueue.isEmpty();
	}

	@Override
	public Movement getMovement() {
		return _taskQueue.peek().getMovement();
	}

	@Override
	public int guessDuration() {
		return _duration;
	}

	@Override
	public void eventFailure() {
		_taskQueue.peek().eventFailure();
	}

	@Override
	public void eventInformationUpdated() {
		_taskQueue.peek().eventInformationUpdated();
	}

	@Override
	public void start() {
		_taskQueue.peek().start();
	}
}