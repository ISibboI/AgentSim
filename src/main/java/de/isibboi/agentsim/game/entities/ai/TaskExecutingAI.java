package de.isibboi.agentsim.game.entities.ai;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Attributes;
import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;

/**
 * An AI with a task queue. If the first task is finished, it is disposed and the next task is executed.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class TaskExecutingAI implements AI {
	private final Queue<Task> _taskQueue;
	private Task _currentTask;

	/**
	 * The task that is executed when there is no task to execute.
	 */
	private Task _idleTask;
	private boolean _firedExecutionFinished;

	/**
	 * Creates a new object.
	 */
	public TaskExecutingAI() {
		_taskQueue = new LinkedList<>();
		_firedExecutionFinished = false;
	}

	@Override
	public void update(final Attributes attributes, final Random random, final int tick) throws GameUpdateException {
		if (_currentTask != null) {
			_currentTask.update(random, tick);

			if (_currentTask.isFinished()) {
				_currentTask = null;

				if (!_firedExecutionFinished) {
					eventExecutionFinished();
					_firedExecutionFinished = true;
				}

				update(attributes, random, tick);
			}
		} else {
			_currentTask = _taskQueue.poll();

			if (_currentTask != null) {
				_firedExecutionFinished = false;
				_currentTask.start();
				update(attributes, random, tick);
			} else {
				_idleTask.update(random, tick);
			}
		}
	}

	@Override
	public Movement getMovement() {
		if (_currentTask == null && _idleTask != null) {
			return _idleTask.getMovement();
		}

		if (_currentTask != null) {
			return _currentTask.getMovement();
		} else {
			return null;
		}
	}

	/**
	 * Enqueues the given task.
	 * @param task The task.
	 */
	public void enqueueTask(final Task task) {
		Objects.requireNonNull(task);

		_taskQueue.add(task);
	}

	/**
	 * Enqueues the given tasks.
	 * @param tasks The tasks.
	 */
	public void enqueueTasks(final Iterable<Task> tasks) {
		for (Task task : tasks) {
			enqueueTask(task);
		}
	}

	/**
	 * Returns false if the queue is empty and no task is executed, except the idle task.
	 * @return False if the queue is empty and no task is executed, except the idle task.
	 */
	public boolean isExecuting() {
		return _currentTask != null;
	}

	/**
	 * Sets the task that should be executed when idling.
	 * @param task The idle task.
	 */
	public void setIdleTask(final Task task) {
		_idleTask = task;
	}

	/**
	 * Fired when the execution of a task has been finished.
	 */
	protected abstract void eventTaskFinished();

	/**
	 * Fired when all tasks have been executed.
	 */
	protected abstract void eventExecutionFinished();
}