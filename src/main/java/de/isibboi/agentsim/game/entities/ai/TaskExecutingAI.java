package de.isibboi.agentsim.game.entities.ai;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private static final Logger _log = LogManager.getLogger(TaskExecutingAI.class);

	private final LinkedList<Task> _taskQueue;
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
				finishTask(attributes, random, tick);
			}
		} else {
			_currentTask = _taskQueue.poll();

			if (_currentTask != null) {
				_firedExecutionFinished = false;
				_currentTask.start();

				// If the task finishes immediately.
				if (_currentTask.isFinished()) {
					finishTask(attributes, random, tick);
				}
			} else {
				if (!_firedExecutionFinished) {
					eventExecutionFinished();
					_firedExecutionFinished = true;
				}

				eventExecutingIdleTask();
				_idleTask.update(random, tick);
			}
		}
	}

	/**
	 * Called when a task is finished.
	 * Fires the correct events.
	 * 
	 * @param attributes The current attributes of the controlled entity.
	 * @param random The pseudo random number generator used for randomness.
	 * @param tick The current tick.
	 * @throws GameUpdateException If finishing the task goes wrong. 
	 */
	private void finishTask(final Attributes attributes, final Random random, final int tick) throws GameUpdateException {
		Task lastTask = _currentTask;
		_currentTask = null;

		if (lastTask.wasSuccessful()) {
			if (_taskQueue.isEmpty()) {
				eventTaskFinished(lastTask, 1);
			} else {
				eventTaskFinished(lastTask, _taskQueue.peek().guessDuration());
			}

			update(attributes, random, tick);
		} else {
			_log.trace("Task was not executed successfully: " + lastTask);

			eventTaskFinished(lastTask, 1);
			_idleTask.update(random, tick);
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
	 * Enqueues the given task at the front of the queue.
	 * @param task The task.
	 */
	public void enqueueTaskFront(final Task task) {
		Objects.requireNonNull(task);

		_taskQueue.add(0, task);
		_log.trace("Enqueued task at front." + ((_currentTask == null) ? "" : " The AI is currently executing another task."));
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
	 * @param task The task that was finished.
	 * @param nextTaskDuration The guesstimated duration of the next task.
	 */
	protected abstract void eventTaskFinished(Task task, int nextTaskDuration);

	/**
	 * Fired when all tasks have been executed.
	 */
	protected abstract void eventExecutionFinished();

	/**
	 * Fired when the idle task is executing.
	 * It it fired once per update, until another task is executing.
	 */
	protected abstract void eventExecutingIdleTask();

	/**
	 * Returns a guess of the time that executing all tasks, enclosing the currently executing task, takes.
	 * @return The guessed time to finish all tasks.
	 */
	public int guessDurationToFinishQueue() {
		int duration = 0;

		for (Task task : _taskQueue) {
			duration += task.guessDuration();
		}

		if (_currentTask != null) {
			duration += _currentTask.guessDuration();
		}

		return duration;
	}
}