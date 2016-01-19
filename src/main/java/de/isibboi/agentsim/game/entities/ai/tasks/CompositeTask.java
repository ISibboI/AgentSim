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
public class CompositeTask extends AbstractTask {
	private final Queue<Task> _taskQueue;
	private final int _duration;
	private boolean _wasSuccessful = true;

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
	public void update(final Random random, final int tick) throws GameUpdateException {
		handleZeroTimeTasks();

		_taskQueue.peek().update(random, tick);

		if (_taskQueue.peek().isFinished()) {
			if (!_taskQueue.remove().wasSuccessful()) {
				_taskQueue.clear();
				_wasSuccessful = false;
			}

			if (!_taskQueue.isEmpty()) {
				_taskQueue.peek().zeroTimeAction();
			}

			handleZeroTimeTasks();
		}
	}

	/**
	 * Handles the fast execution of {@code ZeroTimeTask}s.
	 */
	private void handleZeroTimeTasks() {
		if (!_taskQueue.isEmpty()) {
			Task front = _taskQueue.peek();

			if (front.isFinished()) {
				_taskQueue.poll();
				front = _taskQueue.peek();

				if (front != null) {
					front.zeroTimeAction();
					handleZeroTimeTasks();
				}
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
	public boolean wasSuccessful() {
		return _wasSuccessful;
	}

	@Override
	public double getProgress() {
		// TODO Implement!
		return 0;
	}

	@Override
	public void zeroTimeAction() {
		if (!_taskQueue.isEmpty()) {
			_taskQueue.peek().zeroTimeAction();
		}
	}
}